
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue')
  },
  // 党建工作
  {
    path: '/party/members',
    name: 'PartyMemberManagement',
    component: () => import('../views/PartyMemberManagement.vue')
  },
  {
    path: '/party/activities',
    name: 'PartyActivityManagement',
    component: () => import('../views/PartyActivityManagement.vue')
  },
  // 政务工作
  {
    path: '/government/meetings',
    name: 'MeetingManagement',
    component: () => import('../views/MeetingManagement.vue')
  },
  {
    path: '/government/documents',
    name: 'DocumentRecordManagement',
    component: () => import('../views/DocumentRecordManagement.vue')
  },
  // 便民服务
  {
    path: '/convenience/services',
    name: 'ConvenienceServiceManagement',
    component: () => import('../views/ConvenienceServiceManagement.vue')
  },
  {
    path: '/convenience/special-groups',
    name: 'SpecialGroupManagement',
    component: () => import('../views/SpecialGroupManagement.vue')
  },
  // 三资管理
  {
    path: '/assets/funds',
    name: 'FundManagement',
    component: () => import('../views/FundManagement.vue')
  },
  {
    path: '/assets',
    name: 'AssetsManagement',
    component: () => import('../views/AssetsManagement.vue')
  },
  {
    path: '/assets/resources',
    name: 'ResourceManagement',
    component: () => import('../views/ResourceManagement.vue')
  },
  // 安全防护
  {
    path: '/safety/hazards',
    name: 'SafetyHazardManagement',
    component: () => import('../views/SafetyHazardManagement.vue')
  },
  {
    path: '/safety/flood',
    name: 'FloodControlManagement',
    component: () => import('../views/FloodControlManagement.vue')
  },
  {
    path: '/safety/fire',
    name: 'FireSafetyManagement',
    component: () => import('../views/FireSafetyManagement.vue')
  },
  {
    path: '/safety/food',
    name: 'FoodSafetyManagement',
    component: () => import('../views/FoodSafetyManagement.vue')
  },
  // 乡村振兴
  {
    path: '/rural/toilets',
    name: 'ToiletReformManagement',
    component: () => import('../views/ToiletReformManagement.vue')
  },
  {
    path: '/rural/forest',
    name: 'ForestFireManagement',
    component: () => import('../views/ForestFireManagement.vue')
  },
  {
    path: '/rural/road',
    name: 'RoadTrafficManagement',
    component: () => import('../views/RoadTrafficManagement.vue')
  },
  // 人居环境整治
  {
    path: '/environment/improvement',
    name: 'EnvironmentImprovementManagement',
    component: () => import('../views/EnvironmentImprovementManagement.vue')
  },
  // 综合治理
  {
    path: '/governance/disputes',
    name: 'DisputeManagement',
    component: () => import('../views/DisputeManagement.vue')
  },
  // 信访矛盾调解
  {
    path: '/governance/petition',
    name: 'PetitionManagement',
    component: () => import('../views/PetitionManagement.vue')
  },
  // 人口户籍管理
  {
    path: '/population/list',
    name: 'PopulationManagement',
    component: () => import('../views/PopulationManagement.vue')
  },
  // 土地宅基地管理
  {
    path: '/land/house',
    name: 'LandHouseManagement',
    component: () => import('../views/LandHouseManagement.vue')
  },
  // 惠民补贴发放
  {
    path: '/subsidy/list',
    name: 'SubsidyManagement',
    component: () => import('../views/SubsidyManagement.vue')
  },
  // 志愿者管理
  {
    path: '/volunteer/list',
    name: 'VolunteerManagement',
    component: () => import('../views/VolunteerManagement.vue')
  },
  // 项目工程管理
  {
    path: '/project/list',
    name: 'ProjectManagement',
    component: () => import('../views/ProjectManagement.vue')
  },
  // 媒体宣传管理
  {
    path: '/media/list',
    name: 'MediaManagement',
    component: () => import('../views/MediaManagement.vue')
  },
  // 档案资料管理
  {
    path: '/archive/list',
    name: 'ArchiveManagement',
    component: () => import('../views/ArchiveManagement.vue')
  },
  // 养老医保民政
  {
    path: '/pension/insurance',
    name: 'PensionInsuranceManagement',
    component: () => import('../views/PensionInsuranceManagement.vue')
  },
  // 物资资产仓库
  {
    path: '/warehouse/list',
    name: 'WarehouseManagement',
    component: () => import('../views/WarehouseManagement.vue')
  },
  // 系统权限管理
  {
    path: '/system/permission',
    name: 'SystemPermissionManagement',
    component: () => import('../views/SystemPermissionManagement.vue')
  },
  // 公共功能
  {
    path: '/public/tasks',
    name: 'TaskSupervisionManagement',
    component: () => import('../views/TaskSupervisionManagement.vue')
  },
  {
    path: '/public/documents',
    name: 'DocumentLibraryManagement',
    component: () => import('../views/DocumentLibraryManagement.vue')
  },
  {
    path: '/public/logs',
    name: 'OperationLogManagement',
    component: () => import('../views/OperationLogManagement.vue')
  },
  // 系统设置
  {
    path: '/users',
    name: 'UserManagement',
    component: () => import('../views/UserManagement.vue')
  },
  {
    path: '/village-config',
    name: 'VillageConfig',
    component: () => import('../views/VillageConfig.vue')
  },
  {
    path: '/audit/list',
    name: 'UserInfoAuditManagement',
    component: () => import('../views/UserInfoAuditManagement.vue')
  },
  // 兼容旧路由
  {
    path: '/demand',
    name: 'DemandManagement',
    component: () => import('../views/DemandManagement.vue')
  },
  {
    path: '/notification',
    name: 'NotificationManagement',
    component: () => import('../views/NotificationManagement.vue')
  },
  {
    path: '/public-notice',
    name: 'PublicNoticeManagement',
    component: () => import('../views/PublicNoticeManagement.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
