
<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <div class="login-logo">
          <div class="logo-star">⭐</div>
        </div>
        <h1 class="login-title">村级智慧村务管理系统</h1>
        <p class="login-subtitle">乡村治理一体化平台</p>
      </div>
      <el-form :model="loginForm" label-width="80px" @submit.prevent="handleLogin">
        <el-form-item label="账号">
          <el-input v-model="loginForm.username" placeholder="请输入账号或手机号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%;" @click="handleLogin">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="login-footer">
        <span>村民、村干部、驻村干部统一登录入口</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const router = useRouter()

const loginForm = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  try {
    console.log('Login request:', loginForm)
    const res = await request.post('/auth/login', loginForm)
    console.log('Login response:', res)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('role', res.data.user.role)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    console.error('Login error:', error)
    const msg = error.response?.data?.message || error.response?.statusText || '登录失败，请检查账号密码'
    ElMessage.error(msg)
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 50%, #8B0000 100%);
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -20%;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.15) 0%, transparent 70%);
  border-radius: 50%;
}

.login-container::after {
  content: '';
  position: absolute;
  bottom: -30%;
  left: -10%;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.1) 0%, transparent 70%);
  border-radius: 50%;
}

.login-box {
  width: 420px;
  padding: 48px 40px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
  position: relative;
  z-index: 1;
  border: 2px solid rgba(255, 215, 0, 0.3);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-logo {
  margin-bottom: 16px;
}

.logo-star {
  font-size: 64px;
  filter: drop-shadow(0 4px 8px rgba(210, 38, 48, 0.3));
}

.login-title {
  margin-bottom: 8px;
  color: #D22630;
  font-size: 28px;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(210, 38, 48, 0.1);
}

.login-subtitle {
  color: #FFD700;
  font-size: 16px;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
  color: #999;
  font-size: 12px;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%) !important;
  border: none !important;
  border-radius: 8px !important;
  height: 48px !important;
  line-height: 48px !important;
  font-size: 16px !important;
  font-weight: bold !important;
  box-shadow: 0 4px 16px rgba(210, 38, 48, 0.4) !important;
}

:deep(.el-button--primary:hover) {
  opacity: 0.95;
  transform: translateY(-1px);
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  border: 1px solid rgba(210, 38, 48, 0.2);
  transition: all 0.3s;
}

:deep(.el-input__wrapper:hover) {
  border-color: rgba(210, 38, 48, 0.4);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #D22630;
  box-shadow: 0 0 0 2px rgba(210, 38, 48, 0.1);
}

:deep(.el-form-item__label) {
  color: #666;
  font-weight: 500;
}
</style>
