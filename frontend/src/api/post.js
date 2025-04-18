import request from '@/utils/request'

export function getPosts(params) {
  return request({
    url: '/posts',
    method: 'get',
    params
  })
}

export function getPost(id) {
  return request({
    url: `/posts/${id}`,
    method: 'get'
  })
}

export function createPost(data) {
  return request({
    url: '/posts',
    method: 'post',
    data
  })
}

export function updatePost(id, data) {
  return request({
    url: `/posts/${id}`,
    method: 'put',
    data
  })
}

export function deletePost(id) {
  return request({
    url: `/posts/${id}`,
    method: 'delete'
  })
}

export function publishPost(id) {
  return request({
    url: `/posts/${id}/publish`,
    method: 'post'
  })
}

export function uploadCover(data) {
  return request({
    url: '/posts/cover',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
} 