<template>
  <div class="page-container">
    <van-nav-bar :title="leader?.name || '分管工作'" left-arrow @click-left="goBack" />
    
    <div class="leader-header" v-if="leader">
      <div class="leader-avatar" :style="{ background: leader.color }">
        {{ leader.name.charAt(0) }}
      </div>
      <div class="leader-info">
        <div class="leader-name">{{ leader.name }}</div>
        <div class="leader-title">核心分管工作</div>
      </div>
    </div>

    <div class="works-list" v-if="leader">
      <div 
        v-for="(work, idx) in leader.works" 
        :key="idx" 
        class="work-card"
        :class="{ 'work-card-rural': work.name === '乡村振兴' }"
        @click="work.hasDetail ? goWorkDetail(leader.id, idx) : showToast('暂无详细内容')"
      >
        <div class="work-icon" :class="{ 'work-icon-rural': work.name === '乡村振兴' }" :style="work.name !== '乡村振兴' ? { background: work.color } : {}">
          <span class="work-emoji">{{ work.icon }}</span>
        </div>
        <div class="work-content">
          <div class="work-name" :class="{ 'work-name-rural': work.name === '乡村振兴' }">
            {{ work.name }}
            <span v-if="work.responsible" class="work-responsible">负责人：{{ work.responsible }}</span>
          </div>
          <div class="work-desc" :class="{ 'work-desc-rural': work.name === '乡村振兴' }">
            {{ work.hasDetail ? '点击查看详细内容' : '暂无详细内容' }}
          </div>
        </div>
        <div class="work-arrow" :class="{ 'has-detail': work.hasDetail }">
          <van-icon name="arrow" size="16" :color="work.hasDetail ? '#666' : '#ccc'" />
        </div>
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

