<template>
  <div class="profile-page page-container">
    <div class="top-bar">
      <button class="back-btn" @click="onBack">←</button>
      <div class="top-title">个人信息</div>
      <div class="role-tag">{{ roleName }}</div>
    </div>

    <div class="profile-header">
      <div class="avatar">{{ form.name ? form.name.charAt(0) : '·' }}</div>
      <div class="user-name">{{ form.name || '未设置姓名' }}</div>
      <div class="user-role-tag">{{ roleName }}</div>
    </div>

    <div class="form-section">
      <div class="section-title">基础账号信息</div>
      
      <div class="form-item">
        <label class="form-label">登录账号</label>
        <div class="form-input readonly">
          {{ form.account || '—' }}
        </div>
      </div>

      <div class="form-item">
        <label class="form-label">真实姓名 <span class="required">*</span></label>
        <input 
          v-model="form.name" 
          class="form-input" 
          placeholder="请输入真实姓名"
          @input="checkDirty"
        />
      </div>

      <div class="form-item">
        <label class="form-label">手机号码 <span class="required">*</span></label>
        <input 
          v-model="form.phone" 
          class="form-input" 
          type="tel"
          placeholder="请输入11位手机号码"
          @input="checkDirty"
        />
      </div>

      <div class="form-item">
        <label class="form-label">所属行政村 <span class="required">*</span></label>
        <div class="form-input picker-trigger" @click="showVillagePicker = true">
          <span>{{ form.village || '请选择' }}</span>
          <span class="picker-arrow">›</span>
        </div>
      </div>

      <div class="form-item">
        <label class="form-label">当前角色</label>
        <div class="form-input readonly">
          {{ roleName }}
        </div>
      </div>
    </div>

    <div class="form-section">
      <div class="section-title">党务信息 <span class="audit-hint">修改需审核</span></div>
      
      <div class="form-item">
        <label class="form-label">党员身份 <span class="required">*</span> <span class="audit-tag">需审核</span></label>
        <div class="form-input picker-trigger" @click="showPartyMemberPicker = true">
          <span>{{ form.isPartyMember || '请选择' }}</span>
          <span class="picker-arrow">›</span>
        </div>
      </div>

      <div v-if="form.isPartyMember === '是'" class="form-item">
        <label class="form-label">党员类型 <span class="audit-tag">需审核</span></label>
        <div class="form-input picker-trigger" @click="showPartyTypePicker = true">
          <span>{{ form.partyType || '请选择' }}</span>
          <span class="picker-arrow">›</span>
        </div>
      </div>

      <div v-if="form.isPartyMember === '是'" class="form-item">
        <label class="form-label">入党时间 <span class="audit-tag">需审核</span></label>
        <div class="form-input picker-trigger" @click="showJoinDatePicker = true">
          <span>{{ formatDate(form.joinPartyDate) || '请选择' }}</span>
          <span class="picker-arrow">›</span>
        </div>
      </div>

      <div v-if="form.isPartyMember === '是'" class="form-item">
        <label class="form-label">所属党支部 <span class="audit-tag">需审核</span></label>
        <div class="form-input picker-trigger" @click="showBranchPicker = true">
          <span>{{ form.partyBranch || '请选择' }}</span>
          <span class="picker-arrow">›</span>
        </div>
      </div>

      <div v-if="form.isPartyMember === '是'" class="form-item">
        <label class="form-label">党员积分</label>
        <div class="form-input readonly gold-text">
          {{ form.partyScore || 0 }} 分
        </div>
      </div>
    </div>

    <div class="form-section">
      <div class="section-title">村级职务分工 <span class="audit-hint">修改需审核</span></div>
      
      <div class="form-item">
        <label class="form-label">现任职务 <span class="required">*</span> <span class="audit-tag">需审核</span></label>
        <div class="form-input picker-trigger" @click="showDutyPicker = true">
          <span>{{ form.duty || '请选择' }}</span>
          <span class="picker-arrow">›</span>
        </div>
      </div>

      <div v-if="isOfficialRole" class="form-item">
        <label class="form-label">分管业务条线 <span class="audit-tag">需审核</span></label>
        <div class="form-input picker-trigger" @click="showBusinessPicker = true">
          <span>{{ form.manageBusiness || '请选择' }}</span>
          <span class="picker-arrow">›</span>
        </div>
      </div>

      <div class="form-item">
        <label class="form-label">任职起始日期 <span class="audit-tag">需审核</span></label>
        <div class="form-input picker-trigger" @click="showWorkDatePicker = true">
          <span>{{ formatDate(form.startWorkDate) || '请选择' }}</span>
          <span class="picker-arrow">›</span>
        </div>
      </div>

      <div class="form-item">
        <label class="form-label">负责网格/片区 <span class="audit-tag">需审核</span></label>
        <input 
          v-model="form.gridArea" 
          class="form-input" 
          placeholder="请输入负责的网格或片区"
          @input="checkDirty"
        />
      </div>
    </div>

    <div class="form-section">
      <div class="section-title">补充个人资料</div>
      
      <div class="form-item">
        <label class="form-label">身份证号码</label>
        <input 
          v-model="form.idCard" 
          class="form-input" 
          placeholder="请输入身份证号码"
          @input="checkDirty"
        />
      </div>

      <div class="form-item">
        <label class="form-label">常住地址</label>
        <input 
          v-model="form.address" 
          class="form-input" 
          placeholder="请输入常住地址"
          @input="checkDirty"
        />
      </div>

      <div class="form-item">
        <label class="form-label">紧急联系人</label>
        <input 
          v-model="form.emergencyName" 
          class="form-input" 
          placeholder="请输入紧急联系人姓名"
          @input="checkDirty"
        />
      </div>

      <div class="form-item">
        <label class="form-label">紧急联系电话</label>
        <input 
          v-model="form.emergencyPhone" 
          class="form-input" 
          type="tel"
          placeholder="请输入紧急联系电话"
          @input="checkDirty"
        />
      </div>

      <div class="form-item">
        <label class="form-label">账号创建时间</label>
        <div class="form-input readonly">
          {{ formatDateTime(form.createTime) || '—' }}
        </div>
      </div>

      <div class="form-item">
        <label class="form-label">上次登录时间</label>
        <div class="form-input readonly">
          {{ formatDateTime(form.lastLogin) || '—' }}
        </div>
      </div>
    </div>

    <div class="form-section">
      <div class="section-title">信息变更审核记录</div>
      
      <div v-if="auditList.length === 0" class="empty-tip">
        暂无审核记录
      </div>

      <div v-else class="audit-list">
        <div 
          v-for="item in auditList" 
          :key="item.auditId" 
          class="audit-item"
        >
          <div class="audit-header">
            <span class="audit-type">{{ getChangeTypeLabel(item.changeType) }}</span>
            <span :class="['audit-status', getStatusClass(item.auditStatus)]">
              {{ getStatusLabel(item.auditStatus) }}
            </span>
          </div>
          <div class="audit-time">{{ formatDateTime(item.submitTime) }}</div>
          <div class="audit-content">
            <div v-if="item.auditOpinion" class="audit-opinion">
              <span class="opinion-label">审核意见：</span>
              <span>{{ item.auditOpinion }}</span>
            </div>
          </div>
          <div v-if="item.auditStatus === 2" class="audit-action">
            <button class="reedit-btn" @click="reeditAudit(item)">重新编辑</button>
          </div>
        </div>
      </div>
    </div>

    <div class="submit-area">
      <button 
        class="submit-btn" 
        :class="{ disabled: !isDirty }"
        :disabled="!isDirty"
        @click="submitForm"
      >
        <span v-if="loading">保存中...</span>
        <span v-else>确认修改</span>
      </button>
    </div>

    <van-popup v-model:show="showVillagePicker" position="bottom" :round="true">
      <van-picker 
        :columns="villageOptions" 
        @confirm="onVillageConfirm"
        @cancel="showVillagePicker = false"
      />
    </van-popup>

    <van-popup v-model:show="showPartyMemberPicker" position="bottom" :round="true">
      <van-picker 
        :columns="partyMemberOptions" 
        @confirm="onPartyMemberConfirm"
        @cancel="showPartyMemberPicker = false"
      />
    </van-popup>

    <van-popup v-model:show="showPartyTypePicker" position="bottom" :round="true">
      <van-picker 
        :columns="partyTypeOptions" 
        @confirm="onPartyTypeConfirm"
        @cancel="showPartyTypePicker = false"
      />
    </van-popup>

    <van-popup v-model:show="showBranchPicker" position="bottom" :round="true">
      <van-picker 
        :columns="branchOptions" 
        @confirm="onBranchConfirm"
        @cancel="showBranchPicker = false"
      />
    </van-popup>

    <van-popup v-model:show="showDutyPicker" position="bottom" :round="true">
      <van-picker 
        :columns="dutyOptions" 
        @confirm="onDutyConfirm"
        @cancel="showDutyPicker = false"
      />
    </van-popup>

    <van-popup v-model:show="showBusinessPicker" position="bottom" :round="true">
      <div class="multi-select-popup">
        <div class="popup-header">
          <button class="popup-cancel" @click="showBusinessPicker = false">取消</button>
          <span class="popup-title">分管业务条线</span>
          <button class="popup-confirm" @click="onBusinessConfirm">确认</button>
        </div>
        <div class="popup-content">
          <van-checkbox-group v-model="selectedBusiness">
            <van-checkbox 
              v-for="item in businessOptions" 
              :key="item" 
              :name="item"
            >{{ item }}</van-checkbox>
          </van-checkbox-group>
        </div>
      </div>
    </van-popup>

    <van-popup v-model:show="showJoinDatePicker" position="bottom" :round="true">
      <van-date-picker 
        v-model="joinDateValue"
        :max-date="today"
        @confirm="onJoinDateConfirm"
        @cancel="showJoinDatePicker = false"
      />
    </van-popup>

    <van-popup v-model:show="showWorkDatePicker" position="bottom" :round="true">
      <van-date-picker 
        v-model="workDateValue"
        :max-date="today"
        @confirm="onWorkDateConfirm"
        @cancel="showWorkDatePicker = false"
      />
    </van-popup>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onActivated, watch } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()

