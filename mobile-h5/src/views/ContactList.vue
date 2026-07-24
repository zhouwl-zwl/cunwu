<template>
  <div class="page-container">
    <van-nav-bar title="通讯录" left-arrow @click-left="goBack" />
    
    <div class="search-bar">
      <van-search v-model="searchKeyword" placeholder="搜索姓名、职务、电话..." shape="round" />
    </div>

    <div class="tabs-container">
      <van-tabs v-model="activeTab" sticky>
        <van-tab title="全部" name="all" />
        <van-tab title="冬瓜坡村" name="冬瓜坡村" />
        <van-tab title="枣子山村" name="枣子山村" />
        <van-tab title="新店村" name="新店村" />
        <van-tab title="半冲村" name="半冲村" />
        <van-tab title="兴无村" name="兴无村" />
        <van-tab title="马坡村" name="马坡村" />
        <van-tab title="罗卜田村" name="罗卜田村" />
      </van-tabs>
    </div>

    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-value">{{ filteredList.length }}</span>
        <span class="stat-label">人员</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ villages.length }}</span>
        <span class="stat-label">村落</span>
      </div>
    </div>

    <div class="content-list">
      <div 
        v-for="(village, vIndex) in filteredVillages" 
        :key="village.name"
        class="village-section"
      >
        <div class="village-header">
          <div class="village-name">{{ village.name }}</div>
          <div class="village-count">{{ village.members.length }}人</div>
        </div>
        
        <div 
          v-for="(member, mIndex) in village.members" 
          :key="mIndex"
          class="member-card"
          @click="showMemberDetail(member)"
        >
          <div class="member-avatar">
            {{ member.name.charAt(0) }}
          </div>
          <div class="member-info">
            <div class="member-name">{{ member.name }}</div>
            <div class="member-position">{{ member.position }}</div>
          </div>
          <div class="member-phone">
            <van-icon name="phone" size="16" color="#4CAF50" />
            <span>{{ formatPhone(member.phone) }}</span>
          </div>
          <van-icon name="arrow" size="16" color="#ccc" />
        </div>
      </div>

      <van-empty v-if="filteredVillages.length === 0" description="暂无人员信息" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showDialog } from 'vant'

const router = useRouter()
const searchKeyword = ref('')
const activeTab = ref('all')

const villages = ref([
  {
    name: '冬瓜坡村',
    members: [
      { position: '党支部书记、村委会主任', name: '李复艳', phone: '18374555056', remark: '' },
      { position: '党支部组织、宣传委员、村文', name: '李静', phone: '17388897515', remark: '' },
      { position: '党支部纪检委员', name: '刘宗荣', phone: '18705962422', remark: '' },
      { position: '村委会副主任', name: '李朝健', phone: '19918451108', remark: '' },
      { position: '村妇联主席', name: '张小芳', phone: '17769225569', remark: '' }
    ]
  },
  {
    name: '枣子山村',
    members: [
      { position: '党支部书记、村委会主任', name: '郑明明', phone: '13467404567', remark: '' },
      { position: '党支部组织、宣传委员、村妇', name: '刘正玉', phone: '17363760232', remark: '' },
      { position: '党支部纪检委员', name: '杨群', phone: '17308459456', remark: '' },
      { position: '村委会副主任', name: '郑芳', phone: '18974951799', remark: '' },
      { position: '村文书', name: '郑邦方', phone: '18707456651', remark: '' }
    ]
  },
  {
    name: '新店村',
    members: [
      { position: '党总支书记、村委会主任', name: '张露海', phone: '18847888691', remark: '' },
      { position: '党总支纪检委员', name: '张小华', phone: '19918453375', remark: '' },
      { position: '党总支组织委员、村文书', name: '杨堃', phone: '18796229401', remark: '' },
      { position: '党总支宣传委员、村妇联主席', name: '龙小梅', phone: '17794321019', remark: '' },
      { position: '村委会主任', name: '彭开红', phone: '15115254832', remark: '' },
      { position: '党支部书记、村委会主任', name: '彭开红', phone: '18273853514', remark: '' },
      { position: '党支部组织、宣传委员、村文', name: '彭兵', phone: '17774592207', remark: '' },
      { position: '党支部纪检委员', name: '唐昌齐', phone: '18745236661', remark: '' },
      { position: '村妇联主席', name: '潘金花', phone: '18974951799', remark: '' }
    ]
  },
  {
    name: '半冲村',
    members: [
      { position: '党支部书记、村委会主任', name: '陈宏', phone: '13807453766', remark: '' },
      { position: '乡村振兴第一书记、队长', name: '陈宏', phone: '13807453766', remark: '' },
      { position: '乡村振兴队员', name: '李孝长', phone: '18374519627', remark: '' },
      { position: '乡村振兴队员', name: '陈真', phone: '18974516158', remark: '' },
      { position: '乡村振兴队员', name: '', phone: '', remark: '' }
    ]
  },
  {
    name: '兴无村',
    members: [
      { position: '党支部书记、村委会主任', name: '向勇', phone: '18174563699', remark: '' },
      { position: '党支部组织、宣传委员村妇联主', name: '向艾红', phone: '17769246889', remark: '' },
      { position: '党支部纪检委员', name: '张跃', phone: '19918537956', remark: '' },
      { position: '村文书', name: '杨雪玲', phone: '13874566743', remark: '' },
      { position: '乡村振兴第一书记、队长', name: '杨英涛', phone: '13769247711', remark: '' },
      { position: '乡村振兴队员', name: '张绍平', phone: '13874464265', remark: '' },
      { position: '乡村振兴队员', name: '刘文学', phone: '13973080528', remark: '' }
    ]
  },
  {
    name: '马坡村',
    members: [
      { position: '党支部书记、村委会主任', name: '杨再明', phone: '18174563700', remark: '' },
      { position: '党支部组织、宣传委员', name: '杨再明', phone: '18174563700', remark: '' },
      { position: '党支部纪检委员', name: '杨再明', phone: '18174563700', remark: '' },
      { position: '村委会副主任', name: '', phone: '', remark: '' },
      { position: '村文书', name: '', phone: '', remark: '' }
    ]
  },
  {
    name: '罗卜田村',
    members: [
      { position: '党支部书记、村委会主任', name: '周贤文', phone: '18174563701', remark: '' },
      { position: '党支部组织、宣传委员', name: '', phone: '', remark: '' },
      { position: '党支部纪检委员', name: '', phone: '', remark: '' },
      { position: '村委会副主任', name: '', phone: '', remark: '' },
      { position: '村文书', name: '', phone: '', remark: '' }
    ]
  }
])

