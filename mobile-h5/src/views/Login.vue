<template>
  <div class="login-page" :class="{ loading: isLoading }">
    <div v-if="isLoading" class="loading-overlay">
      <div class="loading-star"></div>
      <p>智慧村务平台</p>
      <div class="loading-spinner"></div>
    </div>
    
    <div v-if="!isLoading" class="login-container">
      <div class="brand-section">
        <div class="brand-content">
          <div class="brand-star"></div>
          <h1>村级智慧村务</h1>
          <div class="decorative-line"></div>
          <p>乡村治理一体化平台</p>
        </div>
        <div class="brand-bg-decoration"></div>
        <div class="brand-features">
          <div class="feature-item">
            <svg viewBox="0 0 24 24" class="feature-icon">
              <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V9z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
            <span>村务管理</span>
          </div>
          <div class="feature-item">
            <svg viewBox="0 0 24 24" class="feature-icon">
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
            </svg>
            <span>乡村振兴</span>
          </div>
          <div class="feature-item">
            <svg viewBox="0 0 24 24" class="feature-icon">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
            </svg>
            <span>村民服务</span>
          </div>
        </div>
      </div>
      
      <div class="form-section">
        <div class="safe-area-top"></div>
        
        <div class="form-card" ref="formCard">
          <div class="card-header">
            <h2>欢迎登录</h2>
            <p>请输入账号信息</p>
          </div>
          
          <form @submit.prevent="onSubmit" class="form-container">
            <div class="form-item">
              <div class="input-wrapper" :class="{ focused: focusedField === 'username', error: usernameError }">
                <svg viewBox="0 0 24 24" class="input-icon">
                  <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                </svg>
                <input
                  v-model="username"
                  type="text"
                  placeholder="请输入账号（村干部账号/村民手机号）"
                  class="input-field"
                  @focus="handleInputFocus('username')"
                  @blur="handleInputBlur"
                />
                <button type="button" v-if="username" class="input-clear" @click="username = ''">
                  <svg viewBox="0 0 24 24">
                    <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
                  </svg>
                </button>
              </div>
              <span v-if="usernameError" class="error-text">{{ usernameError }}</span>
            </div>
            
            <div class="form-item">
              <div class="input-wrapper" :class="{ focused: focusedField === 'password', error: passwordError }">
                <svg viewBox="0 0 24 24" class="input-icon">
                  <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/>
                </svg>
                <input
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="请输入登录密码"
                  class="input-field"
                  @focus="handleInputFocus('password')"
                  @blur="handleInputBlur"
                />
                <button type="button" class="input-eye" @click="togglePassword">
                  <svg v-if="showPassword" viewBox="0 0 24 24">
                    <path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/>
                  </svg>
                  <svg v-else viewBox="0 0 24 24">
                    <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                  </svg>
                </button>
              </div>
              <span v-if="passwordError" class="error-text">{{ passwordError }}</span>
            </div>
            
            <div class="form-options">
              <label class="remember-checkbox">
                <input type="checkbox" v-model="rememberMe" />
                <span class="checkbox-custom"></span>
                <span class="checkbox-label">记住登录</span>
              </label>
              <a href="#" class="forgot-password" @click.prevent="goForgotPassword">忘记密码？</a>
            </div>
            
            <div class="compliance-section">
              <label class="agreement-checkbox">
                <input type="checkbox" v-model="agreeTerms" />
                <span class="checkbox-custom"></span>
                <span class="checkbox-text">
                  我已阅读并同意
                  <a href="#" @click.prevent="showAgreement">《用户服务协议》</a>
                  和
                  <a href="#" @click.prevent="showPrivacy">《隐私保护说明》</a>
                </span>
              </label>
            </div>
            
            <button 
              type="submit" 
              class="login-btn"
              :disabled="!canSubmit"
              :class="{ loading: loading }"
            >
              <span v-if="loading" class="btn-loading"></span>
              {{ loading ? '登录中...' : '登 录' }}
            </button>
          </form>
          
          <div class="register-link">
            <span>还没有账号？</span>
            <a href="/register" @click.prevent="goRegister">
              <span class="register-text">立即注册</span>
            </a>
          </div>
        </div>
        
        <div class="login-footer">
          <div class="footer-info">
            <svg viewBox="0 0 24 24" class="footer-icon">
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z"/>
            </svg>
            <span>本村村民、村干部、驻村干部统一登录通道</span>
          </div>
          <div class="footer-security">
            <svg viewBox="0 0 24 24" class="footer-icon">
              <path d="M12 1a11 11 0 0 0-4.18 21.74L12 19.87l4.18 2.97A11 11 0 0 0 12 1zm0 19a8 8 0 1 1 0-16 8 8 0 0 1 0 16zm3-7a1 1 0 1 1 0-2 1 1 0 0 1 0 2zm-6 0a1 1 0 1 1 0-2 1 1 0 0 1 0 2z"/>
            </svg>
            <span>安全加密登录，请放心使用</span>
          </div>
        </div>
        
        <div class="safe-area-bottom"></div>
      </div>
    </div>

    <div v-if="showAgreementDialog" class="dialog-overlay" @click.self="showAgreementDialog = false">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>用户服务协议</h3>
          <button class="dialog-close" @click="showAgreementDialog = false">
            <svg viewBox="0 0 24 24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        <div class="dialog-body">
          <div class="agreement-columns">
            <div class="agreement-column">
              <p><span class="highlight">欢迎</span>使用村级智慧村务平台。请您仔细阅读以下服务协议...</p>
              <p><span class="number">1.</span> 用户应遵守国家法律法规，不得利用平台从事违法活动。</p>
              <p><span class="number">2.</span> 用户应对自身账号安全负责，不得转借他人使用。</p>
            </div>
            <div class="agreement-column">
              <p><span class="number">3.</span> 平台有权根据需要更新服务协议，用户继续使用即表示同意。</p>
              <p><span class="number">4.</span> 用户在使用平台服务时，应遵守平台的相关规定。</p>
              <p><span class="number">5.</span> 平台对用户提供的服务仅供个人使用，禁止商业用途。</p>
            </div>
          </div>
        </div>
        <div class="dialog-footer">
          <button class="dialog-btn dialog-cancel" @click="showAgreementDialog = false">取消</button>
          <button class="dialog-btn dialog-confirm" @click="showAgreementDialog = false">确认</button>
        </div>
      </div>
    </div>
    
    <div v-if="showPrivacyDialog" class="dialog-overlay" @click.self="showPrivacyDialog = false">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>隐私保护说明</h3>
          <button class="dialog-close" @click="showPrivacyDialog = false">
            <svg viewBox="0 0 24 24">
              <path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
        <div class="dialog-body">
          <div class="agreement-columns">
            <div class="agreement-column">
              <p><span class="highlight">我们重视</span>您的隐私保护，以下是我们的隐私政策...</p>
              <p><span class="number">1.</span> 我们仅收集必要的个人信息用于服务提供。</p>
              <p><span class="number">2.</span> 您的信息将被安全存储，不会泄露给第三方。</p>
            </div>
            <div class="agreement-column">
              <p><span class="number">3.</span> 您有权随时查阅、修改或删除您的个人信息。</p>
              <p><span class="number">4.</span> 我们采用加密技术保护您的信息安全。</p>
              <p><span class="number">5.</span> 如政策有变更，我们将及时通知您。</p>
            </div>
          </div>
        </div>
        <div class="dialog-footer">
          <button class="dialog-btn dialog-cancel" @click="showPrivacyDialog = false">取消</button>
          <button class="dialog-btn dialog-confirm" @click="showPrivacyDialog = false">确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import request from '../utils/request'
