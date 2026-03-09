import request from '@/utils/request'

// 查询设备状态列表
export function listStatus(query) {
  return request({
    url: '/masterpicar/status/list',
    method: 'get',
    params: query
  })
}

// 查询设备状态详细
export function getStatus(statusId) {
  return request({
    url: '/masterpicar/status/' + statusId,
    method: 'get'
  })
}

// 新增设备状态
export function addStatus(data) {
  return request({
    url: '/masterpicar/status',
    method: 'post',
    data: data
  })
}

// 修改设备状态
export function updateStatus(data) {
  return request({
    url: '/masterpicar/status',
    method: 'put',
    data: data
  })
}

// 删除设备状态
export function delStatus(statusId) {
  return request({
    url: '/masterpicar/status/' + statusId,
    method: 'delete'
  })
}
