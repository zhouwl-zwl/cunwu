<template>
  <div class="subsidy-page page-container">
    <van-nav-bar title="惠民补贴" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="我的补贴" name="my" />
      <van-tab title="全村补贴" name="all" />
      <van-tab title="补贴公示" name="public" />
      <van-tab title="异常名单" name="abnormal" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'my'">
      <div class="stats-card">
        <div class="stat-item">
          <div class="stat-value">{{ myStats.totalAmount.toLocaleString() }}</div>
          <div class="stat-label">累计补贴(元)</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ myStats.totalCount }}</div>
          <div class="stat-label">补贴笔数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ myStats.currentYear }}</div>
          <div class="stat-label">本年到账</div>
        </div>
      </div>

      <div class="filter-bar">
        <van-picker :columns="yearColumns" @confirm="onYearSelect" placeholder="选择年份" />
        <van-picker :columns="monthColumns" @confirm="onMonthSelect" placeholder="选择月份" />
      </div>

      <div class="card-list">
        <div v-for="item in mySubsidyList" :key="item.id" class="subsidy-card">
          <div class="subsidy-header">
            <van-tag type="success" size="mini">{{ item.type }}</van-tag>
            <span class="subsidy-time">{{ item.payTime }}</span>
          </div>
          <div class="subsidy-amount">¥{{ item.amount.toLocaleString() }}</div>
          <div class="subsidy-desc">{{ item.description }}</div>
          <div class="subsidy-detail">
            <div class="detail-row">
              <span>发放银行</span>
              <span>{{ item.bank }}</span>
            </div>
            <div class="detail-row">
              <span>银行卡号</span>
              <span>{{ item.cardNumber }}</span>
            </div>
            <div class="detail-row">
              <span>到账时间</span>
              <span>{{ item.arrivalTime }}</span>
            </div>
          </div>
          <div class="subsidy-status" :class="item.status">{{ getStatusLabel(item.status) }}</div>
        </div>
      </div>

      <div class="action-bar">
        <van-button type="primary" icon="camera" @click="saveScreenshot">截图保存记录</van-button>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'all'">
      <div class="filter-bar">
        <van-picker :columns="yearColumns" @confirm="onYearSelect" placeholder="选择年份" />
        <van-picker :columns="groupColumns" @confirm="onGroupSelect" placeholder="选择村组" />
      </div>

      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showImportModal = true">批量导入</van-button>
        <van-button type="warning" @click="generatePublic">生成公示</van-button>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📊 补贴类型统计</div>
        </div>
        <div class="subsidy-grid">
          <div v-for="item in subsidyTypes" :key="item.type" class="subsidy-type-card" @click="viewTypeDetail(item)">
            <div class="subsidy-icon">{{ item.icon }}</div>
            <div class="subsidy-name">{{ item.type }}</div>
            <div class="subsidy-count">{{ item.count }}人</div>
            <div class="subsidy-total">¥{{ item.totalAmount.toLocaleString() }}</div>
          </div>
        </div>
      </div>

      <div class="card-list">
        <div v-for="item in allSubsidyList" :key="item.id" class="subsidy-card">
          <div class="subsidy-header">
            <span class="subsidy-name-tag">{{ item.name }}</span>
            <span class="subsidy-group">{{ item.group }}</span>
          </div>
          <div class="subsidy-info">
            <div class="subsidy-type-row">
              <span>{{ item.type }}</span>
              <span class="subsidy-amount-sm">¥{{ item.amount.toLocaleString() }}</span>
            </div>
            <div class="subsidy-bank">{{ item.bank }} {{ item.cardNumber }}</div>
          </div>
          <div class="subsidy-status" :class="item.status">{{ getStatusLabel(item.status) }}</div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'public'">
      <div class="card-list">
        <div v-for="item in publicList" :key="item.id" class="public-card" @click="viewPublicDetail(item)">
          <div class="public-header">
            <van-tag :type="getTagType(item.category)" size="mini">{{ item.category }}</van-tag>
            <span class="public-time">{{ item.publishTime }}</span>
          </div>
          <div class="public-title">{{ item.title }}</div>
          <div class="public-summary">{{ item.summary }}</div>
          <div class="public-meta">
            <span>{{ item.viewCount }}次阅读</span>
            <span>{{ item.fileCount }}个附件</span>
          </div>
          <div class="public-actions">
            <van-button size="mini" icon="star" @click.stop="toggleFavorite(item)">收藏</van-button>
            <van-button size="mini" icon="share-o" @click.stop="sharePublic(item)">转发</van-button>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'abnormal'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">⚠️ 异常统计</div>
        </div>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value danger">{{ abnormalStats.total }}</div>
            <div class="stat-label">异常总数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value warning">{{ abnormalStats.bankError }}</div>
            <div class="stat-label">银行卡错误</div>
          </div>
          <div class="stat-item">
            <div class="stat-value warning">{{ abnormalStats.infoError }}</div>
            <div class="stat-label">信息不符</div>
          </div>
        </div>
      </div>

      <div class="action-bar">
        <van-button type="primary" icon="download" @click="exportAbnormal">导出名单</van-button>
      </div>

      <div class="card-list">
        <div v-for="item in abnormalList" :key="item.id" class="abnormal-card">
          <div class="abnormal-header">
            <span class="abnormal-name">{{ item.name }}</span>
            <span class="abnormal-type" :class="item.abnormalType">{{ getAbnormalType(item.abnormalType) }}</span>
          </div>
          <div class="abnormal-info">
            <div class="info-row">
              <span>补贴类型</span>
              <span>{{ item.type }}</span>
            </div>
            <div class="info-row">
              <span>村组</span>
              <span>{{ item.group }}</span>
            </div>
            <div class="info-row">
              <span>问题描述</span>
              <span class="danger">{{ item.description }}</span>
            </div>
            <div class="info-row">
              <span>处理状态</span>
              <span :class="item.status">{{ getStatusLabel(item.status) }}</span>
            </div>
          </div>
          <div class="abnormal-actions">
            <van-button size="mini" @click="handleAbnormal(item)">处理</van-button>
          </div>
        </div>
      </div>
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
const activeTab = ref('my')
const isOfficial = true
const showImportModal = ref(false)

