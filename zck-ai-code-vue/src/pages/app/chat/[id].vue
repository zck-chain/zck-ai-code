<template>
  <div class="app-chat-container">
    <!-- 顶部栏 -->
    <div class="top-bar">
      <div class="app-info">
        <h1 class="app-title">{{ appName }}</h1>
      </div>
      <div class="top-actions">
        <!-- 应用详情按钮 -->
        <div class="app-details-container">
          <button
            @click="toggleAppDetails"
            class="details-btn"
          >
            应用详情
          </button>
          <!-- 应用详情悬浮窗 -->
          <div v-if="showAppDetails" class="app-details-popup">
            <div class="popup-header">
              <h3>应用信息</h3>
              <button @click="showAppDetails = false" class="close-btn">×</button>
            </div>
            <div class="popup-content">
              <!-- 应用基础信息 -->
              <div class="app-basic-info">
                <h4>基础信息</h4>
                <div class="info-item">
                  <span class="info-label">创建者：</span>
                  <div class="creator-info">
                    <div class="creator-avatar" :style="{ backgroundColor: getCreatorAvatarColor() }"></div>
                    <span class="creator-name">{{ appCreatorName || '未知' }}</span>
                  </div>
                </div>
                <div class="info-item">
                  <span class="info-label">创建时间：</span>
                  <span class="info-value">{{ formattedCreateTime || '未知' }}</span>
                </div>
              </div>
              <!-- 操作栏（仅本人或管理员可见） -->
              <div v-if="isOwner" class="app-actions">
                <h4>操作</h4>
                <div class="action-buttons">
                  <button @click="editApp" class="action-btn edit-btn">修改</button>
                  <button @click="deleteApp" class="action-btn delete-btn">删除</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 部署按钮 -->
        <button
          @click="deployApp"
          class="deploy-btn"
          :disabled="deploying || !codeGenerated"
        >
          {{ deploying ? '部署中...' : (deployedUrl ? '已部署' : '部署应用') }}
        </button>
      </div>
    </div>

    <!-- 核心内容区域 -->
    <div class="main-content">
      <!-- 左侧对话区域 -->
      <div class="chat-section">
        <!-- 消息区域 -->
        <div class="messages-container">
          <div
            v-for="message in messages"
            :key="message.id"
            :class="['message', message.isUser ? 'user-message' : 'ai-message']"
          >
            <div class="message-content">
              <!-- AI头像 -->
              <template v-if="!message.isUser">
                <div class="message-avatar">
                  <img src="@/assets/logo.svg" alt="AI" class="avatar" />
                </div>
              </template>
              <div class="message-bubble">
                <template v-if="message.isUser">
                  {{ message.content }}
                </template>
                <template v-else>
                  <div v-html="renderMarkdown(message.content)"></div>
                </template>
              </div>
              <!-- 用户头像位置（留空） -->
              <template v-if="message.isUser">
                <div class="message-avatar"></div>
              </template>
            </div>
          </div>
          <div v-if="loading" class="loading-indicator">
            <span>AI 正在生成...</span>
          </div>
        </div>

        <!-- 用户消息输入框 -->
        <div class="input-container">
          <div
            class="input-wrapper"
            @mouseenter="showEditTooltip = !isOwner"
            @mouseleave="showEditTooltip = false"
          >
            <input
              v-model="userInput"
              type="text"
              placeholder="请输入您的需求..."
              class="user-input"
              @keyup.enter="sendMessage"
              :disabled="loading || !isOwner"
            />
            <div v-if="showEditTooltip" class="input-tooltip">
              无法在别人的作品下对话哦~
            </div>
          </div>
          <button
            @click="sendMessage"
            class="send-btn"
            :disabled="loading || !userInput.trim() || !isOwner"
          >
            发送
          </button>
        </div>
      </div>

      <!-- 右侧网页展示区域 -->
      <div class="preview-section">
        <div class="preview-header">
          <h2>生成的网站效果</h2>
        </div>
        <div class="preview-content">
          <div v-if="!codeGenerated" class="preview-placeholder">
            <p>网站文件生成中，请等待...</p>
          </div>
          <iframe
            v-else
            :src="getWebsitePreviewUrl()"
            class="website-preview"
            title="网站预览"
            frameborder="0"
          ></iframe>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import api from '@/api'
import { API_CONFIG } from '@/config/api'
import { useLoginUserStore } from '@/stores/loginUser'
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'

