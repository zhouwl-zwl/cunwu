
<template>
  <div class="forest-fire-page page-container">
    <van-nav-bar title="森林防火" left-arrow @click-left="onBack" />

    <div class="warning-banner">
      <div class="warning-icon">🔥</div>
      <div class="warning-text">
        <div class="warning-title">防火等级：{{ fireLevel.level || '三级' }}</div>
        <div class="warning-desc">{{ fireLevel.suggestion || '加强巡山，注意防火' }}</div>
      </div>
    </div>

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="巡山记录" name="巡山记录" />
      <van-tab title="火情上报" name="火情上报" />
      <van-tab title="宣传记录" name="宣传记录" />
    </van-tabs>

    <div class="table-section">
      <ExcelTable 
        :data="list" 
        :columns="columns" 
        export-filename="森林防火记录.xlsx"
      />
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无巡山记录" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { forestFireData } from '../data/mockData'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeType = ref('')
const fireLevel = reactive({ level: '', suggestion: '' })

const onBack = () => { goBack(router) }

const onTypeChange = () => {
  fetchData()
}

const getStatusName = (status) => {
  const map = { NORMAL: '正常', ABNORMAL: '异常', PENDING: '待处理', DONE: '已处理' }
  return map[status] || '未知'
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const columns = [
  { key: 'type', title: '类型', formatter: (val) => val || '巡山记录' },
  { key: 'title', title: '标题' },
  { key: 'area', title: '巡查区域', formatter: (val) => val || '-' },
  { key: 'inspector', title: '巡查人', formatter: (val) => val || '-' },
  { key: 'inspectDate', title: '巡查时间', formatter: (val) => formatTime(val) },
  { key: 'status', title: '状态', formatter: (val) => getStatusName(val) }
]

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/forest-fire/list', {
      params: { page: 1, size: 20, type: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : forestFireData.records
  } catch (error) {
    list.value = forestFireData.records
  } finally {
    loading.value = false
  }
}

const fetchFireLevel = async () => {
  try {
    const res = await request.get('/forest-fire/level')
    Object.assign(fireLevel, res.data)
  } catch (error) {
    Object.assign(fireLevel, forestFireData.level)
  }
}

onMounted(() => {
  fetchFireLevel()
  fetchData()
})
</script>

<style scoped>
.forest-fire-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.warning-banner {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #FFA500 0%, #D22630 100%);
  margin: 10px 12px;
  border-radius: 14px;
  padding: 14px;
  color: #fff;
}

.warning-icon {
  font-size: 36px;
  margin-right: 12px;
}

.warning-text {
  flex: 1;
}

.warning-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 4px;
}

.warning-desc {
  font-size: 12px;
  opacity: 0.9;
}

.table-section {
  margin: 10px 12px;
}
</style>
