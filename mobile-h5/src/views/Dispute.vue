<template>
  <div class="dispute-page page-container">
    <van-nav-bar title="矛盾信访调处" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="我的诉求" name="my" />
      <van-tab title="全村矛盾" name="all" />
      <van-tab title="信访办理" name="petition" />
      <van-tab title="未化解跟踪" name="tracking" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'my'">
      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showSubmitModal = true">提交诉求</van-button>
      </div>

      <div class="card-list">
        <div v-for="item in myDemands" :key="item.id" class="demand-card">
          <div class="demand-header">
            <span class="demand-tag">{{ item.type }}</span>
            <span class="demand-status" :class="item.status">{{ getStatusLabel(item.status) }}</span>
          </div>
          <div class="demand-title">{{ item.title }}</div>
          <div class="demand-desc">{{ item.content }}</div>
          <div class="demand-info">
            <div class="info-row">
              <span>提交时间</span>
              <span>{{ item.createTime }}</span>
            </div>
            <div class="info-row" v-if="item.mediator">
              <span>调解员</span>
              <span>{{ item.mediator }}</span>
            </div>
          </div>

          <div v-if="item.replies && item.replies.length > 0" class="replies-section">
            <div class="replies-title">调解员回复</div>
            <div v-for="reply in item.replies" :key="reply.id" class="reply-item">
              <div class="reply-header">
                <span class="reply-name">{{ reply.name }}</span>
                <span class="reply-time">{{ reply.time }}</span>
              </div>
              <div class="reply-content">{{ reply.content }}</div>
              <div v-if="reply.photos && reply.photos.length > 0" class="reply-photos">
                <img v-for="(photo, idx) in reply.photos" :key="idx" :src="photo" class="photo-item" @click="previewImage(photo)" />
              </div>
            </div>
          </div>

          <div v-if="item.status === 'completed'" class="demand-actions">
            <van-button size="mini" type="primary" @click="evaluate(item)">满意度评价</van-button>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'all'">
      <div class="filter-bar">
        <van-picker :columns="typeColumns" @confirm="onTypeSelect" placeholder="纠纷类型" />
        <van-picker :columns="statusColumns" @confirm="onStatusSelect" placeholder="处理状态" />
      </div>

      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showAddModal = true">新增纠纷</van-button>
      </div>

      <div class="card-list">
        <div v-for="item in disputeList" :key="item.id" class="dispute-card">
          <div class="dispute-header">
            <span class="dispute-tag">{{ item.type }}</span>
            <span class="dispute-level" :class="item.level">{{ getLevelLabel(item.level) }}</span>
          </div>
          <div class="dispute-title">{{ item.title }}</div>
          <div class="dispute-parties">{{ item.partyA }} 与 {{ item.partyB }}</div>
          <div class="dispute-info">
              <div class="info-row">
                <span>调解人</span>
                <span>{{ item.mediator }}</span>
              </div>
              <div class="info-row">
                <span>状态</span>
                <span :class="item.status">{{ getStatusLabel(item.status) }}</span>
              </div>
            </div>
            <div class="dispute-actions">
              <van-button size="mini" @click="viewDetail(item)">查看详情</van-button>
              <van-button size="mini" type="primary" @click="assignMediator(item)">分配调解员</van-button>
            </div>
          </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'petition'">
      <div class="filter-bar">
        <van-picker :columns="sourceColumns" @confirm="onSourceSelect" placeholder="来源渠道" />
      </div>

      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showAddPetitionModal = true">登记信访件</van-button>
      </div>

      <div class="card-list">
        <div v-for="item in petitionList" :key="item.id" class="petition-card" :class="{ urgent: item.overdue }">
          <div class="petition-header">
            <span class="petition-source">{{ item.source }}</span>
            <span class="petition-time">{{ item.receiveTime }}</span>
          </div>
          <div class="petition-title">{{ item.title }}</div>
          <div class="petition-info">
            <div class="info-row">
              <span>信访人</span>
              <span>{{ item.name }}</span>
            </div>
            <div class="info-row">
              <span>联系电话</span>
              <span>{{ item.phone }}</span>
            </div>
            <div class="info-row">
              <span>时限</span>
              <span :class="{ warning: item.overdue }">{{ item.deadline }}</span>
            </div>
            <div class="info-row">
              <span>状态</span>
              <span :class="item.status">{{ getStatusLabel(item.status) }}</span>
            </div>
          </div>
          <div class="petition-actions">
            <van-button size="mini" @click="handlePetition(item)">办理</van-button>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'tracking'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">📊 未化解统计</div>
        </div>
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-value">{{ trackingStats.total }}</div>
            <div class="stat-label">未化解总数</div>
          </div>
          <div class="stat-item">
            <div class="stat-value danger">{{ trackingStats.over30 }}</div>
            <div class="stat-label">超30天</div>
          </div>
          <div class="stat-item">
            <div class="stat-value warning">{{ trackingStats.over60 }}</div>
            <div class="stat-label">超60天</div>
          </div>
        </div>
      </div>

      <div class="card-list">
        <div v-for="item in trackingList" :key="item.id" class="tracking-card" :class="{ urgent: item.days > 60 }">
          <div class="tracking-header">
            <span class="tracking-tag">{{ item.type }}</span>
            <span class="tracking-days" :class="{ urgent: item.days > 60 }">{{ item.days }}天未化解</span>
          </div>
          <div class="tracking-title">{{ item.title }}</div>
          <div class="tracking-parties">{{ item.partyA }} 与 {{ item.partyB }}</div>
          <div class="tracking-info">
            <div class="info-row">
              <span>负责网格员</span>
              <span>{{ item.gridWorker }}</span>
            </div>
            <div class="info-row">
              <span>上次跟进</span>
              <span>{{ item.lastFollowTime }}</span>
            </div>
            <div class="info-row">
              <span>跟进次数</span>
              <span>{{ item.followCount }}次</span>
            </div>
          </div>
          <div class="tracking-actions">
            <van-button size="mini" type="primary" @click="followUp(item)">跟进处理</van-button>
          </div>
        </div>
      </div>
    </div>

    <van-dialog v-model:show="showSubmitModal" title="提交诉求" @confirm="submitDemand">
      <van-form>
        <van-field v-model="demandForm.title" label="诉求标题" placeholder="请输入诉求标题" />
        <van-field v-model="demandForm.type" label="诉求类型" placeholder="如：邻里纠纷、土地纠纷" />
        <van-field v-model="demandForm.content" label="诉求内容" type="textarea" placeholder="请详细描述诉求内容" />
      </van-form>
    </van-dialog>
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
const showSubmitModal = ref(false)
const showAddModal = ref(false)
const showAddPetitionModal = ref(false)

