<template>
  <div class="about-page page-container">
    <van-nav-bar title="关于我们" left-arrow @click-left="onBack" />

    <div class="about-header">
      <div class="app-logo">🏠</div>
      <div class="app-name">罗卜田乡智慧村务平台</div>
      <div class="app-version">版本号 v1.0.0</div>
    </div>

    <div class="about-card">
      <div class="card-header">
        <van-icon name="info-o" size="20" color="#D22630" />
        <span class="card-title">平台介绍</span>
      </div>
      <div class="card-content">
        <p>罗卜田乡智慧村务平台是为村级治理量身打造的一站式综合服务系统，致力于提升村务管理效率，方便村民办事，推动乡村治理数字化转型。</p>
        <p>我们秉承"服务村民、便捷高效"的理念，提供通知公告、惠民补贴、诉求上报、在线预约等多项便民服务，让数据多跑路，让村民少跑腿。</p>
      </div>
    </div>

    <div class="about-card">
      <div class="card-header">
        <van-icon name="feature-o" size="20" color="#D22630" />
        <span class="card-title">核心功能</span>
      </div>
      <div class="card-content">
        <div class="feature-grid">
          <div class="feature-item">
            <div class="feature-icon">👥</div>
            <div class="feature-name">人口户籍</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">📄</div>
            <div class="feature-name">通知公告</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">💰</div>
            <div class="feature-name">惠民补贴</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">❤️</div>
            <div class="feature-name">民政社保</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">🌿</div>
            <div class="feature-name">人居环境</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">⚖️</div>
            <div class="feature-name">信访调解</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">🏗️</div>
            <div class="feature-name">项目工程</div>
          </div>
          <div class="feature-item">
            <div class="feature-icon">📦</div>
            <div class="feature-name">物资仓库</div>
          </div>
        </div>
      </div>
    </div>

    <div class="about-card">
      <div class="card-header">
        <van-icon name="shield-o" size="20" color="#D22630" />
        <span class="card-title">隐私政策</span>
      </div>
      <div class="card-content">
        <p>我们重视您的隐私保护，严格遵守国家相关法律法规。您的个人信息仅用于业务办理和服务提供，未经授权不会向第三方泄露。</p>
        <button class="policy-btn" @click="showPolicyDetail">查看完整隐私政策</button>
      </div>
    </div>

    <div class="about-card">
      <div class="card-header">
        <van-icon name="file-text-o" size="20" color="#D22630" />
        <span class="card-title">服务协议</span>
      </div>
      <div class="card-content">
        <p>使用本平台即表示您同意《罗卜田乡智慧村务平台服务协议》，请在使用前仔细阅读相关条款。</p>
        <button class="policy-btn" @click="showServiceAgreement">查看服务协议</button>
      </div>
    </div>

    <div class="about-card">
      <div class="card-header">
        <van-icon name="copyright" size="20" color="#D22630" />
        <span class="card-title">版权信息</span>
      </div>
      <div class="card-content">
        <p>© 2024 罗卜田乡村委会 版权所有</p>
        <p>技术支持：村级智慧村务系统开发团队</p>
        <p>备案号：京ICP备XXXXXXXX号</p>
      </div>
    </div>

    <div class="about-card">
      <div class="card-header">
        <van-icon name="phone-o" size="20" color="#D22630" />
        <span class="card-title">联系我们</span>
      </div>
      <div class="card-content">
        <div class="contact-row">
          <span class="contact-label">服务热线</span>
          <span class="contact-value" @click="callPhone">12345</span>
        </div>
        <div class="contact-row">
          <span class="contact-label">电子邮箱</span>
          <span class="contact-value">service@cunwu.gov.cn</span>
        </div>
        <div class="contact-row">
          <span class="contact-label">办公地址</span>
          <span class="contact-value">罗卜田乡村委会办公楼</span>
        </div>
      </div>
    </div>

    <div class="update-check">
      <van-button type="primary" block plain @click="checkUpdate" :loading="checking">
        {{ checking ? '检查更新中...' : '检查更新' }}
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { goBack } from '../utils/index'

const router = useRouter()
const checking = ref(false)

const onBack = () => { goBack(router) }

const callPhone = () => {
  window.location.href = 'tel:12345'
}

const checkUpdate = async () => {
  checking.value = true
  await new Promise(resolve => setTimeout(resolve, 1500))
  checking.value = false
  showToast('当前已是最新版本')
}

const showPolicyDetail = () => {
  showToast('隐私政策详情功能开发中')
}

const showServiceAgreement = () => {
  showToast('服务协议详情功能开发中')
}
</script>

<style scoped>
.about-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.about-header {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  padding: 48px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #fff;
  margin-bottom: 16px;
}

.app-logo {
  font-size: 72px;
  margin-bottom: 16px;
}

.app-name {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 8px;
}

.app-version {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.about-card {
  background: #fff;
  margin: 0 12px 12px;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.card-title {
  flex: 1;
}

.card-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.card-content p {
  margin-bottom: 12px;
}

.card-content p:last-child {
  margin-bottom: 0;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.feature-icon {
  font-size: 32px;
}

.feature-name {
  font-size: 12px;
  color: #666;
  text-align: center;
}

.policy-btn {
  background: rgba(210, 38, 48, 0.1);
  color: #D22630;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  margin-top: 12px;
  cursor: pointer;
}

.contact-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.contact-row:last-child {
  border-bottom: none;
}

.contact-label {
  font-size: 14px;
  color: #999;
}

.contact-value {
  font-size: 14px;
  color: #333;
}

.update-check {
  padding: 20px 12px;
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
</style>
