<template>
  <div class="change-password-page page-container">
    <van-nav-bar title="修改密码" left-arrow @click-left="onBack" />

    <div class="form-card">
      <div class="card-header">
        <div class="header-icon">🔐</div>
        <div class="header-title">账号安全</div>
        <div class="header-desc">修改登录密码，保护账号安全</div>
      </div>

      <van-form @submit="onSubmit">
        <van-field 
          v-model="form.oldPassword" 
          type="password" 
          label="当前密码" 
          placeholder="请输入当前密码"
          :show-password="showOldPassword"
          @click-icon="showOldPassword = !showOldPassword"
          required
        />
        
        <van-field 
          v-model="form.newPassword" 
          type="password" 
          label="新密码" 
          placeholder="请输入新密码（至少6位）"
          :show-password="showNewPassword"
          @click-icon="showNewPassword = !showNewPassword"
          required
        />
        
        <van-field 
          v-model="form.confirmPassword" 
          type="password" 
          label="确认新密码" 
          placeholder="请再次输入新密码"
          :show-password="showConfirmPassword"
          @click-icon="showConfirmPassword = !showConfirmPassword"
          required
        />

        <div class="tips">
          <van-icon name="info-o" size="14" color="#999" />
          <span>密码建议：使用字母、数字和特殊字符组合，长度不少于6位</span>
        </div>

        <div class="submit-btn-wrap">
          <van-button type="primary" block native-type="submit" :loading="submitting">
            {{ submitting ? '修改中...' : '确认修改' }}
          </van-button>
        </div>
      </van-form>
    </div>

    <div class="security-tips">
      <div class="tips-title">安全小贴士</div>
      <ul class="tips-list">
        <li>定期更换密码，建议每3个月更换一次</li>
        <li>不要使用生日、手机号等容易猜测的密码</li>
        <li>不同账号使用不同密码，避免密码泄露影响其他账号</li>
        <li>如果忘记密码，请联系村委会管理员重置</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { useUserStore } from '../store/user'
import { goBack } from '../utils/index'

const router = useRouter()
const userStore = useUserStore()

const submitting = ref(false)
const showOldPassword = ref(false)
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)

const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const onBack = () => { goBack(router) }

const onSubmit = async () => {
  if (!form.oldPassword) {
    showToast('请输入当前密码')
    return
  }
  
  if (!form.newPassword) {
    showToast('请输入新密码')
    return
  }
  
  if (form.newPassword.length < 6) {
    showToast('新密码至少6位')
    return
  }
  
  if (!/^[a-zA-Z0-9!@#$%^&*]{6,}$/.test(form.newPassword)) {
    showToast('密码只能包含字母、数字和常见特殊字符')
    return
  }
  
  if (form.newPassword !== form.confirmPassword) {
    showToast('两次输入的密码不一致')
    return
  }
  
  if (form.newPassword === form.oldPassword) {
    showToast('新密码不能与原密码相同')
    return
  }
  
  submitting.value = true
  try {
    await request.post('/auth/change-password', {
      oldPassword: form.oldPassword,
      newPassword: form.newPassword
    })
    showToast('密码修改成功，请重新登录')
    userStore.logout()
    router.push('/login')
  } catch (error) {
    const msg = error?.response?.data?.message || '修改失败，请检查原密码是否正确'
    showToast(msg)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.change-password-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.form-card {
  background: #fff;
  margin: 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.card-header {
  text-align: center;
  margin-bottom: 24px;
}

.header-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.header-desc {
  font-size: 13px;
  color: #999;
}

.tips {
  display: flex;
  align-items: flex-start;
  gap: 6px;
  padding: 12px 16px;
  background: rgba(33, 150, 243, 0.08);
  border-radius: 8px;
  font-size: 13px;
  color: #666;
  margin-bottom: 20px;
}

.submit-btn-wrap {
  margin-top: 10px;
}

.security-tips {
  background: #fff;
  margin: 0 16px;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.tips-title {
  font-size: 16px;
  font-weight: bold;
  color: #C8102E;
  margin-bottom: 16px;
  padding-left: 12px;
  border-left: 4px solid #FFD700;
}

.tips-list {
  margin: 0;
  padding: 0;
  list-style: none;
}

.tips-list li {
  font-size: 14px;
  color: #666;
  line-height: 1.8;
  padding-left: 20px;
  position: relative;
}

.tips-list li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: #4CAF50;
  font-size: 14px;
}

:deep(.van-nav-bar) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

:deep(.van-nav-bar__title) {
  color: #FFD700;
}

:deep(.van-button--primary) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%) !important;
  border: none !important;
}

:deep(.van-form-item__label) {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

:deep(.van-input__control) {
  font-size: 15px;
}
</style>