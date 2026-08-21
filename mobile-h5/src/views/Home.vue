<template>
  <div class="home-page page-container">
    <div class="header-banner">
      <div class="banner-image">
        <img src="/banner.jpg" alt="罗卜田乡风景" loading="eager" />
        <div class="banner-overlay"></div>
        <div class="banner-title">罗卜田乡</div>
        <div class="banner-subtitle">共建美好家园</div>
      </div>
    </div>

    <div class="search-section">
      <van-search 
        v-model="searchKeyword" 
        placeholder="搜索政策、服务、通知..." 
        @search="onSearch"
        @click="goSearch"
        show-action
      >
        <template #action>
          <van-button text type="primary" @click="goSearch">搜索</van-button>
        </template>
      </van-search>
    </div>

    <div class="emergency-section">
      <div class="emergency-card ripple" @click="showEmergency">
        <div class="emergency-icon">🔔</div>
        <div class="emergency-content">
          <div class="emergency-title">紧急联系 <span class="emergency-badge">24h</span></div>
          <div class="emergency-desc">村委会 138-0013-8000 | 警务室 110</div>
        </div>
        <van-icon name="arrow-right" size="20" color="#D22630" />
      </div>
    </div>

    <div class="module-card animate-slide-up">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="bar-chart-o" size="20" color="#D22630" />
          <h3>基本村情</h3>
        </div>
        <a href="/village-info" class="more-link">全部 →</a>
      </div>
      <div class="grid-3">
        <div class="info-card red-gradient ripple" @click="goPage('/village-info')">
          <van-icon name="friends-o" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ villageInfo?.population || 0 }}人</div>
            <div class="card-label">人口</div>
          </div>
        </div>
        <div class="info-card green-gradient ripple" @click="goPage('/village-info')">
          <van-icon name="map" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ villageInfo?.area || 0 }}km²</div>
            <div class="card-label">面积</div>
          </div>
        </div>
        <div class="info-card blue-gradient ripple" @click="goPage('/village-info')">
          <van-icon name="home-o" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ villageInfo?.householdCount || 0 }}户</div>
            <div class="card-label">户数</div>
          </div>
        </div>
        <div class="info-card purple-gradient ripple" @click="goPage('/village-info')">
          <van-icon name="team" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ villageInfo?.groupCount || '' }}</div>
            <div class="card-label">村民组</div>
          </div>
        </div>
        <div class="info-card orange-gradient ripple" @click="goPage('/village-info')">
          <van-icon name="location-o" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ villageInfo?.location || '' }}</div>
            <div class="card-label">位置</div>
          </div>
        </div>
        <div class="info-card cyan-gradient ripple" @click="goPage('/village-info')">
          <van-icon name="book-o" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">更多</div>
            <div class="card-label">村历史文化</div>
          </div>
        </div>
      </div>
    </div>

    <div class="module-card animate-slide-up" style="animation-delay: 0.05s">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="home" size="20" color="#D22630" />
          <h3>下辖村落</h3>
        </div>
      </div>
      <div class="village-grid">
        <div 
          v-for="village in villages" 
          :key="village.id" 
          class="village-card ripple"
          @click="goVillageDetail(village.id)"
        >
          <div class="village-icon" :style="{ background: village.color }">
            <span class="village-emoji">{{ village.icon }}</span>
          </div>
          <div class="village-info">
            <div class="village-name">{{ village.name }}</div>
            <div class="village-desc">{{ village.desc }}</div>
          </div>
          <van-icon name="arrow-right" size="20" color="#ccc" />
        </div>
      </div>
    </div>

    <div class="module-card animate-slide-up" style="animation-delay: 0.1s">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="contacts" size="20" color="#FF5722" />
          <h3>乡镇主要事务负责人</h3>
        </div>
      </div>
      <div class="village-grid">
        <div 
          v-for="leader in leaders" 
          :key="leader.id" 
          class="village-card ripple"
          @click="goLeaderDetail(leader.id)"
        >
          <div class="village-icon" :style="{ background: leader.color }">
            <van-icon :name="leader.icon" size="32" color="#fff" />
          </div>
          <div class="village-info">
            <div class="village-name">{{ leader.name }}</div>
            <div class="village-desc">{{ leader.workCount }}项分管事务</div>
          </div>
          <van-icon name="arrow-right" size="20" color="#ccc" />
        </div>
      </div>
    </div>

    <div class="module-card thought-module animate-slide-up" style="animation-delay: 0.15s">
      <div class="thought-header">
        <div class="thought-header-left">
          <div class="thought-emblem">☭</div>
          <div>
            <h3 class="thought-title">学习最新思想</h3>
            <div class="thought-subtitle">学思想 · 强党性 · 重实践 · 建新功</div>
          </div>
        </div>
        <a href="/thought-study" class="more-link" @click.prevent="goThoughtStudy">进入 →</a>
      </div>
      <div class="thought-featured" @click="goThoughtStudy">
        <div class="featured-tag">学习专题</div>
        <div class="featured-title">习近平新时代中国特色社会主义思想</div>
        <div class="featured-quote">"人民对美好生活的向往，就是我们的奋斗目标。"</div>
        <div class="thought-stats">
          <span>📖 6大专题</span>
          <span>✨ 30+金句</span>
          <span>🚩 核心要点</span>
        </div>
      </div>
      <div class="thought-mini-grid">
        <div
          v-for="(t, i) in thoughtTopics"
          :key="i"
          class="mini-card"
          :style="{ background: t.bg }"
          @click="goThoughtStudy"
        >
          <div class="mini-icon">{{ t.icon }}</div>
          <div class="mini-name">{{ t.name }}</div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showDialog } from 'vant'
