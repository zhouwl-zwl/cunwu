<template>
  <div class="page-container">
    <van-nav-bar title="罗卜田乡纪检干部花名册" left-arrow @click-left="goBack">
      <template #right>
        <van-icon name="share-o" size="20" color="#fff" @click="handleShare" />
      </template>
    </van-nav-bar>

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ roster.length }}</span>
        <span class="stats-label">纪检干部</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ villageCount }}</span>
        <span class="stats-label">覆盖村落</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ filteredList.length }}</span>
        <span class="stats-label">当前筛选</span>
      </div>
    </div>

    <div class="search-section">
      <van-search 
        v-model="searchKeyword" 
        :placeholder="searchPlaceholder" 
        shape="round"
        @clear="searchKeyword = ''"
      />
      <div class="filter-bar">
        <span class="filter-label">搜索范围：</span>
        <van-radio-group v-model="searchField" direction="horizontal">
          <van-radio name="all">全部</van-radio>
          <van-radio name="name">姓名</van-radio>
          <van-radio name="position">职务</van-radio>
          <van-radio name="phone">电话</van-radio>
        </van-radio-group>
      </div>
    </div>

    <div class="tabs-container">
      <van-tabs v-model="activeTab" sticky>
        <van-tab title="全部" name="all" />
        <van-tab v-for="village in villageTabs" :key="village" :title="village" :name="village" />
      </van-tabs>
    </div>

    <div class="export-bar">
      <div class="export-btn" @click="exportExcel">
        <van-icon name="down" size="16" color="#fff" />
        <span>导出Excel</span>
      </div>
      <span class="export-tip">共 {{ filteredList.length }} 条记录</span>
    </div>

    <div class="roster-list" v-if="filteredList.length > 0">
      <div 
        v-for="(item, index) in filteredList" 
        :key="item.id" 
        class="roster-card"
      >
        <div class="roster-index">{{ index + 1 }}</div>
        <div class="roster-avatar">
          {{ item.name.charAt(0) }}
        </div>
        <div class="roster-info">
          <div class="roster-name">{{ item.name }}</div>
          <div class="roster-position">{{ item.position }}</div>
          <div class="roster-phone" @click="handleCall(item)">
            <van-icon name="phone" size="14" color="#4CAF50" />
            <span>{{ item.phone }}</span>
          </div>
        </div>
        <div class="roster-actions">
          <div class="action-btn call-btn" @click="handleCall(item)">
            <van-icon name="phone" size="18" color="#fff" />
          </div>
          <div class="action-btn share-btn" @click="handleShareItem(item)">
            <van-icon name="share-o" size="18" color="#fff" />
          </div>
        </div>
      </div>
    </div>

    <div class="empty-state" v-else>
      <van-icon name="search" size="48" color="#ddd" />
      <p>未找到匹配的纪检干部</p>
      <van-button type="primary" size="small" @click="resetFilter">重置筛选</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showDialog, showToast } from 'vant'
import * as XLSX from 'xlsx'
import { showSharePanel } from '@/utils/share'

const router = useRouter()
const searchKeyword = ref('')
const searchField = ref('all')
const activeTab = ref('all')

const searchPlaceholder = computed(() => {
  const map = {
    all: '搜索姓名、职务、电话...',
    name: '输入姓名搜索...',
    position: '输入职务搜索...',
    phone: '输入电话号码搜索...'
  }
  return map[searchField.value] || map.all
})

