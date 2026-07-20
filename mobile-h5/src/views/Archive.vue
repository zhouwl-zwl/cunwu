<template>
  <div class="archive-page page-container">
    <van-nav-bar title="档案资料" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="党建档案" name="party" />
      <van-tab title="三资财务" name="finance" />
      <van-tab title="人口土地" name="population" />
      <van-tab title="项目调解" name="project" />
    </van-tabs>

    <div class="search-bar">
      <van-search v-model="searchKeyword" placeholder="搜索档案名称" @search="onSearch" />
    </div>

    <div class="tab-content" v-if="activeTab === 'party'">
      <div class="card-list">
        <div v-for="item in partyList" :key="item.id" class="archive-card" @click="viewArchive(item)">
          <div class="archive-icon">📁</div>
          <div class="archive-info">
            <div class="archive-name">{{ item.name }}</div>
            <div class="archive-meta">{{ item.year }}年 | {{ item.type }}</div>
          </div>
          <div class="archive-arrow">›</div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'finance'">
      <div class="card-list">
        <div v-for="item in financeList" :key="item.id" class="archive-card" @click="viewArchive(item)">
          <div class="archive-icon">💰</div>
          <div class="archive-info">
            <div class="archive-name">{{ item.name }}</div>
            <div class="archive-meta">{{ item.month }}月 | {{ item.amount }}</div>
          </div>
          <div class="archive-arrow">›</div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'population'">
      <div class="card-list">
        <div v-for="item in populationList" :key="item.id" class="archive-card" @click="viewArchive(item)">
          <div class="archive-icon">👥</div>
          <div class="archive-info">
            <div class="archive-name">{{ item.name }}</div>
            <div class="archive-meta">{{ item.count }}条记录</div>
          </div>
          <div class="archive-arrow">›</div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'project'">
      <div class="card-list">
        <div v-for="item in projectList" :key="item.id" class="archive-card" @click="viewArchive(item)">
          <div class="archive-icon">🏗️</div>
          <div class="archive-info">
            <div class="archive-name">{{ item.name }}</div>
            <div class="archive-meta">{{ item.type }} | {{ item.date }}</div>
          </div>
          <div class="archive-arrow">›</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const activeTab = ref('party')
const searchKeyword = ref('')
const isAdmin = true

const partyList = ref([
  { id: 1, name: '党组织建设档案', year: 2024, type: '组织建设' },
  { id: 2, name: '党员名册', year: 2024, type: '党员管理' },
  { id: 3, name: '主题党日活动记录', year: 2024, type: '活动记录' },
  { id: 4, name: '发展党员材料', year: 2024, type: '党员发展' },
  { id: 5, name: '党费收缴台账', year: 2024, type: '党费管理' },
  { id: 6, name: '党建工作汇报', year: 2024, type: '工作汇报' }
])

const financeList = ref([
  { id: 1, name: '2024年1月财务报表', month: '2024-01', amount: '¥125,000' },
  { id: 2, name: '2024年2月财务报表', month: '2024-02', amount: '¥89,500' },
  { id: 3, name: '2024年第一季度三资台账', month: '2024-Q1', amount: '¥356,000' },
  { id: 4, name: '固定资产台账', month: '2024', amount: '¥2,100,000' },
  { id: 5, name: '村集体经济收入明细', month: '2024', amount: '¥480,000' }
])

const populationList = ref([
  { id: 1, name: '全村户籍台账', count: 286 },
  { id: 2, name: '常住人口登记表', count: 245 },
  { id: 3, name: '外出务工人员台账', count: 41 },
  { id: 4, name: '特殊人群档案', count: 32 },
  { id: 5, name: '户籍变动记录', count: 18 }
])

const projectList = ref([
  { id: 1, name: '道路硬化工程档案', type: '工程项目', date: '2024' },
  { id: 2, name: '文化广场建设档案', type: '工程项目', date: '2024' },
  { id: 3, name: '矛盾纠纷调解记录', type: '调解档案', date: '2024' },
  { id: 4, name: '信访处理台账', type: '信访档案', date: '2024' },
  { id: 5, name: '安全隐患排查记录', type: '安全档案', date: '2024' }
])

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'party') {
      const res = await request.get('/archive/party', { params: { keyword: searchKeyword.value } })
      partyList.value = res.data || partyList.value
    } else if (activeTab.value === 'finance') {
      const res = await request.get('/archive/finance', { params: { keyword: searchKeyword.value } })
      financeList.value = res.data || financeList.value
    } else if (activeTab.value === 'population') {
      const res = await request.get('/archive/population', { params: { keyword: searchKeyword.value } })
      populationList.value = res.data || populationList.value
    } else if (activeTab.value === 'project') {
      const res = await request.get('/archive/project', { params: { keyword: searchKeyword.value } })
      projectList.value = res.data || projectList.value
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const onSearch = () => {
  fetchData()
}

const viewArchive = (item) => {
  showToast(`查看 ${item.name} 详情功能开发中`)
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.archive-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.search-bar {
  padding: 12px;
  background: #fff;
}

.card-list {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.archive-card {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.archive-icon {
  font-size: 28px;
  margin-right: 14px;
}

.archive-info {
  flex: 1;
}

.archive-name {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.archive-meta {
  font-size: 12px;
  color: #999;
}

.archive-arrow {
  font-size: 20px;
  color: #ddd;
}
</style>