import { showToast } from 'vant'
import { setCurrentRole } from '../utils/permission'

const router = useRouter()
const userStore = useUserStore()

const username = ref('')
const password = ref('')
const rememberMe = ref(false)
const agreeTerms = ref(true)
const loading = ref(false)
const isLoading = ref(true)
const showAgreementDialog = ref(false)
const showPrivacyDialog = ref(false)
const showPassword = ref(false)
const focusedField = ref('')
const formCard = ref(null)
const usernameError = ref('')
const passwordError = ref('')

const canSubmit = computed(() => {
  return username.value.trim() && password.value.trim() && agreeTerms.value && !loading.value
})

const validateForm = () => {
  username.value = username.value.trim()
  usernameError.value = ''
  passwordError.value = ''
  
  if (!username.value) {
    usernameError.value = '请输入账号'
    return false
  }
  
  if (!password.value) {
    passwordError.value = '请输入密码'
    return false
  }
  
  if (password.value.length < 6) {
    passwordError.value = '密码长度不少于6位'
    return false
  }
  
  if (!agreeTerms.value) {
    showToast('请先阅读并同意服务协议')
    return false
  }
  
  return true
}

const onSubmit = async () => {
  if (!validateForm() || loading.value) return
  
  loading.value = true
  try {
    const res = await request.post('/auth/login', {
      username: username.value,
      password: password.value
    })
    
    if (!res) {
      loading.value = false
      return
    }
    
    const token = res.token || res.data?.token
    const user = res.user || res.data?.user
    
    if (!token) {
      showToast('登录失败：未获取到令牌')
      loading.value = false
      return
    }
    
    userStore.setToken(token)
    userStore.setUserInfo(user)
    
    if (user?.roleCode) {
      setCurrentRole(user.roleCode)
    }
    
    if (rememberMe.value) {
      localStorage.setItem('savedUsername', username.value)
    } else {
      localStorage.removeItem('savedUsername')
    }
    
    showToast('登录成功')
    setTimeout(() => {
      router.push('/')
    }, 500)
  } catch (error) {
    const message = error.response?.data?.message || error.message || '登录失败'
    showToast(message)
  } finally {
    loading.value = false
  }
}

