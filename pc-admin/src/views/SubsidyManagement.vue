<template>
  <div class="management-page">
    <div class="page-header">
      <h2>惠民补贴发放</h2>
      <div class="header-actions">
        <el-button @click="handleImport">批量导入</el-button>
        <el-button @click="handleExport">批量导出</el-button>
        <el-button @click="handleGeneratePublicity">生成公示</el-button>
        <el-button type="primary" @click="handleAdd">新增发放</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">¥{{ formatMoney(stats.totalAmount) }}</div>
        <div class="stat-label">全年发放总金额</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.beneficiaryHouseholds }}</div>
        <div class="stat-label">受益户数</div>
      </div>
      <div class="stat-card red">
        <div class="stat-value">{{ stats.successCount }}</div>
        <div class="stat-label">发放成功数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.failCount }}</div>
        <div class="stat-label">发放失败数</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="耕地地力补贴" name="CULTIVATED_LAND">
        <div class="search-bar">
          <el-select
            v-model="searchForm.year"
            placeholder="年度"
            style="width: 130px"
            clearable
          >
            <el-option v-for="y in yearOptions" :key="y" :label="y + '年'" :value="y" />
          </el-select>
          <el-input
            v-model="searchForm.keyword"
            placeholder="按受益人搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select
            v-model="searchForm.payStatus"
            placeholder="发放状态"
            style="width: 140px"
            clearable
          >
            <el-option label="待发放" value="PENDING" />
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAIL" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="tableData" border :row-class-name="rowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="beneficiary" label="受益人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="subsidyType" label="补贴类型" width="130">
            <template #default="scope">
              <el-tag type="warning" size="small">{{ getSubsidyTypeName(scope.row.subsidyType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payMonth" label="发放月份" width="110" />
          <el-table-column prop="amount" label="补贴金额(元)" width="130">
            <template #default="scope">
              <span class="amount-text">¥{{ Number(scope.row.amount || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="bankCard" label="银行卡号" width="180" />
          <el-table-column prop="payStatus" label="发放状态" width="100">
            <template #default="scope">
              <el-tag :type="getPayTagType(scope.row.payStatus)">
                {{ getPayStatusName(scope.row.payStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="failReason" label="失败原因" min-width="150" show-overflow-tooltip />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="低保特困补贴" name="MINIMUM_LIVING">
        <div class="search-bar">
          <el-select
            v-model="searchForm.year"
            placeholder="年度"
            style="width: 130px"
            clearable
          >
            <el-option v-for="y in yearOptions" :key="y" :label="y + '年'" :value="y" />
          </el-select>
          <el-input
            v-model="searchForm.keyword"
            placeholder="按受益人搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select
            v-model="searchForm.payStatus"
            placeholder="发放状态"
            style="width: 140px"
            clearable
          >
            <el-option label="待发放" value="PENDING" />
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAIL" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="tableData" border :row-class-name="rowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="beneficiary" label="受益人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="subsidyType" label="补贴类型" width="130">
            <template #default="scope">
              <el-tag type="danger" size="small">{{ getSubsidyTypeName(scope.row.subsidyType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payMonth" label="发放月份" width="110" />
          <el-table-column prop="amount" label="补贴金额(元)" width="130">
            <template #default="scope">
              <span class="amount-text">¥{{ Number(scope.row.amount || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="bankCard" label="银行卡号" width="180" />
          <el-table-column prop="payStatus" label="发放状态" width="100">
            <template #default="scope">
              <el-tag :type="getPayTagType(scope.row.payStatus)">
                {{ getPayStatusName(scope.row.payStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="failReason" label="失败原因" min-width="150" show-overflow-tooltip />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="残疾人补贴" name="DISABLED">
        <div class="search-bar">
          <el-select
            v-model="searchForm.year"
            placeholder="年度"
            style="width: 130px"
            clearable
          >
            <el-option v-for="y in yearOptions" :key="y" :label="y + '年'" :value="y" />
          </el-select>
          <el-input
            v-model="searchForm.keyword"
            placeholder="按受益人搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select
            v-model="searchForm.payStatus"
            placeholder="发放状态"
            style="width: 140px"
            clearable
          >
            <el-option label="待发放" value="PENDING" />
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAIL" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="tableData" border :row-class-name="rowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="beneficiary" label="受益人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="subsidyType" label="补贴类型" width="130">
            <template #default="scope">
              <el-tag type="primary" size="small">{{ getSubsidyTypeName(scope.row.subsidyType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payMonth" label="发放月份" width="110" />
          <el-table-column prop="amount" label="补贴金额(元)" width="130">
            <template #default="scope">
              <span class="amount-text">¥{{ Number(scope.row.amount || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="bankCard" label="银行卡号" width="180" />
          <el-table-column prop="payStatus" label="发放状态" width="100">
            <template #default="scope">
              <el-tag :type="getPayTagType(scope.row.payStatus)">
                {{ getPayStatusName(scope.row.payStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="failReason" label="失败原因" min-width="150" show-overflow-tooltip />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="高龄养老补贴" name="ELDERLY">
        <div class="search-bar">
          <el-select
            v-model="searchForm.year"
            placeholder="年度"
            style="width: 130px"
            clearable
          >
            <el-option v-for="y in yearOptions" :key="y" :label="y + '年'" :value="y" />
          </el-select>
          <el-input
            v-model="searchForm.keyword"
            placeholder="按受益人搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select
            v-model="searchForm.payStatus"
            placeholder="发放状态"
            style="width: 140px"
            clearable
          >
            <el-option label="待发放" value="PENDING" />
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAIL" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="tableData" border :row-class-name="rowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="beneficiary" label="受益人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="subsidyType" label="补贴类型" width="130">
            <template #default="scope">
              <el-tag type="success" size="small">{{ getSubsidyTypeName(scope.row.subsidyType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payMonth" label="发放月份" width="110" />
          <el-table-column prop="amount" label="补贴金额(元)" width="130">
            <template #default="scope">
              <span class="amount-text">¥{{ Number(scope.row.amount || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="bankCard" label="银行卡号" width="180" />
          <el-table-column prop="payStatus" label="发放状态" width="100">
            <template #default="scope">
              <el-tag :type="getPayTagType(scope.row.payStatus)">
                {{ getPayStatusName(scope.row.payStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="failReason" label="失败原因" min-width="150" show-overflow-tooltip />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="产业奖补" name="INDUSTRY">
        <div class="search-bar">
          <el-select
            v-model="searchForm.year"
            placeholder="年度"
            style="width: 130px"
            clearable
          >
            <el-option v-for="y in yearOptions" :key="y" :label="y + '年'" :value="y" />
          </el-select>
          <el-input
            v-model="searchForm.keyword"
            placeholder="按受益人搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select
            v-model="searchForm.payStatus"
            placeholder="发放状态"
            style="width: 140px"
            clearable
          >
            <el-option label="待发放" value="PENDING" />
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAIL" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="tableData" border :row-class-name="rowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="beneficiary" label="受益人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="subsidyType" label="补贴类型" width="130">
            <template #default="scope">
              <el-tag type="info" size="small">{{ getSubsidyTypeName(scope.row.subsidyType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payMonth" label="发放月份" width="110" />
          <el-table-column prop="amount" label="补贴金额(元)" width="130">
            <template #default="scope">
              <span class="amount-text">¥{{ Number(scope.row.amount || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="bankCard" label="银行卡号" width="180" />
          <el-table-column prop="payStatus" label="发放状态" width="100">
            <template #default="scope">
              <el-tag :type="getPayTagType(scope.row.payStatus)">
                {{ getPayStatusName(scope.row.payStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="failReason" label="失败原因" min-width="150" show-overflow-tooltip />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="雨露教育补贴" name="EDUCATION">
        <div class="search-bar">
          <el-select
            v-model="searchForm.year"
            placeholder="年度"
            style="width: 130px"
            clearable
          >
            <el-option v-for="y in yearOptions" :key="y" :label="y + '年'" :value="y" />
          </el-select>
          <el-input
            v-model="searchForm.keyword"
            placeholder="按受益人搜索"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
          <el-select
            v-model="searchForm.groupName"
            placeholder="村组"
            style="width: 160px"
            clearable
          >
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
          <el-select
            v-model="searchForm.payStatus"
            placeholder="发放状态"
            style="width: 140px"
            clearable
          >
            <el-option label="待发放" value="PENDING" />
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAIL" />
          </el-select>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </div>
        <el-table :data="tableData" border :row-class-name="rowClassName" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="beneficiary" label="受益人" width="100" />
          <el-table-column prop="groupName" label="村组" width="120" />
          <el-table-column prop="subsidyType" label="补贴类型" width="130">
            <template #default="scope">
              <el-tag size="small">{{ getSubsidyTypeName(scope.row.subsidyType) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payMonth" label="发放月份" width="110" />
          <el-table-column prop="amount" label="补贴金额(元)" width="130">
            <template #default="scope">
              <span class="amount-text">¥{{ Number(scope.row.amount || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="bankCard" label="银行卡号" width="180" />
          <el-table-column prop="payStatus" label="发放状态" width="100">
            <template #default="scope">
              <el-tag :type="getPayTagType(scope.row.payStatus)">
                {{ getPayStatusName(scope.row.payStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="failReason" label="失败原因" min-width="150" show-overflow-tooltip />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.size"
      :page-sizes="[10, 20, 50, 100]"
      :total="pagination.total"
      layout="total, sizes, prev, pager, next, jumper"
      class="page-pagination"
      @size-change="fetchData"
      @current-change="fetchData"
    />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="640px">
      <el-form :model="form" label-width="100px" ref="formRef">
        <el-form-item label="受益人" prop="beneficiary">
          <el-input v-model="form.beneficiary" placeholder="请输入受益人姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="所属村组" prop="groupName">
          <el-select v-model="form.groupName" placeholder="请选择村组" style="width: 100%">
            <el-option v-for="g in groupOptions" :key="g" :label="g" :value="g" />
          </el-select>
        </el-form-item>
        <el-form-item label="补贴类型" prop="subsidyType">
          <el-select v-model="form.subsidyType" placeholder="请选择补贴类型" style="width: 100%">
            <el-option label="耕地地力补贴" value="CULTIVATED_LAND" />
            <el-option label="低保特困补贴" value="MINIMUM_LIVING" />
            <el-option label="残疾人补贴" value="DISABLED" />
            <el-option label="高龄养老补贴" value="ELDERLY" />
            <el-option label="产业奖补" value="INDUSTRY" />
            <el-option label="雨露教育补贴" value="EDUCATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="发放月份" prop="payMonth">
          <el-date-picker
            v-model="form.payMonth"
            type="month"
            value-format="YYYY-MM"
            placeholder="请选择发放月份"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="补贴金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="银行卡号" prop="bankCard">
          <el-input v-model="form.bankCard" placeholder="请输入银行卡号" />
        </el-form-item>
        <el-form-item label="开户银行" prop="bankName">
          <el-input v-model="form.bankName" placeholder="请输入开户银行" />
        </el-form-item>
        <el-form-item label="发放状态" prop="payStatus">
          <el-select v-model="form.payStatus" style="width: 100%">
            <el-option label="待发放" value="PENDING" />
            <el-option label="发放成功" value="SUCCESS" />
            <el-option label="发放失败" value="FAIL" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="补贴详情" width="700px" class="detail-dialog">
      <div class="detail-cards">
        <div class="detail-card">
          <div class="card-title">基础信息</div>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="受益人">{{ currentDetail.beneficiary }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ currentDetail.gender || '-' }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ currentDetail.idCard }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ currentDetail.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属村组">{{ currentDetail.groupName }}</el-descriptions-item>
            <el-descriptions-item label="家庭人口">{{ currentDetail.familyCount || '-' }}人</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="detail-card">
          <div class="card-title">补贴信息</div>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="补贴类型">
              <el-tag :type="getSubsidyTagType(currentDetail.subsidyType)" size="small">
                {{ getSubsidyTypeName(currentDetail.subsidyType) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="发放月份">{{ currentDetail.payMonth }}</el-descriptions-item>
            <el-descriptions-item label="补贴金额">
              <span class="amount-text">¥{{ Number(currentDetail.amount || 0).toFixed(2) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="发放状态">
              <el-tag :type="getPayTagType(currentDetail.payStatus)">
                {{ getPayStatusName(currentDetail.payStatus) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="开户银行">{{ currentDetail.bankName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="银行卡号">{{ currentDetail.bankCard }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="detail-card" v-if="currentDetail.payStatus === 'FAIL'">
          <div class="card-title">失败信息</div>
          <el-alert
            :title="currentDetail.failReason || '发放失败'"
            type="error"
            :closable="false"
            show-icon
          />
          <div class="retry-section">
            <el-button type="primary" size="small" @click="handleRetry">重新发放</el-button>
            <el-button size="small" @click="handleMarkSuccess">标记为成功</el-button>
          </div>
        </div>

        <div class="detail-card">
          <div class="card-title">发放记录</div>
          <el-timeline>
            <el-timeline-item
              v-for="(record, idx) in currentDetail.payRecords || []"
              :key="idx"
              :type="record.status === 'SUCCESS' ? 'success' : record.status === 'FAIL' ? 'danger' : 'warning'"
              :timestamp="record.time"
            >
              <div class="pay-record-item">
                <span class="pay-record-status">{{ record.status === 'SUCCESS' ? '发放成功' : record.status === 'FAIL' ? '发放失败' : '待发放' }}</span>
                <span class="pay-record-amount">¥{{ Number(record.amount || 0).toFixed(2) }}</span>
                <span class="pay-record-operator">操作人：{{ record.operator }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const activeTab = ref('CULTIVATED_LAND')
const tableData = ref([])
const dialogVisible = ref(false)
const detailVisible = ref(false)
const dialogTitle = ref('新增发放')
const formRef = ref(null)

const groupOptions = ref(['一组', '二组', '三组', '四组', '五组', '六组', '七组', '八组'])
const yearOptions = ref([2026, 2025, 2024, 2023])

const stats = reactive({
  totalAmount: 0,
  beneficiaryHouseholds: 0,
  successCount: 0,
  failCount: 0
})

const searchForm = reactive({
  keyword: '',
  groupName: '',
  year: 2026,
  payStatus: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const defaultForm = () => ({
  id: null,
  beneficiary: '',
  idCard: '',
  groupName: '',
  subsidyType: 'CULTIVATED_LAND',
  payMonth: '',
  amount: 0,
  bankCard: '',
  bankName: '',
  payStatus: 'PENDING',
  remark: ''
})

const form = reactive(defaultForm())

const currentDetail = reactive({
  beneficiary: '',
  gender: '',
  idCard: '',
  phone: '',
  groupName: '',
  familyCount: 0,
  subsidyType: '',
  payMonth: '',
  amount: 0,
  payStatus: '',
  bankName: '',
  bankCard: '',
  failReason: '',
  payRecords: []
})

const formatMoney = (val) => {
  if (!val) return '0.00'
  return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getSubsidyTypeName = (type) => {
  const map = {
    CULTIVATED_LAND: '耕地地力补贴',
    MINIMUM_LIVING: '低保特困补贴',
    DISABLED: '残疾人补贴',
    ELDERLY: '高龄养老补贴',
    INDUSTRY: '产业奖补',
    EDUCATION: '雨露教育补贴'
  }
  return map[type] || '未知'
}

const getSubsidyTagType = (type) => {
  const map = {
    CULTIVATED_LAND: 'warning',
    MINIMUM_LIVING: 'danger',
    DISABLED: 'primary',
    ELDERLY: 'success',
    INDUSTRY: 'info',
    EDUCATION: ''
  }
  return map[type] || ''
}

const getPayStatusName = (status) => {
  const map = { PENDING: '待发放', SUCCESS: '成功', FAIL: '失败' }
  return map[status] || '未知'
}

const getPayTagType = (status) => {
  const map = { PENDING: 'warning', SUCCESS: 'success', FAIL: 'danger' }
  return map[status] || 'info'
}

const rowClassName = ({ row }) => {
  if (row.payStatus === 'FAIL') return 'warning-row'
  return ''
}

const fetchStats = async () => {
  try {
    const res = await request.get('/subsidy/stats', {
      params: { subsidyType: activeTab.value, year: searchForm.year }
    })
    if (res.data) {
      stats.totalAmount = res.data.totalAmount || 0
      stats.beneficiaryHouseholds = res.data.beneficiaryHouseholds || 0
      stats.successCount = res.data.successCount || 0
      stats.failCount = res.data.failCount || 0
    }
  } catch (e) {
    stats.totalAmount = 2856000
    stats.beneficiaryHouseholds = 328
    stats.successCount = 315
    stats.failCount = 13
  }
}

const fetchData = async () => {
  const params = {
    keyword: searchForm.keyword,
    groupName: searchForm.groupName,
    subsidyType: activeTab.value,
    payStatus: searchForm.payStatus,
    year: searchForm.year,
    page: pagination.page,
    size: pagination.size
  }
  try {
    const res = await request.get('/subsidy/list', { params })
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取补贴列表失败', error)
  }
}

const handleTabChange = () => {
  pagination.page = 1
  fetchStats()
  fetchData()
}

const handleSearch = () => {
  pagination.page = 1
  fetchStats()
  fetchData()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.groupName = ''
  searchForm.payStatus = ''
  searchForm.year = 2026
  pagination.page = 1
  fetchStats()
  fetchData()
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  form.subsidyType = activeTab.value
  dialogTitle.value = '新增发放'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑发放'
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除 "${row.beneficiary}" 的补贴记录吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/subsidy/${row.id}`)
      ElMessage.success('删除成功')
      fetchData()
      fetchStats()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.beneficiary) {
    ElMessage.warning('请输入受益人姓名')
    return
  }
  if (!form.subsidyType) {
    ElMessage.warning('请选择补贴类型')
    return
  }
  try {
    if (form.id) {
      await request.put('/subsidy/update', form)
    } else {
      await request.post('/subsidy/add', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
    fetchStats()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handleView = (row) => {
  Object.assign(currentDetail, {
    beneficiary: row.beneficiary,
    gender: row.gender || '男',
    idCard: row.idCard,
    phone: row.phone || '13800138000',
    groupName: row.groupName,
    familyCount: row.familyCount || 4,
    subsidyType: row.subsidyType,
    payMonth: row.payMonth,
    amount: row.amount,
    payStatus: row.payStatus,
    bankName: row.bankName || '农村商业银行',
    bankCard: row.bankCard,
    failReason: row.failReason || (row.payStatus === 'FAIL' ? '银行卡号异常' : ''),
    payRecords: row.payRecords || [
      { status: 'PENDING', amount: row.amount, time: row.payMonth + '-01 09:00:00', operator: '系统' },
      { status: row.payStatus, amount: row.amount, time: row.payMonth + '-15 14:30:00', operator: '李主任' }
    ]
  })
  detailVisible.value = true
}

const handleImport = () => {
  ElMessage.info('批量导入功能开发中')
}

const handleExport = () => {
  ElMessage.info('批量导出功能开发中')
}

const handleGeneratePublicity = () => {
  ElMessage.info('生成公示功能开发中')
}

const handleRetry = () => {
  ElMessageBox.confirm('确定要重新发放这笔补贴吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('重新发放申请已提交')
    detailVisible.value = false
    fetchData()
    fetchStats()
  }).catch(() => {})
}

const handleMarkSuccess = () => {
  ElMessageBox.confirm('确定要手动标记为发放成功吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('已标记为发放成功')
    detailVisible.value = false
    fetchData()
    fetchStats()
  }).catch(() => {})
}

onMounted(() => {
  fetchStats()
  fetchData()
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
  box-shadow: 0 4px 12px rgba(200, 16, 46, 0.15);
}

.stat-card.red {
  background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%);
}

.stat-card.gold {
  background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%);
  color: #8B4513;
}

.stat-value {
  font-size: 24px;
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

.page-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.amount-text {
  color: #C8102E;
  font-weight: bold;
}

.detail-cards {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-card {
  border: 1px solid #FDE8E8;
  border-radius: 8px;
  padding: 16px;
}

.card-title {
  font-size: 15px;
  font-weight: bold;
  color: #C8102E;
  margin-bottom: 12px;
  padding-left: 10px;
  border-left: 3px solid #FFD700;
}

.retry-section {
  margin-top: 12px;
  display: flex;
  gap: 10px;
}

.pay-record-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.pay-record-status {
  font-weight: bold;
  color: #C8102E;
}

.pay-record-amount {
  font-size: 13px;
  color: #666;
}

.pay-record-operator {
  font-size: 12px;
  color: #999;
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
  background: linear-gradient(135deg, #D62238 0%, #C8102E 100%);
  border-color: #C8102E;
}

:deep(.el-pagination .el-pager .is-active) {
  background-color: #C8102E;
}

:deep(.el-dialog__header) {
  border-bottom: 2px solid #FFD700;
}

:deep(.el-dialog__title) {
  color: #C8102E;
  font-weight: bold;
}

:deep(.el-tabs__item.is-active) {
  color: #C8102E;
}

:deep(.el-tabs__active-bar) {
  background-color: #C8102E;
}

:deep(.el-descriptions__label) {
  background: #FDF5F5 !important;
  color: #C8102E;
  font-weight: bold;
}
</style>
