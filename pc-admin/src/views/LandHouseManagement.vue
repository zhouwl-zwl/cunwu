<template>
  <div class="management-page">
    <div class="page-header">
      <h2>土地管理</h2>
      <div class="header-actions">
        <el-button @click="handleExport">批量导出</el-button>
        <el-button type="primary" @click="handleAdd">新增审批</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.homesteadCount }}</div>
        <div class="stat-label">宅基地数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.cultivatedArea }}</div>
        <div class="stat-label">耕地面积(亩)</div>
      </div>
      <div class="stat-card red">
        <div class="stat-value">{{ stats.transferCount }}</div>
        <div class="stat-label">土地流转数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.illegalCount }}</div>
        <div class="stat-label">违建数量</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="宅基地台账" name="homestead">
        <div class="search-bar">
          <el-input
            v-model="searchForm.keyword"
            placeholder="按权属人搜索"
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
            v-model="searchForm.approvalStatus"
            placeholder="审批状态"
            style="width: 140px"
            clearable
          >
            <el-option label="待审批" value="PENDING" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="homesteadTableData" border :row-class-name="rowClassNameHomestead" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="owner" label="权属人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="landArea" label="地块面积(㎡)" width="120" />
          <el-table-column prop="confirmNo" label="确权编号" min-width="140" />
          <el-table-column prop="houseFloors" label="房屋层数" width="100" />
          <el-table-column prop="approvalStatus" label="审批状态" width="100">
            <template #default="scope">
              <el-tag :type="getApprovalTagType(scope.row.approvalStatus)">
                {{ getApprovalStatusName(scope.row.approvalStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="isOverArea" label="是否超占" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.isOverArea ? 'danger' : 'success'" size="small">
                {{ scope.row.isOverArea ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="240" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewHomestead(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="承包耕地" name="cultivated">
        <div class="search-bar">
          <el-input
            v-model="searchForm.keyword"
            placeholder="按承包人搜索"
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
            v-model="searchForm.cropType"
            placeholder="种植作物"
            style="width: 140px"
            clearable
          >
            <el-option label="水稻" value="水稻" />
            <el-option label="小麦" value="小麦" />
            <el-option label="玉米" value="玉米" />
            <el-option label="蔬菜" value="蔬菜" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="cultivatedTableData" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="contractor" label="承包人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="location" label="地块位置" min-width="180" show-overflow-tooltip />
          <el-table-column prop="muCount" label="亩数" width="100" />
          <el-table-column prop="cropType" label="种植作物" width="120" />
          <el-table-column prop="subsidyAmount" label="地力补贴金额(元)" width="140">
            <template #default="scope">
              <span class="amount-text">¥{{ scope.row.subsidyAmount || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewCultivated(scope.row)">查看</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="土地流转" name="transfer">
        <div class="search-bar">
          <el-input
            v-model="searchForm.keyword"
            placeholder="按转出方/转入方搜索"
            style="width: 220px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.contractStatus"
            placeholder="合同状态"
            style="width: 140px"
            clearable
          >
            <el-option label="有效" value="VALID" />
            <el-option label="已到期" value="EXPIRED" />
            <el-option label="已终止" value="TERMINATED" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="transferTableData" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="transferor" label="转出方" width="100" />
          <el-table-column prop="transferee" label="转入方" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="area" label="面积(亩)" width="100" />
          <el-table-column prop="transferPeriod" label="流转期限" width="120" />
          <el-table-column prop="rent" label="租金(元/年)" width="120">
            <template #default="scope">
              <span class="amount-text">¥{{ scope.row.rent || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="contractStatus" label="合同状态" width="100">
            <template #default="scope">
              <el-tag :type="getContractTagType(scope.row.contractStatus)">
                {{ getContractStatusName(scope.row.contractStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewTransfer(scope.row)">查看</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="征地拆迁" name="expropriation">
        <div class="search-bar">
          <el-input
            v-model="searchForm.keyword"
            placeholder="按征地地块搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.payStatus"
            placeholder="发放状态"
            style="width: 140px"
            clearable
          >
            <el-option label="待发放" value="PENDING" />
            <el-option label="已发放" value="PAID" />
            <el-option label="部分发放" value="PARTIAL" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="expropriationTableData" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="landPlot" label="征地地块" min-width="160" show-overflow-tooltip />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="area" label="面积(亩)" width="100" />
          <el-table-column prop="compensationStandard" label="补偿标准" width="140" />
          <el-table-column prop="compensationAmount" label="补偿金额(元)" width="140">
            <template #default="scope">
              <span class="amount-text">¥{{ scope.row.compensationAmount || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="payStatus" label="发放状态" width="100">
            <template #default="scope">
              <el-tag :type="getPayTagType(scope.row.payStatus)">
                {{ getPayStatusName(scope.row.payStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewExpropriation(scope.row)">查看</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="违建巡查" name="illegal">
        <div class="search-bar">
          <el-input
            v-model="searchForm.keyword"
            placeholder="按违建人/地点搜索"
            style="width: 220px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.rectifyStatus"
            placeholder="整改状态"
            style="width: 140px"
            clearable
          >
            <el-option label="未整改" value="NOT_RECTIFIED" />
            <el-option label="整改中" value="RECTIFYING" />
            <el-option label="已整改" value="RECTIFIED" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="illegalTableData" border :row-class-name="rowClassNameIllegal" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="location" label="违建地点" min-width="180" show-overflow-tooltip />
          <el-table-column prop="illegalPerson" label="违建人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="illegalArea" label="违建面积(㎡)" width="120" />
          <el-table-column prop="findTime" label="发现时间" width="120" />
          <el-table-column prop="rectifyStatus" label="整改状态" width="100">
            <template #default="scope">
              <el-tag :type="getRectifyTagType(scope.row.rectifyStatus)">
                {{ getRectifyStatusName(scope.row.rectifyStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewIllegal(scope.row)">查看</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
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

    <el-dialog v-model="addDialogVisible" title="新增审批" width="720px" class="add-dialog" :close-on-click-modal="false">
      <el-steps :active="activeStep" align-center finish-status="success" process-status="primary">
        <el-step title="基础信息填写" />
        <el-step title="地块照片上传" />
        <el-step title="提交村两委初审" />
      </el-steps>

      <div class="step-content">
        <div v-show="activeStep === 0">
          <el-form :model="addForm" label-width="110px">
            <el-form-item label="权属人">
              <el-input v-model="addForm.owner" placeholder="请输入权属人姓名" />
            </el-form-item>
            <el-form-item label="所属村组">
              <el-select v-model="addForm.groupName" placeholder="请选择村组" style="width: 100%">
                <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
              </el-select>
            </el-form-item>
            <el-form-item label="地块面积(㎡)">
              <el-input-number v-model="addForm.landArea" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
            <el-form-item label="房屋层数">
              <el-input-number v-model="addForm.houseFloors" :min="1" :max="10" />
            </el-form-item>
            <el-form-item label="地块位置">
              <el-input v-model="addForm.location" type="textarea" :rows="2" placeholder="请输入地块位置" />
            </el-form-item>
            <el-form-item label="申请用途">
              <el-select v-model="addForm.purpose" placeholder="请选择申请用途" style="width: 100%">
                <el-option label="住宅建设" value="住宅建设" />
                <el-option label="旧房翻建" value="旧房翻建" />
                <el-option label="加层扩建" value="加层扩建" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-form>
        </div>

        <div v-show="activeStep === 1">
          <div class="upload-section">
            <div class="upload-title">地块定位照片</div>
            <el-upload
              action="#"
              list-type="picture-card"
              :file-list="addForm.locationPhotos"
              :auto-upload="false"
              :on-change="handleLocationPhotoChange"
              :limit="5"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
            <div class="upload-tip">最多上传5张地块定位照片，支持JPG、PNG格式</div>
          </div>
          <div class="upload-section">
            <div class="upload-title">房屋现状照片</div>
            <el-upload
              action="#"
              list-type="picture-card"
              :file-list="addForm.housePhotos"
              :auto-upload="false"
              :on-change="handleHousePhotoChange"
              :limit="5"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
            <div class="upload-tip">最多上传5张房屋现状照片</div>
          </div>
        </div>

        <div v-show="activeStep === 2">
          <div class="submit-info">
            <el-descriptions :column="2" border size="small">
              <el-descriptions-item label="权属人">{{ addForm.owner }}</el-descriptions-item>
              <el-descriptions-item label="所属村组">{{ addForm.groupName }}</el-descriptions-item>
              <el-descriptions-item label="地块面积">{{ addForm.landArea }} ㎡</el-descriptions-item>
              <el-descriptions-item label="房屋层数">{{ addForm.houseFloors }} 层</el-descriptions-item>
              <el-descriptions-item label="申请用途" :span="2">{{ addForm.purpose }}</el-descriptions-item>
              <el-descriptions-item label="地块位置" :span="2">{{ addForm.location }}</el-descriptions-item>
            </el-descriptions>
            <el-alert
              title="提交后将进入村两委初审流程，初审通过后上报乡镇审批"
              type="warning"
              :closable="false"
              class="submit-alert"
              show-icon
            />
          </div>
        </div>
      </div>

      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button v-if="activeStep > 0" @click="prevStep">上一步</el-button>
        <el-button v-if="activeStep < 2" type="primary" @click="nextStep">下一步</el-button>
        <el-button v-if="activeStep === 2" type="primary" @click="handleSubmit">提交审批</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="详情" width="800px" class="detail-dialog">
      <div class="detail-cards">
        <div class="detail-card">
          <div class="card-title">地块定位照片</div>
          <div class="photo-grid">
            <div v-for="(img, idx) in currentDetail.locationPhotos || []" :key="idx" class="photo-item">
              <img :src="img" class="photo-img" @click="previewImage(img)" />
            </div>
            <div v-if="!currentDetail.locationPhotos || currentDetail.locationPhotos.length === 0" class="no-photo">
              暂无照片
            </div>
          </div>
        </div>

        <div class="detail-card">
          <div class="card-title">基础信息</div>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item v-if="currentDetail.owner" label="权属人">{{ currentDetail.owner }}</el-descriptions-item>
            <el-descriptions-item v-if="currentDetail.contractor" label="承包人">{{ currentDetail.contractor }}</el-descriptions-item>
            <el-descriptions-item label="村组">{{ currentDetail.groupName }}</el-descriptions-item>
            <el-descriptions-item v-if="currentDetail.landArea" label="地块面积">{{ currentDetail.landArea }} ㎡</el-descriptions-item>
            <el-descriptions-item v-if="currentDetail.muCount" label="亩数">{{ currentDetail.muCount }} 亩</el-descriptions-item>
            <el-descriptions-item v-if="currentDetail.confirmNo" label="确权编号">{{ currentDetail.confirmNo }}</el-descriptions-item>
            <el-descriptions-item v-if="currentDetail.houseFloors" label="房屋层数">{{ currentDetail.houseFloors }} 层</el-descriptions-item>
            <el-descriptions-item v-if="currentDetail.location" label="地块位置" :span="2">{{ currentDetail.location }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="detail-card" v-if="currentDetail.confirmCert">
          <div class="card-title">确权证书附件</div>
          <div class="cert-list">
            <a :href="currentDetail.confirmCert" target="_blank" class="cert-item">
              <el-icon><Document /></el-icon>
              <span>确权证书.pdf</span>
            </a>
          </div>
        </div>

        <div class="detail-card" v-if="currentDetail.rebuildRecords && currentDetail.rebuildRecords.length > 0">
          <div class="card-title">历年翻建记录</div>
          <el-table :data="currentDetail.rebuildRecords" border size="small">
            <el-table-column prop="year" label="年份" width="100" />
            <el-table-column prop="type" label="类型" width="120" />
            <el-table-column prop="beforeFloors" label="翻建前层数" width="120" />
            <el-table-column prop="afterFloors" label="翻建后层数" width="120" />
            <el-table-column prop="approver" label="审批人" width="100" />
          </el-table>
        </div>

        <div class="detail-card" v-if="currentDetail.overAreaRecords && currentDetail.overAreaRecords.length > 0">
          <div class="card-title">超占整改记录</div>
          <el-timeline>
            <el-timeline-item
              v-for="(record, idx) in currentDetail.overAreaRecords"
              :key="idx"
              :type="record.status === 'RECTIFIED' ? 'success' : 'danger'"
              :timestamp="record.time"
            >
              <div class="rectify-item">
                <div class="rectify-title">{{ record.title }}</div>
                <div class="rectify-desc">{{ record.description }}</div>
                <div class="rectify-status">
                  <el-tag size="small" :type="record.status === 'RECTIFIED' ? 'success' : 'danger'">
                    {{ record.status === 'RECTIFIED' ? '已整改' : '未整改' }}
                  </el-tag>
                </div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="imagePreviewVisible" width="600px" class="image-preview-dialog">
      <img :src="previewImageUrl" style="width: 100%; height: auto;" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Document } from '@element-plus/icons-vue'
import request from '../utils/request'

const activeTab = ref('homestead')
const homesteadTableData = ref([])
const cultivatedTableData = ref([])
const transferTableData = ref([])
const expropriationTableData = ref([])
const illegalTableData = ref([])
const addDialogVisible = ref(false)
const detailVisible = ref(false)
const imagePreviewVisible = ref(false)
const previewImageUrl = ref('')
const activeStep = ref(0)

const groupOptions = ref(['一组', '二组', '三组', '四组', '五组', '六组', '七组', '八组'])

const stats = reactive({
  homesteadCount: 0,
  cultivatedArea: 0,
  transferCount: 0,
  illegalCount: 0
})

const searchForm = reactive({
  keyword: '',
  groupName: '',
  approvalStatus: '',
  cropType: '',
  contractStatus: '',
  payStatus: '',
  rectifyStatus: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const addForm = reactive({
  owner: '',
  groupName: '',
  landArea: 0,
  houseFloors: 1,
  location: '',
  purpose: '',
  locationPhotos: [],
  housePhotos: []
})

const currentDetail = reactive({
  owner: '',
  contractor: '',
  groupName: '',
  landArea: 0,
  muCount: 0,
  confirmNo: '',
  houseFloors: 0,
  location: '',
  confirmCert: '',
  locationPhotos: [],
  rebuildRecords: [],
  overAreaRecords: []
})

const getApprovalStatusName = (status) => {
  const map = { PENDING: '待审批', APPROVED: '已审批', REJECTED: '已驳回' }
  return map[status] || '未知'
}

const getApprovalTagType = (status) => {
  const map = { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger' }
  return map[status] || 'info'
}

const getContractStatusName = (status) => {
  const map = { VALID: '有效', EXPIRED: '已到期', TERMINATED: '已终止' }
  return map[status] || '未知'
}

const getContractTagType = (status) => {
  const map = { VALID: 'success', EXPIRED: 'warning', TERMINATED: 'info' }
  return map[status] || 'info'
}

const getPayStatusName = (status) => {
  const map = { PENDING: '待发放', PAID: '已发放', PARTIAL: '部分发放' }
  return map[status] || '未知'
}

const getPayTagType = (status) => {
  const map = { PENDING: 'warning', PAID: 'success', PARTIAL: 'primary' }
  return map[status] || 'info'
}

const getRectifyStatusName = (status) => {
  const map = { NOT_RECTIFIED: '未整改', RECTIFYING: '整改中', RECTIFIED: '已整改' }
  return map[status] || '未知'
}

const getRectifyTagType = (status) => {
  const map = { NOT_RECTIFIED: 'danger', RECTIFYING: 'warning', RECTIFIED: 'success' }
  return map[status] || 'info'
}

const rowClassNameHomestead = ({ row }) => {
  if (row.isOverArea || row.approvalStatus !== 'APPROVED') return 'warning-row'
  return ''
}

const rowClassNameIllegal = ({ row }) => {
  if (row.rectifyStatus === 'NOT_RECTIFIED') return 'warning-row'
  return ''
}

const fetchStats = async () => {
  try {
    const res = await request.get('/land-house/stats')
    if (res.data) {
      stats.homesteadCount = res.data.homesteadCount || 0
      stats.cultivatedArea = res.data.cultivatedArea || 0
      stats.transferCount = res.data.transferCount || 0
      stats.illegalCount = res.data.illegalCount || 0
    }
  } catch (e) {
    stats.homesteadCount = 328
    stats.cultivatedArea = 2856
    stats.transferCount = 56
    stats.illegalCount = 12
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
    const res = await request.get('/land-house/list', { params: { ...params, tab: activeTab.value } })
    const records = res.data.records || []
    switch (activeTab.value) {
      case 'homestead':
        homesteadTableData.value = records
        break
      case 'cultivated':
        cultivatedTableData.value = records
        break
      case 'transfer':
        transferTableData.value = records
        break
      case 'expropriation':
        expropriationTableData.value = records
        break
      case 'illegal':
        illegalTableData.value = records
        break
    }
    pagination.total = res.data.total || 0
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
  searchForm.approvalStatus = ''
  searchForm.cropType = ''
  searchForm.contractStatus = ''
  searchForm.payStatus = ''
  searchForm.rectifyStatus = ''
  pagination.page = 1
  fetchData()
}

const handleAdd = () => {
  activeStep.value = 0
  Object.assign(addForm, {
    owner: '',
    groupName: '',
    landArea: 0,
    houseFloors: 1,
    location: '',
    purpose: '',
    locationPhotos: [],
    housePhotos: []
  })
  addDialogVisible.value = true
}

const nextStep = () => {
  if (activeStep.value === 0) {
    if (!addForm.owner) {
      ElMessage.warning('请输入权属人姓名')
      return
    }
    if (!addForm.groupName) {
      ElMessage.warning('请选择所属村组')
      return
    }
  }
  activeStep.value++
}

const prevStep = () => {
  activeStep.value--
}

const handleLocationPhotoChange = (file, fileList) => {
  addForm.locationPhotos = fileList
}

const handleHousePhotoChange = (file, fileList) => {
  addForm.housePhotos = fileList
}

const handleSubmit = async () => {
  try {
    await request.post('/land-house/add', addForm)
    ElMessage.success('提交审批成功')
    addDialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('提交失败')
  }
}

const handleEdit = (row) => {
  ElMessage.info('编辑功能开发中')
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/land-house/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
      fetchStats()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleViewHomestead = (row) => {
  Object.assign(currentDetail, {
    owner: row.owner,
    groupName: row.groupName,
    landArea: row.landArea,
    confirmNo: row.confirmNo,
    houseFloors: row.houseFloors,
    location: row.location,
    confirmCert: row.confirmCert || '',
    locationPhotos: row.locationPhotos || [],
    rebuildRecords: row.rebuildRecords || [
      { year: '2015', type: '加层', beforeFloors: '2层', afterFloors: '3层', approver: '村两委' }
    ],
    overAreaRecords: row.isOverArea ? [
      { title: '超占面积整改通知', description: `超占面积${row.overArea || 20}㎡，限期30日内整改`, status: 'NOT_RECTIFIED', time: '2024-06-15' }
    ] : []
  })
  detailVisible.value = true
}

const handleViewCultivated = (row) => {
  Object.assign(currentDetail, {
    contractor: row.contractor,
    groupName: row.groupName,
    muCount: row.muCount,
    location: row.location,
    locationPhotos: row.locationPhotos || []
  })
  detailVisible.value = true
}

const handleViewTransfer = (row) => {
  Object.assign(currentDetail, {
    owner: row.transferor + ' → ' + row.transferee,
    groupName: row.groupName,
    landArea: row.area * 666.67,
    location: row.location || '',
    locationPhotos: row.locationPhotos || []
  })
  detailVisible.value = true
}

const handleViewExpropriation = (row) => {
  Object.assign(currentDetail, {
    owner: row.landPlot,
    groupName: row.groupName,
    landArea: row.area * 666.67,
    location: row.landPlot,
    locationPhotos: row.locationPhotos || []
  })
  detailVisible.value = true
}

const handleViewIllegal = (row) => {
  Object.assign(currentDetail, {
    owner: row.illegalPerson,
    groupName: row.groupName,
    landArea: row.illegalArea,
    location: row.location,
    locationPhotos: row.locationPhotos || [],
    overAreaRecords: [
      { title: '违建查处通知', description: row.description || '未经审批擅自建房，责令停工整改', status: row.rectifyStatus === 'RECTIFIED' ? 'RECTIFIED' : 'NOT_RECTIFIED', time: row.findTime }
    ]
  })
  detailVisible.value = true
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

.amount-text {
  color: #C8102E;
  font-weight: bold;
}

.step-content {
  margin-top: 30px;
  min-height: 360px;
}

.upload-section {
  margin-bottom: 24px;
}

.upload-title {
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.submit-info {
  padding: 10px 0;
}

.submit-alert {
  margin-top: 20px;
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

.photo-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.photo-item {
  width: 120px;
  height: 120px;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  border: 1px solid #eee;
}

.photo-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-photo {
  color: #999;
  font-size: 13px;
  padding: 20px;
}

.cert-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.cert-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  background: #FDF5F5;
  border-radius: 6px;
  color: #C8102E;
  text-decoration: none;
}

.cert-item:hover {
  background: #FDE8E8;
}

.rectify-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.rectify-title {
  font-weight: bold;
  color: #C8102E;
}

.rectify-desc {
  font-size: 13px;
  color: #666;
}

.rectify-status {
  margin-top: 4px;
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

:deep(.el-step.is-primary .el-step__head.is-process) {
  color: #C8102E;
  border-color: #C8102E;
}

:deep(.el-step.is-primary .el-step__head.is-success) {
  color: #C8102E;
  border-color: #C8102E;
}

:deep(.el-descriptions__label) {
  background: #FDF5F5 !important;
  color: #C8102E;
  font-weight: bold;
}

:deep(.el-upload--picture-card) {
  border-color: #FDE8E8;
  background: #FDF5F5;
  color: #C8102E;
}

:deep(.el-upload--picture-card:hover) {
  border-color: #C8102E;
  color: #C8102E;
}
</style>