const yearColumns = [
  { text: '全部', value: '' },
  { text: '2024', value: '2024' },
  { text: '2023', value: '2023' },
  { text: '2022', value: '2022' }
]
const monthColumns = [
  { text: '全部', value: '' },
  { text: '1月', value: '1' },
  { text: '2月', value: '2' },
  { text: '3月', value: '3' },
  { text: '4月', value: '4' },
  { text: '5月', value: '5' },
  { text: '6月', value: '6' },
  { text: '7月', value: '7' },
  { text: '8月', value: '8' },
  { text: '9月', value: '9' },
  { text: '10月', value: '10' },
  { text: '11月', value: '11' },
  { text: '12月', value: '12' }
]
const groupColumns = [
  { text: '全部', value: '' },
  { text: '第一村民组', value: '第一村民组' },
  { text: '第二村民组', value: '第二村民组' },
  { text: '第三村民组', value: '第三村民组' }
]

const myStats = reactive({ totalAmount: 15680, totalCount: 8, currentYear: 8600 })

const mySubsidyList = ref([
  { id: 1, type: '耕地地力补贴', amount: 1200, description: '2024年度耕地地力保护补贴', payTime: '2024-06-15', bank: '农业银行', cardNumber: '****1234', arrivalTime: '2024-06-16 09:30', status: 'approved' },
  { id: 2, type: '高龄补贴', amount: 800, description: '2024年第二季度高龄补贴', payTime: '2024-04-20', bank: '农商银行', cardNumber: '****5678', arrivalTime: '2024-04-21 10:15', status: 'approved' },
  { id: 3, type: '残疾人补贴', amount: 1500, description: '2024年度残疾人护理补贴', payTime: '2024-03-10', bank: '农业银行', cardNumber: '****1234', arrivalTime: '2024-03-11 14:20', status: 'approved' },
  { id: 4, type: '产业奖补', amount: 3000, description: '特色产业发展奖励', payTime: '2024-02-28', bank: '农商银行', cardNumber: '****5678', arrivalTime: '2024-03-01 11:00', status: 'approved' },
  { id: 5, type: '临时救助', amount: 2000, description: '困难群众临时救助资金', payTime: '2024-01-15', bank: '农业银行', cardNumber: '****1234', arrivalTime: '', status: 'pending' }
])

