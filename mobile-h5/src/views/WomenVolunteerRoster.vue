<template>
  <div class="page-container">
    <van-nav-bar title="罗卜田乡巾帼志愿服务队名单" left-arrow @click-left="goBack">
      <template #right>
        <van-icon name="share-o" size="20" color="#fff" @click="handleShare" />
      </template>
    </van-nav-bar>

    <div class="stats-card women-stats">
      <div class="stats-item">
        <span class="stats-num">{{ roster.length }}</span>
        <span class="stats-label">队员数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ villageCount }}</span>
        <span class="stats-label">覆盖村落</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ teamCount }}</span>
        <span class="stats-label">服务队伍数</span>
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
          <van-radio name="teamName">队伍</van-radio>
          <van-radio name="phone">电话</van-radio>
        </van-radio-group>
      </div>
    </div>

    <div class="tabs-container">
      <van-tabs v-model="activeTeamTab" sticky>
        <van-tab title="全部队伍" name="all" />
        <van-tab v-for="team in teamTabs" :key="team" :title="team" :name="team" />
      </van-tabs>
    </div>

    <div class="tabs-container tabs-secondary">
      <van-tabs v-model="activeVillageTab" sticky offset-top="92">
        <van-tab title="全部村落" name="all" />
        <van-tab v-for="village in villageTabs" :key="village" :title="village" :name="village" />
      </van-tabs>
    </div>

    <div class="export-bar">
      <div class="export-btn women-btn" @click="exportExcel">
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
        <div class="roster-avatar women-avatar">
          {{ item.name.charAt(0) }}
        </div>
        <div class="roster-info">
          <div class="roster-name">{{ item.name }}</div>
          <div class="roster-position">{{ item.village }} · {{ item.teamName }}</div>
          <div class="roster-meta">
            <span class="meta-tag team-tag">{{ item.position }}</span>
            <span class="meta-tag date-tag">{{ item.joinDate }} 加入</span>
          </div>
          <div class="roster-phone" @click="handleCall(item)">
            <van-icon name="phone" size="14" color="#E91E63" />
            <span>{{ item.phone }}</span>
          </div>
        </div>
        <div class="roster-actions">
          <div class="action-btn women-call-btn" @click="handleCall(item)">
            <van-icon name="phone" size="18" color="#fff" />
          </div>
          <div class="action-btn women-share-btn" @click="handleShareItem(item)">
            <van-icon name="share-o" size="18" color="#fff" />
          </div>
        </div>
      </div>
    </div>

    <div class="empty-state" v-else>
      <van-icon name="search" size="48" color="#ddd" />
      <p>未找到匹配的巾帼志愿者</p>
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
const activeTeamTab = ref('all')
const activeVillageTab = ref('all')

const villages = ['罗卜田村', '新店村', '马坡村', '冬瓜坡村', '枣子山村', '半冲村', '兴无村']
const teamNames = ['政策宣传', '创业就业', '家庭建设', '权益维护', '文化体育', '关爱帮扶']

const searchPlaceholder = computed(() => {
  const map = {
    all: '搜索姓名、队伍、电话...',
    name: '输入姓名搜索...',
    teamName: '输入志愿队伍搜索...',
    phone: '输入电话号码搜索...'
  }
  return map[searchField.value] || map.all
})

