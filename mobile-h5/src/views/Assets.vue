<template>
  <div class="assets-page page-container">
    <van-nav-bar title="三资管理" left-arrow @click-left="handleBack" />

    <div class="stats-card">
      <div class="stat-item">
        <div class="stat-value">{{ statistics.assetTotal.toLocaleString() }}</div>
        <div class="stat-label">资产总额(元)</div>
      </div>
      <div class="stat-item">
        <div class="stat-value income">{{ statistics.incomeTotal.toLocaleString() }}</div>
        <div class="stat-label">收入总额(元)</div>
      </div>
      <div class="stat-item">
        <div class="stat-value expense">{{ statistics.expenseTotal.toLocaleString() }}</div>
        <div class="stat-label">支出总额(元)</div>
      </div>
    </div>

    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="资产台账" name="asset">
        <div class="table-section">
          <ExcelTable 
            :data="assets" 
            :columns="assetColumns" 
            export-filename="资产台账.xlsx"
          />
        </div>
      </van-tab>

      <van-tab title="资金流水" name="fund">
        <div class="table-section">
          <ExcelTable 
            :data="fundFlows" 
            :columns="fundColumns" 
            export-filename="资金流水.xlsx"
          />
        </div>
      </van-tab>

      <van-tab title="资源台账" name="resource">
        <div class="table-section">
          <ExcelTable 
            :data="resources" 
            :columns="resourceColumns" 
            export-filename="资源台账.xlsx"
          />
        </div>
      </van-tab>
    </van-tabs>

    <div v-if="isOfficial" class="action-bar">
      <van-button type="primary" icon="plus" @click="showAddModal = true">新增资产</van-button>
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
const activeTab = ref('asset')
const searchKeyword = ref('')
const isOfficial = true
const showAddModal = ref(false)

const fundTypeColumns = [
  { text: '全部', value: '' },
  { text: '收入', value: 'INCOME' },
  { text: '支出', value: 'EXPENSE' }
]

const assetColumns = [
  { key: 'name', title: '资产名称' },
  { key: 'type', title: '资产类型', formatter: (val) => getAssetType(val) },
  { key: 'code', title: '资产编号' },
  { key: 'value', title: '原值(元)', formatter: (val) => `¥${(val || 0).toLocaleString()}` },
  { key: 'netValue', title: '净值(元)', formatter: (val) => `¥${(val || 0).toLocaleString()}` },
  { key: 'status', title: '使用状态' }
]

const fundColumns = [
  { key: 'description', title: '描述' },
  { key: 'type', title: '类型', formatter: (val) => val === 'INCOME' ? '收入' : '支出' },
  { key: 'amount', title: '金额(元)', formatter: (val) => `¥${(val || 0).toLocaleString()}` },
  { key: 'date', title: '日期' },
  { key: 'handler', title: '经手人' },
  { key: 'remark', title: '备注' }
]

const resourceColumns = [
  { key: 'name', title: '资源名称' },
  { key: 'type', title: '资源类型', formatter: (val) => getResourceType(val) },
  { key: 'area', title: '面积(亩)', formatter: (val) => `${val || 0}亩` },
  { key: 'location', title: '位置' },
  { key: 'useType', title: '用途' },
  { key: 'contractStatus', title: '承包状态' }
]

const statistics = reactive({
  assetTotal: 25800000,
  incomeTotal: 3650000,
  expenseTotal: 2890000
})

const assets = ref([
  { id: 1, name: '村办公楼', type: 'building', code: 'ZC001', value: 5000000, netValue: 3500000, status: '使用中', icon: '🏢' },
  { id: 2, name: '文化活动中心', type: 'building', code: 'ZC002', value: 2800000, netValue: 2100000, status: '使用中', icon: '🏛️' },
  { id: 3, name: '村卫生室', type: 'building', code: 'ZC003', value: 1500000, netValue: 1200000, status: '使用中', icon: '🏥' },
  { id: 4, name: '农用机械设备', type: 'equipment', code: 'ZC004', value: 850000, netValue: 425000, status: '使用中', icon: '🚜' },
  { id: 5, name: '村集体货车', type: 'vehicle', code: 'ZC005', value: 280000, netValue: 140000, status: '使用中', icon: '🚛' },
  { id: 6, name: '闲置厂房', type: 'building', code: 'ZC006', value: 1200000, netValue: 800000, status: '闲置', icon: '🏭' }
])

