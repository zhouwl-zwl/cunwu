<template>
  <div class="population-page page-container">
    <van-nav-bar title="人口户籍" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="我的户籍" name="my" />
      <van-tab title="全村户籍" name="all" />
      <van-tab title="外出务工" name="migrant" />
      <van-tab title="户籍变动" name="change" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'my'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">🏠 我的户籍信息</div>
          <van-button type="primary" size="mini" @click="showEditModal = true">修改申请</van-button>
        </div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">户主</span>
            <span class="info-value">{{ myInfo.headOfHousehold }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">村组</span>
            <span class="info-value">{{ myInfo.group }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">户籍地址</span>
            <span class="info-value">{{ myInfo.address }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">联系电话</span>
            <span class="info-value">{{ myInfo.phone }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">年龄</span>
            <span class="info-value">{{ myInfo.age }}岁</span>
          </div>
          <div class="info-item">
            <span class="info-label">参保状态</span>
            <span class="info-value">{{ myInfo.insured }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">就学情况</span>
            <span class="info-value">{{ myInfo.education }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">务工状态</span>
            <span class="info-value">{{ myInfo.employment }}</span>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">👨‍👩‍👧‍👦 家庭成员</div>
        </div>
        <div class="member-list">
          <div v-for="member in familyMembers" :key="member.id" class="member-card">
            <div class="member-avatar">{{ member.name.charAt(0) }}</div>
            <div class="member-info">
              <div class="member-name">{{ member.name }}</div>
              <div class="member-relation">{{ member.relation }}</div>
              <div class="member-detail">{{ member.idCard }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📷 户口本照片</div>
          <van-button type="primary" size="mini" @click="uploadPhoto">上传照片</van-button>
        </div>
        <div class="photo-grid">
          <div v-for="photo in myPhotos" :key="photo.id" class="photo-item" @click="previewPhoto(photo)">
            <img :src="photo.url" :alt="photo.name" />
          </div>
          <div class="photo-upload" @click="uploadPhoto">
            <span>+</span>
          </div>
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'all'">
      <div class="filter-bar">
        <van-picker :columns="groupColumns" @confirm="onGroupSelect" placeholder="选择村组" />
        <van-picker :columns="ageColumns" @confirm="onAgeSelect" placeholder="年龄段" />
        <van-switch v-model="showMigrant" active-text="显示外出" />
      </div>

      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showAddModal = true">新增户籍</van-button>
      </div>

      <div class="search-bar">
        <van-search v-model="searchKeyword" placeholder="搜索户主姓名" @search="onSearch" />
      </div>

      <div class="table-section">
        <ExcelTable 
          :data="householdList" 
          :columns="householdColumns" 
          export-filename="全村户籍.xlsx"
        />
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'migrant'">
      <div class="action-bar">
        <van-button type="primary" icon="plus" @click="showAddMigrantModal = true">新增务工记录</van-button>
      </div>

      <div class="table-section">
        <ExcelTable 
          :data="migrantList" 
          :columns="migrantColumns" 
          export-filename="外出务工.xlsx"
        />
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'change'">
      <div class="table-section">
        <ExcelTable 
          :data="changeList" 
          :columns="changeColumns" 
          export-filename="户籍变动.xlsx"
        />
      </div>
    </div>

    <van-dialog v-model:show="showEditModal" title="户籍信息修改申请" @confirm="submitEdit">
      <van-form @submit="submitEdit">
        <van-field v-model="editForm.field" label="修改字段" placeholder="请选择修改字段" />
        <van-field v-model="editForm.newValue" label="新值" placeholder="请输入新值" />
        <van-field v-model="editForm.reason" label="修改原因" placeholder="请输入修改原因" />
      </van-form>
    </van-dialog>

    <van-dialog v-model:show="showAddModal" title="新增户籍档案" @confirm="submitAdd">
      <van-form>
        <van-field v-model="addForm.headName" label="户主姓名" placeholder="请输入户主姓名" />
        <van-field v-model="addForm.group" label="村组" placeholder="请输入村组" />
        <van-field v-model="addForm.memberCount" label="家庭人数" type="number" placeholder="请输入家庭人数" />
      </van-form>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast, showDialog } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const activeTab = ref('my')
const searchKeyword = ref('')
const showMigrant = ref(false)
const showEditModal = ref(false)
const showAddModal = ref(false)
const showAddMigrantModal = ref(false)

const myInfo = ref({
  headOfHousehold: '张建国',
  group: '第一村民组',
  address: '罗卜田乡第一村民组15号',
  phone: '138****8888',
  age: 45,
  insured: '已参保',
  education: '初中',
  employment: '在家务农'
})

const familyMembers = ref([
  { id: 1, name: '张建国', relation: '户主', idCard: '4103231979****1234' },
  { id: 2, name: '李秀英', relation: '配偶', idCard: '4103231980****5678' },
  { id: 3, name: '张伟', relation: '儿子', idCard: '4103232005****9012' },
  { id: 4, name: '张婷', relation: '女儿', idCard: '4103232008****3456' }
])

const myPhotos = ref([
  { id: 1, url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chinese%20household%20registration%20document%20official%20paper&image_size=portrait_4_3', name: '户口本首页' },
  { id: 2, url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chinese%20family%20registration%20page%20document&image_size=portrait_4_3', name: '家庭成员页' }
])

const householdList = ref([
  { id: 1, group: '第一村民组', headName: '张建国', memberCount: 4, special: true, specialType: '无特殊' },
  { id: 2, group: '第一村民组', headName: '李志强', memberCount: 3, special: true, specialType: '独居老人' },
  { id: 3, group: '第二村民组', headName: '王芳', memberCount: 5, special: true, specialType: '留守儿童' },
  { id: 4, group: '第二村民组', headName: '刘大伟', memberCount: 2, special: true, specialType: '残疾人' },
  { id: 5, group: '第三村民组', headName: '陈秀英', memberCount: 3, special: false }
])

const migrantList = ref([
  { id: 1, name: '张伟', group: '第一村民组', city: '深圳', phone: '139****1234', returnDate: '2024-01-15' },
  { id: 2, name: '李强', group: '第二村民组', city: '广州', phone: '137****5678', returnDate: '2024-01-20' },
  { id: 3, name: '王磊', group: '第一村民组', city: '上海', phone: '136****9012', returnDate: '2024-01-18' }
])

const changeList = ref([
  { id: 1, type: 'newborn', name: '张艺轩', idCard: '4103232024****0012', operator: '王书记', createTime: '2024-01-10', photos: [] },
  { id: 2, type: 'transfer', name: '李娜', idCard: '4103231995****3456', operator: '刘主任', createTime: '2024-01-08', photos: [] },
  { id: 3, type: 'split', name: '张建国', idCard: '4103231979****1234', operator: '王书记', createTime: '2024-01-05', photos: [] }
])

const groupColumns = [
  { text: '全部', value: '' },
  { text: '第一村民组', value: '第一村民组' },
  { text: '第二村民组', value: '第二村民组' },
  { text: '第三村民组', value: '第三村民组' },
  { text: '第四村民组', value: '第四村民组' }
]
const ageColumns = [
  { text: '全部', value: '' },
  { text: '0-18岁', value: 'child' },
  { text: '18-60岁', value: 'adult' },
  { text: '60岁以上', value: 'senior' }
]

const editForm = ref({ field: '', newValue: '', reason: '' })
const addForm = ref({ headName: '', group: '', memberCount: '' })

const householdColumns = [
  { key: 'group', title: '村组' },
  { key: 'headName', title: '户主姓名' },
  { key: 'memberCount', title: '家庭人数', formatter: (val) => `${val || 0}人` },
  { key: 'special', title: '特殊标记', formatter: (val) => val ? '是' : '否' },
  { key: 'specialType', title: '特殊类型', formatter: (val) => val || '无' }
]

const migrantColumns = [
  { key: 'name', title: '姓名' },
  { key: 'group', title: '村组' },
  { key: 'city', title: '务工城市' },
  { key: 'phone', title: '联系电话' },
  { key: 'returnDate', title: '预计返乡' }
]

const changeColumns = [
  { key: 'type', title: '变动类型', formatter: (val) => getChangeTypeLabel(val) },
  { key: 'name', title: '姓名' },
  { key: 'idCard', title: '身份证号' },
  { key: 'createTime', title: '变动时间' },
  { key: 'operator', title: '经办人' }
]

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'all') {
      const res = await request.get('/population/household', {
        params: { keyword: searchKeyword.value, group: '', age: '', migrant: showMigrant.value }
      })
      householdList.value = res.data || householdList.value
    } else if (activeTab.value === 'migrant') {
      const res = await request.get('/population/migrant')
      migrantList.value = res.data || migrantList.value
    } else if (activeTab.value === 'change') {
      const res = await request.get('/population/change')
      changeList.value = res.data || changeList.value
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const onGroupSelect = (value) => {
  showToast(`选择村组：${value}`)
}

const onAgeSelect = (value) => {
  showToast(`选择年龄段：${value}`)
}

const onSearch = () => {
  fetchData()
}

const exportExcel = () => {
  showToast('导出功能开发中')
}

const uploadPhoto = () => {
  showToast('上传照片功能开发中')
}

const previewPhoto = (photo) => {
  showDialog({
    title: photo.name,
    message: `<img src="${photo.url}" style="width:100%" />`,
    confirmButtonText: '知道了',
    showCancelButton: false
  })
}

const submitEdit = () => {
  showToast('修改申请已提交，等待审核')
  showEditModal.value = false
}

const submitAdd = () => {
  showToast('户籍档案已新增')
  showAddModal.value = false
}

const showHouseholdDetail = (item) => {
  showDialog({
    title: `${item.group} - ${item.headName}`,
    message: `家庭人数：${item.memberCount}人\n特殊标记：${item.specialType || '无'}`,
    confirmButtonText: '知道了',
    showCancelButton: false
  })
}

const isSelf = (item) => {
  return item.name === '张伟'
}

const updateMigrant = (item) => {
  showToast('更新务工信息功能开发中')
}

const getChangeTypeLabel = (type) => {
  const map = {
    'newborn': '新生儿上户',
    'transfer': '迁户',
    'split': '分户',
    'cancel': '销户'
  }
  return map[type] || type
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.population-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.table-section {
  margin: 10px 12px;
}

.card {
  background: #fff;
  margin: 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.header-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.info-item {
  display: flex;
  flex-direction: column;
  padding: 10px;
  background: #fafafa;
  border-radius: 10px;
}

.info-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.info-value {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.member-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.member-card {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #fafafa;
  border-radius: 10px;
}

.member-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #D22630, #B01A26);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: bold;
  margin-right: 12px;
}

.member-info {
  flex: 1;
}

.member-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.member-relation {
  font-size: 12px;
  color: #D22630;
  margin: 4px 0;
}

.member-detail {
  font-size: 12px;
  color: #999;
}

.photo-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.photo-item {
  aspect-ratio: 3/4;
  border-radius: 10px;
  overflow: hidden;
}

.photo-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.photo-upload {
  aspect-ratio: 3/4;
  border-radius: 10px;
  border: 2px dashed #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: #999;
}

.filter-bar {
  display: flex;
  gap: 10px;
  padding: 12px;
  background: #fff;
  margin-bottom: 12px;
}

.filter-bar .van-picker {
  flex: 1;
}

.action-bar {
  display: flex;
  gap: 10px;
  padding: 0 12px;
  margin-bottom: 12px;
}

.action-bar .van-button {
  flex: 1;
}

.search-bar {
  padding: 0 12px;
  margin-bottom: 12px;
}

.card-list {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.household-card, .migrant-card, .change-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.household-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.household-group {
  font-size: 12px;
  color: #D22630;
  background: rgba(210, 38, 48, 0.1);
  padding: 4px 10px;
  border-radius: 10px;
}

.special-tag {
  font-size: 12px;
  color: #FFD700;
  background: rgba(255, 215, 0, 0.2);
  padding: 4px 10px;
  border-radius: 10px;
}

.household-head {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.household-count {
  font-size: 13px;
  color: #666;
}

.household-special {
  font-size: 12px;
  color: #F44336;
  margin-top: 4px;
}

.migrant-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.migrant-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #FF9800, #F57C00);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  margin-right: 12px;
}

.migrant-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.migrant-group {
  font-size: 12px;
  color: #999;
}

.migrant-detail {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 13px;
  border-bottom: 1px solid #eee;
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-row span:first-child {
  color: #999;
}

.detail-row span:last-child {
  color: #333;
}

.change-type {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.change-type.newborn {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.change-type.transfer {
  background: rgba(33, 150, 243, 0.2);
  color: #2196F3;
}

.change-type.split {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.change-type.cancel {
  background: rgba(244, 67, 54, 0.2);
  color: #F44336;
}

.change-time {
  font-size: 12px;
  color: #999;
}

.change-name {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin: 8px 0 4px;
}

.change-id {
  font-size: 13px;
  color: #666;
}

.change-operator {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.change-photos {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}

.change-photo {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  object-fit: cover;
}
</style>
