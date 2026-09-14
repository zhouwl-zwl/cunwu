<template>
  <div class="home-page page-container">
    <!-- 紧凑Banner + 搜索 -->
    <div class="hero">
      <div class="hero-bg">
        <img src="/banner.jpg" alt="罗卜田乡" loading="eager" />
        <div class="hero-mask"></div>
      </div>
      <div class="hero-content">
        <div class="hero-title">罗卜田乡</div>
        <div class="hero-subtitle">共建美好家园</div>
      </div>
      <div class="search-bar" @click="goSearch">
        <van-icon name="search" size="16" color="#999" />
        <span class="search-placeholder">搜索政策、服务、通知...</span>
      </div>
    </div>

    <!-- 紧急联系条 -->
    <div class="emergency-bar" @click="showEmergency">
      <span class="emergency-icon">🔔</span>
      <span class="emergency-text">紧急联系 · 24小时</span>
      <span class="emergency-desc">村委会 138-0013-8000</span>
      <van-icon name="arrow" size="14" color="#C8102E" />
    </div>

    <!-- 基本村情 — 紧凑横条 -->
    <div class="stats-bar">
      <div class="stat-item" @click="goPage('/village-info')">
        <div class="stat-value">{{ villageInfo?.population || 8849 }}</div>
        <div class="stat-label">人口</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item" @click="goPage('/village-info')">
        <div class="stat-value">{{ villageInfo?.householdCount || 3072 }}</div>
        <div class="stat-label">户数</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item" @click="goPage('/village-info')">
        <div class="stat-value">{{ villageInfo?.area || 63 }}</div>
        <div class="stat-label">面积(km²)</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item" @click="goPage('/village-info')">
        <div class="stat-value">{{ villageInfo?.groupCount || 107 }}</div>
        <div class="stat-label">村民组</div>
      </div>
    </div>

    <!-- 快捷入口九宫格 -->
    <div class="quick-grid">
      <div class="quick-item" v-for="item in quickEntries" :key="item.name" @click="goPage(item.route)">
        <div class="quick-icon" :style="{ background: item.color }">
          <van-icon :name="item.icon" size="24" color="#fff" />
        </div>
        <div class="quick-name">{{ item.name }}</div>
      </div>
    </div>

    <!-- 下辖村落 — 横向滑动 -->
    <div class="section">
      <div class="section-header">
        <span class="section-title">下辖村落</span>
        <span class="section-count">共7个村</span>
      </div>
      <div class="village-scroll">
        <div
          v-for="village in villages"
          :key="village.id"
          class="village-chip"
          @click="goVillageDetail(village.id)"
        >
          <div class="village-emoji" :style="{ background: village.color }">{{ village.icon }}</div>
          <div class="village-name">{{ village.name }}</div>
          <div class="village-desc">{{ village.desc }}</div>
        </div>
      </div>
    </div>

    <!-- 负责人 — 紧凑列表 -->
    <div class="section">
      <div class="section-header">
        <span class="section-title">乡镇主要事务负责人</span>
      </div>
      <div class="leader-list">
        <div
          v-for="leader in leaders"
          :key="leader.id"
          class="leader-row"
          @click="goLeaderDetail(leader.id)"
        >
          <div class="leader-avatar" :style="{ background: leader.color }">
            {{ leader.name.charAt(0) }}
          </div>
          <div class="leader-info">
            <div class="leader-name">{{ leader.name }}</div>
            <div class="leader-desc">{{ leader.workCount }}项分管事务</div>
          </div>
          <van-icon name="arrow" size="14" color="#ccc" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showDialog } from 'vant'
import request from '../utils/request'

const router = useRouter()

const villageInfo = ref(null)

const villages = ref([
  { id: 1, name: '新店村', icon: '🏠', color: '#D22630', desc: '21组·1568人' },
  { id: 2, name: '罗卜田村', icon: '🥕', color: '#4CAF50', desc: '18组·1420人' },
  { id: 3, name: '兴无村', icon: '💰', color: '#2196F3', desc: '15组·1280人' },
  { id: 4, name: '马坡村', icon: '🐎', color: '#FFA500', desc: '16组·1356人' },
  { id: 5, name: '半冲村', icon: '⛰️', color: '#9C27B0', desc: '12组·1086人' },
  { id: 6, name: '冬瓜坡村', icon: '🥒', color: '#00BCD4', desc: '14组·1156人' },
  { id: 7, name: '枣子山村', icon: '🌰', color: '#8D6E63', desc: '11组·983人' }
])

const leaders = ref([
  { id: 1, name: '欧阳付群', color: '#D22630', workCount: 9 },
  { id: 2, name: '曹海洋', color: '#2196F3', workCount: 6 },
  { id: 3, name: '龙君屹', color: '#4CAF50', workCount: 12 },
  { id: 4, name: '张芷馨', color: '#FFA500', workCount: 12 },
  { id: 5, name: '于鼎馨', color: '#9C27B0', workCount: 1 },
  { id: 6, name: '杨承明', color: '#00BCD4', workCount: 5 },
  { id: 7, name: '杨成', color: '#E91E63', workCount: 14 }
])