const roster = ref([
  { id: 1, name: '于鼎馨', position: '罗卜田乡纪检委员、纪委书记', phone: '15574589507', village: '罗卜田乡', remark: '' },
  { id: 2, name: '张婉堎', position: '罗卜田乡纪检委员、纪委副书记', phone: '15717457502', village: '罗卜田乡', remark: '' },
  { id: 3, name: '向平安', position: '罗卜田乡纪检委员、纪检专干', phone: '15074561234', village: '罗卜田乡', remark: '' },
  { id: 4, name: '涂金萍', position: '罗卜田乡纪检委员', phone: '18166254567', village: '罗卜田乡', remark: '' },
  { id: 5, name: '龙海发', position: '罗卜田乡纪检委员', phone: '15364447890', village: '罗卜田乡', remark: '' },
  { id: 6, name: '李利华', position: '罗卜田乡罗卜田村纪检主任', phone: '13787541234', village: '罗卜田村', remark: '' },
  { id: 7, name: '周光炳', position: '罗卜田乡罗卜田村纪检委员', phone: '19372384567', village: '罗卜田村', remark: '' },
  { id: 8, name: '龙向军', position: '罗卜田乡罗卜田村纪检委员', phone: '13874487890', village: '罗卜田村', remark: '' },
  { id: 9, name: '张卧芯', position: '罗卜田乡罗卜田村纪检委员', phone: '18374551234', village: '罗卜田村', remark: '' },
  { id: 10, name: '李高东', position: '罗卜田乡罗卜田村纪检委员', phone: '13469364567', village: '罗卜田村', remark: '' },
  { id: 11, name: '李复胜', position: '罗卜田乡马坡村纪检主任', phone: '19974527890', village: '马坡村', remark: '' },
  { id: 12, name: '李泽春', position: '罗卜田乡马坡村纪检委员', phone: '19892961234', village: '马坡村', remark: '' },
  { id: 13, name: '邓长兵', position: '罗卜田乡马坡村纪检委员', phone: '17375554567', village: '马坡村', remark: '' },
  { id: 14, name: '刘宗荣', position: '罗卜田乡冬瓜坡村纪检主任', phone: '18076007890', village: '冬瓜坡村', remark: '' },
  { id: 15, name: '李复君', position: '罗卜田乡冬瓜坡村纪检委员', phone: '17308461234', village: '冬瓜坡村', remark: '' },
  { id: 16, name: '李复银', position: '罗卜田乡冬瓜坡村纪检委员', phone: '15580774567', village: '冬瓜坡村', remark: '' },
  { id: 17, name: '杨群', position: '罗卜田乡枣子山村纪检主任', phone: '17308467890', village: '枣子山村', remark: '' },
  { id: 18, name: '郑复均', position: '罗卜田乡枣子山村纪检委员', phone: '14760731234', village: '枣子山村', remark: '' },
  { id: 19, name: '刘正兵', position: '罗卜田乡枣子山村纪检委员', phone: '18797604567', village: '枣子山村', remark: '' },
  { id: 20, name: '郑秀忠', position: '罗卜田乡枣子山村纪检委员', phone: '18797637890', village: '枣子山村', remark: '' },
  { id: 21, name: '李成贵', position: '罗卜田乡枣子山村纪检委员', phone: '19311531234', village: '枣子山村', remark: '' },
  { id: 22, name: '张小华', position: '罗卜田乡新店村纪检主任', phone: '19918457890', village: '新店村', remark: '' },
  { id: 23, name: '杨志国', position: '罗卜田乡新店村纪检委员', phone: '15111501234', village: '新店村', remark: '' },
  { id: 24, name: '张家洪', position: '罗卜田乡新店村纪检委员', phone: '19918584567', village: '新店村', remark: '' },
  { id: 25, name: '唐昌齐', position: '罗卜田乡半冲村纪检主任', phone: '17774597890', village: '半冲村', remark: '' },
  { id: 26, name: '伍代红', position: '罗卜田乡半冲村纪检委员', phone: '18274561234', village: '半冲村', remark: '' },
  { id: 27, name: '张秀腾', position: '罗卜田乡半冲村纪检委员', phone: '17774604567', village: '半冲村', remark: '' },
  { id: 28, name: '张跃', position: '罗卜田乡兴无村纪检主任', phone: '19918547890', village: '兴无村', remark: '' },
  { id: 29, name: '王成国', position: '罗卜田乡兴无村纪检委员', phone: '13874561234', village: '兴无村', remark: '' },
  { id: 30, name: '郑学松', position: '罗卜田乡兴无村纪检委员', phone: '17769244567', village: '兴无村', remark: '' }
])

const villageTabs = computed(() => {
  const villages = [...new Set(roster.value.map(r => r.village))]
  return villages
})

const villageCount = computed(() => {
  return new Set(roster.value.map(r => r.village)).size
})

