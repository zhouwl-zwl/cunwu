<template>
  <div class="mine-page page-container">
    <div class="top-bar">
      <div class="top-title">个人中心</div>
      <div class="role-tag">{{ roleName }}</div>
    </div>

    <div class="user-card ripple" @click="goProfile">
      <div class="user-avatar-wrapper" @click.stop="showAvatarDialog = true">
        <div class="user-avatar">{{ userInfo.name ? userInfo.name.charAt(0) : '·' }}</div>
        <div class="avatar-edit">
          <van-icon name="camera" size="14" />
        </div>
      </div>
      <div class="user-info">
        <div class="user-name">{{ userInfo.name || '未设置姓名' }}</div>
        <div class="user-group">{{ userInfo.group || '未分配村组' }}</div>
        <div class="user-phone">{{ userInfo.phone || '未绑定手机' }}</div>
      </div>
      <van-icon name="arrow-right" size="22" color="rgba(255,255,255,0.7)" />
    </div>

    <div class="card">
      <div class="section-title">我的服务</div>
      <div class="service-grid">

        <div class="service-item ripple" @click="goMyAppointments">
          <div class="service-icon-wrap gold-bg">
            <van-icon name="calendar-o" size="22" />
          </div>
          <div class="service-name">我的预约</div>
          <div v-if="appointmentCount > 0" class="service-badge">{{ appointmentCount }}</div>
        </div>
        <div class="service-item ripple" @click="goMySubsidies">
          <div class="service-icon-wrap orange-bg">
            <van-icon name="gift" size="22" color="#fff" />
          </div>
          <div class="service-name">我的补贴</div>
        </div>
        <div class="service-item ripple" @click="goMyFavorites">
          <div class="service-icon-wrap red-bg">
            <van-icon name="heart-o" size="22" color="#fff" />
          </div>
          <div class="service-name">我的收藏</div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">业务汇总</div>
      <div class="business-grid">
        <div class="business-item ripple" @click="goMyVolunteer">
          <div class="business-icon-wrap purple-bg">
            <van-icon name="smile-o" size="20" color="#fff" />
          </div>
          <div class="business-info">
            <div class="business-name">我的志愿</div>
            <div class="business-value">{{ volunteerStats.hours }}小时 / {{ volunteerStats.points }}积分</div>
          </div>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
        <div class="business-item ripple" @click="goMyNotifications">
          <div class="business-icon-wrap blue-bg">
            <van-icon name="bell" size="20" />
          </div>
          <div class="business-info">
            <div class="business-name">消息通知</div>
            <div class="business-value">{{ unreadCount }}条日志</div>
          </div>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
        <div class="business-item ripple" @click="goMyParty">
          <div class="business-icon-wrap red-bg">
            <van-icon name="flag-o" size="20" color="#fff" />
          </div>
          <div class="business-info">
            <div class="business-name">党员信息</div>
            <div class="business-value">{{ userInfo.isPartyMember === '是' ? userInfo.partyType || '党员' : '非党员' }}</div>
          </div>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
        <div class="business-item ripple" @click="goMyDocument">
          <div class="business-icon-wrap green-bg">
            <van-icon name="folder" size="20" color="#fff" />
          </div>
          <div class="business-info">
            <div class="business-name">我的档案</div>
            <div class="business-value">查看个人档案</div>
          </div>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
      </div>
    </div>

    <div class="card">
      <div class="section-title">设置</div>
      <div class="setting-list">
        <div class="setting-item ripple" @click="goChangePassword">
          <van-icon name="lock" size="18" color="#D22630" />
          <span class="setting-label">修改密码</span>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
        <div class="setting-item ripple" @click="goProfile">
          <van-icon name="user-o" size="18" color="#D22630" />
          <span class="setting-label">编辑资料</span>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
        <div class="setting-item ripple" @click="goNotificationSettings">
          <van-icon name="bell-o" size="18" color="#D22630" />
          <span class="setting-label">消息通知</span>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
        <div class="setting-item ripple" @click="goHelp">
          <van-icon name="help-o" size="18" color="#D22630" />
          <span class="setting-label">帮助与反馈</span>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
        <div class="setting-item ripple" @click="goAbout">
          <van-icon name="info-o" size="18" color="#D22630" />
          <span class="setting-label">关于我们</span>
          <van-icon name="arrow-right" size="18" color="#999" />
        </div>
      </div>
    </div>

    <div class="logout-btn-wrap">
      <button class="logout-btn ripple" @click="handleLogout">退出登录</button>
    </div>

    <van-dialog v-model:show="showAvatarDialog" title="更换头像">
      <div class="avatar-options">
        <button class="avatar-option ripple" @click="uploadAvatar">
          <van-icon name="camera" size="24" color="#D22630" />
          <span>拍照上传</span>
        </button>
        <button class="avatar-option ripple" @click="chooseAvatar">
          <van-icon name="image" size="24" color="#D22630" />
          <span>从相册选择</span>
        </button>
      </div>
      <template #footer>
        <van-button @click="showAvatarDialog = false">取消</van-button>
      </template>
    </van-dialog>

    <input type="file" ref="fileInput" accept="image/*" style="display: none" @change="handleFileSelect" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()

