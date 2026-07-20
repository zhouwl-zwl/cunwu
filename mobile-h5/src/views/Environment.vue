<template>
  <div class="environment-page page-container">
    <van-nav-bar title="人居环境" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="环境巡查" name="inspection" />
      <van-tab title="美丽庭院" name="courtyard" />
      <van-tab title="保洁员管理" name="cleaner" />
      <van-tab title="环卫设施" name="facility" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'inspection'">
      <div class="action-bar" v-if="isOfficial">
        <van-button type="primary" icon="plus" @click="showAddInspectionModal = true">新增巡查</van-button>
      </div>

      <div class="filter-bar">
        <van-picker :columns="statusColumns" @confirm="onStatusSelect" placeholder="处理状态" />
        <van-picker :columns="typeColumns" @confirm="onTypeSelect" placeholder="问题类型" />
      </div>

      <div class="card-list">
        <div v-for="item in inspectionList" :key="item.id" class="inspection-card" :class="{ urgent: item.status === 'overdue' }">
          <div class="inspection-header">
            <span class="inspection-tag" :class="item.type">{{ getTypeLabel(item.type) }}</span>
            <span class="inspection-time">{{ item.discoverTime }}</span>
          </div>
          <div class="inspection-location">{{ item.location }}</div>
          <div class="inspection-desc">{{ item.description }}</div>
          <div class="inspection-status" :class="item.status">{{ getStatusLabel(item.status) }}</div>
          
          <div class="photos-section">
            <div class="photos-title">问题照片</div>
            <div class="inspection-photos">
              <img v-for="(photo, idx) in item.photos" :key="idx" :src="photo" class="photo-item" @click="previewImage(photo)" />
              <div v-if="item.photos.length === 0" class="no-photo">暂无照片</div>
            </div>
          </div>

          <div v-if="item.beforePhoto || item.afterPhoto" class="compare-section">
            <div class="photos-title">整改对比</div>
            <div class="compare-row">
              <div class="compare-item">
                <div class="compare-label">整改前</div>
                <img v-if="item.beforePhoto" :src="item.beforePhoto" class="compare-photo" @click="previewImage(item.beforePhoto)" />
                <div v-else class="no-photo">暂无</div>
              </div>
              <div class="compare-arrow">→</div>
              <div class="compare-item">
                <div class="compare-label">整改后</div>
                <img v-if="item.afterPhoto" :src="item.afterPhoto" class="compare-photo" @click="previewImage(item.afterPhoto)" />
                <div v-else class="no-photo">暂无</div>
              </div>
            </div>
          </div>

          <div v-if="item.deadline && item.status !== 'resolved'" class="deadline-info">
            <span class="deadline-label">整改时限：</span>
            <span :class="{ warning: isOverdue(item.deadline) }">{{ item.deadline }}</span>
          </div>

          <div v-if="isOfficial" class="inspection-actions">
            <van-button v-if="item.status === 'pending'" size="mini" type="primary" @click="handleInspection(item)">处理</van-button>
            <van-button v-if="item.status === 'processing'" size="mini" type="success" @click="completeInspection(item)">完成整改</van-button>
            <van-button v-if="item.status === 'resolved'" size="mini" @click="viewDetail(item)">查看详情</van-button>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'courtyard'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">🏡 本月评比结果</div>
          <div class="month-select">2024年1月</div>
        </div>
        <div class="result-tabs">
          <div class="result-tab" :class="{ active: courtyardTab === 'red' }" @click="courtyardTab = 'red'">红榜</div>
          <div class="result-tab" :class="{ active: courtyardTab === 'black' }" @click="courtyardTab = 'black'">黑榜</div>
        </div>
        
        <div class="courtyard-list">
          <div v-for="item in courtyardList" :key="item.id" class="courtyard-card" :class="{ black: courtyardTab === 'black' }">
            <div class="courtyard-rank">{{ item.rank === '优秀' ? '🏆' : '⚠️' }}</div>
            <div class="courtyard-photo">
              <img :src="item.photo" :alt="item.name" />
            </div>
            <div class="courtyard-info">
              <div class="courtyard-name">{{ item.name }}</div>
              <div class="courtyard-group">{{ item.group }}</div>
              <div class="courtyard-score">评分：{{ item.score }}分</div>
              <div class="courtyard-comment">{{ item.comment }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📊 评比统计</div>
        </div>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">{{ stats.excellent }}</div>
            <div class="stat-label">优秀庭院</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.good }}</div>
            <div class="stat-label">良好庭院</div>
          </div>
          <div class="stat-item">
            <div class="stat-value warning">{{ stats.poor }}</div>
            <div class="stat-label">脏乱庭院</div>
          </div>
        </div>
      </div>

      <div v-if="isOfficial" class="action-bar">
        <van-button type="primary" icon="plus" @click="showEvaluateModal = true">新增评比</van-button>
        <van-button type="warning" @click="generateNotice">生成公示</van-button>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'cleaner'">
      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showAddCleanerModal = true">新增保洁员</van-button>
      </div>

      <div class="card-list">
        <div v-for="item in cleanerList" :key="item.id" class="cleaner-card">
          <div class="cleaner-header">
            <div class="cleaner-avatar">{{ item.name.charAt(0) }}</div>
            <div class="cleaner-info">
              <div class="cleaner-name">{{ item.name }}</div>
              <div class="cleaner-phone">{{ item.phone }}</div>
            </div>
            <div class="cleaner-status" :class="item.status">{{ item.status }}</div>
          </div>
          <div class="cleaner-detail">
            <div class="detail-row">
              <span>负责片区</span>
              <span>{{ item.area }}</span>
            </div>
            <div class="detail-row">
              <span>入职时间</span>
              <span>{{ item.hireDate }}</span>
            </div>
            <div class="detail-row">
              <span>本月出勤</span>
              <span>{{ item.attendance }}天</span>
            </div>
            <div class="detail-row">
              <span>本月工资</span>
              <span>¥{{ item.salary }}</span>
            </div>
            <div class="detail-row">
              <span>工资发放</span>
              <span :class="{ warning: item.salaryStatus === '待发放' }">{{ item.salaryStatus }}</span>
            </div>
          </div>
          <div class="cleaner-actions">
            <van-button size="mini" @click="viewCleanerDetail(item)">查看详情</van-button>
            <van-button size="mini" type="primary" @click="editCleaner(item)">编辑</van-button>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'facility'">
      <div class="search-bar">
        <van-search v-model="searchKeyword" placeholder="搜索设施位置" @search="onSearch" />
      </div>

      <div class="card-list">
        <div v-for="item in facilityList" :key="item.id" class="facility-card">
          <div class="facility-header">
            <span class="facility-icon">{{ item.icon }}</span>
            <span class="facility-type" :class="item.type">{{ getFacilityType(item.type) }}</span>
          </div>
          <div class="facility-location">{{ item.location }}</div>
          <div class="facility-detail">
            <div class="detail-row">
              <span>数量</span>
              <span>{{ item.quantity }}个</span>
            </div>
            <div class="detail-row">
              <span>安装时间</span>
              <span>{{ item.installDate }}</span>
            </div>
            <div class="detail-row">
              <span>维护状态</span>
              <span :class="{ warning: item.maintenanceStatus === '待维护' }">{{ item.maintenanceStatus }}</span>
            </div>
            <div class="detail-row">
              <span>上次维护</span>
              <span>{{ item.lastMaintenance }}</span>
            </div>
          </div>
          <div v-if="isOfficial" class="facility-action">
            <van-button size="mini" @click="recordMaintenance(item)">记录维护</van-button>
          </div>
        </div>
      </div>

      <div v-if="isOfficial" class="action-bar">
        <van-button type="primary" icon="plus" @click="showAddFacilityModal = true">新增设施</van-button>
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
const activeTab = ref('inspection')
const courtyardTab = ref('red')
const isOfficial = true
const searchKeyword = ref('')
const showAddInspectionModal = ref(false)
const showAddCleanerModal = ref(false)
const showAddFacilityModal = ref(false)
const showEvaluateModal = ref(false)

