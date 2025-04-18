import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export function updateProfile(data) {
  return request({
    url: '/auth/update',
    method: 'post',
    data
  })
}

export function uploadAvatar(data) {
  return request({
    url: '/auth/upload-avatar',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
} 