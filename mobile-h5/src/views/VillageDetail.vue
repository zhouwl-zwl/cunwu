<template>
  <div class="village-detail page-container">
    <div class="header-banner">
      <div class="banner-image">
        <img :src="village.image" :alt="village.name" />
        <div class="banner-overlay"></div>
        <div class="banner-title">{{ village.name }}</div>
        <div class="banner-subtitle">{{ village.desc }}</div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="bar-chart-o" size="20" color="#D22630" />
          <h3>基本村情</h3>
        </div>
      </div>
      <div class="grid-4">
        <div class="info-card red-gradient">
          <van-icon name="friends-o" size="24" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ village.population }}人</div>
            <div class="card-label">人口</div>
          </div>
        </div>
        <div class="info-card green-gradient">
          <van-icon name="map" size="24" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ village.area }}km²</div>
            <div class="card-label">面积</div>
          </div>
        </div>
        <div class="info-card blue-gradient">
          <van-icon name="home-o" size="24" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ village.households }}户</div>
            <div class="card-label">户数</div>
          </div>
        </div>
        <div class="info-card purple-gradient">
          <van-icon name="team" size="24" color="#fff" />
          <div class="card-content">
            <div class="card-value">{{ village.groups }}个</div>
            <div class="card-label">村民组</div>
          </div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="wheat" size="20" color="#4CAF50" />
          <h3>乡村振兴</h3>
        </div>
      </div>
      <div class="grid-4">
        <div class="function-card" @click="goPage('/monitoring-households')">
          <div class="card-icon brown-bg">
            <van-icon name="hand" size="28" color="#fff" />
          </div>
          <div class="card-text">监测户信息</div>
        </div>
        <div class="function-card" @click="goPage('/poverty-households')">
          <div class="card-icon green-bg">
            <van-icon name="trophy" size="28" color="#fff" />
          </div>
          <div class="card-text">脱贫户信息</div>
        </div>
        <div class="function-card" @click="goPage('/rain-plan')">
          <div class="card-icon forest-bg">
            <van-icon name="droplets" size="28" color="#fff" />
          </div>
          <div class="card-text">雨露计划</div>
        </div>
        <div class="function-card" @click="goPage('/labor-info')">
          <div class="card-icon sky-blue-bg">
            <van-icon name="user-o" size="28" color="#fff" />
          </div>
          <div class="card-text">务工信息</div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="wallet" size="20" color="#9C27B0" />
          <h3>农村三资</h3>
        </div>
      </div>
      <div class="grid-5">
        <div class="function-card" @click="goPage('/village-assets')">
          <div class="card-icon pink-bg">
            <van-icon name="building" size="28" color="#fff" />
          </div>
          <div class="card-text">村级资产</div>
        </div>
        <div class="function-card" @click="goPage('/village-resources')">
          <div class="card-icon lime-bg">
            <van-icon name="droplet-o" size="28" color="#fff" />
          </div>
          <div class="card-text">村级资源</div>
        </div>
        <div class="function-card" @click="goPage('/village-funds')">
          <div class="card-icon golden-bg">
            <van-icon name="yuan" size="28" color="#fff" />
          </div>
          <div class="card-text">村级资金</div>
        </div>
        <div class="function-card" @click="goPage('/village-contracts')">
          <div class="card-icon orange-bg">
            <van-icon name="file-text" size="28" color="#fff" />
          </div>
          <div class="card-text">村级合同</div>
        </div>
        <div class="function-card" @click="goPage('/collective-economy')">
          <div class="card-icon yellow-bg">
            <van-icon name="shop" size="28" color="#fff" />
          </div>
          <div class="card-text">集体经济</div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="users" size="20" color="#E91E63" />
          <h3>社会救助</h3>
        </div>
      </div>
      <div class="grid-5">
        <div class="function-card" @click="goPage('/subsistence-allowance')">
          <div class="card-icon navy-bg">
            <van-icon name="medal" size="28" color="#fff" />
          </div>
          <div class="card-text">低保</div>
        </div>
        <div class="function-card" @click="goPage('/disabled')">
          <div class="card-icon bronze-bg">
            <van-icon name="accessibility" size="28" color="#fff" />
          </div>
          <div class="card-text">残疾人</div>
        </div>
        <div class="function-card" @click="goPage('/special-support')">
          <div class="card-icon red-bg">
            <van-icon name="heart" size="28" color="#fff" />
          </div>
          <div class="card-text">特困供养</div>
        </div>
        <div class="function-card" @click="goPage('/social-aid')">
          <div class="card-icon white-bg">
            <van-icon name="gift-o" size="28" color="#333" />
          </div>
          <div class="card-text">社会救助</div>
        </div>
        <div class="function-card" @click="goPage('/social-security')">
          <div class="card-icon steel-bg">
            <van-icon name="credit-card" size="28" color="#fff" />
          </div>
          <div class="card-text">社保</div>
        </div>
      </div>
    </div>

    <div class="module-card">
      <div class="module-header">
        <div class="header-left">
          <van-icon name="warning-o" size="20" color="#FF5722" />
          <h3>重点关注</h3>
        </div>
      </div>
      <div class="grid-6">
        <div class="function-card" @click="goPage('/left-behind-elderly')">
          <div class="card-icon orange-dark-bg">
            <van-icon name="user" size="28" color="#fff" />
          </div>
          <div class="card-text">留守老人</div>
        </div>
        <div class="function-card" @click="goPage('/left-behind-children')">
          <div class="card-icon cream-bg">
            <van-icon name="baby" size="28" color="#FF5722" />
          </div>
          <div class="card-text">留守儿童</div>
        </div>
        <div class="function-card" @click="goPage('/drowning-prevention')">
          <div class="card-icon safety-yellow-bg">
            <van-icon name="alert-circle" size="28" color="#FF5722" />
          </div>
          <div class="card-text">防溺水</div>
        </div>
        <div class="function-card" @click="goPage('/key-personnel')">
          <div class="card-icon key-blue-bg">
            <van-icon name="user-circle-o" size="28" color="#fff" />
          </div>
          <div class="card-text">重点人员</div>
        </div>
        <div class="function-card" @click="goPage('/geological-disaster')">
          <div class="card-icon location-bg">
            <van-icon name="map-marker" size="28" color="#fff" />
          </div>
          <div class="card-text">地灾点</div>
        </div>
        <div class="function-card" @click="goPage('/slope-housing-risk')">
          <div class="card-icon danger-bg">
            <van-icon name="triangle" size="28" color="#fff" />
          </div>
          <div class="card-text">切坡建房风险户</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const villageId = ref(parseInt(route.params.id) || 1)

