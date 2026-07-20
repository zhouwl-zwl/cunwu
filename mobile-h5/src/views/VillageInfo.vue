
<template>
  <div class="village-info-page page-container">
    <van-nav-bar title="村情档案" left-arrow @click-left="handleBack" />
    
    <div class="info-section" v-if="villageInfo">
      <div class="village-header">
        <h2>{{ villageInfo.villageName }}</h2>
        <p class="location">📍 {{ villageInfo.location }}</p>
      </div>

      <div class="stats-card">
        <div class="stat-item">
          <div class="stat-value">{{ villageInfo.population }}</div>
          <div class="stat-label">人口总数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ villageInfo.area }}</div>
          <div class="stat-label">面积(km²)</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ villageInfo.householdCount }}</div>
          <div class="stat-label">总户数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ villageInfo.groupCount }}</div>
          <div class="stat-label">村民组</div>
        </div>
      </div>

      <div class="info-card">
        <h3>🏛️ 村历史文化</h3>
        <p>{{ villageInfo.history }}</p>
      </div>

      <div class="info-card">
        <h3>🏆 集体荣誉</h3>
        <p>{{ villageInfo.honors }}</p>
      </div>

      <div class="info-card">
        <h3>🍎 特色农产品</h3>
        <p>{{ villageInfo.products }}</p>
      </div>

      <div class="info-card">
        <h3>💰 集体资产</h3>
        <p class="assets-value">总资产：{{ formatAssets }}元</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { villageInfoData } from '../data/mockData'
import { goBack } from '../utils/index'

const router = useRouter()
const villageInfo = ref(null)

const formatAssets = computed(() => {
  const total = villageInfo.value?.totalAssets || 0
  if (total >= 10000) {
    return (total / 10000).toFixed(1) + '万'
  }
  return total
})

const handleBack = () => {
  goBack(router)
}

const fetchVillageInfo = async () => {
  try {
    const res = await request.get('/public/village-info')
    villageInfo.value = res.data || villageInfoData
  } catch (error) {
    villageInfo.value = villageInfoData
  }
}

onMounted(() => {
  fetchVillageInfo()
})
</script>

<style scoped>
.village-header {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  color: #fff;
  padding: 32px 24px;
  text-align: center;
}

.village-header h2 {
  font-size: 28px;
  margin-bottom: 8px;
}

.location {
  font-size: 14px;
  opacity: 0.9;
}

.stats-card {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  padding: 16px;
  margin: -20px 12px 12px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
  color: #323233;
}

.stat-label {
  font-size: 11px;
  color: #969799;
  margin-top: 4px;
}

.info-card {
  background: #fff;
  margin: 0 12px 12px;
  border-radius: 12px;
  padding: 16px;
}

.info-card h3 {
  font-size: 16px;
  margin-bottom: 12px;
  color: #323233;
}

.info-card p {
  font-size: 14px;
  color: #646566;
  line-height: 1.6;
}

.assets-value {
  font-size: 16px;
  font-weight: bold;
  color: #ee0a24;
}
</style>
