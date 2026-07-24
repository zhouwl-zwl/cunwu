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
        @click="work.hasDetail ? goWorkDetail(leader.id, idx) : showToast('暂无详细内容')"
      >
        <div class="work-icon">
          <van-icon name="folder-o" size="24" color="#D22630" />
        </div>
        <div class="work-content">
          <div class="work-name">{{ work.name }}</div>
          <div class="work-desc">{{ work.hasDetail ? '点击查看详细内容' : '暂无详细内容' }}</div>
        </div>
        <div class="work-arrow" :class="{ 'has-detail': work.hasDetail }">
          <van-icon name="arrow" size="16" :color="work.hasDetail ? '#D22630' : '#ccc'" />
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

const leaderId = ref(parseInt(route.params.id) || 1)

const leadersData = ref([
  {
    id: 1,
    name: '欧阳付群',
    color: 'linear-gradient(135deg, #D22630 0%, #B01A26 100%)',
    works: [
      { name: '政协联络', hasDetail: true },
      { name: '统战', hasDetail: false },
      { name: '民族宗教', hasDetail: false },
      { name: '社会工作', hasDetail: false },
      { name: '乡村振兴', hasDetail: false },
      { name: '营商环境', hasDetail: false },
      { name: '经管（三资）', hasDetail: false },
      { name: '农村人居环境治理', hasDetail: false },
      { name: '生态环保', hasDetail: false }
    ]
  },
  {
    id: 2,
    name: '曹海洋',
    color: 'linear-gradient(135deg, #2196F3 0%, #1565C0 100%)',
    works: [
      { name: '人大工作', hasDetail: false },
      { name: '政法', hasDetail: false },
      { name: '信访维稳', hasDetail: false },
      { name: '禁毒', hasDetail: false },
      { name: '法制', hasDetail: false },
      { name: '非法集资', hasDetail: false }
    ]
  },
  {
    id: 3,
    name: '龙君屹',
    color: 'linear-gradient(135deg, #4CAF50 0%, #388E3C 100%)',
    works: [
      { name: '武装', hasDetail: false },
      { name: '国防动员教育', hasDetail: false },
      { name: '退役军人', hasDetail: false },
      { name: '农业农村', hasDetail: false },
      { name: '依法行政', hasDetail: false },
      { name: '统计', hasDetail: false },
      { name: '民政', hasDetail: false },
      { name: '残联', hasDetail: false },
      { name: '医疗保障', hasDetail: false },
      { name: '卫生健康', hasDetail: false },
      { name: '食品安全', hasDetail: false },
      { name: '教育', hasDetail: false }
    ]
  },
  {
    id: 4,
    name: '张芷馨',
    color: 'linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%)',
    works: [
      { name: '基层党建', hasDetail: false },
      { name: '组织人事', hasDetail: false },
      { name: '党务公开', hasDetail: false },
      { name: '宣传思想文化', hasDetail: false },
      { name: '新时代文明实践', hasDetail: false },
      { name: '意识形态', hasDetail: false },
      { name: '文化', hasDetail: false },
      { name: '科协', hasDetail: false },
      { name: '关工委', hasDetail: false },
      { name: '妇联', hasDetail: false },
      { name: '共青团', hasDetail: false },
      { name: '工会', hasDetail: false }
    ]
  },
  {
    id: 5,
    name: '于鼎馨',
    color: 'linear-gradient(135deg, #FF5722 0%, #E64A19 100%)',
    works: [
      { 
        name: '纪检工作', 
        hasDetail: true,
        children: [
          { name: '协助党委推进全面从严治党', documents: ['党委会议记录'] },
          { name: '政治、日常、专项等监督', documents: ['相关监督检查台账'] },
          { name: '线索处置', documents: ['线索台账'] },
          { name: '案件查办', documents: ['案卷卷宗'] },
          { name: '廉政教育', documents: ['廉政风险排查表', '谈心谈话记录', '警示教育台账'] },
          { name: '队伍建设和监督', documents: ['纪检干部花名册', '培训会议'] }
        ]
      }
    ]
  },
  {
    id: 6,
    name: '杨承明',
    color: 'linear-gradient(135deg, #00BCD4 0%, #0097A7 100%)',
    works: [
      { name: '应急管理', hasDetail: false },
      { name: '安全生产', hasDetail: false },
      { name: '水利', hasDetail: false },
      { name: '防汛抗旱', hasDetail: false },
      { name: '住建', hasDetail: false }
    ]
  },
  {
    id: 7,
    name: '杨成',
    color: 'linear-gradient(135deg, #FF9800 0%, #E65100 100%)',
    works: [
      { name: '办公室', hasDetail: false },
      { name: '政务服务', hasDetail: false },
      { name: '政务公开', hasDetail: false },
      { name: '档案工作', hasDetail: false },
      { name: '保密工作', hasDetail: false },
      { name: '民生实事', hasDetail: false },
      { name: '绩效考核', hasDetail: false },
      { name: '人力资源和社会保障', hasDetail: false },
      { name: '自然资源', hasDetail: false },
      { name: '财务', hasDetail: false },
      { name: '审计', hasDetail: false },
      { name: '林业', hasDetail: false },
      { name: '道路交通安全', hasDetail: false },
      { name: '行政执法', hasDetail: false }
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
  background: #f5f5f5;
  padding-bottom: 20px;
}

.leader-header {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 20px 16px;
  margin-bottom: 12px;
}

.leader-avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  color: #fff;
  margin-right: 16px;
}

.leader-info {
  flex: 1;
}

.leader-name {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 6px;
}

.leader-title {
  font-size: 13px;
  color: #999;
}

.works-list {
  padding: 0 12px;
}

.work-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 12px;
  padding: 14px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.work-card:active {
  transform: scale(0.98);
  background: #f9f9f9;
}

.work-icon {
  width: 44px;
  height: 44px;
  background: rgba(210, 38, 48, 0.08);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 14px;
}

.work-content {
  flex: 1;
  min-width: 0;
}

.work-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
}

.work-desc {
  font-size: 12px;
  color: #999;
}

.work-arrow {
  transition: all 0.2s;
}

.work-arrow.has-detail {
  transform: translateX(2px);
}
</style>