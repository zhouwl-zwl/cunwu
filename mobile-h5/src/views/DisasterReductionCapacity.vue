<template>
  <div class="page-container">
    <van-nav-bar title="社区（行政村）减灾能力" left-arrow @click-left="goBack" />

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ villageData.length }}</span>
        <span class="stats-label">行政村数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ totalHouseholds }}</span>
        <span class="stats-label">总户数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ totalPopulation }}</span>
        <span class="stats-label">常住人口</span>
      </div>
    </div>

    <div class="search-section">
      <van-search v-model="searchKeyword" placeholder="搜索村名" shape="round" />
    </div>

    <div v-for="v in filteredData" :key="v.name" class="village-card">
      <div class="card-header">
        <span class="village-name">{{ v.name }}</span>
        <span class="code-tag">{{ v.code }}</span>
      </div>
      <div class="card-address">{{ v.address }}</div>

      <div class="section-label">人口与户数</div>
      <div class="metrics-grid">
        <div class="metric"><span class="metric-val">{{ v.households }}</span><span class="metric-label">总户数</span></div>
        <div class="metric"><span class="metric-val">{{ v.population }}</span><span class="metric-label">常住人口</span></div>
        <div class="metric"><span class="metric-val">{{ v.children }}</span><span class="metric-label">0-14岁</span></div>
        <div class="metric"><span class="metric-val">{{ v.elderly }}</span><span class="metric-label">65岁以上</span></div>
        <div class="metric"><span class="metric-val">{{ v.disabled }}</span><span class="metric-label">残障人员</span></div>
        <div class="metric"><span class="metric-val">{{ v.clinic }}</span><span class="metric-label">卫生室</span></div>
      </div>

      <div class="section-label">示范社区</div>
      <div class="info-row"><span class="info-label">全国综合减灾示范社区</span><span class="info-val" :class="v.nationalDemo === '是' ? 'yes' : 'no'">{{ v.nationalDemo }}</span></div>
      <div class="info-row"><span class="info-label">省级综合减灾示范社区</span><span class="info-val" :class="v.provinceDemo === '是' ? 'yes' : 'no'">{{ v.provinceDemo }}</span></div>

      <div class="section-label">隐患与预案</div>
      <div class="info-row"><span class="info-label">地质灾害隐患点清单</span><span class="info-val" :class="v.hasHazard === '是' ? 'yes' : 'no'">{{ v.hasHazard }}</span></div>
      <div class="info-row"><span class="info-label">弱势人群清单</span><span class="info-val" :class="v.hasVulnerable === '是' ? 'yes' : 'no'">{{ v.hasVulnerable }}</span></div>
      <div class="info-row"><span class="info-label">灾害类地图</span><span class="info-val" :class="v.hasMap === '是' ? 'yes' : 'no'">{{ v.hasMap }}</span></div>
      <div class="info-row"><span class="info-label">应急预案</span><span class="info-val" :class="v.hasPlan === '是' ? 'yes' : 'no'">{{ v.hasPlan }}</span></div>

      <div class="section-label">资金与物资</div>
      <div class="info-row"><span class="info-label">防灾减灾资金投入</span><span class="info-val">{{ v.fundInput }} 万元</span></div>
      <div class="info-row"><span class="info-label">物资储备方式</span><span class="info-val">{{ v.storage }}</span></div>
      <div class="info-row"><span class="info-label">储备物资折合金额</span><span class="info-val">{{ v.storageAmount }} 万元</span></div>

      <div class="section-label">人员队伍</div>
      <div class="info-row"><span class="info-label">灾害信息员</span><span class="info-val">{{ v.infoOfficer }} 人</span></div>
      <div class="info-row"><span class="info-label">登记注册志愿者</span><span class="info-val">{{ v.volunteers }} 人</span></div>
      <div class="info-row"><span class="info-label">民兵预备役</span><span class="info-val">{{ v.militia }} 人</span></div>
      <div class="info-row"><span class="info-label">避难场所</span><span class="info-val">{{ v.shelter }} 个 / 容量 {{ v.shelterCapacity }} 人</span></div>

      <div class="section-label">预警与上报</div>
      <div class="info-row"><span class="info-label">预警信息接收方式</span><span class="info-val">{{ v.warningReceive }}</span></div>
      <div class="info-row"><span class="info-label">预警信息传达方式</span><span class="info-val">{{ v.warningDeliver }}</span></div>
      <div class="info-row"><span class="info-label">灾情信息上报方式</span><span class="info-val">{{ v.reportMethod }}</span></div>

      <div class="section-label">培训与演练</div>
      <div class="info-row"><span class="info-label">培训活动</span><span class="info-val">{{ v.trainCount }} 次 / {{ v.trainPeople }} 人次</span></div>
      <div class="info-row"><span class="info-label">演练活动</span><span class="info-val">{{ v.drillCount }} 次 / {{ v.drillPeople }} 人次</span></div>

      <div class="section-label">填报信息</div>
      <div class="info-row"><span class="info-label">单位负责人</span><span class="info-val">{{ v.leader }}</span></div>
      <div class="info-row"><span class="info-label">统计负责人</span><span class="info-val">{{ v.statistician }}</span></div>
      <div class="info-row"><span class="info-label">填表人</span><span class="info-val">{{ v.filler }}</span></div>
      <div class="info-row"><span class="info-label">联系电话</span><span class="info-val">{{ v.phone }}</span></div>
      <div class="info-row"><span class="info-label">报出日期</span><span class="info-val">{{ v.reportDate }}</span></div>
    </div>
    <div v-if="filteredData.length === 0" class="empty">暂无数据</div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchKeyword = ref('')

