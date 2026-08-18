<template>
  <div class="page-container">
    <van-nav-bar title="罗卜田乡团员花名册" left-arrow @click-left="goBack">
      <template #right>
        <van-icon name="share-o" size="20" color="#fff" @click="handleShare" />
      </template>
    </van-nav-bar>

    <div class="stats-card league-stats">
      <div class="stats-item">
        <span class="stats-num">{{ roster.length }}</span>
        <span class="stats-label">团员人数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ villageCount }}</span>
        <span class="stats-label">覆盖村落</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ branchCount }}</span>
        <span class="stats-label">团支部数</span>
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
          <van-radio name="branch">支部</van-radio>
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
      <div class="export-btn league-btn" @click="exportExcel">
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
        <div class="roster-avatar league-avatar">
          {{ item.name.charAt(0) }}
        </div>
        <div class="roster-info">
          <div class="roster-name">{{ item.name }} <span class="roster-gender">({{ item.gender }})</span></div>
          <div class="roster-position">{{ item.village }} · {{ item.branch }}</div>
          <div class="roster-meta">
            <span class="meta-tag year-tag">团龄 {{ item.leagueYears }} 年</span>
            <span class="meta-tag date-tag">{{ item.joinDate }} 入团</span>
          </div>
          <div class="roster-phone" @click="handleCall(item)">
            <van-icon name="phone" size="14" color="#FF5722" />
            <span>{{ item.phone }}</span>
          </div>
        </div>
        <div class="roster-actions">
          <div class="action-btn league-call-btn" @click="handleCall(item)">
            <van-icon name="phone" size="18" color="#fff" />
          </div>
          <div class="action-btn league-share-btn" @click="handleShareItem(item)">
            <van-icon name="share-o" size="18" color="#fff" />
          </div>
        </div>
      </div>
    </div>

    <div class="empty-state" v-else>
      <van-icon name="search" size="48" color="#ddd" />
      <p>未找到匹配的团员</p>
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
    all: '搜索姓名、支部、电话...',
    name: '输入姓名搜索...',
    branch: '输入团支部搜索...',
    phone: '输入电话号码搜索...'
  }
  return map[searchField.value] || map.all
})

