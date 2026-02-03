<template>
  <div class="app-edit-page">
    <div class="page-header">
      <button @click="goBack" class="back-btn">
        <span>←</span> 返回
      </button>
      <h1>编辑应用信息</h1>
    </div>

    <div class="edit-container">
      <!-- 基本信息表单 -->
      <div class="basic-info-section">
        <h2>基本信息</h2>

        <div class="form-group">
          <label class="form-label">应用名称 <span class="required">*</span></label>
          <input
            v-model="formData.name"
            type="text"
            class="form-input"
            placeholder="请输入应用名称"
            maxlength="50"
          />
          <div class="char-count">{{ formData.appName }}/50</div>
        </div>

        <div class="form-group">
          <label class="form-label">应用封面 <span class="required">*</span></label>
          <input
            v-model="formData.cover"
            type="text"
            class="form-input"
            placeholder="请输入应用封面图片URL"
            maxlength="200"
          />
          <div class="cover-preview" v-if="formData.cover">
            <img :src="formData.cover" alt="应用封面预览" />
          </div>
          <div class="form-hint">支持网络图片，建议尺寸：400x300</div>
        </div>

        <div class="form-group">
          <label class="form-label">优先级</label>
          <input
            v-model.number="formData.priority"
            type="number"
            class="form-input"
            placeholder="设置为99的将精选应用"
          />
        </div>

        <div class="form-group">
          <label class="form-label">初始提示词</label>
          <textarea
            v-model="formData.prompt"
            class="form-textarea"
            placeholder="请输入初始提示词"
            maxlength="1000"
          ></textarea>
          <div class="char-count">{{ formData.prompt.length }}/1000</div>
          <div class="form-hint">初始提示词不支持修改</div>
        </div>

        <div class="form-group">
          <label class="form-label">代码生成类型</label>
          <input
            v-model="formData.type"
            type="text"
            class="form-input"
            disabled
            placeholder="代码生成类型不支持修改"
          />
        </div>

        <div class="form-group">
          <label class="form-label">部署密钥</label>
          <input
            v-model="formData.deployKey"
            type="text"
            class="form-input"
            disabled
            placeholder="部署密钥不支持修改"
          />
        </div>

        <div class="form-actions">
          <button @click="saveChanges" class="btn-primary">保存修改</button>
          <button @click="resetForm" class="btn-secondary">重置</button>
          <button @click="enterConversation" class="btn-tertiary">进入对话</button>
        </div>
      </div>

      <!-- 应用信息展示 -->
      <div class="app-info-section">
        <h2>应用信息</h2>

        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">应用ID</span>
            <span class="info-value">{{ appInfo.id }}</span>
          </div>

          <div class="info-item">
            <span class="info-label">创建者</span>logo
            <div class="creator-info">
              <img :src="appInfo.creatorAvatar || '/src/assets/logo.png'" alt="创建者头像" class="creator-avatar" />
              <span class="creator-name">{{ appInfo.creatorName }}</span>
            </div>
          </div>

          <div class="info-item">
            <span class="info-label">创建时间</span>
            <span class="info-value">{{ formatDateTime(appInfo.createdAt) }}</span>
          </div>

          <div class="info-item">
            <span class="info-label">更新时间</span>
            <span class="info-value">{{ formatDateTime(appInfo.updatedAt) }}</span>
          </div>

          <div class="info-item">
            <span class="info-label">部署时间</span>
            <span class="info-value">{{ appInfo.deployedAt ? formatDateTime(appInfo.deployedAt) : '未部署' }}</span>
          </div>

          <div class="info-item">
            <span class="info-label">访问链接</span>
            <a :href="appInfo.accessLink" class="info-link" target="_blank" rel="noopener noreferrer">查看链接</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { getAppById, updateApp } from '@/api/appController';

const route = useRoute();
const router = useRouter();
const appId = route.params.id as string;

// 时间格式化函数
const formatDateTime = (dateTime: string | null | undefined): string => {
  if (!dateTime) return '未知';
  // 将ISO格式时间字符串转换为年月日时分秒格式
  return dateTime.replace('T', ' ').replace('Z', '');
};

// 表单数据
const formData = ref({
  appName: '',
  cover: '',
  priority: 0,
  prompt: '',
  type: '',
  deployKey: ''
});

