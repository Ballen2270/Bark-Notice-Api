import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'

// 优化 ElMessage 样式
const elMessageStyles = `
.el-message {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
  width: auto;
  min-width: auto;
  max-width: 80vw;
}

.el-message.el-message--success {
  background: linear-gradient(135deg, rgba(103, 194, 58, 0.95) 0%, rgba(76, 175, 80, 0.95) 100%);
  border: 1px solid rgba(103, 194, 58, 0.8);
}

.el-message.el-message--error {
  background: linear-gradient(135deg, rgba(245, 108, 108, 0.95) 0%, rgba(239, 83, 80, 0.95) 100%);
  border: 1px solid rgba(245, 108, 108, 0.8);
}

.el-message.el-message--warning {
  background: linear-gradient(135deg, rgba(230, 162, 60, 0.95) 0%, rgba(251, 192, 45, 0.95) 100%);
  border: 1px solid rgba(230, 162, 60, 0.8);
}

.el-message.el-message--info {
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.95) 0%, rgba(33, 150, 243, 0.95) 100%);
  border: 1px solid rgba(64, 158, 255, 0.8);
}

.el-message__content {
  color: #ffffff !important;
  font-weight: 500;
  font-size: 14px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.el-message__icon {
  color: #ffffff !important;
  font-size: 16px;
}

.el-message__closeBtn {
  color: rgba(255, 255, 255, 0.8) !important;
}

.el-message__closeBtn:hover {
  color: #ffffff !important;
}
`

// 创建并添加样式
const style = document.createElement('style')
style.textContent = elMessageStyles
document.head.appendChild(style)

const app = createApp(App)

app.use(ElementPlus)
app.use(router)

app.mount('#app') 