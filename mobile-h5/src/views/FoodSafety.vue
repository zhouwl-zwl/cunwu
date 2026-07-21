<template>
  <div class="food-safety-page page-container">
    <van-nav-bar title="食品安全" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="日常检查" name="日常检查" />
      <van-tab title="隐患整改" name="隐患整改" />
      <van-tab title="宣传记录" name="宣传记录" />
    </van-tabs>

    <div class="action-bar">
      <van-button type="primary" icon="plus" @click="handleAdd">新增记录</van-button>
    </div>

    <div class="table-section">
      <ExcelTable 
        :data="list" 
        :columns="columns" 
        export-filename="食品安全记录.xlsx"
      />
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无食品记录" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { foodSafetyData } from '../data/mockData'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const getStatusName = (status) => {
  const map = { QUALIFIED: '合格', UNQUALIFIED: '不合格', PENDING: '待整改', DONE: '已整改' }
  return map[status] || '未知'
}

const columns = [
  { key: 'type', title: '类型' },
  { key: 'title', title: '标题' },
  { key: 'target', title: '检查对象', formatter: (val) => val || '-' },
  { key: 'targetType', title: '对象类型', formatter: (val) => val || '-' },
  { key: 'inspector', title: '检查人', formatter: (val) => val || '-' },
  { key: 'status', title: '状态', formatter: (val) => getStatusName(val) },
  { key: 'inspectDate', title: '检查时间', formatter: (val) => formatTime(val) }
]

const handleItemClick = (item) => {
  showToast(`查看：${item.title}`)
}

const handleAdd = () => {
  showToast('功能开发中，敬请期待')
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/food-safety/list', {
      params: { page: 1, size: 20, type: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : foodSafetyData.records
  } catch (error) {
    list.value = foodSafetyData.records
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.food-safety-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.table-section {
  margin: 10px 12px;
}
</style>