<template>
  <div class="deploy-success-dialog">
    <div class="deploy-success-content">
      <button @click="handleClose" class="close-btn">×</button>
      <div class="success-icon">✅</div>
      <h3 class="success-title">部署成功</h3>
      <p class="success-message">网站部署成功！</p>
      <p class="success-hint">你的网站已经成功部署，可以通过以下链接访问：</p>
      <div class="url-container">
        <input
          type="text"
          :value="deployedUrl"
          class="url-input"
          readonly
        />
        <button @click="copyUrl" class="copy-btn">📋</button>
      </div>
      <div class="success-buttons">
        <button @click="handleVisit" class="visit-btn">访问网站</button>
        <button @click="handleClose" class="close-btn-secondary">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { message } from 'ant-design-vue'

const props = defineProps<{
  visible: boolean
  deployedUrl: string
}>()

const emit = defineEmits(['close', 'visit'])

// 处理关闭
const handleClose = () => {
  emit('close')
}

// 处理访问网站
const handleVisit = () => {
  emit('visit')
}

// 复制部署URL到剪贴板
const copyUrl = () => {
  if (props.deployedUrl) {
    navigator.clipboard.writeText(props.deployedUrl)
      .then(() => {
        message.success('链接已复制到剪贴板');
      })
      .catch(err => {
        console.error('复制失败:', err);
        message.error('复制失败，请手动复制');
      });
  }
};
</script>

<style scoped>
.deploy-success-dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 3000;
}

.deploy-success-content {
  background-color: white;
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-xl);
  padding: var(--spacing-xl);
  min-width: 400px;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-lg);
}

.deploy-success-content .close-btn {
  position: absolute;
  top: var(--spacing-md);
  right: var(--spacing-md);
  background: none;
  border: none;
  font-size: 20px;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 4px;
  border-radius: var(--border-radius-sm);
  transition: all var(--transition-normal);
}

.deploy-success-content .close-btn:hover {
  background-color: var(--background-light);
}

.success-icon {
  font-size: 48px;
}

.success-title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0;
}

.success-message {
  font-size: var(--font-size-md);
  color: var(--text-primary);
  margin: 0;
  text-align: center;
}

.success-hint {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin: 0;
  text-align: center;
}

.url-container {
  width: 100%;
  display: flex;
  gap: var(--spacing-sm);
  margin-top: var(--spacing-sm);
}

.url-input {
  flex: 1;
  padding: var(--spacing-sm) var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  background-color: var(--background-light);
}

.copy-btn {
  padding: var(--spacing-sm) var(--spacing-md);
  background-color: var(--background-light);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.copy-btn:hover {
  background-color: var(--border-color);
}

.success-buttons {
  display: flex;
  gap: var(--spacing-md);
  margin-top: var(--spacing-sm);
}

.visit-btn {
  padding: var(--spacing-sm) var(--spacing-xl);
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.visit-btn:hover {
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}

.close-btn-secondary {
  padding: var(--spacing-sm) var(--spacing-xl);
  background-color: transparent;
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.close-btn-secondary:hover {
  background-color: var(--background-light);
}
</style>
