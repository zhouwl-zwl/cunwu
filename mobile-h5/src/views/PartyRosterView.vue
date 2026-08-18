<template>
  <div class="roster-view-page page-container">
    <van-nav-bar
      :title="pageTitle"
      left-arrow
      @click-left="goBack"
    />

    <div class="roster-hero" :style="{ background: heroGradient }">
      <div class="hero-icon">{{ heroIcon }}</div>
      <div class="hero-name">{{ pageTitle }}</div>
      <div class="hero-desc">{{ heroDesc }}</div>
      <div v-if="updateTip" class="hero-tip">📅 {{ updateTip }}</div>
    </div>

    <div class="roster-stats">
      <div class="stat-item">
        <div class="stat-num">{{ tableData.length }}</div>
        <div class="stat-label">总人数</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <div class="stat-num">{{ maleCount }}</div>
        <div class="stat-label">男性</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <div class="stat-num">{{ femaleCount }}</div>
        <div class="stat-label">女性</div>
      </div>
    </div>

    <div class="roster-toolbar">
      <van-search
        v-model="keyword"
        placeholder="搜索姓名/村组"
        shape="round"
        class="roster-search"
      />
      <van-button
        size="small"
        plain
        type="primary"
        class="export-btn"
        @click="handleExport"
      >
        <van-icon name="down" /> 导出
      </van-button>
    </div>

    <div class="roster-table-wrap">
      <table class="roster-table">
        <thead>
          <tr>
            <th v-for="(col, i) in columns" :key="i" :style="{ width: col.width }">{{ col.label }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, i) in filteredData" :key="i">
            <td v-for="(col, j) in columns" :key="j">
              <span v-if="col.mask" class="mask-text">{{ maskValue(row[col.key]) }}</span>
              <span v-else>{{ row[col.key] }}</span>
            </td>
          </tr>
          <tr v-if="filteredData.length === 0">
            <td :colspan="columns.length" class="empty-row">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="roster-footer">
      <div class="footer-info">
        <van-icon name="warning-o" size="14" color="#D22630" />
        <span>本台账涉及个人隐私，请妥善保管，不得外传</span>
      </div>
      <div class="footer-count">共 {{ filteredData.length }} 条记录</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showToast } from 'vant'

const route = useRoute()
const router = useRouter()

const type = computed(() => route.params.type || 'member-roster')

const config = computed(() => {
  const map = {
    'member-roster': {
      title: '党员花名册',
      icon: '📋',
      desc: '党员系统动态管理 - 全乡党员花名册',
      tip: '',
      gradient: 'linear-gradient(135deg, #D22630 0%, #8B0000 100%)'
    },
    '50year': {
      title: '50年党龄摸底台账',
      icon: '🎖️',
      desc: '50年党龄党员摸底登记台账',
      tip: '',
      gradient: 'linear-gradient(135deg, #B71C1C 0%, #4A148C 100%)'
    },
    '60year-aid': {
      title: '60年以上党龄困难党员补助台账',
      icon: '💝',
      desc: '60年以上党龄困难党员生活补助发放台账',
      tip: '半年更新一次（每年1月、7月）',
      gradient: 'linear-gradient(135deg, #C62828 0%, #880E4F 100%)'
    }
  }
  return map[type.value] || map['member-roster']
})

const pageTitle = computed(() => config.value.title)
const heroIcon = computed(() => config.value.icon)
const heroDesc = computed(() => config.value.desc)
const heroGradient = computed(() => config.value.gradient)
const updateTip = computed(() => config.value.tip)

