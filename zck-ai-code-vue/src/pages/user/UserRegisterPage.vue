<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-header">
        <h2>用户注册</h2>
        <p>欢迎加入AI零代码应用生成平台</p>
      </div>

      <a-form
        :model="formState"
        :rules="rules"
        layout="vertical"
        class="register-form"
        @finish="handleRegister"
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
            placeholder="请输入密码（6-20位）"
            :disabled="loading"
            prefix-icon="lock"
          />
        </a-form-item>

        <!-- 确认密码输入 -->
        <a-form-item label="确认密码" name="checkPassword">
          <a-input-password
            v-model:value="formState.checkPassword"
            placeholder="请再次输入密码"
            :disabled="loading"
            prefix-icon="lock"
          />
        </a-form-item>

        <!-- 错误提示 -->
        <div v-if="errorMessage" class="error-message">
          <a-alert
            type="error"
            message="注册失败"
            description="{{ errorMessage }}"
            show-icon
            banner
          />
        </div>

        <!-- 注册按钮 -->
        <a-form-item>
          <a-button type="primary" html-type="submit" class="register-button" :loading="loading">
            {{ loading ? '注册中...' : '注册' }}
          </a-button>
        </a-form-item>

        <!-- 登录链接 -->
        <div class="login-link">已有账号？<a href="/user/login">立即登录</a></div>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { register } from '@/api/userController'

const router = useRouter()

// 表单状态
const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
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
    {
      validator: (_: any, value: string, callback: (error?: string) => void) => {
        if (value) {
          // 简单的邮箱格式验证
          const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
          if (emailRegex.test(value)) {
            callback()
          } else if (value.length >= 3) {
            callback()
          } else {
            callback('请输入有效的用户名或邮箱')
          }
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' },
    {
      validator: (_: any, value: string, callback: (error?: string) => void) => {
        if (value) {
          // 密码强度验证
          let strength = 0
          if (/[A-Z]/.test(value)) strength++
          if (/[a-z]/.test(value)) strength++
          if (/[0-9]/.test(value)) strength++
          if (/[^A-Za-z0-9]/.test(value)) strength++

          if (strength < 2) {
            callback('密码强度过低，请包含字母和数字')
          } else {
            callback()
          }
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (_: any, value: string, callback: (error?: string) => void) => {
        if (value && value !== formState.userPassword) {
          callback('两次输入的密码不一致')
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

// 处理注册
const handleRegister = async () => {
  try {
    loading.value = true
    errorMessage.value = ''

    const response = await register(formState)
    const result = response.data

    if (result.code === 0) {
      // 注册成功
      message.success('注册成功，即将跳转到登录页面')

      // 延迟跳转到登录页面
      setTimeout(() => {
        router.push('/user/login')
      }, 1500)
    } else {
      errorMessage.value = result.message || '注册失败，请稍后重试'
    }
  } catch (error: any) {
    errorMessage.value = error.message || '网络错误，请稍后重试'
    message.error('注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 24px;
}

.register-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  padding: 32px;
  width: 100%;
  max-width: 400px;
}

.register-header {
  text-align: center;
  margin-bottom: 24px;
}

.register-header h2 {
  margin: 0 0 8px 0;
  color: #1890ff;
  font-size: 24px;
  font-weight: bold;
}

.register-header p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.register-form {
  width: 100%;
}

.register-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.login-link {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #666;
}

.login-link a {
  color: #1890ff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}

.error-message {
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .register-container {
    padding: 24px;
    margin: 0 16px;
  }

  .register-header h2 {
    font-size: 20px;
  }
}
</style>
