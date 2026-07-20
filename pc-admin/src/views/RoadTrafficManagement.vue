
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>道路交通管理</h2>
      <el-button type="primary" @click="handleAdd">新增记录</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索路段/责任人"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="roadName" label="路段名称" min-width="160" />
      <el-table-column prop="roadType" label="道路类型" width="120" />
      <el-table-column prop="length" label="长度(km)" width="100" />
      <el-table-column prop="issueType" label="问题类型" width="120" />
      <el-table-column prop="responsible" label="责任人" width="100" />
      <el-table-column prop="inspectDate" label="巡查日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ getStatusName(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
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
        <el-form-item label="路段名称">
          <el-input v-model="form.roadName" placeholder="请输入路段名称" />
        </el-form-item>
        <el-form-item label="道路类型">
          <el-select v-model="form.roadType" placeholder="请选择道路类型" style="width: 100%">
            <el-option label="村道" value="村道" />
            <el-option label="乡道" value="乡道" />
            <el-option label="县道" value="县道" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="长度(km)">
          <el-input-number v-model="form.length" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="问题类型">
          <el-select v-model="form.issueType" placeholder="请选择问题类型" style="width: 100%">
            <el-option label="路面破损" value="路面破损" />
            <el-option label="交通标志" value="交通标志" />
            <el-option label="护栏损坏" value="护栏损坏" />
            <el-option label="视线遮挡" value="视线遮挡" />
            <el-option label="无问题" value="无问题" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="责任人">
          <el-input v-model="form.responsible" placeholder="请输入责任人" />
        </el-form-item>
        <el-form-item label="巡查日期">
          <el-date-picker
            v-model="form.inspectDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择巡查日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待整改" value="PENDING" />
            <el-option label="整改中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入问题描述" />
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
const dialogTitle = ref('新增记录')

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  roadName: '',
  roadType: '村道',
  length: 0,
  issueType: '无问题',
  responsible: '',
  inspectDate: '',
  status: 'PENDING',
  description: ''
})

const form = reactive(defaultForm())

const getStatusName = (status) => {
  const map = { PENDING: '待整改', PROCESSING: '整改中', COMPLETED: '已完成' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', PROCESSING: 'primary', COMPLETED: 'success' }
  return map[status] || 'info'
}

const fetchData = async () => {
  try {
    const res = await request.get('/road-traffic/list', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取道路交通列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '新增记录'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑记录'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除路段 "${row.roadName}" 吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/road-traffic/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.roadName) {
    ElMessage.warning('请输入路段名称')
    return
  }
  try {
    if (form.id) {
      await request.put('/road-traffic', form)
    } else {
      await request.post('/road-traffic', form)
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
