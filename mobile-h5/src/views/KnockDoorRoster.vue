<template>
  <div class="page-container">
    <van-nav-bar title="重点敲门对象花名册" left-arrow @click-left="goBack" />

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ tableData.length }}</span>
        <span class="stats-label">总人数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ villageCount }}</span>
        <span class="stats-label">涉及村落</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ visitedCount }}</span>
        <span class="stats-label">已上门宣传</span>
      </div>
    </div>

    <div class="search-section">
      <van-search v-model="searchKeyword" placeholder="搜索姓名/村/地址" shape="round" />
    </div>

    <div class="filter-bar">
      <van-tabs v-model="activeTab" sticky>
        <van-tab title="全部" name="all" />
        <van-tab v-for="v in villageList" :key="v" :title="v" :name="v" />
      </van-tabs>
    </div>

    <div class="table-wrapper">
      <div class="table-title">罗卜田乡重点敲门对象花名册</div>
      <div class="table-scroll">
        <table class="data-table">
          <thead>
            <tr>
              <th>序号</th>
              <th>姓名</th>
              <th>性别</th>
              <th>联系方式</th>
              <th>家庭住址</th>
              <th>是否上门宣传</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in filteredData" :key="row.idx">
              <td>{{ row.idx }}</td>
              <td class="bold">{{ row.name }}</td>
              <td>{{ row.gender }}</td>
              <td>{{ row.phone }}</td>
              <td>{{ row.address }}</td>
              <td><span class="status-tag done">{{ row.visited }}</span></td>
            </tr>
            <tr v-if="filteredData.length === 0">
              <td colspan="6" class="empty-cell">暂无数据</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchKeyword = ref('')
const activeTab = ref('all')

