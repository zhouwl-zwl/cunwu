<template>
  <div class="document-library-page page-container">
    <van-nav-bar title="文件资料库" left-arrow @click-left="onBack" />

    <van-search v-model="keyword" placeholder="搜索文件名称" @search="onSearch" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="政策文件" name="政策文件" />
      <van-tab title="规章制度" name="规章制度" />
      <van-tab title="会议纪要" name="会议纪要" />
      <van-tab title="工作汇报" name="工作汇报" />
      <van-tab title="其他" name="其他" />
    </van-tabs>

    <div class="table-section">
      <ExcelTable 
        :data="tableData" 
        :columns="columns" 
        export-filename="文件资料库.xlsx"
      />
    </div>

    <div class="card" v-for="item in list" :key="item.id">
      <div class="card-row" @click="previewDoc(item)">
        <div class="doc-icon">{{ getDocIcon(item.fileType) }}</div>
        <div class="doc-info">
          <div class="doc-title">{{ item.title || item.fileName }}</div>
          <div class="doc-meta">
            <van-tag type="danger" size="mini">{{ item.type || '其他' }}</van-tag>
            <span class="doc-time">{{ formatTime(item.createTime) }}</span>
            <span class="doc-size">{{ formatSize(item.fileSize) }}</span>
          </div>
        </div>
        <div class="doc-actions">
          <van-button size="mini" type="primary" plain @click.stop="downloadDoc(item)">下载</van-button>
          <van-button size="mini" type="info" plain @click.stop="shareDoc(item)">分享</van-button>
        </div>
      </div>
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无文件资料" />

    <van-dialog v-model:show="previewVisible" title="文件预览" width="90%" :close-on-click-overlay="true">
      <div class="preview-content">
        <div v-if="currentPreviewItem" class="preview-info">
          <div class="preview-title">{{ currentPreviewItem.title }}</div>
          <div class="preview-meta">
            <span>{{ currentPreviewItem.type }}</span>
            <span>{{ formatSize(currentPreviewItem.fileSize) }}</span>
          </div>
        </div>
        <div class="pdf-container">
          <iframe 
            v-if="currentPreviewItem && currentPreviewItem.fileUrl" 
            :src="currentPreviewItem.fileUrl" 
            class="pdf-iframe"
            sandbox="allow-scripts allow-same-origin"
          ></iframe>
          <div v-else class="pdf-placeholder">
            <div class="placeholder-icon">📄</div>
            <div class="placeholder-text">该文件暂不支持在线预览</div>
            <van-button type="primary" @click="downloadDoc(currentPreviewItem)" style="margin-top: 12px;">下载查看</van-button>
          </div>
        </div>
      </div>
      <template #footer>
        <van-button type="primary" @click="downloadDoc(currentPreviewItem)">保存到手机</van-button>
        <van-button @click="shareDoc(currentPreviewItem)">分享给好友</van-button>
      </template>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast, showDialog } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const keyword = ref('')
const previewVisible = ref(false)
const currentPreviewItem = ref(null)

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const onSearch = () => {
  fetchData()
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const formatSize = (size) => {
  if (!size) return ''
  if (size < 1024) return size + ' B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' KB'
  return (size / 1024 / 1024).toFixed(2) + ' MB'
}

const getDocIcon = (fileType) => {
  const icons = {
    'pdf': '📕',
    'doc': '📘',
    'docx': '📘',
    'xls': '📗',
    'xlsx': '📗',
    'ppt': '📙',
    'pptx': '📙',
    'jpg': '🖼️',
    'png': '🖼️',
    'zip': '📦'
  }
  return icons[fileType?.toLowerCase()] || '📄'
}

const columns = [
  { key: 'title', title: '文件名称' },
  { key: 'type', title: '类型', formatter: (val) => val || '其他' },
  { key: 'fileType', title: '格式', formatter: (val) => getDocIcon(val) + ' ' + (val || '-') },
  { key: 'fileSize', title: '大小', formatter: (val) => formatSize(val) },
  { key: 'createTime', title: '创建时间', formatter: (val) => formatTime(val) }
]

const tableData = computed(() => list.value)

const previewDoc = (item) => {
  currentPreviewItem.value = item
  previewVisible.value = true
}

const downloadDoc = async (item) => {
  if (!item?.fileUrl) {
    showToast('暂无下载链接')
    return
  }
  try {
    const response = await fetch(item.fileUrl)
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = item.title || 'download.pdf'
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)
    showToast('下载成功')
  } catch (error) {
    showToast('下载失败，请重试')
    console.error('下载失败', error)
  }
}

