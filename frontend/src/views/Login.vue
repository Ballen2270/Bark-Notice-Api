<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>{{ isInitialized ? 'Bark通知系统登录' : 'Bark通知系统初始化' }}</h2>
        </div>
      </template>
      
      <!-- 初始化注册表单 -->
      <el-form v-if="!isInitialized" :model="registerForm" :rules="registerRules" ref="registerFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请设置管理员用户名"></el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请设置管理员密码" show-password></el-input>
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码" show-password></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width: 100%">初始化系统</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 登录表单 -->
      <el-form v-else :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        
        <el-form-item label="验证码" prop="captcha">
          <div class="captcha-container">
            <el-input v-model="loginForm.captcha" placeholder="请输入验证码"></el-input>
            <img 
              v-if="captchaImg" 
              :src="captchaImg" 
              class="captcha-img" 
              @click="refreshCaptcha" 
              alt="验证码"
              title="点击刷新验证码"
            />
            <div v-else class="captcha-loading" @click="refreshCaptcha">
              加载验证码...
            </div>
          </div>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { checkInit, initRegister } from '@/api/user'

const router = useRouter()
const loginFormRef = ref(null)
const registerFormRef = ref(null)
const loading = ref(false)
const captchaImg = ref('')
const captchaKey = ref('')
const isInitialized = ref(true)

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
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.login-card {
  width: 400px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  font-size: 24px;
  color: #409EFF;
}

.captcha-container {
  display: flex;
  align-items: center;
}

.captcha-img {
  margin-left: 10px;
  height: 36px;
  border-radius: 4px;
  cursor: pointer;
}

.captcha-loading {
  margin-left: 10px;
  height: 36px;
  line-height: 36px;
  padding: 0 10px;
  background: #f0f0f0;
  border-radius: 4px;
  cursor: pointer;
  color: #909399;
  font-size: 12px;
}

/* 移动端适配 */
@media (max-width: 767px) {
  .login-card {
    width: 90%;
    max-width: 400px;
  }
  
  .el-form-item__label {
    float: none;
    display: block;
    text-align: left;
    padding: 0 0 8px;
  }
  
  .el-form-item__content {
    margin-left: 0 !important;
  }
}
</style> 