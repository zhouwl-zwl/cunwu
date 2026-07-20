
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>文件收发管理</h2>
      <el-button type="primary" @click="handleAdd">新增记录</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索文件标题/文号"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="title" label="文件标题" min-width="180" />
      <el-table-column prop="docNo" label="文号" width="140" />
      <el-table-column prop="direction" label="收发类型" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.direction === 'IN' ? 'success' : 'warning'">
            {{ scope.row.direction === 'IN' ? '收文' : '发文' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="source" label="来源单位" width="150" />
      <el-table-column prop="receiver" label="接收人" width="100" />
      <el-table-column prop="docDate" label="收发日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '已处理' : '待处理' }}
          </el-tag>
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
        <el-form-item label="文件标题">
          <el-input v-model="form.title" placeholder="请输入文件标题" />
        </el-form-item>
        <el-form-item label="文号">
          <el-input v-model="form.docNo" placeholder="请输入文号" />
        </el-form-item>
        <el-form-item label="收发类型">
          <el-radio-group v-model="form.direction">
            <el-radio label="IN">收文</el-radio>
            <el-radio label="OUT">发文</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="来源单位">
          <el-input v-model="form.source" placeholder="请输入来源单位" />
        </el-form-item>
        <el-form-item label="接收人">
          <el-input v-model="form.receiver" placeholder="请输入接收人" />
        </el-form-item>
        <el-form-item label="收发日期">
          <el-date-picker
            v-model="form.docDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择收发日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">待处理</el-radio>
            <el-radio :label="1">已处理</el-radio>
          </el-radio-group>
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
const dialogTitle = ref('新增记录')

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  title: '',
  docNo: '',
  direction: 'IN',
  source: '',
  receiver: '',
  docDate: '',
  status: 0,
  remark: ''
})

const form = reactive(defaultForm())

const fetchData = async () => {
  try {
    const res = await request.get('/document-record/list', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取文件收发列表失败', error)
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
  ElMessageBox.confirm(`确定要删除文件 "${row.title}" 吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/document-record/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.title) {
    ElMessage.warning('请输入文件标题')
    return
  }
  try {
    if (form.id) {
      await request.put('/document-record', form)
    } else {
      await request.post('/document-record', form)
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
