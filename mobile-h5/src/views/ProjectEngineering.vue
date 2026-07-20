<template>
  <div class="project-page page-container">
    <van-nav-bar title="项目工程" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="在建项目" name="ongoing" />
      <van-tab title="完工项目" name="completed" />
      <van-tab title="合同档案" name="contract" />
      <van-tab title="工程款公示" name="payment" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'ongoing'">
      <div class="card-list">
        <div v-for="item in ongoingProjects" :key="item.id" class="project-card">
          <div class="project-header">
            <span class="project-tag">在建</span>
            <span class="project-progress">{{ item.progress }}%</span>
          </div>
          <div class="project-title">{{ item.name }}</div>
          <div class="project-info">
            <div class="info-row">
              <span>施工单位</span>
              <span>{{ item.company }}</span>
            </div>
            <div class="info-row">
              <span>工期</span>
              <span>{{ item.startDate }} - {{ item.endDate }}</span>
            </div>
            <div class="info-row">
              <span>投资金额</span>
              <span>¥{{ item.amount }}</span>
            </div>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: item.progress + '%' }"></div>
          </div>
          <van-button v-if="isOfficial" size="mini" @click="updateProgress(item)">更新进度</van-button>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'completed'">
      <div class="card-list">
        <div v-for="item in completedProjects" :key="item.id" class="project-card">
          <div class="project-header">
            <span class="project-tag completed">已完工</span>
          </div>
          <div class="project-title">{{ item.name }}</div>
          <div class="project-info">
            <div class="info-row">
              <span>施工单位</span>
              <span>{{ item.company }}</span>
            </div>
            <div class="info-row">
              <span>完工时间</span>
              <span>{{ item.completeDate }}</span>
            </div>
            <div class="info-row">
              <span>验收结果</span>
              <span>{{ item.acceptance }}</span>
            </div>
          </div>
          <div class="project-photos">
            <img v-for="(photo, idx) in item.photos" :key="idx" :src="photo" class="photo-item" />
          </div>
          <van-button size="mini" @click="viewDetail(item)">查看详情</van-button>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'contract'">
      <div class="card-list">
        <div v-for="item in contractList" :key="item.id" class="contract-card">
          <div class="contract-header">
            <div class="contract-icon">📄</div>
            <div class="contract-title">{{ item.name }}</div>
          </div>
          <div class="contract-info">
            <div class="info-row">
              <span>合同编号</span>
              <span>{{ item.code }}</span>
            </div>
            <div class="info-row">
              <span>签订日期</span>
              <span>{{ item.signDate }}</span>
            </div>
            <div class="info-row">
              <span>金额</span>
              <span>¥{{ item.amount }}</span>
            </div>
          </div>
          <div class="contract-actions">
            <van-button size="mini" @click="previewContract(item)">预览</van-button>
            <van-button size="mini" @click="downloadContract(item)">下载</van-button>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'payment'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">💰 工程款支付明细</div>
        </div>
        <div class="payment-list">
          <div v-for="item in paymentList" :key="item.id" class="payment-item">
            <div class="payment-header">
              <span class="payment-project">{{ item.projectName }}</span>
              <span class="payment-status">{{ item.status }}</span>
            </div>
            <div class="payment-info">
              <div class="info-row">
                <span>支付金额</span>
                <span class="payment-amount">¥{{ item.amount }}</span>
              </div>
              <div class="info-row">
                <span>支付时间</span>
                <span>{{ item.payDate }}</span>
              </div>
              <div class="info-row">
                <span>资金来源</span>
                <span>{{ item.source }}</span>
              </div>
            </div>
            <div v-if="item.voucher" class="voucher-link" @click="viewVoucher(item)">查看凭证</div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📊 支付统计</div>
        </div>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">¥{{ totalPayment }}</div>
            <div class="stat-label">累计支付</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ paymentCount }}</div>
            <div class="stat-label">支付次数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ projectCount }}</div>
            <div class="stat-label">涉及项目</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const activeTab = ref('ongoing')
const isOfficial = true

