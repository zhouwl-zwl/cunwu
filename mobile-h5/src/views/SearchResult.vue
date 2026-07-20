<template>
  <div class="search-page page-container">
    <van-nav-bar :title="'搜索结果：' + keyword" left-arrow @click-left="handleBack" />
    
    <div class="search-bar-wrap">
      <van-search 
        v-model="keyword" 
        placeholder="搜索政策、服务、通知..." 
        @search="doSearch"
        show-action
      >
        <template #action>
          <van-button text type="primary" @click="doSearch">搜索</van-button>
        </template>
      </van-search>
    </div>

    <div class="search-tags">
      <span 
        v-for="tag in hotTags" 
        :key="tag" 
        class="search-tag"
        @click="searchByTag(tag)"
      >{{ tag }}</span>
    </div>

    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>搜索中...</p>
    </div>

    <div v-else-if="searchResults.length === 0" class="empty-state">
      <van-icon name="search" size="48" color="#999" />
      <p>未找到相关内容</p>
      <p class="empty-tip">试试其他关键词</p>
    </div>

    <div v-else class="search-results">
      <div class="result-section" v-if="policyResults.length > 0">
        <div class="section-header">
          <van-icon name="file-text" size="18" color="#D22630" />
          <span>政策文件</span>
        </div>
        <div class="result-list">
          <div v-for="item in policyResults" :key="item.id" class="result-card" @click="goDetail(item)">
            <div class="result-icon policy-icon">📋</div>
            <div class="result-content">
              <div class="result-title">{{ item.title }}</div>
              <div class="result-desc">{{ item.description }}</div>
              <div class="result-meta">
                <span class="meta-tag">{{ item.category }}</span>
                <span class="meta-time">{{ item.publishTime }}</span>
              </div>
            </div>
            <van-icon name="arrow-right" size="18" color="#999" />
          </div>
        </div>
      </div>

      <div class="result-section" v-if="serviceResults.length > 0">
        <div class="section-header">
          <van-icon name="service" size="18" color="#D22630" />
          <span>便民服务</span>
        </div>
        <div class="result-list">
          <div v-for="item in serviceResults" :key="item.id" class="result-card" @click="goService(item)">
            <div class="result-icon service-icon">{{ item.icon }}</div>
            <div class="result-content">
              <div class="result-title">{{ item.name }}</div>
              <div class="result-desc">{{ item.description }}</div>
              <div class="result-meta">
                <span class="meta-tag">{{ item.type }}</span>
              </div>
            </div>
            <van-icon name="arrow-right" size="18" color="#999" />
          </div>
        </div>
      </div>

      <div class="result-section" v-if="noticeResults.length > 0">
        <div class="section-header">
          <van-icon name="bell" size="18" color="#D22630" />
          <span>通知公告</span>
        </div>
        <div class="result-list">
          <div v-for="item in noticeResults" :key="item.id" class="result-card" @click="goNoticeDetail(item)">
            <div class="result-icon notice-icon">📢</div>
            <div class="result-content">
              <div class="result-title">{{ item.title }}</div>
              <div class="result-desc">{{ item.content }}</div>
              <div class="result-meta">
                <span class="meta-tag">{{ item.noticeType }}</span>
                <span class="meta-time">{{ item.createTime }}</span>
              </div>
            </div>
            <van-icon name="arrow-right" size="18" color="#999" />
          </div>
        </div>
      </div>

      <div class="result-section" v-if="guideResults.length > 0">
        <div class="section-header">
          <van-icon name="book-open-o" size="18" color="#D22630" />
          <span>办事指南</span>
        </div>
        <div class="result-list">
          <div v-for="item in guideResults" :key="item.id" class="result-card" @click="goGuide(item)">
            <div class="result-icon guide-icon">{{ item.icon }}</div>
            <div class="result-content">
              <div class="result-title">{{ item.title }}</div>
              <div class="result-desc">{{ item.desc }}</div>
              <div class="result-meta">
                <span class="meta-tag">{{ item.type }}</span>
              </div>
            </div>
            <van-icon name="arrow-right" size="18" color="#999" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import { goBack } from '../utils/index'

