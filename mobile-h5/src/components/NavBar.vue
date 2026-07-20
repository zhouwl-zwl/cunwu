<template>
  <van-nav-bar 
    :title="title" 
    left-arrow 
    @click-left="handleBack"
    :z-index="zIndex"
  >
    <template #right>
      <slot name="right"></slot>
    </template>
  </van-nav-bar>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router'
import { goBack } from '../utils/index'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  fallbackPath: {
    type: String,
    default: '/'
  },
  zIndex: {
    type: Number,
    default: 100
  }
})

const router = useRouter()

const handleBack = () => {
  goBack(router, props.fallbackPath)
}
</script>

<style scoped>
:deep(.van-nav-bar) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%) !important;
}

:deep(.van-nav-bar__title) {
  color: #FFD700 !important;
  font-weight: 600;
}

:deep(.van-nav-bar__left) {
  color: #FFD700 !important;
}

:deep(.van-nav-bar__left-icon) {
  color: #FFD700 !important;
}

:deep(.van-icon-arrow-left) {
  color: #FFD700 !important;
}
</style>
