<template>
  <div class="demand-management page-container">
    <div class="page-header">
      <h2>诉求工单管理</h2>
    </div>

    <div class="filter-bar">
      <div 
        v-for="filter in statusFilters" 
        :key="filter.value"
        class="filter-item"
        :class="{ active: statusFilter === filter.value }"
        @click="statusFilter = filter.value"
      >
        {{ filter.label }}
      </div>
    </div>

    <div v-if="demandList.length === 0" class="empty-state">
      <div class="empty-icon">📋</div>
      <p>暂无诉求工单</p>
    </div>

    <div v-else class="news-list">
      <div 
        v-for="item in demandList" 
        :key="item.id" 
        class="news-card" 
        @click="viewDetail(item)"
      >
        <div class="news-content">
          <div class="news-tags">
            <span :class="['news-status', getStatusClass(item.status)]">{{ getStatusName(item.status) }}</span>
            <span class="news-type">{{ getTypeText(item.category || item.demandType) }}</span>
          </div>
          <h3 class="news-title">{{ item.title || '诉求' }}</h3>
          <p class="news-summary">{{ (item.content || '').substring(0, 120) }}...</p>
          <div class="news-meta">
            <span class="news-author">{{ item.name || '匿名' }}</span>
            <span class="news-time">{{ item.createTime }}</span>
          </div>
        </div>
        <div v-if="item.images && getFirstImage(item.images)" class="news-image-wrapper">
          <img :src="getFirstImage(item.images)" class="news-image" />
        </div>
        <div v-if="isAdmin" class="news-actions">
          <button 
            class="action-btn pass" 
            @click.stop="auditItem(item, 'PASS')" 
            :disabled="item.status === 'COMPLETED'"
          >通过</button>
          <button 
            class="action-btn reject" 
            @click.stop="auditItem(item, 'REJECT')" 
            :disabled="item.status === 'COMPLETED'"
          >驳回</button>
        </div>
      </div>
    </div>

    <div v-if="showDetailDialog" class="modal-overlay" @click="closeDetailDialog">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>工单详情</h3>
          <button class="modal-close" @click="closeDetailDialog">✕</button>
        </div>
        <div v-if="currentDemand" class="detail-content">
          <div class="detail-header">
            <span :class="['detail-status', getStatusClass(currentDemand.status)]">{{ getStatusName(currentDemand.status) }}</span>
            <span class="detail-type">{{ getTypeText(currentDemand.category || currentDemand.demandType) }}</span>
            <span class="detail-id">工单编号：{{ currentDemand.id }}</span>
          </div>
          <h3 class="detail-title">{{ currentDemand.title || '诉求' }}</h3>
          <div class="detail-meta">
            <span class="detail-author">提交人：{{ currentDemand.name }}</span>
            <span class="detail-phone">联系电话：{{ currentDemand.phone }}</span>
            <span class="detail-time">提交时间：{{ currentDemand.createTime }}</span>
          </div>
          <div class="detail-body">
            <h4>诉求内容：</h4>
            <p>{{ currentDemand.content }}</p>
          </div>
          <div v-if="currentDemand.images && getImagesArray(currentDemand.images).length > 0" class="detail-images">
            <h4>相关图片：</h4>
            <div class="image-grid">
              <img v-for="(img, idx) in getImagesArray(currentDemand.images)" :key="idx" :src="img" class="detail-image" />
            </div>
          </div>
          <div v-if="currentDemand.processingRecord" class="detail-result">
            <h4>处理记录：</h4>
            <p>{{ currentDemand.processingRecord }}</p>
          </div>
        </div>
        <div class="modal-footer">
          <button class="modal-btn cancel" @click="closeDetailDialog">关闭</button>
          <button 
            v-if="isAdmin && currentDemand?.status !== 'COMPLETED'" 
            class="modal-btn confirm" 
            @click="showHandleDialog = true"
          >处理工单</button>
        </div>
      </div>
    </div>

    <div v-if="showHandleDialog" class="modal-overlay" @click="showHandleDialog = false">
      <div class="modal-content small" @click.stop>
        <div class="modal-header">
          <h3>处理工单</h3>
          <button class="modal-close" @click="showHandleDialog = false">✕</button>
        </div>
        <div class="form-content">
          <div class="form-item">
            <label>处理方案 <span class="required">*</span></label>
            <textarea v-model="handleForm.processingRecord" rows="4" placeholder="请输入处理方案和结果"></textarea>
          </div>
          <div class="form-item">
            <label>处理状态 <span class="required">*</span></label>
            <select v-model="handleForm.status">
              <option value="PROCESSING">处理中</option>
              <option value="COMPLETED">已办结</option>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button class="modal-btn cancel" @click="showHandleDialog = false">取消</button>
          <button class="modal-btn confirm" @click="submitHandle">提交处理</button>
        </div>
      </div>
    </div>

    <div v-if="showRejectDialog" class="modal-overlay" @click="showRejectDialog = false">
      <div class="modal-content small" @click.stop>
        <div class="modal-header">
          <h3>驳回工单</h3>
          <button class="modal-close" @click="showRejectDialog = false">✕</button>
        </div>
        <div class="form-content">
          <div class="form-item">
            <label>驳回原因 <span class="required">*</span></label>
            <textarea v-model="rejectForm.reason" rows="3" placeholder="请输入驳回原因"></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button class="modal-btn cancel" @click="showRejectDialog = false">取消</button>
          <button class="modal-btn danger" @click="submitReject">确认驳回</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue'