const form = reactive({
  account: '',
  name: '',
  phone: '',
  village: '',
  role: '',
  roleCode: '',
  isPartyMember: '',
  partyType: '',
  joinPartyDate: '',
  partyBranch: '',
  partyScore: 0,
  duty: '',
  manageBusiness: '',
  startWorkDate: '',
  gridArea: '',
  idCard: '',
  address: '',
  emergencyName: '',
  emergencyPhone: '',
  createTime: '',
  lastLogin: ''
})

const originalForm = reactive({})

const showVillagePicker = ref(false)
const showPartyMemberPicker = ref(false)
const showPartyTypePicker = ref(false)
const showBranchPicker = ref(false)
const showDutyPicker = ref(false)
const showBusinessPicker = ref(false)
const showJoinDatePicker = ref(false)
const showWorkDatePicker = ref(false)

const selectedBusiness = ref([])
const joinDateValue = ref(new Date())
const workDateValue = ref(new Date())

const loading = ref(false)
const isDirty = ref(false)
const auditList = ref([])

const villageOptions = [
  { text: '罗卜田乡', value: '罗卜田乡' },
  { text: '和平村', value: '和平村' },
  { text: '团结村', value: '团结村' },
  { text: '红旗村', value: '红旗村' },
  { text: '胜利村', value: '胜利村' }
]
const partyMemberOptions = [
  { text: '是', value: '是' },
  { text: '否', value: '否' }
]
const partyTypeOptions = [
  { text: '正式党员', value: '正式党员' },
  { text: '预备党员', value: '预备党员' },
  { text: '流动党员', value: '流动党员' }
]
const branchOptions = [
  { text: '第一党支部', value: '第一党支部' },
  { text: '第二党支部', value: '第二党支部' },
  { text: '第三党支部', value: '第三党支部' },
  { text: '离退休党支部', value: '离退休党支部' }
]
const dutyOptions = [
  { text: '村党支部书记', value: '村党支部书记' },
  { text: '村委会主任', value: '村委会主任' },
  { text: '村两委委员', value: '村两委委员' },
  { text: '网格员', value: '网格员' },
  { text: '村务监督委员', value: '村务监督委员' },
  { text: '驻村工作队队员', value: '驻村工作队队员' },
  { text: '普通村民', value: '普通村民' }
]
const businessOptions = ['党建', '乡村振兴', '综治调解', '三资管理', '安全防护', '便民服务']

