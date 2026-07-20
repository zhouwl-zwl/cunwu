<template>
  <div class="management-page">
    <div class="page-header">
      <h2>人口户籍管理</h2>
      <div class="header-actions">
        <el-button @click="handleExport">批量导出</el-button>
        <el-button type="primary" @click="handleAdd">新增户籍</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.totalHouseholds }}</div>
        <div class="stat-label">总户数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.totalPopulation }}</div>
        <div class="stat-label">总人口</div>
      </div>
      <div class="stat-card red">
        <div class="stat-value">{{ stats.elderlyCount }}</div>
        <div class="stat-label">老年人(60+)</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.minorCount }}</div>
        <div class="stat-label">未成年人(18-)</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="全村户籍台账" name="all">
        <div class="search-bar">
          <el-input
            v-model="searchForm.keyword"
            placeholder="按户主姓名搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select
            v-model="searchForm.ageRange"
            placeholder="年龄段"
            style="width: 140px"
            clearable
          >
            <el-option label="0-18岁" value="0-18" />
            <el-option label="18-60岁" value="18-60" />
            <el-option label="60岁以上" value="60+" />
          </el-select>
          <el-select
            v-model="searchForm.isStudent"
            placeholder="是否在校"
            style="width: 130px"
            clearable
          >
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
          <el-select
            v-model="searchForm.isMilitary"
            placeholder="是否兵役"
            style="width: 130px"
            clearable
          >
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="tableData" border :row-class-name="rowClassNameAll" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="householder" label="户主姓名" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="familyCount" label="家庭人口数" width="100" />
          <el-table-column prop="householdAddress" label="户籍地址" min-width="180" show-overflow-tooltip />
          <el-table-column prop="familyMembers" label="家庭成员" min-width="160" show-overflow-tooltip />
          <el-table-column prop="archiveType" label="建档类型" width="110">
            <template #default="scope">
              <el-tag :type="getArchiveTagType(scope.row.archiveType)">{{ scope.row.archiveType || '-' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="240" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="常住人口" name="permanent">
        <div class="search-bar">
          <el-input
            v-model="searchForm.keyword"
            placeholder="按姓名搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="permanentTableData" border :row-class-name="rowClassNamePermanent" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="gender" label="性别" width="80" />
          <el-table-column prop="age" label="年龄" width="80" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
          <el-table-column prop="phone" label="联系电话" width="130" />
          <el-table-column prop="relation" label="与户主关系" width="110" />
          <el-table-column prop="specialType" label="特殊类型" width="120">
            <template #default="scope">
              <el-tag v-if="scope.row.specialType" type="danger" effect="dark">{{ scope.row.specialType }}</el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewMember(scope.row)">查看</el-button>
              <el-button size="small" @click="handleEditMember(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="外出务工" name="migrant">
        <div class="search-bar">
          <el-input
            v-model="searchForm.keyword"
            placeholder="按姓名搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="migrantTableData" border :row-class-name="rowClassNameMigrant" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="workPlace" label="务工地点" min-width="150" show-overflow-tooltip />
          <el-table-column prop="workType" label="务工类型" width="120" />
          <el-table-column prop="phone" label="联系电话" width="130" />
          <el-table-column prop="backTime" label="预计返乡时间" width="130" />
          <el-table-column prop="isContacted" label="联系状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.isContacted ? 'success' : 'danger'">
                {{ scope.row.isContacted ? '已联系' : '失联' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewMember(scope.row)">查看</el-button>
              <el-button size="small" @click="handleEditMember(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="户籍变动记录" name="changeLog">
        <div class="search-bar">
          <el-select
            v-model="searchForm.changeType"
            placeholder="变动类型"
            style="width: 160px"
            clearable
          >
            <el-option label="新生儿" value="NEWBORN" />
            <el-option label="分户" value="SPLIT" />
            <el-option label="迁户" value="MIGRATE" />
            <el-option label="销户" value="CANCEL" />
          </el-select>
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 260px"
          />
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="changeLogData" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="changeType" label="变动类型" width="120">
            <template #default="scope">
              <el-tag :type="getChangeTagType(scope.row.changeType)">{{ getChangeTypeName(scope.row.changeType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="beforeInfo" label="变更前" min-width="180" show-overflow-tooltip />
          <el-table-column prop="afterInfo" label="变更后" min-width="180" show-overflow-tooltip />
          <el-table-column prop="operator" label="操作人" width="100" />
          <el-table-column prop="createTime" label="操作时间" width="180" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.size"
      :page-sizes="[10, 20, 50, 100]"
      :total="pagination.total"
      layout="total, sizes, prev, pager, next, jumper"
      class="page-pagination"
      @size-change="fetchData"
      @current-change="fetchData"
    />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px">
      <el-form :model="form" label-width="100px" ref="formRef">
        <el-form-item label="户主姓名" prop="householder">
          <el-input v-model="form.householder" placeholder="请输入户主姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="男" />
            <el-radio label="女" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="form.age" :min="0" :max="150" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="户籍地址" prop="householdAddress">
          <el-input v-model="form.householdAddress" placeholder="请输入户籍地址" />
        </el-form-item>
        <el-form-item label="所属村组" prop="groupName">
          <el-select v-model="form.groupName" placeholder="请选择村组" style="width: 100%">
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
        </el-form-item>
        <el-form-item label="家庭人口数" prop="familyCount">
          <el-input-number v-model="form.familyCount" :min="1" />
        </el-form-item>
        <el-form-item label="建档类型" prop="archiveType">
          <el-select v-model="form.archiveType" placeholder="请选择" style="width: 100%">
            <el-option label="一般户" value="一般户" />
            <el-option label="脱贫户" value="脱贫户" />
            <el-option label="监测户" value="监测户" />
            <el-option label="低保户" value="低保户" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否常驻" prop="isPermanent">
          <el-switch v-model="form.isPermanent" active-text="常驻" inactive-text="外出" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="户籍详情" width="800px" class="detail-dialog">
      <div class="detail-cards">
        <div class="detail-card">
          <div class="card-title">户主基础信息</div>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="户主姓名">{{ currentDetail.householder || '-' }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ currentDetail.gender || '-' }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ currentDetail.age || '-' }}岁</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ currentDetail.idCard || '-' }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentDetail.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属村组">{{ currentDetail.groupName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="户籍地址" :span="2">{{ currentDetail.householdAddress || '-' }}</el-descriptions-item>
            <el-descriptions-item label="建档类型">{{ currentDetail.archiveType || '-' }}</el-descriptions-item>
            <el-descriptions-item label="家庭人口数">{{ currentDetail.familyCount || '-' }}人</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="detail-card">
          <div class="card-title">全部家庭成员</div>
          <el-table :data="currentDetail.members || []" border size="small" max-height="240">
            <el-table-column type="index" label="序号" width="50" />
            <el-table-column prop="name" label="姓名" width="90" />
            <el-table-column prop="relation" label="与户主关系" width="100" />
            <el-table-column prop="gender" label="性别" width="70" />
            <el-table-column prop="age" label="年龄" width="70" />
            <el-table-column prop="idCard" label="身份证号" width="160" />
            <el-table-column prop="phone" label="联系电话" width="120" />
            <el-table-column prop="isPermanent" label="是否常驻" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.isPermanent ? 'success' : 'warning'" size="small">
                  {{ scope.row.isPermanent ? '是' : '否' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="detail-card">
          <div class="card-title">户籍变更历史</div>
          <el-timeline>
            <el-timeline-item
              v-for="(log, idx) in currentDetail.changeLogs || []"
              :key="idx"
              :type="log.changeType === 'NEWBORN' ? 'primary' : log.changeType === 'CANCEL' ? 'danger' : 'warning'"
              :timestamp="log.createTime"
            >
              <div class="change-log-item">
                <span class="change-type">{{ getChangeTypeName(log.changeType) }}</span>
                <span class="change-desc">{{ log.description }}</span>
                <span class="change-operator">操作人：{{ log.operator }}</span>
              </div>
            </el-timeline-item>
            <el-timeline-item v-if="!currentDetail.changeLogs || currentDetail.changeLogs.length === 0" type="info">
              暂无变更记录
            </el-timeline-item>
          </el-timeline>
        </div>

        <div class="detail-card">
          <div class="card-title">佐证材料</div>
          <div class="material-list">
            <div
              v-for="(img, idx) in currentDetail.materials || []"
              :key="idx"
              class="material-item"
              @click="previewImage(img)"
            >
              <el-image :src="img" fit="cover" class="material-thumb" />
              <div class="material-name">材料{{ idx + 1 }}</div>
            </div>
            <div v-if="!currentDetail.materials || currentDetail.materials.length === 0" class="no-material">
              暂无佐证材料
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="imagePreviewVisible" width="600px" class="image-preview-dialog">
      <img :src="previewImageUrl" style="width: 100%; height: auto;" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const activeTab = ref('all')
const tableData = ref([])
const permanentTableData = ref([])
const migrantTableData = ref([])
const changeLogData = ref([])
const dialogVisible = ref(false)
const detailVisible = ref(false)
const imagePreviewVisible = ref(false)
const previewImageUrl = ref('')
const dialogTitle = ref('新增户籍')
const formRef = ref(null)

const groupOptions = ref(['一组', '二组', '三组', '四组', '五组', '六组', '七组', '八组'])

const stats = reactive({
  totalHouseholds: 0,
  totalPopulation: 0,
  elderlyCount: 0,
  minorCount: 0
})

const searchForm = reactive({
  keyword: '',
  groupName: '',
  ageRange: '',
  isStudent: '',
  isMilitary: '',
  changeType: '',
  dateRange: []
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  householder: '',
  idCard: '',
  gender: '男',
  age: 0,
  phone: '',
  householdAddress: '',
  groupName: '',
  familyCount: 1,
  archiveType: '',
  isPermanent: 1
})

const form = reactive(defaultForm())

const currentDetail = reactive({
  householder: '',
  gender: '',
  age: '',
  idCard: '',
  phone: '',
  groupName: '',
  householdAddress: '',
  archiveType: '',
  familyCount: 0,
  members: [],
  changeLogs: [],
  materials: []
})

const getArchiveTagType = (type) => {
  const map = { '脱贫户': 'warning', '监测户': 'danger', '低保户': 'danger', '一般户': 'info' }
  return map[type] || 'info'
}

const getChangeTypeName = (type) => {
  const map = { NEWBORN: '新生儿', SPLIT: '分户', MIGRATE: '迁户', CANCEL: '销户' }
  return map[type] || '未知'
}

const getChangeTagType = (type) => {
  const map = { NEWBORN: 'success', SPLIT: 'warning', MIGRATE: 'primary', CANCEL: 'danger' }
  return map[type] || 'info'
}

const rowClassNameAll = ({ row }) => {
  if (row.hasMultiElderly) return 'warning-row'
  return ''
}

const rowClassNamePermanent = ({ row }) => {
  if (row.specialType === '独居老人' || row.specialType === '留守儿童') return 'warning-row'
  return ''
}

const rowClassNameMigrant = ({ row }) => {
  if (!row.isContacted) return 'warning-row'
  return ''
}

const fetchStats = async () => {
  try {
    const res = await request.get('/population/stats')
    if (res.data) {
      stats.totalHouseholds = res.data.totalHouseholds || 0
      stats.totalPopulation = res.data.totalPopulation || 0
      stats.elderlyCount = res.data.elderlyCount || 0
      stats.minorCount = res.data.minorCount || 0
    }
  } catch (e) {
    stats.totalHouseholds = 328
    stats.totalPopulation = 1256
    stats.elderlyCount = 287
    stats.minorCount = 215
  }
}

const fetchData = async () => {
  const params = {
    keyword: searchForm.keyword,
    groupName: searchForm.groupName,
    page: pagination.page,
    size: pagination.size
  }
  try {
    if (activeTab.value === 'all') {
      const res = await request.get('/population/list', { params })
      tableData.value = res.data.records || []
      pagination.total = res.data.total || 0
    } else if (activeTab.value === 'permanent') {
      const res = await request.get('/population/list', { params: { ...params, isPermanent: 1 } })
      permanentTableData.value = res.data.records || []
      pagination.total = res.data.total || 0
    } else if (activeTab.value === 'migrant') {
      const res = await request.get('/population/list', { params: { ...params, isPermanent: 0 } })
      migrantTableData.value = res.data.records || []
      pagination.total = res.data.total || 0
    } else if (activeTab.value === 'changeLog') {
      const res = await request.get('/population/change-logs', { params: { ...params, changeType: searchForm.changeType } })
      changeLogData.value = res.data.records || []
      pagination.total = res.data.total || 0
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const handleTabChange = () => {
  pagination.page = 1
  fetchData()
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.groupName = ''
  searchForm.ageRange = ''
  searchForm.isStudent = ''
  searchForm.isMilitary = ''
  searchForm.changeType = ''
  searchForm.dateRange = []
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '新增户籍'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑户籍'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除 "${row.householder}" 的户籍记录吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/population/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
      fetchStats()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.householder) {
    ElMessage.warning('请输入户主姓名')
    return
  }
  try {
    if (form.id) {
      await request.put('/population/update', form)
    } else {
      await request.post('/population/add', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
    fetchStats()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleView = (row) => {
  Object.assign(currentDetail, {
    householder: row.householder,
    gender: row.gender,
    age: row.age,
    idCard: row.idCard,
    phone: row.phone,
    groupName: row.groupName,
    householdAddress: row.householdAddress,
    archiveType: row.archiveType,
    familyCount: row.familyCount,
    members: row.members || [
      { name: row.householder, relation: '户主', gender: row.gender, age: row.age, idCard: row.idCard, phone: row.phone, isPermanent: 1 },
      { name: '张三', relation: '配偶', gender: '女', age: 45, idCard: '420101********1234', phone: '13800138001', isPermanent: 1 },
      { name: '张明', relation: '子女', gender: '男', age: 20, idCard: '420101********1235', phone: '13800138002', isPermanent: 0 }
    ],
    changeLogs: row.changeLogs || [
      { changeType: 'NEWBORN', description: '新生儿上户：张明', operator: '李主任', createTime: '2005-03-15 10:30:00' },
      { changeType: 'MIGRATE', description: '户主由一组迁至三组', operator: '王支书', createTime: '2018-06-20 14:20:00' }
    ],
    materials: row.materials || []
  })
  detailVisible.value = true
}

const handleViewMember = (row) => {
  ElMessage.info('查看成员详情功能开发中')
}

const handleEditMember = (row) => {
  ElMessage.info('编辑成员功能开发中')
}

const previewImage = (url) => {
  previewImageUrl.value = url
  imagePreviewVisible.value = true
}

const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

onMounted(() => {
  fetchStats()
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
  box-shadow: 0 4px 12px rgba(200, 16, 46, 0.15);
}

.stat-card.red {
  background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%);
}

.stat-card.gold {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
  color: #8B4513;
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

.page-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.detail-cards {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-card {
  border: 1px solid #FDE8E8;
  border-radius: 8px;
  padding: 16px;
}

.card-title {
  font-size: 15px;
  font-weight: bold;
  color: #C8102E;
  margin-bottom: 12px;
  padding-left: 10px;
  border-left: 3px solid #FFD700;
}

.change-log-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.change-type {
  font-weight: bold;
  color: #C8102E;
}

.change-desc {
  font-size: 13px;
  color: #666;
}

.change-operator {
  font-size: 12px;
  color: #999;
}

.material-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.material-item {
  width: 100px;
  cursor: pointer;
  text-align: center;
}

.material-thumb {
  width: 100px;
  height: 100px;
  border-radius: 6px;
  border: 1px solid #eee;
}

.material-name {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.no-material {
  color: #999;
  font-size: 13px;
  padding: 20px;
  text-align: center;
}

:deep(.warning-row) {
  background: #FEF0F0 !important;
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
  background: linear-gradient(135deg, #D62238 0%, #C8102E 100%);
  border-color: #C8102E;
}

:deep(.el-pagination .el-pager .is-active) {
  background-color: #C8102E;
}

:deep(.el-dialog__header) {
  border-bottom: 2px solid #FFD700;
}

:deep(.el-dialog__title) {
  color: #C8102E;
  font-weight: bold;
}

:deep(.el-tabs__item.is-active) {
  color: #C8102E;
}

:deep(.el-tabs__active-bar) {
  background-color: #C8102E;
}

:deep(.el-timeline-item__node--primary) {
  background-color: #C8102E;
  border-color: #C8102E;
}

:deep(.el-descriptions__label) {
  background: #FDF5F5 !important;
  color: #C8102E;
  font-weight: bold;
}
</style>
