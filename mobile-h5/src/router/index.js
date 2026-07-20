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