<template>
  <div class="excel-table">
    <div class="table-header">
      <div class="header-row">
        <div 
          v-for="(col, index) in columns" 
          :key="index" 
          class="header-cell"
          :style="{ width: col.width || `${100 / columns.length}%` }"
        >
          {{ col.title }}
        </div>
      </div>
    </div>
    <div class="table-body">
      <div 
        v-for="(row, rowIndex) in data" 
        :key="rowIndex" 
        class="body-row"
        :class="{ 'row-even': rowIndex % 2 === 0, 'row-odd': rowIndex % 2 === 1 }"
      >
        <div 
          v-for="(col, colIndex) in columns" 
          :key="colIndex" 
          class="body-cell"
          :style="{ width: col.width || `${100 / columns.length}%` }"
        >
          <span v-if="col.formatter">
            {{ col.formatter(row[col.key], row) }}
          </span>
          <span v-else>
            {{ row[col.key] || '-' }}
          </span>
        </div>
      </div>
      <div v-if="data.length === 0" class="empty-row">
        <div class="empty-cell" :style="{ width: '100%' }">暂无数据</div>
      </div>
    </div>
    <div class="table-footer" v-if="showExport">
      <van-button type="primary" size="small" @click="handleExport">
        <van-icon name="download" size="16" />
        导出Excel
      </van-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Button as VanButton, Icon as VanIcon } from 'vant'
import { exportToExcel } from '@/utils/excel'

const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  columns: {
    type: Array,
    default: () => []
  },
  showExport: {
    type: Boolean,
    default: true
  },
  exportFilename: {
    type: String,
    default: 'export.xlsx'
  }
})

const handleExport = () => {
  exportToExcel(props.data, props.columns, props.exportFilename)
}
</script>

<style scoped>
.excel-table {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.table-header {
  background: linear-gradient(135deg, #1989fa 0%, #1677ff 100%);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-row {
  display: flex;
}

.header-cell {
  padding: 12px 8px;
  text-align: center;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border-right: 1px solid rgba(255, 255, 255, 0.2);
}

.header-cell:last-child {
  border-right: none;
}

.table-body {
  max-height: 400px;
  overflow-y: auto;
}

.body-row {
  display: flex;
  border-bottom: 1px solid #f5f5f5;
}

.body-row:last-child {
  border-bottom: none;
}

.row-even {
  background: #fff;
}

.row-odd {
  background: #fafafa;
}

.body-cell {
  padding: 10px 8px;
  text-align: center;
  font-size: 13px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border-right: 1px solid #f5f5f5;
}

.body-cell:last-child {
  border-right: none;
}

.empty-row {
  display: flex;
}

.empty-cell {
  padding: 40px;
  text-align: center;
  color: #999;
  font-size: 14px;
}

.table-footer {
  padding: 12px;
  text-align: right;
  background: #fafafa;
  border-top: 1px solid #f0f0f0;
}
</style>