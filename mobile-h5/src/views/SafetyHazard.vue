
<template>
  <div class="safety-hazard-page page-container">
    <van-nav-bar title="安全隐患排查" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="待整改" name="PENDING" />
      <van-tab title="已整改" name="DONE" />
    </van-tabs>

    <div class="stat-card">
      <div class="stat-item">
        <div class="stat-value">{{ stat.total || 0 }}</div>
        <div class="stat-label">排查总数</div>
      </div>
      <div class="stat-item">
        <div class="stat-value danger">{{ stat.pending || 0 }}</div>
        <div class="stat-label">待整改</div>
      </div>
      <div class="stat-item">
        <div class="stat-value success">{{ stat.done || 0 }}</div>
        <div class="stat-label">已整改</div>
      </div>
    </div>

    <div class="action-bar">
      <van-button type="primary" icon="plus" @click="handleReport">上报隐患</van-button>
    </div>

    <div class="table-section">
      <ExcelTable 
        :data="list" 
        :columns="columns" 
        export-filename="安全隐患排查.xlsx"
      />
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无隐患记录" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { safetyHazardData } from '../data/mockData'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const stat = reactive({ total: 0, pending: 0, done: 0 })

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const getLevelName = (level) => {
  const map = { HIGH: '重大隐患', MEDIUM: '一般隐患', LOW: '轻微隐患' }
  return map[level] || '一般隐患'
}

const getStatusName = (status) => {
  const map = { PENDING: '待整改', PROCESSING: '整改中', DONE: '已整改', REJECTED: '已驳回' }
  return map[status] || '未知'
}

const columns = [
  { key: 'level', title: '隐患等级', formatter: (val) => getLevelName(val) },
  { key: 'hazardType', title: '隐患类型' },
  { key: 'location', title: '隐患位置' },
  { key: 'inspector', title: '排查人' },
  { key: 'inspectDate', title: '排查时间', formatter: (val, row) => formatTime(val || row.createTime) },
  { key: 'status', title: '状态', formatter: (val) => getStatusName(val) }
]

const handleReport = () => {
  showToast('功能开发中，敬请期待')
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/safety-hazard/list', {
      params: { page: 1, size: 20, status: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : safetyHazardData.records
  } catch (error) {
    list.value = safetyHazardData.records
  } finally {
    loading.value = false
  }
}

const fetchStat = async () => {
  try {
    const res = await request.get('/safety-hazard/stat')
    Object.assign(stat, res.data)
  } catch (error) {
    Object.assign(stat, safetyHazardData.stat)
  }
}

onMounted(() => {
  fetchStat()
  fetchData()
})
</script>

<style scoped>
.safety-hazard-page {
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

.stat-value.danger {
  color: #ff976a;
}

.stat-value.success {
  color: #07c160;
}

.stat-label {
  font-size: 12px;
  opacity: 0.9;
}

.table-section {
  margin: 10px 12px;
}
</style>
