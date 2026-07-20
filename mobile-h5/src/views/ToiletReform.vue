
<template>
  <div class="toilet-reform-page page-container">
    <van-nav-bar title="厕所革命" left-arrow @click-left="onBack" />

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

    <div class="card" v-for="item in list" :key="item.id" @click="handleItemClick(item)">
      <div class="card-header">
        <van-tag :type="getStatusType(item.status)" size="medium">{{ getStatusName(item.status) }}</van-tag>
        <span class="card-time">申请：{{ formatTime(item.applyDate || item.createTime) }}</span>
      </div>
      <div class="card-title">{{ item.householder }} 户厕改造</div>
      <div class="info-row">
        <span class="label">户主姓名</span>
        <span class="value">{{ item.householder || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">联系电话</span>
        <span class="value">{{ item.phone || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">家庭住址</span>
        <span class="value">{{ item.address || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">改造类型</span>
        <span class="value">{{ item.reformType || '三格式化粪池' }}</span>
      </div>
      <div class="info-row">
        <span class="label">补贴金额</span>
        <span class="value highlight">{{ item.subsidy ? item.subsidy + '元' : '—' }}</span>
      </div>
      <div class="info-row" v-if="item.completeDate">
        <span class="label">完工日期</span>
        <span class="value">{{ formatTime(item.completeDate) }}</span>
      </div>
      <div class="info-row" v-if="item.remark">
        <span class="label">备注</span>
        <span class="value">{{ item.remark }}</span>
      </div>
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无改造记录" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { toiletReformData } from '../data/mockData'
import { goBack } from '../utils/index'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const stat = reactive({ total: 0, completed: 0, pending: 0 })

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', CONSTRUCTING: 'primary', DONE: 'success', REJECTED: 'danger' }
  return map[status] || 'default'
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

const handleItemClick = (item) => {
  showToast(`查看：${item.householder} 户厕改造`)
}

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
  padding-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
}

.card-time {
  font-size: 12px;
  color: #969799;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
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

.value.highlight {
  color: #D22630;
  font-weight: bold;
}
</style>