const roster = ref([
  { id: 1, name: '李明辉', gender: '男', village: '罗卜田村', branch: '罗卜田村团支部', joinDate: '2020-05-04', leagueYears: 6, phone: '13874550401' },
  { id: 2, name: '王小芳', gender: '女', village: '罗卜田村', branch: '罗卜田村团支部', joinDate: '2019-07-01', leagueYears: 7, phone: '13974550402' },
  { id: 3, name: '张志强', gender: '男', village: '罗卜田村', branch: '罗卜田村团支部', joinDate: '2021-04-28', leagueYears: 5, phone: '13774550403' },
  { id: 4, name: '杨春丽', gender: '女', village: '罗卜田村', branch: '罗卜田村团支部', joinDate: '2022-01-15', leagueYears: 4, phone: '13674550404' },
  { id: 5, name: '周子豪', gender: '男', village: '罗卜田村', branch: '罗卜田村团支部', joinDate: '2018-10-20', leagueYears: 8, phone: '13574550405' },
  { id: 6, name: '刘思琪', gender: '女', village: '罗卜田村', branch: '罗卜田村团支部', joinDate: '2023-03-12', leagueYears: 3, phone: '15874550406' },
  { id: 7, name: '陈俊杰', gender: '男', village: '新店村', branch: '新店村团支部', joinDate: '2019-05-04', leagueYears: 7, phone: '15974550407' },
  { id: 8, name: '赵雅婷', gender: '女', village: '新店村', branch: '新店村团支部', joinDate: '2020-09-18', leagueYears: 6, phone: '15774550408' },
  { id: 9, name: '黄伟杰', gender: '男', village: '新店村', branch: '新店村团支部', joinDate: '2021-06-30', leagueYears: 5, phone: '15674550409' },
  { id: 10, name: '孙美玲', gender: '女', village: '新店村', branch: '新店村团支部', joinDate: '2022-11-25', leagueYears: 4, phone: '15574550410' },
  { id: 11, name: '周嘉豪', gender: '男', village: '新店村', branch: '新店村团支部', joinDate: '2018-03-05', leagueYears: 8, phone: '18874550411' },
  { id: 12, name: '吴思雨', gender: '女', village: '新店村', branch: '新店村团支部', joinDate: '2023-05-20', leagueYears: 3, phone: '18974550412' },
  { id: 13, name: '郑浩然', gender: '男', village: '马坡村', branch: '马坡村团支部', joinDate: '2020-07-01', leagueYears: 6, phone: '18774550413' },
  { id: 14, name: '冯欣怡', gender: '女', village: '马坡村', branch: '马坡村团支部', joinDate: '2019-12-10', leagueYears: 7, phone: '18674550414' },
  { id: 15, name: '许文轩', gender: '男', village: '马坡村', branch: '马坡村团支部', joinDate: '2021-10-08', leagueYears: 5, phone: '18574550415' },
  { id: 16, name: '何梦瑶', gender: '女', village: '马坡村', branch: '马坡村团支部', joinDate: '2022-05-18', leagueYears: 4, phone: '17774550416' },
  { id: 17, name: '罗子阳', gender: '男', village: '马坡村', branch: '马坡村团支部', joinDate: '2018-08-22', leagueYears: 8, phone: '17874550417' },
  { id: 18, name: '梁思远', gender: '男', village: '冬瓜坡村', branch: '冬瓜坡村团支部', joinDate: '2019-04-15', leagueYears: 7, phone: '17674550418' },
  { id: 19, name: '谢雨桐', gender: '女', village: '冬瓜坡村', branch: '冬瓜坡村团支部', joinDate: '2020-11-30', leagueYears: 6, phone: '17574550419' },
  { id: 20, name: '宋梓涵', gender: '男', village: '冬瓜坡村', branch: '冬瓜坡村团支部', joinDate: '2021-08-12', leagueYears: 5, phone: '19974550420' },
  { id: 21, name: '唐若曦', gender: '女', village: '冬瓜坡村', branch: '冬瓜坡村团支部', joinDate: '2023-01-08', leagueYears: 3, phone: '19874550421' },
  { id: 22, name: '韩志远', gender: '男', village: '冬瓜坡村', branch: '冬瓜坡村团支部', joinDate: '2022-06-28', leagueYears: 4, phone: '19774550422' },
  { id: 23, name: '潘佳怡', gender: '女', village: '枣子山村', branch: '枣子山村团支部', joinDate: '2020-03-25', leagueYears: 6, phone: '19674550423' },
  { id: 24, name: '邓子墨', gender: '男', village: '枣子山村', branch: '枣子山村团支部', joinDate: '2019-10-01', leagueYears: 7, phone: '13074550424' },
  { id: 25, name: '曹诗琪', gender: '女', village: '枣子山村', branch: '枣子山村团支部', joinDate: '2021-02-14', leagueYears: 5, phone: '13174550425' },
  { id: 26, name: '曾俊杰', gender: '男', village: '枣子山村', branch: '枣子山村团支部', joinDate: '2018-12-20', leagueYears: 8, phone: '13274550426' },
  { id: 27, name: '肖雅文', gender: '女', village: '枣子山村', branch: '枣子山村团支部', joinDate: '2022-09-15', leagueYears: 4, phone: '13374550427' },
  { id: 28, name: '田宇航', gender: '男', village: '半冲村', branch: '半冲村团支部', joinDate: '2020-06-01', leagueYears: 6, phone: '13474550428' },
  { id: 29, name: '袁梦婷', gender: '女', village: '半冲村', branch: '半冲村团支部', joinDate: '2019-08-18', leagueYears: 7, phone: '15074550429' },
  { id: 30, name: '蒋文博', gender: '男', village: '半冲村', branch: '半冲村团支部', joinDate: '2021-11-30', leagueYears: 5, phone: '15174550430' },
  { id: 31, name: '蔡佳慧', gender: '女', village: '半冲村', branch: '半冲村团支部', joinDate: '2023-02-20', leagueYears: 3, phone: '15274550431' },
  { id: 32, name: '魏浩然', gender: '男', village: '半冲村', branch: '半冲村团支部', joinDate: '2022-04-10', leagueYears: 4, phone: '15374550432' },
  { id: 33, name: '廖思远', gender: '男', village: '半冲村', branch: '半冲村团支部', joinDate: '2018-05-25', leagueYears: 8, phone: '15474550433' },
  { id: 34, name: '范雨晴', gender: '女', village: '兴无村', branch: '兴无村团支部', joinDate: '2020-09-10', leagueYears: 6, phone: '13874550434' },
  { id: 35, name: '江梓豪', gender: '男', village: '兴无村', branch: '兴无村团支部', joinDate: '2019-01-22', leagueYears: 7, phone: '13974550435' },
  { id: 36, name: '卢欣怡', gender: '女', village: '兴无村', branch: '兴无村团支部', joinDate: '2021-07-08', leagueYears: 5, phone: '13774550436' },
  { id: 37, name: '余文博', gender: '男', village: '兴无村', branch: '兴无村团支部', joinDate: '2022-12-01', leagueYears: 4, phone: '13674550437' },
  { id: 38, name: '方梦琪', gender: '女', village: '兴无村', branch: '兴无村团支部', joinDate: '2023-04-18', leagueYears: 3, phone: '13574550438' },
  { id: 39, name: '姚俊杰', gender: '男', village: '兴无村', branch: '兴无村团支部', joinDate: '2018-11-15', leagueYears: 8, phone: '15874550439' },
  { id: 40, name: '谭雅琴', gender: '女', village: '兴无村', branch: '兴无村团支部', joinDate: '2020-02-28', leagueYears: 6, phone: '15974550440' },
  { id: 41, name: '龙子轩', gender: '男', village: '罗卜田村', branch: '罗卜田村团支部', joinDate: '2021-09-05', leagueYears: 5, phone: '15774550441' },
  { id: 42, name: '熊美玲', gender: '女', village: '新店村', branch: '新店村团支部', joinDate: '2022-03-22', leagueYears: 4, phone: '15674550442' }
])

