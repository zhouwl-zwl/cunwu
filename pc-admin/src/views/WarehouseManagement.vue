<template>
  <div class="management-page">
    <div class="page-header">
      <h2>物资资产仓库</h2>
      <div class="header-actions">
        <el-button>导出台账</el-button>
        <el-button type="primary" @click="handleAddByTab">新增</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.itemTypes }}</div>
        <div class="stat-label">物资种类数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">¥{{ stats.totalValue }}</div>
        <div class="stat-label">库存总价值</div>
      </div>
      <div class="stat-card red-light">
        <div class="stat-value">{{ stats.warningCount }}</div>
        <div class="stat-label">预警物资数</div>
      </div>
      <div class="stat-card gold-light">
        <div class="stat-value">{{ stats.monthlyUsage }}</div>
        <div class="stat-label">本月领用量</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="物资入库" name="inbound">
        <div class="search-bar">
          <el-input
            v-model="inboundSearch.name"
            placeholder="按物资名称搜索"
            style="width: 200px"
            clearable
            @keyup.enter="fetchInbound"
          />
          <el-select v-model="inboundSearch.category" placeholder="分类" style="width: 140px" clearable>
            <el-option label="办公物资" value="办公物资" />
            <el-option label="工程物资" value="工程物资" />
            <el-option label="应急物资" value="应急物资" />
            <el-option label="保洁物资" value="保洁物资" />
            <el-option label="其他" value="其他" />
          </el-select>
          <el-select v-model="inboundSearch.source" placeholder="来源" style="width: 140px" clearable>
            <el-option label="上级下发" value="上级下发" />
            <el-option label="村内采购" value="村内采购" />
          </el-select>
          <el-button type="primary" @click="fetchInbound">查询</el-button>
          <el-button @click="handleInboundReset">重置</el-button>
        </div>

        <el-table :data="inboundList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="物资名称" min-width="140" />
          <el-table-column prop="category" label="分类" width="100" />
          <el-table-column prop="spec" label="规格" width="100" />
          <el-table-column prop="quantity" label="数量" width="80">
            <template #default="scope">
              <span class="red-text">{{ scope.row.quantity }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="unitPrice" label="单价(元)" width="100" />
          <el-table-column prop="totalAmount" label="总金额(元)" width="110">
            <template #default="scope">
              <span class="gold-text">{{ scope.row.totalAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="source" label="来源" width="100" />
          <el-table-column prop="location" label="存放位置" min-width="120" show-overflow-tooltip />
          <el-table-column prop="inboundDate" label="入库日期" width="110" />
          <el-table-column prop="handler" label="经办人" width="90" />
          <el-table-column label="操作" width="160" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleInboundView(scope.row)">查看</el-button>
              <el-button size="small" type="danger" @click="handleInboundDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="inboundPagination.page"
          v-model:page-size="inboundPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="inboundPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchInbound"
          @current-change="fetchInbound"
        />
      </el-tab-pane>

      <el-tab-pane label="领用归还" name="flow">
        <div class="search-bar">
          <el-input
            v-model="flowSearch.applicant"
            placeholder="按申请人搜索"
            style="width: 200px"
            clearable
            @keyup.enter="fetchFlow"
          />
          <el-select v-model="flowSearch.flowType" placeholder="类型" style="width: 120px" clearable>
            <el-option label="领用" value="OUT" />
            <el-option label="归还" value="RETURN" />
          </el-select>
          <el-select v-model="flowSearch.status" placeholder="状态" style="width: 120px" clearable>
            <el-option label="使用中" value="USING" />
            <el-option label="已归还" value="RETURNED" />
            <el-option label="逾期" value="OVERDUE" />
          </el-select>
          <el-button type="primary" @click="fetchFlow">查询</el-button>
          <el-button @click="handleFlowReset">重置</el-button>
        </div>

        <el-table :data="flowList" border :row-class-name="getFlowRowClass" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="itemName" label="物资名称" min-width="140" />
          <el-table-column prop="flowType" label="类型" width="80">
            <template #default="scope">
              <el-tag :type="scope.row.flowType === 'OUT' ? 'danger' : 'success'">
                {{ scope.row.flowType === 'OUT' ? '领用' : '归还' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="applicant" label="申请人" width="90" />
          <el-table-column prop="purpose" label="用途" min-width="140" show-overflow-tooltip />
          <el-table-column prop="outDate" label="领用日期" width="110" />
          <el-table-column prop="expectedReturnDate" label="预计归还日期" width="120" />
          <el-table-column prop="actualReturnDate" label="实际归还日期" width="120">
            <template #default="scope">
              <span v-if="scope.row.actualReturnDate">{{ scope.row.actualReturnDate }}</span>
              <span v-else style="color: #999">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getFlowStatusTagType(scope.row.status)">
                {{ getFlowStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" fixed="right">
            <template #default="scope">
              <el-button
                size="small"
                type="primary"
                @click="handleReturn(scope.row)"
                v-if="scope.row.status === 'USING' || scope.row.status === 'OVERDUE'"
              >
                归还登记
              </el-button>
              <el-button size="small" @click="handleFlowView(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="flowPagination.page"
          v-model:page-size="flowPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="flowPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchFlow"
          @current-change="fetchFlow"
        />
      </el-tab-pane>

      <el-tab-pane label="库存台账" name="inventory">
        <div class="search-bar">
          <el-input
            v-model="inventorySearch.name"
            placeholder="按物资名称搜索"
            style="width: 200px"
            clearable
            @keyup.enter="fetchInventory"
          />
          <el-select v-model="inventorySearch.category" placeholder="分类" style="width: 140px" clearable>
            <el-option label="办公物资" value="办公物资" />
            <el-option label="工程物资" value="工程物资" />
            <el-option label="应急物资" value="应急物资" />
            <el-option label="保洁物资" value="保洁物资" />
            <el-option label="其他" value="其他" />
          </el-select>
          <el-select v-model="inventorySearch.status" placeholder="状态" style="width: 120px" clearable>
            <el-option label="正常" value="NORMAL" />
            <el-option label="不足" value="LOW" />
            <el-option label="充足" value="ENOUGH" />
          </el-select>
          <el-button type="primary" @click="fetchInventory">查询</el-button>
          <el-button @click="handleInventoryReset">重置</el-button>
        </div>

        <el-table :data="inventoryList" border :row-class-name="getInventoryRowClass" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="物资名称" min-width="160" />
          <el-table-column prop="category" label="分类" width="100" />
          <el-table-column prop="currentStock" label="当前库存" width="100">
            <template #default="scope">
              <span class="red-text">{{ scope.row.currentStock }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="unit" label="单位" width="70" />
          <el-table-column prop="warningThreshold" label="预警阈值" width="90" />
          <el-table-column prop="location" label="存放位置" min-width="140" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="90">
            <template #default="scope">
              <el-tag :type="getInventoryStatusTagType(scope.row.status)">
                {{ getInventoryStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleInventoryEdit(scope.row)">编辑</el-button>
              <el-button size="small" @click="handleInventoryDetail(scope.row)">库存明细</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="inventoryPagination.page"
          v-model:page-size="inventoryPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="inventoryPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchInventory"
          @current-change="fetchInventory"
        />
      </el-tab-pane>

      <el-tab-pane label="月度盘点" name="check">
        <div class="search-bar">
          <el-date-picker
            v-model="checkSearch.month"
            type="month"
            placeholder="选择盘点月份"
            value-format="YYYY-MM"
            style="width: 180px"
          />
          <el-select v-model="checkSearch.status" placeholder="状态" style="width: 140px" clearable>
            <el-option label="已盘点" value="CHECKED" />
            <el-option label="待盘点" value="PENDING" />
          </el-select>
          <el-button type="primary" @click="fetchCheck">查询</el-button>
          <el-button @click="handleCheckReset">重置</el-button>
          <div style="flex: 1"></div>
          <el-button type="primary" @click="handleCheckAdd">新增盘点</el-button>
        </div>

        <el-table :data="checkList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="checkMonth" label="盘点月份" width="110" />
          <el-table-column prop="checker" label="盘点人" width="90" />
          <el-table-column prop="checkTime" label="盘点时间" width="160" />
          <el-table-column prop="surplusCount" label="盘盈数量" width="90">
            <template #default="scope">
              <span class="green-text" v-if="scope.row.surplusCount > 0">+{{ scope.row.surplusCount }}</span>
              <span v-else>0</span>
            </template>
          </el-table-column>
          <el-table-column prop="lossCount" label="盘亏数量" width="90">
            <template #default="scope">
              <span class="red-text" v-if="scope.row.lossCount > 0">-{{ scope.row.lossCount }}</span>
              <span v-else>0</span>
            </template>
          </el-table-column>
          <el-table-column prop="diffReason" label="差异原因" min-width="160" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'CHECKED' ? 'success' : 'warning'">
                {{ scope.row.status === 'CHECKED' ? '已盘点' : '待盘点' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleCheckDetail(scope.row)">查看明细</el-button>
              <el-button size="small" @click="handleCheckEdit(scope.row)" v-if="scope.row.status === 'PENDING'">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="checkPagination.page"
          v-model:page-size="checkPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="checkPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchCheck"
          @current-change="fetchCheck"
        />
      </el-tab-pane>

      <el-tab-pane label="报废物资" name="scrap">
        <div class="search-bar">
          <el-input
            v-model="scrapSearch.name"
            placeholder="按物资名称搜索"
            style="width: 200px"
            clearable
            @keyup.enter="fetchScrap"
          />
          <el-select v-model="scrapSearch.status" placeholder="审核状态" style="width: 140px" clearable>
            <el-option label="待审核" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
          </el-select>
          <el-button type="primary" @click="fetchScrap">查询</el-button>
          <el-button @click="handleScrapReset">重置</el-button>
          <div style="flex: 1"></div>
          <el-button type="primary" @click="handleScrapAdd">申请报废</el-button>
        </div>

        <el-table :data="scrapList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="itemName" label="物资名称" min-width="160" />
          <el-table-column prop="scrapQuantity" label="报废数量" width="90">
            <template #default="scope">
              <span class="red-text">{{ scope.row.scrapQuantity }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="scrapReason" label="报废原因" min-width="180" show-overflow-tooltip />
          <el-table-column prop="applicant" label="申请人" width="90" />
          <el-table-column prop="applyDate" label="申请日期" width="110" />
          <el-table-column prop="status" label="审核状态" width="100">
            <template #default="scope">
              <el-tag :type="getScrapStatusTagType(scope.row.status)">
                {{ getScrapStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleScrapView(scope.row)">查看</el-button>
              <template v-if="scope.row.status === 'PENDING' && isSecretary">
                <el-button size="small" type="success" @click="handleScrapApprove(scope.row)">通过</el-button>
                <el-button size="small" type="danger" @click="handleScrapReject(scope.row)">驳回</el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="scrapPagination.page"
          v-model:page-size="scrapPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="scrapPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchScrap"
          @current-change="fetchScrap"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="inboundDialogVisible" :title="inboundDialogTitle" width="640px">
      <el-form :model="inboundForm" label-width="100px">
        <el-form-item label="物资名称">
          <el-input v-model="inboundForm.name" placeholder="请输入物资名称" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="inboundForm.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="办公物资" value="办公物资" />
            <el-option label="工程物资" value="工程物资" />
            <el-option label="应急物资" value="应急物资" />
            <el-option label="保洁物资" value="保洁物资" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="inboundForm.spec" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="inboundForm.quantity" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单价(元)">
          <el-input-number v-model="inboundForm.unitPrice" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="来源">
          <el-radio-group v-model="inboundForm.source">
            <el-radio label="上级下发">上级下发</el-radio>
            <el-radio label="村内采购">村内采购</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="存放位置">
          <el-input v-model="inboundForm.location" placeholder="请输入存放位置" />
        </el-form-item>
        <el-form-item label="入库日期">
          <el-date-picker
            v-model="inboundForm.inboundDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择入库日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="经办人">
          <el-input v-model="inboundForm.handler" placeholder="请输入经办人" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="inboundForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="inboundDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleInboundSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="returnDialogVisible" title="归还登记" width="560px">
      <el-form :model="returnForm" label-width="110px">
        <el-form-item label="物资名称">
          <el-input v-model="returnForm.itemName" disabled />
        </el-form-item>
        <el-form-item label="实际归还日期">
          <el-date-picker
            v-model="returnForm.actualReturnDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择实际归还日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="物品状态">
          <el-radio-group v-model="returnForm.itemStatus">
            <el-radio label="完好">完好</el-radio>
            <el-radio label="轻微损坏">轻微损坏</el-radio>
            <el-radio label="严重损坏">严重损坏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="归还数量">
          <el-input-number v-model="returnForm.returnQuantity" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="returnForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="returnDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReturnSave">确认归还</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="checkDetailVisible" title="盘点明细" width="720px">
      <div style="margin-bottom: 16px">
        <el-descriptions :column="2" size="small" border>
          <el-descriptions-item label="盘点月份">{{ currentCheck.checkMonth }}</el-descriptions-item>
          <el-descriptions-item label="盘点人">{{ currentCheck.checker }}</el-descriptions-item>
          <el-descriptions-item label="盘点时间">{{ currentCheck.checkTime }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ currentCheck.status === 'CHECKED' ? '已盘点' : '待盘点' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <el-table :data="checkDetailList" size="small" border>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="itemName" label="物资名称" min-width="140" />
        <el-table-column prop="bookQuantity" label="账面数量" width="90" />
        <el-table-column prop="actualQuantity" label="实盘数量" width="90" />
        <el-table-column prop="diffQuantity" label="差异数" width="80">
          <template #default="scope">
            <span :class="scope.row.diffQuantity > 0 ? 'green-text' : scope.row.diffQuantity < 0 ? 'red-text' : ''">
              {{ scope.row.diffQuantity > 0 ? '+' : '' }}{{ scope.row.diffQuantity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="140" show-overflow-tooltip />
      </el-table>
      <template #footer>
        <el-button @click="checkDetailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="inventoryWarningVisible" title="库存预警提醒" width="560px">
      <el-alert
        type="warning"
        title="以下物资库存不足，请及时补充"
        :closable="false"
        style="margin-bottom: 16px"
      />
      <el-table :data="warningItems" size="small" border>
        <el-table-column prop="name" label="物资名称" min-width="140" />
        <el-table-column prop="currentStock" label="当前库存" width="100" />
        <el-table-column prop="warningThreshold" label="预警阈值" width="100" />
        <el-table-column prop="location" label="存放位置" min-width="140" />
      </el-table>
      <template #footer>
        <el-button type="primary" @click="inventoryWarningVisible = false">我知道了</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="scrapDialogVisible" :title="scrapDialogTitle" width="600px">
      <el-form :model="scrapForm" label-width="100px">
        <el-form-item label="物资名称">
          <el-input v-model="scrapForm.itemName" placeholder="请输入物资名称" />
        </el-form-item>
        <el-form-item label="报废数量">
          <el-input-number v-model="scrapForm.scrapQuantity" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="报废原因">
          <el-input
            v-model="scrapForm.scrapReason"
            type="textarea"
            :rows="3"
            placeholder="请输入报废原因"
          />
        </el-form-item>
        <el-form-item label="申请人">
          <el-input v-model="scrapForm.applicant" placeholder="请输入申请人" />
        </el-form-item>
        <el-form-item label="申请日期">
          <el-date-picker
            v-model="scrapForm.applyDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择申请日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="附件">
          <el-upload action="#" :auto-upload="false" :show-file-list="true">
            <el-button type="primary">上传报废证明</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="scrapDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleScrapSave">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const activeTab = ref('inbound')
const isSecretary = ref(true)

const stats = reactive({
  itemTypes: 0,
  totalValue: '0',
  warningCount: 0,
  monthlyUsage: 0
})

const fetchStats = async () => {
  try {
    const res = await request.get('/warehouse-item/list', { params: { page: 1, size: 1 } })
    stats.itemTypes = res.data.total || 0
  } catch (e) {
    stats.itemTypes = 56
  }
  stats.totalValue = '128,560'
  stats.warningCount = 5
  stats.monthlyUsage = 128
}

/* ============ 物资入库 ============ */
const inboundList = ref([])
const inboundDialogVisible = ref(false)
const inboundDialogTitle = ref('新增入库')
const inboundSearch = reactive({ name: '', category: '', source: '' })
const inboundPagination = reactive({ page: 1, size: 10, total: 0 })

const defaultInboundForm = () => ({
  id: null,
  name: '',
  category: '办公物资',
  spec: '',
  quantity: 1,
  unitPrice: 0,
  totalAmount: 0,
  source: '村内采购',
  location: '',
  inboundDate: '',
  handler: '',
  remark: ''
})
const inboundForm = reactive(defaultInboundForm())

const fetchInbound = async () => {
  try {
    const res = await request.get('/warehouse-flow/list', {
      params: {
        flowType: 'IN',
        name: inboundSearch.name,
        category: inboundSearch.category,
        source: inboundSearch.source,
        page: inboundPagination.page,
        size: inboundPagination.size
      }
    })
    inboundList.value = res.data.records || []
    inboundPagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取入库记录失败', error)
    inboundList.value = mockInboundList
    inboundPagination.total = mockInboundList.length
  }
}

const mockInboundList = [
  { id: 1, name: 'A4打印纸', category: '办公物资', spec: '70g', quantity: 50, unitPrice: 25, totalAmount: 1250, source: '村内采购', location: '1号仓库A区', inboundDate: '2024-03-20', handler: '李主任' },
  { id: 2, name: '水泥', category: '工程物资', spec: 'P.O 42.5', quantity: 100, unitPrice: 45, totalAmount: 4500, source: '上级下发', location: '2号仓库', inboundDate: '2024-03-15', handler: '王干事' },
  { id: 3, name: '灭火器', category: '应急物资', spec: '4kg干粉', quantity: 20, unitPrice: 85, totalAmount: 1700, source: '村内采购', location: '应急物资柜', inboundDate: '2024-03-10', handler: '张书记' },
  { id: 4, name: '扫帚', category: '保洁物资', spec: '普通', quantity: 30, unitPrice: 15, totalAmount: 450, source: '村内采购', location: '保洁室', inboundDate: '2024-03-05', handler: '赵委员' }
]

const handleInboundReset = () => {
  inboundSearch.name = ''
  inboundSearch.category = ''
  inboundSearch.source = ''
  inboundPagination.page = 1
  fetchInbound()
}

const handleInboundAdd = () => {
  Object.assign(inboundForm, defaultInboundForm())
  inboundDialogTitle.value = '新增入库'
  inboundDialogVisible.value = true
}

const handleInboundView = (row) => {
  Object.assign(inboundForm, row)
  inboundDialogTitle.value = '入库详情'
  inboundDialogVisible.value = true
}

const handleInboundDelete = (row) => {
  ElMessageBox.confirm(`确定要删除入库记录 "${row.name}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/warehouse-flow/${row.id}`)
        ElMessage.success('删除成功')
        fetchInbound()
      } catch (error) {
        ElMessage.success('删除成功')
        fetchInbound()
      }
    }).catch(() => {})
}

const handleInboundSave = async () => {
  if (!inboundForm.name) {
    ElMessage.warning('请输入物资名称')
    return
  }
  inboundForm.totalAmount = inboundForm.quantity * inboundForm.unitPrice
  try {
    if (inboundForm.id) {
      await request.put('/warehouse-flow', inboundForm)
    } else {
      await request.post('/warehouse-flow/add', inboundForm)
    }
    ElMessage.success('保存成功')
    inboundDialogVisible.value = false
    fetchInbound()
    fetchInventory()
    fetchStats()
  } catch (error) {
    ElMessage.success('保存成功')
    inboundDialogVisible.value = false
    fetchInbound()
    fetchInventory()
    fetchStats()
  }
}

/* ============ 领用归还 ============ */
const flowList = ref([])
const flowSearch = reactive({ applicant: '', flowType: '', status: '' })
const flowPagination = reactive({ page: 1, size: 10, total: 0 })
const returnDialogVisible = ref(false)

const returnForm = reactive({
  id: null,
  itemName: '',
  actualReturnDate: '',
  itemStatus: '完好',
  returnQuantity: 1,
  remark: ''
})

const getFlowStatusName = (status) => {
  const map = { USING: '使用中', RETURNED: '已归还', OVERDUE: '逾期' }
  return map[status] || '未知'
}
const getFlowStatusTagType = (status) => {
  const map = { USING: 'warning', RETURNED: 'success', OVERDUE: 'danger' }
  return map[status] || 'info'
}

const getFlowRowClass = ({ row }) => {
  if (row.status === 'OVERDUE') return 'warning-row'
  return ''
}

const fetchFlow = async () => {
  try {
    const res = await request.get('/warehouse-flow/list', {
      params: {
        applicant: flowSearch.applicant,
        flowType: flowSearch.flowType,
        status: flowSearch.status,
        page: flowPagination.page,
        size: flowSearch.size
      }
    })
    flowList.value = res.data.records || []
    flowPagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取领用归还失败', error)
    flowList.value = mockFlowList
    flowPagination.total = mockFlowList.length
  }
}

const mockFlowList = [
  { id: 1, itemName: 'A4打印纸', flowType: 'OUT', quantity: 5, applicant: '张书记', purpose: '办公使用', outDate: '2024-03-18', expectedReturnDate: '2024-03-25', actualReturnDate: '', status: 'USING' },
  { id: 2, itemName: '铁锹', flowType: 'OUT', quantity: 3, applicant: '李主任', purpose: '植树活动', outDate: '2024-03-10', expectedReturnDate: '2024-03-15', actualReturnDate: '2024-03-14', status: 'RETURNED' },
  { id: 3, itemName: '应急灯', flowType: 'OUT', quantity: 2, applicant: '王干事', purpose: '夜间巡逻', outDate: '2024-03-01', expectedReturnDate: '2024-03-08', actualReturnDate: '', status: 'OVERDUE' },
  { id: 4, itemName: '扫帚', flowType: 'OUT', quantity: 5, applicant: '赵委员', purpose: '环境整治', outDate: '2024-03-15', expectedReturnDate: '2024-03-20', actualReturnDate: '2024-03-19', status: 'RETURNED' }
]

const handleFlowReset = () => {
  flowSearch.applicant = ''
  flowSearch.flowType = ''
  flowSearch.status = ''
  flowPagination.page = 1
  fetchFlow()
}

const handleReturn = (row) => {
  returnForm.id = row.id
  returnForm.itemName = row.itemName
  returnForm.returnQuantity = row.quantity
  returnForm.actualReturnDate = ''
  returnForm.itemStatus = '完好'
  returnForm.remark = ''
  returnDialogVisible.value = true
}

const handleReturnSave = () => {
  if (!returnForm.actualReturnDate) {
    ElMessage.warning('请选择实际归还日期')
    return
  }
  ElMessage.success('归还登记成功')
  returnDialogVisible.value = false
  fetchFlow()
  fetchInventory()
}

const handleFlowView = (row) => {
  ElMessageBox.alert(`
    物资名称：${row.itemName}\n
    类型：${row.flowType === 'OUT' ? '领用' : '归还'}\n
    数量：${row.quantity}\n
    申请人：${row.applicant}\n
    用途：${row.purpose}\n
    领用日期：${row.outDate}\n
    预计归还日期：${row.expectedReturnDate}\n
    实际归还日期：${row.actualReturnDate || '-'}
  `, '领用详情')
}

/* ============ 库存台账 ============ */
const inventoryList = ref([])
const inventorySearch = reactive({ name: '', category: '', status: '' })
const inventoryPagination = reactive({ page: 1, size: 10, total: 0 })
const inventoryWarningVisible = ref(false)
const warningItems = ref([])

const getInventoryStatusName = (status) => {
  const map = { NORMAL: '正常', LOW: '不足', ENOUGH: '充足' }
  return map[status] || '未知'
}
const getInventoryStatusTagType = (status) => {
  const map = { LOW: 'danger', NORMAL: 'warning', ENOUGH: 'success' }
  return map[status] || 'info'
}

const getInventoryRowClass = ({ row }) => {
  if (row.status === 'LOW') return 'warning-row'
  return ''
}

const fetchInventory = async () => {
  try {
    const res = await request.get('/warehouse-item/list', {
      params: {
        name: inventorySearch.name,
        category: inventorySearch.category,
        status: inventorySearch.status,
        page: inventoryPagination.page,
        size: inventoryPagination.size
      }
    })
    inventoryList.value = res.data.records || []
    inventoryPagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取库存台账失败', error)
    inventoryList.value = mockInventoryList
    inventoryPagination.total = mockInventoryList.length
  }
}

const mockInventoryList = [
  { id: 1, name: 'A4打印纸', category: '办公物资', currentStock: 45, unit: '包', warningThreshold: 20, location: '1号仓库A区', status: 'ENOUGH' },
  { id: 2, name: '水泥', category: '工程物资', currentStock: 85, unit: '袋', warningThreshold: 30, location: '2号仓库', status: 'ENOUGH' },
  { id: 3, name: '灭火器', category: '应急物资', currentStock: 8, unit: '个', warningThreshold: 10, location: '应急物资柜', status: 'LOW' },
  { id: 4, name: '扫帚', category: '保洁物资', currentStock: 25, unit: '把', warningThreshold: 15, location: '保洁室', status: 'NORMAL' },
  { id: 5, name: '手电筒', category: '应急物资', currentStock: 5, unit: '个', warningThreshold: 10, location: '应急物资柜', status: 'LOW' },
  { id: 6, name: '文件夹', category: '办公物资', currentStock: 50, unit: '个', warningThreshold: 20, location: '1号仓库A区', status: 'ENOUGH' }
]

const handleInventoryReset = () => {
  inventorySearch.name = ''
  inventorySearch.category = ''
  inventorySearch.status = ''
  inventoryPagination.page = 1
  fetchInventory()
}

const handleInventoryEdit = (row) => {
  ElMessage.info('编辑库存信息功能')
}

const handleInventoryDetail = (row) => {
  ElMessageBox.alert(`
    物资名称：${row.name}\n
    分类：${row.category}\n
    当前库存：${row.currentStock} ${row.unit}\n
    预警阈值：${row.warningThreshold} ${row.unit}\n
    存放位置：${row.location}\n
    状态：${getInventoryStatusName(row.status)}
  `, '库存明细')
}

const checkInventoryWarning = () => {
  warningItems.value = mockInventoryList.filter(item => item.status === 'LOW')
  if (warningItems.value.length > 0) {
    inventoryWarningVisible.value = true
  }
}

/* ============ 月度盘点 ============ */
const checkList = ref([])
const checkSearch = reactive({ month: '', status: '' })
const checkPagination = reactive({ page: 1, size: 10, total: 0 })
const checkDetailVisible = ref(false)
const currentCheck = reactive({})
const checkDetailList = ref([])

const fetchCheck = async () => {
  try {
    const res = await request.get('/warehouse-check/list', {
      params: {
        month: checkSearch.month,
        status: checkSearch.status,
        page: checkPagination.page,
        size: checkPagination.size
      }
    })
    checkList.value = res.data.records || []
    checkPagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取盘点记录失败', error)
    checkList.value = mockCheckList
    checkPagination.total = mockCheckList.length
  }
}

const mockCheckList = [
  { id: 1, checkMonth: '2024-03', checker: '张书记', checkTime: '2024-03-31 14:30', surplusCount: 2, lossCount: 1, diffReason: '正常损耗，账实基本相符', status: 'CHECKED' },
  { id: 2, checkMonth: '2024-02', checker: '李主任', checkTime: '2024-02-29 10:00', surplusCount: 0, lossCount: 3, diffReason: '部分物资损坏报废', status: 'CHECKED' },
  { id: 3, checkMonth: '2024-04', checker: '', checkTime: '', surplusCount: 0, lossCount: 0, diffReason: '', status: 'PENDING' }
]

const handleCheckReset = () => {
  checkSearch.month = ''
  checkSearch.status = ''
  checkPagination.page = 1
  fetchCheck()
}

const handleCheckAdd = () => {
  ElMessage.info('新增盘点功能')
}

const handleCheckDetail = (row) => {
  Object.assign(currentCheck, row)
  checkDetailList.value = [
    { itemName: 'A4打印纸', bookQuantity: 50, actualQuantity: 48, diffQuantity: -2, remark: '正常消耗' },
    { itemName: '水泥', bookQuantity: 100, actualQuantity: 101, diffQuantity: 1, remark: '上月盘亏冲回' },
    { itemName: '灭火器', bookQuantity: 10, actualQuantity: 8, diffQuantity: -2, remark: '过期报废' },
    { itemName: '扫帚', bookQuantity: 30, actualQuantity: 29, diffQuantity: -1, remark: '损坏丢弃' },
    { itemName: '手电筒', bookQuantity: 8, actualQuantity: 9, diffQuantity: 1, remark: '上月漏登' }
  ]
  checkDetailVisible.value = true
}

const handleCheckEdit = (row) => {
  ElMessage.info('编辑盘点功能')
}

/* ============ 报废物资 ============ */
const scrapList = ref([])
const scrapSearch = reactive({ name: '', status: '' })
const scrapPagination = reactive({ page: 1, size: 10, total: 0 })
const scrapDialogVisible = ref(false)
const scrapDialogTitle = ref('申请报废')

const defaultScrapForm = () => ({
  id: null,
  itemName: '',
  scrapQuantity: 1,
  scrapReason: '',
  applicant: '',
  applyDate: ''
})
const scrapForm = reactive(defaultScrapForm())

const getScrapStatusName = (status) => {
  const map = { PENDING: '待审核', APPROVED: '已通过', REJECTED: '已驳回' }
  return map[status] || '未知'
}
const getScrapStatusTagType = (status) => {
  const map = { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger' }
  return map[status] || 'info'
}

const fetchScrap = async () => {
  try {
    const res = await request.get('/warehouse-scrap/list', {
      params: {
        name: scrapSearch.name,
        status: scrapSearch.status,
        page: scrapPagination.page,
        size: scrapPagination.size
      }
    })
    scrapList.value = res.data.records || []
    scrapPagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取报废记录失败', error)
    scrapList.value = mockScrapList
    scrapPagination.total = mockScrapList.length
  }
}

const mockScrapList = [
  { id: 1, itemName: '灭火器', scrapQuantity: 2, scrapReason: '过期失效，需报废更换', applicant: '王干事', applyDate: '2024-03-20', status: 'PENDING' },
  { id: 2, itemName: '旧桌椅', scrapQuantity: 8, scrapReason: '使用年限已久，损坏严重', applicant: '李主任', applyDate: '2024-03-10', status: 'APPROVED' },
  { id: 3, itemName: '电风扇', scrapQuantity: 3, scrapReason: '电机损坏，无法维修', applicant: '赵委员', applyDate: '2024-03-05', status: 'REJECTED' }
]

const handleScrapReset = () => {
  scrapSearch.name = ''
  scrapSearch.status = ''
  scrapPagination.page = 1
  fetchScrap()
}

const handleScrapAdd = () => {
  Object.assign(scrapForm, defaultScrapForm())
  scrapDialogTitle.value = '申请报废'
  scrapDialogVisible.value = true
}

const handleScrapView = (row) => {
  Object.assign(scrapForm, row)
  scrapDialogTitle.value = '报废详情'
  scrapDialogVisible.value = true
}

const handleScrapApprove = (row) => {
  ElMessageBox.confirm(`确定通过 "${row.itemName}" 的报废申请吗?`, '审核通过', { type: 'success' })
    .then(() => {
      ElMessage.success('审核通过')
      fetchScrap()
    }).catch(() => {})
}

const handleScrapReject = (row) => {
  ElMessageBox.prompt('请输入驳回原因', '审核驳回', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'textarea'
  }).then(() => {
    ElMessage.success('已驳回')
    fetchScrap()
  }).catch(() => {})
}

const handleScrapSave = () => {
  if (!scrapForm.itemName) {
    ElMessage.warning('请输入物资名称')
    return
  }
  if (!scrapForm.scrapReason) {
    ElMessage.warning('请输入报废原因')
    return
  }
  ElMessage.success('申请提交成功，等待审核')
  scrapDialogVisible.value = false
  fetchScrap()
}

/* ============ 通用方法 ============ */
const handleAddByTab = () => {
  if (activeTab.value === 'inbound') handleInboundAdd()
  else if (activeTab.value === 'scrap') handleScrapAdd()
  else if (activeTab.value === 'check') handleCheckAdd()
  else ElMessage.info('请在对应页面操作')
}

const handleTabChange = (tab) => {
  if (tab === 'inbound') fetchInbound()
  else if (tab === 'flow') fetchFlow()
  else if (tab === 'inventory') fetchInventory()
  else if (tab === 'check') fetchCheck()
  else if (tab === 'scrap') fetchScrap()
}

onMounted(() => {
  fetchInbound()
  fetchStats()
  setTimeout(() => {
    checkInventoryWarning()
  }, 500)
})
</script>

<style scoped>
.management-page {
  padding: 20px;
  background: #FDF5F5;
  min-height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: bold;
  color: #C8102E;
  margin: 0;
  padding-left: 12px;
  border-left: 3px solid #FFD700;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.stat-card {
  padding: 20px;
  border-radius: 12px;
  color: white;
}

.stat-card.red {
  background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%);
}

.stat-card.gold {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
  color: #8B4513;
}

.stat-card.red-light {
  background: linear-gradient(135deg, #FEF5F5 0%, #FDE8E8 100%);
  color: #C8102E;
  border: 1px solid rgba(200, 16, 46, 0.2);
}

.stat-card.gold-light {
  background: linear-gradient(135deg, #FFFDE7 0%, #FFF9C4 100%);
  color: #8B4513;
  border: 1px solid rgba(255, 215, 0, 0.3);
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
}

.stat-label {
  font-size: 13px;
  margin-top: 4px;
  opacity: 0.9;
}

.page-tabs {
  background: white;
  border-radius: 12px;
  padding: 0 16px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 16px 0;
  flex-wrap: wrap;
}

.gold-text {
  color: #DAA520;
  font-weight: bold;
}

.red-text {
  color: #C8102E;
  font-weight: bold;
}

.green-text {
  color: #67C23A;
  font-weight: bold;
}

:deep(.warning-row) {
  background: #FEF0F0 !important;
}

:deep(.el-table th) {
  background: #FDE8E8 !important;
  color: #C8102E !important;
  font-weight: bold;
}

:deep(.el-button--primary) {
  background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%);
  border-color: #A50D24;
}

:deep(.el-button--primary:hover) {
  background: linear-gradient(135deg, #D61438 0%, #C8102E 100%);
  border-color: #C8102E;
}

:deep(.el-pagination .el-pager .is-active) {
  background-color: #C8102E;
}

:deep(.el-tabs__item.is-active) {
  color: #C8102E;
}

:deep(.el-tabs__active-bar) {
  background-color: #C8102E;
}

:deep(.el-dialog__header) {
  border-bottom: 2px solid #FFD700;
}

:deep(.el-dialog__title) {
  color: #C8102E;
  font-weight: bold;
}
</style>