const tableData = ref([
  { idx: 1, name: '张秀良', gender: '男', phone: '13648457437', address: '罗卜田村野田冲组', visited: '是' },
  { idx: 2, name: '张廷兵', gender: '男', phone: '18274540318', address: '半冲村大水江组', visited: '是' },
  { idx: 3, name: '龙淑珍', gender: '女', phone: '13034887433', address: '兴无村社冲组', visited: '是' },
  { idx: 4, name: '周蓉', gender: '女', phone: '19372392249', address: '罗卜田村大坪田组', visited: '是' },
  { idx: 5, name: '张昌连', gender: '男', phone: '19374535847', address: '半冲村大水江组', visited: '是' },
  { idx: 6, name: '向光立', gender: '男', phone: '13034887433', address: '兴无村社冲组', visited: '是' },
  { idx: 7, name: '张雨童', gender: '女', phone: '15674502690', address: '罗卜田村烂泥冲组', visited: '是' },
  { idx: 8, name: '张秀槐', gender: '男', phone: '17872452116', address: '半冲村饭炉坡组', visited: '是' },
  { idx: 9, name: '邓运龙', gender: '男', phone: '18274523060', address: '兴无村黄家溪组', visited: '是' },
  { idx: 10, name: '张雨馨', gender: '女', phone: '15674502690', address: '罗卜田村烂泥冲组', visited: '是' },
  { idx: 11, name: '彭宗坤', gender: '男', phone: '15580132087', address: '半冲村青山冲组', visited: '是' },
  { idx: 12, name: '张青梅', gender: '女', phone: '17769245689', address: '兴无村败泥古组', visited: '是' },
  { idx: 13, name: '张道金', gender: '男', phone: '13187145311', address: '罗卜田村老鸭溪组', visited: '是' },
  { idx: 14, name: '潘万发', gender: '男', phone: '13467943117', address: '半冲村大水江组', visited: '是' },
  { idx: 15, name: '向宗球', gender: '男', phone: '19873766399', address: '兴无村沙湾冲组', visited: '是' },
  { idx: 16, name: '刘正炳', gender: '男', phone: '15674585193', address: '罗卜田村木田冲组', visited: '是' },
  { idx: 17, name: '许自国', gender: '男', phone: '15576564097', address: '半冲村欧家冲组', visited: '是' },
  { idx: 18, name: '张隆明', gender: '男', phone: '15580668782', address: '兴无村罗家山组', visited: '是' },
  { idx: 19, name: '覃献平', gender: '男', phone: '18474523072', address: '罗卜田村蒲家冲组', visited: '是' },
  { idx: 20, name: '周宏娥', gender: '女', phone: '15576564097', address: '半冲村欧家冲组', visited: '是' },
  { idx: 21, name: '李国常', gender: '男', phone: '18274563049', address: '罗卜田村桐子坪组', visited: '是' },
  { idx: 22, name: '张庭堂', gender: '男', phone: '13874487647', address: '罗卜田村瓦一坳组', visited: '是' },
  { idx: 23, name: '江贤坤', gender: '男', phone: '19918517099', address: '冬瓜坡村杨家浪组', visited: '是' },
  { idx: 24, name: '邓长荣', gender: '男', phone: '17769222059', address: '冬瓜坡村岩头冲组', visited: '是' },
  { idx: 25, name: '张茂秀', gender: '女', phone: '13637458976', address: '冬瓜坡村泥冲组', visited: '是' },
  { idx: 26, name: '陈玉香', gender: '女', phone: '18390323377', address: '冬瓜坡村黄花坪组', visited: '是' },
  { idx: 27, name: '覃现桥', gender: '男', phone: '19918591545', address: '冬瓜坡村黄花坪组', visited: '是' },
  { idx: 28, name: '黄元国', gender: '男', phone: '13469358032', address: '冬瓜坡村郭家冲组', visited: '是' },
  { idx: 29, name: '江贤梅', gender: '女', phone: '13691718257', address: '冬瓜坡村湾里组', visited: '是' },
  { idx: 30, name: '李复忠', gender: '男', phone: '18692549969', address: '冬瓜坡村水竹园组', visited: '是' },
  { idx: 31, name: '龙桂莲', gender: '女', phone: '19145957486', address: '冬瓜坡村郭家冲组', visited: '是' },
  { idx: 32, name: '邓运清', gender: '男', phone: '18274569864', address: '枣子山村岩者坡组', visited: '是' },
  { idx: 33, name: '李泽秀', gender: '女', phone: '15974028982', address: '枣子山村岩者坡组', visited: '是' },
  { idx: 34, name: '郑秀查', gender: '男', phone: '19974528935', address: '枣子山村枞山脚组', visited: '是' },
  { idx: 35, name: '郑秀湘', gender: '男', phone: '19918501621', address: '枣子山村枣子山组', visited: '是' },
  { idx: 36, name: '郑光松', gender: '男', phone: '15074543344', address: '枣子山村陈家坪组', visited: '是' },
  { idx: 37, name: '郑德顺', gender: '男', phone: '13607454854', address: '枣子山村桥边组', visited: '是' },
  { idx: 38, name: '郑细毛', gender: '男', phone: '15111561899', address: '枣子山村新屋场组', visited: '是' },
  { idx: 39, name: '郑时槐', gender: '男', phone: '15211573120', address: '新店村肖家湾组', visited: '是' },
  { idx: 40, name: '刘志明', gender: '男', phone: '19350197159', address: '新店村黄双溪组', visited: '是' },
  { idx: 41, name: '李伟青', gender: '男', phone: '15111544722', address: '新店村牛塘坡组', visited: '是' },
  { idx: 42, name: '邓长友', gender: '男', phone: '15581548721', address: '新店村老坡背组', visited: '是' },
  { idx: 43, name: '龙林海', gender: '男', phone: '15226420960', address: '新店村月亮田组', visited: '是' },
  { idx: 44, name: '龙林金', gender: '男', phone: '18474520552', address: '新店村月亮田组', visited: '是' },
  { idx: 45, name: '张志英', gender: '女', phone: '17707451290', address: '新店村磨刀堰组', visited: '是' },
  { idx: 46, name: '杨胜玉', gender: '女', phone: '18774744675', address: '新店村大冲组', visited: '是' },
  { idx: 47, name: '蒋早发', gender: '男', phone: '19918591475', address: '新店村板山坪组', visited: '是' },
  { idx: 48, name: '杨儒莲', gender: '男', phone: '13204991687', address: '新店村方木坪组', visited: '是' },
  { idx: 49, name: '邓序常', gender: '男', phone: '17775925813', address: '马坡村马坡组', visited: '是' },
  { idx: 50, name: '蒋国松', gender: '男', phone: '15580667011', address: '马坡村白竹山组', visited: '是' },
  { idx: 51, name: '蒋国凤', gender: '女', phone: '15074535213', address: '马坡村屋背界组', visited: '是' },
  { idx: 52, name: '邓运贵', gender: '女', phone: '18797603847', address: '马坡村屋背界组', visited: '是' },
  { idx: 53, name: '刘脱犯', gender: '女', phone: '17374527905', address: '马坡村桃家垅组', visited: '是' },
  { idx: 54, name: '李复南', gender: '男', phone: '18174579973', address: '马坡村屋背界组', visited: '是' },
  { idx: 55, name: '李培龙', gender: '男', phone: '13789266587', address: '马坡村屋背界组', visited: '是' },
  { idx: 56, name: '刘国菊', gender: '女', phone: '15574506329', address: '马坡村核桃坪组', visited: '是' },
  { idx: 57, name: '李泽元', gender: '男', phone: '15901932302', address: '马坡村桃家垅组', visited: '是' }
])

