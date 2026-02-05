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
        <template
          v-if="
            loginUserStore.loginUser &&
            loginUserStore.loginUser.userName &&
            loginUserStore.loginUser.userName !== '未登录'
          "
        >
          <!-- 已登录状态：只显示用户头像 -->
          <div class="user-profile">
            <a-dropdown trigger="click" :overlay-style="{ animation: 'slide-down 0.3s' }">
              <template #overlay>
                <a-menu @click="handleMenuClick" class="user-dropdown-menu">
                  <a-menu-item key="profile">个人主页</a-menu-item>
                  <a-menu-item key="favorites">我的收藏</a-menu-item>
                  <a-menu-item key="logout">退出登录</a-menu-item>
                </a-menu>
              </template>
              <a class="avatar-trigger" href="javascript:void(0);">
                <a-avatar
                  :src="
                    loginUserStore.loginUser.userAvatar ||
                    'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20icon%20simple%20flat%20design%20blue%20background&image_size=square'
                  "
                  :alt="loginUserStore.loginUser.userName"
                  class="user-avatar"
                />
              </a>
            </a-dropdown>
          </div>
        </template>
        <template v-else>
          <!-- 未登录状态：显示登录按钮 -->
          <a-button type="primary" @click="handleLoginClick">登录</a-button>
        </template>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { logout } from '@/api/userController'

const router = useRouter()
const loginUserStore = useLoginUserStore()

// 原始菜单项配置
const originMenuItems = ref([
  {
    key: 'home',
    label: '首页',
    path: '/',
  },
  {
    key: 'about',
    label: '关于我们',
    path: '/about',
  },
  {
    key: 'services',
    label: '服务',
    path: '/services',
  },
  {
    key: 'contact',
    label: '联系我们',
    path: '/contact',
  },
  {
    key: 'userManage',
    label: '用户管理',
    path: '/admin/userManage',
    requiresAdmin: true,
  },
  {
    key: 'appManage',
    label: '应用管理',
    path: '/admin/appManage',
    requiresAdmin: true,
  },
])

// 基于用户权限过滤后的菜单项
const menuItems = computed(() => {
  const loginUser = loginUserStore.loginUser
  return originMenuItems.value.filter((item) => {
    if (item.requiresAdmin) {
      return loginUser && loginUser.userRole === 'admin'
    }
    return true
  })
})

// 处理菜单点击
const handleMenuClick = (e: any) => {
  const key = typeof e === 'string' ? e : e.key

  // 处理顶部导航栏菜单项
  const navItem = menuItems.value.find((item) => item.key === key)
  if (navItem && navItem.path) {
    router.push(navItem.path)
    return
  }

  // 处理下拉菜单菜单项
  switch (key) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'favorites':
      router.push('/user/favorites')
      break
    case 'logout':
      handleLogout()
      break
    default:
      break
  }
}

// 处理登录按钮点击
const handleLoginClick = () => {
  router.push('/user/login')
}

// 处理退出登录
const handleLogout = async () => {
  try {
    // 调用退出登录API
    const response = await logout()
    if (response.data.code === 0) {
      // 清除本地存储的用户信息
      localStorage.removeItem('userInfo')
      // 更新store中的用户信息为未登录状态
      loginUserStore.setLoginUser({ userName: '未登录' })
      // 跳转到首页
      router.push('/')
      message.success('退出登录成功')
    } else {
      message.error('退出登录失败')
    }
  } catch (error) {
    console.error('退出登录失败:', error)
    message.error('退出登录失败，请稍后重试')
  }
}

// 初始化时获取登录用户信息
onMounted(async () => {
  try {
    await loginUserStore.fetchLoginUser()
  } catch (error) {
    console.error('获取登录用户信息失败:', error)
  }
})
</script>

<style scoped>
.global-header {
  background: var(--background-default);
  box-shadow: var(--shadow-md);
  position: sticky;
  top: 0;
  z-index: 100;
  transition: box-shadow var(--transition-normal);
}

.global-header:hover {
  box-shadow: var(--shadow-lg);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: var(--max-container-width);
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
  height: var(--header-height);
  transition: padding var(--transition-normal);
}

.logo-title {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  transition: gap var(--transition-normal);
}

