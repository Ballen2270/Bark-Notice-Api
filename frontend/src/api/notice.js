import request from './request'

export function pingService() {
    return request({
        url: '/ping',
        method: 'get',
        transformResponse: [(data) => {
            try {
                const response = JSON.parse(data)
                if (response.code === 200 && response.message === 'pong') {
                    return {
                        success: true,
                        data: response,
                        message: response.message,
                        code: 200
                    }
                }
                return JSON.parse(data)
            } catch (e) {
                return data
            }
        }]
    })
}

export function sendNoticePost(data) {
    return request({
        url: '/notice',
        method: 'post',
        data
    })
}

export function registerDevice(devicetoken, key) {
    return request({
        url: '/register',
        method: 'get',
        params: { devicetoken, key }
    })
}
