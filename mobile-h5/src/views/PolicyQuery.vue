<template>
  <div class="page-container">
    <van-nav-bar title="政策查询" left-arrow @click-left="onBack" />
    
    <van-search v-model="keyword" placeholder="搜索政策" @search="onSearch" />
    
    <van-tabs v-model:active="activeTab">
      <van-tab title="全部" name="all">
        <van-list :finished="finished" finished-text="没有更多了" @load="loadData">
          <van-cell v-for="item in policyList" :key="item.id" clickable @click="showDetail(item)">
            <template #title>
              <div class="flex justify-between">
                <span class="font-bold">{{ item.title }}</span>
                <span class="tag">{{ item.category }}</span>
              </div>
            </template>
            <template #value>
              <div class="text-gray-500 text-sm line-clamp-2">
                {{ item.content }}
              </div>
              <div class="text-gray-400 text-xs mt-1">
                {{ item.createTime }}
              </div>
            </template>
          </van-cell>
        </van-list>
      </van-tab>
      
      <van-tab title="低保" name="lowIncome">
        <van-list :finished="finished" finished-text="没有更多了" @load="loadData('低保')">
          <van-cell v-for="item in filteredList('低保')" :key="item.id" clickable @click="showDetail(item)">
            <template #title>{{ item.title }}</template>
            <template #value>
              <div class="text-gray-500 text-sm line-clamp-2">{{ item.content }}</div>
            </template>
          </van-cell>
        </van-list>
      </van-tab>
      
      <van-tab title="养老" name="pension">
        <van-list :finished="finished" finished-text="没有更多了" @load="loadData('养老')">
          <van-cell v-for="item in filteredList('养老')" :key="item.id" clickable @click="showDetail(item)">
            <template #title>{{ item.title }}</template>
            <template #value>
              <div class="text-gray-500 text-sm line-clamp-2">{{ item.content }}</div>
            </template>
          </van-cell>
        </van-list>
      </van-tab>
      
      <van-tab title="产业补贴" name="industry">
        <van-list :finished="finished" finished-text="没有更多了" @load="loadData('产业')">
          <van-cell v-for="item in filteredList('产业')" :key="item.id" clickable @click="showDetail(item)">
            <template #title>{{ item.title }}</template>
            <template #value>
              <div class="text-gray-500 text-sm line-clamp-2">{{ item.content }}</div>
            </template>
          </van-cell>
        </van-list>
      </van-tab>
    </van-tabs>

    <van-dialog v-model:show="showDetailDialog" :title="'政策详情'" width="90%">
      <div v-if="currentPolicy" class="detail-content">
        <div class="detail-title">{{ currentPolicy.title }}</div>
        <div class="detail-meta">
          <span class="tag">{{ currentPolicy.category }}</span>
          <span>{{ currentPolicy.createTime }}</span>
        </div>
        <div class="detail-body">{{ currentPolicy.content }}</div>
      </div>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const activeTab = ref('all')
const keyword = ref('')
const policyList = ref([])
const finished = ref(false)

const showDetailDialog = ref(false)
const currentPolicy = ref(null)

const onBack = () => { goBack(router) }

const filteredList = (category) => {
  return policyList.value.filter(p => p.category.includes(category))
}

const loadData = async (category = '') => {
  try {
    const res = await request.get('/public/policy-list', { params: { page: 1, size: 20, keyword, category } })
    policyList.value = res.records || res.data?.records || res || []
    finished.value = true
  } catch (error) {
    console.error(error)
  }
}

const onSearch = () => {
  finished.value = false
  loadData()
}

const showDetail = (item) => {
  currentPolicy.value = item
  showDetailDialog.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  background: #FDF5F5;
  min-height: 100vh;
}

.tag {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  color: #fff;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
}

.detail-content {
  padding: 16px 0;
}

.detail-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #333;
}

.detail-meta {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  color: #999;
  font-size: 14px;
}

.detail-body {
  font-size: 15px;
  line-height: 1.8;
  color: #666;
  white-space: pre-wrap;
}

:deep(.van-nav-bar) {
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
}

:deep(.van-nav-bar__title) {
  color: #FFD700;
}
</style>
