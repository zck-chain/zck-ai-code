<template>
  <a-layout class="basic-layout">
    <GlobalHeader />
    <a-layout-content class="content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </a-layout-content>
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '../components/GlobalHeader.vue'
import GlobalFooter from '../components/GlobalFooter.vue'
</script>

<style scoped>
.basic-layout {
  min-height: 100vh;
}

.content {
  padding: var(--spacing-lg);
  min-height: var(--content-min-height);
  background: var(--background-page);
}

@media (max-width: var(--breakpoint-md)) {
  .content {
    padding: var(--spacing-md);
  }
}
</style>
