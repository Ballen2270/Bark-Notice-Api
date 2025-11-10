<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="bg-circle circle-1"></div>
      <div class="bg-circle circle-2"></div>
      <div class="bg-circle circle-3"></div>
      <div class="bg-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card-wrapper">
      <el-card class="login-card" shadow="hover">
        <!-- Logo和标题 -->
        <div class="login-header">
          <div class="logo-wrapper">
            <div class="logo-icon">
              <el-icon size="48"><Notification /></el-icon>
            </div>
            <h1 class="app-title">Bark通知系统</h1>
            <p class="app-subtitle">智能通知推送管理平台</p>
          </div>
        </div>

        <!-- 表单切换标签 -->
        <div class="form-tabs">
          <div
            class="tab-item"
            :class="{ 'active': isInitialized }"
            @click="isInitialized = true"
          >
            <el-icon><User /></el-icon>
            <span>用户登录</span>
          </div>
          <div
            v-if="!isInitialized"
            class="tab-item active"
          >
            <el-icon><Setting /></el-icon>
            <span>系统初始化</span>
          </div>
        </div>

        <!-- 初始化注册表单 -->
        <div v-if="!isInitialized" class="form-container">
          <el-form
            :model="registerForm"
            :rules="registerRules"
            ref="registerFormRef"
            class="login-form"
            @submit.prevent="handleRegister"
          >
            <div class="form-item-wrapper">
              <el-form-item prop="username">
                <div class="input-wrapper">
                  <el-icon class="input-icon"><User /></el-icon>
                  <el-input
                    v-model="registerForm.username"
                    placeholder="请设置管理员用户名"
                    size="large"
                    clearable
                  />
                </div>
              </el-form-item>
            </div>

            <div class="form-item-wrapper">
              <el-form-item prop="password">
                <div class="input-wrapper">
                  <el-icon class="input-icon"><Lock /></el-icon>
                  <el-input
                    v-model="registerForm.password"
                    type="password"
                    placeholder="请设置管理员密码"
                    size="large"
                    show-password
                    clearable
                  />
                </div>
              </el-form-item>
            </div>

            <div class="form-item-wrapper">
              <el-form-item prop="confirmPassword">
                <div class="input-wrapper">
                  <el-icon class="input-icon"><Lock /></el-icon>
                  <el-input
                    v-model="registerForm.confirmPassword"
                    type="password"
                    placeholder="请确认密码"
                    size="large"
                    show-password
                    clearable
                  />
                </div>
              </el-form-item>
            </div>

            <el-button
              type="primary"
              @click="handleRegister"
              :loading="loading"
              class="submit-btn"
              size="large"
            >
              <el-icon><Tools /></el-icon>
              初始化系统
            </el-button>
          </el-form>
        </div>

        <!-- 登录表单 -->
        <div v-else class="form-container">
          <el-form
            :model="loginForm"
            :rules="rules"
            ref="loginFormRef"
            class="login-form"
            @submit.prevent="handleLogin"
          >
            <div class="form-item-wrapper">
              <el-form-item prop="username">
                <div class="input-wrapper">
                  <el-icon class="input-icon"><User /></el-icon>
                  <el-input
                    v-model="loginForm.username"
                    placeholder="请输入用户名"
                    size="large"
                    clearable
                    @keyup.enter="focusNext"
                  />
                </div>
              </el-form-item>
            </div>

            <div class="form-item-wrapper">
              <el-form-item prop="password">
                <div class="input-wrapper">
                  <el-icon class="input-icon"><Lock /></el-icon>
                  <el-input
                    v-model="loginForm.password"
                    type="password"
                    placeholder="请输入密码"
                    size="large"
                    show-password
                    clearable
                    @keyup.enter="focusCaptcha"
                  />
                </div>
              </el-form-item>
            </div>

            <div class="form-item-wrapper">
              <el-form-item prop="captcha">
                <div class="captcha-wrapper">
                  <div class="input-wrapper captcha-input">
                    <el-icon class="input-icon"><PictureRounded /></el-icon>
                    <el-input
                      v-model="loginForm.captcha"
                      placeholder="请输入验证码"
                      size="large"
                      clearable
                      @keyup.enter="handleLogin"
                    />
                  </div>
                  <div class="captcha-image-wrapper" @click="refreshCaptcha">
                    <img
                      v-if="captchaImg"
                      :src="captchaImg"
                      class="captcha-image"
                      alt="验证码"
                    />
                    <div v-else class="captcha-loading">
                      <el-icon class="loading-icon"><Loading /></el-icon>
                      <span>加载中...</span>
                    </div>
                    <div class="captcha-overlay">
                      <el-icon><Refresh /></el-icon>
                      <span>点击刷新</span>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </div>

            <el-button
              type="primary"
              @click="handleLogin"
              :loading="loading"
              class="submit-btn"
              size="large"
            >
              <el-icon><Right /></el-icon>
              立即登录
            </el-button>
          </el-form>
        </div>

        <!-- 底部信息 -->
        <div class="login-footer">
          <div class="version-info">
            <el-icon><InfoFilled /></el-icon>
            <span>{{ isInitialized ? '安全登录系统' : '首次使用需要初始化' }}</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { checkInit, initRegister } from '@/api/user'
