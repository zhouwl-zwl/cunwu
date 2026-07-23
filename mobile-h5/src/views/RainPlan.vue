<template>
  <div class="page-container">
    <van-nav-bar title="雨露计划" left-arrow @click-left="goBack" />
    <div class="content-wrapper">
      <van-empty description="暂无雨露计划信息" />
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.back()
}

onMounted(() => {
  document.title = '雨露计划 - 村级智慧村务平台'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.content-wrapper {
  padding: 16px;
}
</style>
