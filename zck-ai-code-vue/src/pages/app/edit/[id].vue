<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api'

// 状态管理
const route = useRoute()
const router = useRouter()
const appId = ref<number>(Number(route.params.id))
const appName = ref('')
const cover = ref('')
const priority = ref(0)
const loading = ref(false)
const isAdmin = ref(false)

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
    } else {
      alert('加载应用信息失败：' + response.data.message)
      router.push('/')
    }
  } catch (error) {
    console.error('加载应用信息失败', error)
    alert('加载应用信息失败，请稍后重试')
    router.push('/')
  } finally {
    loading.value = false
  }
}

// 保存修改
const saveChanges = async () => {
  if (!appName.value.trim()) {
    alert('请输入应用名称')
    return
  }

  try {
    loading.value = true

    // 构建更新请求
    const updateData: API.AppUpdateRequest = {
      id: appId.value,
      appName: appName.value
    }

    // 管理员可以更新更多字段
    if (isAdmin.value) {
      updateData.cover = cover.value
      updateData.priority = priority.value
    }

    // 调用更新接口
    const response = await api.appController.updateApp(updateData)

    if (response.data.code === 0 && response.data.data) {
      alert('保存成功')
      // 跳回上一页
      router.back()
    } else {
      alert('保存失败：' + response.data.message)
    }
  } catch (error) {
    console.error('保存失败', error)
    alert('保存失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 页面加载时初始化
onMounted(() => {
  if (appId.value) {
    loadAppInfo()
    // 这里可以通过获取用户信息来判断是否为管理员
    // 暂时默认不是管理员，实际项目中需要根据后端返回的用户角色判断
    isAdmin.value = false
  }
})
</script>

<template>
  <div class="app-edit-container">
    <div class="page-header">
      <h1>应用信息修改</h1>
    </div>

    <div class="edit-form-section">
      <form class="edit-form" @submit.prevent="saveChanges">
        <div class="form-item">
          <label for="appName">应用名称 <span class="required">*</span></label>
          <input
            id="appName"
            v-model="appName"
            type="text"
            placeholder="请输入应用名称"
            class="form-input"
            required
          />
        </div>

        <div v-if="isAdmin" class="form-item">
          <label for="cover">应用封面</label>
          <input
            id="cover"
            v-model="cover"
            type="text"
            placeholder="请输入应用封面URL"
            class="form-input"
          />
        </div>

        <div v-if="isAdmin" class="form-item">
          <label for="priority">优先级</label>
          <input
            id="priority"
            v-model.number="priority"
            type="number"
            placeholder="请输入优先级"
            class="form-input"
          />
        </div>

        <div class="form-actions">
          <button
            type="button"
            @click="router.back()"
            class="cancel-btn"
          >
            取消
          </button>
          <button
            type="submit"
            class="save-btn"
            :disabled="loading"
          >
            {{ loading ? '保存中...' : '保存' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.app-edit-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 32px;
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
  max-width: 600px;
  margin: 0 auto;
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
}

.form-item label {
  font-size: 14px;
  font-weight: 500;
  color: #666;
}

.required {
  color: #f44336;
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

.form-actions {
  display: flex;
  gap: 16px;
  justify-content: flex-end;
  margin-top: 16px;
}

.cancel-btn {
  padding: 10px 24px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background-color: #e0e0e0;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .app-edit-container {
    padding: 16px;
  }

  .edit-form-section {
    padding: 24px;
  }

  .form-actions {
    flex-direction: column;
  }

  .cancel-btn,
  .save-btn {
    width: 100%;
  }
}
</style>