const roster = ref([
  { id: 1, name: '杨秀兰', village: '罗卜田村', teamName: '政策宣传', position: '队长', phone: '13874540301', joinDate: '2021-03-08' },
  { id: 2, name: '李桂芳', village: '罗卜田村', teamName: '家庭建设', position: '副队长', phone: '13974540302', joinDate: '2021-05-12' },
  { id: 3, name: '张美华', village: '罗卜田村', teamName: '关爱帮扶', position: '队员', phone: '13774540303', joinDate: '2022-01-20' },
  { id: 4, name: '王春燕', village: '罗卜田村', teamName: '文化体育', position: '队员', phone: '13674540304', joinDate: '2022-04-15' },
  { id: 5, name: '陈慧敏', village: '罗卜田村', teamName: '权益维护', position: '队员', phone: '13574540305', joinDate: '2021-09-25' },
  { id: 6, name: '刘玉兰', village: '新店村', teamName: '创业就业', position: '队长', phone: '15874540306', joinDate: '2021-06-18' },
  { id: 7, name: '赵小梅', village: '新店村', teamName: '政策宣传', position: '队员', phone: '15974540307', joinDate: '2022-02-28' },
  { id: 8, name: '黄丽娟', village: '新店村', teamName: '家庭建设', position: '队员', phone: '15774540308', joinDate: '2022-07-14' },
  { id: 9, name: '孙秀珍', village: '新店村', teamName: '关爱帮扶', position: '队员', phone: '15674540309', joinDate: '2021-11-30' },
  { id: 10, name: '周艳红', village: '新店村', teamName: '文化体育', position: '队员', phone: '15574540310', joinDate: '2023-01-05' },
  { id: 11, name: '吴翠萍', village: '马坡村', teamName: '权益维护', position: '队长', phone: '18874540311', joinDate: '2021-04-22' },
  { id: 12, name: '郑雪梅', village: '马坡村', teamName: '创业就业', position: '副队长', phone: '18974540312', joinDate: '2021-08-10' },
  { id: 13, name: '冯玉珍', village: '马坡村', teamName: '政策宣传', position: '队员', phone: '18774540313', joinDate: '2022-03-18' },
  { id: 14, name: '许春花', village: '马坡村', teamName: '家庭建设', position: '队员', phone: '18674540314', joinDate: '2022-09-25' },
  { id: 15, name: '何丽娟', village: '马坡村', teamName: '关爱帮扶', position: '队员', phone: '18574540315', joinDate: '2021-12-20' },
  { id: 16, name: '罗艳芳', village: '冬瓜坡村', teamName: '文化体育', position: '队长', phone: '17774540316', joinDate: '2021-07-15' },
  { id: 17, name: '梁小琴', village: '冬瓜坡村', teamName: '权益维护', position: '队员', phone: '17874540317', joinDate: '2022-05-22' },
  { id: 18, name: '谢桂香', village: '冬瓜坡村', teamName: '创业就业', position: '队员', phone: '17674540318', joinDate: '2022-10-30' },
  { id: 19, name: '宋美华', village: '冬瓜坡村', teamName: '政策宣传', position: '队员', phone: '17574540319', joinDate: '2023-02-14' },
  { id: 20, name: '唐慧敏', village: '枣子山村', teamName: '家庭建设', position: '队长', phone: '19974540320', joinDate: '2021-02-28' },
  { id: 21, name: '韩秀莲', village: '枣子山村', teamName: '关爱帮扶', position: '副队长', phone: '19874540321', joinDate: '2022-06-18' },
  { id: 22, name: '潘玉梅', village: '枣子山村', teamName: '文化体育', position: '队员', phone: '19774540322', joinDate: '2021-10-08' },
  { id: 23, name: '邓春燕', village: '枣子山村', teamName: '权益维护', position: '队员', phone: '19674540323', joinDate: '2023-03-20' },
  { id: 24, name: '曹丽华', village: '半冲村', teamName: '创业就业', position: '队长', phone: '13074540324', joinDate: '2021-05-12' },
  { id: 25, name: '曾晓燕', village: '半冲村', teamName: '政策宣传', position: '队员', phone: '13174540325', joinDate: '2022-08-22' },
  { id: 26, name: '肖美凤', village: '半冲村', teamName: '家庭建设', position: '队员', phone: '13274540326', joinDate: '2022-12-05' },
  { id: 27, name: '田菊花', village: '兴无村', teamName: '关爱帮扶', position: '队长', phone: '13374540327', joinDate: '2021-01-25' },
  { id: 28, name: '袁桂芳', village: '兴无村', teamName: '文化体育', position: '队员', phone: '13474540328', joinDate: '2022-04-10' },
  { id: 29, name: '蒋梅英', village: '兴无村', teamName: '权益维护', position: '队员', phone: '15074540329', joinDate: '2023-01-18' },
  { id: 30, name: '蔡秀兰', village: '兴无村', teamName: '创业就业', position: '队员', phone: '15174540330', joinDate: '2022-11-20' }
])

