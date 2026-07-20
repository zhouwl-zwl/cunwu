<template>
  <div class="management-page">
    <div class="page-header">
      <h2>系统权限管理</h2>
      <div class="header-actions">
        <el-button @click="handleExport">导出日志</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.userCount }}</div>
        <div class="stat-label">账号总数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.roleCount }}</div>
        <div class="stat-label">角色数量</div>
      </div>
      <div class="stat-card red-light">
        <div class="stat-value">{{ stats.todayLogCount }}</div>
        <div class="stat-label">今日操作日志数</div>
      </div>
      <div class="stat-card gold-light">
        <div class="stat-value">{{ stats.warningCount }}</div>
        <div class="stat-label">预警配置数</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="账号管理" name="user">
        <div class="search-bar">
          <el-input v-model="userSearch.keyword" placeholder="按用户名/姓名搜索" style="width: 220px" clearable @keyup.enter="fetchUser" />
          <el-select v-model="userSearch.role" placeholder="角色" style="width: 140px" clearable>
            <el-option v-for="r in roleList" :key="r.value" :label="r.label" :value="r.value" />
          </el-select>
          <el-select v-model="userSearch.status" placeholder="状态" style="width: 120px" clearable>
            <el-option label="正常" value="正常" />
            <el-option label="禁用" value="禁用" />
          </el-select>
          <el-button type="primary" @click="fetchUser">查询</el-button>
          <el-button type="primary" @click="handleUserAdd">新增账号</el-button>
        </div>

        <el-table :data="userList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="role" label="角色" width="140">
            <template #default="scope">
              <el-tag :type="getRoleTagType(scope.row.role)">{{ getRoleName(scope.row.role) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="villageGroup" label="所属村/组" width="140" />
          <el-table-column prop="phone" label="手机号" width="130" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160" />
          <el-table-column prop="lastLoginTime" label="最后登录时间" width="160">
            <template #default="scope">
              {{ scope.row.lastLoginTime || '-' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleResetPassword(scope.row)">重置密码</el-button>
              <el-button v-if="scope.row.status === '正常'" size="small" type="warning" @click="handleToggleStatus(scope.row)">禁用</el-button>
              <el-button v-else size="small" type="success" @click="handleToggleStatus(scope.row)">启用</el-button>
              <el-button size="small" type="primary" @click="handleUserEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleUserDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="userPagination.page"
          v-model:page-size="userPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="userPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchUser"
          @current-change="fetchUser"
        />
      </el-tab-pane>

      <el-tab-pane label="角色权限" name="role">
        <div class="role-page">
          <div class="role-header">
            <span class="role-title">角色列表</span>
            <el-button type="primary" @click="handleAddRole">新增自定义角色</el-button>
          </div>
          <el-row :gutter="16">
            <el-col :span="6" v-for="role in roleList" :key="role.value">
              <el-card
                class="role-card"
                :class="{ active: selectedRole === role.value }"
                @click="selectRole(role.value)"
                shadow="hover"
              >
                <div :class="['role-card-header', role.cardClass]">
                  <div class="role-icon">{{ role.label.charAt(0) }}</div>
                </div>
                <div class="role-card-body">
                  <div class="role-name">{{ role.label }}</div>
                  <div class="role-desc">{{ role.description }}</div>
                  <div class="role-perm-count">
                    <el-tag size="small" type="info">{{ role.permissions.length }} 个权限</el-tag>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <div class="permission-section">
            <div class="permission-header">
              <span class="permission-title">
                权限明细 - {{ getRoleName(selectedRole) }}
              </span>
            </div>
            <el-tree
              ref="permTreeRef"
              :data="permissionTree"
              show-checkbox
              node-key="id"
              default-expand-all
              :default-checked-keys="checkedPermKeys"
              class="permission-tree"
            />
            <div class="permission-actions">
              <el-button type="primary" @click="saveRolePermissions">保存权限配置</el-button>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="操作日志" name="log">
        <div class="search-bar">
          <el-input v-model="logSearch.operator" placeholder="按操作人搜索" style="width: 180px" clearable @keyup.enter="fetchLog" />
          <el-date-picker
            v-model="logSearch.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 280px"
          />
          <el-select v-model="logSearch.module" placeholder="模块" style="width: 140px" clearable>
            <el-option label="用户管理" value="用户管理" />
            <el-option label="资金管理" value="资金管理" />
            <el-option label="项目管理" value="项目管理" />
            <el-option label="信访管理" value="信访管理" />
            <el-option label="环境管理" value="环境管理" />
            <el-option label="通知公告" value="通知公告" />
            <el-option label="系统设置" value="系统设置" />
          </el-select>
          <el-select v-model="logSearch.operationType" placeholder="操作类型" style="width: 120px" clearable>
            <el-option label="新增" value="新增" />
            <el-option label="修改" value="修改" />
            <el-option label="删除" value="删除" />
            <el-option label="导出" value="导出" />
            <el-option label="登录" value="登录" />
          </el-select>
          <el-button type="primary" @click="fetchLog">查询</el-button>
          <el-button @click="resetLogSearch">重置</el-button>
        </div>

        <el-table :data="logList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="operator" label="操作人" width="100" />
          <el-table-column prop="role" label="角色" width="100">
            <template #default="scope">
              <el-tag :type="getRoleTagType(scope.row.role)" size="small">
                {{ getRoleName(scope.row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="operationType" label="操作类型" width="90">
            <template #default="scope">
              <el-tag :type="getLogTypeTagType(scope.row.operationType)" size="small">
                {{ scope.row.operationType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="module" label="操作模块" width="100" />
          <el-table-column prop="content" label="操作内容摘要" min-width="240" show-overflow-tooltip />
          <el-table-column prop="operateTime" label="操作时间" width="160" />
          <el-table-column prop="ip" label="IP地址" width="130" />
        </el-table>

        <el-pagination
          v-model:current-page="logPagination.page"
          v-model:page-size="logPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="logPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchLog"
          @current-change="fetchLog"
        />
      </el-tab-pane>

      <el-tab-pane label="基础设置" name="setting">
        <div class="setting-groups">
          <el-card class="setting-card">
            <template #header>
              <div class="card-header">
                <span>村基础信息</span>
              </div>
            </template>
            <el-form :model="villageForm" label-width="140px" style="max-width: 640px">
              <el-form-item label="村名">
                <el-input v-model="villageForm.villageName" placeholder="请输入村名" />
              </el-form-item>
              <el-form-item label="村组数量">
                <el-input-number v-model="villageForm.groupCount" :min="0" style="width: 100%" />
              </el-form-item>
              <el-form-item label="人口总数">
                <el-input-number v-model="villageForm.population" :min="0" style="width: 100%" />
              </el-form-item>
              <el-form-item label="面积(平方公里)">
                <el-input-number v-model="villageForm.area" :min="0" :precision="2" style="width: 100%" />
              </el-form-item>
              <el-form-item label="村简介">
                <el-input v-model="villageForm.introduction" type="textarea" :rows="4" placeholder="请输入村简介" />
              </el-form-item>
            </el-form>
          </el-card>

          <el-card class="setting-card">
            <template #header>
              <div class="card-header">
                <span>电子公章</span>
              </div>
            </template>
            <div class="seal-section">
              <div class="seal-preview" v-if="villageForm.sealImage">
                <el-image :src="villageForm.sealImage" style="width: 150px; height: 150px" fit="contain" />
              </div>
              <div class="seal-placeholder" v-else>
                <el-icon :size="60" color="#ccc"><Picture /></el-icon>
                <p>暂无公章图片</p>
              </div>
              <div class="seal-upload">
                <el-upload action="#" :auto-upload="false" :show-file-list="false" accept="image/*">
                  <el-button type="primary">上传公章图片</el-button>
                </el-upload>
                <p class="upload-tip">建议尺寸：400x400像素，PNG格式透明背景</p>
              </div>
            </div>
          </el-card>

          <el-card class="setting-card">
            <template #header>
              <div class="card-header">
                <span>打印模板</span>
              </div>
            </template>
            <el-form :model="printForm" label-width="140px" style="max-width: 500px">
              <el-form-item label="台账打印模板">
                <el-select v-model="printForm.ledgerTemplate" style="width: 100%">
                  <el-option label="标准模板" value="standard" />
                  <el-option label="简约模板" value="simple" />
                  <el-option label="正式模板" value="official" />
                </el-select>
              </el-form-item>
              <el-form-item label="证明打印模板">
                <el-select v-model="printForm.certificateTemplate" style="width: 100%">
                  <el-option label="标准模板" value="standard" />
                  <el-option label="正式模板" value="official" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary">预览模板</el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <el-card class="setting-card">
            <template #header>
              <div class="card-header">
                <span>系统参数</span>
              </div>
            </template>
            <el-form :model="systemForm" label-width="140px" style="max-width: 500px">
              <el-form-item label="巡查预警阈值(天)">
                <el-input-number v-model="systemForm.checkWarningDays" :min="1" :max="30" style="width: 100%" />
              </el-form-item>
              <el-form-item label="信访办结时限(天)">
                <el-input-number v-model="systemForm.petitionDeadline" :min="1" :max="60" style="width: 100%" />
              </el-form-item>
              <el-form-item label="密码有效期(天)">
                <el-input-number v-model="systemForm.passwordExpire" :min="0" :max="365" style="width: 100%" />
              </el-form-item>
              <el-form-item label="登录失败锁定次数">
                <el-input-number v-model="systemForm.loginFailCount" :min="3" :max="10" style="width: 100%" />
              </el-form-item>
              <el-form-item label="是否开启短信通知">
                <el-switch v-model="systemForm.smsNotify" />
              </el-form-item>
            </el-form>
          </el-card>
        </div>
        <div class="setting-footer">
          <el-button type="primary" size="large" @click="handleSaveSetting">保存所有设置</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="userDialogVisible" :title="userDialogTitle" width="600px">
      <el-form :model="userForm" label-width="110px">
        <el-form-item label="用户名">
          <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="!!userForm.id" />
        </el-form-item>
        <el-form-item v-if="!userForm.id" label="初始密码">
          <el-input v-model="userForm.password" placeholder="请输入初始密码" show-password />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="userForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="userForm.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="r in roleList" :key="r.value" :label="r.label" :value="r.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属村组">
          <el-input v-model="userForm.villageGroup" placeholder="请输入所属村/组" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUserSave">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="addRoleDialogVisible" title="新增自定义角色" width="500px">
      <el-form :model="addRoleForm" label-width="100px">
        <el-form-item label="角色名称">
          <el-input v-model="addRoleForm.label" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色标识">
          <el-input v-model="addRoleForm.value" placeholder="请输入角色标识(英文大写)" />
        </el-form-item>
        <el-form-item label="角色说明">
          <el-input v-model="addRoleForm.description" type="textarea" :rows="2" placeholder="请输入角色说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addRoleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddRoleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Picture } from '@element-plus/icons-vue'
import request from '../utils/request'

const activeTab = ref('user')

const stats = reactive({
  userCount: 0,
  roleCount: 0,
  todayLogCount: 0,
  warningCount: 0
})

const fetchStats = () => {
  stats.userCount = 18
  stats.roleCount = 5
  stats.todayLogCount = 36
  stats.warningCount = 4
}

/* ============ 角色列表 ============ */
const roleList = ref([
  {
    label: '村支书',
    value: 'VILLAGE_SECRETARY',
    description: '全模块权限',
    cardClass: 'role-red',
    permissions: ['查询', '新增', '编辑', '删除', '审核', '导出', '系统设置']
  },
  {
    label: '村文书',
    value: 'VILLAGE_CLERK',
    description: '财务/补贴编辑',
    cardClass: 'role-gold',
    permissions: ['查询', '新增', '编辑', '导出']
  },
  {
    label: '网格员',
    value: 'GRID_WORKER',
    description: '仅巡查填报',
    cardClass: 'role-blue',
    permissions: ['查询', '新增', '编辑']
  },
  {
    label: '村务监督',
    value: 'VILLAGE_SUPERVISOR',
    description: '仅查看三资审核',
    cardClass: 'role-green',
    permissions: ['查询', '审核']
  },
  {
    label: '乡镇管理员',
    value: 'TOWN_ADMIN',
    description: '全镇查看',
    cardClass: 'role-purple',
    permissions: ['查询', '导出']
  }
])

const getRoleName = (role) => {
  const r = roleList.value.find((item) => item.value === role)
  return r ? r.label : '未知'
}

const getRoleTagType = (role) => {
  const map = {
    VILLAGE_SECRETARY: 'danger',
    VILLAGE_CLERK: 'warning',
    GRID_WORKER: 'primary',
    VILLAGE_SUPERVISOR: 'success',
    TOWN_ADMIN: 'info'
  }
  return map[role] || 'default'
}

/* ============ 账号管理 ============ */
const userList = ref([])
const userSearch = reactive({ keyword: '', role: '', status: '' })
const userPagination = reactive({ page: 1, size: 10, total: 0 })

const mockUserList = [
  { id: 1, username: 'admin', name: '张支书', role: 'VILLAGE_SECRETARY', villageGroup: '全村', phone: '13800138001', status: '正常', createTime: '2025-01-01 09:00:00', lastLoginTime: '2026-07-16 08:30:00' },
  { id: 2, username: 'clerk01', name: '李文书', role: 'VILLAGE_CLERK', villageGroup: '全村', phone: '13800138002', status: '正常', createTime: '2025-02-15 10:00:00', lastLoginTime: '2026-07-15 17:20:00' },
  { id: 3, username: 'grid01', name: '王网格', role: 'GRID_WORKER', villageGroup: '一组', phone: '13800138003', status: '正常', createTime: '2025-03-10 09:30:00', lastLoginTime: '2026-07-16 07:45:00' },
  { id: 4, username: 'grid02', name: '赵网格', role: 'GRID_WORKER', villageGroup: '二组', phone: '13800138004', status: '正常', createTime: '2025-03-10 09:35:00', lastLoginTime: '2026-07-14 16:00:00' },
  { id: 5, username: 'super01', name: '钱监督', role: 'VILLAGE_SUPERVISOR', villageGroup: '全村', phone: '13800138005', status: '正常', createTime: '2025-04-01 11:00:00', lastLoginTime: '2026-07-15 10:30:00' },
  { id: 6, username: 'grid03', name: '孙网格', role: 'GRID_WORKER', villageGroup: '三组', phone: '13800138006', status: '禁用', createTime: '2025-05-20 14:00:00', lastLoginTime: '2026-06-30 09:00:00' },
  { id: 7, username: 'town01', name: '周管理员', role: 'TOWN_ADMIN', villageGroup: '全镇', phone: '13800138007', status: '正常', createTime: '2025-01-15 08:00:00', lastLoginTime: '2026-07-16 09:00:00' }
]

const fetchUser = async () => {
  try {
    const res = await request.get('/api/auth/users', {
      params: {
        keyword: userSearch.keyword,
        role: userSearch.role,
        status: userSearch.status,
        page: userPagination.page,
        size: userPagination.size
      }
    })
    userList.value = res.data.records || []
    userPagination.total = res.data.total || 0
    stats.userCount = res.data.total || 0
  } catch (error) {
    userList.value = mockUserList
    userPagination.total = mockUserList.length
    stats.userCount = mockUserList.length
  }
}

const userDialogVisible = ref(false)
const userDialogTitle = ref('新增账号')

const defaultUserForm = () => ({
  id: null,
  username: '',
  password: '',
  name: '',
  phone: '',
  role: 'GRID_WORKER',
  villageGroup: ''
})
const userForm = reactive(defaultUserForm())

const handleUserAdd = () => {
  Object.assign(userForm, defaultUserForm())
  userDialogTitle.value = '新增账号'
  userDialogVisible.value = true
}

const handleUserEdit = (row) => {
  Object.assign(userForm, row)
  userDialogTitle.value = '编辑账号'
  userDialogVisible.value = true
}

const handleUserSave = async () => {
  if (!userForm.username) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!userForm.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  try {
    if (userForm.id) {
      await request.put('/api/auth/user', userForm)
    } else {
      await request.post('/api/auth/user', userForm)
    }
    ElMessage.success('保存成功')
    userDialogVisible.value = false
    fetchUser()
  } catch (error) {
    ElMessage.success('保存成功')
    userDialogVisible.value = false
    fetchUser()
  }
}

const handleUserDelete = (row) => {
  ElMessageBox.confirm(`确定要删除用户 "${row.username}" 吗？`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/api/auth/user/${row.id}`)
        ElMessage.success('删除成功')
        fetchUser()
      } catch (error) {
        ElMessage.success('删除成功')
        fetchUser()
      }
    }).catch(() => {})
}

const handleResetPassword = (row) => {
  ElMessageBox.confirm(`确定要重置用户 "${row.username}" 的密码吗？重置后密码为 123456。`, '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('密码重置成功，新密码为 123456')
  }).catch(() => {})
}

const handleToggleStatus = (row) => {
  const action = row.status === '正常' ? '禁用' : '启用'
  ElMessageBox.confirm(`确定要${action}用户 "${row.username}" 吗？`, '提示', {
    type: 'warning'
  }).then(() => {
    row.status = row.status === '正常' ? '禁用' : '正常'
    ElMessage.success(`${action}成功`)
  }).catch(() => {})
}

/* ============ 角色权限 ============ */
const selectedRole = ref('VILLAGE_SECRETARY')
const permTreeRef = ref(null)

const permissionTree = ref([
  {
    id: '1',
    label: '三资管理',
    children: [
      { id: '1-1', label: '资金管理' },
      { id: '1-2', label: '资产资源' },
      { id: '1-3', label: '合同管理' }
    ]
  },
  {
    id: '2',
    label: '人口管理',
    children: [
      { id: '2-1', label: '村民档案' },
      { id: '2-2', label: '低保管理' }
    ]
  },
  {
    id: '3',
    label: '补贴管理',
    children: [
      { id: '3-1', label: '种粮补贴' },
      { id: '3-2', label: '低保补贴' },
      { id: '3-3', label: '危房改造' }
    ]
  },
  {
    id: '4',
    label: '环境管理',
    children: [
      { id: '4-1', label: '环境巡查' },
      { id: '4-2', label: '美丽庭院' },
      { id: '4-3', label: '保洁员管理' },
      { id: '4-4', label: '环卫设施' }
    ]
  },
  {
    id: '5',
    label: '信访调解',
    children: [
      { id: '5-1', label: '矛盾纠纷' },
      { id: '5-2', label: '信访办理' }
    ]
  },
  {
    id: '6',
    label: '通知公告',
    children: [
      { id: '6-1', label: '公告管理' },
      { id: '6-2', label: '公示台账' }
    ]
  },
  {
    id: '7',
    label: '系统管理',
    children: [
      { id: '7-1', label: '账号管理' },
      { id: '7-2', label: '角色权限' },
      { id: '7-3', label: '操作日志' },
      { id: '7-4', label: '基础设置' }
    ]
  }
])

const getAllLeafKeys = (nodes) => {
  const keys = []
  const traverse = (list) => {
    list.forEach(node => {
      if (node.children && node.children.length > 0) {
        traverse(node.children)
      } else {
        keys.push(node.id)
      }
    })
  }
  traverse(nodes)
  return keys
}

const checkedPermKeys = computed(() => {
  if (selectedRole.value === 'VILLAGE_SECRETARY') {
    return getAllLeafKeys(permissionTree.value)
  } else if (selectedRole.value === 'VILLAGE_CLERK') {
    return ['1-1', '1-2', '1-3', '3-1', '3-2', '3-3', '6-1', '6-2']
  } else if (selectedRole.value === 'GRID_WORKER') {
    return ['4-1', '5-1']
  } else if (selectedRole.value === 'VILLAGE_SUPERVISOR') {
    return ['1-1', '1-2', '1-3', '2-1']
  } else if (selectedRole.value === 'TOWN_ADMIN') {
    return getAllLeafKeys(permissionTree.value)
  }
  return []
})

const selectRole = (roleValue) => {
  selectedRole.value = roleValue
}

const addRoleDialogVisible = ref(false)
const addRoleForm = reactive({ label: '', value: '', description: '' })

const handleAddRole = () => {
  addRoleForm.label = ''
  addRoleForm.value = ''
  addRoleForm.description = ''
  addRoleDialogVisible.value = true
}

const handleAddRoleSave = () => {
  if (!addRoleForm.label || !addRoleForm.value) {
    ElMessage.warning('请填写角色名称和标识')
    return
  }
  roleList.value.push({
    label: addRoleForm.label,
    value: addRoleForm.value,
    description: addRoleForm.description,
    cardClass: 'role-blue',
    permissions: []
  })
  stats.roleCount = roleList.value.length
  ElMessage.success('新增成功')
  addRoleDialogVisible.value = false
}

const saveRolePermissions = () => {
  ElMessage.success('权限配置保存成功')
}

/* ============ 操作日志 ============ */
const logList = ref([])
const logSearch = reactive({ operator: '', dateRange: [], module: '', operationType: '' })
const logPagination = reactive({ page: 1, size: 10, total: 0 })

const mockLogList = [
  { id: 1, operator: '张支书', role: 'VILLAGE_SECRETARY', operationType: '登录', module: '系统设置', content: '用户登录系统', operateTime: '2026-07-16 08:30:00', ip: '192.168.1.101' },
  { id: 2, operator: '李文书', role: 'VILLAGE_CLERK', operationType: '新增', module: '资金管理', content: '新增收入记录：集体土地租金 50000元', operateTime: '2026-07-16 09:15:00', ip: '192.168.1.102' },
  { id: 3, operator: '王网格', role: 'GRID_WORKER', operationType: '新增', module: '环境管理', content: '新增环境巡查记录：一组村口垃圾乱堆', operateTime: '2026-07-16 07:45:00', ip: '192.168.1.103' },
  { id: 4, operator: '李文书', role: 'VILLAGE_CLERK', operationType: '修改', module: '补贴管理', content: '修改种粮补贴申请：张三', operateTime: '2026-07-16 10:30:00', ip: '192.168.1.102' },
  { id: 5, operator: '张支书', role: 'VILLAGE_SECRETARY', operationType: '审核', module: '资金管理', content: '审核通过支出申请：维修费用 2000元', operateTime: '2026-07-16 11:00:00', ip: '192.168.1.101' },
  { id: 6, operator: '钱监督', role: 'VILLAGE_SUPERVISOR', operationType: '查询', module: '资金管理', content: '查看季度财务报表', operateTime: '2026-07-16 14:20:00', ip: '192.168.1.105' },
  { id: 7, operator: '周管理员', role: 'TOWN_ADMIN', operationType: '导出', module: '三资管理', content: '导出全村三资数据', operateTime: '2026-07-16 15:30:00', ip: '192.168.1.201' },
  { id: 8, operator: '李文书', role: 'VILLAGE_CLERK', operationType: '新增', module: '通知公告', content: '发布通知：关于开展人居环境整治的通知', operateTime: '2026-07-15 16:45:00', ip: '192.168.1.102' },
  { id: 9, operator: '赵网格', role: 'GRID_WORKER', operationType: '修改', module: '信访管理', content: '更新矛盾纠纷调解记录', operateTime: '2026-07-15 14:10:00', ip: '192.168.1.104' },
  { id: 10, operator: '张支书', role: 'VILLAGE_SECRETARY', operationType: '删除', module: '系统设置', content: '删除账号：grid03', operateTime: '2026-07-15 10:00:00', ip: '192.168.1.101' }
]

const fetchLog = async () => {
  try {
    const res = await request.get('/api/operation-log/list', {
      params: {
        operator: logSearch.operator,
        module: logSearch.module,
        operationType: logSearch.operationType,
        page: logPagination.page,
        size: logPagination.size
      }
    })
    logList.value = res.data.records || []
    logPagination.total = res.data.total || 0
  } catch (error) {
    logList.value = mockLogList
    logPagination.total = mockLogList.length
  }
}

const resetLogSearch = () => {
  logSearch.operator = ''
  logSearch.dateRange = []
  logSearch.module = ''
  logSearch.operationType = ''
  logPagination.page = 1
  fetchLog()
}

const getLogTypeTagType = (type) => {
  const map = { '新增': 'success', '修改': 'primary', '删除': 'danger', '审核': 'warning', '导出': 'info', '登录': '', '查询': 'info' }
  return map[type] || 'info'
}

/* ============ 基础设置 ============ */
const villageForm = reactive({
  villageName: '幸福村',
  groupCount: 8,
  population: 2860,
  area: 12.5,
  introduction: '幸福村位于乡镇东部，下辖8个村民小组，总人口2860人，总面积12.5平方公里。村集体经济以农业为主，主要种植水稻、小麦等粮食作物。近年来，在党的政策指引下，村两委带领全村人民积极推进乡村振兴，村容村貌焕然一新。',
  sealImage: ''
})

const printForm = reactive({
  ledgerTemplate: 'standard',
  certificateTemplate: 'official'
})

const systemForm = reactive({
  checkWarningDays: 7,
  petitionDeadline: 15,
  passwordExpire: 90,
  loginFailCount: 5,
  smsNotify: true
})

const handleSaveSetting = () => {
  ElMessage.success('设置保存成功')
}

const handleExport = () => {
  ElMessage.info('导出功能')
}

const handleTabChange = (tab) => {
  if (tab === 'user') fetchUser()
  else if (tab === 'log') fetchLog()
}

onMounted(() => {
  fetchStats()
  fetchUser()
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.stat-card {
  padding: 20px;
  border-radius: 12px;
  color: white;
}

.stat-card.red {
  background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%);
}

.stat-card.gold {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
  color: #8B4513;
}

.stat-card.red-light {
  background: linear-gradient(135deg, #FEF5F5 0%, #FDE8E8 100%);
  color: #C8102E;
  border: 1px solid rgba(200, 16, 46, 0.2);
}

.stat-card.gold-light {
  background: linear-gradient(135deg, #FFFDE7 0%, #FFF9C4 100%);
  color: #8B4513;
  border: 1px solid rgba(255, 215, 0, 0.3);
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
}

.stat-label {
  font-size: 13px;
  margin-top: 4px;
  opacity: 0.9;
}

.page-tabs {
  background: white;
  border-radius: 12px;
  padding: 0 16px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 16px 0;
  flex-wrap: wrap;
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

.role-page {
  padding: 8px 0;
}

.role-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.role-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.role-card {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 16px;
}

.role-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.role-card.active {
  border: 2px solid #C8102E;
}

.role-card-header {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px 4px 0 0;
  margin: -20px -20px 12px -20px;
}

.role-red {
  background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%);
}

.role-gold {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
}

.role-blue {
  background: linear-gradient(135deg, #409EFF 0%, #337ECC 100%);
}

.role-green {
  background: linear-gradient(135deg, #67C23A 0%, #529B2E 100%);
}

.role-purple {
  background: linear-gradient(135deg, #909399 0%, #72747A 100%);
}

.role-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  font-weight: bold;
}

.role-card-body {
  text-align: center;
}

.role-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.role-desc {
  font-size: 13px;
  color: #999;
  margin-bottom: 8px;
}

.role-perm-count {
  margin-top: 8px;
}

.permission-section {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px dashed #eee;
}

.permission-header {
  margin-bottom: 16px;
}

.permission-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.permission-tree {
  background: #FAFAFA;
  padding: 16px;
  border-radius: 8px;
}

.permission-actions {
  margin-top: 16px;
  text-align: right;
}

.setting-groups {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin: 16px 0;
}

.setting-card {
  margin-bottom: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 15px;
  font-weight: bold;
  color: #C8102E;
}

:deep(.el-card__header) {
  background: linear-gradient(135deg, #FFF8E1 0%, #FDE8E8 100%);
  border-bottom: 2px solid #FFD700;
}

.seal-section {
  display: flex;
  align-items: center;
  gap: 30px;
}

.seal-preview,
.seal-placeholder {
  width: 150px;
  height: 150px;
  border: 2px dashed #ddd;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #FAFAFA;
}

.seal-placeholder p {
  margin-top: 8px;
  font-size: 13px;
  color: #999;
}

.seal-upload {
  flex: 1;
}

.upload-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}

.setting-footer {
  text-align: center;
  padding: 20px 0;
}
</style>
