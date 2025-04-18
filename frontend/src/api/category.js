import request from '@/utils/request'

export function getCategories() {
  return request({
    url: '/api/categories',
    method: 'get'
  })
}

export function createCategory(data) {
  return request({
    url: '/api/categories',
    method: 'post',
    data
  })
}

export function updateCategory(id, data) {
  return request({
    url: `/api/categories/${id}`,
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/api/categories/${id}`,
    method: 'delete'
  })
} 