const villageList = computed(() => {
  const set = new Set(tableData.value.map(r => r.address.replace(/(\d+|组)$/, '').trim()))
  return [...new Set(tableData.value.map(r => {
    const m = r.address.match(/^(.+?村)/)
    return m ? m[1] : r.address
  }))].sort()
})

const villageCount = computed(() => villageList.value.length)
const visitedCount = computed(() => tableData.value.filter(r => r.visited === '是').length)

const filteredData = computed(() => {
  let result = tableData.value
  if (activeTab.value !== 'all') {
    result = result.filter(r => r.address.startsWith(activeTab.value))
  }
  if (searchKeyword.value) {
    const kw = searchKeyword.value.toLowerCase()
    result = result.filter(r =>
      r.name.toLowerCase().includes(kw) ||
      r.address.toLowerCase().includes(kw) ||
      r.phone.includes(kw)
    )
  }
  return result
})

const goBack = () => router.back()
</script>

<style scoped>
.page-container { min-height: 100vh; background: #f5f5f5; padding-bottom: 20px; }
.stats-card { display: flex; background: linear-gradient(135deg, #FFA726 0%, #E65100 100%); padding: 20px 12px; margin-bottom: 12px; }
.stats-item { flex: 1; text-align: center; }
.stats-num { display: block; font-size: 24px; font-weight: bold; color: #fff; margin-bottom: 4px; }
.stats-label { font-size: 12px; color: rgba(255,255,255,0.8); }
.search-section { padding: 0 12px; margin-bottom: 8px; }
.filter-bar { margin-bottom: 8px; }
.table-wrapper { background: #fff; margin: 0 12px; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.table-title { font-size: 14px; font-weight: bold; color: #333; padding: 14px 12px; text-align: center; background: linear-gradient(135deg, #FFE0B2 0%, #FFCC80 100%); border-bottom: 2px solid #FFA726; }
.table-scroll { overflow-x: auto; -webkit-overflow-scrolling: touch; }
.data-table { width: 100%; min-width: 700px; border-collapse: collapse; font-size: 12px; }
.data-table thead { background: linear-gradient(135deg, #FFA726 0%, #E65100 100%); position: sticky; top: 0; z-index: 10; }
.data-table th { padding: 10px 6px; text-align: center; color: #fff; font-weight: 600; white-space: nowrap; border-right: 1px solid rgba(255,255,255,0.15); }
.data-table th:last-child { border-right: none; }
.data-table tbody tr:nth-child(even) { background: #FFF8E1; }
.data-table tbody tr:nth-child(odd) { background: #fff; }
.data-table td { padding: 8px 6px; text-align: center; color: #333; white-space: nowrap; border-right: 1px solid #f5e0c0; border-bottom: 1px solid #f5ebe8; }
.data-table td:last-child { border-right: none; }
.data-table td.bold { font-weight: bold; }
.status-tag { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; }
.status-tag.done { background: #4CAF50; color: #fff; }
.empty-cell { padding: 40px; text-align: center; color: #999; }
</style>
