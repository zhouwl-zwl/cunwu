<template>
  <div class="layout-container">
    <main class="main-content">
      <router-view />
    </main>
    
    <BottomNav v-if="showBottomNav" />
  </div>
</template>

<script setup>
import { watch, ref } from 'vue'
import { useRoute } from 'vue-router'
import BottomNav from './BottomNav.vue'

const route = useRoute()
const showBottomNav = ref(true)

watch(() => route.path, (newPath) => {
  const isLoginPage = ['/login', '/register'].includes(newPath)
  showBottomNav.value = !isLoginPage && !route.meta.hideBottomNav
}, { immediate: true })
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background: #FDF5F5;
}

.main-content {
  padding-bottom: 60px;
}
</style>