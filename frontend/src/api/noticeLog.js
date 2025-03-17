import request from './request'

/**
 * 查询日志列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function getLogList(params) {
  // 确保不传递deviceKey参数
  const { deviceKey, ...otherParams } = params || {}
  return request({
    url: '/api/notice/log/list',
    method: 'get',
    params: otherParams
  })
}

/**
 * 按日期统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function countByDate(params) {
  return request({
    url: '/api/notice/log/count/date',
    method: 'get',
    params
  })
}

/**
 * 按状态统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function countByStatus(params) {
  return request({
    url: '/api/notice/log/count/status',
    method: 'get',
    params
  })
}

/**
 * 按分组统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function countByGroup(params) {
  return request({
    url: '/api/notice/log/count/group',
    method: 'get',
    params
  })
}

/**
 * 按设备统计
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function countByDevice(params) {
  return request({
    url: '/api/notice/log/count/device',
    method: 'get',
    params
  })
} 