const leadersData = ref([
  {
    id: 1,
    name: '欧阳付群',
    color: 'linear-gradient(135deg, #D22630 0%, #B01A26 100%)',
    works: [
      { name: '政协联络', icon: '🤝', color: 'linear-gradient(135deg, #E57373, #C62828)', hasDetail: true },
      { name: '统战', icon: '🌐', color: 'linear-gradient(135deg, #BA68C8, #7B1FA2)', hasDetail: true },
      { name: '民族宗教', icon: '🕌', color: 'linear-gradient(135deg, #4DD0E1, #00838F)', hasDetail: true },
      { name: '社会工作', icon: '💝', color: 'linear-gradient(135deg, #F06292, #AD1457)', hasDetail: true },
      { name: '乡村振兴', icon: '🌾', color: 'linear-gradient(135deg, #66BB6A, #2E7D32)', hasDetail: true },
      { name: '营商环境', icon: '🏢', color: 'linear-gradient(135deg, #42A5F5, #1565C0)', hasDetail: true },
      { name: '经管（三资）', icon: '💼', color: 'linear-gradient(135deg, #FFA726, #E65100)', hasDetail: true },
      { name: '农村人居环境治理', icon: '🌿', color: 'linear-gradient(135deg, #81C784, #388E3C)', hasDetail: true },
      { name: '生态环保', icon: '♻️', color: 'linear-gradient(135deg, #26C6DA, #00695C)', hasDetail: true }
    ]
  },
  {
    id: 2,
    name: '曹海洋',
    color: 'linear-gradient(135deg, #2196F3 0%, #1565C0 100%)',
    works: [
      { name: '人大工作', icon: '🏛️', color: 'linear-gradient(135deg, #64B5F6, #1565C0)', hasDetail: false },
      { name: '政法', icon: '⚖️', color: 'linear-gradient(135deg, #5C6BC0, #283593)', hasDetail: false },
      { name: '信访维稳', icon: '📞', color: 'linear-gradient(135deg, #7986CB, #303F9F)', hasDetail: false },
      { name: '禁毒', icon: '🚫', color: 'linear-gradient(135deg, #EF5350, #C62828)', hasDetail: false },
      { name: '法制', icon: '📜', color: 'linear-gradient(135deg, #42A5F5, #1A237E)', hasDetail: false },
      { name: '非法集资', icon: '💰', color: 'linear-gradient(135deg, #FFA726, #E65100)', hasDetail: false }
    ]
  },
  {
    id: 3,
    name: '龙君屹',
    color: 'linear-gradient(135deg, #4CAF50 0%, #388E3C 100%)',
    works: [
      { name: '武装', icon: '🎖️', color: 'linear-gradient(135deg, #8D6E63, #3E2723)', hasDetail: true },
      { name: '国防动员教育', icon: '🛡️', color: 'linear-gradient(135deg, #795548, #3E2723)', hasDetail: true },
      { name: '退役军人', icon: '🪖', color: 'linear-gradient(135deg, #5D4037, #1B0000)', hasDetail: true },
      { name: '农业农村', icon: '🌾', color: 'linear-gradient(135deg, #66BB6A, #1B5E20)', hasDetail: true },
      { name: '畜牧水产', icon: '🐄', color: 'linear-gradient(135deg, #FFA726, #E65100)', hasDetail: true },
      { name: '依法行政', icon: '⚖️', color: 'linear-gradient(135deg, #42A5F5, #0D47A1)', hasDetail: true },
      { name: '统计', icon: '📊', color: 'linear-gradient(135deg, #26A69A, #004D40)', hasDetail: true },
      { name: '民政', icon: '🏘️', color: 'linear-gradient(135deg, #EF9A9A, #C62828)', hasDetail: true },
      { name: '残联', icon: '♿', color: 'linear-gradient(135deg, #90CAF9, #1565C0)', hasDetail: true },
      { name: '医疗保障', icon: '🏥', color: 'linear-gradient(135deg, #A5D6A7, #2E7D32)', hasDetail: true },
      { name: '卫生健康', icon: '💊', color: 'linear-gradient(135deg, #80CBC4, #00695C)', hasDetail: true },
      { name: '食品安全', icon: '🍎', color: 'linear-gradient(135deg, #EF5350, #C62828)', hasDetail: true },
      { name: '教育', icon: '🎓', color: 'linear-gradient(135deg, #9575CD, #4527A0)', hasDetail: true }
    ]
  },
  {
    id: 4,
    name: '张芷馨',
    color: 'linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%)',
    works: [
      { name: '基层党建', icon: '🚩', color: 'linear-gradient(135deg, #E53935, #B71C1C)', hasDetail: true },
      { name: '组织人事', icon: '👥', color: 'linear-gradient(135deg, #7E57C2, #4527A0)', hasDetail: false },
      { name: '党务公开', icon: '📢', color: 'linear-gradient(135deg, #BA68C8, #6A1B9A)', hasDetail: false },
      { name: '宣传思想文化', icon: '📣', color: 'linear-gradient(135deg, #CE93D8, #6A1B9A)', hasDetail: true },
      { name: '新时代文明实践', icon: '🌟', color: 'linear-gradient(135deg, #D4E157, #827717)', hasDetail: true },
      { name: '意识形态', icon: '🧠', color: 'linear-gradient(135deg, #9575CD, #4527A0)', hasDetail: true },
      { name: '文化', icon: '🎭', color: 'linear-gradient(135deg, #F06292, #880E4F)', hasDetail: true },
      { name: '科协', icon: '🔬', color: 'linear-gradient(135deg, #4DD0E1, #006064)', hasDetail: true },
      { name: '关工委', icon: '👴', color: 'linear-gradient(135deg, #BCAAA4, #4E342E)', hasDetail: true },
      { name: '妇联', icon: '👩', color: 'linear-gradient(135deg, #F48FB1, #AD1457)', hasDetail: true },
      { name: '共青团', icon: '🧑', color: 'linear-gradient(135deg, #81D4FA, #01579B)', hasDetail: true },
      { name: '工会', icon: '👷', color: 'linear-gradient(135deg, #FFB74D, #E65100)', hasDetail: true }
    ]
  },
  {
    id: 5,
    name: '于鼎馨',
    color: 'linear-gradient(135deg, #FF5722 0%, #E64A19 100%)',
    works: [
      { 
        name: '纪检工作', 
        icon: '⚖️',
        color: 'linear-gradient(135deg, #FF8A65, #BF360C)',
        hasDetail: true,
        children: [
          { name: '协助党委推进全面从严治党', icon: '📜', documents: ['党委会议记录'] },
          { name: '政治、日常、专项等监督', icon: '👁️', documents: ['相关监督检查台账'] },
          { name: '线索处置', icon: '🔍', documents: ['线索台账'] },
          { name: '案件查办', icon: '⚖️', documents: ['案卷卷宗'] },
          { name: '廉政教育', icon: '🎓', documents: ['廉政风险排查表', '谈心谈话记录', '警示教育台账'] },
          { name: '队伍建设和监督', icon: '👥', documents: ['纪检干部花名册', '培训会议'] }
        ]
      }
    ]
  },
  {
    id: 6,
    name: '杨承明',
    color: 'linear-gradient(135deg, #00BCD4 0%, #0097A7 100%)',
    works: [
      { name: '应急管理', icon: '🚨', color: 'linear-gradient(135deg, #EF5350, #B71C1C)', hasDetail: true },
      { name: '安全生产（消防）', icon: '⛑️', color: 'linear-gradient(135deg, #FFA726, #E65100)', hasDetail: true },
      { name: '水利', icon: '💧', color: 'linear-gradient(135deg, #29B6F6, #01579B)', hasDetail: true },
      { name: '防汛抗旱', icon: '🌊', color: 'linear-gradient(135deg, #4FC3F7, #01579B)', hasDetail: true },
      { name: '住建（燃气）', icon: '🏗️', color: 'linear-gradient(135deg, #8D6E63, #3E2723)', hasDetail: true }
    ]
  },
  {
    id: 7,
    name: '杨成',
    color: 'linear-gradient(135deg, #FF9800 0%, #E65100 100%)',
    works: [
      { name: '办公室', icon: '📋', color: 'linear-gradient(135deg, #FFB74D, #E65100)', hasDetail: false },
      { name: '政务服务', icon: '🏛️', color: 'linear-gradient(135deg, #FFCC80, #EF6C00)', hasDetail: true },
      { name: '政务公开', icon: '📢', color: 'linear-gradient(135deg, #FFE082, #F57F17)', hasDetail: true },
      { name: '档案工作', icon: '📁', color: 'linear-gradient(135deg, #FFAB91, #D84315)', hasDetail: false },
      { name: '保密工作', icon: '🔒', color: 'linear-gradient(135deg, #A1887F, #4E342E)', hasDetail: false },
      { name: '民生实事', icon: '❤️', color: 'linear-gradient(135deg, #EF9A9A, #C62828)', hasDetail: false },
      { name: '绩效考核', icon: '📈', color: 'linear-gradient(135deg, #FFD54F, #FF6F00)', hasDetail: false },
      { name: '人力资源和社会保障', icon: '👤', color: 'linear-gradient(135deg, #CE93D8, #6A1B9A)', hasDetail: true },
      { name: '自然资源', icon: '🌍', color: 'linear-gradient(135deg, #81C784, #1B5E20)', hasDetail: false },
      { name: '财务', icon: '💴', color: 'linear-gradient(135deg, #FFCA28, #F57F17)', hasDetail: false },
      { name: '审计', icon: '🔍', color: 'linear-gradient(135deg, #BCAAA4, #4E342E)', hasDetail: false },
      { name: '林业', icon: '🌲', color: 'linear-gradient(135deg, #66BB6A, #1B5E20)', hasDetail: false },
      { name: '道路交通安全', icon: '🚦', color: 'linear-gradient(135deg, #FF7043, #BF360C)', hasDetail: false },
      { name: '行政执法', icon: '⚖️', color: 'linear-gradient(135deg, #42A5F5, #0D47A1)', hasDetail: false }
    ]
  }
])

