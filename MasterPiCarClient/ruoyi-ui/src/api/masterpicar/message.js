import request from '@/utils/request'

// 查询对话消息列表
export function listMessage(query) {
  return request({
    url: '/masterpicar/message/list',
    method: 'get',
    params: query
  })
}

// 查询对话消息详细
export function getMessage(messageId) {
  return request({
    url: '/masterpicar/message/' + messageId,
    method: 'get'
  })
}

// 新增对话消息
export function addMessage(data) {
  return request({
    url: '/masterpicar/message',
    method: 'post',
    data: data
  })
}

// 修改对话消息
export function updateMessage(data) {
  return request({
    url: '/masterpicar/message',
    method: 'put',
    data: data
  })
}

// 删除对话消息
export function delMessage(messageId) {
  return request({
    url: '/masterpicar/message/' + messageId,
    method: 'delete'
  })
}