const subsidyTypes = ref([
  { type: '低保补贴', icon: '🏠', count: 15, totalAmount: 18000 },
  { type: '耕地补贴', icon: '🌾', count: 86, totalAmount: 103200 },
  { type: '高龄补贴', icon: '👴', count: 23, totalAmount: 27600 },
  { type: '残疾补贴', icon: '♿', count: 8, totalAmount: 12000 },
  { type: '医疗救助', icon: '💊', count: 12, totalAmount: 36000 },
  { type: '临时救助', icon: '🙏', count: 5, totalAmount: 10000 }
])

const allSubsidyList = ref([
  { id: 1, name: '张建国', group: '第一村民组', type: '耕地补贴', amount: 1200, bank: '农业银行', cardNumber: '****1234', status: 'approved' },
  { id: 2, name: '李秀英', group: '第一村民组', type: '高龄补贴', amount: 800, bank: '农商银行', cardNumber: '****5678', status: 'approved' },
  { id: 3, name: '王芳', group: '第二村民组', type: '残疾补贴', amount: 1500, bank: '农业银行', cardNumber: '****9012', status: 'approved' },
  { id: 4, name: '刘大伟', group: '第二村民组', type: '低保补贴', amount: 1200, bank: '农商银行', cardNumber: '****3456', status: 'pending' },
  { id: 5, name: '陈秀英', group: '第三村民组', type: '耕地补贴', amount: 1200, bank: '农业银行', cardNumber: '****7890', status: 'approved' }
])

const publicList = ref([
  { id: 1, title: '2024年耕地地力保护补贴公示', category: '补贴公示', publishTime: '2024-06-10', summary: '根据上级文件要求，现将我村2024年度耕地地力保护补贴发放名单公示如下...', viewCount: 156, fileCount: 3 },
  { id: 2, title: '2024年第一季度低保补贴发放公示', category: '财务公示', publishTime: '2024-04-05', summary: '第一季度低保补贴已发放，请村民查看到账情况...', viewCount: 89, fileCount: 2 },
  { id: 3, title: '高龄老人生活补贴发放通知', category: '政策通知', publishTime: '2024-03-20', summary: '2024年度高龄老人生活补贴申请工作已开始，请符合条件的村民及时申请...', viewCount: 234, fileCount: 1 }
])

const abnormalStats = reactive({ total: 8, bankError: 5, infoError: 3 })

