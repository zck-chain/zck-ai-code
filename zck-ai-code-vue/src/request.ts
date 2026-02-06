import axios, { type AxiosError, type AxiosResponse, type InternalAxiosRequestConfig } from 'axios'
import { message } from 'ant-design-vue'
import { ErrorCode, ERROR_MESSAGES } from '@/utils/responseHandler'
import type { BaseResponse } from '@/utils/responseHandler'
import { API_CONFIG } from '@/config/api'

const { BASE_URL, TIMEOUT } = API_CONFIG

const myAxios = axios.create({
  baseURL: BASE_URL,
  timeout: TIMEOUT,
  withCredentials: true,
})

const handleRequest = (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
  return config
}

const handleRequestError = (error: AxiosError): Promise<AxiosError> => {
  return Promise.reject(error)
}

const checkResponseCode = (response: AxiosResponse): AxiosResponse => {
  const data = response.data as BaseResponse<unknown>

  if (data && typeof data === 'object' && 'code' in data) {
    switch (data.code) {
      case ErrorCode.SUCCESS:
        return response

      case ErrorCode.NOT_LOGIN_ERROR:
        // 白名单接口，允许未登录用户访问
        const publicPaths = [
          '/app/list/featured', // 精选案例
          '/user/get/login'      // 获取登录状态
        ]
        
        const requestUrl = response.config.url || ''
        const isPublicPath = publicPaths.some(path => requestUrl.includes(path))
        
        if (!isPublicPath && 
            !response.request.responseURL.includes('user/get/login') &&
            !window.location.pathname.includes('/user/login')) {
          message.warning(ERROR_MESSAGES[ErrorCode.NOT_LOGIN_ERROR])
          const redirectPath = encodeURIComponent(window.location.pathname + window.location.search)
          window.location.href = `/user/login?redirect=${redirectPath}`
        }
        return response

      case ErrorCode.NO_AUTH_ERROR:
        message.error(ERROR_MESSAGES[ErrorCode.NO_AUTH_ERROR])
        return response

      default:
        if (data.code !== ErrorCode.SUCCESS) {
          const errorMessage = data.message ||
            (data.code !== undefined ? ERROR_MESSAGES[data.code] : undefined) ||
            '未知错误'
          message.error(errorMessage)
        }
        return response
    }
  }

  return response
}

const handleHttpError = (error: AxiosError): void => {
  if (error.response) {
    const status = error.response.status
    const errorMessages: Record<number, string> = {
      401: '未授权，请重新登录',
      403: '禁止访问',
      404: '请求资源不存在',
      500: '服务器内部错误',
    }
    message.error(errorMessages[status] || `请求失败: ${status}`)
  } else if (error.request) {
    message.error('网络错误，请稍后重试')
  } else {
    message.error(`请求错误: ${error.message}`)
  }
}

const handleResponseError = (error: AxiosError): Promise<AxiosError> => {
  handleHttpError(error)
  return Promise.reject(error)
}

myAxios.interceptors.request.use(handleRequest, handleRequestError)

myAxios.interceptors.response.use(checkResponseCode, handleResponseError)

export default myAxios
