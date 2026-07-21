<template>
  <div class="traffic-management-page page-container">
    <van-nav-bar title="交通治理" left-arrow @click-left="onBack" />

    <div class="stat-card">
      <div class="stat-item">
        <div class="stat-value">{{ stat.total || 0 }}</div>
        <div class="stat-label">道路总条数</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stat.maintained || 0 }}</div>
        <div class="stat-label">已维护</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stat.pending || 0 }}</div>
        <div class="stat-label">待维护</div>
      </div>
    </div>

    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="道路台账" name="roads" />
      <van-tab title="养护记录" name="maintenance" />
      <van-tab title="交通安全" name="safety" />
    </van-tabs>

    <template v-if="activeTab === 'roads'">
      <div class="table-section">
        <ExcelTable 
          :data="roadsData" 
          :columns="roadsColumns" 
          export-filename="道路台账.xlsx"
        />
      </div>
    </template>

    <template v-else-if="activeTab === 'maintenance'">
      <div class="table-section">
        <ExcelTable 
          :data="maintenanceData" 
          :columns="maintenanceColumns" 
          export-filename="养护记录.xlsx"
        />
      </div>
    </template>

    <template v-else>
      <div class="table-section">
        <ExcelTable 
          :data="safetyData" 
          :columns="safetyColumns" 
          export-filename="交通安全.xlsx"
        />
      </div>
    </template>

    <van-empty v-if="!loading && !list.length" description="暂无数据" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeTab = ref('roads')
const stat = reactive({
  total: 0,
  maintained: 0,
  pending: 0
})

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const roadsColumns = [
  { key: 'name', title: '道路名称' },
  { key: 'length', title: '长度(公里)', formatter: (val) => `${val || 0}公里` },
  { key: 'width', title: '宽度(米)', formatter: (val) => `${val || 0}米` },
  { key: 'type', title: '道路类型', formatter: (val) => val || '村级道路' },
  { key: 'status', title: '状况', formatter: (val) => val === 'GOOD' ? '良好' : val === 'NORMAL' ? '一般' : '较差' },
  { key: 'lastMaintenance', title: '上次维护', formatter: (val) => formatTime(val) }
]

const maintenanceColumns = [
  { key: 'roadName', title: '道路名称' },
  { key: 'type', title: '养护类型', formatter: (val) => val || '日常养护' },
  { key: 'content', title: '养护内容' },
  { key: 'cost', title: '费用(元)', formatter: (val) => `${val || 0}元` },
  { key: 'person', title: '负责人' },
  { key: 'maintenanceDate', title: '养护日期', formatter: (val) => formatTime(val) }
]

const safetyColumns = [
  { key: 'roadName', title: '道路名称' },
  { key: 'type', title: '隐患类型', formatter: (val) => val || '道路损坏' },
  { key: 'location', title: '具体位置' },
  { key: 'level', title: '等级', formatter: (val) => val === 'HIGH' ? '高' : val === 'MEDIUM' ? '中' : '低' },
  { key: 'status', title: '状态', formatter: (val) => val === 'PENDING' ? '待处理' : val === 'PROCESSING' ? '处理中' : '已完成' },
  { key: 'discoverDate', title: '发现日期', formatter: (val) => formatTime(val) }
]

const roadsData = computed(() => list.value.filter(item => item.category === 'road'))
const maintenanceData = computed(() => list.value.filter(item => item.category === 'maintenance'))
const safetyData = computed(() => list.value.filter(item => item.category === 'safety'))

const fetchData = async () => {
  loading.value = true
  try {
    const url = '/traffic-management/list'
    const res = await request.get(url, { params: { page: 1, size: 20 } })
    list.value = res.records || res.data?.records || res || []
  } catch (error) {
    console.error('获取交通治理数据失败', error)
  } finally {
    loading.value = false
  }
}

const fetchStat = async () => {
  try {
    const res = await request.get('/traffic-management/stat')
    Object.assign(stat, res.data)
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

onMounted(() => {
  fetchStat()
  fetchData()
})
</script>

<style scoped>
.traffic-management-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.stat-card {
  display: flex;
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  margin: 10px 12px;
  border-radius: 14px;
  padding: 16px 0;
}

.stat-item {
  flex: 1;
  text-align: center;
  color: #fff;
  border-right: 1px solid rgba(255, 255, 255, 0.2);
}

.stat-item:last-child {
  border-right: none;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.9;
}

.table-section {
  margin: 10px 12px;
}
</style>