const columns = computed(() => {
  if (type.value === '60year-aid') {
    return [
      { key: 'name', label: '姓名', width: '60px' },
      { key: 'gender', label: '性别', width: '50px' },
      { key: 'village', label: '所在村', width: '80px' },
      { key: 'partyYears', label: '党龄', width: '60px' },
      { key: 'difficulty', label: '困难情况', width: '90px' },
      { key: 'subsidy', label: '补助金额', width: '80px' },
      { key: 'period', label: '发放周期', width: '80px' }
    ]
  }
  if (type.value === '50year') {
    return [
      { key: 'name', label: '姓名', width: '60px' },
      { key: 'gender', label: '性别', width: '50px' },
      { key: 'village', label: '所在村', width: '80px' },
      { key: 'birth', label: '出生年月', width: '80px', mask: true },
      { key: 'joinDate', label: '入党时间', width: '90px' },
      { key: 'partyYears', label: '党龄', width: '60px' },
      { key: 'phone', label: '联系电话', width: '100px', mask: true }
    ]
  }
  return [
    { key: 'name', label: '姓名', width: '60px' },
    { key: 'gender', label: '性别', width: '50px' },
    { key: 'village', label: '所在村', width: '80px' },
    { key: 'branch', label: '所在支部', width: '100px' },
    { key: 'joinDate', label: '入党时间', width: '90px' },
    { key: 'partyYears', label: '党龄', width: '60px' },
    { key: 'phone', label: '联系电话', width: '100px', mask: true }
  ]
})

const mockData = computed(() => {
  if (type.value === '60year-aid') {
    return [
      { name: '张志国', gender: '男', village: '新店村', partyYears: '62年', difficulty: '长期患病', subsidy: '600元/半年', period: '每半年' },
      { name: '李春山', gender: '男', village: '罗卜田村', partyYears: '61年', difficulty: '失能半失能', subsidy: '800元/半年', period: '每半年' },
      { name: '王秀英', gender: '女', village: '兴无村', partyYears: '60年', difficulty: '家庭困难', subsidy: '500元/半年', period: '每半年' },
      { name: '陈德厚', gender: '男', village: '马坡村', partyYears: '63年', difficulty: '独居老人', subsidy: '700元/半年', period: '每半年' },
      { name: '赵桂兰', gender: '女', village: '半冲村', partyYears: '60年', difficulty: '长期患病', subsidy: '600元/半年', period: '每半年' }
    ]
  }
  if (type.value === '50year') {
    return [
      { name: '张志国', gender: '男', village: '新店村', birth: '1948-03', joinDate: '1973-07', partyYears: '52年', phone: '138****5678' },
      { name: '李春山', gender: '男', village: '罗卜田村', birth: '1946-11', joinDate: '1972-05', partyYears: '53年', phone: '139****1234' },
      { name: '王秀英', gender: '女', village: '兴无村', birth: '1949-06', joinDate: '1974-01', partyYears: '51年', phone: '137****6789' },
      { name: '陈德厚', gender: '男', village: '马坡村', birth: '1945-09', joinDate: '1971-03', partyYears: '54年', phone: '136****3456' },
      { name: '赵桂兰', gender: '女', village: '半冲村', birth: '1947-12', joinDate: '1973-09', partyYears: '52年', phone: '135****7890' },
      { name: '刘建国', gender: '男', village: '冬瓜坡村', birth: '1948-07', joinDate: '1973-12', partyYears: '51年', phone: '138****4567' },
      { name: '孙庆福', gender: '男', village: '枣子山村', birth: '1946-02', joinDate: '1972-08', partyYears: '53年', phone: '139****8901' }
    ]
  }
  return [
    { name: '张露海', gender: '男', village: '新店村', branch: '新店村党总支', joinDate: '2005-07', partyYears: '20年', phone: '188****8691' },
    { name: '张小华', gender: '男', village: '新店村', branch: '新店村党总支', joinDate: '2008-05', partyYears: '17年', phone: '199****3755' },
    { name: '杨堃', gender: '男', village: '新店村', branch: '新店村党总支', joinDate: '2012-07', partyYears: '13年', phone: '187****9401' },
    { name: '龙小梅', gender: '女', village: '新店村', branch: '新店村党总支', joinDate: '2015-03', partyYears: '10年', phone: '177****3109' },
    { name: '欧阳付群', gender: '男', village: '罗卜田村', branch: '乡机关支部', joinDate: '2001-07', partyYears: '24年', phone: '138****0001' },
    { name: '黄楚珂', gender: '女', village: '罗卜田村', branch: '乡机关支部', joinDate: '2013-07', partyYears: '12年', phone: '137****0003' },
    { name: '龙君屹', gender: '男', village: '兴无村', branch: '兴无村党支部', joinDate: '2006-07', partyYears: '19年', phone: '136****0004' },
    { name: '张芷馨', gender: '女', village: '马坡村', branch: '马坡村党支部', joinDate: '2009-07', partyYears: '16年', phone: '135****0005' },
    { name: '杨成', gender: '男', village: '半冲村', branch: '乡机关支部', joinDate: '2007-05', partyYears: '18年', phone: '134****0006' }
  ]
})

