<template>
  <div class="page-container">
    <van-nav-bar title="新时代文明实践站志愿者花名册" left-arrow @click-left="goBack">
      <template #right>
        <van-icon name="share-o" size="20" color="#fff" @click="handleShare" />
      </template>
    </van-nav-bar>

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ roster.length }}</span>
        <span class="stats-label">志愿者数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ villageCount }}</span>
        <span class="stats-label">覆盖村落</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ categoryCount }}</span>
        <span class="stats-label">服务类别数</span>
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
          <van-radio name="category">类别</van-radio>
          <van-radio name="phone">电话</van-radio>
        </van-radio-group>
      </div>
    </div>

    <div class="tabs-container">
      <van-tabs v-model="activeCategoryTab" sticky>
        <van-tab title="全部类别" name="all" />
        <van-tab v-for="cat in categoryTabs" :key="cat" :title="cat" :name="cat" />
      </van-tabs>
    </div>

    <div class="tabs-container tabs-secondary">
      <van-tabs v-model="activeVillageTab" sticky offset-top="92">
        <van-tab title="全部村落" name="all" />
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
          <div class="roster-name">{{ item.name }} <span class="roster-gender">({{ item.gender }})</span></div>
          <div class="roster-position">{{ item.village }} · {{ item.category }}</div>
          <div class="roster-meta">
            <span class="meta-tag">编号 {{ item.volunteerId }}</span>
            <span class="meta-tag hours-tag">时长 {{ item.hours }}h</span>
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
      <p>未找到匹配的志愿者</p>
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
const activeCategoryTab = ref('all')
const activeVillageTab = ref('all')

const villages = ['罗卜田村', '新店村', '马坡村', '冬瓜坡村', '枣子山村', '半冲村', '兴无村']
const categories = ['理论宣讲', '文化服务', '助学支教', '扶贫帮困', '环境保护', '卫生整治']

const searchPlaceholder = computed(() => {
  const map = {
    all: '搜索姓名、类别、电话...',
    name: '输入姓名搜索...',
    category: '输入服务类别搜索...',
    phone: '输入电话号码搜索...'
  }
  return map[searchField.value] || map.all
})