const today = new Date()

const roleName = computed(() => {
  const roleMap = {
    'VILLAGER': '普通村民',
    'VILLAGE_OFFICIAL': '村级网格员',
    'RESIDENT_OFFICIAL': '驻村干部',
    'ADMIN': '村级管理员'
  }
  return roleMap[form.roleCode] || '未分配角色'
})

const isOfficialRole = computed(() => false)

const onBack = () => { goBack(router) }

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const formatDateTime = (date) => {
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}

const getChangeTypeLabel = (type) => {
  const map = {
    'party': '党务变更',
    'duty': '职务变更',
    'all': '党务/职务变更'
  }
  return map[type] || '信息变更'
}

const getStatusLabel = (status) => {
  if (status === 0) return '待审核'
  if (status === 1) return '已通过'
  if (status === 2) return '已驳回'
  return '未知'
}

const getStatusClass = (status) => {
  if (status === 0) return 'pending'
  if (status === 1) return 'passed'
  if (status === 2) return 'rejected'
  return ''
}

const checkDirty = () => {
  for (const key in form) {
    if (form[key] !== originalForm[key]) {
      isDirty.value = true
      return
    }
  }
  isDirty.value = false
}

const onVillageConfirm = ({ selectedOptions }) => {
  form.village = selectedOptions[0]
  showVillagePicker.value = false
  checkDirty()
}

