
<template>
  <div class="user-management">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="showAddDialog = true">新增用户</el-button>
    </div>

    <el-input placeholder="搜索用户名" v-model="searchKeyword" style="width: 200px; margin-bottom: 16px;" />
    
    <el-table :data="userList" border>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="role" label="角色">
        <template #default="scope">
          <el-tag :type="getRoleTagType(scope.row.role)">{{ getRoleName(scope.row.role) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '已审核' : '待审核' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
          <el-button v-if="scope.row.status === 0" size="small" type="success" @click="approveUser(scope.row.id)">审核通过</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showAddDialog" :title="editUserForm.id ? '编辑用户' : '新增用户'">
      <el-form :model="editUserForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editUserForm.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="editUserForm.password" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="editUserForm.name" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editUserForm.phone" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="editUserForm.role">
            <el-option label="普通村民" value="VILLAGER" />
            <el-option label="村干部" value="VILLAGE_OFFICIAL" />
            <el-option label="驻村干部" value="RESIDENT_OFFICIAL" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const userList = ref([])
const searchKeyword = ref('')
const showAddDialog = ref(false)

const editUserForm = reactive({
  id: null,
  username: '',
  password: '',
  name: '',
  phone: '',
  role: 'VILLAGER'
})

const getRoleName = (role) => {
  if (role === 'VILLAGER') return '普通村民'
  if (role === 'VILLAGE_OFFICIAL') return '村干部'
  if (role === 'RESIDENT_OFFICIAL') return '驻村干部'
  return '未知'
}

const getRoleTagType = (role) => {
  if (role === 'VILLAGER') return 'default'
  if (role === 'VILLAGE_OFFICIAL') return 'primary'
  if (role === 'RESIDENT_OFFICIAL') return 'warning'
  return 'default'
}

const fetchUsers = async () => {
  try {
    const res = await request.get('/auth/users', { params: { keyword: searchKeyword.value } })
    userList.value = res.data.records
  } catch (error) {
    console.error('获取用户列表失败', error)
  }
}

const editUser = (row) => {
  Object.assign(editUserForm, row)
  showAddDialog.value = true
}

const deleteUser = async (id) => {
  try {
    await request.delete(`/auth/user/${id}`)
    ElMessage.success('删除成功')
    fetchUsers()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const approveUser = async (id) => {
  try {
    await request.put(`/auth/user/${id}/approve`)
    ElMessage.success('审核通过')
    fetchUsers()
  } catch (error) {
    ElMessage.error('审核失败')
  }
}

const saveUser = async () => {
  try {
    if (editUserForm.id) {
      await request.put('/auth/user', editUserForm)
    } else {
      await request.post('/auth/user', editUserForm)
    }
    ElMessage.success('保存成功')
    showAddDialog.value = false
    fetchUsers()
    Object.assign(editUserForm, { id: null, username: '', password: '', name: '', phone: '', role: 'VILLAGER' })
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-management {
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
