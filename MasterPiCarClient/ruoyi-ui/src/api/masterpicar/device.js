import request from '@/utils/request'

// 查询机器人设备列表
export function listDevice(query) {
  return request({
    url: '/masterpicar/device/list',
    method: 'get',
    params: query
  })
}

// 查询机器人设备详细
export function getDevice(deviceId) {
  return request({
    url: '/masterpicar/device/' + deviceId,
    method: 'get'
  })
}

// 新增机器人设备
export function addDevice(data) {
  return request({
    url: '/masterpicar/device',
    method: 'post',
    data: data
  })
}

// 修改机器人设备
export function updateDevice(data) {
  return request({
    url: '/masterpicar/device',
    method: 'put',
    data: data
  })
}

// 删除机器人设备
export function delDevice(deviceId) {
  return request({
    url: '/masterpicar/device/' + deviceId,
    method: 'delete'
  })
}
