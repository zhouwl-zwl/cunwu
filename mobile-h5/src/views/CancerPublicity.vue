<template>
  <div class="page-container">
    <van-nav-bar title="两癌宣传资料" left-arrow @click-left="goBack">
      <template #right>
        <van-icon name="share-o" size="20" color="#fff" @click="handleShare" />
      </template>
    </van-nav-bar>

    <div class="stats-card publicity-stats">
      <div class="stats-item">
        <span class="stats-num">{{ documents.length }}</span>
        <span class="stats-label">资料总数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ thisYearCount }}</span>
        <span class="stats-label">本年度下发</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ totalCoverage }}</span>
        <span class="stats-label">覆盖人数(女性)</span>
      </div>
    </div>

    <div class="search-section">
      <van-search 
        v-model="searchKeyword" 
        :placeholder="searchPlaceholder" 
        shape="round"
        @clear="searchKeyword = ''"
      />
      <div class="filter-bar">
        <span class="filter-label">搜索范围：</span>
        <van-radio-group v-model="searchField" direction="horizontal">
          <van-radio name="all">全部</van-radio>
          <van-radio name="title">资料名称</van-radio>
          <van-radio name="source">文件来源</van-radio>
          <van-radio name="target">宣传对象</van-radio>
        </van-radio-group>
      </div>
    </div>

    <div class="tabs-container">
      <van-tabs v-model="activeTypeTab" sticky>
        <van-tab title="全部" name="all" />
        <van-tab title="宫颈癌防治" name="宫颈癌" />
        <van-tab title="乳腺癌防治" name="乳腺癌" />
        <van-tab title="综合科普" name="综合" />
      </van-tabs>
    </div>

    <div class="export-bar">
      <div class="export-btn publicity-btn" @click="exportExcel">
        <van-icon name="down" size="16" color="#fff" />
        <span>导出清单Excel</span>
      </div>
      <span class="export-tip">共 {{ filteredList.length }} 份资料</span>
    </div>

    <div class="doc-list" v-if="filteredList.length > 0">
      <van-collapse v-model="activeNames">
        <van-collapse-item 
          v-for="(item, index) in filteredList" 
          :key="item.id" 
          :title="item.title"
          :name="item.id"
        >
          <template #title>
            <div class="doc-title-row">
              <div class="doc-index">{{ index + 1 }}</div>
              <div class="doc-title-text">
                <span class="doc-title">{{ item.title }}</span>
                <div class="doc-type-tag" :class="getTypeClass(item.type)">{{ item.type }}防治</div>
              </div>
            </div>
          </template>
          <van-cell-group inset class="doc-detail-group">
            <van-cell title="资料名称" :value="item.title" />
            <van-cell title="下发时间" :value="item.publishDate" />
            <van-cell title="文件来源" :value="item.source" />
            <van-cell title="宣传对象" :value="item.target" />
            <van-cell title="资料编号" :value="item.docNo" />
            <van-cell title="页数" :value="item.pageCount + '页'" />
            <van-cell title="宣传形式" :value="item.format" />
          </van-cell-group>
          <div class="doc-summary-section">
            <div class="summary-title">资料摘要</div>
            <div class="summary-content">{{ item.summary }}</div>
          </div>
          <div class="doc-actions">
            <div class="doc-action-btn" @click="handleView(item)">
              <van-icon name="eye-o" size="16" />
              <span>查看详情</span>
            </div>
            <div class="doc-action-btn" @click="handleShareItem(item)">
              <van-icon name="share-o" size="16" />
              <span>分享资料</span>
            </div>
          </div>
        </van-collapse-item>
      </van-collapse>
    </div>

    <div class="empty-state" v-else>
      <van-icon name="search" size="48" color="#ddd" />
      <p>未找到匹配的宣传资料</p>
      <van-button type="primary" size="small" @click="resetFilter">重置筛选</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import * as XLSX from 'xlsx'
import { showSharePanel } from '@/utils/share'

const router = useRouter()
const searchKeyword = ref('')
const searchField = ref('all')
const activeTypeTab = ref('all')
const activeNames = ref([])

const searchPlaceholder = computed(() => {
  const map = {
    all: '搜索资料名称、来源、宣传对象...',
    title: '输入资料名称搜索...',
    source: '输入文件来源搜索...',
    target: '输入宣传对象搜索...'
  }
  return map[searchField.value] || map.all
})