const router = useRouter()
const route = useRoute()

const keyword = ref('')
const loading = ref(false)

const hotTags = ['低保', '补贴', '医保', '养老', '土地', '纠纷', '环境', '道路']

const mockPolicies = [
  { id: 1, title: '浙江省城乡居民最低生活保障办法', description: '浙江省政府发布的城乡居民最低生活保障政策，明确保障范围、申请条件和审批程序', category: '民政政策', publishTime: '2024-01-15' },
  { id: 2, title: '耕地地力保护补贴政策解读', description: '2024年度耕地地力保护补贴发放标准及申请流程', category: '农业政策', publishTime: '2024-02-20' },
  { id: 3, title: '高龄老人生活补贴实施细则', description: '60岁以上老人生活补贴申请条件和发放标准', category: '民政政策', publishTime: '2024-03-10' },
  { id: 4, title: '残疾人就业保障金管理办法', description: '残疾人就业保障相关政策和扶持措施', category: '残联政策', publishTime: '2024-04-05' },
  { id: 5, title: '农村危房改造补助政策', description: '农村危房改造申请条件和补助标准', category: '住建政策', publishTime: '2024-05-18' }
]

const mockServices = [
  { id: 1, name: '低保申请', icon: '🏠', description: '在线申请城乡居民最低生活保障', type: '政务服务' },
  { id: 2, name: '补贴查询', icon: '💰', description: '查询各项惠民补贴发放情况', type: '政务服务' },
  { id: 3, name: '医保查询', icon: '💊', description: '查询医保缴费和报销信息', type: '医疗服务' },
  { id: 4, name: '法律援助', icon: '⚖️', description: '获取免费法律咨询和援助', type: '法律服务' },
  { id: 5, name: '就业服务', icon: '💼', description: '职业培训和就业岗位推荐', type: '就业服务' }
]

const mockNotices = [
  { id: 1, title: '2024年第二季度低保申请开始', content: '符合条件的村民请于6月30日前到村委会办理申请', noticeType: '政务通知', createTime: '2024-06-01' },
  { id: 2, title: '夏季消防安全检查通知', content: '请村民注意夏季用电安全，配合消防安全检查', noticeType: '安全通知', createTime: '2024-06-15' },
  { id: 3, title: '耕地补贴发放公示', content: '2024年度耕地补贴已发放，请查看到账情况', noticeType: '财务公开', createTime: '2024-07-01' },
  { id: 4, title: '养老保险政策调整公告', content: '城乡居民养老保险缴费标准调整通知', noticeType: '政策通知', createTime: '2024-05-20' }
]

const mockGuides = [
  { id: 1, title: '户籍迁移办理流程', desc: '所需材料、办理时限、注意事项', type: '户籍办理', icon: '📋' },
  { id: 2, title: '社保医保参保指南', desc: '参保条件、缴费标准、报销流程', type: '社保办理', icon: '💳' },
  { id: 3, title: '残疾人证办理', desc: '申请条件、评定流程、补贴政策', type: '残联办理', icon: '♿' },
  { id: 4, title: '危房改造申请', desc: '申请条件、审批流程、补助标准', type: '住建办理', icon: '🏠' }
]

const searchResults = ref([])

const policyResults = computed(() => searchResults.value.filter(item => item.type === 'policy'))
const serviceResults = computed(() => searchResults.value.filter(item => item.type === 'service'))
const noticeResults = computed(() => searchResults.value.filter(item => item.type === 'notice'))
const guideResults = computed(() => searchResults.value.filter(item => item.type === 'guide'))

const handleBack = () => {
  goBack(router)
}

const searchByTag = (tag) => {
  keyword.value = tag
  doSearch()
}

