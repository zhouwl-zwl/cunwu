<template>
  <div class="land-page page-container">
    <van-nav-bar title="土地管理" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="我的宅基地" name="homestead" />
      <van-tab title="承包耕地" name="farmland" />
      <van-tab title="土地流转" name="transfer" />
      <van-tab title="违建巡查" name="violation" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'homestead'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">🏠 我的宅基地信息</div>
          <van-button type="primary" size="mini" @click="showApplyModal = true">申请翻建</van-button>
        </div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">地块编号</span>
            <span class="info-value">{{ homesteadInfo.code }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">面积</span>
            <span class="info-value">{{ homesteadInfo.area }}㎡</span>
          </div>
          <div class="info-item">
            <span class="info-label">四至</span>
            <span class="info-value">{{ homesteadInfo.boundary }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">审批时间</span>
            <span class="info-value">{{ homesteadInfo.approveTime }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">房屋层数</span>
            <span class="info-value">{{ homesteadInfo.floors }}层</span>
          </div>
          <div class="info-item">
            <span class="info-label">使用状态</span>
            <span class="info-value">{{ homesteadInfo.status }}</span>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📄 确权证书</div>
        </div>
        <div class="certificate-item" @click="previewCertificate">
          <img :src="homesteadInfo.certificateUrl" alt="确权证书" />
          <div class="certificate-overlay">点击查看</div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📋 审批进度</div>
        </div>
        <div class="progress-list">
          <div v-for="(item, index) in approvalProgress" :key="index" class="progress-item">
            <div class="progress-icon" :class="{ done: item.done, current: item.current }">
              {{ item.icon }}
            </div>
            <div class="progress-info">
              <div class="progress-title">{{ item.title }}</div>
              <div class="progress-time">{{ item.time }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'farmland'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">🌾 我的耕地信息</div>
        </div>
        <div class="farmland-list">
          <div v-for="item in farmlandList" :key="item.id" class="farmland-card">
            <div class="farmland-header">
              <span class="farmland-code">{{ item.code }}</span>
              <span class="farmland-status">{{ item.status }}</span>
            </div>
            <div class="farmland-info">
              <div class="farmland-area">面积：{{ item.area }}亩</div>
              <div class="farmland-crop">种植作物：{{ item.crop }}</div>
              <div class="farmland-subsidy">地力补贴：¥{{ item.subsidy }}/年</div>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📑 流转合同</div>
        </div>
        <div v-if="contractList.length === 0" class="empty-state">
          <div class="empty-icon">📝</div>
          <div class="empty-text">暂无流转合同</div>
        </div>
        <div v-else class="contract-list">
          <div v-for="item in contractList" :key="item.id" class="contract-item">
            <div class="contract-info">
              <div class="contract-title">{{ item.title }}</div>
              <div class="contract-term">期限：{{ item.startDate }} - {{ item.endDate }}</div>
              <div class="contract-rent">租金：¥{{ item.rent }}/年</div>
            </div>
            <div class="contract-action">
              <van-button size="mini" @click="viewContract(item)">查看</van-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'transfer'">
      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showAddTransferModal = true">新增流转</van-button>
      </div>

      <div class="card-list">
        <div v-for="item in transferList" :key="item.id" class="transfer-card">
          <div class="transfer-header">
            <span class="transfer-code">{{ item.code }}</span>
            <span class="transfer-status" :class="item.status">{{ getStatusLabel(item.status) }}</span>
          </div>
          <div class="transfer-info">
            <div class="transfer-parties">
              <span>出让方：{{ item.seller }}</span>
              <span>受让方：{{ item.buyer }}</span>
            </div>
            <div class="transfer-detail">
              <span>面积：{{ item.area }}亩</span>
              <span>租金：¥{{ item.rent }}/年</span>
            </div>
            <div class="transfer-term">期限：{{ item.startDate }} - {{ item.endDate }}</div>
          </div>
          <div class="transfer-actions">
            <van-button size="mini" @click="viewContract(item)">合同</van-button>
            <van-button size="mini" @click="viewPayments(item)">租金明细</van-button>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'violation'">
      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showAddViolationModal = true">新增违建</van-button>
      </div>

      <div class="filter-bar">
        <van-picker :columns="riskColumns" @confirm="onRiskSelect" placeholder="风险等级" />
        <van-picker :columns="statusColumns" @confirm="onStatusSelect" placeholder="处理状态" />
      </div>

      <div class="card-list">
        <div v-for="item in violationList" :key="item.id" class="violation-card" :class="{ urgent: item.riskLevel === 'high' }">
          <div class="violation-header">
            <span class="risk-tag" :class="item.riskLevel">{{ getRiskLabel(item.riskLevel) }}</span>
            <span class="violation-time">{{ item.discoverTime }}</span>
          </div>
          <div class="violation-info">
            <div class="violation-location">{{ item.location }}</div>
            <div class="violation-detail">占地面积：{{ item.area }}㎡</div>
          </div>
          <div class="violation-status" :class="item.status">{{ getViolationStatus(item.status) }}</div>
          <div class="violation-photos">
            <img v-for="(photo, idx) in item.photos" :key="idx" :src="photo" class="violation-photo" />
          </div>
        </div>
      </div>
    </div>

    <van-dialog v-model:show="showApplyModal" title="宅基地翻建申请" @confirm="submitApply">
      <van-form>
        <van-field v-model="applyForm.type" label="申请类型" placeholder="翻建/新建" />
        <van-field v-model="applyForm.reason" label="申请原因" placeholder="请输入申请原因" />
        <van-field v-model="applyForm.floors" label="拟建房层数" type="number" placeholder="请输入层数" />
      </van-form>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast, showDialog } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const activeTab = ref('homestead')
const showApplyModal = ref(false)
const showAddTransferModal = ref(false)
const showAddViolationModal = ref(false)

const homesteadInfo = ref({
  code: 'HD-2024-0015',
  area: 120,
  boundary: '东至张三家，西至李四家，南至村路，北至农田',
  approveTime: '2020-06-15',
  floors: 2,
  status: '正常使用',
  certificateUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chinese%20land%20certificate%20document%20official%20paper&image_size=portrait_4_3'
})

const approvalProgress = ref([
  { icon: '📝', title: '申请提交', time: '2024-01-10', done: true, current: false },
  { icon: '👀', title: '村两委初审', time: '2024-01-12', done: true, current: false },
  { icon: '🏛️', title: '乡镇审批', time: '2024-01-15', done: false, current: true },
  { icon: '✅', title: '审批通过', time: '', done: false, current: false }
])

const farmlandList = ref([
  { id: 1, code: 'GD-001', area: 5.2, crop: '小麦', subsidy: 624, status: '已确权' },
  { id: 2, code: 'GD-002', area: 3.8, crop: '玉米', subsidy: 456, status: '已确权' },
  { id: 3, code: 'GD-003', area: 2.5, crop: '花生', subsidy: 300, status: '已确权' }
])

const contractList = ref([
  { id: 1, title: '耕地流转合同', startDate: '2024-01-01', endDate: '2029-12-31', rent: 2000 }
])

const transferList = ref([
  { id: 1, code: 'LZ-2024-001', seller: '张建国', buyer: '农业合作社', area: 10, rent: 5000, startDate: '2024-01-01', endDate: '2029-12-31', status: 'active' },
  { id: 2, code: 'LZ-2024-002', seller: '李志强', buyer: '种植大户', area: 8, rent: 4000, startDate: '2024-03-01', endDate: '2029-02-28', status: 'active' }
])

const violationList = ref([
  { id: 1, location: '第一村民组15号南侧', area: 30, riskLevel: 'high', status: 'pending', discoverTime: '2024-01-10', photos: [] },
  { id: 2, location: '第二村民组8号东侧', area: 20, riskLevel: 'medium', status: 'processing', discoverTime: '2024-01-08', photos: [] },
  { id: 3, location: '第三村民组20号北侧', area: 15, riskLevel: 'low', status: 'resolved', discoverTime: '2024-01-05', photos: [] }
])

const riskColumns = [
  { text: '全部', value: '' },
  { text: '高风险', value: 'high' },
  { text: '中风险', value: 'medium' },
  { text: '低风险', value: 'low' }
]
const statusColumns = [
  { text: '全部', value: '' },
  { text: '待处理', value: 'pending' },
  { text: '整改中', value: 'processing' },
  { text: '已完成', value: 'resolved' }
]

const applyForm = ref({ type: '', reason: '', floors: '' })

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'transfer') {
      const res = await request.get('/land/transfer')
      transferList.value = res.data || transferList.value
    } else if (activeTab.value === 'violation') {
      const res = await request.get('/land/violation')
      violationList.value = res.data || violationList.value
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const previewCertificate = () => {
  showDialog({
    title: '确权证书',
    message: `<img src="${homesteadInfo.value.certificateUrl}" style="width:100%" />`,
    confirmButtonText: '知道了',
    showCancelButton: false
  })
}

const submitApply = () => {
  showToast('申请已提交，等待审核')
  showApplyModal.value = false
}

const viewContract = (item) => {
  showToast('查看合同功能开发中')
}

const viewPayments = (item) => {
  showToast('查看租金明细功能开发中')
}

const getStatusLabel = (status) => {
  const map = {
    'active': '执行中',
    'expired': '已到期',
    'pending': '待审核'
  }
  return map[status] || status
}

const getRiskLabel = (level) => {
  const map = {
    'high': '高风险',
    'medium': '中风险',
    'low': '低风险'
  }
  return map[level] || level
}

const getViolationStatus = (status) => {
  const map = {
    'pending': '待处理',
    'processing': '整改中',
    'resolved': '已完成',
    'overdue': '逾期未整改'
  }
  return map[status] || status
}

const onRiskSelect = (value) => {
  showToast(`选择风险等级：${value}`)
}

const onStatusSelect = (value) => {
  showToast(`选择状态：${value}`)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.land-page {
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.header-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.info-item {
  display: flex;
  flex-direction: column;
  padding: 10px;
  background: #fafafa;
  border-radius: 10px;
}

.info-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.info-value {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.certificate-item {
  position: relative;
  border-radius: 10px;
  overflow: hidden;
}

.certificate-item img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.certificate-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  padding: 8px;
  text-align: center;
  font-size: 13px;
}

.progress-list {
  padding-left: 10px;
}

.progress-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px dashed #eee;
}

.progress-item:last-child {
  border-bottom: none;
}

.progress-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #eee;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  margin-right: 12px;
}

.progress-icon.done {
  background: #4CAF50;
  color: #fff;
}

.progress-icon.current {
  background: #D22630;
  color: #fff;
}

.progress-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.progress-time {
  font-size: 12px;
  color: #999;
}

.farmland-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.farmland-card {
  padding: 12px;
  background: #fafafa;
  border-radius: 10px;
}

.farmland-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.farmland-code {
  font-size: 12px;
  color: #D22630;
  background: rgba(210, 38, 48, 0.1);
  padding: 4px 10px;
  border-radius: 10px;
}

.farmland-status {
  font-size: 12px;
  color: #4CAF50;
}

.farmland-area {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.farmland-crop, .farmland-subsidy {
  font-size: 12px;
  color: #666;
  margin-bottom: 2px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.empty-text {
  font-size: 14px;
  color: #999;
}

.contract-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.contract-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #fafafa;
  border-radius: 10px;
}

.contract-title {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.contract-term, .contract-rent {
  font-size: 12px;
  color: #666;
}

.action-bar {
  display: flex;
  padding: 0 12px;
  margin-bottom: 12px;
}

.action-bar .van-button {
  flex: 1;
}

.filter-bar {
  display: flex;
  gap: 10px;
  padding: 12px;
  background: #fff;
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

.transfer-card, .violation-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.violation-card.urgent {
  border-left: 4px solid #F44336;
}

.transfer-header, .violation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.transfer-code, .risk-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.transfer-code {
  color: #D22630;
  background: rgba(210, 38, 48, 0.1);
}

.risk-tag.high {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.risk-tag.medium {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.risk-tag.low {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.transfer-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.transfer-status.active {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.transfer-status.expired {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.violation-time {
  font-size: 12px;
  color: #999;
}

.transfer-parties {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-bottom: 8px;
}

.transfer-parties span {
  font-size: 13px;
  color: #333;
}

.transfer-detail {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
}

.transfer-detail span {
  font-size: 13px;
  color: #666;
}

.transfer-term {
  font-size: 12px;
  color: #999;
}

.transfer-actions {
  display: flex;
  gap: 10px;
  margin-top: 12px;
}

.violation-location {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.violation-detail {
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
}

.violation-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  display: inline-block;
}

.violation-status.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.violation-status.processing {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.violation-status.resolved {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.violation-status.overdue {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.violation-photos {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}

.violation-photo {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  object-fit: cover;
}
</style>
