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

        <!-- 错误提示 -->
        <div v-if="errorMessage" class="error-message">
          <a-alert
            type="error"
            message="登录失败"
            description="{{ errorMessage }}"
            show-icon
            banner
          />
        </div>

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
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { login } from '@/api/userController'

const router = useRouter()

// 表单状态
const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

// 加载状态
const loading = ref(false)

// 错误信息
const errorMessage = ref('')

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
    errorMessage.value = ''

    const response = await login(formState)
    const result = response.data

    if (result.code === 0 && result.data) {
      // 登录成功，保存用户信息
      localStorage.setItem('userInfo', JSON.stringify(result.data))
      message.success('登录成功')

      // 跳转到首页
      router.push('/')
    } else {
      errorMessage.value = result.message || '登录失败，请检查账号密码'
    }
  } catch (error: any) {
    errorMessage.value = error.message || '网络错误，请稍后重试'
    message.error('登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 24px;
}

.login-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: 32px;
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.login-header h2 {
  margin: 0 0 8px 0;
  color: #1890ff;
  font-size: 24px;
  font-weight: bold;
}

.login-header p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.login-form {
  width: 100%;
}

.login-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.register-link {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #666;
}

.register-link a {
  color: #1890ff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

.error-message {
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .login-container {
    padding: 24px;
    margin: 0 16px;
  }

  .login-header h2 {
    font-size: 20px;
  }
}
</style>
