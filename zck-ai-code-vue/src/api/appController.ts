// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 管理员删除应用 管理员根据ID删除任意应用 POST /app/admin/delete */
export async function adminDeleteApp(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/app/admin/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 管理员获取应用详情 管理员根据ID查看任意应用详情 GET /app/admin/get */
export async function adminGetAppById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.adminGetAppByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseApp>('/app/admin/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 管理员查询应用列表 管理员分页查询所有应用列表，支持根据除时间外的任何字段查询，每页数量不限 POST /app/admin/list/page */
export async function adminListAppByPage(
  body: API.AppQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageApp>('/app/admin/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 管理员更新应用 管理员更新任意应用信息，支持更新应用名称、应用封面、优先级 POST /app/admin/update */
export async function adminUpdateApp(body: API.AppUpdateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/app/admin/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 此处后端没有提供注释 GET /app/build/status/${param0} */
export async function getBuildStatus(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getBuildStatusParams,
  options?: { [key: string]: any }
) {
  const { appId: param0, ...queryParams } = params
  return request<API.BaseResponseMapStringObject>(`/app/build/status/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 生成代码 通过对话方式生成代码，流式返回结果 GET /app/chat/gen/code */
export async function chatToGenCode(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.chatToGenCodeParams,
  options?: { [key: string]: any }
) {
  return request<API.ServerSentEventString[]>('/app/chat/gen/code', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 创建应用 用户创建新应用，须填写 initPrompt POST /app/create */
export async function createApp(body: API.AppAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/app/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 删除应用 用户删除自己的应用 POST /app/delete */
export async function deleteApp(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/app/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 部署应用 部署应用并返回部署 URL POST /app/deploy */
export async function deployApp(body: API.AppDeployRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseString>('/app/deploy', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 下载代码 下载代码并返回代码压缩包.zip GET /app/download/${param0} */
export async function downloadAppCode(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadAppCodeParams,
  options?: { [key: string]: any }
) {
  const { appId: param0, ...queryParams } = params
  return request<any>(`/app/download/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取应用详情 用户根据ID查看自己的应用详情 GET /app/get */
export async function getAppById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAppByIdParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseApp>('/app/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 查询精选应用列表 分页查询精选的应用列表，支持根据名称查询，每页最多20个 POST /app/list/featured/page */
export async function listFeaturedAppByPage(
  body: API.AppQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageApp>('/app/list/featured/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 查询我的应用列表 用户分页查询自己的应用列表，支持根据名称查询，每页最多20个 POST /app/list/my/page */
export async function listMyAppByPage(body: API.AppQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponsePageApp>('/app/list/my/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 优化提示词 用户创建新应用，须填写 initPrompt POST /app/oiptimise */
export async function getAiCodeGenPrompt(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAiCodeGenPromptParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse>('/app/oiptimise', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 更新应用 用户修改自己的应用信息，目前只支持修改应用名称 POST /app/update */
export async function updateApp(body: API.AppUpdateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/app/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
