<template>
  <div class="page-container">
    <van-nav-bar title="脱贫户信息" left-arrow @click-left="goBack" />
    <div class="content-wrapper">
      <van-search 
        v-model="searchValue" 
        placeholder="搜索姓名或电话" 
        @search="onSearch"
      />
      <div class="stats-row">
        <div class="stat-item">
          <div class="stat-value">{{ povertyHouseholds.length }}</div>
          <div class="stat-label">脱贫户数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ totalMembers }}</div>
          <div class="stat-label">总人口</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ uniqueHelpers }}</div>
          <div class="stat-label">帮扶责任人</div>
        </div>
      </div>
      <van-table :columns="columns" :data="filteredData" row-key="id" sticky />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const searchValue = ref('')

const povertyHouseholds = ref([
  { id: 1, name: '叶睿', address: 'XX省XX市XX县生态村第9组67号', phone: '15874889514', members: 6, year: 2023, helper: '孙鑫博' },
  { id: 2, name: '苏鑫', address: 'XX省XX市XX县实干村第12组65号', phone: '14555677222', members: 5, year: 2018, helper: '潘丽' },
  { id: 3, name: '曾勇华', address: 'XX省XX市XX县美食村第7组32号', phone: '18740745696', members: 5, year: 2023, helper: '汪宇' },
  { id: 4, name: '田斌勇', address: 'XX省XX市XX县数字村第7组97号', phone: '19374922119', members: 5, year: 2020, helper: '孟明秀英' },
  { id: 5, name: '贾强', address: 'XX省XX市XX县安乐村第15组135号', phone: '14766843087', members: 6, year: 2019, helper: '吴平' },
  { id: 6, name: '杜桂英', address: 'XX省XX市XX县幸福里第10组145号', phone: '14580590881', members: 2, year: 2023, helper: '丁斌梓' },
  { id: 7, name: '杨洁', address: 'XX省XX市XX县共富村第1组94号', phone: '15591794273', members: 5, year: 2019, helper: '陈天' },
  { id: 8, name: '白健', address: 'XX省XX市XX县种植村第7组62号', phone: '17794501481', members: 2, year: 2022, helper: '张雨' },
  { id: 9, name: '黄彬鑫', address: 'XX省XX市XX县放心村第11组22号', phone: '17756543016', members: 6, year: 2024, helper: '郭鑫明' },
  { id: 10, name: '朱信', address: 'XX省XX市XX县诚信村第1组167号', phone: '15839342023', members: 3, year: 2020, helper: '徐静娜' },
  { id: 11, name: '龙慧', address: 'XX省XX市XX县创新村第7组101号', phone: '18464214079', members: 4, year: 2018, helper: '闫波' },
  { id: 12, name: '程浩', address: 'XX省XX市XX县为民村第7组181号', phone: '19231740221', members: 3, year: 2023, helper: '赵博修' },
  { id: 13, name: '赵烨炎', address: 'XX省XX市XX县革命村第9组87号', phone: '18972120506', members: 3, year: 2018, helper: '高永伟' },
  { id: 14, name: '萧娟平', address: 'XX省XX市XX县惠民村第9组137号', phone: '18642737494', members: 6, year: 2021, helper: '李佳' },
  { id: 15, name: '夏杰', address: 'XX省XX市XX县和睦村第14组68号', phone: '18666831356', members: 4, year: 2019, helper: '姜鸿佳' },
  { id: 16, name: '龚平雨', address: 'XX省XX市XX县文化村第11组78号', phone: '13284430390', members: 3, year: 2022, helper: '罗雨' },
  { id: 17, name: '雷彬', address: 'XX省XX市XX县智能村第2组104号', phone: '17171543685', members: 3, year: 2019, helper: '尹丹' },
  { id: 18, name: '孟军', address: 'XX省XX市XX县共享村第8组78号', phone: '13668557258', members: 4, year: 2025, helper: '史强慧' },
  { id: 19, name: '胡子', address: 'XX省XX市XX县样板村第15组142号', phone: '18428563632', members: 5, year: 2024, helper: '魏轩' },
  { id: 20, name: '马琪', address: 'XX省XX市XX县养殖村第3组72号', phone: '17892012512', members: 2, year: 2018, helper: '龚建' },
  { id: 21, name: '谭涛红', address: 'XX省XX市XX县新农村第13组175号', phone: '18352923062', members: 2, year: 2018, helper: '汤华' },
  { id: 22, name: '姜泽', address: 'XX省XX市XX县助民村第14组44号', phone: '13911820573', members: 3, year: 2025, helper: '林丽天' },
  { id: 23, name: '余昊勇', address: 'XX省XX市XX县友爱村第11组188号', phone: '13996190999', members: 2, year: 2022, helper: '孔文豪' },
  { id: 24, name: '谢红莉', address: 'XX省XX市XX县无公害村第15组170号', phone: '19364015274', members: 6, year: 2025, helper: '石文' },
  { id: 25, name: '任红', address: 'XX省XX市XX县能干村第13组177号', phone: '18503709761', members: 2, year: 2023, helper: '毛博' },
  { id: 26, name: '郭英桂', address: 'XX省XX市XX县智能村第1组33号', phone: '15264416817', members: 6, year: 2020, helper: '蒋轩鑫' },
  { id: 27, name: '魏伊文', address: 'XX省XX市XX县风景村第7组5号', phone: '18388402933', members: 6, year: 2022, helper: '江斌' },
  { id: 28, name: '范昊莉', address: 'XX省XX市XX县奋斗村第9组33号', phone: '18077392433', members: 1, year: 2019, helper: '万鹏丹' },
  { id: 29, name: '赵龙辉', address: 'XX省XX市XX县光明村第10组105号', phone: '17733172253', members: 5, year: 2019, helper: '潘伊涛' },
  { id: 30, name: '田博辉', address: 'XX省XX市XX县道德村第15组139号', phone: '13249299259', members: 6, year: 2024, helper: '郑丽' },
  { id: 31, name: '吕泽萍', address: 'XX省XX市XX县解放村第15组6号', phone: '13492655804', members: 3, year: 2025, helper: '魏杰黎' },
  { id: 32, name: '郝磊子', address: 'XX省XX市XX县卫生村第3组4号', phone: '13184581458', members: 4, year: 2024, helper: '邵宸黎' },
  { id: 33, name: '黄强丽', address: 'XX省XX市XX县电商村第14组115号', phone: '15141889143', members: 2, year: 2025, helper: '莫宸杰' },
  { id: 34, name: '覃泽建', address: 'XX省XX市XX县带民村第2组153号', phone: '13134453659', members: 6, year: 2023, helper: '白文敏' },
  { id: 35, name: '沈明彬', address: 'XX省XX市XX县正气村第15组161号', phone: '13424818256', members: 6, year: 2025, helper: '黄凯桂英' },
  { id: 36, name: '毛芳明', address: 'XX省XX市XX县开放村第13组149号', phone: '19963593382', members: 4, year: 2022, helper: '黄磊伟' },
  { id: 37, name: '姜轩兰', address: 'XX省XX市XX县新农村第8组23号', phone: '18490900244', members: 1, year: 2021, helper: '杜杰' },
  { id: 38, name: '韩平建', address: 'XX省XX市XX县开拓村第1组83号', phone: '17739852473', members: 1, year: 2020, helper: '秦超梅' },
  { id: 39, name: '叶洋俊', address: 'XX省XX市XX县前进村第4组38号', phone: '15156685134', members: 6, year: 2023, helper: '白英' },
  { id: 40, name: '戴凯', address: 'XX省XX市XX县绿色村第4组95号', phone: '18210893133', members: 1, year: 2018, helper: '董海伟' },
  { id: 41, name: '邓子娜', address: 'XX省XX市XX县奋斗村第8组56号', phone: '17770024841', members: 3, year: 2018, helper: '曾磊琳' },
  { id: 42, name: '顾雨君', address: 'XX省XX市XX县标兵村第7组74号', phone: '17635607737', members: 2, year: 2020, helper: '孟梅' },
  { id: 43, name: '方刚黎', address: 'XX省XX市XX县爱民村第10组150号', phone: '19396853802', members: 4, year: 2025, helper: '高琳强' },
  { id: 44, name: '宋伟文', address: 'XX省XX市XX县前进村第2组167号', phone: '15108571626', members: 5, year: 2020, helper: '袁波' },
  { id: 45, name: '谢伟', address: 'XX省XX市XX县放心村第12组112号', phone: '15019926523', members: 4, year: 2023, helper: '邹娜' },
  { id: 46, name: '沈凯', address: 'XX省XX市XX县旅游村第6组96号', phone: '17699175221', members: 6, year: 2019, helper: '郭红' },
  { id: 47, name: '谭刚梦', address: 'XX省XX市XX县胜利村第3组45号', phone: '18184794746', members: 3, year: 2025, helper: '龙宇' },
  { id: 48, name: '江文桂', address: 'XX省XX市XX县和平村第14组93号', phone: '18075067893', members: 1, year: 2024, helper: '曾辉' },
  { id: 49, name: '毛超霞', address: 'XX省XX市XX县直播村第5组95号', phone: '18456009258', members: 3, year: 2023, helper: '田泽' },
  { id: 50, name: '范雨', address: 'XX省XX市XX县实干村第5组13号', phone: '15268106249', members: 1, year: 2018, helper: '毛丹晨' }
])

