<template>
  <div class="page-container">
    <van-nav-bar title="通讯录" left-arrow @click-left="goBack">
      <template #right>
        <van-icon name="share-o" size="20" color="#fff" @click="handleShare" />
      </template>
    </van-nav-bar>
    
    <div class="search-bar">
      <van-search 
        v-model="searchKeyword" 
        :placeholder="searchPlaceholder" 
        shape="round"
        @clear="searchKeyword = ''"
      >
        <template #left-icon>
          <van-icon name="search" size="16" />
        </template>
      </van-search>
      <div class="search-filter">
        <span class="filter-label">搜索范围：</span>
        <van-radio-group v-model="searchField" direction="horizontal">
          <van-radio name="all">全部</van-radio>
          <van-radio name="name">姓名</van-radio>
          <van-radio name="position">职务</van-radio>
          <van-radio name="phone">电话</van-radio>
        </van-radio-group>
      </div>
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
      <div class="stat-item export-btn" @click="exportExcel">
        <van-icon name="down" size="22" color="#fff" />
        <span class="stat-label" style="color:#fff">导出Excel</span>
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
        >
          <div class="member-avatar">
            {{ member.name.charAt(0) }}
          </div>
          <div class="member-info" @click="showMemberDetail(member)">
            <div class="member-name">{{ member.name }}</div>
            <div class="member-position">{{ member.position }}</div>
          </div>
          <div class="member-phone" @click="callPhone(member)">
            <van-icon name="phone" size="16" color="#4CAF50" />
            <span>{{ formatPhone(member.phone) }}</span>
          </div>
          <div class="member-actions">
            <a 
              v-if="member.phone"
              :href="`tel:${member.phone}`" 
              class="call-btn"
              @click.prevent="callPhone(member)"
            >
              <van-icon name="phone" size="16" color="#fff" />
            </a>
            <div class="share-btn" @click="shareMember(member)">
              <van-icon name="share-o" size="16" color="#4CAF50" />
            </div>
          </div>
        </div>
      </div>

      <van-empty v-if="filteredVillages.length === 0" description="暂无人员信息" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showDialog, showToast, RadioGroup, Radio } from 'vant'
import * as XLSX from 'xlsx'
import { showSharePanel } from '@/utils/share'

const router = useRouter()
const searchKeyword = ref('')
const searchField = ref('all')
const activeTab = ref('all')

const searchPlaceholder = computed(() => {
  const map = {
    all: '搜索姓名、职务、电话...',
    name: '输入姓名搜索...',
    position: '输入职务搜索...',
    phone: '输入电话号码搜索...'
  }
  return map[searchField.value] || map.all
})

