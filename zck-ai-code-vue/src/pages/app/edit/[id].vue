<template>
  <div class="app-edit-container">
    <div class="page-header">
      <button @click="goBack" class="back-btn">返回</button>
      <h1>编辑应用信息</h1>
    </div>

    <div class="edit-form-section">
      <h2 class="section-title">基本信息</h2>
      <form class="edit-form" @submit.prevent="saveChanges">
        <div class="form-item">
          <label for="appName">*应用名称</label>
          <input
            id="appName"
            v-model="appName"
            type="text"
            placeholder="请输入应用名称"
            class="form-input"
            required
            maxlength="50"
          />
          <div class="input-counter">{{ appName.length }}/50</div>
        </div>

        <div class="form-item">
          <label for="cover">应用封面</label>
          <input
            id="cover"
            v-model="cover"
            type="text"
            placeholder="请输入应用封面URL"
            class="form-input"
          />
          <div v-if="cover" class="cover-preview">
            <img :src="cover" alt="应用封面" class="cover-image" />
          </div>
          <div class="form-hint">支持图片链接，建议尺寸：400x300</div>
        </div>

        <div class="form-item">
          <label for="priority">优先级</label>
          <input
            id="priority"
            v-model.number="priority"
            type="number"
            placeholder="请输入优先级"
            class="form-input"
          />
          <div class="form-hint">设置为99表示精选应用</div>
        </div>

        <div class="form-item">
          <label for="initPrompt">初始提示词</label>
          <textarea
            id="initPrompt"
            v-model="initPrompt"
            class="form-textarea"
            disabled
            maxlength="1000"
          ></textarea>
          <div class="input-counter">{{ initPrompt.length }}/1000</div>
          <div class="form-hint">初始提示词不可修改</div>
        </div>

        <div class="form-item">
          <label for="codeGenType">生成类型</label>
          <input
            id="codeGenType"
            v-model="codeGenType"
            type="text"
            class="form-input"
            disabled
          />
          <div class="form-hint">生成类型不可修改</div>
        </div>

        <div class="form-item">
          <label for="deployKey">部署密钥</label>
          <input
            id="deployKey"
            v-model="deployKey"
            type="text"
            class="form-input"
            disabled
          />
          <div class="form-hint">部署密钥不可修改</div>
        </div>

        <div class="form-actions">
          <button
            type="button"
            @click="resetForm"
            class="reset-btn"
          >
            重置
          </button>
          <button
            type="button"
            @click="enterChat"
            class="chat-btn"
          >
            进入对话
          </button>
          <button
            type="submit"
            class="save-btn"
            :disabled="loading"
          >
            {{ loading ? '保存中...' : '保存修改' }}
          </button>
        </div>
      </form>

      <h2 class="section-title">应用信息</h2>
      <div class="app-info-section">
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">应用ID</span>
            <span class="info-value">{{ appId }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">创建者</span>
            <span class="info-value">{{ authorName }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">创建时间</span>
            <span class="info-value">{{ createTime }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">更新时间</span>
            <span class="info-value">{{ updateTime }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">部署时间</span>
            <span class="info-value">{{ deployedTime || '未部署' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">访问链接</span>
            <a href="#" class="info-link">查看预览</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import api from '@/api'
import { useLoginUserStore } from '@/stores/loginUser'

// 状态管理
const route = useRoute()
const router = useRouter()
const loginUserStore = useLoginUserStore()
const appId = ref<string>(route.params.id as string)
const appName = ref('')
const cover = ref('')
const priority = ref(0)
const initPrompt = ref('')
const codeGenType = ref('')
const deployKey = ref('')
const authorName = ref('')
const createTime = ref('')
const updateTime = ref('')
const deployedTime = ref('')
const loading = ref(false)

// 加载应用信息
const loadAppInfo = async () => {
  try {
    loading.value = true
    const response = await api.appController.getAppById({
      id: appId.value
    })

    if (response.data.code === 0 && response.data.data) {
      const appData = response.data.data
      appName.value = appData.appName || ''
      cover.value = appData.cover || ''
      priority.value = appData.priority || 0
      initPrompt.value = appData.initPrompt || ''
      codeGenType.value = appData.codeGenType || ''
      deployKey.value = appData.deployKey || ''
      authorName.value = appData.authorName || ''
      createTime.value = appData.createTime || ''
      updateTime.value = appData.updateTime || ''
      deployedTime.value = appData.deployedTime || ''
    } else {
      message.error('加载应用信息失败：' + response.data.message)
      router.push('/')
    }
  } catch (error) {
    console.error('加载应用信息失败', error)
    message.error('加载应用信息失败，请稍后重试')
    router.push('/')
  } finally {
    loading.value = false
  }
}

// 保存修改
const saveChanges = async () => {
  if (!appName.value.trim()) {
    message.warning('请输入应用名称')
    return
  }

  try {
    loading.value = true

    // 构建更新请求
    const updateData: API.AppUpdateRequest = {
      id: appId.value,
      appName: appName.value,
      cover: cover.value,
      priority: priority.value
    }

    // 调用更新接口
    const response = await api.appController.updateApp(updateData)

    if (response.data.code === 0 && response.data.data) {
      message.success('保存成功')
      // 重新加载数据
      loadAppInfo()
    } else {
      message.error('保存失败：' + response.data.message)
    }
  } catch (error) {
    console.error('保存失败', error)
    message.error('保存失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 返回按钮功能
const goBack = () => {
  // 检查是否有来源页面，否则返回首页
  if (document.referrer) {
    router.back()
  } else {
    router.push('/')
  }
}

// 重置表单
const resetForm = () => {
  loadAppInfo()
}

// 进入对话
const enterChat = () => {
  // 检查登录状态
  if (!loginUserStore.isLoggedIn()) {
    // 保存当前操作路径，登录后返回
    const redirectPath = encodeURIComponent(`/app/chat/${appId.value}?view=1`)
    router.push(`/user/login?redirect=${redirectPath}`)
    return
  }

  router.push(`/app/chat/${appId.value}?view=1`)
}

// 页面加载时初始化
onMounted(() => {
  if (appId.value) {
    loadAppInfo()
  }
})
</script>

<style scoped>
.app-edit-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 32px;
  gap: 16px;
}

.back-btn {
  background: none;
  border: none;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background-color: #e0e0e0;
}

.page-header h1 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.edit-form-section {
  background-color: white;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 800px;
  margin: 0 auto;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0 0 24px 0;
  padding-bottom: 12px;
  border-bottom: 1px solid #e0e0e0;
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: relative;
}

.form-item label {
  font-size: 14px;
  font-weight: 500;
  color: #666;
}

.form-input {
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
}

.form-input:focus {
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

.form-textarea {
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
  resize: vertical;
  min-height: 100px;
}

.form-textarea:focus {
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

.input-counter {
  position: absolute;
  right: 16px;
  bottom: 12px;
  font-size: 12px;
  color: #999;
}

.cover-preview {
  margin-top: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
  max-width: 200px;
}

.cover-image {
  width: 100%;
  height: auto;
  display: block;
}

.form-hint {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.form-actions {
  display: flex;
  gap: 16px;
  justify-content: flex-end;
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e0e0e0;
}

.reset-btn {
  padding: 10px 24px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.reset-btn:hover {
  background-color: #e0e0e0;
}

.chat-btn {
  padding: 10px 24px;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.chat-btn:hover {
  background-color: #1976D2;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
}

.save-btn {
  padding: 10px 24px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.save-btn:hover:not(:disabled) {
  background-color: #45a049;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
}

.save-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.app-info-section {
  margin-top: 48px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-label {
  font-size: 12px;
  color: #999;
}

.info-value {
  font-size: 14px;
  color: #333;
}

.info-link {
  font-size: 14px;
  color: #4CAF50;
  text-decoration: none;
  cursor: pointer;
}

.info-link:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-edit-container {
    padding: 16px;
  }

  .edit-form-section {
    padding: 24px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }

  .reset-btn,
  .chat-btn,
  .save-btn {
    width: 100%;
  }
}
</style>
