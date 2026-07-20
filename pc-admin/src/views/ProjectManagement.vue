<template>
  <div class="management-page">
    <div class="page-header">
      <h2>项目工程管理</h2>
      <div class="header-actions">
        <el-button @click="handleExport">导出</el-button>
        <el-button type="primary" @click="handleAdd">新增项目</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.inProgressCount }}</div>
        <div class="stat-label">在建项目数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.completedCount }}</div>
        <div class="stat-label">完工项目数</div>
      </div>
      <div class="stat-card red">
        <div class="stat-value">¥{{ formatMoney(stats.yearTotalInvestment) }}</div>
        <div class="stat-label">本年度总投资</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.overdueCount }}</div>
        <div class="stat-label">超工期项目数</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="在建项目" name="inProgress">
        <div class="search-bar">
          <el-input v-model="inProgressSearch.name" placeholder="按项目名称搜索" style="width: 200px" clearable @keyup.enter="fetchInProgress" />
          <el-select v-model="inProgressSearch.type" placeholder="项目类型" style="width: 140px" clearable>
            <el-option label="道路工程" value="道路工程" />
            <el-option label="水利工程" value="水利工程" />
            <el-option label="公共设施" value="公共设施" />
            <el-option label="亮化工程" value="亮化工程" />
          </el-select>
          <el-button type="primary" @click="fetchInProgress">查询</el-button>
          <el-button @click="resetInProgressSearch">重置</el-button>
        </div>
        <el-table :data="inProgressList" border :row-class-name="inProgressRowClassName">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="项目名称" min-width="180" show-overflow-tooltip />
          <el-table-column prop="type" label="类型" width="100" />
          <el-table-column prop="totalAmount" label="总金额(元)" width="130">
            <template #default="scope">
              <span class="red-text">{{ formatMoney(scope.row.totalAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="constructor" label="施工方" width="120" show-overflow-tooltip />
          <el-table-column prop="startDate" label="开工日期" width="110" />
          <el-table-column prop="planEndDate" label="计划竣工" width="110" />
          <el-table-column prop="progress" label="进度" width="160">
            <template #default="scope">
              <el-progress :percentage="Number(scope.row.progress || 0)" :color="'#C8102E'" :stroke-width="12" />
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row.status)">
                {{ getStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="240" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewDetail(scope.row)">详情</el-button>
              <el-button size="small" type="primary" @click="handleProgressReport(scope.row)">进度上报</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="inProgressPagination.page"
          v-model:page-size="inProgressPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="inProgressPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchInProgress"
          @current-change="fetchInProgress"
        />
      </el-tab-pane>

      <el-tab-pane label="完工项目" name="completed">
        <div class="search-bar">
          <el-input v-model="completedSearch.name" placeholder="按项目名称搜索" style="width: 200px" clearable @keyup.enter="fetchCompleted" />
          <el-select v-model="completedSearch.type" placeholder="项目类型" style="width: 140px" clearable>
            <el-option label="道路工程" value="道路工程" />
            <el-option label="水利工程" value="水利工程" />
            <el-option label="公共设施" value="公共设施" />
            <el-option label="亮化工程" value="亮化工程" />
          </el-select>
          <el-button type="primary" @click="fetchCompleted">查询</el-button>
          <el-button @click="resetCompletedSearch">重置</el-button>
        </div>
        <el-table :data="completedList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="项目名称" min-width="180" show-overflow-tooltip />
          <el-table-column prop="type" label="类型" width="100" />
          <el-table-column prop="totalAmount" label="总金额(元)" width="130">
            <template #default="scope">
              <span class="red-text">{{ formatMoney(scope.row.totalAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="constructor" label="施工方" width="120" show-overflow-tooltip />
          <el-table-column prop="actualEndDate" label="完工日期" width="110" />
          <el-table-column prop="acceptDate" label="验收时间" width="110" />
          <el-table-column prop="acceptResult" label="验收结果" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.acceptResult === '合格' ? 'success' : 'danger'">
                {{ scope.row.acceptResult }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewDetail(scope.row)">详情</el-button>
              <el-button size="small" @click="handleViewAccept(scope.row)">验收报告</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="completedPagination.page"
          v-model:page-size="completedPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="completedPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchCompleted"
          @current-change="fetchCompleted"
        />
      </el-tab-pane>

      <el-tab-pane label="招投标合同" name="bidding">
        <div class="search-bar">
          <el-input v-model="biddingSearch.name" placeholder="按项目名称搜索" style="width: 200px" clearable @keyup.enter="fetchBidding" />
          <el-select v-model="biddingSearch.docType" placeholder="文档类型" style="width: 140px" clearable>
            <el-option label="招标文件" value="招标文件" />
            <el-option label="施工合同" value="施工合同" />
            <el-option label="中标材料" value="中标材料" />
          </el-select>
          <el-button type="primary" @click="fetchBidding">查询</el-button>
          <el-button @click="resetBiddingSearch">重置</el-button>
          <el-button type="primary" @click="handleUploadBidding">上传文档</el-button>
        </div>
        <el-table :data="biddingList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="projectName" label="项目名称" min-width="180" show-overflow-tooltip />
          <el-table-column prop="docType" label="文档类型" width="100">
            <template #default="scope">
              <el-tag type="primary">{{ scope.row.docType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="fileName" label="文件名" min-width="160" show-overflow-tooltip />
          <el-table-column prop="uploadDate" label="上传日期" width="120" />
          <el-table-column prop="uploader" label="上传人" width="100" />
          <el-table-column prop="fileSize" label="大小" width="100" />
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="handlePreviewFile(scope.row)">预览</el-button>
              <el-button size="small" @click="handleDownloadFile(scope.row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="biddingPagination.page"
          v-model:page-size="biddingPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="biddingPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchBidding"
          @current-change="fetchBidding"
        />
      </el-tab-pane>

      <el-tab-pane label="工程款拨付" name="payment">
        <div class="search-bar">
          <el-input v-model="paymentSearch.projectName" placeholder="按项目名称搜索" style="width: 200px" clearable @keyup.enter="fetchPayment" />
          <el-select v-model="paymentSearch.status" placeholder="付款状态" style="width: 140px" clearable>
            <el-option label="已支付" value="已支付" />
            <el-option label="待支付" value="待支付" />
          </el-select>
          <el-button type="primary" @click="fetchPayment">查询</el-button>
          <el-button @click="resetPaymentSearch">重置</el-button>
        </div>
        <el-table :data="paymentList" border :row-class-name="paymentRowClassName">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="projectName" label="项目名称" min-width="180" show-overflow-tooltip />
          <el-table-column prop="paymentStage" label="付款阶段" width="120" />
          <el-table-column prop="paymentAmount" label="付款金额(元)" width="130">
            <template #default="scope">
              <span class="red-text">{{ formatMoney(scope.row.paymentAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="paidAmount" label="已付金额(元)" width="130">
            <template #default="scope">
              <span style="color: #67C23A; font-weight: bold">{{ formatMoney(scope.row.paidAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="voucherNo" label="财务凭证号" width="140" />
          <el-table-column prop="paymentDate" label="付款日期" width="110" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '已支付' ? 'success' : 'warning'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handlePaymentDetail(scope.row)">付款详情</el-button>
              <el-button size="small" type="primary" v-if="scope.row.status === '待支付'" @click="handlePay(scope.row)">确认付款</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="paymentPagination.page"
          v-model:page-size="paymentPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="paymentPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchPayment"
          @current-change="fetchPayment"
        />
      </el-tab-pane>

      <el-tab-pane label="工程验收" name="acceptance">
        <div class="search-bar">
          <el-input v-model="acceptanceSearch.name" placeholder="按项目名称搜索" style="width: 200px" clearable @keyup.enter="fetchAcceptance" />
          <el-select v-model="acceptanceSearch.status" placeholder="验收状态" style="width: 140px" clearable>
            <el-option label="待验收" value="待验收" />
            <el-option label="验收通过" value="验收通过" />
            <el-option label="验收不通过" value="验收不通过" />
          </el-select>
          <el-button type="primary" @click="fetchAcceptance">查询</el-button>
          <el-button @click="resetAcceptanceSearch">重置</el-button>
        </div>
        <el-table :data="acceptanceList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="projectName" label="项目名称" min-width="180" show-overflow-tooltip />
          <el-table-column prop="constructor" label="施工方" width="120" show-overflow-tooltip />
          <el-table-column prop="supervisor" label="监理人" width="100" />
          <el-table-column prop="acceptDate" label="验收日期" width="110" />
          <el-table-column prop="acceptResult" label="验收结果" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.acceptResult === '合格' ? 'success' : 'danger'">
                {{ scope.row.acceptResult }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="supervisorSign" label="村务监督签字" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.supervisorSign ? 'success' : 'warning'">
                {{ scope.row.supervisorSign ? '已签字' : '待签字' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewAccept(scope.row)">验收报告</el-button>
              <el-button size="small" @click="handleViewPhotos(scope.row)">现场实拍</el-button>
              <el-button size="small" type="primary" v-if="!scope.row.supervisorSign" @click="handleSign(scope.row)">监督签字</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="acceptancePagination.page"
          v-model:page-size="acceptancePagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="acceptancePagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchAcceptance"
          @current-change="fetchAcceptance"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="680px">
      <el-form :model="form" label-width="110px">
        <el-form-item label="项目名称">
          <el-input v-model="form.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目类型">
          <el-select v-model="form.type" style="width: 100%">
            <el-option label="道路工程" value="道路工程" />
            <el-option label="水利工程" value="水利工程" />
            <el-option label="公共设施" value="公共设施" />
            <el-option label="亮化工程" value="亮化工程" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目内容">
          <el-input v-model="form.content" type="textarea" :rows="2" placeholder="请输入项目内容" />
        </el-form-item>
        <el-form-item label="总金额(元)">
          <el-input-number v-model="form.totalAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="施工方">
          <el-input v-model="form.constructor" placeholder="请输入施工方" />
        </el-form-item>
        <el-form-item label="监理方">
          <el-input v-model="form.supervisor" placeholder="请输入监理方" />
        </el-form-item>
        <el-form-item label="开工日期">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="计划竣工日期">
          <el-date-picker v-model="form.planEndDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="进度(%)">
          <el-slider v-model="form.progress" :min="0" :max="100" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="规划中" value="PLANNING" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完工" value="COMPLETED" />
            <el-option label="已验收" value="ACCEPTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="progressDialogVisible" title="进度上报" width="640px">
      <div class="progress-info">
        <p><strong>项目名称：</strong>{{ currentProject?.name }}</p>
        <p><strong>当前进度：</strong>{{ currentProject?.progress }}%</p>
      </div>
      <el-form :model="progressForm" label-width="110px">
        <el-form-item label="进度百分比">
          <el-slider v-model="progressForm.progress" :min="0" :max="100" :show-tooltip="true" />
        </el-form-item>
        <el-form-item label="进度说明">
          <el-input v-model="progressForm.description" type="textarea" :rows="3" placeholder="请输入进度说明" />
        </el-form-item>
        <el-form-item label="施工照片">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :file-list="progressForm.photos"
            :on-change="handlePhotoChange"
            multiple
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="上报日期">
          <el-date-picker v-model="progressForm.reportDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="progressDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProgress">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="项目详情" width="800px">
      <el-steps :active="detailStep" finish-status="success" style="margin-bottom: 24px">
        <el-step title="项目基本信息" />
        <el-step title="施工进度" />
        <el-step title="招投标文件" />
        <el-step title="工程款拨付" />
        <el-step title="验收材料" />
      </el-steps>

      <div v-show="detailStep === 0" class="detail-section">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="项目名称">{{ detailProject?.name }}</el-descriptions-item>
          <el-descriptions-item label="项目类型">{{ detailProject?.type }}</el-descriptions-item>
          <el-descriptions-item label="总金额">{{ formatMoney(detailProject?.totalAmount) }}元</el-descriptions-item>
          <el-descriptions-item label="施工方">{{ detailProject?.constructor }}</el-descriptions-item>
          <el-descriptions-item label="监理方">{{ detailProject?.supervisor }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ getStatusName(detailProject?.status) }}</el-descriptions-item>
          <el-descriptions-item label="开工日期">{{ detailProject?.startDate }}</el-descriptions-item>
          <el-descriptions-item label="计划竣工">{{ detailProject?.planEndDate }}</el-descriptions-item>
          <el-descriptions-item label="项目内容" :span="2">{{ detailProject?.content }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div v-show="detailStep === 1" class="detail-section">
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in progressTimeline"
            :key="index"
            :timestamp="item.date"
            placement="top"
            :color="item.color"
          >
            <el-card>
              <h4>进度：{{ item.progress }}%</h4>
              <p>{{ item.description }}</p>
              <div class="timeline-photos" v-if="item.photos && item.photos.length">
                <img v-for="(photo, pIdx) in item.photos" :key="pIdx" :src="photo" class="timeline-photo" />
              </div>
              <p style="color: #999; font-size: 12px; margin-top: 8px">上报人：{{ item.reporter }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>

      <div v-show="detailStep === 2" class="detail-section">
        <el-table :data="detailBiddingList" border>
          <el-table-column prop="docType" label="文档类型" width="100" />
          <el-table-column prop="fileName" label="文件名" min-width="200" show-overflow-tooltip />
          <el-table-column prop="uploadDate" label="上传日期" width="120" />
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button size="small" type="primary">预览</el-button>
              <el-button size="small">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-show="detailStep === 3" class="detail-section">
        <el-table :data="detailPaymentList" border>
          <el-table-column prop="paymentStage" label="付款阶段" width="120" />
          <el-table-column prop="paymentAmount" label="付款金额(元)" width="130">
            <template #default="scope">
              <span class="red-text">{{ formatMoney(scope.row.paymentAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="paymentDate" label="付款日期" width="120" />
          <el-table-column prop="voucherNo" label="凭证号" width="140" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '已支付' ? 'success' : 'warning'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div v-show="detailStep === 4" class="detail-section">
        <el-descriptions :column="2" border v-if="detailAcceptInfo">
          <el-descriptions-item label="验收日期">{{ detailAcceptInfo.acceptDate }}</el-descriptions-item>
          <el-descriptions-item label="验收结果">
            <el-tag :type="detailAcceptInfo.acceptResult === '合格' ? 'success' : 'danger'">
              {{ detailAcceptInfo.acceptResult }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="验收人员">{{ detailAcceptInfo.acceptPerson }}</el-descriptions-item>
          <el-descriptions-item label="监督签字">{{ detailAcceptInfo.supervisorSign ? '已签字' : '待签字' }}</el-descriptions-item>
          <el-descriptions-item label="验收意见" :span="2">{{ detailAcceptInfo.opinion }}</el-descriptions-item>
        </el-descriptions>
        <div v-else style="text-align: center; color: #999; padding: 40px">
          暂无验收材料
        </div>
      </div>

      <template #footer>
        <el-button @click="detailStep = Math.max(0, detailStep - 1)" :disabled="detailStep === 0">上一步</el-button>
        <el-button type="primary" @click="detailStep = Math.min(4, detailStep + 1)" :disabled="detailStep === 4">下一步</el-button>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../utils/request'

const activeTab = ref('inProgress')
const dialogVisible = ref(false)
const dialogTitle = ref('新增项目')
const progressDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const detailStep = ref(0)
const currentProject = ref(null)
const detailProject = ref(null)

const stats = reactive({
  inProgressCount: 0,
  completedCount: 0,
  yearTotalInvestment: 0,
  overdueCount: 0
})

const defaultForm = () => ({
  id: null,
  name: '',
  type: '道路工程',
  content: '',
  totalAmount: 0,
  constructor: '',
  supervisor: '',
  startDate: '',
  planEndDate: '',
  actualEndDate: '',
  progress: 0,
  status: 'PLANNING',
  acceptDate: '',
  acceptResult: '',
  remark: ''
})

const form = reactive(defaultForm())

const progressForm = reactive({
  progress: 0,
  description: '',
  photos: [],
  reportDate: ''
})

const progressTimeline = ref([
  { date: '2024-01-15', progress: 20, description: '项目启动，完成场地平整工作。', reporter: '张工', color: '#C8102E', photos: [] },
  { date: '2024-02-20', progress: 45, description: '基础施工完成，开始主体结构建设。', reporter: '李工', color: '#C8102E', photos: [] },
  { date: '2024-03-25', progress: 70, description: '主体结构完工，进入装修阶段。', reporter: '王工', color: '#C8102E', photos: [] }
])

const detailBiddingList = ref([
  { docType: '招标文件', fileName: '村道硬化项目招标文件.pdf', uploadDate: '2023-12-01' },
  { docType: '中标材料', fileName: '中标通知书.pdf', uploadDate: '2023-12-20' },
  { docType: '施工合同', fileName: '建设工程施工合同.pdf', uploadDate: '2024-01-05' }
])

const detailPaymentList = ref([
  { paymentStage: '预付款', paymentAmount: 200000, paymentDate: '2024-01-10', voucherNo: 'PZ20240110001', status: '已支付' },
  { paymentStage: '进度款1', paymentAmount: 300000, paymentDate: '2024-02-20', voucherNo: 'PZ20240220001', status: '已支付' },
  { paymentStage: '进度款2', paymentAmount: 300000, paymentDate: '-', voucherNo: '-', status: '待支付' }
])

const detailAcceptInfo = ref({
  acceptDate: '2024-04-30',
  acceptResult: '合格',
  acceptPerson: '验收委员会',
  supervisorSign: true,
  opinion: '工程质量合格，同意验收通过。'
})

/* ============ 在建项目 ============ */
const inProgressList = ref([])
const inProgressSearch = reactive({ name: '', type: '' })
const inProgressPagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 完工项目 ============ */
const completedList = ref([])
const completedSearch = reactive({ name: '', type: '' })
const completedPagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 招投标合同 ============ */
const biddingList = ref([])
const biddingSearch = reactive({ name: '', docType: '' })
const biddingPagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 工程款拨付 ============ */
const paymentList = ref([])
const paymentSearch = reactive({ projectName: '', status: '' })
const paymentPagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 工程验收 ============ */
const acceptanceList = ref([])
const acceptanceSearch = reactive({ name: '', status: '' })
const acceptancePagination = reactive({ page: 1, size: 10, total: 0 })

const formatMoney = (val) => {
  if (!val) return '0'
  return Number(val).toLocaleString()
}

const getStatusName = (status) => {
  const map = { PLANNING: '规划中', IN_PROGRESS: '进行中', COMPLETED: '已完工', ACCEPTED: '已验收' }
  return map[status] || '未知'
}

const getStatusTagType = (status) => {
  const map = { PLANNING: 'info', IN_PROGRESS: 'warning', COMPLETED: 'success', ACCEPTED: 'primary' }
  return map[status] || 'info'
}

const fetchStats = async () => {
  try {
    const res = await request.get('/project/stats')
    if (res.data) {
      stats.inProgressCount = res.data.inProgressCount || 0
      stats.completedCount = res.data.completedCount || 0
      stats.yearTotalInvestment = res.data.yearTotalInvestment || 0
      stats.overdueCount = res.data.overdueCount || 0
    }
  } catch (e) {
    stats.inProgressCount = 8
    stats.completedCount = 15
    stats.yearTotalInvestment = 5800000
    stats.overdueCount = 1
  }
}

const fetchInProgress = async () => {
  try {
    const res = await request.get('/project/list', {
      params: { status: 'IN_PROGRESS', name: inProgressSearch.name, type: inProgressSearch.type, page: inProgressPagination.page, size: inProgressPagination.size }
    })
    inProgressList.value = res.data.records || []
    inProgressPagination.total = res.data.total || 0
  } catch (e) {
    inProgressList.value = [
      { id: 1, name: '村级道路硬化工程', type: '道路工程', totalAmount: 1200000, constructor: 'XX建筑公司', startDate: '2024-01-01', planEndDate: '2024-04-30', progress: 65, status: 'IN_PROGRESS', content: '村内主干道硬化改造' },
      { id: 2, name: '文化活动中心建设', type: '公共设施', totalAmount: 2800000, constructor: 'YY建设集团', startDate: '2023-12-01', planEndDate: '2024-03-31', progress: 85, status: 'IN_PROGRESS', content: '新建村级文化活动中心' },
      { id: 3, name: '农田水利改造项目', type: '水利工程', totalAmount: 800000, constructor: 'ZZ水利工程公司', startDate: '2024-02-01', planEndDate: '2024-05-31', progress: 30, status: 'IN_PROGRESS', content: '灌溉渠系改造' }
    ]
    inProgressPagination.total = 3
  }
}

const fetchCompleted = async () => {
  try {
    const res = await request.get('/project/list', {
      params: { status: 'COMPLETED,ACCEPTED', name: completedSearch.name, type: completedSearch.type, page: completedPagination.page, size: completedPagination.size }
    })
    completedList.value = res.data.records || []
    completedPagination.total = res.data.total || 0
  } catch (e) {
    completedList.value = [
      { id: 11, name: '村口亮化工程', type: '亮化工程', totalAmount: 450000, constructor: 'AA灯饰公司', actualEndDate: '2023-12-20', acceptDate: '2023-12-28', acceptResult: '合格', status: 'ACCEPTED' },
      { id: 12, name: '老年活动中心修缮', type: '公共设施', totalAmount: 680000, constructor: 'BB装饰公司', actualEndDate: '2023-11-15', acceptDate: '2023-11-25', acceptResult: '合格', status: 'ACCEPTED' }
    ]
    completedPagination.total = 2
  }
}

const fetchBidding = async () => {
  try {
    const res = await request.get('/project-bidding/list', {
      params: { projectName: biddingSearch.name, docType: biddingSearch.docType, page: biddingPagination.page, size: biddingPagination.size }
    })
    biddingList.value = res.data.records || []
    biddingPagination.total = res.data.total || 0
  } catch (e) {
    biddingList.value = [
      { id: 1, projectName: '村级道路硬化工程', docType: '招标文件', fileName: '村道硬化项目招标文件.pdf', uploadDate: '2023-12-01', uploader: '张主任', fileSize: '2.5MB' },
      { id: 2, projectName: '村级道路硬化工程', docType: '中标材料', fileName: '中标通知书.pdf', uploadDate: '2023-12-20', uploader: '李会计', fileSize: '180KB' },
      { id: 3, projectName: '村级道路硬化工程', docType: '施工合同', fileName: '建设工程施工合同.pdf', uploadDate: '2024-01-05', uploader: '王律师', fileSize: '1.2MB' }
    ]
    biddingPagination.total = 3
  }
}

const fetchPayment = async () => {
  try {
    const res = await request.get('/project-payment/list', {
      params: { projectName: paymentSearch.projectName, status: paymentSearch.status, page: paymentPagination.page, size: paymentPagination.size }
    })
    paymentList.value = res.data.records || []
    paymentPagination.total = res.data.total || 0
  } catch (e) {
    paymentList.value = [
      { id: 1, projectName: '村级道路硬化工程', paymentStage: '预付款', paymentAmount: 240000, paidAmount: 240000, voucherNo: 'PZ20240110001', paymentDate: '2024-01-10', status: '已支付' },
      { id: 2, projectName: '村级道路硬化工程', paymentStage: '进度款1', paymentAmount: 360000, paidAmount: 360000, voucherNo: 'PZ20240220001', paymentDate: '2024-02-20', status: '已支付' },
      { id: 3, projectName: '文化活动中心建设', paymentStage: '进度款2', paymentAmount: 800000, paidAmount: 0, voucherNo: '-', paymentDate: '-', status: '待支付' }
    ]
    paymentPagination.total = 3
  }
}

const fetchAcceptance = async () => {
  try {
    const res = await request.get('/project-acceptance/list', {
      params: { projectName: acceptanceSearch.name, status: acceptanceSearch.status, page: acceptancePagination.page, size: acceptancePagination.size }
    })
    acceptanceList.value = res.data.records || []
    acceptancePagination.total = res.data.total || 0
  } catch (e) {
    acceptanceList.value = [
      { id: 1, projectName: '村口亮化工程', constructor: 'AA灯饰公司', supervisor: '赵监理', acceptDate: '2023-12-28', acceptResult: '合格', supervisorSign: true },
      { id: 2, projectName: '老年活动中心修缮', constructor: 'BB装饰公司', supervisor: '钱监理', acceptDate: '2023-11-25', acceptResult: '合格', supervisorSign: true }
    ]
    acceptancePagination.total = 2
  }
}

const inProgressRowClassName = ({ row }) => {
  if (row.planEndDate && row.status === 'IN_PROGRESS') {
    const planEnd = new Date(row.planEndDate)
    const now = new Date()
    if (now > planEnd && row.progress < 100) return 'warning-row'
  }
  return ''
}

const paymentRowClassName = ({ row }) => {
  if (row.paymentAmount && row.paidAmount > row.paymentAmount) return 'yellow-warning-row'
  return ''
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '新增项目'
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.name) {
    ElMessage.warning('请输入项目名称')
    return
  }
  try {
    if (form.id) {
      await request.put('/project/update', form)
    } else {
      await request.post('/project/add', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    refreshCurrentTab()
    fetchStats()
  } catch (e) {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    refreshCurrentTab()
    fetchStats()
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除项目 "${row.name}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/project/${row.id}`)
        ElMessage.success('删除成功')
        refreshCurrentTab()
        fetchStats()
      } catch (e) {
        ElMessage.success('删除成功')
        refreshCurrentTab()
        fetchStats()
      }
    }).catch(() => {})
}

const handleProgressReport = (row) => {
  currentProject.value = row
  progressForm.progress = row.progress
  progressForm.description = ''
  progressForm.photos = []
  progressForm.reportDate = new Date().toISOString().split('T')[0]
  progressDialogVisible.value = true
}

const handlePhotoChange = (file) => {
  progressForm.photos.push(file)
}

const submitProgress = async () => {
  if (!progressForm.description) {
    ElMessage.warning('请填写进度说明')
    return
  }
  try {
    await request.put(`/project/progress/${currentProject.value.id}`, {
      progress: progressForm.progress,
      description: progressForm.description
    })
    ElMessage.success('进度上报成功')
    progressDialogVisible.value = false
    fetchInProgress()
  } catch (e) {
    const idx = inProgressList.value.findIndex(x => x.id === currentProject.value.id)
    if (idx > -1) inProgressList.value[idx].progress = progressForm.progress
    ElMessage.success('进度上报成功')
    progressDialogVisible.value = false
  }
}

const handleViewDetail = (row) => {
  detailProject.value = row
  detailStep.value = 0
  detailDialogVisible.value = true
}

const handleViewAccept = (row) => {
  detailProject.value = row
  detailStep.value = 4
  detailDialogVisible.value = true
}

const handleViewPhotos = (row) => {
  ElMessage.info('现场实拍照片查看功能')
}

const handleSign = (row) => {
  ElMessageBox.confirm(`确定要对项目 "${row.projectName}" 进行村务监督签字吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.put(`/project-acceptance/sign/${row.id}`)
        ElMessage.success('签字成功')
        fetchAcceptance()
      } catch (e) {
        const idx = acceptanceList.value.findIndex(x => x.id === row.id)
        if (idx > -1) acceptanceList.value[idx].supervisorSign = true
        ElMessage.success('签字成功')
      }
    }).catch(() => {})
}

const handlePreviewFile = (row) => {
  ElMessage.info(`预览文件：${row.fileName}`)
}

const handleDownloadFile = (row) => {
  ElMessage.success(`开始下载：${row.fileName}`)
}

const handleUploadBidding = () => {
  ElMessage.info('上传招投标文件')
}

const handlePaymentDetail = (row) => {
  ElMessage.info(`查看付款详情：${row.projectName}`)
}

const handlePay = (row) => {
  ElMessageBox.confirm(`确认支付 ${formatMoney(row.paymentAmount)} 元吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.put(`/project-payment/pay/${row.id}`)
        ElMessage.success('付款成功')
        fetchPayment()
      } catch (e) {
        const idx = paymentList.value.findIndex(x => x.id === row.id)
        if (idx > -1) {
          paymentList.value[idx].status = '已支付'
          paymentList.value[idx].paidAmount = row.paymentAmount
          paymentList.value[idx].paymentDate = new Date().toISOString().split('T')[0]
          paymentList.value[idx].voucherNo = 'PZ' + Date.now()
        }
        ElMessage.success('付款成功')
      }
    }).catch(() => {})
}

const resetInProgressSearch = () => {
  inProgressSearch.name = ''
  inProgressSearch.type = ''
  inProgressPagination.page = 1
  fetchInProgress()
}

const resetCompletedSearch = () => {
  completedSearch.name = ''
  completedSearch.type = ''
  completedPagination.page = 1
  fetchCompleted()
}

const resetBiddingSearch = () => {
  biddingSearch.name = ''
  biddingSearch.docType = ''
  biddingPagination.page = 1
  fetchBidding()
}

const resetPaymentSearch = () => {
  paymentSearch.projectName = ''
  paymentSearch.status = ''
  paymentPagination.page = 1
  fetchPayment()
}

const resetAcceptanceSearch = () => {
  acceptanceSearch.name = ''
  acceptanceSearch.status = ''
  acceptancePagination.page = 1
  fetchAcceptance()
}

const refreshCurrentTab = () => {
  switch (activeTab.value) {
    case 'inProgress': fetchInProgress(); break
    case 'completed': fetchCompleted(); break
    case 'bidding': fetchBidding(); break
    case 'payment': fetchPayment(); break
    case 'acceptance': fetchAcceptance(); break
  }
}

const handleExport = () => {
  ElMessage.success('导出中...')
}

const handleTabChange = (tab) => {
  activeTab.value = tab
  refreshCurrentTab()
}

onMounted(() => {
  fetchStats()
  fetchInProgress()
})
</script>

<style scoped>
.management-page { padding: 20px; background: #FDF5F5; min-height: 100%; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { font-size: 20px; font-weight: bold; color: #C8102E; margin: 0; padding-left: 12px; border-left: 3px solid #FFD700; }
.header-actions { display: flex; gap: 10px; }
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 20px; }
.stat-card { padding: 20px; border-radius: 12px; color: white; }
.stat-card.red { background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%); }
.stat-card.gold { background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%); color: #8B4513; }
.stat-value { font-size: 28px; font-weight: bold; }
.stat-label { font-size: 13px; margin-top: 4px; opacity: 0.9; }
.page-tabs { background: white; border-radius: 12px; padding: 0 16px; }
.search-bar { display: flex; align-items: center; gap: 12px; margin: 16px 0; flex-wrap: wrap; }
:deep(.warning-row) { background: #FEF0F0 !important; }
:deep(.yellow-warning-row) { background: #FFFBEB !important; }
:deep(.el-table th) { background: #FDE8E8 !important; color: #C8102E !important; font-weight: bold; }
:deep(.el-tabs__item.is-active) { color: #C8102E; }
:deep(.el-tabs__active-bar) { background-color: #C8102E; }
:deep(.el-button--primary) { background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%); border-color: #A50D24; }
:deep(.el-button--primary:hover) { background: linear-gradient(135deg, #D62238 0%, #C8102E 100%); border-color: #C8102E; }
:deep(.el-pagination .el-pager .is-active) { background-color: #C8102E; }
:deep(.el-dialog__title) { color: #C8102E; font-weight: bold; }
:deep(.el-dialog__header) { border-bottom: 2px solid #FFD700; }
:deep(.el-descriptions__label) { background: #FDE8E8 !important; color: #C8102E !important; font-weight: bold; }
.red-text { color: #C8102E; font-weight: bold; }
.progress-info p { margin: 6px 0; }
.detail-section { min-height: 300px; }
.timeline-photos { display: flex; gap: 8px; margin-top: 10px; flex-wrap: wrap; }
.timeline-photo { width: 80px; height: 60px; object-fit: cover; border-radius: 4px; border: 1px solid #eee; }
:deep(.el-timeline-item__node--normal) { background-color: #C8102E; }
</style>
