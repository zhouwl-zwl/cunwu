<template>
  <div class="page-container">
    <van-nav-bar title="罗卜田乡危房统计表" left-arrow @click-left="goBack">
      <template #right>
        <van-icon name="share-o" size="20" color="#fff" @click="handleShare" />
      </template>
    </van-nav-bar>

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ tableData.length }}</span>
        <span class="stats-label">危房总数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ villageCount }}</span>
        <span class="stats-label">涉及村落</span>
      </div>
      <div class="stats-item">
        <span class="stats-num level-c">{{ levelCount.C }}</span>
        <span class="stats-label">C级</span>
      </div>
      <div class="stats-item">
        <span class="stats-num level-d">{{ levelCount.D }}</span>
        <span class="stats-label">D级</span>
      </div>
    </div>

    <div class="search-section">
      <van-search
        v-model="searchKeyword"
        placeholder="搜索姓名/村社区/地址"
        shape="round"
        @clear="searchKeyword = ''"
      />
    </div>

    <div class="filter-bar">
      <van-tabs v-model="activeTab" sticky>
        <van-tab title="全部" name="all" />
        <van-tab v-for="v in villageList" :key="v" :title="v" :name="v" />
      </van-tabs>
    </div>

    <div class="export-bar">
      <div class="export-btn" @click="exportExcel">
        <van-icon name="down" size="16" color="#fff" />
        <span>导出Excel</span>
      </div>
      <span class="export-tip">共 {{ filteredData.length }} 条记录</span>
    </div>

    <div class="table-wrapper">
      <div class="table-title">全省专项行动入库、各地日常巡查排查动态新增结构安全隐患问题台账（罗卜田乡）</div>
      <div class="table-scroll">
        <table class="data-table">
          <thead>
            <tr>
              <th class="col-idx">序号</th>
              <th class="col-village">村社区</th>
              <th class="col-id">房屋编号</th>
              <th class="col-name">姓名</th>
              <th class="col-num">层数</th>
              <th class="col-num">面积</th>
              <th class="col-group">组</th>
              <th class="col-addr">地址</th>
              <th class="col-use">房屋用途</th>
              <th class="col-level">安全等级</th>
              <th class="col-manage">管理措施</th>
              <th class="col-eng">工程措施</th>
              <th class="col-status">销号审核状态</th>
              <th class="col-date">整治时间</th>
              <th class="col-review">复查情况</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(row, idx) in filteredData" :key="idx">
              <td class="col-idx">{{ idx + 1 }}</td>
              <td class="col-village">{{ row.village }}</td>
              <td class="col-id">{{ row.houseId }}</td>
              <td class="col-name">{{ row.name }}</td>
              <td class="col-num">{{ row.floors }}</td>
              <td class="col-num">{{ row.area }}</td>
              <td class="col-group">{{ row.group }}</td>
              <td class="col-addr">{{ row.address }}</td>
              <td class="col-use">{{ row.usage }}</td>
              <td class="col-level">
                <span class="level-tag" :class="row.level === 'C' ? 'level-c-tag' : 'level-d-tag'">{{ row.level }}</span>
              </td>
              <td class="col-manage">{{ row.management }}</td>
              <td class="col-eng">{{ row.engineering }}</td>
              <td class="col-status">{{ row.auditStatus }}</td>
              <td class="col-date">{{ row.repairDate }}</td>
              <td class="col-review">{{ row.review }}</td>
            </tr>
            <tr v-if="filteredData.length === 0">
              <td colspan="15" class="empty-cell">暂无数据</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { exportToExcel } from '@/utils/excel'

const router = useRouter()
const searchKeyword = ref('')
const activeTab = ref('all')

