<template>
  <div class="demand-page page-container">
    <van-nav-bar title="我为群众办实事" left-arrow @click-left="handleBack" />
    
    <div class="demand-content">
      <div class="demand-card">
        <div class="card-header">
          <div class="header-icon-wrap">
            <van-icon name="volume-o" size="32" color="#FFD700" />
          </div>
          <div class="header-title">诉求上报</div>
          <div class="header-desc">请详细描述您的诉求，我们将尽快处理</div>
        </div>

        <van-steps :active="currentStep" active-color="#D22630" finish-color="#D22630" size="small">
          <van-step title="基本信息" />
          <van-step title="诉求详情" />
          <van-step title="提交确认" />
        </van-steps>

        <div class="step-content">
          <div v-if="currentStep === 0" class="step-item">
            <van-form>
              <van-field
                v-model="form.name"
                placeholder="您的姓名"
                left-icon="user"
                required
                :error="nameError"
                error-message="请输入姓名"
                @blur="validateName"
              />
              <van-field
                v-model="form.phone"
                placeholder="联系电话"
                left-icon="phone"
                maxlength="11"
                required
                :error="phoneError"
                error-message="请输入正确的手机号"
                @blur="validatePhone"
              />
              <van-field
                v-model="form.demandType"
                placeholder="诉求分类"
                left-icon="tag"
                readonly
                clickable
                @click="showTypePicker = true"
                required
              />
            </van-form>
          </div>

          <div v-if="currentStep === 1" class="step-item">
            <van-form>
              <van-field
                v-model="form.content"
                type="textarea"
                placeholder="请详细描述您的诉求（不少于10字）"
                rows="6"
                required
                :error="contentError"
                error-message="请详细描述您的诉求"
                @blur="validateContent"
              />

              <div class="upload-section">
                <div class="upload-header">
                  <span class="upload-title">现场图片（选填）</span>
                  <span class="upload-count">{{ images.length }}/9</span>
                </div>
                <div class="images-list">
                  <div class="image-item" v-for="(img, index) in images" :key="index">
                    <img :src="img" :alt="`图片${index + 1}`" />
                    <div class="delete-btn" @click="removeImage(index)">
                      <van-icon name="cross" size="16" />
                    </div>
                  </div>
                  <div class="add-image" @click="chooseImage" v-if="images.length < 9">
                    <van-icon name="plus" size="24" />
                    <span>添加图片</span>
                  </div>
                </div>
                <div class="upload-tips">
                  <van-icon name="info-o" size="14" color="#999" />
                  <span>最多上传9张图片，支持jpg、png格式</span>
                </div>
              </div>
            </van-form>
          </div>

          <div v-if="currentStep === 2" class="step-item">
            <div class="confirm-section">
              <div class="confirm-item">
                <span class="confirm-label">姓名</span>
                <span class="confirm-value">{{ form.name }}</span>
              </div>
              <div class="confirm-item">
                <span class="confirm-label">电话</span>
                <span class="confirm-value">{{ form.phone }}</span>
              </div>
              <div class="confirm-item">
                <span class="confirm-label">分类</span>
                <span class="confirm-value">{{ form.demandType }}</span>
              </div>
              <div class="confirm-item">
                <span class="confirm-label">内容</span>
                <span class="confirm-value">{{ form.content }}</span>
              </div>
              <div v-if="images.length > 0" class="confirm-item">
                <span class="confirm-label">图片</span>
                <div class="confirm-images">
                  <img v-for="(img, idx) in images" :key="idx" :src="img" class="confirm-image" />
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="form-tips" v-if="currentStep === 0">
          <div class="tip-item">
            <van-icon name="check-circle" size="14" color="#4CAF50" />
            <span>请如实填写联系信息，方便我们联系您</span>
          </div>
        </div>

        <div class="form-tips" v-if="currentStep === 1">
          <div class="tip-item">
            <van-icon name="check-circle" size="14" color="#4CAF50" />
            <span>上传相关图片可帮助我们更快了解情况</span>
          </div>
          <div class="tip-item">
            <van-icon name="check-circle" size="14" color="#4CAF50" />
            <span>提交后可在"我的诉求"中查看处理进度</span>
          </div>
        </div>

        <div class="btn-group">
          <van-button v-if="currentStep > 0" type="default" block @click="prevStep">上一步</van-button>
          <van-button v-if="currentStep < 2" type="primary" block @click="nextStep">下一步</van-button>
          <van-button v-if="currentStep === 2" type="primary" block :loading="loading" @click="onSubmit">
            {{ loading ? '提交中...' : '提交诉求' }}
          </van-button>
        </div>
      </div>
    </div>

    <van-picker
      v-model:show="showTypePicker"
      :columns="typeColumns"
      @confirm="onTypeConfirm"
    />

    <van-image-preview v-model:show="showPreview" :images="images" :start-position="previewIndex" />
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { showToast } from 'vant'
import { goBack } from '../utils/index'

const router = useRouter()

const currentStep = ref(0)

