<template>
  <div class="page-container">
    <van-nav-bar 
      :title="workDetail?.workName || '工作详情'" 
      :class="{ 'nav-bar-rural': workDetail?.workName === '乡村振兴' }"
      left-arrow 
      @click-left="goBack" 
    />
    
    <div 
      class="detail-header" 
      :class="{ 'detail-header-rural': workDetail?.workName === '乡村振兴' }" 
      v-if="workDetail"
    >
      <div class="header-title">
        <span v-if="workDetail.workName === '乡村振兴'" class="header-rural-icon">🌾</span>
        {{ workDetail.workName }}
      </div>
      <div class="header-subtitle">
        {{ workDetail.leaderName }}
      </div>
    </div>

    <div class="detail-content" v-if="workDetail">
      <div class="section-card" v-if="workDetail.children">
        <div class="section-title" :class="{ 'section-title-rural': workDetail.workName === '乡村振兴' }">
          <van-icon name="file-text-o" size="16" :color="workDetail.workName === '乡村振兴' ? '#2E7D32' : '#D22630'" />
          <span>分管事务</span>
        </div>
        
        <div class="children-list">
          <div 
            v-for="(child, idx) in workDetail.children" 
            :key="idx" 
            class="child-card"
            :class="{ 'child-card-rural': workDetail.workName === '乡村振兴' }"
            @click="toggleChild(idx)"
          >
            <div class="child-header">
              <div class="child-left">
                <div 
                  class="child-number" 
                  :class="{ 'child-number-rural': workDetail.workName === '乡村振兴', 'child-number-icon': child.icon }"
                >
                  <span v-if="child.icon" class="child-icon">{{ child.icon }}</span>
                  <span v-else>{{ idx + 1 }}</span>
                </div>
                <div class="child-name">
                  {{ child.name }}
                  <span v-if="child.responsible" class="child-responsible" :class="{ 'child-responsible-rural': workDetail.workName === '乡村振兴' }">{{ child.responsible }}</span>
                </div>
              </div>
              <div class="child-arrow" :class="{ expanded: expandedIndex === idx }">
                <van-icon name="arrow" size="16" :color="workDetail.workName === '乡村振兴' ? '#2E7D32' : '#D22630'" />
              </div>
            </div>
            
            <div class="child-detail" v-show="expandedIndex === idx">
              <div class="detail-section">
                <div class="detail-label">相关资料台账</div>
                <div class="doc-tags">
                  <span 
                    v-for="(doc, dIdx) in child.documents" 
                    :key="dIdx" 
                    class="doc-tag"
                    :class="{ clickable: isRosterDoc(doc), 'doc-tag-rural': workDetail.workName === '乡村振兴' }"
                    @click.stop="handleDocClick(doc)"
                  >
                    {{ doc }}
                    <van-icon v-if="isRosterDoc(doc)" name="arrow" size="10" />
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="roster-entry" v-if="hasRoster">
          <div class="roster-entry-card" @click="goRoster">
            <div class="roster-entry-icon">
              <van-icon name="friends-o" size="24" color="#fff" />
            </div>
            <div class="roster-entry-content">
              <div class="roster-entry-title">纪检干部花名册</div>
              <div class="roster-entry-desc">查看全部30位纪检干部信息</div>
            </div>
            <van-icon name="arrow" size="18" color="#D22630" />
          </div>
        </div>
      </div>

      <div class="section-card" v-if="workDetail.workItems">
        <div class="section-title" :class="{ 'section-title-rural': workDetail.workName === '乡村振兴' }">
          <van-icon name="description" size="16" :color="workDetail.workName === '乡村振兴' ? '#2E7D32' : '#D22630'" />
          <span>工作内容</span>
        </div>
        <div class="work-items">
          <div 
            v-for="(item, idx) in workDetail.workItems" 
            :key="idx" 
            class="work-item"
            :class="{ 'work-item-rural': workDetail.workName === '乡村振兴' }"
          >
            <div class="item-number" :class="{ 'item-number-rural': workDetail.workName === '乡村振兴', 'item-number-icon': typeof item === 'object' && item.icon }">
              <span v-if="typeof item === 'object' && item.icon" class="item-icon">{{ item.icon }}</span>
              <span v-else>{{ idx + 1 }}</span>
            </div>
            <div class="item-content">{{ typeof item === 'object' ? item.text : item }}</div>
          </div>
        </div>
      </div>

      <div class="section-card" v-if="workDetail.documents && workDetail.documents.length > 0">
        <div class="section-title" :class="{ 'section-title-rural': workDetail.workName === '乡村振兴' }">
          <van-icon name="folder-o" size="16" :color="workDetail.workName === '乡村振兴' ? '#2E7D32' : '#4CAF50'" />
          <span>相关资料台账</span>
        </div>
        <div class="document-list">
          <div 
            v-for="(doc, idx) in workDetail.documents" 
            :key="idx" 
            class="document-item"
            :class="{ 'document-item-rural': workDetail.workName === '乡村振兴' }"
          >
            <div class="doc-icon" :class="{ 'doc-icon-emoji': doc.icon }">
              <span v-if="doc.icon" class="doc-emoji">{{ doc.icon }}</span>
              <van-icon v-else name="file" size="20" :color="workDetail.workName === '乡村振兴' ? '#2E7D32' : '#666'" />
            </div>
            <div class="doc-content">
              <div class="doc-title">{{ doc.title }}</div>
              <div class="doc-desc">{{ doc.description }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-card" v-if="!workDetail.children && !workDetail.workItems">
        <van-empty description="暂无相关资料台账" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'

const router = useRouter()
const route = useRoute()

const leaderId = ref(parseInt(route.params.leaderId) || 1)
const workIndex = ref(parseInt(route.params.workIndex) || 0)
const expandedIndex = ref(-1)

const ROSTER_DOC = '纪检干部花名册'

const workDetails = ref([
  {
    leaderId: 1,
    workIndex: 0,
    leaderName: '欧阳付群',
    workName: '政协联络',
    workItems: [
      { icon: '📋', text: '3项标注为XXXX的工作事项' }
    ],
    documents: [
      { icon: '📄', title: '政协联络资料台账1', description: '对应第1组工作事项的资料台账' },
      { icon: '📄', title: '政协联络资料台账2', description: '对应第2组工作事项的资料台账' },
      { icon: '📄', title: '政协联络资料台账3', description: '对应第3组工作事项的资料台账' }
    ]
  },
  {
    leaderId: 1,
    workIndex: 4,
    leaderName: '欧阳付群',
    workName: '乡村振兴',
    children: [
      { 
        name: '监测帮扶',
        icon: '📊',
        responsible: '陈勇',
        documents: [
          '脱贫户识别台账',
          '监测对象认定表',
          '家庭收入核查表',
          '脱贫户基本信息表',
          '监测对象动态管理台账',
          '信息更新记录'
        ]
      },
      { 
        name: '平台预警',
        icon: '🔔',
        responsible: '陈勇',
        documents: [
          '预警信息台账',
          '预警处置记录表',
          '预警整改追踪表'
        ]
      },
      { 
        name: '易地搬迁', 
        icon: '🏠',
        responsible: '杨军',
        documents: ['易地扶贫搬迁人员花名册'] 
      },
      { 
        name: '厕所革命', 
        icon: '🚽',
        responsible: '杨军',
        documents: ['改厕台账', '问题厕所整改台账'] 
      },
      { 
        name: '雨露计划', 
        icon: '💧',
        responsible: '杨军',
        documents: ['雨露计划台账'] 
      },
      { 
        name: '小额信贷', 
        icon: '💰',
        responsible: '杨军',
        documents: ['小额信贷贴息台账'] 
      },
      { 
        name: '乡村振兴项目', 
        icon: '🏗️',
        responsible: '杨军',
        documents: ['乡村振兴项目资产台账', '"四个一批"分类施策台账'] 
      },
      { 
        name: '产业帮扶', 
        icon: '🌱',
        responsible: '杨军',
        documents: ['2025年产业奖补台账'] 
      }
    ],
    workItems: [
      { icon: '📊', text: '监测帮扶：灾、突发事件、经营亏损等导致家庭收入严重下降生活困难的农户，并纳入监测对象；更新维护脱贫户与监测对象基本信息' },
      { icon: '🔔', text: '平台预警：实时监测预警信息，及时发现并处置异常情况，跟踪整改落实' },
      { icon: '🏠', text: '易地搬迁：易地扶贫搬迁人员花名册' },
      { icon: '🚽', text: '厕所革命：改厕台账、问题厕所整改台账' },
      { icon: '💧', text: '雨露计划：雨露计划台账' },
      { icon: '💰', text: '小额信贷：小额信贷贴息台账' },
      { icon: '🏗️', text: '乡村振兴项目：乡村振兴项目资产台账、"四个一批"分类施策台账' },
      { icon: '🌱', text: '产业帮扶：2025年产业奖补台账' }
    ],
    documents: [
      { icon: '📊', title: '监测帮扶资料', description: '脱贫户识别、监测对象认定、信息维护等资料' },
      { icon: '🔔', title: '平台预警资料', description: '预警信息台账、预警处置记录、预警整改追踪' },
      { icon: '🏠', title: '易地搬迁资料', description: '易地扶贫搬迁人员花名册等资料' },
      { icon: '🚽', title: '厕所革命资料', description: '改厕台账、问题厕所整改台账' },
      { icon: '💧', title: '雨露计划资料', description: '雨露计划台账' },
      { icon: '💰', title: '小额信贷资料', description: '小额信贷贴息台账' },
      { icon: '🏗️', title: '乡村振兴项目资料', description: '项目资产台账、"四个一批"分类施策台账' },
      { icon: '🌱', title: '产业帮扶资料', description: '2025年产业奖补台账' }
    ]
  },
  {
    leaderId: 1,
    workIndex: 6,
    leaderName: '欧阳付群',
    workName: '经管（三资）',
    children: [
      { 
        name: '清查整治',
        icon: '📋',
        responsible: '黄楚珂',
        documents: [
          '2026年罗卜田乡农村集体"三资"管理突出问题专项整治工作资料汇编'
        ]
      },
      { 
        name: '资金合同',
        icon: '📄',
        responsible: '黄楚珂',
        documents: [
          '各村集体资金承包合同'
        ]
      }
    ],
    workItems: [
      { icon: '📋', text: '清查整治：开展农村集体"三资"管理突出问题专项整治，整理汇编相关工作资料' },
      { icon: '📄', text: '资金合同：管理各村集体资金承包合同，确保合同规范合规' }
    ],
    documents: [
      { icon: '📋', title: '清查整治资料汇编', description: '2026年罗卜田乡农村集体"三资"管理突出问题专项整治工作资料汇编' },
      { icon: '📄', title: '资金承包合同', description: '各村集体资金承包合同' }
    ]
  },
  {
    leaderId: 1,
    workIndex: 7,
    leaderName: '欧阳付群',
    workName: '农村人居环境治理',
    children: [
      { 
        name: '环境整治宣传',
        icon: '📢',
        responsible: '涂金萍',
        documents: [
          '门前三包表',
          '一单四制表'
        ]
      },
      { 
        name: '垃圾清运督查',
        icon: '🗑️',
        responsible: '涂金萍',
        documents: [
          '农村垃圾生活收设施摸底排查表'
        ]
      },
      { 
        name: '固废排查',
        icon: '🔍',
        responsible: '涂金萍',
        documents: [
          '固体废物规范点位清单'
        ]
      }
    ],
    workItems: [
      { icon: '📢', text: '环境整治宣传：宣传农村人居环境整治政策，确保房前屋后整洁，发放门前三包表，做好一单四制表' },
      { icon: '🗑️', text: '垃圾清运督查：督查各村做好垃圾清扫和转运，填写农村垃圾生活收设施摸底排查表' },
      { icon: '🔍', text: '固废排查：做好固体废物排查工作，填写固体废物规范点位清单' }
    ],
    documents: [
      { icon: '📢', title: '环境整治宣传资料', description: '农村人居环境整治政策宣传资料、门前三包表、一单四制表' },
      { icon: '🗑️', title: '垃圾清运督查资料', description: '农村垃圾清扫转运督查资料、垃圾生活收设施摸底排查表' },
      { icon: '🔍', title: '固废排查资料', description: '固体废物排查资料、固体废物规范点位清单' }
    ]
  },
  {
    leaderId: 1,
    workIndex: 8,
    leaderName: '欧阳付群',
    workName: '生态环保',
    children: [
      { 
        name: '生态问题排查',
        icon: '🌱',
        responsible: '涂金萍',
        documents: [
          '农村生活污水处理设施排查表',
          '农村黑臭水体排查表',
          '秸秆焚烧排查台账'
        ]
      }
    ],
    workItems: [
      { icon: '🌱', text: '生态问题排查：排查农村生活污水处理设施、农村黑臭水体、秸秆焚烧等情况' }
    ],
    documents: [
      { icon: '🌱', title: '生态环保排查资料', description: '农村生活污水处理设施排查、黑臭水体排查、秸秆焚烧排查台账' }
    ]
  },
  {
    leaderId: 3,
    workIndex: 0,
    leaderName: '龙君屹',
    workName: '武装',
    children: [
      { 
        name: '乡基干民兵花名册',
        icon: '🔒',
        responsible: '涂金萍',
        documents: [
          '乡基干民兵花名册（保密）'
        ]
      },
      { 
        name: '村民兵花名册',
        icon: '🔒',
        responsible: '涂金萍',
        documents: [
          '村民兵花名册（保密）'
        ]
      },
      { 
        name: '潜力调查表',
        icon: '🔒',
        responsible: '涂金萍',
        documents: [
          '潜力调查表（保密）'
        ]
      }
    ],
    workItems: [
      { icon: '🔒', text: '乡基干民兵花名册（保密，不公开）' },
      { icon: '🔒', text: '村民兵花名册（保密，不公开）' },
      { icon: '🔒', text: '潜力调查表（保密，不公开）' }
    ],
    documents: [
      { icon: '🔒', title: '乡基干民兵花名册', description: '保密内容，不公开' },
      { icon: '🔒', title: '村民兵花名册', description: '保密内容，不公开' },
      { icon: '🔒', title: '潜力调查表', description: '保密内容，不公开' }
    ]
  },
  {
    leaderId: 3,
    workIndex: 1,
    leaderName: '龙君屹',
    workName: '国防动员教育',
    children: [
      { 
        name: '年度计划',
        icon: '🔒',
        responsible: '涂金萍',
        documents: [
          '国防教育年度计划（保密）'
        ]
      },
      { 
        name: '教案',
        icon: '🔒',
        responsible: '涂金萍',
        documents: [
          '国防教育教案（保密）'
        ]
      }
    ],
    workItems: [
      { icon: '🔒', text: '年度计划（保密，不公开）' },
      { icon: '🔒', text: '教案（保密，不公开）' }
    ],
    documents: [
      { icon: '🔒', title: '年度计划', description: '保密内容，不公开' },
      { icon: '🔒', title: '教案', description: '保密内容，不公开' }
    ]
  },
  {
    leaderId: 3,
    workIndex: 2,
    leaderName: '龙君屹',
    workName: '退役军人',
    children: [
      { 
        name: '优抚对象花名册',
        icon: '🪖',
        responsible: '涂金萍',
        documents: [
          '优抚对象花名册'
        ]
      }
    ],
    workItems: [
      { icon: '🪖', text: '优抚对象花名册：登记优抚对象信息，包括各类优抚人员的基本情况和动态管理' }
    ],
    documents: [
      { icon: '🪖', title: '优抚对象花名册', description: '优抚对象信息登记与动态管理台账' }
    ]
  },
  {
    leaderId: 3,
    workIndex: 3,
    leaderName: '龙君屹',
    workName: '农业农村',
    children: [
      { 
        name: '粮食大豆油菜任务表',
        icon: '🌾',
        responsible: '涂金萍',
        documents: [
          '粮食任务分配及完成表',
          '大豆任务分配及完成表',
          '油菜任务分配及完成表'
        ]
      },
      { 
        name: '农业补贴发放表',
        icon: '💰',
        responsible: '涂金萍',
        documents: [
          '地力补贴发放表',
          '农机补贴发放表'
        ]
      },
      { 
        name: '农机统计表',
        icon: '🚜',
        responsible: '涂金萍',
        documents: [
          '农机统计表'
        ]
      }
    ],
    workItems: [
      { icon: '🌾', text: '粮食大豆油菜任务表：粮食、大豆、油菜等农作物生产任务分配及完成情况统计' },
      { icon: '💰', text: '农业补贴发放表：地力补贴、农机补贴等各项农业补贴发放情况登记' },
      { icon: '🚜', text: '农机统计表：农业机械保有量及使用情况统计' }
    ],
    documents: [
      { icon: '🌾', title: '粮食大豆油菜任务表', description: '粮食、大豆、油菜任务分配及完成情况表' },
      { icon: '💰', title: '农业补贴发放表', description: '地力补贴、农机补贴等发放情况表' },
      { icon: '🚜', title: '农机统计表', description: '农业机械保有量及使用情况统计表' }
    ]
  },
  {
    leaderId: 3,
    workIndex: 4,
    leaderName: '龙君屹',
    workName: '畜牧水产',
    children: [
      { 
        name: '禁捕退捕摸排表',
        icon: '📋',
        responsible: '涂金萍',
        documents: [
          '禁捕退捕摸排表',
          '退捕渔民安置保障表'
        ]
      },
      { 
        name: '生猪牛羊任务表',
        icon: '🐖',
        responsible: '涂金萍',
        documents: [
          '生猪任务分配及完成表',
          '牛羊任务分配及完成表',
          '生猪检疫表',
          '牛羊检疫表'
        ]
      },
      { 
        name: '鸡鸭水产任务表',
        icon: '🐔',
        responsible: '涂金萍',
        documents: [
          '鸡鸭任务分配及完成表',
          '水产养殖任务表',
          '鸡鸭检疫表',
          '水产养殖检疫表'
        ]
      }
    ],
    workItems: [
      { icon: '📋', text: '禁捕退捕摸排表：禁捕退捕情况摸排登记，退捕渔民安置保障等相关表格（不含执法）' },
      { icon: '🐖', text: '生猪牛羊任务表：生猪、牛羊养殖任务分配及完成情况，检疫等相关表格' },
      { icon: '🐔', text: '鸡鸭水产任务表：鸡鸭、水产养殖任务分配及完成情况，检疫等相关表格' }
    ],
    documents: [
      { icon: '📋', title: '禁捕退捕摸排表', description: '禁捕退捕摸排、退捕渔民安置等相关表格（不含执法）' },
      { icon: '🐖', title: '生猪牛羊任务表', description: '生猪、牛羊任务分配完成及检疫情况表' },
      { icon: '🐔', title: '鸡鸭水产任务表', description: '鸡鸭、水产任务分配完成及检疫情况表' }
    ]
  },
  {
    leaderId: 5,
    workIndex: 0,
    leaderName: '于鼎馨',
    workName: '纪检工作',
    children: [
      { name: '协助党委推进全面从严治党', icon: '📜', documents: ['党委会议记录'] },
      { name: '政治、日常、专项等监督', icon: '👁️', documents: ['相关监督检查台账'] },
      { name: '线索处置', icon: '🔍', documents: ['线索台账'] },
      { name: '案件查办', icon: '⚖️', documents: ['案卷卷宗'] },
      { name: '廉政教育', icon: '🎓', documents: ['廉政风险排查表', '谈心谈话记录', '警示教育台账'] },
      { name: '队伍建设和监督', icon: '👥', documents: ['纪检干部花名册', '培训会议'] }
    ]
  }
])

const workDetail = computed(() => {
  return workDetails.value.find(
    d => d.leaderId === leaderId.value && d.workIndex === workIndex.value
  )
})

const hasRoster = computed(() => {
  if (!workDetail.value?.children) return false
  return workDetail.value.children.some(child => 
    child.documents && child.documents.includes(ROSTER_DOC)
  )
})

const isRosterDoc = (doc) => doc === ROSTER_DOC

const handleDocClick = (doc) => {
  if (isRosterDoc(doc)) {
    goRoster()
  } else {
    showToast(`${doc} - 暂无详情`)
  }
}

const goRoster = () => {
  router.push('/discipline-roster')
}

const toggleChild = (idx) => {
  expandedIndex.value = expandedIndex.value === idx ? -1 : idx
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  if (sessionStorage.getItem('leader_unlocked') !== 'true') {
    router.replace('/')
    return
  }
  if (workDetail.value) {
    document.title = `${workDetail.value.workName} - 工作详情`
  }
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.detail-header {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  padding: 24px 16px;
  text-align: center;
  color: #fff;
  margin-bottom: 12px;
}

.header-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 6px;
}

.header-subtitle {
  font-size: 14px;
  opacity: 0.9;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
}

.responsible-tag {
  display: inline-block;
  background: rgba(255, 255, 255, 0.25);
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.detail-content {
  padding: 0 12px;
}

.section-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.children-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.child-card {
  background: #f8f9fa;
  border-radius: 10px;
  overflow: hidden;
}

.child-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 14px;
  cursor: pointer;
}

.child-header:active {
  background: #f0f0f0;
}

.child-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.child-number {
  width: 22px;
  height: 22px;
  background: #FF5722;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  flex-shrink: 0;
}

.child-number-icon {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #FFF3E0 0%, #FFE0B2 100%);
  border-radius: 10px;
}

.child-icon {
  font-size: 18px;
  line-height: 1;
}

.child-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 6px;
}

