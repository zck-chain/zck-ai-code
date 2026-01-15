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
import GlobalHeader from '../components/GlobalHeader.vue';
import GlobalFooter from '../components/GlobalFooter.vue';
</script>

<style scoped>
.basic-layout {
  min-height: 100vh;
}

.content {
  padding: 24px;
  min-height: calc(100vh - 64px - 70px);
  background: #f0f2f5;
}

@media (max-width: 768px) {
  .content {
    padding: 16px;
  }
}
</style>