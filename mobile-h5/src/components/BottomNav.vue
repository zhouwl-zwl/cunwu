<template>
  <div v-if="showBottomNav" class="bottom-nav safe-area-bottom">
    <div 
      v-for="item in navItems" 
      :key="item.path" 
      class="nav-item"
      :class="{ active: isActive(item.path) }"
      @click="navigate(item.path)"
    >
      <div class="nav-icon-wrapper">
        <van-icon :name="item.activeIcon" v-if="isActive(item.path)" size="24" color="#FFD700" />
        <van-icon :name="item.icon" v-else size="24" color="rgba(255,255,255,0.7)" />
      </div>
      <div class="nav-text" :class="{ active: isActive(item.path) }">{{ item.text }}</div>
      <div v-if="item.badge && item.badge > 0" class="nav-badge">
        {{ item.badge > 99 ? '99+' : item.badge }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const currentPath = ref('/')
const showBottomNav = ref(false)

const navItems = [
  { path: '/', icon: 'home-o', activeIcon: 'home', text: '首页', badge: 0 }
]

const isActive = (path) => {
  if (path === '/') {
    return currentPath.value === '/'
  }
  return currentPath.value.startsWith(path)
}

const navigate = (path) => {
  if (currentPath.value === path) return
  router.push(path)
}

const updatePath = () => {
  currentPath.value = router.currentRoute.value.path
}

const checkScreenWidth = () => {
  showBottomNav.value = window.innerWidth <= 768
}

const updateBadge = () => {
  const notificationItem = navItems.find(item => item.path === '/notifications')
  if (notificationItem) {
    const unread = localStorage.getItem('unreadCount') || '0'
    notificationItem.badge = parseInt(unread)
  }
}

onMounted(() => {
  updatePath()
  checkScreenWidth()
  updateBadge()
  router.afterEach(updatePath)
  window.addEventListener('resize', checkScreenWidth)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkScreenWidth)
})

watch(() => route.path, () => {
  updateBadge()
})
</script>

<style scoped>
.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 50%, #8B0A1F 100%);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 
    0 -4px 24px rgba(200, 16, 46, 0.3), 
    0 -1px 0 rgba(255, 215, 0, 0.25),
    0 -2px 0 rgba(255, 255, 255, 0.05) inset;
  z-index: 999;
  padding-top: 3px;
  padding-bottom: env(safe-area-inset-bottom);
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 1px;
    background: linear-gradient(90deg, 
      transparent 0%, 
      rgba(255, 215, 0, 0.3) 50%, 
      transparent 100%);
  }
}

.nav-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  transition: all var(--transition-normal);
  cursor: pointer;
  padding-top: 4px;
}

.nav-item:active {
  transform: scale(0.92);
}

.nav-item.active {
  transform: translateY(-4px);
  
  &::after {
    content: '';
    position: absolute;
    bottom: 6px;
    width: 20px;
    height: 3px;
    background: var(--gradient-gold);
    border-radius: 2px;
    box-shadow: 0 0 8px rgba(255, 215, 0, 0.5);
  }
}

.nav-icon-wrapper {
  position: relative;
  transition: all var(--transition-normal);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
}

.nav-item.active .nav-icon-wrapper {
  filter: drop-shadow(0 2px 6px rgba(255, 215, 0, 0.5));
}

.nav-text {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.65);
  margin-top: 2px;
  transition: all var(--transition-normal);
  font-weight: var(--font-weight-medium);
}

.nav-text.active {
  color: var(--gold-color);
  font-weight: var(--font-weight-semibold);
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.25);
  font-size: 12px;
}

.nav-badge {
  position: absolute;
  top: -4px;
  right: 50%;
  transform: translateX(14px);
  min-width: 18px;
  height: 18px;
  background: linear-gradient(135deg, #FF6B6B 0%, #EE5A5A 100%);
  color: #fff;
  font-size: 10px;
  font-weight: var(--font-weight-bold);
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 5px;
  box-shadow: 
    0 2px 6px rgba(255, 71, 87, 0.4),
    0 0 0 2px rgba(200, 16, 46, 0.9);
  animation: badgePop 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  z-index: 1;
}

@keyframes badgePop {
  0% { transform: translateX(14px) scale(0); }
  60% { transform: translateX(14px) scale(1.2); }
  100% { transform: translateX(14px) scale(1); }
}
</style>