<template>
  <div class="warehouse-page page-container">
    <van-nav-bar title="物资仓库" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="库存台账" name="stock" />
      <van-tab title="物资入库" name="inbound" />
      <van-tab title="领用归还" name="borrow" />
      <van-tab title="月度盘点" name="inventory" />
      <van-tab title="报废物资" name="scrap" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'stock'">
      <div class="search-bar">
        <van-search v-model="searchKeyword" placeholder="搜索物资名称" @search="onSearch" />
      </div>

      <div class="filter-bar">
        <van-picker :columns="categoryColumns" @confirm="onCategorySelect" placeholder="物资分类" />
      </div>

      <div class="table-section">
        <ExcelTable 
          :data="stockList" 
          :columns="stockColumns" 
          export-filename="库存台账.xlsx"
        />
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'inbound'">
      <div>
        <div class="card">
          <div class="card-header">
            <div class="header-title">📦 新增入库</div>
          </div>
          <van-form @submit="submitInbound">
            <van-field v-model="inboundForm.name" label="物资名称" placeholder="请输入物资名称" />
            <van-field v-model="inboundForm.spec" label="规格型号" placeholder="请输入规格型号" />
            <van-field v-model="inboundForm.quantity" label="入库数量" type="number" placeholder="请输入数量" />
            <van-field v-model="inboundForm.unit" label="计量单位" placeholder="如：件、个、箱" />
            <van-field v-model="inboundForm.source" label="来源渠道" placeholder="请输入来源渠道" />
            <van-field v-model="inboundForm.location" label="存放位置" placeholder="请输入存放位置" />
            <div class="upload-section">
              <div class="upload-label">物资照片</div>
              <div class="upload-grid">
                <div v-for="(photo, idx) in inboundForm.photos" :key="idx" class="upload-item">
                  <img :src="photo" />
                </div>
                <div class="upload-add" @click="uploadPhoto">+</div>
              </div>
            </div>
            <van-button type="primary" block native-type="submit">确认入库</van-button>
          </van-form>
        </div>

        <div class="table-section">
          <div class="table-title">📋 入库记录</div>
          <ExcelTable 
            :data="inboundRecords" 
            :columns="inboundColumns" 
            export-filename="入库记录.xlsx"
          />
        </div>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'borrow'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">📝 申领物资</div>
        </div>
        <van-form @submit="submitBorrow">
          <van-field v-model="borrowForm.name" label="物资名称" placeholder="请输入物资名称" />
          <van-field v-model="borrowForm.quantity" label="申领数量" type="number" placeholder="请输入数量" />
          <van-field v-model="borrowForm.purpose" label="领用用途" placeholder="请输入领用用途" />
          <van-button type="primary" block native-type="submit">提交申领</van-button>
        </van-form>
      </div>

      <div class="table-section">
        <div class="table-title">📋 我的领用记录</div>
        <ExcelTable 
          :data="borrowRecords" 
          :columns="borrowColumns" 
          export-filename="领用记录.xlsx"
        />
      </div>

      <div class="table-section">
        <div class="table-title">📋 待归还记录</div>
        <ExcelTable 
          :data="pendingReturn" 
          :columns="returnColumns" 
          export-filename="待归还记录.xlsx"
        />
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'inventory'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">📊 本月盘点</div>
        </div>
        <van-form @submit="submitInventory">
          <van-field v-model="inventoryForm.month" label="盘点月份" placeholder="如：2024-01" />
          <van-field v-model="inventoryForm.note" label="盘点备注" type="textarea" placeholder="如有盘盈盘亏请说明" />
          <van-button type="primary" block native-type="submit">提交盘点</van-button>
        </van-form>
      </div>

      <div class="table-section">
        <div class="table-title">📋 盘点记录</div>
        <ExcelTable 
          :data="inventoryRecords" 
          :columns="inventoryColumns" 
          export-filename="盘点记录.xlsx"
        />
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'scrap'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">🗑️ 登记报废</div>
        </div>
        <van-form @submit="submitScrap">
          <van-field v-model="scrapForm.name" label="物资名称" placeholder="请输入物资名称" />
          <van-field v-model="scrapForm.quantity" label="报废数量" type="number" placeholder="请输入数量" />
          <van-field v-model="scrapForm.reason" label="报废原因" placeholder="如：损坏、过期" />
          <van-button type="primary" block native-type="submit">提交报废</van-button>
        </van-form>
      </div>

      <div class="table-section">
        <div class="table-title">📋 报废记录</div>
        <ExcelTable 
          :data="scrapRecords" 
          :columns="scrapColumns" 
          export-filename="报废记录.xlsx"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const activeTab = ref('stock')
