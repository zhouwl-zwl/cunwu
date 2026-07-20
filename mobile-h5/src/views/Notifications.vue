
<template>
  <div class="notifications-page page-container">
    <van-nav-bar title="通知公告" left-arrow @click-left="handleBack" />
    
    <van-search v-model="searchText" placeholder="搜索通知" @search="onSearch" />
    
    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="信息审核通知" name="audit" />
      <van-tab title="紧急通知" name="紧急通知" />
      <van-tab title="党建通知" name="党建通知" />
      <van-tab title="民生通知" name="民生通知" />
      <van-tab title="产业通知" name="产业通知" />
    </van-tabs>

    <van-cell-group>
      <van-cell 
        v-for="notice in notifications" 
        :key="notice.id" 
        clickable 
        @click="handleNoticeClick(notice)"
      >
        <template #icon>
          <van-tag v-if="notice.isTop === 1" type="danger">置顶</van-tag>
          <van-tag v-else-if="notice.type === 'audit'" type="warning" size="small">审核</van-tag>
        </template>
        <template #title>
          <div :class="['notice-title', { 'audit-title': notice.type === 'audit' }]">{{ notice.title }}</div>
          <div class="notice-content text-ellipsis-2">{{ notice.content }}</div>
        </template>
        <template #label>
          <span :class="['notice-type', { 'audit-type': notice.type === 'audit' }]">{{ getTypeLabel(notice.type) }}</span>
          <span class="notice-time">{{ formatTime(notice.createTime) }}</span>
        </template>
      </van-cell>
    </van-cell-group>

    <van-pagination 
      v-model:current-page="currentPage" 
      :total-items="total" 
      :page-size="10"
      @change="onPageChange"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onActivated } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const userStore = useUserStore()

const searchText = ref('')
const activeType = ref('')
const notifications = ref([])
const currentPage = ref(1)
const total = ref(0)

const handleBack = () => {
  goBack(router)
}

const getTypeLabel = (type) => {
  const map = {
    'audit': '信息审核',
    '紧急通知': '紧急通知',
    '党建通知': '党建通知',
    '民生通知': '民生通知',
    '产业通知': '产业通知',
    '政策通知': '政策通知',
    '村务通知': '村务通知'
  }
  return map[type] || type || '通知'
}

const handleNoticeClick = (notice) => {
  if (notice.type === 'audit') {
    router.push('/profile')
  } else {
    router.push(`/notification-detail/${notice.id}`)
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getMonth() + 1}月${date.getDate()}日`
}

const onSearch = () => {
  currentPage.value = 1
  fetchNotifications()
}

const onTypeChange = () => {
  currentPage.value = 1
  fetchNotifications()
}

const onPageChange = () => {
  fetchNotifications()
}

const fetchNotifications = async () => {
  try {
    const userId = userStore.userInfo?.id
    const res = await request.get('/notification/user-list', {
      params: {
        userId: userId,
        page: currentPage.value,
        type: activeType.value || undefined
      }
    })
    notifications.value = res.records || res.data?.records || []
    total.value = res.total || res.data?.total || 0
  } catch (error) {
    console.error('获取通知失败', error)
  }
}

onMounted(() => {
  fetchNotifications()
})

onActivated(() => {
  fetchNotifications()
})
</script>

<style scoped>
.notice-title {
  font-size: 15px;
  font-weight: bold;
  color: #323233;
  margin-bottom: 4px;
}

.notice-title.audit-title {
  color: #C8102E;
}

.notice-content {
  font-size: 13px;
  color: #969799;
}

.notice-type {
  font-size: 12px;
  color: #1989fa;
  margin-right: 8px;
}

.notice-type.audit-type {
  color: #C8102E;
  font-weight: bold;
}

.notice-time {
  font-size: 12px;
  color: #969799;
}
</style>
