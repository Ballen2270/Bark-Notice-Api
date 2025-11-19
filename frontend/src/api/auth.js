import request from './request'

export function login(data) {
    return request({
        url: '/auth/login',
        method: 'post',
        data
    })
}


export function getUserInfo() {
    return request({
        url: '/auth/info',
        method: 'get'
    })
}

export function getCaptcha() {
    return request({
        url: '/auth/captcha/generate',
        method: 'get'
    })
}

export function changePassword(data) {
    return request({
        url: '/auth/changePassword',
        method: 'post',
        data
    })
}
