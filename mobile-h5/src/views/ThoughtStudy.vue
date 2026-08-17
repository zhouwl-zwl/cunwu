<template>
  <div class="thought-study-page">
    <div class="hero-section">
      <div class="hero-bg"></div>
      <div class="hero-content">
        <div class="party-emblem">☭</div>
        <div class="hero-title">学习最新思想</div>
        <div class="hero-subtitle">学思想 · 强党性 · 重实践 · 建新功</div>
        <div class="hero-decoration">
          <span class="star">★</span>
          <span class="star">★</span>
          <span class="star">★</span>
          <span class="star">★</span>
          <span class="star">★</span>
        </div>
      </div>
    </div>

    <div class="stats-bar">
      <div class="stat-item">
        <div class="stat-num">{{ topics.length }}</div>
        <div class="stat-label">学习专题</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <div class="stat-num">{{ totalPoints }}</div>
        <div class="stat-label">核心要点</div>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <div class="stat-num">{{ goldSentences.length }}</div>
        <div class="stat-label">金句语录</div>
      </div>
    </div>

    <div class="section-title">
      <span class="title-bar"></span>
      <span class="title-text">学习专题</span>
      <span class="title-en">SPECIAL TOPICS</span>
    </div>

    <div class="topic-list">
      <div
        v-for="(topic, index) in topics"
        :key="topic.id"
        class="topic-card"
        :style="{ animationDelay: index * 0.08 + 's' }"
      >
        <div class="topic-header" @click="toggleTopic(topic.id)">
          <div class="topic-icon" :style="{ background: topic.gradient }">
            <span>{{ topic.icon }}</span>
          </div>
          <div class="topic-info">
            <div class="topic-name">{{ topic.name }}</div>
            <div class="topic-desc">{{ topic.brief }}</div>
          </div>
          <van-icon
            :name="expandedId === topic.id ? 'arrow-up' : 'arrow-down'"
            size="18"
            color="#B01A26"
          />
        </div>

        <transition name="expand">
          <div v-if="expandedId === topic.id" class="topic-detail">
            <div class="detail-section">
              <div class="detail-label">
                <van-icon name="bookmark-o" size="14" color="#D22630" />
                <span>核心要点</span>
              </div>
              <ul class="point-list">
                <li v-for="(point, i) in topic.points" :key="i">{{ point }}</li>
              </ul>
            </div>
            <div class="detail-section">
              <div class="detail-label">
                <van-icon name="comment-o" size="14" color="#D22630" />
                <span>金句摘录</span>
              </div>
              <div class="gold-quote">{{ topic.goldQuote }}</div>
            </div>
          </div>
        </transition>
      </div>
    </div>

    <div class="section-title">
      <span class="title-bar"></span>
      <span class="title-text">金句语录</span>
      <span class="title-en">GOLDEN WORDS</span>
    </div>

    <div class="quote-scroll">
      <div
        v-for="(q, i) in goldSentences"
        :key="i"
        class="quote-card"
        :style="{ background: q.bg }"
      >
        <div class="quote-mark">"</div>
        <div class="quote-text">{{ q.text }}</div>
        <div class="quote-source">— {{ q.source }}</div>
      </div>
    </div>

    <div class="footer-pledge">
      <div class="pledge-icon">🚩</div>
      <div class="pledge-text">
        学思用贯通 · 知信行统一<br/>
        <span class="pledge-sub">以学铸魂 · 以学增智 · 以学正风 · 以学促干</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const expandedId = ref(1)

const toggleTopic = (id) => {
  expandedId.value = expandedId.value === id ? null : id
}

