import request from '@/utils/request'
// 获取仓库数量
export function getCount() {
  return request({
      url: '/storage/getCount',
      method: 'get'
  })
}

// 查询所有仓库信息
export function queryStorage() {
  return request({
      url: '/storage/queryStorage',
      method: 'get'
  })
}

// 分页询查仓库信息
export function queryStoragesByPage(params) {
  return request({
      url: '/storage/queryStoragesByPage',
      method: 'get',
      params
  })
}

// 添加仓库信息
export function addStorage(data) {
  return request({
      url: '/storage/addStorage',
      method: 'post',
      data
  })
}
//  更新图书信息
export function updateStorage(data) {
  return request({
      url: '/storage/updateStorage',
      method: 'put',
      data
  })
}
