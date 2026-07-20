<template>
  <div class="register-page">
    <van-nav-bar title="村民注册" left-arrow @click-left="handleBack" />
    
    <div class="register-content">
      <div class="register-card">
        <div class="card-header">
          <div class="header-icon">👤</div>
          <div class="header-title">创建账号</div>
          <div class="header-desc">填写信息完成注册，等待村干部审核</div>
        </div>

        <van-form @submit="onSubmit">
          <van-field
            v-model="phone"
            placeholder="请输入手机号"
            left-icon="phone"
            maxlength="11"
            required
            :error="phoneError"
            error-message="请输入正确的手机号"
            @blur="validatePhone"
          />
          <van-field
            v-model="code"
            placeholder="请输入验证码"
            left-icon="captcha"
            maxlength="4"
            required
          >
            <template #button>
              <van-button 
                size="small" 
                type="primary" 
                @click="getCode" 
                :disabled="countdown > 0"
                class="code-btn"
              >
                {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
              </van-button>
            </template>
          </van-field>
          <van-field
            v-model="password"
            type="password"
            placeholder="请设置登录密码（至少6位）"
            left-icon="lock"
            required
            :error="passwordError"
            error-message="密码不符合要求"
            @blur="validatePassword"
          />
          <van-field
            v-model="confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            left-icon="lock"
            required
            :error="confirmPasswordError"
            error-message="两次密码不一致"
            @blur="validateConfirmPassword"
          />
          <van-field
            v-model="realName"
            placeholder="请输入真实姓名"
            left-icon="user"
            required
          />
          <van-field
            v-model="address"
            placeholder="请输入详细住址"
            left-icon="map-o"
          />

          <div class="password-strength" v-if="password">
            <div class="strength-label">密码强度</div>
            <div class="strength-bars">
              <div :class="['strength-bar', getStrengthClass(1)]"></div>
              <div :class="['strength-bar', getStrengthClass(2)]"></div>
              <div :class="['strength-bar', getStrengthClass(3)]"></div>
            </div>
            <div class="strength-text">{{ getStrengthText() }}</div>
          </div>

          <div class="agreement">
            <van-checkbox v-model="agree">
              我已阅读并同意
              <a href="#" class="agreement-link">《村民服务协议》</a>
              和
              <a href="#" class="agreement-link">《隐私政策》</a>
            </van-checkbox>
          </div>
          <van-button type="primary" native-type="submit" block :loading="loading" class="submit-btn">
            {{ loading ? '注册中...' : '提交注册' }}
          </van-button>
        </van-form>
      </div>
      
      <p class="tips">注册成功后需等待村干部审核，请耐心等待</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { showToast } from 'vant'
import { goBack } from '../utils/index'

const router = useRouter()

onMounted(() => {
  router.push('/')
})

const phone = ref('')
const code = ref('')
const password = ref('')
const confirmPassword = ref('')
const realName = ref('')
const address = ref('')
const agree = ref(false)
const countdown = ref(0)
const loading = ref(false)

const phoneError = ref(false)
const passwordError = ref(false)
const confirmPasswordError = ref(false)

const handleBack = () => {
  goBack(router)
}

const validatePhone = () => {
  phoneError.value = !/^1[3-9]\d{9}$/.test(phone.value)
}

const validatePassword = () => {
  if (!password.value) {
    passwordError.value = false
    return
  }
  passwordError.value = password.value.length < 6 || !/^[a-zA-Z0-9!@#$%^&*]{6,}$/.test(password.value)
}

const validateConfirmPassword = () => {
  confirmPasswordError.value = password.value !== confirmPassword.value
}

const getStrengthClass = (level) => {
  const strength = getPasswordStrength()
  if (strength >= level) {
    return 'active level' + strength
  }
  return ''
}

const getPasswordStrength = () => {
  let strength = 0
  if (password.value.length >= 6) strength++
  if (password.value.length >= 8) strength++
  if (/[a-zA-Z]/.test(password.value) && /[0-9]/.test(password.value)) strength++
  if (/[!@#$%^&*]/.test(password.value)) strength++
  return Math.min(strength, 3)
}

const getStrengthText = () => {
  const strength = getPasswordStrength()
  if (strength === 1) return '弱'
  if (strength === 2) return '中'
  if (strength === 3) return '强'
  return ''
}

const getCode = async () => {
  if (!phone.value) {
    showToast('请输入手机号')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(phone.value)) {
    showToast('请输入正确的手机号')
    return
  }
  try {
    await request.get('/auth/verify-code', { params: { phone: phone.value } })
    showToast('验证码已发送')
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    showToast('发送验证码失败，请稍后重试')
  }
}

const onSubmit = async () => {
  if (!phone.value || !code.value || !password.value || !realName.value) {
    showToast('请填写完整信息')
    return
  }
  if (!/^1[3-9]\d{9}$/.test(phone.value)) {
    showToast('请输入正确的手机号')
    return
  }
  if (password.value.length < 6) {
    showToast('密码至少6位')
    return
  }
  if (password.value !== confirmPassword.value) {
    showToast('两次输入的密码不一致')
    return
  }
  if (!agree.value) {
    showToast('请同意服务协议')
    return
  }
  
  loading.value = true
  try {
    await request.post('/auth/register', {
      phone: phone.value,
      password: password.value,
      realName: realName.value,
      address: address.value
    })
    showToast('注册成功，请等待审核')
    router.push('/login')
  } catch (error) {
    showToast(error.response?.data?.message || error.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: #FDF5F5;
}

.register-content {
  padding: 24px 16px;
}

.register-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
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

.password-strength {
  margin-bottom: 16px;
  padding: 12px;
  background: #FAFAFA;
  border-radius: 8px;
}

.strength-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.strength-bars {
  display: flex;
  gap: 6px;
  margin-bottom: 6px;
}

.strength-bar {
  flex: 1;
  height: 6px;
  background: #eee;
  border-radius: 3px;
  transition: all 0.3s;
}

.strength-bar.active {
  background: #D22630;
}

.strength-bar.level2 {
  background: linear-gradient(90deg, #D22630 0%, #FFD700 100%);
}

.strength-bar.level3 {
  background: #FFD700;
}

.strength-text {
  font-size: 12px;
  color: #999;
  text-align: right;
}

.agreement {
  margin-bottom: 24px;
  font-size: 12px;
}

.agreement-link {
  color: #C8102E;
}

.tips {
  text-align: center;
  color: #969799;
  font-size: 12px;
  margin-top: 16px;
}

.code-btn {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%) !important;
  border: none !important;
}

.submit-btn {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%) !important;
  border: none !important;
  height: 48px;
  border-radius: 12px;
  font-weight: bold;
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

:deep(.van-checkbox__icon) {
  border-color: #C8102E !important;
}

:deep(.van-checkbox__icon--checked) {
  background: #C8102E !important;
  border-color: #C8102E !important;
}
</style>