const documents = ref([
  {
    id: 1,
    title: '宫颈癌预防与早期筛查知识手册',
    type: '宫颈癌',
    publishDate: '2026-03-15',
    source: '湖南省卫生健康委员会',
    target: '35-64岁农村妇女',
    docNo: 'HN-2026-CC-001',
    pageCount: 32,
    format: '印刷手册 + 电子版',
    summary: '本手册详细介绍了宫颈癌的发病原因、高危因素、早期症状、筛查方法及预防措施。重点介绍了HPV疫苗接种的重要性和适宜人群，以及定期进行TCT+HPV联合筛查的必要性。手册图文并茂，语言通俗易懂，适合基层妇女阅读学习。'
  },
  {
    id: 2,
    title: '乳腺癌自我检查与早期诊断指南',
    type: '乳腺癌',
    publishDate: '2026-02-20',
    source: '湖南省妇女联合会',
    target: '20岁以上成年女性',
    docNo: 'HN-2026-BC-001',
    pageCount: 28,
    format: '宣传折页 + 视频教程',
    summary: '本指南系统讲解了乳腺癌的流行病学特征、自我检查方法（视诊+触诊）、最佳检查时间、发现异常后的就医流程，以及乳腺彩超、钼靶检查的适应症和注意事项。配有详细的步骤图解，帮助女性掌握正确的自检方法。'
  },
  {
    id: 3,
    title: '农村适龄妇女"两癌"免费检查政策解读',
    type: '综合',
    publishDate: '2026-01-10',
    source: '湖南省妇幼保健院',
    target: '35-64岁农村户籍妇女',
    docNo: 'HN-2026-POL-001',
    pageCount: 16,
    format: '政策宣传单 + 问答手册',
    summary: '详细解读湖南省农村适龄妇女"两癌"免费检查项目政策，包括检查对象、检查内容、检查流程、检查机构、经费保障、异常结果随访等内容。采用一问一答形式，清晰解答群众关心的各类问题，帮助符合条件的妇女及时享受到免费检查服务。'
  },
  {
    id: 4,
    title: 'HPV疫苗接种全攻略',
    type: '宫颈癌',
    publishDate: '2026-04-05',
    source: '怀化市疾病预防控制中心',
    target: '9-45岁女性',
    docNo: 'HH-2026-HPV-001',
    pageCount: 24,
    format: '科普手册 + 预约指引卡',
    summary: '全面介绍二价、四价、九价HPV疫苗的区别、接种程序、接种禁忌症、不良反应及处理、接种前注意事项等内容。重点说明不同价次疫苗的预防范围和适用年龄，解答"疫苗是否有效""接种后还需要筛查吗"等常见疑问，附全市接种点地址及预约方式。'
  },
  {
    id: 5,
    title: '乳腺癌患者术后康复指导手册',
    type: '乳腺癌',
    publishDate: '2025-11-18',
    source: '湖南省肿瘤医院',
    target: '乳腺癌术后患者及家属',
    docNo: 'HN-2025-BCR-002',
    pageCount: 48,
    format: '康复手册 + 运动图解',
    summary: '针对乳腺癌术后患者的康复需求，详细介绍术后伤口护理、患肢功能锻炼方法（分阶段）、饮食调理、心理调适、定期复查时间表、内分泌治疗注意事项等内容。配有专业的康复操图解，指导患者循序渐进地进行功能恢复训练。'
  },
  {
    id: 6,
    title: '宫颈癌治疗与康复科普读本',
    type: '宫颈癌',
    publishDate: '2025-09-25',
    source: '湖南省抗癌协会',
    target: '宫颈癌患者及家属、基层医务人员',
    docNo: 'HN-2025-CCR-003',
    pageCount: 56,
    format: '科普读本 + 典型案例',
    summary: '系统介绍宫颈癌的临床分期、治疗方案选择（手术/放疗/化疗/靶向治疗/免疫治疗）、治疗期间不良反应的应对、治疗后的随访计划、康复期的生活方式调整等。通过真实案例分享，帮助患者树立战胜疾病的信心。'
  },
  {
    id: 7,
    title: '"两癌"防治核心信息100问',
    type: '综合',
    publishDate: '2026-05-12',
    source: '怀化市卫生健康委员会',
    target: '全体女性居民',
    docNo: 'HH-2026-QA-001',
    pageCount: 64,
    format: '问答手册 + 口袋书',
    summary: '精选100个群众最关心的"两癌"防治问题，涵盖病因、预防、筛查、诊断、治疗、康复、政策等各个方面。采用简洁明了的问答形式，每个问题配有专家权威解答，便于快速查阅。64开小开本设计，方便随身携带阅读。'
  },
  {
    id: 8,
    title: '乳腺健康养护与疾病预防手册',
    type: '乳腺癌',
    publishDate: '2026-03-08',
    source: '罗卜田乡人民政府卫生计生办',
    target: '全乡18岁以上女性',
    docNo: 'LBT-2026-BH-001',
    pageCount: 20,
    format: '宣传手册 + 健康讲座PPT',
    summary: '结合农村女性生活特点，介绍日常乳腺健康养护方法，包括内衣选择、饮食调理、情绪管理、适度运动等方面的建议。同时讲解乳腺增生、乳腺结节、乳腺囊肿等常见乳腺疾病的鉴别知识，指导女性正确看待乳腺问题，避免过度焦虑或忽视症状。'
  },
  {
    id: 9,
    title: '宫颈癌筛查异常结果处理指引',
    type: '宫颈癌',
    publishDate: '2025-12-20',
    source: '怀化市妇幼保健院',
    target: '筛查结果异常的妇女',
    docNo: 'HH-2025-CCF-004',
    pageCount: 36,
    format: '就医指引 + 流程图解',
    summary: '针对宫颈癌筛查中常见的异常结果（HPV阳性、ASC-US、LSIL、HSIL等），逐一解释其意义、需要进一步做的检查项目、各检查项目的目的和注意事项、确诊后的治疗方案选择等。附有清晰的就医流程图，帮助异常结果患者理清思路，避免延误诊疗。'
  },
  {
    id: 10,
    title: '关注女性健康 "两癌"早防早治倡议书',
    type: '综合',
    publishDate: '2026-06-01',
    source: '罗卜田乡妇女联合会',
    target: '全乡女性及家庭成员',
    docNo: 'LBT-2026-INIT-001',
    pageCount: 6,
    format: '倡议书 + 宣传海报',
    summary: '向全乡妇女姐妹发出"两癌"早防早治倡议，呼吁广大女性树立健康意识，主动学习"两癌"防治知识，养成良好生活习惯，定期参加免费筛查，发现异常及时就医。同时号召家庭成员关心女性健康，共同守护家庭幸福。'
  },
  {
    id: 11,
    title: '乳腺癌靶向治疗与新药进展科普',
    type: '乳腺癌',
    publishDate: '2026-04-28',
    source: '湖南省医学科学研究院',
    target: '乳腺癌患者及家属',
    docNo: 'HN-2026-BCT-002',
    pageCount: 40,
    format: '科普报告 + 专家解读',
    summary: '介绍近年来乳腺癌靶向治疗的最新进展，包括HER2阳性乳腺癌的靶向药物选择、HR+/HER2-乳腺癌的CDK4/6抑制剂应用、三阴性乳腺癌的免疫治疗突破等。同时讲解靶向药物的常见副作用管理、医保报销政策等患者关心的实际问题。'
  }
])

