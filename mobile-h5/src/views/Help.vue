<template>
  <div class="help-page page-container">
    <van-nav-bar title="帮助与反馈" left-arrow @click-left="onBack" />

    <div class="help-section">
      <div class="section-header">
        <van-icon name="help-circle-o" size="24" color="#D22630" />
        <span class="section-title">常见问题</span>
      </div>
      
      <van-accordion v-model="openId" borderless>
        <van-accordion-item title="如何修改个人信息？" name="1">
          <div class="accordion-content">
            登录后进入"个人中心"，点击"编辑资料"即可修改个人信息。部分信息（如党员身份、村级职务）修改后需管理员审核通过。
          </div>
        </van-accordion-item>
        <van-accordion-item title="如何提交诉求？" name="2">
          <div class="accordion-content">
            在首页或个人中心点击"我的诉求"，选择"新增诉求"，填写诉求类型、详细内容，可上传相关图片，提交后等待处理。您可以随时查看诉求处理进度。
          </div>
        </van-accordion-item>
        <van-accordion-item title="补贴到账时间是多久？" name="3">
          <div class="accordion-content">
            各类补贴按政策规定时间发放，一般在每月固定日期到账。您可在"惠民补贴"模块查看具体发放记录和到账时间。如有疑问，请联系村委会。
          </div>
        </van-accordion-item>
        <van-accordion-item title="如何预约办事？" name="4">
          <div class="accordion-content">
            进入"便民服务"模块，选择需要办理的事项，填写预约时间和联系信息，提交后等待工作人员确认。预约成功后请准时前往服务大厅办理。
          </div>
        </van-accordion-item>
        <van-accordion-item title="忘记密码怎么办？" name="5">
          <div class="accordion-content">
            在登录页面点击"忘记密码"，输入注册手机号，获取验证码后设置新密码。如无法获取验证码，请联系管理员重置密码。
          </div>
        </van-accordion-item>
        <van-accordion-item title="如何查看通知公告？" name="6">
          <div class="accordion-content">
            在首页点击"通知公告"模块，即可查看最新的村务通知、政策公告和重要信息。您可以收藏感兴趣的公告，方便随时查看。
          </div>
        </van-accordion-item>
        <van-accordion-item title="补贴查询显示异常怎么办？" name="7">
          <div class="accordion-content">
            如补贴查询数据显示异常，请检查网络连接。若问题持续存在，请联系村委会会计核实您的个人信息和银行账户信息是否正确。
          </div>
        </van-accordion-item>
      </van-accordion>
    </div>

    <div class="help-section">
      <div class="section-header">
        <van-icon name="message-o" size="24" color="#D22630" />
        <span class="section-title">意见反馈</span>
      </div>

      <van-form @submit="onSubmit">
        <van-field
          v-model="feedbackForm.type"
          label="反馈类型"
          placeholder="请选择反馈类型"
          readonly
          clickable
          @click="showTypePicker = true"
        />
        
        <van-field
          v-model="feedbackForm.content"
          label="反馈内容"
          type="textarea"
          placeholder="请详细描述您遇到的问题或建议，以便我们更好地为您服务..."
          rows="4"
        />
        
        <van-field
          v-model="feedbackForm.contact"
          label="联系方式"
          placeholder="请输入您的联系电话，方便我们联系您"
          type="tel"
          maxlength="11"
        />

        <div class="submit-btn-wrap">
          <van-button type="primary" block native-type="submit" :loading="submitting" class="submit-btn">
            {{ submitting ? '提交中...' : '提交反馈' }}
          </van-button>
        </div>
      </van-form>
    </div>

    <div class="help-section">
      <div class="section-header">
        <van-icon name="phone-o" size="24" color="#D22630" />
        <span class="section-title">联系我们</span>
      </div>
      
      <div class="contact-list">
        <div class="contact-item" @click="callPhone('12345')">
          <div class="contact-icon">📞</div>
          <div class="contact-info">
            <span class="contact-label">服务热线</span>
            <span class="contact-value">12345</span>
          </div>
          <van-icon name="arrow-right" size="16" color="#999" />
        </div>
        <div class="contact-item">
          <div class="contact-icon">📧</div>
          <div class="contact-info">
            <span class="contact-label">电子邮箱</span>
            <span class="contact-value">service@cunwu.gov.cn</span>
          </div>
          <van-icon name="arrow-right" size="16" color="#999" />
        </div>
        <div class="contact-item">
          <div class="contact-icon">📍</div>
          <div class="contact-info">
            <span class="contact-label">办公地址</span>
            <span class="contact-value">罗卜田乡村委会办公楼</span>
          </div>
          <van-icon name="arrow-right" size="16" color="#999" />
        </div>
        <div class="contact-item">
          <div class="contact-icon">🕐</div>
          <div class="contact-info">
            <span class="contact-label">办公时间</span>
            <span class="contact-value">周一至周五 8:30-17:30</span>
          </div>
          <van-icon name="arrow-right" size="16" color="#999" />
        </div>
      </div>
    </div>

    <div class="help-section">
      <div class="section-header">
        <van-icon name="file-text-o" size="24" color="#D22630" />
        <span class="section-title">使用指南</span>
      </div>
      <div class="guide-list">
        <div class="guide-item" @click="viewGuide('user')">
          <div class="guide-icon">👤</div>
          <div class="guide-info">
            <div class="guide-title">用户操作指南</div>
            <div class="guide-desc">详细介绍平台各项功能的使用方法</div>
          </div>
          <van-icon name="arrow-right" size="16" color="#999" />
        </div>
        <div class="guide-item" @click="viewGuide('privacy')">
          <div class="guide-icon">🔒</div>
          <div class="guide-info">
            <div class="guide-title">隐私保护说明</div>
            <div class="guide-desc">了解您的个人信息如何被保护</div>
          </div>
          <van-icon name="arrow-right" size="16" color="#999" />
        </div>
      </div>
    </div>

    <van-popup v-model:show="showTypePicker" position="bottom" :round="true">
      <van-picker :columns="typeOptions" @confirm="onTypeConfirm" @cancel="showTypePicker = false" />
    </van-popup>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()