const roster = ref([
  { id: 1, volunteerId: 'V0001', name: '李明华', gender: '男', village: '罗卜田村', category: '理论宣讲', hours: 156, phone: '13874510001', joinDate: '2022-03-15' },
  { id: 2, volunteerId: 'V0002', name: '王秀兰', gender: '女', village: '罗卜田村', category: '文化服务', hours: 98, phone: '13974510002', joinDate: '2022-05-20' },
  { id: 3, volunteerId: 'V0003', name: '张建军', gender: '男', village: '罗卜田村', category: '卫生整治', hours: 203, phone: '13774510003', joinDate: '2021-11-08' },
  { id: 4, volunteerId: 'V0004', name: '杨春燕', gender: '女', village: '罗卜田村', category: '助学支教', hours: 124, phone: '13674510004', joinDate: '2023-01-10' },
  { id: 5, volunteerId: 'V0005', name: '周志远', gender: '男', village: '罗卜田村', category: '扶贫帮困', hours: 187, phone: '13574510005', joinDate: '2022-07-22' },
  { id: 6, volunteerId: 'V0006', name: '刘芳', gender: '女', village: '新店村', category: '环境保护', hours: 145, phone: '15874510006', joinDate: '2022-09-01' },
  { id: 7, volunteerId: 'V0007', name: '陈海波', gender: '男', village: '新店村', category: '理论宣讲', hours: 112, phone: '15974510007', joinDate: '2023-02-14' },
  { id: 8, volunteerId: 'V0008', name: '赵美玲', gender: '女', village: '新店村', category: '文化服务', hours: 176, phone: '15774510008', joinDate: '2021-08-30' },
  { id: 9, volunteerId: 'V0009', name: '孙伟强', gender: '男', village: '新店村', category: '卫生整治', hours: 89, phone: '15674510009', joinDate: '2023-04-05' },
  { id: 10, volunteerId: 'V0010', name: '黄丽娟', gender: '女', village: '新店村', category: '助学支教', hours: 215, phone: '15574510010', joinDate: '2022-01-18' },
  { id: 11, volunteerId: 'V0011', name: '吴德胜', gender: '男', village: '马坡村', category: '扶贫帮困', hours: 134, phone: '18874510011', joinDate: '2022-06-25' },
  { id: 12, volunteerId: 'V0012', name: '郑小英', gender: '女', village: '马坡村', category: '环境保护', hours: 167, phone: '18974510012', joinDate: '2021-12-12' },
  { id: 13, volunteerId: 'V0013', name: '冯志斌', gender: '男', village: '马坡村', category: '理论宣讲', hours: 78, phone: '18774510013', joinDate: '2023-05-20' },
  { id: 14, volunteerId: 'V0014', name: '许春梅', gender: '女', village: '马坡村', category: '文化服务', hours: 198, phone: '18674510014', joinDate: '2022-04-03' },
  { id: 15, volunteerId: 'V0015', name: '何光明', gender: '男', village: '马坡村', category: '卫生整治', hours: 156, phone: '18574510015', joinDate: '2022-10-15' },
  { id: 16, volunteerId: 'V0016', name: '罗玉珍', gender: '女', village: '冬瓜坡村', category: '助学支教', hours: 132, phone: '17774510016', joinDate: '2023-01-28' },
  { id: 17, volunteerId: 'V0017', name: '梁建华', gender: '男', village: '冬瓜坡村', category: '扶贫帮困', hours: 245, phone: '17874510017', joinDate: '2021-09-10' },
  { id: 18, volunteerId: 'V0018', name: '谢桂芳', gender: '女', village: '冬瓜坡村', category: '环境保护', hours: 108, phone: '17674510018', joinDate: '2022-08-05' },
  { id: 19, volunteerId: 'V0019', name: '宋海涛', gender: '男', village: '冬瓜坡村', category: '理论宣讲', hours: 167, phone: '17574510019', joinDate: '2022-03-22' },
  { id: 20, volunteerId: 'V0020', name: '唐雪梅', gender: '女', village: '冬瓜坡村', category: '文化服务', hours: 143, phone: '17474510020', joinDate: '2023-03-08' },
  { id: 21, volunteerId: 'V0021', name: '韩志勇', gender: '男', village: '枣子山村', category: '卫生整治', hours: 189, phone: '19974510021', joinDate: '2021-10-18' },
  { id: 22, volunteerId: 'V0022', name: '潘美华', gender: '女', village: '枣子山村', category: '助学支教', hours: 126, phone: '19874510022', joinDate: '2022-11-30' },
  { id: 23, volunteerId: 'V0023', name: '邓永昌', gender: '男', village: '枣子山村', category: '扶贫帮困', hours: 201, phone: '19774510023', joinDate: '2022-02-14' },
  { id: 24, volunteerId: 'V0024', name: '曹丽华', gender: '女', village: '枣子山村', category: '环境保护', hours: 154, phone: '19674510024', joinDate: '2023-04-19' },
  { id: 25, volunteerId: 'V0025', name: '彭俊杰', gender: '男', village: '枣子山村', category: '理论宣讲', hours: 93, phone: '19574510025', joinDate: '2023-06-01' },
  { id: 26, volunteerId: 'V0026', name: '曾秀莲', gender: '女', village: '半冲村', category: '文化服务', hours: 178, phone: '13074510026', joinDate: '2021-07-22' },
  { id: 27, volunteerId: 'V0027', name: '肖文斌', gender: '男', village: '半冲村', category: '卫生整治', hours: 165, phone: '13174510027', joinDate: '2022-05-09' },
  { id: 28, volunteerId: 'V0028', name: '田慧敏', gender: '女', village: '半冲村', category: '助学支教', hours: 234, phone: '13274510028', joinDate: '2021-12-01' },
  { id: 29, volunteerId: 'V0029', name: '袁建平', gender: '男', village: '半冲村', category: '扶贫帮困', hours: 112, phone: '13374510029', joinDate: '2023-02-27' },
  { id: 30, volunteerId: 'V0030', name: '蒋玉兰', gender: '女', village: '半冲村', category: '环境保护', hours: 145, phone: '13474510030', joinDate: '2022-09-18' },
  { id: 31, volunteerId: 'V0031', name: '蔡德明', gender: '男', village: '兴无村', category: '理论宣讲', hours: 178, phone: '15074510031', joinDate: '2022-01-05' },
  { id: 32, volunteerId: 'V0032', name: '魏秋香', gender: '女', village: '兴无村', category: '文化服务', hours: 134, phone: '15174510032', joinDate: '2022-07-12' },
  { id: 33, volunteerId: 'V0033', name: '廖志华', gender: '男', village: '兴无村', category: '卫生整治', hours: 198, phone: '15274510033', joinDate: '2021-11-25' },
  { id: 34, volunteerId: 'V0034', name: '范小玲', gender: '女', village: '兴无村', category: '助学支教', hours: 167, phone: '15374510034', joinDate: '2023-03-15' },
  { id: 35, volunteerId: 'V0035', name: '江国栋', gender: '男', village: '兴无村', category: '扶贫帮困', hours: 223, phone: '15474510035', joinDate: '2022-04-28' }
])