// 配置marked使用highlight.js进行代码高亮
marked.setOptions({
  highlight: function(code, lang) {
    const language = hljs.getLanguage(lang) ? lang : 'plaintext'
    return hljs.highlight(code, { language }).value
  },
  langPrefix: 'hljs language-',
  breaks: true,
  gfm: true
})

// 渲染Markdown内容
const renderMarkdown = (content: string) => {
  if (!content) return ''
  return marked(content)
}

// 状态管理
const route = useRoute()
const router = useRouter()
const loginUserStore = useLoginUserStore()
const appId = ref<string>(route.params.id as string)
const appName = ref('')
const messages = ref<{ id: number; content: string; isUser: boolean }[]>([])
const userInput = ref('')
const loading = ref(false)
const deploying = ref(false)
const deployedUrl = ref('')
const codeGenerated = ref(false)
const codeGenType = ref('')
const eventSource = ref<EventSource | null>(null)
const isOwner = ref(false)
const showEditTooltip = ref(false)
// 应用详情相关状态
const showAppDetails = ref(false)
const appCreatorName = ref('')
const appCreateTime = ref('')
const formattedCreateTime = ref('')

// 加载应用信息
const loadAppInfo = async () => {
  try {
    const response = await api.appController.getAppById({
      id: appId.value
    })

    if (response.data.code === 0 && response.data.data) {
      const appData = response.data.data
      appName.value = appData.appName || '未命名应用'
      codeGenType.value = appData.codeGenType || ''
      appCreatorName.value = appData.authorName || '未知'
      appCreateTime.value = appData.createTime || ''
      formattedCreateTime.value = formatDate(appData.createTime)

      // 权限校验：检查当前用户是否是应用的所有者
      if (loginUserStore.isLoggedIn()) {
        const currentUserId = loginUserStore.user?.id || ''
        const appOwnerId = appData.authorId || ''
        isOwner.value = currentUserId === appOwnerId
      }

      // 检查是否有 ?view=1 参数，如果没有则自动发送初始提示词
      const viewParam = route.query.view as string
      if (appData.initPrompt && viewParam !== '1') {
        // 自动发送初始提示词给AI
        await sendMessageToAI(appData.initPrompt)
      }
    } else {
      message.error('加载应用信息失败：' + response.data.message)
    }
  } catch (error) {
    console.error('加载应用信息失败', error)
    message.error('加载应用信息失败，请稍后重试')
  }
}

// 切换应用详情弹窗
const toggleAppDetails = () => {
  showAppDetails.value = !showAppDetails.value
}

// 获取创建者头像颜色
const getCreatorAvatarColor = () => {
  const colors = [
    '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7',
    '#DDA0DD', '#98D8C8', '#F7DC6F', '#BB8FCE', '#85C1E9'
  ];
  const creatorName = appCreatorName.value || '未知';
  let hash = 0;
  for (let i = 0; i < creatorName.length; i++) {
    hash = creatorName.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash % colors.length);
  return colors[index];
};

// 格式化日期
const formatDate = (dateString: string | undefined): string => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};

// 编辑应用
const editApp = () => {
  message.info('编辑功能开发中');
  showAppDetails.value = false;
};

// 删除应用
const deleteApp = () => {
  if (confirm('确定要删除这个应用吗？此操作不可恢复。')) {
    // 这里可以添加删除应用的API调用
    message.success('应用删除成功');
    router.push('/');
  }
  showAppDetails.value = false;
};

