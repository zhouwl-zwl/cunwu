<template>
  <div class="page-container">
    <van-nav-bar title="乡镇主要事务负责人" left-arrow @click-left="goBack" />
    
    <div class="header-card">
      <div class="header-icon">
        <van-icon name="contacts" size="40" color="#fff" />
      </div>
      <div class="header-title">罗卜田乡班子成员分管工作明细表</div>
      <div class="header-desc">共{{ leaders.length }}位班子成员</div>
    </div>

    <div class="leader-list">
      <div 
        v-for="leader in leaders" 
        :key="leader.id" 
        class="leader-card"
        @click="goDetail(leader.id)"
      >
        <div class="leader-avatar" :style="{ background: leader.color }">
          {{ leader.name.charAt(0) }}
        </div>
        <div class="leader-info">
          <div class="leader-name">{{ leader.name }}</div>
          <div class="leader-count">分管 {{ leader.works.length }} 项事务</div>
          <div class="leader-works">
            <span 
              v-for="(work, idx) in leader.works.slice(0, 3)" 
              :key="idx" 
              class="work-tag"
            >{{ work.name }}</span>
            <span v-if="leader.works.length > 3" class="work-tag more-tag">+{{ leader.works.length - 3 }}</span>
          </div>
        </div>
        <van-icon name="arrow" size="16" color="#ccc" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const leaders = ref([
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
      { name: '纪检工作', hasDetail: true }
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

const goBack = () => {
  router.back()
}

const goDetail = (id) => {
  router.push(`/leader-detail/${id}`)
}

onMounted(() => {
  document.title = '乡镇主要事务负责人 - 村级智慧村务平台'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.header-card {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  padding: 24px 16px;
  text-align: center;
  color: #fff;
  margin-bottom: 12px;
}

.header-icon {
  margin-bottom: 12px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 6px;
}

.header-desc {
  font-size: 13px;
  opacity: 0.9;
}

.leader-list {
  padding: 0 12px;
}

.leader-card {
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

.leader-card:active {
  transform: scale(0.98);
  background: #f9f9f9;
}

.leader-avatar {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  margin-right: 14px;
}

.leader-info {
  flex: 1;
  min-width: 0;
}

.leader-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 6px;
}

.leader-count {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.leader-works {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.work-tag {
  padding: 3px 8px;
  background: #f0f0f0;
  border-radius: 6px;
  font-size: 11px;
  color: #666;
}

.work-tag.more-tag {
  background: #D22630;
  color: #fff;
}
</style>