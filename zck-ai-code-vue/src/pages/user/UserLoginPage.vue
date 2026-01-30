<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <h2>用户登录</h2>
        <p>欢迎使用AI零代码应用生成平台</p>
      </div>

      <a-form
        :model="formState"
        :rules="rules"
        layout="vertical"
        class="login-form"
        @finish="handleLogin"
      >
        <!-- 用户名/邮箱输入 -->
        <a-form-item label="用户名/邮箱" name="userAccount">
          <a-input
            v-model:value="formState.userAccount"
            placeholder="请输入用户名或邮箱"
            :disabled="loading"
            prefix-icon="user"
          />
        </a-form-item>

        <!-- 密码输入 -->
        <a-form-item label="密码" name="userPassword">
          <a-input-password
            v-model:value="formState.userPassword"
            placeholder="请输入密码"
            :disabled="loading"
            prefix-icon="lock"
          />
        </a-form-item>

        <!-- 登录按钮 -->
        <a-form-item>
          <a-button type="primary" html-type="submit" class="login-button" :loading="loading">
            {{ loading ? '登录中...' : '登录' }}
          </a-button>
        </a-form-item>

        <!-- 注册链接 -->
        <div class="register-link">还没有账号？<a href="/user/register">立即注册</a></div>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { message } from 'ant-design-vue'
import { login } from '@/api/userController'
import { useLoginUserStore } from '@/stores/loginUser'

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()

// 表单状态
const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

// 加载状态
const loading = ref(false)

// 表单验证规则
const rules = {
  userAccount: [
    { required: true, message: '请输入用户名或邮箱', trigger: 'blur' },
    { min: 3, max: 50, message: '长度应在3-50个字符之间', trigger: 'blur' },
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' },
  ],
}

// 处理登录
const handleLogin = async () => {
  try {
    loading.value = true

    const response = await login(formState)
    const result = response.data

    if (result.code === 0 && result.data) {
      // 登录成功，保存用户信息
      localStorage.setItem('userInfo', JSON.stringify(result.data))
      // 更新登录用户store
      loginUserStore.setLoginUser(result.data)
      message.success('登录成功')

      // 检查是否有重定向路径
      const redirectPath = route.query.redirect as string
      if (redirectPath) {
        // 处理重定向路径，确保是有效的相对路径
        let path = decodeURIComponent(redirectPath)
        // 如果是完整URL，提取路径部分
        if (path.startsWith('http://') || path.startsWith('https://')) {
          const url = new URL(path)
          path = url.pathname
        }
        // 跳转到重定向路径
        router.push(path)
      } else {
        // 跳转到首页
        router.push('/')
      }
    }
    // 登录失败的错误提示由全局响应拦截器处理
  } catch (error) {
    // 网络错误的错误提示由全局响应拦截器处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - var(--header-height));
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, var(--background-page) 0%, #e0f2fe 100%);
  padding: var(--spacing-lg);
}

.login-container {
  background: var(--background-default);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-xl);
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: var(--spacing-lg);
}

.login-header h2 {
  margin: 0 0 var(--spacing-xs) 0;
  color: var(--primary-color);
  font-size: var(--font-size-xxl);
  font-weight: var(--font-weight-bold);
}

.login-header p {
  margin: 0;
  color: var(--text-secondary);
  font-size: var(--font-size-sm);
}

.login-form {
  width: 100%;
}

.login-button {
  width: 100%;
  height: 40px;
  font-size: var(--font-size-md);
}

.register-link {
  text-align: center;
  margin-top: var(--spacing-md);
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.register-link a {
  color: var(--primary-color);
  text-decoration: none;
  transition: color var(--transition-normal);
}

.register-link a:hover {
  color: var(--primary-hover);
  text-decoration: underline;
}

@media (max-width: var(--breakpoint-md)) {
  .login-container {
    padding: var(--spacing-lg);
    margin: 0 var(--spacing-md);
  }

  .login-header h2 {
    font-size: var(--font-size-xl);
  }
}
</style>