const leader = computed(() => {
  return leadersData.value.find(l => l.id === leaderId.value)
})

const goBack = () => {
  router.back()
}

const goWorkDetail = (leaderId, workIndex) => {
  router.push(`/leader-work-detail/${leaderId}/${workIndex}`)
}

onMounted(() => {
  if (leader.value) {
    document.title = `${leader.value.name} - 分管工作`
  }
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: var(--background-color);
  padding-bottom: var(--spacing-5);
  font-family: var(--font-family-base);
}

/* ===== 顶部个人信息区 ===== */
.leader-header {
  display: flex;
  align-items: center;
  background: var(--card-background);
  padding: var(--spacing-5) var(--spacing-lg);
  margin-bottom: var(--spacing-md);
  position: relative;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

/* 顶部高光条 */
.leader-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.6), transparent);
  opacity: 0.5;
}

/* 右上角光斑装饰 */
.leader-header::after {
  content: '';
  position: absolute;
  top: -40px;
  right: -40px;
  width: 120px;
  height: 120px;
  background: radial-gradient(circle, var(--primary-soft) 0%, transparent 70%);
  border-radius: var(--radius-full);
  opacity: 0.6;
}

.leader-avatar {
  width: 64px;
  height: 64px;
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-3xl);
  font-weight: var(--font-weight-bold);
  color: #fff;
  margin-right: var(--spacing-lg);
  position: relative;
  flex-shrink: 0;
  box-shadow: var(--shadow-md);
  z-index: 1;
}

/* 头像顶部高光 */
.leader-avatar::after {
  content: '';
  position: absolute;
  top: 3px;
  left: 3px;
  right: 3px;
  height: 42%;
  background: linear-gradient(to bottom, rgba(255, 255, 255, 0.3), transparent);
  border-radius: var(--radius-full) var(--radius-full) 0 0;
  pointer-events: none;
}

.leader-info {
  flex: 1;
  position: relative;
  z-index: 1;
}