import request from '../utils/request'

const router = useRouter()
const route = useRoute()

const searchKeyword = ref('')
const villageInfo = ref(null)

const villages = ref([
  { id: 1, name: '新店村', icon: '🏠', color: 'linear-gradient(135deg, #D22630 0%, #B01A26 100%)', desc: '21个村民组，1568人' },
  { id: 2, name: '罗卜田村', icon: '🥕', color: 'linear-gradient(135deg, #4CAF50 0%, #388E3C 100%)', desc: '18个村民组，1420人' },
  { id: 3, name: '兴无村', icon: '💰', color: 'linear-gradient(135deg, #2196F3 0%, #1976D2 100%)', desc: '15个村民组，1280人' },
  { id: 4, name: '马坡村', icon: '🐎', color: 'linear-gradient(135deg, #FFA500 0%, #FF8C00 100%)', desc: '16个村民组，1356人' },
  { id: 5, name: '半冲村', icon: '⛰️', color: 'linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%)', desc: '12个村民组，1086人' },
  { id: 6, name: '冬瓜坡村', icon: '🥒', color: 'linear-gradient(135deg, #00BCD4 0%, #0097A7 100%)', desc: '14个村民组，1156人' },
  { id: 7, name: '枣子山村', icon: '🌰', color: 'linear-gradient(135deg, #8D6E63 0%, #5D4037 100%)', desc: '11个村民组，983人' }
])

const leaders = ref([
  { id: 1, name: '欧阳付群', icon: 'user-o', color: 'linear-gradient(135deg, #D22630 0%, #B01A26 100%)', workCount: 9 },
  { id: 2, name: '曹海洋', icon: 'user-o', color: 'linear-gradient(135deg, #4CAF50 0%, #388E3C 100%)', workCount: 6 },
  { id: 3, name: '龙君屹', icon: 'user-o', color: 'linear-gradient(135deg, #2196F3 0%, #1976D2 100%)', workCount: 12 },
  { id: 4, name: '张芷馨', icon: 'user-o', color: 'linear-gradient(135deg, #FFA500 0%, #FF8C00 100%)', workCount: 12 },
  { id: 5, name: '于鼎馨', icon: 'user-o', color: 'linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%)', workCount: 1 },
  { id: 6, name: '杨承明', icon: 'user-o', color: 'linear-gradient(135deg, #00BCD4 0%, #0097A7 100%)', workCount: 5 },
  { id: 7, name: '杨成', icon: 'user-o', color: 'linear-gradient(135deg, #E91E63 0%, #C2185B 100%)', workCount: 14 }
])

const thoughtTopics = ref([
  { icon: '🚩', name: '二十大精神', bg: 'linear-gradient(135deg, #C62828 0%, #7F0000 100%)' },
  { icon: '🌾', name: '乡村振兴', bg: 'linear-gradient(135deg, #E65100 0%, #BF360C 100%)' },
  { icon: '🌟', name: '中国式现代化', bg: 'linear-gradient(135deg, #B71C1C 0%, #880E4F 100%)' },
  { icon: '🤝', name: '共同富裕', bg: 'linear-gradient(135deg, #D84315 0%, #BF360C 100%)' },
  { icon: '💡', name: '新发展理念', bg: 'linear-gradient(135deg, #C62828 0%, #4A148C 100%)' },
  { icon: '📖', name: '新思想', bg: 'linear-gradient(135deg, #D22630 0%, #8B0000 100%)' }
])

