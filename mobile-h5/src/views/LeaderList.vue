<template>
  <div class="page-container">
    <van-nav-bar title="乡镇主要事务负责人" left-arrow @click-left="goBack" />
    
    <div class="header-card">
      <div class="header-icon">
        <van-icon name="contacts" size="40" color="#fff" />
      </div>
      <div class="header-title">罗卜田乡班子成员分管工作明细表</div>
      <div class="header-desc">共{{ filteredLeaders.length }}位班子成员</div>
    </div>

    <div class="filter-section">
      <van-search 
        v-model="searchKeyword" 
        placeholder="搜索姓名或分管领域..." 
        shape="round"
        @clear="searchKeyword = ''"
      />
      <div class="filter-tags">
        <span 
          v-for="cat in categories" 
          :key="cat.key" 
          class="filter-tag"
          :class="{ active: activeCategory === cat.key }"
          @click="activeCategory = cat.key"
        >{{ cat.label }}</span>
      </div>
    </div>

    <div class="leader-list" v-if="filteredLeaders.length > 0">
      <div 
        v-for="leader in filteredLeaders" 
        :key="leader.id" 
        class="leader-card"
        @click="goDetail(leader.id)"
      >
        <div class="leader-avatar" :style="{ background: leader.color }">
          {{ leader.name.charAt(0) }}
        </div>
        <div class="leader-info">
          <div class="leader-name">{{ leader.name }}</div>
          <div class="leader-count">分管 {{ leader.works.length }} 项事务</div>
          <div class="leader-works">
            <span 
              v-for="(work, idx) in leader.works.slice(0, 3)" 
              :key="idx" 
              class="work-tag"
            >{{ work.name }}</span>
            <span v-if="leader.works.length > 3" class="work-tag more-tag">+{{ leader.works.length - 3 }}</span>
          </div>
        </div>
        <van-icon name="arrow" size="16" color="#ccc" />
      </div>
    </div>

    <div class="empty-state" v-else>
      <van-icon name="search" size="48" color="#ddd" />
      <p>未找到匹配的负责人</p>
      <van-button type="primary" size="small" @click="resetFilter">重置筛选</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchKeyword = ref('')
const activeCategory = ref('all')

const LEADER_AUTH_KEY = 'leader_unlocked'

onMounted(() => {
  if (sessionStorage.getItem(LEADER_AUTH_KEY) !== 'true') {
    router.replace('/')
    return
  }
  document.title = '乡镇主要事务负责人 - 村级智慧村务平台'
})

const categories = [
  { key: 'all', label: '全部' },
  { key: 'politics', label: '党政综合' },
  { key: 'economy', label: '经济发展' },
  { key: 'society', label: '社会事务' },
  { key: 'party', label: '党建群团' },
  { key: 'discipline', label: '纪检监察' },
  { key: 'emergency', label: '应急安全' },
  { key: 'agriculture', label: '农业农村' },
  { key: 'office', label: '行政办公' }
]

const workCategoryMap = {
  politics: ['政协联络', '统战', '民族宗教', '办公室', '政务服务', '政务公开', '档案工作', '保密工作'],
  economy: ['乡村振兴', '营商环境', '经管（三资）', '财务', '审计'],
  society: ['信访维稳', '禁毒', '法制', '非法集资', '民政', '残联', '医疗保障', '卫生健康', '食品安全', '教育', '人力资源和社会保障'],
  party: ['基层党建', '组织人事', '党务公开', '宣传思想文化', '新时代文明实践', '意识形态', '文化', '科协', '关工委', '妇联', '共青团', '工会'],
  discipline: ['纪检工作'],
  emergency: ['应急管理', '安全生产', '水利', '防汛抗旱'],
  agriculture: ['农业农村', '禁捕退捕', '畜牧', '农机', '宅基地审批', '林业', '农村人居环境治理', '生态环保'],
  office: ['人大工作', '政法', '武装', '国防动员教育', '退役军人', '依法行政', '统计', '绩效考核', '自然资源', '道路交通安全', '行政执法', '住建', '民生实事']
}

