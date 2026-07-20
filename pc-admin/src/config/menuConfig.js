export const menuConfig = [
  {
    id: 1,
    title: '党建工作',
    icon: 'flag',
    defaultOpen: false,
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
    title: '政务工作',
    icon: 'document',
    defaultOpen: false,
    children: [
      { id: 21, name: '会议纪要', route: '/government/meetings' },
      { id: 22, name: '文件收发', route: '/government/documents' },
      { id: 23, name: '任务督办', route: '/public/tasks' },
      { id: 24, name: '操作日志', route: '/public/logs' }
    ]
  },
  {
    id: 3,
    title: '便民服务',
    icon: 'service',
    defaultOpen: false,
    children: [
      { id: 31, name: '便民服务', route: '/convenience/services' },
      { id: 32, name: '特殊群体', route: '/convenience/special-groups' },
      { id: 33, name: '村民诉求', route: '/demand' },
      { id: 34, name: '通知公告', route: '/notification' }
    ]
  },
  {
    id: 4,
    title: '三资管理',
    icon: 'money',
    defaultOpen: false,
    children: [
      { id: 41, name: '资金管理', route: '/assets/funds' },
      { id: 42, name: '资产管理', route: '/assets' },
      { id: 43, name: '资源管理', route: '/assets/resources' }
    ]
  },
  {
    id: 5,
    title: '安全防护',
    icon: 'warning',
    defaultOpen: false,
    children: [
      { id: 51, name: '安全隐患', route: '/safety/hazards' },
      { id: 52, name: '防汛管理', route: '/safety/flood' },
      { id: 53, name: '消防安全', route: '/safety/fire' },
      { id: 54, name: '食品安全', route: '/safety/food' },
      { id: 55, name: '森林防火', route: '/rural/forest' },
      { id: 56, name: '道路交通', route: '/rural/road' }
    ]
  },
  {
    id: 6,
    title: '乡村振兴',
    icon: 'wheat',
    defaultOpen: false,
    children: [
      { id: 61, name: '厕所改革', route: '/rural/toilets' },
      { id: 62, name: '人居环境', route: '/environment/improvement' }
    ]
  },
  {
    id: 7,
    title: '综合治理',
    icon: 'shield',
    defaultOpen: false,
    children: [
      { id: 71, name: '矛盾纠纷', route: '/governance/disputes' },
      { id: 72, name: '信访调解', route: '/governance/petition' }
    ]
  },
  {
    id: 8,
    title: '人口户籍',
    icon: 'people',
    defaultOpen: false,
    children: [
      { id: 81, name: '户籍台账', route: '/population/list' }
    ]
  },
  {
    id: 9,
    title: '土地管理',
    icon: 'location',
    defaultOpen: false,
    children: [
      { id: 91, name: '宅基地管理', route: '/land/house' }
    ]
  },
  {
    id: 10,
    title: '惠民补贴',
    icon: 'gift',
    defaultOpen: false,
    children: [
      { id: 101, name: '补贴发放', route: '/subsidy/list' }
    ]
  },
  {
    id: 11,
    title: '民政社保',
    icon: 'heart',
    defaultOpen: false,
    children: [
      { id: 111, name: '养老医保', route: '/pension/insurance' }
    ]
  },
  {
    id: 12,
    title: '项目工程',
    icon: 'building',
    defaultOpen: false,
    children: [
      { id: 121, name: '工程项目', route: '/project/list' }
    ]
  },
  {
    id: 13,
    title: '志愿服务',
    icon: 'hand',
    defaultOpen: false,
    children: [
      { id: 131, name: '志愿者管理', route: '/volunteer/list' }
    ]
  },
  {
    id: 14,
    title: '媒体宣传',
    icon: 'chat',
    defaultOpen: false,
    children: [
      { id: 141, name: '新闻稿件', route: '/media/list' }
    ]
  },
  {
    id: 15,
    title: '档案资料',
    icon: 'folder',
    defaultOpen: false,
    children: [
      { id: 151, name: '档案管理', route: '/archive/list' }
    ]
  },
  {
    id: 16,
    title: '物资仓库',
    icon: 'box',
    defaultOpen: false,
    children: [
      { id: 161, name: '物资台账', route: '/warehouse/list' }
    ]
  },
  {
    id: 17,
    title: '系统设置',
    icon: 'setting',
    defaultOpen: false,
    children: [
      { id: 171, name: '用户管理', route: '/users' },
      { id: 172, name: '系统权限', route: '/system/permission' },
      { id: 173, name: '村情配置', route: '/village-config' },
      { id: 174, name: '信息变更审核', route: '/audit/list' }
    ]
  }
]
