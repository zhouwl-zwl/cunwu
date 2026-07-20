
<template>
  <div class="party-member-page page-container">
    <van-nav-bar title="党员管理" left-arrow @click-left="onBack" />

    <div class="stat-card">
      <div class="stat-item">
        <div class="stat-value">{{ stat.total || 0 }}</div>
        <div class="stat-label">党员总数</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stat.active || 0 }}</div>
        <div class="stat-label">正式党员</div>
      </div>
      <div class="stat-item">
        <div class="stat-value">{{ stat.probation || 0 }}</div>
        <div class="stat-label">预备党员</div>
      </div>
    </div>

    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="党员台账" name="ledger" />
      <van-tab title="积分管理" name="score" />
    </van-tabs>

    <template v-if="activeTab === 'ledger'">
      <div class="card" v-for="item in list" :key="item.id">
        <div class="member-row">
          <div class="member-avatar">
            <span>{{ item.name ? item.name.charAt(0) : '党' }}</span>
          </div>
          <div class="member-info">
            <div class="member-name">
              {{ item.name }}
              <van-tag :type="item.memberType === '正式党员' ? 'danger' : 'warning'" size="mini">
                {{ item.memberType || '正式党员' }}
              </van-tag>
            </div>
            <div class="member-meta">入党时间：{{ formatTime(item.joinDate) }}</div>
            <div class="member-meta">所在支部：{{ item.branch || '村党支部' }}</div>
            <div class="member-meta">职务：{{ item.position || '党员' }}</div>
          </div>
        </div>
      </div>
    </template>

    <template v-else>
      <div class="card" v-for="item in list" :key="item.id">
        <div class="member-row">
          <div class="member-avatar">
            <span>{{ item.name ? item.name.charAt(0) : '党' }}</span>
          </div>
          <div class="member-info">
            <div class="member-name">{{ item.name }}</div>
            <div class="score-row">
              <span class="score-label">年度积分：</span>
              <span class="score-value">{{ item.score || 0 }}分</span>
            </div>
            <van-progress :percentage="getScorePercent(item.score)" :color="getScoreColor(item.score)" stroke-width="6" />
          </div>
        </div>
      </div>
    </template>

    <van-empty v-if="!loading && !list.length" description="暂无党员信息" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { goBack } from '../utils/index'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const activeTab = ref('ledger')
const stat = reactive({
  total: 0,
  active: 0,
  probation: 0
})

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const formatTime = (time) => {
  if (!time) return '—'
  const date = new Date(time)
  return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}`
}

const getScorePercent = (score) => {
  const s = score || 0
  const percent = Math.min(100, (s / 120) * 100)
  return Math.round(percent)
}

const getScoreColor = (score) => {
  const s = score || 0
  if (s >= 90) return '#07c160'
  if (s >= 60) return '#FFD700'
  return '#D22630'
}

const fetchData = async () => {
  loading.value = true
  try {
    const url = activeTab.value === 'score' ? '/party-member/score-list' : '/party-member/list'
    const res = await request.get(url, { params: { page: 1, size: 20 } })
    list.value = res.records || res.data?.records || res || []
  } catch (error) {
    console.error('获取党员数据失败', error)
  } finally {
    loading.value = false
  }
}

const fetchStat = async () => {
  try {
    const res = await request.get('/party-member/stat')
    Object.assign(stat, res.data)
  } catch (error) {
    console.error('获取党员统计失败', error)
  }
}

onMounted(() => {
  fetchStat()
  fetchData()
})
</script>

<style scoped>
.party-member-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.stat-card {
  display: flex;
  background: linear-gradient(135deg, #D22630 0%, #B01A26 100%);
  margin: 10px 12px;
  border-radius: 14px;
  padding: 16px 0;
}

.stat-item {
  flex: 1;
  text-align: center;
  color: #fff;
  border-right: 1px solid rgba(255, 215, 0, 0.3);
}

.stat-item:last-child {
  border-right: none;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #FFD700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.9;
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
  align-items: flex-start;
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
  margin-top: 4px;
}

.score-row {
  font-size: 13px;
  color: #666;
  margin-bottom: 8px;
}

.score-value {
  color: #D22630;
  font-weight: bold;
}
</style>