const leaders = ref([
  {
    id: 1,
    name: '欧阳付群',
    color: 'linear-gradient(135deg, #D22630 0%, #B01A26 100%)',
    works: [
      { name: '政协联络', hasDetail: true },
      { name: '统战', hasDetail: false },
      { name: '民族宗教', hasDetail: false },
      { name: '社会工作', hasDetail: false },
      { name: '乡村振兴', hasDetail: true },
      { name: '营商环境', hasDetail: false },
      { name: '经管（三资）', hasDetail: true },
      { name: '农村人居环境治理', hasDetail: true },
      { name: '生态环保', hasDetail: true }
    ]
  },
  {
    id: 2,
    name: '曹海洋',
    color: 'linear-gradient(135deg, #2196F3 0%, #1565C0 100%)',
    works: [
      { name: '人大工作', hasDetail: false },
      { name: '政法', hasDetail: false },
      { name: '信访维稳', hasDetail: false },
      { name: '禁毒', hasDetail: false },
      { name: '法制', hasDetail: false },
      { name: '非法集资', hasDetail: false }
    ]
  },
  {
    id: 3,
    name: '龙君屹',
    color: 'linear-gradient(135deg, #4CAF50 0%, #388E3C 100%)',
    works: [
      { name: '武装', hasDetail: false },
      { name: '国防动员教育', hasDetail: false },
      { name: '退役军人', hasDetail: false },
      { name: '农业农村', hasDetail: false },
      { name: '依法行政', hasDetail: false },
      { name: '统计', hasDetail: false },
      { name: '民政', hasDetail: false },
      { name: '残联', hasDetail: false },
      { name: '医疗保障', hasDetail: false },
      { name: '卫生健康', hasDetail: false },
      { name: '食品安全', hasDetail: false },
      { name: '教育', hasDetail: false }
    ]
  },
  {
    id: 4,
    name: '张芷馨',
    color: 'linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%)',
    works: [
      { name: '基层党建', hasDetail: false },
      { name: '组织人事', hasDetail: false },
      { name: '党务公开', hasDetail: false },
      { name: '宣传思想文化', hasDetail: false },
      { name: '新时代文明实践', hasDetail: false },
      { name: '意识形态', hasDetail: false },
      { name: '文化', hasDetail: false },
      { name: '科协', hasDetail: false },
      { name: '关工委', hasDetail: false },
      { name: '妇联', hasDetail: false },
      { name: '共青团', hasDetail: false },
      { name: '工会', hasDetail: false }
    ]
  },
  {
    id: 5,
    name: '于鼎馨',
    color: 'linear-gradient(135deg, #FF5722 0%, #E64A19 100%)',
    works: [
      { name: '纪检工作', hasDetail: true }
    ]
  },
  {
    id: 6,
    name: '杨承明',
    color: 'linear-gradient(135deg, #00BCD4 0%, #0097A7 100%)',
    works: [
      { name: '应急管理', hasDetail: false },
      { name: '安全生产', hasDetail: false },
      { name: '水利', hasDetail: false },
      { name: '防汛抗旱', hasDetail: false },
      { name: '住建', hasDetail: false }
    ]
  },
  {
    id: 7,
    name: '杨成',
    color: 'linear-gradient(135deg, #FF9800 0%, #E65100 100%)',
    works: [
      { name: '办公室', hasDetail: false },
      { name: '政务服务', hasDetail: false },
      { name: '政务公开', hasDetail: false },
      { name: '档案工作', hasDetail: false },
      { name: '保密工作', hasDetail: false },
      { name: '民生实事', hasDetail: false },
      { name: '绩效考核', hasDetail: false },
      { name: '人力资源和社会保障', hasDetail: false },
      { name: '自然资源', hasDetail: false },
      { name: '财务', hasDetail: false },
      { name: '审计', hasDetail: false },
      { name: '林业', hasDetail: false },
      { name: '道路交通安全', hasDetail: false },
      { name: '行政执法', hasDetail: false }
    ]
  }
])

const filteredLeaders = computed(() => {
  let result = leaders.value
  
  if (activeCategory.value !== 'all') {
    const workNames = workCategoryMap[activeCategory.value] || []
    result = result.filter(leader => 
      leader.works.some(work => workNames.some(name => work.name.includes(name)) || 
        workNames.some(name => name.includes(work.name)))
    )
  }
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(leader => 
      leader.name.toLowerCase().includes(keyword) ||
      leader.works.some(work => work.name.toLowerCase().includes(keyword))
    )
  }
  
  return result
})

const resetFilter = () => {
  searchKeyword.value = ''
  activeCategory.value = 'all'
}

const goBack = () => {
  router.back()
}

const goDetail = (id) => {
  router.push(`/leader-detail/${id}`)
}
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.header-card {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  padding: 24px 16px;
  text-align: center;
  color: #fff;
  margin-bottom: 12px;
}

.header-icon {
  margin-bottom: 12px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 6px;
}

.header-desc {
  font-size: 13px;
  opacity: 0.9;
}

.leader-list {
  padding: 0 12px;
}

.leader-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 12px;
  padding: 14px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.leader-card:active {
  transform: scale(0.98);
  background: #f9f9f9;
}

.leader-avatar {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  margin-right: 14px;
}

.leader-info {
  flex: 1;
  min-width: 0;
}

.leader-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 6px;
}

.leader-count {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.leader-works {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.work-tag {
  padding: 3px 8px;
  background: #f0f0f0;
  border-radius: 6px;
  font-size: 11px;
  color: #666;
}

.work-tag.more-tag {
  background: #D22630;
  color: #fff;
}

.filter-section {
  background: #fff;
  padding: 12px;
  margin-bottom: 12px;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.filter-tag {
  padding: 6px 12px;
  background: #f5f5f5;
  border-radius: 16px;
  font-size: 12px;
  color: #666;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-tag.active {
  background: var(--gradient-primary);
  color: #fff;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.3);
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