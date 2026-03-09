import request from '@/utils/request'

// 查询指令执行列表
export function listExec(query) {
  return request({
    url: '/masterpicar/exec/list',
    method: 'get',
    params: query
  })
}

// 查询指令执行详细
export function getExec(execId) {
  return request({
    url: '/masterpicar/exec/' + execId,
    method: 'get'
  })
}

// 新增指令执行
export function addExec(data) {
  return request({
    url: '/masterpicar/exec',
    method: 'post',
    data: data
  })
}

// 修改指令执行
export function updateExec(data) {
  return request({
    url: '/masterpicar/exec',
    method: 'put',
    data: data
  })
}

// 删除指令执行
export function delExec(execId) {
  return request({
    url: '/masterpicar/exec/' + execId,
    method: 'delete'
  })
}
