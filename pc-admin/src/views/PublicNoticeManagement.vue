
<template>
  <div class="public-notice-management">
    <div class="page-header">
      <h2>三务公开管理</h2>
      <el-button type="primary" @click="showAddDialog = true">发布公示</el-button>
    </div>

    <el-select v-model="typeFilter" placeholder="筛选类型" style="width: 150px; margin-bottom: 16px;">
      <el-option label="全部" value="" />
      <el-option label="党务公开" value="党务公开" />
      <el-option label="村务公开" value="村务公开" />
      <el-option label="财务公开" value="财务公开" />
    </el-select>

    <el-table :data="noticeList" border>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="noticeType" label="类型">
        <template #default="scope">
          <el-tag :type="getTypeTagType(scope.row.noticeType)">{{ scope.row.noticeType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startDate" label="开始日期" />
      <el-table-column prop="endDate" label="结束日期" />
      <el-table-column prop="createTime" label="发布时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="editNotice(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteNotice(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showAddDialog" :title="editForm.id ? '编辑公示' : '发布公示'" width="600px">
      <el-form :model="editForm">
        <el-form-item label="标题">
          <el-input v-model="editForm.title" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="editForm.noticeType">
            <el-option label="党务公开" value="党务公开" />
            <el-option label="村务公开" value="村务公开" />
            <el-option label="财务公开" value="财务公开" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" v-model="editForm.content" rows="6" />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="editForm.startDate" type="date" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="editForm.endDate" type="date" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveNotice">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const noticeList = ref([])
const typeFilter = ref('')
const showAddDialog = ref(false)

const editForm = reactive({
  id: null,
  title: '',
  noticeType: '党务公开',
  content: '',
  startDate: '',
  endDate: ''
})

const getTypeTagType = (type) => {
  if (type === '党务公开') return 'danger'
  if (type === '村务公开') return 'primary'
  if (type === '财务公开') return 'warning'
  return 'default'
}

const fetchNotices = async () => {
  try {
    const res = await request.get('/public-notice/list', { params: { noticeType: typeFilter.value } })
    noticeList.value = res.data.records
  } catch (error) {
    console.error('获取公示列表失败', error)
  }
}

const editNotice = (row) => {
  Object.assign(editForm, row)
  showAddDialog.value = true
}

const deleteNotice = async (id) => {
  try {
    await request.delete(`/public-notice/${id}`)
    ElMessage.success('删除成功')
    fetchNotices()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const saveNotice = async () => {
  try {
    if (editForm.id) {
      await request.put('/public-notice', editForm)
    } else {
      await request.post('/public-notice', editForm)
    }
    ElMessage.success('保存成功')
    showAddDialog.value = false
    fetchNotices()
    Object.assign(editForm, { id: null, title: '', noticeType: '党务公开', content: '', startDate: '', endDate: '' })
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

watch(typeFilter, () => {
  fetchNotices()
})

onMounted(() => {
  fetchNotices()
})
</script>

<style scoped>
.public-notice-management {
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