const tableData = computed(() => mockData.value)

const keyword = ref('')
const filteredData = computed(() => {
  if (!keyword.value.trim()) return tableData.value
  const kw = keyword.value.trim()
  return tableData.value.filter(row =>
    String(row.name || '').includes(kw) ||
    String(row.village || '').includes(kw) ||
    String(row.branch || '').includes(kw)
  )
})

const maleCount = computed(() => tableData.value.filter(r => r.gender === '男').length)
const femaleCount = computed(() => tableData.value.filter(r => r.gender === '女').length)

const maskValue = (val) => val || ''

const goBack = () => router.back()

const handleExport = () => {
  showToast('导出功能开发中')
}
</script>

<style scoped>
.roster-view-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.roster-hero {
  padding: 24px 16px 20px;
  color: #fff;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.roster-hero::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 120px;
  height: 120px;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.15) 0%, transparent 70%);
  border-radius: 50%;
  transform: translate(30%, -30%);
}

.hero-icon {
  font-size: 40px;
  margin-bottom: 6px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.hero-name {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 4px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.hero-desc {
  font-size: 12px;
  opacity: 0.9;
  margin-bottom: 6px;
}

.hero-tip {
  display: inline-block;
  font-size: 11px;
  background: rgba(255, 215, 0, 0.2);
  border: 1px solid rgba(255, 215, 0, 0.4);
  padding: 3px 10px;
  border-radius: 10px;
  color: #FFD700;
}

.roster-stats {
  display: flex;
  align-items: center;
  justify-content: space-around;
  background: #fff;
  margin: -16px 12px 12px;
  padding: 14px 12px;
  border-radius: 14px;
  box-shadow: 0 4px 14px rgba(210, 38, 48, 0.1);
  border: 1px solid rgba(210, 38, 48, 0.08);
  position: relative;
  z-index: 2;
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-num {
  font-size: 20px;
  font-weight: bold;
  color: #D22630;
}

.stat-label {
  font-size: 11px;
  color: #999;
  margin-top: 2px;
}

.stat-divider {
  width: 1px;
  height: 24px;
  background: linear-gradient(180deg, transparent, #eee, transparent);
}

.roster-toolbar {
  display: flex;
  gap: 8px;
  padding: 0 12px 12px;
  align-items: center;
}

.roster-search {
  flex: 1;
  padding: 0;
}

.roster-search :deep(.van-search__content) {
  background: #fff;
}

.export-btn {
  flex-shrink: 0;
  border-color: #D22630;
  color: #D22630;
  white-space: nowrap;
}

.roster-table-wrap {
  margin: 0 12px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
  overflow-x: auto;
}

.roster-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
  min-width: 500px;
}

.roster-table thead {
  background: linear-gradient(135deg, #D22630 0%, #8B0000 100%);
  color: #fff;
}

.roster-table th {
  padding: 10px 8px;
  text-align: center;
  font-weight: 500;
  white-space: nowrap;
}

.roster-table td {
  padding: 10px 8px;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
  color: #555;
  white-space: nowrap;
}

.roster-table tbody tr:nth-child(even) {
  background: #FFF8F0;
}

.roster-table tbody tr:active {
  background: #FFE0E0;
}

.mask-text {
  color: #999;
}

.empty-row {
  text-align: center;
  color: #999;
  padding: 30px 0;
}

.roster-footer {
  padding: 16px 12px 0;
}

.footer-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 11px;
  color: #D22630;
  background: rgba(210, 38, 48, 0.06);
  padding: 8px 12px;
  border-radius: 8px;
  border: 1px dashed rgba(210, 38, 48, 0.2);
  margin-bottom: 8px;
}

.footer-count {
  font-size: 11px;
  color: #999;
  text-align: center;
}
</style>
