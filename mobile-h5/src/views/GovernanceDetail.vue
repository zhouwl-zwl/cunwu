<template>
  <div class="governance-detail-page page-container">
    <van-nav-bar title="纠纷详情" left-arrow @click-left="handleBack" />

    <div v-if="detail" class="detail-content">
      <div class="detail-header">
        <div class="header-icon">⚖️</div>
        <div class="header-info">
          <h2 class="header-title">{{ detail.title }}</h2>
          <div class="header-tags">
            <span class="tag type" :class="detail.type">{{ getTypeLabel(detail.type) }}</span>
            <span class="tag status" :class="detail.status">{{ getStatusLabel(detail.status) }}</span>
          </div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📋 基本信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">案件编号</span>
            <span class="info-value">{{ detail.code }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">登记时间</span>
            <span class="info-value">{{ detail.createTime }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">当事人</span>
            <span class="info-value">{{ detail.parties }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">调解员</span>
            <span class="info-value">{{ detail.mediator }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">联系电话</span>
            <span class="info-value">{{ detail.phone }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">所在网格</span>
            <span class="info-value">{{ detail.gridName }}</span>
          </div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📝 纠纷描述</h3>
        <p class="card-desc">{{ detail.description }}</p>
      </div>

      <div class="info-card">
        <h3 class="card-title">📋 诉求内容</h3>
        <div class="demand-list">
          <div v-for="(demand, index) in detail.demands" :key="index" class="demand-item">
            <span class="demand-number">{{ index + 1 }}</span>
            <span class="demand-text">{{ demand }}</span>
          </div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📊 调解记录</h3>
        <div class="record-list">
          <div v-for="(record, index) in detail.mediationRecords" :key="index" class="record-item">
            <div class="record-header">
              <span class="record-date">{{ record.date }}</span>
              <span class="record-status" :class="record.status">{{ record.status }}</span>
            </div>
            <div class="record-content">
              <div class="record-desc">{{ record.description }}</div>
              <div v-if="record.participants" class="record-participants">参与人员：{{ record.participants }}</div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="detail.result" class="info-card">
        <h3 class="card-title">✅ 调解结果</h3>
        <div class="result-content">
          <div class="result-item">
            <span class="result-label">处理结果</span>
            <span class="result-value" :class="detail.result.type">{{ getResultLabel(detail.result.type) }}</span>
          </div>
          <div class="result-item">
            <span class="result-label">达成协议</span>
            <span class="result-value">{{ detail.result.agreement }}</span>
          </div>
          <div class="result-item">
            <span class="result-label">协议内容</span>
            <span class="result-desc">{{ detail.result.content }}</span>
          </div>
          <div class="result-item">
            <span class="result-label">签字日期</span>
            <span class="result-value">{{ detail.result.signDate }}</span>
          </div>
        </div>
      </div>

      <div v-if="isOfficial" class="action-bar">
        <van-button type="primary" @click="addRecord">添加调解记录</van-button>
        <van-button type="warning" @click="viewReport">查看报告</van-button>
      </div>
    </div>

    <div v-else class="loading-state">
      <van-loading type="spinner" color="#D22630" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import { goBack } from '../utils/index'
import request from '../utils/request'

const router = useRouter()
const route = useRoute()
const detail = ref(null)
const isOfficial = true

const mockDetail = {
  id: 1,
  code: 'JJ20240115001',
  title: '邻里噪音扰民纠纷',
  type: 'neighbor',
  status: 'pending',
  createTime: '2024-01-15 10:30',
  parties: '张某 vs 李某',
  mediator: '王支书',
  phone: '138****1234',
  gridName: '第一网格',
  description: '张姓居民反映邻居李姓居民夜间噪音过大，影响正常休息。据反映，李某家中经常在晚上11点以后播放音乐、进行装修等活动，导致张某及其家人无法正常入睡。双方曾多次沟通，但未能达成一致意见。',
  demands: [
    '要求李某在晚上10点后停止一切扰民活动',
    '要求李某对造成的影响进行道歉',
    '如果继续扰民，要求村委会介入处理'
  ],
  mediationRecords: [
    { date: '2024-01-15 14:00', status: '已完成', description: '接到张某反映后，第一时间联系双方了解情况，初步掌握纠纷基本情况。', participants: '王支书、张某' },
    { date: '2024-01-16 09:30', status: '已完成', description: '组织双方进行第一次调解，听取双方陈述，李某承认夜间有噪音，但认为是正常生活声音。', participants: '王支书、张某、李某' },
    { date: '2024-01-17 10:00', status: '进行中', description: '邀请网格员一起再次调解，向李某宣传《治安管理处罚法》相关规定。', participants: '王支书、网格员、张某、李某' }
  ],
  result: null
}

const handleBack = () => {
  goBack(router)
}

const addRecord = () => {
  showToast('添加调解记录功能开发中')
}

const viewReport = () => {
  showToast('报告查看功能开发中')
}

const getTypeLabel = (type) => {
  const map = {
    'neighbor': '邻里纠纷',
    'land': '土地纠纷',
    'family': '婚姻家庭',
    'economic': '经济纠纷'
  }
  return map[type] || type
}

const getStatusLabel = (status) => {
  const map = {
    'pending': '待调解',
    'processing': '调解中',
    'resolved': '已达成协议',
    'failed': '未达成协议'
  }
  return map[status] || status
}

const getResultLabel = (type) => {
  const map = {
    'success': '调解成功',
    'failed': '调解失败',
    'partial': '部分达成'
  }
  return map[type] || type
}

const fetchDetail = async () => {
  const id = route.params.id
  try {
    const res = await request.get(`/governance/dispute/detail/${id}`)
    detail.value = res.data || mockDetail
  } catch (error) {
    console.error('获取纠纷详情失败', error)
    detail.value = mockDetail
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.governance-detail-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.detail-content {
  padding: 12px;
}

.detail-header {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  padding: 24px;
  border-radius: 14px;
  margin-bottom: 12px;
  color: #fff;
}

.header-icon {
  font-size: 48px;
  margin-right: 16px;
}

.header-info {
  flex: 1;
}

.header-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.header-tags {
  display: flex;
  gap: 10px;
}

.tag {
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.2);
}

.info-card {
  background: #fff;
  border-radius: 14px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 14px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.info-item {
  display: flex;
  flex-direction: column;
  padding: 10px;
  background: #fafafa;
  border-radius: 10px;
}

.info-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: bold;
}

.card-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  background: #fafafa;
  padding: 12px;
  border-radius: 10px;
}

.demand-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.demand-item {
  display: flex;
  align-items: flex-start;
  padding: 10px;
  background: #fafafa;
  border-radius: 10px;
}

.demand-number {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #D22630;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  margin-right: 12px;
  flex-shrink: 0;
}

.demand-text {
  font-size: 14px;
  color: #333;
  line-height: 1.5;
}

.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.record-item {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.record-date {
  font-size: 13px;
  font-weight: bold;
  color: #333;
}

.record-status {
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 8px;
}

.record-status.已完成 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.record-status.进行中 {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.record-content {
  padding-top: 8px;
  border-top: 1px solid #eee;
}

.record-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
}

.record-participants {
  font-size: 12px;
  color: #999;
  margin-top: 6px;
}

.result-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.result-item {
  display: flex;
  flex-direction: column;
  padding: 10px;
  background: #fafafa;
  border-radius: 10px;
}

.result-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.result-value {
  font-size: 14px;
  color: #333;
  font-weight: bold;
}

.result-value.success {
  color: #4CAF50;
}

.result-value.failed {
  color: #F44336;
}

.result-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
}

.action-bar {
  display: flex;
  gap: 12px;
  padding: 16px 12px;
}

.action-bar .van-button {
  flex: 1;
}

.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}
</style>