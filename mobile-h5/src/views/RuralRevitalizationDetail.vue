<template>
  <div class="revitalization-detail-page page-container">
    <van-nav-bar title="帮扶项目详情" left-arrow @click-left="handleBack" />

    <div v-if="detail" class="detail-content">
      <div class="detail-header">
        <div class="header-icon">{{ detail.icon }}</div>
        <div class="header-info">
          <h2 class="header-title">{{ detail.title }}</h2>
          <span class="header-tag" :class="detail.status">{{ detail.status }}</span>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📋 项目信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">项目类型</span>
            <span class="info-value">{{ detail.type }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">投资金额</span>
            <span class="info-value highlight">{{ detail.investment }}万元</span>
          </div>
          <div class="info-item">
            <span class="info-label">受益户数</span>
            <span class="info-value">{{ detail.benefitHouseholds }}户</span>
          </div>
          <div class="info-item">
            <span class="info-label">实施时间</span>
            <span class="info-value">{{ detail.implementDate }}</span>
          </div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📝 项目简介</h3>
        <p class="card-desc">{{ detail.description }}</p>
      </div>

      <div class="info-card">
        <h3 class="card-title">🎯 项目目标</h3>
        <div class="goal-list">
          <div v-for="(goal, index) in detail.goals" :key="index" class="goal-item">
            <span class="goal-number">{{ index + 1 }}</span>
            <span class="goal-text">{{ goal }}</span>
          </div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">👥 责任单位</h3>
        <div class="unit-card">
          <div class="unit-name">{{ detail.unit }}</div>
          <div class="unit-contact">负责人：{{ detail.contactPerson }}</div>
          <div class="unit-phone">联系电话：{{ detail.contactPhone }}</div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📊 进度跟踪</h3>
        <div class="progress-list">
          <div v-for="(item, index) in detail.progress" :key="index" class="progress-item">
            <div class="progress-header">
              <span class="progress-date">{{ item.date }}</span>
              <span class="progress-status" :class="item.status">{{ item.status }}</span>
            </div>
            <div class="progress-desc">{{ item.desc }}</div>
          </div>
        </div>
      </div>

      <div v-if="isOfficial" class="action-bar">
        <van-button type="primary" @click="updateProgress">更新进度</van-button>
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
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const route = useRoute()
const detail = ref(null)
const isOfficial = true

const mockDetail = {
  id: 1,
  title: '特色农产品种植基地',
  type: '产业帮扶',
  investment: 320,
  benefitHouseholds: 20,
  implementDate: '2024-03',
  status: '进行中',
  icon: '🌾',
  description: '本项目计划建设现代化温室大棚50亩，引进优良品种和先进种植技术，发展有机蔬菜和特色水果种植产业。项目建成后，预计年产各类农产品100吨，产值达500万元，直接带动20户贫困户增收致富。',
  goals: [
    '建设50亩现代化温室大棚',
    '引进10个优良蔬菜品种',
    '培训农户50人次',
    '实现年产值500万元',
    '带动20户贫困户增收'
  ],
  unit: '县农业农村局',
  contactPerson: '李主任',
  contactPhone: '139****5678',
  progress: [
    { date: '2024-03-01', status: '已完成', desc: '项目立项审批通过' },
    { date: '2024-03-15', status: '已完成', desc: '土地平整完成' },
    { date: '2024-04-01', status: '进行中', desc: '大棚建设中，已完成60%' },
    { date: '2024-05-01', status: '待开始', desc: '预计开始种苗移栽' },
    { date: '2024-06-01', status: '待开始', desc: '预计正式投产' }
  ]
}

const handleBack = () => {
  goBack(router)
}

const updateProgress = () => {
  showToast('更新进度功能开发中')
}

const viewReport = () => {
  showToast('报告查看功能开发中')
}

const fetchDetail = async () => {
  const id = route.params.id
  const type = route.query.type
  try {
    const res = await request.get(`/rural-revitalization/detail/${id}`, { params: { type } })
    detail.value = res.data || mockDetail
  } catch (error) {
    console.error('获取帮扶项目详情失败', error)
    detail.value = mockDetail
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.revitalization-detail-page {
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
  margin-bottom: 8px;
}

.header-tag {
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.2);
}

.header-tag.进行中 {
  background: rgba(33, 150, 243, 0.8);
}

.header-tag.已完成 {
  background: rgba(76, 175, 80, 0.8);
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

.info-value.highlight {
  color: #D22630;
}

.card-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.goal-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.goal-item {
  display: flex;
  align-items: center;
  padding: 10px;
  background: #fafafa;
  border-radius: 10px;
}

.goal-number {
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
}

.goal-text {
  font-size: 14px;
  color: #333;
}

.unit-card {
  background: #fafafa;
  border-radius: 10px;
  padding: 14px;
}

.unit-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.unit-contact, .unit-phone {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.progress-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.progress-item {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
}

.progress-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.progress-date {
  font-size: 13px;
  font-weight: bold;
  color: #333;
}

.progress-status {
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 8px;
}

.progress-status.已完成 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.progress-status.进行中 {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.progress-status.待开始 {
  background: rgba(158, 158, 158, 0.2);
  color: #9E9E9E;
}

.progress-desc {
  font-size: 13px;
  color: #666;
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