const onPartyMemberConfirm = ({ selectedOptions }) => {
  form.isPartyMember = selectedOptions[0]
  if (form.isPartyMember !== '是') {
    form.partyType = ''
    form.joinPartyDate = ''
    form.partyBranch = ''
  }
  showPartyMemberPicker.value = false
  checkDirty()
}

const onPartyTypeConfirm = ({ selectedOptions }) => {
  form.partyType = selectedOptions[0]
  showPartyTypePicker.value = false
  checkDirty()
}

const onBranchConfirm = ({ selectedOptions }) => {
  form.partyBranch = selectedOptions[0]
  showBranchPicker.value = false
  checkDirty()
}

const onDutyConfirm = ({ selectedOptions }) => {
  form.duty = selectedOptions[0]
  showDutyPicker.value = false
  checkDirty()
}

const onBusinessConfirm = () => {
  form.manageBusiness = selectedBusiness.value.join(',')
  showBusinessPicker.value = false
  checkDirty()
}

const onJoinDateConfirm = (date) => {
  form.joinPartyDate = formatDate(date)
  showJoinDatePicker.value = false
  checkDirty()
}

const onWorkDateConfirm = (date) => {
  form.startWorkDate = formatDate(date)
  showWorkDatePicker.value = false
  checkDirty()
}

const validateForm = () => {
  if (!form.name) {
    showToast('请输入真实姓名')
    return false
  }
  if (!form.phone) {
    showToast('请输入手机号码')
    return false
  }
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    showToast('请输入正确的11位手机号码')
    return false
  }
  if (!form.village) {
    showToast('请选择所属行政村')
    return false
  }
  if (!form.duty) {
    showToast('请选择现任职务')
    return false
  }
  if (isOfficialRole.value && !form.manageBusiness) {
    showToast('村干部请至少选择一项分管业务')
    return false
  }
  if (form.idCard && !/^\d{17}[\dXx]$/.test(form.idCard)) {
    showToast('请输入正确的身份证号码')
    return false
  }
  return true
}

