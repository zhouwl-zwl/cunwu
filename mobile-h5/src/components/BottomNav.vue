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
  height: 56px;
  background: rgba(200, 16, 46, 0.98);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -4px 20px rgba(200, 16, 46, 0.3), 0 -1px 0 rgba(255, 215, 0, 0.2);
  z-index: 999;
  padding-top: 2px;
}

.nav-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  transition: all 0.25s ease;
  cursor: pointer;
}

.nav-item:active {
  transform: scale(0.95);
}

.nav-item.active {
  transform: translateY(-3px);
}

.nav-icon-wrapper {
  position: relative;
  transition: all 0.25s ease;
}

.nav-item.active .nav-icon-wrapper {
  filter: drop-shadow(0 2px 4px rgba(255, 215, 0, 0.4));
}

.nav-text {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 2px;
  transition: all 0.25s ease;
}

.nav-text.active {
  color: #FFD700;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.nav-badge {
  position: absolute;
  top: -2px;
  right: 50%;
  transform: translateX(12px);
  min-width: 16px;
  height: 16px;
  background: #FF4757;
  color: #fff;
  font-size: 10px;
  font-weight: bold;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
  box-shadow: 0 2px 4px rgba(255, 71, 87, 0.4);
  animation: badgePop 0.3s ease;
}

@keyframes badgePop {
  0% { transform: translateX(12px) scale(0); }
  50% { transform: translateX(12px) scale(1.2); }
  100% { transform: translateX(12px) scale(1); }
}
</style>