import request from '../utils/request'

const demandList = ref([])
const statusFilter = ref('')
const showDetailDialog = ref(false)
const showHandleDialog = ref(false)
const showRejectDialog = ref(false)
const currentDemand = ref(null)

const statusFilters = [
  { label: '全部', value: '' },
  { label: '待受理', value: 'PENDING' },
  { label: '处理中', value: 'PROCESSING' },
  { label: '已办结', value: 'COMPLETED' }
]

const handleForm = reactive({
  processingRecord: '',
  status: 'PROCESSING'
})

const rejectForm = reactive({
  reason: ''
})

const isAdmin = computed(() => {
  return localStorage.getItem('role') === 'ADMIN'
})

const getStatusName = (status) => {
  if (status === 'PENDING') return '待受理'
  if (status === 'PROCESSING') return '处理中'
  if (status === 'COMPLETED') return '已办结'
  if (status === 'REJECTED') return '已驳回'
  return status || '未知'
}

const getStatusClass = (status) => {
  if (status === 'PENDING') return 'pending'
  if (status === 'PROCESSING') return 'processing'
  if (status === 'COMPLETED') return 'completed'
  if (status === 'REJECTED') return 'rejected'
  return 'pending'
}

const getTypeText = (type) => {
  const map = { 
    'dispute': '矛盾纠纷', 'environment': '环境问题', 'road': '道路损坏', 'safety': '安全隐患', 'other': '其他',
    'DISPUTE': '矛盾纠纷', 'ENVIRONMENT': '环境问题', 'ROAD': '道路损坏', 'SAFETY': '安全隐患', 'OTHER': '其他',
    'DISPUTE_CATEGORY': '矛盾纠纷', 'ENVIRONMENT_CATEGORY': '环境问题', 'ROAD_CATEGORY': '道路损坏', 'SAFETY_CATEGORY': '安全隐患', 'OTHER_CATEGORY': '其他'
  }
  return map[type] || (type || '其他')
}

const getFirstImage = (images) => {
  if (!images) return ''
  if (typeof images === 'string') {
    try {
      const arr = JSON.parse(images)
      return arr[0] || ''
    } catch {
      return images
    }
  }
  return images[0] || ''
}

const getImagesArray = (images) => {
  if (!images) return []
  if (typeof images === 'string') {
    try {
      return JSON.parse(images)
    } catch {
      return [images]
    }
  }
  return images || []
}

const fetchDemands = async () => {
  try {
    const res = await request.get('/demand/list', { params: { status: statusFilter.value } })
    demandList.value = res.data.records || []
  } catch (error) {
    console.error('获取工单列表失败', error)
  }
}

const viewDetail = (row) => {
  currentDemand.value = row
  showDetailDialog.value = true
}

const closeDetailDialog = () => {
  showDetailDialog.value = false
  currentDemand.value = null
}

const auditItem = (item, action) => {
  currentDemand.value = item
  if (action === 'REJECT') {
    rejectForm.reason = ''
    showRejectDialog.value = true
  } else {
    handleForm.processingRecord = ''
    handleForm.status = 'PROCESSING'
    showHandleDialog.value = true
  }
}

const submitHandle = async () => {
  if (!handleForm.processingRecord) {
    alert('请填写处理方案')
    return
  }
  try {
    await request.put(`/demand/${currentDemand.value.id}`, handleForm)
    alert('处理成功')
    showHandleDialog.value = false
    closeDetailDialog()
    fetchDemands()
  } catch (error) {
    alert('处理失败')
  }
}

const submitReject = async () => {
  if (!rejectForm.reason) {
    alert('请填写驳回原因')
    return
  }
  try {
    await request.put(`/demand/${currentDemand.value.id}`, { 
      status: 'REJECTED', 
      processingRecord: `驳回原因：${rejectForm.reason}` 
    })
    alert('驳回成功')
    showRejectDialog.value = false
    closeDetailDialog()
    fetchDemands()
  } catch (error) {
    alert('驳回失败')
  }
}

watch(statusFilter, () => {
  fetchDemands()
})

onMounted(() => {
  fetchDemands()
})
</script>

<style scoped>
.demand-management {
  padding: 16px;
  background: #FDF5F5;
  min-height: 100%;
}

.page-container {
  min-height: 100%;
}

