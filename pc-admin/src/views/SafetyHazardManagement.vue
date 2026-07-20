
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>安全隐患排查</h2>
      <el-button type="primary" @click="handleAdd">新增隐患</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索隐患描述/位置"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="description" label="隐患描述" min-width="180" />
      <el-table-column prop="category" label="隐患类别" width="120" />
      <el-table-column prop="location" label="隐患位置" width="150" />
      <el-table-column prop="level" label="隐患级别" width="100">
        <template #default="scope">
          <el-tag :type="getLevelType(scope.row.level)">{{ scope.row.level }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="inspector" label="排查人" width="100" />
      <el-table-column prop="inspectDate" label="排查日期" width="120" />
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
        <el-form-item label="隐患描述">
          <el-input v-model="form.description" placeholder="请输入隐患描述" />
        </el-form-item>
        <el-form-item label="隐患类别">
          <el-select v-model="form.category" placeholder="请选择隐患类别" style="width: 100%">
            <el-option label="建筑施工" value="建筑施工" />
            <el-option label="燃气安全" value="燃气安全" />
            <el-option label="用电安全" value="用电安全" />
            <el-option label="危房" value="危房" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="隐患位置">
          <el-input v-model="form.location" placeholder="请输入隐患位置" />
        </el-form-item>
        <el-form-item label="隐患级别">
          <el-select v-model="form.level" placeholder="请选择隐患级别" style="width: 100%">
            <el-option label="一般" value="一般" />
            <el-option label="较大" value="较大" />
            <el-option label="重大" value="重大" />
          </el-select>
        </el-form-item>
        <el-form-item label="排查人">
          <el-input v-model="form.inspector" placeholder="请输入排查人" />
        </el-form-item>
        <el-form-item label="排查日期">
          <el-date-picker
            v-model="form.inspectDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择排查日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待整改" value="PENDING" />
            <el-option label="整改中" value="PROCESSING" />
            <el-option label="已整改" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="整改措施">
          <el-input v-model="form.measure" type="textarea" :rows="3" placeholder="请输入整改措施" />
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
const dialogTitle = ref('新增隐患')

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  description: '',
  category: '建筑施工',
  location: '',
  level: '一般',
  inspector: '',
  inspectDate: '',
  status: 'PENDING',
  measure: ''
})

const form = reactive(defaultForm())

const getLevelType = (level) => {
  const map = { '一般': 'info', '较大': 'warning', '重大': 'danger' }
  return map[level] || 'info'
}

const getStatusName = (status) => {
  const map = { PENDING: '待整改', PROCESSING: '整改中', COMPLETED: '已整改' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { PENDING: 'danger', PROCESSING: 'warning', COMPLETED: 'success' }
  return map[status] || 'info'
}

const fetchData = async () => {
  try {
    const res = await request.get('/safety-hazard/list', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取安全隐患列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '新增隐患'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑隐患'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除该隐患记录吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/safety-hazard/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.description) {
    ElMessage.warning('请输入隐患描述')
    return
  }
  try {
    if (form.id) {
      await request.put('/safety-hazard', form)
    } else {
      await request.post('/safety-hazard', form)
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
