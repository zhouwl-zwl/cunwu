
<template>
  <div class="demand-detail-page page-container">
    <van-nav-bar title="诉求详情" left-arrow @click-left="onBack" />

    <div class="card">
      <div class="status-section">
        <van-tag :type="getStatusType(detail.status)" size="large">{{ getStatusName(detail.status) }}</van-tag>
        <van-tag plain type="danger" size="medium">{{ detail.demandType || '其他' }}</van-tag>
      </div>
      <div class="info-row">
        <span class="label">提交人</span>
        <span class="value">{{ detail.name }}</span>
      </div>
      <div class="info-row">
        <span class="label">联系电话</span>
        <span class="value">{{ detail.phone }}</span>
      </div>
      <div class="info-row">
        <span class="label">提交时间</span>
        <span class="value">{{ formatTime(detail.createTime) }}</span>
      </div>
      <div class="content-section">
        <div class="section-title">诉求内容</div>
        <div class="content-text">{{ detail.content }}</div>
      </div>
      <div class="content-section" v-if="imageList.length">
        <div class="section-title">现场图片</div>
        <div class="image-list">
          <img v-for="(img, index) in imageList" :key="index" :src="img" class="image-item" @click="previewImage(index)" />
        </div>
      </div>
    </div>

    <div class="card" v-if="detail.reply">
      <div class="section-title">处理回复</div>
      <div class="content-text">{{ detail.reply }}</div>
      <div class="info-row" v-if="detail.handler">
        <span class="label">处理人</span>
        <span class="value">{{ detail.handler }}</span>
      </div>
      <div class="info-row" v-if="detail.handleTime">
        <span class="label">处理时间</span>
        <span class="value">{{ formatTime(detail.handleTime) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showImagePreview } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const route = useRoute()
const detail = ref({})

const onBack = () => { goBack(router) }

const imageList = computed(() => {
  if (!detail.value.images) return []
  return detail.value.images.split(',').filter(Boolean)
})

const getStatusType = (status) => {
  const map = { PENDING: 'warning', PROCESSING: 'primary', DONE: 'success', REJECTED: 'danger' }
  return map[status] || 'default'
}

const getStatusName = (status) => {
  const map = { PENDING: '待处理', PROCESSING: '处理中', DONE: '已办结', REJECTED: '已驳回' }
  return map[status] || '未知'
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
}

const previewImage = (index) => {
  showImagePreview({
    images: imageList.value,
    startPosition: index
  })
}

const fetchDetail = async () => {
  try {
    const res = await request.get(`/demand/${route.params.id}`)
    detail.value = res.data || {}
  } catch (error) {
    console.error('获取诉求详情失败', error)
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.demand-detail-page {
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

.status-section {
  display: flex;
  gap: 8px;
  margin-bottom: 14px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}

.info-row:last-child {
  border-bottom: none;
}

.label {
  color: #969799;
  font-size: 14px;
}

.value {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.content-section {
  margin-top: 14px;
}

.section-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 8px;
  padding-left: 8px;
  border-left: 3px solid #FFD700;
}

.content-text {
  font-size: 14px;
  color: #333;
  line-height: 1.7;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.image-item {
  width: calc((100% - 16px) / 3);
  aspect-ratio: 1;
  border-radius: 8px;
  object-fit: cover;
}
</style>