// 发送消息给AI
const sendMessageToAI = async (userMessage: string) => {
  try {
    loading.value = true

    // 添加用户消息到列表
    messages.value.push({
      id: Date.now(),
      content: userMessage,
      isUser: true
    })

    // 清空输入框
    userInput.value = ''

    // 创建SSE连接，设置withCredentials为true以携带认证信息
    const url = `${API_CONFIG.BASE_URL}/app/chat/gen/code?message=${encodeURIComponent(userMessage)}&appId=${appId.value}`
    eventSource.value = new EventSource(url, { withCredentials: true })

    // 存储AI回复内容
    let aiResponse = ''
    const aiMessageId = Date.now() + 1

    // 添加AI消息占位符
    messages.value.push({
      id: aiMessageId,
      content: '',
      isUser: false
    })

    // 处理SSE事件
    eventSource.value.onmessage = (event) => {
      try {
        const data = event.data
        if (data) {
          // 解析JSON数据，提取实际内容
          const parsedData = JSON.parse(data)
          let actualContent = parsedData.d

          // 处理代码块格式，确保使用正确的Markdown标记
          // 处理HTML代码块
          actualContent = actualContent.replace(/```html([\s\S]*?)```/g, '\n```html\n$1\n```\n')
          // 处理CSS代码块
          actualContent = actualContent.replace(/```css([\s\S]*?)```/g, '\n```css\n$1\n```\n')
          // 处理JavaScript代码块
          actualContent = actualContent.replace(/```javascript([\s\S]*?)```/g, '\n```javascript\n$1\n```\n')
          // 处理可能的HTML代码块标记问题
          actualContent = actualContent.replace(/"""html/g, '```html')
          actualContent = actualContent.replace(/"""css/g, '```css')
          actualContent = actualContent.replace(/"""javascript/g, '```javascript')
          actualContent = actualContent.replace(/"""/g, '```')

          // 累加AI回复内容
          aiResponse += actualContent

          // 更新AI消息
          const aiMessageIndex = messages.value.findIndex(msg => msg.id === aiMessageId)
          if (aiMessageIndex !== -1 && messages.value[aiMessageIndex]) {
            messages.value[aiMessageIndex].content = aiResponse
          }
        }
      } catch (error) {
        console.error('处理SSE消息失败', error)
      }
    }

    // 处理done事件
    eventSource.value.addEventListener('done', () => {
      // 流式传输结束
      eventSource.value?.close()
      loading.value = false
      codeGenerated.value = true
    })

    // 处理SSE错误
    eventSource.value.onerror = (error) => {
      console.error('SSE连接错误', error)
      eventSource.value?.close()
      loading.value = false
    }
  } catch (error) {
    console.error('发送消息失败', error)
    message.error('发送消息失败，请稍后重试')
    loading.value = false
  }
}

// 发送用户消息
const sendMessage = () => {
  if (!userInput.value.trim()) return
  sendMessageToAI(userInput.value)
}

// 部署应用
const deployApp = async () => {
  try {
    deploying.value = true
    const response = await api.appController.deployApp({
      appId: appId.value
    })

    if (response.data.code === 0 && response.data.data) {
      deployedUrl.value = response.data.data
      message.success('应用部署成功！访问地址：' + response.data.data)
    } else {
      message.error('部署失败：' + response.data.message)
    }
  } catch (error) {
    console.error('部署应用失败', error)
    message.error('部署应用失败，请稍后重试')
  } finally {
    deploying.value = false
  }
}

// 生成网站预览URL
const getWebsitePreviewUrl = () => {
  if (!codeGenerated.value || !codeGenType.value) return ''
  return `${API_CONFIG.BASE_URL}/static/${codeGenType.value}_${appId.value}/`
}

// 清理SSE连接
const cleanupEventSource = () => {
  if (eventSource.value) {
    eventSource.value.close()
    eventSource.value = null
  }
}

// 创建新应用
const createNewApp = async (prompt: string) => {
  try {
    loading.value = true
    const response = await api.appController.createApp({
      initPrompt: prompt
    })

    if (response.data.code === 0 && response.data.data) {
      appId.value = response.data.data
      await loadAppInfo()
    } else {
      message.error('创建应用失败：' + response.data.message)
      router.push('/')
    }
  } catch (error) {
    console.error('创建应用失败', error)
    message.error('创建应用失败，请稍后重试')
    router.push('/')
  } finally {
    loading.value = false
  }
}

// 页面加载时初始化
onMounted(async () => {
  const idParam = route.params.id as string

  if (idParam === 'new') {
    // 处理新应用创建
    const prompt = route.query.prompt as string

    if (!prompt) {
      message.error('缺少创建应用的必要参数')
      router.push('/')
      return
    }

    // 检查登录状态
    if (!loginUserStore.isLoggedIn()) {
      const redirectPath = encodeURIComponent(`/app/chat/new?prompt=${encodeURIComponent(prompt)}`)
      router.push(`/user/login?redirect=${redirectPath}`)
      return
    }

    // 创建新应用
    await createNewApp(prompt)
  } else if (appId.value) {
    // 加载现有应用
    await loadAppInfo()
  }
})

// 页面卸载时清理
onUnmounted(() => {
  cleanupEventSource()
})
</script>

<style scoped>
.app-chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--background-page);
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 var(--spacing-xl);
  height: var(--header-height);
  background-color: var(--background-default);
  box-shadow: var(--shadow-md);
  z-index: 100;
}

