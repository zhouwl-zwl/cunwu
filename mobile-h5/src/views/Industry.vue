<template>
  <div class="industry-page page-container">
    <van-nav-bar title="产业发展" left-arrow @click-left="handleBack" />

    <div class="stats-card">
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalCooperatives }}</div>
        <div class="stat-label">合作社数量</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalIncome }}</div>
        <div class="stat-label">年总收入(万元)</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stats.totalEmployment }}</div>
        <div class="stat-label">带动就业(人)</div>
      </div>
    </div>

    <div class="filter-bar">
      <van-search v-model="searchKeyword" placeholder="搜索合作社名称" @search="onSearch" />
    </div>

    <div class="card-list">
      <div v-for="item in industryList" :key="item.id" class="industry-card ripple" @click="goDetail(item.id)">
        <div class="card-header">
          <div class="card-icon-wrap" :class="getIconBgClass(item.type)">
            <van-icon :name="getIconName(item.type)" size="24" color="#fff" />
          </div>
          <div class="card-title">{{ item.name }}</div>
          <span class="card-tag" :class="item.status">{{ item.status === '运营中' ? '运营中' : '筹备中' }}</span>
        </div>
        <div class="card-info">
          <div class="info-row">
            <span class="info-label">类型</span>
            <span class="info-value">{{ item.type }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">负责人</span>
            <span class="info-value">{{ item.manager }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">年产值</span>
            <span class="info-value highlight">{{ item.annualIncome }}万元</span>
          </div>
          <div class="info-row">
            <span class="info-label">就业人数</span>
            <span class="info-value">{{ item.employment }}人</span>
          </div>
        </div>
        <div class="card-desc">{{ item.description }}</div>
        <div class="card-footer">
          <span class="footer-text">查看详情</span>
          <van-icon name="arrow-right" size="16" color="#999" />
        </div>
      </div>
    </div>

    <div v-if="isOfficial" class="action-bar">
      <van-button type="primary" icon="plus" @click="showAddModal = true">新增产业项目</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const searchKeyword = ref('')
const isOfficial = true
const showAddModal = ref(false)

const stats = reactive({
  totalCooperatives: 6,
  totalIncome: 2850,
  totalEmployment: 320
})

const industryList = ref([
  { id: 1, name: '罗卜田乡生态农业合作社', type: '种植养殖', manager: '张建国', annualIncome: 850, employment: 85, status: '运营中', description: '主要从事有机蔬菜种植、家禽养殖，产品远销省内外各大超市' },
  { id: 2, name: '青山茶叶专业合作社', type: '特色种植', manager: '李明辉', annualIncome: 620, employment: 60, status: '运营中', description: '种植高山云雾茶，年产茶叶50吨，获得有机认证' },
  { id: 3, name: '碧水渔业合作社', type: '水产养殖', manager: '王水生', annualIncome: 480, employment: 45, status: '运营中', description: '利用村内水库发展特色鱼类养殖，产品供应周边城市' },
  { id: 4, name: '乡村旅游发展公司', type: '旅游服务', manager: '赵美丽', annualIncome: 560, employment: 70, status: '运营中', description: '开发乡村旅游项目，提供农家乐、采摘园、民宿等服务' },
  { id: 5, name: '农产品加工合作社', type: '农产品加工', manager: '陈伟强', annualIncome: 340, employment: 60, status: '运营中', description: '从事农产品深加工，生产酱菜、干货、糕点等产品' }
])

const getIconName = (type) => {
  const map = {
    '种植养殖': 'sprout',
    '特色种植': 'coffee',
    '水产养殖': 'droplet-o',
    '旅游服务': 'map-o',
    '农产品加工': 'factory-o'
  }
  return map[type] || 'shop-o'
}

const getIconBgClass = (type) => {
  const map = {
    '种植养殖': 'green-gradient',
    '特色种植': 'purple-gradient',
    '水产养殖': 'blue-gradient',
    '旅游服务': 'orange-gradient',
    '农产品加工': 'red-gradient'
  }
  return map[type] || 'gray-gradient'
}

const handleBack = () => {
  goBack(router)
}

const goDetail = (id) => {
  router.push(`/industry-detail/${id}`)
}

const onSearch = () => {
  if (!searchKeyword.value) {
    fetchData()
    return
  }
  const filtered = industryList.value.filter(item => 
    item.name.includes(searchKeyword.value)
  )
  industryList.value = filtered.length > 0 ? filtered : industryList.value
  showToast(`搜索到 ${industryList.value.length} 条结果`)
}

const fetchData = async () => {
  try {
    const res = await request.get('/industry/list')
    industryList.value = res.data?.records || industryList.value
    stats.totalCooperatives = res.data?.totalCooperatives || stats.totalCooperatives
    stats.totalIncome = res.data?.totalIncome || stats.totalIncome
    stats.totalEmployment = res.data?.totalEmployment || stats.totalEmployment
  } catch (error) {
    console.error('获取产业数据失败', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.industry-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.stats-card {
  display: flex;
  background: #fff;
  margin: 12px;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #D22630;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.filter-bar {
  padding: 0 12px;
  margin-bottom: 12px;
}

.card-list {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.industry-card {
  background: #fff;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.card-icon-wrap {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.card-icon-wrap.red-gradient {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

.card-icon-wrap.green-gradient {
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
}

.card-icon-wrap.blue-gradient {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
}

.card-icon-wrap.orange-gradient {
  background: linear-gradient(135deg, #FFA500 0%, #FF8C00 100%);
}

.card-icon-wrap.purple-gradient {
  background: linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%);
}

.card-icon-wrap.gray-gradient {
  background: linear-gradient(135deg, #9E9E9E 0%, #757575 100%);
}

.card-title {
  flex: 1;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.card-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.card-tag.运营中 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.card-tag.筹备中 {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.card-info {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
  margin-bottom: 10px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 13px;
  border-bottom: 1px solid #eee;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  color: #999;
}

.info-value {
  color: #333;
}

.info-value.highlight {
  color: #D22630;
  font-weight: bold;
}

.card-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 12px;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #D22630;
}

.action-bar {
  padding: 16px 12px;
}

.action-bar .van-button {
  width: 100%;
}

.ripple {
  position: relative;
  overflow: hidden;
  
  &::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    background: rgba(210, 38, 48, 0.05);
    border-radius: 50%;
    transform: translate(-50%, -50%);
    transition: width 0.4s, height 0.4s;
  }
  
  &:active::after {
    width: 200px;
    height: 200px;
  }
}
</style>