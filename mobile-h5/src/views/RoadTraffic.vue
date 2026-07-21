
<template>
  <div class="road-traffic-page page-container">
    <van-nav-bar title="道路交通" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="安全劝导" name="安全劝导" />
      <van-tab title="道路养护" name="道路养护" />
      <van-tab title="隐患排查" name="隐患排查" />
    </van-tabs>

    <div class="action-bar">
      <van-button type="primary" icon="plus" @click="handleAdd">新增记录</van-button>
    </div>

    <div class="table-section">
      <ExcelTable 
        :data="list" 
        :columns="columns" 
        export-filename="道路交通记录.xlsx"
      />
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无道路记录" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { roadTrafficData } from '../data/mockData'
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
  const map = { NORMAL: '正常', ABNORMAL: '异常', PENDING: '待处理', DONE: '已处理' }
  return map[status] || '未知'
}

const columns = [
  { key: 'type', title: '类型', formatter: (val) => val || '安全劝导' },
  { key: 'title', title: '标题' },
  { key: 'roadSection', title: '所在路段', formatter: (val) => val || '—' },
  { key: 'person', title: '负责人', formatter: (val) => val || '—' },
  { key: 'recordDate', title: '记录时间', formatter: (val) => formatTime(val) },
  { key: 'status', title: '状态', formatter: (val) => getStatusName(val) }
]

const handleAdd = () => {
  showToast('功能开发中，敬请期待')
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/road-traffic/list', {
      params: { page: 1, size: 20, type: activeType.value || undefined }
    })
    const data = res.records || res.data?.records || res || []
    list.value = data.length > 0 ? data : roadTrafficData.records
  } catch (error) {
    list.value = roadTrafficData.records
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.road-traffic-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.action-bar {
  display: flex;
  justify-content: flex-end;
  padding: 10px 12px;
}

.table-section {
  margin: 10px 12px;
}
</style>