const villages = ref([
  {
    name: '冬瓜坡村',
    members: [
      { position: '党支部书记、村委会主任', name: '李复艳', phone: '18374555056', remark: '' },
      { position: '党支部组织、宣传委员、村文书', name: '李静', phone: '17788897515', remark: '' },
      { position: '党支部纪检委员', name: '刘宗荣', phone: '18075996242', remark: '' },
      { position: '村委会副主任', name: '李培健', phone: '19918451108', remark: '' },
      { position: '村妇联主席', name: '张小芳', phone: '17769225569', remark: '' }
    ]
  },
  {
    name: '枣子山村',
    members: [
      { position: '党支部书记、村委会主任', name: '郑明明', phone: '13467404567', remark: '' },
      { position: '党支部组织、宣传委员、村妇联', name: '刘正玉', phone: '17365760232', remark: '' },
      { position: '党支部纪检委员', name: '杨群', phone: '17308459456', remark: '' },
      { position: '村委会副主任', name: '郑芳', phone: '18974591799', remark: '' },
      { position: '村文书', name: '郑邱方', phone: '18707456651', remark: '' }
    ]
  },
  {
    name: '新店村',
    members: [
      { position: '党总支书记、村委会主任', name: '张露海', phone: '18874588691', remark: '' },
      { position: '党总支纪检委员', name: '张小华', phone: '19918453755', remark: '' },
      { position: '党总支组织委员、村文书', name: '杨堃', phone: '18796229401', remark: '' },
      { position: '党总支宣传委员、村妇联主席', name: '龙小梅', phone: '17774523109', remark: '' }
    ]
  },
  {
    name: '半冲村',
    members: [
      { position: '党支部书记、村委会主任', name: '彭开红', phone: '15115254832', remark: '' },
      { position: '党支部组织、宣传委员、村文书', name: '彭兵', phone: '18273853514', remark: '' },
      { position: '党支部纪检委员', name: '唐昌齐', phone: '17774592207', remark: '' },
      { position: '村妇联主席', name: '蒲金花', phone: '18474525661', remark: '' },
      { position: '乡村振兴第一书记、队长', name: '陈东', phone: '13807457766', remark: '' },
      { position: '乡村振兴队员', name: '李承长', phone: '18574519627', remark: '' },
      { position: '乡村振兴队员', name: '陈真', phone: '18974516158', remark: '' }
    ]
  },
  {
    name: '兴无村',
    members: [
      { position: '党支部书记、村委会主任', name: '向勇', phone: '18174586399', remark: '' },
      { position: '党支部组织、宣传委员村妇联主席', name: '向艾红', phone: '17769245689', remark: '' },
      { position: '党支部纪检委员', name: '张跃', phone: '19918537956', remark: '' },
      { position: '村文书', name: '杨雪玲', phone: '13874560743', remark: '' },
      { position: '乡村振兴第一书记、队长', name: '杨英涛', phone: '13769247711', remark: '' },
      { position: '乡村振兴队员', name: '张绍平', phone: '13874464265', remark: '' },
      { position: '乡村振兴队员', name: '刘文军', phone: '13973080528', remark: '' }
    ]
  },
  {
    name: '马坡村',
    members: [
      { position: '党支部书记、村委会主任', name: '李友香', phone: '18075598897', remark: '' },
      { position: '党支部组织、宣传委员', name: '李小洲', phone: '18075595377', remark: '' },
      { position: '党支部纪检委员', name: '李复胜', phone: '19974521375', remark: '' },
      { position: '村文书', name: '李华', phone: '18273199594', remark: '' },
      { position: '村妇联主席', name: '郑卫芳', phone: '19375089447', remark: '' }
    ]
  },
  {
    name: '罗卜田村',
    members: [
      { position: '党总支书记、村委会主任', name: '钦山', phone: '13874417046', remark: '' },
      { position: '党总支副书记', name: '刘锋', phone: '13647457437', remark: '' },
      { position: '党总支纪检委员', name: '李利华', phone: '13787544708', remark: '' },
      { position: '党总支组织委员、村妇联主席', name: '邓京南', phone: '18074525378', remark: '' },
      { position: '党总支宣传委员、村文书', name: '李艳琪', phone: '15226463734', remark: '' }
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
    result = result.map(village => {
      let filtered = village.members
      if (searchField.value === 'name') {
        filtered = village.members.filter(m => m.name.toLowerCase().includes(keyword))
      } else if (searchField.value === 'position') {
        filtered = village.members.filter(m => m.position.toLowerCase().includes(keyword))
      } else if (searchField.value === 'phone') {
        filtered = village.members.filter(m => m.phone.includes(keyword))
      } else {
        filtered = village.members.filter(m =>
          m.name.toLowerCase().includes(keyword) ||
          m.position.toLowerCase().includes(keyword) ||
          m.phone.includes(keyword)
        )
      }
      return { ...village, members: filtered }
    }).filter(village => village.members.length > 0)
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

const exportExcel = () => {
  const data = []
  filteredVillages.value.forEach(village => {
    village.members.forEach(member => {
      data.push({
        '村名': village.name,
        '姓名': member.name,
        '职务': member.position,
        '联系电话': member.phone,
        '备注': member.remark || ''
      })
    })
  })

  if (data.length === 0) {
    showToast('暂无可导出数据')
    return
  }

  const ws = XLSX.utils.json_to_sheet(data)
  ws['!cols'] = [
    { wch: 14 },
    { wch: 12 },
    { wch: 32 },
    { wch: 16 },
    { wch: 16 }
  ]
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, ws, '通讯录')

  const fileName = activeTab.value === 'all'
    ? '罗卜田乡村干部通讯录.xlsx'
    : `${activeTab.value}通讯录.xlsx`
  XLSX.writeFile(wb, fileName)
  showToast(`已导出 ${data.length} 条数据`)
}

const showMemberDetail = (member) => {
  if (!member.phone) {
    showDialog({
      title: member.name,
      message: `职务：${member.position}`,
      confirmButtonText: '确定'
    })
    return
  }
  showDialog({
    title: member.name,
    message: `职务：${member.position}\n电话：${member.phone}`,
    confirmButtonText: '拨打电话',
    confirmButtonColor: '#4CAF50',
    cancelButtonText: '关闭'
  }).then(() => {
    doCall(member.phone)
  }).catch(() => {})
}

const callPhone = (member) => {
  if (!member.phone) {
    showToast('暂无电话号码')
    return
  }
  doCall(member.phone)
}

const doCall = (phone) => {
  showDialog({
    title: '拨打电话',
    message: `确定要拨打 ${phone} 吗？`,
    confirmButtonText: '拨打',
    confirmButtonColor: '#4CAF50',
    cancelButtonText: '取消'
  }).then(() => {
    window.location.href = `tel:${phone}`
  }).catch(() => {})
}

const handleShare = () => {
  showSharePanel({
    title: '罗卜田乡通讯录',
    text: '查看罗卜田乡支两委联系方式'
  })
}

const shareMember = (member) => {
  showSharePanel({
    title: `${member.name} - ${member.position}`,
    text: `${member.position}：${member.name}\n电话：${member.phone || '暂无'}`,
    url: window.location.href
  })
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

.search-filter {
  display: flex;
  align-items: center;
  margin-top: 8px;
  padding: 8px 0;
  font-size: 13px;
}

.filter-label {
  color: #666;
  margin-right: 12px;
  font-weight: 500;
}

.search-filter .van-radio-group {
  flex: 1;
}

.search-filter .van-radio {
  font-size: 13px;
}

.search-filter .van-radio__label {
  font-size: 13px;
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

.export-btn {
  background: var(--gradient-primary);
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  gap: 4px;
  transition: transform 0.2s;
  box-shadow: 0 2px 8px rgba(210, 38, 48, 0.3);
}

.export-btn:active {
  transform: scale(0.95);
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
  background: var(--gradient-primary-light);
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
  cursor: default;
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
  cursor: pointer;
}

.call-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--gradient-success);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: none;
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
  transition: transform 0.2s, box-shadow 0.2s;
  text-decoration: none;
}

.call-btn:active {
  transform: scale(0.92);
  box-shadow: 0 1px 4px rgba(76, 175, 80, 0.3);
}

.member-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.share-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--gradient-blue);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.share-btn:active {
  transform: scale(0.92);
}
</style>