.logo {
  width: 32px;
  height: 32px;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.title {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  margin: 0;
  color: var(--primary-color);
  transition:
    font-size var(--transition-normal),
    color var(--transition-normal);
}

.title:hover {
  color: var(--primary-hover);
}

.menu {
  flex: 1;
  margin: 0 var(--spacing-xxl);
  transition: margin var(--transition-normal);
}

/* 菜单项样式优化 */
.menu :deep(.ant-menu-item) {
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  padding: 0 var(--spacing-md);
  margin: 0 var(--spacing-xs);
  border-radius: var(--border-radius-md);
  transition: all var(--transition-normal);
}

.menu :deep(.ant-menu-item:hover) {
  color: var(--primary-color);
  background-color: rgba(24, 144, 255, 0.1);
}

.menu :deep(.ant-menu-item-selected) {
  color: var(--primary-color);
  background-color: rgba(24, 144, 255, 0.1);
  font-weight: var(--font-weight-medium);
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.user-profile {
  display: flex;
  align-items: center;
}

.avatar-trigger {
  display: inline-block;
  position: relative;
  cursor: pointer;
  padding: var(--spacing-xs);
  border-radius: var(--border-radius-md);
  transition: all var(--transition-normal);
}

.avatar-trigger:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.user-avatar {
  width: 32px;
  height: 32px;
  transition: transform var(--transition-normal);
  border-radius: var(--border-radius-full);
  overflow: hidden;
}

.avatar-trigger:hover .user-avatar {
  transform: scale(1.1);
}

.avatar-trigger:focus {
  outline: none;
  box-shadow: none;
}

/* 登录按钮样式优化 */
.user-info :deep(.ant-btn-primary) {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
  font-size: var(--font-size-sm);
  padding: var(--spacing-xs) var(--spacing-md);
  border-radius: var(--border-radius-md);
  transition: all var(--transition-normal);
}

.user-info :deep(.ant-btn-primary:hover) {
  background-color: var(--primary-hover);
  border-color: var(--primary-hover);
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.3);
}

.user-info :deep(.ant-btn-primary:active) {
  background-color: var(--primary-active);
  border-color: var(--primary-active);
}

/* 下拉菜单样式优化 */
.user-info :deep(.ant-dropdown-menu) {
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-lg);
  padding: var(--spacing-xs) 0;
  border: 1px solid var(--border-color);
  animation: slide-down var(--transition-normal);
}

/* 下拉菜单动画 */
@keyframes slide-down {
  from {
    opacity: 0;
    transform: translateY(-8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-info :deep(.ant-dropdown-menu-item) {
  font-size: var(--font-size-sm);
  padding: 10px var(--spacing-md);
  margin: 0;
  transition: all var(--transition-normal);
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.user-info :deep(.ant-dropdown-menu-item:hover) {
  color: var(--primary-color);
  background-color: rgba(24, 144, 255, 0.1);
}

.user-info :deep(.ant-dropdown-menu-item:active) {
  background-color: rgba(24, 144, 255, 0.15);
}

/* 修复下拉菜单箭头问题 */
.user-info :deep(.ant-dropdown-arrow) {
  display: none;
}

/* 确保下拉菜单在点击其他区域时关闭且位置正确 */
.user-info :deep(.ant-dropdown) {
  z-index: 1050;
}

/* 修复下拉菜单宽度和位置 */
.user-info :deep(.ant-dropdown-menu) {
  min-width: 120px;
  max-width: 200px;
  right: 0;
  left: auto !important;
}

/* 确保菜单项完整显示 */
.user-info :deep(.ant-dropdown-menu-item) {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 响应式设计优化 */
@media (max-width: var(--breakpoint-lg)) {
  .header-content {
    padding: 0 20px;
  }

  .menu {
    margin: 0 32px;
  }

  .title {
    font-size: 17px;
  }
}

@media (max-width: var(--breakpoint-md)) {
  .header-content {
    padding: 0 var(--spacing-md);
  }

  .title {
    font-size: var(--font-size-md);
  }

  .menu {
    margin: 0 var(--spacing-md);
  }

  .logo-title {
    gap: var(--spacing-sm);
  }

  .logo {
    width: 28px;
    height: 28px;
  }

  .user-name {
    display: none;
  }

  .user-profile {
    gap: var(--spacing-xs);
  }
}

@media (max-width: var(--breakpoint-xs)) {
  .header-content {
    padding: 0 var(--spacing-sm);
  }

  .title {
    font-size: 15px;
  }

  .menu {
    margin: 0 var(--spacing-sm);
  }

  .menu :deep(.ant-menu-item) {
    padding: 0 var(--spacing-md);
    font-size: 13px;
  }
}
</style>
