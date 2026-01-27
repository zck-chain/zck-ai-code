import request from '../pages/request'

/**
 * 添加用户
 * @param body 用户添加请求参数
 * @param options 额外选项
 * @returns 返回添加结果
 */
export async function addUser(body: API.UserAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/user/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/**
 * 删除用户
 * @param body 删除请求参数
 * @param options 额外选项
 * @returns 返回删除结果
 */
export async function deleteUser(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/user/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/**
 * 根据ID获取用户
 * @param params 获取用户参数
 * @param options 额外选项
 * @returns 返回用户信息
 */
export async function getUserById(params: API.getUserByIdParams, options?: { [key: string]: any }) {
  return request<API.BaseResponseUser>('/user/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/**
 * 获取登录用户信息
 * @param options 额外选项
 * @returns 返回登录用户信息
 */
export async function getLoginUser(options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginUserVO>('/user/get/login', {
    method: 'GET',
    ...(options || {}),
  })
}

/**
 * 根据ID获取用户VO
 * @param params 获取用户VO参数
 * @param options 额外选项
 * @returns 返回用户VO信息
 */
export async function getUserInfoVo(
  params: API.getUserInfoVoParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserVO>('/user/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/**
 * 分页获取用户VO列表
 * @param body 分页查询参数
 * @param options 额外选项
 * @returns 返回用户VO列表
 */
export async function listUserVoByPage(
  body: API.UserQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageUserVO>('/user/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/**
 * 用户登录
 * @param body 登录请求参数
 * @param options 额外选项
 * @returns 返回登录结果
 */
export async function login(body: API.UserLoginRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginUserVO>('/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/**
 * 用户登出
 * @param options 额外选项
 * @returns 返回登出结果
 */
export async function logout(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/user/logout', {
    method: 'GET',
    ...(options || {}),
  })
}

/**
 * 用户注册
 * @param body 注册请求参数
 * @param options 额外选项
 * @returns 返回注册结果
 */
export async function register(body: API.UserRegisterRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/**
 * 更新用户信息
 * @param body 更新请求参数
 * @param options 额外选项
 * @returns 返回更新结果
 */
export async function updateUser(body: API.UserUpdateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/user/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/**
 * 上传用户头像
 * @param formData 表单数据，包含头像文件
 * @param options 额外选项
 * @returns 返回上传结果
 */
export async function uploadAvatar(formData: FormData, options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/user/upload/avatar', {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data: formData,
    ...(options || {}),
  })
}
