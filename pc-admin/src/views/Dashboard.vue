<template>
  <div class="dashboard page-container">
    <div class="header-banner">
      <div class="banner-image">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=traditional%20chinese%20village%20arch%20bridge%20entrance%20scenic%20view%20landscape%20peaceful%20countryside&image_size=landscape_16_9" alt="村特色风景" />
        <div class="banner-overlay"></div>
        <div class="banner-title">幸福村</div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <span class="header-icon">📊</span>
          <h3>基本村情</h3>
        </div>
      </div>
      <div class="grid-4">
        <div class="info-card red-gradient">
          <div class="card-icon">👥</div>
          <div class="card-content">
            <div class="card-value">{{ villageInfo.population || 0 }}</div>
            <div class="card-label">总人口</div>
          </div>
        </div>
        <div class="info-card gold-gradient">
          <div class="card-icon">🏠</div>
          <div class="card-content">
            <div class="card-value">{{ villageInfo.households || 0 }}</div>
            <div class="card-label">总户数</div>
          </div>
        </div>
        <div class="info-card red-light-gradient">
          <div class="card-icon">💰</div>
          <div class="card-content">
            <div class="card-value">{{ villageInfo.assetTotal || 0 }}万</div>
            <div class="card-label">三资总额</div>
          </div>
        </div>
        <div class="info-card gold-light-gradient">
          <div class="card-icon">📋</div>
          <div class="card-content">
            <div class="card-value">{{ moduleStats.demand || 0 }}</div>
            <div class="card-label">诉求工单</div>
          </div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <span class="header-icon">📈</span>
          <h3>本月收支</h3>
        </div>
      </div>
      <div class="income-expense-row">
        <div class="income-expense-card income">
          <div class="ie-value">+{{ fundStats.income || 0 }}</div>
          <div class="ie-label">收入(元)</div>
        </div>
        <div class="income-expense-card expense">
          <div class="ie-value">-{{ fundStats.expense || 0 }}</div>
          <div class="ie-label">支出(元)</div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <span class="header-icon">📋</span>
          <h3>诉求办结情况</h3>
        </div>
      </div>
      <div class="progress-list">
        <div class="progress-item">
          <div class="progress-header">
            <span class="progress-label">待受理</span>
            <span class="progress-count">{{ demandStats.pending || 0 }}</span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill warning" :style="{ width: getPercent(demandStats.pending, demandStats.total) + '%' }"></div>
          </div>
        </div>
        <div class="progress-item">
          <div class="progress-header">
            <span class="progress-label">处理中</span>
            <span class="progress-count">{{ demandStats.processing || 0 }}</span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill primary" :style="{ width: getPercent(demandStats.processing, demandStats.total) + '%' }"></div>
          </div>
        </div>
        <div class="progress-item">
          <div class="progress-header">
            <span class="progress-label">已办结</span>
            <span class="progress-count">{{ demandStats.completed || 0 }}</span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill success" :style="{ width: getPercent(demandStats.completed, demandStats.total) + '%' }"></div>
          </div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <span class="header-icon">✅</span>
          <h3>待办任务</h3>
        </div>
      </div>
      <div class="todo-grid">
        <div class="todo-card pending">
          <div class="todo-value">{{ taskStats.pending || 0 }}</div>
          <div class="todo-label">待开始</div>
        </div>
        <div class="todo-card processing">
          <div class="todo-value">{{ taskStats.processing || 0 }}</div>
          <div class="todo-label">进行中</div>
        </div>
        <div class="todo-card overdue">
          <div class="todo-value">{{ taskStats.overdue || 0 }}</div>
          <div class="todo-label">已逾期</div>
        </div>
        <div class="todo-card completed">
          <div class="todo-value">{{ taskStats.completed || 0 }}</div>
          <div class="todo-label">已完成</div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <span class="header-icon">🚀</span>
          <h3>业务模块</h3>
        </div>
      </div>
      <div class="business-grid">
        <div class="business-card" v-for="module in businessModules" :key="module.name" @click="goModule(module.path)">
          <div class="business-icon">{{ module.icon }}</div>
          <div class="business-name">{{ module.name }}</div>
          <div class="business-count">{{ module.count }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()

const villageInfo = reactive({
  population: 0,
  households: 0,
  assetTotal: 0
})

const demandStats = reactive({
  total: 0,
  pending: 0,
  processing: 0,
  completed: 0
})

const fundStats = reactive({
  income: 0,
  expense: 0
})

const taskStats = reactive({
  pending: 0,
  processing: 0,
  overdue: 0,
  completed: 0
})

const moduleStats = reactive({
  demand: 0,
  partyMember: 0,
  partyActivity: 0,
  meeting: 0,
  document: 0,
  convenience: 0,
  specialGroup: 0,
  fund: 0,
  asset: 0,
  resource: 0,
  safetyHazard: 0,
  flood: 0,
  fire: 0,
  food: 0,
  toilet: 0,
  forest: 0,
  road: 0,
  dispute: 0
})

const businessModules = computed(() => [
  { name: '党建工作', icon: '🚩', path: '/party/members', count: moduleStats.partyMember + moduleStats.partyActivity },
  { name: '政务工作', icon: '📜', path: '/government/meetings', count: moduleStats.meeting + moduleStats.document },
  { name: '便民服务', icon: '🤝', path: '/convenience/services', count: moduleStats.convenience + moduleStats.specialGroup },
  { name: '三资管理', icon: '💼', path: '/assets/funds', count: moduleStats.fund + moduleStats.asset + moduleStats.resource },
  { name: '安全防护', icon: '⚠️', path: '/safety/hazards', count: moduleStats.safetyHazard + moduleStats.flood + moduleStats.fire + moduleStats.food },
  { name: '乡村振兴', icon: '🌾', path: '/rural/toilets', count: moduleStats.toilet + moduleStats.forest + moduleStats.road },
  { name: '综合治理', icon: '⚖️', path: '/governance/disputes', count: moduleStats.dispute },
  { name: '人口户籍', icon: '👥', path: '/population/list', count: 0 },
  { name: '土地管理', icon: '📍', path: '/land/house', count: 0 },
  { name: '惠民补贴', icon: '🎁', path: '/subsidy/list', count: 0 },
  { name: '民政社保', icon: '❤️', path: '/pension/insurance', count: 0 },
  { name: '项目工程', icon: '🏗️', path: '/project/list', count: 0 },
  { name: '志愿服务', icon: '🙋', path: '/volunteer/list', count: 0 },
  { name: '媒体宣传', icon: '📰', path: '/media/list', count: 0 },
  { name: '档案资料', icon: '📁', path: '/archive/list', count: 0 },
  { name: '物资仓库', icon: '📦', path: '/warehouse/list', count: 0 },
  { name: '人居环境', icon: '🌿', path: '/environment/improvement', count: 0 },
  { name: '信访调解', icon: '📝', path: '/governance/petition', count: 0 },
  { name: '通知公告', icon: '📢', path: '/notification', count: 0 },
  { name: '系统设置', icon: '⚙️', path: '/system/permission', count: 0 }
])

const getPercent = (val, total) => {
  if (!total) return 0
  return Math.min(100, Math.round((val / total) * 100))
}

const goModule = (path) => {
  router.push(path)
}

const fetchModuleCount = async (url, key) => {
  try {
    const res = await request.get(url, { params: { page: 1, size: 1 } })
    if (res && res.data) {
      moduleStats[key] = res.data.total || (res.data.records ? res.data.records.length : 0)
    }
  } catch (error) {
  }
}

const fetchVillageAndDemand = async () => {
  try {
    const villageRes = await request.get('/public/village-info')
    Object.assign(villageInfo, villageRes.data)
  } catch (error) {
    console.error('获取村情信息失败', error)
  }
  try {
    const demandRes = await request.get('/demand/stats')
    Object.assign(demandStats, demandRes.data)
    moduleStats.demand = demandStats.total
  } catch (error) {
    console.error('获取诉求统计失败', error)
  }
  try {
    const fundRes = await request.get('/fund/stats')
    Object.assign(fundStats, fundRes.data)
  } catch (error) {
  }
  try {
    const taskRes = await request.get('/task-supervision/stats')
    Object.assign(taskStats, taskRes.data)
  } catch (error) {
  }
}

const fetchAllModuleStats = async () => {
  const tasks = [
    fetchModuleCount('/party-member/list', 'partyMember'),
    fetchModuleCount('/party-activity/list', 'partyActivity'),
    fetchModuleCount('/meeting/list', 'meeting'),
    fetchModuleCount('/document-record/list', 'document'),
    fetchModuleCount('/convenience-service/list', 'convenience'),
    fetchModuleCount('/special-group/list', 'specialGroup'),
    fetchModuleCount('/fund/list', 'fund'),
    fetchModuleCount('/asset/list', 'asset'),
    fetchModuleCount('/resource/list', 'resource'),
    fetchModuleCount('/safety-hazard/list', 'safetyHazard'),
    fetchModuleCount('/flood-control/list', 'flood'),
    fetchModuleCount('/fire-safety/list', 'fire'),
    fetchModuleCount('/food-safety/list', 'food'),
    fetchModuleCount('/toilet-reform/list', 'toilet'),
    fetchModuleCount('/forest-fire/list', 'forest'),
    fetchModuleCount('/road-traffic/list', 'road'),
    fetchModuleCount('/dispute/list', 'dispute')
  ]
  await Promise.all(tasks)
}

onMounted(() => {
  fetchVillageAndDemand()
  fetchAllModuleStats()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.page-container {
  min-height: 100%;
  background: #FDF5F5;
}

.header-banner {
  width: 100%;
  position: relative;
}

.banner-image {
  width: 100%;
  height: 200px;
  position: relative;
  overflow: hidden;
}

.banner-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(200, 16, 46, 0.3), rgba(200, 16, 46, 0.6));
}

.banner-title {
  position: absolute;
  bottom: 24px;
  left: 20px;
  font-size: 32px;
  font-weight: bold;
  color: #FFFFFF;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.module-card {
  background: #FFFFFF;
  margin: 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(200, 16, 46, 0.08);
}

.module-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-icon {
  font-size: 20px;
}

.module-header h3 {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.grid-4 {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.info-card {
  padding: 16px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: transform 0.2s;
}

.info-card:active {
  transform: scale(0.98);
}

.red-gradient {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

.gold-gradient {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
}

.red-light-gradient {
  background: linear-gradient(135deg, #FEF5F5 0%, #FDE8E8 100%);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.gold-light-gradient {
  background: linear-gradient(135deg, #FFFDE7 0%, #FFF9C4 100%);
  border: 1px solid rgba(255, 215, 0, 0.3);
}

.card-icon {
  font-size: 32px;
}

.card-content {
  flex: 1;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #FFFFFF;
}

.red-light-gradient .card-value,
.gold-light-gradient .card-value {
  color: #D22630;
}

.card-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
  margin-top: 2px;
}

.red-light-gradient .card-label {
  color: #999;
}

.gold-light-gradient .card-label {
  color: #8B4513;
}

.income-expense-row {
  display: flex;
  gap: 16px;
}

.income-expense-card {
  flex: 1;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
}

.income-expense-card.income {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

.income-expense-card.expense {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
}

.ie-value {
  font-size: 28px;
  font-weight: bold;
}

.income-expense-card.income .ie-value {
  color: #FFD700;
}

.income-expense-card.expense .ie-value {
  color: #8B4513;
}

.ie-label {
  font-size: 13px;
  margin-top: 8px;
}

.income-expense-card.income .ie-label {
  color: rgba(255, 255, 255, 0.8);
}

.income-expense-card.expense .ie-label {
  color: rgba(0, 0, 0, 0.6);
}

.progress-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.progress-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.progress-label {
  font-size: 14px;
  color: #666;
}

.progress-count {
  font-size: 14px;
  font-weight: bold;
  color: #D22630;
}

.progress-bar {
  height: 12px;
  background: #f0f0f0;
  border-radius: 6px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 6px;
  transition: width 0.3s;
}

.progress-fill.warning {
  background: linear-gradient(90deg, #e6a23c, #ebb563);
}

.progress-fill.primary {
  background: linear-gradient(90deg, #409eff, #66b1ff);
}

.progress-fill.success {
  background: linear-gradient(90deg, #67c23a, #85ce61);
}

.todo-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.todo-card {
  padding: 16px 8px;
  text-align: center;
  border-radius: 12px;
  border-top: 4px solid;
  background: #FAFAFA;
}

.todo-card.pending {
  border-top-color: #909399;
}

.todo-card.processing {
  border-top-color: #409eff;
}

.todo-card.overdue {
  border-top-color: #D22630;
}

.todo-card.completed {
  border-top-color: #67c23a;
}

.todo-value {
  font-size: 24px;
  font-weight: bold;
  color: #D22630;
}

.todo-label {
  font-size: 11px;
  color: #999;
  margin-top: 4px;
}

.business-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.business-card {
  padding: 16px 8px;
  text-align: center;
  border-radius: 12px;
  background: linear-gradient(135deg, #FEF5F5 0%, #FFF9F9 100%);
  border: 1px solid rgba(210, 38, 48, 0.1);
  transition: all 0.2s;
}

.business-card:active {
  transform: scale(0.98);
  background: linear-gradient(135deg, #FDE8E8 0%, #FCE4E4 100%);
}

.business-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.business-name {
  font-size: 13px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.business-count {
  font-size: 18px;
  font-weight: bold;
  color: #D22630;
}

@media (min-width: 768px) {
  .banner-image {
    height: 280px;
  }
  
  .grid-4 {
    grid-template-columns: repeat(4, 1fr);
  }
  
  .business-grid {
    grid-template-columns: repeat(5, 1fr);
  }
  
  .module-card {
    margin: 24px;
    padding: 24px;
  }
  
  .module-header h3 {
    font-size: 18px;
  }
}
</style>
