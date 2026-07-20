
<template>
  <div class="village-config">
    <div class="page-header">
      <h2>村情配置</h2>
    </div>

    <el-form :model="configForm" label-width="120px">
      <el-form-item label="村名">
        <el-input v-model="configForm.villageName" />
      </el-form-item>
      <el-form-item label="人口数">
        <el-input-number v-model="configForm.population" />
      </el-form-item>
      <el-form-item label="户数">
        <el-input-number v-model="configForm.households" />
      </el-form-item>
      <el-form-item label="面积(km²)">
        <el-input v-model="configForm.area" />
      </el-form-item>
      <el-form-item label="三资总额(万)">
        <el-input-number v-model="configForm.assetTotal" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveConfig">保存配置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const configForm = reactive({
  villageName: '',
  population: 0,
  households: 0,
  area: '',
  assetTotal: 0
})

const fetchConfig = async () => {
  try {
    const res = await request.get('/public/village-info')
    Object.assign(configForm, res.data)
  } catch (error) {
    console.error('获取配置失败', error)
  }
}

const saveConfig = async () => {
  try {
    await request.put('/public/village-info', configForm)
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

onMounted(() => {
  fetchConfig()
})
</script>

<style scoped>
.village-config {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 20px;
}
</style>
