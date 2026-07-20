<template>
  <div class="rural-revitalization-page page-container">
    <van-nav-bar title="乡村振兴" left-arrow @click-left="handleBack" />

    <div class="stats-card">
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalProjects }}</div>
        <div class="stat-label">振兴项目</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalInvestment }}</div>
        <div class="stat-label">总投资(万元)</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalHouseholds }}</div>
        <div class="stat-label">帮扶户数</div>
      </div>
    </div>

    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="帮扶项目" name="project">
        <div class="card-list">
          <div v-for="item in projectList" :key="item.id" class="project-card" @click="goDetail(item.id)">
            <div class="card-header">
              <div class="card-icon">{{ item.icon }}</div>
              <div class="card-title">{{ item.title }}</div>
              <span class="card-tag" :class="item.status">{{ item.status }}</span>
            </div>
            <div class="card-info">
              <div class="info-row">
                <span class="info-label">项目类型</span>
                <span class="info-value">{{ item.type }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">投资金额</span>
                <span class="info-value highlight">{{ item.investment }}万元</span>
              </div>
              <div class="info-row">
                <span class="info-label">受益户数</span>
                <span class="info-value">{{ item.benefitHouseholds }}户</span>
              </div>
              <div class="info-row">
                <span class="info-label">实施时间</span>
                <span class="info-value">{{ item.implementDate }}</span>
              </div>
            </div>
            <div class="card-desc">{{ item.description }}</div>
            <div class="card-footer">
              <span class="footer-text">查看详情</span>
              <van-icon name="arrow-right" size="16" color="#999" />
            </div>
          </div>
        </div>
      </van-tab>

      <van-tab title="帮扶户" name="household">
        <div class="card-list">
          <div v-for="item in householdList" :key="item.id" class="household-card" @click="goHouseholdDetail(item.id)">
            <div class="card-header">
              <div class="card-avatar">{{ item.name.charAt(0) }}</div>
              <div class="card-info">
                <div class="card-title">{{ item.name }}</div>
                <div class="card-subtitle">{{ item.group }} · {{ item.category }}</div>
              </div>
              <span class="card-tag" :class="item.status">{{ item.status }}</span>
            </div>
            <div class="card-detail">
              <div class="detail-row">
                <span>家庭人口</span>
                <span>{{ item.familyCount }}人</span>
              </div>
              <div class="detail-row">
                <span>帮扶措施</span>
                <span>{{ item.measures }}</span>
              </div>
              <div class="detail-row">
                <span>年度收入</span>
                <span>{{ item.annualIncome }}元</span>
              </div>
            </div>
          </div>
        </div>
      </van-tab>

      <van-tab title="驻村工作队" name="team">
        <div class="card-list">
          <div v-for="item in teamList" :key="item.id" class="team-card">
            <div class="card-header">
              <div class="card-avatar">{{ item.name.charAt(0) }}</div>
              <div class="card-info">
                <div class="card-title">{{ item.name }}</div>
                <div class="card-subtitle">{{ item.position }}</div>
              </div>
            </div>
            <div class="card-detail">
              <div class="detail-row">
                <span>派出单位</span>
                <span>{{ item.unit }}</span>
              </div>
              <div class="detail-row">
                <span>联系电话</span>
                <span>{{ item.phone }}</span>
              </div>
              <div class="detail-row">
                <span>驻村时间</span>
                <span>{{ item.startDate }} - {{ item.endDate }}</span>
              </div>
            </div>
            <div class="card-actions">
              <van-button size="mini" type="primary" @click="callPhone(item.phone)">拨打电话</van-button>
            </div>
          </div>
        </div>
      </van-tab>
    </van-tabs>

    <div v-if="isOfficial" class="action-bar">
      <van-button type="primary" icon="plus" @click="showAddModal = true">新增帮扶项目</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const activeTab = ref('project')
const isOfficial = true
const showAddModal = ref(false)

const stats = reactive({
  totalProjects: 8,
  totalInvestment: 1560,
  totalHouseholds: 45
})

const projectList = ref([
  { id: 1, title: '特色农产品种植基地', type: '产业帮扶', investment: 320, benefitHouseholds: 20, implementDate: '2024-03', status: '进行中', icon: '🌾', description: '建设现代化温室大棚，种植有机蔬菜和特色水果' },
  { id: 2, title: '乡村旅游基础设施建设', type: '基础设施', investment: 450, benefitHouseholds: 30, implementDate: '2024-01', status: '进行中', icon: '🛣️', description: '修建旅游步道、停车场、观景台等设施' },
  { id: 3, title: '农村饮水安全工程', type: '民生工程', investment: 180, benefitHouseholds: 15, implementDate: '2024-06', status: '已完成', icon: '💧', description: '改造升级全村饮水管网，保障饮水安全' },
  { id: 4, title: '电商扶贫项目', type: '产业帮扶', investment: 200, benefitHouseholds: 18, implementDate: '2024-04', status: '进行中', icon: '🛒', description: '建设农村电商服务站，帮助村民销售农产品' },
  { id: 5, title: '道路硬化工程', type: '基础设施', investment: 410, benefitHouseholds: 45, implementDate: '2024-02', status: '已完成', icon: '🚧', description: '硬化村内主干道和巷道，改善出行条件' }
])

const householdList = ref([
  { id: 1, name: '张某某', group: '第一村民组', category: '低保户', familyCount: 3, measures: '产业帮扶+医疗救助', annualIncome: 28500, status: '已脱贫' },
  { id: 2, name: '李某某', group: '第二村民组', category: '特困户', familyCount: 2, measures: '兜底保障+就业帮扶', annualIncome: 22000, status: '已脱贫' },
  { id: 3, name: '王某某', group: '第三村民组', category: '一般贫困户', familyCount: 4, measures: '产业帮扶+教育资助', annualIncome: 35000, status: '已脱贫' },
  { id: 4, name: '赵某某', group: '第一村民组', category: '边缘易致贫户', familyCount: 5, measures: '就业帮扶+产业带动', annualIncome: 42000, status: '监测中' }
])

const teamList = ref([
  { id: 1, name: '陈书记', position: '驻村第一书记', unit: '县组织部', phone: '138****1234', startDate: '2023-01', endDate: '2025-12' },
  { id: 2, name: '刘主任', position: '驻村工作队员', unit: '县农业农村局', phone: '139****5678', startDate: '2023-01', endDate: '2025-12' },
  { id: 3, name: '王技术员', position: '驻村工作队员', unit: '县科技局', phone: '137****9012', startDate: '2023-06', endDate: '2025-12' }
])

const handleBack = () => {
  goBack(router)
}

const goDetail = (id) => {
  router.push(`/rural-revitalization-detail/${id}`)
}

const goHouseholdDetail = (id) => {
  router.push(`/rural-revitalization-detail/${id}?type=household`)
}

const callPhone = (phone) => {
  showToast(`拨打电话: ${phone}`)
}

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'project') {
      const res = await request.get('/rural-revitalization/projects')
      projectList.value = res.data?.records || projectList.value
    } else if (activeTab.value === 'household') {
      const res = await request.get('/rural-revitalization/households')
      householdList.value = res.data?.records || householdList.value
    } else if (activeTab.value === 'team') {
      const res = await request.get('/rural-revitalization/team')
      teamList.value = res.data?.records || teamList.value
    }
  } catch (error) {
    console.error('获取乡村振兴数据失败', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.rural-revitalization-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.stats-card {
  display: flex;
  background: #fff;
  margin: 12px;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #D22630;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.card-list {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.project-card, .household-card, .team-card {
  background: #fff;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.card-icon {
  font-size: 32px;
  margin-right: 12px;
}

.card-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #D22630, #B01A26);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  margin-right: 12px;
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.card-subtitle {
  font-size: 12px;
  color: #999;
}

.card-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.card-tag.进行中 {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.card-tag.已完成 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.card-tag.监测中 {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.card-tag.已脱贫 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.card-info {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
  margin-bottom: 10px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 13px;
  border-bottom: 1px solid #eee;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  color: #999;
}

.info-value {
  color: #333;
}

.info-value.highlight {
  color: #D22630;
  font-weight: bold;
}

.card-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 12px;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #D22630;
}

.card-detail {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 13px;
  border-bottom: 1px solid #eee;
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-row span:first-child {
  color: #999;
}

.detail-row span:last-child {
  color: #333;
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.action-bar {
  padding: 16px 12px;
}

.action-bar .van-button {
  width: 100%;
}
</style>