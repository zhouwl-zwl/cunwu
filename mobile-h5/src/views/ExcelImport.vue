<template>
  <div class="page-container excel-import">
    <van-nav-bar
      title="表格查看"
      left-text="返回"
      left-arrow
      :right-text="unlocked ? '锁定' : ''"
      @click-left="onBack"
      @click-right="lock"
      :border="false"
    />
    <div class="page-body">
      <!-- 密码门 -->
      <div v-if="!unlocked" class="lock-card">
        <div class="lock-icon">🔒</div>
        <div class="lock-title">受保护的表格功能</div>
        <div class="lock-desc">导入 Excel 前请输入访问密码</div>
        <van-field
          v-model="password"
          :type="showPwd ? 'text' : 'password'"
          center
          clearable
          placeholder="请输入密码"
          @keyup.enter="unlock"
        >
          <template #right-icon>
            <van-icon :name="showPwd ? 'eye-o' : 'closed-eye'" @click="showPwd = !showPwd" />
          </template>
        </van-field>
        <div v-if="error" class="lock-error">密码错误，请重试</div>
        <van-button
          type="danger"
          block
          round
          class="unlock-btn"
          :loading="checking"
          @click="unlock"
        >解锁</van-button>
      </div>

      <template v-else>
        <!-- 未选择文件 -->
        <div v-if="!headers.length" class="import-card">
          <input
            ref="fileInputRef"
            type="file"
            accept=".xlsx,.xls,.csv"
            hidden
            @change="onFileChange"
          />
          <div class="upload-box" @click="chooseFile">
            <van-icon name="description" size="40" color="#C8102E" />
            <div class="upload-title">{{ parsing ? '正在解析…' : '点击选择 Excel 文件' }}</div>
            <div class="upload-tip">支持 .xlsx / .xls / .csv</div>
            <div class="upload-tip">文件仅在本地浏览器解析，不会上传到服务器</div>
          </div>
        </div>

        <!-- 展示区 -->
        <template v-else>
          <div class="result-bar">
            <div class="result-info">
              共 {{ filteredRows.length }} 行 × {{ headers.length }} 列
              <span v-if="keyword" class="filtered-tag">（已筛选）</span>
            </div>
            <van-button size="small" icon="replay" @click="resetFile">重新导入</van-button>
          </div>

          <van-search v-model="keyword" placeholder="搜索表格内容" />

          <div v-if="sheetNames.length > 1" class="sheet-tabs">
            <span
              v-for="name in sheetNames"
              :key="name"
              class="sheet-tab"
              :class="{ active: name === activeSheet }"
              @click="switchSheet(name)"
            >{{ name }}</span>
          </div>

          <div v-if="filteredRows.length" class="table-wrap">
            <table class="data-table">
              <thead>
                <tr>
                  <th class="index-col">#</th>
                  <th v-for="(h, i) in headers" :key="i">{{ h }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(row, ri) in pageRows" :key="ri">
                  <td class="index-col">{{ (page - 1) * PAGE_SIZE + ri + 1 }}</td>
                  <td v-for="(h, ci) in headers" :key="ci">{{ row[ci] }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <van-empty v-else description="没有匹配的数据" />

          <van-pagination
            v-if="filteredRows.length > PAGE_SIZE"
            v-model="page"
            :total-items="filteredRows.length"
            :items-per-page="PAGE_SIZE"
            :show-page-size="3"
            force-ellipses
          />
        </template>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import * as XLSX from 'xlsx'
import { showToast } from 'vant'
import { sha256Hex } from '@/utils/sha256'

// ==================== 密码配置 ====================
// 默认密码：cunwu2026
// 修改密码：用 Node 执行
//   node -e "console.log(require('crypto').createHash('sha256').update('新密码','utf8').digest('hex'))"
// 把输出的十六进制字符串替换下面的 PASSWORD_HASH 即可。
const PASSWORD_HASH = '761f91931ce7c170ec8f00741e36b5de0a12b2a535180a155cf5ee6820bb69eb'
// ==================================================

const UNLOCK_KEY = 'excel_import_unlocked'
const PAGE_SIZE = 20

const router = useRouter()

// 密码门状态
const unlocked = ref(false)
const password = ref('')
const showPwd = ref(false)
const checking = ref(false)
const error = ref(false)

// 表格状态
const fileInputRef = ref(null)
const parsing = ref(false)
const sheetNames = ref([])
const activeSheet = ref('')
let workbook = null
const headers = ref([])
const tableRows = ref([])
const keyword = ref('')
const page = ref(1)

const filteredRows = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return tableRows.value
  return tableRows.value.filter(row =>
    row.some(cell => String(cell).toLowerCase().includes(kw))
  )
})

const pageRows = computed(() => {
  const start = (page.value - 1) * PAGE_SIZE
  return filteredRows.value.slice(start, start + PAGE_SIZE)
})

watch(keyword, () => { page.value = 1 })
watch(filteredRows, (rows) => {
  const maxPage = Math.max(1, Math.ceil(rows.length / PAGE_SIZE))
  if (page.value > maxPage) page.value = maxPage
})

