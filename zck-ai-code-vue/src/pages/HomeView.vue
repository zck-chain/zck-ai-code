<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import api from '@/api'
import { useLoginUserStore } from '@/stores/loginUser'

// 状态管理
const router = useRouter()
const loginUserStore = useLoginUserStore()
const promptInput = ref('')
const myApps = ref<API.App[]>([])
const featuredApps = ref<API.App[]>([])
const myAppsPage = ref(1)
const featuredAppsPage = ref(1)
const pageSize = 20
const loading = ref(false)

// 示例提示词
const examplePrompts = [
  '使用 NoCode 创建一个高效的小工具，帮我计算...',
  '波普风电商页面',
  '企业网站',
  '电商运营后台',
  '暗黑话题社区'
]

// 创建应用
const createApp = async () => {
  if (!promptInput.value.trim()) {
    message.warning('请输入提示词')
    return
  }

  // 检查登录状态
  if (!loginUserStore.isLoggedIn()) {
    // 保存当前操作路径，登录后返回
    const redirectPath = encodeURIComponent(`/app/chat/new?prompt=${encodeURIComponent(promptInput.value)}`)
    router.push(`/user/login?redirect=${redirectPath}`)
    return
  }

  try {
    loading.value = true
    const response = await api.appController.createApp({
      initPrompt: promptInput.value
    })

    if (response.data.code === 0 && response.data.data) {
      // 跳转到应用生成对话页
      router.push(`/app/chat/${response.data.data}`)
    } else {
      message.error('创建应用失败：' + response.data.message)
    }
  } catch (error) {
    console.error('创建应用失败', error)
    message.error('创建应用失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 加载我的应用列表
const loadMyApps = async (page: number = 1) => {
  try {
    const response = await api.appController.listMyAppByPage({
      pageNum: page,
      pageSize: pageSize
    })

    if (response.data.code === 0 && response.data.data) {
      myApps.value = response.data.data.records || []
      myAppsPage.value = page
    }
  } catch (error) {
    console.error('加载我的应用失败', error)
  }
}

// 加载精选应用列表
const loadFeaturedApps = async (page: number = 1) => {
  try {
    const response = await api.appController.listFeaturedAppByPage({
      pageNum: page,
      pageSize: pageSize
    })

    if (response.data.code === 0 && response.data.data) {
      featuredApps.value = response.data.data.records || []
      featuredAppsPage.value = page
    }
  } catch (error) {
    console.error('加载精选应用失败', error)
  }
}

// 跳转到应用对话页
const goToAppChat = (appId: number) => {
  // 检查登录状态
  if (!loginUserStore.isLoggedIn()) {
    // 保存当前操作路径，登录后返回
    const redirectPath = encodeURIComponent(`/app/chat/${appId}`)
    router.push(`/user/login?redirect=${redirectPath}`)
    return
  }

  router.push(`/app/chat/${appId}`)
}

// 辅助函数：生成随机颜色
const getRandomColor = (id: number | undefined): string => {
  const colors = [
    '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7',
    '#DDA0DD', '#98D8C8', '#F7DC6F', '#BB8FCE', '#85C1E9'
  ];
  const index = Math.abs((id || 0) % colors.length);
  return colors[index] as string;
};

// 辅助函数：格式化日期
const formatDate = (dateString: string | undefined): string => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

// 页面加载时初始化数据
onMounted(() => {
  if (loginUserStore.isLoggedIn()) {
    loadMyApps()
  }
  loadFeaturedApps()

  // 添加假数据到精选案例，用于查看样式效果
  setTimeout(() => {
    if (featuredApps.value.length === 0) {
      featuredApps.value = [
        {
          id: 1,
          appName: 'NoCode创新挑战赛官网',
          cover: 'https://img95.699pic.com/photo/40182/0485.jpg_wh300.jpg',
          createTime: '2026-01-08',
          codeGenType: 'web',
          authorName: '探索',
          category: '网站'
        },
        {
          id: 2,
          appName: '新居民',
          cover: 'https://img95.699pic.com/photo/40006/2382.jpg_wh300.jpg',
          createTime: '2025-10-31',
          codeGenType: 'web',
          authorName: 'jdbkwjxkmkg',
          category: '用户应用'
        },
        {
          id: 3,
          appName: '循环绿意',
          cover: 'https://img95.699pic.com/photo/50062/5740.jpg_wh300.jpg',
          createTime: '2025-11-01',
          codeGenType: 'web',
          authorName: 'hhNSB58613682',
          category: '网站'
        },
        {
          id: 4,
          appName: 'WePin (拼拼)',
          cover: 'https://img95.699pic.com/photo/40177/7519.jpg_wh300.jpg',
          createTime: '2025-09-29',
          codeGenType: 'web',
          authorName: 'krd1684909708',
          category: '用户应用'
        },
        {
          id: 5,
          appName: 'Echo回声',
          cover: 'https://img95.699pic.com/photo/50045/1369.jpg_wh300.jpg',
          createTime: '2025-11-11',
          codeGenType: 'web',
          authorName: '栀子相惜quokka',
          category: '工具'
        },
        {
          id: 6,
          appName: '办公用品管理平台',
          cover: 'https://img95.699pic.com/photo/50049/5886.jpg_wh300.jpg',
          createTime: '2025-11-07',
          codeGenType: 'web',
          authorName: 'NoCode小助手',
          category: '管理平台'
        },
        {
          id: 7,
          appName: '双国时光',
          cover: 'https://img95.699pic.com/photo/50045/0842.jpg_wh300.jpg',
          createTime: '2025-08-21',
          codeGenType: 'web',
          authorName: '骑手1288',
          category: '网站'
        },
        {
          id: 8,
          appName: '智能饮食推荐面板',
          cover: 'https://img95.699pic.com/photo/50035/1510.jpg_wh300.jpg',
          createTime: '2025-09-09',
          codeGenType: 'web',
          authorName: 'JZ237',
          category: '工具'
        }
      ]
    }
  }, 500)
})
</script>

<template>
  <div class="home-container">
    <!-- 封面图片 -->
    <div class="hero-section">
      <div class="hero-bg" style="background-image: url('https://img95.699pic.com/photo/40182/0485.jpg_wh1200.jpg');"></div>
      <div class="hero-content">
        <!-- 网站标题 -->
        <div class="header text-center mb-5">
          <h1 class="title-reference">
            一句话 <span class="logo-reference">🐱</span> 呈所想
          </h1>
          <p class="subtitle-reference">与 AI 对话轻松创建应用和网站</p>
        </div>

        <!-- 用户提示词输入框 -->
        <div class="container-reference mb-5">
          <div class="input-container-reference">
            <textarea
              v-model="promptInput"
              placeholder="使用 NoCode 创建一个高效的小工具，帮我计算......"
              class="input-reference"
              rows="3"
            ></textarea>
            <div class="input-actions flex gap-3">
              <button class="action-btn-reference">
                📁 上传
              </button>
              <button class="action-btn-reference">
                ✨ 优化
              </button>
            </div>
            <button
              @click="createApp"
              class="create-btn-reference"
              :disabled="loading"
              style="position: absolute; right: 16px; bottom: 16px;"
            >
              {{ loading ? '⏳' : '↑' }}
            </button>
          </div>
          <div class="examples-reference">
            <span
              v-for="(example, index) in examplePrompts.slice(1)"
              :key="index"
              class="tag-reference"
              @click="promptInput = example"
            >
              {{ example }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 案例广场 -->
    <div class="apps-section w-full max-w-6xl mb-5">
      <div class="case-square-header flex justify-between items-center mb-5">
        <h2 class="section-title text-left mb-0">案例广场</h2>
        <div class="filter-container flex items-center gap-3">
          <select class="filter-select border border-gray-300 rounded-md px-3 py-2 text-sm">
            <option>默认排序</option>
            <option>最新发布</option>
            <option>最受欢迎</option>
          </select>
        </div>
      </div>

      <!-- 分类标签 -->
      <div class="category-tags flex flex-wrap gap-2 mb-5">
        <button class="category-tag active">全部</button>
        <button class="category-tag">工具</button>
        <button class="category-tag">网站</button>
        <button class="category-tag">数据分析</button>
        <button class="category-tag">活动页面</button>
        <button class="category-tag">管理平台</button>
        <button class="category-tag">用户应用</button>
        <button class="category-tag">个人管理</button>
        <button class="category-tag">游戏</button>
        <button class="category-tag more">
          📋 全部类别
        </button>
      </div>

      <!-- 案例卡片网格 -->
      <div class="apps-grid grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4" v-if="featuredApps.length > 0">
        <div
          v-for="app in featuredApps"
          :key="app.id"
          class="case-card"
          @click="goToAppChat(app.id!)"
        >
          <div class="case-cover" :style="{ backgroundImage: `url(${app.cover})` }"></div>
          <div class="case-info">
            <h3 class="case-name">{{ app.appName }}</h3>
            <div class="case-meta flex items-center justify-between">
              <div class="author-info flex items-center gap-2">
                <div class="author-avatar" :style="{ backgroundColor: getRandomColor(app.id!) }"></div>
                <span class="author-name">{{ app.authorName }}</span>
              </div>
              <span class="case-date">{{ formatDate(app.createTime) }}</span>
            </div>
            <div class="case-category">
              <span class="category-badge">{{ app.category }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="empty-state text-center p-5 bg-white rounded-lg shadow-md" v-else>
        <p>暂无精选应用</p>
      </div>
    </div>

    <!-- 我的应用分页列表 -->
    <div v-if="loginUserStore.isLoggedIn()" class="apps-section w-full max-w-6xl mb-5">
      <h2 class="section-title text-center mb-4">我的作品</h2>
      <div class="apps-grid grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4" v-if="myApps.length > 0">
        <div
          v-for="app in myApps"
          :key="app.id"
          class="card-reference"
          @click="goToAppChat(app.id!)"
        >
          <div class="app-cover" :style="{ backgroundImage: `url(${app.cover || 'https://via.placeholder.com/400x300'})` }"></div>
          <h3 class="app-name">{{ app.appName || '未命名应用' }}</h3>
          <p class="app-time">创建于 {{ new Date(app.createTime || '').toLocaleString('zh-CN', { month: 'numeric', day: 'numeric', hour: '2-digit', minute: '2-digit' }) }}</p>
        </div>
      </div>
      <div class="empty-state text-center p-5 bg-white rounded-lg shadow-md" v-else>
        <p>暂无应用，开始创建您的第一个应用吧！</p>
      </div>
      <div class="pagination flex justify-center items-center gap-3" v-if="myApps.length > 0">
        <button
          @click="loadMyApps(myAppsPage - 1)"
          :disabled="myAppsPage === 1"
          class="btn-secondary"
        >
          上一页
        </button>
        <span class="page-info">第 {{ myAppsPage }} 页</span>
        <button
          @click="loadMyApps(myAppsPage + 1)"
          class="btn-secondary"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 封面图片样式 */
.hero-section {
  position: relative;
  height: 600px;
  margin-bottom: var(--spacing-xxxl);
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  filter: brightness(0.7);
  z-index: 1;
}

.hero-content {
  position: relative;
  z-index: 2;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: var(--spacing-xl);
  color: white;
}

.hero-content .header h1 {
  color: white;
  font-size: var(--font-size-xxxxl);
  margin-bottom: var(--spacing-md);
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.hero-content .header p {
  color: rgba(255, 255, 255, 0.9);
  font-size: var(--font-size-lg);
  margin-bottom: var(--spacing-xl);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.hero-content .container-reference {
  max-width: 800px;
  width: 100%;
}

.hero-content .input-reference {
  background-color: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.hero-content .examples-reference {
  margin-top: var(--spacing-md);
}

.hero-content .tag-reference {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.hero-content .tag-reference:hover {
  background-color: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
}

/* 应用卡片样式 */
.app-cover {
  height: 200px;
  background-size: cover;
  background-position: center;
  background-color: var(--border-light);
}

.app-name {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  margin: var(--spacing-md);
  color: var(--text-primary);
}

.app-time {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0 var(--spacing-md) var(--spacing-md);
}

/* 响应式网格布局 */
.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
}

@media (max-width: var(--breakpoint-md)) {
  .apps-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

/* 案例广场样式 */
.case-square-header {
  margin-bottom: var(--spacing-lg);
}

.filter-select {
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  padding: var(--spacing-xs) var(--spacing-md);
  font-size: var(--font-size-sm);
  background-color: var(--background-default);
  color: var(--text-primary);
}

/* 分类标签样式 */
.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-xs);
  margin-bottom: var(--spacing-lg);
}

.category-tag {
  padding: var(--spacing-xs) var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-full);
  font-size: var(--font-size-sm);
  background-color: var(--background-default);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.category-tag:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
}

.category-tag.active {
  background-color: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.category-tag.more {
  border: 1px dashed var(--border-color);
}

/* 案例卡片样式 */
.case-card {
  background-color: var(--background-default);
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-normal);
}

.case-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.case-cover {
  height: 180px;
  background-size: cover;
  background-position: center;
  background-color: var(--border-light);
}

.case-info {
  padding: var(--spacing-md);
}

.case-name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-bold);
  margin: 0 0 var(--spacing-sm);
  color: var(--text-primary);
}

.case-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-sm);
  font-size: var(--font-size-xs);
}

.author-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.author-avatar {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.author-name {
  color: var(--text-secondary);
}

.case-date {
  color: var(--text-tertiary);
}

.case-category {
  display: flex;
  align-items: center;
}

.category-badge {
  padding: 2px 8px;
  background-color: var(--background-light);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
}

/* 响应式网格布局 */
@media (max-width: var(--breakpoint-lg)) {
  .apps-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

@media (max-width: var(--breakpoint-md)) {
  .apps-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }

  .case-cover {
    height: 150px;
  }

  .category-tags {
    gap: 4px;
  }

  .category-tag {
    padding: 4px 12px;
    font-size: 12px;
  }
}
</style>
