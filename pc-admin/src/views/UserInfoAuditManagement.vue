<template>
  <div class="audit-management">
    <div class="page-header">
      <h2>人员信息变更审核</h2>
      <div class="header-actions">
        <el-button size="small" type="success" @click="batchPass" :disabled="selectedIds.length === 0">批量通过</el-button>
        <el-button size="small" type="danger" @click="showBatchRejectDialog = true" :disabled="selectedIds.length === 0">批量驳回</el-button>
      </div>
    </div>

    <div class="search-bar">
      <el-input placeholder="搜索姓名" v-model="searchName" style="width: 180px; margin-right: 12px;" />
      <el-select v-model="auditStatus" placeholder="审核状态" style="width: 140px; margin-right: 12px;">
        <el-option label="全部" :value="null" />
        <el-option label="待审核" :value="0" />
        <el-option label="已通过" :value="1" />
        <el-option label="已驳回" :value="2" />
      </el-select>
      <el-select v-model="changeType" placeholder="变更类型" style="width: 140px; margin-right: 12px;">
        <el-option label="全部" :value="null" />
        <el-option label="党务变更" value="party" />
        <el-option label="职务变更" value="duty" />
        <el-option label="党务/职务变更" value="all" />
      </el-select>
      <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" style="width: 260px;" />
      <el-button type="primary" @click="fetchAuditList">查询</el-button>
    </div>

    <el-table :data="auditList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="auditId" label="序号" width="80" />
      <el-table-column prop="villageName" label="所属村" width="120" />
      <el-table-column prop="changeType" label="变更类型" width="130">
        <template #default="scope">
          <el-tag :type="getChangeTypeTagType(scope.row.changeType)">
            {{ getChangeTypeLabel(scope.row.changeType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="submitTime" label="提交时间" width="180" />
      <el-table-column prop="auditStatus" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.auditStatus)" effect="dark">
            {{ getStatusLabel(scope.row.auditStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditAdminName" label="审核人" width="100" />
      <el-table-column prop="auditTime" label="审核时间" width="180" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="viewDetail(scope.row)">查看详情</el-button>
          <el-button v-if="scope.row.auditStatus === 0" size="small" type="success" @click="auditPass(scope.row)">通过</el-button>
          <el-button v-if="scope.row.auditStatus === 0" size="small" type="danger" @click="showRejectDialog(scope.row)">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50]"
        :page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>

    <el-dialog v-model="showDetailDialog" title="审核详情" width="800px">
      <div class="detail-content">
        <div class="detail-header">
          <span class="detail-type">{{ getChangeTypeLabel(detailData.changeType) }}</span>
          <span :class="['detail-status', getStatusClass(detailData.auditStatus)]">
            {{ getStatusLabel(detailData.auditStatus) }}
          </span>
        </div>
        <div class="detail-info">
          <div class="info-item">
            <span class="info-label">提交时间：</span>
            <span>{{ detailData.submitTime }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">审核人：</span>
            <span>{{ detailData.auditAdminName || '—' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">审核时间：</span>
            <span>{{ detailData.auditTime || '—' }}</span>
          </div>
          <div v-if="detailData.auditOpinion" class="info-item">
            <span class="info-label">审核意见：</span>
            <span class="opinion-text">{{ detailData.auditOpinion }}</span>
          </div>
        </div>

        <div class="compare-section">
          <div class="compare-column">
            <div class="column-header original">修改前</div>
            <div class="compare-content">
              <div v-if="oldData" class="compare-list">
                <div class="compare-item">
                  <span class="compare-label">党员身份：</span>
                  <span>{{ oldData.isPartyMember || '—' }}</span>
                </div>
                <div class="compare-item">
                  <span class="compare-label">党员类型：</span>
                  <span>{{ oldData.partyType || '—' }}</span>
                </div>
                <div class="compare-item">
                  <span class="compare-label">入党时间：</span>
                  <span>{{ oldData.joinPartyDate || '—' }}</span>
                </div>
                <div class="compare-item">
                  <span class="compare-label">所属党支部：</span>
                  <span>{{ oldData.partyBranch || '—' }}</span>
                </div>
                <div class="compare-item">
                  <span class="compare-label">现任职务：</span>
                  <span>{{ oldData.duty || '—' }}</span>
                </div>
                <div class="compare-item">
                  <span class="compare-label">分管业务：</span>
                  <span>{{ oldData.manageBusiness || '—' }}</span>
                </div>
                <div class="compare-item">
                  <span class="compare-label">任职时间：</span>
                  <span>{{ oldData.startWorkDate || '—' }}</span>
                </div>
                <div class="compare-item">
                  <span class="compare-label">负责网格：</span>
                  <span>{{ oldData.gridArea || '—' }}</span>
                </div>
              </div>
            </div>
          </div>

          <div class="compare-arrow">→</div>

          <div class="compare-column">
            <div class="column-header new">修改后</div>
            <div class="compare-content">
              <div v-if="newData" class="compare-list">
                <div class="compare-item" :class="{ changed: isChanged('isPartyMember') }">
                  <span class="compare-label">党员身份：</span>
                  <span>{{ newData.isPartyMember || '—' }}</span>
                </div>
                <div class="compare-item" :class="{ changed: isChanged('partyType') }">
                  <span class="compare-label">党员类型：</span>
                  <span>{{ newData.partyType || '—' }}</span>
                </div>
                <div class="compare-item" :class="{ changed: isChanged('joinPartyDate') }">
                  <span class="compare-label">入党时间：</span>
                  <span>{{ newData.joinPartyDate || '—' }}</span>
                </div>
                <div class="compare-item" :class="{ changed: isChanged('partyBranch') }">
                  <span class="compare-label">所属党支部：</span>
                  <span>{{ newData.partyBranch || '—' }}</span>
                </div>
                <div class="compare-item" :class="{ changed: isChanged('duty') }">
                  <span class="compare-label">现任职务：</span>
                  <span>{{ newData.duty || '—' }}</span>
                </div>
                <div class="compare-item" :class="{ changed: isChanged('manageBusiness') }">
                  <span class="compare-label">分管业务：</span>
                  <span>{{ newData.manageBusiness || '—' }}</span>
                </div>
                <div class="compare-item" :class="{ changed: isChanged('startWorkDate') }">
                  <span class="compare-label">任职时间：</span>
                  <span>{{ newData.startWorkDate || '—' }}</span>
                </div>
                <div class="compare-item" :class="{ changed: isChanged('gridArea') }">
                  <span class="compare-label">负责网格：</span>
                  <span>{{ newData.gridArea || '—' }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="showDetailDialog = false">关闭</el-button>
        <el-button v-if="detailData.auditStatus === 0" type="danger" @click="showRejectDialog(detailData)">驳回</el-button>
        <el-button v-if="detailData.auditStatus === 0" type="primary" @click="auditPass(detailData)">审核通过</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showRejectDialogVisible" title="驳回审核" width="400px">
      <el-form :model="rejectForm">
        <el-form-item label="驳回意见" label-width="70px">
          <el-input type="textarea" v-model="rejectForm.opinion" rows="4" placeholder="请输入驳回原因，如：入党时间材料不全、职务与村两委备案不符" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="doReject">确认驳回</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showBatchRejectDialog" title="批量驳回" width="400px">
      <el-form :model="batchRejectForm">
        <el-form-item label="驳回意见" label-width="70px">
          <el-input type="textarea" v-model="batchRejectForm.opinion" rows="4" placeholder="请输入统一驳回原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showBatchRejectDialog = false">取消</el-button>
        <el-button type="danger" @click="doBatchReject">确认批量驳回</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const auditList = ref([])
const searchName = ref('')
const auditStatus = ref(null)
const changeType = ref(null)
const dateRange = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedIds = ref([])

const showDetailDialog = ref(false)
const showRejectDialogVisible = ref(false)
const showBatchRejectDialog = ref(false)

const detailData = reactive({})
const oldData = ref({})
const newData = ref({})

const rejectForm = reactive({
  opinion: ''
})

const batchRejectForm = reactive({
  opinion: ''
})

const currentRejectId = ref(null)

const getChangeTypeLabel = (type) => {
  const map = {
    'party': '党务变更',
    'duty': '职务变更',
    'all': '党务/职务变更'
  }
  return map[type] || '信息变更'
}

const getChangeTypeTagType = (type) => {
  if (type === 'party') return 'warning'
  if (type === 'duty') return 'primary'
  if (type === 'all') return 'danger'
  return 'default'
}

const getStatusLabel = (status) => {
  if (status === 0) return '待审核'
  if (status === 1) return '已通过'
  if (status === 2) return '已驳回'
  return '未知'
}

const getStatusTagType = (status) => {
  if (status === 0) return 'warning'
  if (status === 1) return 'success'
  if (status === 2) return 'danger'
  return 'default'
}

const getStatusClass = (status) => {
  if (status === 0) return 'pending'
  if (status === 1) return 'passed'
  if (status === 2) return 'rejected'
  return ''
}

const isChanged = (key) => {
  return oldData.value[key] !== newData.value[key]
}

const fetchAuditList = async () => {
  try {
    const params = {
      current: currentPage.value,
      size: pageSize.value
    }
    if (searchName.value) params.name = searchName.value
    if (auditStatus.value !== null) params.auditStatus = auditStatus.value
    if (changeType.value) params.changeType = changeType.value
    
    const res = await request.get('/audit/list', { params })
    auditList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('获取审核列表失败', error)
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchAuditList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchAuditList()
}

const handleSelectionChange = (val) => {
  selectedIds.value = val.map(item => item.auditId)
}

const viewDetail = async (row) => {
  try {
    const res = await request.get(`/audit/detail/${row.auditId}`)
    Object.assign(detailData, res.data)
    oldData.value = JSON.parse(res.data.oldData)
    newData.value = JSON.parse(res.data.newData)
    showDetailDialog.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const auditPass = async (row) => {
  try {
    await request.post('/audit/pass', {
      auditId: row.auditId,
      adminName: '管理员'
    })
    ElMessage.success('审核通过')
    showDetailDialog.value = false
    fetchAuditList()
  } catch (error) {
    ElMessage.error('审核失败')
  }
}

const showRejectDialog = (row) => {
  currentRejectId.value = row.auditId
  rejectForm.opinion = ''
  showRejectDialogVisible.value = true
}

const doReject = async () => {
  if (!rejectForm.opinion.trim()) {
    ElMessage.warning('请输入驳回意见')
    return
  }
  try {
    await request.post('/audit/reject', {
      auditId: currentRejectId.value,
      adminName: '管理员',
      opinion: rejectForm.opinion
    })
    ElMessage.success('已驳回')
    showRejectDialogVisible.value = false
    showDetailDialog.value = false
    fetchAuditList()
  } catch (error) {
    ElMessage.error('驳回失败')
  }
}

const batchPass = async () => {
  try {
    await request.post('/audit/batch-pass', {
      auditIds: selectedIds.value,
      adminName: '管理员'
    })
    ElMessage.success('批量审核通过')
    selectedIds.value = []
    fetchAuditList()
  } catch (error) {
    ElMessage.error('批量审核失败')
  }
}

const doBatchReject = async () => {
  if (!batchRejectForm.opinion.trim()) {
    ElMessage.warning('请输入驳回意见')
    return
  }
  try {
    await request.post('/audit/batch-reject', {
      auditIds: selectedIds.value,
      adminName: '管理员',
      opinion: batchRejectForm.opinion
    })
    ElMessage.success('批量驳回完成')
    showBatchRejectDialog.value = false
    selectedIds.value = []
    fetchAuditList()
  } catch (error) {
    ElMessage.error('批量驳回失败')
  }
}

onMounted(() => {
  fetchAuditList()
})
</script>

<style scoped>
.audit-management {
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
  color: #C8102E;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.detail-content {
  padding: 10px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.detail-type {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.detail-status {
  font-size: 14px;
  padding: 4px 12px;
  border-radius: 4px;
  font-weight: bold;
}

.detail-status.pending {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.detail-status.passed {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.detail-status.rejected {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.detail-info {
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 8px;
}

.info-label {
  font-weight: bold;
  color: #666;
}

.opinion-text {
  color: #C8102E;
}

.compare-section {
  display: flex;
  gap: 20px;
}

.compare-column {
  flex: 1;
}

.column-header {
  font-size: 16px;
  font-weight: bold;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
}

.column-header.original {
  background: #F5F5F5;
  color: #666;
}

.column-header.new {
  background: #FFF5F5;
  color: #C8102E;
}

.compare-arrow {
  display: flex;
  align-items: center;
  font-size: 24px;
  color: #ccc;
}

.compare-content {
  background: #FAFAFA;
  border-radius: 4px;
  padding: 10px;
}

.compare-list {
  display: flex;
  flex-direction: column;
}

.compare-item {
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.compare-item:last-child {
  border-bottom: none;
}

.compare-item.changed {
  background: #FFF5F5;
  margin: 0 -10px;
  padding-left: 10px;
}

.compare-label {
  color: #666;
  margin-right: 8px;
}

.compare-item.changed span:last-child {
  color: #C8102E;
  font-weight: bold;
}
</style>
