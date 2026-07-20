
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

    <div class="card" v-for="item in list" :key="item.id" @click="handleItemClick(item)">
      <div class="card-header">
        <van-tag :type="getPriorityType(item.priority)" size="medium">{{ getPriorityName(item.priority) }}</van-tag>
        <van-tag :type="getStatusType(item.status)" plain size="medium">{{ getStatusName(item.status) }}</van-tag>
      </div>
      <div class="card-title">{{ item.title }}</div>
      <div class="card-content">{{ item.content || item.description || '暂无描述' }}</div>
      <div class="info-row">
        <span class="label">任务来源</span>
        <span class="value">{{ item.source || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">责任人</span>
        <span class="value">{{ item.assignee || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">下发时间</span>
        <span class="value">{{ formatTime(item.createTime) }}</span>
      </div>
      <div class="info-row">
        <span class="label">截止时间</span>
        <span class="value" :class="{ 'text-danger': isOverdue(item) }">{{ formatTime(item.deadline) }}</span>
      </div>
      <div class="info-row" v-if="item.progress !== undefined">
        <span class="label">完成进度</span>
        <span class="value">{{ item.progress || 0 }}%</span>
      </div>
      <van-progress v-if="item.progress !== undefined" :percentage="item.progress || 0" color="#D22630" stroke-width="6" />
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

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const stat = reactive({ total: 0, overdue: 0, done: 0 })

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getPriorityType = (priority) => {
  const map = { URGENT: 'danger', HIGH: 'warning', NORMAL: 'primary', LOW: 'default' }
  return map[priority] || 'default'
}

const getPriorityName = (priority) => {
  const map = { URGENT: '紧急', HIGH: '重要', NORMAL: '一般', LOW: '低' }
  return map[priority] || '一般'
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', PROCESSING: 'primary', DONE: 'success', OVERDUE: 'danger' }
  return map[status] || 'default'
}

const getStatusName = (status) => {
  const map = { PENDING: '待办理', PROCESSING: '进行中', DONE: '已完成', OVERDUE: '已逾期' }
  return map[status] || '未知'
}

const isOverdue = (item) => {
  if (!item.deadline || item.status === 'DONE') return false
  return new Date(item.deadline).getTime() < Date.now()
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const handleItemClick = (item) => {
  showToast(`查看：${item.title}`)
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

.card {
  background: #fff;
  margin: 10px 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.card-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 4px 0;
}

.label {
  color: #969799;
  font-size: 13px;
}

.value {
  color: #333;
  font-size: 13px;
  font-weight: 500;
  text-align: right;
  max-width: 60%;
}

.text-danger {
  color: #D22630;
  font-weight: bold;
}
</style>
