// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 删除应用所有版本 删除指定应用的所有版本记录（谨慎操作） DELETE /versions/${param0} */
export async function deleteAppVersions(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteAppVersionsParams,
  options?: { [key: string]: any }
) {
  const { appId: param0, ...queryParams } = params
  return request<API.BaseResponseString>(`/versions/${param0}`, {
    method: 'DELETE',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 对比两个版本 对比指定两个版本之间的代码差异 GET /versions/${param0}/compare/${param1}/${param2} */
export async function compareVersions(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.compareVersionsParams,
  options?: { [key: string]: any }
) {
  const { appId: param0, versionId1: param1, versionId2: param2, ...queryParams } = params
  return request<API.BaseResponseDiffResult>(`/versions/${param0}/compare/${param1}/${param2}`, {
    method: 'GET',
    params: {
      ...queryParams,
      request: undefined,
      ...queryParams['request'],
    },
    ...(options || {}),
  })
}

/** 与最新版本对比 将提供的代码与指定应用的最新版本进行对比 POST /versions/${param0}/compare/latest */
export async function compareWithLatest(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.compareWithLatestParams,
  body: API.CompareLatestRequest,
  options?: { [key: string]: any }
) {
  const { appId: param0, ...queryParams } = params
  return request<API.BaseResponseDiffResult>(`/versions/${param0}/compare/latest`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  })
}

/** 获取最新版本 获取指定应用的最新代码版本 GET /versions/${param0}/latest */
export async function getLatestVersion(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getLatestVersionParams,
  options?: { [key: string]: any }
) {
  const { appId: param0, ...queryParams } = params
  return request<API.BaseResponseVersionCode>(`/versions/${param0}/latest`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 获取版本列表 获取指定应用的所有版本列表，按创建时间倒序排列 GET /versions/${param0}/list */
export async function getVersionList(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getVersionListParams,
  options?: { [key: string]: any }
) {
  const { appId: param0, ...queryParams } = params
  return request<API.BaseResponseListVersionCode>(`/versions/${param0}/list`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 回滚到指定版本 将应用代码回滚到指定的历史版本，并创建新的回滚版本 POST /versions/${param0}/rollback/${param1} */
export async function rollbackToVersion(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.rollbackToVersionParams,
  body: API.RollbackRequest,
  options?: { [key: string]: any }
) {
  const { appId: param0, targetVersionId: param1, ...queryParams } = params
  return request<API.BaseResponseVersionCode>(`/versions/${param0}/rollback/${param1}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...queryParams },
    data: body,
    ...(options || {}),
  })
}

/** 搜索版本 根据关键字搜索指定应用的版本 GET /versions/${param0}/search */
export async function searchVersions(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.searchVersionsParams,
  options?: { [key: string]: any }
) {
  const { appId: param0, ...queryParams } = params
  return request<API.BaseResponseListVersionCode>(`/versions/${param0}/search`, {
    method: 'GET',
    params: {
      ...queryParams,
    },
    ...(options || {}),
  })
}

/** 获取版本统计 获取指定应用的版本统计信息 GET /versions/${param0}/statistics */
export async function getVersionStatistics(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getVersionStatisticsParams,
  options?: { [key: string]: any }
) {
  const { appId: param0, ...queryParams } = params
  return request<API.BaseResponseVersionStatistics>(`/versions/${param0}/statistics`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}

/** 创建新版本 为指定应用创建新的代码版本 POST /versions/create */
export async function createVersion(
  body: API.CreateVersionRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseVersionCode>('/versions/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取版本详情 根据版本ID获取具体的版本信息 GET /versions/detail/${param0} */
export async function getVersionDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getVersionDetailParams,
  options?: { [key: string]: any }
) {
  const { versionId: param0, ...queryParams } = params
  return request<API.BaseResponseVersionCode>(`/versions/detail/${param0}`, {
    method: 'GET',
    params: { ...queryParams },
    ...(options || {}),
  })
}
