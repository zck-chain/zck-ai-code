import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'

// 错误码枚举
export enum ErrorCode {
  SUCCESS = 0,
  PARAMS_ERROR = 40000,
  NOT_LOGIN_ERROR = 40100,
  NO_AUTH_ERROR = 40101,
  NOT_FOUND_ERROR = 40400,
  FORBIDDEN_ERROR = 40300,
  SYSTEM_ERROR = 50000,
  OPERATION_ERROR = 50001
}

// 错误码消息映射
export const ERROR_MESSAGES: Record<number, string> = {
  [ErrorCode.SUCCESS]: "ok",
  [ErrorCode.PARAMS_ERROR]: "请求参数错误",
  [ErrorCode.NOT_LOGIN_ERROR]: "未登录",
  [ErrorCode.NO_AUTH_ERROR]: "无权限",
  [ErrorCode.NOT_FOUND_ERROR]: "请求数据不存在",
  [ErrorCode.FORBIDDEN_ERROR]: "禁止访问",
  [ErrorCode.SYSTEM_ERROR]: "系统内部异常",
  [ErrorCode.OPERATION_ERROR]: "操作失败"
}

// 基础响应类型
export type BaseResponse<T> = {
  code?: number
  data?: T
  message?: string
}

/**
 * 处理API响应
 * @param response API响应数据
 * @returns 响应中的data字段数据
 */
export function handleResponse<T>(response: BaseResponse<T>): T {
  const { code, data, message } = response

  // 检查响应状态码
  if (code === undefined) {
    throw new Error('响应数据格式错误，缺少code字段')
  }

  // 处理不同状态码
  switch (code) {
    case ErrorCode.SUCCESS:
      // 成功状态，返回data字段
      return data as T
    case ErrorCode.NOT_LOGIN_ERROR:
      // 未登录状态，跳转到登录页面
      handleNotLogin()
      throw new Error(message || ERROR_MESSAGES[code])
    case ErrorCode.NO_AUTH_ERROR:
      // 无权限状态，跳转到权限申请页面
      handleNoAuth()
      throw new Error(message || ERROR_MESSAGES[code])
    default:
      // 其他错误状态，显示错误提示
      showError(message || ERROR_MESSAGES[code] || '未知错误')
      throw new Error(message || ERROR_MESSAGES[code] || '未知错误')
  }
}

/**
 * 处理未登录状态
 */
function handleNotLogin(): void {
  const router = useRouter()
  // 跳转到登录页面，并携带当前页面作为重定向地址
  router.push({
    path: '/user/login',
    query: {
      redirect: window.location.href
    }
  })
}

/**
 * 处理无权限状态
 */
function handleNoAuth(): void {
  // 显示权限不足提示
  message.error('权限不足')
  // 这里可以跳转到权限申请页面
  // const router = useRouter()
  // router.push('/permission/apply')
}

/**
 * 显示错误提示
 * @param errorMessage 错误消息
 */
function showError(errorMessage: string): void {
  message.error(errorMessage)
}

/**
 * 检查响应是否成功
 * @param response API响应数据
 * @returns 是否成功
 */
export function isResponseSuccess(response: BaseResponse<any>): boolean {
  return response.code === ErrorCode.SUCCESS
}

/**
 * 提取响应数据
 * @param response API响应数据
 * @returns 响应中的data字段数据
 */
export function extractResponseData<T>(response: BaseResponse<T>): T | undefined {
  return response.data
}