const submitForm = async () => {
  if (!validateForm()) return
  
  loading.value = true
  try {
    const params = {
      name: form.name,
      phone: form.phone,
      village: form.village,
      isPartyMember: form.isPartyMember,
      partyType: form.partyType,
      joinPartyDate: form.joinPartyDate,
      partyBranch: form.partyBranch,
      duty: form.duty,
      manageBusiness: form.manageBusiness,
      startWorkDate: form.startWorkDate,
      gridArea: form.gridArea,
      idCard: form.idCard,
      address: form.address,
      emergencyName: form.emergencyName,
      emergencyPhone: form.emergencyPhone
    }
    
    const res = await request.put('/auth/update-profile', params)
    
    if (res.message.includes('审核')) {
      showToast(res.message)
    } else {
      showToast('信息修改成功')
      Object.assign(originalForm, form)
      isDirty.value = false
    }
    
    fetchAuditList()
  } catch (error) {
    if (error.message) {
      showToast(error.message)
    } else {
      showToast('修改失败，请重试')
    }
  } finally {
    loading.value = false
  }
}

const fetchUserInfo = async () => {
  try {
    const res = await request.get('/auth/user-info')
    Object.assign(form, res.data)
    Object.assign(originalForm, res.data)
    
    if (form.manageBusiness) {
      selectedBusiness.value = form.manageBusiness.split(',')
    }
    
    if (form.joinPartyDate) {
      joinDateValue.value = new Date(form.joinPartyDate)
    }
    
    if (form.startWorkDate) {
      workDateValue.value = new Date(form.startWorkDate)
    }
  } catch (error) {
    console.error('获取用户信息失败', error)
  }
}

const fetchAuditList = async () => {
  try {
    const res = await request.get('/audit/user-list')
    auditList.value = res.data
  } catch (error) {
    console.error('获取审核记录失败', error)
  }
}

const reeditAudit = (item) => {
  try {
    const newData = JSON.parse(item.newData)
    if (newData.isPartyMember) form.isPartyMember = newData.isPartyMember
    if (newData.partyType) form.partyType = newData.partyType
    if (newData.joinPartyDate) {
      form.joinPartyDate = newData.joinPartyDate
      joinDateValue.value = new Date(newData.joinPartyDate)
    }
    if (newData.partyBranch) form.partyBranch = newData.partyBranch
    if (newData.duty) form.duty = newData.duty
    if (newData.manageBusiness) {
      form.manageBusiness = newData.manageBusiness
      selectedBusiness.value = newData.manageBusiness.split(',')
    }
    if (newData.startWorkDate) {
      form.startWorkDate = newData.startWorkDate
      workDateValue.value = new Date(newData.startWorkDate)
    }
    if (newData.gridArea) form.gridArea = newData.gridArea
    
    isDirty.value = true
    showToast('已恢复上次提交内容')
  } catch (e) {
    console.error('解析审核数据失败', e)
  }
}

onMounted(() => {
  fetchUserInfo()
  fetchAuditList()
})

onActivated(() => {
  fetchUserInfo()
  fetchAuditList()
})

watch(() => form.isPartyMember, (newVal) => {
  if (newVal !== '是') {
    form.partyType = ''
    form.joinPartyDate = ''
    form.partyBranch = ''
  }
})
</script>

<style scoped>
.profile-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.top-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 56px;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  display: flex;
  align-items: center;
  padding: 0 16px;
  z-index: 50;
  box-shadow: 0 2px 8px rgba(200, 16, 46, 0.3);
}

