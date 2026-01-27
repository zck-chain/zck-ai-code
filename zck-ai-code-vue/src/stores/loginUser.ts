import { defineStore } from 'pinia'
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { getLoginUser } from '@/api/userController'

export const useLoginUserStore = defineStore('loginUser', () => {
  // 默认值
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  // 加载状态
  const loading = ref(false)

  // 获取登录用户信息
  async function fetchLoginUser() {
    try {
      loading.value = true
      const res = await getLoginUser()
      if (res.data.code === 0 && res.data.data) {
        loginUser.value = res.data.data
      } else {
        // 保持默认状态
        loginUser.value = {
          userName: '未登录',
        }
      }
    } catch (error) {
      console.error('获取登录用户信息失败:', error)
      message.error('获取登录状态失败，请稍后重试')
      // 保持默认状态
      loginUser.value = {
        userName: '未登录',
      }
    } finally {
      loading.value = false
    }
  }

  // 更新登录用户信息
  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser
  }

  // 检查用户是否已登录
  function isLoggedIn(): boolean {
    return loginUser.value.userName !== '未登录' && !!loginUser.value.id
  }

  return { loginUser, loading, fetchLoginUser, setLoginUser, isLoggedIn }
})
