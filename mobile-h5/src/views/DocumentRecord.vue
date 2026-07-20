
<template>
  <div class="document-record-page page-container">
    <van-nav-bar title="文件收发" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="上级来文" name="上级来文" />
      <van-tab title="本村上报" name="本村上报" />
      <van-tab title="村内发文" name="村内发文" />
    </van-tabs>

    <div class="action-bar">
      <van-button type="primary" icon="plus" @click="handleAdd">新增文件</van-button>
    </div>

    <div class="card" v-for="item in list" :key="item.id" @click="handleItemClick(item)">
      <div class="card-header">
        <van-tag :type="getDocTagType(item.type)" size="medium">{{ item.type || '上级来文' }}</van-tag>
        <van-tag v-if="item.priority" :type="item.priority === '紧急' ? 'danger' : 'warning'" plain size="medium">
          {{ item.priority }}
        </van-tag>
      </div>
      <div class="card-title">{{ item.title }}</div>
      <div class="card-content">{{ item.content || item.summary || '暂无内容描述' }}</div>
      <div class="info-row">
        <span class="label">文号</span>
        <span class="value">{{ item.docNo || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">发文单位</span>
        <span class="value">{{ item.fromUnit || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">收发时间</span>
        <span class="value">{{ formatTime(item.docDate || item.createTime) }}</span>
      </div>
      <div class="info-row" v-if="item.attachment">
        <span class="label">附件</span>
        <span class="value link" @click="previewAttachment(item)">查看附件</span>
      </div>
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无文件记录" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { documentRecordData } from '../data/mockData'
import { goBack } from '../utils/index'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getDocTagType = (type) => {
  if (type === '上级来文') return 'danger'
  if (type === '本村上报') return 'primary'
  if (type === '村内发文') return 'warning'
  return 'default'
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const previewAttachment = (item) => {
  showToast(`附件：${item.attachmentName || item.attachment || '文件'}`)
}

const handleItemClick = (item) => {
  showToast(`查看：${item.title}`)
}

const handleAdd = () => {
  showToast('功能开发中，敬请期待')
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/document-record/list', {
      params: { page: 1, size: 20, type: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : documentRecordData.records
  } catch (error) {
    list.value = documentRecordData.records
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.document-record-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.card {
  background: #fff;
  margin: 10px 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.card-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 4px 0;
}

.label {
  color: #969799;
  font-size: 13px;
}

.value {
  color: #333;
  font-size: 13px;
  font-weight: 500;
}

.link {
  color: #D22630;
}
</style>