.app-info {
  display: flex;
  align-items: center;
}

.app-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0;
}

.top-actions {
  display: flex;
  gap: var(--spacing-md);
  position: relative;
}

/* 应用详情按钮和悬浮窗样式 */
.app-details-container {
  position: relative;
}

.details-btn {
  padding: var(--spacing-sm) var(--spacing-xl);
  background-color: var(--background-light);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.details-btn:hover {
  background-color: var(--background-default);
  border-color: var(--primary-color);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

/* 应用详情悬浮窗 */
.app-details-popup {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: var(--spacing-xs);
  width: 320px;
  background-color: white;
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-lg);
  z-index: 1000;
  overflow: hidden;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-md) var(--spacing-lg);
  background-color: var(--background-light);
  border-bottom: 1px solid var(--border-color);
}

.popup-header h3 {
  margin: 0;
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
}

.close-btn {
  background: none;
  border: none;
  font-size: var(--font-size-xl);
  cursor: pointer;
  color: var(--text-secondary);
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all var(--transition-normal);
}

.close-btn:hover {
  background-color: var(--background-light);
  color: var(--text-primary);
}

.popup-content {
  padding: var(--spacing-lg);
}

/* 应用基础信息 */
.app-basic-info {
  margin-bottom: var(--spacing-lg);
}

.app-basic-info h4 {
  margin: 0 0 var(--spacing-md) 0;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  color: var(--text-secondary);
  text-transform: uppercase;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: var(--spacing-md);
  font-size: var(--font-size-sm);
}

.info-label {
  width: 80px;
  color: var(--text-secondary);
}

.info-value {
  color: var(--text-primary);
  flex: 1;
}

/* 创建者信息 */
.creator-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.creator-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: var(--spacing-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: var(--font-weight-bold);
  font-size: var(--font-size-sm);
}

.creator-name {
  color: var(--text-primary);
}

/* 操作栏 */
.app-actions {
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-color);
}

.app-actions h4 {
  margin: 0 0 var(--spacing-md) 0;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  color: var(--text-secondary);
  text-transform: uppercase;
}

.action-buttons {
  display: flex;
  gap: var(--spacing-sm);
}

.action-btn {
  padding: var(--spacing-xs) var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-bold);
  cursor: pointer;
  transition: all var(--transition-normal);
  flex: 1;
}

.edit-btn {
  background-color: var(--background-light);
  color: var(--text-primary);
}

