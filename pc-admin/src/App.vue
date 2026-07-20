<template>
  <div class="app-container">
    <template v-if="isLoggedIn">
      <div class="mobile-top-bar">
        <button class="menu-toggle" @click="showMobileMenu = !showMobileMenu">
          <svg viewBox="0 0 24 24">
            <path d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z"/>
          </svg>
        </button>
        <div class="top-title">村级智慧村务</div>
        <div class="top-right">
          <span class="user-info">欢迎, {{ roleName }}</span>
        </div>
      </div>
      
      <main class="main-content">
        <router-view />
      </main>
      
      <div class="bottom-nav">
        <div 
          v-for="item in navItems" 
          :key="item.path" 
          class="nav-item"
          :class="{ active: currentPath.startsWith(item.path) }"
          @click="navigate(item.path)"
        >
          <div class="nav-icon">{{ item.icon }}</div>
          <div class="nav-text">{{ item.text }}</div>
        </div>
      </div>
      
      <div v-if="showMobileMenu" class="mobile-menu-overlay" @click="showMobileMenu = false">
        <div class="mobile-menu-content" @click.stop>
          <div class="mobile-menu-header">
            <div class="mobile-brand">
              <div class="mobile-star"></div>
              <h2>村级智慧村务</h2>
            </div>
            <button class="menu-close" @click="showMobileMenu = false">
              <svg viewBox="0 0 24 24">
                <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
              </svg>
            </button>
          </div>
          <div class="mobile-menu-nav">
            <MenuCollapse 
              v-for="menu in menuConfig" 
              :key="menu.id" 
              :menu="menu" 
            />
          </div>
          <div class="mobile-menu-footer">
            <button class="logout-btn" @click="handleLogout">退出登录</button>
          </div>
        </div>
      </div>
    </template>
    <router-view v-else />
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import MenuCollapse from './components/MenuCollapse.vue'
import { menuConfig } from './config/menuConfig'

const router = useRouter()
const route = useRoute()
const showMobileMenu = ref(false)
const currentPath = ref('/')

const navItems = [
  { path: '/dashboard', icon: '🏠', text: '首页' },
  { path: '/demand', icon: '📋', text: '诉求' },
  { path: '/notification', icon: '📢', text: '通知' },
  { path: '/users', icon: '👤', text: '管理' }
]

const isLoggedIn = computed(() => {
  return !!localStorage.getItem('token')
})

const role = computed(() => {
  return localStorage.getItem('role') || ''
})

const roleName = computed(() => {
  if (role.value === 'VILLAGER') return '普通村民'
  if (role.value === 'VILLAGE_OFFICIAL') return '村干部'
  if (role.value === 'RESIDENT_OFFICIAL') return '驻村干部'
  return '管理员'
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  router.push('/login')
}

const navigate = (path) => {
  router.push(path)
  currentPath.value = path
}

watch(() => route.path, () => {
  showMobileMenu.value = false
  currentPath.value = route.path
})

onMounted(() => {
  currentPath.value = route.path
  if (isLoggedIn.value && route.path === '/login') {
    router.push('/dashboard')
  }
})
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background: #FDF5F5;
}

.main-content {
  min-height: 100vh;
  padding-top: 56px;
  padding-bottom: 60px;
}

.mobile-top-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 56px;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  display: flex;
  align-items: center;
  padding: 0 16px;
  z-index: 50;
  box-shadow: 0 2px 8px rgba(200, 16, 46, 0.3);
}

.menu-toggle {
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

.menu-toggle svg {
  width: 22px;
  height: 22px;
  fill: none;
  stroke: #FFD700;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.top-title {
  flex: 1;
  text-align: center;
  font-size: 17px;
  font-weight: 700;
  color: #FFFFFF;
  margin-left: -40px;
}

.top-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-info {
  font-size: 13px;
  color: #FFD700;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -4px 20px rgba(200, 16, 46, 0.4);
  z-index: 999;
  border-top: 2px solid #FFD700;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  transition: all 0.3s;
}

.nav-item.active {
  transform: translateY(-2px);
}

.nav-item.active .nav-icon {
  font-size: 28px;
  filter: drop-shadow(0 2px 4px rgba(255, 215, 0, 0.5));
}

.nav-item.active .nav-text {
  color: #FFD700;
  font-weight: bold;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.nav-icon {
  font-size: 24px;
  margin-bottom: 2px;
  transition: font-size 0.3s;
}

.nav-text {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.8);
  transition: color 0.3s;
}

.mobile-menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 200;
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
  max-height: calc(85vh - 140px);
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

.mobile-menu-footer {
  padding: 16px 24px;
  border-top: 1px solid rgba(255, 215, 0, 0.1);
}

.mobile-menu-footer .logout-btn {
  width: 100%;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: #FFD700;
  font-weight: bold;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 15px;
}
</style>
