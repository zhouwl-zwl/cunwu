<template>
  <div class="notification-settings-page page-container">
    <van-nav-bar title="消息通知设置" left-arrow @click-left="onBack" />

    <div class="settings-card">
      <div class="card-header">
        <van-icon name="bell-o" size="20" color="#C8102E" />
        <span class="card-title">全局通知</span>
      </div>
      <div class="card-content">
        <div class="setting-item">
          <span class="setting-label">开启消息通知</span>
          <van-switch v-model="globalEnabled" @change="onGlobalChange" />
        </div>
        <div class="setting-item">
          <span class="setting-label">声音提醒</span>
          <van-switch v-model="soundEnabled" :disabled="!globalEnabled" />
        </div>
        <div class="setting-item">
          <span class="setting-label">震动提醒</span>
          <van-switch v-model="vibrationEnabled" :disabled="!globalEnabled" />
        </div>
      </div>
    </div>

    <div class="settings-card">
      <div class="card-header">
        <van-icon name="tag-o" size="20" color="#C8102E" />
        <span class="card-title">分类通知</span>
      </div>
      <div class="card-content">
        <div class="setting-item">
          <div class="setting-info">
            <span class="setting-label">补贴发放通知</span>
            <span class="setting-desc">补贴到账时提醒</span>
          </div>
          <van-switch v-model="subsidyEnabled" :disabled="!globalEnabled" @change="onSettingChange('subsidy')" />
        </div>
        <div class="setting-item">
          <div class="setting-info">
            <span class="setting-label">诉求处理通知</span>
            <span class="setting-desc">诉求受理、处理进度、办结时提醒</span>
          </div>
          <van-switch v-model="demandEnabled" :disabled="!globalEnabled" @change="onSettingChange('demand')" />
        </div>
        <div class="setting-item">
          <div class="setting-info">
            <span class="setting-label">隐患预警通知</span>
            <span class="setting-desc">安全隐患、整改提醒</span>
          </div>
          <van-switch v-model="hazardEnabled" :disabled="!globalEnabled" @change="onSettingChange('hazard')" />
        </div>
        <div class="setting-item">
          <div class="setting-info">
            <span class="setting-label">预约提醒</span>
            <span class="setting-desc">办事预约确认、提醒</span>
          </div>
          <van-switch v-model="appointmentEnabled" :disabled="!globalEnabled" @change="onSettingChange('appointment')" />
        </div>
        <div class="setting-item">
          <div class="setting-info">
            <span class="setting-label">公告通知</span>
            <span class="setting-desc">村务公告、政策通知</span>
          </div>
          <van-switch v-model="noticeEnabled" :disabled="!globalEnabled" @change="onSettingChange('notice')" />
        </div>
        <div class="setting-item">
          <div class="setting-info">
            <span class="setting-label">审核通知</span>
            <span class="setting-desc">党员身份、职务审核结果</span>
          </div>
          <van-switch v-model="auditEnabled" :disabled="!globalEnabled" @change="onSettingChange('audit')" />
        </div>
        <div class="setting-item">
          <div class="setting-info">
            <span class="setting-label">志愿活动通知</span>
            <span class="setting-desc">志愿活动报名、签到提醒</span>
          </div>
          <van-switch v-model="volunteerEnabled" :disabled="!globalEnabled" @change="onSettingChange('volunteer')" />
        </div>
      </div>
    </div>

    <div class="settings-card">
      <div class="card-header">
        <van-icon name="clock-o" size="20" color="#C8102E" />
        <span class="card-title">免打扰时段</span>
      </div>
      <div class="card-content">
        <div class="setting-item">
          <span class="setting-label">开启免打扰</span>
          <van-switch v-model="doNotDisturbEnabled" />
        </div>
        <div v-if="doNotDisturbEnabled" class="time-range">
          <div class="time-item">
            <span class="time-label">开始时间</span>
            <van-field v-model="startTime" readonly clickable @click="showStartTimePicker = true" />
          </div>
          <div class="time-divider">—</div>
          <div class="time-item">
            <span class="time-label">结束时间</span>
            <van-field v-model="endTime" readonly clickable @click="showEndTimePicker = true" />
          </div>
        </div>
      </div>
    </div>

    <div class="settings-card">
      <div class="card-header">
        <van-icon name="history" size="20" color="#C8102E" />
        <span class="card-title">通知历史</span>
      </div>
      <div class="card-content">
        <div v-if="notificationHistory.length === 0" class="empty-state">
          <div class="empty-icon">📭</div>
          <div class="empty-text">暂无通知记录</div>
        </div>
        <div v-else class="history-list">
          <div 
            v-for="item in notificationHistory" 
            :key="item.id" 
            class="history-item"
            :class="{ read: item.read }"
            @click="markAsRead(item)"
          >
            <div class="history-icon">{{ getNotificationIcon(item.type) }}</div>
            <div class="history-content">
              <div class="history-title">{{ item.title }}</div>
              <div class="history-desc">{{ item.description }}</div>
              <div class="history-time">{{ formatTime(item.time) }}</div>
            </div>
            <div v-if="!item.read" class="unread-dot"></div>
          </div>
        </div>
        <div class="clear-btn-wrap">
          <van-button size="small" type="danger" plain @click="clearHistory">清空通知</van-button>
        </div>
      </div>
    </div>

    <van-popup v-model:show="showStartTimePicker" position="bottom" :round="true">
      <van-picker :columns="timeOptions" @confirm="onStartTimeConfirm" @cancel="showStartTimePicker = false" />
    </van-popup>

    <van-popup v-model:show="showEndTimePicker" position="bottom" :round="true">
      <van-picker :columns="timeOptions" @confirm="onEndTimeConfirm" @cancel="showEndTimePicker = false" />
    </van-popup>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { goBack } from '../utils/index'