import {
  User,
  Lock,
  PictureRounded,
  Notification,
  Setting,
  Tools,
  Right,
  InfoFilled,
  Loading,
  Refresh
} from '@element-plus/icons-vue'

const router = useRouter()
const loginFormRef = ref(null)
const registerFormRef = ref(null)
const loading = ref(false)
const captchaImg = ref('')
const captchaKey = ref('')
const isInitialized = ref(true)

// 焦点管理方法
const focusNext = () => {
  const passwordInput = document.querySelector('input[type="password"]')
  if (passwordInput) {
    passwordInput.focus()
  }
}

const focusCaptcha = () => {
  const captchaInput = document.querySelector('input[placeholder="请输入验证码"]')
  if (captchaInput) {
    captchaInput.focus()
  }
}

// 登录表单
const loginForm = reactive({
  username: '',
  password: '',
  captcha: ''
})

// 注册表单
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

// 注册表单验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应在3-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 获取验证码
const fetchCaptcha = async () => {
  try {
    const response = await axios.get('/api/auth/captcha/generate')
    if (response.data.code === '000000') {
      captchaImg.value = response.data.data.image
      captchaKey.value = response.data.data.key
    } else {
      ElMessage.error('获取验证码失败')
    }
  } catch (error) {
    console.error('获取验证码失败:', error)
    ElMessage.error('获取验证码失败，请刷新页面重试')
  }
}

// 刷新验证码
const refreshCaptcha = () => {
  captchaImg.value = ''
  fetchCaptcha()
}

// 检查系统是否已初始化
const checkInitialization = async () => {
  try {
    const response = await checkInit()
    console.log(response)
    isInitialized.value = response.data.isInit
    if (!isInitialized.value) {
      ElMessage.info('系统未初始化，请先设置管理员账号')
    }
  } catch (error) {
    console.error('检查系统初始化状态失败:', error)
    ElMessage.error('检查系统状态失败，请刷新页面重试')
  }
}

// 处理初始化注册
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await initRegister({
          username: registerForm.username,
          password: registerForm.password
        })
        
        if (response.code === '000000') {
          ElMessage.success('系统初始化成功，请登录')
          isInitialized.value = true
          // 清空表单
          registerForm.username = ''
          registerForm.password = ''
          registerForm.confirmPassword = ''
          // 初始化成功后获取验证码
          fetchCaptcha()
        } else {
          ElMessage.error(response.data.msg || '初始化失败')
        }
      } catch (error) {
        console.error('初始化失败:', error)
        ElMessage.error(error.response?.data?.msg || '初始化失败，请重试')
      } finally {
        loading.value = false
      }
    }
  })
}

