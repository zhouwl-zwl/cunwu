<template>
  <div class="civil-page page-container">
    <van-nav-bar title="民政社保" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="低保特困" name="lowIncome" />
      <van-tab title="残疾人服务" name="disabled" />
      <van-tab title="养老认证" name="pension" />
      <van-tab title="医保缴费" name="medical" />
      <van-tab title="临时救助" name="assistance" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'lowIncome'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">🏠 低保政策</div>
        </div>
        <div class="policy-content">
          <div class="policy-item">
            <div class="policy-title">申请条件</div>
            <div class="policy-desc">家庭人均收入低于当地最低生活保障标准，且家庭财产状况符合当地人民政府规定条件的城乡居民家庭，可申请低保。</div>
          </div>
          <div class="policy-item">
            <div class="policy-title">保障标准</div>
            <div class="policy-desc">农村居民最低生活保障标准：每人每月560元；城市居民最低生活保障标准：每人每月780元。</div>
          </div>
        </div>
        <van-button type="primary" block @click="showApplyModal = true">申请低保</van-button>
      </div>

      <div class="card" v-if="isOfficial">
        <div class="card-header">
          <div class="header-title">📋 低保户台账</div>
          <van-button type="primary" size="mini" @click="showAddModal = true">新增</van-button>
        </div>
        <div class="table-section">
          <ExcelTable 
            :data="lowIncomeList" 
            :columns="lowIncomeColumns" 
            export-filename="低保户台账.xlsx"
          />
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'disabled'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">♿ 我的残疾证信息</div>
        </div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">残疾证号</span>
            <span class="info-value">{{ disabledInfo.cardNo }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">残疾等级</span>
            <span class="info-value">{{ disabledInfo.level }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">残疾类别</span>
            <span class="info-value">{{ disabledInfo.category }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">有效期至</span>
            <span class="info-value" :class="{ expired: disabledInfo.expiring }">{{ disabledInfo.expireDate }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">护理补贴</span>
            <span class="info-value">¥{{ disabledInfo.careSubsidy }}/月</span>
          </div>
          <div class="info-item">
            <span class="info-label">生活补贴</span>
            <span class="info-value">¥{{ disabledInfo.lifeSubsidy }}/月</span>
          </div>
        </div>
        <div v-if="disabledInfo.expiring" class="expire-warning">
          ⚠️ 您的残疾证将于{{ disabledInfo.expireDate }}到期，请及时申请换证！
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📑 补贴发放记录</div>
        </div>
        <div class="table-section">
          <ExcelTable 
            :data="subsidyRecords" 
            :columns="subsidyColumns" 
            export-filename="补贴发放记录.xlsx"
          />
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'pension'">
      <div class="card" v-if="pensionInfo.status === 'pending'">
        <div class="warning-card">
          <div class="warning-icon">⚠️</div>
          <div class="warning-text">您的养老资格认证尚未完成，请尽快认证！</div>
          <van-button type="primary" block @click="doPensionCert">立即认证</van-button>
        </div>
      </div>

      <div class="card" v-else>
        <div class="success-card">
          <div class="success-icon">✅</div>
          <div class="success-text">养老资格认证已完成</div>
          <div class="success-detail">上次认证时间：{{ pensionInfo.lastCertTime }}</div>
        </div>
      </div>

      <div class="card" v-if="isOfficial">
        <div class="card-header">
          <div class="header-title">📋 未认证人员名单</div>
        </div>
        <div class="table-section">
          <ExcelTable 
            :data="uncertList" 
            :columns="uncertColumns" 
            export-filename="未认证人员名单.xlsx"
          />
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'medical'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">💊 我的医保信息</div>
        </div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">参保状态</span>
            <span class="info-value" :class="{ active: medicalInfo.status === '已参保' }">{{ medicalInfo.status }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">参保类型</span>
            <span class="info-value">{{ medicalInfo.type }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">缴费年度</span>
            <span class="info-value">{{ medicalInfo.year }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">缴费金额</span>
            <span class="info-value">¥{{ medicalInfo.amount }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">缴费方式</span>
            <span class="info-value">{{ medicalInfo.payMethod }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">缴费时间</span>
            <span class="info-value">{{ medicalInfo.payTime }}</span>
          </div>
        </div>
      </div>

      <div class="card" v-if="isOfficial">
        <div class="card-header">
          <div class="header-title">📋 未缴费人员</div>
        </div>
        <div class="table-section">
          <ExcelTable 
            :data="unpaidList" 
            :columns="unpaidColumns" 
            export-filename="未缴费人员.xlsx"
          />
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'assistance'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">🙏 临时救助申请</div>
        </div>
        <van-form @submit="submitAssistance">
          <van-field v-model="assistanceForm.reason" label="困难事由" placeholder="请详细描述困难情况" />
          <van-field v-model="assistanceForm.amount" label="申请金额" type="number" placeholder="请输入申请金额" />
          <van-field v-model="assistanceForm.contact" label="联系电话" placeholder="请输入联系电话" />
          <div class="upload-section">
            <div class="upload-label">佐证材料</div>
            <div class="upload-grid">
              <div v-for="(photo, idx) in assistanceForm.photos" :key="idx" class="upload-item">
                <img :src="photo" />
              </div>
              <div class="upload-add" @click="uploadPhoto">+</div>
            </div>
          </div>
          <van-button type="primary" block native-type="submit">提交申请</van-button>
        </van-form>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📋 我的救助记录</div>
        </div>
        <div class="table-section">
          <ExcelTable 
            :data="assistanceRecords" 
            :columns="assistanceRecordsColumns" 
            export-filename="我的救助记录.xlsx"
          />
        </div>
      </div>

      <div class="card" v-if="isOfficial">
        <div class="card-header">
          <div class="header-title">📋 待审核申请</div>
        </div>
        <div class="table-section">
          <ExcelTable 
            :data="pendingApplications" 
            :columns="pendingApplicationsColumns" 
            export-filename="待审核申请.xlsx"
          />
        </div>
      </div>
    </div>

    <van-dialog v-model:show="showApplyModal" title="低保申请" @confirm="submitLowIncome">
      <van-form>
        <van-field v-model="applyForm.name" label="申请人姓名" placeholder="请输入姓名" />
        <van-field v-model="applyForm.idCard" label="身份证号" placeholder="请输入身份证号" />
        <van-field v-model="applyForm.group" label="村组" placeholder="请输入村组" />
        <van-field v-model="applyForm.reason" label="困难原因" placeholder="请描述困难情况" />
      </van-form>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const activeTab = ref('lowIncome')
const isOfficial = true
const showApplyModal = ref(false)
const showAddModal = ref(false)

const lowIncomeList = ref([
  { id: 1, name: '李志强', group: '第一村民组', type: '低保', status: '正常' },
  { id: 2, name: '王芳', group: '第二村民组', type: '特困', status: '正常' },
  { id: 3, name: '陈秀英', group: '第三村民组', type: '低保', status: '待复核' }
])

const disabledInfo = ref({
  cardNo: '4103231970****1234',
  level: '三级',
  category: '肢体残疾',
  expireDate: '2024-12-31',
  expiring: true,
  careSubsidy: 100,
  lifeSubsidy: 150
})

const subsidyRecords = ref([
  { id: 1, type: '护理补贴', date: '2024-01-15', amount: 100 },
  { id: 2, type: '生活补贴', date: '2024-01-15', amount: 150 },
  { id: 3, type: '护理补贴', date: '2024-01-15', amount: 100 }
])

const pensionInfo = ref({
  status: 'completed',
  lastCertTime: '2024-01-10'
})

const uncertList = ref([
  { id: 1, name: '张老太', group: '第一村民组', age: 78 },
  { id: 2, name: '王大爷', group: '第二村民组', age: 82 }
])

const medicalInfo = ref({
  status: '已参保',
  type: '城乡居民医保',
  year: '2024',
  amount: 380,
  payMethod: '个人缴费',
  payTime: '2023-12-15'
})

const unpaidList = ref([
  { id: 1, name: '张伟', group: '第一村民组' },
  { id: 2, name: '李强', group: '第二村民组' }
])

const assistanceForm = ref({ reason: '', amount: '', contact: '', photos: [] })

const assistanceRecords = ref([
  { id: 1, reason: '医疗救助', date: '2024-01-10', status: '已发放' },
  { id: 2, reason: '临时困难救助', date: '2024-01-05', status: '已发放' }
])

const pendingApplications = ref([
  { id: 1, name: '张建国', reason: '医疗困难', amount: 5000, status: 'pending' },
  { id: 2, name: '李秀英', reason: '生活困难', amount: 2000, status: 'pending' }
])

const applyForm = ref({ name: '', idCard: '', group: '', reason: '' })

const lowIncomeColumns = [
  { key: 'name', title: '姓名' },
  { key: 'group', title: '村组' },
  { key: 'type', title: '类型' },
  { key: 'status', title: '状态' }
]

const subsidyColumns = [
  { key: 'type', title: '补贴类型' },
  { key: 'date', title: '发放日期' },
  { key: 'amount', title: '金额', formatter: (val) => `¥${val}` }
]

const uncertColumns = [
  { key: 'name', title: '姓名' },
  { key: 'group', title: '村组' },
  { key: 'age', title: '年龄', formatter: (val) => `${val}岁` }
]

const unpaidColumns = [
  { key: 'name', title: '姓名' },
  { key: 'group', title: '村组' }
]

const assistanceRecordsColumns = [
  { key: 'reason', title: '救助事由' },
  { key: 'date', title: '申请日期' },
  { key: 'status', title: '状态' }
]

const pendingApplicationsColumns = [
  { key: 'name', title: '申请人' },
  { key: 'reason', title: '困难原因' },
  { key: 'amount', title: '申请金额', formatter: (val) => `¥${val}` },
  { key: 'status', title: '状态', formatter: (val) => val === 'pending' ? '待审核' : val }
]

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'lowIncome') {
      const res = await request.get('/civil/low-income')
      lowIncomeList.value = res.data || lowIncomeList.value
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const submitLowIncome = () => {
  showToast('申请已提交')
  showApplyModal.value = false
}

const doPensionCert = () => {
  showToast('认证功能开发中')
}

const certForOthers = (item) => {
  showToast(`${item.name}代办认证功能开发中`)
}

const uploadPhoto = () => {
  showToast('上传照片功能开发中')
}

const submitAssistance = () => {
  showToast('申请已提交')
  assistanceForm.value = { reason: '', amount: '', contact: '', photos: [] }
}

const approveApplication = (item) => {
  showToast(`已通过 ${item.name} 的申请`)
}

const rejectApplication = (item) => {
  showToast(`已驳回 ${item.name} 的申请`)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.civil-page {
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

.info-value.active {
  color: #4CAF50;
}

.info-value.expired {
  color: #F44336;
}

.expire-warning {
  background: rgba(255, 215, 0, 0.15);
  padding: 12px;
  border-radius: 10px;
  margin-top: 14px;
  font-size: 13px;
  color: #DAA520;
  text-align: center;
}

.policy-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 14px;
}

.policy-item {
  background: #fafafa;
  padding: 12px;
  border-radius: 10px;
}

.policy-title {
  font-size: 14px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 6px;
}

.policy-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.6;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #fafafa;
  border-radius: 10px;
}

.item-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.item-group, .item-date, .item-detail {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.item-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.item-status.normal {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.item-status.待复核 {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.item-status.已发放 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.item-status.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.item-amount {
  font-size: 16px;
  font-weight: bold;
  color: #D22630;
}

.item-tag {
  font-size: 12px;
  color: #F44336;
  background: rgba(244, 67, 54, 0.2);
  padding: 4px 10px;
  border-radius: 10px;
}

.item-actions {
  display: flex;
  gap: 8px;
}

.warning-card {
  text-align: center;
  padding: 24px;
}

.warning-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.warning-text {
  font-size: 16px;
  font-weight: bold;
  color: #DAA520;
  margin-bottom: 16px;
}

.success-card {
  text-align: center;
  padding: 24px;
}

.success-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.success-text {
  font-size: 16px;
  font-weight: bold;
  color: #4CAF50;
  margin-bottom: 8px;
}

.success-detail {
  font-size: 13px;
  color: #999;
}

.upload-section {
  margin: 16px 0;
}

.upload-label {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
}

.upload-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.upload-item {
  aspect-ratio: 1;
  border-radius: 10px;
  overflow: hidden;
}

.upload-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-add {
  aspect-ratio: 1;
  border-radius: 10px;
  border: 2px dashed #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #999;
}

.table-section {
  margin: 0 -14px -14px;
}
</style>
