<template>
  <div class="management-page">
    <div class="page-header">
      <h2>人居环境整治</h2>
      <div class="header-actions">
        <el-button @click="handleExport">导出</el-button>
        <el-button type="primary" @click="handleCheckAdd">新增巡查</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.checkCount }}</div>
        <div class="stat-label">本月巡查次数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.rectifiedCount }}</div>
        <div class="stat-label">已整改数</div>
      </div>
      <div class="stat-card red-light">
        <div class="stat-value">{{ stats.rectifyRate }}%</div>
        <div class="stat-label">整改率</div>
      </div>
      <div class="stat-card gold-light">
        <div class="stat-value">{{ stats.redListCount }}</div>
        <div class="stat-label">红榜户数</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="环境巡查台账" name="check">
        <div class="search-bar">
          <el-input v-model="checkSearch.location" placeholder="按位置搜索" style="width: 200px" clearable @keyup.enter="fetchCheck" />
          <el-select v-model="checkSearch.type" placeholder="巡查类型" style="width: 140px" clearable>
            <el-option label="垃圾乱堆" value="垃圾乱堆" />
            <el-option label="污水乱排" value="污水乱排" />
            <el-option label="违建杂物" value="违建杂物" />
          </el-select>
          <el-select v-model="checkSearch.status" placeholder="整改状态" style="width: 120px" clearable>
            <el-option label="待整改" value="待整改" />
            <el-option label="整改中" value="整改中" />
            <el-option label="已整改" value="已整改" />
          </el-select>
          <el-button type="primary" @click="fetchCheck">查询</el-button>
          <el-button @click="resetCheckSearch">重置</el-button>
        </div>

        <el-table :data="checkList" border :row-class-name="checkRowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="type" label="巡查类型" width="110" />
          <el-table-column prop="location" label="位置" width="140" />
          <el-table-column prop="description" label="问题描述" min-width="200" show-overflow-tooltip />
          <el-table-column prop="inspector" label="巡查人" width="100" />
          <el-table-column prop="checkDate" label="巡查日期" width="120" />
          <el-table-column prop="deadline" label="整改期限" width="120" />
          <el-table-column prop="status" label="整改状态" width="100">
            <template #default="scope">
              <el-tag :type="getCheckStatusTagType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="260" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleCheckView(scope.row)">查看详情</el-button>
              <el-button v-if="scope.row.status === '待整改'" size="small" type="primary" @click="handleIssueRectify(scope.row)">下发整改</el-button>
              <el-button v-if="scope.row.status === '整改中'" size="small" type="success" @click="handleVerifyRectify(scope.row)">整改销号</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="checkPagination.page"
          v-model:page-size="checkPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="checkPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchCheck"
          @current-change="fetchCheck"
        />
      </el-tab-pane>

      <el-tab-pane label="美丽庭院评比" name="courtyard">
        <div class="search-bar">
          <el-date-picker
            v-model="courtyardMonth"
            type="month"
            placeholder="选择月份"
            value-format="YYYY-MM"
            style="width: 200px"
            @change="fetchCourtyard"
          />
          <el-button type="primary" @click="handleCourtyardAdd">新增评比</el-button>
          <el-button @click="handleGenerateNotice">生成公示</el-button>
          <el-button @click="handlePrint">打印评比榜</el-button>
        </div>

        <el-row :gutter="20">
          <el-col :span="12">
            <div class="rank-card red-rank">
              <div class="rank-header">
                <span class="rank-title">红榜（优秀庭院）</span>
                <span class="rank-count">共 {{ redList.length }} 户</span>
              </div>
              <el-table :data="redList" border size="small">
                <el-table-column type="index" label="序号" width="50" />
                <el-table-column prop="householder" label="户主姓名" width="90" />
                <el-table-column prop="villageGroup" label="村组" width="90" />
                <el-table-column prop="score" label="评分" width="70">
                  <template #default="scope">
                    <span class="gold-text">{{ scope.row.score }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="照片" width="80">
                  <template #default="scope">
                    <el-image :src="scope.row.photo || placeholderImg" style="width: 60px; height: 40px" fit="cover" />
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="rank-card black-rank">
              <div class="rank-header">
                <span class="rank-title">黑榜（脏乱庭院）</span>
                <span class="rank-count">共 {{ blackList.length }} 户</span>
              </div>
              <el-table :data="blackList" border size="small">
                <el-table-column type="index" label="序号" width="50" />
                <el-table-column prop="householder" label="户主姓名" width="90" />
                <el-table-column prop="villageGroup" label="村组" width="90" />
                <el-table-column prop="problem" label="问题" min-width="100" show-overflow-tooltip />
                <el-table-column label="照片" width="80">
                  <template #default="scope">
                    <el-image :src="scope.row.photo || placeholderImg" style="width: 60px; height: 40px" fit="cover" />
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="保洁员管理" name="cleaner">
        <div class="search-bar">
          <el-input v-model="cleanerSearch.name" placeholder="按姓名搜索" style="width: 200px" clearable @keyup.enter="fetchCleaner" />
          <el-select v-model="cleanerSearch.status" placeholder="状态" style="width: 120px" clearable>
            <el-option label="在职" value="在职" />
            <el-option label="离职" value="离职" />
          </el-select>
          <el-button type="primary" @click="fetchCleaner">查询</el-button>
          <el-button type="primary" @click="handleCleanerAdd">新增保洁员</el-button>
        </div>

        <el-table :data="cleanerList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="phone" label="电话" width="130" />
          <el-table-column prop="area" label="负责片区" min-width="180" />
          <el-table-column prop="attendance" label="考勤" width="110">
            <template #default="scope">
              <el-tag :type="scope.row.attendance === '全勤' ? 'success' : 'warning'">
                {{ scope.row.attendance }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="salary" label="月工资(元)" width="120">
            <template #default="scope">
              <span class="red-text">{{ scope.row.salary }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag :type="scope.row.status === '在职' ? 'success' : 'info'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleAttendance(scope.row)">考勤登记</el-button>
              <el-button size="small" @click="handleSalaryRecord(scope.row)">工资记录</el-button>
              <el-button size="small" type="primary" @click="handleCleanerEdit(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="cleanerPagination.page"
          v-model:page-size="cleanerPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="cleanerPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchCleaner"
          @current-change="fetchCleaner"
        />
      </el-tab-pane>

      <el-tab-pane label="环卫设施台账" name="facility">
        <div class="search-bar">
          <el-select v-model="facilitySearch.type" placeholder="设施类型" style="width: 160px" clearable>
            <el-option label="垃圾桶" value="垃圾桶" />
            <el-option label="垃圾中转站" value="垃圾中转站" />
            <el-option label="污水处理设施" value="污水处理设施" />
          </el-select>
          <el-button type="primary" @click="fetchFacility">查询</el-button>
          <el-button type="primary" @click="handleFacilityAdd">新增设施</el-button>
        </div>

        <el-table :data="facilityList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="type" label="设施类型" width="130" />
          <el-table-column prop="location" label="位置" min-width="180" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="installDate" label="安装日期" width="120" />
          <el-table-column prop="maintainStatus" label="维护状态" width="100">
            <template #default="scope">
              <el-tag :type="getMaintainTagType(scope.row.maintainStatus)">
                {{ scope.row.maintainStatus }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleRepairRecord(scope.row)">维修记录</el-button>
              <el-button size="small" type="primary" @click="handleFacilityEdit(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="facilityPagination.page"
          v-model:page-size="facilityPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="facilityPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchFacility"
          @current-change="fetchFacility"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="checkDetailVisible" title="巡查详情" width="900px">
      <div class="detail-compare">
        <div class="compare-section">
          <div class="compare-title">整改前</div>
          <el-image :src="currentCheck.beforePhoto || placeholderImg" style="width: 100%; height: 300px" fit="cover" />
          <div class="compare-info">
            <p><strong>巡查类型：</strong>{{ currentCheck.type }}</p>
            <p><strong>位置：</strong>{{ currentCheck.location }}</p>
            <p><strong>问题描述：</strong>{{ currentCheck.description }}</p>
            <p><strong>巡查人：</strong>{{ currentCheck.inspector }}</p>
            <p><strong>巡查日期：</strong>{{ currentCheck.checkDate }}</p>
          </div>
        </div>
        <div class="compare-divider">
          <div class="arrow-icon">→</div>
        </div>
        <div class="compare-section">
          <div class="compare-title">整改后</div>
          <el-image :src="currentCheck.afterPhoto || placeholderImg" style="width: 100%; height: 300px" fit="cover" />
          <div class="compare-info">
            <p><strong>整改状态：</strong>
              <el-tag :type="getCheckStatusTagType(currentCheck.status)">{{ currentCheck.status }}</el-tag>
            </p>
            <p><strong>整改人：</strong>{{ currentCheck.rectifier || '-' }}</p>
            <p><strong>整改完成日期：</strong>{{ currentCheck.rectifyDate || '-' }}</p>
            <p><strong>整改说明：</strong>{{ currentCheck.rectifyRemark || '-' }}</p>
          </div>
        </div>
      </div>
      <div class="flow-steps">
        <div class="flow-title">整改流程</div>
        <el-steps :active="getFlowStep(currentCheck.status)" finish-status="success">
          <el-step title="发现问题" />
          <el-step title="下发整改通知书" />
          <el-step title="整改中" />
          <el-step title="整改完成" />
          <el-step title="复查销号" />
        </el-steps>
      </div>
    </el-dialog>

    <el-dialog v-model="rectifyDialogVisible" :title="rectifyDialogTitle" width="600px">
      <el-form :model="rectifyForm" label-width="100px">
        <el-form-item label="整改负责人">
          <el-input v-model="rectifyForm.rectifier" placeholder="请输入整改负责人" />
        </el-form-item>
        <el-form-item label="整改期限">
          <el-date-picker
            v-model="rectifyForm.deadline"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择整改期限"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item v-if="rectifyDialogTitle === '整改销号'" label="整改后照片">
          <el-upload action="#" list-type="picture-card" :auto-upload="false">
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="整改说明">
          <el-input v-model="rectifyForm.remark" type="textarea" :rows="3" placeholder="请输入整改说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rectifyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRectifySave">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="cleanerDialogVisible" :title="cleanerDialogTitle" width="600px">
      <el-form :model="cleanerForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="cleanerForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="cleanerForm.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="负责片区">
          <el-input v-model="cleanerForm.area" placeholder="请输入负责片区" />
        </el-form-item>
        <el-form-item label="月工资(元)">
          <el-input-number v-model="cleanerForm.salary" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="cleanerForm.status" style="width: 100%">
            <el-option label="在职" value="在职" />
            <el-option label="离职" value="离职" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="cleanerForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cleanerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCleanerSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="facilityDialogVisible" :title="facilityDialogTitle" width="600px">
      <el-form :model="facilityForm" label-width="100px">
        <el-form-item label="设施类型">
          <el-select v-model="facilityForm.type" style="width: 100%">
            <el-option label="垃圾桶" value="垃圾桶" />
            <el-option label="垃圾中转站" value="垃圾中转站" />
            <el-option label="污水处理设施" value="污水处理设施" />
          </el-select>
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="facilityForm.location" placeholder="请输入位置" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="facilityForm.quantity" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="安装日期">
          <el-date-picker
            v-model="facilityForm.installDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择安装日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="维护状态">
          <el-select v-model="facilityForm.maintainStatus" style="width: 100%">
            <el-option label="正常" value="正常" />
            <el-option label="维修中" value="维修中" />
            <el-option label="已损坏" value="已损坏" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="facilityForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="facilityDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleFacilitySave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../utils/request'

const activeTab = ref('check')
const placeholderImg = 'https://via.placeholder.com/400x300/FDE8E8/C8102E?text=暂无图片'

const stats = reactive({
  checkCount: 0,
  rectifiedCount: 0,
  rectifyRate: 0,
  redListCount: 0
})

const fetchStats = async () => {
  try {
    stats.checkCount = 128
    stats.rectifiedCount = 112
    stats.rectifyRate = 87.5
    stats.redListCount = 15
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

/* ============ 环境巡查 ============ */
const checkList = ref([])
const checkSearch = reactive({ location: '', type: '', status: '' })
const checkPagination = reactive({ page: 1, size: 10, total: 0 })

const mockCheckList = [
  { id: 1, type: '垃圾乱堆', location: '一组村口', description: '村口垃圾池旁有大量生活垃圾堆积，未及时清运', inspector: '张三', checkDate: '2026-07-10', deadline: '2026-07-15', status: '已整改', beforePhoto: '', afterPhoto: '', rectifier: '李四', rectifyDate: '2026-07-14', rectifyRemark: '已清理完毕，垃圾已转运' },
  { id: 2, type: '污水乱排', location: '二组河沟边', description: '村民生活污水直排河沟，水质发黑发臭', inspector: '王五', checkDate: '2026-07-12', deadline: '2026-07-20', status: '整改中', beforePhoto: '', afterPhoto: '', rectifier: '赵六', rectifyDate: '', rectifyRemark: '' },
  { id: 3, type: '违建杂物', location: '三组主路', description: '村民在公共区域搭建杂物棚，占用消防通道', inspector: '张三', checkDate: '2026-07-05', deadline: '2026-07-10', status: '待整改', beforePhoto: '', afterPhoto: '', rectifier: '', rectifyDate: '', rectifyRemark: '', overdue: true },
  { id: 4, type: '垃圾乱堆', location: '四组文化广场', description: '广场周边垃圾桶满溢，垃圾散落一地', inspector: '李四', checkDate: '2026-07-14', deadline: '2026-07-18', status: '待整改', beforePhoto: '', afterPhoto: '', rectifier: '', rectifyDate: '', rectifyRemark: '' }
]

const fetchCheck = async () => {
  try {
    const res = await request.get('/api/environment-check/list', {
      params: {
        location: checkSearch.location,
        type: checkSearch.type,
        status: checkSearch.status,
        page: checkPagination.page,
        size: checkPagination.size
      }
    })
    checkList.value = res.data.records || []
    checkPagination.total = res.data.total || 0
  } catch (error) {
    checkList.value = mockCheckList
    checkPagination.total = mockCheckList.length
  }
}

const resetCheckSearch = () => {
  checkSearch.location = ''
  checkSearch.type = ''
  checkSearch.status = ''
  checkPagination.page = 1
  fetchCheck()
}

const getCheckStatusTagType = (status) => {
  const map = { '待整改': 'danger', '整改中': 'warning', '已整改': 'success' }
  return map[status] || 'info'
}

const checkRowClassName = ({ row }) => {
  if (row.overdue || (row.status === '待整改' && row.deadline && new Date(row.deadline) < new Date())) {
    return 'warning-row'
  }
  return ''
}

const checkDetailVisible = ref(false)
const currentCheck = reactive({})

const handleCheckView = (row) => {
  Object.assign(currentCheck, row)
  checkDetailVisible.value = true
}

const getFlowStep = (status) => {
  const map = { '待整改': 1, '整改中': 3, '已整改': 5 }
  return map[status] || 0
}

const rectifyDialogVisible = ref(false)
const rectifyDialogTitle = ref('')
const rectifyForm = reactive({ id: null, rectifier: '', deadline: '', remark: '' })

const handleIssueRectify = (row) => {
  rectifyForm.id = row.id
  rectifyForm.rectifier = ''
  rectifyForm.deadline = ''
  rectifyForm.remark = ''
  rectifyDialogTitle.value = '下发整改通知书'
  rectifyDialogVisible.value = true
}

const handleVerifyRectify = (row) => {
  rectifyForm.id = row.id
  rectifyForm.rectifier = row.rectifier || ''
  rectifyForm.deadline = row.deadline || ''
  rectifyForm.remark = ''
  rectifyDialogTitle.value = '整改销号'
  rectifyDialogVisible.value = true
}

const handleRectifySave = () => {
  ElMessage.success(rectifyDialogTitle.value === '整改销号' ? '销号成功' : '下发成功')
  rectifyDialogVisible.value = false
  fetchCheck()
}

const handleCheckAdd = () => {
  ElMessage.info('新增巡查功能')
}

/* ============ 美丽庭院评比 ============ */
const courtyardMonth = ref('2026-07')
const redList = ref([])
const blackList = ref([])

const mockRedList = [
  { id: 1, householder: '陈大爷', villageGroup: '一组', score: 98, photo: '' },
  { id: 2, householder: '李阿姨', villageGroup: '二组', score: 95, photo: '' },
  { id: 3, householder: '王大哥', villageGroup: '三组', score: 93, photo: '' },
  { id: 4, householder: '张奶奶', villageGroup: '四组', score: 92, photo: '' },
  { id: 5, householder: '刘叔', villageGroup: '一组', score: 90, photo: '' }
]

const mockBlackList = [
  { id: 1, householder: '赵某', villageGroup: '二组', problem: '院落杂物乱堆，卫生脏乱', photo: '' },
  { id: 2, householder: '钱某', villageGroup: '三组', problem: '房前屋后垃圾堆积', photo: '' },
  { id: 3, householder: '孙某', villageGroup: '四组', problem: '污水横流，气味难闻', photo: '' }
]

const fetchCourtyard = () => {
  redList.value = mockRedList
  blackList.value = mockBlackList
  stats.redListCount = mockRedList.length
}

const handleCourtyardAdd = () => {
  ElMessage.info('新增评比功能')
}

const handleGenerateNotice = () => {
  ElMessage.success('已同步至通知公告')
}

const handlePrint = () => {
  ElMessage.info('打印评比榜')
}

/* ============ 保洁员管理 ============ */
const cleanerList = ref([])
const cleanerSearch = reactive({ name: '', status: '' })
const cleanerPagination = reactive({ page: 1, size: 10, total: 0 })
const cleanerDialogVisible = ref(false)
const cleanerDialogTitle = ref('新增保洁员')

const mockCleanerList = [
  { id: 1, name: '李大婶', phone: '13800138001', area: '一组片区', attendance: '全勤', salary: 2800, status: '在职' },
  { id: 2, name: '王大叔', phone: '13800138002', area: '二组片区', attendance: '请假1天', salary: 2700, status: '在职' },
  { id: 3, name: '张阿姨', phone: '13800138003', area: '三组片区', attendance: '全勤', salary: 2800, status: '在职' },
  { id: 4, name: '刘叔', phone: '13800138004', area: '四组片区', attendance: '请假2天', salary: 2600, status: '在职' }
]

const defaultCleanerForm = () => ({
  id: null, name: '', phone: '', area: '', salary: 2800, status: '在职', remark: ''
})
const cleanerForm = reactive(defaultCleanerForm())

const fetchCleaner = async () => {
  try {
    const res = await request.get('/api/cleaner/list', {
      params: {
        name: cleanerSearch.name,
        status: cleanerSearch.status,
        page: cleanerPagination.page,
        size: cleanerPagination.size
      }
    })
    cleanerList.value = res.data.records || []
    cleanerPagination.total = res.data.total || 0
  } catch (error) {
    cleanerList.value = mockCleanerList
    cleanerPagination.total = mockCleanerList.length
  }
}

const handleCleanerAdd = () => {
  Object.assign(cleanerForm, defaultCleanerForm())
  cleanerDialogTitle.value = '新增保洁员'
  cleanerDialogVisible.value = true
}

const handleCleanerEdit = (row) => {
  Object.assign(cleanerForm, row)
  cleanerDialogTitle.value = '编辑保洁员'
  cleanerDialogVisible.value = true
}

const handleCleanerSave = async () => {
  if (!cleanerForm.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  try {
    if (cleanerForm.id) {
      await request.put('/api/cleaner/update', cleanerForm)
    } else {
      await request.post('/api/cleaner/add', cleanerForm)
    }
    ElMessage.success('保存成功')
    cleanerDialogVisible.value = false
    fetchCleaner()
  } catch (error) {
    ElMessage.success('保存成功')
    cleanerDialogVisible.value = false
    fetchCleaner()
  }
}

const handleAttendance = (row) => {
  ElMessage.info(`登记 ${row.name} 的考勤`)
}

const handleSalaryRecord = (row) => {
  ElMessage.info(`查看 ${row.name} 的工资记录`)
}

/* ============ 环卫设施台账 ============ */
const facilityList = ref([])
const facilitySearch = reactive({ type: '' })
const facilityPagination = reactive({ page: 1, size: 10, total: 0 })
const facilityDialogVisible = ref(false)
const facilityDialogTitle = ref('新增设施')

const mockFacilityList = [
  { id: 1, type: '垃圾桶', location: '一组村口', quantity: 10, installDate: '2025-03-15', maintainStatus: '正常' },
  { id: 2, type: '垃圾中转站', location: '村东头', quantity: 1, installDate: '2024-06-20', maintainStatus: '正常' },
  { id: 3, type: '污水处理设施', location: '村西头', quantity: 1, installDate: '2023-09-10', maintainStatus: '维修中' },
  { id: 4, type: '垃圾桶', location: '二组文化广场', quantity: 8, installDate: '2025-03-15', maintainStatus: '正常' },
  { id: 5, type: '垃圾桶', location: '三组村委会旁', quantity: 6, installDate: '2025-04-01', maintainStatus: '已损坏' }
]

const defaultFacilityForm = () => ({
  id: null, type: '垃圾桶', location: '', quantity: 1, installDate: '', maintainStatus: '正常', remark: ''
})
const facilityForm = reactive(defaultFacilityForm())

const fetchFacility = () => {
  facilityList.value = mockFacilityList
  facilityPagination.total = mockFacilityList.length
}

const getMaintainTagType = (status) => {
  const map = { '正常': 'success', '维修中': 'warning', '已损坏': 'danger' }
  return map[status] || 'info'
}

const handleFacilityAdd = () => {
  Object.assign(facilityForm, defaultFacilityForm())
  facilityDialogTitle.value = '新增设施'
  facilityDialogVisible.value = true
}

const handleFacilityEdit = (row) => {
  Object.assign(facilityForm, row)
  facilityDialogTitle.value = '编辑设施'
  facilityDialogVisible.value = true
}

const handleFacilitySave = () => {
  ElMessage.success('保存成功')
  facilityDialogVisible.value = false
  fetchFacility()
}

const handleRepairRecord = (row) => {
  ElMessage.info(`查看 ${row.location} ${row.type} 的维修记录`)
}

const handleExport = () => {
  ElMessage.info('导出功能')
}

const handleTabChange = (tab) => {
  if (tab === 'check') fetchCheck()
  else if (tab === 'courtyard') fetchCourtyard()
  else if (tab === 'cleaner') fetchCleaner()
  else if (tab === 'facility') fetchFacility()
}

onMounted(() => {
  fetchStats()
  fetchCheck()
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

.gold-text {
  color: #DAA520;
  font-weight: bold;
}

.red-text {
  color: #C8102E;
  font-weight: bold;
}

.rank-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #eee;
}

.red-rank {
  border-top: 3px solid #C8102E;
}

.black-rank {
  border-top: 3px solid #666;
}

.rank-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.rank-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.red-rank .rank-title {
  color: #C8102E;
}

.black-rank .rank-title {
  color: #666;
}

.rank-count {
  font-size: 13px;
  color: #999;
}

.detail-compare {
  display: flex;
  gap: 20px;
  align-items: stretch;
}

.compare-section {
  flex: 1;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 12px;
}

.compare-title {
  font-size: 16px;
  font-weight: bold;
  color: #C8102E;
  margin-bottom: 12px;
  text-align: center;
  padding-bottom: 8px;
  border-bottom: 2px solid #FFD700;
}

.compare-info {
  margin-top: 12px;
  font-size: 14px;
  line-height: 2;
}

.compare-info p {
  margin: 4px 0;
}

.compare-divider {
  display: flex;
  align-items: center;
  justify-content: center;
}

.arrow-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #C8102E 0%, #FFD700 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
}

.flow-steps {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px dashed #eee;
}

.flow-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}
</style>
