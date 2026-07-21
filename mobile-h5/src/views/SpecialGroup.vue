
<template>
  <div class="special-group-page page-container">
    <van-nav-bar title="特殊群体台账" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="留守儿童" name="留守儿童" />
      <van-tab title="独居老人" name="独居老人" />
      <van-tab title="残疾人" name="残疾人" />
      <van-tab title="低保户" name="低保户" />
    </van-tabs>

    <div class="table-section">
      <ExcelTable 
        :data="tableData" 
        :columns="tableColumns" 
        export-filename="特殊群体台账.xlsx"
      />
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无台账信息" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
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

const tableColumns = [
  { key: 'name', title: '姓名' },
  { key: 'groupType', title: '群体类型', formatter: (val) => val || '特殊群体' },
  { key: 'gender', title: '性别', formatter: (val) => val === '男' ? '男' : '女' },
  { key: 'age', title: '年龄', formatter: (val) => `${val || '-'}岁` },
  { key: 'phone', title: '联系电话', formatter: (val) => val || '—' },
  { key: 'address', title: '家庭住址', formatter: (val) => val || '—' },
  { key: 'guardian', title: '监护人/联系人', formatter: (val) => val || '—' },
  { key: 'lastVisitDate', title: '上次走访', formatter: (val) => formatTime(val) },
  { key: 'visitRemark', title: '走访备注', formatter: (val) => val || '—' }
]

const tableData = computed(() => list.value)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/special-group/list', {
      params: { page: 1, size: 20, groupType: activeType.value || undefined }
    })
    list.value = res.records || res.data?.records || res || []
  } catch (error) {
    console.error('获取特殊群体台账失败', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.special-group-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.table-section {
  margin: 10px 12px;
}
</style>
