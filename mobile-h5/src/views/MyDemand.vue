<template>
  <div class="my-demand-page page-container">
    <van-nav-bar title="我的诉求" left-arrow @click-left="onBack">
      <template #right>
        <van-button type="primary" size="small" @click="goSubmit">新增诉求</van-button>
      </template>
    </van-nav-bar>

    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="全部" name="all">
        <van-pull-refresh v-model="isRefreshing" @refresh="onRefresh">
          <div v-if="allList.length === 0 && !loading" class="empty-state">
            <div class="empty-icon">📝</div>
            <div class="empty-text">暂无诉求记录</div>
            <van-button type="primary" size="small" @click="goSubmit">提交诉求</van-button>
          </div>
          <van-list v-else v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
            <div 
              v-for="item in allList" 
              :key="item.id" 
              class="demand-card"
              @click="showDetail(item)"
            >
              <div class="demand-header">
                <span class="demand-type">{{ getTypeText(item.demandType) }}</span>
                <span :class="['demand-status', getStatusClass(item.status)]">{{ getStatusText(item.status) }}</span>
              </div>
              <div class="demand-title">{{ item.title || '诉求' }}</div>
              <div class="demand-content">{{ item.content?.substring(0, 60) }}...</div>
              <div class="demand-footer">
                <span class="demand-time">{{ formatTime(item.createTime) }}</span>
                <div v-if="item.images && item.images.length > 0" class="demand-images">
                  <img v-for="(img, idx) in getImagesArray(item.images).slice(0, 3)" :key="idx" :src="img" class="demand-image" />
                </div>
              </div>
              <div v-if="item.replies && item.replies.length > 0" class="reply-badge">
                <van-icon name="message-o" size="14" />
                <span>{{ item.replies.length }}条回复</span>
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
      </van-tab>
      
      <van-tab title="待受理" name="pending">
        <van-pull-refresh v-model="isRefreshing" @refresh="onRefresh">
          <div v-if="pendingList.length === 0 && !loading" class="empty-state">
            <div class="empty-icon">⏳</div>
            <div class="empty-text">暂无待受理的诉求</div>
          </div>
          <van-list v-else v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
            <div 
              v-for="item in pendingList" 
              :key="item.id" 
              class="demand-card"
              @click="showDetail(item)"
            >
              <div class="demand-header">
                <span class="demand-type">{{ getTypeText(item.demandType) }}</span>
                <span class="demand-status pending">待受理</span>
              </div>
              <div class="demand-title">{{ item.title || '诉求' }}</div>
              <div class="demand-content">{{ item.content?.substring(0, 60) }}...</div>
              <div class="demand-footer">
                <span class="demand-time">{{ formatTime(item.createTime) }}</span>
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
      </van-tab>
      
      <van-tab title="处理中" name="processing">
        <van-pull-refresh v-model="isRefreshing" @refresh="onRefresh">
          <div v-if="processingList.length === 0 && !loading" class="empty-state">
            <div class="empty-icon">🔄</div>
            <div class="empty-text">暂无处理中的诉求</div>
          </div>
          <van-list v-else v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
            <div 
              v-for="item in processingList" 
              :key="item.id" 
              class="demand-card"
              @click="showDetail(item)"
            >
              <div class="demand-header">
                <span class="demand-type">{{ getTypeText(item.demandType) }}</span>
                <span class="demand-status processing">处理中</span>
              </div>
              <div class="demand-title">{{ item.title || '诉求' }}</div>
              <div class="demand-content">{{ item.content?.substring(0, 60) }}...</div>
              <div class="demand-footer">
                <span class="demand-time">{{ formatTime(item.createTime) }}</span>
              </div>
              <div v-if="item.replies && item.replies.length > 0" class="reply-badge">
                <van-icon name="message-o" size="14" />
                <span>{{ item.replies.length }}条回复</span>
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
      </van-tab>
      
      <van-tab title="已办结" name="completed">
        <van-pull-refresh v-model="isRefreshing" @refresh="onRefresh">
          <div v-if="completedList.length === 0 && !loading" class="empty-state">
            <div class="empty-icon">✅</div>
            <div class="empty-text">暂无已办结的诉求</div>
          </div>
          <van-list v-else v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
            <div 
              v-for="item in completedList" 
              :key="item.id" 
              class="demand-card"
              @click="showDetail(item)"
            >
              <div class="demand-header">
                <span class="demand-type">{{ getTypeText(item.demandType) }}</span>
                <span :class="['demand-status', getStatusClass(item.status)]">{{ getStatusText(item.status) }}</span>
              </div>
              <div class="demand-title">{{ item.title || '诉求' }}</div>
              <div class="demand-content">{{ item.content?.substring(0, 60) }}...</div>
              <div class="demand-footer">
                <span class="demand-time">{{ formatTime(item.createTime) }}</span>
              </div>
              <div v-if="item.rating" class="rating-badge">
                <van-rate v-model="item.rating" size="14" readonly />
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
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
          <span>提交时间：{{ formatDateTime(currentDemand.createTime) }}</span>
        </div>
        <div class="detail-body">
          <p>{{ currentDemand.content }}</p>
        </div>
        <div v-if="currentDemand.images && currentDemand.images.length > 0" class="detail-images">
          <img v-for="(img, idx) in getImagesArray(currentDemand.images)" :key="idx" :src="img" class="detail-image" @click="previewImage(img)" />
        </div>
        <div v-if="currentDemand.replies && currentDemand.replies.length > 0" class="replies-section">
          <div class="replies-title">调解员回复</div>
          <div v-for="reply in currentDemand.replies" :key="reply.id" class="reply-item">
            <div class="reply-header">
              <span class="reply-name">{{ reply.name }}</span>
              <span class="reply-time">{{ formatDateTime(reply.time) }}</span>
            </div>
            <div class="reply-content">{{ reply.content }}</div>
            <div v-if="reply.photos && reply.photos.length > 0" class="reply-photos">
              <img v-for="(photo, idx) in reply.photos" :key="idx" :src="photo" class="photo-item" @click="previewImage(photo)" />
            </div>
          </div>
        </div>
        <div v-if="currentDemand.processingRecord" class="detail-result">
          <h4>处理结果：</h4>
          <p>{{ currentDemand.processingRecord }}</p>
        </div>
        <div v-if="currentDemand.status === '已办结' && !currentDemand.rating" class="detail-rating">
          <span class="label">满意度评价：</span>
          <van-rate v-model="rating" size="20" />
          <van-button type="primary" size="small" @click="submitRating">提交评价</van-button>
        </div>
      </div>
    </van-dialog>

    <van-image-preview v-model:show="showPreview" :images="previewImages" :start-position="previewIndex" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { useUserStore } from '../store/user'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('all')
