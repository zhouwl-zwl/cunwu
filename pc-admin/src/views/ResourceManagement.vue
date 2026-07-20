
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>资源管理</h2>
      <el-button type="primary" @click="handleAdd">新增资源</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索资源名称"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="name" label="资源名称" min-width="160" />
      <el-table-column prop="type" label="资源类型" width="120" />
      <el-table-column prop="area" label="面积(亩)" width="100" />
      <el-table-column prop="location" label="位置" width="160" />
      <el-table-column prop="useStatus" label="使用状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.useStatus === 'IN_USE' ? 'success' : 'info'">
            {{ scope.row.useStatus === 'IN_USE' ? '使用中' : '闲置' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="contractor" label="承包人" width="100" />
      <el-table-column prop="contractEndDate" label="承包截止" width="120" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.size"
      :page-sizes="[10, 20, 50, 100]"
      :total="pagination.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="fetchData"
      @current-change="fetchData"
    />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="资源名称">
          <el-input v-model="form.name" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="资源类型">
          <el-select v-model="form.type" placeholder="请选择资源类型" style="width: 100%">
            <el-option label="耕地" value="耕地" />
            <el-option label="林地" value="林地" />
            <el-option label="水域" value="水域" />
            <el-option label="草地" value="草地" />
            <el-option label="建设用地" value="建设用地" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="面积(亩)">
          <el-input-number v-model="form.area" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location" placeholder="请输入位置" />
        </el-form-item>
        <el-form-item label="使用状态">
          <el-radio-group v-model="form.useStatus">
            <el-radio label="IN_USE">使用中</el-radio>
            <el-radio label="IDLE">闲置</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="承包人">
          <el-input v-model="form.contractor" placeholder="请输入承包人" />
        </el-form-item>
        <el-form-item label="承包截止">
          <el-date-picker
            v-model="form.contractEndDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择承包截止日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const tableData = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增资源')

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  name: '',
  type: '耕地',
  area: 0,
  location: '',
  useStatus: 'IDLE',
  contractor: '',
  contractEndDate: '',
  remark: ''
})

const form = reactive(defaultForm())

const fetchData = async () => {
  try {
    const res = await request.get('/resource/list', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取资源列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '新增资源'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑资源'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除资源 "${row.name}" 吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/resource/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.name) {
    ElMessage.warning('请输入资源名称')
    return
  }
  try {
    if (form.id) {
      await request.put('/resource', form)
    } else {
      await request.post('/resource', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.management-page {
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
  color: #D22630;
  font-weight: bold;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  border-color: #B01A26;
}

:deep(.el-table th) {
  background: #FDE8E8 !important;
  color: #D22630;
  font-weight: bold;
}

:deep(.el-pagination .el-pager .is-active) {
  background-color: #D22630;
}
</style>