// 应用信息
const appInfo = ref({
  id: appId,
  creatorAvatar: '',
  creatorName: '',
  createdAt: '',
  updatedAt: '',
  deployedAt: '',
  accessLink: ''
});

// 获取应用数据
const fetchAppData = async () => {
  try {
    const response = await getAppById({ id: appId });
    if (response.data.code === 0 && response.data.data) {
      const appData = response.data.data;
      formData.value = {
        name: appData.appName || '',
        cover: appData.cover || '',
        priority: appData.priority || 0,
        prompt: appData.initPrompt || '',
        type: appData.codeGenType || '',
        deployKey: appData.deployKey || ''
      };

      const previewDomain = import.meta.env.VITE_PREVIEW_DOMAIN || 'http://localhost:5173';
      appInfo.value = {
        id: appId,
        creatorAvatar: appData.creatorAvatar || '',
        creatorName: appData.creatorName || '',
        createdAt: appData.createTime || '',
        updatedAt: appData.updateTime || '',
        deployedAt: appData.deployedTime || '',
        accessLink: `${previewDomain}/app/chat/${appId}`
      };
      message.success('获取应用数据成功');
    } else {
      message.error('获取应用数据失败');
    }
  } catch (error) {
    console.error('获取应用数据失败:', error);
    message.error('获取应用数据失败，请稍后重试');
  }
};

// 保存修改
const saveChanges = async () => {
  try {
    const updateRequest = {
      id: appId,
      appName: formData.value.name,
      cover: formData.value.cover,
      initPrompt: formData.value.prompt,
      codeGenType: formData.value.type,
      deployKey: formData.value.deployKey,
      priority: formData.value.priority,
      // 其他字段根据需要添加
    };

    const response = await updateApp(updateRequest);
    if (response.data.code === 0) {
      message.success('保存成功！');
      // 重新获取最新数据
      await fetchAppData();
    } else {
      message.error('保存失败');
    }
  } catch (error) {
    console.error('保存失败:', error);
    message.error('保存失败，请稍后重试');
  }
};

// 重置表单
const resetForm = () => {
  // 重新获取原始数据
  fetchAppData();
};

// 进入对话
const enterConversation = () => {
  router.push(`/app/chat/${appId}`);
};

// 返回上一页
const goBack = () => {
  router.back();
};

// 组件挂载时获取数据
onMounted(() => {
  fetchAppData();
});
</script>

<style scoped>
.app-edit-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.back-btn {
  display: flex;
  align-items: center;
  background: none;
  border: none;
  font-size: 16px;
  color: #333;
  cursor: pointer;
  margin-right: 20px;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.back-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.back-btn span {
  margin-right: 4px;
  font-size: 18px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.edit-container {
  max-width: 800px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.basic-info-section {
  margin-bottom: 40px;
}

.basic-info-section h2,
.app-info-section h2 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.form-group {
  margin-bottom: 24px;
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.required {
  color: #ff4d4f;
}

.form-input,
.form-textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.char-count {
  text-align: right;
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.cover-preview {
  margin-top: 12px;
  width: 200px;
  height: 150px;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.cover-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.form-hint {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 30px;
}

.btn-primary,
.btn-secondary,
.btn-tertiary {
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
}

.btn-primary {
  background-color: #1890ff;
  color: #fff;
}

.btn-primary:hover {
  background-color: #40a9ff;
}

.btn-secondary {
  background-color: #fff;
  color: #333;
  border: 1px solid #d9d9d9;
}

.btn-secondary:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.btn-tertiary {
  background-color: #f5f5f5;
  color: #333;
}

.btn-tertiary:hover {
  background-color: #e6f7ff;
  color: #1890ff;
}

.app-info-section {
  margin-top: 40px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
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
  font-weight: 400;
}

.creator-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.creator-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
}

.creator-name {
  font-size: 14px;
  color: #333;
}

.info-link {
  font-size: 14px;
  color: #1890ff;
  text-decoration: none;
}

.info-link:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-edit-page {
    padding: 10px;
  }

  .edit-container {
    padding: 20px;
  }

  .form-actions {
    flex-direction: column;
  }

  .btn-primary,
  .btn-secondary,
  .btn-tertiary {
    width: 100%;
    text-align: center;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
