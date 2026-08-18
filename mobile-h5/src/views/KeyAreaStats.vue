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

      <van-tab title="切坡建房" name="cutslope">
        <div class="search-section">
          <van-search
            v-model="cutslopeSearch"
            placeholder="搜索村名/户主姓名/电话"
            shape="round"
          />
          <div class="search-tip">共 {{ filteredCutslope.length }} 条记录</div>
        </div>
        <div class="table-wrapper">
          <div class="table-scroll">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>所属村</th>
                  <th>户主姓名</th>
                  <th>全家人数</th>
                  <th>长住人数</th>
                  <th>电话</th>
                  <th>风险等级</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="r in filteredCutslope" :key="r.idx">
                  <td>{{ r.idx }}</td>
                  <td>{{ r.village }}</td>
                  <td class="bold">{{ r.name }}</td>
                  <td>{{ r.familySize || '-' }}</td>
                  <td>{{ r.residentSize || '-' }}</td>
                  <td>{{ r.phone || '-' }}</td>
                  <td><span class="level-tag" :class="r.riskLevel === '高' ? 'high' : 'mid'">{{ r.riskLevel }}</span></td>
                </tr>
                <tr v-if="filteredCutslope.length === 0">
                  <td colspan="7" class="empty-cell">暂无数据</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </van-tab>

      <van-tab title="地灾点" name="geology">
        <div class="geology-list">
          <div v-for="r in geoData" :key="r.idx" class="geo-card">
            <div class="geo-card-header">
              <span class="geo-idx">{{ r.idx }}</span>
              <span class="geo-name">{{ r.name }}</span>
              <span class="level-tag" :class="r.riskLevel.includes('高') ? 'high' : 'mid'">{{ r.riskLevel }}</span>
            </div>
            <div class="geo-info-grid">
              <div class="geo-info-item"><span class="geo-label">统一编号</span><span class="geo-val">{{ r.code || '-' }}</span></div>
              <div class="geo-info-item"><span class="geo-label">隐患点位置</span><span class="geo-val">{{ r.location }}</span></div>
              <div class="geo-info-item"><span class="geo-label">灾害类型</span><span class="geo-val">{{ r.disasterType }}</span></div>
              <div class="geo-info-item"><span class="geo-label">隐患规模</span><span class="geo-val">{{ r.scale || '-' }}万m³</span></div>
              <div class="geo-info-item"><span class="geo-label">规模等级</span><span class="geo-val">{{ r.scaleLevel || '-' }}</span></div>
              <div class="geo-info-item"><span class="geo-label">险情等级</span><span class="geo-val">{{ r.dangerLevel || '-' }}</span></div>
              <div class="geo-info-item"><span class="geo-label">威胁户数</span><span class="geo-val">{{ r.households || '-' }}户</span></div>
              <div class="geo-info-item"><span class="geo-label">威胁人数</span><span class="geo-val">{{ r.people || '-' }}人</span></div>
              <div class="geo-info-item"><span class="geo-label">威胁财产</span><span class="geo-val">{{ r.property || '-' }}万</span></div>
              <div class="geo-info-item"><span class="geo-label">稳定性</span><span class="geo-val">{{ r.stability || '-' }}</span></div>
              <div class="geo-info-item"><span class="geo-label">已采取措施</span><span class="geo-val">{{ r.measures || '-' }}</span></div>
            </div>
            <div class="geo-section-title">防灾责任人</div>
            <div class="geo-people-grid">
              <div class="geo-person">
                <span class="geo-person-role">县级</span>
                <span class="geo-person-name">{{ r.countyName }}</span>
                <span class="geo-person-phone">{{ r.countyPhone || '-' }}</span>
              </div>
              <div class="geo-person">
                <span class="geo-person-role">乡级</span>
                <span class="geo-person-name">{{ r.townName }}</span>
                <span class="geo-person-phone">{{ r.townPhone || '-' }}</span>
              </div>
              <div class="geo-person">
                <span class="geo-person-role">村级</span>
                <span class="geo-person-name">{{ r.villageName }}</span>
                <span class="geo-person-phone">{{ r.villagePhone || '-' }}</span>
              </div>
              <div class="geo-person" v-if="r.point1Name">
                <span class="geo-person-role">点长1</span>
                <span class="geo-person-name">{{ r.point1Name }}</span>
                <span class="geo-person-phone">{{ r.point1Phone || '-' }}</span>
              </div>
              <div class="geo-person" v-if="r.point2Name">
                <span class="geo-person-role">点长2</span>
                <span class="geo-person-name">{{ r.point2Name }}</span>
                <span class="geo-person-phone">{{ r.point2Phone || '-' }}</span>
              </div>
            </div>
          </div>
        </div>
      </van-tab>

      <van-tab title="地灾点人员" name="geoPeople">
        <div class="search-section">
          <van-search
            v-model="geoPeopleSearch"
            placeholder="搜索隐患点/村名/姓名"
            shape="round"
          />
          <div class="search-tip">共 {{ filteredGeoPeople.length }} 条记录</div>
        </div>
        <div class="table-wrapper">
          <div class="table-scroll">
            <table class="data-table">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>村</th>
                  <th>组</th>
                  <th>隐患点</th>
                  <th>姓名</th>
                  <th>年龄</th>
                  <th>关系</th>
                  <th>常住</th>
                  <th>电话</th>
                  <th>备注</th>
                  <th>5.27转移</th>
                  <th>6.20转移</th>
                  <th>6.21转移</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="r in filteredGeoPeople" :key="r.idx" :class="{ 'head-row': r.isHead }">
                  <td>{{ r.idx }}</td>
                  <td>{{ r.village }}</td>
                  <td>{{ r.group || '-' }}</td>
                  <td class="bold">{{ r.pointName || '-' }}</td>
                  <td class="bold">{{ r.name }}</td>
                  <td>{{ r.age || '-' }}</td>
                  <td>{{ r.relation || '-' }}</td>
                  <td>{{ r.resident || '-' }}</td>
                  <td>{{ r.phone || '-' }}</td>
                  <td>{{ r.remark || '-' }}</td>
                  <td>{{ r.transfer1 || '-' }}</td>
                  <td>{{ r.transfer2 || '-' }}</td>
                  <td>{{ r.transfer3 || '-' }}</td>
                </tr>
                <tr v-if="filteredGeoPeople.length === 0">
                  <td colspan="13" class="empty-cell">暂无数据</td>
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
const cutslopeSearch = ref('')
const geoPeopleSearch = ref('')

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

