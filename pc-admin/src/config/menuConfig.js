export const menuConfig = [
  {
    id: 1,
    title: '党建工作',
    icon: 'flag',
    defaultOpen: true,
    children: [
      { id: 11, name: '党员管理', route: '/party/members' },
      { id: 12, name: '党组织生活', route: '/party/activities' },
      { id: 13, name: '发展党员', route: '/party/develop' },
      { id: 14, name: '党务公开', route: '/party/public' },
      { id: 15, name: '党建考核', route: '/party/assessment' },
      { id: 16, name: '志愿服务', route: '/party/volunteer' }
    ]
  },
  {
    id: 2,
    title: '村务政务',
    icon: 'document',
    defaultOpen: false,
    children: [
      { id: 21, name: '会议纪要', route: '/government/meetings' },
      { id: 22, name: '文件收发', route: '/government/documents' },
      { id: 23, name: '任务督办', route: '/public/tasks' },
      { id: 24, name: '村务公开', route: '/public-notice' },
      { id: 25, name: '通知公告', route: '/notification' },
      { id: 26, name: '操作日志', route: '/public/logs' }
    ]
  },
  {
    id: 3,
    title: '三资管理',
    icon: 'money',
    defaultOpen: false,
    children: [
      { id: 31, name: '资金管理', route: '/assets/funds' },
      { id: 32, name: '资产管理', route: '/assets' },
      { id: 33, name: '资源管理', route: '/assets/resources' },
      { id: 34, name: '工程项目', route: '/project/list' }
    ]
  },
  {
    id: 4,
    title: '乡村建设',
    icon: 'home',
    defaultOpen: false,
    children: [
      { id: 41, name: '厕所改革', route: '/rural/toilets' },
      { id: 42, name: '人居环境', route: '/environment/improvement' },
      { id: 43, name: '宅基地管理', route: '/land/house' }
    ]
  },
  {
    id: 5,
    title: '安全治理',
    icon: 'shield',
    defaultOpen: false,
    children: [
      { id: 51, name: '安全隐患', route: '/safety/hazards' },
      { id: 52, name: '防汛管理', route: '/safety/flood' },
      { id: 53, name: '消防安全', route: '/safety/fire' },
      { id: 54, name: '食品安全', route: '/safety/food' },
      { id: 55, name: '森林防火', route: '/rural/forest' },
      { id: 56, name: '道路交通', route: '/rural/road' },
      { id: 57, name: '矛盾纠纷', route: '/governance/disputes' },
      { id: 58, name: '信访调解', route: '/governance/petition' }
    ]
  },
  {
    id: 6,
    title: '民生服务',
    icon: 'users',
    defaultOpen: false,
    children: [
      { id: 61, name: '便民服务', route: '/convenience/services' },
      { id: 62, name: '特殊群体', route: '/convenience/special-groups' },
      { id: 63, name: '村民诉求', route: '/demand' },
      { id: 64, name: '人口户籍', route: '/population/list' },
      { id: 65, name: '补贴发放', route: '/subsidy/list' },
      { id: 66, name: '养老医保', route: '/pension/insurance' },
      { id: 67, name: '志愿者管理', route: '/volunteer/list' }
    ]
  },
  {
    id: 7,
    title: '宣传档案',
    icon: 'chat',
    defaultOpen: false,
    children: [
      { id: 71, name: '新闻稿件', route: '/media/list' },
      { id: 72, name: '文档库', route: '/public/documents' },
      { id: 73, name: '档案管理', route: '/archive/list' },
      { id: 74, name: '物资仓库', route: '/warehouse/list' }
    ]
  },
  {
    id: 8,
    title: '系统设置',
    icon: 'setting',
    defaultOpen: false,
    children: [
      { id: 81, name: '用户管理', route: '/users' },
      { id: 82, name: '系统权限', route: '/system/permission' },
      { id: 83, name: '村情配置', route: '/village-config' },
      { id: 84, name: '信息变更审核', route: '/audit/list' }
    ]
  }
]
