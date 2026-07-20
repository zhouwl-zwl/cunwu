<template>
  <div class="page-container">
    <van-nav-bar title="诉求进度查询" left-arrow @click-left="onBack">
      <template #right>
        <van-button v-if="hasUploadPermission" type="primary" size="small" @click="goSubmit">上报诉求</van-button>
      </template>
    </van-nav-bar>
    
    <van-tabs v-model:active="activeTab">
      <van-tab title="全部" name="all">
        <div v-if="allList.length === 0" class="empty-state">
          <van-empty description="暂无诉求记录" />
        </div>
        <div v-else class="news-list">
          <div 
            v-for="item in allList" 
            :key="item.id" 
            class="news-card" 
            @click="showDetail(item)"
          >
            <div class="news-content">
              <h3 class="news-title">{{ item.title || '诉求' }}</h3>
              <p class="news-summary">{{ item.content?.substring(0, 80) }}...</p>
              <div class="news-meta">
                <span class="news-time">{{ item.createTime }}</span>
                <span :class="['news-status', getStatusClass(item.status)]">{{ getStatusText(item.status) }}</span>
              </div>
            </div>
            <div v-if="item.images && item.images.length > 0" class="news-image-wrapper">
              <img :src="getFirstImage(item.images)" class="news-image" />
            </div>
          </div>
        </div>
      </van-tab>
      
      <van-tab title="处理中" name="processing">
        <div v-if="processingList.length === 0" class="empty-state">
          <van-empty description="暂无处理中的诉求" />
        </div>
        <div v-else class="news-list">
          <div 
            v-for="item in processingList" 
            :key="item.id" 
            class="news-card" 
            @click="showDetail(item)"
          >
            <div class="news-content">
              <h3 class="news-title">{{ item.title || '诉求' }}</h3>
              <p class="news-summary">{{ item.content?.substring(0, 80) }}...</p>
              <div class="news-meta">
                <span class="news-time">{{ item.createTime }}</span>
                <span class="news-status processing">处理中</span>
              </div>
            </div>
            <div v-if="item.images && item.images.length > 0" class="news-image-wrapper">
              <img :src="getFirstImage(item.images)" class="news-image" />
            </div>
          </div>
        </div>
      </van-tab>
      
      <van-tab title="已完成" name="completed">
        <div v-if="completedList.length === 0" class="empty-state">
          <van-empty description="暂无已完成的诉求" />
        </div>
        <div v-else class="news-list">
          <div 
            v-for="item in completedList" 
            :key="item.id" 
            class="news-card" 
            @click="showDetail(item)"
          >
            <div class="news-content">
              <h3 class="news-title">{{ item.title || '诉求' }}</h3>
              <p class="news-summary">{{ item.content?.substring(0, 80) }}...</p>
              <div class="news-meta">
                <span class="news-time">{{ item.createTime }}</span>
                <span class="news-status completed">已完成</span>
              </div>
            </div>
            <div v-if="item.images && item.images.length > 0" class="news-image-wrapper">
              <img :src="getFirstImage(item.images)" class="news-image" />
            </div>
          </div>
        </div>
      </van-tab>
    </van-tabs>

    <van-dialog v-model:show="showDetailDialog" :title="'诉求详情'" width="90%">
      <div v-if="currentDemand" class="detail-content">
        <div class="detail-header">
          <span :class="['detail-status', getStatusClass(currentDemand.status)]">{{ getStatusText(currentDemand.status) }}</span>
          <span class="detail-type">{{ getTypeText(currentDemand.demandType) }}</span>
        </div>
        <h3 class="detail-title">{{ currentDemand.title || '诉求' }}</h3>
        <div class="detail-meta">
          <span>提交时间：{{ currentDemand.createTime }}</span>
        </div>
        <div class="detail-body">
          <p>{{ currentDemand.content }}</p>
        </div>
        <div v-if="currentDemand.images && currentDemand.images.length > 0" class="detail-images">
          <img v-for="(img, idx) in getImagesArray(currentDemand.images)" :key="idx" :src="img" class="detail-image" />
        </div>
        <div v-if="currentDemand.processingRecord" class="detail-result">
          <h4>处理结果：</h4>
          <p>{{ currentDemand.processingRecord }}</p>
        </div>
        <div v-if="currentDemand.status === '已完成'" class="detail-rating">
          <span class="label">评价：</span>
          <van-rate v-model="rating" size="20" />
          <van-button type="primary" size="small" @click="submitRating">提交评价</van-button>
        </div>
      </div>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { showToast } from 'vant'
import { useUserStore } from '../store/user'
import { goBack } from '../utils/index'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('all')

const allList = ref([])
const processingList = ref([])
const completedList = ref([])

const showDetailDialog = ref(false)
const currentDemand = ref(null)
const rating = ref(5)

const hasUploadPermission = computed(() => true)

const onBack = () => { goBack(router) }

const goSubmit = () => {
  router.push('/demand-submit')
}