const goRegister = () => {
  router.push('/register')
}

const goForgotPassword = () => {
  showToast('请联系管理员重置密码')
}

const showAgreement = () => {
  showAgreementDialog.value = true
}

const showPrivacy = () => {
  showPrivacyDialog.value = true
}

const togglePassword = () => {
  showPassword.value = !showPassword.value
}

const handleInputFocus = (field) => {
  focusedField.value = field
}

const handleInputBlur = () => {
  focusedField.value = ''
}

onMounted(() => {
  setTimeout(() => {
    isLoading.value = false
    router.push('/')
  }, 1200)
  
  window.addEventListener('resize', handleResize)
})

const handleResize = () => {
  if (focusedField.value && formCard.value) {
    formCard.value.scrollIntoView({ behavior: 'smooth', block: 'center' })
  }
}

watch([username, password], () => {
  usernameError.value = ''
  passwordError.value = ''
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #FDF5F5 0%, #FAEEEE 50%, #F5DADA 100%);
  display: flex;
  overflow: hidden;
  position: relative;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.login-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M30 5 L35 20 L50 20 L38 30 L43 45 L30 35 L17 45 L22 30 L10 20 L25 20 Z' fill='%23C8102E' fill-opacity='0.04'/%3E%3C/svg%3E");
  background-size: 60px 60px;
  background-repeat: repeat;
  background-position: 0 0;
  pointer-events: none;
}

.login-container {
  flex: 1;
  display: flex;
  animation: pageFadeIn 0.6s ease-out;
}

@keyframes pageFadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, #C8102E 0%, #A80E28 50%, #880C22 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: loadingFadeOut 0.4s ease-in 0.8s forwards;
}

@keyframes loadingFadeOut {
  to { opacity: 0; pointer-events: none; }
}

.loading-star {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #FFD700 0%, #FFA500 50%, #E5C100 100%);
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
  animation: breathe 2s ease-in-out infinite;
  margin-bottom: 20px;
}

@keyframes breathe {
  0%, 100% { opacity: 0.7; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.05); }
}

.loading-overlay p {
  color: #fff;
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 24px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 215, 0, 0.3);
  border-top-color: #FFD700;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.brand-section {
  flex: 1;
  background: linear-gradient(180deg, #C8102E 0%, #A80E28 50%, #880C22 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  position: relative;
  overflow: hidden;
  padding: 40px 20px;
}

.brand-bg-decoration {
  position: absolute;
  top: -10%;
  right: -10%;
  width: 60%;
  height: 60%;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.08) 0%, transparent 70%);
  border-radius: 50%;
}

.brand-content {
  text-align: center;
  color: #fff;
  z-index: 1;
  padding: 20px 0;
}

.brand-star {
  width: 80px;
  height: 80px;
  margin: 0 auto 24px;
  background: linear-gradient(135deg, #FFD700 0%, #FFA500 50%, #E5C100 100%);
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
  box-shadow: 0 4px 20px rgba(255, 215, 0, 0.4), inset 0 2px 4px rgba(255, 255, 255, 0.5);
  animation: fadeInScale 0.8s ease-out;
}

@keyframes fadeInScale {
  from { opacity: 0; transform: scale(0.5); }
  to { opacity: 1; transform: scale(1); }
}

.brand-content h1 {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 12px;
  letter-spacing: 2px;
  color: #FFFFFF;
}

.decorative-line {
  width: 80px;
  height: 3px;
  background: linear-gradient(90deg, #FFD700, #FFE499, #FFD700);
  margin: 0 auto 16px;
  border-radius: 2px;
}

.brand-content p {
  font-size: 16px;
  color: #FFE499;
}

.brand-features {
  display: flex;
  width: 100%;
  justify-content: space-around;
  z-index: 1;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 215, 0, 0.1);
}

.feature-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 12px 0;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 215, 0, 0.1);
}

