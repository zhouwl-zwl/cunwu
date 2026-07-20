export const menuConfig = [
  {
    id: 1,
    title: '党建工作',
    icon: 'flag',
    defaultOpen: true,
    children: [
      { id: 11, name: '党员管理', route: '/party-member' },
      { id: 12, name: '党组织生活', route: '/party-activity' },
      { id: 13, name: '发展党员', route: '/party/develop' },
      { id: 14, name: '党务公开', route: '/party/public' },
      { id: 15, name: '党建考核', route: '/party/assessment' },
      { id: 16, name: '志愿服务', route: '/party/volunteer' }
    ]
  },
  {
    id: 2,
    title: '村务管理',
    icon: 'home',
    defaultOpen: false,
    children: [
      { id: 21, name: '村情概况', route: '/village-info' },
      { id: 22, name: '组织架构', route: '/org-members' },
      { id: 23, name: '村务公开', route: '/public-notices' },
      { id: 24, name: '三资管理', route: '/assets' }
    ]
  },
  {
    id: 3,
    title: '乡村振兴',
    icon: 'wheat',
    defaultOpen: false,
    children: [
      { id: 31, name: '产业发展', route: '/industry' },
      { id: 32, name: '环境整治', route: '/environment' },
      { id: 33, name: '厕所革命', route: '/toilet-reform' }
    ]
  },
  {
    id: 4,
    title: '村民服务',
    icon: 'users',
    defaultOpen: false,
    children: [
      { id: 41, name: '便民服务', route: '/convenience' },
      { id: 42, name: '政策查询', route: '/policy' }
    ]
  },
  {
    id: 5,
    title: '综合治理',
    icon: 'shield',
    defaultOpen: false,
    children: [
      { id: 51, name: '矛盾纠纷', route: '/dispute' },
      { id: 52, name: '安全隐患', route: '/safety-hazard' },
      { id: 53, name: '消防安全', route: '/fire-safety' }
    ]
  }
]

export const getMenuByRoute = (route) => {
  for (const parent of menuConfig) {
    for (const child of parent.children) {
      if (route.startsWith(child.route)) {
        return { parent, child }
      }
    }
  }
  return null
}
