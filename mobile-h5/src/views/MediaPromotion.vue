<template>
  <div class="media-page page-container">
    <van-nav-bar title="媒体宣传" left-arrow @click-left="onBack" />
    
    <van-tabs v-model:active="activeTab" @change="onTabChange">
      <van-tab title="村内新闻" name="news" />
      <van-tab title="素材图库" name="gallery" />
      <van-tab title="政策宣传" name="policy" />
      <van-tab title="媒体投稿" name="submission" />
    </van-tabs>

    <div class="tab-content" v-if="activeTab === 'news'">
      <div class="table-section">
        <ExcelTable 
          :data="newsData" 
          :columns="newsColumns" 
          export-filename="村内新闻.xlsx"
        />
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'gallery'">
      <div class="search-bar">
        <van-search v-model="searchKeyword" placeholder="搜索图片" @search="onSearch" />
      </div>

      <div class="table-section">
        <ExcelTable 
          :data="galleryData" 
          :columns="galleryColumns" 
          export-filename="素材图库.xlsx"
        />
      </div>

      <div v-if="isOfficial" class="upload-section">
        <van-button type="primary" block icon="plus" @click="uploadImage">上传图片</van-button>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'policy'">
      <div class="table-section">
        <ExcelTable 
          :data="policyData" 
          :columns="policyColumns" 
          export-filename="政策宣传.xlsx"
        />
      </div>

      <div v-if="isOfficial" class="action-bar">
        <van-button type="primary" icon="plus" @click="addPolicy">新增宣传活动</van-button>
      </div>
    </div>

    <div class="tab-content" v-if="activeTab === 'submission'">
      <div class="card">
        <div class="card-header">
          <div class="header-title">📝 媒体投稿</div>
        </div>
        <van-form @submit="submitSubmission">
          <van-field v-model="submissionForm.title" label="稿件标题" placeholder="请输入稿件标题" />
          <van-field v-model="submissionForm.media" label="投稿媒体" placeholder="如：县融媒体中心" />
          <van-field v-model="submissionForm.content" label="稿件内容" type="textarea" placeholder="请输入稿件内容" />
          <div class="upload-section">
            <div class="upload-label">刊登截图</div>
            <div class="upload-grid">
              <div v-for="(photo, idx) in submissionForm.photos" :key="idx" class="upload-item">
                <img :src="photo" />
              </div>
              <div class="upload-add" @click="uploadPhoto">+</div>
            </div>
          </div>
          <van-button type="primary" block native-type="submit">提交投稿</van-button>
        </van-form>
      </div>

      <div class="card">
        <div class="card-header">
          <div class="header-title">📋 投稿记录</div>
        </div>
        <div class="table-section">
          <ExcelTable 
            :data="submissionData" 
            :columns="submissionColumns" 
            export-filename="投稿记录.xlsx"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import request from '../utils/request'
import { goBack } from '../utils/index'
import ExcelTable from '../components/ExcelTable.vue'

const router = useRouter()
const activeTab = ref('news')
const isOfficial = true
const searchKeyword = ref('')

const newsList = ref([
  { id: 1, title: '罗卜田乡开展春节慰问活动', tag: '活动动态', date: '2024-01-28', description: '村两委走访慰问村内孤寡老人、留守儿童和困难群众，送去节日祝福和慰问品', author: '王书记', likes: 56, comments: 12 },
  { id: 2, title: '村内环境整治取得显著成效', tag: '环境整治', date: '2024-01-20', description: '经过一个月的集中整治，村内环境卫生得到明显改善', author: '李主任', likes: 42, comments: 8 },
  { id: 3, title: '政策宣传进万家活动启动', tag: '政策宣传', date: '2024-01-15', description: '志愿者们走村入户，宣传反诈、防火等安全知识', author: '张委员', likes: 38, comments: 6 }
])