const topics = ref([
  {
    id: 1,
    name: '习近平新时代中国特色社会主义思想',
    brief: '党的指导思想，行动指南',
    icon: '📖',
    gradient: 'linear-gradient(135deg, #D22630 0%, #8B0000 100%)',
    points: [
      '明确坚持和发展中国特色社会主义的总任务',
      '明确新时代我国社会主要矛盾',
      '明确中国特色社会主义事业总体布局是"五位一体"',
      '明确全面深化改革总目标',
      '明确全面推进依法治国总目标',
      '明确中国特色大国外交，推动构建人类命运共同体'
    ],
    goldQuote: '人民对美好生活的向往，就是我们的奋斗目标。'
  },
  {
    id: 2,
    name: '党的二十大精神',
    brief: '高举旗帜，凝聚力量，团结奋进',
    icon: '🚩',
    gradient: 'linear-gradient(135deg, #C62828 0%, #7F0000 100%)',
    points: [
      '深刻领会大会的主题',
      '深刻领会过去5年工作和新时代10年伟大变革',
      '深刻领会开辟马克思主义中国化时代化新境界',
      '深刻领会新时代新征程中国共产党的使命任务',
      '深刻领会未来一个时期党和国家事业发展的战略部署'
    ],
    goldQuote: '江山就是人民，人民就是江山。中国共产党领导人民打江山、守江山，守的是人民的心。'
  },
  {
    id: 3,
    name: '乡村振兴战略',
    brief: '产业兴旺、生态宜居、乡风文明、治理有效、生活富裕',
    icon: '🌾',
    gradient: 'linear-gradient(135deg, #E65100 0%, #BF360C 100%)',
    points: [
      '产业振兴：发展现代农业，促进农村一二三产业融合发展',
      '人才振兴：培育新型农民，吸引人才返乡创业',
      '文化振兴：传承优秀传统文化，推进移风易俗',
      '生态振兴：改善农村人居环境，建设美丽乡村',
      '组织振兴：建强基层党组织，提升治理能力'
    ],
    goldQuote: '民族要复兴，乡村必振兴。'
  },
  {
    id: 4,
    name: '中国式现代化',
    brief: '人口规模巨大、共同富裕、物质精神协调、人与自然和谐、和平发展',
    icon: '🌟',
    gradient: 'linear-gradient(135deg, #B71C1C 0%, #880E4F 100%)',
    points: [
      '人口规模巨大的现代化',
      '全体人民共同富裕的现代化',
      '物质文明和精神文明相协调的现代化',
      '人与自然和谐共生的现代化',
      '走和平发展道路的现代化'
    ],
    goldQuote: '中国式现代化是全体人民共同富裕的现代化，不能只是少数人富裕，而是要全体人民共同富裕。'
  },
  {
    id: 5,
    name: '共同富裕',
    brief: '全体人民共同富裕，是中国式现代化的本质特征',
    icon: '🤝',
    gradient: 'linear-gradient(135deg, #D84315 0%, #BF360C 100%)',
    points: [
      '共同富裕是社会主义的本质要求',
      '坚持在发展中保障和改善民生',
      '促进社会公平正义，促进人的全面发展',
      '扎实推动共同富裕，分阶段促进共同富裕',
      '鼓励勤劳创新致富，坚持尽力而为、量力而行'
    ],
    goldQuote: '共同富裕是全体人民的富裕，是人民群众物质生活和精神生活都富裕，不是少数人的富裕，也不是整齐划一的平均主义。'
  },
  {
    id: 6,
    name: '新发展理念',
    brief: '创新、协调、绿色、开放、共享',
    icon: '💡',
    gradient: 'linear-gradient(135deg, #C62828 0%, #4A148C 100%)',
    points: [
      '创新是引领发展的第一动力',
      '协调是持续健康发展的内在要求',
      '绿色是永续发展的必要条件',
      '开放是国家繁荣发展的必由之路',
      '共享是中国特色社会主义的本质要求'
    ],
    goldQuote: '新发展理念是指挥棒、红绿灯，是引领高质量发展的根本遵循。'
  }
])