const filteredVillages = computed(() => {
  let result = villages.value
  
  if (activeTab.value !== 'all') {
    result = result.filter(v => v.name === activeTab.value)
  }
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.map(village => ({
      ...village,
      members: village.members.filter(member => 
        member.name.toLowerCase().includes(keyword) ||
        member.position.toLowerCase().includes(keyword) ||
        member.phone.includes(keyword)
      )
    })).filter(village => village.members.length > 0)
  }
  
  return result
})

const filteredList = computed(() => {
  return filteredVillages.value.reduce((sum, v) => sum + v.members.length, 0)
})

const formatPhone = (phone) => {
  if (!phone || phone.length < 11) return phone
  return phone.replace(/(\d{3})(\d{4})(\d{4})/, '$1****$3')
}

const goBack = () => {
  router.back()
}

const showMemberDetail = (member) => {
  if (!member.phone) return
  showDialog({
    title: member.name,
    message: `职务：${member.position}\n电话：${member.phone}`,
    confirmButtonText: '拨打电话',
    confirmButtonColor: '#4CAF50',
    cancelButtonText: '关闭'
  }).then(() => {
    window.location.href = `tel:${member.phone}`
  }).catch(() => {})
}

onMounted(() => {
  document.title = '通讯录 - 村级智慧村务平台'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.search-bar {
  background: #fff;
  padding: 8px 12px;
}

.tabs-container {
  background: #fff;
}

.stats-bar {
  display: flex;
  background: #fff;
  padding: 12px 16px;
  margin-bottom: 12px;
  justify-content: center;
  gap: 40px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #D22630;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.content-list {
  padding: 0 12px;
}

.village-section {
  background: #fff;
  border-radius: 12px;
  margin-bottom: 12px;
  overflow: hidden;
}

.village-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px;
  background: linear-gradient(135deg, rgba(210, 38, 48, 0.06) 0%, rgba(210, 38, 48, 0.02) 100%);
  border-bottom: 1px solid #f0f0f0;
}

.village-name {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
}

.village-count {
  font-size: 13px;
  color: #999;
  background: #fff;
  padding: 4px 10px;
  border-radius: 10px;
}

.member-card {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: background 0.2s;
}

.member-card:last-child {
  border-bottom: none;
}

.member-card:active {
  background: #f9f9f9;
}

.member-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  margin-right: 14px;
}

.member-info {
  flex: 1;
  min-width: 0;
}

.member-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.member-position {
  font-size: 12px;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.member-phone {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #4CAF50;
  margin-right: 8px;
}
</style>