const quickEntries = [
  { name: '村情概况', icon: 'friends-o', route: '/village-info', color: '#D22630' },
  { name: '三务公开', icon: 'eye-o', route: '/public-notices', color: '#4CAF50' },
  { name: '通知公告', icon: 'bell', route: '/notifications', color: '#FF9800' },
  { name: '诉求上报', icon: 'edit', route: '/demand', color: '#2196F3' },
  { name: '惠民补贴', icon: 'gift-o', route: '/subsidy', color: '#9C27B0' },
  { name: '政策查询', icon: 'search', route: '/policy', color: '#00BCD4' },
  { name: '党建', icon: 'flag-o', route: '/party-member', color: '#F44336' },
  { name: '更多', icon: 'apps-o', route: '/village-affairs', color: '#607D8B' }
]

const goPage = (path) => router.push(path)
const goVillageDetail = (id) => router.push(`/village-detail/${id}`)
const goLeaderDetail = (id) => router.push(`/leader-detail/${id}`)
const goSearch = () => router.push('/search-result')

const showEmergency = () => {
  showDialog({
    title: '紧急联系',
    message: '村委会：138-0013-8000\n警务室：110\n火警：119\n急救：120',
    confirmButtonText: '拨打村委会',
    confirmButtonColor: '#C8102E',
    cancelButtonText: '关闭'
  }).then(() => {
    window.location.href = 'tel:13800138000'
  }).catch(() => {})
}

const fetchHomeData = async () => {
  try {
    const res = await request.get('/public/home-data')
    villageInfo.value = res.villageInfo || res.data?.villageInfo
  } catch (error) {
    villageInfo.value = {
      population: 8849,
      area: 63,
      householdCount: 3072,
      groupCount: 107
    }
  }
}

onMounted(() => {
  fetchHomeData()
})
</script>

<style scoped>
.home-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #f5f5f5;
}

/* Hero 区域 */
.hero {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
}

.hero-bg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hero-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(139, 18, 24, 0.8) 0%, rgba(176, 26, 38, 0.4) 50%, transparent 100%);
}

.hero-content {
  position: absolute;
  left: 20px;
  bottom: 44px;
  z-index: 1;
}

.hero-title {
  font-size: 26px;
  font-weight: 800;
  color: #FFD700;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
  letter-spacing: 2px;
}

.hero-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
  letter-spacing: 4px;
  margin-top: 4px;
}

.search-bar {
  position: absolute;
  left: 16px;
  right: 16px;
  bottom: 8px;
  background: #fff;
  border-radius: 20px;
  height: 38px;
  display: flex;
  align-items: center;
  padding: 0 14px;
  gap: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.12);
  z-index: 2;
}

.search-placeholder {
  font-size: 13px;
  color: #999;
}

/* 紧急联系条 */
.emergency-bar {
  display: flex;
  align-items: center;
  gap: 6px;
  margin: 8px 12px;
  padding: 8px 14px;
  background: #FFF5F5;
  border-radius: 10px;
  border: 1px solid rgba(200, 16, 46, 0.1);
}

.emergency-icon {
  font-size: 16px;
}

.emergency-text {
  font-size: 13px;
  font-weight: 600;
  color: #C8102E;
  white-space: nowrap;
}

.emergency-desc {
  flex: 1;
  font-size: 11px;
  color: #999;
  text-align: right;
}

/* 基本村情 — 紧凑横条 */
.stats-bar {
  display: flex;
  align-items: center;
  background: #fff;
  margin: 0 12px 8px;
  border-radius: 12px;
  padding: 14px 0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  font-size: 18px;
  font-weight: 800;
  color: #333;
}

.stat-label {
  font-size: 11px;
  color: #999;
  margin-top: 2px;
}

.stat-divider {
  width: 1px;
  height: 24px;
  background: #f0f0f0;
}

/* 九宫格快捷入口 */
.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1px;
  background: #fff;
  margin: 0 12px 8px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
  padding: 12px 0;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  cursor: pointer;
}

.quick-item:active .quick-icon {
  transform: scale(0.9);
}

.quick-icon {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s;
}

.quick-name {
  font-size: 11px;
  color: #666;
}

/* 通用 section */
.section {
  background: #fff;
  margin: 0 12px 8px;
  border-radius: 12px;
  padding: 14px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.section-title {
  font-size: 15px;
  font-weight: 700;
  color: #333;
}

.section-count {
  font-size: 12px;
  color: #999;
}

/* 村落横向滑动 */
.village-scroll {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
  padding-bottom: 4px;
}

.village-scroll::-webkit-scrollbar {
  display: none;
}

.village-chip {
  flex-shrink: 0;
  width: 120px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 10px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.village-chip:active {
  transform: scale(0.96);
}

.village-emoji {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin: 0 auto 8px;
}

.village-name {
  font-size: 13px;
  font-weight: 600;
  color: #333;
}

.village-desc {
  font-size: 11px;
  color: #999;
  margin-top: 2px;
}

/* 负责人列表 — 紧凑行 */
.leader-list {
  display: flex;
  flex-direction: column;
}

.leader-row {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}

.leader-row:last-child {
  border-bottom: none;
}

.leader-row:active {
  background: #fafafa;
}

.leader-avatar {
  width: 38px;
  height: 38px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  font-weight: 700;
  color: #fff;
  margin-right: 12px;
  flex-shrink: 0;
}

.leader-info {
  flex: 1;
}

.leader-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.leader-desc {
  font-size: 11px;
  color: #999;
  margin-top: 1px;
}
</style>
