<template>
  <div class="page-container">
    <van-nav-bar title="防溺水教育" left-arrow @click-left="goBack" />
    
    <div class="search-bar">
      <van-search v-model="searchKeyword" placeholder="搜索教育内容" shape="round" />
    </div>

    <div class="stats-bar">
      <div class="stat-item">
        <div class="stat-value">{{ educationList.length }}</div>
        <div class="stat-label">教育课程</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <div class="stat-value">{{ completedCount }}</div>
        <div class="stat-label">已学习</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <div class="stat-value">{{ pendingCount }}</div>
        <div class="stat-label">待学习</div>
      </div>
    </div>

    <div class="content-list">
      <div 
        v-for="item in filteredList" 
        :key="item.id" 
        class="edu-card"
        @click="viewDetail(item)"
      >
        <div class="edu-icon" :class="item.type === 'video' ? 'video-icon' : 'doc-icon'">
          <van-icon :name="item.type === 'video' ? 'play-circle-o' : 'file-text-o'" size="28" color="#fff" />
        </div>
        <div class="edu-content">
          <div class="edu-title">{{ item.title }}</div>
          <div class="edu-desc">{{ item.desc }}</div>
          <div class="edu-meta">
            <span class="meta-item">
              <van-icon name="clock-o" size="12" />
              {{ item.duration }}
            </span>
            <span class="meta-item">
              <van-icon name="eye-o" size="12" />
              {{ item.views }}次学习
            </span>
            <span class="status-tag" :class="item.completed ? 'completed' : 'pending'">
              {{ item.completed ? '已学习' : '待学习' }}
            </span>
          </div>
        </div>
        <van-icon name="arrow" size="16" color="#ccc" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchKeyword = ref('')

const educationList = ref([
  { id: 1, title: '防溺水安全知识', desc: '了解溺水的危害和预防措施', type: 'video', duration: '15分钟', views: 328, completed: true },
  { id: 2, title: '游泳安全须知', desc: '游泳前的准备和注意事项', type: 'doc', duration: '10分钟', views: 256, completed: true },
  { id: 3, title: '溺水急救方法', desc: '发现有人溺水如何正确施救', type: 'video', duration: '20分钟', views: 412, completed: false },
  { id: 4, title: '儿童防溺水教育', desc: '针对儿童的防溺水安全教育', type: 'video', duration: '12分钟', views: 567, completed: false },
  { id: 5, title: '户外水域安全指南', desc: '河流、湖泊、水库等野外水域安全', type: 'doc', duration: '8分钟', views: 189, completed: false },
  { id: 6, title: '心肺复苏(CPR)教程', desc: '溺水急救的心肺复苏操作方法', type: 'video', duration: '25分钟', views: 678, completed: false },
  { id: 7, title: '汛期安全防范知识', desc: '雨季汛期防溺水安全要点', type: 'doc', duration: '6分钟', views: 234, completed: false },
  { id: 8, title: '水上运动安全规范', desc: '划船、漂流等水上活动安全', type: 'video', duration: '18分钟', views: 145, completed: false }
])

const completedCount = computed(() => educationList.value.filter(i => i.completed).length)
const pendingCount = computed(() => educationList.value.filter(i => !i.completed).length)

const filteredList = computed(() => {
  if (!searchKeyword.value) return educationList.value
  const keyword = searchKeyword.value.toLowerCase()
  return educationList.value.filter(item => 
    item.title.toLowerCase().includes(keyword) || 
    item.desc.toLowerCase().includes(keyword)
  )
})

const goBack = () => {
  router.back()
}

const viewDetail = (item) => {
  console.log('查看教育内容:', item.title)
}

onMounted(() => {
  document.title = '防溺水教育 - 村级智慧村务平台'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.search-bar {
  background: #fff;
  padding: 8px 12px;
}

.stats-bar {
  display: flex;
  background: #fff;
  padding: 16px;
  margin-bottom: 12px;
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #2196F3;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.stat-divider {
  width: 1px;
  background: #eee;
}

.content-list {
  padding: 0 12px;
}

.edu-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 12px;
  padding: 14px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.edu-card:active {
  transform: scale(0.98);
  background: #f9f9f9;
}

.edu-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 14px;
  flex-shrink: 0;
}

.video-icon {
  background: linear-gradient(135deg, #E91E63 0%, #C2185B 100%);
}

.doc-icon {
  background: linear-gradient(135deg, #2196F3 0%, #1565C0 100%);
}

.edu-content {
  flex: 1;
  min-width: 0;
}

.edu-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.edu-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.edu-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.meta-item {
  font-size: 11px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 3px;
}

.status-tag {
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 500;
}

.status-tag.completed {
  background: #E8F5E9;
  color: #4CAF50;
}

.status-tag.pending {
  background: #FFF3E0;
  color: #FF9800;
}
</style>