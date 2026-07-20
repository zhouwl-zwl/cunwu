
<template>
  <div class="org-members-page page-container">
    <van-nav-bar title="组织架构" left-arrow @click-left="onBack" />

    <van-tabs v-model:active="activeOrg" @change="onOrgChange">
      <van-tab title="支委" name="支委" />
      <van-tab title="村委" name="村委" />
      <van-tab title="监委" name="监委" />
    </van-tabs>

    <div class="card" v-for="item in list" :key="item.id" @click="showDetail(item)">
      <div class="member-row">
        <div class="member-avatar">
          <span>{{ item.name ? item.name.charAt(0) : '·' }}</span>
        </div>
        <div class="member-info">
          <div class="member-name">
            {{ item.name }}
            <van-tag type="danger" size="mini">{{ item.position }}</van-tag>
          </div>
          <div class="member-meta">分工：{{ item.division || '—' }}</div>
          <div class="member-meta">电话：{{ item.phone || '—' }}</div>
        </div>
        <div class="member-arrow">›</div>
      </div>
    </div>

    <van-empty v-if="!loading && !list.length" description="暂无人员信息" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { orgMembersData } from '../data/mockData'
import { goBack } from '../utils/index'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeOrg = ref('支委')

const onBack = () => { goBack(router) }

const onOrgChange = () => {
  fetchData()
}

const showDetail = (item) => {
  showToast(`${item.name} - ${item.position}`)
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await request.get('/public/org-members', { params: { orgType: activeOrg.value } })
    const rawData = res.data || []
    const data = Array.isArray(rawData) ? rawData : []
    list.value = data.length > 0 ? data : orgMembersData[activeOrg.value] || []
  } catch (error) {
    list.value = orgMembersData[activeOrg.value] || []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.org-members-page {
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

.member-row {
  display: flex;
  align-items: center;
}

.member-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
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
  margin-bottom: 6px;
}

.member-meta {
  font-size: 12px;
  color: #969799;
  margin-top: 2px;
}

.member-arrow {
  font-size: 22px;
  color: #ccc;
}
</style>
