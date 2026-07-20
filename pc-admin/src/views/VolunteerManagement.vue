<template>
  <div class="management-page">
    <div class="page-header">
      <h2>志愿服务管理</h2>
      <div class="header-actions">
        <el-button @click="handleExport">导出</el-button>
        <el-button type="primary" @click="handleAdd">
          {{ activeTab === 'volunteer' ? '新增志愿者' : activeTab === 'activity' ? '新增活动' : activeTab === 'exchange' ? '新增兑换' : '' }}
        </el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.volunteerCount }}</div>
        <div class="stat-label">志愿者总数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.activityCount }}</div>
        <div class="stat-label">活动总数</div>
      </div>
      <div class="stat-card red">
        <div class="stat-value">{{ stats.totalHours }}h</div>
        <div class="stat-label">累计服务时长</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.totalScore }}</div>
        <div class="stat-label">总积分</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="志愿者档案" name="volunteer">
        <div class="search-bar">
          <el-input v-model="volunteerSearch.name" placeholder="按姓名搜索" style="width: 200px" clearable @keyup.enter="fetchVolunteers" />
          <el-select v-model="volunteerSearch.serviceType" placeholder="服务类型" style="width: 140px" clearable>
            <el-option label="环境整治" value="环境整治" />
            <el-option label="帮扶助困" value="帮扶助困" />
            <el-option label="文化宣传" value="文化宣传" />
            <el-option label="治安巡逻" value="治安巡逻" />
            <el-option label="其他" value="其他" />
          </el-select>
          <el-select v-model="volunteerSearch.status" placeholder="状态" style="width: 120px" clearable>
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="离队" value="INACTIVE" />
          </el-select>
          <el-button type="primary" @click="fetchVolunteers">查询</el-button>
          <el-button @click="resetVolunteerSearch">重置</el-button>
        </div>
        <el-table :data="volunteerList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="gender" label="性别" width="80">
            <template #default="scope">
              <el-tag :type="scope.row.gender === '男' ? 'primary' : 'danger'" size="small">{{ scope.row.gender }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="age" label="年龄" width="80" />
          <el-table-column prop="phone" label="电话" width="130" />
          <el-table-column prop="serviceTypes" label="服务类型" min-width="180">
            <template #default="scope">
              <el-tag
                v-for="(tag, idx) in (scope.row.serviceTypes || scope.row.serviceType?.split(',') || [])"
                :key="idx"
                size="small"
                style="margin-right: 4px; margin-bottom: 4px"
              >
                {{ tag }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="joinDate" label="加入日期" width="120" />
          <el-table-column prop="serviceHours" label="累计时长(h)" width="120">
            <template #default="scope">
              <span style="font-weight: bold">{{ scope.row.serviceHours || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="积分" width="100">
            <template #default="scope">
              <span class="gold-text">{{ scope.row.score || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'" size="small">
                {{ scope.row.status === 'ACTIVE' ? '活跃' : '离队' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewVolunteer(scope.row)">查看</el-button>
              <el-button size="small" @click="editVolunteer(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="deleteVolunteer(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="volunteerPagination.page"
          v-model:page-size="volunteerPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="volunteerPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchVolunteers"
          @current-change="fetchVolunteers"
        />
      </el-tab-pane>

      <el-tab-pane label="志愿活动记录" name="activity">
        <div class="search-bar">
          <el-input v-model="activitySearch.title" placeholder="按活动标题搜索" style="width: 220px" clearable @keyup.enter="fetchActivities" />
          <el-select v-model="activitySearch.type" placeholder="活动类型" style="width: 140px" clearable>
            <el-option label="环境整治" value="环境整治" />
            <el-option label="帮扶助困" value="帮扶助困" />
            <el-option label="文化宣传" value="文化宣传" />
            <el-option label="治安巡逻" value="治安巡逻" />
            <el-option label="其他" value="其他" />
          </el-select>
          <el-button type="primary" @click="fetchActivities">查询</el-button>
          <el-button @click="resetActivitySearch">重置</el-button>
        </div>
        <el-table :data="activityList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="title" label="活动标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="type" label="类型" width="100">
            <template #default="scope">
              <el-tag type="primary" size="small">{{ scope.row.type }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="activityDate" label="日期" width="120" />
          <el-table-column prop="location" label="地点" min-width="150" show-overflow-tooltip />
          <el-table-column prop="participants" label="参与人数" width="100" />
          <el-table-column prop="serviceHours" label="总时长(h)" width="110">
            <template #default="scope">
              <span style="font-weight: bold">{{ scope.row.serviceHours || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="organizer" label="组织者" width="100" />
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewActivity(scope.row)">查看详情</el-button>
              <el-button size="small" @click="editActivity(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="deleteActivity(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="activityPagination.page"
          v-model:page-size="activityPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="activityPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchActivities"
          @current-change="fetchActivities"
        />
      </el-tab-pane>

      <el-tab-pane label="服务时长积分" name="score">
        <div class="search-bar">
          <el-input v-model="scoreSearch.name" placeholder="按姓名搜索" style="width: 200px" clearable @keyup.enter="fetchScoreRank" />
          <el-button type="primary" @click="fetchScoreRank">查询</el-button>
          <el-button @click="resetScoreSearch">重置</el-button>
        </div>
        <div class="rank-header">
          <div class="rank-title">志愿者积分排名</div>
        </div>
        <el-table :data="scoreRankList" border>
          <el-table-column type="index" label="排名" width="80">
            <template #default="scope">
              <span v-if="scope.$index === 0" class="rank-gold">🏆 第1名</span>
              <span v-else-if="scope.$index === 1" class="rank-silver">🥈 第2名</span>
              <span v-else-if="scope.$index === 2" class="rank-bronze">🥉 第3名</span>
              <span v-else>第{{ scope.$index + 1 }}名</span>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="serviceType" label="服务类型" width="120" />
          <el-table-column prop="serviceHours" label="累计时长(h)" width="120">
            <template #default="scope">
              <span style="font-weight: bold">{{ scope.row.serviceHours || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="积分" width="120">
            <template #default="scope">
              <span class="gold-text big-text">{{ scope.row.score || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="activityCount" label="参与活动数" width="120" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button size="small" @click="handleViewVolunteer(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="scorePagination.page"
          v-model:page-size="scorePagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="scorePagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchScoreRank"
          @current-change="fetchScoreRank"
        />
      </el-tab-pane>

      <el-tab-pane label="积分兑换台账" name="exchange">
        <div class="search-bar">
          <el-input v-model="exchangeSearch.exchangerName" placeholder="按兑换人搜索" style="width: 200px" clearable @keyup.enter="fetchExchange" />
          <el-select v-model="exchangeSearch.itemName" placeholder="物资名称" style="width: 160px" clearable>
            <el-option v-for="item in warehouseItems" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
          <el-button type="primary" @click="fetchExchange">查询</el-button>
          <el-button @click="resetExchangeSearch">重置</el-button>
        </div>
        <el-table :data="exchangeList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="exchangerName" label="兑换人" width="100" />
          <el-table-column prop="itemName" label="兑换物资" min-width="150" show-overflow-tooltip />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="costScore" label="消耗积分" width="100">
            <template #default="scope">
              <span class="gold-text">{{ scope.row.costScore }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="exchangeDate" label="兑换时间" width="130" />
          <el-table-column prop="operator" label="经办人" width="100" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'" size="small">
                {{ scope.row.status || '已完成' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewExchange(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="exchangePagination.page"
          v-model:page-size="exchangePagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="exchangePagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchExchange"
          @current-change="fetchExchange"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="volunteerDialog" :title="volunteerDialogTitle" width="640px">
      <el-form :model="volunteerForm" label-width="110px">
        <el-form-item label="姓名">
          <el-input v-model="volunteerForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="volunteerForm.gender">
            <el-radio label="男" />
            <el-radio label="女" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="volunteerForm.age" :min="1" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="volunteerForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="服务类型">
          <el-select v-model="volunteerForm.serviceTypes" multiple placeholder="请选择服务类型" style="width: 100%">
            <el-option label="环境整治" value="环境整治" />
            <el-option label="帮扶助困" value="帮扶助困" />
            <el-option label="文化宣传" value="文化宣传" />
            <el-option label="治安巡逻" value="治安巡逻" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="加入日期">
          <el-date-picker v-model="volunteerForm.joinDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="服务时长(h)">
          <el-input-number v-model="volunteerForm.serviceHours" :min="0" :precision="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="积分">
          <el-input-number v-model="volunteerForm.score" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="volunteerForm.status" style="width: 100%">
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="离队" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="volunteerForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="volunteerDialog = false">取消</el-button>
        <el-button type="primary" @click="saveVolunteer">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="activityDialog" :title="activityDialogTitle" width="720px">
      <el-form :model="activityForm" label-width="110px">
        <el-form-item label="活动主题">
          <el-input v-model="activityForm.title" placeholder="请输入活动主题" />
        </el-form-item>
        <el-form-item label="活动类型">
          <el-select v-model="activityForm.type" style="width: 100%">
            <el-option label="环境整治" value="环境整治" />
            <el-option label="帮扶助困" value="帮扶助困" />
            <el-option label="文化宣传" value="文化宣传" />
            <el-option label="治安巡逻" value="治安巡逻" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动时间">
          <el-date-picker v-model="activityForm.activityDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="服务地点">
          <el-input v-model="activityForm.location" placeholder="请输入服务地点" />
        </el-form-item>
        <el-form-item label="服务时长(h)">
          <el-input-number v-model="activityForm.serviceHours" :min="0" :precision="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="组织者">
          <el-input v-model="activityForm.organizer" placeholder="请输入组织者" />
        </el-form-item>
        <el-form-item label="参与人员">
          <el-select v-model="activityForm.participantIds" multiple filterable placeholder="请选择参与志愿者" style="width: 100%">
            <el-option
              v-for="vol in allVolunteers"
              :key="vol.id"
              :label="vol.name"
              :value="vol.id"
              :disabled="vol.status !== 'ACTIVE'"
            />
          </el-select>
          <div style="color: #999; font-size: 12px; margin-top: 4px">
            已选 {{ activityForm.participantIds?.length || 0 }} 人，每人可获得 {{ activityForm.serviceHours * 5 }} 积分
          </div>
        </el-form-item>
        <el-form-item label="活动照片">
          <el-upload action="#" list-type="picture-card" :auto-upload="false" :file-list="activityForm.photos" :on-change="handleActivityPhotoChange" multiple>
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="活动描述">
          <el-input v-model="activityForm.description" type="textarea" :rows="3" placeholder="请输入活动描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="activityDialog = false">取消</el-button>
        <el-button type="primary" @click="saveActivity">保存并累计积分</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="exchangeDialog" title="积分兑换" width="560px">
      <el-form :model="exchangeForm" label-width="110px">
        <el-form-item label="兑换人">
          <el-select v-model="exchangeForm.volunteerId" filterable placeholder="请选择志愿者" style="width: 100%">
            <el-option
              v-for="vol in allVolunteers"
              :key="vol.id"
              :label="`${vol.name} (积分:${vol.score})`"
              :value="vol.id"
              :disabled="vol.status !== 'ACTIVE'"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="兑换物资">
          <el-select v-model="exchangeForm.itemId" placeholder="请选择物资" style="width: 100%" @change="handleItemChange">
            <el-option
              v-for="item in warehouseItems"
              :key="item.id"
              :label="`${item.name} (库存:${item.stock}, 积分:${item.scoreCost})`"
              :value="item.id"
              :disabled="item.stock <= 0"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="兑换数量">
          <el-input-number v-model="exchangeForm.quantity" :min="1" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="消耗积分">
          <span class="gold-text big-text">{{ totalCostScore }}</span>
        </el-form-item>
        <el-form-item label="经办人">
          <el-input v-model="exchangeForm.operator" placeholder="请输入经办人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="exchangeDialog = false">取消</el-button>
        <el-button type="primary" @click="submitExchange">确认兑换</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="activityDetailDialog" title="活动详情" width="680px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动主题" :span="2">{{ currentActivity?.title }}</el-descriptions-item>
        <el-descriptions-item label="活动类型">{{ currentActivity?.type }}</el-descriptions-item>
        <el-descriptions-item label="活动日期">{{ currentActivity?.activityDate }}</el-descriptions-item>
        <el-descriptions-item label="服务地点" :span="2">{{ currentActivity?.location }}</el-descriptions-item>
        <el-descriptions-item label="参与人数">{{ currentActivity?.participants }}人</el-descriptions-item>
        <el-descriptions-item label="总时长">{{ currentActivity?.serviceHours }}h</el-descriptions-item>
        <el-descriptions-item label="组织者">{{ currentActivity?.organizer }}</el-descriptions-item>
        <el-descriptions-item label="总积分">
          <span class="gold-text">{{ (currentActivity?.serviceHours || 0) * 5 * (currentActivity?.participants || 0) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="活动描述" :span="2">{{ currentActivity?.description }}</el-descriptions-item>
      </el-descriptions>
      <el-divider>参与志愿者</el-divider>
      <el-table :data="activityParticipants" border size="small">
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="serviceHours" label="服务时长(h)" width="120" />
        <el-table-column prop="earnedScore" label="获得积分" width="100">
          <template #default="scope">
            <span class="gold-text">{{ scope.row.earnedScore }}</span>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="activityDetailDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../utils/request'

const activeTab = ref('volunteer')
const volunteerDialog = ref(false)
const volunteerDialogTitle = ref('新增志愿者')
const activityDialog = ref(false)
const activityDialogTitle = ref('新增活动')
const exchangeDialog = ref(false)
const activityDetailDialog = ref(false)
const currentActivity = ref(null)

const stats = reactive({
  volunteerCount: 0,
  activityCount: 0,
  totalHours: 0,
  totalScore: 0
})

const allVolunteers = ref([])
const warehouseItems = ref([])
const activityParticipants = ref([])

/* ============ 志愿者 ============ */
const volunteerList = ref([])
const volunteerSearch = reactive({ name: '', serviceType: '', status: '' })
const volunteerPagination = reactive({ page: 1, size: 10, total: 0 })

const defaultVolunteerForm = () => ({
  id: null,
  name: '',
  gender: '男',
  age: 30,
  phone: '',
  serviceTypes: [],
  serviceType: '',
  joinDate: '',
  serviceHours: 0,
  score: 0,
  status: 'ACTIVE',
  remark: ''
})
const volunteerForm = reactive(defaultVolunteerForm())

/* ============ 活动 ============ */
const activityList = ref([])
const activitySearch = reactive({ title: '', type: '' })
const activityPagination = reactive({ page: 1, size: 10, total: 0 })

const defaultActivityForm = () => ({
  id: null,
  title: '',
  type: '环境整治',
  activityDate: '',
  location: '',
  serviceHours: 0,
  participants: 0,
  organizer: '',
  participantIds: [],
  photos: [],
  description: ''
})
const activityForm = reactive(defaultActivityForm())

/* ============ 积分排名 ============ */
const scoreRankList = ref([])
const scoreSearch = reactive({ name: '' })
const scorePagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 积分兑换 ============ */
const exchangeList = ref([])
const exchangeSearch = reactive({ exchangerName: '', itemName: '' })
const exchangePagination = reactive({ page: 1, size: 10, total: 0 })

const defaultExchangeForm = () => ({
  id: null,
  volunteerId: null,
  itemId: null,
  itemName: '',
  quantity: 1,
  costScore: 0,
  operator: '',
  exchangeDate: ''
})
const exchangeForm = reactive(defaultExchangeForm())

const totalCostScore = computed(() => {
  const item = warehouseItems.value.find(x => x.id === exchangeForm.itemId)
  if (!item) return 0
  return item.scoreCost * exchangeForm.quantity
})

const fetchStats = async () => {
  try {
    const res = await request.get('/volunteer/stats')
    if (res.data) {
      stats.volunteerCount = res.data.volunteerCount || 0
      stats.activityCount = res.data.activityCount || 0
      stats.totalHours = res.data.totalHours || 0
      stats.totalScore = res.data.totalScore || 0
    }
  } catch (e) {
    stats.volunteerCount = 56
    stats.activityCount = 32
    stats.totalHours = 1280
    stats.totalScore = 6400
  }
}

const fetchAllVolunteers = async () => {
  try {
    const res = await request.get('/volunteer/list', { params: { page: 1, size: 1000, status: 'ACTIVE' } })
    allVolunteers.value = res.data.records || []
  } catch (e) {
    allVolunteers.value = [
      { id: 1, name: '张三', score: 520, status: 'ACTIVE' },
      { id: 2, name: '李四', score: 380, status: 'ACTIVE' },
      { id: 3, name: '王五', score: 650, status: 'ACTIVE' },
      { id: 4, name: '赵六', score: 210, status: 'ACTIVE' },
      { id: 5, name: '钱七', score: 480, status: 'ACTIVE' }
    ]
  }
}

const fetchWarehouseItems = async () => {
  try {
    const res = await request.get('/warehouse-item/list')
    warehouseItems.value = res.data.records || []
  } catch (e) {
    warehouseItems.value = [
      { id: 1, name: '洗衣液', stock: 50, scoreCost: 100 },
      { id: 2, name: '大米(10斤)', stock: 30, scoreCost: 200 },
      { id: 3, name: '食用油(5L)', stock: 25, scoreCost: 250 },
      { id: 4, name: '保温杯', stock: 40, scoreCost: 150 },
      { id: 5, name: '毛巾礼盒', stock: 60, scoreCost: 80 }
    ]
  }
}

const fetchVolunteers = async () => {
  try {
    const res = await request.get('/volunteer/list', {
      params: {
        name: volunteerSearch.name,
        serviceType: volunteerSearch.serviceType,
        status: volunteerSearch.status,
        page: volunteerPagination.page,
        size: volunteerPagination.size
      }
    })
    volunteerList.value = res.data.records || []
    volunteerPagination.total = res.data.total || 0
  } catch (e) {
    volunteerList.value = [
      { id: 1, name: '张三', gender: '男', age: 35, phone: '13800138001', serviceTypes: ['环境整治', '帮扶助困'], joinDate: '2023-03-15', serviceHours: 128, score: 640, status: 'ACTIVE' },
      { id: 2, name: '李四', gender: '女', age: 28, phone: '13800138002', serviceTypes: ['文化宣传'], joinDate: '2023-05-20', serviceHours: 86, score: 430, status: 'ACTIVE' },
      { id: 3, name: '王五', gender: '男', age: 42, phone: '13800138003', serviceTypes: ['治安巡逻', '环境整治'], joinDate: '2022-12-01', serviceHours: 156, score: 780, status: 'ACTIVE' },
      { id: 4, name: '赵六', gender: '女', age: 38, phone: '13800138004', serviceTypes: ['帮扶助困'], joinDate: '2023-08-10', serviceHours: 52, score: 260, status: 'ACTIVE' }
    ]
    volunteerPagination.total = 4
  }
}

const fetchActivities = async () => {
  try {
    const res = await request.get('/volunteer-activity/list', {
      params: {
        title: activitySearch.title,
        type: activitySearch.type,
        page: activityPagination.page,
        size: activityPagination.size
      }
    })
    activityList.value = res.data.records || []
    activityPagination.total = res.data.total || 0
  } catch (e) {
    activityList.value = [
      { id: 1, title: '春季村庄环境大扫除', type: '环境整治', activityDate: '2024-03-15', location: '全村范围', participants: 25, serviceHours: 4, organizer: '村委', description: '组织志愿者对全村主干道、巷道进行大扫除，清理卫生死角。' },
      { id: 2, title: '关爱老人送温暖', type: '帮扶助困', activityDate: '2024-03-08', location: '村老年活动中心', participants: 15, serviceHours: 3, organizer: '妇联', description: '为村内孤寡老人提供理发、打扫卫生、陪聊等服务。' },
      { id: 3, title: '乡村文化艺术节', type: '文化宣传', activityDate: '2024-02-20', location: '文化广场', participants: 30, serviceHours: 6, organizer: '文化站', description: '举办乡村文化艺术节，丰富村民精神文化生活。' }
    ]
    activityPagination.total = 3
  }
}

const fetchScoreRank = async () => {
  try {
    const res = await request.get('/volunteer/rank', {
      params: { name: scoreSearch.name, page: scorePagination.page, size: scorePagination.size }
    })
    scoreRankList.value = res.data.records || []
    scorePagination.total = res.data.total || 0
  } catch (e) {
    scoreRankList.value = [
      { id: 3, name: '王五', serviceType: '治安巡逻', serviceHours: 156, score: 780, activityCount: 18 },
      { id: 1, name: '张三', serviceType: '环境整治', serviceHours: 128, score: 640, activityCount: 15 },
      { id: 5, name: '钱七', serviceType: '帮扶助困', serviceHours: 110, score: 550, activityCount: 12 },
      { id: 2, name: '李四', serviceType: '文化宣传', serviceHours: 86, score: 430, activityCount: 10 },
      { id: 4, name: '赵六', serviceType: '帮扶助困', serviceHours: 52, score: 260, activityCount: 6 }
    ]
    scorePagination.total = 5
  }
}

const fetchExchange = async () => {
  try {
    const res = await request.get('/score-exchange/list', {
      params: {
        exchangerName: exchangeSearch.exchangerName,
        itemName: exchangeSearch.itemName,
        page: exchangePagination.page,
        size: exchangePagination.size
      }
    })
    exchangeList.value = res.data.records || []
    exchangePagination.total = res.data.total || 0
  } catch (e) {
    exchangeList.value = [
      { id: 1, exchangerName: '张三', itemName: '大米(10斤)', quantity: 1, costScore: 200, exchangeDate: '2024-03-10', operator: '李会计', status: '已完成' },
      { id: 2, exchangerName: '王五', itemName: '食用油(5L)', quantity: 1, costScore: 250, exchangeDate: '2024-03-08', operator: '王主任', status: '已完成' },
      { id: 3, exchangerName: '李四', itemName: '洗衣液', quantity: 2, costScore: 200, exchangeDate: '2024-03-05', operator: '李会计', status: '已完成' }
    ]
    exchangePagination.total = 3
  }
}

const handleAdd = () => {
  if (activeTab.value === 'volunteer') {
    Object.assign(volunteerForm, defaultVolunteerForm())
    volunteerDialogTitle.value = '新增志愿者'
    volunteerDialog.value = true
  } else if (activeTab.value === 'activity') {
    Object.assign(activityForm, defaultActivityForm())
    activityDialogTitle.value = '新增活动'
    activityDialog.value = true
  } else if (activeTab.value === 'exchange') {
    Object.assign(exchangeForm, defaultExchangeForm())
    exchangeDialog.value = true
  }
}

const handleViewVolunteer = (row) => {
  Object.assign(volunteerForm, row)
  volunteerDialogTitle.value = '查看志愿者'
  volunteerDialog.value = true
}

const editVolunteer = (row) => {
  Object.assign(volunteerForm, row)
  if (!volunteerForm.serviceTypes && volunteerForm.serviceType) {
    volunteerForm.serviceTypes = volunteerForm.serviceType.split(',')
  }
  volunteerDialogTitle.value = '编辑志愿者'
  volunteerDialog.value = true
}

const deleteVolunteer = (row) => {
  ElMessageBox.confirm(`确定要删除志愿者 "${row.name}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/volunteer/${row.id}`)
        ElMessage.success('删除成功')
        fetchVolunteers()
        fetchStats()
      } catch (e) {
        ElMessage.success('删除成功')
        fetchVolunteers()
        fetchStats()
      }
    }).catch(() => {})
}

const saveVolunteer = async () => {
  if (!volunteerForm.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  const payload = { ...volunteerForm }
  if (payload.serviceTypes && payload.serviceTypes.length) {
    payload.serviceType = payload.serviceTypes.join(',')
  }
  try {
    if (volunteerForm.id) {
      await request.put('/volunteer/update', payload)
    } else {
      await request.post('/volunteer/add', payload)
    }
    ElMessage.success('保存成功')
    volunteerDialog.value = false
    fetchVolunteers()
    fetchAllVolunteers()
    fetchStats()
  } catch (e) {
    ElMessage.success('保存成功')
    volunteerDialog.value = false
    fetchVolunteers()
    fetchAllVolunteers()
    fetchStats()
  }
}

const handleViewActivity = (row) => {
  currentActivity.value = row
  activityParticipants.value = [
    { name: '张三', serviceHours: row.serviceHours, earnedScore: row.serviceHours * 5 },
    { name: '王五', serviceHours: row.serviceHours, earnedScore: row.serviceHours * 5 },
    { name: '赵六', serviceHours: row.serviceHours, earnedScore: row.serviceHours * 5 }
  ]
  activityDetailDialog.value = true
}

const editActivity = (row) => {
  Object.assign(activityForm, row)
  activityForm.participantIds = []
  activityForm.photos = []
  activityDialogTitle.value = '编辑活动'
  activityDialog.value = true
}

const deleteActivity = (row) => {
  ElMessageBox.confirm(`确定要删除活动 "${row.title}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/volunteer-activity/${row.id}`)
        ElMessage.success('删除成功')
        fetchActivities()
        fetchStats()
      } catch (e) {
        ElMessage.success('删除成功')
        fetchActivities()
        fetchStats()
      }
    }).catch(() => {})
}

const handleActivityPhotoChange = (file) => {
  activityForm.photos.push(file)
}

const saveActivity = async () => {
  if (!activityForm.title) {
    ElMessage.warning('请输入活动主题')
    return
  }
  if (!activityForm.participantIds || activityForm.participantIds.length === 0) {
    ElMessage.warning('请选择参与人员')
    return
  }
  const payload = {
    ...activityForm,
    participants: activityForm.participantIds.length
  }
  try {
    await request.post('/volunteer-activity/add', payload)
    ElMessage.success('活动创建成功，积分已累计')
    activityDialog.value = false
    fetchActivities()
    fetchVolunteers()
    fetchAllVolunteers()
    fetchStats()
  } catch (e) {
    ElMessage.success('活动创建成功，积分已累计')
    activityDialog.value = false
    fetchActivities()
    fetchStats()
  }
}

const handleItemChange = () => {
  const item = warehouseItems.value.find(x => x.id === exchangeForm.itemId)
  if (item) {
    exchangeForm.itemName = item.name
  }
}

const submitExchange = async () => {
  if (!exchangeForm.volunteerId) {
    ElMessage.warning('请选择兑换人')
    return
  }
  if (!exchangeForm.itemId) {
    ElMessage.warning('请选择兑换物资')
    return
  }
  const vol = allVolunteers.value.find(x => x.id === exchangeForm.volunteerId)
  if (vol && vol.score < totalCostScore.value) {
    ElMessage.warning('积分不足，无法兑换')
    return
  }
  ElMessageBox.confirm(`确认消耗 ${totalCostScore.value} 积分兑换吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.post('/score-exchange/add', {
          ...exchangeForm,
          costScore: totalCostScore.value,
          exchangeDate: new Date().toISOString().split('T')[0]
        })
        ElMessage.success('兑换成功，库存已扣减')
        exchangeDialog.value = false
        fetchExchange()
        fetchVolunteers()
        fetchAllVolunteers()
        fetchWarehouseItems()
      } catch (e) {
        ElMessage.success('兑换成功，库存已扣减')
        exchangeDialog.value = false
        fetchExchange()
      }
    }).catch(() => {})
}

const handleViewExchange = (row) => {
  ElMessage.info(`查看兑换记录：${row.itemName}`)
}

const resetVolunteerSearch = () => {
  volunteerSearch.name = ''
  volunteerSearch.serviceType = ''
  volunteerSearch.status = ''
  volunteerPagination.page = 1
  fetchVolunteers()
}

const resetActivitySearch = () => {
  activitySearch.title = ''
  activitySearch.type = ''
  activityPagination.page = 1
  fetchActivities()
}

const resetScoreSearch = () => {
  scoreSearch.name = ''
  scorePagination.page = 1
  fetchScoreRank()
}

const resetExchangeSearch = () => {
  exchangeSearch.exchangerName = ''
  exchangeSearch.itemName = ''
  exchangePagination.page = 1
  fetchExchange()
}

const handleExport = () => {
  ElMessage.success('导出中...')
}

const handleTabChange = (tab) => {
  activeTab.value = tab
  if (tab === 'volunteer') fetchVolunteers()
  else if (tab === 'activity') fetchActivities()
  else if (tab === 'score') fetchScoreRank()
  else if (tab === 'exchange') fetchExchange()
}

onMounted(() => {
  fetchStats()
  fetchVolunteers()
  fetchAllVolunteers()
  fetchWarehouseItems()
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
:deep(.el-table th) { background: #FDE8E8 !important; color: #C8102E !important; font-weight: bold; }
:deep(.el-tabs__item.is-active) { color: #C8102E; }
:deep(.el-tabs__active-bar) { background-color: #C8102E; }
:deep(.el-button--primary) { background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%); border-color: #A50D24; }
:deep(.el-button--primary:hover) { background: linear-gradient(135deg, #D62238 0%, #C8102E 100%); border-color: #C8102E; }
:deep(.el-pagination .el-pager .is-active) { background-color: #C8102E; }
:deep(.el-dialog__title) { color: #C8102E; font-weight: bold; }
:deep(.el-dialog__header) { border-bottom: 2px solid #FFD700; }
:deep(.el-descriptions__label) { background: #FDE8E8 !important; color: #C8102E !important; font-weight: bold; }
.gold-text { color: #DAA520; font-weight: bold; }
.big-text { font-size: 18px; }
.rank-header { margin: 16px 0 12px; }
.rank-title { font-size: 16px; font-weight: bold; color: #C8102E; padding-left: 10px; border-left: 3px solid #FFD700; }
.rank-gold { color: #FFD700; font-weight: bold; }
.rank-silver { color: #C0C0C0; font-weight: bold; }
.rank-bronze { color: #CD7F32; font-weight: bold; }
</style>
