<template>
  <div class="page-container">
    <van-nav-bar title="防溺水宣传" left-arrow @click-left="goBack" />
    
    <div class="stats-card">
      <div class="stats-item">
        <div class="stats-num">{{ promotionList.length }}</div>
        <div class="stats-label">宣传活动</div>
      </div>
      <div class="stats-item">
        <div class="stats-num">{{ totalParticipants }}</div>
        <div class="stats-label">参与人次</div>
      </div>
      <div class="stats-item">
        <div class="stats-num">{{ thisMonthCount }}</div>
        <div class="stats-label">本月活动</div>
      </div>
    </div>

    <div class="section-title">
      <span class="title-bar"></span>
      <span>宣传活动记录</span>
    </div>

    <div class="promotion-list">
      <div 
        v-for="item in promotionList" 
        :key="item.id" 
        class="promo-card"
        @click="viewDetail(item)"
      >
        <div class="promo-image">
          <img :src="item.image" :alt="item.title" />
          <div class="promo-tag">{{ item.type }}</div>
        </div>
        <div class="promo-info">
          <div class="promo-title">{{ item.title }}</div>
          <div class="promo-desc">{{ item.desc }}</div>
          <div class="promo-meta">
            <div class="meta-left">
              <span class="meta-item">
                <van-icon name="location-o" size="12" />
                {{ item.location }}
              </span>
              <span class="meta-item">
                <van-icon name="users-o" size="12" />
                {{ item.participants }}人参与
              </span>
            </div>
            <div class="meta-date">{{ item.date }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const promotionList = ref([
  {
    id: 1,
    title: '暑期防溺水安全宣传活动',
    desc: '面向全村中小学生开展防溺水安全教育',
    type: '宣传活动',
    location: '村文化广场',
    participants: 156,
    date: '2026-07-15',
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Village%20safety%20promotion%20event%20children%20learning%20water%20safety%20outdoor%20activity&image_size=landscape_4_3'
  },
  {
    id: 2,
    title: '防溺水知识进村入户',
    desc: '走村入户发放防溺水宣传手册',
    type: '入户宣传',
    location: '全村各村民组',
    participants: 328,
    date: '2026-07-10',
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Volunteers%20distributing%20safety%20brochures%20to%20villagers%20in%20rural%20China&image_size=landscape_4_3'
  },
  {
    id: 3,
    title: '校园防溺水专题讲座',
    desc: '邀请派出所民警开展防溺水专题讲座',
    type: '专题讲座',
    location: '村小学',
    participants: 234,
    date: '2026-06-28',
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=School%20safety%20lecture%20students%20listening%20to%20water%20safety%20presentation&image_size=landscape_4_3'
  },
  {
    id: 4,
    title: '防溺水应急演练',
    desc: '组织村民开展溺水应急救援演练',
    type: '应急演练',
    location: '村水库旁',
    participants: 67,
    date: '2026-06-20',
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Water%20rescue%20emergency%20drill%20volunteers%20practicing%20lifesaving%20techniques&image_size=landscape_4_3'
  },
  {
    id: 5,
    title: '防溺水标语宣传',
    desc: '在村口、塘边、河边设置安全警示标语',
    type: '标语宣传',
    location: '全村危险水域',
    participants: 12,
    date: '2026-06-15',
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Water%20safety%20warning%20signs%20by%20the%20pond%20in%20Chinese%20village&image_size=landscape_4_3'
  },
  {
    id: 6,
    title: '家长会防溺水宣传',
    desc: '通过家长会向家长宣传防溺水安全知识',
    type: '会议宣传',
    location: '村小学会议室',
    participants: 189,
    date: '2026-05-25',
    image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Parent%20meeting%20about%20child%20safety%20education%20school%20meeting%20room&image_size=landscape_4_3'
  }
])

const totalParticipants = computed(() => 
  promotionList.value.reduce((sum, item) => sum + item.participants, 0)
)

const thisMonthCount = computed(() => 
  promotionList.value.filter(item => item.date.startsWith('2026-07')).length
)

const goBack = () => {
  router.back()
}

const viewDetail = (item) => {
  console.log('查看宣传详情:', item.title)
}

onMounted(() => {
  document.title = '防溺水宣传 - 村级智慧村务平台'
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.stats-card {
  display: flex;
  background: linear-gradient(135deg, #FF9800 0%, #FF5722 100%);
  margin: 12px;
  border-radius: 12px;
  padding: 20px 0;
  color: #fff;
}

.stats-item {
  flex: 1;
  text-align: center;
}

.stats-num {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 12px;
  opacity: 0.9;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px 8px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.title-bar {
  width: 4px;
  height: 18px;
  background: linear-gradient(180deg, #FF9800 0%, #FF5722 100%);
  border-radius: 2px;
}

.promotion-list {
  padding: 0 12px;
}

.promo-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.promo-card:active {
  transform: scale(0.98);
}

.promo-image {
  position: relative;
  width: 100%;
  height: 160px;
}

.promo-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.promo-tag {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(255, 87, 34, 0.9);
  color: #fff;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 500;
}

.promo-info {
  padding: 14px;
}

.promo-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.promo-desc {
  font-size: 13px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.5;
}

.promo-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.meta-left {
  display: flex;
  gap: 12px;
}

.meta-item {
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 3px;
}

.meta-date {
  font-size: 12px;
  color: #FF9800;
  font-weight: 500;
}
</style>