
<template>
  <div class="fire-safety-page page-container">
    <van-nav-bar title="消防安全" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="巡查记录" name="巡查记录" />
      <van-tab title="隐患整改" name="隐患整改" />
      <van-tab title="宣传培训" name="宣传培训" />
    </van-tabs>

    <div class="action-bar">
      <van-button type="primary" icon="plus" @click="handleAdd">新增记录</van-button>
    </div>

    <div class="card" v-for="item in list" :key="item.id" @click="handleItemClick(item)">
      <div class="card-header">
        <van-tag type="danger" size="medium">{{ item.type || '巡查记录' }}</van-tag>
        <van-tag :type="getStatusType(item.status)" plain size="medium">{{ getStatusName(item.status) }}</van-tag>
      </div>
      <div class="card-title">{{ item.title }}</div>
      <div class="card-content">{{ item.content || item.description || '暂无描述' }}</div>
      <div class="info-row">
        <span class="label">检查地点</span>
        <span class="value">{{ item.location || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">检查人</span>
        <span class="value">{{ item.inspector || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">检查时间</span>
        <span class="value">{{ formatTime(item.inspectDate || item.createTime) }}</span>
      </div>
      <div class="info-row" v-if="item.hazardCount !== undefined">
        <span class="label">发现问题</span>
        <span class="value">{{ item.hazardCount || 0 }}项</span>
      </div>
      <div class="info-row" v-if="item.remark">
        <span class="label">备注</span>
        <span class="value">{{ item.remark }}</span>
      </div>
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无消防记录" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { fireSafetyData } from '../data/mockData'
import { goBack } from '../utils/index'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getStatusType = (status) => {
  const map = { NORMAL: 'success', ABNORMAL: 'warning', PENDING: 'danger', DONE: 'success' }
  return map[status] || 'default'
}

const getStatusName = (status) => {
  const map = { NORMAL: '正常', ABNORMAL: '异常', PENDING: '待整改', DONE: '已整改' }
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

const handleAdd = () => {
  showToast('功能开发中，敬请期待')
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/fire-safety/list', {
      params: { page: 1, size: 20, type: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : fireSafetyData.records
  } catch (error) {
    list.value = fireSafetyData.records
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.fire-safety-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
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