const galleryList = ref([
  { id: 1, url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chinese%20village%20spring%20festival%20celebration%20activity&image_size=square', name: '春节活动' },
  { id: 2, url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=village%20environment%20cleanup%20volunteer%20activity&image_size=square', name: '环境整治' },
  { id: 3, url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=elderly%20care%20visiting%20activity%20in%20village&image_size=square', name: '敬老活动' },
  { id: 4, url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=village%20meeting%20government%20officials&image_size=square', name: '村会议' },
  { id: 5, url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=village%20road%20construction%20project&image_size=square', name: '道路建设' },
  { id: 6, url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=village%20cultural%20square%20activity&image_size=square', name: '文化广场' }
])

const policyList = ref([
  { id: 1, title: '反诈宣传进万家', type: '反诈宣传', date: '2024-01-15', participants: 50, materials: 200 },
  { id: 2, title: '森林防火宣传', type: '防火宣传', date: '2024-01-10', participants: 30, materials: 150 },
  { id: 3, title: '乡村振兴政策宣传', type: '政策宣传', date: '2024-01-05', participants: 40, materials: 180 }
])

const submissionForm = ref({ title: '', media: '', content: '', photos: [] })

const submissionRecords = ref([
  { id: 1, title: '罗卜田乡环境整治成效显著', media: '县融媒体中心', date: '2024-01-20', status: '已刊登' },
  { id: 2, title: '志愿者服务暖人心', media: '乡镇公众号', date: '2024-01-15', status: '已刊登' }
])

const newsColumns = [
  { key: 'title', title: '标题' },
  { key: 'tag', title: '标签', formatter: (val) => val || '普通' },
  { key: 'date', title: '日期' },
  { key: 'author', title: '作者', formatter: (val) => val || '-' },
  { key: 'likes', title: '点赞数', formatter: (val) => `${val || 0}` },
  { key: 'comments', title: '评论数', formatter: (val) => `${val || 0}` }
]

const galleryColumns = [
  { key: 'name', title: '名称' },
  { key: 'url', title: '图片地址', formatter: (val) => val || '-' }
]

const policyColumns = [
  { key: 'title', title: '活动名称' },
  { key: 'type', title: '类型', formatter: (val) => val || '宣传活动' },
  { key: 'date', title: '日期' },
  { key: 'participants', title: '参与人数', formatter: (val) => `${val || 0}人` },
  { key: 'materials', title: '宣传物料', formatter: (val) => `${val || 0}份` }
]

const submissionColumns = [
  { key: 'title', title: '稿件标题' },
  { key: 'media', title: '投稿媒体', formatter: (val) => val || '-' },
  { key: 'date', title: '投稿日期' },
  { key: 'status', title: '状态' }
]

const newsData = computed(() => newsList.value)
const galleryData = computed(() => galleryList.value)
const policyData = computed(() => policyList.value)
const submissionData = computed(() => submissionRecords.value)

const onBack = () => { goBack(router) }

const onTabChange = () => {
  fetchData()
}

const fetchData = async () => {
  try {
    if (activeTab.value === 'news') {
      const res = await request.get('/media/news')
      newsList.value = res.data || newsList.value
    } else if (activeTab.value === 'gallery') {
      const res = await request.get('/media/gallery', { params: { keyword: searchKeyword.value } })
      galleryList.value = res.data || galleryList.value
    } else if (activeTab.value === 'policy') {
      const res = await request.get('/media/policy')
      policyList.value = res.data || policyList.value
    }
  } catch (error) {
    console.error('获取数据失败', error)
  }
}

const onSearch = () => {
  fetchData()
}

const viewNewsDetail = (item) => {
  showToast(`查看 ${item.title} 详情功能开发中`)
}

const previewImage = (item) => {
  showToast(`预览图片功能开发中`)
}

const uploadImage = () => {
  showToast('上传图片功能开发中')
}

const viewPolicyDetail = (item) => {
  showToast(`查看 ${item.title} 详情功能开发中`)
}

const addPolicy = () => {
  showToast('新增宣传活动功能开发中')
}

const uploadPhoto = () => {
  showToast('上传照片功能开发中')
}

const submitSubmission = () => {
  showToast('投稿已提交')
  submissionForm.value = { title: '', media: '', content: '', photos: [] }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.media-page {
  padding-bottom: calc(env(safe-area-inset-bottom) + 60px);
}

.card {
  background: #fff;
  margin: 12px;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.card-header {
  margin-bottom: 14px;
}

.header-title {
  font-size: 15px;
  font-weight: bold;
  color: #D22630;
}

.search-bar {
  padding: 0 12px;
  margin-bottom: 12px;
}

.card-list {
  padding: 0 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.news-card, .policy-card {
  background: #fff;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 2px 12px rgba(210, 38, 48, 0.08);
  border: 1px solid rgba(210, 38, 48, 0.1);
}

.news-header, .policy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.news-tag, .policy-tag {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
  background: rgba(210, 38, 48, 0.1);
  color: #D22630;
}

.news-time, .policy-time {
  font-size: 12px;
  color: #999;
}

.news-title, .policy-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.news-desc {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.news-author {
  font-size: 12px;
  color: #999;
}

.news-stats {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #666;
}

.policy-info {
  background: #fafafa;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 12px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  font-size: 13px;
  border-bottom: 1px solid #eee;
}

.info-row:last-child {
  border-bottom: none;
}

.info-row span:first-child {
  color: #999;
}

.info-row span:last-child {
  color: #333;
}

.gallery-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  padding: 0 12px;
}

.gallery-item {
  position: relative;
  aspect-ratio: 1;
  border-radius: 10px;
  overflow: hidden;
}

.gallery-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.gallery-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 6px;
  font-size: 11px;
  text-align: center;
}

.upload-section {
  padding: 12px;
  margin-top: 12px;
}

.upload-label {
  font-size: 14px;
  font-weight: bold;
  color: #333;
  margin-bottom: 12px;
}

.upload-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.upload-item {
  aspect-ratio: 1;
  border-radius: 10px;
  overflow: hidden;
}

.upload-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-add {
  aspect-ratio: 1;
  border-radius: 10px;
  border: 2px dashed #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #999;
}

.action-bar {
  display: flex;
  padding: 12px;
}

.action-bar .van-button {
  flex: 1;
}

.table-section {
  margin: 10px 12px;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #fafafa;
  border-radius: 10px;
}

.item-name {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.item-detail {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.item-status {
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 10px;
}

.item-status.已刊登 {
  background: rgba(76, 175, 80, 0.2);
  color: #4CAF50;
}
</style>
