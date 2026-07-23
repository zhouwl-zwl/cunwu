<template>
  <div class="page-container">
    <van-nav-bar title="防溺水" left-arrow @click-left="goBack" />
    
    <div class="header-banner">
      <div class="banner-icon">
        <van-icon name="alert-circle" size="48" color="#FF5722" />
      </div>
      <div class="banner-title">防溺水安全管理</div>
      <div class="banner-desc">珍爱生命 预防溺水</div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="friends-o" size="20" color="#2196F3" />
          <h3>功能模块</h3>
        </div>
      </div>
      <div class="grid-2">
        <div class="function-card" @click="goPage('/drowning-education')">
          <div class="card-icon blue-bg">
            <van-icon name="book-o" size="36" color="#fff" />
          </div>
          <div class="card-text">防溺水教育</div>
          <div class="card-sub">安全知识学习</div>
        </div>
        <div class="function-card" @click="goPage('/drowning-promotion')">
          <div class="card-icon orange-bg">
            <van-icon name="volume-o" size="36" color="#fff" />
          </div>
          <div class="card-text">防溺水宣传</div>
          <div class="card-sub">宣传活动记录</div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="warning-o" size="20" color="#FF5722" />
          <h3>安全提示</h3>
        </div>
      </div>
      <div class="tips-list">
        <div class="tip-item">
          <van-icon name="checked" size="18" color="#4CAF50" />
          <span>不准私自下水游泳</span>
        </div>
        <div class="tip-item">
          <van-icon name="checked" size="18" color="#4CAF50" />
          <span>不准擅自与他人结伴游泳</span>
        </div>
        <div class="tip-item">
          <van-icon name="checked" size="18" color="#4CAF50" />
          <span>不准在无家长或老师带队的情况下游泳</span>
        </div>
        <div class="tip-item">
          <van-icon name="checked" size="18" color="#4CAF50" />
          <span>不准到不熟悉的水域游泳</span>
        </div>
        <div class="tip-item">
          <van-icon name="checked" size="18" color="#4CAF50" />
          <span>不准到无安全设施、无救护人员的水域游泳</span>
        </div>
        <div class="tip-item">
          <van-icon name="checked" size="18" color="#4CAF50" />
          <span>不准不熟悉水性的学生擅自下水施救</span>
        </div>
      </div>
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

const goPage = (path) => {
  router.push(path)
}

onMounted(() => {
  document.title = '防溺水 - 村级智慧村务平台'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.header-banner {
  background: linear-gradient(135deg, #FF9800 0%, #FF5722 100%);
  padding: 30px 20px;
  text-align: center;
  color: #fff;
}

.banner-icon {
  margin-bottom: 12px;
}

.banner-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 6px;
}

.banner-desc {
  font-size: 13px;
  opacity: 0.9;
}

.module-card {
  background: #fff;
  margin: 12px;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.module-header {
  margin-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.module-header h3 {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.grid-2 {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.function-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 12px;
  border-radius: 12px;
  background: #f9f9f9;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid #eee;
}

.function-card:active {
  transform: scale(0.95);
  background: #f0f0f0;
}

.card-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
}

.card-icon.blue-bg {
  background: linear-gradient(135deg, #2196F3 0%, #1565C0 100%);
}

.card-icon.orange-bg {
  background: linear-gradient(135deg, #FF9800 0%, #E65100 100%);
}

.card-text {
  font-size: 15px;
  color: #333;
  font-weight: 600;
  margin-bottom: 4px;
}

.card-sub {
  font-size: 12px;
  color: #999;
}

.tips-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  background: #FFF8E1;
  border-radius: 8px;
  font-size: 14px;
  color: #333;
  line-height: 1.5;
}
</style>