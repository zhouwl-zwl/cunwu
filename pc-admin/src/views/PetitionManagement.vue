<template>
  <div class="management-page">
    <div class="page-header">
      <h2>信访矛盾调解</h2>
      <div class="header-actions">
        <el-button @click="handleExport">导出</el-button>
        <el-button type="primary" @click="handleAddDispute">新增纠纷</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.totalCount }}</div>
        <div class="stat-label">本月纠纷总数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.resolveRate }}%</div>
        <div class="stat-label">化解率</div>
      </div>
      <div class="stat-card red-light">
        <div class="stat-value">{{ stats.majorCount }}</div>
        <div class="stat-label">重大纠纷数</div>
      </div>
      <div class="stat-card gold-light">
        <div class="stat-value">{{ stats.petitionCount }}</div>
        <div class="stat-label">信访件数</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="矛盾纠纷台账" name="dispute">
        <div class="search-bar">
          <el-input v-model="disputeSearch.party" placeholder="按当事人搜索" style="width: 200px" clearable @keyup.enter="fetchDispute" />
          <el-select v-model="disputeSearch.type" placeholder="纠纷类型" style="width: 120px" clearable>
            <el-option label="邻里" value="邻里" />
            <el-option label="土地" value="土地" />
            <el-option label="婚姻" value="婚姻" />
            <el-option label="劳资" value="劳资" />
            <el-option label="其他" value="其他" />
          </el-select>
          <el-select v-model="disputeSearch.level" placeholder="矛盾等级" style="width: 120px" clearable>
            <el-option label="一般" value="一般" />
            <el-option label="较大" value="较大" />
            <el-option label="重大" value="重大" />
          </el-select>
          <el-select v-model="disputeSearch.status" placeholder="状态" style="width: 120px" clearable>
            <el-option label="待调解" value="待调解" />
            <el-option label="调解中" value="调解中" />
            <el-option label="已化解" value="已化解" />
            <el-option label="未化解" value="未化解" />
          </el-select>
          <el-button type="primary" @click="fetchDispute">查询</el-button>
          <el-button @click="resetDisputeSearch">重置</el-button>
        </div>

        <el-table :data="disputeList" border :row-class-name="disputeRowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="type" label="纠纷类型" width="90" />
          <el-table-column prop="party" label="当事人" width="120" />
          <el-table-column prop="reason" label="事发事由" min-width="200" show-overflow-tooltip />
          <el-table-column prop="level" label="矛盾等级" width="90">
            <template #default="scope">
              <el-tag :type="getLevelTagType(scope.row.level)">
                {{ scope.row.level }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="registerTime" label="登记时间" width="160" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="260" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleDisputeView(scope.row)">查看详情</el-button>
              <el-button v-if="scope.row.status === '待调解'" size="small" type="primary" @click="handleAssignMediator(scope.row)">分配调解员</el-button>
              <el-button size="small" @click="handleMediationRecord(scope.row)">调解记录</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="disputePagination.page"
          v-model:page-size="disputePagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="disputePagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchDispute"
          @current-change="fetchDispute"
        />
      </el-tab-pane>

      <el-tab-pane label="调解记录" name="mediation">
        <div class="search-bar">
          <el-input v-model="mediationSearch.party" placeholder="按当事人搜索" style="width: 200px" clearable @keyup.enter="fetchMediation" />
          <el-button type="primary" @click="fetchMediation">查询</el-button>
        </div>

        <el-table :data="mediationList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="disputeType" label="纠纷类型" width="100" />
          <el-table-column prop="party" label="当事人" width="120" />
          <el-table-column prop="mediator" label="调解员" width="100" />
          <el-table-column prop="mediationTimes" label="调解次数" width="90" />
          <el-table-column prop="lastMediationTime" label="最近调解时间" width="160" />
          <el-table-column prop="result" label="调解结果" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.result === '成功' ? 'success' : 'danger'">
                {{ scope.row.result }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewMediation(scope.row)">查看详情</el-button>
              <el-button size="small" type="primary" @click="handleAddMediation(scope.row)">新增调解</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="mediationPagination.page"
          v-model:page-size="mediationPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="mediationPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchMediation"
          @current-change="fetchMediation"
        />
      </el-tab-pane>

      <el-tab-pane label="信访件办理" name="petition">
        <div class="search-bar">
          <el-input v-model="petitionSearch.petitioner" placeholder="按信访人搜索" style="width: 200px" clearable @keyup.enter="fetchPetition" />
          <el-select v-model="petitionSearch.type" placeholder="信访类型" style="width: 140px" clearable>
            <el-option label="上级转办" value="上级转办" />
            <el-option label="村民来访" value="村民来访" />
            <el-option label="网上信访" value="网上信访" />
          </el-select>
          <el-select v-model="petitionSearch.status" placeholder="办理状态" style="width: 120px" clearable>
            <el-option label="待办理" value="待办理" />
            <el-option label="办理中" value="办理中" />
            <el-option label="已办结" value="已办结" />
            <el-option label="超期" value="超期" />
          </el-select>
          <el-button type="primary" @click="fetchPetition">查询</el-button>
          <el-button type="primary" @click="handlePetitionAdd">新增信访</el-button>
        </div>

        <el-table :data="petitionList" border :row-class-name="petitionRowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="type" label="信访类型" width="110" />
          <el-table-column prop="petitioner" label="信访人" width="100" />
          <el-table-column prop="reason" label="事由" min-width="200" show-overflow-tooltip />
          <el-table-column prop="registerTime" label="登记时间" width="160" />
          <el-table-column prop="deadline" label="办结时限" width="120" />
          <el-table-column prop="status" label="办理状态" width="100">
            <template #default="scope">
              <el-tag :type="getPetitionStatusTagType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handlePetitionView(scope.row)">查看详情</el-button>
              <el-button v-if="scope.row.status !== '已办结'" size="small" type="primary" @click="handlePetitionReply(scope.row)">办理回复</el-button>
              <el-button size="small" @click="handlePetitionUpload(scope.row)">上传材料</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="petitionPagination.page"
          v-model:page-size="petitionPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="petitionPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchPetition"
          @current-change="fetchPetition"
        />
      </el-tab-pane>

      <el-tab-pane label="未化解跟踪" name="unresolved">
        <div class="search-bar">
          <el-input v-model="unresolvedSearch.party" placeholder="按当事人搜索" style="width: 200px" clearable @keyup.enter="fetchUnresolved" />
          <el-button type="primary" @click="fetchUnresolved">查询</el-button>
        </div>

        <el-table :data="unresolvedList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="type" label="纠纷类型" width="100" />
          <el-table-column prop="party" label="当事人" width="120" />
          <el-table-column prop="reason" label="事由" min-width="180" show-overflow-tooltip />
          <el-table-column prop="level" label="等级" width="80">
            <template #default="scope">
              <el-tag :type="getLevelTagType(scope.row.level)">
                {{ scope.row.level }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="trackCount" label="跟踪次数" width="90" />
          <el-table-column prop="lastTrackTime" label="最近跟踪时间" width="160" />
          <el-table-column prop="responsiblePerson" label="跟踪负责人" width="100" />
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleTrackRecord(scope.row)">跟踪记录</el-button>
              <el-button size="small" type="primary" @click="handleAddTrack(scope.row)">新增跟踪</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="unresolvedPagination.page"
          v-model:page-size="unresolvedPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="unresolvedPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchUnresolved"
          @current-change="fetchUnresolved"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="disputeDetailVisible" title="纠纷详情" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="纠纷类型">{{ currentDispute.type }}</el-descriptions-item>
        <el-descriptions-item label="当事人">{{ currentDispute.party }}</el-descriptions-item>
        <el-descriptions-item label="矛盾等级">
          <el-tag :type="getLevelTagType(currentDispute.level)">{{ currentDispute.level }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="登记时间">{{ currentDispute.registerTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(currentDispute.status)">{{ currentDispute.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="调解员">{{ currentDispute.mediator || '-' }}</el-descriptions-item>
        <el-descriptions-item label="事发事由" :span="2">{{ currentDispute.reason }}</el-descriptions-item>
      </el-descriptions>

      <div class="flow-section">
        <div class="section-title">调解流程</div>
        <el-steps :active="getMediationStep(currentDispute.status)" finish-status="success" direction="vertical">
          <el-step title="纠纷登记">
            <template #description>
              <p>登记时间：{{ currentDispute.registerTime }}</p>
              <p>登记人：{{ currentDispute.registrar || '村文书' }}</p>
            </template>
          </el-step>
          <el-step title="分配调解员">
            <template #description>
              <p v-if="currentDispute.mediator">调解员：{{ currentDispute.mediator }}</p>
              <p v-else>待分配</p>
            </template>
          </el-step>
          <el-step title="第一次调解">
            <template #description>
              <p v-if="currentDispute.firstMediationTime">时间：{{ currentDispute.firstMediationTime }}</p>
              <p v-if="currentDispute.firstMediationRecord">笔录：{{ currentDispute.firstMediationRecord }}</p>
              <p v-else>暂未调解</p>
            </template>
          </el-step>
          <el-step title="第二次调解">
            <template #description>
              <p v-if="currentDispute.secondMediationTime">时间：{{ currentDispute.secondMediationTime }}</p>
              <p v-if="currentDispute.secondMediationRecord">笔录：{{ currentDispute.secondMediationRecord }}</p>
              <p v-else>暂未调解</p>
            </template>
          </el-step>
          <el-step :title="currentDispute.status === '已化解' ? '调解成功' : '未化解'">
            <template #description>
              <p v-if="currentDispute.result">结果：{{ currentDispute.result }}</p>
              <p v-else>处理中</p>
            </template>
          </el-step>
        </el-steps>
      </div>

      <div class="upload-section">
        <div class="section-title">调解材料</div>
        <el-upload action="#" list-type="picture-card" :auto-upload="false" multiple>
          <el-icon><Plus /></el-icon>
          <template #tip>
            <div class="el-upload__tip">调解笔录照片、调解协议书等</div>
          </template>
        </el-upload>
      </div>
    </el-dialog>

    <el-dialog v-model="assignDialogVisible" title="分配调解员" width="500px">
      <el-form :model="assignForm" label-width="100px">
        <el-form-item label="调解员">
          <el-select v-model="assignForm.mediator" placeholder="请选择调解员" style="width: 100%">
            <el-option label="张主任" value="张主任" />
            <el-option label="李副主任" value="李副主任" />
            <el-option label="王委员" value="王委员" />
            <el-option label="赵调解员" value="赵调解员" />
          </el-select>
        </el-form-item>
        <el-form-item label="调解期限">
          <el-date-picker
            v-model="assignForm.deadline"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择调解期限"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignSave">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="petitionReplyVisible" title="办理回复" width="600px">
      <el-form :model="replyForm" label-width="100px">
        <el-form-item label="办理进度">
          <el-select v-model="replyForm.progress" style="width: 100%">
            <el-option label="办理中" value="办理中" />
            <el-option label="已办结" value="已办结" />
          </el-select>
        </el-form-item>
        <el-form-item label="回复内容">
          <el-input v-model="replyForm.content" type="textarea" :rows="6" placeholder="请输入办理回复内容" />
        </el-form-item>
        <el-form-item label="附件材料">
          <el-upload action="#" list-type="text" :auto-upload="false" multiple>
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="petitionReplyVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReplySave">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="trackDialogVisible" title="新增跟踪记录" width="500px">
      <el-form :model="trackForm" label-width="100px">
        <el-form-item label="跟踪时间">
          <el-date-picker
            v-model="trackForm.trackTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择跟踪时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="跟踪方式">
          <el-select v-model="trackForm.trackType" style="width: 100%">
            <el-option label="电话回访" value="电话回访" />
            <el-option label="上门走访" value="上门走访" />
            <el-option label="村委会调解" value="村委会调解" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟踪情况">
          <el-input v-model="trackForm.content" type="textarea" :rows="4" placeholder="请输入跟踪情况" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="trackDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTrackSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../utils/request'

const activeTab = ref('dispute')

const stats = reactive({
  totalCount: 0,
  resolveRate: 0,
  majorCount: 0,
  petitionCount: 0
})

const fetchStats = () => {
  stats.totalCount = 36
  stats.resolveRate = 72.2
  stats.majorCount = 3
  stats.petitionCount = 12
}

/* ============ 矛盾纠纷台账 ============ */
const disputeList = ref([])
const disputeSearch = reactive({ party: '', type: '', level: '', status: '' })
const disputePagination = reactive({ page: 1, size: 10, total: 0 })

const mockDisputeList = [
  { id: 1, type: '土地', party: '张某 vs 李某', reason: '宅基地边界争议，双方各执一词，互不相让', level: '较大', registerTime: '2026-07-02 09:30:00', status: '调解中', mediator: '张主任', registrar: '村文书', firstMediationTime: '2026-07-05 14:00:00', firstMediationRecord: '第一次调解，双方初步表达诉求' },
  { id: 2, type: '邻里', party: '王某 vs 赵某', reason: '因噪音问题引发口角，情绪激动', level: '一般', registerTime: '2026-07-05 10:15:00', status: '待调解', mediator: '', registrar: '村文书' },
  { id: 3, type: '婚姻', party: '刘某夫妇', reason: '家庭矛盾，婚姻破裂，涉及财产分割', level: '重大', registerTime: '2026-06-28 11:00:00', status: '未化解', mediator: '李副主任', registrar: '村文书', firstMediationTime: '2026-07-01 09:00:00', firstMediationRecord: '第一次调解，双方分歧较大', secondMediationTime: '2026-07-08 14:30:00', secondMediationRecord: '第二次调解，仍未达成一致', result: '未达成协议' },
  { id: 4, type: '劳资', party: '陈某', reason: '务工工资拖欠问题', level: '一般', registerTime: '2026-07-10 08:45:00', status: '已化解', mediator: '王委员', registrar: '村文书', firstMediationTime: '2026-07-12 10:00:00', firstMediationRecord: '调解成功，工资已结清', result: '调解成功' },
  { id: 5, type: '其他', party: '周某', reason: '村务公开问题质疑', level: '一般', registerTime: '2026-07-12 15:20:00', status: '调解中', mediator: '张主任', registrar: '村文书', firstMediationTime: '2026-07-13 09:00:00', firstMediationRecord: '已解释相关政策，待进一步沟通' }
]

const fetchDispute = async () => {
  try {
    const res = await request.get('/api/petition/list', {
      params: {
        petitionType: 'dispute',
        party: disputeSearch.party,
        type: disputeSearch.type,
        level: disputeSearch.level,
        status: disputeSearch.status,
        page: disputePagination.page,
        size: disputePagination.size
      }
    })
    disputeList.value = res.data.records || []
    disputePagination.total = res.data.total || 0
  } catch (error) {
    disputeList.value = mockDisputeList
    disputePagination.total = mockDisputeList.length
  }
}

const resetDisputeSearch = () => {
  disputeSearch.party = ''
  disputeSearch.type = ''
  disputeSearch.level = ''
  disputeSearch.status = ''
  disputePagination.page = 1
  fetchDispute()
}

const getLevelTagType = (level) => {
  const map = { '一般': 'success', '较大': 'warning', '重大': 'danger' }
  return map[level] || 'info'
}

const getStatusTagType = (status) => {
  const map = { '待调解': 'warning', '调解中': 'primary', '已化解': 'success', '未化解': 'danger' }
  return map[status] || 'info'
}

const disputeRowClassName = ({ row }) => {
  if (row.level === '重大') {
    return 'warning-row'
  }
  return ''
}

const disputeDetailVisible = ref(false)
const currentDispute = reactive({})

const handleDisputeView = (row) => {
  Object.assign(currentDispute, row)
  disputeDetailVisible.value = true
}

const getMediationStep = (status) => {
  const map = { '待调解': 1, '调解中': 3, '已化解': 5, '未化解': 5 }
  return map[status] || 0
}

const assignDialogVisible = ref(false)
const assignForm = reactive({ id: null, mediator: '', deadline: '' })

const handleAssignMediator = (row) => {
  assignForm.id = row.id
  assignForm.mediator = ''
  assignForm.deadline = ''
  assignDialogVisible.value = true
}

const handleAssignSave = () => {
  if (!assignForm.mediator) {
    ElMessage.warning('请选择调解员')
    return
  }
  ElMessage.success('分配成功')
  assignDialogVisible.value = false
  fetchDispute()
}

const handleMediationRecord = (row) => {
  Object.assign(currentDispute, row)
  disputeDetailVisible.value = true
}

const handleAddDispute = () => {
  ElMessage.info('新增纠纷功能')
}

/* ============ 调解记录 ============ */
const mediationList = ref([])
const mediationSearch = reactive({ party: '' })
const mediationPagination = reactive({ page: 1, size: 10, total: 0 })

const mockMediationList = [
  { id: 1, disputeType: '土地', party: '张某 vs 李某', mediator: '张主任', mediationTimes: 2, lastMediationTime: '2026-07-12 14:00:00', result: '调解中' },
  { id: 2, disputeType: '婚姻', party: '刘某夫妇', mediator: '李副主任', mediationTimes: 3, lastMediationTime: '2026-07-10 09:30:00', result: '未成功' },
  { id: 3, disputeType: '劳资', party: '陈某', mediator: '王委员', mediationTimes: 1, lastMediationTime: '2026-07-12 10:00:00', result: '成功' },
  { id: 4, disputeType: '邻里', party: '周某 vs 吴某', mediator: '赵调解员', mediationTimes: 2, lastMediationTime: '2026-07-08 15:00:00', result: '成功' }
]

const fetchMediation = () => {
  mediationList.value = mockMediationList
  mediationPagination.total = mockMediationList.length
}

const handleViewMediation = (row) => {
  ElMessage.info(`查看 ${row.party} 的调解详情`)
}

const handleAddMediation = (row) => {
  ElMessage.info(`为 ${row.party} 新增调解记录`)
}

/* ============ 信访件办理 ============ */
const petitionList = ref([])
const petitionSearch = reactive({ petitioner: '', type: '', status: '' })
const petitionPagination = reactive({ page: 1, size: 10, total: 0 })

const mockPetitionList = [
  { id: 1, type: '上级转办', petitioner: '孙某某', reason: '反映村道路硬化工程质量问题', registerTime: '2026-07-01 10:00:00', deadline: '2026-07-15', status: '办理中' },
  { id: 2, type: '村民来访', petitioner: '钱某某', reason: '申请低保待遇', registerTime: '2026-07-05 09:30:00', deadline: '2026-07-20', status: '待办理' },
  { id: 3, type: '网上信访', petitioner: '郑某某', reason: '反映饮用水水质问题', registerTime: '2026-06-25 14:20:00', deadline: '2026-07-10', status: '超期' },
  { id: 4, type: '上级转办', petitioner: '冯某某', reason: '举报村干部违规行为', registerTime: '2026-07-08 11:00:00', deadline: '2026-07-22', status: '办理中' },
  { id: 5, type: '村民来访', petitioner: '陈某某', reason: '咨询养老保险政策', registerTime: '2026-07-10 08:45:00', deadline: '2026-07-17', status: '已办结' }
]

const fetchPetition = () => {
  petitionList.value = mockPetitionList
  petitionPagination.total = mockPetitionList.length
  stats.petitionCount = mockPetitionList.length
}

const getPetitionStatusTagType = (status) => {
  const map = { '待办理': 'warning', '办理中': 'primary', '已办结': 'success', '超期': 'danger' }
  return map[status] || 'info'
}

const petitionRowClassName = ({ row }) => {
  if (row.status === '超期') {
    return 'warning-row'
  }
  return ''
}

const petitionReplyVisible = ref(false)
const replyForm = reactive({ id: null, progress: '办理中', content: '' })

const handlePetitionView = (row) => {
  ElMessage.info(`查看信访件 ${row.petitioner} 的详情`)
}

const handlePetitionReply = (row) => {
  replyForm.id = row.id
  replyForm.progress = row.status === '已办结' ? '已办结' : '办理中'
  replyForm.content = ''
  petitionReplyVisible.value = true
}

const handlePetitionUpload = (row) => {
  ElMessage.info(`为 ${row.petitioner} 上传材料`)
}

const handleReplySave = () => {
  if (!replyForm.content) {
    ElMessage.warning('请输入回复内容')
    return
  }
  ElMessage.success('提交成功')
  petitionReplyVisible.value = false
  fetchPetition()
}

const handlePetitionAdd = () => {
  ElMessage.info('新增信访功能')
}

/* ============ 未化解跟踪 ============ */
const unresolvedList = ref([])
const unresolvedSearch = reactive({ party: '' })
const unresolvedPagination = reactive({ page: 1, size: 10, total: 0 })

const mockUnresolvedList = [
  { id: 1, type: '婚姻', party: '刘某夫妇', reason: '家庭矛盾，婚姻破裂，涉及财产分割', level: '重大', trackCount: 5, lastTrackTime: '2026-07-14 09:00:00', responsiblePerson: '李副主任' },
  { id: 2, type: '土地', party: '张某 vs 李某', reason: '宅基地边界争议', level: '较大', trackCount: 3, lastTrackTime: '2026-07-13 14:30:00', responsiblePerson: '张主任' },
  { id: 3, type: '邻里', party: '王某 vs 赵某', reason: '建房采光问题', level: '一般', trackCount: 2, lastTrackTime: '2026-07-12 10:00:00', responsiblePerson: '王委员' }
]

const fetchUnresolved = () => {
  unresolvedList.value = mockUnresolvedList
  unresolvedPagination.total = mockUnresolvedList.length
}

const trackDialogVisible = ref(false)
const trackForm = reactive({ id: null, trackTime: '', trackType: '电话回访', content: '' })

const handleTrackRecord = (row) => {
  ElMessage.info(`查看 ${row.party} 的跟踪记录`)
}

const handleAddTrack = (row) => {
  trackForm.id = row.id
  trackForm.trackTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
  trackForm.trackType = '电话回访'
  trackForm.content = ''
  trackDialogVisible.value = true
}

const handleTrackSave = () => {
  if (!trackForm.content) {
    ElMessage.warning('请输入跟踪情况')
    return
  }
  ElMessage.success('保存成功')
  trackDialogVisible.value = false
  fetchUnresolved()
}

const handleExport = () => {
  ElMessage.info('导出功能')
}

const handleTabChange = (tab) => {
  if (tab === 'dispute') fetchDispute()
  else if (tab === 'mediation') fetchMediation()
  else if (tab === 'petition') fetchPetition()
  else if (tab === 'unresolved') fetchUnresolved()
}

onMounted(() => {
  fetchStats()
  fetchDispute()
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

.flow-section {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px dashed #eee;
}

.section-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.upload-section {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px dashed #eee;
}
</style>