const cutslopeData = ref([
    { idx: 1, village: '兴无村', name: '向太东', familySize: '4', residentSize: '', phone: '19976846467', riskLevel: '中' },
    { idx: 2, village: '兴无村', name: '向国梅', familySize: '1', residentSize: '1', phone: '15674590278', riskLevel: '中' },
    { idx: 3, village: '兴无村', name: '张邦政', familySize: '5', residentSize: '2', phone: '15580632620', riskLevel: '中' },
    { idx: 4, village: '兴无村', name: '张家沅', familySize: '4', residentSize: '1', phone: '18574557252', riskLevel: '中' },
    { idx: 5, village: '兴无村', name: '张邦平', familySize: '3', residentSize: '2', phone: '13874423181', riskLevel: '中' },
    { idx: 6, village: '兴无村', name: '张邦政', familySize: '5', residentSize: '2', phone: '15580632620', riskLevel: '中' },
    { idx: 7, village: '兴无村新屋田组', name: '邓开云', familySize: '4', residentSize: '', phone: '15111561229', riskLevel: '中' },
    { idx: 8, village: '兴无村', name: '张邦源', familySize: '3', residentSize: '', phone: '18707458779', riskLevel: '中' },
    { idx: 9, village: '冬瓜坡村唐山冲组', name: '邓开梅', familySize: '4', residentSize: '2', phone: '18244832125', riskLevel: '中' },
    { idx: 10, village: '兴无村', name: '张隆均', familySize: '6', residentSize: '', phone: '15574596865', riskLevel: '中' },
    { idx: 11, village: '兴无村', name: '张道根', familySize: '4', residentSize: '2', phone: '13974577691', riskLevel: '中' },
    { idx: 12, village: '兴无村', name: '张白权', familySize: '5', residentSize: '', phone: '18774734148', riskLevel: '中' },
    { idx: 13, village: '兴无村', name: '张隆荣', familySize: '2', residentSize: '2', phone: '13467400410', riskLevel: '中' },
    { idx: 14, village: '兴无村', name: '张隆海', familySize: '5', residentSize: '2', phone: '18674561962', riskLevel: '中' },
    { idx: 15, village: '兴无村黄家塘组', name: '黄泽根', familySize: '3', residentSize: '2', phone: '18374548345', riskLevel: '中' },
    { idx: 16, village: '兴无村黄家溪组', name: '张邦锡', familySize: '4', residentSize: '1', phone: '15111544856', riskLevel: '中' },
    { idx: 17, village: '兴无村黄家塘组', name: '黄泽元', familySize: '3', residentSize: '2', phone: '15974028148', riskLevel: '中' },
    { idx: 18, village: '兴无村黄家溪组', name: '张邦元', familySize: '2', residentSize: '1', phone: '15974021658', riskLevel: '中' },
    { idx: 19, village: '兴无村黄家溪组', name: '张邦桥', familySize: '6', residentSize: '2', phone: '18797613309', riskLevel: '中' },
    { idx: 20, village: '兴无村黄家塘组', name: '张道军', familySize: '4', residentSize: '2', phone: '18374544356', riskLevel: '中' },
    { idx: 21, village: '兴无村', name: '秦继会', familySize: '6', residentSize: '2', phone: '17769429697', riskLevel: '中' },
    { idx: 22, village: '兴无村', name: '张海军', familySize: '4', residentSize: '', phone: '18670483188', riskLevel: '中' },
    { idx: 23, village: '兴无村', name: '向太均', familySize: '5', residentSize: '3', phone: '18244833358', riskLevel: '中' },
    { idx: 24, village: '冬瓜坡村杨家浪组', name: '江贤高', familySize: '4', residentSize: '2', phone: '19918517099', riskLevel: '中' },
    { idx: 25, village: '冬瓜坡村杨家浪组', name: '李成', familySize: '2', residentSize: '2', phone: '19152093857', riskLevel: '中' },
    { idx: 26, village: '兴无村', name: '向光其', familySize: '5', residentSize: '1', phone: '18244801549', riskLevel: '中' },
    { idx: 27, village: '兴无村', name: '向光松', familySize: '2', residentSize: '1', phone: '18574557951', riskLevel: '中' },
    { idx: 28, village: '冬瓜坡村杨家浪组', name: '江贤国', familySize: '2', residentSize: '1', phone: '15581586631', riskLevel: '中' },
    { idx: 29, village: '兴无村', name: '向光沅', familySize: '6', residentSize: '', phone: '18674526491', riskLevel: '中' },
    { idx: 30, village: '冬瓜坡村杨家浪组', name: '黄平', familySize: '3', residentSize: '2', phone: '18574552309', riskLevel: '中' },
    { idx: 31, village: '冬瓜坡村塘山冲组', name: '邓长海', familySize: '2', residentSize: '4', phone: '17769221509', riskLevel: '中' },
    { idx: 32, village: '冬瓜坡村杨家浪组', name: '李复友', familySize: '1', residentSize: '1', phone: '18274519357', riskLevel: '中' },
    { idx: 33, village: '冬瓜坡村湾里组', name: '杨必冬', familySize: '2', residentSize: '2', phone: '15074590334', riskLevel: '中' },
    { idx: 34, village: '兴无村', name: '向开均', familySize: '1', residentSize: '', phone: '13874581741', riskLevel: '中' },
    { idx: 35, village: '冬瓜坡村许家山组', name: '江贤梅', familySize: '1', residentSize: '', phone: '15607455260', riskLevel: '中' },
    { idx: 36, village: '冬瓜坡村郭家冲', name: '李复桥', familySize: '3', residentSize: '2', phone: '19107454549', riskLevel: '中' },
    { idx: 37, village: '冬瓜坡村湾里组', name: '覃现凤', familySize: '1', residentSize: '1', phone: '19145953957', riskLevel: '中' },
    { idx: 38, village: '兴无村', name: '杨元沅', familySize: '5', residentSize: '3', phone: '15673099781', riskLevel: '中' },
    { idx: 39, village: '兴无村', name: '杨胜荣', familySize: '3', residentSize: '2', phone: '15526175110', riskLevel: '中' },
    { idx: 40, village: '兴无村', name: '向开云', familySize: '2', residentSize: '', phone: '13357130200', riskLevel: '中' },
    { idx: 41, village: '兴无村', name: '张建', familySize: '4', residentSize: '1', phone: '18797553894', riskLevel: '中' },
    { idx: 42, village: '兴无村', name: '向开政', familySize: '3', residentSize: '1', phone: '15348451306', riskLevel: '中' },
    { idx: 43, village: '兴无村', name: '张邦生', familySize: '4', residentSize: '2', phone: '18244864148', riskLevel: '中' },
    { idx: 44, village: '兴无村', name: '张邦行', familySize: '2', residentSize: '2', phone: '13789264207', riskLevel: '中' },
    { idx: 45, village: '兴无村郭家塘组', name: '张邦海', familySize: '3', residentSize: '2', phone: '19974555931', riskLevel: '中' },
    { idx: 46, village: '兴无村', name: '向开忠', familySize: '2', residentSize: '', phone: '15574584272', riskLevel: '中' },
    { idx: 47, village: '兴无村', name: '杨胜沅', familySize: '5', residentSize: '2', phone: '17769249829', riskLevel: '中' },
    { idx: 48, village: '兴无村败泥古组', name: '彭开中', familySize: '4', residentSize: '1', phone: '18574518297', riskLevel: '中' },
    { idx: 49, village: '兴无村败泥古组', name: '向宗宏', familySize: '5', residentSize: '1', phone: '17769225909', riskLevel: '中' },
    { idx: 50, village: '兴无村败泥古组', name: '郑学松', familySize: '1', residentSize: '1', phone: '17769244182', riskLevel: '中' },
    { idx: 51, village: '兴无村败泥古组', name: '张青梅', familySize: '1', residentSize: '1', phone: '15526180007', riskLevel: '中' },
    { idx: 52, village: '兴无村', name: '向铁均', familySize: '4', residentSize: '2', phone: '17775195969', riskLevel: '中' },
    { idx: 53, village: '冬瓜坡村郭家冲', name: '李复艳', familySize: '3', residentSize: '2', phone: '15674566176', riskLevel: '中' },
    { idx: 54, village: '兴无村', name: '张家成', familySize: '2', residentSize: '1', phone: '15115278831', riskLevel: '中' },
    { idx: 55, village: '冬瓜坡村郭家冲组', name: '李复华', familySize: '3', residentSize: '2', phone: '17769228069', riskLevel: '中' },
    { idx: 56, village: '冬瓜坡村郭家冲组', name: '黄幼生', familySize: '3', residentSize: '1', phone: '18774744455', riskLevel: '中' },
    { idx: 57, village: '新店村磨刀堰组', name: '舒云珍', familySize: '3', residentSize: '1', phone: '13047297556', riskLevel: '中' },
    { idx: 58, village: '新店村对门冲组', name: '朱小红', familySize: '3', residentSize: '1', phone: '13618569077', riskLevel: '中' },
    { idx: 59, village: '新店村对门冲组', name: '李林', familySize: '4', residentSize: '4', phone: '13469343023', riskLevel: '中' },
    { idx: 60, village: '新店村磨刀堰组', name: '朱永军', familySize: '4', residentSize: '3', phone: '17775196267', riskLevel: '中' },
    { idx: 61, village: '新店村磨刀堰组', name: '朱永洪', familySize: '2', residentSize: '1', phone: '19974537739', riskLevel: '中' },
    { idx: 62, village: '兴无村', name: '向光贵', familySize: '3', residentSize: '2', phone: '13034894613', riskLevel: '中' },
    { idx: 63, village: '冬瓜坡村水竹园组', name: '李复银', familySize: '3', residentSize: '2', phone: '15674532380', riskLevel: '中' },
    { idx: 64, village: '冬瓜坡村罗家冲组', name: '李复龙', familySize: '5', residentSize: '2', phone: '15674525791', riskLevel: '中' },
    { idx: 65, village: '新店村对门冲组', name: '张邦海', familySize: '3', residentSize: '2', phone: '19974555931', riskLevel: '中' },
    { idx: 66, village: '新店村勾计山组', name: '张家楷', familySize: '4', residentSize: '2', phone: '13789164969', riskLevel: '中' },
    { idx: 67, village: '兴无村杜冲组', name: '杨必清', familySize: '3', residentSize: '1', phone: '17769249078', riskLevel: '中' },
    { idx: 68, village: '新店村磨刀堰组', name: '朱永平', familySize: '3', residentSize: '1', phone: '19892972468', riskLevel: '中' },
    { idx: 69, village: '兴无村', name: '向灵明', familySize: '4', residentSize: '2', phone: '18570999038', riskLevel: '中' },
    { idx: 70, village: '新店村方木坪组', name: '张邦银', familySize: '6', residentSize: '5', phone: '15274555748', riskLevel: '中' },
    { idx: 71, village: '兴无村', name: '向宗发', familySize: '2', residentSize: '2', phone: '13467404680', riskLevel: '中' },
    { idx: 72, village: '新店村方木坪组', name: '张元海', familySize: '2', residentSize: '2', phone: '19985361670', riskLevel: '中' },
    { idx: 73, village: '兴无村', name: '向开沅', familySize: '1', residentSize: '', phone: '18627486568', riskLevel: '中' },
    { idx: 74, village: '兴无村', name: '向宗浩', familySize: '4', residentSize: '1', phone: '13787549206', riskLevel: '中' },
    { idx: 75, village: '兴无村', name: '向开林', familySize: '3', residentSize: '1', phone: '17398701170', riskLevel: '中' },
    { idx: 76, village: '新店村磨刀堰组', name: '张家明', familySize: '1', residentSize: '', phone: '13973094982', riskLevel: '中' },
    { idx: 77, village: '兴无村', name: '姚本权', familySize: '3', residentSize: '1', phone: '13034873273', riskLevel: '中' },
    { idx: 78, village: '新店村对门冲组', name: '向宗平', familySize: '4', residentSize: '2', phone: '15674551708', riskLevel: '中' },
    { idx: 79, village: '兴无村', name: '姚本元', familySize: '4', residentSize: '2', phone: '15580685630', riskLevel: '中' },
    { idx: 80, village: '新店村罗家冲组', name: '张雨', familySize: '4', residentSize: '2', phone: '13789293766', riskLevel: '中' },
    { idx: 81, village: '新店村方木坪组', name: '张华', familySize: '4', residentSize: '2', phone: '17374524519', riskLevel: '中' },
    { idx: 82, village: '兴无村', name: '刘道阳', familySize: '4', residentSize: '', phone: '15869918798', riskLevel: '中' },
    { idx: 83, village: '冬瓜坡村黄花坪组', name: '李复海', familySize: '5', residentSize: '3', phone: '15574549924', riskLevel: '中' },
    { idx: 84, village: '兴无村', name: '杨宗海', familySize: '4', residentSize: '2', phone: '15580661975', riskLevel: '中' },
    { idx: 85, village: '冬瓜坡村田冲组', name: '李志华', familySize: '5', residentSize: '2', phone: '15399835677', riskLevel: '中' },
    { idx: 86, village: '冬瓜坡村黄花坪组', name: '李培亮', familySize: '5', residentSize: '3', phone: '17374523839', riskLevel: '中' },
    { idx: 87, village: '兴无村', name: '杨宗元', familySize: '3', residentSize: '', phone: '13034864260', riskLevel: '中' },
    { idx: 88, village: '冬瓜坡村黄花坪组', name: '李泽河、李泽九', familySize: '9', residentSize: '', phone: '15111500999', riskLevel: '中' },
    { idx: 89, village: '冬瓜坡村黄花坪组', name: '李保兴', familySize: '5', residentSize: '2', phone: '17365780232', riskLevel: '中' },
    { idx: 90, village: '兴无村', name: '杨宗凯', familySize: '4', residentSize: '1', phone: '19918456063', riskLevel: '中' },
    { idx: 91, village: '冬瓜坡村泥冲组', name: '李培胜', familySize: '4', residentSize: '2', phone: '19974563823', riskLevel: '中' },
    { idx: 92, village: '兴无村', name: '向宗尧', familySize: '5', residentSize: '1', phone: '17775193873', riskLevel: '中' },
    { idx: 93, village: '冬瓜坡村泥冲组', name: '彭云湘', familySize: '6', residentSize: '2', phone: '18307459459', riskLevel: '中' },
    { idx: 94, village: '新店村毛家垅组', name: '郭祥华', familySize: '4', residentSize: '2', phone: '13973094982', riskLevel: '中' },
    { idx: 95, village: '冬瓜坡村黄花坪组', name: '李佰桃', familySize: '4', residentSize: '2', phone: '13530295802', riskLevel: '中' },
    { idx: 96, village: '新店村牛塘坡组', name: '李泽建', familySize: '2', residentSize: '2', phone: '13789284062', riskLevel: '中' },
    { idx: 97, village: '冬瓜坡村黄花坪组', name: '李复林', familySize: '5', residentSize: '1', phone: '13787549178', riskLevel: '中' },
    { idx: 98, village: '冬瓜坡村黄花坪组', name: '张龙海', familySize: '1', residentSize: '', phone: '18174537145', riskLevel: '中' },
    { idx: 99, village: '冬瓜坡村泥冲组', name: '龙林香', familySize: '1', residentSize: '1', phone: '19918451108', riskLevel: '中' },
    { idx: 100, village: '新店村沙田坡组', name: '张家林', familySize: '4', residentSize: '2', phone: '13034854217', riskLevel: '中' },
    { idx: 101, village: '新店村板山坪组', name: '蒋家付', familySize: '4', residentSize: '1', phone: '19918587945', riskLevel: '中' },
    { idx: 102, village: '新店村板山坪组', name: '秦隆军', familySize: '3', residentSize: '2', phone: '17774563517', riskLevel: '中' },
    { idx: 103, village: '新店村翁田垅村', name: '龙建国', familySize: '4', residentSize: '2', phone: '15580769278', riskLevel: '中' },
    { idx: 104, village: '新店村翁田垅村', name: '龙文川', familySize: '3', residentSize: '1', phone: '15869948049', riskLevel: '中' },
    { idx: 105, village: '新店村翁田垅村', name: '李培松', familySize: '2', residentSize: '1', phone: '13469338438', riskLevel: '中' },
    { idx: 106, village: '冬瓜坡村泥冲组', name: '秦冰', familySize: '5', residentSize: '5', phone: '13789293501', riskLevel: '中' },
    { idx: 107, village: '新店村翁田垅村', name: '李培学', familySize: '6', residentSize: '5', phone: '17774592939', riskLevel: '中' },
    { idx: 108, village: '冬瓜坡村泥冲组', name: '秦建军', familySize: '4', residentSize: '2', phone: '18274529792', riskLevel: '中' },
    { idx: 109, village: '新店村大冲组', name: '张佰武', familySize: '3', residentSize: '2', phone: '13677459795', riskLevel: '中' },
    { idx: 110, village: '新店村大沙坪组', name: '张邦炎', familySize: '3', residentSize: '2', phone: '19918592487', riskLevel: '中' },
    { idx: 111, village: '新店村元边组', name: '杨序云', familySize: '2', residentSize: '1', phone: '18174551039', riskLevel: '中' },
    { idx: 112, village: '新店村大冲组', name: '李复林', familySize: '4', residentSize: '1', phone: '13348756669', riskLevel: '中' },
    { idx: 113, village: '新店村大冲组', name: '李复元', familySize: '4', residentSize: '', phone: '13556200725', riskLevel: '中' },
    { idx: 114, village: '新店村月亮田村', name: '龙林兵', familySize: '2', residentSize: '', phone: '13973094982', riskLevel: '中' },
    { idx: 115, village: '新店村月亮田村', name: '黄渊美', familySize: '2', residentSize: '1', phone: '17774526997', riskLevel: '中' },
    { idx: 116, village: '新店村坳背组', name: '杨承华', familySize: '3', residentSize: '3', phone: '15674558867', riskLevel: '中' },
    { idx: 117, village: '新店村月亮田组', name: '龙克栋', familySize: '7', residentSize: '3', phone: '18797552814', riskLevel: '中' },
    { idx: 118, village: '新店村月亮田组', name: '向开明', familySize: '2', residentSize: '2', phone: '15325396896', riskLevel: '中' },
    { idx: 119, village: '新店村月亮田组', name: '龙桂林', familySize: '3', residentSize: '3', phone: '18474525781', riskLevel: '中' },
    { idx: 120, village: '罗卜田村老鸦溪组', name: '李开球', familySize: '4', residentSize: '2', phone: '13047291530', riskLevel: '中' },
    { idx: 121, village: '罗卜田村老鸦溪组', name: '杨仕友', familySize: '5', residentSize: '3', phone: '15674510645', riskLevel: '中' },
    { idx: 122, village: '罗卜田村老鸦溪组', name: '李开友', familySize: '3', residentSize: '2', phone: '15874520515', riskLevel: '中' },
    { idx: 123, village: '罗卜田村老鸦溪组', name: '秦继富', familySize: '4', residentSize: '3', phone: '18574550375', riskLevel: '中' },
    { idx: 124, village: '罗卜田村老鸦溪组', name: '肖一彪', familySize: '3', residentSize: '', phone: '13973094982', riskLevel: '中' },
    { idx: 125, village: '罗卜田村老鸦溪组', name: '李忠贵', familySize: '4', residentSize: '2', phone: '18574550872', riskLevel: '中' },
    { idx: 126, village: '罗卜田村二里五组', name: '龚志松', familySize: '3', residentSize: '2', phone: '15674557671', riskLevel: '中' },
    { idx: 127, village: '罗卜田村高坡界组', name: '汪友军', familySize: '2', residentSize: '2', phone: '19174578573', riskLevel: '中' },
    { idx: 128, village: '罗卜田村二里五组', name: '李复贵', familySize: '4', residentSize: '3', phone: '17375909897', riskLevel: '中' },
    { idx: 129, village: '罗卜田村高坡界组', name: '张帮林', familySize: '', residentSize: '', phone: '15115187473', riskLevel: '中' },
    { idx: 130, village: '罗卜田村高坡界组', name: '杨仁珍', familySize: '7', residentSize: '', phone: '13204992295', riskLevel: '中' },
    { idx: 131, village: '罗卜田村老鸦溪组', name: '张道明', familySize: '4', residentSize: '2', phone: '13187150469', riskLevel: '中' },
    { idx: 132, village: '罗卜田村老鸦溪组', name: '张道生', familySize: '2', residentSize: '1', phone: '15526138492', riskLevel: '中' },
    { idx: 133, village: '罗卜田村二里五组', name: '杨长梅', familySize: '1', residentSize: '', phone: '13874524558', riskLevel: '中' },
    { idx: 134, village: '罗卜田村蒲家冲组', name: '张道清', familySize: '4', residentSize: '2', phone: '15574547168', riskLevel: '中' },
    { idx: 135, village: '罗卜田村洋火坪组', name: '杨平平', familySize: '2', residentSize: '', phone: '15115183041', riskLevel: '中' },
    { idx: 136, village: '罗卜田村蒲家冲组', name: '覃献华', familySize: '4', residentSize: '', phone: '13974557981', riskLevel: '中' },
    { idx: 137, village: '罗卜田村蒲家冲组', name: '张道光', familySize: '2', residentSize: '2', phone: '18174528381', riskLevel: '中' },
    { idx: 138, village: '罗卜田村洋火坪组', name: '黄友明', familySize: '4', residentSize: '3', phone: '13469355080', riskLevel: '中' },
    { idx: 139, village: '罗卜田村洋火坪组', name: '龚小明', familySize: '2', residentSize: '1', phone: '18674531275', riskLevel: '中' },
    { idx: 140, village: '罗卜田村洋火坪组', name: '龚志国', familySize: '3', residentSize: '', phone: '13762951616', riskLevel: '中' },
    { idx: 141, village: '罗卜田村洋火坪组', name: '吴康敏', familySize: '4', residentSize: '2', phone: '15111560427', riskLevel: '中' },
    { idx: 142, village: '罗卜田村团坡组', name: '李复爱', familySize: '1', residentSize: '1', phone: '18674516336', riskLevel: '中' },
    { idx: 143, village: '罗卜田村湾泉组', name: '杨炉香', familySize: '2', residentSize: '2', phone: '13174529837', riskLevel: '中' },
    { idx: 144, village: '罗卜田村罗卜田组(小学)', name: '张儒云屋226°方向141米处', familySize: '', residentSize: '', phone: '18152700719', riskLevel: '中' },
    { idx: 145, village: '罗卜田村湾泉组', name: '江兴华', familySize: '4', residentSize: '1', phone: '15526177995', riskLevel: '高' },
    { idx: 146, village: '罗卜田村湾泉组', name: '汪长军', familySize: '5', residentSize: '2', phone: '17315926919', riskLevel: '中' },
    { idx: 147, village: '罗卜田村罗卜田组', name: '张儒云', familySize: '3', residentSize: '1', phone: '15580668578', riskLevel: '中' },
    { idx: 148, village: '马坡村三角丘组', name: '李家江', familySize: '4', residentSize: '', phone: '18075999397', riskLevel: '中' },
    { idx: 149, village: '马坡村三角丘组', name: '李燕文', familySize: '3', residentSize: '1', phone: '17670417987', riskLevel: '中' },
    { idx: 150, village: '罗卜田村罗卜田组', name: '张茂春', familySize: '1', residentSize: '', phone: '13467412035', riskLevel: '中' },
    { idx: 151, village: '罗卜田村蒲家冲组', name: '许小林', familySize: '2', residentSize: '1', phone: '15119066147', riskLevel: '中' },
    { idx: 152, village: '罗卜田村罗卜田组', name: '张茂林', familySize: '4', residentSize: '2', phone: '18244817428', riskLevel: '中' },
    { idx: 153, village: '罗卜田村罗卜田组', name: '殷林', familySize: '5', residentSize: '4', phone: '13974557610', riskLevel: '中' },
    { idx: 154, village: '罗卜田村罗卜田组', name: '高传思', familySize: '2', residentSize: '2', phone: '18273854280', riskLevel: '中' },
    { idx: 155, village: '罗卜田村罗卜田组', name: '李勇', familySize: '4', residentSize: '1', phone: '15774288229', riskLevel: '中' },
    { idx: 156, village: '罗卜田村罗卜田组', name: '李良贵', familySize: '4', residentSize: '2', phone: '19918458553', riskLevel: '中' },
    { idx: 157, village: '罗卜田村罗卜田组', name: '龚志宏', familySize: '2', residentSize: '2', phone: '19974517175', riskLevel: '中' },
    { idx: 158, village: '罗卜田村罗卜田组', name: '龚志兵', familySize: '1', residentSize: '', phone: '15226460319', riskLevel: '中' },
    { idx: 159, village: '罗卜田村罗卜田组', name: '彭建军、李复忠', familySize: '8', residentSize: '4', phone: '18674503046', riskLevel: '中' },
    { idx: 160, village: '罗卜田村罗卜田组', name: '张爱军', familySize: '4', residentSize: '', phone: '18174528381', riskLevel: '中' },
    { idx: 161, village: '罗卜田村罗卜田组', name: '邓运湘', familySize: '2', residentSize: '', phone: '13332675172', riskLevel: '中' },
    { idx: 162, village: '罗卜田村罗卜田组', name: '肖世沅、龙克锡、刘正军', familySize: '6', residentSize: '6', phone: '19958240395', riskLevel: '中' },
    { idx: 163, village: '马坡村三角丘组', name: '李泽梅', familySize: '5', residentSize: '1', phone: '18274501319', riskLevel: '中' },
    { idx: 164, village: '罗卜田村罗卜田组', name: '蒋才平', familySize: '5', residentSize: '2', phone: '18174257096', riskLevel: '中' },
    { idx: 165, village: '罗卜田村罗卜田组', name: '钦永和', familySize: '2', residentSize: '1', phone: '19174513985', riskLevel: '中' },
    { idx: 166, village: '罗卜田村罗卜田组', name: '邱小洪', familySize: '4', residentSize: '2', phone: '18267740769', riskLevel: '中' },
    { idx: 167, village: '罗卜田村罗卜田组', name: '蒋家明', familySize: '4', residentSize: '3', phone: '13487551637', riskLevel: '中' },
    { idx: 168, village: '罗卜田村罗卜田组', name: '杨小清', familySize: '1', residentSize: '1', phone: '13789262885', riskLevel: '中' },
    { idx: 169, village: '罗卜田村罗卜田组', name: '刘成辉', familySize: '3', residentSize: '2', phone: '18244804380', riskLevel: '中' },
    { idx: 170, village: '罗卜田村罗卜田组', name: '郑秀凤', familySize: '4', residentSize: '1', phone: '15111597273', riskLevel: '中' },
    { idx: 171, village: '罗卜田村罗卜田组', name: '邱波', familySize: '4', residentSize: '', phone: '15576515730', riskLevel: '中' },
    { idx: 172, village: '罗卜田村罗卜田组', name: '李复明', familySize: '2', residentSize: '2', phone: '18274561727', riskLevel: '中' },
    { idx: 173, village: '罗卜田村罗卜田组', name: '李良均', familySize: '2', residentSize: '2', phone: '15807429695', riskLevel: '中' },
    { idx: 174, village: '罗卜田村罗卜田组', name: '钦爱平', familySize: '1', residentSize: '', phone: '15323646888', riskLevel: '中' },
    { idx: 175, village: '罗卜田村罗卜田组', name: '龚志坤', familySize: '5', residentSize: '2', phone: '15115261493', riskLevel: '中' },
    { idx: 176, village: '罗卜田村罗卜田组', name: '龚国平', familySize: '3', residentSize: '', phone: '13469327394', riskLevel: '中' },
    { idx: 177, village: '罗卜田村罗卜田组', name: '钦永堂', familySize: '2', residentSize: '', phone: '15869970608', riskLevel: '中' },
    { idx: 178, village: '罗卜田村罗卜田组', name: '谭利军', familySize: '3', residentSize: '3', phone: '15348480558', riskLevel: '中' },
    { idx: 179, village: '罗卜田村罗卜田组', name: '李复湘', familySize: '2', residentSize: '1', phone: '13789266192', riskLevel: '中' },
    { idx: 180, village: '罗卜田村罗卜田组', name: '高建平', familySize: '4', residentSize: '2', phone: '19918508497', riskLevel: '中' },
    { idx: 181, village: '罗卜田村罗卜田组', name: '肖德秀', familySize: '2', residentSize: '', phone: '13662854710', riskLevel: '中' },
    { idx: 182, village: '罗卜田村罗卜田组', name: '龚志坤', familySize: '5', residentSize: '2', phone: '15115261493', riskLevel: '中' },
    { idx: 183, village: '罗卜田村罗卜田组', name: '龚国荣', familySize: '3', residentSize: '2', phone: '17769226783', riskLevel: '中' },
    { idx: 184, village: '罗卜田村罗卜田组', name: '龚春雷', familySize: '5', residentSize: '1', phone: '13874478207', riskLevel: '中' },
    { idx: 185, village: '罗卜田村罗卜田组', name: '刘正松', familySize: '2', residentSize: '2', phone: '15115278577', riskLevel: '中' },
    { idx: 186, village: '马坡村土寨组', name: '李红、李兵', familySize: '6', residentSize: '', phone: '18774586859', riskLevel: '中' },
    { idx: 187, village: '罗卜田村桑树坪组', name: '张仲贵', familySize: '2', residentSize: '2', phone: '18274523724', riskLevel: '中' },
    { idx: 188, village: '罗卜田村桑树坪组', name: '张家林', familySize: '5', residentSize: '3', phone: '15874568245', riskLevel: '中' },
    { idx: 189, village: '马坡村屋背界组', name: '李复亮', familySize: '4', residentSize: '2', phone: '18774586859', riskLevel: '中' },
    { idx: 190, village: '马坡村屋背界组', name: '李泽发', familySize: '7', residentSize: '2', phone: '18797550469', riskLevel: '中' },
    { idx: 191, village: '罗卜田村桑树坪组', name: '张家林', familySize: '5', residentSize: '3', phone: '15874568245', riskLevel: '中' },
    { idx: 192, village: '马坡村屋背界', name: '李泽达', familySize: '9', residentSize: '7', phone: '生子18174507973', riskLevel: '中' },
    { idx: 193, village: '马坡村桃家垅组', name: '李泽斌', familySize: '4', residentSize: '2', phone: '19976824137', riskLevel: '中' },
    { idx: 194, village: '马坡村李家冲组', name: '李复林', familySize: '2', residentSize: '', phone: '19152119148', riskLevel: '中' },
    { idx: 195, village: '马坡村李家冲组', name: '李复达', familySize: '4', residentSize: '2', phone: '18944923565', riskLevel: '中' },
    { idx: 196, village: '马坡村李家冲组', name: '李复洪', familySize: '6', residentSize: '2', phone: '18974581628', riskLevel: '中' },
    { idx: 197, village: '马坡村李家冲组', name: '李复和', familySize: '1', residentSize: '', phone: '15774282235', riskLevel: '中' },
    { idx: 198, village: '罗卜田村木田冲组', name: '刘平', familySize: '1', residentSize: '1', phone: '13319616238', riskLevel: '中' },
    { idx: 199, village: '罗卜田村木田冲组', name: '江贤会', familySize: '4', residentSize: '4', phone: '19896472407', riskLevel: '中' },
    { idx: 200, village: '罗卜田村大坪田组', name: '李培金', familySize: '5', residentSize: '2', phone: '18574533721', riskLevel: '中' },
    { idx: 201, village: '马坡村李家冲组', name: '李伟星', familySize: '1', residentSize: '', phone: '18574547186', riskLevel: '中' },
    { idx: 202, village: '罗卜田村木田冲组', name: '刘正宏', familySize: '2', residentSize: '2', phone: '13034874209', riskLevel: '中' },
    { idx: 203, village: '罗卜田村对木坪组', name: '刘良刚', familySize: '3', residentSize: '3', phone: '13789266483', riskLevel: '中' },
    { idx: 204, village: '罗卜田村大坪田组', name: '李培忠', familySize: '4', residentSize: '2', phone: '15674597599', riskLevel: '中' },
    { idx: 205, village: '罗卜田村对木坪组', name: '刘良刚', familySize: '3', residentSize: '3', phone: '13789266483', riskLevel: '中' },
    { idx: 206, village: '罗卜田村瓦窑坳组', name: '钦勇', familySize: '4', residentSize: '1', phone: '19896467489', riskLevel: '中' },
    { idx: 207, village: '马坡村团坡界组', name: '杨凤', familySize: '2', residentSize: '', phone: '15096244666', riskLevel: '中' },
    { idx: 208, village: '罗卜田村木田冲组', name: '刘正才', familySize: '4', residentSize: '3', phone: '13467400096', riskLevel: '中' },
    { idx: 209, village: '罗卜田村瓦窑坳组', name: '张廷森', familySize: '1', residentSize: '1', phone: '13973094982', riskLevel: '中' },
    { idx: 210, village: '马坡村李家冲组', name: '邱荣香', familySize: '4', residentSize: '3', phone: '19918528471', riskLevel: '中' },
    { idx: 211, village: '马坡村红星组', name: '周锋', familySize: '4', residentSize: '2', phone: '13968170261', riskLevel: '中' },
    { idx: 212, village: '罗卜田村瓦窑坳组', name: '钦代贵', familySize: '6', residentSize: '2', phone: '17769326930', riskLevel: '中' },
    { idx: 213, village: '罗卜田村大坪田组', name: '李培贵', familySize: '4', residentSize: '', phone: '18574546739', riskLevel: '中' },
    { idx: 214, village: '罗卜田村瓦窑坳组', name: '秦隆海', familySize: '6', residentSize: '3', phone: '19152177297', riskLevel: '中' },
    { idx: 215, village: '罗卜田村瓦窑坳组', name: '张庭堂', familySize: '1', residentSize: '1', phone: '13874487647', riskLevel: '中' },
    { idx: 216, village: '马坡村权坡组', name: '邓序光', familySize: '6', residentSize: '2', phone: '15526122411', riskLevel: '中' },
    { idx: 217, village: '马坡村权坡组', name: '肖世耒', familySize: '2', residentSize: '2', phone: '18244801321', riskLevel: '中' },
    { idx: 218, village: '马坡村权坡组', name: '肖世忠', familySize: '4', residentSize: '3', phone: '15367570638', riskLevel: '中' },
    { idx: 219, village: '马坡村权坡组', name: '肖德斌', familySize: '5', residentSize: '', phone: '15807429910', riskLevel: '中' },
    { idx: 220, village: '马坡村丁家界组', name: '李建华', familySize: '3', residentSize: '2', phone: '18874573109', riskLevel: '中' },
    { idx: 221, village: '马坡村丁家界组', name: '肖昌英', familySize: '2', residentSize: '2', phone: '13627453284', riskLevel: '中' },
    { idx: 222, village: '马坡村马坡组', name: '邓序榕', familySize: '5', residentSize: '2', phone: '15974042798', riskLevel: '中' },
    { idx: 223, village: '马坡村马坡组', name: '李军', familySize: '2', residentSize: '1', phone: '19375017798', riskLevel: '中' },
    { idx: 224, village: '马坡村丁家界组', name: '肖昌龙', familySize: '3', residentSize: '2', phone: '19918519491', riskLevel: '中' },
    { idx: 225, village: '罗卜田村老树冲组', name: '张廷中', familySize: '7', residentSize: '2', phone: '15115277990', riskLevel: '中' },
    { idx: 226, village: '马坡村马坡组', name: '邓运福', familySize: '5', residentSize: '2', phone: '13789293952', riskLevel: '中' },
    { idx: 227, village: '罗卜田村长垠坡组', name: '张茂军', familySize: '5', residentSize: '4', phone: '15807429764', riskLevel: '中' },
    { idx: 228, village: '马坡村马坡组', name: '邓长兵', familySize: '5', residentSize: '4', phone: '17375552188', riskLevel: '中' },
    { idx: 229, village: '罗卜田村老树冲组', name: '李良金', familySize: '2', residentSize: '2', phone: '15897452346', riskLevel: '中' },
    { idx: 230, village: '枣子山村岩寨坡组', name: '罗文前', familySize: '4', residentSize: '3', phone: '13787544755', riskLevel: '中' },
    { idx: 231, village: '枣子山村岩寨坡组', name: '郑再华', familySize: '5', residentSize: '1', phone: '18607450827', riskLevel: '中' },
    { idx: 232, village: '枣子山村大洋溪组', name: '郑秀平', familySize: '3', residentSize: '2', phone: '13617454092', riskLevel: '中' },
    { idx: 233, village: '枣子山村大洋溪组', name: '郑再和', familySize: '6', residentSize: '4', phone: '13607414516', riskLevel: '中' },
    { idx: 234, village: '枣子山村大洋溪组', name: '郑友洪', familySize: '3', residentSize: '2', phone: '13974532709', riskLevel: '中' },
    { idx: 235, village: '枣子山村大洋溪组', name: '李培发', familySize: '5', residentSize: '2', phone: '19974537572', riskLevel: '中' },
    { idx: 236, village: '枣子山村晏家冲组', name: '杨家枚', familySize: '3', residentSize: '1', phone: '15897423176', riskLevel: '中' },
    { idx: 237, village: '枣子山村渗水塘组', name: '刘正华', familySize: '4', residentSize: '2', phone: '17374551772', riskLevel: '中' },
    { idx: 238, village: '罗卜田村溪边组', name: '邱茂良', familySize: '2', residentSize: '2', phone: '15673081892', riskLevel: '中' },
    { idx: 239, village: '枣子山村渗水塘组', name: '郑秀忠', familySize: '6', residentSize: '1', phone: '18797629680', riskLevel: '中' },
    { idx: 240, village: '枣子山村渗水塘组', name: '刘正华', familySize: '3', residentSize: '1', phone: '18152734172', riskLevel: '中' },
    { idx: 241, village: '枣子山村渗水塘组', name: '郑光槐', familySize: '1', residentSize: '1', phone: '13789290468', riskLevel: '中' },
    { idx: 242, village: '枣子山村晏家冲组', name: '粟艳', familySize: '2', residentSize: '', phone: '17775161703', riskLevel: '中' },
    { idx: 243, village: '枣子山村晏家冲组', name: '刘正早', familySize: '5', residentSize: '2', phone: '15096235932', riskLevel: '中' },
    { idx: 244, village: '枣子山村老屯组', name: '杨文秀', familySize: '4', residentSize: '', phone: '13467954709', riskLevel: '中' },
    { idx: 245, village: '枣子山村渗水塘组', name: '郑小林', familySize: '5', residentSize: '2', phone: '13004487773', riskLevel: '中' },
    { idx: 246, village: '枣子山村老屯组', name: '郑复胜', familySize: '5', residentSize: '2', phone: '15211572493', riskLevel: '中' },
    { idx: 247, village: '枣子山村老屯组', name: '郑复友', familySize: '3', residentSize: '1', phone: '18797552341', riskLevel: '中' },
    { idx: 248, village: '半冲村', name: '伍代明', familySize: '7', residentSize: '3', phone: '15399826707', riskLevel: '中' },
    { idx: 249, village: '枣子山村老屯组', name: '李小枚', familySize: '3', residentSize: '', phone: '13874478821', riskLevel: '中' },
    { idx: 250, village: '枣子山村桥边组', name: '郑复均', familySize: '6', residentSize: '4', phone: '18797552341', riskLevel: '中' },
    { idx: 251, village: '罗卜田村溪边组', name: '杨志海', familySize: '2', residentSize: '2', phone: '17774585609', riskLevel: '中' },
    { idx: 252, village: '罗卜田村溪边组', name: '岳昌发', familySize: '3', residentSize: '3', phone: '18719282279', riskLevel: '中' },
    { idx: 253, village: '枣子山村桥边组', name: '郑小建', familySize: '3', residentSize: '', phone: '18874514840', riskLevel: '中' },
    { idx: 254, village: '半冲村', name: '钦永贵', familySize: '1', residentSize: '1', phone: '13874409193', riskLevel: '中' },
    { idx: 255, village: '罗卜田村溪边组', name: '郑德华', familySize: '3', residentSize: '2', phone: '18357193438', riskLevel: '中' },
    { idx: 256, village: '枣子山村老屯组', name: '郑建华', familySize: '4', residentSize: '2', phone: '18797551553', riskLevel: '中' },
    { idx: 257, village: '罗卜田村杠冲组', name: '周建明', familySize: '2', residentSize: '2', phone: '13787523041', riskLevel: '中' },
    { idx: 258, village: '半冲村', name: '彭宏大', familySize: '1', residentSize: '1', phone: '15386259511', riskLevel: '中' },
    { idx: 259, village: '半冲村', name: '高伟', familySize: '5', residentSize: '1', phone: '13762928689', riskLevel: '中' },
    { idx: 260, village: '枣子山村枞山脚组', name: '郑秀刚', familySize: '2', residentSize: '2', phone: '13974567202', riskLevel: '中' },
    { idx: 261, village: '枣子山村枞山脚组', name: '郑再甲', familySize: '1', residentSize: '', phone: '19372372279', riskLevel: '中' },
    { idx: 262, village: '枣子山村枞山脚组', name: '郑光明', familySize: '2', residentSize: '2', phone: '15343272808', riskLevel: '中' },
    { idx: 263, village: '枣子山村枞山脚组', name: '邓和平', familySize: '2', residentSize: '1', phone: '19974528935', riskLevel: '中' },
    { idx: 264, village: '枣子山村曾家埌组', name: '付华松', familySize: '2', residentSize: '', phone: '18874588865', riskLevel: '中' },
    { idx: 265, village: '枣子山村新屋场组', name: '郑秀海', familySize: '6', residentSize: '2', phone: '13907455284', riskLevel: '中' },
    { idx: 266, village: '枣子山村枞山脚组', name: '邓长平', familySize: '4', residentSize: '2', phone: '15211514686', riskLevel: '中' },
    { idx: 267, village: '罗卜田村杠冲组', name: '郑敏', familySize: '3', residentSize: '2', phone: '19375088177', riskLevel: '中' },
    { idx: 268, village: '半冲村下湾组', name: '彭先贵', familySize: '4', residentSize: '1', phone: '18774734767', riskLevel: '中' },
    { idx: 269, village: '枣子山村曾家埌组', name: '付铁山', familySize: '4', residentSize: '1', phone: '18797552577', riskLevel: '中' },
    { idx: 270, village: '枣子山村枞山脚组', name: '杨学平', familySize: '6', residentSize: '5', phone: '15386287283', riskLevel: '中' },
    { idx: 271, village: '半冲村', name: '梁忠成', familySize: '5', residentSize: '2', phone: '18707453953', riskLevel: '中' },
    { idx: 272, village: '半冲村', name: '邓开成', familySize: '2', residentSize: '1', phone: '15211512108', riskLevel: '中' },
    { idx: 273, village: '半冲村下湾组', name: '彭开成', familySize: '6', residentSize: '6', phone: '18273853514', riskLevel: '中' },
    { idx: 274, village: '半冲村下湾组', name: '彭勇', familySize: '3', residentSize: '', phone: '18244803231', riskLevel: '中' },
    { idx: 275, village: '半冲村', name: '彭先国', familySize: '2', residentSize: '1', phone: '13874450197', riskLevel: '中' },
    { idx: 276, village: '枣子山村新屋场组', name: '郑秀光', familySize: '3', residentSize: '2', phone: '15111561899', riskLevel: '中' },
    { idx: 277, village: '枣子山村新屋场组', name: '郑秀珍', familySize: '5', residentSize: '2', phone: '15074590074', riskLevel: '中' },
    { idx: 278, village: '半冲村下湾组', name: '吴同章', familySize: '4', residentSize: '3', phone: '18890683827', riskLevel: '中' },
    { idx: 279, village: '枣子山村新屋场组', name: '郑秀明', familySize: '4', residentSize: '2', phone: '13789262328', riskLevel: '中' },
    { idx: 280, village: '枣子山村新屋场组', name: '郑秀连', familySize: '3', residentSize: '2', phone: '15807429864', riskLevel: '中' },
    { idx: 281, village: '半冲村下湾组', name: '彭开金', familySize: '6', residentSize: '5', phone: '13974522976', riskLevel: '中' },
    { idx: 282, village: '半冲村', name: '彭宗兴', familySize: '3', residentSize: '2', phone: '18244801483', riskLevel: '中' },
    { idx: 283, village: '半冲村', name: '邓开银', familySize: '6', residentSize: '3', phone: '17774550563', riskLevel: '中' },
    { idx: 284, village: '枣子山村新屋场组', name: '邓长春', familySize: '3', residentSize: '21', phone: '13874515054', riskLevel: '中' },
    { idx: 285, village: '半冲村', name: '许自明', familySize: '', residentSize: '', phone: '18218836757', riskLevel: '中' },
    { idx: 286, village: '半冲村', name: '张廷球', familySize: '2', residentSize: '2', phone: '15674584081', riskLevel: '中' },
    { idx: 287, village: '半冲村', name: '钦小兰', familySize: '1', residentSize: '1', phone: '15211541776', riskLevel: '中' },
    { idx: 288, village: '半冲村', name: '彭开成', familySize: '4', residentSize: '3', phone: '18244850653', riskLevel: '中' },
    { idx: 289, village: '半冲村', name: '许昆有', familySize: '4', residentSize: '2', phone: '16658003903', riskLevel: '中' },
    { idx: 290, village: '枣子山村岩对坎组', name: '李复前', familySize: '2', residentSize: '2', phone: '18274581638', riskLevel: '中' },
    { idx: 291, village: '枣子山村马田浪组', name: '李复均', familySize: '6', residentSize: '1', phone: '15974003952', riskLevel: '中' },
    { idx: 292, village: '新店村翁田垅村', name: '张宗成', familySize: '3', residentSize: '1', phone: '15807405595', riskLevel: '中' },
    { idx: 293, village: '罗卜田村湾泉组', name: '汪长军', familySize: '5', residentSize: '2', phone: '17315926919', riskLevel: '中' },
    { idx: 294, village: '罗卜田村罗卜田组', name: '黄孝品', familySize: '2', residentSize: '', phone: '13973094982', riskLevel: '中' },
    { idx: 295, village: '罗卜田村罗卜田组', name: '杨志元', familySize: '3', residentSize: '3', phone: '19918584775', riskLevel: '中' },
    { idx: 296, village: '罗卜田村罗卜田组', name: '李勇', familySize: '4', residentSize: '4', phone: '15115119110', riskLevel: '中' },
    { idx: 297, village: '罗卜田村罗卜田组', name: '龚仕祥', familySize: '4', residentSize: '3', phone: '13973094982', riskLevel: '中' },
    { idx: 298, village: '罗卜田村罗卜田组', name: '龚国平', familySize: '3', residentSize: '', phone: '13469327394', riskLevel: '中' },
    { idx: 299, village: '罗卜田村罗卜田组', name: '龚国平', familySize: '3', residentSize: '', phone: '13469327394', riskLevel: '中' },
    { idx: 300, village: '罗卜田村洋火坪组', name: '何永香', familySize: '2', residentSize: '1', phone: '13973094982', riskLevel: '中' },
    { idx: 301, village: '罗卜田村团坡组', name: '李复龙', familySize: '3', residentSize: '', phone: '13973094982', riskLevel: '中' },
    { idx: 302, village: '罗卜田村烂泥冲组', name: '张道生', familySize: '4', residentSize: '3', phone: '15526138492', riskLevel: '中' },
    { idx: 303, village: '罗卜田村烂泥冲组', name: '张秀煌', familySize: '6', residentSize: '3', phone: '14760738463', riskLevel: '中' },
    { idx: 304, village: '兴无村败泥古组', name: '郑仕成', familySize: '2', residentSize: '2', phone: '17769238628', riskLevel: '中' },
    { idx: 305, village: '马坡村权坡组', name: '肖世均', familySize: '4', residentSize: '1', phone: '19918582975', riskLevel: '中' },
    { idx: 306, village: '罗卜乡田半冲村', name: '钦开发', familySize: '3', residentSize: '1', phone: '17775922919', riskLevel: '中' },
    { idx: 307, village: '罗卜乡田半冲村', name: '李复东', familySize: '1', residentSize: '', phone: '13467454976', riskLevel: '中' },
    { idx: 308, village: '半冲村', name: '钦昌富', familySize: '2', residentSize: '2', phone: '17873893868', riskLevel: '中' },
    { idx: 309, village: '半冲村', name: '李泽才', familySize: '6', residentSize: '2', phone: '17774595337', riskLevel: '中' },
    { idx: 310, village: '半冲村', name: '李复旺', familySize: '3', residentSize: '1', phone: '17775198597', riskLevel: '中' },
    { idx: 311, village: '半冲村', name: '钦开才', familySize: '3', residentSize: '2', phone: '13272289659', riskLevel: '中' },
    { idx: 312, village: '地点半冲村', name: '李复祥', familySize: '5', residentSize: '2', phone: '18374506287', riskLevel: '中' },
    { idx: 313, village: '半冲村袁家冲', name: '彭开发', familySize: '2', residentSize: '1', phone: '18474536368', riskLevel: '中' },
    { idx: 314, village: '半冲村', name: '张华美', familySize: '2', residentSize: '', phone: '15377407986', riskLevel: '中' },
    { idx: 315, village: '半冲村毛东田组', name: '张廷伦', familySize: '3', residentSize: '2', phone: '19186767319', riskLevel: '中' },
    { idx: 316, village: '半冲村', name: '许昆能', familySize: '3', residentSize: '2', phone: '17365782838', riskLevel: '中' },
    { idx: 317, village: '半冲村', name: '马仁玉', familySize: '3', residentSize: '1', phone: '19974561197', riskLevel: '中' },
    { idx: 318, village: '半冲村塘铺湾组', name: '梁和顺', familySize: '1', residentSize: '1', phone: '18274562760', riskLevel: '中' },
    { idx: 319, village: '半冲村塘铺湾组', name: '梁和松', familySize: '2', residentSize: '2', phone: '18274562760', riskLevel: '中' },
    { idx: 320, village: '半冲村塘铺湾组', name: '梁和发', familySize: '3', residentSize: '', phone: '', riskLevel: '中' },
    { idx: 321, village: '半冲村塘铺湾组', name: '梁和清', familySize: '3', residentSize: '2', phone: '17774563855', riskLevel: '中' },
    { idx: 322, village: '半冲村塘铺湾组', name: '梁宗武', familySize: '', residentSize: '', phone: '', riskLevel: '中' },
    { idx: 323, village: '半冲村塘铺湾组', name: '梁和均', familySize: '4', residentSize: '2', phone: '19303061543', riskLevel: '中' },
    { idx: 324, village: '半冲村塘铺湾组', name: '梁宗发', familySize: '2', residentSize: '2', phone: '19174591018', riskLevel: '中' },
    { idx: 325, village: '半冲村', name: '唐大军', familySize: '2', residentSize: '2', phone: '17774540127', riskLevel: '中' },
    { idx: 326, village: '半冲村', name: '许昆发', familySize: '5', residentSize: '1', phone: '13034871125', riskLevel: '中' },
    { idx: 327, village: '半冲村', name: '张廷元', familySize: '1', residentSize: '', phone: '', riskLevel: '中' },
    { idx: 328, village: '半冲村', name: '罗文连', familySize: '2', residentSize: '', phone: '18458213605', riskLevel: '中' },
    { idx: 329, village: '半冲村', name: '张廷喜', familySize: '2', residentSize: '2', phone: '18274540318', riskLevel: '中' },
    { idx: 330, village: '半冲村', name: '许法兵', familySize: '4', residentSize: '', phone: '18774793221', riskLevel: '中' },
    { idx: 331, village: '冬瓜坡村塘山冲组', name: '邓开政', familySize: '2', residentSize: '2', phone: '15581556231', riskLevel: '中' },
    { idx: 332, village: '冬瓜坡村塘山冲组', name: '邓长岩', familySize: '4', residentSize: '2', phone: '19311522397', riskLevel: '中' },
    { idx: 333, village: '冬瓜坡村塘山冲组', name: '张邦华', familySize: '5', residentSize: '2', phone: '15674588270', riskLevel: '中' },
    { idx: 334, village: '兴无村', name: '李培华', familySize: '3', residentSize: '1', phone: '18574558017', riskLevel: '中' },
    { idx: 335, village: '兴无村', name: '向开华', familySize: '4', residentSize: '1', phone: '17769238768', riskLevel: '中' },
    { idx: 336, village: '新店村邓家山组', name: '郑光梅', familySize: '2', residentSize: '1', phone: '13973094709', riskLevel: '中' },
    { idx: 337, village: '兴无村', name: '张小和', familySize: '2', residentSize: '1', phone: '15869934832', riskLevel: '中' },
    { idx: 338, village: '冬瓜坡村郭家冲组', name: '李海平', familySize: '5', residentSize: '3', phone: '19918511439', riskLevel: '中' },
    { idx: 339, village: '新店村勾计山组', name: '邓梅香', familySize: '2', residentSize: '1', phone: '13469343281', riskLevel: '中' },
    { idx: 340, village: '新店村罗家冲组', name: '吴海军', familySize: '5', residentSize: '1', phone: '18774583475', riskLevel: '中' },
    { idx: 341, village: '冬瓜坡村泥冲组', name: '邓小荣', familySize: '6', residentSize: '', phone: '18273854750', riskLevel: '中' },
    { idx: 342, village: '冬瓜坡村田冲组', name: '李复胜', familySize: '2', residentSize: '2', phone: '18244801091', riskLevel: '中' },
    { idx: 343, village: '新店村翁田垅组', name: '龙林清', familySize: '6', residentSize: '', phone: '15115278301', riskLevel: '中' },
    { idx: 344, village: '新店村翁田垅组', name: '龙日光', familySize: '4', residentSize: '3', phone: '13469355217', riskLevel: '高' },
    { idx: 345, village: '新店村元边组', name: '杨序林', familySize: '3', residentSize: '', phone: '13973094982', riskLevel: '中' },
    { idx: 346, village: '新店村坳背组', name: '杨承国', familySize: '1', residentSize: '', phone: '13973094982', riskLevel: '中' },
    { idx: 347, village: '新店村坳背组', name: '杨承军', familySize: '3', residentSize: '2', phone: '13467458261', riskLevel: '中' },
    { idx: 348, village: '罗卜田村蒲家冲组', name: '覃献林', familySize: '1', residentSize: '1', phone: '15580717238', riskLevel: '中' },
    { idx: 349, village: '罗卜田村桑树坪组', name: '张桂清', familySize: '2', residentSize: '1', phone: '15576530639', riskLevel: '中' },
    { idx: 350, village: '马坡村白竹山组', name: '蒋国和', familySize: '6', residentSize: '6', phone: '15354459713', riskLevel: '中' },
    { idx: 351, village: '马坡村屋背界组', name: '何继日', familySize: '3', residentSize: '1', phone: '17775924479', riskLevel: '中' },
    { idx: 352, village: '马坡村李家冲组', name: '李复平', familySize: '2', residentSize: '2', phone: '17374506177', riskLevel: '中' },
    { idx: 353, village: '罗卜田村烂泥冲组', name: '张秀洪', familySize: '5', residentSize: '2', phone: '15581559639', riskLevel: '中' },
    { idx: 354, village: '枣子山村岩寨坡组', name: '唐建国、唐克逵', familySize: '6', residentSize: '2', phone: '13469338603', riskLevel: '中' },
    { idx: 355, village: '枣子山村桥边组', name: '郑德顺', familySize: '1', residentSize: '1', phone: '13100214138', riskLevel: '中' },
    { idx: 356, village: '枣子山村桥边组', name: '郑德凡', familySize: '3', residentSize: '2', phone: '15574522605', riskLevel: '中' },
    { idx: 357, village: '枣子山村老屯组', name: '刘萍', familySize: '5', residentSize: '3', phone: '19974537713', riskLevel: '中' },
    { idx: 358, village: '枣子山村枞山脚组', name: '郑再林', familySize: '3', residentSize: '2', phone: '19372366658', riskLevel: '中' },
    { idx: 359, village: '半冲村', name: '钦开云', familySize: '3', residentSize: '2', phone: '18374548509', riskLevel: '中' },
    { idx: 360, village: '枣子山村新屋场组', name: '郑再国', familySize: '4', residentSize: '2', phone: '19918533495', riskLevel: '中' },
    { idx: 361, village: '枣子山村新屋场组', name: '郑光来', familySize: '1', residentSize: '1', phone: '15211514624', riskLevel: '中' },
    { idx: 362, village: '半冲村', name: '杨军', familySize: '4', residentSize: '2', phone: '18974597209', riskLevel: '中' },
    { idx: 363, village: '半冲村', name: '张平果、张昌旺', familySize: '6', residentSize: '3', phone: '18274582101、19891925147', riskLevel: '中' },
    { idx: 364, village: '半冲村', name: '杨成', familySize: '7', residentSize: '1', phone: '18797544238', riskLevel: '中' },
    { idx: 365, village: '半冲村', name: '许昆林', familySize: '3', residentSize: '2', phone: '18474521062', riskLevel: '中' }
])

