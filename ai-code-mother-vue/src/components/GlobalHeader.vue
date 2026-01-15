<template>
  <a-layout-header class="global-header">
    <div class="header-content">
      <!-- 左侧 Logo 和标题 -->
      <div class="logo-title">
        <img src="../assets/logo.svg" alt="Logo" class="logo" />
        <h1 class="title">AI零代码应用生成平台</h1>
      </div>
      
      <!-- 中间菜单项 -->
      <a-menu 
        :items="menuItems" 
        mode="horizontal" 
        theme="light"
        class="menu"
        @click="handleMenuClick"
      />
      
      <!-- 右侧用户信息 -->
      <div class="user-info">
        <a-button type="primary">登录</a-button>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// 菜单项配置
const menuItems = ref([
  {
    key: 'home',
    label: '首页',
    path: '/'
  },
  {
    key: 'about',
    label: '关于我们',
    path: '/about'
  },
  {
    key: 'services',
    label: '服务',
    path: '/services'
  },
  {
    key: 'contact',
    label: '联系我们',
    path: '/contact'
  }
]);

// 处理菜单点击
const handleMenuClick = (e: any) => {
  const item = menuItems.value.find(item => item.key === e.key);
  if (item && item.path) {
    router.push(item.path);
  }
};
</script>

<style scoped>
.global-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
}

.logo-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  width: 32px;
  height: 32px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  margin: 0;
  color: #1890ff;
}

.menu {
  flex: 1;
  margin: 0 48px;
}

.user-info {
  display: flex;
  align-items: center;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }
  
  .title {
    font-size: 16px;
  }
  
  .menu {
    margin: 0 24px;
  }
}
</style>