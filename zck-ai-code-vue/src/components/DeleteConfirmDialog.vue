<template>
  <div class="delete-confirm-dialog">
    <div class="confirm-content">
      <div class="confirm-icon">⚠️</div>
      <div class="confirm-message">确定要删除这个应用吗？</div>
      <div class="confirm-buttons">
        <button @click="handleCancel" class="cancel-btn">取消</button>
        <button @click="handleConfirm" class="confirm-btn">确定</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  visible: boolean
}>()

const emit = defineEmits(['cancel', 'confirm'])

// 处理取消
const handleCancel = () => {
  emit('cancel')
}

// 处理确认
const handleConfirm = () => {
  emit('confirm')
}
</script>

<style scoped>
.delete-confirm-dialog {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: var(--spacing-xs);
  background-color: white;
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-lg);
  z-index: 2000;
  padding: var(--spacing-lg);
  min-width: 300px;
}

.confirm-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-md);
}

.confirm-icon {
  font-size: 24px;
}

.confirm-message {
  font-size: var(--font-size-md);
  color: var(--text-primary);
  text-align: center;
}

.confirm-buttons {
  display: flex;
  gap: var(--spacing-md);
  margin-top: var(--spacing-sm);
}

.cancel-btn {
  padding: var(--spacing-sm) var(--spacing-md);
  background-color: transparent;
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.cancel-btn:hover {
  background-color: var(--background-light);
}

.confirm-btn {
  padding: var(--spacing-sm) var(--spacing-md);
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.confirm-btn:hover {
  background-color: var(--primary-dark);
  transform: translateY(-1px);
  box-shadow: var(--shadow-md);
}
</style>
