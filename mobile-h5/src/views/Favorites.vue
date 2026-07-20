<template>
  <div class="favorites-page page-container">
    <van-nav-bar title="我的收藏" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeTab" @change="onTabChange" class="tabs">
      <van-tab title="公示收藏" name="notice">
        <van-pull-refresh v-model="isRefreshing" @refresh="onRefresh">
          <div v-if="noticeList.length === 0 && !loading" class="empty-state">
            <div class="empty-icon">📋</div>
            <div class="empty-text">暂无公示收藏</div>
            <div class="empty-tip">去看看通知公告吧</div>
          </div>
          <van-list v-else v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
            <div class="card" v-for="item in noticeList" :key="item.id" @click="goDetail('notice', item.id)">
              <div class="card-header">
                <van-tag type="danger" size="mini">{{ item.type }}</van-tag>
                <span class="card-time">{{ formatTime(item.createTime) }}</span>
              </div>
              <div class="card-title">{{ item.title }}</div>
              <div class="card-footer">
                <van-button size="mini" type="primary" plain @click.stop="cancelFavorite('notice', item.id)">取消收藏</van-button>
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
      </van-tab>
      <van-tab title="政策收藏" name="policy">
        <van-pull-refresh v-model="isRefreshing" @refresh="onRefresh">
          <div v-if="policyList.length === 0 && !loading" class="empty-state">
            <div class="empty-icon">📜</div>
            <div class="empty-text">暂无政策收藏</div>
            <div class="empty-tip">去看看最新政策吧</div>
          </div>
          <van-list v-else v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
            <div class="card" v-for="item in policyList" :key="item.id" @click="goDetail('policy', item.id)">
              <div class="card-header">
                <van-tag type="primary" size="mini">{{ item.type }}</van-tag>
                <span class="card-time">{{ formatTime(item.createTime) }}</span>
              </div>
              <div class="card-title">{{ item.title }}</div>
              <div class="card-footer">
                <van-button size="mini" type="primary" plain @click.stop="cancelFavorite('policy', item.id)">取消收藏</van-button>
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
      </van-tab>
      <van-tab title="文件收藏" name="document">
        <van-pull-refresh v-model="isRefreshing" @refresh="onRefresh">
          <div v-if="documentList.length === 0 && !loading" class="empty-state">
            <div class="empty-icon">📄</div>
            <div class="empty-text">暂无文件收藏</div>
            <div class="empty-tip">去看看村务文件吧</div>
          </div>
          <van-list v-else v-model:loading="loading" :finished="finished" finished-text="没有更多了" @load="onLoad">
            <div class="card" v-for="item in documentList" :key="item.id" @click="goDetail('document', item.id)">
              <div class="card-header">
                <van-tag type="warning" size="mini">{{ item.type }}</van-tag>
                <span class="card-time">{{ formatTime(item.createTime) }}</span>
              </div>
              <div class="card-title">{{ item.title }}</div>
              <div class="card-footer">
                <van-button size="mini" type="primary" plain @click.stop="cancelFavorite('document', item.id)">取消收藏</van-button>
              </div>
            </div>
          </van-list>
        </van-pull-refresh>
      </van-tab>
    </van-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const activeTab = ref('notice')
const noticeList = ref([])
const policyList = ref([])
const documentList = ref([])

const isRefreshing = ref(false)
const loading = ref(false)
const finished = ref(false)
const page = ref(1)
const pageSize = 10

const onBack = () => { goBack(router) }

const onTabChange = () => {
  page.value = 1
  finished.value = false
  fetchData()
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`
}

const goDetail = (type, id) => {
  if (type === 'notice') {
    router.push(`/public-notice-detail/${id}`)
  } else if (type === 'policy') {
    showToast('政策详情功能开发中')
  } else if (type === 'document') {
    showToast('文件详情功能开发中')
  }
}

const cancelFavorite = (type, id) => {
  showToast('已取消收藏')
  if (type === 'notice') {
    noticeList.value = noticeList.value.filter(item => item.id !== id)
  } else if (type === 'policy') {
    policyList.value = policyList.value.filter(item => item.id !== id)
  } else if (type === 'document') {
    documentList.value = documentList.value.filter(item => item.id !== id)
  }
}

const fetchData = async () => {
  try {
    const res = await request.get('/favorites/list', {
      params: { type: activeTab.value, page: page.value, size: pageSize }
    })
    const rawData = res || []
    const data = Array.isArray(rawData) ? rawData : []
    if (page.value === 1) {
      if (activeTab.value === 'notice') {
        noticeList.value = data
      } else if (activeTab.value === 'policy') {
        policyList.value = data
      } else if (activeTab.value === 'document') {
        documentList.value = data
      }
    } else {
      if (activeTab.value === 'notice') {
        noticeList.value = [...noticeList.value, ...data]
      } else if (activeTab.value === 'policy') {
        policyList.value = [...policyList.value, ...data]
      } else if (activeTab.value === 'document') {
        documentList.value = [...documentList.value, ...data]
      }
    }
    finished.value = data.length < pageSize
  } catch (error) {
    console.error('获取收藏列表失败', error)
    if (page.value === 1) {
      if (activeTab.value === 'notice') {
        noticeList.value = [
          { id: 1, title: '2024年春节放假通知', type: '通知公告', createTime: '2024-01-15' },
          { id: 2, title: '村务公开第一季度报告', type: '村务公开', createTime: '2024-04-01' },
          { id: 3, title: '2024年耕地地力保护补贴公示', type: '补贴公示', createTime: '2024-06-10' }
        ]
      } else if (activeTab.value === 'policy') {
        policyList.value = [
          { id: 1, title: '乡村振兴补贴政策解读', type: '补贴政策', createTime: '2024-02-10' },
          { id: 2, title: '农村危房改造实施方案', type: '住房政策', createTime: '2024-03-05' },
          { id: 3, title: '新型农村合作医疗实施细则', type: '医保政策', createTime: '2024-01-20' }
        ]
      } else if (activeTab.value === 'document') {
        documentList.value = [
          { id: 1, title: '罗卜田乡村民自治章程', type: '规章制度', createTime: '2024-01-01' },
          { id: 2, title: '村务监督委员会工作制度', type: '管理制度', createTime: '2024-01-15' }
        ]
      }
    }
  }
}

const onRefresh = async () => {
  isRefreshing.value = true
  page.value = 1
  finished.value = false
  await fetchData()
  isRefreshing.value = false
}

const onLoad = async () => {
  if (finished.value) return
  loading.value = true
  page.value++
  await fetchData()
  loading.value = false
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.favorites-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.tabs {
  background: #fff;
}

:deep(.van-tabs__nav) {
  background: #fff;
}

:deep(.van-tab--active) {
  color: #D22630;
}

:deep(.van-tabs__active-bar) {
  background: #D22630;
}

.content {
  padding: 12px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}

.empty-tip {
  font-size: 13px;
  color: #999;
}

.card-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.card {
  background: #fff;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.card-time {
  font-size: 12px;
  color: #999;
}

.card-title {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
}

:deep(.van-nav-bar) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

:deep(.van-nav-bar__title) {
  color: #FFD700;
}

:deep(.van-button--primary) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%) !important;
  border: none !important;
}
</style>