const router = useRouter()

const globalEnabled = ref(true)
const soundEnabled = ref(true)
const vibrationEnabled = ref(false)
const doNotDisturbEnabled = ref(false)

const startTime = ref('22:00')
const endTime = ref('08:00')

const showStartTimePicker = ref(false)
const showEndTimePicker = ref(false)

const timeOptions = []
for (let i = 0; i < 24; i++) {
  for (let j = 0; j < 60; j += 30) {
    timeOptions.push(`${String(i).padStart(2, '0')}:${String(j).padStart(2, '0')}`)
  }
}

const subsidyEnabled = ref(true)
const demandEnabled = ref(true)
const hazardEnabled = ref(true)
const appointmentEnabled = ref(true)
const noticeEnabled = ref(true)
const auditEnabled = ref(true)
const volunteerEnabled = ref(true)

const notificationHistory = ref([
  { id: 1, type: 'subsidy', title: '补贴到账通知', description: '您的耕地地力补贴已到账，金额1200元', time: '2024-07-15 10:30', read: false },
  { id: 2, type: 'demand', title: '诉求处理进度', description: '您提交的道路维修诉求已受理', time: '2024-07-15 09:15', read: false },
  { id: 3, type: 'notice', title: '村务公告', description: '关于开展环境卫生整治的通知', time: '2024-07-14 16:00', read: true },
  { id: 4, type: 'appointment', title: '预约确认', description: '您的办事预约已确认，时间：7月16日上午9点', time: '2024-07-14 14:30', read: true },
  { id: 5, type: 'audit', title: '审核结果', description: '您的党员身份审核已通过', time: '2024-07-13 11:20', read: true }
])

const onBack = () => { goBack(router) }

const onGlobalChange = (val) => {
  if (!val) {
    showToast('已关闭所有消息通知')
  } else {
    showToast('已开启消息通知')
  }
}

const settingEnabledMap = {
  subsidy: subsidyEnabled,
  demand: demandEnabled,
  hazard: hazardEnabled,
  appointment: appointmentEnabled,
  notice: noticeEnabled,
  audit: auditEnabled,
  volunteer: volunteerEnabled
}

const onSettingChange = (type) => {
  const settingNames = {
    subsidy: '补贴发放通知',
    demand: '诉求处理通知',
    hazard: '隐患预警通知',
    appointment: '预约提醒',
    notice: '公告通知',
    audit: '审核通知',
    volunteer: '志愿活动通知'
  }
  showToast(`${settingNames[type]}已${settingEnabledMap[type].value ? '开启' : '关闭'}`)
}

const onStartTimeConfirm = ({ selectedOptions }) => {
  startTime.value = selectedOptions[0]
  showStartTimePicker.value = false
}

const onEndTimeConfirm = ({ selectedOptions }) => {
  endTime.value = selectedOptions[0]
  showEndTimePicker.value = false
}

const getNotificationIcon = (type) => {
  const icons = {
    subsidy: '💰',
    demand: '📝',
    hazard: '⚠️',
    appointment: '📅',
    notice: '📢',
    audit: '✅',
    volunteer: '🙋'
  }
  return icons[type] || '🔔'
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (days === 0) {
    return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  } else if (days === 1) {
    return '昨天'
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return time.split(' ')[0]
  }
}

const markAsRead = (item) => {
  item.read = true
}

const clearHistory = () => {
  notificationHistory.value = []
  showToast('已清空通知历史')
}
</script>

<style scoped>
.notification-settings-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
  background: #FDF5F5;
}

.settings-card {
  background: #fff;
  margin: 12px;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.card-title {
  flex: 1;
}

.card-content {
  display: flex;
  flex-direction: column;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f5;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-info {
  flex: 1;
}

.setting-label {
  font-size: 15px;
  color: #333;
}

.setting-desc {
  display: block;
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.time-range {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 0;
}

.time-item {
  flex: 1;
}

.time-label {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
}

.time-divider {
  color: #999;
  font-size: 18px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.empty-text {
  font-size: 14px;
  color: #999;
}

.history-list {
  display: flex;
  flex-direction: column;
}

.history-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f5;
}

.history-item:last-child {
  border-bottom: none;
}

.history-item.read {
  opacity: 0.6;
}

.history-icon {
  font-size: 24px;
  flex-shrink: 0;
}

.history-content {
  flex: 1;
  min-width: 0;
}

.history-title {
  font-size: 15px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.history-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.history-time {
  font-size: 12px;
  color: #999;
}

.unread-dot {
  width: 8px;
  height: 8px;
  background: #C8102E;
  border-radius: 50%;
  flex-shrink: 0;
  margin-top: 8px;
}

.clear-btn-wrap {
  text-align: center;
  margin-top: 16px;
}
</style>