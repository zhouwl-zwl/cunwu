<template>
  <div 
    class="menu-item" 
    :class="{ active: isActive }"
    @click="handleClick"
  >
    <span class="menu-text">{{ item.name }}</span>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

const router = useRouter()
const route = useRoute()

const isActive = computed(() => {
  return route.path.startsWith(props.item.route)
})

const handleClick = () => {
  router.push(props.item.route)
}
</script>

<style scoped>
.menu-item {
  height: 44px;
  display: flex;
  align-items: center;
  padding: 0 20px 0 48px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.08);
}

.menu-item .menu-text {
  font-size: 14px;
  color: #FFFFFF;
  font-weight: 400;
}

.menu-item.active {
  background: linear-gradient(90deg, #9E0F1E 0%, #C23220 100%);
  box-shadow: 0 4px 12px rgba(158, 15, 30, 0.4), inset 0 1px 0 rgba(255, 215, 0, 0.2);
}

.menu-item.active .menu-text {
  color: #FFD700;
  font-weight: 700;
  font-size: 15px;
}
</style>