const form = reactive({
  name: '',
  phone: '',
  demandType: '',
  content: ''
})

const images = ref([])
const loading = ref(false)
const showTypePicker = ref(false)
const showPreview = ref(false)
const previewIndex = ref(0)

const nameError = ref(false)
const phoneError = ref(false)
const contentError = ref(false)

const typeColumns = [
  { text: '民生保障', value: '民生保障' },
  { text: '土地纠纷', value: '土地纠纷' },
  { text: '矛盾调解', value: '矛盾调解' },
  { text: '环境整治', value: '环境整治' },
  { text: '道路维护', value: '道路维护' },
  { text: '安全隐患', value: '安全隐患' },
  { text: '帮扶救助', value: '帮扶救助' },
  { text: '其他', value: '其他' }
]

const handleBack = () => {
  goBack(router)
}

const validateName = () => {
  nameError.value = !form.name.trim()
}

const validatePhone = () => {
  phoneError.value = !/^1[3-9]\d{9}$/.test(form.phone)
}

const validateContent = () => {
  contentError.value = form.content.trim().length < 10
}

const onTypeConfirm = ({ selectedOptions }) => {
  form.demandType = selectedOptions[0]?.label || selectedOptions[0]
  showTypePicker.value = false
}

const chooseImage = () => {
  const demoImg = 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=village%20problem%20scene%20photo%20evidence&image_size=square'
  images.value.push(demoImg)
  showToast(`已添加${images.value.length}张图片`)
}

const removeImage = (index) => {
  images.value.splice(index, 1)
}

const prevStep = () => {
  currentStep.value--
}

const nextStep = () => {
  if (currentStep.value === 0) {
    validateName()
    validatePhone()
    
    if (!form.name.trim()) {
      showToast('请输入姓名')
      return
    }
    
    if (!/^1[3-9]\d{9}$/.test(form.phone)) {
      showToast('请输入正确的手机号')
      return
    }
    
    if (!form.demandType) {
      showToast('请选择诉求分类')
      return
    }
  } else if (currentStep.value === 1) {
    validateContent()
    
    if (form.content.trim().length < 10) {
      showToast('请详细描述您的诉求（不少于10字）')
      return
    }
  }
  
  currentStep.value++
}

const onSubmit = async () => {
  loading.value = true
  try {
    await request.post('/demand/submit', {
      name: form.name,
      phone: form.phone,
      demandType: form.demandType,
      content: form.content,
      images: images.value.join(',')
    })
    showToast('提交成功，我们将尽快处理')
    router.push('/demand/my')
  } catch (error) {
    showToast(error.response?.data?.message || error.message || '提交失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.demand-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.demand-content {
  padding: 24px 16px;
}

.demand-card {
  background: #fff;
  border-radius: 20px;
  padding: 28px;
  box-shadow: 0 8px 28px rgba(210, 38, 48, 0.08), 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(210, 38, 48, 0.06);
}

.card-header {
  text-align: center;
  margin-bottom: 24px;
}

.header-icon-wrap {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, rgba(210, 38, 48, 0.1) 0%, rgba(210, 38, 48, 0.05) 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
  border: 2px solid rgba(210, 38, 48, 0.2);
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

.step-content {
  margin: 24px 0;
}

.step-item {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateX(10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.upload-section {
  margin-top: 16px;
}

.upload-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
  color: #323233;
}

.upload-title {
  font-weight: bold;
}

.upload-count {
  color: #969799;
}

.images-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.image-item {
  width: calc((100% - 24px) / 3);
  aspect-ratio: 1;
  position: relative;
  border-radius: 10px;
  overflow: hidden;
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.delete-btn {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 24px;
  height: 24px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-image {
  width: calc((100% - 24px) / 3);
  aspect-ratio: 1;
  border: 2px dashed #E0E0E0;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #969799;
  cursor: pointer;
  transition: all 0.3s;
}

.add-image:active {
  border-color: #D22630;
  background: rgba(210, 38, 48, 0.05);
}

.add-image span {
  font-size: 12px;
  margin-top: 4px;
}

.upload-tips {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 10px;
  font-size: 12px;
  color: #969799;
}

.confirm-section {
  background: #fafafa;
  border-radius: 12px;
  padding: 16px;
}

.confirm-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.confirm-item:last-child {
  border-bottom: none;
}

.confirm-label {
  font-size: 14px;
  color: #999;
}

.confirm-value {
  font-size: 14px;
  color: #333;
  text-align: right;
  flex: 1;
  margin-left: 16px;
}

.confirm-images {
  display: flex;
  flex: 1;
  gap: 8px;
  margin-left: 16px;
}

.confirm-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  object-fit: cover;
}

.form-tips {
  background: rgba(76, 175, 80, 0.08);
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 20px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.btn-group {
  display: flex;
  gap: 12px;
}

.btn-group .van-button {
  flex: 1;
  height: 48px;
  border-radius: 12px;
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

:deep(.van-field__control) {
  font-size: 15px;
}
</style>
