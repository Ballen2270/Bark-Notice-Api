# Bark-Notice-Api

非常感谢Bark作者开源了Bark和Bark-Server，让我们能够免费享受到Apple设备的实时推送服务。相比于钉钉、Server酱等webhook工具，Bark的自由便捷让我爱不释手。

为了实现多设备推送和保障隐私，我基于SpringBoot对Bark-Server进行了一层封装，开发了Bark-Notice-Api。个人编码能力有限，仅满足个人需求。

Bark-Notice-Api的工作原理是：其他服务只需发送简单明文请求，API会对请求内容进行加密处理，然后将加密后的数据转发给Bark-Server。同时，API会对请求进行简单日志记录。

# Bark-server

Bark-Server传送门

https://github.com/Finb/bark-server

# 开始

## Mysql

Bark Notice API需要mysql支持，数据库执行notice-Api.sql文件，创建服务所需要的两张表，作日志记录

## Bark-Server

需要部署原生Bark-Server,详情请见

```
https://bark.day.app/#/deploy
```

## Bark App

App Store 中下载Bark App

## Bark Notice API

### docker

```
docker run -d --name bark-notice-api \
  --restart always \
  --network host \
  -e SPRING_PROFILES_ACTIVE="pro" \ #无需更改
  -e MYSQL_HOST_NAME="127.0.0.1" \ #Mysql服务ip
  -e MYSQL_PORT="3306" \ #Mysql服务端口
  -e MYSQL_USERNAME="root" \ #Mysql服务账号
  -e MYSQL_PASSWORD="root" \ #Mysql服务密码
  -e BARK_SERVER_URL="http://192.168.11.1:9988" \ #Bark-server 部署的URL
  -e TZ=Asia/Shanghai \ 
  ballen2270/bark-notice-api:latest
```

## 配置设备

1. 打开Bark App点击右上角加号

2. 填入你已经部署好的Bark-Notice-Api URL -> 右上角勾选

3. 成功会显示你的私人服务器URL以及Devicekey（会在生成设备配置中用到）

4. Bark App ->设置 复制DeviceToekn（切记不要泄露）

5. 生成设备配置 post请求

	```shell
	curl --location 'https://bark-notice-api.com/device/gen' \ #需要替换成私人bark-notice-api URL地址
	--header 'Content-Type: application/json' \
	--data '{
	  "deviceToken": "步骤4中复制的deviceToken",
	  "name": "设备名称",
	  "deviceKey": "URL末尾的devideKey",
	  "algorithm": "无需填写 默认为AES",
	  "model": "无需填写 默认为CBC 目前仅支持CBC",
	  "padding": "无需填写 默认为PKCS7Padding 目前仅支持",
	  "encodeKey": "可自定义 也可由Bark-Notice-Api自动生成",
	  "iv": "可自定义 也可由Bark-Notice-Api自动生成"
	}
	'
	```

	

6. Bark App首页下拉找到加密设置

	```
	算法：AES256
	模式：CBC
	Padding：pkcs7
	Key：从步骤五中返回中复制encodeKey到这里
	iv：从步骤五中返回中复制iv到这里
	```

7. 点击右上角完成 会发送两条测试请求到服务器，请保持服务器可用

8. 如需多设备推送，可在新设备上重复上述过程，即可实现多设备 消息推送

9. 测试消息发送

	```shell
	curl --location 'https://notice-api.com/notice' \ #需要替换成私人bark-notice-api URL地址
	--header 'Content-Type: application/json' \
	--data '{
	    "body": "msg body",
	    "group": "test",
	    "title": "test title"
	}'
	```

	

# 图解

## 生成加密设备配置

<img src="/image/genDeviceConf.png" alt="addPersonalService.png" style="zoom: 33%;" />

## 添加私有服务器

<img src="/image/addPersonalService.png" alt="添加私有服务器" style="zoom: 25%;" />

## 推送消息

<img src="/image/pushMessage.png" alt="推送消息" style="zoom: 67%;" />

# API

## 注册

请求方法：GET

请求地址：/register

请求格式：

```
?deviceToken={0}&key={1}
```



## 消息推送

请求方法：GET

请求地址：/notice

请求格式1：

```
?title={0}&body={1}&group={2}
```

请求格式2：

```
/{title}/{body}?group={0}
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

