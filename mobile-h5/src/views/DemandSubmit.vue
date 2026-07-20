<template>
  <div class="page-container">
    <NavBar :title="title" />
    <van-form @submit="onSubmit">
      <van-cell-group inset class="card">
        <van-form-item label="诉求类型" required>
          <van-picker :columns="demandTypeOptions" @change="onTypeChange" v-model="form.demandType" />
        </van-form-item>
        
        <van-form-item label="诉求标题" required>
          <van-input v-model="form.title" placeholder="请输入诉求标题" />
        </van-form-item>
        
        <van-form-item label="诉求内容" required>
          <van-field v-model="form.content" type="textarea" placeholder="请详细描述您的诉求" rows="4" />
        </van-form-item>
        
        <van-form-item label="联系电话">
          <van-input v-model="form.phone" placeholder="请输入联系电话" />
        </van-form-item>
        
        <van-form-item label="上传图片">
          <van-uploader :after-read="onAfterRead" :max-count="9" />
        </van-form-item>
        
        <van-form-item>
          <van-button type="primary" native-type="submit" block :loading="submitting">提交诉求</van-button>
        </van-form-item>
      </van-cell-group>
    </van-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '../utils/request'
import { showToast } from 'vant'
import NavBar from '../components/NavBar.vue'

const router = useRouter()
const route = useRoute()
const title = ref('诉求上报')
const submitting = ref(false)

const demandTypeOptions = [
  { text: '矛盾纠纷', value: 'dispute' },
  { text: '环境问题', value: 'environment' },
  { text: '道路损坏', value: 'road' },
  { text: '安全隐患', value: 'safety' },
  { text: '其他', value: 'other' }
]

const form = reactive({
  demandType: '',
  title: '',
  content: '',
  phone: '',
  images: []
})

const onTypeChange = ({ selectedOptions }) => {
  form.demandType = selectedOptions[0]?.value || ''
}

const onAfterRead = (file) => {
  form.images.push(file.content)
}

const onSubmit = async () => {
  if (!form.demandType || !form.title || !form.content) {
    showToast('请填写完整信息')
    return
  }
  
  submitting.value = true
  try {
    await request.post('/demand/submit', {
      name: '村民',
      phone: form.phone,
      demandType: form.demandType,
      content: form.content,
      images: JSON.stringify(form.images)
    })
    showToast('提交成功，我们会尽快处理')
    router.push('/demand-list')
  } catch (error) {
    showToast('提交失败，请重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.page-container {
  background: #FDF5F5;
  min-height: 100vh;
}

.card {
  background: #fff;
  border-radius: 12px;
  margin: 16px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.06);
}



:deep(.van-button--primary) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%) !important;
  border: none !important;
}
</style>