.leader-name {
  font-size: var(--font-size-2xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-2);
  letter-spacing: var(--letter-spacing-tight);
  line-height: var(--line-height-tight);
}

.leader-title {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-normal);
  color: var(--text-tertiary);
  letter-spacing: var(--letter-spacing-wide);
}

/* ===== 工作列表 ===== */
.works-list {
  padding: 0 var(--spacing-md);
}

.work-card {
  display: flex;
  align-items: center;
  background: var(--card-background);
  border-radius: var(--radius-xl);
  padding: var(--spacing-5) var(--spacing-lg);
  margin-bottom: var(--spacing-3);
  cursor: pointer;
  transition: all var(--transition-normal);
  box-shadow: var(--shadow-sm);
  position: relative;
  overflow: hidden;
  border: 1px solid var(--border-color);
}

/* 卡片顶部高光 */
.work-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.5), transparent);
  opacity: 0.6;
  pointer-events: none;
}

/* 按压反馈 - 弹簧动效 */
.work-card:active {
  transform: scale(0.985) translateY(1px);
  box-shadow: var(--shadow-xs);
  transition: all var(--transition-fast);
}

.work-icon {
  width: 50px;
  height: 50px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: var(--spacing-4);
  box-shadow: var(--shadow-md);
  flex-shrink: 0;
  position: relative;
  overflow: hidden;
}

/* 图标顶部高光 */
.work-icon::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 50%;
  background: linear-gradient(to bottom, rgba(255, 255, 255, 0.25), transparent);
  border-radius: var(--radius-lg) var(--radius-lg) 0 0;
  pointer-events: none;
}

.work-emoji {
  font-size: 26px;
  line-height: 1;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.15));
}

.work-content {
  flex: 1;
  min-width: 0;
}

.work-name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-2);
  display: flex;
  align-items: center;
  gap: var(--spacing-2);
  flex-wrap: wrap;
  line-height: var(--line-height-tight);
}

.work-responsible {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: #fff;
  background: var(--gradient-primary);
  padding: var(--spacing-1) var(--spacing-3);
  border-radius: var(--radius-full);
  letter-spacing: 0.01em;
}

.work-desc {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-normal);
  color: var(--text-tertiary);
  line-height: var(--line-height-normal);
}

.work-arrow {
  transition: transform var(--transition-normal);
  flex-shrink: 0;
}

.work-arrow.has-detail {
  transform: translateX(2px);
}

.work-card:active .work-arrow.has-detail {
  transform: translateX(4px);
}

/* ===== 乡村振兴特色卡片 ===== */
.work-card-rural {
  background: linear-gradient(135deg, #66BB6A 0%, #43A047 40%, #2E7D32 70%, #1B5E20 100%);
  border-radius: var(--radius-xl);
  position: relative;
  overflow: hidden;
  box-shadow: 0 6px 20px rgba(76, 175, 80, 0.35), 0 2px 6px rgba(0, 0, 0, 0.08);
  padding: var(--spacing-5) var(--spacing-lg);
  border: none;
}

/* 主光斑 - 右上角 */
.work-card-rural::before {
  content: '';
  position: absolute;
  top: -30px;
  right: -20px;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.25) 0%, transparent 65%);
  border-radius: var(--radius-full);
  pointer-events: none;
}

/* 底部反光条 */
.work-card-rural::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 24px;
  background: linear-gradient(to top, rgba(255, 255, 255, 0.12) 0%, transparent 100%);
  pointer-events: none;
}

/* 按压效果 - 绿色版 */
.work-card-rural:active {
  transform: scale(0.985) translateY(1px);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 40%, #2E7D32 70%, #1B5E20 100%);
}

.work-icon-rural {
  background: rgba(255, 255, 255, 0.28) !important;
  backdrop-filter: blur(var(--blur-sm));
  -webkit-backdrop-filter: blur(var(--blur-sm));
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15), inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

.work-icon-rural::after {
  background: linear-gradient(to bottom, rgba(255, 255, 255, 0.35), transparent);
}

.rural-icon {
  font-size: 26px;
  line-height: 1;
}

.work-name-rural {
  color: #fff !important;
  font-weight: var(--font-weight-semibold);
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.15);
}

.work-name-rural .work-responsible {
  background: rgba(255, 255, 255, 0.3);
  color: #fff;
  backdrop-filter: blur(var(--blur-sm));
  -webkit-backdrop-filter: blur(var(--blur-sm));
  border: 1px solid rgba(255, 255, 255, 0.2);
  text-shadow: none;
}

.work-desc-rural {
  color: rgba(255, 255, 255, 0.82) !important;
  font-weight: var(--font-weight-normal);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.work-arrow-rural {
  z-index: 1;
}
</style>