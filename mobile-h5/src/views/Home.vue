<template>
  <div class="home-page page-container">
    <div class="header-banner">
      <div class="banner-image">
        <img src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=long%20Chinese%20Dong%20ethnic%20wind%20rain%20bridge%20with%20multiple%20towers%20over%20green%20river%20blue%20sky%20red%20lanterns%20stone%20pillars%20perfect%20reflection%20panoramic%20wide%20angle&image_size=landscape_16_9" alt="罗卜田乡风景" />
        <div class="banner-overlay"></div>
        <div class="banner-title">罗卜田乡</div>
        <div class="banner-subtitle">共建美好家园</div>
      </div>
      <div class="banner-buttons">
        <div class="banner-btn primary ripple" @click="goDemand">
          <van-icon name="heart-o" size="28" />
          <span>我为群众办实事</span>
        </div>
        <div class="banner-btn gold ripple" @click="goOfficialWork">
          <van-icon name="user" size="28" />
          <span>诉求管理</span>
        </div>
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
          <van-icon name="building-o" size="20" color="#D22630" />
          <h3>组织架构</h3>
        </div>
      </div>
      <div class="org-tabs">
        <div 
          v-for="tab in orgTabs" 
          :key="tab.name" 
          class="org-tab"
          :class="{ active: activeOrg === tab.name }"
          @click="onOrgChange(tab.name)"
        >
          <van-icon :name="tab.icon" size="24" />
          <span class="tab-text">{{ tab.name }}</span>
        </div>
      </div>
      <div class="org-members-list" v-if="orgMembers.length">
        <div v-for="member in orgMembers" :key="member.id" class="member-item ripple" @click="showMemberDetail(member)">
          <div class="member-avatar">
            <span>{{ member.name.charAt(0) }}</span>
          </div>
          <div class="member-info">
            <div class="member-name">{{ member.name }}</div>
            <div class="member-position">{{ member.position }}</div>
          </div>
          <van-icon name="arrow-right" size="20" color="#ccc" />
        </div>
      </div>
      <div class="empty-org" v-else>
        <p>暂无人员信息</p>
      </div>
    </div>

    <div class="module-card animate-slide-up" style="animation-delay: 0.05s">
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

    <div class="module-card animate-slide-up" style="animation-delay: 0.1s">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="file-text" size="20" color="#D22630" />
          <h3>三务公开</h3>
        </div>
        <a href="/public-notices" class="more-link">全部 →</a>
      </div>
      <div class="grid-3">
        <div class="info-card red-gradient ripple" @click="goPage('/public-notices?type=党务公开')">
          <van-icon name="star-o" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">党务公开</div>
            <div class="card-label">党员名单、党日活动</div>
          </div>
        </div>
        <div class="info-card blue-gradient ripple" @click="goPage('/public-notices?type=村务公开')">
          <van-icon name="file-o" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">村务公开</div>
            <div class="card-label">土地流转、集体项目</div>
          </div>
        </div>
        <div class="info-card purple-gradient ripple" @click="goPage('/public-notices?type=财务公开')">
          <van-icon name="wallet" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">财务公开</div>
            <div class="card-label">集体资产{{ formatAssets }}元</div>
          </div>
        </div>
      </div>
    </div>

    <div class="module-card animate-slide-up" style="animation-delay: 0.15s">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="service" size="20" color="#D22630" />
          <h3>便民服务</h3>
        </div>
      </div>
      <div class="grid-3">
        <div class="info-card blue-gradient ripple" @click="goPage('/dispute')">
          <van-icon name="friends" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">矛盾纠纷</div>
            <div class="card-label">调处记录</div>
          </div>
        </div>
        <div class="info-card orange-gradient ripple" @click="goPage('/subsidy')">
          <van-icon name="gift" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">惠民补贴</div>
            <div class="card-label">补贴查询、申请</div>
          </div>
        </div>
        <div class="info-card red-gradient ripple" @click="goPage('/safety-hazard')">
          <van-icon name="warning-o" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">灾害防范</div>
            <div class="card-label">隐患排查、上报</div>
          </div>
        </div>
        <div class="info-card cyan-gradient ripple" @click="goPage('/notifications')">
          <van-icon name="bell" size="28" color="#fff" />
          <div class="card-content">
            <div class="card-value">通知公告</div>
            <div class="card-label">最新通知</div>
          </div>
        </div>
      </div>
    </div>

    <div class="module-card animate-slide-up" style="animation-delay: 0.2s">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="bell-o" size="20" color="#D22630" />
          <h3>最新通知</h3>
        </div>
        <a href="/notifications" class="more-link">全部 →</a>
      </div>
      <div class="notifications-list">
        <div 
          v-for="notice in notifications" 
          :key="notice.id" 
          class="notification-item ripple"
          @click="goPage(`/notification-detail/${notice.id}`)"
        >
          <div class="notification-tag" :class="notice.isTop === 1 ? 'top-tag' : 'type-tag'">
            {{ notice.isTop === 1 ? '置顶' : notice.type }}
          </div>
          <div class="notification-content">
            <div class="notification-title">{{ notice.title }}</div>
            <div class="notification-time">{{ formatTime(notice.createTime) }}</div>
          </div>
          <van-icon name="arrow-right" size="20" color="#ccc" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast, showDialog } from 'vant'
