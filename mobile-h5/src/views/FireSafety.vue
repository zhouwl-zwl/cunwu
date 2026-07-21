
<template>
  <div class="fire-safety-page page-container">
    <van-nav-bar title="消防安全" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="巡查记录" name="巡查记录" />
      <van-tab title="隐患整改" name="隐患整改" />
      <van-tab title="宣传培训" name="宣传培训" />
    </van-tabs>

    <div class="table-section">
      <ExcelTable 
        :data="tableData" 
        :columns="tableColumns" 
        export-filename="消防安全记录.xlsx"
      />
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无消防记录" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { fireSafetyData } from '../data/mockData'
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

const getStatusName = (status) => {
  const map = { NORMAL: '正常', ABNORMAL: '异常', PENDING: '待整改', DONE: '已整改' }
  return map[status] || '未知'
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const tableColumns = [
  { key: 'type', title: '类型', formatter: (val) => val || '巡查记录' },
  { key: 'title', title: '标题' },
  { key: 'location', title: '检查地点', formatter: (val) => val || '-' },
  { key: 'inspector', title: '检查人', formatter: (val) => val || '-' },
  { key: 'inspectDate', title: '检查时间', formatter: (val, row) => formatTime(val || row.createTime) },
  { key: 'status', title: '状态', formatter: (val) => getStatusName(val) }
]

const tableData = computed(() => list.value)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/fire-safety/list', {
      params: { page: 1, size: 20, type: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : fireSafetyData.records
  } catch (error) {
    list.value = fireSafetyData.records
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.fire-safety-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.table-section {
  margin: 10px 12px;
}
</style>
