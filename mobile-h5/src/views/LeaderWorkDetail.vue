<template>
  <div class="page-container">
    <van-nav-bar :title="workDetail?.workName || '工作详情'" left-arrow @click-left="goBack" />
    
    <div class="detail-header" v-if="workDetail">
      <div class="header-title">{{ workDetail.workName }}</div>
      <div class="header-subtitle">{{ workDetail.leaderName }}</div>
    </div>

    <div class="detail-content" v-if="workDetail">
      <div class="section-card" v-if="workDetail.children">
        <div class="section-title">
          <van-icon name="file-text-o" size="16" color="#D22630" />
          <span>分管事务</span>
        </div>
        
        <div class="children-list">
          <div 
            v-for="(child, idx) in workDetail.children" 
            :key="idx" 
            class="child-card"
            @click="toggleChild(idx)"
          >
            <div class="child-header">
              <div class="child-left">
                <div class="child-number">{{ idx + 1 }}</div>
                <div class="child-name">{{ child.name }}</div>
              </div>
              <div class="child-arrow" :class="{ expanded: expandedIndex === idx }">
                <van-icon name="arrow" size="16" color="#D22630" />
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
                  >
                    {{ doc }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="section-card" v-else>
        <div class="section-title">
          <van-icon name="file-text-o" size="16" color="#D22630" />
          <span>工作内容</span>
        </div>
        <div class="work-items">
          <div 
            v-for="(item, idx) in workDetail.workItems" 
            :key="idx" 
            class="work-item"
          >
            <div class="item-number">{{ idx + 1 }}</div>
            <div class="item-content">{{ item }}</div>
          </div>
        </div>

        <div v-if="workDetail.documents && workDetail.documents.length > 0">
          <div class="section-title">
            <van-icon name="folder-o" size="16" color="#4CAF50" />
            <span>相关资料台账</span>
          </div>
          <div class="document-list">
            <div 
              v-for="(doc, idx) in workDetail.documents" 
              :key="idx" 
              class="document-item"
            >
              <div class="doc-icon">
                <van-icon name="file" size="20" color="#666" />
              </div>
              <div class="doc-content">
                <div class="doc-title">{{ doc.title }}</div>
                <div class="doc-desc">{{ doc.description }}</div>
              </div>
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

const router = useRouter()
const route = useRoute()

const leaderId = ref(parseInt(route.params.leaderId) || 1)
const workIndex = ref(parseInt(route.params.workIndex) || 0)
const expandedIndex = ref(-1)

const workDetails = ref([
  {
    leaderId: 1,
    workIndex: 0,
    leaderName: '欧阳付群',
    workName: '政协联络',
    workItems: [
      '3项标注为XXXX的工作事项'
    ],
    documents: [
      { title: '政协联络资料台账1', description: '对应第1组工作事项的资料台账' },
      { title: '政协联络资料台账2', description: '对应第2组工作事项的资料台账' },
      { title: '政协联络资料台账3', description: '对应第3组工作事项的资料台账' }
    ]
  },
  {
    leaderId: 5,
    workIndex: 0,
    leaderName: '于鼎馨',
    workName: '纪检工作',
    children: [
      { name: '协助党委推进全面从严治党', documents: ['党委会议记录'] },
      { name: '政治、日常、专项等监督', documents: ['相关监督检查台账'] },
      { name: '线索处置', documents: ['线索台账'] },
      { name: '案件查办', documents: ['案卷卷宗'] },
      { name: '廉政教育', documents: ['廉政风险排查表', '谈心谈话记录', '警示教育台账'] },
      { name: '队伍建设和监督', documents: ['纪检干部花名册', '培训会议'] }
    ]
  }
])

const workDetail = computed(() => {
  return workDetails.value.find(
    d => d.leaderId === leaderId.value && d.workIndex === workIndex.value
  )
})

const toggleChild = (idx) => {
  expandedIndex.value = expandedIndex.value === idx ? -1 : idx
}

const goBack = () => {
  router.back()
}

onMounted(() => {
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

.child-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
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
</style>