const thisYearCount = computed(() => {
  return documents.value.filter(d => d.publishDate.startsWith('2026')).length
})

const totalCoverage = computed(() => {
  return 2860
})

const getTypeClass = (type) => {
  const map = {
    '宫颈癌': 'type-cervical',
    '乳腺癌': 'type-breast',
    '综合': 'type-general'
  }
  return map[type] || 'type-general'
}

const filteredList = computed(() => {
  let result = documents.value

  if (activeTypeTab.value !== 'all') {
    result = result.filter(d => d.type === activeTypeTab.value)
  }

  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(item => {
      if (searchField.value === 'title') {
        return item.title.toLowerCase().includes(keyword)
      } else if (searchField.value === 'source') {
        return item.source.toLowerCase().includes(keyword)
      } else if (searchField.value === 'target') {
        return item.target.toLowerCase().includes(keyword)
      } else {
        return item.title.toLowerCase().includes(keyword) ||
               item.source.toLowerCase().includes(keyword) ||
               item.target.toLowerCase().includes(keyword) ||
               item.summary.toLowerCase().includes(keyword) ||
               item.publishDate.includes(keyword)
      }
    })
  }

  return result
})

const resetFilter = () => {
  searchKeyword.value = ''
  searchField.value = 'all'
  activeTypeTab.value = 'all'
  activeNames.value = []
}

const handleView = (item) => {
  showToast(`正在打开：${item.title}`)
}

const handleShare = () => {
  showSharePanel({
    title: '两癌宣传资料',
    description: `共${documents.value.length}份宣传资料，本年度下发${thisYearCount.value}份，覆盖女性${totalCoverage.value}人`,
    url: window.location.href
  })
}

