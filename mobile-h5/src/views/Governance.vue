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

        <div class="card-list">
          <div v-for="item in disputeList" :key="item.id" class="dispute-card" :class="{ urgent: item.status === 'pending' }" @click="goDetail(item.id)">
            <div class="card-header">
              <span class="card-tag" :class="item.type">{{ getTypeLabel(item.type) }}</span>
              <span class="card-time">{{ item.createTime }}</span>
            </div>
            <div class="card-title">{{ item.title }}</div>
            <div class="card-desc">{{ item.description }}</div>
            <div class="card-info">
              <div class="info-item">
                <span>当事人</span>
                <span>{{ item.parties }}</span>
              </div>
              <div class="info-item">
                <span>调解员</span>
                <span>{{ item.mediator }}</span>
              </div>
            </div>
            <div class="card-footer">
              <span class="status-tag" :class="item.status">{{ getStatusLabel(item.status) }}</span>
              <span class="footer-text">查看详情</span>
            </div>
          </div>
        </div>
      </van-tab>

      <van-tab title="平安建设" name="safety">
        <div class="card-list">
          <div v-for="item in safetyList" :key="item.id" class="safety-card">
            <div class="card-header">
              <div class="card-icon">{{ item.icon }}</div>
              <div class="card-title">{{ item.title }}</div>
            </div>
            <div class="card-info">
              <div class="info-row">
                <span>活动时间</span>
                <span>{{ item.date }}</span>
              </div>
              <div class="info-row">
                <span>参与人数</span>
                <span>{{ item.participants }}人</span>
              </div>
              <div class="info-row">
                <span>活动地点</span>
                <span>{{ item.location }}</span>
              </div>
            </div>
            <div class="card-desc">{{ item.description }}</div>
          </div>
        </div>
      </van-tab>

      <van-tab title="网格管理" name="grid">
        <div class="card-list">
          <div v-for="item in gridList" :key="item.id" class="grid-card">
            <div class="card-header">
              <div class="card-avatar">{{ item.name.charAt(0) }}</div>
              <div class="card-info">
                <div class="card-title">{{ item.name }}</div>
                <div class="card-subtitle">{{ item.gridName }}网格员</div>
              </div>
              <span class="card-tag" :class="item.status">{{ item.status }}</span>
            </div>
            <div class="card-detail">
              <div class="detail-row">
                <span>负责片区</span>
                <span>{{ item.area }}</span>
              </div>
              <div class="detail-row">
                <span>联系电话</span>
                <span>{{ item.phone }}</span>
              </div>
              <div class="detail-row">
                <span>管理户数</span>
                <span>{{ item.households }}户</span>
              </div>
            </div>
            <div class="card-actions">
              <van-button size="mini" @click="callPhone(item.phone)">联系</van-button>
            </div>
          </div>
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

const goDetail = (id) => {
  router.push(`/governance-detail/${id}`)
}

const callPhone = (phone) => {
  showToast(`拨打电话: ${phone}`)
}

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

.card-list {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.dispute-card, .safety-card, .grid-card {
  background: #fff;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.dispute-card.urgent {
  border-left: 4px solid #F44336;
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.card-icon {
  font-size: 32px;
  margin-right: 12px;
}

.card-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #607D8B, #455A64);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  margin-right: 12px;
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.card-subtitle {
  font-size: 12px;
  color: #999;
}

.card-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.card-tag.neighbor {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.card-tag.land {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.card-tag.family {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.card-tag.economic {
  background: rgba(156, 39, 176, 0.2);
  color: #9C27B0;
}

.card-tag.在职 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.card-time {
  font-size: 12px;
  color: #999;
  margin-left: auto;
}

.card-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 12px;
}

.card-info {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 4px 0;
  font-size: 13px;
}

.info-item span:first-child {
  color: #999;
}

.info-item span:last-child {
  color: #333;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 13px;
  border-bottom: 1px solid #eee;
}

.info-row:last-child {
  border-bottom: none;
}

.info-row span:first-child {
  color: #999;
}

.info-row span:last-child {
  color: #333;
}

.card-detail {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
  margin-bottom: 12px;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 13px;
  border-bottom: 1px solid #eee;
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-row span:first-child {
  color: #999;
}

.detail-row span:last-child {
  color: #333;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.status-tag.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.status-tag.processing {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.status-tag.resolved {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.status-tag.failed {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.footer-text {
  font-size: 13px;
  color: #D22630;
}

.card-actions {
  display: flex;
  justify-content: flex-end;
}

.action-bar {
  padding: 16px 12px;
}

.action-bar .van-button {
  width: 100%;
}
</style>