<template>
  <div class="page-container">
    <van-nav-bar title="罗卜田乡重点部位统计表" left-arrow @click-left="goBack" />

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ totalSummary }}</span>
        <span class="stats-label">合计</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ summaryData.水库 }}</span>
        <span class="stats-label">水库(座)</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ summaryData.斜坡 }}</span>
        <span class="stats-label">斜坡单元(处)</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ summaryData.切坡 }}</span>
        <span class="stats-label">切坡建房(处)</span>
      </div>
    </div>

    <van-tabs v-model="activeTab" sticky>
      <van-tab title="汇总表" name="summary">
        <div class="table-wrapper">
          <div class="table-title">罗卜田乡防汛抗旱重点区域统计表</div>
          <div class="table-scroll">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>村名称</th>
                  <th>小Ⅰ型水库</th>
                  <th>小Ⅱ型水库</th>
                  <th>中风险斜坡单元</th>
                  <th>山洪灾害易发区</th>
                  <th>切坡建房</th>
                  <th>地质灾害隐患点</th>
                  <th>合计</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="row in summaryRows" :key="row.idx">
                  <td>{{ row.idx }}</td>
                  <td class="bold">{{ row.village }}</td>
                  <td>{{ row.type1 || '-' }}</td>
                  <td>{{ row.type2 || '-' }}</td>
                  <td>{{ row.slope || '-' }}</td>
                  <td>{{ row.flood || '-' }}</td>
                  <td>{{ row.cutslope || '-' }}</td>
                  <td>{{ row.geology || '-' }}</td>
                  <td class="bold">{{ row.total }}</td>
                </tr>
                <tr class="total-row">
                  <td>-</td>
                  <td class="bold">汇总</td>
                  <td>{{ summaryData.水库1 }}</td>
                  <td>{{ summaryData.水库2 }}</td>
                  <td>{{ summaryData.斜坡 }}</td>
                  <td>{{ summaryData.山洪 }}</td>
                  <td>{{ summaryData.切坡 }}</td>
                  <td>{{ summaryData.地灾 }}</td>
                  <td class="bold">{{ totalSummary }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </van-tab>

      <van-tab title="水库" name="reservoir">
        <div class="table-wrapper">
          <div class="table-title">2025年小型水库防汛责任人名单</div>
          <div class="table-scroll">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>水库名称</th>
                  <th>总库容(万m³)</th>
                  <th>型号</th>
                  <th>所在地</th>
                  <th>坝型</th>
                  <th>坝高(m)</th>
                  <th>县级责任人</th>
                  <th>职务</th>
                  <th>电话</th>
                  <th>行政负责人</th>
                  <th>职务</th>
                  <th>电话</th>
                  <th>管护员</th>
                  <th>电话</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="r in reservoirData" :key="r.idx">
                  <td>{{ r.idx }}</td>
                  <td class="bold">{{ r.name }}</td>
                  <td>{{ r.capacity }}</td>
                  <td>{{ r.type }}</td>
                  <td>{{ r.location }}</td>
                  <td>{{ r.damType }}</td>
                  <td>{{ r.damHeight }}</td>
                  <td>{{ r.countyLeader }}</td>
                  <td>{{ r.countyTitle }}</td>
                  <td>{{ r.countyPhone }}</td>
                  <td>{{ r.townLeader }}</td>
                  <td>{{ r.townTitle }}</td>
                  <td>{{ r.townPhone }}</td>
                  <td>{{ r.keeper }}</td>
                  <td>{{ r.keeperPhone }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </van-tab>

      <van-tab title="中风险斜坡" name="slope">
        <div class="table-wrapper">
          <div class="table-title">中风险以上斜坡（沟谷）单元地质灾害六员共管统计表</div>
          <div class="table-scroll">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>村</th>
                  <th>组/地名</th>
                  <th>编号</th>
                  <th>户数</th>
                  <th>人数</th>
                  <th>房屋(栋)</th>
                  <th>风险等级</th>
                  <th>乡级责任人</th>
                  <th>职务</th>
                  <th>电话</th>
                  <th>村级责任人</th>
                  <th>电话</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="r in slopeData" :key="r.idx">
                  <td>{{ r.idx }}</td>
                  <td>{{ r.village }}</td>
                  <td>{{ r.group }}</td>
                  <td>{{ r.code }}</td>
                  <td>{{ r.households }}</td>
                  <td>{{ r.people }}</td>
                  <td>{{ r.houses }}</td>
                  <td><span class="level-tag" :class="r.level === '高风险' ? 'high' : 'mid'">{{ r.level }}</span></td>
                  <td>{{ r.townLeader }}</td>
                  <td>{{ r.townTitle }}</td>
                  <td>{{ r.townPhone }}</td>
                  <td>{{ r.villageLeader }}</td>
                  <td>{{ r.villagePhone }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </van-tab>

      <van-tab title="山洪灾害" name="flood">
        <div class="table-wrapper">
          <div class="table-title">2025年山洪灾害危险区责任人信息</div>
          <div class="table-scroll">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>村</th>
                  <th>危险区名称</th>
                  <th>户数</th>
                  <th>人口</th>
                  <th>房屋</th>
                  <th>乡级责任人</th>
                  <th>职务</th>
                  <th>电话</th>
                  <th>村级责任人</th>
                  <th>职务</th>
                  <th>电话</th>
                  <th>监测员</th>
                  <th>电话</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="r in floodData" :key="r.idx">
                  <td>{{ r.idx }}</td>
                  <td>{{ r.village }}</td>
                  <td class="bold">{{ r.area }}</td>
                  <td>{{ r.households }}</td>
                  <td>{{ r.people }}</td>
                  <td>{{ r.houses }}</td>
                  <td>{{ r.townLeader }}</td>
                  <td>{{ r.townTitle }}</td>
                  <td>{{ r.townPhone }}</td>
                  <td>{{ r.villageLeader }}</td>
                  <td>{{ r.villageTitle }}</td>
                  <td>{{ r.villagePhone }}</td>
                  <td>{{ r.monitor }}</td>
                  <td>{{ r.monitorPhone }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeTab = ref('summary')

const summaryRows = ref([
  { idx: 1, village: '半冲村', type1: '', type2: '', slope: 4, flood: 5, cutslope: 49, geology: 1, total: 59 },
  { idx: 2, village: '冬瓜坡村', type1: 1, type2: '', slope: '', flood: '', cutslope: 35, geology: '', total: 36 },
  { idx: 3, village: '罗卜田村', type1: '', type2: 1, slope: 8, flood: '', cutslope: 103, geology: 1, total: 113 },
  { idx: 4, village: '马坡村', type1: '', type2: 1, slope: 2, flood: '', cutslope: 31, geology: 1, total: 35 },
  { idx: 5, village: '新店村', type1: '', type2: 2, slope: 2, flood: 1, cutslope: 43, geology: '', total: 48 },
  { idx: 6, village: '兴无村', type1: '', type2: '', slope: 1, flood: 1, cutslope: 60, geology: 1, total: 63 },
  { idx: 7, village: '枣子山村', type1: '', type2: 2, slope: 2, flood: '', cutslope: 44, geology: 1, total: 49 }
])

const summaryData = { 水库1: 1, 水库2: 6, 水库: 7, 斜坡: 19, 山洪: 7, 切坡: 365, 地灾: 5 }
const totalSummary = computed(() => 403)

const reservoirData = ref([
  { idx: 1, name: '黄花坪', capacity: 174, type: '小(一)型', location: '冬瓜坡村', damType: '心墙坝', damHeight: 25, countyLeader: '唐江鲤', countyTitle: '县政协主席', countyPhone: '13874533233', townLeader: '黄伟', townTitle: '党委书记', townPhone: '15115187567', keeper: '李复君', keeperPhone: '17308459039' },
  { idx: 2, name: '木田冲', capacity: 71.3, type: '小(二)型', location: '罗卜田村', damType: '重力坝', damHeight: 20, countyLeader: '李沛', countyTitle: '人大主席', countyPhone: '13638457602', townLeader: '刘锋', townTitle: '村干部', townPhone: '13647457437', keeper: '龙客有', keeperPhone: '13637458826' },
  { idx: 3, name: '桐牯冲', capacity: 13.6, type: '小(二)型', location: '新店村', damType: '心墙坝', damHeight: 19, countyLeader: '李卓玲', countyTitle: '乡长', countyPhone: '13787572760', townLeader: '龙客有', townTitle: '群众', townPhone: '13637458826', keeper: '龙客有', keeperPhone: '13637458826' },
  { idx: 4, name: '船坡', capacity: 62.2, type: '小(二)型', location: '马坡村', damType: '心墙坝', damHeight: 15, countyLeader: '黄婧婕', countyTitle: '乡纪委书记', countyPhone: '15807452222', townLeader: '周太和', townTitle: '群众', townPhone: '15581585328', keeper: '周太和', keeperPhone: '15581585328' },
  { idx: 5, name: '桥头冲', capacity: 22, type: '小(二)型', location: '枣子山村', damType: '心墙坝', damHeight: 12, countyLeader: '付磊', countyTitle: '党委副书记', countyPhone: '13469350956', townLeader: '杨芳金', townTitle: '群众', townPhone: '13874428104', keeper: '杨芳金', keeperPhone: '13874428104' },
  { idx: 6, name: '苗圃洞', capacity: 30.9, type: '小(二)型', location: '枣子山村', damType: '心墙坝', damHeight: 14.95, countyLeader: '龙君屹', countyTitle: '政法武装委员、副乡长', countyPhone: '13378926513', townLeader: '刘正军', townTitle: '群众', townPhone: '13707450945', keeper: '刘正军', keeperPhone: '13707450945' },
  { idx: 7, name: '枫木湾', capacity: 33.1, type: '小(二)型', location: '新店村', damType: '心墙坝', damHeight: 38, countyLeader: '杨承明', countyTitle: '副乡长', countyPhone: '13467400837', townLeader: '龙克志', townTitle: '群众', townPhone: '15574565862', keeper: '龙克志', keeperPhone: '15574565862' }
])

const slopeData = ref([
  { idx: 1, village: '兴无村', group: '大屋场组、社冲组', code: 'XP22402878', households: 20, people: 69, houses: 23, level: '中风险', townLeader: '符明', townTitle: '副乡长', townPhone: '13487453792', villageLeader: '张久云', villagePhone: '18274523060' },
  { idx: 2, village: '罗卜田村', group: '高坡界组', code: 'XP22403205', households: 8, people: 9, houses: 25, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 3, village: '罗卜田村', group: '洋火坪组', code: 'XP22403250', households: 23, people: 30, houses: 25, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 4, village: '罗卜田村', group: '罗卜田小学斜坡', code: 'XP22403290', households: 270, people: 9, houses: 9, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 5, village: '罗卜田村', group: '湾泉组', code: 'XP22403299', households: 3, people: 9, houses: 3, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 6, village: '罗卜田村', group: '罗卜田斜坡', code: 'XP22403305', households: 120, people: 40, houses: 40, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 7, village: '罗卜田村', group: '乡人民政府斜坡', code: 'XP22403318', households: 30, people: 11, houses: 11, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 8, village: '马坡村', group: '土寨组', code: 'XP22403337', households: 2, people: 9, houses: 3, level: '中风险', townLeader: '黄婧婕', townTitle: '纪委书记', townPhone: '15807452222', villageLeader: '李友香', villagePhone: '18075598897' },
  { idx: 9, village: '马坡村', group: '团坡界组', code: 'XP22403385', households: 32, people: 72, houses: 36, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 10, village: '罗卜田村', group: '团坡埌斜坡', code: 'XP22403395', households: 18, people: 38, houses: 19, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 11, village: '枣子山村', group: '大洋溪组', code: 'XP22403529', households: 14, people: 42, houses: 14, level: '高风险', townLeader: '龙君屹', townTitle: '武装委员、副乡长', townPhone: '17670434237', villageLeader: '郑复均', villagePhone: '14760734583' },
  { idx: 12, village: '枣子山村', group: '渗水塘组', code: 'XP22403545', households: 13, people: 33, houses: 11, level: '中风险', townLeader: '龙君屹', townTitle: '武装委员、副乡长', townPhone: '17670434237', villageLeader: '郑复均', villagePhone: '14760734583' },
  { idx: 13, village: '半冲村', group: '下湾组', code: 'XP22403633', households: 18, people: 57, houses: 19, level: '中风险', townLeader: '付磊', townTitle: '副书记', townPhone: '13469350956', villageLeader: '唐大军', villagePhone: '17774540127' },
  { idx: 14, village: '半冲村', group: '袁家冲组', code: 'XP22403641', households: 4, people: 15, houses: 5, level: '中风险', townLeader: '付磊', townTitle: '副书记', townPhone: '13469350956', villageLeader: '唐大军', villagePhone: '17774540127' },
  { idx: 15, village: '半冲村', group: '袁家冲组', code: 'XP22403651', households: 10, people: 39, houses: 13, level: '中风险', townLeader: '付磊', townTitle: '副书记', townPhone: '13469350956', villageLeader: '唐大军', villagePhone: '17774540127' },
  { idx: 16, village: '半冲村', group: '塘铺湾', code: 'XP22403714', households: 9, people: 30, houses: 10, level: '高风险', townLeader: '付磊', townTitle: '副书记', townPhone: '13469350956', villageLeader: '唐大军', villagePhone: '17774540127' },
  { idx: 17, village: '罗卜田村', group: '木田冲斜坡1', code: 'XP224D0021', households: 10, people: 45, houses: 15, level: '中风险', townLeader: '李沛', townTitle: '人大主席', townPhone: '13638457602', villageLeader: '钦山', villagePhone: '13874417046' },
  { idx: 18, village: '新店村', group: '磨刀塖斜坡', code: '4312281602819', households: 6, people: 21, houses: 7, level: '中风险', townLeader: '杨承明', townTitle: '副乡长', townPhone: '13467400837', villageLeader: '杨宗武', villagePhone: '17774534319' },
  { idx: 19, village: '新店村', group: '上黄双溪斜坡', code: '4312281602910', households: 6, people: 18, houses: 6, level: '中风险', townLeader: '杨承明', townTitle: '副乡长', townPhone: '13467400837', villageLeader: '杨宗武', villagePhone: '17774534319' }
])

const floodData = ref([
  { idx: 1, village: '半冲村', area: '雄家园组', households: 3, people: 23, houses: 3, townLeader: '付磊', townTitle: '罗卜田乡党委副书记', townPhone: '13469350956', villageLeader: '唐大军', villageTitle: '半冲村村书记', villagePhone: '17774540127', monitor: '许昆成', monitorPhone: '17775194297' },
  { idx: 2, village: '半冲村', area: '大水江组', households: 3, people: 15, houses: 3, townLeader: '付磊', townTitle: '罗卜田乡党委副书记', townPhone: '13469350956', villageLeader: '唐大军', villageTitle: '半冲村村书记', villagePhone: '17774540127', monitor: '张廷喜', monitorPhone: '19118571579' },
  { idx: 3, village: '半冲村', area: '堰塘冲组', households: 2, people: 15, houses: 2, townLeader: '付磊', townTitle: '罗卜田乡党委副书记', townPhone: '13469350956', villageLeader: '唐大军', villageTitle: '半冲村村书记', villagePhone: '17774540127', monitor: '彭明生', monitorPhone: '13787585866' },
  { idx: 4, village: '兴无村', area: '黄家溪组', households: 6, people: 39, houses: 6, townLeader: '李卓玲', townTitle: '罗卜田乡人民政府乡长', townPhone: '13787572760', villageLeader: '张久云', villageTitle: '兴无村村书记', villagePhone: '18274523060', monitor: '张邦锡', monitorPhone: '15111544856' },
  { idx: 5, village: '半冲村', area: '园家冲组', households: 2, people: 14, houses: 2, townLeader: '付磊', townTitle: '罗卜田乡党委副书记', townPhone: '13469350956', villageLeader: '唐大军', villageTitle: '半冲村村书记', villagePhone: '17774540127', monitor: '李泽财', monitorPhone: '17774595337' },
  { idx: 6, village: '半冲村', area: '下塘组', households: 4, people: 31, houses: 4, townLeader: '付磊', townTitle: '罗卜田乡党委副书记', townPhone: '13469350956', villageLeader: '唐大军', villageTitle: '半冲村村书记', villagePhone: '17774540127', monitor: '彭勇', monitorPhone: '18244803231' },
  { idx: 7, village: '枣子山村', area: '渗水塘组', households: 15, people: 100, houses: 15, townLeader: '龙君屹', townTitle: '罗卜田乡武装政法委员', townPhone: '17670434237', villageLeader: '郑复均', villageTitle: '枣子山村村书记', villagePhone: '14760734583', monitor: '刘正兵', monitorPhone: '18797603375' }
])

const goBack = () => router.back()
</script>

<style scoped>
.page-container { min-height: 100vh; background: #f5f5f5; padding-bottom: 20px; }
.stats-card { display: flex; background: linear-gradient(135deg, #FFA726 0%, #E65100 100%); padding: 20px 12px; margin-bottom: 12px; }
.stats-item { flex: 1; text-align: center; }
.stats-num { display: block; font-size: 22px; font-weight: bold; color: #fff; margin-bottom: 4px; }
.stats-label { font-size: 11px; color: rgba(255,255,255,0.8); }
.table-wrapper { background: #fff; margin: 12px; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.table-title { font-size: 13px; font-weight: bold; color: #333; padding: 14px 12px; text-align: center; background: linear-gradient(135deg, #FFE0B2 0%, #FFCC80 100%); border-bottom: 2px solid #FFA726; }
.table-scroll { overflow-x: auto; -webkit-overflow-scrolling: touch; }
.data-table { width: 100%; min-width: 900px; border-collapse: collapse; font-size: 12px; }
.data-table thead { background: linear-gradient(135deg, #FFA726 0%, #E65100 100%); position: sticky; top: 0; z-index: 10; }
.data-table th { padding: 10px 6px; text-align: center; color: #fff; font-weight: 600; white-space: nowrap; border-right: 1px solid rgba(255,255,255,0.15); }
.data-table th:last-child { border-right: none; }
.data-table tbody tr:nth-child(even) { background: #FFF8E1; }
.data-table tbody tr:nth-child(odd) { background: #fff; }
.data-table td { padding: 8px 6px; text-align: center; color: #333; white-space: nowrap; border-right: 1px solid #f5e0c0; border-bottom: 1px solid #f5ebe8; }
.data-table td:last-child { border-right: none; }
.data-table td.bold { font-weight: bold; }
.total-row { background: #FFE0B2 !important; font-weight: bold; }
.level-tag { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; color: #fff; }
.level-tag.high { background: #F44336; }
.level-tag.mid { background: #FF9800; }
</style>