const villageTabs = computed(() => villages)

const teamTabs = computed(() => teamNames)

const villageCount = computed(() => {
  return new Set(roster.value.map(r => r.village)).size
})

const teamCount = computed(() => {
  return new Set(roster.value.map(r => r.teamName)).size
})

const filteredList = computed(() => {
  let result = roster.value

  if (activeTeamTab.value !== 'all') {
    result = result.filter(r => r.teamName === activeTeamTab.value)
  }

  if (activeVillageTab.value !== 'all') {
    result = result.filter(r => r.village === activeVillageTab.value)
  }

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(item => {
      if (searchField.value === 'name') {
        return item.name.toLowerCase().includes(keyword)
      } else if (searchField.value === 'teamName') {
        return item.teamName.toLowerCase().includes(keyword)
      } else if (searchField.value === 'phone') {
        return item.phone.includes(keyword)
      } else {
        return item.name.toLowerCase().includes(keyword) ||
               item.teamName.toLowerCase().includes(keyword) ||
               item.phone.includes(keyword) ||
               item.village.toLowerCase().includes(keyword) ||
               item.position.toLowerCase().includes(keyword)
      }
    })
  }

  return result
})

const resetFilter = () => {
  searchKeyword.value = ''
  searchField.value = 'all'
  activeTeamTab.value = 'all'
  activeVillageTab.value = 'all'
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
    title: '罗卜田乡巾帼志愿服务队名单',
    description: `共${roster.value.length}位巾帼志愿者，覆盖${villageCount.value}个村落，${teamCount.value}支服务队伍`,
    url: window.location.href
  })
}

const handleShareItem = (item) => {
  showSharePanel({
    title: `${item.name} - 巾帼志愿者`,
    description: `${item.teamName} · ${item.position}\n${item.village}\n加入时间：${item.joinDate}\n电话：${item.phone}`,
    url: window.location.href
  })
}

const exportExcel = () => {
  const data = filteredList.value.map((item, index) => ({
    '序号': index + 1,
    '姓名': item.name,
    '所属村': item.village,
    '志愿队伍': item.teamName,
    '职务': item.position,
    '联系电话': item.phone,
    '加入时间': item.joinDate
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
    { wch: 12 },
    { wch: 12 },
    { wch: 16 },
    { wch: 14 }
  ]
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '巾帼志愿者')
  XLSX.writeFile(wb, '罗卜田乡巾帼志愿服务队名单.xlsx')
  showToast('导出成功')
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  document.title = '罗卜田乡巾帼志愿服务队名单'
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

.women-stats {
  background: linear-gradient(135deg, #E91E63, #F06292);
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

.tabs-secondary {
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

.women-btn {
  background: linear-gradient(135deg, #E91E63, #F06292);
  box-shadow: 0 2px 8px rgba(233, 30, 99, 0.3);
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

.women-avatar {
  background: linear-gradient(135deg, #E91E63, #F48FB1);
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

.team-tag {
  background: #fce4ec;
  color: #c2185b;
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

.women-call-btn {
  background: linear-gradient(135deg, #E91E63, #F48FB1);
  box-shadow: 0 2px 8px rgba(233, 30, 99, 0.3);
}

.share-btn {
  background: var(--gradient-info);
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
}

.women-share-btn {
  background: linear-gradient(135deg, #9C27B0, #CE93D8);
  box-shadow: 0 2px 8px rgba(156, 39, 176, 0.3);
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
