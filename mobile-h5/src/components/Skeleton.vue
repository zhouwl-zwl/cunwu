<template>
  <div class="skeleton-container">
    <div class="skeleton-header" v-if="showHeader">
      <div class="skeleton-avatar"></div>
      <div class="skeleton-info">
        <div class="skeleton-name"></div>
        <div class="skeleton-desc"></div>
      </div>
    </div>

    <div class="skeleton-banner" v-if="showBanner">
      <div class="banner-content">
        <div class="banner-title"></div>
        <div class="banner-desc"></div>
      </div>
    </div>

    <div class="skeleton-grid" v-if="showGrid">
      <div class="grid-item" v-for="n in 8" :key="n">
        <div class="grid-icon"></div>
        <div class="grid-text"></div>
      </div>
    </div>

    <div class="skeleton-list" v-if="showList">
      <div class="list-item" v-for="n in 4" :key="n">
        <div class="list-header">
          <div class="list-tag"></div>
          <div class="list-tag"></div>
        </div>
        <div class="list-title"></div>
        <div class="list-content"></div>
        <div class="list-footer">
          <div class="list-time"></div>
          <div class="list-extra"></div>
        </div>
      </div>
    </div>

    <div class="skeleton-card" v-if="showCard">
      <div class="card-header">
        <div class="card-title"></div>
      </div>
      <div class="card-body">
        <div class="card-row" v-for="n in 5" :key="n">
          <div class="card-label"></div>
          <div class="card-value"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  showHeader: {
    type: Boolean,
    default: false
  },
  showBanner: {
    type: Boolean,
    default: false
  },
  showGrid: {
    type: Boolean,
    default: false
  },
  showList: {
    type: Boolean,
    default: false
  },
  showCard: {
    type: Boolean,
    default: false
  }
})
</script>

<style scoped>
.skeleton-container {
  padding: 16px;
  background: #fff;
}

.skeleton-header {
  display: flex;
  align-items: center;
  padding: 16px;
  margin-bottom: 16px;
}

.skeleton-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

.skeleton-info {
  flex: 1;
  margin-left: 16px;
}

.skeleton-name {
  width: 60%;
  height: 20px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
  margin-bottom: 8px;
}

.skeleton-desc {
  width: 40%;
  height: 14px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

.skeleton-banner {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 16px;
}

.banner-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.banner-title {
  width: 120px;
  height: 24px;
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.3);
  animation: skeleton-loading 1.5s infinite;
}

.banner-desc {
  width: 80px;
  height: 16px;
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.2);
  margin-top: 8px;
  animation: skeleton-loading 1.5s infinite;
}

.skeleton-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}

.grid-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.grid-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
  margin-bottom: 8px;
}

.grid-text {
  width: 60%;
  height: 14px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

.skeleton-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.list-item {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.list-header {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.list-tag {
  width: 40px;
  height: 20px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

.list-title {
  width: 80%;
  height: 18px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
  margin-bottom: 8px;
}

.list-content {
  width: 100%;
  height: 14px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
  margin-bottom: 12px;
}

.list-footer {
  display: flex;
  justify-content: space-between;
}

.list-time {
  width: 80px;
  height: 12px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

.list-extra {
  width: 60px;
  height: 12px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

.skeleton-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
}

.card-header {
  margin-bottom: 16px;
}

.card-title {
  width: 50%;
  height: 20px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.card-row {
  display: flex;
  justify-content: space-between;
}

.card-label {
  width: 60px;
  height: 14px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

.card-value {
  width: 60%;
  height: 14px;
  border-radius: 4px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite;
}

@keyframes skeleton-loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}
</style>
