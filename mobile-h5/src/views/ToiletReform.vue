
<template>
  <div class="toilet-reform-page page-container">
    <van-nav-bar title="交通治理" left-arrow @click-left="onBack" />

    <div class="stat-card">
      <div class="stat-item">
        <div class="stat-value">{{ stat.total || 0 }}</div>
        <div class="stat-label">改造总数</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stat.completed || 0 }}</div>
        <div class="stat-label">已完成</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stat.pending || 0 }}</div>
        <div class="stat-label">待改造</div>
      </div>
    </div>

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="待改造" name="PENDING" />
      <van-tab title="施工中" name="CONSTRUCTING" />
      <van-tab title="已完成" name="DONE" />
    </van-tabs>

    <div class="action-bar">
      <van-button type="primary" icon="plus" @click="handleApply">申请改造</van-button>
    </div>

    <div class="table-section">
      <ExcelTable 
        :data="tableData" 
        :columns="toiletColumns" 
        export-filename="厕所改造台账.xlsx"
      />
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无改造记录" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { toiletReformData } from '../data/mockData'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const stat = reactive({ total: 0, completed: 0, pending: 0 })

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getStatusName = (status) => {
  const map = { PENDING: '待改造', CONSTRUCTING: '施工中', DONE: '已完成', REJECTED: '已驳回' }
  return map[status] || '未知'
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const toiletColumns = [
  { key: 'householder', title: '户主姓名' },
  { key: 'phone', title: '联系电话' },
  { key: 'address', title: '家庭住址' },
  { key: 'reformType', title: '改造类型', formatter: (val) => val || '三格式化粪池' },
  { key: 'status', title: '状态', formatter: (val) => getStatusName(val) },
  { key: 'subsidy', title: '补贴金额', formatter: (val) => val ? val + '元' : '—' },
  { key: 'applyDate', title: '申请日期', formatter: (val) => formatTime(val) },
  { key: 'completeDate', title: '完工日期', formatter: (val) => formatTime(val) }
]

const tableData = computed(() => list.value)

const handleApply = () => {
  showToast('功能开发中，敬请期待')
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/toilet-reform/list', {
      params: { page: 1, size: 20, status: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : toiletReformData.records
  } catch (error) {
    list.value = toiletReformData.records
  } finally {
    loading.value = false
  }
}

const fetchStat = async () => {
  try {
    const res = await request.get('/toilet-reform/stat')
    Object.assign(stat, res.data)
  } catch (error) {
    Object.assign(stat, toiletReformData.stat)
  }
}

onMounted(() => {
  fetchStat()
  fetchData()
})
</script>

<style scoped>
.toilet-reform-page {
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