const filteredList = computed(() => {
  let result = roster.value

  if (activeTab.value !== 'all') {
    result = result.filter(r => r.village === activeTab.value)
  }

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(item => {
      if (searchField.value === 'name') {
        return item.name.toLowerCase().includes(keyword)
      } else if (searchField.value === 'position') {
        return item.position.toLowerCase().includes(keyword)
      } else if (searchField.value === 'phone') {
        return item.phone.includes(keyword)
      } else {
        return item.name.toLowerCase().includes(keyword) ||
               item.position.toLowerCase().includes(keyword) ||
               item.phone.includes(keyword)
      }
    })
  }

  return result
})

const resetFilter = () => {
  searchKeyword.value = ''
  searchField.value = 'all'
  activeTab.value = 'all'
}

const handleCall = (item) => {
  showDialog({
    title: '拨打电话',
    message: `确定要拨打 ${item.name} 的电话 ${item.phone} 吗？`,
    confirmButtonText: '拨打',
    showCancelButton: true
  }).then(() => {
    window.location.href = `tel:${item.phone}`
  }).catch(() => {})
}

const handleShare = () => {
  showSharePanel({
    title: '罗卜田乡纪检干部花名册',
    description: `共${roster.value.length}位纪检干部，覆盖${villageCount.value}个村落`,
    url: window.location.href
  })
}

const handleShareItem = (item) => {
  showSharePanel({
    title: `${item.name} - 纪检干部`,
    description: `${item.position}\n电话：${item.phone}`,
    url: window.location.href
  })
}

const exportExcel = () => {
  const data = filteredList.value.map((item, index) => ({
    '序号': index + 1,
    '姓名': item.name,
    '职务': item.position,
    '联系电话': item.phone,
    '所属村落': item.village,
    '备注': item.remark || '-'
  }))

  if (data.length === 0) {
    showToast('暂无可导出数据')
    return
  }

  const ws = XLSX.utils.json_to_sheet(data)
  ws['!cols'] = [
    { wch: 8 },
    { wch: 12 },
    { wch: 32 },
    { wch: 16 },
    { wch: 12 },
    { wch: 10 }
  ]
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '纪检干部花名册')
  XLSX.writeFile(wb, '罗卜田乡纪检干部花名册.xlsx')
  showToast('导出成功')
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  document.title = '罗卜田乡纪检干部花名册'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.stats-card {
  display: flex;
  background: var(--gradient-primary);
  padding: 16px;
  color: #fff;
  margin-bottom: 12px;
}

.stats-item {
  flex: 1;
  text-align: center;
}

.stats-num {
  display: block;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 12px;
  opacity: 0.9;
}

.search-section {
  background: #fff;
  padding: 8px 12px;
  margin-bottom: 10px;
}

.filter-bar {
  display: flex;
  align-items: center;
  margin-top: 8px;
  padding: 8px 0;
  font-size: 13px;
}

.filter-label {
  color: #666;
  margin-right: 12px;
  font-weight: 500;
}

.filter-bar .van-radio-group {
  flex: 1;
}

.filter-bar .van-radio {
  font-size: 13px;
}

.filter-bar .van-radio__label {
  font-size: 13px;
}

.tabs-container {
  background: #fff;
  margin-bottom: 10px;
}

.export-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 12px 12px;
}

.export-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--gradient-primary);
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.3);
  font-size: 13px;
  color: #fff;
  transition: transform 0.2s;
}

.export-btn:active {
  transform: scale(0.95);
}

.export-tip {
  font-size: 12px;
  color: #999;
}

.roster-list {
  padding: 0 12px;
}

.roster-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 12px;
  padding: 12px 14px;
  margin-bottom: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  position: relative;
}

.roster-index {
  position: absolute;
  top: 8px;
  right: 12px;
  font-size: 11px;
  color: #ccc;
}

.roster-avatar {
  width: 46px;
  height: 46px;
  border-radius: 50%;
  background: var(--gradient-primary);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  margin-right: 12px;
  flex-shrink: 0;
}

.roster-info {
  flex: 1;
  min-width: 0;
}

.roster-name {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.roster-position {
  font-size: 12px;
  color: #666;
  margin-bottom: 6px;
  line-height: 1.4;
}

.roster-phone {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #4CAF50;
  font-weight: 500;
}

.roster-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.action-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.action-btn:active {
  transform: scale(0.92);
}

.call-btn {
  background: var(--gradient-success);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
}

.share-btn {
  background: var(--gradient-info);
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.empty-state p {
  color: #999;
  margin: 12px 0 20px;
  font-size: 14px;
}
</style>