const villageTabs = computed(() => villages)

const categoryTabs = computed(() => categories)

const villageCount = computed(() => {
  return new Set(roster.value.map(r => r.village)).size
})

const categoryCount = computed(() => {
  return new Set(roster.value.map(r => r.category)).size
})

const filteredList = computed(() => {
  let result = roster.value

  if (activeCategoryTab.value !== 'all') {
    result = result.filter(r => r.category === activeCategoryTab.value)
  }

  if (activeVillageTab.value !== 'all') {
    result = result.filter(r => r.village === activeVillageTab.value)
  }

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(item => {
      if (searchField.value === 'name') {
        return item.name.toLowerCase().includes(keyword)
      } else if (searchField.value === 'category') {
        return item.category.toLowerCase().includes(keyword)
      } else if (searchField.value === 'phone') {
        return item.phone.includes(keyword)
      } else {
        return item.name.toLowerCase().includes(keyword) ||
               item.category.toLowerCase().includes(keyword) ||
               item.phone.includes(keyword) ||
               item.village.toLowerCase().includes(keyword) ||
               item.volunteerId.toLowerCase().includes(keyword)
      }
    })
  }

  return result
})

const resetFilter = () => {
  searchKeyword.value = ''
  searchField.value = 'all'
  activeCategoryTab.value = 'all'
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
    title: '新时代文明实践站志愿者花名册',
    description: `共${roster.value.length}位志愿者，覆盖${villageCount.value}个村落，${categoryCount.value}个服务类别`,
    url: window.location.href
  })
}

const handleShareItem = (item) => {
  showSharePanel({
    title: `${item.name} - 志愿者`,
    description: `${item.volunteerId}\n${item.village} · ${item.category}\n服务时长：${item.hours}小时\n电话：${item.phone}`,
    url: window.location.href
  })
}

const exportExcel = () => {
  const data = filteredList.value.map((item, index) => ({
    '序号': index + 1,
    '姓名': item.name,
    '性别': item.gender,
    '所属村': item.village,
    '志愿者编号': item.volunteerId,
    '服务类别': item.category,
    '服务时长': item.hours,
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
    { wch: 8 },
    { wch: 12 },
    { wch: 14 },
    { wch: 12 },
    { wch: 10 },
    { wch: 16 },
    { wch: 14 }
  ]
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '志愿者花名册')
  XLSX.writeFile(wb, '新时代文明实践站志愿者花名册.xlsx')
  showToast('导出成功')
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  document.title = '新时代文明实践站志愿者花名册'
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

.hours-tag {
  background: #fff3e0;
  color: #e65100;
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