.page-header {
  margin-bottom: 16px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.filter-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.filter-bar::-webkit-scrollbar {
  display: none;
}

.filter-item {
  padding: 8px 16px;
  background: #FFFFFF;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: all 0.2s;
}

.filter-item.active {
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  color: #FFFFFF;
  box-shadow: 0 4px 12px rgba(200, 16, 46, 0.3);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-state p {
  font-size: 16px;
  color: #999;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.news-card {
  background: #FFFFFF;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(200, 16, 46, 0.08);
  transition: transform 0.2s;
}

.news-card:active {
  transform: scale(0.98);
}

.news-content {
  display: flex;
  flex-direction: column;
}

.news-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.news-status {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 4px;
  font-weight: 500;
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

.news-type {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 4px;
  background: #f5f5f5;
  color: #666;
}

.news-title {
  font-size: 17px;
  font-weight: bold;
  color: #333;
  line-height: 1.4;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-summary {
  font-size: 14px;
  color: #999;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  align-items: center;
  gap: 16px;
}

.news-author {
  font-size: 13px;
  color: #666;
}

.news-time {
  font-size: 13px;
  color: #bbb;
}

.news-image-wrapper {
  width: 100%;
  height: 160px;
  border-radius: 10px;
  overflow: hidden;
  margin-top: 12px;
}

.news-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.news-actions {
  display: flex;
  gap: 12px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.action-btn {
  flex: 1;
  padding: 10px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: bold;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.action-btn.pass {
  background: linear-gradient(135deg, #67c23a 0%, #52c41a 100%);
  color: #FFFFFF;
}

.action-btn.reject {
  background: linear-gradient(135deg, #f56c6c 0%, #ef4444 100%);
  color: #FFFFFF;
}

.action-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 300;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: fadeIn 0.2s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-content {
  width: 90%;
  max-width: 500px;
  background: #FFFFFF;
  border-radius: 16px;
  overflow: hidden;
  animation: slideUp 0.3s ease;
}

.modal-content.small {
  max-width: 400px;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
}

.modal-header h3 {
  font-size: 18px;
  font-weight: bold;
  color: #FFFFFF;
  margin: 0;
}

.modal-close {
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 8px;
  color: #FFFFFF;
  font-size: 18px;
  cursor: pointer;
}

.detail-content {
  padding: 20px;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.detail-status {
  font-size: 13px;
  padding: 4px 12px;
  border-radius: 4px;
  font-weight: 500;
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
  font-size: 13px;
  padding: 4px 12px;
  border-radius: 4px;
  background: #f5f5f5;
  color: #666;
}

.detail-id {
  font-size: 13px;
  color: #999;
}

.detail-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
}

.detail-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.detail-author, .detail-phone, .detail-time {
  font-size: 14px;
  color: #666;
}

.detail-body {
  margin-bottom: 20px;
}

.detail-body h4 {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.detail-body p {
  font-size: 15px;
  color: #333;
  line-height: 1.8;
}

.detail-images {
  margin-bottom: 20px;
}

.detail-images h4 {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
}

.image-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.detail-image {
  width: calc(33.33% - 8px);
  aspect-ratio: 1;
  object-fit: cover;
  border-radius: 8px;
  cursor: pointer;
}

.detail-result {
  background: #f9f9f9;
  padding: 16px;
  border-radius: 10px;
  margin-bottom: 16px;
}

.detail-result h4 {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.detail-result p {
  font-size: 14px;
  color: #666;
  line-height: 1.7;
}

.form-content {
  padding: 20px;
}

.form-item {
  margin-bottom: 16px;
}

.form-item label {
  display: block;
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.required {
  color: #D22630;
}

.form-item textarea,
.form-item select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  box-sizing: border-box;
  resize: none;
}

.form-item textarea:focus,
.form-item select:focus {
  outline: none;
  border-color: #C8102E;
}

.modal-footer {
  display: flex;
  gap: 12px;
  padding: 16px 20px;
  border-top: 1px solid #f0f0f0;
}

.modal-btn {
  flex: 1;
  padding: 12px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: bold;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.modal-btn.cancel {
  background: #f5f5f5;
  color: #666;
}

.modal-btn.confirm {
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  color: #FFFFFF;
}

.modal-btn.danger {
  background: linear-gradient(135deg, #f56c6c 0%, #ef4444 100%);
  color: #FFFFFF;
}

@media (min-width: 768px) {
  .demand-management {
    padding: 24px;
  }
  
  .news-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
    gap: 20px;
  }
  
  .news-card {
    flex-direction: row;
  }
  
  .news-content {
    margin-right: 16px;
  }
  
  .news-image-wrapper {
    width: 140px;
    height: 110px;
    margin-top: 0;
    flex-shrink: 0;
  }
  
  .modal-content {
    max-width: 700px;
  }
  
  .modal-content.small {
    max-width: 600px;
  }
}
</style>
