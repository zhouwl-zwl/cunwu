
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>会议管理</h2>
      <el-button type="primary" @click="handleAdd">新增会议</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索会议标题"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="title" label="会议标题" min-width="180" />
      <el-table-column prop="meetingType" label="会议类型" width="120" />
      <el-table-column prop="meetingDate" label="会议时间" width="120" />
      <el-table-column prop="location" label="会议地点" width="150" />
      <el-table-column prop="host" label="主持人" width="100" />
      <el-table-column prop="participantCount" label="参会人数" width="100" />
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
        <el-form-item label="会议标题">
          <el-input v-model="form.title" placeholder="请输入会议标题" />
        </el-form-item>
        <el-form-item label="会议类型">
          <el-select v-model="form.meetingType" placeholder="请选择会议类型" style="width: 100%">
            <el-option label="村两委会" value="村两委会" />
            <el-option label="党员大会" value="党员大会" />
            <el-option label="村民代表大会" value="村民代表大会" />
            <el-option label="工作例会" value="工作例会" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="会议时间">
          <el-date-picker
            v-model="form.meetingDate"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择会议时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="会议地点">
          <el-input v-model="form.location" placeholder="请输入会议地点" />
        </el-form-item>
        <el-form-item label="主持人">
          <el-input v-model="form.host" placeholder="请输入主持人" />
        </el-form-item>
        <el-form-item label="参会人数">
          <el-input-number v-model="form.participantCount" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待召开" value="PENDING" />
            <el-option label="进行中" value="PROCESSING" />
            <el-option label="已结束" value="FINISHED" />
            <el-option label="已取消" value="CANCELED" />
          </el-select>
        </el-form-item>
        <el-form-item label="会议内容">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入会议内容" />
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
const dialogTitle = ref('新增会议')

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  title: '',
  meetingType: '村两委会',
  meetingDate: '',
  location: '',
  host: '',
  participantCount: 0,
  status: 'PENDING',
  content: ''
})

const form = reactive(defaultForm())

const getStatusName = (status) => {
  const map = { PENDING: '待召开', PROCESSING: '进行中', FINISHED: '已结束', CANCELED: '已取消' }
  return map[status] || '未知'
}

const getStatusType = (status) => {
  const map = { PENDING: 'warning', PROCESSING: 'primary', FINISHED: 'success', CANCELED: 'info' }
  return map[status] || 'info'
}

const fetchData = async () => {
  try {
    const res = await request.get('/meeting/list', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取会议列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '新增会议'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑会议'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除会议 "${row.title}" 吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/meeting/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.title) {
    ElMessage.warning('请输入会议标题')
    return
  }
  try {
    if (form.id) {
      await request.put('/meeting', form)
    } else {
      await request.post('/meeting', form)
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
