
<template>
  <div class="party-building-page page-container">
    <van-nav-bar title="党建工作" left-arrow @click-left="handleBack" />
    
    <van-cell-group>
      <van-cell v-for="item in partyBuildings" :key="item.id" clickable>
        <template #title>{{ item.title }}</template>
        <template #label>
          <span>{{ item.type }}</span>
          <span>{{ formatTime(item.createTime) }}</span>
        </template>
      </van-cell>
    </van-cell-group>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const partyBuildings = ref([])

const handleBack = () => {
  goBack(router)
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

const fetchData = async () => {
  try {
    const res = await request.get('/party-building/list')
    partyBuildings.value = res.records || res.data?.records || []
  } catch (error) {
    console.error('获取党建数据失败', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
</style>