.child-responsible {
  font-size: 11px;
  font-weight: 500;
  color: #fff;
  background: linear-gradient(135deg, #FF5722 0%, #E64A19 100%);
  padding: 2px 8px;
  border-radius: 10px;
}

.child-arrow {
  transition: transform 0.2s;
}

.child-arrow.expanded {
  transform: rotate(90deg);
}

.child-detail {
  padding: 0 14px 14px;
  animation: slideDown 0.2s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.detail-section {
  background: #fff;
  border-radius: 8px;
  padding: 12px;
}

.detail-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.doc-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.doc-tag {
  display: inline-block;
  padding: 4px 10px;
  background: rgba(210, 38, 48, 0.08);
  color: #D22630;
  font-size: 12px;
  border-radius: 12px;
}

.doc-tag.clickable {
  cursor: pointer;
  background: var(--gradient-primary);
  color: #fff;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  box-shadow: 0 2px 6px rgba(210, 38, 48, 0.3);
}

.roster-entry {
  margin-top: 12px;
}

.roster-entry-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #FFF5F5 0%, #FFE8E8 100%);
  border: 1px solid rgba(210, 38, 48, 0.2);
  border-radius: 12px;
  padding: 14px;
  cursor: pointer;
  transition: all 0.2s;
}

.roster-entry-card:active {
  transform: scale(0.98);
  background: linear-gradient(135deg, #FFE8E8 0%, #FFD8D8 100%);
}

.roster-entry-icon {
  width: 44px;
  height: 44px;
  background: var(--gradient-primary);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.roster-entry-content {
  flex: 1;
}

.roster-entry-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.roster-entry-desc {
  font-size: 12px;
  color: #999;
}

.work-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.work-item {
  display: flex;
  gap: 10px;
}

.item-number {
  width: 24px;
  height: 24px;
  background: #D22630;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  flex-shrink: 0;
}

.item-number-icon {
  width: 34px;
  height: 34px;
  background: linear-gradient(135deg, #FFEBEE 0%, #FFCDD2 100%);
  border-radius: 10px;
}

.item-icon {
  font-size: 18px;
  line-height: 1;
}

.item-content {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  padding-top: 2px;
}

.document-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.document-item {
  display: flex;
  align-items: flex-start;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 10px;
}

.doc-icon {
  margin-right: 12px;
  margin-top: 2px;
  flex-shrink: 0;
}

.doc-icon-emoji {
  width: 34px;
  height: 34px;
  background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.doc-emoji {
  font-size: 18px;
  line-height: 1;
}

.doc-content {
  flex: 1;
}

.doc-title {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.doc-desc {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
}

.empty-card {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  text-align: center;
}

/* 乡村振兴绿色主题 */
:deep(.nav-bar-rural) {
  background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%) !important;
}

.detail-header-rural {
  background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 50%, #1B5E20 100%) !important;
  position: relative;
  overflow: hidden;
}

.detail-header-rural::before {
  content: '';
  position: absolute;
  top: -30px;
  right: -30px;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, transparent 70%);
  border-radius: 50%;
}

.header-rural-icon {
  margin-right: 8px;
  font-size: 22px;
}

.section-title-rural {
  border-bottom-color: #E8F5E9 !important;
}

.child-card-rural {
  border-left: 3px solid #4CAF50;
}

.child-number-rural {
  background: #4CAF50 !important;
}

.child-card-rural .child-number-icon {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%) !important;
}

.child-responsible-rural {
  background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%) !important;
}

.doc-tag-rural {
  background: rgba(76, 175, 80, 0.1) !important;
  color: #2E7D32 !important;
}

.work-item-rural {
  background: linear-gradient(135deg, #f1f8e9 0%, #e8f5e9 100%);
  border-left: 3px solid #4CAF50;
}

.item-number-rural {
  background: #4CAF50 !important;
}

.work-item-rural .item-number-icon {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%) !important;
}

.document-item-rural {
  background: linear-gradient(135deg, #f1f8e9 0%, #e8f5e9 100%);
  border: 1px solid #C8E6C9;
}

.document-item-rural .doc-icon-emoji {
  background: linear-gradient(135deg, #E8F5E9 0%, #C8E6C9 100%) !important;
}
</style>