const userInfo = reactive({
  name: '',
  roleCode: '',
  group: '',
  phone: '',
  isPartyMember: '',
  partyType: ''
})

const showAvatarDialog = ref(false)
const fileInput = ref(null)

const appointmentCount = ref(1)
const unreadCount = ref(25)

const volunteerStats = reactive({
  hours: 28,
  points: 140
})

const roleName = computed(() => {
  const roleMap = {
    'VILLAGER': '普通村民',
    'VILLAGE_OFFICIAL': '村级网格员',
    'RESIDENT_OFFICIAL': '驻村干部',
    'ADMIN': '村级管理员'
  }
  return roleMap[userInfo.roleCode] || '未分配角色'
})

const goProfile = () => {
  router.push('/profile')
}

const goMyAppointments = () => {
  router.push('/appointment/my')
}

const goMySubsidies = () => {
  router.push('/subsidy/my')
}

const goMyFavorites = () => {
  router.push('/favorites')
}

const goMyVolunteer = () => {
  router.push('/volunteer')
}

const goMyNotifications = () => {
  router.push('/notifications')
}

const goMyParty = () => {
  router.push('/profile')
}

const goMyDocument = () => {
  router.push('/archive')
}

const goNotificationSettings = () => {
  router.push('/notification-settings')
}

const goHelp = () => {
  router.push('/help')
}

const goAbout = () => {
  router.push('/about')
}

const goChangePassword = () => {
  router.push('/change-password')
}

const uploadAvatar = () => {
  showAvatarDialog.value = false
  fileInput.value?.click()
}

const chooseAvatar = () => {
  showAvatarDialog.value = false
  fileInput.value?.click()
}

const handleFileSelect = (event) => {
  const file = event.target.files?.[0]
  if (!file) return
  
  if (!file.type.startsWith('image/')) {
    showToast('请选择图片文件')
    return
  }
  
  const maxSize = 2 * 1024 * 1024
  if (file.size > maxSize) {
    showToast('图片大小不能超过2MB')
    return
  }
  
  const formData = new FormData()
  formData.append('avatar', file)
  
  request.post('/auth/upload-avatar', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }).then(() => {
    showToast('头像上传成功')
    fetchUserInfo()
  }).catch(() => {
    showToast('头像上传失败')
  })
  
  event.target.value = ''
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
  showToast('已退出登录')
}

const fetchUserInfo = async () => {
  try {
    const res = await request.get('/auth/user-info')
    userInfo.name = res.name || res.data?.name || ''
    userInfo.roleCode = res.roleCode || res.data?.roleCode || ''
    userInfo.group = res.group || res.data?.group || ''
    userInfo.phone = res.phone || res.data?.phone || ''
    userInfo.isPartyMember = res.isPartyMember || res.data?.isPartyMember || ''
    userInfo.partyType = res.partyType || res.data?.partyType || ''
  } catch (error) {
    console.error('获取用户信息失败', error)
    userInfo.name = '张三'
    userInfo.roleCode = 'VILLAGER'
    userInfo.group = '第一村民组'
    userInfo.phone = '13800138000'
    userInfo.isPartyMember = '否'
  }
}