const columns = [
  { title: '序号', dataIndex: 'id', width: 60 },
  { title: '户主姓名', dataIndex: 'name', width: 100 },
  { title: '地址', dataIndex: 'address', ellipsis: true },
  { title: '电话', dataIndex: 'phone', width: 120 },
  { title: '人数', dataIndex: 'members', width: 60 },
  { title: '脱贫年份', dataIndex: 'year', width: 80 },
  { title: '帮扶责任人', dataIndex: 'helper', width: 100 }
]

const filteredData = computed(() => {
  if (!searchValue.value) return povertyHouseholds.value
  const search = searchValue.value.toLowerCase()
  return povertyHouseholds.value.filter(item => 
    item.name.toLowerCase().includes(search) ||
    item.phone.includes(search) ||
    item.address.toLowerCase().includes(search)
  )
})

const totalMembers = computed(() => {
  return povertyHouseholds.value.reduce((sum, item) => sum + item.members, 0)
})

const uniqueHelpers = computed(() => {
  return new Set(povertyHouseholds.value.map(item => item.helper)).size
})

const goBack = () => {
  router.back()
}

const onSearch = () => {
  console.log('搜索:', searchValue.value)
}

onMounted(() => {
  document.title = '脱贫户信息 - 村级智慧村务平台'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.content-wrapper {
  padding: 16px;
}

.stats-row {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.stat-item {
  flex: 1;
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
  border-radius: 12px;
  padding: 16px 12px;
  text-align: center;
  color: #fff;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
}

.stat-label {
  font-size: 12px;
  opacity: 0.9;
  margin-top: 4px;
}
</style>