const goThoughtStudy = () => {
  router.push('/thought-study')
}

const formatAssets = computed(() => {
  const total = villageInfo.value?.totalAssets || 0
  if (total >= 10000) {
    return (total / 10000).toFixed(0)
  }
  return total
})

const goPage = (path) => {
  router.push(path)
}

const goVillageDetail = (id) => {
  router.push(`/village-detail/${id}`)
}

const goLeaderDetail = (id) => {
  router.push(`/leader-detail/${id}`)
}

const goSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push(`/search-result?keyword=${encodeURIComponent(searchKeyword.value)}`)
  } else {
    router.push('/search-result')
  }
}

const onSearch = (value) => {
  if (value.trim()) {
    router.push(`/search-result?keyword=${encodeURIComponent(value)}`)
  }
}

const showEmergency = () => {
  showDialog({
    title: '紧急联系方式',
    message: `村委会：138-0013-8000\n警务室：110\n火警：119\n急救：120\n\n如需拨打电话，请点击下方按钮。`,
    confirmButtonText: '拨打村委会',
    confirmButtonColor: '#D22630',
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
    console.error('获取首页数据失败', error)
    villageInfo.value = {
      population: 8849,
      area: 63,
      householdCount: 3072,
      groupCount: 107,
      location: '县境东南部，距县城22公里',
      totalAssets: 2115800
    }
  }
}

onMounted(() => {
  fetchHomeData()
})
</script>

<style scoped>
.home-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 70px);
}

/* Banner 区域 - 更有层次 */
.header-banner {
  background: #fff;
  padding: 0;
  margin-bottom: var(--spacing-md);
  overflow: hidden;
  position: relative;
}

.banner-image {
  position: relative;
  width: 100%;
  height: 280px;
}

.banner-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 140px;
  background: linear-gradient(to top, 
    rgba(139, 18, 24, 0.95) 0%, 
    rgba(176, 26, 38, 0.6) 40%, 
    rgba(210, 38, 48, 0.2) 70%, 
    transparent 100%);
}

.banner-overlay::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, 
    rgba(255, 215, 0, 0.05) 0%, 
    transparent 50%);
}

.banner-title {
  position: absolute;
  bottom: 36px;
  left: 20px;
  font-size: 32px;
  font-weight: var(--font-weight-bold);
  color: var(--gold-color);
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.4), 0 0 20px rgba(255, 215, 0, 0.2);
  letter-spacing: 2px;
  font-family: var(--font-family-serif);
}

.banner-subtitle {
  position: absolute;
  bottom: 16px;
  left: 20px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.92);
  letter-spacing: 4px;
  font-weight: var(--font-weight-medium);
}

.banner-decoration {
  position: absolute;
  top: 20px;
  right: 20px;
  display: flex;
  gap: 6px;
  
  .deco-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: var(--gold-color);
    opacity: 0.6;
    
    &:nth-child(2) { opacity: 0.8; width: 8px; height: 8px; }
    &:nth-child(3) { opacity: 0.5; }
  }
}

/* 搜索区域 - 更现代 */
.search-section {
  padding: 0 var(--spacing-md) var(--spacing-md);
  margin-top: -20px;
  position: relative;
  z-index: 10;
}

.search-section :deep(.van-search) {
  background: transparent;
}

.search-section :deep(.van-search__content) {
  background: #fff;
  border-radius: 24px;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-color);
  height: 46px;
}

/* 紧急联系 - 更醒目 */
.emergency-section {
  padding: 0 var(--spacing-md) var(--spacing-md);
}

.emergency-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, 
    rgba(239, 68, 68, 0.08) 0%, 
    rgba(239, 68, 68, 0.04) 100%);
  border-radius: var(--radius-xl);
  padding: var(--spacing-md) var(--spacing-lg);
  border: 1px solid rgba(239, 68, 68, 0.15);
  position: relative;
  overflow: hidden;
  transition: all var(--transition-fast);
  
  &:active {
    transform: scale(0.99);
    background: linear-gradient(135deg, 
      rgba(239, 68, 68, 0.12) 0%, 
      rgba(239, 68, 68, 0.06) 100%);
  }
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 80px;
    height: 80px;
    background: radial-gradient(circle, rgba(239, 68, 68, 0.08) 0%, transparent 70%);
    border-radius: 50%;
    transform: translate(30%, -30%);
  }
}

