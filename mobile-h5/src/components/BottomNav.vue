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
        <van-icon :name="isActive(item.path) ? item.activeIcon : item.icon" size="22" :color="isActive(item.path) ? '#C8102E' : '#999'" />
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
  { path: '/', icon: 'home-o', activeIcon: 'home', text: '首页', badge: 0 },
  { path: '/village-affairs', icon: 'apps-o', activeIcon: 'apps', text: '村务', badge: 0 },
  { path: '/services', icon: 'service-o', activeIcon: 'service', text: '服务', badge: 0 },
  { path: '/mine', icon: 'user-o', activeIcon: 'user', text: '我的', badge: 0 }
]

const isActive = (path) => {
  if (path === '/') {
    return currentPath.value === '/' || currentPath.value === ''
  }
  if (path === '/mine') {
    return currentPath.value.startsWith('/mine') || currentPath.value.startsWith('/profile') || currentPath.value.startsWith('/archive') || currentPath.value.startsWith('/favorites') || currentPath.value.startsWith('/help') || currentPath.value.startsWith('/about') || currentPath.value.startsWith('/notification-settings')
  }
  return currentPath.value.startsWith(path)
}

const navigate = (path) => {
  if (isActive(path)) return
  router.push(path)
}

const updatePath = () => {
  currentPath.value = router.currentRoute.value.path
}

const checkScreenWidth = () => {
  showBottomNav.value = window.innerWidth <= 768
}

const updateBadge = () => {
  const notifItem = navItems.find(item => item.path === '/services')
  if (notifItem) {
    const unread = localStorage.getItem('unreadCount') || '0'
    notifItem.badge = parseInt(unread)
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
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -1px 8px rgba(0, 0, 0, 0.06);
  z-index: 999;
  padding-bottom: env(safe-area-inset-bottom);
  border-top: 1px solid #f0f0f0;
}

.nav-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  transition: all 0.2s ease;
  cursor: pointer;
}

.nav-item:active {
  transform: scale(0.92);
}

.nav-icon-wrapper {
  position: relative;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
}

.nav-text {
  font-size: 10px;
  color: #999;
  margin-top: 2px;
  transition: all 0.2s ease;
}

.nav-text.active {
  color: #C8102E;
  font-weight: 600;
}

.nav-item.active .nav-icon-wrapper {
  filter: drop-shadow(0 2px 4px rgba(200, 16, 46, 0.3));
}

.nav-badge {
  position: absolute;
  top: 2px;
  right: 50%;
  transform: translateX(16px);
  min-width: 16px;
  height: 16px;
  background: #FF4D4F;
  color: #fff;
  font-size: 9px;
  font-weight: 700;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
  z-index: 1;
}
</style>
