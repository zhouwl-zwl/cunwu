<template>
  <div class="page-container">
    <van-nav-bar title="农村供水工程现状摸底表" left-arrow @click-left="goBack" />

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ tableData.length }}</span>
        <span class="stats-label">供水工程</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ totalPopulation }}</span>
        <span class="stats-label">供水人口</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ shortageCount }}</span>
        <span class="stats-label">季节性缺水</span>
      </div>
    </div>

    <div v-for="(item, idx) in tableData" :key="idx" class="project-card">
      <div class="card-header">
        <span class="project-name">{{ item.name }}</span>
        <span class="type-tag" :class="item.type === '千人工程' ? 'big' : 'small'">{{ item.type }}</span>
      </div>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">所在位置</span>
          <span class="info-val">{{ item.location }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">建成年份</span>
          <span class="info-val">{{ item.year }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">供水人口</span>
          <span class="info-val bold">{{ item.population }} 人</span>
        </div>
        <div class="info-item">
          <span class="info-label">水源类型</span>
          <span class="info-val">{{ item.waterSource }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">季节性缺水</span>
          <span class="info-val" :class="item.shortage === '否' ? 'yes' : 'no'">{{ item.shortage }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">水源水量</span>
          <span class="info-val">{{ item.waterAmount }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const tableData = ref([
  { name: '罗卜田乡集镇供水工程', type: '千人工程', location: '罗卜田乡罗卜田村', year: '2011年', population: 1200, waterSource: '山溪水', shortage: '否', waterAmount: '一般' },
  { name: '罗卜田乡半冲供水工程', type: '千人以下', location: '罗卜田乡半冲村', year: '2017年', population: 544, waterSource: '山溪水', shortage: '否', waterAmount: '一般' },
  { name: '罗卜田乡马坡村供水工程', type: '千人以下', location: '罗卜田乡马坡村', year: '2017年', population: 477, waterSource: '山溪水', shortage: '否', waterAmount: '一般' },
  { name: '罗卜田乡新店村沙田坡农村饮水工程', type: '千人以下', location: '罗卜田乡新店村', year: '2017年', population: 556, waterSource: '山溪水', shortage: '否', waterAmount: '一般' },
  { name: '枣子山村渗水塘供水工程', type: '千人以下', location: '罗卜田乡枣子山村', year: '2017年', population: 350, waterSource: '山溪水', shortage: '否', waterAmount: '一般' },
  { name: '新店村大冲供水工程', type: '千人以下', location: '罗卜田乡新店村', year: '2017年', population: 130, waterSource: '山溪水', shortage: '否', waterAmount: '一般' }
])

const totalPopulation = computed(() => tableData.value.reduce((s, r) => s + r.population, 0))
const shortageCount = computed(() => tableData.value.filter(r => r.shortage === '是').length)

const goBack = () => router.back()
</script>

<style scoped>
.page-container { min-height: 100vh; background: #f5f5f5; padding-bottom: 20px; }
.stats-card { display: flex; background: linear-gradient(135deg, #29B6F6 0%, #01579B 100%); padding: 20px 12px; margin-bottom: 12px; }
.stats-item { flex: 1; text-align: center; }
.stats-num { display: block; font-size: 24px; font-weight: bold; color: #fff; margin-bottom: 4px; }
.stats-label { font-size: 12px; color: rgba(255,255,255,0.8); }
.project-card { background: #fff; margin: 0 12px 12px; border-radius: 12px; padding: 16px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.card-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; }
.project-name { font-size: 15px; font-weight: bold; color: #333; flex: 1; }
.type-tag { padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; color: #fff; }
.type-tag.big { background: #29B6F6; }
.type-tag.small { background: #81C784; }
.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 8px 16px; }
.info-item { display: flex; flex-direction: column; gap: 2px; }
.info-label { font-size: 12px; color: #999; }
.info-val { font-size: 14px; color: #333; }
.info-val.bold { font-weight: bold; color: #29B6F6; }
.info-val.yes { color: #4CAF50; }
.info-val.no { color: #F44336; }
</style>