const villageTabs = computed(() => {
  return [...new Set(roster.value.map(r => r.village))]
})

const villageCount = computed(() => {
  return new Set(roster.value.map(r => r.village)).size
})

const branchCount = computed(() => {
  return new Set(roster.value.map(r => r.branch)).size
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
      } else if (searchField.value === 'branch') {
        return item.branch.toLowerCase().includes(keyword)
      } else if (searchField.value === 'phone') {
        return item.phone.includes(keyword)
      } else {
        return item.name.toLowerCase().includes(keyword) ||
               item.branch.toLowerCase().includes(keyword) ||
               item.phone.includes(keyword) ||
               item.village.toLowerCase().includes(keyword) ||
               item.joinDate.includes(keyword)
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
    title: '罗卜田乡团员花名册',
    description: `共${roster.value.length}位团员，覆盖${villageCount.value}个村落，${branchCount.value}个团支部`,
    url: window.location.href
  })
}

const handleShareItem = (item) => {
  showSharePanel({
    title: `${item.name} - 团员`,
    description: `${item.branch}\n入团时间：${item.joinDate}\n团龄：${item.leagueYears}年\n电话：${item.phone}`,
    url: window.location.href
  })
}

const exportExcel = () => {
  const data = filteredList.value.map((item, index) => ({
    '序号': index + 1,
    '姓名': item.name,
    '性别': item.gender,
    '所属村': item.village,
    '团支部': item.branch,
    '入团时间': item.joinDate,
    '团龄': item.leagueYears,
    '联系电话': item.phone
  }))

  if (data.length === 0) {
    showToast('暂无可导出数据')
    return
  }

  const ws = XLSX.utils.json_to_sheet(data)
  ws['!cols'] = [
    { wch: 8 },
    { wch: 12 },
    { wch: 8 },
    { wch: 12 },
    { wch: 18 },
    { wch: 14 },
    { wch: 8 },
    { wch: 16 }
  ]
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '团员花名册')
  XLSX.writeFile(wb, '罗卜田乡团员花名册.xlsx')
  showToast('导出成功')
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  document.title = '罗卜田乡团员花名册'
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

.league-stats {
  background: linear-gradient(135deg, #FF5722, #FF8A65);
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

.league-btn {
  background: linear-gradient(135deg, #FF5722, #FF8A65);
  box-shadow: 0 2px 8px rgba(255, 87, 34, 0.3);
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

.league-avatar {
  background: linear-gradient(135deg, #FF5722, #FFAB91);
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

.roster-gender {
  font-size: 12px;
  font-weight: normal;
  color: #999;
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

.year-tag {
  background: #fff3e0;
  color: #e65100;
}

.date-tag {
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

.league-call-btn {
  background: linear-gradient(135deg, #FF5722, #FF8A65);
  box-shadow: 0 2px 8px rgba(255, 87, 34, 0.3);
}

.share-btn {
  background: var(--gradient-info);
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
}

.league-share-btn {
  background: linear-gradient(135deg, #FF9800, #FFCC80);
  box-shadow: 0 2px 8px rgba(255, 152, 0, 0.3);
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
