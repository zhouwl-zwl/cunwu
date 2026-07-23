export const menuConfig = [
  {
    id: 1,
    title: '党建工作',
    icon: 'flag',
    defaultOpen: true,
    children: [
      { id: 11, name: '党员管理', route: '/party-member' },
      { id: 12, name: '组织生活', route: '/party-activity' },
      { id: 13, name: '党建考核', route: '/party-building' },
      { id: 14, name: '党务公开', route: '/public-notices?type=党务公开' },
      { id: 15, name: '发展党员', route: '/party/develop' },
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
      { id: 23, name: '村务公开', route: '/public-notices?type=村务公开' },
      { id: 24, name: '村会议事', route: '/governance' },
      { id: 25, name: '任务督办', route: '/task-supervision' },
      { id: 26, name: '土地管理', route: '/land-management' }
    ]
  },
  {
    id: 3,
    title: '三资管理',
    icon: 'wallet',
    defaultOpen: false,
    children: [
      { id: 31, name: '资产登记', route: '/assets' },
      { id: 32, name: '资源管理', route: '/assets' },
      { id: 33, name: '资金公开', route: '/public-notices?type=财务公开' },
      { id: 34, name: '工程项目', route: '/project-engineering' }
    ]
  },
  {
    id: 4,
    title: '乡村振兴',
    icon: 'wheat',
    defaultOpen: false,
    children: [
      { id: 41, name: '产业发展', route: '/industry' },
      { id: 42, name: '环境整治', route: '/environment' },
      { id: 43, name: '交通治理', route: '/traffic-management' },
      { id: 44, name: '振兴项目', route: '/rural-revitalization' }
    ]
  },
  {
    id: 5,
    title: '民政事务',
    icon: 'users',
    defaultOpen: false,
    children: [
      { id: 51, name: '民政服务', route: '/civil-affairs' },
      { id: 52, name: '特殊群体', route: '/special-group' },
      { id: 53, name: '人口信息', route: '/population' },
      { id: 54, name: '惠民补贴', route: '/subsidy' }
    ]
  },
  {
    id: 6,
    title: '安全管理',
    icon: 'shield',
    defaultOpen: false,
    children: [
      { id: 61, name: '消防安全', route: '/fire-safety' },
      { id: 62, name: '森林防火', route: '/forest-fire' },
      { id: 63, name: '食品安全', route: '/food-safety' },
      { id: 64, name: '道路交通', route: '/road-traffic' },
      { id: 65, name: '灾害防范', route: '/safety-hazard' }
    ]
  },
  {
    id: 7,
    title: '便民服务',
    icon: 'service',
    defaultOpen: false,
    children: [
      { id: 71, name: '矛盾纠纷', route: '/dispute' },
      { id: 72, name: '政策查询', route: '/policy' },
      { id: 73, name: '媒体宣传', route: '/media-promotion' },
      { id: 74, name: '文档库', route: '/document-library' },
      { id: 75, name: '物资仓库', route: '/material-warehouse' },
      { id: 76, name: '通知公告', route: '/notifications' }
    ]
  }
]

export const getMenuByRoute = (route) => {
  for (const parent of menuConfig) {
    for (const child of parent.children) {
      if (route.startsWith(child.route.split('?')[0])) {
        return { parent, child }
      }
    }
  }
  return null
}