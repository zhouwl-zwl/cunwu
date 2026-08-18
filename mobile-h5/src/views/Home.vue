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
          <div class="emergency-title">紧急联系</div>
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
        <div class="thought-stars">
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
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.search-section {
  padding: 0 12px 12px;
}

.search-section :deep(.van-search) {
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.08);
}

.emergency-section {
  padding: 0 12px 12px;
}

.emergency-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, rgba(210, 38, 48, 0.08) 0%, rgba(210, 38, 48, 0.04) 100%);
  border-radius: 14px;
  padding: 14px 16px;
  border: 1px solid rgba(210, 38, 48, 0.15);
}

.emergency-icon {
  font-size: 32px;
  margin-right: 12px;
}

.emergency-content {
  flex: 1;
}

.emergency-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 4px;
}

.emergency-desc {
  font-size: 12px;
  color: #666;
}

.header-banner {
  background: #fff;
  padding: 0;
  margin-bottom: 12px;
  overflow: hidden;
}

.banner-image {
  position: relative;
  width: 100%;
  height: 260px;
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
  height: 100px;
  background: linear-gradient(to top, rgba(210, 38, 48, 0.95), rgba(210, 38, 48, 0.3), transparent);
}

.banner-title {
  position: absolute;
  bottom: 20px;
  left: 16px;
  font-size: 28px;
  font-weight: bold;
  color: #FFD700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.banner-subtitle {
  position: absolute;
  bottom: 8px;
  left: 16px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.9);
}

.module-card {
  background: #fff;
  margin: 0 12px 12px;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.module-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.module-header h3 {
  font-size: 17px;
  font-weight: bold;
  color: #333;
}

.more-link {
  font-size: 13px;
  color: #999;
}

.grid-3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.info-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 8px;
  border-radius: 14px;
  color: #fff;
  text-align: center;
  transition: transform 0.2s, box-shadow 0.2s;
  position: relative;
  overflow: hidden;
}

.info-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255,255,255,0.1) 0%, transparent 50%);
  pointer-events: none;
}

.info-card:active {
  transform: scale(0.95);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
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
  margin-top: 8px;
}

.card-value {
  font-size: 15px;
  font-weight: bold;
  margin-bottom: 4px;
}

.card-label {
  font-size: 10px;
  opacity: 0.85;
}

.village-grid {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.village-card {
  display: flex;
  align-items: center;
  padding: 16px;
  background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 100%);
  border-radius: 14px;
  border: 1px solid #eee;
  transition: all 0.2s;
  position: relative;
  overflow: hidden;
}

.village-card::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 60px;
  height: 60px;
  background: radial-gradient(circle, rgba(210, 38, 48, 0.05) 0%, transparent 70%);
  border-radius: 50%;
  transform: translate(30%, -30%);
}

.village-card:active {
  background: #f0f0f0;
  transform: scale(0.98);
}

.village-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 14px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  flex-shrink: 0;
}

.village-emoji {
  font-size: 28px;
  line-height: 1;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.2));
}

.village-info {
  flex: 1;
}

.village-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.village-desc {
  font-size: 12px;
  color: #999;
}

.ripple {
  position: relative;
  overflow: hidden;
  
  &::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    background: rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    transform: translate(-50%, -50%);
    transition: width 0.4s, height 0.4s;
  }
  
  &:active::after {
    width: 200px;
    height: 200px;
  }
}

.animate-slide-up {
  animation: slideUp 0.4s ease-out;
  opacity: 0;
  animation-fill-mode: forwards;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.thought-module {
  background: linear-gradient(135deg, #FFFAFA 0%, #FFF8F0 100%);
  border: 1px solid rgba(210, 38, 48, 0.15);
  position: relative;
  overflow: hidden;
}

.thought-module::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 120px;
  height: 120px;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.08) 0%, transparent 70%);
  border-radius: 50%;
  transform: translate(40%, -40%);
  pointer-events: none;
}

.thought-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.thought-header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.thought-emblem {
  font-size: 28px;
  color: #D22630;
  text-shadow: 0 2px 6px rgba(210, 38, 48, 0.3);
  animation: emblemPulse 2.5s ease-in-out infinite alternate;
}

@keyframes emblemPulse {
  from { text-shadow: 0 2px 6px rgba(210, 38, 48, 0.3); }
  to { text-shadow: 0 2px 12px rgba(210, 38, 48, 0.6), 0 0 20px rgba(255, 215, 0, 0.4); }
}

.thought-title {
  font-size: 17px;
  font-weight: bold;
  color: #5D0000;
  letter-spacing: 1px;
  margin: 0;
}

.thought-subtitle {
  font-size: 10px;
  color: #B01A26;
  opacity: 0.7;
  margin-top: 2px;
  letter-spacing: 0.5px;
}

.thought-featured {
  background: linear-gradient(135deg, #D22630 0%, #8B0000 100%);
  border-radius: 14px;
  padding: 16px;
  color: #fff;
  margin-bottom: 12px;
  position: relative;
  overflow: hidden;
  transition: transform 0.2s;
}

.thought-featured:active {
  transform: scale(0.98);
}

.thought-featured::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.2) 0%, transparent 70%);
  border-radius: 50%;
  transform: translate(30%, -30%);
}

.thought-featured::after {
  content: '★ ★ ★ ★ ★';
  position: absolute;
  bottom: 8px;
  right: 12px;
  font-size: 8px;
  color: rgba(255, 215, 0, 0.4);
  letter-spacing: 2px;
}

.featured-tag {
  display: inline-block;
  background: rgba(255, 215, 0, 0.25);
  color: #FFD700;
  font-size: 10px;
  padding: 3px 10px;
  border-radius: 10px;
  border: 1px solid rgba(255, 215, 0, 0.4);
  margin-bottom: 10px;
  letter-spacing: 1px;
}

.featured-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #fff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.featured-quote {
  font-size: 12px;
  color: rgba(255, 215, 0, 0.95);
  line-height: 1.6;
  font-style: italic;
  margin-bottom: 12px;
  position: relative;
  z-index: 1;
}

.thought-stars {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  position: relative;
  z-index: 1;
}

.thought-stars span {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.9);
  background: rgba(255, 255, 255, 0.15);
  padding: 3px 8px;
  border-radius: 8px;
}

.thought-mini-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}

.mini-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 6px;
  border-radius: 12px;
  color: #fff;
  text-align: center;
  transition: transform 0.2s;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.15);
  position: relative;
  overflow: hidden;
}

.mini-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255,255,255,0.15) 0%, transparent 50%);
}

.mini-card:active {
  transform: scale(0.95);
}

.mini-icon {
  font-size: 22px;
  margin-bottom: 4px;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.2));
}

.mini-name {
  font-size: 11px;
  font-weight: 500;
  z-index: 1;
}
</style>