const getStatusText = (status) => {
  const map = { '待处理': '待处理', '处理中': '处理中', '已完成': '已完成', '已驳回': '已驳回', 'PENDING': '待受理', 'PROCESSING': '处理中', 'COMPLETED': '已办结' }
  return map[status] || status
}

const getStatusClass = (status) => {
  const map = { '待处理': 'pending', '处理中': 'processing', '已完成': 'completed', '已驳回': 'rejected', 'PENDING': 'pending', 'PROCESSING': 'processing', 'COMPLETED': 'completed' }
  return map[status] || 'pending'
}

const getTypeText = (type) => {
  const map = { 'dispute': '矛盾纠纷', 'environment': '环境问题', 'road': '道路损坏', 'safety': '安全隐患', 'other': '其他', 'DISPUTE': '矛盾纠纷', 'ENVIRONMENT': '环境问题', 'ROAD': '道路损坏', 'SAFETY': '安全隐患', 'OTHER': '其他' }
  return map[type] || type
}

const getFirstImage = (images) => {
  if (typeof images === 'string') {
    const arr = JSON.parse(images)
    return arr[0] || ''
  }
  return images[0] || ''
}

const getImagesArray = (images) => {
  if (typeof images === 'string') {
    return JSON.parse(images)
  }
  return images || []
}

const loadAll = async () => {
  try {
    const res = await request.get('/demand/list', { params: { page: 1, size: 20 } })
    allList.value = res.records || res.data?.records || []
  } catch (error) {
    console.error(error)
  }
}

const loadProcessing = async () => {
  try {
    const res = await request.get('/demand/list', { params: { page: 1, size: 20, status: '处理中' } })
    processingList.value = res.records || res.data?.records || []
  } catch (error) {
    console.error(error)
  }
}

const loadCompleted = async () => {
  try {
    const res = await request.get('/demand/list', { params: { page: 1, size: 20, status: '已完成' } })
    completedList.value = res.records || res.data?.records || []
  } catch (error) {
    console.error(error)
  }
}

const showDetail = (item) => {
  currentDemand.value = item
  rating.value = item.rating || 5
  showDetailDialog.value = true
}

const submitRating = async () => {
  try {
    await request.put(`/demand/rating/${currentDemand.value.id}`, { rating })
    showToast('评价成功')
    showDetailDialog.value = false
    loadCompleted()
  } catch (error) {
    showToast('评价失败')
  }
}

onMounted(() => {
  loadAll()
})
</script>

<style scoped>
.page-container {
  background: #FDF5F5;
  min-height: 100vh;
  padding-bottom: 20px;
}

.empty-state {
  padding: 60px 20px;
}

.news-list {
  padding: 12px;
}

.news-card {
  display: flex;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.news-card:active {
  transform: scale(0.98);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.news-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-right: 12px;
  min-width: 0;
}

.news-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  line-height: 1.4;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-summary {
  font-size: 13px;
  color: #999;
  line-height: 1.5;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.news-time {
  font-size: 12px;
  color: #bbb;
}

.news-status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
}

.news-status.pending {
  background: #fff7e6;
  color: #faad14;
}

.news-status.processing {
  background: #e6f7ff;
  color: #1890ff;
}

.news-status.completed {
  background: #f6ffed;
  color: #52c41a;
}

.news-status.rejected {
  background: #fff2f0;
  color: #ff4d4f;
}

.news-image-wrapper {
  flex-shrink: 0;
  width: 100px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
}

.news-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-content {
  padding: 8px 0;
}

.detail-header {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.detail-status {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 4px;
}

.detail-status.pending {
  background: #fff7e6;
  color: #faad14;
}

.detail-status.processing {
  background: #e6f7ff;
  color: #1890ff;
}

.detail-status.completed {
  background: #f6ffed;
  color: #52c41a;
}

.detail-status.rejected {
  background: #fff2f0;
  color: #ff4d4f;
}

.detail-type {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 4px;
  background: #f5f5f5;
  color: #666;
}

.detail-title {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 8px;
}

.detail-meta {
  font-size: 13px;
  color: #999;
  margin-bottom: 16px;
}

.detail-body {
  font-size: 15px;
  color: #333;
  line-height: 1.7;
  margin-bottom: 16px;
}

.detail-images {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.detail-image {
  width: calc(33.33% - 6px);
  aspect-ratio: 1;
  object-fit: cover;
  border-radius: 8px;
}

.detail-result {
  background: #f9f9f9;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.detail-result h4 {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.detail-result p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.detail-rating {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-top: 12px;
  border-top: 1px solid #f5f5f5;
}

.label {
  font-size: 14px;
  color: #999;
}

:deep(.van-nav-bar) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

:deep(.van-nav-bar__title) {
  color: #FFD700;
}

:deep(.van-button--primary) {
  background: #FFD700 !important;
  color: #C8102E !important;
  border: none !important;
}

:deep(.van-tab__text) {
  color: #999;
}

:deep(.van-tab--active .van-tab__text) {
  color: #C8102E;
}

:deep(.van-tabs__nav) {
  background: #fff;
}
</style>