const isOfficial = true
const searchKeyword = ref('')

const categoryColumns = [
  { text: '全部', value: '' },
  { text: '防汛物资', value: '防汛物资' },
  { text: '保洁工具', value: '保洁工具' },
  { text: '办公用品', value: '办公用品' },
  { text: '应急物资', value: '应急物资' }
]

const stockColumns = [
  { key: 'name', title: '物资名称' },
  { key: 'spec', title: '规格型号' },
  { key: 'category', title: '物资分类' },
  { key: 'location', title: '存放位置' },
  { key: 'quantity', title: '库存数量' },
  { key: 'unit', title: '计量单位' },
  { key: 'threshold', title: '库存阈值' }
]

const inboundColumns = [
  { key: 'name', title: '物资名称' },
  { key: 'quantity', title: '入库数量' },
  { key: 'unit', title: '计量单位' },
  { key: 'date', title: '入库日期' },
  { key: 'operator', title: '操作人' }
]

const borrowColumns = [
  { key: 'name', title: '物资名称' },
  { key: 'quantity', title: '领用数量' },
  { key: 'unit', title: '计量单位' },
  { key: 'purpose', title: '领用用途' },
  { key: 'status', title: '状态' }
]

const returnColumns = [
  { key: 'name', title: '物资名称' },
  { key: 'borrower', title: '领用人' },
  { key: 'quantity', title: '数量' },
  { key: 'unit', title: '单位' },
  { key: 'borrowDate', title: '领用日期' }
]

const inventoryColumns = [
  { key: 'month', title: '盘点月份' },
  { key: 'status', title: '状态' },
  { key: 'date', title: '盘点日期' },
  { key: 'profitLoss', title: '盘盈盘亏', formatter: (val) => val || '无' }
]

const scrapColumns = [
  { key: 'name', title: '物资名称' },
  { key: 'quantity', title: '报废数量' },
  { key: 'unit', title: '计量单位' },
  { key: 'reason', title: '报废原因' },
  { key: 'status', title: '状态' }
]

const stockList = ref([
  { id: 1, name: '铁锹', spec: '大号', quantity: 25, unit: '把', category: '防汛物资', location: '仓库A区', threshold: 10 },
  { id: 2, name: '雨衣', spec: '加厚款', quantity: 18, unit: '件', category: '防汛物资', location: '仓库A区', threshold: 15 },
  { id: 3, name: '扫帚', spec: '竹制', quantity: 50, unit: '把', category: '保洁工具', location: '仓库B区', threshold: 20 },
  { id: 4, name: '垃圾桶', spec: '240L', quantity: 8, unit: '个', category: '保洁工具', location: '仓库B区', threshold: 5 },
  { id: 5, name: '打印纸', spec: 'A4', quantity: 3, unit: '箱', category: '办公用品', location: '办公室', threshold: 5 },
  { id: 6, name: '手电筒', spec: '充电式', quantity: 12, unit: '个', category: '应急物资', location: '仓库A区', threshold: 8 }
])

const inboundForm = ref({ name: '', spec: '', quantity: '', unit: '', source: '', location: '', photos: [] })

const inboundRecords = ref([
  { id: 1, name: '铁锹', quantity: 10, unit: '把', date: '2024-01-15', operator: '李主任' },
  { id: 2, name: '雨衣', quantity: 5, unit: '件', date: '2024-01-10', operator: '王书记' }
])

const borrowForm = ref({ name: '', quantity: '', purpose: '' })

const borrowRecords = ref([
  { id: 1, name: '铁锹', quantity: 2, unit: '把', purpose: '道路维修', status: '已归还' },
  { id: 2, name: '扫帚', quantity: 5, unit: '把', purpose: '环境整治', status: '使用中' }
])

const pendingReturn = ref([
  { id: 1, name: '手电筒', borrower: '张三', quantity: 1, unit: '个', borrowDate: '2024-01-05' },
  { id: 2, name: '雨衣', borrower: '李四', quantity: 2, unit: '件', borrowDate: '2024-01-08' }
])

const inventoryForm = ref({ month: '', note: '' })

const inventoryRecords = ref([
  { id: 1, month: '2024-01', status: '已完成', date: '2024-01-31', profitLoss: '' },
  { id: 2, month: '2023-12', status: '已完成', date: '2023-12-31', profitLoss: '盘盈：扫帚2把' },
  { id: 3, month: '2023-11', status: '已完成', date: '2023-11-30', profitLoss: '盘亏：铁锹1把' }
])

