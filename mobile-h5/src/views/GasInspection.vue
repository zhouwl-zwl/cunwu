<template>
  <div class="page-container">
    <van-nav-bar :title="pageTitle" left-arrow @click-left="goBack" />

    <div class="stats-card">
      <div class="stats-item">
        <span class="stats-num">{{ tableData.length }}</span>
        <span class="stats-label">排查总数</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ rectifiedCount }}</span>
        <span class="stats-label">已整改</span>
      </div>
      <div class="stats-item">
        <span class="stats-num">{{ pendingCount }}</span>
        <span class="stats-label">待整改</span>
      </div>
    </div>

    <div class="table-wrapper">
      <div class="table-title">罗卜田乡燃气"百日行动"排查台账</div>
      <div class="table-subtitle">芷江侗族自治县罗卜田乡 填报时间：2022.8.10</div>
      <div class="table-scroll">
        <table class="data-table">
          <thead>
            <tr>
              <th>序号</th>
              <th>用户名称</th>
              <th>用户类别</th>
              <th>用户性质</th>
              <th>隐患问题描述</th>
              <th>整改措施</th>
              <th>整改情况</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in tableData" :key="row.idx">
              <td>{{ row.idx }}</td>
              <td>{{ row.name }}</td>
              <td>{{ row.category }}</td>
              <td>{{ row.nature }}</td>
              <td>{{ row.hazard }}</td>
              <td>{{ row.measure }}</td>
              <td>
                <span v-if="row.status" class="status-tag" :class="row.status.includes('整改到位') ? 'done' : 'pending'">{{ row.status }}</span>
                <span v-else class="status-tag pending">待整改</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const pageTitle = '燃气\u201c百日行动\u201d排查台账'

const tableData = ref([
  { idx: 1, name: '唐和平饭店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 2, name: '龚小刚包子店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 3, name: '李小波粉店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 4, name: '江元香粉店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 5, name: '李柳柳饭店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 6, name: '杨爱红包子店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 7, name: '邱小平饭店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 8, name: '龚国平粉店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 9, name: '刘正军粉店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '8.12已安装燃气报警整改到位' },
  { idx: 10, name: '龚志坤粉店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 11, name: '龚志高饭店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 12, name: '张洁粉店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 13, name: '谭利军炸鸡店', category: '瓶装燃气用户', nature: '工商用户', hazard: '未安装燃气泄露报警器', measure: '限期整改', status: '' },
  { idx: 14, name: '李元浩液化气供应站', category: '供应站', nature: '工商用户', hazard: '灭火器数量不足', measure: '限期整改', status: '' },
  { idx: 15, name: '许玉芳液化气供应站', category: '供应站', nature: '工商用户', hazard: '安全牌掉落', measure: '立即整改', status: '8.10整改到位' }
])

const rectifiedCount = computed(() => tableData.value.filter(r => r.status && r.status.includes('整改到位')).length)
const pendingCount = computed(() => tableData.value.filter(r => !r.status || !r.status.includes('整改到位')).length)

const goBack = () => router.back()
</script>

<style scoped>
.page-container { min-height: 100vh; background: #f5f5f5; padding-bottom: 20px; }
.stats-card { display: flex; background: linear-gradient(135deg, #8D6E63 0%, #5D4037 100%); padding: 20px 12px; margin-bottom: 12px; }
.stats-item { flex: 1; text-align: center; }
.stats-num { display: block; font-size: 24px; font-weight: bold; color: #fff; margin-bottom: 4px; }
.stats-label { font-size: 12px; color: rgba(255,255,255,0.8); }
.table-wrapper { background: #fff; margin: 0 12px; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.table-title { font-size: 14px; font-weight: bold; color: #333; padding: 14px 12px 4px; text-align: center; }
.table-subtitle { font-size: 12px; color: #999; padding: 0 12px 12px; text-align: center; }
.table-scroll { overflow-x: auto; -webkit-overflow-scrolling: touch; }
.data-table { width: 100%; min-width: 800px; border-collapse: collapse; font-size: 12px; }
.data-table thead { background: linear-gradient(135deg, #8D6E63 0%, #5D4037 100%); position: sticky; top: 0; z-index: 10; }
.data-table th { padding: 10px 6px; text-align: center; color: #fff; font-weight: 600; white-space: nowrap; border-right: 1px solid rgba(255,255,255,0.15); }
.data-table th:last-child { border-right: none; }
.data-table tbody tr:nth-child(even) { background: #FAF5F3; }
.data-table tbody tr:nth-child(odd) { background: #fff; }
.data-table td { padding: 8px 6px; text-align: center; color: #333; white-space: nowrap; border-right: 1px solid #f0e0db; border-bottom: 1px solid #f5ebe8; }
.data-table td:last-child { border-right: none; }
.status-tag { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; }
.status-tag.done { background: #4CAF50; color: #fff; }
.status-tag.pending { background: #FF9800; color: #fff; }
</style>