const openId = ref('')
const showTypePicker = ref(false)
const submitting = ref(false)

const typeOptions = ['功能异常', '操作疑问', '建议反馈', '其他']

const feedbackForm = reactive({
  type: '',
  content: '',
  contact: ''
})

const onBack = () => { goBack(router) }

const onTypeConfirm = ({ selectedOptions }) => {
  feedbackForm.type = selectedOptions[0]
  showTypePicker.value = false
}

const callPhone = (phone) => {
  window.location.href = `tel:${phone}`
}

const viewGuide = (type) => {
  if (type === 'user') {
    showToast('用户操作指南功能开发中')
  } else {
    showToast('隐私保护说明功能开发中')
  }
}

const onSubmit = async () => {
  if (!feedbackForm.type) {
    showToast('请选择反馈类型')
    return
  }
  if (!feedbackForm.content.trim()) {
    showToast('请输入反馈内容')
    return
  }
  
  submitting.value = true
  try {
    await request.post('/feedback/submit', feedbackForm)
    showToast('反馈提交成功，感谢您的意见！')
    feedbackForm.type = ''
    feedbackForm.content = ''
    feedbackForm.contact = ''
  } catch (error) {
    showToast('提交失败，请重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.help-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.help-section {
  background: #fff;
  margin: 0 12px 12px;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.section-title {
  flex: 1;
}

.accordion-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  padding: 8px 0;
}

.submit-btn-wrap {
  margin-top: 16px;
}

.submit-btn {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%) !important;
  border: none !important;
  height: 48px;
  border-radius: 12px;
}

.contact-list {
  display: flex;
  flex-direction: column;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
}

.contact-item:last-child {
  border-bottom: none;
}

.contact-icon {
  font-size: 24px;
  margin-right: 12px;
}

.contact-info {
  flex: 1;
}

.contact-label {
  font-size: 13px;
  color: #999;
  display: block;
}

.contact-value {
  font-size: 15px;
  color: #333;
  font-weight: 500;
}

.guide-list {
  display: flex;
  flex-direction: column;
}

.guide-item {
  display: flex;
  align-items: center;
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
}

.guide-item:last-child {
  border-bottom: none;
}

.guide-icon {
  font-size: 28px;
  margin-right: 12px;
}

.guide-info {
  flex: 1;
}

.guide-title {
  font-size: 15px;
  color: #333;
  font-weight: 500;
  margin-bottom: 4px;
}

.guide-desc {
  font-size: 13px;
  color: #999;
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