.back-btn {
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 8px;
  color: #FFFFFF;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}

.top-title {
  flex: 1;
  text-align: center;
  font-size: 17px;
  font-weight: bold;
  color: #FFFFFF;
  margin-left: -40px;
}

.role-tag {
  background: rgba(255, 215, 0, 0.2);
  color: #FFD700;
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  font-weight: bold;
}

.profile-header {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  padding: 80px 16px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #fff;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid #FFD700;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  font-weight: bold;
  color: #FFD700;
  margin-bottom: 12px;
}

.user-name {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;
}

.user-role-tag {
  font-size: 13px;
  color: #FFD700;
  background: rgba(0, 0, 0, 0.2);
  padding: 4px 12px;
  border-radius: 12px;
}

.form-section {
  background: #FFFFFF;
  margin: 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(200, 16, 46, 0.08);
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #C8102E;
  margin-bottom: 20px;
  padding-left: 12px;
  border-left: 4px solid #FFD700;
  display: flex;
  align-items: center;
  gap: 8px;
}

.audit-hint {
  font-size: 12px;
  font-weight: normal;
  color: #C8102E;
}

.form-item {
  margin-bottom: 24px;
}

.form-item:last-child {
  margin-bottom: 0;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.required {
  color: #C8102E;
}

.audit-tag {
  font-size: 12px;
  font-weight: normal;
  color: #C8102E;
  background: rgba(200, 16, 46, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid rgba(200, 16, 46, 0.2);
  border-radius: 10px;
  font-size: 15px;
  color: #333;
  box-sizing: border-box;
  background: #FAFAFA;
  transition: all 0.2s;
}

.form-input:focus {
  outline: none;
  border-color: #C8102E;
  box-shadow: 0 0 0 2px rgba(200, 16, 46, 0.1);
}

.form-input.readonly {
  background: #F5F5F5;
  color: #999;
}

.form-input.gold-text {
  color: #FFD700;
  font-weight: bold;
  font-size: 18px;
}

.form-input.picker-trigger {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
}

.picker-arrow {
  color: #999;
  font-size: 18px;
}

.submit-area {
  padding: 20px 16px;
  padding-bottom: calc(env(safe-area-inset-bottom) + 80px);
}

.submit-btn {
  width: 100%;
  height: 48px;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  color: #FFD700;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(200, 16, 46, 0.3);
  transition: all 0.2s;
}

.submit-btn:active {
  transform: scale(0.98);
}

.submit-btn.disabled {
  background: #ccc;
  color: #999;
  box-shadow: none;
  cursor: not-allowed;
}

.empty-tip {
  text-align: center;
  color: #999;
  padding: 30px 0;
}

.audit-list {
  margin-top: 10px;
}

.audit-item {
  background: #F9F9F9;
  border-radius: 10px;
  padding: 15px;
  margin-bottom: 12px;
}

.audit-item:last-child {
  margin-bottom: 0;
}

.audit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.audit-type {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.audit-status {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 10px;
  font-weight: bold;
}

.audit-status.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.audit-status.passed {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.audit-status.rejected {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.audit-time {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.audit-content {
  font-size: 13px;
  color: #666;
}

.audit-opinion {
  padding: 8px;
  background: #FFF5F5;
  border-radius: 6px;
  margin-top: 8px;
}

.opinion-label {
  font-weight: bold;
  color: #C8102E;
}

.audit-action {
  margin-top: 12px;
  text-align: right;
}

.reedit-btn {
  background: #C8102E;
  color: #FFD700;
  font-size: 13px;
  padding: 6px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.multi-select-popup {
  width: 100%;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.popup-cancel {
  color: #999;
  font-size: 15px;
  border: none;
  background: none;
  cursor: pointer;
}

.popup-title {
  font-size: 17px;
  font-weight: bold;
  color: #333;
}

.popup-confirm {
  color: #C8102E;
  font-size: 15px;
  font-weight: bold;
  border: none;
  background: none;
  cursor: pointer;
}

.popup-content {
  padding: 20px;
  max-height: 300px;
  overflow-y: auto;
}
</style>
