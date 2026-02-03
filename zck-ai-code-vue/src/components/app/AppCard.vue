<template>
  <div class="app-card">
    <div class="app-cover" :style="{ backgroundImage: `url(${app.cover || 'https://via.placeholder.com/400x300'})` }">
      <div class="app-cover-actions">
        <button
          @click="$emit('view-chat', app.id!)"
          class="btn-view-chat"
        >
          查看对话
        </button>
        <button
          v-if="app.deployKey"
          @click="$emit('view-work', app.deployKey)"
          class="btn-view-work"
        >
          查看作品
        </button>
      </div>
    </div>
    <div class="app-info">
      <h3 class="app-name">{{ app.appName || '未命名应用' }}</h3>
      <div class="app-meta">
        <span class="author-name">{{ app.authorName || '我' }}</span>
      </div>
      <div class="app-footer">
        <span class="programming-tag">编程号</span>
        <span class="codefather-tag">codefather</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  app: API.App
}>()

defineEmits<{
  (e: 'view-chat', appId: string | number): void
  (e: 'view-work', deployKey: string): void
}>()
</script>

<style scoped>
.app-card {
  background-color: var(--background-default);
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
  border: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  height: 100%;
  position: relative;
  animation: fadeInUp 0.6s ease-out;
}

.app-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-color);
}

.app-card:hover .app-cover {
  transform: scale(1.03);
}

.app-cover {
  height: 200px;
  background-size: cover;
  background-position: center;
  background-color: #f3f4f6;
  border-radius: var(--border-radius-lg) var(--border-radius-lg) 0 0;
  transition: var(--transition-normal);
  overflow: hidden;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.app-cover::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, rgba(0,0,0,0.2) 0%, rgba(0,0,0,0.4) 100%);
  pointer-events: none;
  z-index: 1;
}

.app-cover-actions {
  display: flex;
  gap: 0.75rem;
  position: relative;
  z-index: 2;
}

.btn-view-chat {
  padding: 0.5rem 1rem;
  background-color: #1677ff;
  color: white;
  border: none;
  border-radius: var(--border-radius-sm);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-family: inherit;
}

.btn-view-chat:hover {
  background-color: #4096ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(22, 119, 255, 0.3);
}

.btn-view-work {
  padding: 0.5rem 1rem;
  background-color: white;
  color: #333;
  border: none;
  border-radius: var(--border-radius-sm);
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-family: inherit;
}

.btn-view-work:hover {
  background-color: #f5f5f5;
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.app-info {
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  flex: 1;
}

.app-name {
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
  font-family: inherit;
  line-height: 1.4;
  transition: var(--transition-normal);
}

.app-card:hover .app-name {
  color: var(--primary-color);
}

.app-meta {
  font-size: 0.75rem;
  color: var(--text-tertiary);
  margin: 0;
}

.author-name {
  color: var(--text-tertiary);
  font-size: 0.75rem;
  font-family: inherit;
}

.app-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  padding-top: 0.75rem;
  border-top: 1px solid var(--border-color);
}

.programming-tag {
  font-size: 0.75rem;
  color: var(--text-tertiary);
  font-family: inherit;
}

.codefather-tag {
  font-size: 0.75rem;
  color: var(--text-tertiary);
  font-family: inherit;
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

@media (max-width: 768px) {
  .app-cover {
    height: 150px;
  }
  
  .app-info {
    padding: 1rem;
  }
  
  .app-name {
    font-size: 0.875rem;
  }
}
</style>