import request from '../utils/request'

const router = useRouter()
const route = useRoute()

const searchKeyword = ref('')
const villageInfo = ref(null)
const orgMembers = ref([])
const activeOrg = ref('支委')
const notifications = ref([])

const orgTabs = [
  { name: '支委', icon: 'building' },
  { name: '村委', icon: 'home' },
  { name: '监委', icon: 'eye-o' }
]

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

const goDemand = () => {
  router.push('/demand')
}

const goOfficialWork = () => {
  router.push('/demand/my')
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

const onOrgChange = (name) => {
  activeOrg.value = name
  fetchOrgMembers(name)
}

const fetchOrgMembers = async (orgType) => {
  try {
    const res = await request.get('/public/org-members', { params: { orgType } })
    orgMembers.value = res.data || []
  } catch (error) {
    console.error('获取组织成员失败', error)
    orgMembers.value = []
  }
}

const showMemberDetail = (member) => {
  showDialog({
    title: member.name,
    message: `${member.position}\n分工：${member.division}\n电话：${member.phone}`,
    confirmButtonText: '拨打电话',
    confirmButtonColor: '#D22630',
    cancelButtonText: '关闭'
  }).then(() => {
    window.location.href = `tel:${member.phone}`
  }).catch(() => {})
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

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

const fetchHomeData = async () => {
  try {
    const res = await request.get('/public/home-data')
    villageInfo.value = res.villageInfo || res.data?.villageInfo
    notifications.value = res.notifications || res.data?.notifications || []
    await fetchOrgMembers(activeOrg.value)
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
    notifications.value = [
      { id: 1, title: '交通安全整治行动通知', type: '安全通知', isTop: 1, createTime: '2026-07-18' },
      { id: 2, title: '柑橘品改推进工作公告', type: '产业通知', isTop: 0, createTime: '2026-07-17' },
      { id: 3, title: '防汛备汛工作部署', type: '政务通知', isTop: 0, createTime: '2026-07-16' }
    ]
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
  height: 180px;
}

.banner-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 80px;
  background: linear-gradient(to top, rgba(210, 38, 48, 0.9), transparent);
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

.banner-buttons {
  display: flex;
  gap: 8px;
  padding: 12px;
}

.banner-btn {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 8px;
  border-radius: 12px;
  color: #fff;
  font-size: 13px;
  font-weight: bold;
  transition: transform 0.2s;
}

.banner-btn:active {
  transform: scale(0.95);
}

.banner-btn.primary {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  box-shadow: 0 4px 12px rgba(210, 38, 48, 0.3);
}

.banner-btn.gold {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
  box-shadow: 0 4px 12px rgba(255, 215, 0, 0.3);
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

.org-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.org-tab {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 8px;
  border-radius: 12px;
  background: #f5f5f5;
  transition: all 0.3s;
}

.org-tab.active {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

.org-tab.active .tab-icon,
.org-tab.active .tab-text {
  color: #FFD700;
}

.tab-text {
  font-size: 12px;
  color: #666;
  font-weight: bold;
}

.org-members-list {
  padding: 0;
}

.member-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.member-item:last-child {
  border-bottom: none;
}

.member-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  color: #FFD700;
  margin-right: 12px;
}

.member-info {
  flex: 1;
}

.member-name {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 2px;
}

.member-position {
  font-size: 12px;
  color: #999;
}

.empty-org {
  padding: 24px;
  text-align: center;
  color: #969799;
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
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

.info-card.green-gradient {
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
}

.info-card.blue-gradient {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
}

.info-card.orange-gradient {
  background: linear-gradient(135deg, #FFA500 0%, #FF8C00 100%);
}

.info-card.purple-gradient {
  background: linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%);
}

.info-card.cyan-gradient {
  background: linear-gradient(135deg, #00BCD4 0%, #0097A7 100%);
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

.notifications-list {
  padding: 0;
}

.notification-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-tag {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: bold;
  margin-right: 12px;
}

.notification-tag.top-tag {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  color: #FFD700;
}

.notification-tag.type-tag {
  background: #f0f0f0;
  color: #666;
}

.notification-content {
  flex: 1;
}

.notification-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.notification-time {
  font-size: 11px;
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
</style>