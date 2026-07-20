
<template>
  <div class="notification-detail-page page-container">
    <van-nav-bar title="通知详情" left-arrow @click-left="handleBack" />
    
    <div class="detail-content" v-if="notification">
      <div class="detail-header">
        <van-tag v-if="notification.isTop === 1" type="danger">置顶</van-tag>
        <van-tag v-else :type="getTagType(notification.type)" size="medium">{{ notification.type }}</van-tag>
        <h1 class="detail-title">{{ notification.title }}</h1>
        <p class="detail-time">{{ formatTime(notification.createTime) }}</p>
      </div>
      
      <div class="detail-body">
        <p>{{ notification.content }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const route = useRoute()
const notification = ref(null)

const handleBack = () => {
  goBack(router)
}

const getTagType = (type) => {
  if (type === '紧急通知') return 'danger'
  if (type === '党建通知') return 'primary'
  if (type === '民生通知') return 'success'
  return 'default'
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

const fetchNotification = async () => {
  try {
    const res = await request.get(`/notification/${route.params.id}`)
    notification.value = res.data
  } catch (error) {
    console.error('获取通知详情失败', error)
  }
}

onMounted(() => {
  fetchNotification()
})
</script>

<style scoped>
.detail-content {
  padding: 16px;
}

.detail-header {
  margin-bottom: 24px;
}

.detail-title {
  font-size: 20px;
  font-weight: bold;
  color: #323233;
  margin: 12px 0;
}

.detail-time {
  font-size: 14px;
  color: #969799;
}

.detail-body {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  line-height: 1.8;
  font-size: 15px;
  color: #646566;
}
</style>