const showDetailDialog = ref(false)
const currentDemand = ref(null)
const rating = ref(5)
const showPreview = ref(false)
const previewImages = ref([])
const previewIndex = ref(0)

const isRefreshing = ref(false)
const loading = ref(false)
const finished = ref(false)
const page = ref(1)
const pageSize = 10

const demandList = ref([])

const allList = computed(() => demandList.value)
const pendingList = computed(() => demandList.value.filter(item => ['PENDING', '待处理'].includes(item.status)))
const processingList = computed(() => demandList.value.filter(item => ['PROCESSING', '处理中'].includes(item.status)))
const completedList = computed(() => demandList.value.filter(item => ['COMPLETED', '已完成', '已办结'].includes(item.status)))

const onBack = () => { goBack(router) }

const goSubmit = () => {
  router.push('/demand-submit')
}

const onTabChange = () => {
  page.value = 1
  finished.value = false
  demandList.value = []
  fetchMyDemands()
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

const getImagesArray = (images) => {
  if (typeof images === 'string') {
    return JSON.parse(images)
  }
  return images || []
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (days === 0) {
    return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  } else if (days === 1) {
    return '昨天'
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return time.split(' ')[0]
  }
}

const formatDateTime = (time) => {
  if (!time) return ''
  return time
}

const showDetail = (item) => {
  currentDemand.value = item
  rating.value = item.rating || 5
  showDetailDialog.value = true
}

const previewImage = (img) => {
  previewImages.value = getImagesArray(currentDemand.value.images)
  previewIndex.value = previewImages.value.indexOf(img)
  showPreview.value = true
}

const submitRating = async () => {
  try {
    await request.put(`/demand/rating/${currentDemand.value.id}`, { rating: rating.value })
    showToast('评价成功')
    currentDemand.value.rating = rating.value
    showDetailDialog.value = false
  } catch (error) {
    showToast('评价失败')
  }
}

const fetchMyDemands = async () => {
  try {
    const userId = userStore.userInfo?.id
    const res = await request.get('/demand/my-demands', {
      params: { userId, page: page.value, size: pageSize }
    })
    const rawData = res?.records || res?.data?.records || []
    const data = Array.isArray(rawData) ? rawData : []
    if (page.value === 1) {
      demandList.value = data
    } else {
      demandList.value = [...demandList.value, ...data]
    }
    finished.value = data.length < pageSize
  } catch (error) {
    console.error('获取我的诉求失败', error)
    if (page.value === 1) {
      demandList.value = [
        { id: 1, demandType: 'environment', title: '村内垃圾堆积问题', content: '村东头垃圾桶旁边垃圾堆积严重，已经有好几天了，请尽快安排清理。', status: 'COMPLETED', createTime: '2024-07-10 14:30', images: ['https://via.placeholder.com/300x200'], processingRecord: '已安排保洁员清理完毕，后续将加强巡查。', rating: 5, replies: [{ id: 1, name: '张网格员', time: '2024-07-10 16:00', content: '收到，已安排人员处理。' }] },
        { id: 2, demandType: 'road', title: '村道坑洼不平', content: '从村委会到第三村民组的道路有几处坑洼，下雨天积水严重，影响出行安全。', status: 'PROCESSING', createTime: '2024-07-12 09:15', images: ['https://via.placeholder.com/300x200', 'https://via.placeholder.com/300x200'], replies: [{ id: 1, name: '李主任', time: '2024-07-12 10:00', content: '已列入维修计划，预计下周开工。', photos: ['https://via.placeholder.com/300x200'] }] },
        { id: 3, demandType: 'dispute', title: '邻里土地边界纠纷', content: '与邻居王某某因宅基地边界问题存在争议，希望村委会出面调解。', status: 'PENDING', createTime: '2024-07-15 11:20', images: [] },
        { id: 4, demandType: 'safety', title: '村口路灯损坏', content: '村口主干道的路灯已经坏了三天了，晚上出行很不方便，存在安全隐患。', status: 'COMPLETED', createTime: '2024-07-08 19:30', images: [], processingRecord: '路灯已修复完成。', rating: 4 }
      ]
    }
  }
}

const onRefresh = async () => {
  isRefreshing.value = true
  page.value = 1
  finished.value = false
  await fetchMyDemands()
  isRefreshing.value = false
}

const onLoad = async () => {
  if (finished.value) return
  loading.value = true
  page.value++
  await fetchMyDemands()
  loading.value = false
}

onMounted(() => {
  fetchMyDemands()
})
</script>

<style scoped>
.my-demand-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80px 20px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 15px;
  color: #999;
  margin-bottom: 20px;
}