const villageData = ref([
  { name: '马坡村', code: '431228224204', address: '湖南省怀化市芷江侗族自治县罗卜田乡马坡村核桃坪组', households: 357, population: 1094, children: 212, elderly: 195, disabled: 33, clinic: 1, nationalDemo: '否', provinceDemo: '否', hasHazard: '是', hasVulnerable: '是', hasMap: '是', hasPlan: '是', fundInput: '1.1650', storage: '实物储备;协议储备', storageAmount: '0.7000', infoOfficer: 1, volunteers: 0, militia: 5, shelter: 1, shelterCapacity: 200, warningReceive: '电话;微信', warningDeliver: '广播;电话;微信;短信', reportMethod: '电话;微信', trainCount: 1, trainPeople: 70, drillCount: 4, drillPeople: 121, leader: '李友香', statistician: '李复胜', filler: '郑卫芳', phone: '18774588838', reportDate: '2024/11/01' },
  { name: '枣子山村', code: '431228224203', address: '湖南省怀化市芷江侗族自治县罗卜田乡枣子山村枣子山组', households: 441, population: 1340, children: 217, elderly: 162, disabled: 45, clinic: 1, nationalDemo: '否', provinceDemo: '否', hasHazard: '是', hasVulnerable: '是', hasMap: '是', hasPlan: '是', fundInput: '1.3280', storage: '实物储备', storageAmount: '0.8000', infoOfficer: 1, volunteers: 0, militia: 3, shelter: 1, shelterCapacity: 200, warningReceive: '网络系统;电话;微信', warningDeliver: '广播;电话;微信;短信', reportMethod: '电话;微信', trainCount: 3, trainPeople: 150, drillCount: 4, drillPeople: 120, leader: '郑复均', statistician: '李春艳', filler: '刘安少', phone: '18074505867', reportDate: '2024/11/01' },
  { name: '半冲村', code: '431228224202', address: '湖南省怀化市芷江侗族自治县罗卜田乡半冲村村委会袁家冲组', households: 273, population: 798, children: 122, elderly: 118, disabled: 29, clinic: 1, nationalDemo: '否', provinceDemo: '否', hasHazard: '是', hasVulnerable: '是', hasMap: '是', hasPlan: '是', fundInput: '1.1400', storage: '实物储备', storageAmount: '0.7000', infoOfficer: 1, volunteers: 0, militia: 5, shelter: 1, shelterCapacity: 300, warningReceive: '网络系统;电话;微信', warningDeliver: '广播;电话;微信;短信', reportMethod: '上报系统;电话;微信', trainCount: 2, trainPeople: 90, drillCount: 4, drillPeople: 50, leader: '唐大军', statistician: '邓春艳', filler: '彭开红', phone: '15115254852', reportDate: '2024/11/01' },
  { name: '新店村', code: '431228224206', address: '湖南省怀化市芷江侗族自治县罗卜田乡新店村村委会新店组', households: 499, population: 1384, children: 187, elderly: 186, disabled: 63, clinic: 1, nationalDemo: '否', provinceDemo: '否', hasHazard: '是', hasVulnerable: '是', hasMap: '否', hasPlan: '是', fundInput: '1.7000', storage: '实物储备', storageAmount: '0.5000', infoOfficer: 1, volunteers: 0, militia: 4, shelter: 1, shelterCapacity: 500, warningReceive: '网络系统;电话;微信', warningDeliver: '广播;电话;微信;短信', reportMethod: '上报系统;电话;微信', trainCount: 1, trainPeople: 180, drillCount: 4, drillPeople: 270, leader: '杨宗武', statistician: '张露海', filler: '杨军', phone: '17774534319', reportDate: '2024/11/01' },
  { name: '罗卜田村', code: '431228224200', address: '湖南省怀化市芷江侗族自治县罗卜田乡罗卜田村村委会罗卜田组', households: 834, population: 1830, children: 180, elderly: 426, disabled: 56, clinic: 2, nationalDemo: '否', provinceDemo: '否', hasHazard: '是', hasVulnerable: '是', hasMap: '是', hasPlan: '是', fundInput: '2.1800', storage: '实物储备;协议储备', storageAmount: '1.0000', infoOfficer: 1, volunteers: 0, militia: 9, shelter: 1, shelterCapacity: 500, warningReceive: '网络系统;电话;微信', warningDeliver: '广播;电话;微信;短信;电子广告牌', reportMethod: '上报系统;电话;微信', trainCount: 1, trainPeople: 200, drillCount: 4, drillPeople: 150, leader: '钦山', statistician: '刘峰', filler: '李艳琪', phone: '15221436734', reportDate: '2024/11/01' },
  { name: '冬瓜坡村', code: '431228224205', address: '湖南省怀化市芷江侗族自治县罗卜田乡冬瓜坡村村委会郭家冲组', households: 352, population: 1043, children: 152, elderly: 150, disabled: 27, clinic: 1, nationalDemo: '否', provinceDemo: '否', hasHazard: '否', hasVulnerable: '是', hasMap: '是', hasPlan: '是', fundInput: '0.9000', storage: '实物储备', storageAmount: '0.8000', infoOfficer: 1, volunteers: 0, militia: 5, shelter: 1, shelterCapacity: 400, warningReceive: '网络系统;电话;微信', warningDeliver: '广播;电话;微信;短信', reportMethod: '上报系统;电话;微信', trainCount: 1, trainPeople: 80, drillCount: 4, drillPeople: 100, leader: '李复艳', statistician: '刘宗荣', filler: '李培健', phone: '18374555056', reportDate: '2024/11/01' },
  { name: '兴无村', code: '431228224207', address: '湖南省怀化市芷江侗族自治县罗卜田乡兴无村村委会杉湾冲组', households: 319, population: 600, children: 151, elderly: 176, disabled: 21, clinic: 1, nationalDemo: '否', provinceDemo: '否', hasHazard: '是', hasVulnerable: '是', hasMap: '是', hasPlan: '是', fundInput: '1.0700', storage: '实物储备;协议储备', storageAmount: '0.5000', infoOfficer: 1, volunteers: 0, militia: 10, shelter: 1, shelterCapacity: 200, warningReceive: '电话;微信', warningDeliver: '广播;电话;微信;短信', reportMethod: '电话;微信', trainCount: 1, trainPeople: 100, drillCount: 4, drillPeople: 90, leader: '张久云', statistician: '向勇', filler: '杨雪玲', phone: '13874560743', reportDate: '2024/11/01' }
])

