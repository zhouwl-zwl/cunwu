<template>
  <div class="assets-detail-page page-container">
    <van-nav-bar :title="pageTitle" left-arrow @click-left="handleBack" />

    <div v-if="detail" class="detail-content">
      <div class="detail-header">
        <div class="header-icon">{{ detail.icon }}</div>
        <div class="header-info">
          <h2 class="header-title">{{ detail.name }}</h2>
          <span class="header-tag" :class="detail.type">{{ getTypeLabel(detail.type) }}</span>
        </div>
      </div>

      <div v-if="detail.type === 'building' || detail.type === 'equipment' || detail.type === 'vehicle'" class="asset-detail">
        <div class="info-card">
          <h3 class="card-title">📋 基本信息</h3>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">资产编号</span>
              <span class="info-value">{{ detail.code }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">资产类型</span>
              <span class="info-value">{{ getAssetTypeLabel(detail.type) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">购置日期</span>
              <span class="info-value">{{ detail.purchaseDate }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">使用年限</span>
              <span class="info-value">{{ detail.serviceLife }}年</span>
            </div>
            <div class="info-item">
              <span class="info-label">原值</span>
              <span class="info-value highlight">¥{{ detail.value.toLocaleString() }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">净值</span>
              <span class="info-value">¥{{ detail.netValue.toLocaleString() }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">折旧率</span>
              <span class="info-value">{{ detail.depreciationRate }}%</span>
            </div>
            <div class="info-item">
              <span class="info-label">使用状态</span>
              <span class="info-value" :class="{ warning: detail.status === '闲置' }">{{ detail.status }}</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <h3 class="card-title">📝 资产简介</h3>
          <p class="card-desc">{{ detail.description }}</p>
        </div>

        <div class="info-card">
          <h3 class="card-title">📍 存放位置</h3>
          <div class="location-info">
            <div class="location-item">
              <span class="location-label">所在地址</span>
              <span class="location-value">{{ detail.location }}</span>
            </div>
            <div class="location-item">
              <span class="location-label">保管人</span>
              <span class="location-value">{{ detail.custodian }}</span>
            </div>
            <div class="location-item">
              <span class="location-label">联系电话</span>
              <span class="location-value">{{ detail.phone }}</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <h3 class="card-title">📊 折旧记录</h3>
          <div class="depreciation-list">
            <div v-for="(record, index) in detail.depreciationRecords" :key="index" class="depreciation-item">
              <div class="depreciation-header">
                <span class="depreciation-year">{{ record.year }}</span>
                <span class="depreciation-amount">-¥{{ record.amount.toLocaleString() }}</span>
              </div>
              <div class="depreciation-desc">{{ record.description }}</div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="resource-detail">
        <div class="info-card">
          <h3 class="card-title">📋 资源信息</h3>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">资源类型</span>
              <span class="info-value">{{ getResourceTypeLabel(detail.type) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">面积</span>
              <span class="info-value highlight">{{ detail.area }}亩</span>
            </div>
            <div class="info-item">
              <span class="info-label">位置</span>
              <span class="info-value">{{ detail.location }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">用途</span>
              <span class="info-value">{{ detail.useType }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">承包状态</span>
              <span class="info-value" :class="{ warning: detail.contractStatus === '未承包' }">{{ detail.contractStatus }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">评估价值</span>
              <span class="info-value highlight">¥{{ detail.valuation.toLocaleString() }}</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <h3 class="card-title">📝 资源简介</h3>
          <p class="card-desc">{{ detail.description }}</p>
        </div>

        <div v-if="detail.contractStatus === '已承包'" class="info-card">
          <h3 class="card-title">📋 承包信息</h3>
          <div class="contract-info">
            <div class="contract-item">
              <span class="contract-label">承包人</span>
              <span class="contract-value">{{ detail.contractor }}</span>
            </div>
            <div class="contract-item">
              <span class="contract-label">承包金额</span>
              <span class="contract-value highlight">¥{{ detail.contractAmount.toLocaleString() }}/年</span>
            </div>
            <div class="contract-item">
              <span class="contract-label">承包期限</span>
              <span class="contract-value">{{ detail.contractStart }} - {{ detail.contractEnd }}</span>
            </div>
            <div class="contract-item">
              <span class="contract-label">联系电话</span>
              <span class="contract-value">{{ detail.contractorPhone }}</span>
            </div>
          </div>
        </div>

        <div class="info-card">
          <h3 class="card-title">📊 收益记录</h3>
          <div class="income-list">
            <div v-for="(record, index) in detail.incomeRecords" :key="index" class="income-item">
              <div class="income-header">
                <span class="income-date">{{ record.date }}</span>
                <span class="income-amount">+¥{{ record.amount.toLocaleString() }}</span>
              </div>
              <div class="income-desc">{{ record.description }}</div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="isOfficial" class="action-bar">
        <van-button type="primary" @click="editAsset">编辑信息</van-button>
        <van-button type="warning" @click="viewReport">查看报表</van-button>
      </div>
    </div>

    <div v-else class="loading-state">
      <van-loading type="spinner" color="#D22630" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const route = useRoute()
const detail = ref(null)
const isOfficial = true

const pageTitle = computed(() => {
  const type = route.query.type
  return type === 'resource' ? '资源详情' : '资产详情'
})

const assetMockDetail = {
  id: 1,
  name: '村办公楼',
  type: 'building',
  code: 'ZC001',
  icon: '🏢',
  purchaseDate: '2010-05-20',
  serviceLife: 30,
  value: 5000000,
  netValue: 3500000,
  depreciationRate: 5,
  status: '使用中',
  location: '村中心广场北侧',
  custodian: '王主任',
  phone: '139****5678',
  description: '村办公楼建于2010年，建筑面积1200平方米，包含会议室、办公室、便民服务大厅等功能区域，是全村政务办公和公共服务的核心场所。',
  depreciationRecords: [
    { year: '2021年', amount: 250000, description: '年度折旧计提' },
    { year: '2022年', amount: 250000, description: '年度折旧计提' },
    { year: '2023年', amount: 250000, description: '年度折旧计提' },
    { year: '2024年', amount: 250000, description: '年度折旧计提' }
  ]
}

const resourceMockDetail = {
  id: 1,
  name: '村集体林地',
  type: 'forest',
  icon: '🌲',
  area: 500,
  location: '村西山坡',
  useType: '林业生产',
  contractStatus: '已承包',
  valuation: 1500000,
  description: '村集体林地位于村西山坡，总面积500亩，主要种植松树、杉树等经济林木。林地生态环境良好，具有较高的经济价值和生态价值。',
  contractor: '刘林业',
  contractAmount: 50000,
  contractStart: '2022-01-01',
  contractEnd: '2031-12-31',
  contractorPhone: '138****9012',
  incomeRecords: [
    { date: '2022年', amount: 50000, description: '林地承包款' },
    { date: '2023年', amount: 50000, description: '林地承包款' },
    { date: '2024年', amount: 50000, description: '林地承包款' }
  ]
}

const handleBack = () => {
  goBack(router)
}

const editAsset = () => {
  showToast('编辑功能开发中')
}

const viewReport = () => {
  showToast('报表查看功能开发中')
}

const getTypeLabel = (type) => {
  const map = {
    'building': '房屋建筑',
    'equipment': '机械设备',
    'vehicle': '交通工具',
    'forest': '林地',
    'water': '水域',
    'land': '耕地'
  }
  return map[type] || type
}

const getAssetTypeLabel = (type) => {
  const map = {
    'building': '房屋建筑',
    'equipment': '机械设备',
    'vehicle': '交通工具'
  }
  return map[type] || type
}

const getResourceTypeLabel = (type) => {
  const map = {
    'land': '耕地',
    'forest': '林地',
    'water': '水域'
  }
  return map[type] || type
}

const fetchDetail = async () => {
  const id = route.params.id
  const type = route.query.type
  try {
    const res = await request.get(`/asset/detail/${id}`, { params: { type } })
    detail.value = res.data || (type === 'resource' ? resourceMockDetail : assetMockDetail)
  } catch (error) {
    console.error('获取资产详情失败', error)
    detail.value = type === 'resource' ? resourceMockDetail : assetMockDetail
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped>
.assets-detail-page {
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

.info-value.highlight {
  color: #D22630;
}

.info-value.warning {
  color: #F44336;
}

.card-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  background: #fafafa;
  padding: 12px;
  border-radius: 10px;
}

.location-info, .contract-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.location-item, .contract-item {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  background: #fafafa;
  border-radius: 10px;
}

.location-label, .contract-label {
  font-size: 13px;
  color: #999;
}

.location-value, .contract-value {
  font-size: 13px;
  color: #333;
  font-weight: bold;
}

.contract-value.highlight {
  color: #D22630;
}

.depreciation-list, .income-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.depreciation-item, .income-item {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
}

.depreciation-header, .income-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.depreciation-year, .income-date {
  font-size: 13px;
  font-weight: bold;
  color: #333;
}

.depreciation-amount {
  font-size: 14px;
  font-weight: bold;
  color: #F44336;
}

.income-amount {
  font-size: 14px;
  font-weight: bold;
  color: #4CAF50;
}

.depreciation-desc, .income-desc {
  font-size: 12px;
  color: #999;
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