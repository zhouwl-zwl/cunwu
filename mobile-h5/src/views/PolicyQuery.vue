<template>
  <div class="page-container">
    <van-nav-bar title="政策查询" left-arrow @click-left="onBack" />
    
    <van-search v-model="keyword" placeholder="搜索政策" @search="onSearch" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="全部" name="all" />
      <van-tab title="低保" name="lowIncome" />
      <van-tab title="养老" name="pension" />
      <van-tab title="产业补贴" name="industry" />
    </van-tabs>

    <div class="table-section">
      <ExcelTable 
        :data="displayData" 
        :columns="policyColumns" 
        export-filename="政策列表.xlsx"
      />
    </div>

    <van-empty v-if="!loading && policyList.length === 0" description="暂无政策信息" />

    <van-dialog v-model:show="showDetailDialog" :title="'政策详情'" width="90%">
      <div v-if="currentPolicy" class="detail-content">
        <div class="detail-title">{{ currentPolicy.title }}</div>
        <div class="detail-meta">
          <span class="tag">{{ currentPolicy.category }}</span>
          <span>{{ currentPolicy.createTime }}</span>
        </div>
        <div class="detail-body">{{ currentPolicy.content }}</div>
      </div>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const activeTab = ref('all')
const keyword = ref('')
const policyList = ref([])
const loading = ref(false)

const showDetailDialog = ref(false)
const currentPolicy = ref(null)

const onBack = () => { goBack(router) }

const onTabChange = () => {
  loadData()
}

const getCategoryName = (category) => {
  const map = {
    '低保': '低保政策',
    '养老': '养老政策',
    '产业': '产业补贴'
  }
  return map[category] || category || '其他'
}

const policyColumns = [
  { key: 'title', title: '政策标题' },
  { key: 'category', title: '分类', formatter: (val) => getCategoryName(val) },
  { key: 'createTime', title: '发布时间' }
]

const displayData = computed(() => {
  let data = policyList.value
  if (activeTab.value === 'lowIncome') {
    data = data.filter(p => p.category.includes('低保'))
  } else if (activeTab.value === 'pension') {
    data = data.filter(p => p.category.includes('养老'))
  } else if (activeTab.value === 'industry') {
    data = data.filter(p => p.category.includes('产业'))
  }
  return data
})

const loadData = async () => {
  loading.value = true
  try {
    const categoryMap = {
      'lowIncome': '低保',
      'pension': '养老',
      'industry': '产业',
      'all': ''
    }
    const category = categoryMap[activeTab.value] || ''
    const res = await request.get('/public/policy-list', { params: { page: 1, size: 20, keyword, category } })
    policyList.value = res.records || res.data?.records || res || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const onSearch = () => {
  loadData()
}

const showDetail = (item) => {
  currentPolicy.value = item
  showDetailDialog.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  background: #FDF5F5;
  min-height: 100vh;
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.table-section {
  margin: 10px 12px;
}

.tag {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  color: #fff;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
}

.detail-content {
  padding: 16px 0;
}

.detail-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #333;
}

.detail-meta {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  color: #999;
  font-size: 14px;
}

.detail-body {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  white-space: pre-wrap;
}

:deep(.van-nav-bar) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

:deep(.van-nav-bar__title) {
  color: #FFD700;
}
</style>