const typeColumns = [
  { text: '全部', value: '' },
  { text: '婚姻', value: '婚姻' },
  { text: '土地', value: '土地' },
  { text: '邻里', value: '邻里' },
  { text: '劳资', value: '劳资' }
]
const statusColumns = [
  { text: '全部', value: '' },
  { text: '待受理', value: 'pending' },
  { text: '调解中', value: 'processing' },
  { text: '已办结', value: 'completed' }
]
const sourceColumns = [
  { text: '全部', value: '' },
  { text: '上级转办', value: '上级转办' },
  { text: '村民来访', value: '村民来访' },
  { text: '电话反映', value: '电话反映' }
]

const myDemands = ref([
  { 
    id: 1, 
    title: '邻里噪音问题', 
    type: '邻里纠纷', 
    content: '邻居家装修噪音太大，影响正常生活', 
    createTime: '2024-01-10', 
    status: 'processing', 
    mediator: '李主任',
    replies: [
      { id: 1, name: '李主任', time: '2024-01-11', content: '已联系对方，要求合理安排装修时间' },
      { id: 2, name: '李主任', time: '2024-01-12', content: '对方已承诺避开休息时间施工', photos: [] }
    ]
  },
  { 
    id: 2, 
    title: '土地边界纠纷', 
    type: '土地纠纷', 
    content: '与邻居存在土地边界争议', 
    createTime: '2024-01-05', 
    status: 'completed', 
    mediator: '王书记',
    replies: [
      { id: 1, name: '王书记', time: '2024-01-06', content: '已组织双方现场勘测', photos: [] },
      { id: 2, name: '王书记', time: '2024-01-08', content: '双方已达成一致，签订调解协议', photos: [] }
    ]
  },
  { 
    id: 3, 
    title: '环境问题', 
    type: '环境纠纷', 
    content: '垃圾堆积影响居住环境', 
    createTime: '2024-01-03', 
    status: 'pending',
    replies: []
  }
])

const disputeList = ref([
  { id: 1, title: '邻里噪音纠纷', type: '邻里', partyA: '张建国', partyB: '李志强', mediator: '李主任', level: 'low', status: 'processing' },
  { id: 2, title: '土地边界争议', type: '土地', partyA: '王芳', partyB: '刘大伟', mediator: '王书记', level: 'medium', status: 'pending' },
  { id: 3, title: '婚姻家庭纠纷', type: '婚姻', partyA: '陈秀英', partyB: '赵刚', mediator: '张委员', level: 'high', status: 'pending' }
])

const petitionList = ref([
  { id: 1, title: '关于道路维修的建议', source: '村民来访', name: '张建国', phone: '138****1234', receiveTime: '2024-01-10', deadline: '2024-01-25', status: 'processing', overdue: false },
  { id: 2, title: '土地流转问题反映', source: '上级转办', name: '李志强', phone: '139****5678', receiveTime: '2024-01-05', deadline: '2024-01-15', status: 'pending', overdue: true },
  { id: 3, title: '环境整治建议', source: '电话反映', name: '王芳', phone: '137****9012', receiveTime: '2024-01-03', deadline: '2024-01-18', status: 'completed', overdue: false }
])