const totalHouseholds = computed(() => villageData.value.reduce((s, v) => s + v.households, 0))
const totalPopulation = computed(() => villageData.value.reduce((s, v) => s + v.population, 0))
const filteredData = computed(() => {
  if (!searchKeyword.value) return villageData.value
  return villageData.value.filter(v => v.name.includes(searchKeyword.value))
})

const goBack = () => router.back()
</script>

<style scoped>
.page-container { min-height: 100vh; background: #f5f5f5; padding-bottom: 20px; }
.stats-card { display: flex; background: linear-gradient(135deg, #EF5350 0%, #B71C1C 100%); padding: 20px 12px; margin-bottom: 12px; }
.stats-item { flex: 1; text-align: center; }
.stats-num { display: block; font-size: 24px; font-weight: bold; color: #fff; margin-bottom: 4px; }
.stats-label { font-size: 12px; color: rgba(255,255,255,0.8); }
.search-section { padding: 0 12px; margin-bottom: 8px; }
.village-card { background: #fff; margin: 0 12px 12px; border-radius: 12px; padding: 16px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.card-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 4px; }
.village-name { font-size: 16px; font-weight: bold; color: #333; }
.code-tag { font-size: 11px; color: #999; background: #f0f0f0; padding: 2px 8px; border-radius: 4px; }
.card-address { font-size: 12px; color: #999; margin-bottom: 12px; }
.section-label { font-size: 12px; font-weight: bold; color: #EF5350; margin: 12px 0 6px; padding-left: 8px; border-left: 3px solid #EF5350; }
.metrics-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; padding: 12px; background: #FAFAFA; border-radius: 8px; }
.metric { text-align: center; }
.metric-val { display: block; font-size: 18px; font-weight: bold; color: #EF5350; }
.metric-label { font-size: 11px; color: #999; }
.info-row { display: flex; justify-content: space-between; align-items: center; padding: 6px 0; border-bottom: 1px solid #f5f5f5; }
.info-row:last-child { border-bottom: none; }
.info-label { font-size: 13px; color: #666; }
.info-val { font-size: 13px; color: #333; font-weight: 500; text-align: right; max-width: 60%; }
.info-val.yes { color: #4CAF50; }
.info-val.no { color: #F44336; }
.empty { text-align: center; padding: 40px; color: #999; }
</style>
