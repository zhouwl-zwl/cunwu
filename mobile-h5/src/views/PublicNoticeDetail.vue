
<template>
  <div class="notice-detail-page page-container">
    <van-nav-bar title="公示详情" left-arrow @click-left="handleBack" />
    
    <div class="detail-content" v-if="notice">
      <div class="detail-header">
        <van-tag :type="getTagType(notice.noticeType)" size="medium">{{ notice.noticeType }}</van-tag>
        <h1 class="detail-title">{{ notice.title }}</h1>
        <p class="detail-time">{{ formatTime(notice.createTime) }}</p>
      </div>
      
      <div class="detail-body">
        <p>{{ notice.content }}</p>
      </div>

      <div class="detail-footer">
        <van-button type="primary" block>留言提问</van-button>
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
const notice = ref(null)

const handleBack = () => {
  goBack(router)
}

const getTagType = (type) => {
  if (type === '党务公开') return 'danger'
  if (type === '村务公开') return 'primary'
  if (type === '财务公开') return 'warning'
  return 'default'
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

const fetchNotice = async () => {
  try {
    const res = await request.get(`/public-notice/${route.params.id}`)
    notice.value = res.data
  } catch (error) {
    console.error('获取公示详情失败', error)
  }
}

onMounted(() => {
  fetchNotice()
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

.detail-footer {
  margin-top: 24px;
}
</style>