const abnormalList = ref([
  { id: 1, name: '赵刚', group: '第一村民组', type: '耕地补贴', abnormalType: 'bank', description: '银行卡号错误，无法转账', status: 'pending' },
  { id: 2, name: '孙师傅', group: '第二村民组', type: '低保补贴', abnormalType: 'info', description: '身份证信息与系统不符', status: 'pending' },
  { id: 3, name: '周大妈', group: '第三村民组', type: '高龄补贴', abnormalType: 'bank', description: '银行卡已注销', status: 'processing' },
  { id: 4, name: '吴大爷', group: '第一村民组', type: '残疾补贴', abnormalType: 'info', description: '残疾证号码错误', status: 'pending' }
])

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'my') {
      const res = await request.get('/subsidy/my')
      mySubsidyList.value = res.list || res.data?.list || mySubsidyList.value
      myStats.totalAmount = res.stats?.totalAmount || res.data?.stats?.totalAmount || myStats.totalAmount
      myStats.totalCount = res.stats?.totalCount || res.data?.stats?.totalCount || myStats.totalCount
    } else if (activeTab.value === 'all') {
      const res = await request.get('/subsidy/all')
      subsidyTypes.value = res.types || res.data?.types || subsidyTypes.value
      allSubsidyList.value = res.list || res.data?.list || allSubsidyList.value
    } else if (activeTab.value === 'public') {
      const res = await request.get('/subsidy/public')
      publicList.value = res.data || publicList.value
    } else if (activeTab.value === 'abnormal') {
      const res = await request.get('/subsidy/abnormal')
      abnormalList.value = res.list || res.data?.list || abnormalList.value
      abnormalStats.total = res.stats?.total || res.data?.stats?.total || abnormalStats.total
      abnormalStats.bankError = res.stats?.bankError || res.data?.stats?.bankError || abnormalStats.bankError
      abnormalStats.infoError = res.stats?.infoError || res.data?.stats?.infoError || abnormalStats.infoError
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const onYearSelect = (value) => {
  showToast(`选择年份：${value}`)
}

const onMonthSelect = (value) => {
  showToast(`选择月份：${value}`)
}

const onGroupSelect = (value) => {
  showToast(`选择村组：${value}`)
}

const getStatusLabel = (status) => {
  const map = {
    'pending': '待发放',
    'approved': '已发放',
    'rejected': '已驳回',
    'processing': '处理中'
  }
  return map[status] || status
}

const getTagType = (category) => {
  const map = {
    '补贴公示': 'primary',
    '财务公示': 'warning',
    '政策通知': 'success'
  }
  return map[category] || 'default'
}

const getAbnormalType = (type) => {
  const map = {
    'bank': '银行卡错误',
    'info': '信息不符'
  }
  return map[type] || type
}

const saveScreenshot = () => {
  showToast('截图已保存到相册')
}

const viewTypeDetail = (item) => {
  showToast(`查看${item.type}详情`)
}

const generatePublic = () => {
  showToast('补贴公示已生成，同步至通知公告')
}

const viewPublicDetail = (item) => {
  showToast(`查看${item.title}详情`)
}

const toggleFavorite = (item) => {
  showToast('已收藏')
}

const sharePublic = (item) => {
  showToast('已复制链接，可转发至微信群')
}

const exportAbnormal = () => {
  showToast('异常名单已导出')
}

const handleAbnormal = (item) => {
  showToast(`处理${item.name}的异常`)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.subsidy-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.card {
  background: #fff;
  margin: 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  margin-bottom: 14px;
}

.header-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
}

.action-bar {
  display: flex;
  gap: 10px;
  padding: 0 12px;
  margin-bottom: 12px;
}

.action-bar .van-button {
  flex: 1;
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

.stats-card {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin: 12px;
  padding: 16px;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  border-radius: 14px;
  color: #fff;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-value.danger {
  color: #F44336;
}

.stat-value.warning {
  color: #FF9800;
}

.stat-label {
  font-size: 12px;
  opacity: 0.85;
}

.subsidy-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.subsidy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.subsidy-time {
  font-size: 12px;
  color: #999;
}

.subsidy-name-tag {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.subsidy-group {
  font-size: 12px;
  color: #999;
  padding: 4px 10px;
  background: #f0f0f0;
  border-radius: 10px;
}

.subsidy-amount {
  font-size: 24px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 6px;
}

.subsidy-amount-sm {
  font-size: 16px;
  font-weight: bold;
  color: #D22630;
}

.subsidy-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.subsidy-detail {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 10px;
}

.subsidy-type-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
  font-size: 14px;
}

.subsidy-type-row span:first-child {
  color: #666;
}

.subsidy-bank {
  font-size: 12px;
  color: #999;
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

.detail-row span:last-child.danger {
  color: #F44336;
}

.subsidy-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  display: inline-block;
}

.subsidy-status.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.subsidy-status.approved {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.subsidy-status.processing {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.subsidy-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.subsidy-type-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 14px 8px;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  border-radius: 12px;
  color: #fff;
}

.subsidy-icon {
  font-size: 24px;
  margin-bottom: 6px;
}

.subsidy-name {
  font-size: 12px;
  font-weight: bold;
  margin-bottom: 4px;
}

.subsidy-count {
  font-size: 11px;
  opacity: 0.85;
  margin-bottom: 2px;
}

.subsidy-total {
  font-size: 14px;
  font-weight: bold;
}

.public-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.public-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.public-time {
  font-size: 12px;
  color: #999;
}

.public-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.public-summary {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.public-meta {
  font-size: 12px;
  color: #999;
  margin-bottom: 12px;
}

.public-meta span {
  margin-right: 16px;
}

.public-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.abnormal-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
  border-left: 4px solid #F44336;
}

.abnormal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.abnormal-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.abnormal-type {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.abnormal-type.bank {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.abnormal-type.info {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.abnormal-info {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 12px;
}

.abnormal-actions {
  display: flex;
  justify-content: flex-end;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.no-permission-card {
  margin: 40px 12px;
  padding: 40px 20px;
  background: #fff;
  border-radius: 14px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.no-permission-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.no-permission-text {
  font-size: 14px;
  color: #999;
}
</style>