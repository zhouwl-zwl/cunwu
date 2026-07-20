
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>消防安全管理</h2>
      <el-button type="primary" @click="handleAdd">新增记录</el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索位置/检查人"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="location" label="检查位置" min-width="160" />
      <el-table-column prop="facilityType" label="设施类型" width="120" />
      <el-table-column prop="inspectResult" label="检查结果" width="120">
        <template #default="scope">
          <el-tag :type="scope.row.inspectResult === 'PASS' ? 'success' : 'danger'">
            {{ scope.row.inspectResult === 'PASS' ? '合格' : '不合格' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="inspector" label="检查人" width="100" />
      <el-table-column prop="inspectDate" label="检查日期" width="120" />
      <el-table-column prop="nextInspectDate" label="下次检查" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '正常' : '停用' }}
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
        <el-form-item label="检查位置">
          <el-input v-model="form.location" placeholder="请输入检查位置" />
        </el-form-item>
        <el-form-item label="设施类型">
          <el-select v-model="form.facilityType" placeholder="请选择设施类型" style="width: 100%">
            <el-option label="灭火器" value="灭火器" />
            <el-option label="消火栓" value="消火栓" />
            <el-option label="消防通道" value="消防通道" />
            <el-option label="烟感报警器" value="烟感报警器" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="检查结果">
          <el-radio-group v-model="form.inspectResult">
            <el-radio label="PASS">合格</el-radio>
            <el-radio label="FAIL">不合格</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="检查人">
          <el-input v-model="form.inspector" placeholder="请输入检查人" />
        </el-form-item>
        <el-form-item label="检查日期">
          <el-date-picker
            v-model="form.inspectDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择检查日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="下次检查">
          <el-date-picker
            v-model="form.nextInspectDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择下次检查日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="问题描述">
          <el-input v-model="form.problem" type="textarea" :rows="3" placeholder="请输入问题描述" />
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
  location: '',
  facilityType: '灭火器',
  inspectResult: 'PASS',
  inspector: '',
  inspectDate: '',
  nextInspectDate: '',
  status: 1,
  problem: ''
})

const form = reactive(defaultForm())

const fetchData = async () => {
  try {
    const res = await request.get('/fire-safety/list', {
      params: {
        keyword: searchKeyword.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取消防安全列表失败', error)
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
  ElMessageBox.confirm(`确定要删除该记录吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/fire-safety/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.location) {
    ElMessage.warning('请输入检查位置')
    return
  }
  try {
    if (form.id) {
      await request.put('/fire-safety', form)
    } else {
      await request.post('/fire-safety', form)
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