const geoData = ref([
    {
      idx: 1, name: '罗卜田乡半冲村燕子岩滑坡', code: '431228011303', location: '罗卜田乡半冲村燕子岩组',
      disasterType: '滑坡', scale: '0.75', scaleLevel: '小型', dangerLevel: '小型', riskLevel: '高风险',
      households: '6', people: '19', property: '80',
      trigger: '自然、人为', stability: '暂时稳定', measures: '地质灾害专业监测',
      countyName: '唐江鲤', countyTitle: '县政协党组书记、主席', countyPhone: '13874533233',
      townName: '李卓玲', townTitle: '乡长', townPhone: '13787572760',
      villageName: '唐大军', villageTitle: '书记', villagePhone: '17774540127',
      point1Name: '吴同章', point1Phone: '18890683827',
      point2Name: '梁和松', point2Phone: '18169458321'
    },
    {
      idx: 2, name: '罗卜田小学不稳定斜坡', code: '431228001188', location: '罗卜田乡罗卜田村罗卜田组',
      disasterType: '不稳定斜坡', scale: '0.125', scaleLevel: '小型', dangerLevel: '中型', riskLevel: '中风险',
      households: '', people: '206', property: '200',
      trigger: '自然、人为', stability: '暂时稳定', measures: '地质灾害专业监测',
      countyName: '唐江鲤', countyTitle: '县政协党组书记、主席', countyPhone: '13874533233',
      townName: '李卓玲', townTitle: '乡长', townPhone: '13787572760',
      villageName: '钦山', villageTitle: '书记', villagePhone: '13874417046',
      point1Name: '蒋荣华', point1Phone: '13789264803',
      point2Name: '李军', point2Phone: '15974028083'
    },
    {
      idx: 3, name: '罗卜田乡马坡村土寨组滑坡', code: '431228011267', location: '罗卜田乡马坡村土寨组',
      disasterType: '滑坡', scale: '1.05', scaleLevel: '小型', dangerLevel: '小型', riskLevel: '中风险',
      households: '1', people: '5', property: '10',
      trigger: '自然、人为', stability: '暂时稳定', measures: '群测群防    立警示牌',
      countyName: '唐江鲤', countyTitle: '县政协党组书记、主席', countyPhone: '13874533233',
      townName: '李卓玲', townTitle: '乡长', townPhone: '13787572760',
      villageName: '李友香', villageTitle: '书记', villagePhone: '18075598897',
      point1Name: '李明', point1Phone: '17375927127',
      point2Name: '蒋金香', point2Phone: '19976827242'
    },
    {
      idx: 4, name: '罗卜田乡兴无村大屋场组不稳定斜坡', code: '431228011266', location: '罗卜田乡兴无村大屋场组',
      disasterType: '不稳定斜坡', scale: '0.045', scaleLevel: '小型', dangerLevel: '小型', riskLevel: '中风险',
      households: '1', people: '4', property: '10',
      trigger: '自然、人为', stability: '暂时稳定', measures: '群测群防    立警示牌',
      countyName: '唐江鲤', countyTitle: '县政协党组书记、主席', countyPhone: '13874533233',
      townName: '李卓玲', townTitle: '乡长', townPhone: '13787572760',
      villageName: '张久云', villageTitle: '书记', villagePhone: '18274523060',
      point1Name: '蒲桂银', point1Phone: '15580685630',
      point2Name: '向勇', point2Phone: '18174586399'
    },
    {
      idx: 5, name: '罗卜田乡枣子山大洋溪组滑坡', code: '431228011310', location: '罗卜田乡枣子山村大洋溪组',
      disasterType: '滑坡', scale: '1.26', scaleLevel: '小型', dangerLevel: '小型', riskLevel: '高风险',
      households: '13', people: '57', property: '500',
      trigger: '自然、人为', stability: '暂时稳定', measures: '地质灾害专业监测',
      countyName: '唐江鲤', countyTitle: '县政协党组书记、主席', countyPhone: '13874533233',
      townName: '李卓玲', townTitle: '乡长', townPhone: '13787572760',
      villageName: '郑复均', villageTitle: '书记', villagePhone: '14760734583',
      point1Name: '郑再和', point1Phone: '13607414516',
      point2Name: '郑友洪', point2Phone: '13974532709'
    }
])

