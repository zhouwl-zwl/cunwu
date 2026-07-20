
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>文件资料库</h2>
      <el-button type="primary" @click="handleAdd">新增文件</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索文件名称"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="name" label="文件名称" min-width="200" />
      <el-table-column prop="category" label="文件类别" width="120" />
      <el-table-column prop="fileType" label="文件类型" width="100" />
      <el-table-column prop="fileSize" label="文件大小" width="100" />
      <el-table-column prop="uploader" label="上传人" width="100" />
      <el-table-column prop="uploadDate" label="上传日期" width="120" />
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleDownload(scope.row)">下载</el-button>
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
        <el-form-item label="文件名称">
          <el-input v-model="form.name" placeholder="请输入文件名称" />
        </el-form-item>
        <el-form-item label="文件类别">
          <el-select v-model="form.category" placeholder="请选择文件类别" style="width: 100%">
            <el-option label="政策文件" value="政策文件" />
            <el-option label="规章制度" value="规章制度" />
            <el-option label="会议纪要" value="会议纪要" />
            <el-option label="工作报告" value="工作报告" />
            <el-option label="通知公告" value="通知公告" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="文件类型">
          <el-input v-model="form.fileType" placeholder="如:PDF、DOC、XLS等" />
        </el-form-item>
        <el-form-item label="文件大小">
          <el-input v-model="form.fileSize" placeholder="如:1.2MB" />
        </el-form-item>
        <el-form-item label="文件路径">
          <el-input v-model="form.filePath" placeholder="请输入文件存储路径" />
        </el-form-item>
        <el-form-item label="上传人">
          <el-input v-model="form.uploader" placeholder="请输入上传人" />
        </el-form-item>
        <el-form-item label="上传日期">
          <el-date-picker
            v-model="form.uploadDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择上传日期"
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
const dialogTitle = ref('新增文件')

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  name: '',
  category: '政策文件',
  fileType: '',
  fileSize: '',
  filePath: '',
  uploader: '',
  uploadDate: '',
  remark: ''
})

const form = reactive(defaultForm())

const fetchData = async () => {
  try {
    const res = await request.get('/document-library/list', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取文件资料列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '新增文件'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑文件'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除文件 "${row.name}" 吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/document-library/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleDownload = (row) => {
  if (row.filePath) {
    window.open(row.filePath, '_blank')
  } else {
    ElMessage.warning('该文件暂无可下载路径')
  }
}

const handleSave = async () => {
  if (!form.name) {
    ElMessage.warning('请输入文件名称')
    return
  }
  try {
    if (form.id) {
      await request.put('/document-library', form)
    } else {
      await request.post('/document-library', form)
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
