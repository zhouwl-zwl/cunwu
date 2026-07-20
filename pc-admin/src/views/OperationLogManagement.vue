
<template>
  <div class="management-page">
    <div class="page-header">
      <h2>操作日志</h2>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索操作人/模块"
        style="width: 220px"
        clearable
        @keyup.enter="handleSearch"
      />
      <el-select v-model="searchModule" placeholder="操作模块" style="width: 180px" clearable>
        <el-option label="用户管理" value="用户管理" />
        <el-option label="党建工作" value="党建工作" />
        <el-option label="政务工作" value="政务工作" />
        <el-option label="便民服务" value="便民服务" />
        <el-option label="三资管理" value="三资管理" />
        <el-option label="安全防护" value="安全防护" />
        <el-option label="乡村振兴" value="乡村振兴" />
        <el-option label="综合治理" value="综合治理" />
        <el-option label="公共功能" value="公共功能" />
        <el-option label="系统设置" value="系统设置" />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column prop="operator" label="操作人" width="120" />
      <el-table-column prop="module" label="操作模块" width="120" />
      <el-table-column prop="action" label="操作类型" width="120">
        <template #default="scope">
          <el-tag :type="getActionType(scope.row.action)">{{ scope.row.action }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="操作内容" min-width="240" />
      <el-table-column prop="ip" label="IP地址" width="140" />
      <el-table-column prop="operateTime" label="操作时间" width="180" />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'

const tableData = ref([])
const searchKeyword = ref('')
const searchModule = ref('')

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const getActionType = (action) => {
  if (action === '新增') return 'success'
  if (action === '修改') return 'warning'
  if (action === '删除') return 'danger'
  if (action === '查询') return 'info'
  if (action === '登录') return 'primary'
  if (action === '登出') return 'info'
  return 'default'
}

const fetchData = async () => {
  try {
    const res = await request.get('/operation-log/list', {
      params: {
        keyword: searchKeyword.value,
        module: searchModule.value,
        page: pagination.page,
        size: pagination.size
      }
    })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取操作日志列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
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
