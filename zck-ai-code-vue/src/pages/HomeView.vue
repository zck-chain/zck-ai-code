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

// 打字机效果相关变量
const placeholderText = '使用 NoCode 创建一个高效的小工具，帮我计算......'
const typedPlaceholder = ref('')
const isTyping = ref(true)

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

// 打字机效果实现
const typePlaceholder = async () => {
  typedPlaceholder.value = '';
  isTyping.value = true;

  for (let i = 0; i < placeholderText.length; i++) {
    typedPlaceholder.value = placeholderText.slice(0, i + 1);
    await new Promise(resolve => setTimeout(resolve, 80)); // 调整打字速度
  }

  // 打字完成后，延迟一段时间隐藏光标
  await new Promise(resolve => setTimeout(resolve, 1000));
  isTyping.value = false;
};

// 页面加载时初始化数据
onMounted(() => {
  if (loginUserStore.isLoggedIn()) {
    loadMyApps()
  }
  loadFeaturedApps()

  // 启动打字机效果
  typePlaceholder()

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
      <div class="hero-content">
        <!-- 网站标题 -->
        <div class="hero-header text-center mb-6">
          <h1 class="hero-title">
            NoCode
          </h1>
          <p class="hero-subtitle">与 AI 对话轻松创建应用和网站</p>
        </div>

        <!-- 用户提示词输入框 -->
        <div class="hero-input-container mb-4">
          <div class="input-wrapper">
            <textarea
              v-model="promptInput"
              :placeholder="typedPlaceholder + (isTyping ? '|' : '')"
              class="hero-textarea"
              rows="2"
            ></textarea>
            <div class="input-actions flex gap-2">
              <button class="action-btn">
                📁 上传
              </button>
              <button class="action-btn">
                ✨ 优化
              </button>
            </div>
            <button
              @click="createApp"
              class="create-btn"
              :disabled="loading"
            >
              {{ loading ? '⏳' : '↑' }}
            </button>
          </div>
          <div class="hero-examples">
            <span
              v-for="(example, index) in examplePrompts.slice(1)"
              :key="index"
              class="hero-tag"
              @click="promptInput = example"
            >
              {{ example }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 我的应用分页列表 -->
    <div v-if="loginUserStore.isLoggedIn()" class="apps-section w-full max-w-6xl mb-10">
      <h2 class="section-title text-center mb-6">我的作品</h2>
      <div class="apps-grid" v-if="myApps.length > 0">
        <div
          v-for="app in myApps"
          :key="app.id"
          class="case-card"
          @click="goToAppChat(app.id!)"
        >
          <div class="case-cover" :style="{ backgroundImage: `url(${app.cover || 'https://via.placeholder.com/400x300'})` }"></div>
          <div class="case-info">
            <h3 class="case-name">{{ app.appName || '未命名应用' }}</h3>
            <p class="case-description">{{ app.initPrompt ? app.initPrompt.substring(0, 60) + '...' : '暂无描述' }}</p>
            <div class="case-meta flex items-center justify-between">
              <div class="author-info flex items-center gap-2">
                <div class="author-avatar" :style="{ backgroundColor: getRandomColor(app.id!) }"></div>
                <span class="author-name">{{ app.authorName || '我' }}</span>
              </div>
              <span class="case-date">{{ formatDate(app.createTime) }}</span>
            </div>
            <div class="case-category">
              <span class="category-badge">{{ app.category || '个人应用' }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="empty-state text-center p-8 bg-white rounded-lg border border-gray-100" v-else>
        <p class="text-gray-500">暂无应用，开始创建您的第一个应用吧！</p>
      </div>
      <div class="pagination flex justify-center items-center gap-3 mt-6" v-if="myApps.length > 0">
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

    <!-- 案例广场 -->
    <div class="apps-section w-full max-w-6xl mb-10">
      <div class="case-square-header flex justify-between items-center mb-6">
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
      <div class="category-tags flex flex-wrap gap-3 mb-8">
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
      <div class="apps-grid" v-if="featuredApps.length > 0">
        <div
          v-for="app in featuredApps"
          :key="app.id"
          class="case-card"
          @click="goToAppChat(app.id!)"
        >
          <div class="case-cover" :style="{ backgroundImage: `url(${app.cover})` }"></div>
          <div class="case-info">
            <h3 class="case-name">{{ app.appName }}</h3>
            <p class="case-description">{{ app.initPrompt ? app.initPrompt.substring(0, 60) + '...' : '暂无描述' }}</p>
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
      <div class="empty-state text-center p-8 bg-white rounded-lg border border-gray-100" v-else>
        <p class="text-gray-500">暂无精选应用</p>
      </div>
    </div>

    <!-- 右下角互动小人 -->
    <div class="interactive-character">
      👋
    </div>
  </div>
</template>

<style scoped>
/* 全局变量 */
:root {
  --primary-color: #3b82f6;
  --primary-hover: #2563eb;
  --secondary-color: #60a5fa;
  --background-color: #f9fafb;
  --card-background: #ffffff;
  --text-primary: #111827;
  --text-secondary: #4b5563;
  --text-tertiary: #9ca3af;
  --border-color: #e5e7eb;
  --border-radius: 12px;
  --box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  --box-shadow-hover: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  --transition: all 0.3s ease;
  --spacing-xs: 0.25rem;
  --spacing-sm: 0.5rem;
  --spacing-md: 1rem;
  --spacing-lg: 1.5rem;
  --spacing-xl: 2rem;
  --spacing-xxl: 3rem;
  --spacing-xxxl: 4rem;
}

/* 封面图片样式 */
.hero-section {
  position: relative;
  height: 450px;
  margin-bottom: var(--spacing-xxxl);
  overflow: hidden;
  background: linear-gradient(135deg, #f5f7ff 0%, #eef2ff 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: var(--box-shadow);
}

.hero-content {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 800px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-xl);
  color: var(--text-primary);
  text-align: center;
}

.hero-header {
  margin-bottom: var(--spacing-lg);
}

.hero-title {
  color: var(--text-primary);
  font-size: 4rem;
  margin-bottom: var(--spacing-sm);
  font-weight: 800;
  letter-spacing: -0.025em;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: fadeInDown 0.8s ease-out;
}

.hero-subtitle {
  color: var(--text-secondary);
  font-size: 1.25rem;
  margin-bottom: var(--spacing-xl);
  font-weight: 400;
  line-height: 1.6;
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.hero-input-container {
  max-width: 700px;
  width: 100%;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

.input-wrapper {
  position: relative;
  width: 100%;
  background-color: var(--card-background);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
  overflow: hidden;
  transition: var(--transition);
}

.input-wrapper:focus-within {
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  border-color: var(--primary-color);
  transform: translateY(-1px);
}

.hero-textarea {
  background-color: var(--card-background);
  border: none;
  box-shadow: none;
  width: 100%;
  min-height: 80px;
  padding: 1.25rem;
  font-size: 1rem;
  line-height: 1.5;
  resize: none;
  border-radius: var(--border-radius);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  color: var(--text-primary);
  transition: var(--transition);
}

.hero-textarea::placeholder {
  color: var(--text-tertiary);
  font-style: italic;
}

.hero-textarea:focus {
  outline: none;
}

.input-actions {
  padding: 0 1.25rem 1.25rem;
  display: flex;
  gap: 0.75rem;
}

.action-btn {
  background-color: var(--card-background);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: var(--transition);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.action-btn:hover {
  background-color: #f3f4f6;
  border-color: var(--primary-color);
  color: var(--primary-color);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.1);
}

.create-btn {
  position: absolute;
  right: 1rem;
  bottom: 1rem;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  border: none;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: var(--transition);
  color: white;
  font-size: 1.25rem;
  box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.3);
}

.create-btn:hover {
  background: linear-gradient(135deg, var(--primary-hover), var(--primary-color));
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(59, 130, 246, 0.4);
}

.create-btn:disabled {
  background: #93c5fd;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.hero-examples {
  margin-top: var(--spacing-xl);
  display: flex;
  justify-content: center;
  gap: 1rem;
  flex-wrap: wrap;
  animation: fadeInUp 0.8s ease-out 0.6s both;
}

.hero-tag {
  background-color: var(--card-background);
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
  border-radius: 20px;
  padding: 0.375rem 1rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: var(--transition);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.hero-tag:hover {
  background-color: #eff6ff;
  border-color: var(--primary-color);
  color: var(--primary-color);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.1);
}

/* 应用卡片样式 */
.app-cover {
  height: 200px;
  background-size: cover;
  background-position: center;
  background-color: #f3f4f6;
  border-radius: 8px 8px 0 0;
  transition: var(--transition);
}

.app-name {
  font-size: 1.125rem;
  font-weight: 600;
  margin: 1rem;
  color: var(--text-primary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.app-time {
  font-size: 0.875rem;
  color: var(--text-tertiary);
  margin: 0 1rem 1rem;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 响应式网格布局 */
.apps-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .apps-grid {
    grid-template-columns: repeat(3, 1fr);
    gap: 1.25rem;
  }
}

@media (max-width: 900px) {
  .apps-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }
}

@media (max-width: 600px) {
  .apps-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
}

/* 案例广场样式 */
.case-square-header {
  margin-bottom: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  animation: fadeInDown 0.8s ease-out;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.section-title::after {
  content: '';
  display: inline-block;
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), transparent);
  border-radius: 3px;
}

.filter-select {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  background-color: var(--card-background);
  color: var(--text-secondary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  transition: var(--transition);
  cursor: pointer;
}

.filter-select:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  border-color: var(--primary-color);
}

/* 分类标签样式 */
.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin-bottom: 2rem;
  animation: fadeInUp 0.8s ease-out 0.2s both;
}

.category-tag {
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: 20px;
  font-size: 0.875rem;
  background-color: var(--card-background);
  color: var(--text-secondary);
  cursor: pointer;
  transition: var(--transition);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  position: relative;
  overflow: hidden;
}

.category-tag:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
  background-color: #eff6ff;
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.1);
}

.category-tag.active {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  border-color: var(--primary-color);
  box-shadow: 0 4px 6px rgba(59, 130, 246, 0.3);
}

.category-tag.more {
  border: 1px dashed var(--border-color);
  color: var(--text-tertiary);
}

.category-tag.more:hover {
  border-color: var(--primary-color);
  color: var(--primary-color);
  background-color: #eff6ff;
}

/* 案例卡片样式 */
.case-card {
  background-color: var(--card-background);
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: var(--box-shadow);
  transition: var(--transition);
  border: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  height: 100%;
  position: relative;
  animation: fadeInUp 0.6s ease-out;
}

.case-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--box-shadow-hover);
  border-color: var(--primary-color);
}

