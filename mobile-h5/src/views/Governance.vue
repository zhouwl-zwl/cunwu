<template>
  <div class="governance-page page-container">
    <van-nav-bar title="综治调解" left-arrow @click-left="handleBack" />

    <div class="stats-card">
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalCases }}</div>
        <div class="stat-label">纠纷案件</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.resolvedCases }}</div>
        <div class="stat-label">已调解</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.successRate }}%</div>
        <div class="stat-label">成功率</div>
      </div>
    </div>

    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="矛盾纠纷" name="dispute">
        <div class="filter-bar">
          <van-picker :columns="statusColumns" @confirm="onStatusSelect" placeholder="处理状态" />
          <van-picker :columns="typeColumns" @confirm="onTypeSelect" placeholder="纠纷类型" />
        </div>

        <div class="table-section">
          <ExcelTable 
            :data="disputeList" 
            :columns="disputeColumns" 
            export-filename="矛盾纠纷.xlsx"
          />
        </div>
      </van-tab>

      <van-tab title="平安建设" name="safety">
        <div class="table-section">
          <ExcelTable 
            :data="safetyList" 
            :columns="safetyColumns" 
            export-filename="平安建设.xlsx"
          />
        </div>
      </van-tab>

      <van-tab title="网格管理" name="grid">
        <div class="table-section">
          <ExcelTable 
            :data="gridList" 
            :columns="gridColumns" 
            export-filename="网格管理.xlsx"
          />
        </div>
      </van-tab>
    </van-tabs>

    <div v-if="isOfficial" class="action-bar">
      <van-button type="primary" icon="plus" @click="showAddModal = true">新增纠纷案件</van-button>
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
const activeTab = ref('dispute')
const isOfficial = true
const showAddModal = ref(false)

const statusColumns = [
  { text: '全部', value: '' },
  { text: '待调解', value: 'pending' },
  { text: '调解中', value: 'processing' },
  { text: '已达成协议', value: 'resolved' },
  { text: '未达成协议', value: 'failed' }
]
const typeColumns = [
  { text: '全部', value: '' },
  { text: '邻里纠纷', value: 'neighbor' },
  { text: '土地纠纷', value: 'land' },
  { text: '婚姻家庭', value: 'family' },
  { text: '经济纠纷', value: 'economic' }
]

const stats = reactive({
  totalCases: 25,
  resolvedCases: 23,
  successRate: 92
})

const disputeList = ref([
  { id: 1, title: '邻里噪音扰民纠纷', type: 'neighbor', description: '张姓居民反映邻居夜间噪音过大，影响正常休息', parties: '张某 vs 李某', mediator: '王支书', createTime: '2024-01-15', status: 'pending' },
  { id: 2, title: '土地边界争议', type: 'land', description: '两户村民因宅基地边界问题产生纠纷', parties: '刘某 vs 赵某', mediator: '李主任', createTime: '2024-01-12', status: 'processing' },
  { id: 3, title: '家庭财产分割纠纷', type: 'family', description: '兄弟三人因父母遗产分割产生矛盾', parties: '王大、王二、王三', mediator: '张主任', createTime: '2024-01-10', status: 'resolved' },
  { id: 4, title: '承包款拖欠纠纷', type: 'economic', description: '承包商拖欠村民土地承包款', parties: '村民集体 vs 承包商', mediator: '陈书记', createTime: '2024-01-08', status: 'resolved' },
  { id: 5, title: '房屋采光纠纷', type: 'neighbor', description: '新建房屋影响邻居采光', parties: '孙某 vs 周某', mediator: '王支书', createTime: '2024-01-05', status: 'processing' }
])

const safetyList = ref([
  { id: 1, title: '安全知识讲座', icon: '📚', date: '2024-01-20', participants: 45, location: '村文化广场', description: '邀请派出所民警开展防诈骗、消防安全知识讲座' },
  { id: 2, title: '夜间巡逻', icon: '🚓', date: '2024-01-18', participants: 12, location: '全村范围', description: '组织党员志愿者开展夜间治安巡逻' },
  { id: 3, title: '矛盾排查', icon: '🔍', date: '2024-01-15', participants: 8, location: '各村民组', description: '网格员入户排查矛盾纠纷隐患' },
  { id: 4, title: '消防安全演练', icon: '🔥', date: '2024-01-10', participants: 60, location: '村小学', description: '联合消防中队开展消防演练' }
])

const gridList = ref([
  { id: 1, name: '王组长', gridName: '第一网格', area: '第一、二村民组', phone: '138****1111', households: 85, status: '在职' },
  { id: 2, name: '李组长', gridName: '第二网格', area: '第三、四村民组', phone: '139****2222', households: 78, status: '在职' },
  { id: 3, name: '张组长', gridName: '第三网格', area: '第五、六村民组', phone: '137****3333', households: 92, status: '在职' }
])

const handleBack = () => {
  goBack(router)
}

const disputeColumns = [
  { key: 'title', title: '纠纷标题' },
  { key: 'type', title: '纠纷类型', formatter: (val) => getTypeLabel(val) },
  { key: 'parties', title: '当事人' },
  { key: 'mediator', title: '调解员' },
  { key: 'createTime', title: '创建时间' },
  { key: 'status', title: '状态', formatter: (val) => getStatusLabel(val) }
]

const safetyColumns = [
  { key: 'title', title: '活动名称' },
  { key: 'date', title: '活动时间' },
  { key: 'participants', title: '参与人数', formatter: (val) => `${val || 0}人` },
  { key: 'location', title: '活动地点' },
  { key: 'description', title: '活动描述' }
]

const gridColumns = [
  { key: 'name', title: '姓名' },
  { key: 'gridName', title: '网格名称' },
  { key: 'area', title: '负责片区' },
  { key: 'phone', title: '联系电话' },
  { key: 'households', title: '管理户数', formatter: (val) => `${val || 0}户` },
  { key: 'status', title: '状态' }
]

const onTabChange = () => {
  fetchData()
}

const onStatusSelect = (value) => {
  showToast(`选择状态：${value}`)
}

const onTypeSelect = (value) => {
  showToast(`选择类型：${value}`)
}

const getTypeLabel = (type) => {
  const map = {
    'neighbor': '邻里纠纷',
    'land': '土地纠纷',
    'family': '婚姻家庭',
    'economic': '经济纠纷'
  }
  return map[type] || type
}

const getStatusLabel = (status) => {
  const map = {
    'pending': '待调解',
    'processing': '调解中',
    'resolved': '已达成协议',
    'failed': '未达成协议'
  }
  return map[status] || status
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'dispute') {
      const res = await request.get('/governance/disputes')
      disputeList.value = res.data?.records || disputeList.value
    } else if (activeTab.value === 'safety') {
      const res = await request.get('/governance/safety')
      safetyList.value = res.data?.records || safetyList.value
    } else if (activeTab.value === 'grid') {
      const res = await request.get('/governance/grid')
      gridList.value = res.data?.records || gridList.value
    }
  } catch (error) {
    console.error('获取综治调解数据失败', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.governance-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.table-section {
  margin: 10px 12px;
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
  display: flex;
  gap: 10px;
  padding: 0 12px;
  margin-bottom: 12px;
}

.filter-bar .van-picker {
  flex: 1;
}

.action-bar {
  padding: 16px 12px;
}

.action-bar .van-button {
  width: 100%;
}
</style>