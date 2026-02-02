<template>
  <div class="home-container">
    <!-- 封面图片 -->
    <div class="hero-section">
      <div class="hero-content">
        <!-- 网站标题 -->
        <div class="hero-header text-center mb-6">
          <h1 class="hero-title">
            AiCode
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
        >
          <div class="case-cover" :style="{ backgroundImage: `url(${app.cover || 'https://via.placeholder.com/400x300'})` }">
            <div class="case-cover-actions">
              <button
                @click="goToAppChat(app.id!)"
                class="btn-view-chat"
              >
                查看对话
              </button>
              <button
                v-if="app.deployKey"
                @click="viewWork(app.deployKey)"
                class="btn-view-work"
              >
                查看作品
              </button>
            </div>
          </div>
          <div class="case-info">
            <h3 class="case-name">{{ app.appName || '未命名应用' }}</h3>
            <div class="case-meta">
              <span class="author-name">{{ app.authorName || '我' }}</span>
            </div>
            <div class="case-footer">
              <span class="programming-tag">编程号</span>
              <span class="codefather-tag">codefather</span>
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
        <span class="page-info">第 {{ myAppsPage }} 页，共 {{ myAppsTotalPage }} 页</span>
        <button
          @click="() => {
            if (myAppsPage >= myAppsTotalPage) {
              message.info('到最后一页了');
            } else {
              loadMyApps(myAppsPage + 1);
            }
          }"
          :disabled="myAppsPage >= myAppsTotalPage"
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
        >
          <div class="case-cover" :style="{ backgroundImage: `url(${app.cover})` }">
            <div class="case-cover-actions">
              <button
                @click="goToAppChat(app.id!)"
                class="btn-view-chat"
              >
                查看对话
              </button>
              <button
                v-if="app.deployKey"
                @click="viewWork(app.deployKey)"
                class="btn-view-work"
              >
                查看作品
              </button>
            </div>
          </div>
          <div class="case-info">
            <h3 class="case-name">{{ app.appName }}</h3>
            <div class="case-meta">
              <span class="author-name">{{ app.authorName }}</span>
            </div>
            <div class="case-footer">
              <span class="programming-tag">编程号</span>
              <span class="codefather-tag">codefather</span>
            </div>
          </div>
        </div>
      </div>
      <div class="empty-state text-center p-8 bg-white rounded-lg border border-gray-100" v-else>
        <p class="text-gray-500">暂无精选应用</p>
      </div>
      <div class="pagination flex justify-center items-center gap-3 mt-6" v-if="featuredApps.length > 0">
        <button
          @click="loadFeaturedApps(featuredAppsPage - 1)"
          :disabled="featuredAppsPage === 1"
          class="btn-secondary"
        >
          上一页
        </button>
        <span class="page-info">第 {{ featuredAppsPage }} 页，共 {{ featuredAppsTotalPage }} 页</span>
        <button
          @click="() => {
            if (featuredAppsPage >= featuredAppsTotalPage) {
              message.info('到最后一页了');
            } else {
              loadFeaturedApps(featuredAppsPage + 1);
            }
          }"
          :disabled="featuredAppsPage >= featuredAppsTotalPage"
          class="btn-secondary"
        >
          下一页
        </button>
      </div>
    </div>

    <!-- 右下角互动小人 -->
    <div class="interactive-character">
      👋
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import api from '@/api'
import { useLoginUserStore } from '@/stores/loginUser'
import '@/styles/home.styles.css'

// 状态管理
const router = useRouter()
const loginUserStore = useLoginUserStore()
const promptInput = ref('')
const myApps = ref<API.App[]>([])
const featuredApps = ref<API.App[]>([])
const myAppsPage = ref(1)
const featuredAppsPage = ref(1)
const myAppsTotalPage = ref(1)
const featuredAppsTotalPage = ref(1)
const pageSize = 8
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
    message.warning('请先登录')
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
      myAppsTotalPage.value = response.data.data.totalPage || 1
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
      featuredAppsTotalPage.value = response.data.data.totalPage || 1
    }
  } catch (error) {
    console.error('加载精选应用失败', error)
  }
}

// 跳转到应用对话页
const goToAppChat = (appId: string | number) => {
  // 检查登录状态
  if (!loginUserStore.isLoggedIn()) {
    // 保存当前操作路径，登录后返回
    const redirectPath = encodeURIComponent(`/app/chat/${appId}?view=1`)
    router.push(`/user/login?redirect=${redirectPath}`)
    return
  }

  router.push(`/app/chat/${appId}?view=1`)
}

// 查看作品
const viewWork = (deployKey: string) => {
  // 确保在浏览器环境中执行
  if (typeof window !== 'undefined') {
    const url = `http://localhost/${deployKey}`;

    try {
      // 测试window.open是否可用
      if (typeof window.open === 'function') {
        const result = window.open(url, '_blank');
        if (result === null) {
          alert('打开新窗口失败，可能被浏览器弹窗拦截器阻止，请检查浏览器设置');
        }
      } else {
        alert('打开新窗口功能不可用');
      }
    } catch (error) {
      alert('打开新窗口时出错，请稍后重试');
    }
  } else {
    alert('当前环境无法打开新窗口');
  }
}



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
})
</script>