.demand-list {
  padding: 12px;
}

.demand-card {
  background: #fff;
  border-radius: 14px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  position: relative;
}

.demand-header {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.demand-type {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 4px;
  background: rgba(200, 16, 46, 0.1);
  color: #C8102E;
}

.demand-status {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 4px;
  font-weight: bold;
}

.demand-status.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.demand-status.processing {
  background: rgba(33, 150, 243, 0.2);
  color: #1976D2;
}

.demand-status.completed {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.demand-status.rejected {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.demand-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.demand-content {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 12px;
}

.demand-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.demand-time {
  font-size: 12px;
  color: #999;
}

.demand-images {
  display: flex;
  gap: 4px;
}

.demand-image {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  object-fit: cover;
}

.reply-badge {
  position: absolute;
  bottom: 16px;
  right: 16px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #1976D2;
  background: rgba(33, 150, 243, 0.1);
  padding: 4px 8px;
  border-radius: 4px;
}

.rating-badge {
  position: absolute;
  bottom: 16px;
  right: 16px;
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
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.detail-status.processing {
  background: rgba(33, 150, 243, 0.2);
  color: #1976D2;
}

.detail-status.completed {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.detail-status.rejected {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
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

.replies-section {
  background: #F9F9F9;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.replies-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
}

.reply-item {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.reply-name {
  font-size: 13px;
  font-weight: bold;
  color: #333;
}

.reply-time {
  font-size: 12px;
  color: #999;
}

.reply-content {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

.reply-photos {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-top: 8px;
}

.photo-item {
  width: 60px;
  height: 60px;
  border-radius: 6px;
  object-fit: cover;
}

.detail-result {
  background: #F6FFED;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 16px;
  border-left: 4px solid #4CAF50;
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