const tableData = ref([
  { village: '早子山村', houseId: 'YH431228000006115', name: '李泽恒', floors: 1, area: 110, group: '黄糯科组', address: '黄糯科组组无路(街、巷)无号', usage: '自住', level: 'D', management: '封控警示', engineering: '维修加固', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '维修加固，人员不居住。' },
  { village: '马坡村', houseId: 'YH431228000009526', name: '彭怀英', floors: 1, area: 100, group: '核桃坪组', address: '核桃坪组组无路(街、巷)无号', usage: '自住', level: 'C', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-13', review: '房屋已拆除' },
  { village: '早子山村', houseId: 'YH431228000018557', name: '杨成枚', floors: 1, area: 125, group: '新屋场组', address: '新屋场组组/路(街、巷)/号', usage: '自住', level: 'C', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-13', review: '房屋已拆除' },
  { village: '罗卜田村', houseId: 'YH431228000020397', name: '李复英,郑平辉', floors: 1, area: 200, group: '福禄坪组', address: '福禄坪组组无路(街、巷)25号', usage: '自住', level: 'D', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '房屋已拆除' },
  { village: '早子山村', houseId: 'YH431228000023642', name: '钦永英', floors: 1, area: 58, group: '鲢鱼山组', address: '鲢鱼山组组/路(街、巷)/号', usage: '自住', level: 'C', management: '封控警示', engineering: '维修加固', auditStatus: '销号审核通过', repairDate: '2023-10-14', review: '维修加固已消除隐患' },
  { village: '早子山村', houseId: 'YH431228000026709', name: '郑再海', floors: 1, area: 150, group: '枞山脚组', address: '枞山脚组组无路(街、巷)无号', usage: '自住', level: 'D', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '房屋已拆除' },
  { village: '冬瓜坡村', houseId: 'YH431228000038591', name: '张邦军', floors: 1, area: 110, group: '许家山', address: '许家山组/路(街、巷)/号', usage: '自住', level: 'C', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '房屋已拆除' },
  { village: '早子山村', houseId: 'YH431228000039207', name: '肖明顺', floors: 1, area: 180, group: '言家冲', address: '言家冲组无路(街、巷)无号', usage: '自住', level: 'C', management: '封控警示', engineering: '维修加固', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '维修加固已消除隐患' },
  { village: '早子山村', houseId: 'YH431228000040350', name: '邱光枚', floors: 1, area: 165, group: '桥边', address: '桥边组/路(街、巷)/号', usage: '自住', level: 'C', management: '封控警示', engineering: '维修加固', auditStatus: '销号审核通过', repairDate: '2023-10-14', review: '维修加固已消除隐患' },
  { village: '早子山村', houseId: 'YH431228000040407', name: '郑德刚', floors: 1, area: 185, group: '桥边', address: '桥边组/路(街、巷)/号', usage: '自住', level: 'D', management: '封控警示', engineering: '维修加固', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '维修加固已消除隐患' },
  { village: '早子山村', houseId: 'YH431228000040478', name: '郑军华', floors: 1, area: 125, group: '鲢鱼山组', address: '鲢鱼山组组/路(街、巷)/号', usage: '自住', level: 'C', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '无人居住，挂牌警示。' },
  { village: '冬瓜坡村', houseId: 'YH431228000049017', name: '黄幼贵', floors: 1, area: 180, group: '郭家冲', address: '郭家冲组/路(街、巷)/号', usage: '自住', level: 'D', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '无人居住，挂牌警示。' },
  { village: '冬瓜坡村', houseId: 'YH431228000051154', name: '黄元海,黄元清', floors: 1, area: 100, group: '郭家冲', address: '郭家冲组/路(街、巷)/号', usage: '自住', level: 'D', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '无人居住，挂牌警示。' },
  { village: '冬瓜坡村', houseId: 'YH431228000057286', name: '秦建军,秦军', floors: 1, area: 200, group: '泥冲', address: '泥冲组无路(街、巷)无号', usage: '自住', level: 'D', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-14', review: '房屋已拆除' },
  { village: '早子山村', houseId: 'YH431228000078695', name: '张翠红', floors: 2, area: 240, group: '言家冲', address: '言家冲组无路(街、巷)无号', usage: '自住', level: 'C', management: '封控警示', engineering: '拆除', auditStatus: '销号审核通过', repairDate: '2023-10-20', review: '无人居住' },
  { village: '早子山村', houseId: 'YH431228000079784', name: '郑复来', floors: 1, area: 95, group: '老屯', address: '老屯组/路(街、巷)/号', usage: '自住', level: 'C', management: '封控警示', engineering: '维修加固', auditStatus: '销号审核通过', repairDate: '2023-10-14', review: '维修加固已消除隐患' }
])

const villageList = computed(() => {
  const set = new Set(tableData.value.map(r => r.village))
  return [...set].sort()
})

const villageCount = computed(() => villageList.value.length)

const levelCount = computed(() => {
  const c = tableData.value.filter(r => r.level === 'C').length
  const d = tableData.value.filter(r => r.level === 'D').length
  return { C: c, D: d }
})

const filteredData = computed(() => {
  let result = tableData.value
  if (activeTab.value !== 'all') {
    result = result.filter(r => r.village === activeTab.value)
  }
  if (searchKeyword.value) {
    const kw = searchKeyword.value.toLowerCase()
    result = result.filter(r =>
      r.name.toLowerCase().includes(kw) ||
      r.village.toLowerCase().includes(kw) ||
      r.address.toLowerCase().includes(kw)
    )
  }
  return result
})

const exportColumns = [
  { title: '序号', key: 'index' },
  { title: '村社区', key: 'village' },
  { title: '房屋编号', key: 'houseId' },
  { title: '姓名', key: 'name' },
  { title: '层数', key: 'floors' },
  { title: '面积', key: 'area' },
  { title: '组', key: 'group' },
  { title: '地址', key: 'address' },
  { title: '房屋用途', key: 'usage' },
  { title: '房屋安全等级', key: 'level' },
  { title: '管理措施', key: 'management' },
  { title: '工程措施', key: 'engineering' },
  { title: '销号审核状态', key: 'auditStatus' },
  { title: '整治时间', key: 'repairDate' },
  { title: '复查情况', key: 'review' }
]

const exportExcel = () => {
  const exportData = filteredData.value.map((r, i) => ({ ...r, index: i + 1 }))
  exportToExcel(exportData, exportColumns, '罗卜田乡危房统计表.xlsx')
}

const handleShare = () => {
  showToast('分享功能开发中')
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.stats-card {
  display: flex;
  background: linear-gradient(135deg, #8D6E63 0%, #5D4037 100%);
  padding: 20px 12px;
  margin-bottom: 12px;
}

.stats-item {
  flex: 1;
  text-align: center;
}

.stats-num {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #fff;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
}

.stats-num.level-c {
  color: #FFD54F;
}

.stats-num.level-d {
  color: #FF8A65;
}

.search-section {
  padding: 0 12px;
  margin-bottom: 8px;
}

.filter-bar {
  margin-bottom: 8px;
}

.export-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px 12px;
}

.export-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  background: linear-gradient(135deg, #8D6E63 0%, #5D4037 100%);
  color: #fff;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(93, 64, 55, 0.3);
}

.export-tip {
  font-size: 12px;
  color: #999;
}

.table-wrapper {
  background: #fff;
  margin: 0 12px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.table-title {
  font-size: 13px;
  font-weight: bold;
  color: #333;
  padding: 14px 12px;
  text-align: center;
  background: linear-gradient(135deg, #D7CCC8 0%, #BCAAA4 100%);
  border-bottom: 2px solid #8D6E63;
  line-height: 1.5;
}

.table-scroll {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.data-table {
  width: 100%;
  min-width: 1400px;
  border-collapse: collapse;
  font-size: 12px;
}

.data-table thead {
  background: linear-gradient(135deg, #8D6E63 0%, #5D4037 100%);
  position: sticky;
  top: 0;
  z-index: 10;
}

.data-table th {
  padding: 10px 6px;
  text-align: center;
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  white-space: nowrap;
  border-right: 1px solid rgba(255, 255, 255, 0.15);
}

.data-table th:last-child {
  border-right: none;
}

.data-table tbody tr:nth-child(even) {
  background: #FAF5F3;
}

.data-table tbody tr:nth-child(odd) {
  background: #fff;
}

.data-table tbody tr:active {
  background: #F5EBE8;
}

.data-table td {
  padding: 8px 6px;
  text-align: center;
  color: #333;
  white-space: nowrap;
  border-right: 1px solid #f0e0db;
  border-bottom: 1px solid #f5ebe8;
}

.data-table td:last-child {
  border-right: none;
}

.col-idx { width: 40px; min-width: 40px; }
.col-village { width: 90px; min-width: 90px; }
.col-id { width: 160px; min-width: 160px; }
.col-name { width: 100px; min-width: 100px; }
.col-num { width: 50px; min-width: 50px; }
.col-group { width: 90px; min-width: 90px; }
.col-addr { width: 200px; min-width: 200px; }
.col-use { width: 60px; min-width: 60px; }
.col-level { width: 60px; min-width: 60px; }
.col-manage { width: 120px; min-width: 120px; }
.col-eng { width: 100px; min-width: 100px; }
.col-status { width: 110px; min-width: 110px; }
.col-date { width: 90px; min-width: 90px; }
.col-review { width: 180px; min-width: 180px; }

.level-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  color: #fff;
}

.level-c-tag {
  background: #FF9800;
}

.level-d-tag {
  background: #F44336;
}

.empty-cell {
  padding: 40px;
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style>
