import request from '@/utils/request'

// 查询对话会话列表
export function listSession(query) {
  return request({
    url: '/masterpicar/session/list',
    method: 'get',
    params: query
  })
}

// 查询对话会话详细
export function getSession(sessionId) {
  return request({
    url: '/masterpicar/session/' + sessionId,
    method: 'get'
  })
}

// 新增对话会话
export function addSession(data) {
  return request({
    url: '/masterpicar/session',
    method: 'post',
    data: data
  })
}

// 修改对话会话
export function updateSession(data) {
  return request({
    url: '/masterpicar/session',
    method: 'put',
    data: data
  })
}

// 删除对话会话
export function delSession(sessionId) {
  return request({
    url: '/masterpicar/session/' + sessionId,
    method: 'delete'
  })
}