.case-card:hover .case-cover {
  transform: scale(1.03);
}

.case-cover {
  height: 160px;
  background-size: cover;
  background-position: center;
  background-color: #f3f4f6;
  border-radius: var(--border-radius) var(--border-radius) 0 0;
  transition: var(--transition);
  overflow: hidden;
}

.case-cover::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 160px;
  background: linear-gradient(180deg, transparent 60%, rgba(0, 0, 0, 0.1) 100%);
  pointer-events: none;
}

.case-info {
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  flex: 1;
}

.case-name {
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  line-height: 1.4;
  transition: var(--transition);
}

.case-card:hover .case-name {
  color: var(--primary-color);
}

.case-description {
  font-size: 0.875rem;
  margin: 0;
  color: var(--text-secondary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  line-height: 1.5;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.case-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0.75rem;
  font-size: 0.75rem;
  color: var(--text-tertiary);
}

.author-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.author-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  transition: var(--transition);
}

.case-card:hover .author-avatar {
  transform: scale(1.1);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.author-name {
  color: var(--text-tertiary);
  font-size: 0.75rem;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.case-date {
  color: var(--text-tertiary);
  font-size: 0.75rem;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.case-category {
  display: flex;
  align-items: center;
}

.category-badge {
  padding: 0.25rem 0.75rem;
  background-color: #f9fafb;
  border: 1px solid var(--border-color);
  border-radius: 12px;
  font-size: 0.75rem;
  color: var(--text-secondary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  transition: var(--transition);
}

.case-card:hover .category-badge {
  background-color: #eff6ff;
  border-color: var(--primary-color);
  color: var(--primary-color);
}

/* 我的应用样式 */
.apps-section {
  margin: 0 auto;
  padding: 0 1.25rem;
  padding-top: 3rem;
  padding-bottom: 3rem;
}

.apps-section h2 {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  margin-bottom: 1.5rem;
  text-align: center;
  position: relative;
  animation: fadeInDown 0.8s ease-out;
}

.apps-section h2::after {
  content: '';
  display: block;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), transparent);
  border-radius: 3px;
  margin: 0.5rem auto 0;
}

.card-reference {
  background-color: var(--card-background);
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: var(--box-shadow);
  transition: var(--transition);
  border: 1px solid var(--border-color);
}

.card-reference:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  border-color: var(--primary-color);
}

.pagination {
  margin-top: 1.5rem;
  justify-content: center;
  animation: fadeInUp 0.8s ease-out 0.4s both;
}

.btn-secondary {
  background-color: var(--card-background);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  color: var(--text-secondary);
  cursor: pointer;
  transition: var(--transition);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-secondary:hover:not(:disabled) {
  background-color: #eff6ff;
  border-color: var(--primary-color);
  color: var(--primary-color);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.1);
}

.btn-secondary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.page-info {
  font-size: 0.875rem;
  color: var(--text-tertiary);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

/* 空状态样式 */
.empty-state {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: var(--card-background);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  padding: 4rem 2rem;
  text-align: center;
  margin: 2rem 0;
  box-shadow: var(--box-shadow);
  transition: var(--transition);
  animation: fadeInUp 0.8s ease-out;
}

.empty-state:hover {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

.empty-state p {
  color: var(--text-tertiary);
  font-size: 1rem;
  margin: 0;
}

/* 全局样式 */
.home-container {
  min-height: 100vh;
  background-color: var(--background-color);
  position: relative;
}

/* 互动小人样式 */
.interactive-character {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  width: 60px;
  height: 60px;
  background-color: var(--primary-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  transition: var(--transition);
  box-shadow: 0 4px 6px rgba(59, 130, 246, 0.3);
  z-index: 1000;
  animation: bounce 2s infinite;
}

.interactive-character:hover {
  transform: scale(1.1) translateY(-2px);
  box-shadow: 0 10px 15px rgba(59, 130, 246, 0.4);
  background-color: var(--primary-hover);
  animation: none;
}

.interactive-character:hover::before {
  content: '你好！';
  position: absolute;
  bottom: 100%;
  right: 0;
  background-color: var(--text-primary);
  color: white;
  padding: 0.5rem 0.75rem;
  border-radius: 8px;
  font-size: 0.875rem;
  white-space: nowrap;
  margin-bottom: 0.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: -1;
}

.interactive-character:hover::after {
  content: '';
  position: absolute;
  bottom: 100%;
  right: 1rem;
  border-width: 0.5rem;
  border-style: solid;
  border-color: var(--text-primary) transparent transparent transparent;
  margin-bottom: -0.5rem;
  z-index: -1;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .hero-section {
    height: 500px;
    padding: 0 1.25rem;
  }

  .hero-title {
    font-size: 3rem;
  }

  .hero-subtitle {
    font-size: 1.125rem;
  }

  .hero-input-container {
    max-width: 100%;
  }

  .input-actions {
    flex-wrap: wrap;
  }

  .category-tags {
    justify-content: center;
  }

  .apps-section {
    padding: 0 1rem;
    padding-top: 2rem;
    padding-bottom: 2rem;
  }

  .interactive-character {
    bottom: 1.5rem;
    right: 1.5rem;
    width: 50px;
    height: 50px;
    font-size: 1.25rem;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 2.25rem;
  }

  .hero-subtitle {
    font-size: 1rem;
  }

  .hero-textarea {
    font-size: 0.875rem;
    min-height: 70px;
  }

  .action-btn {
    font-size: 0.75rem;
    padding: 0.375rem 0.75rem;
  }

  .create-btn {
    width: 42px;
    height: 42px;
    font-size: 1.125rem;
  }

  .hero-tag {
    font-size: 0.75rem;
    padding: 0.25rem 0.75rem;
  }

  .section-title {
    font-size: 1.25rem;
  }

  .category-tag {
    font-size: 0.75rem;
    padding: 0.375rem 0.75rem;
  }

  .interactive-character {
    bottom: 1rem;
    right: 1rem;
    width: 45px;
    height: 45px;
    font-size: 1.125rem;
  }
}

/* 动画定义 */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

/* 响应式网格布局 */
@media (max-width: 1200px) {
  .apps-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .apps-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .apps-grid {
    grid-template-columns: 1fr;
  }

  .case-cover {
    height: 150px;
  }

  .case-cover::before {
    height: 150px;
  }
}
</style>