const handleShareItem = (item) => {
  showSharePanel({
    title: item.title,
    description: `${item.type}防治\n文件来源：${item.source}\n下发时间：${item.publishDate}\n宣传对象：${item.target}`,
    url: window.location.href
  })
}

const exportExcel = () => {
  const data = filteredList.value.map((item, index) => ({
    '序号': index + 1,
    '资料名称': item.title,
    '下发时间': item.publishDate,
    '文件来源': item.source,
    '宣传对象': item.target
  }))

  if (data.length === 0) {
    showToast('暂无可导出数据')
    return
  }

  const ws = XLSX.utils.json_to_sheet(data)
  ws['!cols'] = [
    { wch: 8 },
    { wch: 40 },
    { wch: 14 },
    { wch: 28 },
    { wch: 24 }
  ]
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '两癌宣传资料清单')
  XLSX.writeFile(wb, '两癌宣传资料清单.xlsx')
  showToast('导出成功')
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  document.title = '两癌宣传资料'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.stats-card {
  display: flex;
  background: var(--gradient-primary);
  padding: 16px;
  color: #fff;
  margin-bottom: 12px;
}

.publicity-stats {
  background: linear-gradient(135deg, #E91E63, #AD1457);
}

.stats-item {
  flex: 1;
  text-align: center;
}

.stats-num {
  display: block;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 12px;
  opacity: 0.9;
}

.search-section {
  background: #fff;
  padding: 8px 12px;
  margin-bottom: 10px;
}

.filter-bar {
  display: flex;
  align-items: center;
  margin-top: 8px;
  padding: 8px 0;
  font-size: 13px;
}

.filter-label {
  color: #666;
  margin-right: 12px;
  font-weight: 500;
}

.filter-bar .van-radio-group {
  flex: 1;
}

.filter-bar .van-radio {
  font-size: 13px;
}

.filter-bar .van-radio__label {
  font-size: 13px;
}

.tabs-container {
  background: #fff;
  margin-bottom: 10px;
}

.export-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 12px 12px;
}

.export-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--gradient-primary);
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.3);
  font-size: 13px;
  color: #fff;
  transition: transform 0.2s;
}

.publicity-btn {
  background: linear-gradient(135deg, #E91E63, #AD1457);
  box-shadow: 0 2px 8px rgba(233, 30, 99, 0.3);
}

.export-btn:active {
  transform: scale(0.95);
}

.export-tip {
  font-size: 12px;
  color: #999;
}

.doc-list {
  padding: 0 12px;
}

.doc-list :deep(.van-collapse-item) {
  margin-bottom: 10px;
  border-radius: 12px;
  overflow: hidden;
  border: none;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.doc-list :deep(.van-cell-group--inset) {
  margin: 12px;
}

.doc-title-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.doc-index {
  flex-shrink: 0;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #E91E63, #F06292);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: bold;
  margin-top: 2px;
}

.doc-title-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding-right: 20px;
}

.doc-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  line-height: 1.4;
}

.doc-type-tag {
  display: inline-block;
  width: fit-content;
  padding: 2px 10px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
}

.type-cervical {
  background: #e3f2fd;
  color: #1565c0;
}

.type-breast {
  background: #fce4ec;
  color: #c2185b;
}

.type-general {
  background: #e8f5e9;
  color: #2e7d32;
}

.doc-detail-group {
  background: #fafafa;
  border-radius: 8px;
}

.doc-summary-section {
  padding: 12px 16px;
}

.summary-title {
  font-size: 13px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  padding-left: 8px;
  border-left: 3px solid #E91E63;
}

.summary-content {
  font-size: 13px;
  color: #666;
  line-height: 1.7;
  padding: 8px 12px;
  background: #fff8f8;
  border-radius: 8px;
}

.doc-actions {
  display: flex;
  gap: 12px;
  padding: 8px 16px 16px;
}

.doc-action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 10px 0;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  transition: transform 0.2s;
}

.doc-action-btn:active {
  transform: scale(0.96);
}

.doc-action-btn:first-child {
  background: linear-gradient(135deg, #E91E63, #F06292);
  color: #fff;
  box-shadow: 0 2px 8px rgba(233, 30, 99, 0.25);
}

.doc-action-btn:last-child {
  background: #f5f5f5;
  color: #666;
  border: 1px solid #eee;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.empty-state p {
  color: #999;
  margin: 12px 0 20px;
  font-size: 14px;
}
</style>
