import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: {
      title: '登录',
      requireAuth: false,
      hideBottomNav: true
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: {
      title: '注册',
      requireAuth: false,
      hideBottomNav: true
    }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../components/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: {
          title: '首页',
          requireAuth: false,
          icon: 'home'
        }
      },
      {
        path: 'org-members',
        name: 'OrgMembers',
        component: () => import('../views/OrgMembers.vue'),
        meta: {
          title: '组织成员',
          requireAuth: false
        }
      },
      {
        path: 'village-info',
        name: 'VillageInfo',
        component: () => import('../views/VillageInfo.vue'),
        meta: {
          title: '村情概况',
          requireAuth: false
        }
      },
      {
        path: 'village-detail/:id',
        name: 'VillageDetail',
        component: () => import('../views/VillageDetail.vue'),
        meta: {
          title: '村详情',
          requireAuth: false
        }
      },
      {
        path: 'public-notices',
        name: 'PublicNotices',
        component: () => import('../views/PublicNotices.vue'),
        meta: {
          title: '三务公开',
          requireAuth: false
        }
      },
      {
        path: 'public-notice-detail/:id',
        name: 'PublicNoticeDetail',
        component: () => import('../views/PublicNoticeDetail.vue'),
        meta: {
          title: '公告详情',
          requireAuth: false
        }
      },
      {
        path: 'demand',
        name: 'Demand',
        component: () => import('../views/Demand.vue'),
        meta: {
          title: '诉求上报',
          requireAuth: false
        }
      },
      {
        path: 'demand-list',
        name: 'DemandList',
        component: () => import('../views/DemandList.vue'),
        meta: {
          title: '诉求列表',
          requireAuth: false
        }
      },
      {
        path: 'demand-submit',
        name: 'DemandSubmit',
        component: () => import('../views/DemandSubmit.vue'),
        meta: {
          title: '诉求提交',
          requireAuth: false
        }
      },
      {
        path: 'demand-detail/:id',
        name: 'DemandDetail',
        component: () => import('../views/DemandDetail.vue'),
        meta: {
          title: '诉求详情',
          requireAuth: false
        }
      },
      {
        path: 'demand/my',
        name: 'MyDemands',
        component: () => import('../views/MyDemand.vue'),
        meta: {
          title: '我的诉求',
          requireAuth: false
        }
      },
      {
        path: 'notifications',
        name: 'Notifications',
        component: () => import('../views/Notifications.vue'),
        meta: {
          title: '通知公告',
          requireAuth: false
        }
      },
      {
        path: 'safety-hazard',
        name: 'SafetyHazard',
        component: () => import('../views/SafetyHazard.vue'),
        meta: {
          title: '灾害防范',
          requireAuth: false
        }
      },
      {
        path: 'notification-detail/:id',
        name: 'NotificationDetail',
        component: () => import('../views/NotificationDetail.vue'),
        meta: {
          title: '通知详情',
          requireAuth: false
        }
      },
      {
        path: 'dispute',
        name: 'Dispute',
        component: () => import('../views/Dispute.vue'),
        meta: {
          title: '矛盾纠纷',
          requireAuth: false
        }
      },
      {
        path: 'subsidy',
        name: 'Subsidy',
        component: () => import('../views/Subsidy.vue'),
        meta: {
          title: '惠民补贴',
          requireAuth: false
        }
      },
      {
        path: 'mine',
        name: 'Mine',
        component: () => import('../views/Mine.vue'),
        meta: {
          title: '我的',
          requireAuth: false,
          icon: 'mine'
        }
      },
      {
        path: 'change-password',
        name: 'ChangePassword',
        component: () => import('../views/ChangePassword.vue'),
        meta: {
          title: '修改密码',
          requireAuth: false
        }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue'),
        meta: {
          title: '个人资料',
          requireAuth: false
        }
      },
      {
        path: 'archive',
        name: 'Archive',
        component: () => import('../views/Archive.vue'),
        meta: {
          title: '个人档案',
          requireAuth: false
        }
      },
      {
        path: 'favorites',
        name: 'Favorites',
        component: () => import('../views/Favorites.vue'),
        meta: {
          title: '我的收藏',
          requireAuth: false
        }
      },
      {
        path: 'notification-settings',
        name: 'NotificationSettings',
        component: () => import('../views/NotificationSettings.vue'),
        meta: {
          title: '消息设置',
          requireAuth: false
        }
      },
      {
        path: 'help',
        name: 'Help',
        component: () => import('../views/Help.vue'),
        meta: {
          title: '帮助与反馈',
          requireAuth: false
        }
      },
      {
        path: 'about',
        name: 'About',
        component: () => import('../views/About.vue'),
        meta: {
          title: '关于我们',
          requireAuth: false
        }
      },
      {
        path: 'subsidy/my',
        name: 'MySubsidy',
        component: () => import('../views/Subsidy.vue'),
        meta: {
          title: '我的补贴',
          requireAuth: false
        }
      },
      {
        path: 'data-screen',
        name: 'DataScreen',
        component: () => import('../views/DataScreen.vue'),
        meta: {
          title: '数据概览',
          requireAuth: false
        }
      },
      {
        path: 'search-result',
        name: 'SearchResult',
        component: () => import('../views/SearchResult.vue'),
        meta: {
          title: '搜索结果',
          requireAuth: false
        }
      },
      {
        path: 'party-member',
        name: 'PartyMember',
        component: () => import('../views/PartyMember.vue'),
        meta: {
          title: '党员管理',
          requireAuth: false
        }
      },
      {
        path: 'party-activity',
        name: 'PartyActivity',
        component: () => import('../views/PartyActivity.vue'),
        meta: {
          title: '党组织生活',
          requireAuth: false
        }
      },
      {
        path: 'party-building',
        name: 'PartyBuilding',
        component: () => import('../views/PartyBuilding.vue'),
        meta: {
          title: '党建工作',
          requireAuth: false
        }
      },
      {
        path: 'industry',
        name: 'Industry',
        component: () => import('../views/Industry.vue'),
        meta: {
          title: '产业发展',
          requireAuth: false
        }
      },
      {
        path: 'environment',
        name: 'Environment',
        component: () => import('../views/Environment.vue'),
        meta: {
          title: '环境整治',
          requireAuth: false
        }
      },
      {
        path: 'traffic-management',
        name: 'TrafficManagement',
        component: () => import('../views/TrafficManagement.vue'),
        meta: {
          title: '交通治理',
          requireAuth: false
        }
      },
      {
        path: 'rural-revitalization',
        name: 'RuralRevitalization',
        component: () => import('../views/RuralRevitalization.vue'),
        meta: {
          title: '乡村振兴',
          requireAuth: false
        }
      },
      {
        path: 'assets',
        name: 'Assets',
        component: () => import('../views/Assets.vue'),
        meta: {
          title: '三资管理',
          requireAuth: false
        }
      },
      {
        path: 'governance',
        name: 'Governance',
        component: () => import('../views/Governance.vue'),
        meta: {
          title: '村务治理',
          requireAuth: false
        }
      },
      {
        path: 'civil-affairs',
        name: 'CivilAffairs',
        component: () => import('../views/CivilAffairs.vue'),
        meta: {
          title: '民政事务',
          requireAuth: false
        }
      },
      {
        path: 'special-group',
        name: 'SpecialGroup',
        component: () => import('../views/SpecialGroup.vue'),
        meta: {
          title: '特殊群体',
          requireAuth: false
        }
      },
      {
        path: 'population',
        name: 'Population',
        component: () => import('../views/Population.vue'),
        meta: {
          title: '人口信息',
          requireAuth: false
        }
      },
      {
        path: 'fire-safety',
        name: 'FireSafety',
        component: () => import('../views/FireSafety.vue'),
        meta: {
          title: '消防安全',
          requireAuth: false
        }
      },
      {
        path: 'food-safety',
        name: 'FoodSafety',
        component: () => import('../views/FoodSafety.vue'),
        meta: {
          title: '食品安全',
          requireAuth: false
        }
      },
      {
        path: 'forest-fire',
        name: 'ForestFire',
        component: () => import('../views/ForestFire.vue'),
        meta: {
          title: '森林防火',
          requireAuth: false
        }
      },
      {
        path: 'road-traffic',
        name: 'RoadTraffic',
        component: () => import('../views/RoadTraffic.vue'),
        meta: {
          title: '道路交通',
          requireAuth: false
        }
      },
      {
        path: 'policy',
        name: 'PolicyQuery',
        component: () => import('../views/PolicyQuery.vue'),
        meta: {
          title: '政策查询',
          requireAuth: false
        }
      },
      {
        path: 'village-meeting',
        name: 'VillageMeeting',
        component: () => import('../views/VillageMeeting.vue'),
        meta: {
          title: '村会议事',
          requireAuth: false
        }
      },
      {
        path: 'project-engineering',
        name: 'ProjectEngineering',
        component: () => import('../views/ProjectEngineering.vue'),
        meta: {
          title: '工程项目',
          requireAuth: false
        }
      },
      {
        path: 'land-management',
        name: 'LandManagement',
        component: () => import('../views/LandManagement.vue'),
        meta: {
          title: '土地管理',
          requireAuth: false
        }
      },
      {
        path: 'media-promotion',
        name: 'MediaPromotion',
        component: () => import('../views/MediaPromotion.vue'),
        meta: {
          title: '媒体宣传',
          requireAuth: false
        }
      },
      {
        path: 'document-library',
        name: 'DocumentLibrary',
        component: () => import('../views/DocumentLibrary.vue'),
        meta: {
          title: '文档库',
          requireAuth: false
        }
      },
      {
        path: 'task-supervision',
        name: 'TaskSupervision',
        component: () => import('../views/TaskSupervision.vue'),
        meta: {
          title: '任务督办',
          requireAuth: false
        }
      },
      {
        path: 'material-warehouse',
        name: 'MaterialWarehouse',
        component: () => import('../views/MaterialWarehouse.vue'),
        meta: {
          title: '物资仓库',
          requireAuth: false
        }
      },
      {
        path: 'monitoring-households',
        name: 'MonitoringHouseholds',
        component: () => import('../views/MonitoringHouseholds.vue'),
        meta: {
          title: '监测户信息',
          requireAuth: false
        }
      },
      {
        path: 'poverty-households',
        name: 'PovertyHouseholds',
        component: () => import('../views/PovertyHouseholds.vue'),
        meta: {
          title: '脱贫户信息',
          requireAuth: false
        }
      },
      {
        path: 'rain-plan',
        name: 'RainPlan',
        component: () => import('../views/RainPlan.vue'),
        meta: {
          title: '雨露计划',
          requireAuth: false
        }
      },
      {
        path: 'labor-info',
        name: 'LaborInfo',
        component: () => import('../views/LaborInfo.vue'),
        meta: {
          title: '务工信息',
          requireAuth: false
        }
      },
      {
        path: 'village-assets',
        name: 'VillageAssets',
        component: () => import('../views/VillageAssets.vue'),
        meta: {
          title: '村级资产',
          requireAuth: false
        }
      },
      {
        path: 'village-resources',
        name: 'VillageResources',
        component: () => import('../views/VillageResources.vue'),
        meta: {
          title: '村级资源',
          requireAuth: false
        }
      },
      {
        path: 'village-funds',
        name: 'VillageFunds',
        component: () => import('../views/VillageFunds.vue'),
        meta: {
          title: '村级资金',
          requireAuth: false
        }
      },
      {
        path: 'village-contracts',
        name: 'VillageContracts',
        component: () => import('../views/VillageContracts.vue'),
        meta: {
          title: '村级合同',
          requireAuth: false
        }
      },
      {
        path: 'collective-economy',
        name: 'CollectiveEconomy',
        component: () => import('../views/CollectiveEconomy.vue'),
        meta: {
          title: '集体经济',
          requireAuth: false
        }
      },
      {
        path: 'subsistence-allowance',
        name: 'SubsistenceAllowance',
        component: () => import('../views/SubsistenceAllowance.vue'),
        meta: {
          title: '低保',
          requireAuth: false
        }
      },
      {
        path: 'disabled',
        name: 'Disabled',
        component: () => import('../views/Disabled.vue'),
        meta: {
          title: '残疾人',
          requireAuth: false
        }
      },
      {
        path: 'special-support',
        name: 'SpecialSupport',
        component: () => import('../views/SpecialSupport.vue'),
        meta: {
          title: '特困供养',
          requireAuth: false
        }
      },
      {
        path: 'social-aid',
        name: 'SocialAid',
        component: () => import('../views/SocialAid.vue'),
        meta: {
          title: '社会救助',
          requireAuth: false
        }
      },
      {
        path: 'social-security',
        name: 'SocialSecurity',
        component: () => import('../views/SocialSecurity.vue'),
        meta: {
          title: '社保',
          requireAuth: false
        }
      },
      {
        path: 'left-behind-elderly',
        name: 'LeftBehindElderly',
        component: () => import('../views/LeftBehindElderly.vue'),
        meta: {
          title: '留守老人',
          requireAuth: false
        }
      },
      {
        path: 'left-behind-children',
        name: 'LeftBehindChildren',
        component: () => import('../views/LeftBehindChildren.vue'),
        meta: {
          title: '留守儿童',
          requireAuth: false
        }
      },
      {
        path: 'drowning-prevention',
        name: 'DrowningPrevention',
        component: () => import('../views/DrowningPrevention.vue'),
        meta: {
          title: '防溺水',
          requireAuth: false
        }
      },
      {
        path: 'drowning-education',
        name: 'DrowningEducation',
        component: () => import('../views/DrowningEducation.vue'),
        meta: {
          title: '防溺水教育',
          requireAuth: false
        }
      },
      {
        path: 'drowning-promotion',
        name: 'DrowningPromotion',
        component: () => import('../views/DrowningPromotion.vue'),
        meta: {
          title: '防溺水宣传',
          requireAuth: false
        }
      },
      {
        path: 'key-personnel',
        name: 'KeyPersonnel',
        component: () => import('../views/KeyPersonnel.vue'),
        meta: {
          title: '重点人员',
          requireAuth: false
        }
      },
      {
        path: 'geological-disaster',
        name: 'GeologicalDisaster',
        component: () => import('../views/GeologicalDisaster.vue'),
        meta: {
          title: '地灾点',
          requireAuth: false
        }
      },
      {
        path: 'slope-housing-risk',
        name: 'SlopeHousingRisk',
        component: () => import('../views/SlopeHousingRisk.vue'),
        meta: {
          title: '切坡建房风险户',
          requireAuth: false
        }
      },
      {
        path: 'contact-list',
        name: 'ContactList',
        component: () => import('../views/ContactList.vue'),
        meta: {
          title: '通讯录',
          requireAuth: false
        }
      },
      {
        path: 'leader-list',
        name: 'LeaderList',
        component: () => import('../views/LeaderList.vue'),
        meta: {
          title: '乡镇主要事务负责人',
          requireAuth: false
        }
      },
      {
        path: 'leader-detail/:id',
        name: 'LeaderDetail',
        component: () => import('../views/LeaderDetail.vue'),
        meta: {
          title: '分管工作',
          requireAuth: false
        }
      },
      {
        path: 'leader-work-detail/:leaderId/:workIndex',
        name: 'LeaderWorkDetail',
        component: () => import('../views/LeaderWorkDetail.vue'),
        meta: {
          title: '工作详情',
          requireAuth: false
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    }
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - 村级智慧村务平台`
  }
  next()
})

export default router