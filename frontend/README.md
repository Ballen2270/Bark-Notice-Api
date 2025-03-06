# Bark通知管理系统前端

这是Bark通知API的后台管理系统前端，基于Vue 3、Element Plus和Vite构建。

## 功能特性

- 设备管理：添加、编辑、激活和停用设备
- 通知管理：发送通知到所有设备或特定设备
- 设备注册：注册新的Bark设备
- 系统状态监控：查看API服务状态和设备统计

## 技术栈

- Vue 3 - 渐进式JavaScript框架
- Vue Router - 官方路由管理器
- Element Plus - 基于Vue 3的组件库
- Axios - 基于Promise的HTTP客户端
- Vite - 现代前端构建工具

## 开发环境设置

### 前提条件

- Node.js (v14+)
- npm 或 yarn

### 安装依赖

```bash
npm install
# 或
yarn install
```

### 启动开发服务器

```bash
npm run dev
# 或
yarn dev
```

开发服务器将在 http://localhost:3000 启动。

### 构建生产版本

```bash
npm run build
# 或
yarn build
```

构建后的文件将生成在 `dist` 目录中。

### 预览生产版本

```bash
npm run preview
# 或
yarn preview
```

## 项目结构

```
frontend/
├── public/             # 静态资源
├── src/                # 源代码
│   ├── api/            # API请求
│   ├── assets/         # 资源文件
│   ├── components/     # 公共组件
│   ├── router/         # 路由配置
│   ├── utils/          # 工具函数
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   └── main.js         # 入口文件
├── index.html          # HTML模板
├── vite.config.js      # Vite配置
└── package.json        # 项目依赖
```

## 配置

默认情况下，前端会将API请求代理到 `http://localhost:8080`。如需修改后端API地址，请编辑 `vite.config.js` 文件中的 `server.proxy` 配置。

## 许可证

[MIT](LICENSE) 