const trackingStats = reactive({ total: 5, over30: 3, over60: 1 })

const trackingList = ref([
  { id: 1, title: '土地边界争议', type: '土地纠纷', partyA: '王芳', partyB: '刘大伟', days: 75, gridWorker: '赵网格员', lastFollowTime: '2024-01-01', followCount: 3 },
  { id: 2, title: '婚姻家庭纠纷', type: '婚姻纠纷', partyA: '陈秀英', partyB: '赵刚', days: 45, gridWorker: '钱网格员', lastFollowTime: '2024-01-05', followCount: 2 },
  { id: 3, title: '劳资纠纷', type: '劳资纠纷', partyA: '孙师傅', partyB: '施工队', days: 25, gridWorker: '孙网格员', lastFollowTime: '2024-01-10', followCount: 1 }
])

const demandForm = ref({ title: '', type: '', content: '' })

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'my') {
      const res = await request.get('/demand/my-demands')
      myDemands.value = res.data || myDemands.value
    } else if (activeTab.value === 'all') {
      const res = await request.get('/dispute/list')
      disputeList.value = res.data || disputeList.value
    } else if (activeTab.value === 'petition') {
      const res = await request.get('/petition/list')
      petitionList.value = res.data || petitionList.value
    } else if (activeTab.value === 'tracking') {
      const res = await request.get('/dispute/tracking')
      trackingList.value = res.list || res.data?.list || trackingList.value
      trackingStats.total = res.stats?.total || res.data?.stats?.total || trackingStats.total
      trackingStats.over30 = res.stats?.over30 || res.data?.stats?.over30 || trackingStats.over30
      trackingStats.over60 = res.stats?.over60 || res.data?.stats?.over60 || trackingStats.over60
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const onTypeSelect = (value) => {
  showToast(`选择类型：${value}`)
}

const onStatusSelect = (value) => {
  showToast(`选择状态：${value}`)
}

const onSourceSelect = (value) => {
  showToast(`选择来源：${value}`)
}

const getStatusLabel = (status) => {
  const map = {
    'pending': '待受理',
    'processing': '调解中',
    'completed': '已办结'
  }
  return map[status] || status
}

const getLevelLabel = (level) => {
  const map = {
    'low': '轻微',
    'medium': '一般',
    'high': '重大'
  }
  return map[level] || level
}

const previewImage = (url) => {
  showToast('图片预览功能开发中')
}

const submitDemand = () => {
  showToast('诉求已提交')
  showSubmitModal.value = false
  demandForm.value = { title: '', type: '', content: '' }
}

const evaluate = (item) => {
  showToast(`评价 ${item.title} 功能开发中`)
}

const viewDetail = (item) => {
  showToast(`查看 ${item.title} 详情`)
}

const assignMediator = (item) => {
  showToast(`为 ${item.title} 分配调解员`)
}

const handlePetition = (item) => {
  showToast(`办理 ${item.title}`)
}

const followUp = (item) => {
  showToast(`跟进 ${item.title}`)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.dispute-page {
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

.demand-card, .dispute-card, .petition-card, .tracking-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.petition-card.urgent, .tracking-card.urgent {
  border-left: 4px solid #F44336;
}

.demand-header, .dispute-header, .petition-header, .tracking-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.demand-tag, .dispute-tag, .petition-source, .tracking-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  background: rgba(210, 38, 48, 0.1);
  color: #D22630;
}

.demand-status, .dispute-level, .tracking-days {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.demand-status.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.demand-status.processing {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.demand-status.completed {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.dispute-level.low {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.dispute-level.medium {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.dispute-level.high {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.tracking-days {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.tracking-days.urgent {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.petition-time {
  font-size: 12px;
  color: #999;
}

.demand-title, .dispute-title, .petition-title, .tracking-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.demand-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.dispute-parties, .tracking-parties {
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.demand-info, .dispute-info, .petition-info, .tracking-info {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 12px;
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

.info-row span:last-child.warning {
  color: #F44336;
}

.replies-section {
  background: #f0f8ff;
  border-radius: 10px;
  padding: 12px;
  margin-bottom: 12px;
  border: 1px solid #e6f2ff;
}

.replies-title {
  font-size: 13px;
  font-weight: bold;
  color: #2196F3;
  margin-bottom: 10px;
}

.reply-item {
  margin-bottom: 12px;
}

.reply-item:last-child {
  margin-bottom: 0;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.reply-name {
  font-size: 13px;
  font-weight: bold;
  color: #333;
}

.reply-time {
  font-size: 12px;
  color: #999;
}

.reply-content {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 8px;
}

.reply-photos {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.photo-item {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  object-fit: cover;
}

.demand-actions, .dispute-actions, .petition-actions, .tracking-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
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

.stat-value.danger {
  color: #F44336;
}

.stat-value.warning {
  color: #FF9800;
}

.stat-label {
  font-size: 12px;
  color: #999;
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