const fetchCounts = async () => {
  try {
    const res = await request.get('/mine/counts')
    appointmentCount.value = res.appointmentCount || res.data?.appointmentCount || 0
    unreadCount.value = res.unreadCount || res.data?.unreadCount || 0
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

const fetchVolunteerStats = async () => {
  try {
    const res = await request.get('/volunteer/my-stats')
    volunteerStats.hours = res.hours || res.data?.hours || 0
    volunteerStats.points = res.points || res.data?.points || 0
  } catch (error) {
    console.error('获取志愿统计失败', error)
  }
}

onMounted(() => {
  fetchUserInfo()
  fetchCounts()
  fetchVolunteerStats()
})
</script>

<style scoped>
.mine-page {
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

.top-title {
  flex: 1;
  text-align: center;
  font-size: 17px;
  font-weight: bold;
  color: #FFFFFF;
}

.role-tag {
  background: rgba(255, 215, 0, 0.2);
  color: #FFD700;
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  font-weight: bold;
}

.user-card {
  background: linear-gradient(135deg, #D22630 0%, #A80E28 100%);
  margin: 72px 12px 12px;
  border-radius: 20px;
  padding: 24px;
  display: flex;
  align-items: center;
  color: #fff;
  cursor: pointer;
  box-shadow: 0 8px 24px rgba(200, 16, 46, 0.25);
  position: relative;
  overflow: hidden;
}

.user-card::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.08) 0%, transparent 70%);
  border-radius: 50%;
}

.user-avatar-wrapper {
  position: relative;
  margin-right: 14px;
}

.user-avatar {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0.1) 100%);
  border: 3px solid #FFD700;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: bold;
  color: #FFD700;
  box-shadow: 0 4px 12px rgba(255, 215, 0, 0.3);
}

.avatar-edit {
  position: absolute;
  bottom: -2px;
  right: -2px;
  width: 24px;
  height: 24px;
  background: #FFD700;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #C8102E;
}

.user-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 4px;
}

.user-group {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 2px;
}

.user-phone {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.card {
  background: #fff;
  margin: 0 12px 12px;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 4px 16px rgba(210, 38, 48, 0.06);
  border: 1px solid rgba(210, 38, 48, 0.08);
  transition: all 0.25s ease;
}

.card:active {
  transform: scale(0.99);
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.1);
}

.section-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 14px;
  padding-left: 8px;
  border-left: 3px solid #D22630;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.service-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 0;
  position: relative;
  cursor: pointer;
}

.service-icon-wrap {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.25s ease;
}

.service-item:active .service-icon-wrap {
  transform: scale(0.95);
}

.service-icon-wrap.red-bg {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

.service-icon-wrap.gold-bg {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
}

.service-icon-wrap.orange-bg {
  background: linear-gradient(135deg, #FFA500 0%, #FF8C00 100%);
}

.service-icon-wrap.purple-bg {
  background: linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%);
}

.service-icon-wrap.blue-bg {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
}

.service-icon-wrap.green-bg {
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
}

.service-name {
  font-size: 12px;
  color: #666;
}

.service-badge {
  position: absolute;
  top: 4px;
  right: 4px;
  background: #C8102E;
  color: #fff;
  font-size: 10px;
  min-width: 16px;
  height: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}

.business-grid {
  display: flex;
  flex-direction: column;
}

.business-item {
  display: flex;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}

.business-item:last-child {
  border-bottom: none;
}

.business-icon-wrap {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.business-info {
  flex: 1;
}

.business-name {
  font-size: 15px;
  color: #333;
  margin-bottom: 4px;
}

.business-value {
  font-size: 12px;
  color: #999;
}

.setting-list {
  display: flex;
  flex-direction: column;
}

.setting-item {
  display: flex;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-label {
  flex: 1;
  font-size: 15px;
  color: #333;
  margin-left: 10px;
}

.logout-btn-wrap {
  padding: 20px 12px;
}

.logout-btn {
  width: 100%;
  height: 44px;
  background: #fff;
  color: #C8102E;
  font-size: 15px;
  font-weight: bold;
  border: 1px solid #C8102E;
  border-radius: 10px;
  cursor: pointer;
}

.avatar-options {
  display: flex;
  gap: 24px;
  justify-content: center;
  padding: 20px 0;
}

.avatar-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 12px;
  border: none;
  cursor: pointer;
}

.avatar-option span {
  font-size: 13px;
  color: #333;
}

.ripple {
  position: relative;
  overflow: hidden;
  
  &::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    transform: translate(-50%, -50%);
    transition: width 0.4s, height 0.4s;
  }
  
  &:active::after {
    width: 200px;
    height: 200px;
  }
}
</style>