const geoPeopleData = ref([
    { idx: 1, village: '罗卜田村', group: '罗卜田组', pointName: '罗卜田小学不稳定斜坡', name: '校长杨海龙', age: '', relation: '', resident: '是', destination: '学生各自家里', phone: '13787523309', remark: '2023年学生213人，教师28人', transfer1: '25人', transfer2: '放假无人', transfer3: '', isHead: true },
    { idx: 2, village: '兴无村', group: '大屋场组', pointName: '大屋场组不稳定斜坡', name: '姚本权', age: '63', relation: '户主', resident: '是', destination: '村部老年活动中心', phone: '', remark: '在家', transfer1: '1人', transfer2: '转移至芷江', transfer3: '转移至芷江', isHead: true },
    { idx: 3, village: '兴无村', group: '大屋场组', pointName: '大屋场组不稳定斜坡', name: '张早秀', age: '59', relation: '之妻', resident: '否', destination: '', phone: '17347450240', remark: '在怀化', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 4, village: '兴无村', group: '大屋场组', pointName: '大屋场组不稳定斜坡', name: '姚杰', age: '28', relation: '之女', resident: '否', destination: '', phone: '', remark: '外嫁怀化在外务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 5, village: '兴无村', group: '大屋场组', pointName: '大屋场组不稳定斜坡', name: '姚懿峰', age: '35', relation: '之子', resident: '否', destination: '', phone: '', remark: '在上海外务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 6, village: '马坡村', group: '土寨组', pointName: '土寨组滑坡', name: '龙成凤', age: '65', relation: '户主', resident: '是', destination: '马坡村部', phone: '17378026980', remark: '本组', transfer1: '', transfer2: '无人在家', transfer3: '转移2人', isHead: true },
    { idx: 7, village: '马坡村', group: '土寨组', pointName: '土寨组滑坡', name: '李兵', age: '36', relation: '之子', resident: '否', destination: '', phone: '18607487850', remark: '本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 8, village: '马坡村', group: '土寨组', pointName: '土寨组滑坡', name: '李帛航', age: '7', relation: '孙子', resident: '否', destination: '', phone: '', remark: '芷江', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 9, village: '马坡村', group: '土寨组', pointName: '土寨组滑坡', name: '李红', age: '40', relation: '之子', resident: '否', destination: '', phone: '13789290809', remark: '(芷江)刑警大队', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 10, village: '马坡村', group: '土寨组', pointName: '土寨组滑坡', name: '李子毅', age: '4', relation: '孙子', resident: '否', destination: '', phone: '', remark: '芷江', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 11, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑再和', age: '53', relation: '户主', resident: '是', destination: '枣子山村部', phone: '13607414516', remark: '晚上在集镇居住，白天回组里做事', transfer1: '', transfer2: '转移4人', transfer3: '转移4人', isHead: true },
    { idx: 12, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑毛', age: '32', relation: '之子', resident: '否', destination: '', phone: '', remark: '在温州务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 13, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑影娣', age: '29', relation: '之女', resident: '否', destination: '', phone: '', remark: '外嫁(在温州务工)', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 14, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '冯时兰', age: '53', relation: '之妻', resident: '是', destination: '', phone: '', remark: '在集镇居住', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 15, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑子豪', age: '2', relation: '孙子', resident: '是', destination: '', phone: '', remark: '常住集镇', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 16, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '邓厚美', age: '83', relation: '母', resident: '是', destination: '', phone: '', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 17, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '何军秀', age: '27', relation: '媳', resident: '是', destination: '', phone: '', remark: '偶尔在集镇住，偶尔在外务工，偶尔回组里', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 18, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑子洋', age: '2', relation: '孙子', resident: '是', destination: '', phone: '', remark: '由郑再和照顾', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 19, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑再罗', age: '58', relation: '户主', resident: '是', destination: '枣子山村部', phone: '17872457715', remark: '常住本组', transfer1: '', transfer2: '转移3人', transfer3: '转移3人', isHead: true },
    { idx: 20, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '张秀菊', age: '59', relation: '之妻', resident: '是', destination: '', phone: '', remark: '在集镇居住', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 21, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑平', age: '37', relation: '之子', resident: '否', destination: '', phone: '', remark: '在外务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 22, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '刘小艳', age: '36', relation: '媳', resident: '是', destination: '', phone: '', remark: '在集镇照顾小孩', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 23, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑子君', age: '13', relation: '孙女', resident: '是', destination: '', phone: '', remark: '放假住在本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 24, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑子龙', age: '11', relation: '孙子', resident: '是', destination: '', phone: '', remark: '放假住在本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 25, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑秀国(重点)', age: '61', relation: '户主', resident: '是', destination: '枣子山村部', phone: '18774744430', remark: '常住本组', transfer1: '', transfer2: '转移2人', transfer3: '转移2人', isHead: true },
    { idx: 26, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑小毛', age: '42', relation: '之子', resident: '否', destination: '', phone: '', remark: '在外务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 27, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '胡朝美', age: '51', relation: '之妻', resident: '是', destination: '', phone: '', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 28, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '周光梅(重点)', age: '84', relation: '户主', resident: '是', destination: '枣子山村部', phone: '18774744430', remark: '现在玉屏县女儿家住', transfer1: '', transfer2: '', transfer3: '', isHead: true },
    { idx: 29, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑秀平(重点)', age: '63', relation: '户主', resident: '是', destination: '枣子山村部', phone: '17308459456', remark: '常住本组', transfer1: '', transfer2: '转移2人', transfer3: '转移2人', isHead: true },
    { idx: 30, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '田忠美', age: '51', relation: '之妻', resident: '是', destination: '', phone: '', remark: '麻缨塘精神病医院治疗', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 31, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑艳辉', age: '15', relation: '之子', resident: '是', destination: '', phone: '', remark: '职工放假回家', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 32, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '(大)郑在刚', age: '51', relation: '户主', resident: '否', destination: '枣子山村部', phone: '', remark: '在外务工', transfer1: '', transfer2: '', transfer3: '', isHead: true },
    { idx: 33, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '邱荣珍', age: '46', relation: '之妻', resident: '否', destination: '', phone: '18674531762', remark: '常住芷江县城', transfer1: '', transfer2: '转移1人', transfer3: '转移1人', isHead: false },
    { idx: 34, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑玉兰', age: '17', relation: '之女', resident: '否', destination: '', phone: '', remark: '常住芷江县城', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 35, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑在培(重点)', age: '68', relation: '户主', resident: '是', destination: '枣子山村部', phone: '13517455954', remark: '常住本组', transfer1: '', transfer2: '转移4人', transfer3: '转移4人', isHead: true },
    { idx: 36, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '杨正军', age: '46', relation: '女婿', resident: '是', destination: '', phone: '', remark: '广州务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 37, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑菊秀', age: '41', relation: '之女', resident: '是', destination: '', phone: '', remark: '广州务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 38, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑小兵', age: '23', relation: '外孙子', resident: '否', destination: '', phone: '', remark: '广州务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 39, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '杨红', age: '12', relation: '外孙女', resident: '是', destination: '', phone: '', remark: '罗卜田读书，常住集镇', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 40, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '李泽竹', age: '66', relation: '之妻', resident: '是', destination: '', phone: '', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 41, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑在洪(重点)', age: '54', relation: '户主', resident: '否', destination: '枣子山村部', phone: '15580169039', remark: '在外务工', transfer1: '', transfer2: '', transfer3: '', isHead: true },
    { idx: 42, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑德', age: '28', relation: '之子', resident: '否', destination: '', phone: '', remark: '在外务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 43, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '(小)郑在刚', age: '49', relation: '户主', resident: '否', destination: '枣子山村部', phone: '13168877369', remark: '在外务工', transfer1: '', transfer2: '', transfer3: '', isHead: true },
    { idx: 44, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '高昌贵', age: '79', relation: '母', resident: '是', destination: '', phone: '', remark: '常住本组', transfer1: '', transfer2: '转移1人', transfer3: '转移1人', isHead: false },
    { idx: 45, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑淇之', age: '15', relation: '之子', resident: '是', destination: '', phone: '', remark: '芷江读书，常住芷江，偶尔回本组', transfer1: '', transfer2: '转移1人', transfer3: '转移1人', isHead: false },
    { idx: 46, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑再华', age: '58', relation: '户主', resident: '是', destination: '枣子山村部', phone: '18244814896', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: true },
    { idx: 47, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '李泽英', age: '57', relation: '之妻', resident: '是', destination: '', phone: '', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 48, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑淞云', age: '31', relation: '之子', resident: '否', destination: '', phone: '', remark: '常住芷江', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 49, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '林艳萍', age: '25', relation: '媳', resident: '否', destination: '', phone: '', remark: '常住芷江', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 50, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑凯文', age: '4', relation: '孙子', resident: '是', destination: '', phone: '', remark: '常住芷江', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 51, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑再林', age: '71', relation: '户主', resident: '是', destination: '枣子山村部', phone: '18244833404', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: true },
    { idx: 52, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '李泽玉', age: '70', relation: '之妻', resident: '是', destination: '', phone: '', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 53, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑德球', age: '48', relation: '长子', resident: '否', destination: '', phone: '', remark: '常住碧涌', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 54, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '杨清梅', age: '44', relation: '长媳', resident: '否', destination: '', phone: '', remark: '常住碧涌', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 55, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑芸群', age: '22', relation: '长孙女', resident: '否', destination: '', phone: '', remark: '常住碧涌', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 56, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑景丰', age: '16', relation: '长孙子', resident: '否', destination: '', phone: '', remark: '常住碧涌', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 57, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑德云', age: '43', relation: '次子', resident: '否', destination: '', phone: '', remark: '常住碧涌', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 58, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '刘国英', age: '41', relation: '次媳', resident: '否', destination: '', phone: '', remark: '常住碧涌', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 59, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑雅晴', age: '17', relation: '次孙女', resident: '否', destination: '', phone: '', remark: '常住碧涌', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 60, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑雅文', age: '11', relation: '次孙子', resident: '否', destination: '', phone: '', remark: '常住碧涌', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 61, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑友洪', age: '53', relation: '户主', resident: '是', destination: '枣子山村部', phone: '13974532709', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: true },
    { idx: 62, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '李连秀', age: '48', relation: '之妻', resident: '是', destination: '', phone: '', remark: '常住本组', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 63, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑淳', age: '22', relation: '之子', resident: '否', destination: '', phone: '', remark: '麻阳教书，常住麻阳', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 64, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑翠平', age: '62', relation: '之姐', resident: '否', destination: '', phone: '', remark: '外嫁洪江', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 65, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '谭狮林', age: '36', relation: '外孙', resident: '否', destination: '', phone: '', remark: '洪江住', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 66, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑再均', age: '42', relation: '户主', resident: '是', destination: '本组外有房', phone: '19574527429', remark: '常住本组', transfer1: '', transfer2: '转移1人', transfer3: '转移1人', isHead: true },
    { idx: 67, village: '枣子山村', group: '大洋溪组', pointName: '大洋溪组滑坡', name: '郑羽芳', age: '15', relation: '之子', resident: '是', destination: '', phone: '', remark: '常住集镇', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 68, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁和松', age: '60', relation: '户主', resident: '是', destination: '半冲铁灯坡园艺场', phone: '15386255098', remark: '在家', transfer1: '', transfer2: '', transfer3: '转移2人', isHead: true },
    { idx: 69, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '粟正连', age: '59', relation: '之妻', resident: '是', destination: '', phone: '', remark: '在家', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 70, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁能文', age: '37', relation: '户主', resident: '否', destination: '半冲铁灯坡园艺场', phone: '17769457309', remark: '务工', transfer1: '', transfer2: '', transfer3: '转移3人', isHead: true },
    { idx: 71, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '沈丹', age: '34', relation: '之妻', resident: '否', destination: '', phone: '', remark: '务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 72, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁俊怡', age: '13', relation: '之女', resident: '是', destination: '', phone: '', remark: '在家', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 73, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁雨嫣', age: '11', relation: '之女', resident: '是', destination: '', phone: '', remark: '在家', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 74, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁和顺', age: '59', relation: '户主', resident: '是', destination: '半冲铁灯坡园艺场', phone: '6748618', remark: '在家', transfer1: '', transfer2: '', transfer3: '转移1人', isHead: true },
    { idx: 75, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁露露', age: '26', relation: '之女', resident: '否', destination: '', phone: '', remark: '外嫁', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 76, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁梳梳', age: '30', relation: '之女', resident: '否', destination: '', phone: '', remark: '外嫁', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 77, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '龚志源', age: '', relation: '户主', resident: '是', destination: '半冲铁灯坡园艺场', phone: '18674514558', remark: '广州务工', transfer1: '', transfer2: '', transfer3: '转移1人', isHead: true },
    { idx: 78, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁红梅', age: '', relation: '之妻', resident: '是', destination: '', phone: '', remark: '在家', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 79, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁和发', age: '53', relation: '户主', resident: '否', destination: '半冲铁灯坡园艺场', phone: '13415887965', remark: '务工', transfer1: '', transfer2: '', transfer3: '', isHead: true },
    { idx: 80, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '危登秀', age: '48', relation: '之妻', resident: '否', destination: '', phone: '', remark: '务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 81, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁玉红', age: '25', relation: '之女', resident: '否', destination: '', phone: '', remark: '外嫁', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 82, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁毓墙', age: '19', relation: '之子', resident: '否', destination: '', phone: '', remark: '务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 83, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁和清', age: '58', relation: '户主', resident: '是', destination: '半冲铁灯坡园艺场', phone: '17774563855', remark: '上海外务工', transfer1: '', transfer2: '', transfer3: '转移1人', isHead: true },
    { idx: 84, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '郭春香', age: '54', relation: '之妻', resident: '是', destination: '', phone: '', remark: '在家', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 85, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁燕', age: '32', relation: '之女', resident: '否', destination: '', phone: '', remark: '外嫁', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 86, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁幼龙', age: '24', relation: '之子', resident: '否', destination: '', phone: '', remark: '务工', transfer1: '', transfer2: '', transfer3: '', isHead: false },
    { idx: 87, village: '半冲村', group: '燕子岩组', pointName: '燕子岩滑坡', name: '梁冬生', age: '', relation: '户主', resident: '是', destination: '半冲铁灯坡园艺场', phone: '联系龚志源', remark: '在家', transfer1: '', transfer2: '', transfer3: '转移1人', isHead: true }
])

const filteredCutslope = computed(() => {
  if (!cutslopeSearch.value) return cutslopeData.value
  const kw = cutslopeSearch.value.toLowerCase()
  return cutslopeData.value.filter(r =>
    r.village.toLowerCase().includes(kw) ||
    r.name.toLowerCase().includes(kw) ||
    r.phone.toLowerCase().includes(kw)
  )
})

const filteredGeoPeople = computed(() => {
  if (!geoPeopleSearch.value) return geoPeopleData.value
  const kw = geoPeopleSearch.value.toLowerCase()
  return geoPeopleData.value.filter(r =>
    r.pointName.toLowerCase().includes(kw) ||
    r.village.toLowerCase().includes(kw) ||
    r.name.toLowerCase().includes(kw)
  )
})

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
.search-section { padding: 12px; }
.search-tip { font-size: 12px; color: #999; padding: 4px 16px 0; }
.empty-cell { padding: 40px; text-align: center; color: #999; font-size: 14px; }
.head-row { background: #FFF3E0 !important; font-weight: 600; }
.geology-list { padding: 12px; }
.geo-card { background: #fff; margin-bottom: 12px; border-radius: 12px; padding: 16px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.geo-card-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.geo-idx { display: inline-flex; align-items: center; justify-content: center; width: 24px; height: 24px; background: linear-gradient(135deg, #FFA726 0%, #E65100 100%); color: #fff; border-radius: 50%; font-size: 12px; font-weight: bold; }
.geo-name { font-size: 15px; font-weight: bold; color: #333; flex: 1; }
.geo-info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 8px 16px; margin-bottom: 12px; }
.geo-info-item { display: flex; flex-direction: column; gap: 2px; }
.geo-label { font-size: 11px; color: #999; }
.geo-val { font-size: 13px; color: #333; }
.geo-section-title { font-size: 13px; font-weight: bold; color: #E65100; margin-bottom: 8px; padding-left: 8px; border-left: 3px solid #FFA726; }
.geo-people-grid { display: flex; flex-wrap: wrap; gap: 8px; }
.geo-person { display: flex; align-items: center; gap: 4px; background: #FFF8E1; padding: 4px 10px; border-radius: 16px; font-size: 12px; }
.geo-person-role { color: #E65100; font-weight: 600; }
.geo-person-name { color: #333; }
.geo-person-phone { color: #666; }
</style>