.feature-item:hover .feature-icon,
.feature-item:hover span {
  color: #FFD700;
}

.feature-icon {
  width: 24px;
  height: 24px;
  fill: none;
  stroke: rgba(255, 255, 255, 0.8);
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
  transition: stroke 0.3s ease;
}

.feature-item span {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.75);
  transition: color 0.3s ease;
}

.form-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 40px;
  justify-content: center;
  max-width: 500px;
  margin: 0 auto;
}

.safe-area-top {
  height: env(safe-area-inset-top);
}

.safe-area-bottom {
  height: env(safe-area-inset-bottom);
}

.form-card {
  background: #fff;
  border-radius: 20px;
  padding: 36px 24px;
  box-shadow: 0 12px 40px rgba(200, 16, 46, 0.12), 0 2px 8px rgba(0, 0, 0, 0.04);
  animation: cardSlideUp 0.6s ease-out;
  border: 1px solid rgba(200, 16, 46, 0.08);
}

@keyframes cardSlideUp {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

.card-header {
  margin-bottom: 32px;
}

.card-header h2 {
  font-size: 24px;
  font-weight: 700;
  color: #C8102E;
  margin-bottom: 8px;
}

.card-header p {
  font-size: 13px;
  color: #666;
}

.form-container {
  margin: 0;
}

.form-item {
  margin-bottom: 24px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  background: #FDFDFD;
  border: 2px solid #F0F0F0;
  border-radius: 16px;
  padding: 0 16px;
  height: 52px;
  transition: all 0.25s ease;
}

.input-wrapper.focused {
  background: #fff;
  border-color: #C8102E;
  box-shadow: 0 0 0 4px rgba(200, 16, 46, 0.08);
}

.input-wrapper.error {
  border-color: #C8102E;
  box-shadow: 0 0 0 4px rgba(200, 16, 46, 0.1);
}

.input-icon {
  width: 18px;
  height: 18px;
  fill: none;
  stroke: #FFD700;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
  margin-right: 12px;
  flex-shrink: 0;
  transition: stroke 0.3s ease;
}

.input-wrapper.focused .input-icon {
  stroke: #C8102E;
}

.input-field {
  flex: 1;
  height: 100%;
  border: none;
  outline: none;
  background: transparent;
  font-size: 15px;
  color: #333;
}

.input-field::placeholder {
  color: #999;
}

.input-clear,
.input-eye {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.input-clear svg,
.input-eye svg {
  width: 18px;
  height: 18px;
  fill: none;
  stroke: #999;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.input-clear:hover svg,
.input-eye:hover svg {
  stroke: #C8102E;
}

.error-text {
  font-size: 13px;
  color: #C8102E;
  margin-top: 6px;
  display: block;
  animation: shake 0.3s ease;
}

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-4px); }
  75% { transform: translateX(4px); }
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.remember-checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.remember-checkbox input {
  display: none;
}

.checkbox-custom {
  width: 18px;
  height: 18px;
  border: 2px solid #C8102E;
  border-radius: 4px;
  position: relative;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.remember-checkbox input:checked + .checkbox-custom {
  background: #C8102E;
  border-color: #C8102E;
}

.remember-checkbox input:checked + .checkbox-custom::after {
  content: '';
  position: absolute;
  top: 2px;
  left: 6px;
  width: 5px;
  height: 10px;
  border: solid #FFD700;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-label {
  font-size: 14px;
  color: #333;
}

.forgot-password {
  color: #C8102E;
  font-size: 14px;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background 0.3s ease;
}

.forgot-password:hover {
  background: rgba(200, 16, 46, 0.08);
}

.compliance-section {
  margin-bottom: 28px;
}

.agreement-checkbox {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  cursor: pointer;
}

.agreement-checkbox input {
  display: none;
}

.agreement-checkbox .checkbox-custom {
  width: 16px;
  height: 16px;
  margin-top: 2px;
}

.agreement-checkbox input:checked + .checkbox-custom {
  background: #C8102E;
  border-color: #C8102E;
}

.agreement-checkbox input:checked + .checkbox-custom::after {
  content: '';
  position: absolute;
  top: 1px;
  left: 5px;
  width: 4px;
  height: 8px;
  border: solid #FFD700;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-text {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
}

.checkbox-text a {
  color: #C8102E;
  text-decoration: underline;
}

.login-btn {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  border: none;
  border-radius: 16px;
  color: #fff;
  font-size: 17px;
  font-weight: 700;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 6px 20px rgba(200, 16, 46, 0.35);
  transition: all 0.25s ease;
  position: relative;
  overflow: hidden;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: left 0.5s ease;
}

.login-btn:hover::before {
  left: 100%;
}

.login-btn:not(:disabled):active {
  transform: scale(0.98);
  background: linear-gradient(135deg, #A80E28 0%, #880C22 100%);
  box-shadow: 0 3px 10px rgba(200, 16, 46, 0.3);
}

.login-btn:disabled {
  background: linear-gradient(135deg, #E0E0E0 0%, #D0D0D0 100%);
  color: #999;
  box-shadow: none;
  cursor: not-allowed;
}

.login-btn.loading {
  pointer-events: none;
}

.btn-loading {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.register-link {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: #666;
}

.register-link a {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  text-decoration: none;
}

.register-text {
  color: #C8102E;
  font-weight: 700;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  padding: 0 16px;
}

.footer-info,
.footer-security {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
}

.footer-icon {
  width: 14px;
  height: 14px;
  fill: none;
  stroke: #C8102E;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 20px;
  animation: fadeIn 0.2s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.dialog-content {
  background: #FDF0F0;
  border-radius: 12px;
  width: 100%;
  max-width: 600px;
  max-height: 70vh;
  overflow: hidden;
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #F0D0D0;
}

.dialog-header h3 {
  font-size: 18px;
  font-weight: 700;
  color: #C8102E;
  margin: 0;
}

.dialog-close {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
}

.dialog-close svg {
  width: 20px;
  height: 20px;
  fill: none;
  stroke: #666;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.dialog-body {
  padding: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.agreement-columns {
  display: flex;
  gap: 24px;
}

.agreement-column {
  flex: 1;
}

.agreement-column p {
  font-size: 14px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 16px;
}

.agreement-column p:last-child {
  margin-bottom: 0;
}

.agreement-column .highlight {
  color: #C8102E;
  font-weight: 700;
}

.agreement-column .number {
  color: #C8102E;
  font-weight: 600;
  margin-right: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 20px;
  border-top: 1px solid #F0D0D0;
}

.dialog-btn {
  padding: 10px 24px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
}

.dialog-cancel {
  background: #fff;
  color: #666;
  border: 1px solid #F0D0D0;
}

.dialog-cancel:hover {
  background: #FAFAFA;
}

.dialog-confirm {
  background: #C8102E;
  color: #fff;
  border: 1px solid #FFD700;
}

.dialog-confirm:hover {
  background: #A80E28;
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    min-height: 100vh;
  }
  
  .brand-section {
    flex: none;
    padding: 28px 20px;
    min-height: 180px;
    background: linear-gradient(180deg, #C8102E 0%, #A80E28 100%);
  }
  
  .brand-content {
    padding: 0;
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  
  .brand-star {
    width: 52px;
    height: 52px;
    margin-bottom: 10px;
  }
  
  .brand-content h1 {
    font-size: 22px;
    letter-spacing: 1px;
  }
  
  .brand-content p {
    font-size: 13px;
  }
  
  .brand-features {
    padding-top: 12px;
  }
  
  .feature-icon {
    width: 18px;
    height: 18px;
  }
  
  .feature-item span {
    font-size: 11px;
  }
  
  .form-section {
    flex: 1;
    padding: 24px 16px;
    justify-content: flex-start;
    padding-top: 0;
  }
  
  .form-card {
    padding: 28px 20px;
    border-radius: 20px;
    margin-top: -20px;
    z-index: 10;
    box-shadow: 0 8px 32px rgba(200, 16, 46, 0.12);
  }
  
  .card-header {
    margin-bottom: 24px;
  }
  
  .card-header h2 {
    font-size: 20px;
  }
  
  .form-item {
    margin-bottom: 20px;
  }
  
  .input-wrapper {
    height: 48px;
    border-radius: 14px;
  }
  
  .login-btn {
    height: 48px;
    font-size: 16px;
    border-radius: 14px;
  }
  
  .form-options {
    margin-bottom: 20px;
  }
  
  .compliance-section {
    margin-bottom: 24px;
  }
  
  .footer-info,
  .footer-security {
    font-size: 11px;
  }
  
  .agreement-columns {
    flex-direction: column;
    gap: 12px;
  }
  
  .dialog-content {
    margin: 12px;
    border-radius: 16px;
  }
  
  .dialog-body {
    padding: 16px;
  }
  
  .dialog-footer {
    padding: 12px 16px;
  }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .brand-content h1 {
    font-size: 28px;
  }
  
  .brand-features {
    gap: 20px;
  }
  
  .form-section {
    padding: 30px;
  }
}
</style>