const goldSentences = ref([
  {
    text: '人民对美好生活的向往，就是我们的奋斗目标。',
    source: '习近平总书记',
    bg: 'linear-gradient(135deg, #D22630 0%, #8B0000 100%)'
  },
  {
    text: '江山就是人民，人民就是江山。',
    source: '党的二十大报告',
    bg: 'linear-gradient(135deg, #B71C1C 0%, #880E4F 100%)'
  },
  {
    text: '民族要复兴，乡村必振兴。',
    source: '习近平总书记',
    bg: 'linear-gradient(135deg, #E65100 0%, #BF360C 100%)'
  },
  {
    text: '绿水青山就是金山银山。',
    source: '习近平总书记',
    bg: 'linear-gradient(135deg, #2E7D32 0%, #1B5E20 100%)'
  },
  {
    text: '幸福都是奋斗出来的。',
    source: '习近平总书记',
    bg: 'linear-gradient(135deg, #C62828 0%, #4A148C 100%)'
  }
])

const totalPoints = computed(() => {
  return topics.value.reduce((sum, t) => sum + t.points.length, 0)
})
</script>

<style scoped>
.thought-study-page {
  min-height: 100vh;
  background: linear-gradient(180deg, #FFF8F0 0%, #FFFAFA 100%);
  padding-bottom: 40px;
}

.hero-section {
  position: relative;
  height: 240px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at top, rgba(255, 215, 0, 0.25) 0%, transparent 60%),
    linear-gradient(135deg, #D22630 0%, #8B0000 50%, #5D0000 100%);
}

.hero-bg::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image:
    radial-gradient(circle at 20% 30%, rgba(255, 215, 0, 0.15) 0%, transparent 30%),
    radial-gradient(circle at 80% 70%, rgba(255, 215, 0, 0.1) 0%, transparent 30%);
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  color: #fff;
  animation: fadeInUp 0.8s ease-out;
}

.party-emblem {
  font-size: 56px;
  color: #FFD700;
  text-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
  margin-bottom: 8px;
  animation: glow 2s ease-in-out infinite alternate;
}

@keyframes glow {
  from { text-shadow: 0 4px 12px rgba(255, 215, 0, 0.6); }
  to { text-shadow: 0 4px 20px rgba(255, 215, 0, 1), 0 0 30px rgba(255, 215, 0, 0.5); }
}

.hero-title {
  font-size: 28px;
  font-weight: bold;
  color: #FFD700;
  letter-spacing: 4px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
  margin-bottom: 8px;
}

.hero-subtitle {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.9);
  letter-spacing: 2px;
  margin-bottom: 12px;
}

.hero-decoration {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.star {
  color: #FFD700;
  font-size: 14px;
  animation: twinkle 1.5s ease-in-out infinite;
}

.star:nth-child(2) { animation-delay: 0.2s; }
.star:nth-child(3) { animation-delay: 0.4s; }
.star:nth-child(4) { animation-delay: 0.6s; }
.star:nth-child(5) { animation-delay: 0.8s; }

@keyframes twinkle {
  0%, 100% { opacity: 0.5; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.2); }
}

