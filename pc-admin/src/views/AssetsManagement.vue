
<template>
  <div class="assets-management">
    <div class="page-header">
      <h2>三资管理</h2>
      <el-button type="primary" @click="showAddDialog = true">新增资产</el-button>
    </div>

    <el-table :data="assetList" border>
      <el-table-column prop="name" label="资产名称" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="value" label="价值(元)" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="createTime" label="登记时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="editAsset(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteAsset(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showAddDialog" :title="editForm.id ? '编辑资产' : '新增资产'">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="资产名称">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="editForm.type">
            <el-option label="房屋" value="房屋" />
            <el-option label="设备" value="设备" />
            <el-option label="山林" value="山林" />
            <el-option label="土地" value="土地" />
          </el-select>
        </el-form-item>
        <el-form-item label="价值(元)">
          <el-input-number v-model="editForm.value" />
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="editForm.location" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveAsset">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const assetList = ref([])
const showAddDialog = ref(false)

const editForm = reactive({
  id: null,
  name: '',
  type: '房屋',
  value: 0,
  location: ''
})

const fetchAssets = async () => {
  try {
    const res = await request.get('/asset/list')
    assetList.value = res.data.records
  } catch (error) {
    console.error('获取资产列表失败', error)
  }
}

const editAsset = (row) => {
  Object.assign(editForm, row)
  showAddDialog.value = true
}

const deleteAsset = async (id) => {
  try {
    await request.delete(`/asset/${id}`)
    ElMessage.success('删除成功')
    fetchAssets()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const saveAsset = async () => {
  try {
    if (editForm.id) {
      await request.put('/asset', editForm)
    } else {
      await request.post('/asset', editForm)
    }
    ElMessage.success('保存成功')
    showAddDialog.value = false
    fetchAssets()
    Object.assign(editForm, { id: null, name: '', type: '房屋', value: 0, location: '' })
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

onMounted(() => {
  fetchAssets()
})
</script>

<style scoped>
.assets-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 20px;
}
</style>