const villagesData = ref([
  {
    id: 1,
    name: '新店村',
    desc: '21个村民组，1568人',
    population: 1568,
    area: 8.5,
    households: 426,
    groups: 21,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20village%20landscape%20with%20traditional%20houses%20green%20mountains%20rice%20fields%20blue%20sky%20peaceful%20countryside&image_size=landscape_16_9'
  },
  {
    id: 2,
    name: '罗卜田村',
    desc: '18个村民组，1420人',
    population: 1420,
    area: 9.2,
    households: 388,
    groups: 18,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20rural%20village%20with%20citrus%20orchards%20mountain%20views%20traditional%20architecture%20agricultural%20landscape&image_size=landscape_16_9'
  },
  {
    id: 3,
    name: '兴无村',
    desc: '15个村民组，1280人',
    population: 1280,
    area: 7.8,
    households: 352,
    groups: 15,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Modern%20Chinese%20village%20development%20new%20houses%20infrastructure%20green%20environment%20prosperous&image_size=landscape_16_9'
  },
  {
    id: 4,
    name: '马坡村',
    desc: '16个村民组，1356人',
    population: 1356,
    area: 8.9,
    households: 376,
    groups: 16,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20village%20on%20slopes%20terraced%20fields%20mountain%20village%20traditional%20farming%20scenery&image_size=landscape_16_9'
  },
  {
    id: 5,
    name: '半冲村',
    desc: '12个村民组，1086人',
    population: 1086,
    area: 6.5,
    households: 302,
    groups: 12,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Valley%20village%20in%20China%20lush%20green%20valley%20peaceful%20countryside%20traditional%20houses%20stream&image_size=landscape_16_9'
  },
  {
    id: 6,
    name: '冬瓜坡村',
    desc: '14个村民组，1156人',
    population: 1156,
    area: 7.2,
    households: 328,
    groups: 14,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20village%20with%20vegetable%20fields%20agricultural%20village%20greenhouses%20farm%20landscape&image_size=landscape_16_9'
  },
  {
    id: 7,
    name: '枣子山村',
    desc: '11个村民组，983人',
    population: 983,
    area: 5.8,
    households: 276,
    groups: 11,
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chinese%20village%20with%20jujube%20trees%20hilly%20terrain%20traditional%20architecture%20scenic%20views&image_size=landscape_16_9'
  }
])

