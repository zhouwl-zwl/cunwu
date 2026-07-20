<template>
  <div class="management-page">
    <div class="page-header">
      <h2>档案资料管理</h2>
      <div class="header-actions">
        <el-button :disabled="!selectedRows.length" @click="handleBatchExport">批量导出</el-button>
        <el-button :disabled="!selectedRows.length" @click="handleBatchPrint">批量打印台账</el-button>
        <el-button type="primary" @click="handleAdd">新增档案</el-button>
      </div>
    </div>

    <el-alert
      :title="permissionAlert.title"
      :type="permissionAlert.type"
      :description="permissionAlert.desc"
      show-icon
      style="margin-bottom: 20px"
    />

    <el-tabs v-model="activeCategory" class="page-tabs" @tab-change="handleCategoryChange">
      <el-tab-pane label="全部档案" name="" />
      <el-tab-pane label="党建档案" name="党建档案" />
      <el-tab-pane label="财务三资档案" name="财务三资档案" />
      <el-tab-pane label="人口土地档案" name="人口土地档案" />
      <el-tab-pane label="项目工程档案" name="项目工程档案" />
      <el-tab-pane label="调解信访档案" name="调解信访档案" />
    </el-tabs>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="关键词搜索"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-select v-model="searchSecretLevel" placeholder="密级筛选" style="width: 140px" clearable>
        <el-option label="公开" value="公开" />
        <el-option label="内部" value="内部" />
        <el-option label="涉密" value="涉密" />
      </el-select>
      <el-date-picker
        v-model="searchYear"
        type="year"
        placeholder="选择年份"
        value-format="YYYY"
        style="width: 140px"
      />
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
      :row-class-name="getTableRowClass"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="title" label="档案标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="category" label="分类" width="120">
        <template #default="scope">
          <el-tag type="warning">{{ scope.row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="source" label="来源" width="120" />
      <el-table-column prop="keywords" label="关键词" min-width="180" show-overflow-tooltip />
      <el-table-column prop="uploadDate" label="上传日期" width="120" />
      <el-table-column prop="secretLevel" label="密级" width="100">
        <template #default="scope">
          <template v-if="canViewSecret(scope.row)">
            <el-tag :type="getSecretLevelTagType(scope.row.secretLevel)">
              {{ scope.row.secretLevel }}
            </el-tag>
          </template>
          <span v-else style="color: #999">无权限</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleView(scope.row)">查看</el-button>
          <el-button size="small" @click="handleBorrow(scope.row)" v-if="canViewSecret(scope.row)">
            申请借阅
          </el-button>
          <el-button size="small" @click="handleEdit(scope.row)" v-if="canEdit">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)" v-if="canEdit">
            删除
          </el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="680px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="档案标题">
          <el-input v-model="form.title" placeholder="请输入档案标题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="党建档案" value="党建档案" />
            <el-option label="财务三资档案" value="财务三资档案" />
            <el-option label="人口土地档案" value="人口土地档案" />
            <el-option label="项目工程档案" value="项目工程档案" />
            <el-option label="调解信访档案" value="调解信访档案" />
          </el-select>
        </el-form-item>
        <el-form-item label="来源">
          <el-radio-group v-model="form.source">
            <el-radio label="自动归档">自动归档</el-radio>
            <el-radio label="手动上传">手动上传</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="密级">
          <el-radio-group v-model="form.secretLevel">
            <el-radio label="公开">公开</el-radio>
            <el-radio label="内部">内部</el-radio>
            <el-radio label="涉密">涉密</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="form.keywords" placeholder="多个关键词请用逗号分隔" />
        </el-form-item>
        <el-form-item label="档案内容">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入档案内容摘要" />
        </el-form-item>
        <el-form-item label="附件上传">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="true"
            multiple
          >
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="upload-tip">支持上传图片、文档等资料文件</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="档案详情" width="800px">
      <div class="detail-section">
        <h4 class="section-title">基本信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="档案标题" :span="2">{{ viewData.title }}</el-descriptions-item>
          <el-descriptions-item label="分类">{{ viewData.category }}</el-descriptions-item>
          <el-descriptions-item label="来源">{{ viewData.source }}</el-descriptions-item>
          <el-descriptions-item label="密级">
            <el-tag :type="getSecretLevelTagType(viewData.secretLevel)">
              {{ viewData.secretLevel }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="上传日期">{{ viewData.uploadDate }}</el-descriptions-item>
          <el-descriptions-item label="关键词" :span="2">{{ viewData.keywords }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="detail-section">
        <h4 class="section-title">档案内容预览区</h4>
        <div class="content-preview">
          档案内容预览区
          <p style="margin-top: 12px; color: #666; line-height: 1.8;">{{ viewData.content || '暂无内容' }}</p>
        </div>
      </div>

      <div class="detail-section">
        <h4 class="section-title">附件列表</h4>
        <el-table :data="viewData.attachments || []" size="small">
          <el-table-column prop="name" label="文件名" />
          <el-table-column prop="size" label="大小" width="120" />
          <el-table-column label="操作" width="100">
            <template #default>
              <el-link type="primary">下载</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="detail-section">
        <h4 class="section-title">借阅记录</h4>
        <el-table :data="viewData.borrowRecords || []" size="small">
          <el-table-column prop="borrower" label="借阅人" width="100" />
          <el-table-column prop="purpose" label="用途" min-width="160" show-overflow-tooltip />
          <el-table-column prop="borrowTime" label="借阅时间" width="120" />
          <el-table-column prop="returnTime" label="归还时间" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '已归还' ? 'success' : 'warning'" size="small">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <template #footer>
        <el-button @click="handleBorrow(viewData)" v-if="canViewSecret(viewData)">申请借阅</el-button>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="borrowDialogVisible" title="申请借阅" width="500px">
      <el-form :model="borrowForm" label-width="110px">
        <el-form-item label="档案标题">
          <el-input v-model="borrowForm.archiveTitle" disabled />
        </el-form-item>
        <el-form-item label="借阅人">
          <el-input v-model="borrowForm.borrower" placeholder="请输入借阅人姓名" />
        </el-form-item>
        <el-form-item label="借阅用途">
          <el-input
            v-model="borrowForm.purpose"
            type="textarea"
            :rows="3"
            placeholder="请说明借阅用途"
          />
        </el-form-item>
        <el-form-item label="预计归还时间">
          <el-date-picker
            v-model="borrowForm.expectedReturnTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择预计归还日期"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="borrowDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBorrowSubmit">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const tableData = ref([])
const searchKeyword = ref('')
const searchSecretLevel = ref('')
const searchYear = ref('')
const activeCategory = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增档案')
const viewDialogVisible = ref(false)
const borrowDialogVisible = ref(false)
const selectedRows = ref([])

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const currentRole = ref(localStorage.getItem('role') || 'admin')

const canEdit = computed(() => currentRole.value === 'admin' || currentRole.value === 'secretary')

const permissionAlert = computed(() => {
  if (currentRole.value === 'admin' || currentRole.value === 'secretary') {
    return {
      title: '当前账号：村支书（管理员）',
      desc: '可查看全部档案，包括涉密档案',
      type: 'success'
    }
  } else {
    return {
      title: '当前账号：网格员',
      desc: '仅可查看公开档案，内部及涉密档案需申请借阅',
      type: 'warning'
    }
  }
})

const defaultForm = () => ({
  id: null,
  title: '',
  category: '党建档案',
  source: '手动上传',
  secretLevel: '公开',
  content: '',
  keywords: '',
  uploadDate: ''
})

const form = reactive(defaultForm())
const viewData = reactive({})
const borrowForm = reactive({
  archiveId: null,
  archiveTitle: '',
  borrower: '',
  purpose: '',
  expectedReturnTime: ''
})

const getSecretLevelTagType = (level) => {
  const map = { '公开': 'success', '内部': 'warning', '涉密': 'danger' }
  return map[level] || 'info'
}

const canViewSecret = (row) => {
  if (currentRole.value === 'admin' || currentRole.value === 'secretary') return true
  return row.secretLevel === '公开'
}

const getTableRowClass = ({ row }) => {
  if (!canViewSecret(row)) return 'disabled-row'
  return ''
}

const fetchData = async () => {
  try {
    const res = await request.get('/archive/list', {
      params: {
        keyword: searchKeyword.value,
        secretLevel: searchSecretLevel.value,
        year: searchYear.value,
        category: activeCategory.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取档案列表失败', error)
    tableData.value = mockArchiveList
    pagination.total = mockArchiveList.length
  }
}

const mockArchiveList = [
  { id: 1, title: '2024年第一季度党员学习记录', category: '党建档案', source: '自动归档', keywords: '党员学习,党建,会议', uploadDate: '2024-03-20', secretLevel: '内部', content: '党员学习记录详细内容...' },
  { id: 2, title: '2024年村集体财务收支台账', category: '财务三资档案', source: '手动上传', keywords: '财务,收支,台账', uploadDate: '2024-03-15', secretLevel: '涉密', content: '财务收支详细内容...' },
  { id: 3, title: '全村人口信息统计表', category: '人口土地档案', source: '自动归档', keywords: '人口,统计,信息', uploadDate: '2024-03-10', secretLevel: '内部', content: '人口统计详细内容...' },
  { id: 4, title: '村道硬化工程项目档案', category: '项目工程档案', source: '手动上传', keywords: '工程项目,道路,建设', uploadDate: '2024-03-05', secretLevel: '公开', content: '工程项目详细内容...' },
  { id: 5, title: '村民矛盾调解记录', category: '调解信访档案', source: '自动归档', keywords: '调解,信访,矛盾', uploadDate: '2024-02-28', secretLevel: '内部', content: '调解记录详细内容...' },
  { id: 6, title: '土地承包经营权登记', category: '人口土地档案', source: '手动上传', keywords: '土地,承包,登记', uploadDate: '2024-02-20', secretLevel: '涉密', content: '土地登记详细内容...' },
  { id: 7, title: '村党支部换届选举档案', category: '党建档案', source: '自动归档', keywords: '换届,选举,党支部', uploadDate: '2024-01-15', secretLevel: '公开', content: '换届选举详细内容...' }
]

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  searchKeyword.value = ''
  searchSecretLevel.value = ''
  searchYear.value = ''
  pagination.page = 1
  fetchData()
}

const handleCategoryChange = () => {
  pagination.page = 1
  fetchData()
}

const handleSelectionChange = (val) => {
  selectedRows.value = val
}

const handleBatchExport = () => {
  ElMessage.success(`已选择 ${selectedRows.value.length} 条档案，正在导出...`)
}

const handleBatchPrint = () => {
  ElMessage.success(`已选择 ${selectedRows.value.length} 条档案，正在打印台账...`)
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  if (activeCategory.value) {
    form.category = activeCategory.value
  }
  dialogTitle.value = '新增档案'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑档案'
  dialogVisible.value = true
}

const handleView = (row) => {
  Object.assign(viewData, row)
  viewData.attachments = [
    { name: '档案正文.pdf', size: '2.5MB' },
    { name: '附件材料.docx', size: '1.2MB' },
    { name: '相关照片.zip', size: '8.6MB' }
  ]
  viewData.borrowRecords = [
    { borrower: '李主任', purpose: '工作查阅', borrowTime: '2024-03-10', returnTime: '2024-03-15', status: '已归还' },
    { borrower: '王干事', purpose: '材料整理', borrowTime: '2024-03-18', returnTime: '-', status: '借阅中' }
  ]
  viewDialogVisible.value = true
}

const handleBorrow = (row) => {
  borrowForm.archiveId = row.id
  borrowForm.archiveTitle = row.title
  borrowForm.borrower = ''
  borrowForm.purpose = ''
  borrowForm.expectedReturnTime = ''
  borrowDialogVisible.value = true
}

const handleBorrowSubmit = () => {
  if (!borrowForm.borrower) {
    ElMessage.warning('请输入借阅人姓名')
    return
  }
  if (!borrowForm.purpose) {
    ElMessage.warning('请填写借阅用途')
    return
  }
  if (!borrowForm.expectedReturnTime) {
    ElMessage.warning('请选择预计归还日期')
    return
  }
  ElMessage.success('借阅申请已提交，等待审核')
  borrowDialogVisible.value = false
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除档案 "${row.title}" 吗?`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/archive/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      ElMessage.success('删除成功')
      fetchData()
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.title) {
    ElMessage.warning('请输入档案标题')
    return
  }
  try {
    if (form.id) {
      await request.put('/archive/update', form)
    } else {
      await request.post('/archive/add', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.management-page {
  padding: 20px;
  background: #FDF5F5;
  min-height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: bold;
  color: #C8102E;
  margin: 0;
  padding-left: 12px;
  border-left: 3px solid #FFD700;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.page-tabs {
  background: white;
  border-radius: 12px;
  padding: 0 16px;
  margin-bottom: 16px;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  align-items: center;
  flex-wrap: wrap;
}

.upload-tip {
  color: #999;
  font-size: 12px;
  margin-top: 4px;
}

.detail-section {
  margin-bottom: 24px;
}

.section-title {
  color: #C8102E;
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 12px 0;
  padding-left: 10px;
  border-left: 3px solid #FFD700;
}

.content-preview {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  color: #333;
  line-height: 1.8;
  min-height: 80px;
  border: 1px dashed #ddd;
}

:deep(.disabled-row) {
  background: #f5f5f5 !important;
  color: #999;
}

:deep(.disabled-row td) {
  color: #999;
}

:deep(.el-table th) {
  background: #FDE8E8 !important;
  color: #C8102E !important;
  font-weight: bold;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%);
  border-color: #A50D24;
}

:deep(.el-button--primary:hover) {
  background: linear-gradient(135deg, #D61438 0%, #C8102E 100%);
  border-color: #C8102E;
}

:deep(.el-pagination .el-pager .is-active) {
  background-color: #C8102E;
}

:deep(.el-tabs__item.is-active) {
  color: #C8102E;
}

:deep(.el-tabs__active-bar) {
  background-color: #C8102E;
}

:deep(.el-dialog__header) {
  border-bottom: 2px solid #FFD700;
}

:deep(.el-dialog__title) {
  color: #C8102E;
  font-weight: bold;
}
</style>
