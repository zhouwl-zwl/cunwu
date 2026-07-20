
<template>
  <div class="public-notices-page page-container">
    <van-nav-bar title="三务公开" left-arrow @click-left="handleBack" />
    
    <van-tabs v-model:active="activeType" @change="onTypeChange">
      <van-tab title="全部" name="" />
      <van-tab title="党务公开" name="党务公开" />
      <van-tab title="村务公开" name="村务公开" />
      <van-tab title="财务公开" name="财务公开" />
    </van-tabs>

    <van-cell-group>
      <van-cell 
        v-for="notice in notices" 
        :key="notice.id" 
        clickable 
        @click="goDetail(notice.id)"
      >
        <template #title>
          <div class="notice-title">{{ notice.title }}</div>
          <div class="notice-content text-ellipsis-2">{{ notice.content }}</div>
        </template>
        <template #label>
          <van-tag :type="getTagType(notice.noticeType)" size="small">{{ notice.noticeType }}</van-tag>
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()

const activeType = ref('')
const notices = ref([])
const currentPage = ref(1)
const total = ref(0)

const handleBack = () => {
  goBack(router)
}

const goDetail = (id) => {
  router.push(`/public-notice-detail/${id}`)
}

const getTagType = (type) => {
  if (type === '党务公开') return 'danger'
  if (type === '村务公开') return 'primary'
  if (type === '财务公开') return 'warning'
  return 'default'
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const onTypeChange = () => {
  currentPage.value = 1
  fetchNotices()
}

const onPageChange = () => {
  fetchNotices()
}

const fetchNotices = async () => {
  try {
    const res = await request.get('/public/public-notices', {
      params: {
        page: currentPage.value,
        noticeType: activeType.value || undefined
      }
    })
    notices.value = res.records || res.data?.records || []
    total.value = res.total || res.data?.total || 0
  } catch (error) {
    console.error('获取公开信息失败', error)
    notices.value = [
      { id: 1, title: '2024年第一季度党费收缴通知', content: '根据上级党委要求，现将2024年第一季度党费收缴工作通知如下，请各党员按时足额缴纳党费。', noticeType: '党务公开', createTime: '2024-01-15' },
      { id: 2, title: '关于开展2024年度党员主题党日活动的通知', content: '为深入学习贯彻党的二十大精神，进一步增强党组织凝聚力，决定开展党员主题党日活动。', noticeType: '党务公开', createTime: '2024-02-20' },
      { id: 3, title: '罗卜田乡土地流转公示', content: '现将我村2024年度土地流转情况公示如下，公示期7天，请村民监督。', noticeType: '村务公开', createTime: '2024-03-10' },
      { id: 4, title: '罗卜田乡2024年集体资产经营情况报告', content: '2024年度我村集体资产经营情况良好，总收入达到1200万元。', noticeType: '财务公开', createTime: '2024-04-05' },
      { id: 5, title: '关于村道硬化工程招标公告', content: '罗卜田乡拟对村主干道进行硬化改造，现面向社会公开招标。', noticeType: '村务公开', createTime: '2024-05-18' },
      { id: 6, title: '罗卜田乡2024年第二季度财务收支情况', content: '第二季度总收入280万元，总支出195万元，结余85万元。', noticeType: '财务公开', createTime: '2024-07-01' }
    ]
    total.value = 6
  }
}

onMounted(() => {
  fetchNotices()
})
</script>

<style scoped>
.notice-title {
  font-size: 15px;
  font-weight: bold;
  color: #323233;
  margin-bottom: 4px;
}

.notice-content {
  font-size: 13px;
  color: #969799;
}

.notice-time {
  font-size: 12px;
  color: #969799;
  margin-left: 8px;
}
</style>
