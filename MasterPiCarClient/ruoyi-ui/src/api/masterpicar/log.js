import request from '@/utils/request'

// 查询执行日志列表
export function listLog(query) {
  return request({
    url: '/masterpicar/log/list',
    method: 'get',
    params: query
  })
}

// 查询执行日志详细
export function getLog(logId) {
  return request({
    url: '/masterpicar/log/' + logId,
    method: 'get'
  })
}

// 新增执行日志
export function addLog(data) {
  return request({
    url: '/masterpicar/log',
    method: 'post',
    data: data
  })
}

// 修改执行日志
export function updateLog(data) {
  return request({
    url: '/masterpicar/log',
    method: 'put',
    data: data
  })
}

// 删除执行日志
export function delLog(logId) {
  return request({
    url: '/masterpicar/log/' + logId,
    method: 'delete'
  })
}