.emergency-icon {
  font-size: 36px;
  margin-right: var(--spacing-md);
  position: relative;
  animation: bellShake 2.5s ease-in-out infinite;
}

@keyframes bellShake {
  0%, 90%, 100% { transform: rotate(0); }
  92% { transform: rotate(-10deg); }
  94% { transform: rotate(10deg); }
  96% { transform: rotate(-8deg); }
  98% { transform: rotate(8deg); }
}

.emergency-content {
  flex: 1;
}

.emergency-title {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--danger-color);
  margin-bottom: var(--spacing-1);
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  
  .emergency-badge {
    font-size: 10px;
    background: var(--danger-color);
    color: #fff;
    padding: 1px 6px;
    border-radius: 4px;
    font-weight: var(--font-weight-medium);
  }
}

.emergency-desc {
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
  line-height: 1.4;
}

/* 模块卡片 - 统一升级 */
.module-card {
  background: var(--card-background);
  margin: 0 var(--spacing-md) var(--spacing-md);
  border-radius: var(--radius-xl);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-color);
  position: relative;
  overflow: hidden;
  transition: all var(--transition-normal);
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba(255,255,255,0.6), transparent);
    opacity: 0.4;
  }
}

.module-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.module-header h3 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  letter-spacing: 0.3px;
}

.more-link {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  display: flex;
  align-items: center;
  gap: 2px;
  transition: color var(--transition-fast);
  
  &:active {
    color: var(--primary-color);
  }
}

/* 基本村情宫格 - 更精致 */
.grid-3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-sm);
}

.info-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-md) var(--spacing-2);
  border-radius: var(--radius-lg);
  color: #fff;
  text-align: center;
  transition: all var(--transition-fast);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 50%;
    background: linear-gradient(to bottom, rgba(255,255,255,0.15), transparent);
    pointer-events: none;
  }
  
  &::after {
    content: '';
    position: absolute;
    top: -10px;
    right: -10px;
    width: 40px;
    height: 40px;
    background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, transparent 70%);
    border-radius: 50%;
  }
  
  &:active {
    transform: scale(0.96);
    box-shadow: var(--shadow-md);
  }
}

.info-card.red-gradient {
  background: var(--gradient-primary);
}

.info-card.green-gradient {
  background: var(--gradient-success);
}

.info-card.blue-gradient {
  background: var(--gradient-info);
}

.info-card.orange-gradient {
  background: var(--gradient-warning);
}

.info-card.purple-gradient {
  background: var(--gradient-purple);
}

.info-card.cyan-gradient {
  background: var(--gradient-cyan);
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: var(--spacing-sm);
  position: relative;
  z-index: 1;
}

.card-value {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  margin-bottom: var(--spacing-1);
  font-family: var(--font-family-number);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.15);
}

.card-label {
  font-size: 11px;
  opacity: 0.9;
  font-weight: var(--font-weight-medium);
}

/* 村/负责人列表 - 更现代 */
.village-grid {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.village-card {
  display: flex;
  align-items: center;
  padding: var(--spacing-md);
  background: var(--gray-50);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-light);
  transition: all var(--transition-fast);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 80px;
    height: 80px;
    background: radial-gradient(circle, rgba(210, 38, 48, 0.04) 0%, transparent 70%);
    border-radius: 50%;
    transform: translate(30%, -30%);
    transition: all var(--transition-fast);
  }
  
  &:active {
    background: var(--primary-soft);
    transform: scale(0.99);
    border-color: var(--border-color);
    
    &::before {
      background: radial-gradient(circle, rgba(210, 38, 48, 0.08) 0%, transparent 70%);
    }
  }
}

.village-icon {
  width: 52px;
  height: 52px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: var(--spacing-md);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.12);
  flex-shrink: 0;
  position: relative;
  overflow: hidden;
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 50%;
    background: linear-gradient(to bottom, rgba(255,255,255,0.2), transparent);
    border-radius: var(--radius-lg) var(--radius-lg) 0 0;
  }
}

.village-emoji {
  font-size: 26px;
  line-height: 1;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.15));
  position: relative;
  z-index: 1;
}

.village-info {
  flex: 1;
  min-width: 0;
}

.village-name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-1);
}

.village-desc {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
}