const ongoingProjects = ref([
  { id: 1, name: '罗卜田乡道路硬化工程', company: 'XX建筑公司', startDate: '2024-01-01', endDate: '2024-06-30', amount: 1200000, progress: 65 },
  { id: 2, name: '村文化广场建设', company: 'XX市政公司', startDate: '2024-02-15', endDate: '2024-08-31', amount: 800000, progress: 40 },
  { id: 3, name: '农田水利灌溉工程', company: 'XX水利公司', startDate: '2024-03-01', endDate: '2024-09-30', amount: 600000, progress: 25 }
])

const completedProjects = ref([
  { id: 1, name: '罗卜田乡饮水安全工程', company: 'XX水务公司', completeDate: '2023-12-20', acceptance: '验收合格', photos: [] },
  { id: 2, name: '村内环境整治工程', company: 'XX环保公司', completeDate: '2023-11-15', acceptance: '验收合格', photos: [] }
])

const contractList = ref([
  { id: 1, name: '道路硬化工程合同', code: 'HT-2024-001', signDate: '2024-01-01', amount: 1200000 },
  { id: 2, name: '文化广场建设合同', code: 'HT-2024-002', signDate: '2024-02-15', amount: 800000 },
  { id: 3, name: '招投标文件', code: 'ZB-2024-001', signDate: '2023-12-20', amount: 0 }
])

const paymentList = ref([
  { id: 1, projectName: '道路硬化工程', amount: 360000, payDate: '2024-01-15', source: '财政资金', status: '已支付', voucher: true },
  { id: 2, projectName: '道路硬化工程', amount: 360000, payDate: '2024-03-20', source: '财政资金', status: '已支付', voucher: true },
  { id: 3, projectName: '文化广场建设', amount: 240000, payDate: '2024-02-20', source: '财政资金', status: '已支付', voucher: true }
])

const totalPayment = computed(() => {
  return paymentList.value.reduce((sum, item) => sum + item.amount, 0).toLocaleString()
})

const paymentCount = computed(() => paymentList.value.length)
const projectCount = computed(() => new Set(paymentList.value.map(item => item.projectName)).size)

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'ongoing') {
      const res = await request.get('/project/ongoing')
      ongoingProjects.value = res.data || ongoingProjects.value
    } else if (activeTab.value === 'completed') {
      const res = await request.get('/project/completed')
      completedProjects.value = res.data || completedProjects.value
    } else if (activeTab.value === 'contract') {
      const res = await request.get('/project/contract')
      contractList.value = res.data || contractList.value
    } else if (activeTab.value === 'payment') {
      const res = await request.get('/project/payment')
      paymentList.value = res.data || paymentList.value
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const updateProgress = (item) => {
  showToast(`更新 ${item.name} 进度功能开发中`)
}

const viewDetail = (item) => {
  showToast(`查看 ${item.name} 详情功能开发中`)
}

const previewContract = (item) => {
  showToast(`预览 ${item.name} 功能开发中`)
}

const downloadContract = (item) => {
  showToast(`下载 ${item.name} 功能开发中`)
}

const viewVoucher = (item) => {
  showToast('查看凭证功能开发中')
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.project-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.card {
  background: #fff;
  margin: 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  margin-bottom: 14px;
}

.header-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
}

.card-list {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.project-card, .contract-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.project-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.project-tag.completed {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.project-progress {
  font-size: 14px;
  font-weight: bold;
  color: #D22630;
}

.project-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
}

.project-info {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
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

.info-row span:first-child {
  color: #999;
}

.info-row span:last-child {
  color: #333;
}

.progress-bar {
  height: 8px;
  background: #eee;
  border-radius: 4px;
  margin: 12px 0;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #D22630, #F57C00);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.project-photos {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.photo-item {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  object-fit: cover;
}

.contract-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.contract-icon {
  font-size: 24px;
  margin-right: 10px;
}

.contract-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.contract-info {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 12px;
}

.contract-actions {
  display: flex;
  gap: 10px;
}

.payment-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.payment-item {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
}

.payment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.payment-project {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.payment-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.payment-info {
  margin-bottom: 8px;
}

.payment-amount {
  font-size: 16px;
  font-weight: bold;
  color: #D22630;
}

.voucher-link {
  font-size: 13px;
  color: #2196F3;
  text-align: right;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.stat-item {
  text-align: center;
  padding: 16px 10px;
  background: #fafafa;
  border-radius: 10px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #999;
}
</style>
