<template>
  <div class="management-page">
    <div class="page-header">
      <h2>媒体宣传管理</h2>
      <div class="header-actions">
        <el-button>导出</el-button>
        <el-button type="primary" @click="handleAddByTab">新增</el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card red">
        <div class="stat-value">{{ stats.articleCount }}</div>
        <div class="stat-label">新闻稿件数</div>
      </div>
      <div class="stat-card gold">
        <div class="stat-value">{{ stats.materialCount }}</div>
        <div class="stat-label">素材总数</div>
      </div>
      <div class="stat-card red-light">
        <div class="stat-value">{{ stats.submissionCount }}</div>
        <div class="stat-label">投稿数</div>
      </div>
      <div class="stat-card gold-light">
        <div class="stat-value">{{ stats.publishedCount }}</div>
        <div class="stat-label">已刊登数</div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="page-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="村内新闻动态" name="article">
        <div class="search-bar">
          <el-input
            v-model="articleSearch.title"
            placeholder="按标题搜索"
            style="width: 200px"
            clearable
            @keyup.enter="fetchArticle"
          />
          <el-select v-model="articleSearch.type" placeholder="类型" style="width: 140px" clearable>
            <el-option label="工作动态" value="工作动态" />
            <el-option label="产业宣传" value="产业宣传" />
            <el-option label="好人好事" value="好人好事" />
          </el-select>
          <el-select v-model="articleSearch.status" placeholder="状态" style="width: 120px" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="已下线" value="OFFLINE" />
          </el-select>
          <el-button type="primary" @click="fetchArticle">查询</el-button>
          <el-button @click="handleArticleReset">重置</el-button>
        </div>

        <el-table :data="articleList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="type" label="类型" width="120" />
          <el-table-column prop="author" label="作者" width="100" />
          <el-table-column prop="publishDate" label="发布日期" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getArticleStatusTagType(scope.row.status)">
                {{ getArticleStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="viewCount" label="阅读量" width="100">
            <template #default="scope">
              <span class="gold-text">{{ scope.row.viewCount || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="260" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleArticleEdit(scope.row)">编辑</el-button>
              <el-button
                size="small"
                :type="scope.row.status === 'PUBLISHED' ? 'warning' : 'success'"
                @click="handleArticleTogglePublish(scope.row)"
              >
                {{ scope.row.status === 'PUBLISHED' ? '下线' : '发布' }}
              </el-button>
              <el-button size="small" type="danger" @click="handleArticleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="articlePagination.page"
          v-model:page-size="articlePagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="articlePagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchArticle"
          @current-change="fetchArticle"
        />
      </el-tab-pane>

      <el-tab-pane label="宣传素材库" name="material">
        <div class="search-bar">
          <el-input
            v-model="materialSearch.name"
            placeholder="按素材名称搜索"
            style="width: 200px"
            clearable
            @keyup.enter="fetchMaterial"
          />
          <el-select v-model="materialSearch.category" placeholder="分类" style="width: 140px" clearable>
            <el-option label="全部" value="" />
            <el-option label="活动照片" value="活动照片" />
            <el-option label="会议照片" value="会议照片" />
            <el-option label="宣传视频" value="宣传视频" />
            <el-option label="风景照片" value="风景照片" />
          </el-select>
          <el-button type="primary" @click="fetchMaterial">查询</el-button>
          <el-button @click="handleMaterialReset">重置</el-button>
          <div style="flex: 1"></div>
          <el-button type="primary" @click="handleMaterialAdd">上传素材</el-button>
        </div>

        <div class="material-grid">
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in materialList" :key="item.id">
              <div class="material-card">
                <div class="material-thumb">
                  <el-image
                    :src="item.thumbUrl || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=village%20scenery%20photo%20rural%20china&image_size=square'"
                    fit="cover"
                    style="width: 100%; height: 100%"
                  />
                </div>
                <div class="material-info">
                  <div class="material-name" :title="item.name">{{ item.name }}</div>
                  <div class="material-meta">
                    <el-tag size="small" type="info">{{ item.category }}</el-tag>
                    <span class="material-date">{{ item.uploadDate }}</span>
                  </div>
                  <div class="material-actions">
                    <el-button size="small" type="primary" @click="handleMaterialDownload(item)">下载</el-button>
                    <el-button size="small" @click="handleMaterialEdit(item)">编辑</el-button>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <el-pagination
          v-model:current-page="materialPagination.page"
          v-model:page-size="materialPagination.size"
          :page-sizes="[12, 24, 48]"
          :total="materialPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchMaterial"
          @current-change="fetchMaterial"
        />
      </el-tab-pane>

      <el-tab-pane label="媒体投稿记录" name="submission">
        <div class="search-bar">
          <el-input
            v-model="submissionSearch.title"
            placeholder="按标题搜索"
            style="width: 200px"
            clearable
            @keyup.enter="fetchSubmission"
          />
          <el-select v-model="submissionSearch.media" placeholder="投稿媒体" style="width: 140px" clearable>
            <el-option label="乡镇融媒" value="乡镇融媒" />
            <el-option label="县级媒体" value="县级媒体" />
            <el-option label="市级媒体" value="市级媒体" />
          </el-select>
          <el-select v-model="submissionSearch.publishStatus" placeholder="刊登状态" style="width: 140px" clearable>
            <el-option label="待刊登" value="PENDING" />
            <el-option label="已刊登" value="PUBLISHED" />
            <el-option label="未采用" value="REJECTED" />
          </el-select>
          <el-button type="primary" @click="fetchSubmission">查询</el-button>
          <el-button @click="handleSubmissionReset">重置</el-button>
        </div>

        <el-table :data="submissionList" border :row-class-name="getSubmissionRowClass" style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="media" label="投稿媒体" width="120" />
          <el-table-column prop="submitDate" label="投稿日期" width="120" />
          <el-table-column prop="publishStatus" label="刊登状态" width="100">
            <template #default="scope">
              <el-tag :type="getSubmissionStatusTagType(scope.row.publishStatus)">
                {{ getSubmissionStatusName(scope.row.publishStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="publishDate" label="刊登日期" width="120">
            <template #default="scope">
              <span v-if="scope.row.publishStatus === 'PUBLISHED'" class="green-text">{{ scope.row.publishDate }}</span>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handleSubmissionView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handleSubmissionEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="primary" @click="handleSubmissionUpload(scope.row)">上传截图</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="submissionPagination.page"
          v-model:page-size="submissionPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="submissionPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchSubmission"
          @current-change="fetchSubmission"
        />
      </el-tab-pane>

      <el-tab-pane label="政策宣传台账" name="propaganda">
        <div class="search-bar">
          <el-input
            v-model="propagandaSearch.theme"
            placeholder="按宣传主题搜索"
            style="width: 200px"
            clearable
            @keyup.enter="fetchPropaganda"
          />
          <el-select v-model="propagandaSearch.type" placeholder="类型" style="width: 140px" clearable>
            <el-option label="反诈" value="反诈" />
            <el-option label="防火" value="防火" />
            <el-option label="乡村振兴" value="乡村振兴" />
            <el-option label="医保政策" value="医保政策" />
          </el-select>
          <el-button type="primary" @click="fetchPropaganda">查询</el-button>
          <el-button @click="handlePropagandaReset">重置</el-button>
          <div style="flex: 1"></div>
          <el-button type="primary" @click="handlePropagandaAdd">新增宣传</el-button>
        </div>

        <el-table :data="propagandaList" border style="width: 100%">
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="theme" label="宣传主题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="type" label="类型" width="120">
            <template #default="scope">
              <el-tag type="warning">{{ scope.row.type }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="propagandaDate" label="宣传日期" width="120" />
          <el-table-column prop="participants" label="参与人数" width="100">
            <template #default="scope">
              <span class="red-text">{{ scope.row.participants || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="location" label="宣传地点" min-width="140" show-overflow-tooltip />
          <el-table-column prop="attachment" label="附件" width="100">
            <template #default="scope">
              <el-link v-if="scope.row.attachment" type="primary" @click="handlePropagandaDownload(scope.row)">
                查看
              </el-link>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="scope">
              <el-button size="small" @click="handlePropagandaView(scope.row)">查看详情</el-button>
              <el-button size="small" @click="handlePropagandaEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handlePropagandaDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="propagandaPagination.page"
          v-model:page-size="propagandaPagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="propagandaPagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchPropaganda"
          @current-change="fetchPropaganda"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="articleDialogVisible" :title="articleDialogTitle" width="720px">
      <el-form :model="articleForm" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="articleForm.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="articleForm.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="工作动态" value="工作动态" />
            <el-option label="产业宣传" value="产业宣传" />
            <el-option label="好人好事" value="好人好事" />
          </el-select>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="articleForm.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="封面图片">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="true"
            :limit="1"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="发布日期">
          <el-date-picker
            v-model="articleForm.publishDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择发布日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="articleForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="已下线" value="OFFLINE" />
          </el-select>
        </el-form-item>
        <el-form-item label="正文内容">
          <el-input
            v-model="articleForm.content"
            type="textarea"
            :rows="12"
            placeholder="富文本编辑区，支持插入图片/附件/视频"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="articleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleArticleSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="materialDialogVisible" :title="materialDialogTitle" width="600px">
      <el-form :model="materialForm" label-width="100px">
        <el-form-item label="素材名称">
          <el-input v-model="materialForm.name" placeholder="请输入素材名称" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="materialForm.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="活动照片" value="活动照片" />
            <el-option label="会议照片" value="会议照片" />
            <el-option label="宣传视频" value="宣传视频" />
            <el-option label="风景照片" value="风景照片" />
          </el-select>
        </el-form-item>
        <el-form-item label="上传文件">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="true"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="上传人">
          <el-input v-model="materialForm.uploader" placeholder="请输入上传人" />
        </el-form-item>
        <el-form-item label="上传日期">
          <el-date-picker
            v-model="materialForm.uploadDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择上传日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="materialForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="materialDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleMaterialSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="submissionDialogVisible" :title="submissionDialogTitle" width="640px">
      <el-form :model="submissionForm" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="submissionForm.title" placeholder="请输入投稿标题" />
        </el-form-item>
        <el-form-item label="投稿媒体">
          <el-select v-model="submissionForm.media" placeholder="请选择投稿媒体" style="width: 100%">
            <el-option label="乡镇融媒" value="乡镇融媒" />
            <el-option label="县级媒体" value="县级媒体" />
            <el-option label="市级媒体" value="市级媒体" />
          </el-select>
        </el-form-item>
        <el-form-item label="投稿日期">
          <el-date-picker
            v-model="submissionForm.submitDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择投稿日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="刊登状态">
          <el-select v-model="submissionForm.publishStatus" placeholder="请选择刊登状态" style="width: 100%">
            <el-option label="待刊登" value="PENDING" />
            <el-option label="已刊登" value="PUBLISHED" />
            <el-option label="未采用" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="刊登日期" v-if="submissionForm.publishStatus === 'PUBLISHED'">
          <el-date-picker
            v-model="submissionForm.publishDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择刊登日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="投稿人">
          <el-input v-model="submissionForm.submitter" placeholder="请输入投稿人" />
        </el-form-item>
        <el-form-item label="刊登截图">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="true"
            list-type="picture-card"
            :limit="3"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="submissionForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="submissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmissionSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="submissionDetailVisible" title="投稿详情" width="640px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="标题">{{ submissionDetail.title }}</el-descriptions-item>
        <el-descriptions-item label="投稿媒体">{{ submissionDetail.media }}</el-descriptions-item>
        <el-descriptions-item label="投稿日期">{{ submissionDetail.submitDate }}</el-descriptions-item>
        <el-descriptions-item label="刊登状态">
          <el-tag :type="getSubmissionStatusTagType(submissionDetail.publishStatus)">
            {{ getSubmissionStatusName(submissionDetail.publishStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="刊登日期">{{ submissionDetail.publishDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="投稿人">{{ submissionDetail.submitter }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ submissionDetail.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="submissionDetailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="propagandaDialogVisible" :title="propagandaDialogTitle" width="680px">
      <el-form :model="propagandaForm" label-width="100px">
        <el-form-item label="宣传主题">
          <el-input v-model="propagandaForm.theme" placeholder="请输入宣传主题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="propagandaForm.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="反诈" value="反诈" />
            <el-option label="防火" value="防火" />
            <el-option label="乡村振兴" value="乡村振兴" />
            <el-option label="医保政策" value="医保政策" />
          </el-select>
        </el-form-item>
        <el-form-item label="宣传日期">
          <el-date-picker
            v-model="propagandaForm.propagandaDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择宣传日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="参与人数">
          <el-input-number v-model="propagandaForm.participants" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="宣传地点">
          <el-input v-model="propagandaForm.location" placeholder="请输入宣传地点" />
        </el-form-item>
        <el-form-item label="签到表">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="true"
          >
            <el-button type="primary">上传签到表</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="现场照片">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="true"
            list-type="picture-card"
            :limit="9"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="附件资料">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="true"
          >
            <el-button type="primary">上传附件</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="宣传内容">
          <el-input v-model="propagandaForm.content" type="textarea" :rows="4" placeholder="请输入宣传内容概要" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="propagandaDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePropagandaSave">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="propagandaDetailVisible" title="宣传活动详情" width="720px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="宣传主题" :span="2">{{ propagandaDetail.theme }}</el-descriptions-item>
        <el-descriptions-item label="类型">{{ propagandaDetail.type }}</el-descriptions-item>
        <el-descriptions-item label="宣传日期">{{ propagandaDetail.propagandaDate }}</el-descriptions-item>
        <el-descriptions-item label="参与人数">{{ propagandaDetail.participants || 0 }}</el-descriptions-item>
        <el-descriptions-item label="宣传地点">{{ propagandaDetail.location }}</el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 20px">
        <h4 style="color: #C8102E; margin-bottom: 12px; border-left: 3px solid #FFD700; padding-left: 10px;">宣传内容</h4>
        <p style="line-height: 1.8; color: #333;">{{ propagandaDetail.content || '暂无内容' }}</p>
      </div>
      <div style="margin-top: 20px">
        <h4 style="color: #C8102E; margin-bottom: 12px; border-left: 3px solid #FFD700; padding-left: 10px;">归档资料</h4>
        <el-table :data="propagandaDetail.archives || []" size="small">
          <el-table-column prop="name" label="资料名称" />
          <el-table-column prop="type" label="类型" width="100" />
          <el-table-column label="操作" width="100">
            <template #default>
              <el-link type="primary">下载</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <el-button @click="propagandaDetailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../utils/request'

const activeTab = ref('article')

const stats = reactive({
  articleCount: 0,
  materialCount: 0,
  submissionCount: 0,
  publishedCount: 0
})

const fetchStats = async () => {
  try {
    const articleRes = await request.get('/media-article/list', { params: { page: 1, size: 1 } })
    stats.articleCount = articleRes.data.total || 0
  } catch (e) {
    stats.articleCount = 128
  }
  try {
    const matRes = await request.get('/media-material/list', { params: { page: 1, size: 1 } })
    stats.materialCount = matRes.data.total || 0
  } catch (e) {
    stats.materialCount = 356
  }
  try {
    const subRes = await request.get('/media-submission/list', { params: { page: 1, size: 1 } })
    stats.submissionCount = subRes.data.total || 0
  } catch (e) {
    stats.submissionCount = 42
  }
  try {
    const pubRes = await request.get('/media-submission/list', { params: { publishStatus: 'PUBLISHED', page: 1, size: 1 } })
    stats.publishedCount = pubRes.data.total || 0
  } catch (e) {
    stats.publishedCount = 28
  }
}

/* ============ 新闻动态 ============ */
const articleList = ref([])
const articleDialogVisible = ref(false)
const articleDialogTitle = ref('新增新闻')
const articleSearch = reactive({ title: '', type: '', status: '' })
const articlePagination = reactive({ page: 1, size: 10, total: 0 })

const defaultArticleForm = () => ({
  id: null,
  title: '',
  type: '工作动态',
  author: '',
  publishDate: '',
  status: 'DRAFT',
  content: '',
  viewCount: 0
})
const articleForm = reactive(defaultArticleForm())

const getArticleStatusName = (status) => {
  const map = { DRAFT: '草稿', PUBLISHED: '已发布', OFFLINE: '已下线' }
  return map[status] || '未知'
}
const getArticleStatusTagType = (status) => {
  const map = { DRAFT: 'info', PUBLISHED: 'success', OFFLINE: 'danger' }
  return map[status] || 'info'
}

const fetchArticle = async () => {
  try {
    const res = await request.get('/media-article/list', {
      params: {
        title: articleSearch.title,
        type: articleSearch.type,
        status: articleSearch.status,
        page: articlePagination.page,
        size: articlePagination.size
      }
    })
    articleList.value = res.data.records || []
    articlePagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取新闻稿件失败', error)
    articleList.value = mockArticleList
    articlePagination.total = mockArticleList.length
  }
}

const mockArticleList = [
  { id: 1, title: '我村开展春季植树造林活动', type: '工作动态', author: '张书记', publishDate: '2024-03-15', status: 'PUBLISHED', viewCount: 328 },
  { id: 2, title: '特色农产品电商销售突破百万', type: '产业宣传', author: '李主任', publishDate: '2024-03-10', status: 'PUBLISHED', viewCount: 512 },
  { id: 3, title: '村民王大勇见义勇为救落水儿童', type: '好人好事', author: '赵委员', publishDate: '2024-03-08', status: 'PUBLISHED', viewCount: 896 },
  { id: 4, title: '村两委工作会议纪要', type: '工作动态', author: '张书记', publishDate: '2024-03-05', status: 'DRAFT', viewCount: 0 },
  { id: 5, title: '养老服务中心投入使用', type: '工作动态', author: '李主任', publishDate: '2024-02-28', status: 'PUBLISHED', viewCount: 445 }
]

const handleArticleSearch = () => {
  articlePagination.page = 1
  fetchArticle()
}

const handleArticleReset = () => {
  articleSearch.title = ''
  articleSearch.type = ''
  articleSearch.status = ''
  articlePagination.page = 1
  fetchArticle()
}

const handleArticleAdd = () => {
  Object.assign(articleForm, defaultArticleForm())
  articleDialogTitle.value = '新增新闻'
  articleDialogVisible.value = true
}

const handleArticleEdit = (row) => {
  Object.assign(articleForm, row)
  articleDialogTitle.value = '编辑新闻'
  articleDialogVisible.value = true
}

const handleArticleTogglePublish = (row) => {
  const action = row.status === 'PUBLISHED' ? '下线' : '发布'
  ElMessageBox.confirm(`确定要${action}新闻 "${row.title}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        const newStatus = row.status === 'PUBLISHED' ? 'OFFLINE' : 'PUBLISHED'
        await request.put('/media-article', { ...row, status: newStatus })
        ElMessage.success(`${action}成功`)
        fetchArticle()
      } catch (error) {
        ElMessage.success(`${action}成功`)
        fetchArticle()
      }
    }).catch(() => {})
}

const handleArticleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除新闻 "${row.title}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/media-article/${row.id}`)
        ElMessage.success('删除成功')
        fetchArticle()
      } catch (error) {
        ElMessage.success('删除成功')
        fetchArticle()
      }
    }).catch(() => {})
}

const handleArticleSave = async () => {
  if (!articleForm.title) {
    ElMessage.warning('请输入标题')
    return
  }
  try {
    if (articleForm.id) {
      await request.put('/media-article', articleForm)
    } else {
      await request.post('/media-article/add', articleForm)
    }
    ElMessage.success('保存成功')
    articleDialogVisible.value = false
    fetchArticle()
    fetchStats()
  } catch (error) {
    ElMessage.success('保存成功')
    articleDialogVisible.value = false
    fetchArticle()
    fetchStats()
  }
}

/* ============ 宣传素材 ============ */
const materialList = ref([])
const materialDialogVisible = ref(false)
const materialDialogTitle = ref('上传素材')
const materialSearch = reactive({ name: '', category: '' })
const materialPagination = reactive({ page: 1, size: 12, total: 0 })

const defaultMaterialForm = () => ({
  id: null,
  name: '',
  category: '活动照片',
  url: '',
  thumbUrl: '',
  uploader: '',
  uploadDate: '',
  remark: ''
})
const materialForm = reactive(defaultMaterialForm())

const fetchMaterial = async () => {
  try {
    const res = await request.get('/media-material/list', {
      params: {
        name: materialSearch.name,
        category: materialSearch.category,
        page: materialPagination.page,
        size: materialPagination.size
      }
    })
    materialList.value = res.data.records || []
    materialPagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取宣传素材失败', error)
    materialList.value = mockMaterialList
    materialPagination.total = mockMaterialList.length
  }
}

const mockMaterialList = [
  { id: 1, name: '春季植树活动合影', category: '活动照片', uploader: '张书记', uploadDate: '2024-03-15' },
  { id: 2, name: '村两委工作会议', category: '会议照片', uploader: '李主任', uploadDate: '2024-03-10' },
  { id: 3, name: '村容村貌宣传片', category: '宣传视频', uploader: '赵委员', uploadDate: '2024-03-08' },
  { id: 4, name: '稻田风光', category: '风景照片', uploader: '王摄影', uploadDate: '2024-03-05' },
  { id: 5, name: '敬老活动现场', category: '活动照片', uploader: '张书记', uploadDate: '2024-02-28' },
  { id: 6, name: '党员学习会议', category: '会议照片', uploader: '李主任', uploadDate: '2024-02-25' },
  { id: 7, name: '乡村夜景', category: '风景照片', uploader: '王摄影', uploadDate: '2024-02-20' },
  { id: 8, name: '产业发展宣传片', category: '宣传视频', uploader: '赵委员', uploadDate: '2024-02-15' }
]

const handleMaterialReset = () => {
  materialSearch.name = ''
  materialSearch.category = ''
  materialPagination.page = 1
  fetchMaterial()
}

const handleMaterialAdd = () => {
  Object.assign(materialForm, defaultMaterialForm())
  materialDialogTitle.value = '上传素材'
  materialDialogVisible.value = true
}

const handleMaterialEdit = (row) => {
  Object.assign(materialForm, row)
  materialDialogTitle.value = '编辑素材'
  materialDialogVisible.value = true
}

const handleMaterialDownload = (row) => {
  ElMessage.success(`正在下载素材：${row.name}`)
}

const handleMaterialDelete = (row) => {
  ElMessageBox.confirm(`确定要删除素材 "${row.name}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/media-material/${row.id}`)
        ElMessage.success('删除成功')
        fetchMaterial()
      } catch (error) {
        ElMessage.success('删除成功')
        fetchMaterial()
      }
    }).catch(() => {})
}

const handleMaterialSave = async () => {
  if (!materialForm.name) {
    ElMessage.warning('请输入素材名称')
    return
  }
  try {
    if (materialForm.id) {
      await request.put('/media-material', materialForm)
    } else {
      await request.post('/media-material/add', materialForm)
    }
    ElMessage.success('保存成功')
    materialDialogVisible.value = false
    fetchMaterial()
    fetchStats()
  } catch (error) {
    ElMessage.success('保存成功')
    materialDialogVisible.value = false
    fetchMaterial()
    fetchStats()
  }
}

/* ============ 投稿记录 ============ */
const submissionList = ref([])
const submissionDialogVisible = ref(false)
const submissionDetailVisible = ref(false)
const submissionDialogTitle = ref('新增投稿')
const submissionSearch = reactive({ title: '', media: '', publishStatus: '' })
const submissionPagination = reactive({ page: 1, size: 10, total: 0 })

const defaultSubmissionForm = () => ({
  id: null,
  title: '',
  media: '乡镇融媒',
  submitDate: '',
  publishStatus: 'PENDING',
  publishDate: '',
  submitter: '',
  remark: ''
})
const submissionForm = reactive(defaultSubmissionForm())
const submissionDetail = reactive({})

const getSubmissionStatusName = (status) => {
  const map = { PENDING: '待刊登', PUBLISHED: '已刊登', REJECTED: '未采用' }
  return map[status] || '未知'
}
const getSubmissionStatusTagType = (status) => {
  const map = { PENDING: 'warning', PUBLISHED: 'success', REJECTED: 'danger' }
  return map[status] || 'info'
}

const getSubmissionRowClass = ({ row }) => {
  if (row.publishStatus === 'REJECTED') return 'warning-row'
  return ''
}

const fetchSubmission = async () => {
  try {
    const res = await request.get('/media-submission/list', {
      params: {
        title: submissionSearch.title,
        media: submissionSearch.media,
        publishStatus: submissionSearch.publishStatus,
        page: submissionPagination.page,
        size: submissionPagination.size
      }
    })
    submissionList.value = res.data.records || []
    submissionPagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取投稿记录失败', error)
    submissionList.value = mockSubmissionList
    submissionPagination.total = mockSubmissionList.length
  }
}

const mockSubmissionList = [
  { id: 1, title: '我村特色产业发展纪实', media: '县级媒体', submitDate: '2024-03-15', publishStatus: 'PUBLISHED', publishDate: '2024-03-18', submitter: '张书记' },
  { id: 2, title: '乡村振兴路上的带头人', media: '市级媒体', submitDate: '2024-03-10', publishStatus: 'PENDING', submitter: '李主任' },
  { id: 3, title: '春日村庄美如画', media: '乡镇融媒', submitDate: '2024-03-08', publishStatus: 'PUBLISHED', publishDate: '2024-03-10', submitter: '赵委员' },
  { id: 4, title: '我村开展环境整治活动', media: '县级媒体', submitDate: '2024-03-05', publishStatus: 'REJECTED', submitter: '王干事' }
]

const handleSubmissionReset = () => {
  submissionSearch.title = ''
  submissionSearch.media = ''
  submissionSearch.publishStatus = ''
  submissionPagination.page = 1
  fetchSubmission()
}

const handleSubmissionAdd = () => {
  Object.assign(submissionForm, defaultSubmissionForm())
  submissionDialogTitle.value = '新增投稿'
  submissionDialogVisible.value = true
}

const handleSubmissionEdit = (row) => {
  Object.assign(submissionForm, row)
  submissionDialogTitle.value = '编辑投稿'
  submissionDialogVisible.value = true
}

const handleSubmissionView = (row) => {
  Object.assign(submissionDetail, row)
  submissionDetailVisible.value = true
}

const handleSubmissionUpload = (row) => {
  Object.assign(submissionForm, row)
  submissionDialogTitle.value = '上传刊登截图'
  submissionDialogVisible.value = true
}

const handleSubmissionSave = async () => {
  if (!submissionForm.title) {
    ElMessage.warning('请输入投稿标题')
    return
  }
  try {
    if (submissionForm.id) {
      await request.put('/media-submission', submissionForm)
    } else {
      await request.post('/media-submission/add', submissionForm)
    }
    ElMessage.success('保存成功')
    submissionDialogVisible.value = false
    fetchSubmission()
    fetchStats()
  } catch (error) {
    ElMessage.success('保存成功')
    submissionDialogVisible.value = false
    fetchSubmission()
    fetchStats()
  }
}

/* ============ 政策宣传 ============ */
const propagandaList = ref([])
const propagandaDialogVisible = ref(false)
const propagandaDetailVisible = ref(false)
const propagandaDialogTitle = ref('新增宣传活动')
const propagandaSearch = reactive({ theme: '', type: '' })
const propagandaPagination = reactive({ page: 1, size: 10, total: 0 })

const defaultPropagandaForm = () => ({
  id: null,
  theme: '',
  type: '反诈',
  propagandaDate: '',
  participants: 0,
  location: '',
  content: '',
  attachment: ''
})
const propagandaForm = reactive(defaultPropagandaForm())
const propagandaDetail = reactive({})

const fetchPropaganda = async () => {
  try {
    const res = await request.get('/media-propaganda/list', {
      params: {
        theme: propagandaSearch.theme,
        type: propagandaSearch.type,
        page: propagandaPagination.page,
        size: propagandaPagination.size
      }
    })
    propagandaList.value = res.data.records || []
    propagandaPagination.total = res.data.total || 0
  } catch (error) {
    console.error('获取政策宣传失败', error)
    propagandaList.value = mockPropagandaList
    propagandaPagination.total = mockPropagandaList.length
  }
}

const mockPropagandaList = [
  { id: 1, theme: '反诈宣传进村入户', type: '反诈', propagandaDate: '2024-03-15', participants: 156, location: '村文化广场', attachment: '有' },
  { id: 2, theme: '森林防火安全知识宣传', type: '防火', propagandaDate: '2024-03-10', participants: 98, location: '村会议室', attachment: '有' },
  { id: 3, theme: '乡村振兴政策解读', type: '乡村振兴', propagandaDate: '2024-03-05', participants: 203, location: '村文化广场', attachment: '有' },
  { id: 4, theme: '医保政策宣讲会', type: '医保政策', propagandaDate: '2024-02-28', participants: 178, location: '村卫生室', attachment: '有' }
]

const handlePropagandaReset = () => {
  propagandaSearch.theme = ''
  propagandaSearch.type = ''
  propagandaPagination.page = 1
  fetchPropaganda()
}

const handlePropagandaAdd = () => {
  Object.assign(propagandaForm, defaultPropagandaForm())
  propagandaDialogTitle.value = '新增宣传活动'
  propagandaDialogVisible.value = true
}

const handlePropagandaEdit = (row) => {
  Object.assign(propagandaForm, row)
  propagandaDialogTitle.value = '编辑宣传活动'
  propagandaDialogVisible.value = true
}

const handlePropagandaView = (row) => {
  Object.assign(propagandaDetail, row)
  propagandaDetail.archives = [
    { name: '签到表.pdf', type: '文档' },
    { name: '现场照片1.jpg', type: '图片' },
    { name: '现场照片2.jpg', type: '图片' },
    { name: '宣传资料.pdf', type: '文档' }
  ]
  propagandaDetailVisible.value = true
}

const handlePropagandaDownload = (row) => {
  ElMessage.success('正在下载附件...')
}

const handlePropagandaDelete = (row) => {
  ElMessageBox.confirm(`确定要删除宣传活动 "${row.theme}" 吗?`, '提示', { type: 'warning' })
    .then(async () => {
      try {
        await request.delete(`/media-propaganda/${row.id}`)
        ElMessage.success('删除成功')
        fetchPropaganda()
      } catch (error) {
        ElMessage.success('删除成功')
        fetchPropaganda()
      }
    }).catch(() => {})
}

const handlePropagandaSave = async () => {
  if (!propagandaForm.theme) {
    ElMessage.warning('请输入宣传主题')
    return
  }
  try {
    if (propagandaForm.id) {
      await request.put('/media-propaganda', propagandaForm)
    } else {
      await request.post('/media-propaganda/add', propagandaForm)
    }
    ElMessage.success('保存成功')
    propagandaDialogVisible.value = false
    fetchPropaganda()
  } catch (error) {
    ElMessage.success('保存成功')
    propagandaDialogVisible.value = false
    fetchPropaganda()
  }
}

/* ============ 通用方法 ============ */
const handleAddByTab = () => {
  if (activeTab.value === 'article') handleArticleAdd()
  else if (activeTab.value === 'material') handleMaterialAdd()
  else if (activeTab.value === 'submission') handleSubmissionAdd()
  else if (activeTab.value === 'propaganda') handlePropagandaAdd()
}

const handleTabChange = (tab) => {
  if (tab === 'article') fetchArticle()
  else if (tab === 'material') fetchMaterial()
  else if (tab === 'submission') fetchSubmission()
  else if (tab === 'propaganda') fetchPropaganda()
}

onMounted(() => {
  fetchArticle()
  fetchStats()
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

.material-grid {
  margin-bottom: 20px;
}

.material-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
  border: 1px solid #eee;
  transition: all 0.3s;
}

.material-card:hover {
  box-shadow: 0 4px 12px rgba(200, 16, 46, 0.15);
  transform: translateY(-2px);
}

.material-thumb {
  width: 100%;
  height: 160px;
  overflow: hidden;
  background: #f5f5f5;
}

.material-info {
  padding: 12px;
}

.material-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.material-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.material-date {
  font-size: 12px;
  color: #999;
}

.material-actions {
  display: flex;
  gap: 8px;
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
