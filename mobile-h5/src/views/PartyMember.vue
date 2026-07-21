
<template>
  <div class="party-member-page page-container">
    <van-nav-bar title="党员管理" left-arrow @click-left="onBack" />

    <div class="stat-card">
      <div class="stat-item">
        <div class="stat-value">{{ stat.total || 0 }}</div>
        <div class="stat-label">党员总数</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stat.active || 0 }}</div>
        <div class="stat-label">正式党员</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stat.probation || 0 }}</div>
        <div class="stat-label">预备党员</div>
      </div>
    </div>

    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="党员台账" name="ledger" />
      <van-tab title="积分管理" name="score" />
    </van-tabs>

    <template v-if="activeTab === 'ledger'">
      <div class="table-section">
        <ExcelTable 
          :data="ledgerData" 
          :columns="ledgerColumns" 
          export-filename="党员台账.xlsx"
        />
      </div>
    </template>

    <template v-else>
      <div class="table-section">
        <ExcelTable 
          :data="scoreData" 
          :columns="scoreColumns" 
          export-filename="党员积分.xlsx"
        />
      </div>
    </template>

    <van-empty v-if="!loading && !list.length" description="暂无党员信息" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeTab = ref('ledger')
const stat = reactive({
  total: 0,
  active: 0,
  probation: 0
})

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`
}

const getScorePercent = (score) => {
  const s = score || 0
  const percent = Math.min(100, (s / 120) * 100)
  return Math.round(percent)
}

const getScoreColor = (score) => {
  const s = score || 0
  if (s >= 90) return '#07c160'
  if (s >= 60) return '#FFD700'
  return '#D22630'
}

const ledgerColumns = [
  { key: 'name', title: '姓名' },
  { key: 'memberType', title: '党员类型', formatter: (val) => val || '正式党员' },
  { key: 'branch', title: '所在支部', formatter: (val) => val || '村党支部' },
  { key: 'joinDate', title: '入党时间', formatter: (val) => formatTime(val) },
  { key: 'position', title: '职务', formatter: (val) => val || '党员' }
]

const scoreColumns = [
  { key: 'name', title: '姓名' },
  { key: 'score', title: '年度积分', formatter: (val) => `${val || 0}分` },
  { key: 'branch', title: '所在支部', formatter: (val) => val || '村党支部' },
  { key: 'rank', title: '排名', formatter: (val) => `第${val || '-'}名` },
  { key: 'level', title: '等级', formatter: (val) => val || '普通' }
]

const ledgerData = computed(() => list.value)
const scoreData = computed(() => list.value)

const fetchData = async () => {
  loading.value = true
  try {
    const url = activeTab.value === 'score' ? '/party-member/score-list' : '/party-member/list'
    const res = await request.get(url, { params: { page: 1, size: 20 } })
    list.value = res.records || res.data?.records || res || []
  } catch (error) {
    console.error('获取党员数据失败', error)
  } finally {
    loading.value = false
  }
}

const fetchStat = async () => {
  try {
    const res = await request.get('/party-member/stat')
    Object.assign(stat, res.data)
  } catch (error) {
    console.error('获取党员统计失败', error)
  }
}

onMounted(() => {
  fetchStat()
  fetchData()
})
</script>

<style scoped>
.party-member-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.stat-card {
  display: flex;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  margin: 10px 12px;
  border-radius: 14px;
  padding: 16px 0;
}

.stat-item {
  flex: 1;
  text-align: center;
  color: #fff;
  border-right: 1px solid rgba(255, 215, 0, 0.3);
}

.stat-item:last-child {
  border-right: none;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #FFD700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.9;
}

.table-section {
  margin: 10px 12px;
}
</style>
