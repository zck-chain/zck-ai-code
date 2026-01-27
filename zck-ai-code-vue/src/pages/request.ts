import axios from 'axios'
import { message } from 'ant-design-vue'
import { ErrorCode, ERROR_MESSAGES } from '@/utils/responseHandler'
import type { BaseResponse } from '@/utils/responseHandler'

// 创建 Axios 实例
const myAxios = axios.create({
  baseURL: 'http://localhost:8123/api',
  timeout: 60000,
  withCredentials: true,
})

// 全局请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // Do something before request is sent
    return config
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error)
  },
)

// 全局响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    const { data } = response as { data: BaseResponse<any> }

    // 检查响应是否为BaseResponse格式
    if (data && typeof data === 'object' && 'code' in data) {
      // 处理不同状态码
      switch (data.code) {
        case ErrorCode.SUCCESS:
          // 成功状态，直接返回响应
          return response
        case ErrorCode.NOT_LOGIN_ERROR:
          // 未登录状态
          if (
            !response.request.responseURL.includes('user/get/login') &&
            !window.location.pathname.includes('/user/login')
          ) {
            message.warning(ERROR_MESSAGES[ErrorCode.NOT_LOGIN_ERROR])
            window.location.href = `/user/login?redirect=${window.location.href}`
          }
          return response
        case ErrorCode.NO_AUTH_ERROR:
          // 无权限状态
          message.error(ERROR_MESSAGES[ErrorCode.NO_AUTH_ERROR])
          // 这里可以跳转到权限申请页面
          return response
        default:
          // 其他错误状态，显示错误提示
          if (data.code !== ErrorCode.SUCCESS) {
            const errorMessage =
              data.message ||
              (data.code !== undefined ? ERROR_MESSAGES[data.code] : undefined) ||
              '未知错误'
            message.error(errorMessage)
          }
          return response
      }
    }

    // 非BaseResponse格式的响应，直接返回
    return response
  },
  function (error) {
    // 处理HTTP错误
    if (error.response) {
      // 服务器返回错误状态码
      const status = error.response.status
      switch (status) {
        case 401:
          message.error('未授权，请重新登录')
          break
        case 403:
          message.error('禁止访问')
          break
        case 404:
          message.error('请求资源不存在')
          break
        case 500:
          message.error('服务器内部错误')
          break
        default:
          message.error(`请求失败: ${status}`)
      }
    } else if (error.request) {
      // 请求已发送但没有收到响应
      message.error('网络错误，请稍后重试')
    } else {
      // 请求配置出错
      message.error(`请求错误: ${error.message}`)
    }
    return Promise.reject(error)
  },
)

export default myAxios
