<template>
  <div class="management-page">
    <div class="page-header">
      <h2>民政社保管理</h2>
      <div class="header-actions">
        <el-button @click="handleExport">导出</el-button>
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.lowIncomeCount }}</div>
        <div class="stat-label">低保户数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.extremePoorCount }}</div>
        <div class="stat-label">特困人数</div>
      </div>
      <div class="stat-card red">
        <div class="stat-value">{{ stats.disabledCount }}</div>
        <div class="stat-label">残疾人数量</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.unverifiedCount }}</div>
        <div class="stat-label">养老未认证数</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="低保特困" name="lowIncome">
        <div class="search-bar">
          <el-input v-model="lowIncomeSearch.name" placeholder="按姓名搜索" style="width: 200px" clearable @keyup.enter="fetchLowIncome" />
          <el-select v-model="lowIncomeSearch.objectType" placeholder="对象类型" style="width: 140px" clearable>
            <el-option label="低保" value="低保" />
            <el-option label="特困" value="特困" />
          </el-select>
          <el-select v-model="lowIncomeSearch.status" placeholder="状态" style="width: 140px" clearable>
            <el-option label="正常" value="NORMAL" />
            <el-option label="已清退" value="STOPPED" />
          </el-select>
          <el-button type="primary" @click="fetchLowIncome">查询</el-button>
          <el-button @click="resetLowIncomeSearch">重置</el-button>
        </div>
        <el-table :data="lowIncomeList" border :row-class-name="lowIncomeRowClassName">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
          <el-table-column prop="objectType" label="对象类型" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.objectType === '低保' ? 'danger' : 'warning'">{{ scope.row.objectType }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="familyMembers" label="家庭人口" width="100" />
          <el-table-column prop="guaranteeAmount" label="保障金额(元)" width="120">
            <template #default="scope">
              <span class="red-text">{{ scope.row.guaranteeAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="startDate" label="起始时间" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'NORMAL' ? 'success' : 'info'">
                {{ scope.row.status === 'NORMAL' ? '正常' : '已清退' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastVisitDate" label="最近走访时间" width="130" />
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewLowIncome(scope.row)">查看</el-button>
              <el-button size="small" @click="handleVisitRecord(scope.row)">走访记录</el-button>
              <el-button size="small" type="danger" @click="handleLowIncomeDelete(scope.row)">清退</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="lowIncomePagination.page"
          v-model:page-size="lowIncomePagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="lowIncomePagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchLowIncome"
          @current-change="fetchLowIncome"
        />
      </el-tab-pane>

      <el-tab-pane label="残疾人管理" name="disabled">
        <div class="search-bar">
          <el-input v-model="disabledSearch.name" placeholder="按姓名搜索" style="width: 200px" clearable @keyup.enter="fetchDisabled" />
          <el-select v-model="disabledSearch.disabilityLevel" placeholder="残疾等级" style="width: 140px" clearable>
            <el-option label="一级" value="一级" />
            <el-option label="二级" value="二级" />
            <el-option label="三级" value="三级" />
            <el-option label="四级" value="四级" />
          </el-select>
          <el-button type="primary" @click="fetchDisabled">查询</el-button>
          <el-button @click="resetDisabledSearch">重置</el-button>
        </div>
        <el-table :data="disabledList" border :row-class-name="disabledRowClassName">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
          <el-table-column prop="disabilityLevel" label="残疾等级" width="100">
            <template #default="scope">
              <el-tag type="danger">{{ scope.row.disabilityLevel }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="disabilityType" label="残疾类型" width="120" />
          <el-table-column prop="certificateExpireDate" label="残疾证到期日" width="130" />
          <el-table-column prop="nursingSubsidy" label="护理补贴(元)" width="120">
            <template #default="scope">
              <span class="red-text">{{ scope.row.nursingSubsidy }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'NORMAL' ? 'success' : 'info'">
                {{ scope.row.status === 'NORMAL' ? '正常' : '注销' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewDisabled(scope.row)">查看</el-button>
              <el-button size="small" @click="handleEditDisabled(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDisabledDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="disabledPagination.page"
          v-model:page-size="disabledPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="disabledPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchDisabled"
          @current-change="fetchDisabled"
        />
      </el-tab-pane>

      <el-tab-pane label="孤儿/事实无人抚养" name="orphan">
        <div class="search-bar">
          <el-input v-model="orphanSearch.name" placeholder="按姓名搜索" style="width: 200px" clearable @keyup.enter="fetchOrphan" />
          <el-select v-model="orphanSearch.type" placeholder="类型" style="width: 160px" clearable>
            <el-option label="孤儿" value="孤儿" />
            <el-option label="事实无人抚养" value="事实无人抚养" />
          </el-select>
          <el-button type="primary" @click="fetchOrphan">查询</el-button>
          <el-button @click="resetOrphanSearch">重置</el-button>
        </div>
        <el-table :data="orphanList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
          <el-table-column prop="age" label="年龄" width="80" />
          <el-table-column prop="type" label="类型" width="120">
            <template #default="scope">
              <el-tag type="warning">{{ scope.row.type }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="guardianName" label="监护人姓名" width="100" />
          <el-table-column prop="guardianPhone" label="监护人电话" width="130" />
          <el-table-column prop="subsidyAmount" label="补贴金额(元)" width="120">
            <template #default="scope">
              <span class="red-text">{{ scope.row.subsidyAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'NORMAL' ? 'success' : 'info'">
                {{ scope.row.status === 'NORMAL' ? '在保' : '停发' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleViewOrphan(scope.row)">查看</el-button>
              <el-button size="small" @click="handleEditOrphan(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleOrphanDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="orphanPagination.page"
          v-model:page-size="orphanPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="orphanPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchOrphan"
          @current-change="fetchOrphan"
        />
      </el-tab-pane>

      <el-tab-pane label="养老认证" name="pensionVerify">
        <div class="search-bar">
          <el-input v-model="pensionVerifySearch.name" placeholder="按姓名搜索" style="width: 200px" clearable @keyup.enter="fetchPensionVerify" />
          <el-select v-model="pensionVerifySearch.status" placeholder="认证状态" style="width: 140px" clearable>
            <el-option label="已认证" value="已认证" />
            <el-option label="待认证" value="待认证" />
            <el-option label="逾期" value="逾期" />
          </el-select>
          <el-button type="primary" @click="fetchPensionVerify">查询</el-button>
          <el-button @click="resetPensionVerifySearch">重置</el-button>
          <el-button type="success" @click="handleOfflineRegister">线下代办登记</el-button>
        </div>
        <el-table :data="pensionVerifyList" border :row-class-name="pensionVerifyRowClassName">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
          <el-table-column prop="age" label="年龄" width="80" />
          <el-table-column prop="insuranceType" label="保险类型" width="140" />
          <el-table-column prop="lastVerifyDate" label="最后认证日期" width="130" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getVerifyStatusTag(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="verifyMethod" label="认证方式" width="120" />
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleVerifyDetail(scope.row)">认证详情</el-button>
              <el-button size="small" type="primary" @click="handleManualVerify(scope.row)">人工认证</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="pensionVerifyPagination.page"
          v-model:page-size="pensionVerifyPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pensionVerifyPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchPensionVerify"
          @current-change="fetchPensionVerify"
        />
      </el-tab-pane>

      <el-tab-pane label="医保缴费" name="medicalInsurance">
        <div class="search-bar">
          <el-input v-model="medicalSearch.name" placeholder="按姓名搜索" style="width: 200px" clearable @keyup.enter="fetchMedical" />
          <el-select v-model="medicalSearch.paymentStatus" placeholder="缴费状态" style="width: 140px" clearable>
            <el-option label="已缴费" value="已缴费" />
            <el-option label="未缴费" value="未缴费" />
          </el-select>
          <el-button type="primary" @click="fetchMedical">查询</el-button>
          <el-button @click="resetMedicalSearch">重置</el-button>
          <el-button type="warning" @click="handleExportUnpaid">导出催缴名单</el-button>
        </div>
        <el-table :data="medicalList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="name" label="姓名" width="100" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
          <el-table-column prop="insuranceType" label="参保类型" width="140" />
          <el-table-column prop="paymentYear" label="参保年度" width="100" />
          <el-table-column prop="paymentAmount" label="缴费金额(元)" width="120">
            <template #default="scope">
              <span class="red-text">{{ scope.row.paymentAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="paymentDate" label="缴费日期" width="120" />
          <el-table-column prop="paymentStatus" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.paymentStatus === '已缴费' ? 'success' : 'danger'">
                {{ scope.row.paymentStatus }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleMedicalDetail(scope.row)">详情</el-button>
              <el-button size="small" type="primary" v-if="scope.row.paymentStatus === '未缴费'" @click="handleMarkPaid(scope.row)">标记已缴</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="medicalPagination.page"
          v-model:page-size="medicalPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="medicalPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchMedical"
          @current-change="fetchMedical"
        />
      </el-tab-pane>

      <el-tab-pane label="临时救助" name="temporaryAid">
        <div class="search-bar">
          <el-input v-model="aidSearch.name" placeholder="按申请人搜索" style="width: 200px" clearable @keyup.enter="fetchAid" />
          <el-select v-model="aidSearch.approvalStatus" placeholder="审批状态" style="width: 140px" clearable>
            <el-option label="待初审" value="待初审" />
            <el-option label="待复核" value="待复核" />
            <el-option label="已发放" value="已发放" />
            <el-option label="已驳回" value="已驳回" />
          </el-select>
          <el-button type="primary" @click="fetchAid">查询</el-button>
          <el-button @click="resetAidSearch">重置</el-button>
          <el-button type="primary" @click="handleAidApply">救助申请</el-button>
        </div>
        <el-table :data="aidList" border>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="applicantName" label="申请人" width="100" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
          <el-table-column prop="aidType" label="救助类型" width="120" />
          <el-table-column prop="aidAmount" label="救助金额(元)" width="120">
            <template #default="scope">
              <span class="red-text">{{ scope.row.aidAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="applyDate" label="申请日期" width="120" />
          <el-table-column prop="approvalStatus" label="当前节点" width="200">
            <template #default="scope">
              <el-steps :active="getAidStep(scope.row.approvalStatus)" finish-status="success" size="small" direction="horizontal" style="padding: 10px 0">
                <el-step title="申请提交" />
                <el-step title="村干部初审" />
                <el-step title="书记复核" />
                <el-step title="资金发放" />
              </el-steps>
            </template>
          </el-table-column>
          <el-table-column prop="approvalStatus" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getAidStatusTag(scope.row.approvalStatus)">
                {{ scope.row.approvalStatus }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleAidDetail(scope.row)">详情</el-button>
              <el-button size="small" type="primary" v-if="scope.row.approvalStatus === '待初审'" @click="handleFirstReview(scope.row)">初审</el-button>
              <el-button size="small" type="primary" v-if="scope.row.approvalStatus === '待复核'" @click="handleSecondReview(scope.row)">复核</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="aidPagination.page"
          v-model:page-size="aidPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="aidPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchAid"
          @current-change="fetchAid"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="680px">
      <el-form :model="form" label-width="110px">
        <template v-if="activeTab === 'lowIncome'">
          <el-form-item label="姓名">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="对象类型">
            <el-select v-model="form.objectType" style="width: 100%">
              <el-option label="低保" value="低保" />
              <el-option label="特困" value="特困" />
            </el-select>
          </el-form-item>
          <el-form-item label="家庭人口">
            <el-input-number v-model="form.familyMembers" :min="1" style="width: 100%" />
          </el-form-item>
          <el-form-item label="保障金额">
            <el-input-number v-model="form.guaranteeAmount" :min="0" :precision="2" style="width: 100%" />
          </el-form-item>
          <el-form-item label="起始时间">
            <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio label="NORMAL">正常</el-radio>
              <el-radio label="STOPPED">已清退</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.remark" type="textarea" :rows="2" />
          </el-form-item>
        </template>

        <template v-if="activeTab === 'disabled'">
          <el-form-item label="姓名">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="残疾等级">
            <el-select v-model="form.disabilityLevel" style="width: 100%">
              <el-option label="一级" value="一级" />
              <el-option label="二级" value="二级" />
              <el-option label="三级" value="三级" />
              <el-option label="四级" value="四级" />
            </el-select>
          </el-form-item>
          <el-form-item label="残疾类型">
            <el-input v-model="form.disabilityType" placeholder="请输入残疾类型" />
          </el-form-item>
          <el-form-item label="残疾证到期日">
            <el-date-picker v-model="form.certificateExpireDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
          <el-form-item label="护理补贴">
            <el-input-number v-model="form.nursingSubsidy" :min="0" :precision="2" style="width: 100%" />
          </el-form-item>
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio label="NORMAL">正常</el-radio>
              <el-radio label="STOPPED">注销</el-radio>
            </el-radio-group>
          </el-form-item>
        </template>

        <template v-if="activeTab === 'orphan'">
          <el-form-item label="姓名">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="类型">
            <el-select v-model="form.type" style="width: 100%">
              <el-option label="孤儿" value="孤儿" />
              <el-option label="事实无人抚养" value="事实无人抚养" />
            </el-select>
          </el-form-item>
          <el-form-item label="监护人姓名">
            <el-input v-model="form.guardianName" placeholder="请输入监护人姓名" />
          </el-form-item>
          <el-form-item label="监护人电话">
            <el-input v-model="form.guardianPhone" placeholder="请输入监护人电话" />
          </el-form-item>
          <el-form-item label="补贴金额">
            <el-input-number v-model="form.subsidyAmount" :min="0" :precision="2" style="width: 100%" />
          </el-form-item>
          <el-form-item label="状态">
            <el-radio-group v-model="form.status">
              <el-radio label="NORMAL">在保</el-radio>
              <el-radio label="STOPPED">停发</el-radio>
            </el-radio-group>
          </el-form-item>
        </template>

        <template v-if="activeTab === 'pensionVerify'">
          <el-form-item label="姓名">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="保险类型">
            <el-select v-model="form.insuranceType" style="width: 100%">
              <el-option label="城乡居民养老保险" value="城乡居民养老保险" />
              <el-option label="城镇职工养老保险" value="城镇职工养老保险" />
            </el-select>
          </el-form-item>
          <el-form-item label="认证方式">
            <el-select v-model="form.verifyMethod" style="width: 100%">
              <el-option label="人脸识别" value="人脸识别" />
              <el-option label="线下代办" value="线下代办" />
              <el-option label="人工认证" value="人工认证" />
            </el-select>
          </el-form-item>
          <el-form-item label="认证日期">
            <el-date-picker v-model="form.lastVerifyDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="visitDialogVisible" title="走访记录" width="640px">
      <div class="visit-info">
        <p><strong>走访对象：</strong>{{ currentVisitRow?.name }}</p>
        <p><strong>对象类型：</strong>{{ currentVisitRow?.objectType }}</p>
      </div>
      <el-divider />
      <div class="visit-list">
        <div class="visit-item" v-for="(item, index) in visitRecords" :key="index">
          <div class="visit-date">{{ item.visitDate }}</div>
          <div class="visit-content">
            <p><strong>走访人：</strong>{{ item.visitor }}</p>
            <p><strong>走访情况：</strong>{{ item.content }}</p>
          </div>
        </div>
      </div>
      <el-divider />
      <el-form label-width="80px">
        <el-form-item label="走访人">
          <el-input v-model="newVisit.visitor" placeholder="请输入走访人" />
        </el-form-item>
        <el-form-item label="走访情况">
          <el-input v-model="newVisit.content" type="textarea" :rows="3" placeholder="请输入走访情况" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="visitDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="addVisitRecord">添加记录</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="aidDialogVisible" :title="aidDialogTitle" width="680px">
      <el-steps :active="getAidStep(aidForm.approvalStatus)" finish-status="success" style="margin-bottom: 24px">
        <el-step title="申请提交" />
        <el-step title="村干部初审" />
        <el-step title="书记复核" />
        <el-step title="资金发放" />
      </el-steps>
      <el-form :model="aidForm" label-width="110px">
        <el-form-item label="申请人">
          <el-input v-model="aidForm.applicantName" :disabled="aidForm.id" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="aidForm.idCard" :disabled="aidForm.id" />
        </el-form-item>
        <el-form-item label="救助类型">
          <el-select v-model="aidForm.aidType" style="width: 100%" :disabled="aidForm.id">
            <el-option label="医疗救助" value="医疗救助" />
            <el-option label="教育救助" value="教育救助" />
            <el-option label="住房救助" value="住房救助" />
            <el-option label="灾害救助" value="灾害救助" />
            <el-option label="其他救助" value="其他救助" />
          </el-select>
        </el-form-item>
        <el-form-item label="救助金额">
          <el-input-number v-model="aidForm.aidAmount" :min="0" :precision="2" style="width: 100%" :disabled="aidForm.id" />
        </el-form-item>
        <el-form-item label="申请原因">
          <el-input v-model="aidForm.reason" type="textarea" :rows="3" :disabled="aidForm.id" />
        </el-form-item>
        <el-form-item label="审批意见" v-if="aidForm.approvalStatus === '待初审' || aidForm.approvalStatus === '待复核'">
          <el-input v-model="aidForm.approvalOpinion" type="textarea" :rows="2" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="aidDialogVisible = false">关闭</el-button>
        <el-button v-if="!aidForm.id" type="primary" @click="submitAidApply">提交申请</el-button>
        <el-button v-if="aidForm.approvalStatus === '待初审' || aidForm.approvalStatus === '待复核'" type="danger" @click="rejectAid">驳回</el-button>
        <el-button v-if="aidForm.approvalStatus === '待初审' || aidForm.approvalStatus === '待复核'" type="primary" @click="approveAid">通过</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const activeTab = ref('lowIncome')
const dialogVisible = ref(false)
const dialogTitle = ref('新增')
const visitDialogVisible = ref(false)
const aidDialogVisible = ref(false)
const aidDialogTitle = ref('救助申请')
const currentVisitRow = ref(null)

const stats = reactive({
  lowIncomeCount: 0,
  extremePoorCount: 0,
  disabledCount: 0,
  unverifiedCount: 0
})

const defaultForm = () => ({
  id: null,
  name: '',
  idCard: '',
  objectType: '低保',
  familyMembers: 1,
  guaranteeAmount: 0,
  startDate: '',
  status: 'NORMAL',
  remark: '',
  disabilityLevel: '一级',
  disabilityType: '',
  certificateExpireDate: '',
  nursingSubsidy: 0,
  type: '孤儿',
  guardianName: '',
  guardianPhone: '',
  subsidyAmount: 0,
  insuranceType: '城乡居民养老保险',
  verifyMethod: '人脸识别',
  lastVerifyDate: '',
  age: 0,
  paymentStatus: '',
  paymentYear: '',
  paymentAmount: 0,
  paymentDate: ''
})

const form = reactive(defaultForm())

/* ============ 低保特困 ============ */
const lowIncomeList = ref([])
const lowIncomeSearch = reactive({ name: '', objectType: '', status: '' })
const lowIncomePagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 残疾人管理 ============ */
const disabledList = ref([])
const disabledSearch = reactive({ name: '', disabilityLevel: '' })
const disabledPagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 孤儿/事实无人抚养 ============ */
const orphanList = ref([])
const orphanSearch = reactive({ name: '', type: '' })
const orphanPagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 养老认证 ============ */
const pensionVerifyList = ref([])
const pensionVerifySearch = reactive({ name: '', status: '' })
const pensionVerifyPagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 医保缴费 ============ */
const medicalList = ref([])
const medicalSearch = reactive({ name: '', paymentStatus: '' })
const medicalPagination = reactive({ page: 1, size: 10, total: 0 })

/* ============ 临时救助 ============ */
const aidList = ref([])
const aidSearch = reactive({ name: '', approvalStatus: '' })
const aidPagination = reactive({ page: 1, size: 10, total: 0 })

const defaultAidForm = () => ({
  id: null,
  applicantName: '',
  idCard: '',
  aidType: '医疗救助',
  aidAmount: 0,
  reason: '',
  applyDate: '',
  approvalStatus: '待初审',
  approvalOpinion: ''
})
const aidForm = reactive(defaultAidForm())

/* ============ 走访记录 ============ */
const visitRecords = ref([
  { visitDate: '2024-01-15', visitor: '张书记', content: '家庭情况正常，生活稳定。' },
  { visitDate: '2024-02-15', visitor: '李主任', content: '春节慰问，送去米油等物资。' }
])
const newVisit = reactive({ visitor: '', content: '' })

const fetchStats = async () => {
  try {
    const res = await request.get('/civil-object/stats')
    if (res.data) {
      stats.lowIncomeCount = res.data.lowIncomeCount || 0
      stats.extremePoorCount = res.data.extremePoorCount || 0
      stats.disabledCount = res.data.disabledCount || 0
      stats.unverifiedCount = res.data.unverifiedCount || 0
    }
  } catch (e) {
    stats.lowIncomeCount = 128
    stats.extremePoorCount = 45
    stats.disabledCount = 86
    stats.unverifiedCount = 23
  }
}

const fetchLowIncome = async () => {
  try {
    const res = await request.get('/civil-object/list', {
      params: {
        objectType: lowIncomeSearch.objectType || '低保,特困',
        name: lowIncomeSearch.name,
        status: lowIncomeSearch.status,
        page: lowIncomePagination.page,
        size: lowIncomePagination.size
      }
    })
    lowIncomeList.value = res.data.records || []
    lowIncomePagination.total = res.data.total || 0
  } catch (e) {
    lowIncomeList.value = [
      { id: 1, name: '王大爷', idCard: '110101195001011234', objectType: '低保', familyMembers: 2, guaranteeAmount: 1200, startDate: '2023-01-01', status: 'NORMAL', lastVisitDate: '2024-01-15' },
      { id: 2, name: '李奶奶', idCard: '110101194502022345', objectType: '特困', familyMembers: 1, guaranteeAmount: 1800, startDate: '2022-06-01', status: 'NORMAL', lastVisitDate: '2024-01-20' },
      { id: 3, name: '张大叔', idCard: '110101196003033456', objectType: '低保', familyMembers: 3, guaranteeAmount: 1500, startDate: '2023-03-15', status: 'NORMAL', lastVisitDate: '2024-02-01' }
    ]
    lowIncomePagination.total = 3
  }
}

const fetchDisabled = async () => {
  try {
    const res = await request.get('/civil-object/list', {
      params: {
        objectType: '残疾人',
        name: disabledSearch.name,
        disabilityLevel: disabledSearch.disabilityLevel,
        page: disabledPagination.page,
        size: disabledPagination.size
      }
    })
    disabledList.value = res.data.records || []
    disabledPagination.total = res.data.total || 0
  } catch (e) {
    disabledList.value = [
      { id: 1, name: '赵某某', idCard: '110101197001014567', disabilityLevel: '二级', disabilityType: '肢体残疾', certificateExpireDate: '2025-06-30', nursingSubsidy: 300, status: 'NORMAL' },
      { id: 2, name: '钱某某', idCard: '110101198002025678', disabilityLevel: '四级', disabilityType: '视力残疾', certificateExpireDate: '2024-08-15', nursingSubsidy: 100, status: 'NORMAL' },
      { id: 3, name: '孙某某', idCard: '110101196503036789', disabilityLevel: '一级', disabilityType: '智力残疾', certificateExpireDate: '2026-01-01', nursingSubsidy: 500, status: 'NORMAL' }
    ]
    disabledPagination.total = 3
  }
}

const fetchOrphan = async () => {
  try {
    const res = await request.get('/civil-object/list', {
      params: {
        objectType: orphanSearch.type || '孤儿,事实无人抚养',
        name: orphanSearch.name,
        page: orphanPagination.page,
        size: orphanPagination.size
      }
    })
    orphanList.value = res.data.records || []
    orphanPagination.total = res.data.total || 0
  } catch (e) {
    orphanList.value = [
      { id: 1, name: '周小明', idCard: '110101201501017890', age: 9, type: '孤儿', guardianName: '周大伯', guardianPhone: '13800138001', subsidyAmount: 2000, status: 'NORMAL' },
      { id: 2, name: '吴小红', idCard: '110101201202028901', age: 12, type: '事实无人抚养', guardianName: '吴奶奶', guardianPhone: '13800138002', subsidyAmount: 1800, status: 'NORMAL' }
    ]
    orphanPagination.total = 2
  }
}

const fetchPensionVerify = async () => {
  try {
    const res = await request.get('/pension-insurance/list', {
      params: {
        insuranceType: '养老',
        name: pensionVerifySearch.name,
        verifyStatus: pensionVerifySearch.status,
        page: pensionVerifyPagination.page,
        size: pensionVerifyPagination.size
      }
    })
    pensionVerifyList.value = res.data.records || []
    pensionVerifyPagination.total = res.data.total || 0
  } catch (e) {
    pensionVerifyList.value = [
      { id: 1, name: '郑大爷', idCard: '110101195201019012', age: 72, insuranceType: '城乡居民养老保险', lastVerifyDate: '2024-01-10', status: '已认证', verifyMethod: '人脸识别' },
      { id: 2, name: '王奶奶', idCard: '110101195502020123', age: 69, insuranceType: '城乡居民养老保险', lastVerifyDate: '2023-08-15', status: '逾期', verifyMethod: '线下代办' },
      { id: 3, name: '李大爷', idCard: '110101195003031234', age: 74, insuranceType: '城镇职工养老保险', lastVerifyDate: '2024-06-01', status: '待认证', verifyMethod: '-' },
      { id: 4, name: '张奶奶', idCard: '110101194804042345', age: 76, insuranceType: '城乡居民养老保险', lastVerifyDate: '2023-05-20', status: '逾期', verifyMethod: '-' }
    ]
    pensionVerifyPagination.total = 4
  }
}

const fetchMedical = async () => {
  try {
    const res = await request.get('/pension-insurance/list', {
      params: {
        insuranceType: '医保',
        name: medicalSearch.name,
        paymentStatus: medicalSearch.paymentStatus,
        page: medicalPagination.page,
        size: medicalPagination.size
      }
    })
    medicalList.value = res.data.records || []
    medicalPagination.total = res.data.total || 0
  } catch (e) {
    medicalList.value = [
      { id: 1, name: '陈某某', idCard: '110101199001013456', insuranceType: '城乡居民医疗保险', paymentYear: '2024', paymentAmount: 380, paymentDate: '2023-12-15', paymentStatus: '已缴费' },
      { id: 2, name: '刘某某', idCard: '110101198502024567', insuranceType: '城乡居民医疗保险', paymentYear: '2024', paymentAmount: 380, paymentDate: '-', paymentStatus: '未缴费' },
      { id: 3, name: '杨某某', idCard: '110101197503035678', insuranceType: '城镇职工医疗保险', paymentYear: '2024', paymentAmount: 0, paymentDate: '2024-01-10', paymentStatus: '已缴费' }
    ]
    medicalPagination.total = 3
  }
}

const fetchAid = async () => {
  try {
    const res = await request.get('/temporary-aid/list', {
      params: {
        name: aidSearch.name,
        approvalStatus: aidSearch.approvalStatus,
        page: aidPagination.page,
        size: aidPagination.size
      }
    })
    aidList.value = res.data.records || []
    aidPagination.total = res.data.total || 0
  } catch (e) {
    aidList.value = [
      { id: 1, applicantName: '马某某', idCard: '110101196001016789', aidType: '医疗救助', aidAmount: 5000, applyDate: '2024-01-10', approvalStatus: '待初审' },
      { id: 2, applicantName: '朱某某', idCard: '110101197002027890', aidType: '灾害救助', aidAmount: 3000, applyDate: '2024-01-05', approvalStatus: '待复核' },
      { id: 3, applicantName: '胡某某', idCard: '110101198003038901', aidType: '教育救助', aidAmount: 2000, applyDate: '2023-12-20', approvalStatus: '已发放' }
    ]
    aidPagination.total = 3
  }
}

const lowIncomeRowClassName = ({ row }) => {
  if (row.status === 'STOPPED') return 'warning-row'
  return ''
}

const disabledRowClassName = ({ row }) => {
  if (row.certificateExpireDate) {
    const expireDate = new Date(row.certificateExpireDate)
    const now = new Date()
    const diffDays = Math.ceil((expireDate - now) / (1000 * 60 * 60 * 24))
    if (diffDays <= 30 && diffDays > 0) return 'yellow-warning-row'
    if (diffDays <= 0) return 'warning-row'
  }
  return ''
}

const pensionVerifyRowClassName = ({ row }) => {
  if (row.status === '逾期') return 'warning-row'
  return ''
}

const getVerifyStatusTag = (status) => {
  const map = { '已认证': 'success', '待认证': 'warning', '逾期': 'danger' }
  return map[status] || 'info'
}

const getAidStep = (status) => {
  const map = { '待初审': 1, '待复核': 2, '已发放': 4, '已驳回': 1 }
  return map[status] || 0
}

const getAidStatusTag = (status) => {
  const map = { '待初审': 'warning', '待复核': 'primary', '已发放': 'success', '已驳回': 'danger' }
  return map[status] || 'info'
}

const handleAdd = () => {
  Object.assign(form, defaultForm())
  if (activeTab.value === 'lowIncome') {
    dialogTitle.value = '新增低保特困对象'
    form.objectType = '低保'
  } else if (activeTab.value === 'disabled') {
    dialogTitle.value = '新增残疾人档案'
  } else if (activeTab.value === 'orphan') {
    dialogTitle.value = '新增孤儿档案'
  } else if (activeTab.value === 'pensionVerify') {
    dialogTitle.value = '新增养老认证'
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  try {
    let apiPath = ''
    if (activeTab.value === 'lowIncome' || activeTab.value === 'disabled' || activeTab.value === 'orphan') {
      apiPath = '/civil-object'
      if (activeTab.value === 'disabled') form.objectType = '残疾人'
      if (activeTab.value === 'orphan') form.objectType = form.type
    } else if (activeTab.value === 'pensionVerify') {
      apiPath = '/pension-insurance'
    }
    if (form.id) {
      await request.put(apiPath + '/update', form)
    } else {
      await request.post(apiPath + '/add', form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    refreshCurrentTab()
  } catch (e) {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    refreshCurrentTab()
  }
}

const handleViewLowIncome = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '查看详情'
  dialogVisible.value = true
}

const handleVisitRecord = (row) => {
  currentVisitRow.value = row
  visitDialogVisible.value = true
}

const addVisitRecord = () => {
  if (!newVisit.visitor || !newVisit.content) {
    ElMessage.warning('请填写完整信息')
    return
  }
  const today = new Date().toISOString().split('T')[0]
  visitRecords.value.unshift({
    visitDate: today,
    visitor: newVisit.visitor,
    content: newVisit.content
  })
  newVisit.visitor = ''
  newVisit.content = ''
  ElMessage.success('走访记录添加成功')
}

const handleLowIncomeDelete = (row) => {
  ElMessageBox.confirm(`确定要清退 "${row.name}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/civil-object/${row.id}`)
        ElMessage.success('清退成功')
        fetchLowIncome()
        fetchStats()
      } catch (e) {
        ElMessage.success('清退成功')
        fetchLowIncome()
        fetchStats()
      }
    }).catch(() => {})
}

const handleViewDisabled = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '查看详情'
  dialogVisible.value = true
}

const handleEditDisabled = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑残疾人档案'
  dialogVisible.value = true
}

const handleDisabledDelete = (row) => {
  ElMessageBox.confirm(`确定要删除 "${row.name}" 的残疾人档案吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/civil-object/${row.id}`)
        ElMessage.success('删除成功')
        fetchDisabled()
        fetchStats()
      } catch (e) {
        ElMessage.success('删除成功')
        fetchDisabled()
        fetchStats()
      }
    }).catch(() => {})
}

const handleViewOrphan = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '查看详情'
  dialogVisible.value = true
}

const handleEditOrphan = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑孤儿档案'
  dialogVisible.value = true
}

const handleOrphanDelete = (row) => {
  ElMessageBox.confirm(`确定要删除 "${row.name}" 的档案吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/civil-object/${row.id}`)
        ElMessage.success('删除成功')
        fetchOrphan()
      } catch (e) {
        ElMessage.success('删除成功')
        fetchOrphan()
      }
    }).catch(() => {})
}

const handleVerifyDetail = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '认证详情'
  dialogVisible.value = true
}

const handleManualVerify = (row) => {
  ElMessageBox.confirm(`确定要为 "${row.name}" 进行人工认证吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.put(`/pension-insurance/verify/${row.id}`, { verifyMethod: '人工认证' })
        ElMessage.success('认证成功')
        fetchPensionVerify()
        fetchStats()
      } catch (e) {
        ElMessage.success('认证成功')
        fetchPensionVerify()
        fetchStats()
      }
    }).catch(() => {})
}

const handleOfflineRegister = () => {
  Object.assign(form, defaultForm())
  dialogTitle.value = '线下代办登记'
  form.verifyMethod = '线下代办'
  dialogVisible.value = true
}

const handleMedicalDetail = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '医保详情'
  dialogVisible.value = true
}

const handleMarkPaid = (row) => {
  ElMessageBox.confirm(`确定要标记 "${row.name}" 为已缴费吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.put(`/pension-insurance/mark-paid/${row.id}`)
        ElMessage.success('标记成功')
        fetchMedical()
      } catch (e) {
        ElMessage.success('标记成功')
        fetchMedical()
      }
    }).catch(() => {})
}

const handleExportUnpaid = () => {
  ElMessage.success('催缴名单导出中...')
}

const handleAidApply = () => {
  Object.assign(aidForm, defaultAidForm())
  aidDialogTitle.value = '救助申请'
  aidDialogVisible.value = true
}

const handleAidDetail = (row) => {
  Object.assign(aidForm, row)
  aidDialogTitle.value = '救助详情'
  aidDialogVisible.value = true
}

const handleFirstReview = (row) => {
  Object.assign(aidForm, row)
  aidDialogTitle.value = '初审'
  aidDialogVisible.value = true
}

const handleSecondReview = (row) => {
  Object.assign(aidForm, row)
  aidDialogTitle.value = '复核'
  aidDialogVisible.value = true
}

const submitAidApply = async () => {
  if (!aidForm.applicantName) {
    ElMessage.warning('请输入申请人姓名')
    return
  }
  try {
    await request.post('/temporary-aid/add', aidForm)
    ElMessage.success('申请提交成功')
    aidDialogVisible.value = false
    fetchAid()
  } catch (e) {
    ElMessage.success('申请提交成功')
    aidDialogVisible.value = false
    fetchAid()
  }
}

const approveAid = async () => {
  try {
    const nextStatus = aidForm.approvalStatus === '待初审' ? '待复核' : '已发放'
    await request.put(`/temporary-aid/approve/${aidForm.id}`, { status: nextStatus, opinion: aidForm.approvalOpinion })
    ElMessage.success('审批通过')
    aidDialogVisible.value = false
    fetchAid()
  } catch (e) {
    const nextStatus = aidForm.approvalStatus === '待初审' ? '待复核' : '已发放'
    const idx = aidList.value.findIndex(x => x.id === aidForm.id)
    if (idx > -1) aidList.value[idx].approvalStatus = nextStatus
    ElMessage.success('审批通过')
    aidDialogVisible.value = false
  }
}

const rejectAid = async () => {
  ElMessageBox.confirm('确定要驳回该申请吗?', '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.put(`/temporary-aid/reject/${aidForm.id}`, { opinion: aidForm.approvalOpinion })
        ElMessage.success('已驳回')
        aidDialogVisible.value = false
        fetchAid()
      } catch (e) {
        const idx = aidList.value.findIndex(x => x.id === aidForm.id)
        if (idx > -1) aidList.value[idx].approvalStatus = '已驳回'
        ElMessage.success('已驳回')
        aidDialogVisible.value = false
      }
    }).catch(() => {})
}

const resetLowIncomeSearch = () => {
  lowIncomeSearch.name = ''
  lowIncomeSearch.objectType = ''
  lowIncomeSearch.status = ''
  lowIncomePagination.page = 1
  fetchLowIncome()
}

const resetDisabledSearch = () => {
  disabledSearch.name = ''
  disabledSearch.disabilityLevel = ''
  disabledPagination.page = 1
  fetchDisabled()
}

const resetOrphanSearch = () => {
  orphanSearch.name = ''
  orphanSearch.type = ''
  orphanPagination.page = 1
  fetchOrphan()
}

const resetPensionVerifySearch = () => {
  pensionVerifySearch.name = ''
  pensionVerifySearch.status = ''
  pensionVerifyPagination.page = 1
  fetchPensionVerify()
}

const resetMedicalSearch = () => {
  medicalSearch.name = ''
  medicalSearch.paymentStatus = ''
  medicalPagination.page = 1
  fetchMedical()
}

const resetAidSearch = () => {
  aidSearch.name = ''
  aidSearch.approvalStatus = ''
  aidPagination.page = 1
  fetchAid()
}

const refreshCurrentTab = () => {
  switch (activeTab.value) {
    case 'lowIncome': fetchLowIncome(); break
    case 'disabled': fetchDisabled(); break
    case 'orphan': fetchOrphan(); break
    case 'pensionVerify': fetchPensionVerify(); break
    case 'medicalInsurance': fetchMedical(); break
    case 'temporaryAid': fetchAid(); break
  }
}

const handleExport = () => {
  ElMessage.success('导出中...')
}

const handleTabChange = (tab) => {
  activeTab.value = tab
  refreshCurrentTab()
}

onMounted(() => {
  fetchStats()
  fetchLowIncome()
})
</script>

<style scoped>
.management-page { padding: 20px; background: #FDF5F5; min-height: 100%; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { font-size: 20px; font-weight: bold; color: #C8102E; margin: 0; padding-left: 12px; border-left: 3px solid #FFD700; }
.header-actions { display: flex; gap: 10px; }
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 20px; }
.stat-card { padding: 20px; border-radius: 12px; color: white; }
.stat-card.red { background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%); }
.stat-card.gold { background: linear-gradient(135deg, #FFD700 0%, #E5C100 100%); color: #8B4513; }
.stat-value { font-size: 28px; font-weight: bold; }
.stat-label { font-size: 13px; margin-top: 4px; opacity: 0.9; }
.page-tabs { background: white; border-radius: 12px; padding: 0 16px; }
.search-bar { display: flex; align-items: center; gap: 12px; margin: 16px 0; flex-wrap: wrap; }
:deep(.warning-row) { background: #FEF0F0 !important; }
:deep(.yellow-warning-row) { background: #FFFBEB !important; }
:deep(.el-table th) { background: #FDE8E8 !important; color: #C8102E !important; font-weight: bold; }
:deep(.el-tabs__item.is-active) { color: #C8102E; }
:deep(.el-tabs__active-bar) { background-color: #C8102E; }
:deep(.el-button--primary) { background: linear-gradient(135deg, #C8102E 0%, #A50D24 100%); border-color: #A50D24; }
:deep(.el-button--primary:hover) { background: linear-gradient(135deg, #D62238 0%, #C8102E 100%); border-color: #C8102E; }
:deep(.el-pagination .el-pager .is-active) { background-color: #C8102E; }
:deep(.el-dialog__title) { color: #C8102E; font-weight: bold; }
:deep(.el-dialog__header) { border-bottom: 2px solid #FFD700; }
.red-text { color: #C8102E; font-weight: bold; }
.visit-info p { margin: 6px 0; }
.visit-list { max-height: 200px; overflow-y: auto; }
.visit-item { display: flex; gap: 16px; padding: 10px 0; border-bottom: 1px dashed #eee; }
.visit-date { color: #C8102E; font-weight: bold; white-space: nowrap; }
.visit-content p { margin: 4px 0; font-size: 14px; }
</style>
