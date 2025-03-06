import request from './request'

/**
 * 检查系统是否已初始化
 * @returns {Promise} 请求响应
 */
export function checkInit() {
  return request({
    url: '/auth/checkInit',
    method: 'get'
  })
}

/**
 * 系统初始化注册
 * @param {Object} data 注册参数
 * @returns {Promise} 请求响应
 */
export function initRegister(data) {
  return request({
    url: '/auth/initRegister',
    method: 'post',
    data
  })
}

/**
 * 用户登录
 * @param {Object} data 登录参数
 * @returns {Promise} 请求响应
 */
export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

/**
 * 获取验证码
 * @returns {Promise} 请求响应
 */
export function getCaptcha() {
  return request({
    url: '/api/auth/captcha/generate',
    method: 'get'
  })
}

/**
 * 获取用户信息
 * @returns {Promise} 请求响应
 */
export function getUserInfo() {
  return request({
    url: '/api/auth/info',
    method: 'get'
  })
}

/**
 * 修改密码
 * @param {Object} data 密码参数
 * @returns {Promise} 请求响应
 */
export function changePassword(data) {
  return request({
    url: '/api/auth/changePassword',
    method: 'post',
    data
  })
}

/**
 * 获取用户列表
 * @returns {Promise} 请求响应
 */
export function getUserList() {
  return request({
    url: '/user/list',
    method: 'get'
  })
}

/**
 * 激活用户
 * @param {Number} userId 用户ID
 * @returns {Promise} 请求响应
 */
export function activateUser(userId) {
  return request({
    url: '/user/activate',
    method: 'post',
    data: { userId }
  })
}

/**
 * 停用用户
 * @param {Number} userId 用户ID
 * @returns {Promise} 请求响应
 */
export function deactivateUser(userId) {
  return request({
    url: '/user/deactivate',
    method: 'post',
    data: { userId }
  })
}

/**
 * 获取用户详情
 * @param {Number} id 用户ID
 * @returns {Promise} 请求响应
 */
export function getUserDetail(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
} 