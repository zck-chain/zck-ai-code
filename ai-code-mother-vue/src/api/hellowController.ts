// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 此处后端没有提供注释 GET /hello/world */
export async function hello(options?: { [key: string]: any }) {
  return request<API.BaseResponse>('/hello/world', {
    method: 'GET',
    ...(options || {}),
  })
}
