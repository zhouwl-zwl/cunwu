<template>
  <div class="page-container">
    <van-nav-bar title="罗卜田乡农家书屋管理员名单" left-arrow @click-left="goBack">
      <template #right>
        <van-icon name="share-o" size="20" color="#fff" @click="handleShare" />
      </template>
    </van-nav-bar>

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ roster.length }}</span>
        <span class="stats-label">管理员数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ villageCount }}</span>
        <span class="stats-label">覆盖村落</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ totalBooks }}</span>
        <span class="stats-label">藏书总量</span>
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
          <van-radio name="village">村名</van-radio>
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
        <div class="roster-avatar library-avatar">
          {{ item.name.charAt(0) }}
        </div>
        <div class="roster-info">
          <div class="roster-name">{{ item.name }}</div>
          <div class="roster-position">{{ item.village }} · {{ item.position }}</div>
          <div class="roster-meta">
            <span class="meta-tag book-tag">藏书 {{ item.bookCount }} 册</span>
            <span class="meta-tag year-tag">管理 {{ item.manageSince }} 年</span>
          </div>
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
      <p>未找到匹配的管理员</p>
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

const villages = ['罗卜田村', '新店村', '马坡村', '冬瓜坡村', '枣子山村', '半冲村', '兴无村']

const searchPlaceholder = computed(() => {
  const map = {
    all: '搜索姓名、村名、电话...',
    name: '输入姓名搜索...',
    village: '输入村名搜索...',
    phone: '输入电话号码搜索...'
  }
  return map[searchField.value] || map.all
})

const roster = ref([
  { id: 1, name: '李桂英', village: '罗卜田村', position: '农家书屋管理员', phone: '13874520101', bookCount: 3280, manageSince: 8 },
  { id: 2, name: '王建国', village: '新店村', position: '农家书屋管理员', phone: '13974520102', bookCount: 2856, manageSince: 6 },
  { id: 3, name: '张淑芬', village: '马坡村', position: '农家书屋管理员', phone: '13774520103', bookCount: 2640, manageSince: 5 },
  { id: 4, name: '杨振山', village: '冬瓜坡村', position: '农家书屋管理员', phone: '13674520104', bookCount: 2180, manageSince: 4 },
  { id: 5, name: '周玉兰', village: '枣子山村', position: '农家书屋管理员', phone: '13574520105', bookCount: 2430, manageSince: 7 },
  { id: 6, name: '刘承祖', village: '半冲村', position: '农家书屋管理员', phone: '15874520106', bookCount: 1960, manageSince: 3 },
  { id: 7, name: '陈慧敏', village: '兴无村', position: '农家书屋管理员', phone: '15974520107', bookCount: 2215, manageSince: 5 }
])

const villageTabs = computed(() => {
  return [...new Set(roster.value.map(r => r.village))]
})

const villageCount = computed(() => {
  return new Set(roster.value.map(r => r.village)).size
})

const totalBooks = computed(() => {
  return roster.value.reduce((sum, item) => sum + item.bookCount, 0)
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
      } else if (searchField.value === 'village') {
        return item.village.toLowerCase().includes(keyword)
      } else if (searchField.value === 'phone') {
        return item.phone.includes(keyword)
      } else {
        return item.name.toLowerCase().includes(keyword) ||
               item.village.toLowerCase().includes(keyword) ||
               item.phone.includes(keyword) ||
               item.position.toLowerCase().includes(keyword)
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
    title: '罗卜田乡农家书屋管理员名单',
    description: `共${roster.value.length}位管理员，覆盖${villageCount.value}个村落，藏书${totalBooks.value}册`,
    url: window.location.href
  })
}

const handleShareItem = (item) => {
  showSharePanel({
    title: `${item.name} - 农家书屋管理员`,
    description: `${item.village}\n藏书量：${item.bookCount}册\n管理年限：${item.manageSince}年\n电话：${item.phone}`,
    url: window.location.href
  })
}

const exportExcel = () => {
  const data = filteredList.value.map((item, index) => ({
    '序号': index + 1,
    '姓名': item.name,
    '所属村': item.village,
    '职务': item.position,
    '联系电话': item.phone,
    '藏书量': item.bookCount,
    '管理年限': item.manageSince
  }))

  if (data.length === 0) {
    showToast('暂无可导出数据')
    return
  }

  const ws = XLSX.utils.json_to_sheet(data)
  ws['!cols'] = [
    { wch: 8 },
    { wch: 12 },
    { wch: 12 },
    { wch: 20 },
    { wch: 16 },
    { wch: 10 },
    { wch: 10 }
  ]
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '农家书屋管理员')
  XLSX.writeFile(wb, '罗卜田乡农家书屋管理员名单.xlsx')
  showToast('导出成功')
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  document.title = '罗卜田乡农家书屋管理员名单'
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

.library-avatar {
  background: linear-gradient(135deg, #8B4513, #D2691E);
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

.roster-meta {
  display: flex;
  gap: 6px;
  margin-bottom: 6px;
  flex-wrap: wrap;
}

.meta-tag {
  display: inline-block;
  padding: 2px 8px;
  background: #f0f4ff;
  color: #5b7bd5;
  border-radius: 4px;
  font-size: 11px;
}

.book-tag {
  background: #fff8e1;
  color: #f57c00;
}

.year-tag {
  background: #e8f5e9;
  color: #2e7d32;
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
