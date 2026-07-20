<template>
  <div class="industry-detail-page page-container">
    <van-nav-bar title="产业详情" left-arrow @click-left="handleBack" />

    <div v-if="detail" class="detail-content">
      <div class="detail-header">
        <div class="header-icon">{{ detail.icon }}</div>
        <div class="header-info">
          <h2 class="header-title">{{ detail.name }}</h2>
          <span class="header-tag" :class="detail.status">{{ detail.status === '运营中' ? '运营中' : '筹备中' }}</span>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📋 基本信息</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">类型</span>
            <span class="info-value">{{ detail.type }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">负责人</span>
            <span class="info-value">{{ detail.manager }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">成立时间</span>
            <span class="info-value">{{ detail.establishDate }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">注册资金</span>
            <span class="info-value">{{ detail.registeredCapital }}万元</span>
          </div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📊 经营数据</h3>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">{{ detail.annualIncome }}</div>
            <div class="stat-label">年产值(万元)</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ detail.employment }}</div>
            <div class="stat-label">就业人数(人)</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ detail.members }}</div>
            <div class="stat-label">成员户数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ detail.profitRate }}%</div>
            <div class="stat-label">利润率</div>
          </div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📝 项目简介</h3>
        <p class="card-desc">{{ detail.description }}</p>
      </div>

      <div class="info-card">
        <h3 class="card-title">🌾 主要产品</h3>
        <div class="product-list">
          <div v-for="product in detail.products" :key="product.id" class="product-item">
            <div class="product-icon">{{ product.icon }}</div>
            <div class="product-info">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-desc">{{ product.desc }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="info-card">
        <h3 class="card-title">📈 发展历程</h3>
        <div class="timeline">
          <div v-for="(event, index) in detail.history" :key="index" class="timeline-item">
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <div class="timeline-date">{{ event.date }}</div>
              <div class="timeline-desc">{{ event.desc }}</div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="isOfficial" class="action-bar">
        <van-button type="primary" @click="editIndustry">编辑信息</van-button>
        <van-button type="warning" @click="viewReport">查看报表</van-button>
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
  name: '罗卜田乡生态农业合作社',
  type: '种植养殖',
  manager: '张建国',
  establishDate: '2018-03-15',
  registeredCapital: 500,
  annualIncome: 850,
  employment: 85,
  members: 45,
  profitRate: 28,
  status: '运营中',
  icon: '🌾',
  description: '罗卜田乡生态农业合作社成立于2018年，是全村首个规模化农业专业合作社。合作社主要从事有机蔬菜种植、家禽养殖和农产品加工销售。通过引入现代化种植技术和科学管理模式，产品远销省内外各大超市和农贸市场，成为当地知名的绿色农产品品牌。',
  products: [
    { id: 1, name: '有机蔬菜', desc: '西红柿、黄瓜、茄子等20余种', icon: '🥬' },
    { id: 2, name: '生态土鸡', desc: '散养土鸡及土鸡蛋', icon: '🐔' },
    { id: 3, name: '有机水果', desc: '草莓、蓝莓、樱桃等', icon: '🍓' },
    { id: 4, name: '农产品加工', desc: '酱菜、干货、调味品', icon: '📦' }
  ],
  history: [
    { date: '2018年', desc: '合作社正式成立，首批成员20户' },
    { date: '2020年', desc: '获得有机认证，产品进入省级超市' },
    { date: '2021年', desc: '扩建温室大棚，面积达50亩' },
    { date: '2023年', desc: '年产值突破800万元，成员增至45户' }
  ]
}

const handleBack = () => {
  goBack(router)
}

const editIndustry = () => {
  showToast('编辑功能开发中')
}

const viewReport = () => {
  showToast('报表查看功能开发中')
}

const fetchDetail = async () => {
  const id = route.params.id
  try {
    const res = await request.get(`/industry/detail/${id}`)
    detail.value = res.data || mockDetail
  } catch (error) {
    console.error('获取产业详情失败', error)
    detail.value = mockDetail
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.industry-detail-page {
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
}

.stat-item {
  text-align: center;
  padding: 12px 8px;
  background: #fafafa;
  border-radius: 10px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #D22630;
}

.stat-label {
  font-size: 11px;
  color: #999;
  margin-top: 4px;
}

.card-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.product-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 10px;
  background: #fafafa;
  border-radius: 10px;
}

.product-icon {
  font-size: 28px;
  margin-right: 12px;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 2px;
}

.product-desc {
  font-size: 12px;
  color: #999;
}

.timeline {
  position: relative;
  padding-left: 20px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 6px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: #eee;
}

.timeline-item {
  position: relative;
  margin-bottom: 16px;
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -16px;
  top: 4px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #D22630;
}

.timeline-content {
  padding-left: 8px;
}

.timeline-date {
  font-size: 13px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.timeline-desc {
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