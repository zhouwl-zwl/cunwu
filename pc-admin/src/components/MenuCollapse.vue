<template>
  <div class="menu-collapse">
    <div class="collapse-header" @click="toggleCollapse">
      <svg viewBox="0 0 24 24" class="header-icon">
        <path v-for="(d, i) in iconPaths" :key="i" :d="d" />
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
        v-for="child in menu.children" 
        :key="child.id" 
        :item="child" 
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import MenuItem from './MenuItem.vue'

const props = defineProps({
  menu: {
    type: Object,
    required: true
  }
})

// 各分组图标的 path 集合（24x24, stroke 风格）
const ICON_PATHS = {
  flag: ['M4 17h16v2H4zM4 12h16v2H4zM4 7h16v2H4z'],
  document: ['M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z', 'M14 2v6h6', 'M8 13h8M8 17h5'],
  money: ['M3 5h18a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1z', 'M2 10h20'],
  home: ['M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z'],
  shield: ['M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4z'],
  users: ['M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2', 'M13 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0z', 'M23 21v-2a4 4 0 0 0-3-3.87', 'M16 3.13a4 4 0 0 1 0 7.75'],
  chat: ['M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8z'],
  setting: ['M4 21v-7M4 10V3M12 21v-9M12 8V3M20 21v-5M20 12V3M1 14h6M9 8h6M17 16h6']
}

const iconPaths = computed(() => ICON_PATHS[props.menu.icon] || ICON_PATHS.flag)

const isExpanded = ref(false)
const storageKey = `menu_open_v2_${props.menu.id}`

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