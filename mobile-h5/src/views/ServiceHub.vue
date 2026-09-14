<template>
  <div class="page-container service-hub">
    <van-nav-bar title="服务大厅" :border="false" />
    <div class="page-body">
      <!-- 搜索 -->
      <div class="search-bar" @click="goSearch">
        <van-icon name="search" size="16" color="#999" />
        <span class="search-placeholder">搜索政策、服务、通知...</span>
      </div>

      <!-- 常用服务 -->
      <div class="menu-group">
        <div class="group-title">常用服务</div>
        <div class="group-grid">
          <div class="grid-item" v-for="item in commonServices" :key="item.name" @click="goRoute(item.route)">
            <div class="grid-icon" :style="{ background: item.color }">
              <van-icon :name="item.icon" size="22" color="#fff" />
            </div>
            <div class="grid-name">{{ item.name }}</div>
          </div>
        </div>
      </div>

      <!-- 诉求与反馈 -->
      <div class="menu-group">
        <div class="group-title">诉求与反馈</div>
        <div class="list-group">
          <div class="list-item" v-for="item in feedbackServices" :key="item.name" @click="goRoute(item.route)">
            <div class="list-icon" :style="{ background: item.color }">
              <van-icon :name="item.icon" size="18" color="#fff" />
            </div>
            <span class="list-name">{{ item.name }}</span>
            <van-icon name="arrow" size="14" color="#ccc" />
          </div>
        </div>
      </div>

      <!-- 通知与公告 -->
      <div class="menu-group">
        <div class="group-title">通知公告</div>
        <div class="list-group">
          <div class="list-item" v-for="item in noticeServices" :key="item.name" @click="goRoute(item.route)">
            <div class="list-icon" :style="{ background: item.color }">
              <van-icon :name="item.icon" size="18" color="#fff" />
            </div>
            <span class="list-name">{{ item.name }}</span>
            <div class="list-badge" v-if="item.badge">{{ item.badge }}</div>
            <van-icon name="arrow" size="14" color="#ccc" />
          </div>
        </div>
      </div>

      <!-- 学习园地 -->
      <div class="menu-group">
        <div class="group-title">学习园地</div>
        <div class="list-group">
          <div class="list-item" v-for="item in studyServices" :key="item.name" @click="goRoute(item.route)">
            <div class="list-icon" :style="{ background: item.color }">
              <van-icon :name="item.icon" size="18" color="#fff" />
            </div>
            <span class="list-name">{{ item.name }}</span>
            <van-icon name="arrow" size="14" color="#ccc" />
          </div>
        </div>
      </div>

      <!-- 便民资源 -->
      <div class="menu-group">
        <div class="group-title">便民资源</div>
        <div class="list-group">
          <div class="list-item" v-for="item in resourceServices" :key="item.name" @click="goRoute(item.route)">
            <div class="list-icon" :style="{ background: item.color }">
              <van-icon :name="item.icon" size="18" color="#fff" />
            </div>
            <span class="list-name">{{ item.name }}</span>
            <van-icon name="arrow" size="14" color="#ccc" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const commonServices = [
  { name: '政策查询', route: '/policy', icon: 'search', color: '#2196F3' },
  { name: '惠民补贴', route: '/subsidy', icon: 'gift-o', color: '#FF9800' },
  { name: '矛盾调解', route: '/dispute', icon: 'chat-o', color: '#4CAF50' },
  { name: '社保查询', route: '/social-security', icon: 'medal-o', color: '#9C27B0' },
  { name: '社会救助', route: '/social-aid', icon: 'heart-o', color: '#E91E63' },
  { name: '低保申请', route: '/subsistence-allowance', icon: 'balance-o', color: '#00BCD4' },
  { name: '残疾人服务', route: '/disabled', icon: 'friends-o', color: '#FF5722' },
  { name: '特困供养', route: '/special-support', icon: 'shield-o', color: '#795548' }
]

const feedbackServices = [
  { name: '诉求上报', route: '/demand', icon: 'edit', color: '#D22630' },
  { name: '我的诉求', route: '/demand/my', icon: 'records', color: '#FF9800' },
  { name: '诉求列表', route: '/demand-list', icon: 'list-switching', color: '#2196F3' }
]

const noticeServices = [
  { name: '通知公告', route: '/notifications', icon: 'bell', color: '#FF9800', badge: '' },
  { name: '三务公开', route: '/public-notices', icon: 'eye-o', color: '#4CAF50', badge: '' },
  { name: '村会议事', route: '/governance', icon: 'comment-o', color: '#2196F3', badge: '' }
]

const studyServices = [
  { name: '思想学习', route: '/thought-study', icon: 'bookmark-o', color: '#D22630' },
  { name: '媒体宣传', route: '/media-promotion', icon: 'tv-o', color: '#9C27B0' },
  { name: '文档库', route: '/document-library', icon: 'description', color: '#607D8B' },
  { name: '防溺水教育', route: '/drowning-prevention', icon: 'lifebuoy', color: '#00BCD4' }
]

const resourceServices = [
  { name: '物资仓库', route: '/material-warehouse', icon: 'shop-o', color: '#FF5722' },
  { name: '重点敲门', route: '/knock-door-roster', icon: 'send-gift-o', color: '#E91E63' },
  { name: '重点人员', route: '/key-personnel', icon: 'contact', color: '#3F51B5' },
  { name: '留守老人', route: '/left-behind-elderly', icon: 'friends-o', color: '#795548' },
  { name: '留守儿童', route: '/left-behind-children', icon: 'smile-o', color: '#009688' },
  { name: '雨露计划', route: '/rain-plan', icon: 'umbrella-circle', color: '#00BCD4' }
]

const goRoute = (route) => router.push(route)
const goSearch = () => router.push('/search-result')
</script>

<style scoped>
.service-hub {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #f5f5f5;
}

.page-body {
  padding: 0 12px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #fff;
  border-radius: 20px;
  height: 38px;
  padding: 0 14px;
  margin-bottom: 10px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.search-placeholder {
  font-size: 13px;
  color: #999;
}

.menu-group {
  margin-bottom: 8px;
  background: #fff;
  border-radius: 12px;
  padding: 14px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.group-title {
  font-size: 14px;
  font-weight: 700;
  color: #333;
  margin-bottom: 14px;
  padding-left: 8px;
  border-left: 3px solid #C8102E;
}

/* 九宫格 */
.group-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px 0;
}

.grid-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  cursor: pointer;
}

.grid-item:active .grid-icon {
  transform: scale(0.9);
}

.grid-icon {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s;
}

.grid-name {
  font-size: 11px;
  color: #666;
}

/* 列表样式 */
.list-group {
  display: flex;
  flex-direction: column;
}

.list-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}

.list-item:last-child {
  border-bottom: none;
}

.list-item:active {
  background: #fafafa;
}

.list-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.list-name {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.list-badge {
  min-width: 18px;
  height: 18px;
  background: #FF4D4F;
  color: #fff;
  font-size: 10px;
  font-weight: 700;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 5px;
  margin-right: 8px;
}
</style>