const fundFlows = ref([
  { id: 1, description: '土地承包租金收入', amount: 1200000, type: 'INCOME', date: '2024-01-15', handler: '张会计', remark: '2024年度土地承包款' },
  { id: 2, description: '产业项目分红', amount: 850000, type: 'INCOME', date: '2024-01-12', handler: '张会计', remark: '合作社年度分红' },
  { id: 3, description: '基础设施建设支出', amount: 650000, type: 'EXPENSE', date: '2024-01-10', handler: '李主任', remark: '道路维修工程款' },
  { id: 4, description: '保洁员工资发放', amount: 90000, type: 'EXPENSE', date: '2024-01-08', handler: '张会计', remark: '季度工资发放' },
  { id: 5, description: '财政转移支付', amount: 1500000, type: 'INCOME', date: '2024-01-05', handler: '张会计', remark: '村级办公经费' },
  { id: 6, description: '办公设备采购', amount: 45000, type: 'EXPENSE', date: '2024-01-03', handler: '王支书', remark: '电脑、打印机等' }
])

const resources = ref([
  { id: 1, name: '村集体林地', type: 'forest', area: 500, location: '村西山坡', useType: '林业生产', contractStatus: '已承包', icon: '🌲' },
  { id: 2, name: '鱼塘', type: 'water', area: 30, location: '第三村民组', useType: '水产养殖', contractStatus: '已承包', icon: '🐟' },
  { id: 3, name: '荒地', type: 'land', area: 120, location: '村东南', useType: '待开发', contractStatus: '未承包', icon: '🌾' },
  { id: 4, name: '水库', type: 'water', area: 80, location: '村北', useType: '灌溉、养殖', contractStatus: '已承包', icon: '💧' },
  { id: 5, name: '果园', type: 'land', area: 60, location: '第二村民组', useType: '水果种植', contractStatus: '已承包', icon: '🍎' }
])

const handleBack = () => {
  goBack(router)
}

const goDetail = (id, type) => {
  router.push(`/assets-detail/${id}?type=${type}`)
}

const onTabChange = () => {
  fetchData()
}

const onSearch = () => {
  if (!searchKeyword.value) {
    fetchAssets()
    return
  }
  const filtered = assets.value.filter(item => 
    item.name.includes(searchKeyword.value)
  )
  assets.value = filtered.length > 0 ? filtered : assets.value
  showToast(`搜索到 ${assets.value.length} 条结果`)
}

const onFundTypeSelect = (value) => {
  showToast(`选择类型：${value}`)
}

const getAssetType = (type) => {
  const map = {
    'building': '房屋建筑',
    'equipment': '机械设备',
    'vehicle': '交通工具'
  }
  return map[type] || type
}

const getResourceType = (type) => {
  const map = {
    'land': '耕地',
    'forest': '林地',
    'water': '水域'
  }
  return map[type] || type
}

const fetchAssets = async () => {
  try {
    const res = await request.get('/asset/list')
    assets.value = res.data?.records || assets.value
  } catch (error) {
    console.error('获取资产失败', error)
  }
}

const fetchFundFlow = async () => {
  try {
    const res = await request.get('/asset/fund-flow')
    fundFlows.value = res.data?.records || fundFlows.value
  } catch (error) {
    console.error('获取资金流水失败', error)
  }
}

const fetchResource = async () => {
  try {
    const res = await request.get('/asset/resource')
    resources.value = res.data?.records || resources.value
  } catch (error) {
    console.error('获取资源失败', error)
  }
}

const fetchStatistics = async () => {
  try {
    const res = await request.get('/asset/statistics')
    Object.assign(statistics, res.data)
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

const fetchData = async () => {
  if (activeTab.value === 'asset') {
    fetchAssets()
  } else if (activeTab.value === 'fund') {
    fetchFundFlow()
  } else if (activeTab.value === 'resource') {
    fetchResource()
  }
}

onMounted(() => {
  fetchAssets()
  fetchFundFlow()
  fetchResource()
  fetchStatistics()
})
</script>

<style scoped>
.assets-page {
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
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.stat-value.income {
  color: #07c160;
}

.stat-value.expense {
  color: #ee0a24;
}

.stat-label {
  font-size: 12px;
  color: #969799;
  margin-top: 4px;
}

.table-section {
  margin: 10px 12px;
}

.action-bar {
  padding: 16px 12px;
}

.action-bar .van-button {
  width: 100%;
}
</style>