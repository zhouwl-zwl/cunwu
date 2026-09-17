<template>
  <div class="page-container village-affairs">
    <van-nav-bar title="村务中心" :border="false" />
    <div class="page-body">
      <div
        v-for="group in menuGroups"
        :key="group.id"
        class="menu-group"
      >
        <div class="group-title">{{ group.title }}</div>
        <div class="group-grid">
          <div
            v-for="item in group.children"
            :key="item.id"
            class="grid-item"
            @click="goRoute(item.route)"
          >
            <div class="grid-icon" :style="{ background: item.color }">
              <van-icon :name="item.icon" size="22" color="#fff" />
            </div>
            <div class="grid-name">{{ item.name }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const menuGroups = [
  {
    id: 1,
    title: '党建',
    children: [
      { id: 11, name: '党员管理', route: '/party-member', icon: 'friends-o', color: '#E91E63' },
      { id: 12, name: '组织生活', route: '/party-activity', icon: 'calendar-o', color: '#9C27B0' },
      { id: 13, name: '党建考核', route: '/party-building', icon: 'medal-o', color: '#673AB7' },
      { id: 14, name: '党员花名册', route: '/party-roster/all', icon: 'contact', color: '#3F51B5' },
      { id: 15, name: '思想学习', route: '/thought-study', icon: 'bookmark-o', color: '#2196F3' },
      { id: 16, name: '志愿服务', route: '/volunteer-roster', icon: 'smile-o', color: '#00BCD4' }
    ]
  },
  {
    id: 2,
    title: '村务管理',
    children: [
      { id: 21, name: '村情概况', route: '/village-info', icon: 'home-o', color: '#4CAF50' },
      { id: 22, name: '组织架构', route: '/org-members', icon: 'cluster-o', color: '#009688' },
      { id: 23, name: '三务公开', route: '/public-notices', icon: 'eye-o', color: '#8BC34A' },
      { id: 24, name: '村会议事', route: '/governance', icon: 'comment-o', color: '#CDDC39' },
      { id: 25, name: '任务督办', route: '/task-supervision', icon: 'todo-list-o', color: '#FFC107' },
      { id: 26, name: '土地管理', route: '/land-management', icon: 'location-o', color: '#FF9800' },
      { id: 27, name: '表格查看', route: '/excel-import', icon: 'description', color: '#00BCD4' }
    ]
  },
  {
    id: 3,
    title: '三资管理',
    children: [
      { id: 31, name: '资产登记', route: '/assets', icon: 'shop-o', color: '#FF5722' },
      { id: 32, name: '工程项目', route: '/project-engineering', icon: 'build-o', color: '#795548' },
      { id: 33, name: '集体经济', route: '/collective-economy', icon: 'chart-trending-o', color: '#607D8B' },
      { id: 34, name: '村级资产', route: '/village-assets', icon: 'gem-o', color: '#F44336' }
    ]
  },
  {
    id: 4,
    title: '乡村振兴',
    children: [
      { id: 41, name: '产业发展', route: '/industry', icon: 'gift-o', color: '#E91E63' },
      { id: 42, name: '环境整治', route: '/environment', icon: 'flower-o', color: '#4CAF50' },
      { id: 43, name: '交通治理', route: '/traffic-management', icon: 'logistics', color: '#2196F3' },
      { id: 44, name: '振兴项目', route: '/rural-revitalization', icon: 'fire-o', color: '#FF9800' }
    ]
  },
  {
    id: 5,
    title: '民政事务',
    children: [
      { id: 51, name: '民政服务', route: '/civil-affairs', icon: 'service-o', color: '#00BCD4' },
      { id: 52, name: '特殊群体', route: '/special-group', icon: 'friends-o', color: '#3F51B5' },
      { id: 53, name: '人口信息', route: '/population', icon: 'chart-o', color: '#009688' },
      { id: 54, name: '惠民补贴', route: '/subsidy', icon: 'gold-o', color: '#FFC107' },
      { id: 55, name: '监测户', route: '/monitoring-households', icon: 'eye-o', color: '#FF5722' },
      { id: 56, name: '务工信息', route: '/labor-info', icon: 'manager-o', color: '#795548' }
    ]
  },
  {
    id: 6,
    title: '安全管理',
    children: [
      { id: 61, name: '消防安全', route: '/fire-safety', icon: 'fire-o', color: '#F44336' },
      { id: 62, name: '森林防火', route: '/forest-fire', icon: 'tree', color: '#4CAF50' },
      { id: 63, name: '食品安全', route: '/food-safety', icon: 'point-gift-o', color: '#FF9800' },
      { id: 64, name: '道路交通', route: '/road-traffic', icon: 'logistics', color: '#2196F3' },
      { id: 65, name: '灾害防范', route: '/safety-hazard', icon: 'warning-o', color: '#9C27B0' }
    ]
  },
  {
    id: 7,
    title: '安全台账',
    children: [
      { id: 71, name: '危房统计', route: '/hazardous-house-stats', icon: 'wap-home-o', color: '#FF5722' },
      { id: 72, name: '燃气排查', route: '/gas-inspection', icon: 'fire-o', color: '#E91E63' },
      { id: 73, name: '减灾能力', route: '/disaster-reduction', icon: 'shield-o', color: '#00BCD4' },
      { id: 74, name: '重点部位', route: '/key-area-stats', icon: 'location-o', color: '#607D8B' },
      { id: 75, name: '供水工程', route: '/water-supply-stats', icon: 'records', color: '#009688' }
    ]
  }
]

const goRoute = (route) => {
  if (route) {
    router.push(route)
  }
}
</script>

<style scoped>
.village-affairs {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #f5f5f5;
}

.page-body {
  padding: 0 12px;
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
</style>
