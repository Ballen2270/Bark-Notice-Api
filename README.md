# Bark-Notice-Api

先感谢作者无私开源bark以及server源码，能够让我们能够免费享受到Apple设备实时推送服务，nas用户狂喜，体验过钉钉webhook、server酱等webhook工具后，还是觉得bark更加牛逼，哈哈。

针对够推送多个设备以及隐私考虑，对Bark-Server用SpringBoot进行了封装一层（加了一坨上去），本人代码水平有限，仅实现个人所需

其他服务只需要明码请求，经过Bark-Notice-Api加密以后再推送给Bark-Server，并对日志做记录

# Bark-server

github传送门

```
https://github.com/Finb/bark-server
```

使用文档地址

```
https://bark.day.app/#/?id=bark
```

# API

原生api主要是分为三部分，注册、加密以及消息推送

注册和加密原生api是共用一个register接口，因此只对请求做转发，并且记录加密部分的key以及iv

# 注册

请求方法：GET

请求地址：/register

请求格式：

```
?deviceToken={0}&key={1}
```



# 消息推送

请求方法：GET

请求地址：/notice

请求格式1：

```
notice?title={0}&body={1}&group={2}
```

请求格式2：

```
notice/{title}/{body}?group={0}
```



请求方法：POST

请求地址：/notice

请求格式：

```json
{
    "body": "body", #消息内容
    "group": "group", #消息分组
    "title": "title" #通知标题
}
```

返回格式：

```json
{
    "code": "000000",
    "msg": "发送成功",
    "data": {
        "sendNum": 1, #发送数量
        "successNum": 1 #发送成功数量
    }
}
```