const scrapForm = ref({ name: '', quantity: '', reason: '' })

const scrapRecords = ref([
  { id: 1, name: '旧扫帚', quantity: 10, unit: '把', reason: '磨损严重', status: '已审核' },
  { id: 2, name: '过期药品', quantity: 5, unit: '盒', reason: '保质期已过', status: '待审核' }
])

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'stock') {
      const res = await request.get('/warehouse/stock', { params: { keyword: searchKeyword.value } })
      stockList.value = res.data || stockList.value
    } else if (activeTab.value === 'inbound') {
      const res = await request.get('/warehouse/inbound')
      inboundRecords.value = res.data || inboundRecords.value
    } else if (activeTab.value === 'borrow') {
      const res = await request.get('/warehouse/borrow')
      borrowRecords.value = res.borrowRecords || res.data?.borrowRecords || borrowRecords.value
      pendingReturn.value = res.pendingReturn || res.data?.pendingReturn || pendingReturn.value
    } else if (activeTab.value === 'inventory') {
      const res = await request.get('/warehouse/inventory')
      inventoryRecords.value = res.data || inventoryRecords.value
    } else if (activeTab.value === 'scrap') {
      const res = await request.get('/warehouse/scrap')
      scrapRecords.value = res.data || scrapRecords.value
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const onSearch = () => {
  fetchData()
}

const onCategorySelect = (value) => {
  showToast(`选择分类：${value}`)
}

const uploadPhoto = () => {
  showToast('上传照片功能开发中')
}

const submitInbound = () => {
  showToast('入库成功')
  inboundForm.value = { name: '', spec: '', quantity: '', unit: '', source: '', location: '', photos: [] }
}

const submitBorrow = () => {
  showToast('申领已提交')
  borrowForm.value = { name: '', quantity: '', purpose: '' }
}

const confirmReturn = (item) => {
  showToast(`${item.borrower}的${item.name}已归还`)
}

const submitInventory = () => {
  showToast('盘点已提交')
  inventoryForm.value = { month: '', note: '' }
}

const submitScrap = () => {
  showToast('报废申请已提交，等待审核')
  scrapForm.value = { name: '', quantity: '', reason: '' }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.warehouse-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.table-section {
  margin: 10px 12px;
}

.table-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 12px;
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
  margin-bottom: 14px;
}

.header-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
}

.search-bar {
  padding: 0 12px;
  margin-bottom: 12px;
}

.filter-bar {
  padding: 0 12px;
  margin-bottom: 12px;
}

.filter-bar .van-picker {
  width: 100%;
}

.card-list {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.stock-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.stock-card.warning {
  border-left: 4px solid #F44336;
}

.stock-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.stock-category {
  font-size: 12px;
  color: #D22630;
  background: rgba(210, 38, 48, 0.1);
  padding: 4px 10px;
  border-radius: 10px;
}

.stock-location {
  font-size: 12px;
  color: #999;
}

.stock-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.stock-spec {
  font-size: 13px;
  color: #666;
}

.stock-quantity {
  display: flex;
  align-items: baseline;
  margin-top: 12px;
}

.quantity-value {
  font-size: 24px;
  font-weight: bold;
  color: #D22630;
}

.quantity-unit {
  font-size: 14px;
  color: #666;
  margin-left: 4px;
}

.stock-warning {
  font-size: 12px;
  color: #F44336;
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px dashed #eee;
}

.no-permission-card {
  margin: 40px 12px;
  padding: 40px 20px;
  background: #fff;
  border-radius: 14px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.no-permission-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.no-permission-text {
  font-size: 14px;
  color: #999;
}

.upload-section {
  margin: 16px 0;
}

.upload-label {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
}

.upload-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.upload-item {
  aspect-ratio: 1;
  border-radius: 10px;
  overflow: hidden;
}

.upload-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-add {
  aspect-ratio: 1;
  border-radius: 10px;
  border: 2px dashed #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #999;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #fafafa;
  border-radius: 10px;
}

.item-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.item-detail {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.item-operator {
  font-size: 12px;
  color: #666;
}

.item-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.item-status.已归还 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.item-status.使用中 {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.item-status.已审核 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}

.item-status.待审核 {
  background: rgba(255, 215, 0, 0.2);
  color: #DAA520;
}

.item-profit {
  font-size: 12px;
  color: #F44336;
}
</style>