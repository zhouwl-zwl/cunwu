<template>
  <div class="menu-collapse">
    <div class="collapse-header" @click="toggleCollapse">
      <svg viewBox="0 0 24 24" class="header-icon" v-if="menu.icon === 'flag'">
        <path d="M4 17h16v2H4zM4 12h16v2H4zM4 7h16v2H4z"/>
        <path d="M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-2 16H8v-2h8v2zm0-4H8v-2h8v2zm0-4H8V8h8v2zm0-4H8V4h8v2z"/>
      </svg>
      <svg viewBox="0 0 24 24" class="header-icon" v-else-if="menu.icon === 'home'">
        <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/>
      </svg>
      <svg viewBox="0 0 24 24" class="header-icon" v-else-if="menu.icon === 'wheat'">
        <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
      </svg>
      <svg viewBox="0 0 24 24" class="header-icon" v-else-if="menu.icon === 'users'">
        <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
      </svg>
      <svg viewBox="0 0 24 24" class="header-icon" v-else-if="menu.icon === 'shield'">
        <path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4z"/>
      </svg>
      <span class="header-title">{{ menu.title }}</span>
      <svg 
        viewBox="0 0 24 24" 
        class="header-arrow"
        :class="{ expanded: isExpanded }"
      >
        <path d="M7 10l5 5 5-5H7z"/>
      </svg>
    </div>
    
    <div class="collapse-content" :class="{ expanded: isExpanded }">
      <MenuItem 
        v-for="child in (menu?.children || [])" 
        :key="child?.id || Math.random()" 
        :item="child" 
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import MenuItem from './MenuItem.vue'

const props = defineProps({
  menu: {
    type: Object,
    required: true
  }
})

const isExpanded = ref(false)
const storageKey = `menu_open_${props.menu.id}`

const toggleCollapse = () => {
  isExpanded.value = !isExpanded.value
  localStorage.setItem(storageKey, String(isExpanded.value))
}

onMounted(() => {
  const saved = localStorage.getItem(storageKey)
  if (saved !== null) {
    isExpanded.value = saved === 'true'
  } else {
    isExpanded.value = props.menu.defaultOpen || false
    localStorage.setItem(storageKey, String(isExpanded.value))
  }
})
</script>

<style scoped>
.menu-collapse {
  margin-bottom: 4px;
}

.collapse-header {
  display: flex;
  align-items: center;
  height: 56px;
  padding: 0 20px;
  background: #C8102E;
  cursor: pointer;
  transition: all 0.2s ease;
}

.collapse-header:hover {
  background: #B00E28;
}

.collapse-header:active {
  opacity: 0.9;
}

.header-icon {
  width: 24px;
  height: 24px;
  fill: none;
  stroke: #FFD700;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
  margin-right: 12px;
  flex-shrink: 0;
}

.header-title {
  flex: 1;
  font-size: 16px;
  font-weight: 700;
  color: #FFFFFF;
}

.header-arrow {
  width: 20px;
  height: 20px;
  fill: none;
  stroke: #FFD700;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
  transition: transform 0.2s ease;
  flex-shrink: 0;
}

.header-arrow.expanded {
  transform: rotate(180deg);
}

.collapse-content {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.25s ease-out;
}

.collapse-content.expanded {
  max-height: 800px;
}
</style>