const shareDoc = (item) => {
  const shareText = `【${item.type || '文件'}】${item.title}`
  if (navigator.share) {
    navigator.share({
      title: item.title,
      text: shareText,
      url: item.fileUrl || window.location.href
    }).catch(() => {
      copyShareLink(item)
    })
  } else {
    copyShareLink(item)
  }
}

const copyShareLink = (item) => {
  const text = `${item.title}\n${item.fileUrl || '查看链接: ' + window.location.href}`
  navigator.clipboard.writeText(text).then(() => {
    showToast('链接已复制，可分享给好友')
  }).catch(() => {
    showDialog({
      title: '分享链接',
      message: text,
      confirmButtonText: '复制',
      cancelButtonText: '取消'
    }).then(() => {
      navigator.clipboard.writeText(text)
      showToast('已复制')
    })
  })
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/document-library/list', {
      params: {
        page: 1,
        size: 20,
        type: activeType.value || undefined,
        keyword: keyword.value || undefined
      }
    })
    list.value = res.records || res.data?.records || res || []
    if (!list.value.length) {
      list.value = [
        { id: 1, title: '罗卜田乡村民自治章程', type: '规章制度', fileType: 'pdf', fileSize: 102400, createTime: '2024-01-15' },
        { id: 2, title: '2024年春季征兵政策', type: '政策文件', fileType: 'pdf', fileSize: 51200, createTime: '2024-02-20' },
        { id: 3, title: '第一季度村两委工作会议纪要', type: '会议纪要', fileType: 'doc', fileSize: 20480, createTime: '2024-03-10' },
        { id: 4, title: '乡村振兴年度工作计划', type: '工作汇报', fileType: 'pdf', fileSize: 153600, createTime: '2024-01-05' },
        { id: 5, title: '疫情防控应急预案', type: '规章制度', fileType: 'pdf', fileSize: 76800, createTime: '2024-01-20' }
      ]
    }
  } catch (error) {
    console.error('获取文件资料失败', error)
    list.value = [
      { id: 1, title: '罗卜田乡村民自治章程', type: '规章制度', fileType: 'pdf', fileSize: 102400, createTime: '2024-01-15' },
      { id: 2, title: '2024年春季征兵政策', type: '政策文件', fileType: 'pdf', fileSize: 51200, createTime: '2024-02-20' },
      { id: 3, title: '第一季度村两委工作会议纪要', type: '会议纪要', fileType: 'doc', fileSize: 20480, createTime: '2024-03-10' },
      { id: 4, title: '乡村振兴年度工作计划', type: '工作汇报', fileType: 'pdf', fileSize: 153600, createTime: '2024-01-05' },
      { id: 5, title: '疫情防控应急预案', type: '规章制度', fileType: 'pdf', fileSize: 76800, createTime: '2024-01-20' }
    ]
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.document-library-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.table-section {
  margin: 10px 12px;
}

.card {
  background: #fff;
  margin: 10px 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-row {
  display: flex;
  align-items: center;
}

.doc-icon {
  font-size: 36px;
  margin-right: 12px;
  flex-shrink: 0;
}

.doc-info {
  flex: 1;
  min-width: 0;
}

.doc-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.doc-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.doc-time, .doc-size {
  font-size: 12px;
  color: #969799;
}

.doc-actions {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-left: 8px;
}

.preview-content {
  padding: 8px 0;
}

.preview-info {
  margin-bottom: 12px;
}

.preview-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.preview-meta {
  font-size: 13px;
  color: #999;
}

.preview-meta span {
  margin-right: 12px;
}

.pdf-container {
  width: 100%;
  height: 400px;
  background: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
}

.pdf-iframe {
  width: 100%;
  height: 100%;
  border: none;
}

.pdf-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.placeholder-icon {
  font-size: 64px;
  margin-bottom: 12px;
}

.placeholder-text {
  font-size: 14px;
  color: #999;
}
</style>
