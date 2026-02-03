<template>
  <div class="app-details-popup">
    <div class="popup-header">
      <h3>应用信息</h3>
      <button @click="$emit('close')" class="close-btn">×</button>
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
          <button @click="$emit('edit')" class="action-btn edit-btn">修改</button>
          <button @click="$emit('delete')" class="action-btn delete-btn">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  appCreatorName: string | undefined
  formattedCreateTime: string | undefined
  isOwner: boolean
}>()

defineEmits<{
  (e: 'close'): void
  (e: 'edit'): void
  (e: 'delete'): void
}>()

// 获取创建者头像颜色
const getCreatorAvatarColor = () => {
  const colors = [
    '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7',
    '#DDA0DD', '#98D8C8', '#F7DC6F', '#BB8FCE', '#85C1E9'
  ]
  const creatorName = defineProps<{ appCreatorName: string | undefined }>().appCreatorName || '未知'
  let hash = 0
  for (let i = 0; i < creatorName.length; i++) {
    hash = creatorName.charCodeAt(i) + ((hash << 5) - hash)
  }
  const index = Math.abs(hash % colors.length)
  return colors[index]
}
</script>

<style scoped>
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
</style>