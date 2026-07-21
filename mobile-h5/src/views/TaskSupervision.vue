
<template>
  <div class="task-supervision-page page-container">
    <van-nav-bar title="任务督办" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="待办理" name="PENDING" />
      <van-tab title="进行中" name="PROCESSING" />
      <van-tab title="已完成" name="DONE" />
      <van-tab title="逾期" name="OVERDUE" />
    </van-tabs>

    <div class="stat-card">
      <div class="stat-item">
        <div class="stat-value">{{ stat.total || 0 }}</div>
        <div class="stat-label">任务总数</div>
      </div>
      <div class="stat-item">
        <div class="stat-value danger">{{ stat.overdue || 0 }}</div>
        <div class="stat-label">逾期</div>
      </div>
      <div class="stat-item">
        <div class="stat-value success">{{ stat.done || 0 }}</div>
        <div class="stat-label">已完成</div>
      </div>
    </div>

    <div class="action-bar">
      <van-button type="primary" icon="plus" @click="handleAdd">新增任务</van-button>
    </div>

    <div class="table-section">
      <ExcelTable 
        :data="list" 
        :columns="columns" 
        export-filename="任务督办.xlsx"
      />
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无督办任务" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { taskSupervisionData } from '../data/mockData'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const stat = reactive({ total: 0, overdue: 0, done: 0 })

const columns = [
  { key: 'title', title: '任务标题' },
  { key: 'priority', title: '优先级', formatter: (val) => getPriorityName(val) },
  { key: 'status', title: '状态', formatter: (val) => getStatusName(val) },
  { key: 'assignee', title: '责任人', formatter: (val) => val || '—' },
  { key: 'deadline', title: '截止时间', formatter: (val) => formatTime(val) },
  { key: 'progress', title: '进度', formatter: (val) => `${val || 0}%` }
]

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getPriorityName = (priority) => {
  const map = { URGENT: '紧急', HIGH: '重要', NORMAL: '一般', LOW: '低' }
  return map[priority] || '一般'
}

const getStatusName = (status) => {
  const map = { PENDING: '待办理', PROCESSING: '进行中', DONE: '已完成', OVERDUE: '已逾期' }
  return map[status] || '未知'
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const handleAdd = () => {
  showToast('功能开发中，敬请期待')
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/task-supervision/list', {
      params: { page: 1, size: 20, status: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : taskSupervisionData.records
  } catch (error) {
    list.value = taskSupervisionData.records
  } finally {
    loading.value = false
  }
}

const fetchStat = async () => {
  try {
    const res = await request.get('/task-supervision/stat')
    Object.assign(stat, res.data)
  } catch (error) {
    Object.assign(stat, taskSupervisionData.stat)
  }
}

onMounted(() => {
  fetchStat()
  fetchData()
})
</script>

<style scoped>
.task-supervision-page {
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
