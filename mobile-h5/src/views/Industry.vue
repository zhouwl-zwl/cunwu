<template>
  <div class="industry-page page-container">
    <van-nav-bar title="产业发展" left-arrow @click-left="handleBack" />

    <div class="stats-card">
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalCooperatives }}</div>
        <div class="stat-label">合作社数量</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalIncome }}</div>
        <div class="stat-label">年总收入(万元)</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalEmployment }}</div>
        <div class="stat-label">带动就业(人)</div>
      </div>
    </div>

    <div class="filter-bar">
      <van-search v-model="searchKeyword" placeholder="搜索合作社名称" @search="onSearch" />
    </div>

    <div class="table-section">
      <ExcelTable 
        :data="industryList" 
        :columns="industryColumns" 
        export-filename="产业发展.xlsx"
      />
    </div>

    <div v-if="isOfficial" class="action-bar">
      <van-button type="primary" icon="plus" @click="showAddModal = true">新增产业项目</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const searchKeyword = ref('')
const isOfficial = true
const showAddModal = ref(false)

const stats = reactive({
  totalCooperatives: 6,
  totalIncome: 2850,
  totalEmployment: 320
})

const industryList = ref([
  { id: 1, name: '罗卜田乡生态农业合作社', type: '种植养殖', manager: '张建国', annualIncome: 850, employment: 85, status: '运营中', description: '主要从事有机蔬菜种植、家禽养殖，产品远销省内外各大超市' },
  { id: 2, name: '青山茶叶专业合作社', type: '特色种植', manager: '李明辉', annualIncome: 620, employment: 60, status: '运营中', description: '种植高山云雾茶，年产茶叶50吨，获得有机认证' },
  { id: 3, name: '碧水渔业合作社', type: '水产养殖', manager: '王水生', annualIncome: 480, employment: 45, status: '运营中', description: '利用村内水库发展特色鱼类养殖，产品供应周边城市' },
  { id: 4, name: '乡村旅游发展公司', type: '旅游服务', manager: '赵美丽', annualIncome: 560, employment: 70, status: '运营中', description: '开发乡村旅游项目，提供农家乐、采摘园、民宿等服务' },
  { id: 5, name: '农产品加工合作社', type: '农产品加工', manager: '陈伟强', annualIncome: 340, employment: 60, status: '运营中', description: '从事农产品深加工，生产酱菜、干货、糕点等产品' }
])

const industryColumns = [
  { key: 'name', title: '合作社名称' },
  { key: 'type', title: '类型' },
  { key: 'manager', title: '负责人' },
  { key: 'annualIncome', title: '年产值(万元)' },
  { key: 'employment', title: '就业人数', formatter: (val) => `${val || 0}人` },
  { key: 'status', title: '状态' }
]

const handleBack = () => {
  goBack(router)
}

const onSearch = () => {
  if (!searchKeyword.value) {
    fetchData()
    return
  }
  const filtered = industryList.value.filter(item => 
    item.name.includes(searchKeyword.value)
  )
  industryList.value = filtered.length > 0 ? filtered : industryList.value
  showToast(`搜索到 ${industryList.value.length} 条结果`)
}

const fetchData = async () => {
  try {
    const res = await request.get('/industry/list')
    industryList.value = res.data?.records || industryList.value
    stats.totalCooperatives = res.data?.totalCooperatives || stats.totalCooperatives
    stats.totalIncome = res.data?.totalIncome || stats.totalIncome
    stats.totalEmployment = res.data?.totalEmployment || stats.totalEmployment
  } catch (error) {
    console.error('获取产业数据失败', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.industry-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.stats-card {
  display: flex;
  background: #fff;
  margin: 12px;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #D22630;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.filter-bar {
  padding: 0 12px;
  margin-bottom: 12px;
}

.table-section {
  margin: 0 12px;
}

.action-bar {
  padding: 16px 12px;
}

.action-bar .van-button {
  width: 100%;
}
</style>