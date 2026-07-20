<template>
  <div class="layout-container">
    <main class="main-content">
      <router-view />
    </main>
    
    <button class="menu-fab" @click="showMobileMenu = !showMobileMenu" v-if="showBottomNav">
      <svg viewBox="0 0 24 24">
        <path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"/>
      </svg>
    </button>
    
    <BottomNav v-if="showBottomNav" />
    
    <div v-if="showMobileMenu" class="mobile-menu-overlay" @click="showMobileMenu = false">
      <div class="mobile-menu-content" @click.stop>
        <div class="mobile-menu-header">
          <div class="mobile-brand">
            <div class="mobile-star"></div>
            <h2>罗卜田乡智慧村务</h2>
          </div>
          <button class="menu-close" @click="showMobileMenu = false">
            <svg viewBox="0 0 24 24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        <div class="mobile-menu-nav">
          <div 
            v-for="item in menuItems" 
            :key="item.path" 
            class="mobile-menu-item"
            :class="{ active: isActive(item.path) }"
            @click="navigate(item.path)"
          >
            <van-icon :name="item.icon" size="20" />
            <span>{{ item.text }}</span>
          </div>
          <div class="mobile-menu-divider"></div>
          <div 
            v-for="item in subMenuItems" 
            :key="item.path" 
            class="mobile-menu-item"
            :class="{ active: isActive(item.path) }"
            @click="navigate(item.path)"
          >
            <van-icon :name="item.icon" size="20" />
            <span>{{ item.text }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BottomNav from './BottomNav.vue'

const router = useRouter()
const route = useRoute()
const showMobileMenu = ref(false)
const showBottomNav = ref(true)

const menuItems = [
  { path: '/', icon: 'home', text: '首页' },
  { path: '/notifications', icon: 'bell', text: '通知公告' },
  { path: '/convenience', icon: 'service', text: '便民服务' },
  { path: '/mine', icon: 'user', text: '个人中心' }
]

const subMenuItems = [
  { path: '/dispute', icon: 'balance-o', text: '矛盾调处' },
  { path: '/subsidy', icon: 'gift', text: '惠民补贴' },
  { path: '/village-info', icon: 'map', text: '村情概况' },
  { path: '/org-members', icon: 'team', text: '组织架构' },
  { path: '/data-screen', icon: 'bar-chart', text: '数据大屏' }
]

const isActive = (path) => {
  if (path === '/') {
    return route.path === '/'
  }
  return route.path.startsWith(path)
}

const navigate = (path) => {
  showMobileMenu.value = false
  router.push(path)
}

watch(() => route.path, (newPath) => {
  showMobileMenu.value = false
  const isLoginPage = ['/login', '/register'].includes(newPath)
  showBottomNav.value = !isLoginPage && !route.meta.hideBottomNav
})
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background: #FDF5F5;
}

.main-content {
  padding-bottom: 60px;
}

.menu-fab {
  position: fixed;
  right: 16px;
  bottom: 72px;
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 16px rgba(200, 16, 46, 0.4);
  z-index: 100;
  cursor: pointer;
  transition: all 0.25s ease;
}

.menu-fab:hover {
  transform: scale(1.05);
}

.menu-fab:active {
  transform: scale(0.95);
}

.menu-fab svg {
  width: 24px;
  height: 24px;
  fill: none;
  stroke: #FFD700;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.mobile-menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 300;
  animation: fadeIn 0.2s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.mobile-menu-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  max-height: 85vh;
  background: #C8102E;
  border-radius: 20px 20px 0 0;
  overflow: hidden;
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from { 
    opacity: 0; 
    transform: translateY(100%); 
  }
  to { 
    opacity: 1; 
    transform: translateY(0); 
  }
}

.mobile-menu-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid rgba(255, 215, 0, 0.1);
}

.mobile-brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.mobile-star {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #FFD700 0%, #FFA500 50%, #E5C100 100%);
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
}

.mobile-brand h2 {
  font-size: 18px;
  font-weight: 700;
  color: #FFFFFF;
  margin: 0;
}

.menu-close {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.menu-close svg {
  width: 20px;
  height: 20px;
  fill: none;
  stroke: #FFFFFF;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.mobile-menu-nav {
  max-height: calc(85vh - 80px);
  overflow-y: auto;
  padding: 8px 0;
}

.mobile-menu-nav::-webkit-scrollbar {
  width: 4px;
}

.mobile-menu-nav::-webkit-scrollbar-track {
  background: transparent;
}

.mobile-menu-nav::-webkit-scrollbar-thumb {
  background: rgba(255, 215, 0, 0.3);
  border-radius: 2px;
}

.mobile-menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 24px;
  color: rgba(255, 255, 255, 0.8);
  font-size: 15px;
  transition: all 0.2s;
  cursor: pointer;
}

.mobile-menu-item:active {
  background: rgba(255, 255, 255, 0.1);
}

.mobile-menu-item.active {
  background: rgba(255, 215, 0, 0.15);
  color: #FFD700;
}

.mobile-menu-divider {
  height: 1px;
  margin: 8px 24px;
  background: rgba(255, 255, 255, 0.1);
}
</style>