.stats-bar {
  display: flex;
  align-items: center;
  justify-content: space-around;
  background: #fff;
  margin: -30px 12px 16px;
  padding: 18px 12px;
  border-radius: 16px;
  box-shadow: 0 8px 24px rgba(210, 38, 48, 0.15);
  border: 1px solid rgba(210, 38, 48, 0.1);
  position: relative;
  z-index: 3;
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-num {
  font-size: 22px;
  font-weight: bold;
  background: linear-gradient(135deg, #D22630 0%, #8B0000 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.stat-label {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.stat-divider {
  width: 1px;
  height: 28px;
  background: linear-gradient(180deg, transparent, #eee, transparent);
}

.section-title {
  display: flex;
  align-items: center;
  padding: 16px 16px 12px;
  gap: 8px;
}

.title-bar {
  width: 4px;
  height: 18px;
  background: linear-gradient(180deg, #D22630 0%, #8B0000 100%);
  border-radius: 2px;
}

.title-text {
  font-size: 17px;
  font-weight: bold;
  color: #333;
}

.title-en {
  font-size: 10px;
  color: #D22630;
  opacity: 0.5;
  letter-spacing: 1px;
  margin-left: 4px;
}

.topic-list {
  padding: 0 12px;
}

.topic-card {
  background: #fff;
  border-radius: 14px;
  margin-bottom: 10px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(210, 38, 48, 0.06);
  border: 1px solid rgba(210, 38, 48, 0.08);
  animation: slideIn 0.5s ease-out forwards;
  opacity: 0;
}

@keyframes slideIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.topic-header {
  display: flex;
  align-items: center;
  padding: 14px;
  cursor: pointer;
  transition: background 0.2s;
}

.topic-header:active {
  background: #FFF8F0;
}

.topic-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  box-shadow: 0 4px 10px rgba(210, 38, 48, 0.2);
  flex-shrink: 0;
}

.topic-icon span {
  font-size: 24px;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.2));
}

.topic-info {
  flex: 1;
}

.topic-name {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.topic-desc {
  font-size: 11px;
  color: #999;
  line-height: 1.4;
}

.topic-detail {
  padding: 0 14px 14px;
  border-top: 1px dashed rgba(210, 38, 48, 0.15);
}

.detail-section {
  margin-top: 12px;
}

.detail-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  font-weight: bold;
  color: #D22630;
  margin-bottom: 8px;
}

.point-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.point-list li {
  position: relative;
  padding-left: 16px;
  font-size: 12px;
  color: #555;
  line-height: 1.7;
  margin-bottom: 4px;
}

.point-list li::before {
  content: '◆';
  position: absolute;
  left: 0;
  color: #D22630;
  font-size: 8px;
  top: 4px;
}

.gold-quote {
  background: linear-gradient(135deg, rgba(255, 215, 0, 0.08) 0%, rgba(210, 38, 48, 0.05) 100%);
  border-left: 3px solid #FFD700;
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 13px;
  color: #5D0000;
  line-height: 1.6;
  font-style: italic;
}

.expand-enter-active,
.expand-leave-active {
  transition: all 0.3s ease;
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  opacity: 0;
  max-height: 0;
}

.expand-enter-to,
.expand-leave-from {
  opacity: 1;
  max-height: 1000px;
}

.quote-scroll {
  display: flex;
  gap: 12px;
  padding: 0 12px;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  -webkit-overflow-scrolling: touch;
}

.quote-scroll::-webkit-scrollbar {
  display: none;
}

.quote-card {
  min-width: 240px;
  padding: 18px 16px;
  border-radius: 14px;
  color: #fff;
  position: relative;
  overflow: hidden;
  scroll-snap-align: start;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.15);
}

.quote-card::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 80px;
  height: 80px;
  background: radial-gradient(circle, rgba(255, 215, 0, 0.2) 0%, transparent 70%);
  border-radius: 50%;
  transform: translate(30%, -30%);
}

.quote-mark {
  font-size: 32px;
  color: rgba(255, 215, 0, 0.6);
  line-height: 1;
  font-family: serif;
  margin-bottom: 4px;
}

.quote-text {
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 8px;
  font-weight: 500;
}

.quote-source {
  font-size: 11px;
  opacity: 0.85;
  text-align: right;
}

.footer-pledge {
  margin: 24px 12px 0;
  padding: 20px;
  background: linear-gradient(135deg, #D22630 0%, #8B0000 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 6px 20px rgba(210, 38, 48, 0.3);
  position: relative;
  overflow: hidden;
}

.footer-pledge::before {
  content: '';
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 80% 20%, rgba(255, 215, 0, 0.15) 0%, transparent 40%),
    radial-gradient(circle at 20% 80%, rgba(255, 215, 0, 0.1) 0%, transparent 40%);
}

.pledge-icon {
  font-size: 36px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
  z-index: 1;
}

.pledge-text {
  color: #FFD700;
  font-size: 15px;
  font-weight: bold;
  letter-spacing: 1px;
  line-height: 1.6;
  z-index: 1;
}

.pledge-sub {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.9);
  font-weight: normal;
  letter-spacing: 0;
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