const statusColumns = [
  { text: '全部', value: '' },
  { text: '待处理', value: 'pending' },
  { text: '整改中', value: 'processing' },
  { text: '已完成', value: 'resolved' },
  { text: '逾期未整改', value: 'overdue' }
]
const typeColumns = [
  { text: '全部', value: '' },
  { text: '垃圾乱堆', value: 'garbage' },
  { text: '污水乱排', value: 'sewage' },
  { text: '私搭乱建', value: 'illegal' }
]

const stats = reactive({ excellent: 8, good: 15, poor: 3 })

const inspectionList = ref([
  { id: 1, type: 'garbage', location: '第一村民组15号门前', description: '垃圾堆积，影响环境', discoverTime: '2024-01-10', status: 'pending', deadline: '2024-01-15', photos: [], beforePhoto: '', afterPhoto: '' },
  { id: 2, type: 'sewage', location: '第二村民组8号东侧', description: '污水乱排，气味难闻', discoverTime: '2024-01-08', status: 'processing', deadline: '2024-01-18', photos: [], beforePhoto: '', afterPhoto: '' },
  { id: 3, type: 'garbage', location: '第三村民组20号北侧', description: '建筑垃圾未清理', discoverTime: '2024-01-05', status: 'resolved', deadline: '2024-01-12', photos: [], beforePhoto: '', afterPhoto: '' },
  { id: 4, type: 'illegal', location: '村主干道旁', description: '私搭乱建，占用公共空间', discoverTime: '2024-01-03', status: 'overdue', deadline: '2024-01-10', photos: [], beforePhoto: '', afterPhoto: '' }
])