// 登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await axios.post('/api/auth/login', {
          username: loginForm.username,
          password: loginForm.password,
          captcha: loginForm.captcha,
          captchaKey: captchaKey.value
        })
        
        if (response.data.code === '000000') {
          // 登录成功，保存令牌和用户信息
          localStorage.setItem('token', response.data.data.token)
          localStorage.setItem('userInfo', JSON.stringify(response.data.data.userInfo))
          
          // 设置axios请求头
          axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.data.token}`
          
          ElMessage.success('登录成功')
          
          // 跳转到首页
          router.push('/')
        } else {
          ElMessage.error(response.data.msg || '登录失败')
          // 刷新验证码
          refreshCaptcha()
        }
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error(error.response?.data?.msg || '登录失败，请重试')
        // 刷新验证码
        refreshCaptcha()
      } finally {
        loading.value = false
      }
    }
  })
}

// 页面加载时检查系统初始化状态并获取验证码
onMounted(async () => {
  await checkInitialization()
  if (isInitialized.value) {
    fetchCaptcha()
  }
})
</script>

<style scoped>
.login-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  overflow: hidden;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.circle-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 15%;
  animation-delay: 2s;
}

.circle-3 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

.bg-shapes {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.shape {
  position: absolute;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 20px;
  animation: rotate 20s linear infinite;
}

.shape-1 {
  width: 80px;
  height: 80px;
  top: 20%;
  right: 20%;
  animation-delay: 0s;
}

.shape-2 {
  width: 60px;
  height: 60px;
  bottom: 30%;
  left: 15%;
  animation-delay: 5s;
  animation-direction: reverse;
}

.shape-3 {
  width: 40px;
  height: 40px;
  top: 50%;
  left: 40%;
  animation-delay: 10s;
}

/* 登录卡片包装器 */
.login-card-wrapper {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 440px;
  padding: var(--spacing-lg);
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: var(--radius-large);
  box-shadow:
    0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.login-card:hover {
  transform: translateY(-2px);
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

/* 登录头部 */
.login-header {
  text-align: center;
  padding: var(--spacing-xl) var(--spacing-lg) var(--spacing-lg);
}

.logo-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: var(--spacing-md);
  box-shadow: 0 10px 25px rgba(0, 122, 255, 0.3);
  color: white;
  animation: pulse-icon 2s ease-in-out infinite;
}

.app-title {
  font-size: var(--font-size-xl);
  font-weight: 700;
  margin: 0 0 var(--spacing-xs) 0;
  background: linear-gradient(135deg, var(--text-primary) 0%, #4a5568 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.app-subtitle {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin: 0;
  font-weight: 400;
}

/* 表单标签 */
.form-tabs {
  display: flex;
  background: var(--bg-secondary);
  border-radius: var(--radius-medium);
  padding: 4px;
  margin: 0 var(--spacing-lg) var(--spacing-lg);
}

.tab-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-small);
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.tab-item.active {
  background: var(--bg-primary);
  color: var(--primary-color);
  box-shadow: 0 2px 8px rgba(0, 122, 255, 0.2);
}

.tab-item:hover:not(.active) {
  color: var(--text-primary);
}

/* 表单容器 */
.form-container {
  padding: 0 var(--spacing-lg) var(--spacing-lg);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.form-item-wrapper {
  position: relative;
  margin: 0 var(--spacing-lg);
}

.form-item-wrapper .el-form-item {
  margin-bottom: 0;
}

/* 输入框样式 */
.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;
  left: var(--spacing-md);
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-secondary);
  transition: color 0.2s;
  font-size: 18px;
  z-index: 10;
  pointer-events: none;
}

.input-wrapper :deep(.el-input) {
  width: 100%;
}

.input-wrapper :deep(.el-input__wrapper) {
  background: var(--bg-secondary);
  border: 2px solid transparent;
  border-radius: var(--radius-medium);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  padding-left: calc(18px + var(--spacing-md) * 2);
  box-shadow: none;
}

.input-wrapper:hover :deep(.el-input__wrapper) {
  border-color: var(--border-color);
}

.input-wrapper:focus-within :deep(.el-input__wrapper) {
  border-color: var(--primary-color);
  background: var(--bg-primary);
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.1);
}

.input-wrapper:focus-within .input-icon {
  color: var(--primary-color);
}

.input-wrapper :deep(.el-input__inner) {
  color: var(--text-primary);
  font-size: var(--font-size-md);
  height: 44px;
  line-height: 44px;
}


/* 验证码样式 */
.captcha-wrapper {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.captcha-input {
  order: 1;
}

.captcha-image-wrapper {
  position: relative;
  width: 100%;
  height: 44px;
  border-radius: var(--radius-medium);
  overflow: hidden;
  cursor: pointer;
  background: var(--bg-secondary);
  border: 2px solid transparent;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  order: 2;
}

.captcha-image-wrapper:hover {
  border-color: var(--primary-color);
  transform: scale(1.02);
}

.captcha-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.captcha-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
  gap: var(--spacing-xs);
}

.loading-icon {
  animation: spin 1s linear infinite;
}

.captcha-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 122, 255, 0.8);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: opacity 0.2s;
  font-size: var(--font-size-xs);
}

.captcha-image-wrapper:hover .captcha-overlay {
  opacity: 1;
}

/* 提交按钮 */
.submit-btn {
  width: calc(100% - var(--spacing-lg) * 2);
  margin: 0 var(--spacing-lg);
  height: 48px;
  border-radius: var(--radius-medium);
  font-size: var(--font-size-md);
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%);
  border: none;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 14px rgba(0, 122, 255, 0.3);
}

.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(0, 122, 255, 0.4);
}

.submit-btn:active {
  transform: translateY(0);
}

/* 底部信息 */
.login-footer {
  padding: var(--spacing-md) var(--spacing-lg) var(--spacing-lg);
}

.version-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
  background: var(--bg-secondary);
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-medium);
}

/* 动画效果 */
@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes pulse-icon {
  0%, 100% {
    transform: scale(1);
    box-shadow: 0 10px 25px rgba(0, 122, 255, 0.3);
  }
  50% {
    transform: scale(1.05);
    box-shadow: 0 15px 35px rgba(0, 122, 255, 0.4);
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 移动端适配 */
@media (max-width: 767px) {
  .login-container {
    padding: var(--spacing-md);
    min-height: 100vh;
    align-items: flex-start;
    padding-top: env(safe-area-inset-top, var(--spacing-md));
  }

  .login-card-wrapper {
    max-width: 100%;
    padding: var(--spacing-md);
    margin-top: var(--spacing-lg);
  }

  .login-header {
    padding: var(--spacing-lg) var(--spacing-md) var(--spacing-md);
  }

  .logo-icon {
    width: 60px;
    height: 60px;
  }

  .app-title {
    font-size: var(--font-size-lg);
  }

  .app-subtitle {
    font-size: var(--font-size-xs);
  }

  .form-tabs {
    margin: 0 var(--spacing-md) var(--spacing-md);
  }

  .form-container {
    padding: 0;
  }

  .form-item-wrapper {
    margin: 0 var(--spacing-md);
  }

  .tab-item {
    font-size: var(--font-size-xs);
    padding: var(--spacing-xs) var(--spacing-sm);
  }

  .input-wrapper :deep(.el-input__inner) {
    height: 44px;
    font-size: var(--font-size-sm);
  }

  /* 验证码样式在移动端保持一致，无需额外修改 */

  .captcha-image-wrapper:hover {
    transform: scale(1.05);
  }

  .submit-btn {
    width: calc(100% - var(--spacing-md) * 2);
    margin: var(--spacing-md) var(--spacing-md) 0;
    height: 48px;
    font-size: var(--font-size-sm);
  }

  .bg-circle {
    display: none;
  }

  .shape {
    display: none;
  }

  /* 优化移动端表单间距 */
  .login-form {
    gap: var(--spacing-lg);
  }

  .form-item-wrapper {
    margin-bottom: var(--spacing-xs);
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: var(--spacing-sm);
    padding-top: env(safe-area-inset-top, var(--spacing-sm));
  }

  .login-card-wrapper {
    padding: var(--spacing-sm);
    margin-top: var(--spacing-md);
  }

  .login-header {
    padding: var(--spacing-md) var(--spacing-sm) var(--spacing-sm);
  }

  .logo-icon {
    width: 50px;
    height: 50px;
  }

  .app-title {
    font-size: var(--font-size-md);
  }

  .app-subtitle {
    font-size: 10px;
  }

  .form-tabs {
    margin: 0 var(--spacing-sm) var(--spacing-sm);
  }

  .form-container {
    padding: 0;
  }

  .form-item-wrapper {
    margin: 0 var(--spacing-sm);
  }

  .login-footer {
    padding: var(--spacing-sm);
  }

  .input-wrapper :deep(.el-input__inner) {
    height: 44px;
    font-size: var(--font-size-sm);
  }

  .captcha-image-wrapper {
    height: 40px;
  }

  .submit-btn {
    width: calc(100% - var(--spacing-sm) * 2);
    margin: var(--spacing-md) var(--spacing-sm) 0;
    height: 48px;
    font-size: var(--font-size-sm);
  }

  .login-form {
    gap: var(--spacing-md);
  }
}

/* 高对比度模式 */
@media (prefers-contrast: high) {
  .login-container {
    background: linear-gradient(135deg, #000000 0%, #333333 100%);
  }

  .login-card {
    background: #ffffff;
    border: 2px solid #000000;
  }
}

/* 移动端键盘弹出适配 */
@media (max-height: 600px) and (orientation: landscape) {
  .login-container {
    align-items: center;
    padding: var(--spacing-sm);
  }

  .login-card-wrapper {
    margin-top: 0;
    max-width: 500px;
  }

  .login-header {
    padding: var(--spacing-md) var(--spacing-sm) var(--spacing-sm);
  }

  .logo-icon {
    width: 40px;
    height: 40px;
  }

  .app-title {
    font-size: var(--font-size-md);
  }

  .app-subtitle {
    display: none;
  }

  .form-tabs {
    margin: 0 var(--spacing-sm) var(--spacing-sm);
  }

  .form-container {
    padding: 0;
  }

  .form-item-wrapper {
    margin: 0 var(--spacing-sm);
  }

  .login-form {
    gap: var(--spacing-sm);
  }

  .input-wrapper :deep(.el-input__inner) {
    height: 40px;
  }

  .captcha-image-wrapper {
    height: 40px;
  }

  .submit-btn {
    width: calc(100% - var(--spacing-sm) * 2);
    margin: var(--spacing-sm) var(--spacing-sm) 0;
    height: 44px;
  }
}

/* 减少动画模式 */
@media (prefers-reduced-motion: reduce) {
  .bg-circle,
  .shape,
  .logo-icon,
  .submit-btn,
  .input-wrapper,
  .captcha-image-wrapper {
    animation: none !important;
    transition: none !important;
  }

  .login-card:hover,
  .submit-btn:hover,
  .input-wrapper:focus-within,
  .captcha-image-wrapper:hover {
    transform: none !important;
  }
}
</style> 