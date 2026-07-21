<template>
  <div class="project-page page-container">
    <van-nav-bar title="项目工程" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="在建项目" name="ongoing" />
      <van-tab title="完工项目" name="completed" />
      <van-tab title="合同档案" name="contract" />
      <van-tab title="工程款公示" name="payment" />
    </van-tabs>

    <div v-if="activeTab === 'ongoing'" class="table-section">
      <ExcelTable 
        :data="ongoingProjects" 
        :columns="ongoingColumns" 
        export-filename="在建项目.xlsx"
      />
    </div>

    <div v-else-if="activeTab === 'completed'" class="table-section">
      <ExcelTable 
        :data="completedProjects" 
        :columns="completedColumns" 
        export-filename="完工项目.xlsx"
      />
    </div>

    <div v-else-if="activeTab === 'contract'" class="table-section">
      <ExcelTable 
        :data="contractList" 
        :columns="contractColumns" 
        export-filename="合同档案.xlsx"
      />
    </div>

    <div v-else-if="activeTab === 'payment'">
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

      <div class="table-section">
        <ExcelTable 
          :data="paymentList" 
          :columns="paymentColumns" 
          export-filename="工程款支付明细.xlsx"
        />
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
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const activeTab = ref('ongoing')
const isOfficial = true

const ongoingColumns = [
  { key: 'name', title: '项目名称' },
  { key: 'company', title: '施工单位' },
  { key: 'startDate', title: '开工时间' },
  { key: 'endDate', title: '竣工时间' },
  { key: 'amount', title: '投资金额', formatter: (val) => `¥${val?.toLocaleString() || 0}` },
  { key: 'progress', title: '进度', formatter: (val) => `${val || 0}%` }
]

const completedColumns = [
  { key: 'name', title: '项目名称' },
  { key: 'company', title: '施工单位' },
  { key: 'completeDate', title: '完工时间' },
  { key: 'acceptance', title: '验收结果' }
]

const contractColumns = [
  { key: 'name', title: '合同名称' },
  { key: 'code', title: '合同编号' },
  { key: 'signDate', title: '签订日期' },
  { key: 'amount', title: '金额', formatter: (val) => `¥${val?.toLocaleString() || 0}` }
]

const paymentColumns = [
  { key: 'projectName', title: '项目名称' },
  { key: 'amount', title: '支付金额', formatter: (val) => `¥${val?.toLocaleString() || 0}` },
  { key: 'payDate', title: '支付时间' },
  { key: 'source', title: '资金来源' },
  { key: 'status', title: '状态' }
]

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



onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.project-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.table-section {
  margin: 10px 12px;
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
