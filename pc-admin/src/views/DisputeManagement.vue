
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>矛盾纠纷管理</h2>
      <el-button type="primary" @click="handleAdd">新增纠纷</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索标题/当事人"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="title" label="纠纷标题" min-width="180" />
      <el-table-column prop="category" label="纠纷类型" width="120" />
      <el-table-column prop="partyA" label="当事人A" width="100" />
      <el-table-column prop="partyB" label="当事人B" width="100" />
      <el-table-column prop="mediator" label="调解人" width="100" />
      <el-table-column prop="occurDate" label="发生日期" width="120" />
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
        <el-form-item label="纠纷标题">
          <el-input v-model="form.title" placeholder="请输入纠纷标题" />
        </el-form-item>
        <el-form-item label="纠纷类型">
          <el-select v-model="form.category" placeholder="请选择纠纷类型" style="width: 100%">
            <el-option label="邻里纠纷" value="邻里纠纷" />
            <el-option label="婚姻家庭" value="婚姻家庭" />
            <el-option label="土地纠纷" value="土地纠纷" />
            <el-option label="山林纠纷" value="山林纠纷" />
            <el-option label="经济纠纷" value="经济纠纷" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="当事人A">
          <el-input v-model="form.partyA" placeholder="请输入当事人A" />
        </el-form-item>
        <el-form-item label="当事人B">
          <el-input v-model="form.partyB" placeholder="请输入当事人B" />
        </el-form-item>
        <el-form-item label="调解人">
          <el-input v-model="form.mediator" placeholder="请输入调解人" />
        </el-form-item>
        <el-form-item label="发生日期">
          <el-date-picker
            v-model="form.occurDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择发生日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待调解" value="PENDING" />
            <el-option label="调解中" value="PROCESSING" />
            <el-option label="已调解" value="RESOLVED" />
            <el-option label="未达成" value="UNRESOLVED" />
          </el-select>
        </el-form-item>
        <el-form-item label="纠纷描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入纠纷描述" />
        </el-form-item>
        <el-form-item label="调解结果">
          <el-input v-model="form.result" type="textarea" :rows="3" placeholder="请输入调解结果" />
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
const dialogTitle = ref('新增纠纷')

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  title: '',
  category: '邻里纠纷',
  partyA: '',
  partyB: '',
  mediator: '',
  occurDate: '',
  status: 'PENDING',
  description: '',
  result: ''
})

const form = reactive(defaultForm())

const getStatusName = (status) => {
  const map = { PENDING: '待调解', PROCESSING: '调解中', RESOLVED: '已调解', UNRESOLVED: '未达成' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', PROCESSING: 'primary', RESOLVED: 'success', UNRESOLVED: 'danger' }
  return map[status] || 'info'
}

const fetchData = async () => {
  try {
    const res = await request.get('/dispute/list', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取矛盾纠纷列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '新增纠纷'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑纠纷'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除纠纷 "${row.title}" 吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/dispute/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.title) {
    ElMessage.warning('请输入纠纷标题')
    return
  }
  try {
    if (form.id) {
      await request.put('/dispute', form)
    } else {
      await request.post('/dispute', form)
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