.edit-btn:hover {
  background-color: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.delete-btn {
  background-color: var(--background-light);
  color: var(--error-color);
  border-color: var(--error-color);
}

.delete-btn:hover {
  background-color: var(--error-color);
  color: white;
}

.deploy-btn {
  padding: var(--spacing-sm) var(--spacing-xl);
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius-md);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.deploy-btn:hover:not(:disabled) {
  background-color: var(--primary-hover);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.deploy-btn:disabled {
  background-color: var(--border-light);
  color: var(--text-tertiary);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.main-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.chat-section {
  width: 50%;
  display: flex;
  flex-direction: column;
  border-right: 1px solid var(--border-color);
  background-color: var(--background-default);
}

.messages-container {
  flex: 1;
  padding: var(--spacing-xl);
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.message {
  display: flex;
  max-width: 80%;
}

.user-message {
  align-self: flex-end;
  justify-content: flex-end;
}

.ai-message {
  align-self: flex-start;
  justify-content: flex-start;
}

.message-content {
  display: flex;
  align-items: flex-start;
  gap: var(--spacing-sm);
}

.message-avatar {
  display: flex;
  align-items: flex-start;
  margin-top: 2px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.message-bubble {
  padding: var(--spacing-md) var(--spacing-lg);
  border-radius: 20px;
  font-size: var(--font-size-sm);
  line-height: 1.5;
  word-wrap: break-word;
}

.user-message .message-bubble {
  background-color: var(--primary-color);
  color: white;
  border-bottom-right-radius: 4px;
}

.ai-message .message-bubble {
  background-color: var(--background-light);
  color: var(--text-primary);
  border-bottom-left-radius: 4px;
}

.loading-indicator {
  align-self: flex-start;
  padding: var(--spacing-md);
  color: var(--text-secondary);
  font-size: var(--font-size-sm);
}

/* Markdown渲染样式 */
.ai-message .message-bubble {
  /* 允许Markdown内容的样式正常显示 */
}

/* 代码块样式 */
.ai-message .message-bubble pre {
  background-color: var(--background-light);
  border-radius: var(--border-radius-sm);
  padding: var(--spacing-md);
  overflow-x: auto;
  font-family: var(--font-family-mono);
  font-size: var(--font-size-xs);
  line-height: 1.4;
  margin: var(--spacing-md) 0;
}

.ai-message .message-bubble code {
  font-family: var(--font-family-mono);
  font-size: var(--font-size-xs);
  background-color: var(--background-light);
  padding: 2px 4px;
  border-radius: 4px;
}

.ai-message .message-bubble pre code {
  background-color: transparent;
  padding: 0;
  border-radius: 0;
}

/* 标题样式 */
.ai-message .message-bubble h1,
.ai-message .message-bubble h2,
.ai-message .message-bubble h3,
.ai-message .message-bubble h4,
.ai-message .message-bubble h5,
.ai-message .message-bubble h6 {
  margin: var(--spacing-md) 0 var(--spacing-sm) 0;
  color: var(--text-primary);
  font-weight: var(--font-weight-bold);
}

.ai-message .message-bubble h1 {
  font-size: var(--font-size-xl);
}

.ai-message .message-bubble h2 {
  font-size: var(--font-size-lg);
}

.ai-message .message-bubble h3 {
  font-size: var(--font-size-md);
}

/* 列表样式 */
.ai-message .message-bubble ul,
.ai-message .message-bubble ol {
  margin: var(--spacing-md) 0;
  padding-left: var(--spacing-xl);
}

.ai-message .message-bubble li {
  margin: var(--spacing-xs) 0;
}

/* 引用样式 */
.ai-message .message-bubble blockquote {
  border-left: 4px solid var(--primary-color);
  padding-left: var(--spacing-md);
  margin: var(--spacing-md) 0;
  color: var(--text-secondary);
  font-style: italic;
}

/* 链接样式 */
.ai-message .message-bubble a {
  color: var(--primary-color);
  text-decoration: none;
  transition: color var(--transition-normal);
}

.ai-message .message-bubble a:hover {
  color: var(--primary-hover);
  text-decoration: underline;
}

/* 段落样式 */
.ai-message .message-bubble p {
  margin: var(--spacing-sm) 0;
}

.input-container {
  display: flex;
  gap: var(--spacing-md);
  padding: var(--spacing-lg) var(--spacing-xl);
  border-top: 1px solid var(--border-color);
  background-color: var(--background-default);
}

.input-wrapper {
  position: relative;
  flex: 1;
}

.input-tooltip {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background-color: var(--text-primary);
  color: white;
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-xs);
  white-space: nowrap;
  margin-bottom: var(--spacing-xs);
  z-index: 1000;
}

.input-tooltip::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border-width: 4px;
  border-style: solid;
  border-color: var(--text-primary) transparent transparent transparent;
}

.user-input {
  width: 100%;
  padding: var(--spacing-sm) var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: 20px;
  font-size: var(--font-size-sm);
  outline: none;
  transition: all var(--transition-normal);
  background-color: var(--background-default);
  color: var(--text-primary);
}

.user-input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
}

.user-input:disabled {
  background-color: var(--background-light);
  cursor: not-allowed;
}

.send-btn {
  padding: 0 var(--spacing-xl);
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 20px;
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.send-btn:hover:not(:disabled) {
  background-color: var(--primary-hover);
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.send-btn:disabled {
  background-color: var(--border-light);
  color: var(--text-tertiary);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.preview-section {
  width: 50%;
  display: flex;
  flex-direction: column;
  background-color: var(--background-light);
}

.preview-header {
  padding: var(--spacing-lg) var(--spacing-xl);
  border-bottom: 1px solid var(--border-color);
  background-color: var(--background-default);
}

.preview-header h2 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0;
}

.preview-content {
  flex: 1;
  overflow: hidden;
}

.preview-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--background-light);
}

.preview-placeholder p {
  font-size: var(--font-size-md);
  color: var(--text-tertiary);
  margin: 0;
}

.website-preview {
  width: 100%;
  height: 100%;
  border: none;
}

/* 响应式设计 */
@media (max-width: var(--breakpoint-lg)) {
  .main-content {
    flex-direction: column;
  }

  .chat-section {
    width: 100%;
    height: 50%;
    border-right: none;
    border-bottom: 1px solid var(--border-color);
  }

  .preview-section {
    width: 100%;
    height: 50%;
  }
}
</style>
