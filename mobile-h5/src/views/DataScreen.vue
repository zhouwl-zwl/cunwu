<template>
  <div class="data-screen-page page-container">
    <van-nav-bar title="数据大屏" left-arrow @click-left="onBack">
      <template #right>
        <van-button text type="primary" @click="captureScreen">截图分享</van-button>
      </template>
    </van-nav-bar>

    <div class="screen-header">
      <div class="village-name">{{ villageInfo.name || '罗卜田乡' }}综合数据</div>
      <div class="update-time">更新时间：{{ currentTime }}</div>
    </div>

    <div class="overview-grid">
      <div class="overview-item red">
        <div class="overview-value">{{ overview.population || 0 }}</div>
        <div class="overview-label">人口总数</div>
      </div>
      <div class="overview-item gold">
        <div class="overview-value">{{ overview.household || 0 }}</div>
        <div class="overview-label">户数</div>
      </div>
      <div class="overview-item red">
        <div class="overview-value">{{ overview.partyMember || 0 }}</div>
        <div class="overview-label">党员人数</div>
      </div>
      <div class="overview-item gold">
        <div class="overview-value">{{ overview.groupCount || 0 }}</div>
        <div class="overview-label">村民组数</div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">人口年龄分布</div>
      <div class="chart-container">
        <div class="bar-chart">
          <div v-for="(item, index) in ageDistribution" :key="index" class="bar-item">
            <div class="bar-label">{{ item.label }}</div>
            <div class="bar-track">
              <div class="bar-fill" :style="{ width: item.percent + '%' }" :class="item.color"></div>
            </div>
            <div class="bar-value">{{ item.value }}人</div>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">补贴发放统计</div>
      <div class="chart-container">
        <div class="pie-chart-wrapper">
          <div class="pie-chart">
            <div 
              v-for="(item, index) in subsidyDistribution" 
              :key="index" 
              class="pie-segment"
              :style="getPieStyle(item.percent, index)"
            ></div>
            <div class="pie-center">
              <div class="pie-total">{{ subsidyTotal }}万元</div>
              <div class="pie-label">发放总额</div>
            </div>
          </div>
          <div class="pie-legend">
            <div v-for="(item, index) in subsidyDistribution" :key="index" class="legend-item">
              <span class="legend-dot" :style="{ background: item.color }"></span>
              <span class="legend-text">{{ item.label }}</span>
              <span class="legend-value">{{ item.percent }}%</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">矛盾纠纷化解率</div>
      <div class="chart-container">
        <div class="rate-display">
          <div class="rate-circle">
            <div class="rate-ring">
              <svg class="progress-ring" width="120" height="120">
                <circle class="ring-bg" cx="60" cy="60" r="50" fill="none" stroke="#f0f0f0" stroke-width="8" />
                <circle 
                  class="ring-progress" 
                  cx="60" cy="60" r="50" 
                  fill="none" 
                  :stroke="disputeRate >= 80 ? '#07c160' : disputeRate >= 50 ? '#e6a23c' : '#D22630'"
                  stroke-width="8"
                  stroke-linecap="round"
                  :stroke-dasharray="circumference"
                  :stroke-dashoffset="rateOffset"
                  transform="rotate(-90 60 60)"
                />
              </svg>
              <div class="rate-inner">
                <div class="rate-value">{{ disputeRate }}%</div>
                <div class="rate-label">化解率</div>
              </div>
            </div>
          </div>
          <div class="rate-details">
            <div class="detail-item">
              <div class="detail-value success">{{ governance.disputeResolved || 0 }}</div>
              <div class="detail-label">已化解</div>
            </div>
            <div class="detail-item">
              <div class="detail-value warning">{{ governance.disputeCount - (governance.disputeResolved || 0) }}</div>
              <div class="detail-label">待化解</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">安全隐患统计</div>
      <div class="chart-container">
        <div class="hazard-bar-chart">
          <div v-for="(item, index) in hazardDistribution" :key="index" class="hazard-bar-item">
            <div class="hazard-bar-label">{{ item.label }}</div>
            <div class="hazard-bar-track">
              <div class="hazard-bar-fill" :style="{ width: Math.min(item.count * 10, 100) + '%' }"></div>
            </div>
            <div class="hazard-bar-value">{{ item.count }}项</div>
          </div>
        </div>
        <div class="hazard-summary">
          <div class="summary-item">
            <span class="summary-label">隐患总数</span>
            <span class="summary-value danger">{{ safety.hazardCount || 0 }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">已整改</span>
            <span class="summary-value success">{{ safety.hazardDone || 0 }}</span>
          </div>
          <div class="summary-item">
            <span class="summary-label">整改率</span>
            <span class="summary-value highlight">{{ safety.hazardCount ? Math.round((safety.hazardDone / safety.hazardCount) * 100) : 0 }}%</span>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">党务工作</div>
      <div class="data-row">
        <div class="data-item">
          <span class="data-label">党组织活动</span>
          <span class="data-value">{{ party.activityCount || 0 }}次</span>
        </div>
        <div class="data-item">
          <span class="data-label">党员积分达标</span>
          <span class="data-value">{{ party.scorePass || 0 }}人</span>
        </div>
      </div>
      <div class="data-row">
        <div class="data-item">
          <span class="data-label">三会一课</span>
          <span class="data-value">{{ party.meetingCount || 0 }}次</span>
        </div>
        <div class="data-item">
          <span class="data-label">主题党日</span>
          <span class="data-value">{{ party.themeDayCount || 0 }}次</span>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">便民服务</div>
      <div class="data-row">
        <div class="data-item">
          <span class="data-label">诉求办理</span>
          <span class="data-value">{{ service.demandCount || 0 }}件</span>
        </div>
        <div class="data-item">
          <span class="data-label">民生代办</span>
          <span class="data-value">{{ service.convenienceCount || 0 }}件</span>
        </div>
      </div>
      <div class="data-row">
        <div class="data-item">
          <span class="data-label">办事预约</span>
          <span class="data-value">{{ service.appointmentCount || 0 }}次</span>
        </div>
        <div class="data-item">
          <span class="data-label">特殊群体</span>
          <span class="data-value">{{ service.specialGroupCount || 0 }}人</span>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">任务管理</div>
      <div class="task-tabs">
        <div 
          v-for="tab in taskTabs" 
          :key="tab.key" 
          class="task-tab"
          :class="{ active: activeTaskTab === tab.key }"
          @click="activeTaskTab = tab.key"
        >
          <span class="tab-text">{{ tab.text }}</span>
          <span class="tab-count" :class="tab.color">{{ getTaskCount(tab.key) }}</span>
        </div>
      </div>
      <div class="task-list">
        <div v-for="task in filteredTasks" :key="task.id" class="task-item">
          <div class="task-priority" :class="task.priority"></div>
          <div class="task-content">
            <div class="task-title">{{ task.title }}</div>
            <div class="task-meta">
              <span>{{ task.assignee }}</span>
              <span>{{ task.deadline }}</span>
            </div>
          </div>
          <div class="task-status" :class="task.status">{{ getTaskStatusLabel(task.status) }}</div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">风险预警</div>
      <div class="risk-list">
        <div v-for="risk in risks" :key="risk.id" class="risk-item" :class="risk.level">
          <div class="risk-icon">{{ risk.icon }}</div>
          <div class="risk-content">
            <div class="risk-title">{{ risk.title }}</div>
            <div class="risk-desc">{{ risk.description }}</div>
            <div class="risk-meta">
              <span>{{ risk.time }}</span>
              <span>{{ risk.location }}</span>
            </div>
          </div>
          <van-icon name="arrow-right" size="16" color="#999" />
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">月度工作统计</div>
      <div class="chart-container">
        <div class="line-chart">
          <div class="chart-grid">
            <div class="grid-line"></div>
            <div class="grid-line"></div>
            <div class="grid-line"></div>
            <div class="grid-line"></div>
          </div>
          <div class="chart-bars">
            <div v-for="(item, index) in monthlyData" :key="index" class="chart-bar-item">
              <div class="bar-wrapper">
                <div class="bar-fill" :style="{ height: item.value + '%' }"></div>
              </div>
              <div class="bar-label">{{ item.month }}</div>
            </div>
          </div>
        </div>
        <div class="monthly-legend">
          <div class="legend-item">
            <span class="legend-color"></span>
            <span class="legend-text">诉求办理数</span>
          </div>
        </div>
      </div>
    </div>

    <van-dialog v-model:show="shareVisible" title="截图保存">
      <div class="share-content">
        <img :src="screenshotData" class="share-image" v-if="screenshotData" />
        <div v-else class="share-placeholder">截图准备中...</div>
      </div>
      <template #footer>
        <van-button type="primary" @click="downloadScreenshot">保存图片</van-button>
        <van-button @click="shareScreenshot">分享给好友</van-button>
      </template>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const currentTime = ref('')
const shareVisible = ref(false)
const screenshotData = ref('')
let timer = null

const circumference = 2 * Math.PI * 50

const villageInfo = ref({})
const overview = reactive({
  population: 8849,
  household: 3072,
  partyMember: 318,
  groupCount: 107
})
const party = reactive({ activityCount: 44, scorePass: 11, meetingCount: 102, themeDayCount: 66 })
const village = reactive({ meetingCount: 5, documentCount: 5, taskCount: 5, completionRate: 80 })
const service = reactive({ demandCount: 0, convenienceCount: 3, appointmentCount: 0, specialGroupCount: 0 })
const safety = reactive({ hazardCount: 97, hazardDone: 91, fireCheckCount: 58, foodCheckCount: 5 })
const governance = reactive({ disputeCount: 31, disputeResolved: 31, forestPatrol: 3, toiletReform: 0 })

const activeTaskTab = ref('all')

const taskTabs = [
  { key: 'all', text: '全部', color: 'all' },
  { key: 'pending', text: '待处理', color: 'warning' },
  { key: 'processing', text: '处理中', color: 'primary' },
  { key: 'completed', text: '已完成', color: 'success' }
]

const tasks = ref([
  { id: 1, title: '交通安全整治', assignee: '李主任', deadline: '2026-07-25', priority: 'high', status: 'processing' },
  { id: 2, title: '柑橘品改推进', assignee: '王主任', deadline: '2026-08-10', priority: 'high', status: 'processing' },
  { id: 3, title: '防汛备汛工作', assignee: '张书记', deadline: '2026-07-20', priority: 'high', status: 'completed' },
  { id: 4, title: '地质灾害巡查', assignee: '刘委员', deadline: '2026-07-28', priority: 'medium', status: 'pending' },
  { id: 5, title: '环境卫生整治', assignee: '赵主任', deadline: '2026-07-30', priority: 'low', status: 'pending' }
])

const risks = ref([
  { id: 1, title: '地质灾害风险', description: '5处地质灾害隐患点需重点监测', level: 'high', icon: '⛰️', time: '2026-07-18', location: '全乡范围' },
  { id: 2, title: '森林防火预警', description: '清明期间需加强火源管控', level: 'high', icon: '🔥', time: '2026-07-17', location: '各林区' },
  { id: 3, title: '道路交通安全', description: '山多路弯，需加强交通劝导', level: 'medium', icon: '🚗', time: '2026-07-16', location: '全乡道路' },
  { id: 4, title: '防汛备汛提醒', description: '7座水库需定期检查', level: 'medium', icon: '💧', time: '2026-07-15', location: '全乡水库' }
])

const monthlyData = ref([
  { month: '1月', value: 65 },
  { month: '2月', value: 45 },
  { month: '3月', value: 80 },
  { month: '4月', value: 55 },
  { month: '5月', value: 90 },
  { month: '6月', value: 75 },
  { month: '7月', value: 85 }
])

const disputeRate = computed(() => {
  if (!governance.disputeCount) return 0
  return Math.round((governance.disputeResolved / governance.disputeCount) * 100)
})

const rateOffset = computed(() => {
  return circumference - (disputeRate.value / 100) * circumference
})

const subsidyTotal = computed(() => {
  return subsidyDistribution.reduce((sum, item) => sum + item.value, 0)
})

const filteredTasks = computed(() => {
  if (activeTaskTab.value === 'all') return tasks.value
  return tasks.value.filter(task => task.status === activeTaskTab.value)
})

const getTaskCount = (status) => {
  if (status === 'all') return tasks.value.length
  return tasks.value.filter(task => task.status === status).length
}

const getTaskStatusLabel = (status) => {
  const labels = { pending: '待处理', processing: '处理中', completed: '已完成' }
  return labels[status] || status
}

const ageDistribution = ref([
  { label: '0-18岁', value: 1858, percent: 21, color: 'red' },
  { label: '18-60岁', value: 5309, percent: 60, color: 'gold' },
  { label: '60岁以上', value: 1682, percent: 19, color: 'blue' }
])

const subsidyDistribution = ref([
  { label: '低保补贴', value: 60.996, percent: 58, color: '#C8102E' },
  { label: '残疾补贴', value: 14.982, percent: 14, color: '#FFD700' },
  { label: '耕地补贴', value: 18.0, percent: 17, color: '#409EFF' },
  { label: '高龄补贴', value: 5.0, percent: 5, color: '#67C23A' },
  { label: '其他补贴', value: 6.0, percent: 6, color: '#909399' }
])

const hazardDistribution = ref([
  { label: '综合隐患', count: 97 },
  { label: '消防安全', count: 58 },
  { label: '道路交通', count: 12 },
  { label: '地质灾害', count: 5 }
])

const onBack = () => { goBack(router) }

const getPieStyle = (percent, index) => {
  const colors = ['#C8102E', '#FFD700', '#409EFF', '#67C23A', '#909399']
  let startAngle = 0
  for (let i = 0; i < index; i++) {
    startAngle += subsidyDistribution.value[i].percent * 3.6
  }
  const endAngle = startAngle + percent * 3.6
  return {
    background: `conic-gradient(${colors[index]} ${startAngle}deg ${endAngle}deg, transparent ${endAngle}deg ${startAngle + 360}deg)`
  }
}

const updateTime = () => {
  const date = new Date()
  currentTime.value = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`
}

const captureScreen = async () => {
  showToast({ message: '正在生成截图...', duration: 2000 })
  setTimeout(() => {
    const canvas = document.createElement('canvas')
    const page = document.querySelector('.data-screen-page')
    if (!page) {
      showToast('截图失败')
      return
    }
    const width = page.scrollWidth
    const height = page.scrollHeight
    canvas.width = width
    canvas.height = height
    const ctx = canvas.getContext('2d')
    ctx.fillStyle = '#FDF5F5'
    ctx.fillRect(0, 0, width, height)
    screenshotData.value = canvas.toDataURL('image/png')
    shareVisible.value = true
  }, 1000)
}

const downloadScreenshot = () => {
  if (!screenshotData.value) return
  const link = document.createElement('a')
  link.href = screenshotData.value
  link.download = `罗卜田乡数据大屏_${new Date().getTime()}.png`
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  showToast('图片已保存')
}

const shareScreenshot = () => {
  if (!screenshotData.value) return
  const shareText = '罗卜田乡综合数据大屏'
  if (navigator.share) {
    navigator.share({
      title: '罗卜田乡数据大屏',
      text: shareText,
      url: window.location.href
    }).catch(() => {
      navigator.clipboard.writeText(shareText + '\n' + window.location.href)
      showToast('链接已复制')
    })
  } else {
    navigator.clipboard.writeText(shareText + '\n' + window.location.href)
    showToast('链接已复制')
  }
}

const fetchData = async () => {
  try {
    const res = await request.get('/data-screen/overview')
    const data = res.data || {}
    villageInfo.value = data.villageInfo || {}
    Object.assign(overview, data.overview || {})
    Object.assign(party, data.party || {})
    Object.assign(village, data.village || {})
    Object.assign(service, data.service || {})
    Object.assign(safety, data.safety || {})
    Object.assign(governance, data.governance || {})
  } catch (error) {
    console.error('获取大屏数据失败', error)
    overview.population = 620
    overview.household = 185
    overview.partyMember = 45
    overview.groupCount = 5
    party.activityCount = 12
    party.scorePass = 40
    party.meetingCount = 8
    party.themeDayCount = 6
    service.demandCount = 32
    service.convenienceCount = 18
    service.appointmentCount = 25
    service.specialGroupCount = 12
    safety.hazardCount = 18
    safety.hazardDone = 15
    governance.disputeCount = 10
    governance.disputeResolved = 8
  }
}

onMounted(() => {
  updateTime()
  fetchData()
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.data-screen-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: linear-gradient(180deg, #D22630 0%, #B01A26 30%, #f5f5f5 30%);
}

.screen-header {
  text-align: center;
  padding: 16px;
  color: #FFD700;
}

.village-name {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 6px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.update-time {
  font-size: 12px;
  opacity: 0.85;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin: 0 12px 12px;
}

.overview-item {
  background: #fff;
  border-radius: 14px;
  padding: 18px 12px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.overview-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 4px;
}

.overview-item.red .overview-value {
  color: #D22630;
}

.overview-item.gold .overview-value {
  color: #E5C100;
}

.overview-label {
  font-size: 13px;
  color: #666;
}

.card {
  background: #fff;
  margin: 0 12px 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 12px;
  padding-left: 8px;
  border-left: 3px solid #FFD700;
}

.chart-container {
  width: 100%;
}

.bar-chart {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.bar-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.bar-label {
  width: 70px;
  font-size: 12px;
  color: #666;
  flex-shrink: 0;
}

.bar-track {
  flex: 1;
  height: 20px;
  background: #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  border-radius: 10px;
  transition: width 0.5s ease;
}

.bar-fill.red {
  background: linear-gradient(90deg, #C8102E, #D22630);
}

.bar-fill.gold {
  background: linear-gradient(90deg, #FFD700, #E5C100);
}

.bar-fill.blue {
  background: linear-gradient(90deg, #409EFF, #66B1FF);
}

.bar-value {
  width: 60px;
  font-size: 12px;
  font-weight: bold;
  color: #333;
  text-align: right;
  flex-shrink: 0;
}

.pie-chart-wrapper {
  display: flex;
  align-items: center;
  gap: 16px;
}

.pie-chart {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: #f5f5f5;
}

.pie-segment {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.pie-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 70px;
  height: 70px;
  background: #fff;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.pie-total {
  font-size: 16px;
  font-weight: bold;
  color: #D22630;
}

.pie-label {
  font-size: 10px;
  color: #999;
}

.pie-legend {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.legend-dot {
  width: 12px;
  height: 12px;
  border-radius: 3px;
}

.legend-text {
  flex: 1;
  font-size: 12px;
  color: #666;
}

.legend-value {
  font-size: 12px;
  font-weight: bold;
  color: #333;
}

.rate-display {
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.rate-circle {
  flex-shrink: 0;
}

.rate-ring {
  position: relative;
  width: 120px;
  height: 120px;
}

.progress-ring {
  transform: rotate(-90deg);
}

.ring-progress {
  transition: stroke-dashoffset 0.5s ease;
}

.rate-inner {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.rate-value {
  font-size: 24px;
  font-weight: bold;
  color: #D22630;
}

.rate-label {
  font-size: 11px;
  color: #999;
}

.rate-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-item {
  text-align: center;
}

.detail-value {
  font-size: 20px;
  font-weight: bold;
  display: block;
}

.detail-value.success {
  color: #07c160;
}

.detail-value.warning {
  color: #e6a23c;
}

.detail-label {
  font-size: 12px;
  color: #999;
}

.hazard-bar-chart {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 16px;
}

.hazard-bar-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.hazard-bar-label {
  width: 70px;
  font-size: 12px;
  color: #666;
}

.hazard-bar-track {
  flex: 1;
  height: 16px;
  background: #f0f0f0;
  border-radius: 8px;
  overflow: hidden;
}

.hazard-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #C8102E, #D22630);
  border-radius: 8px;
  transition: width 0.5s ease;
}

.hazard-bar-value {
  width: 40px;
  font-size: 12px;
  font-weight: bold;
  color: #D22630;
  text-align: right;
}

.hazard-summary {
  display: flex;
  justify-content: space-around;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.summary-item {
  text-align: center;
}

.summary-label {
  font-size: 12px;
  color: #999;
  display: block;
  margin-bottom: 4px;
}

.summary-value {
  font-size: 18px;
  font-weight: bold;
}

.summary-value.danger {
  color: #D22630;
}

.summary-value.success {
  color: #07c160;
}

.summary-value.highlight {
  color: #D22630;
}

.data-row {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
}

.data-row:last-child {
  margin-bottom: 0;
}

.data-item {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: #fafafa;
  border-radius: 8px;
}

.data-label {
  font-size: 13px;
  color: #666;
}

.data-value {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
}

.data-value.highlight {
  color: #D22630;
}

.data-value.success {
  color: #07c160;
}

.share-content {
  padding: 8px 0;
}

.share-image {
  width: 100%;
  max-height: 400px;
  object-fit: contain;
  border-radius: 8px;
}

.share-placeholder {
  text-align: center;
  padding: 40px;
  color: #999;
}

.task-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.task-tab {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 8px 12px;
  background: #f5f5f5;
  border-radius: 20px;
  font-size: 13px;
  transition: all 0.25s;
}

.task-tab.active {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  color: #fff;
}

.tab-count {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  background: rgba(0,0,0,0.1);
}

.task-tab.active .tab-count {
  background: rgba(255,255,255,0.2);
}

.tab-count.warning { background: #fef08a; color: #ca8a04; }
.tab-count.primary { background: #dbeafe; color: #1d4ed8; }
.tab-count.success { background: #bbf7d0; color: #166534; }
.tab-count.all { background: #f3f4f6; color: #374151; }

.task-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.task-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #fafafa;
  border-radius: 10px;
  gap: 10px;
}

.task-priority {
  width: 6px;
  height: 40px;
  border-radius: 3px;
  flex-shrink: 0;
}

.task-priority.high { background: #D22630; }
.task-priority.medium { background: #e6a23c; }
.task-priority.low { background: #909399; }

.task-content {
  flex: 1;
  min-width: 0;
}

.task-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.task-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #999;
}

.task-status {
  font-size: 11px;
  padding: 4px 10px;
  border-radius: 12px;
}

.task-status.pending { background: #fef08a; color: #ca8a04; }
.task-status.processing { background: #dbeafe; color: #1d4ed8; }
.task-status.completed { background: #bbf7d0; color: #166534; }

.risk-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.risk-item {
  display: flex;
  align-items: center;
  padding: 14px;
  background: #fafafa;
  border-radius: 12px;
  border-left: 4px solid;
}

.risk-item.high { border-left-color: #D22630; }
.risk-item.medium { border-left-color: #e6a23c; }
.risk-item.low { border-left-color: #909399; }

.risk-icon {
  font-size: 24px;
  margin-right: 12px;
}

.risk-content {
  flex: 1;
}

.risk-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.risk-desc {
  font-size: 12px;
  color: #666;
  margin-bottom: 6px;
}

.risk-meta {
  display: flex;
  gap: 12px;
  font-size: 11px;
  color: #999;
}

.line-chart {
  position: relative;
  height: 150px;
}

.chart-grid {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 24px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.grid-line {
  height: 1px;
  background: #f0f0f0;
}

.chart-bars {
  position: absolute;
  bottom: 24px;
  left: 0;
  right: 0;
  height: calc(100% - 24px);
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
}

.chart-bar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
}

.bar-wrapper {
  width: 24px;
  height: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.bar-fill {
  width: 100%;
  background: linear-gradient(180deg, #D22630 0%, #B01A26 100%);
  border-radius: 4px 4px 0 0;
  transition: height 0.5s ease;
}

.bar-label {
  font-size: 11px;
  color: #999;
}

.monthly-legend {
  display: flex;
  justify-content: center;
  margin-top: 12px;
}

.monthly-legend .legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.monthly-legend .legend-color {
  width: 16px;
  height: 16px;
  background: linear-gradient(180deg, #D22630 0%, #B01A26 100%);
  border-radius: 4px;
}

.monthly-legend .legend-text {
  font-size: 12px;
  color: #666;
}

@media (min-width: 768px) {
  .overview-grid {
    grid-template-columns: repeat(4, 1fr);
  }
  
  .pie-chart-wrapper {
    justify-content: center;
  }
}
</style>