const doSearch = async () => {
  if (!keyword.value.trim()) {
    showToast('请输入搜索关键词')
    return
  }
  
  loading.value = true
  
  await new Promise(resolve => setTimeout(resolve, 500))
  
  const kw = keyword.value.toLowerCase()
  const results = []
  
  mockPolicies.forEach(item => {
    if (item.title.toLowerCase().includes(kw) || item.description.toLowerCase().includes(kw) || item.category.toLowerCase().includes(kw)) {
      results.push({ ...item, type: 'policy' })
    }
  })
  
  mockServices.forEach(item => {
    if (item.name.toLowerCase().includes(kw) || item.description.toLowerCase().includes(kw) || item.type.toLowerCase().includes(kw)) {
      results.push({ ...item, type: 'service' })
    }
  })
  
  mockNotices.forEach(item => {
    if (item.title.toLowerCase().includes(kw) || item.content.toLowerCase().includes(kw) || item.noticeType.toLowerCase().includes(kw)) {
      results.push({ ...item, type: 'notice' })
    }
  })
  
  mockGuides.forEach(item => {
    if (item.title.toLowerCase().includes(kw) || item.desc.toLowerCase().includes(kw) || item.type.toLowerCase().includes(kw)) {
      results.push({ ...item, type: 'guide' })
    }
  })
  
  searchResults.value = results
  loading.value = false
}

const goDetail = (item) => {
  showToast(`查看政策：${item.title}`)
}

const goService = (item) => {
  if (item.name === '补贴查询') {
    router.push('/subsidy')
  } else {
    showToast(`${item.name}功能开发中`)
  }
}

const goNoticeDetail = (item) => {
  router.push(`/notification-detail/${item.id}`)
}

const goGuide = (item) => {
  showToast(`查看办事指南：${item.title}`)
}

onMounted(() => {
  keyword.value = route.query.keyword || ''
  if (keyword.value) {
    doSearch()
  }
})
</script>

<style scoped>
.search-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.search-bar-wrap {
  padding: 12px;
}

.search-bar-wrap :deep(.van-search) {
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.08);
}

.search-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 0 12px 12px;
}

.search-tag {
  padding: 6px 14px;
  background: rgba(210, 38, 48, 0.08);
  color: #D22630;
  font-size: 13px;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.2s;
}

.search-tag:active {
  background: rgba(210, 38, 48, 0.2);
  transform: scale(0.95);
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60px 20px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(210, 38, 48, 0.2);
  border-top-color: #D22630;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-state p {
  color: #999;
  font-size: 14px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60px 20px;
}

.empty-state p {
  margin-top: 12px;
  color: #999;
  font-size: 14px;
}

.empty-tip {
  font-size: 12px !important;
  color: #bbb !important;
}

.search-results {
  padding: 0 12px;
}

.result-section {
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.result-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.result-card {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 14px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.06);
  cursor: pointer;
  transition: all 0.2s;
}

.result-card:active {
  transform: scale(0.99);
  background: #fafafa;
}

.result-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  margin-right: 12px;
}

.policy-icon {
  background: linear-gradient(135deg, rgba(210, 38, 48, 0.1) 0%, rgba(210, 38, 48, 0.05) 100%);
}

.service-icon {
  background: linear-gradient(135deg, rgba(33, 150, 243, 0.1) 0%, rgba(33, 150, 243, 0.05) 100%);
}

.notice-icon {
  background: linear-gradient(135deg, rgba(255, 152, 0, 0.1) 0%, rgba(255, 152, 0, 0.05) 100%);
}

.guide-icon {
  background: linear-gradient(135deg, rgba(76, 175, 80, 0.1) 0%, rgba(76, 175, 80, 0.05) 100%);
}

.result-content {
  flex: 1;
  min-width: 0;
}

.result-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.result-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.result-meta {
  display: flex;
  gap: 8px;
}

.meta-tag {
  font-size: 11px;
  color: #D22630;
  background: rgba(210, 38, 48, 0.08);
  padding: 2px 8px;
  border-radius: 6px;
}

.meta-time {
  font-size: 11px;
  color: #999;
}

:deep(.van-nav-bar) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

:deep(.van-nav-bar__title) {
  color: #FFD700;
}
</style>
