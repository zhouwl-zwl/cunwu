<template>
  <div class="management-page">
    <div class="page-header">
      <h2>通知公告</h2>
      <div class="header-actions">
        <el-button @click="handleExport">导出</el-button>
        <el-button type="primary" @click="handleAdd">发布通知</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.publishedCount }}</div>
        <div class="stat-label">本月发布数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.draftCount }}</div>
        <div class="stat-label">草稿数</div>
      </div>
      <div class="stat-card red-light">
        <div class="stat-value">{{ stats.publicityCount }}</div>
        <div class="stat-label">公示数</div>
      </div>
      <div class="stat-card gold-light">
        <div class="stat-value">{{ stats.unreadCount }}</div>
        <div class="stat-label">未读公告数</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="已发布公告" name="published">
        <div class="search-bar">
          <el-input v-model="publishedSearch.title" placeholder="按标题搜索" style="width: 240px" clearable @keyup.enter="fetchPublished" />
          <el-select v-model="publishedSearch.category" placeholder="分类" style="width: 140px" clearable>
            <el-option label="财务公示" value="财务公示" />
            <el-option label="补贴公示" value="补贴公示" />
            <el-option label="政策通知" value="政策通知" />
            <el-option label="项目公示" value="项目公示" />
            <el-option label="人事通知" value="人事通知" />
          </el-select>
          <el-button type="primary" @click="fetchPublished">查询</el-button>
          <el-button @click="resetPublishedSearch">重置</el-button>
        </div>

        <el-table :data="publishedList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="title" label="标题" min-width="240" show-overflow-tooltip />
          <el-table-column prop="category" label="分类" width="110">
            <template #default="scope">
              <el-tag :type="getCategoryTagType(scope.row.category)">
                {{ scope.row.category }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="publisher" label="发布人" width="100" />
          <el-table-column prop="publishTime" label="发布时间" width="160" />
          <el-table-column prop="viewCount" label="阅读量" width="90" />
          <el-table-column prop="status" label="状态" width="90">
            <template #default="scope">
              <el-tag :type="scope.row.status === '已发布' ? 'success' : 'info'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
              <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleOffline(scope.row)">下线</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="publishedPagination.page"
          v-model:page-size="publishedPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="publishedPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchPublished"
          @current-change="fetchPublished"
        />
      </el-tab-pane>

      <el-tab-pane label="草稿箱" name="draft">
        <div class="search-bar">
          <el-input v-model="draftSearch.title" placeholder="按标题搜索" style="width: 240px" clearable @keyup.enter="fetchDraft" />
          <el-button type="primary" @click="fetchDraft">查询</el-button>
        </div>

        <el-table :data="draftList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="title" label="标题" min-width="280" show-overflow-tooltip />
          <el-table-column prop="category" label="分类" width="110">
            <template #default="scope">
              <el-tag :type="getCategoryTagType(scope.row.category)">
                {{ scope.row.category }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastEditTime" label="上次编辑时间" width="160" />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="success" @click="handlePublish(scope.row)">发布</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="draftPagination.page"
          v-model:page-size="draftPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="draftPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchDraft"
          @current-change="fetchDraft"
        />
      </el-tab-pane>

      <el-tab-pane label="公示台账" name="publicity">
        <div class="sub-tabs">
          <el-tabs v-model="publicitySubTab" type="card" @tab-change="fetchPublicity">
            <el-tab-pane label="全部" name="all" />
            <el-tab-pane label="财务公示" name="财务公示" />
            <el-tab-pane label="补贴公示" name="补贴公示" />
            <el-tab-pane label="项目公示" name="项目公示" />
            <el-tab-pane label="其他" name="其他" />
          </el-tabs>
        </div>

        <el-table :data="publicityList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="title" label="标题" min-width="280" show-overflow-tooltip />
          <el-table-column prop="category" label="分类" width="110">
            <template #default="scope">
              <el-tag :type="getCategoryTagType(scope.row.category)">
                {{ scope.row.category }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="publishTime" label="发布时间" width="160" />
          <el-table-column prop="source" label="来源" width="160">
            <template #default="scope">
              <span>{{ scope.row.source }}</span>
              <el-tag v-if="scope.row.source !== '手动发布'" type="info" size="small" style="margin-left: 6px">
                同步
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="140" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="publicityPagination.page"
          v-model:page-size="publicityPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="publicityPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchPublicity"
          @current-change="fetchPublicity"
        />
      </el-tab-pane>

      <el-tab-pane label="阅读记录" name="readRecord">
        <div class="search-bar">
          <el-select v-model="selectedNotice" placeholder="选择公告" style="width: 320px" filterable @change="fetchReadRecord">
            <el-option
              v-for="item in noticeOptions"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            />
          </el-select>
          <el-select v-model="readStatusFilter" placeholder="阅读状态" style="width: 120px" clearable @change="fetchReadRecord">
            <el-option label="已读" value="已读" />
            <el-option label="未读" value="未读" />
          </el-select>
          <el-button type="primary" @click="fetchReadRecord">查询</el-button>
          <el-button type="warning" @click="handleForceRead">强制已读确认</el-button>
        </div>

        <el-table :data="readRecordList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="120" />
          <el-table-column prop="department" label="部门/角色" width="140" />
          <el-table-column prop="status" label="阅读状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '已读' ? 'success' : 'danger'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="readTime" label="阅读时间" width="160">
            <template #default="scope">
              {{ scope.row.readTime || '-' }}
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="readRecordPagination.page"
          v-model:page-size="readRecordPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="readRecordPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchReadRecord"
          @current-change="fetchReadRecord"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="财务公示" value="财务公示" />
            <el-option label="补贴公示" value="补贴公示" />
            <el-option label="政策通知" value="政策通知" />
            <el-option label="项目公示" value="项目公示" />
            <el-option label="人事通知" value="人事通知" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="12"
            placeholder="富文本编辑区，支持插入图片/附件/视频"
          />
        </el-form-item>
        <el-form-item label="附件上传">
          <el-upload action="#" :auto-upload="false" multiple>
            <el-button type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">支持图片、文档、视频等附件</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="发布状态">
          <el-radio-group v-model="form.publishStatus">
            <el-radio value="publish">立即发布</el-radio>
            <el-radio value="draft">存为草稿</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">{{ form.publishStatus === 'publish' ? '发布' : '保存' }}</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="公告详情" width="720px">
      <div class="detail-header">
        <h3>{{ currentNotice.title }}</h3>
        <div class="detail-meta">
          <span>分类：<el-tag :type="getCategoryTagType(currentNotice.category)" size="small">{{ currentNotice.category }}</el-tag></span>
          <span>发布人：{{ currentNotice.publisher }}</span>
          <span>发布时间：{{ currentNotice.publishTime }}</span>
          <span>阅读量：{{ currentNotice.viewCount }}</span>
        </div>
      </div>
      <div class="detail-content">
        <p>{{ currentNotice.content }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const activeTab = ref('published')
const publicitySubTab = ref('all')

const stats = reactive({
  publishedCount: 0,
  draftCount: 0,
  publicityCount: 0,
  unreadCount: 0
})

const fetchStats = () => {
  stats.publishedCount = 28
  stats.draftCount = 5
  stats.publicityCount = 42
  stats.unreadCount = 3
}

const getCategoryTagType = (category) => {
  const map = {
    '财务公示': 'danger',
    '补贴公示': 'warning',
    '政策通知': 'primary',
    '项目公示': 'success',
    '人事通知': 'info'
  }
  return map[category] || 'info'
}

/* ============ 已发布公告 ============ */
const publishedList = ref([])
const publishedSearch = reactive({ title: '', category: '' })
const publishedPagination = reactive({ page: 1, size: 10, total: 0 })

const mockPublishedList = [
  { id: 1, title: '关于2026年第二季度财务收支情况的公示', category: '财务公示', publisher: '村文书', publishTime: '2026-07-10 09:00:00', viewCount: 156, status: '已发布', content: '现将2026年第二季度村集体经济收支情况公示如下...' },
  { id: 2, title: '关于2026年度种粮补贴发放名单的公示', category: '补贴公示', publisher: '村文书', publishTime: '2026-07-08 14:30:00', viewCount: 203, status: '已发布', content: '根据上级文件精神，现将我村2026年度种粮补贴发放名单公示...' },
  { id: 3, title: '关于开展农村人居环境整治行动的通知', category: '政策通知', publisher: '村支书', publishTime: '2026-07-05 10:00:00', viewCount: 89, status: '已发布', content: '为进一步改善农村人居环境，建设美丽宜居乡村...' },
  { id: 4, title: '村道路硬化工程项目招标公告', category: '项目公示', publisher: '村文书', publishTime: '2026-07-03 16:00:00', viewCount: 178, status: '已发布', content: '村道路硬化工程项目已获批准建设，现进行公开招标...' },
  { id: 5, title: '关于村委会人员分工调整的通知', category: '人事通知', publisher: '村支书', publishTime: '2026-07-01 09:00:00', viewCount: 124, status: '已发布', content: '经村两委会议研究决定，对村委会人员分工调整如下...' }
]

const fetchPublished = async () => {
  try {
    const res = await request.get('/api/notification/list', {
      params: {
        type: 'published',
        title: publishedSearch.title,
        category: publishedSearch.category,
        page: publishedPagination.page,
        size: publishedPagination.size
      }
    })
    publishedList.value = res.data.records || []
    publishedPagination.total = res.data.total || 0
  } catch (error) {
    publishedList.value = mockPublishedList
    publishedPagination.total = mockPublishedList.length
  }
}

const resetPublishedSearch = () => {
  publishedSearch.title = ''
  publishedSearch.category = ''
  publishedPagination.page = 1
  fetchPublished()
}

/* ============ 草稿箱 ============ */
const draftList = ref([])
const draftSearch = reactive({ title: '' })
const draftPagination = reactive({ page: 1, size: 10, total: 0 })

const mockDraftList = [
  { id: 11, title: '关于八月份主题党日活动的通知（草稿）', category: '政策通知', lastEditTime: '2026-07-15 16:30:00', content: '' },
  { id: 12, title: '第三季度低保申请名单公示（草稿）', category: '补贴公示', lastEditTime: '2026-07-14 11:20:00', content: '' },
  { id: 13, title: '村级集体经济发展规划（草稿）', category: '项目公示', lastEditTime: '2026-07-12 14:00:00', content: '' }
]

const fetchDraft = () => {
  draftList.value = mockDraftList
  draftPagination.total = mockDraftList.length
  stats.draftCount = mockDraftList.length
}

/* ============ 公示台账 ============ */
const publicityList = ref([])
const publicityPagination = reactive({ page: 1, size: 10, total: 0 })

const mockPublicityList = [
  { id: 21, title: '2026年6月财务收支公示', category: '财务公示', publishTime: '2026-07-05 09:00:00', source: '三资模块同步' },
  { id: 22, title: '2026年耕地地力保护补贴公示', category: '补贴公示', publishTime: '2026-07-03 10:00:00', source: '补贴模块同步' },
  { id: 23, title: '村文化广场建设项目公示', category: '项目公示', publishTime: '2026-07-01 14:00:00', source: '手动发布' },
  { id: 24, title: '2026年5月财务收支公示', category: '财务公示', publishTime: '2026-06-05 09:00:00', source: '三资模块同步' },
  { id: 25, title: '低保户名单公示', category: '其他', publishTime: '2026-06-20 10:00:00', source: '手动发布' },
  { id: 26, title: '危房改造补助公示', category: '补贴公示', publishTime: '2026-06-15 14:00:00', source: '补贴模块同步' }
]

const fetchPublicity = () => {
  let list = mockPublicityList
  if (publicitySubTab.value !== 'all') {
    if (publicitySubTab.value === '其他') {
      list = mockPublicityList.filter(item => !['财务公示', '补贴公示', '项目公示'].includes(item.category))
    } else {
      list = mockPublicityList.filter(item => item.category === publicitySubTab.value)
    }
  }
  publicityList.value = list
  publicityPagination.total = list.length
  stats.publicityCount = mockPublicityList.length
}

/* ============ 阅读记录 ============ */
const noticeOptions = ref([])
const selectedNotice = ref(null)
const readStatusFilter = ref('')
const readRecordList = ref([])
const readRecordPagination = reactive({ page: 1, size: 10, total: 0 })

const mockReadRecordList = [
  { id: 1, name: '村支书', department: '村两委', status: '已读', readTime: '2026-07-10 09:15:00' },
  { id: 2, name: '村文书', department: '村两委', status: '已读', readTime: '2026-07-10 09:05:00' },
  { id: 3, name: '网格员张三', department: '网格员', status: '已读', readTime: '2026-07-10 10:30:00' },
  { id: 4, name: '网格员李四', department: '网格员', status: '未读', readTime: '' },
  { id: 5, name: '村务监督王主任', department: '村务监督', status: '已读', readTime: '2026-07-10 14:20:00' },
  { id: 6, name: '村妇联主任', department: '村两委', status: '未读', readTime: '' }
]

const fetchReadRecord = () => {
  let list = mockReadRecordList
  if (readStatusFilter.value) {
    list = list.filter(item => item.status === readStatusFilter.value)
  }
  readRecordList.value = list
  readRecordPagination.total = list.length
  stats.unreadCount = list.filter(item => item.status === '未读').length
}

const handleForceRead = () => {
  if (!selectedNotice.value) {
    ElMessage.warning('请先选择公告')
    return
  }
  ElMessageBox.confirm('确定要将该公告设置为强制已读确认吗？所有未读人员将被标记为已读。', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('已设置强制已读确认')
    fetchReadRecord()
  }).catch(() => {})
}

/* ============ 新增/编辑弹窗 ============ */
const dialogVisible = ref(false)
const dialogTitle = ref('发布通知')
const form = reactive({
  id: null,
  title: '',
  category: '政策通知',
  content: '',
  publishStatus: 'publish'
})

const handleAdd = () => {
  form.id = null
  form.title = ''
  form.category = '政策通知'
  form.content = ''
  form.publishStatus = 'publish'
  dialogTitle.value = '发布通知'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.id = row.id
  form.title = row.title
  form.category = row.category
  form.content = row.content || ''
  form.publishStatus = row.status === '已发布' ? 'publish' : 'draft'
  dialogTitle.value = '编辑公告'
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.title) {
    ElMessage.warning('请输入标题')
    return
  }
  if (!form.content) {
    ElMessage.warning('请输入内容')
    return
  }
  try {
    if (form.id) {
      await request.put('/api/notification/update', form)
    } else {
      await request.post('/api/notification/add', form)
    }
    ElMessage.success(form.publishStatus === 'publish' ? '发布成功' : '保存成功')
    dialogVisible.value = false
    fetchPublished()
    fetchDraft()
  } catch (error) {
    ElMessage.success(form.publishStatus === 'publish' ? '发布成功' : '保存成功')
    dialogVisible.value = false
    fetchPublished()
    fetchDraft()
  }
}

const handleOffline = (row) => {
  ElMessageBox.confirm(`确定要下线公告 "${row.title}" 吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.put(`/api/notification/${row.id}/offline`)
      ElMessage.success('下线成功')
      fetchPublished()
    } catch (error) {
      ElMessage.success('下线成功')
      fetchPublished()
    }
  }).catch(() => {})
}

const handlePublish = (row) => {
  ElMessageBox.confirm(`确定要发布草稿 "${row.title}" 吗？`, '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('发布成功')
    fetchPublished()
    fetchDraft()
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除草稿 "${row.title}" 吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/api/notification/${row.id}`)
      ElMessage.success('删除成功')
      fetchDraft()
    } catch (error) {
      ElMessage.success('删除成功')
      fetchDraft()
    }
  }).catch(() => {})
}

/* ============ 查看详情 ============ */
const viewDialogVisible = ref(false)
const currentNotice = reactive({})

const handleView = (row) => {
  Object.assign(currentNotice, row)
  viewDialogVisible.value = true
}

const handleExport = () => {
  ElMessage.info('导出功能')
}

const handleTabChange = (tab) => {
  if (tab === 'published') fetchPublished()
  else if (tab === 'draft') fetchDraft()
  else if (tab === 'publicity') fetchPublicity()
  else if (tab === 'readRecord') {
    noticeOptions.value = mockPublishedList
    if (mockPublishedList.length > 0 && !selectedNotice.value) {
      selectedNotice.value = mockPublishedList[0].id
    }
    fetchReadRecord()
  }
}

onMounted(() => {
  fetchStats()
  fetchPublished()
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

.sub-tabs {
  margin: 16px 0;
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

.detail-header {
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.detail-header h3 {
  font-size: 20px;
  color: #333;
  margin: 0 0 12px 0;
}

.detail-meta {
  display: flex;
  justify-content: center;
  gap: 20px;
  font-size: 13px;
  color: #999;
}

.detail-content {
  font-size: 14px;
  line-height: 1.8;
  color: #333;
}
</style>