const village = computed(() => {
  return villagesData.value.find(v => v.id === villageId.value) || villagesData.value[0]
})

const goPage = (path) => {
  router.push(path)
}

onMounted(() => {
  document.title = `${village.value.name} - 村级智慧村务平台`
})
</script>

<style scoped>
.village-detail {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.header-banner {
  background: #fff;
  padding: 0;
  margin-bottom: 12px;
  overflow: hidden;
}

.banner-image {
  position: relative;
  width: 100%;
  height: 180px;
}

.banner-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 80px;
  background: linear-gradient(to top, rgba(210, 38, 48, 0.9), transparent);
}

.banner-title {
  position: absolute;
  bottom: 20px;
  left: 16px;
  font-size: 28px;
  font-weight: bold;
  color: #FFD700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.banner-subtitle {
  position: absolute;
  bottom: 8px;
  left: 16px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.9);
}

.module-card {
  background: #fff;
  margin: 0 12px 12px;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.module-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.module-header h3 {
  font-size: 17px;
  font-weight: bold;
  color: #333;
}

.grid-4 {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 8px;
}

.grid-5 {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
}

.grid-6 {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
}

.info-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 14px 6px;
  border-radius: 12px;
  color: #fff;
  text-align: center;
  transition: transform 0.2s;
}

.info-card:active {
  transform: scale(0.95);
}

.info-card.red-gradient {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

.info-card.green-gradient {
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
}

.info-card.blue-gradient {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
}

.info-card.purple-gradient {
  background: linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%);
}

.function-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 4px;
  border-radius: 12px;
  cursor: pointer;
  transition: transform 0.2s;
}

.function-card:active {
  transform: scale(0.95);
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}

.card-text {
  font-size: 12px;
  color: #333;
  text-align: center;
  font-weight: 500;
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 6px;
}

.card-value {
  font-size: 13px;
  font-weight: bold;
  margin-bottom: 3px;
}

.card-label {
  font-size: 9px;
  opacity: 0.85;
}

.card-icon.brown-bg {
  background: linear-gradient(135deg, #8B5A2B 0%, #654321 100%);
}

.card-icon.green-bg {
  background: linear-gradient(135deg, #32CD32 0%, #228B22 100%);
}

.card-icon.forest-bg {
  background: linear-gradient(135deg, #228B22 0%, #006400 100%);
}

.card-icon.sky-blue-bg {
  background: linear-gradient(135deg, #4169E1 0%, #2563EB 100%);
}

.card-icon.pink-bg {
  background: linear-gradient(135deg, #FF69B4 0%, #FF1493 100%);
}

.card-icon.lime-bg {
  background: linear-gradient(135deg, #32CD32 0%, #3CB371 100%);
}

.card-icon.golden-bg {
  background: linear-gradient(135deg, #DAA520 0%, #B8860B 100%);
}

.card-icon.orange-bg {
  background: linear-gradient(135deg, #FF8C00 0%, #FF6347 100%);
}

.card-icon.yellow-bg {
  background: linear-gradient(135deg, #FFD700 0%, #FFA500 100%);
}

.card-icon.navy-bg {
  background: linear-gradient(135deg, #4169E1 0%, #1E3A8A 100%);
}

.card-icon.bronze-bg {
  background: linear-gradient(135deg, #CD7F32 0%, #8B4513 100%);
}

.card-icon.red-bg {
  background: linear-gradient(135deg, #DC143C 0%, #8B0000 100%);
}

.card-icon.white-bg {
  background: linear-gradient(135deg, #F5F5F5 0%, #E8E8E8 100%);
  border: 1px solid #ddd;
}

.card-icon.steel-bg {
  background: linear-gradient(135deg, #708090 0%, #4682B4 100%);
}

.card-icon.orange-dark-bg {
  background: linear-gradient(135deg, #FF5722 0%, #E64A19 100%);
}

.card-icon.cream-bg {
  background: linear-gradient(135deg, #FFF8DC 0%, #FAFAD2 100%);
  border: 1px solid #FFD700;
}

.card-icon.safety-yellow-bg {
  background: linear-gradient(135deg, #FFEB3B 0%, #FFC107 100%);
}

.card-icon.key-blue-bg {
  background: linear-gradient(135deg, #2196F3 0%, #1565C0 100%);
}

.card-icon.location-bg {
  background: linear-gradient(135deg, #00BCD4 0%, #0097A7 100%);
}

.card-icon.danger-bg {
  background: linear-gradient(135deg, #FF9800 0%, #F44336 100%);
}
</style>
