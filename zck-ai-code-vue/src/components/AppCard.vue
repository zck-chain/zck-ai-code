<script setup lang="ts">
const getCoverUrl = (cover?: string) => {
  if (!cover) return ''
  if (cover.startsWith('http://') || cover.startsWith('https://')) {
    return cover
  }
  return `https://${cover}`
}

defineProps<{
  app: API.App
}>()

defineEmits<{
  (e: 'view-chat', appId: string | number): void
  (e: 'view-work', deployKey: string): void
}>()
</script>

<template>
  <div class="case-card">
    <div class="case-cover" :style="{ backgroundImage: `url(${getCoverUrl(app.cover) || 'https://via.placeholder.com/400x300'})` }">
      <div class="case-cover-actions">
        <button
          @click="app.id && $emit('view-chat', app.id)"
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
</template>

<style scoped>
.case-card {
  background-color: white;
  border-radius: var(--border-radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-normal);
}

.case-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.case-cover {
  position: relative;
  width: 100%;
  height: 200px;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  overflow: hidden;
}

.case-cover-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  padding: var(--spacing-md);
  display: flex;
  gap: var(--spacing-sm);
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.case-card:hover .case-cover-actions {
  opacity: 1;
}

.btn-view-chat,
.btn-view-work {
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--border-radius-sm);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--transition-normal);
  border: none;
}

.btn-view-chat {
  background-color: var(--primary-color);
  color: white;
}

.btn-view-chat:hover {
  background-color: var(--primary-dark);
}

.btn-view-work {
  background-color: white;
  color: var(--text-primary);
}

.btn-view-work:hover {
  background-color: var(--background-light);
}

.case-info {
  padding: var(--spacing-md);
}

.case-name {
  font-size: var(--font-size-md);
  font-weight: var(--font-weight-bold);
  color: var(--text-primary);
  margin: 0 0 var(--spacing-xs) 0;
  line-height: 1.4;
}

.case-meta {
  margin-bottom: var(--spacing-sm);
}

.author-name {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.case-footer {
  display: flex;
  gap: var(--spacing-xs);
  margin-top: var(--spacing-xs);
}

.programming-tag,
.codefather-tag {
  font-size: var(--font-size-xs);
  padding: 2px 8px;
  border-radius: var(--border-radius-sm);
  background-color: var(--background-light);
  color: var(--text-secondary);
}
</style>
