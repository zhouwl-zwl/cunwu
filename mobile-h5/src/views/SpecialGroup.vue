
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

    <div class="card" v-for="item in list" :key="item.id">
      <div class="card-header">
        <van-tag type="danger" size="medium">{{ item.groupType || '特殊群体' }}</van-tag>
        <van-tag :type="getVisitTagType(item.lastVisitDate)" plain size="medium">
          {{ getVisitStatus(item.lastVisitDate) }}
        </van-tag>
      </div>
      <div class="member-row">
        <div class="member-avatar">
          <span>{{ item.name ? item.name.charAt(0) : '·' }}</span>
        </div>
        <div class="member-info">
          <div class="member-name">{{ item.name }}</div>
          <div class="member-meta">{{ item.gender === '男' ? '男' : '女' }} · {{ item.age || '—' }}岁</div>
        </div>
      </div>
      <div class="info-row">
        <span class="label">联系电话</span>
        <span class="value">{{ item.phone || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">家庭住址</span>
        <span class="value">{{ item.address || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">监护人/联系人</span>
        <span class="value">{{ item.guardian || '—' }}</span>
      </div>
      <div class="info-row">
        <span class="label">上次走访</span>
        <span class="value">{{ formatTime(item.lastVisitDate) }}</span>
      </div>
      <div class="info-row" v-if="item.visitRemark">
        <span class="label">走访备注</span>
        <span class="value">{{ item.visitRemark }}</span>
      </div>
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无台账信息" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'

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

const getVisitStatus = (lastVisitDate) => {
  if (!lastVisitDate) return '未走访'
  const days = Math.floor((Date.now() - new Date(lastVisitDate).getTime()) / (1000 * 60 * 60 * 24))
  if (days > 30) return `已${days}天未走访`
  return `近期已走访`
}

const getVisitTagType = (lastVisitDate) => {
  if (!lastVisitDate) return 'danger'
  const days = Math.floor((Date.now() - new Date(lastVisitDate).getTime()) / (1000 * 60 * 60 * 24))
  if (days > 30) return 'danger'
  if (days > 15) return 'warning'
  return 'success'
}

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

.card {
  background: #fff;
  margin: 10px 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.member-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
}

.member-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  color: #FFD700;
  margin-right: 12px;
  flex-shrink: 0;
}

.member-info {
  flex: 1;
}

.member-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.member-meta {
  font-size: 12px;
  color: #969799;
  margin-top: 2px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 4px 0;
}

.label {
  color: #969799;
  font-size: 13px;
}

.value {
  color: #333;
  font-size: 13px;
  font-weight: 500;
  text-align: right;
  max-width: 60%;
}
</style>
