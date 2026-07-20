
<template>
  <div class="party-activity-page page-container">
    <van-nav-bar title="党组织生活" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="三会一课" name="三会一课" />
      <van-tab title="主题党日" name="主题党日" />
      <van-tab title="组织生活会" name="组织生活会" />
    </van-tabs>

    <div class="card" v-for="item in list" :key="item.id">
      <div class="card-header">
        <van-tag type="danger" size="medium">{{ item.type || '三会一课' }}</van-tag>
        <span class="card-time">{{ formatTime(item.activityDate || item.createTime) }}</span>
      </div>
      <div class="card-title">{{ item.title }}</div>
      <div class="card-content">{{ item.content || item.summary || '暂无内容描述' }}</div>
      <div class="card-footer">
        <span class="footer-item">主持人：{{ item.host || '—' }}</span>
        <span class="footer-item">参与：{{ item.attendance || 0 }}人</span>
      </div>
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无活动记录" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/party-activity/list', {
      params: { page: 1, size: 20, type: activeType.value || undefined }
    })
    list.value = res.records || res.data?.records || res || []
  } catch (error) {
    console.error('获取党组织生活数据失败', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.party-activity-page {
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

.card-time {
  font-size: 12px;
  color: #969799;
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
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  padding-top: 10px;
  border-top: 1px solid #f5f5f5;
}

.footer-item {
  font-size: 12px;
  color: #969799;
}
</style>
