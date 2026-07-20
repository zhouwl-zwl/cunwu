
<template>
  <div class="forest-fire-page page-container">
    <van-nav-bar title="森林防火" left-arrow @click-left="onBack" />

    <div class="warning-banner">
      <div class="warning-icon">🔥</div>
      <div class="warning-text">
        <div class="warning-title">防火等级：{{ fireLevel.level || '三级' }}</div>
        <div class="warning-desc">{{ fireLevel.suggestion || '加强巡山，注意防火' }}</div>
      </div>
    </div>

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="巡山记录" name="巡山记录" />
      <van-tab title="火情上报" name="火情上报" />
      <van-tab title="宣传记录" name="宣传记录" />
    </van-tabs>

    <div class="action-bar">
      <van-button type="primary" icon="plus" @click="handleAdd">新增记录</van-button>
    </div>

    <div class="card" v-for="item in list" :key="item.id" @click="handleItemClick(item)">
      <div class="card-header">
        <van-tag type="danger" size="medium">{{ item.type || '巡山记录' }}</van-tag>
        <van-tag :type="getStatusType(item.status)" plain size="medium">{{ getStatusName(item.status) }}</van-tag>
      </div>
      <div class="card-title">{{ item.title }}</div>
      <div class="card-content">{{ item.content || item.description || '暂无描述' }}</div>
      <div class="info-row">
        <span class="label">巡查区域</span>
        <span class="value">{{ item.area || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">巡查人</span>
        <span class="value">{{ item.inspector || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">巡查时间</span>
        <span class="value">{{ formatTime(item.inspectDate || item.createTime) }}</span>
      </div>
      <div class="info-row" v-if="item.foundIssue !== undefined">
        <span class="label">发现问题</span>
        <span class="value" :class="{ 'text-danger': item.foundIssue }">{{ item.foundIssue ? '是' : '无' }}</span>
      </div>
      <div class="info-row" v-if="item.remark">
        <span class="label">备注</span>
        <span class="value">{{ item.remark }}</span>
      </div>
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无巡山记录" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { forestFireData } from '../data/mockData'
import { goBack } from '../utils/index'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const fireLevel = reactive({ level: '', suggestion: '' })

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getStatusType = (status) => {
  const map = { NORMAL: 'success', ABNORMAL: 'warning', PENDING: 'danger', DONE: 'success' }
  return map[status] || 'default'
}

const getStatusName = (status) => {
  const map = { NORMAL: '正常', ABNORMAL: '异常', PENDING: '待处理', DONE: '已处理' }
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
    const res = await request.get('/forest-fire/list', {
      params: { page: 1, size: 20, type: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : forestFireData.records
  } catch (error) {
    list.value = forestFireData.records
  } finally {
    loading.value = false
  }
}

const fetchFireLevel = async () => {
  try {
    const res = await request.get('/forest-fire/level')
    Object.assign(fireLevel, res.data)
  } catch (error) {
    Object.assign(fireLevel, forestFireData.level)
  }
}

onMounted(() => {
  fetchFireLevel()
  fetchData()
})
</script>

<style scoped>
.forest-fire-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.warning-banner {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #FFA500 0%, #D22630 100%);
  margin: 10px 12px;
  border-radius: 14px;
  padding: 14px;
  color: #fff;
}

.warning-icon {
  font-size: 36px;
  margin-right: 12px;
}

.warning-text {
  flex: 1;
}

.warning-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 4px;
}

.warning-desc {
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
