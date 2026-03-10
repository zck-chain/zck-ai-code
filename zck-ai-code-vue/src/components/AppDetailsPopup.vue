<template>
  <div class="app-details-popup">
    <div class="popup-header">
      <h3>应用信息</h3>
      <button @click="handleClose" class="close-btn">×</button>
    </div>
    <div class="popup-content">
      <!-- 应用基础信息 -->
      <div class="app-basic-info">
        <h4>基础信息</h4>
        <div class="info-item">
          <span class="info-label">创建者：</span>
          <div class="creator-info">
            <div class="creator-avatar" :style="{ backgroundColor: avatarColor }"></div>
            <span class="creator-name">{{ appCreatorName || '未知' }}</span>
          </div>
        </div>
        <div class="info-item">
          <span class="info-label">创建时间：</span>
          <span class="info-value">{{ formattedCreateTime || '未知' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">生成类型：</span>
          <span class="info-value code-type-tag">{{ codeGenTypeName }}</span>
        </div>
      </div>
      <!-- 操作栏（仅本人或管理员可见） -->
      <div v-if="isOwner" class="app-actions">
        <h4>操作</h4>
        <div class="action-buttons">
          <button @click="handleEdit" class="action-btn edit-btn">修改</button>
          <button @click="handleDelete" class="action-btn delete-btn">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { CODE_GEN_TYPE_CONFIG } from '@/config/codeGenType'

const props = defineProps<{
  visible: boolean
  appCreatorName: string
  formattedCreateTime: string
  codeGenType: string
  isOwner: boolean
}>()

const emit = defineEmits(['close', 'edit', 'delete'])

// 计算创建者头像颜色
const avatarColor = computed(() => {
  const colors = [
    '#4CAF50', '#2196F3', '#FF9800', '#9C27B0',
    '#F44336', '#00BCD4', '#795548', '#607D8B'
  ];
  const name = props.appCreatorName || '未知';
  const index = name.split('').reduce((sum, char) => sum + char.charCodeAt(0), 0) % colors.length;
  return colors[index];
})

// 计算代码生成类型名称
const codeGenTypeName = computed(() => {
  const config = CODE_GEN_TYPE_CONFIG[props.codeGenType as keyof typeof CODE_GEN_TYPE_CONFIG]
  return config ? config.label : props.codeGenType || '未知'
})

// 处理关闭
const handleClose = () => {
  emit('close')
}

// 处理编辑
const handleEdit = () => {
  emit('edit')
}

// 处理删除
const handleDelete = () => {
  emit('delete')
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
  padding: var(--spacing-md);
  border-bottom: 1px solid var(--border-color);
  background-color: var(--background-light);
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
  font-size: 20px;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--border-radius-sm);
  transition: all var(--transition-normal);
}

.close-btn:hover {
  background-color: var(--border-color);
}

.popup-content {
  padding: var(--spacing-md);
}

.app-basic-info {
  margin-bottom: var(--spacing-md);
}

.app-basic-info h4,
.app-actions h4 {
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm) 0;
  padding-bottom: var(--spacing-xs);
  border-bottom: 1px solid var(--border-color);
}

.info-item {
  margin-bottom: var(--spacing-sm);
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.info-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  min-width: 60px;
}

.creator-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  flex: 1;
}

.creator-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-bold);
}

.creator-name {
  font-size: var(--font-size-sm);
  color: var(--text-primary);
}

.info-value {
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  flex: 1;
}

.code-type-tag {
  font-size: var(--font-size-xs);
  font-weight: var(--font-weight-medium);
  color: #28a745;
  background-color: rgba(40, 167, 69, 0.1);
  padding: 2px 8px;
  border-radius: var(--border-radius-sm);
  border: 1px solid #28a745;
  box-shadow: 0 1px 3px rgba(40, 167, 69, 0.2);
  display: inline-block;
}

.app-actions {
  margin-top: var(--spacing-md);
}

.action-buttons {
  display: flex;
  gap: var(--spacing-xs);
}

.action-btn {
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--transition-normal);
  border: 1px solid transparent;
  background-color: transparent;
  text-align: left;
}

.edit-btn {
  background-color: transparent;
  color: var(--primary-color);
  border: 1px solid var(--primary-color);
}

.edit-btn:hover {
  background-color: var(--primary-color);
  color: white;
}

.delete-btn {
  background-color: transparent;
  color: var(--error-color);
  border: 1px solid var(--error-color);
}

.delete-btn:hover {
  background-color: var(--error-color);
  color: white;
}
</style>