/* 学习思想模块 - 更庄重精致 */
.thought-module {
  background: linear-gradient(135deg, #FFFAFA 0%, #FFF8F0 100%);
  border: 1px solid rgba(210, 38, 48, 0.12);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 160px;
    height: 160px;
    background: radial-gradient(circle, rgba(255, 215, 0, 0.06) 0%, transparent 70%);
    border-radius: 50%;
    transform: translate(40%, -40%);
    pointer-events: none;
  }
  
  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 120px;
    height: 120px;
    background: radial-gradient(circle, rgba(210, 38, 48, 0.04) 0%, transparent 70%);
    border-radius: 50%;
    transform: translate(-40%, 40%);
    pointer-events: none;
  }
}

.thought-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
  position: relative;
  z-index: 1;
}

.thought-header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.thought-emblem {
  font-size: 32px;
  color: var(--primary-color);
  text-shadow: 0 2px 8px rgba(210, 38, 48, 0.25);
  animation: emblemPulse 3s ease-in-out infinite alternate;
}

@keyframes emblemPulse {
  from { 
    text-shadow: 0 2px 6px rgba(210, 38, 48, 0.3); 
    transform: scale(1);
  }
  to { 
    text-shadow: 0 2px 12px rgba(210, 38, 48, 0.5), 0 0 20px rgba(255, 215, 0, 0.3);
    transform: scale(1.05);
  }
}

.thought-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: #5D0000;
  letter-spacing: 1px;
  margin: 0;
  font-family: var(--font-family-serif);
}

.thought-subtitle {
  font-size: 11px;
  color: var(--primary-600);
  opacity: 0.75;
  margin-top: 2px;
  letter-spacing: 1px;
  font-weight: var(--font-weight-medium);
}

.thought-featured {
  background: var(--gradient-primary);
  border-radius: var(--radius-lg);
  padding: var(--spacing-lg);
  color: #fff;
  margin-bottom: var(--spacing-md);
  position: relative;
  overflow: hidden;
  transition: all var(--transition-fast);
  
  &:active {
    transform: scale(0.99);
  }
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 120px;
    height: 120px;
    background: radial-gradient(circle, rgba(255, 215, 0, 0.18) 0%, transparent 70%);
    border-radius: 50%;
    transform: translate(30%, -30%);
  }
  
  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, transparent, rgba(255, 215, 0, 0.3), transparent);
  }
}

.featured-tag {
  display: inline-block;
  background: rgba(255, 215, 0, 0.2);
  color: var(--gold-color);
  font-size: 11px;
  padding: 4px 12px;
  border-radius: 12px;
  border: 1px solid rgba(255, 215, 0, 0.35);
  margin-bottom: var(--spacing-sm);
  letter-spacing: 1px;
  font-weight: var(--font-weight-medium);
  position: relative;
  z-index: 1;
}

.featured-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  margin-bottom: var(--spacing-sm);
  color: #fff;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 1;
  letter-spacing: 0.5px;
}

.featured-quote {
  font-size: var(--font-size-sm);
  color: rgba(255, 215, 0, 0.95);
  line-height: 1.7;
  font-style: italic;
  margin-bottom: var(--spacing-md);
  position: relative;
  z-index: 1;
  padding-left: var(--spacing-sm);
  border-left: 2px solid rgba(255, 215, 0, 0.4);
}

.thought-stats {
  display: flex;
  gap: var(--spacing-md);
  flex-wrap: wrap;
  position: relative;
  z-index: 1;
}

.thought-stats span {
  font-size: var(--font-size-xs);
  color: rgba(255, 255, 255, 0.92);
  background: rgba(255, 255, 255, 0.12);
  padding: 4px 10px;
  border-radius: 6px;
  backdrop-filter: blur(4px);
  font-weight: var(--font-weight-medium);
}

/* 学习专题小卡片 */
.thought-mini-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-sm);
  position: relative;
  z-index: 1;
}

.mini-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-md) var(--spacing-2);
  border-radius: var(--radius-lg);
  color: #fff;
  text-align: center;
  transition: all var(--transition-fast);
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.12);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 50%;
    background: linear-gradient(to bottom, rgba(255,255,255,0.18), transparent);
  }
  
  &:active {
    transform: scale(0.95);
    box-shadow: 0 4px 12px rgba(210, 38, 48, 0.2);
  }
}

.mini-icon {
  font-size: 24px;
  margin-bottom: var(--spacing-2);
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.15));
  position: relative;
  z-index: 1;
}

.mini-name {
  font-size: 11px;
  font-weight: var(--font-weight-medium);
  z-index: 1;
  position: relative;
}

/* 入场动画 */
.animate-slide-up {
  animation: slideUp 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  opacity: 0;
  animation-fill-mode: forwards;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(24px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 移除重复的 ripple，使用全局样式 */
</style>
