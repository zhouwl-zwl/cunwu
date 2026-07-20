
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

    <div class="card" v-for="item in list" :key="item.id" @click="handleItemClick(item)">
      <div class="card-header">
        <van-tag :type="getLevelType(item.level)" size="medium">{{ getLevelName(item.level) }}</van-tag>
        <van-tag :type="getStatusType(item.status)" plain size="medium">{{ getStatusName(item.status) }}</van-tag>
      </div>
      <div class="card-title">{{ item.title || item.hazardType }}</div>
      <div class="card-content">{{ item.description || item.content || '暂无描述' }}</div>
      <div class="info-row">
        <span class="label">隐患类型</span>
        <span class="value">{{ item.hazardType || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">隐患位置</span>
        <span class="value">{{ item.location || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">排查人</span>
        <span class="value">{{ item.inspector || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">排查时间</span>
        <span class="value">{{ formatTime(item.inspectDate || item.createTime) }}</span>
      </div>
      <div class="info-row" v-if="item.deadline">
        <span class="label">整改期限</span>
        <span class="value">{{ formatTime(item.deadline) }}</span>
      </div>
      <div class="info-row" v-if="item.remark">
        <span class="label">整改说明</span>
        <span class="value">{{ item.remark }}</span>
      </div>
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

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const stat = reactive({ total: 0, pending: 0, done: 0 })

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getLevelType = (level) => {
  const map = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'primary' }
  return map[level] || 'default'
}

const getLevelName = (level) => {
  const map = { HIGH: '重大隐患', MEDIUM: '一般隐患', LOW: '轻微隐患' }
  return map[level] || '一般隐患'
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', PROCESSING: 'primary', DONE: 'success', REJECTED: 'danger' }
  return map[status] || 'default'
}

const getStatusName = (status) => {
  const map = { PENDING: '待整改', PROCESSING: '整改中', DONE: '已整改', REJECTED: '已驳回' }
  return map[status] || '未知'
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const handleItemClick = (item) => {
  showToast(`查看：${item.title}`)
}

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
</style>