const chooseFile = () => {
  if (parsing.value) return
  fileInputRef.value && fileInputRef.value.click()
}

const onFileChange = async (e) => {
  const file = e.target.files && e.target.files[0]
  if (!file) return
  parsing.value = true
  try {
    const buf = await file.arrayBuffer()
    workbook = XLSX.read(new Uint8Array(buf), { type: 'array' })
    sheetNames.value = workbook.SheetNames
    loadSheet(workbook.SheetNames[0])
    showToast(`已导入：${file.name}`)
  } catch (err) {
    console.error(err)
    showToast('文件解析失败，请确认是有效的 Excel 文件')
  } finally {
    parsing.value = false
    e.target.value = ''
  }
}

const loadSheet = (name) => {
  const ws = workbook.Sheets[name]
  // header:1 → 二维数组（第 0 行为表头），raw:false → 日期等按显示文本输出
  const matrix = XLSX.utils.sheet_to_json(ws, { header: 1, defval: '', raw: false })
  const rows = matrix.filter(r => r.some(c => String(c).trim() !== ''))
  headers.value = rows.length
    ? rows[0].map((h, i) => String(h).trim() || `列${i + 1}`)
    : []
  tableRows.value = rows.slice(1)
  activeSheet.value = name
  page.value = 1
}

const switchSheet = (name) => {
  if (name === activeSheet.value) return
  keyword.value = ''
  loadSheet(name)
}

const resetFile = () => {
  workbook = null
  sheetNames.value = []
  activeSheet.value = ''
  headers.value = []
  tableRows.value = []
  keyword.value = ''
  page.value = 1
}

const unlock = () => {
  if (!password.value) {
    showToast('请输入密码')
    return
  }
  checking.value = true
  error.value = false
  setTimeout(() => {
    if (sha256Hex(password.value) === PASSWORD_HASH) {
      unlocked.value = true
      sessionStorage.setItem(UNLOCK_KEY, '1')
      showToast('解锁成功')
    } else {
      error.value = true
      showToast('密码错误')
    }
    password.value = ''
    checking.value = false
  }, 250)
}

const lock = () => {
  unlocked.value = false
  sessionStorage.removeItem(UNLOCK_KEY)
  resetFile()
}

const onBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/')
  }
}

onMounted(() => {
  unlocked.value = sessionStorage.getItem(UNLOCK_KEY) === '1'
})
</script>

<style scoped>
.excel-import {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #f5f5f5;
}

.page-body {
  padding: 0 12px;
}

/* 密码门 */
.lock-card {
  margin-top: 40px;
  background: #fff;
  border-radius: 12px;
  padding: 32px 20px;
  text-align: center;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.lock-icon {
  font-size: 44px;
  margin-bottom: 12px;
}

.lock-title {
  font-size: 16px;
  font-weight: 700;
  color: #333;
}

.lock-desc {
  font-size: 13px;
  color: #999;
  margin: 6px 0 20px;
}

.lock-error {
  color: #ee0a24;
  font-size: 12px;
  margin-top: 8px;
}

.unlock-btn {
  margin-top: 16px;
}

/* 上传区 */
.import-card {
  margin-top: 12px;
}

.upload-box {
  background: #fff;
  border: 1px dashed #d0d0d0;
  border-radius: 12px;
  padding: 36px 16px;
  text-align: center;
  cursor: pointer;
}

.upload-box:active {
  background: #fafafa;
}

.upload-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-top: 10px;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

/* 结果栏 */
.result-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-radius: 10px;
  padding: 10px 14px;
  margin-bottom: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.result-info {
  font-size: 13px;
  color: #666;
}

.filtered-tag {
  color: #C8102E;
}

/* sheet 切换 */
.sheet-tabs {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  padding-bottom: 6px;
  margin-bottom: 4px;
}

.sheet-tab {
  flex-shrink: 0;
  padding: 5px 14px;
  border-radius: 14px;
  background: #fff;
  font-size: 12px;
  color: #666;
  border: 1px solid #eee;
}

.sheet-tab.active {
  background: #C8102E;
  color: #fff;
  border-color: #C8102E;
  font-weight: 600;
}

/* 数据表 */
.table-wrap {
  background: #fff;
  border-radius: 10px;
  overflow: auto;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
  max-height: calc(100vh - 300px);
}

.data-table {
  border-collapse: collapse;
  width: 100%;
}

.data-table th,
.data-table td {
  border: 1px solid #f0f0f0;
  padding: 8px 10px;
  font-size: 12px;
  color: #333;
  white-space: nowrap;
  text-align: left;
}

.data-table th {
  position: sticky;
  top: 0;
  background: linear-gradient(135deg, #C8102E 0%, #A80E28 100%);
  color: #fff;
  font-weight: 600;
  z-index: 1;
}

.data-table tbody tr:nth-child(even) {
  background: #fafafa;
}

.index-col {
  color: #999;
  min-width: 36px;
  text-align: center !important;
}

/* 分页 */
.van-pagination {
  margin: 10px 0;
}
</style>