const courtyardList = ref([
  { id: 1, name: '张建国家', group: '第一村民组', score: 95, rank: '优秀', comment: '庭院整洁，绿化美观', photo: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=beautiful%20chinese%20village%20courtyard%20garden%20clean&image_size=square' },
  { id: 2, name: '李秀英家', group: '第一村民组', score: 92, rank: '优秀', comment: '庭院布置精致，花草茂盛', photo: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=neat%20chinese%20rural%20home%20courtyard&image_size=square' },
  { id: 3, name: '王芳家', group: '第二村民组', score: 88, rank: '良好', comment: '整体整洁，略有杂物', photo: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chinese%20village%20house%20yard%20average&image_size=square' },
  { id: 4, name: '赵刚家', group: '第三村民组', score: 65, rank: '较差', comment: '杂物较多，需整改', photo: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=messy%20chinese%20village%20courtyard%20cluttered&image_size=square' }
])

const cleanerList = ref([
  { id: 1, name: '刘大爷', phone: '138****1234', area: '第一村民组', hireDate: '2023-01-15', attendance: 28, salary: 2500, salaryStatus: '已发放', status: '在职' },
  { id: 2, name: '王大妈', phone: '139****5678', area: '第二村民组', hireDate: '2023-03-20', attendance: 25, salary: 2500, salaryStatus: '已发放', status: '在职' },
  { id: 3, name: '陈阿姨', phone: '137****9012', area: '第三村民组', hireDate: '2023-06-10', attendance: 26, salary: 2500, salaryStatus: '待发放', status: '在职' }
])

const facilityList = ref([
  { id: 1, icon: '🗑️', type: 'trash', location: '第一村民组主干道', quantity: 5, installDate: '2023-01-15', maintenanceStatus: '正常', lastMaintenance: '2024-01-01' },
  { id: 2, icon: '🗑️', type: 'trash', location: '第二村民组中心广场', quantity: 3, installDate: '2023-03-20', maintenanceStatus: '待维护', lastMaintenance: '2023-11-15' },
  { id: 3, icon: '💧', type: 'sewage', location: '村污水处理站', quantity: 1, installDate: '2022-06-10', maintenanceStatus: '正常', lastMaintenance: '2024-01-10' },
  { id: 4, icon: '🚰', type: 'water', location: '第三村民组饮水点', quantity: 2, installDate: '2023-08-15', maintenanceStatus: '正常', lastMaintenance: '2024-01-05' }
])

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'inspection') {
      const res = await request.get('/environment/inspection')
      inspectionList.value = res.data || inspectionList.value
    } else if (activeTab.value === 'courtyard') {
      const res = await request.get('/environment/courtyard')
      courtyardList.value = res.list || res.data?.list || courtyardList.value
      stats.excellent = res.stats?.excellent || res.data?.stats?.excellent || stats.excellent
      stats.good = res.stats?.good || res.data?.stats?.good || stats.good
      stats.poor = res.stats?.poor || res.data?.stats?.poor || stats.poor
    } else if (activeTab.value === 'cleaner') {
      const res = await request.get('/environment/cleaner')
      cleanerList.value = res.data || cleanerList.value
    } else if (activeTab.value === 'facility') {
      const res = await request.get('/environment/facility', { params: { keyword: searchKeyword.value } })
      facilityList.value = res.data || facilityList.value
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const onSearch = () => {
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
    'garbage': '垃圾乱堆',
    'sewage': '污水乱排',
    'illegal': '私搭乱建'
  }
  return map[type] || type
}

const getStatusLabel = (status) => {
  const map = {
    'pending': '待处理',
    'processing': '整改中',
    'resolved': '已完成',
    'overdue': '逾期未整改'
  }
  return map[status] || status
}

const getFacilityType = (type) => {
  const map = {
    'trash': '垃圾桶',
    'sewage': '污水处理',
    'water': '饮水设施'
  }
  return map[type] || type
}

const isOverdue = (deadline) => {
  return new Date(deadline) < new Date()
}

const previewImage = (url) => {
  showToast('图片预览功能开发中')
}

const handleInspection = (item) => {
  showToast(`处理 ${item.location} 的环境问题`)
}

const completeInspection = (item) => {
  showToast(`完成 ${item.location} 的整改`)
}

const viewDetail = (item) => {
  showToast(`查看 ${item.location} 详情`)
}

const generateNotice = () => {
  showToast('评比公示已生成，同步至通知公告')
}

const viewCleanerDetail = (item) => {
  showToast(`查看 ${item.name} 详情`)
}

const editCleaner = (item) => {
  showToast(`编辑 ${item.name}`)
}

const recordMaintenance = (item) => {
  showToast(`记录 ${item.location} 的维护情况`)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.environment-page {
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

.month-select {
  font-size: 13px;
  color: #666;
  padding: 4px 10px;
  background: #f0f0f0;
  border-radius: 10px;
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

.search-bar {
  padding: 0 12px;
  margin-bottom: 12px;
}

.card-list {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.inspection-card, .cleaner-card, .facility-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.inspection-card.urgent {
  border-left: 4px solid #F44336;
}

.inspection-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.inspection-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.inspection-tag.garbage {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.inspection-tag.sewage {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.inspection-tag.illegal {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.inspection-time {
  font-size: 12px;
  color: #999;
}

.inspection-location {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 6px;
}

.inspection-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.inspection-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  display: inline-block;
  margin-bottom: 12px;
}

.inspection-status.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.inspection-status.processing {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.inspection-status.resolved {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.inspection-status.overdue {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.photos-section {
  margin-bottom: 12px;
}

.photos-title {
  font-size: 13px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.inspection-photos {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.photo-item {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.no-photo {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #999;
}

.compare-section {
  margin-bottom: 12px;
}

.compare-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.compare-item {
  flex: 1;
}

.compare-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 6px;
}

.compare-photo {
  width: 100%;
  height: 100px;
  border-radius: 8px;
  object-fit: cover;
}

.compare-arrow {
  font-size: 20px;
  color: #D22630;
}

.deadline-info {
  font-size: 13px;
  color: #666;
  margin-bottom: 12px;
}

.deadline-info .warning {
  color: #F44336;
  font-weight: bold;
}

.inspection-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.result-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 14px;
}

.result-tab {
  flex: 1;
  text-align: center;
  padding: 10px;
  border-radius: 10px;
  background: #fafafa;
  font-size: 14px;
  font-weight: bold;
  color: #666;
}

.result-tab.active {
  background: linear-gradient(135deg, #D22630, #B01A26);
  color: #fff;
}

.courtyard-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.courtyard-card {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #fafafa;
  border-radius: 10px;
}

.courtyard-card.black {
  background: #f8f8f8;
  border: 1px solid #eee;
}

.courtyard-rank {
  font-size: 24px;
  margin-right: 12px;
}

.courtyard-photo {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 12px;
}

.courtyard-photo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.courtyard-info {
  flex: 1;
}

.courtyard-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.courtyard-group {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.courtyard-score {
  font-size: 12px;
  color: #D22630;
  margin-bottom: 2px;
}

.courtyard-comment {
  font-size: 11px;
  color: #999;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.stat-item {
  text-align: center;
  padding: 16px 10px;
  background: #fafafa;
  border-radius: 10px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 4px;
}

.stat-value.warning {
  color: #F44336;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.cleaner-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.cleaner-avatar {
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

.cleaner-info {
  flex: 1;
}

.cleaner-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.cleaner-phone {
  font-size: 12px;
  color: #999;
}

.cleaner-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.cleaner-status.在职 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.cleaner-detail {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
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

.detail-row span:last-child.warning {
  color: #F44336;
}

.cleaner-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.facility-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.facility-icon {
  font-size: 24px;
  margin-right: 10px;
}

.facility-type {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.facility-type.trash {
  background: rgba(156, 163, 175, 0.2);
  color: #6B7280;
}

.facility-type.sewage {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.facility-type.water {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.facility-location {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.facility-detail {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 12px;
}

.facility-action {
  display: flex;
  justify-content: flex-end;
}
</style>