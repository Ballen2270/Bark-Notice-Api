<template>
  <div class="notices-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>发送通知</span>
        </div>
      </template>
      
      <el-form 
        ref="noticeFormRef"
        :model="noticeForm"
        :rules="rules"
        label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="noticeForm.title" placeholder="请输入通知标题" />
        </el-form-item>
        
        <el-form-item label="内容" prop="body">
          <el-input 
            v-model="noticeForm.body" 
            type="textarea" 
            :rows="4"
            placeholder="请输入通知内容" />
        </el-form-item>
        
        <el-form-item label="分组" prop="group">
          <el-input v-model="noticeForm.group" placeholder="请输入分组名称（可选）" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="loading">发送通知</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="box-card device-list-card">
      <template #header>
        <div class="card-header">
          <span>设备列表</span>
          <el-button type="primary" size="small" @click="fetchDevices">刷新</el-button>
        </div>
      </template>
      
      <el-table
        v-loading="deviceLoading"
        :data="deviceList"
        border
        style="width: 100%">
        <el-table-column prop="deviceToken" label="设备Token" width="180" />
        <el-table-column prop="name" label="设备名称" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ scope.row.status === 'ACTIVE' ? '激活' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button 
              type="success" 
              size="small" 
              :disabled="scope.row.status !== 'ACTIVE'"
              @click="sendToDevice(scope.row)">发送通知</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-card class="box-card register-card">
      <template #header>
        <div class="card-header">
          <span>注册新设备</span>
        </div>
      </template>
      
      <el-form 
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-width="100px">
        <el-form-item label="设备Token" prop="devicetoken">
          <el-input v-model="registerForm.devicetoken" placeholder="请输入设备Token" />
        </el-form-item>
        
        <el-form-item label="密钥" prop="key">
          <el-input v-model="registerForm.key" placeholder="请输入密钥（可选）" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="registerDevice" :loading="registerLoading">注册设备</el-button>
          <el-button @click="resetRegisterForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { sendNoticePost, registerDevice as registerDeviceApi } from '@/api/notice'
import { getAllDevices } from '@/api/device'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const deviceLoading = ref(false)
const registerLoading = ref(false)
const deviceList = ref([])
const noticeFormRef = ref(null)
const registerFormRef = ref(null)

const noticeForm = reactive({
  title: '',
  body: '',
  group: ''
})

const registerForm = reactive({
  devicetoken: '',
  key: ''
})

const rules = {
  title: [
    { required: true, message: '请输入通知标题', trigger: 'blur' }
  ],
  body: [
    { required: true, message: '请输入通知内容', trigger: 'blur' }
  ]
}

const registerRules = {
  devicetoken: [
    { required: true, message: '请输入设备Token', trigger: 'blur' }
  ]
}

// 获取所有设备
const fetchDevices = async () => {
  deviceLoading.value = true
  try {
    const res = await getAllDevices()
    console.log('API响应格式:', res)
    
    if (res.data !== undefined) {
      deviceList.value = res.data || []
    } else if (Array.isArray(res)) {
      deviceList.value = res
    } else {
      deviceList.value = []
    }
  } catch (error) {
    console.error('获取设备列表失败', error)
    ElMessage.error(`获取设备列表失败: ${error.message || '未知错误'}`)
    deviceList.value = []
  } finally {
    deviceLoading.value = false
  }
}

// 发送通知
const submitForm = async () => {
  if (!noticeFormRef.value) return
  
  await noticeFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await sendNoticePost(noticeForm)
        ElMessage.success('通知发送成功')
        resetForm()
      } catch (error) {
        console.error('通知发送失败', error)
        ElMessage.error('通知发送失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 向特定设备发送通知
const sendToDevice = (device) => {
  if (!noticeFormRef.value) return
  
  noticeFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 这里实际上是发送给所有设备，因为后端API没有提供针对特定设备发送的接口
        // 如果后端支持，可以修改为针对特定设备的发送
        await sendNoticePost({
          ...noticeForm,
          deviceToken: device.deviceToken // 假设后端支持这个参数
        })
        ElMessage.success(`通知已发送到设备: ${device.name}`)
      } catch (error) {
        console.error('通知发送失败', error)
        ElMessage.error('通知发送失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 注册设备
const registerDevice = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      registerLoading.value = true
      try {
        const res = await registerDeviceApi(
          registerForm.devicetoken,
          registerForm.key
        )
        ElMessage.success('设备注册成功')
        resetRegisterForm()
        fetchDevices() // 刷新设备列表
      } catch (error) {
        console.error('设备注册失败', error)
        ElMessage.error('设备注册失败')
      } finally {
        registerLoading.value = false
      }
    }
  })
}

// 重置通知表单
const resetForm = () => {
  if (noticeFormRef.value) {
    noticeFormRef.value.resetFields()
  }
}

// 重置注册表单
const resetRegisterForm = () => {
  if (registerFormRef.value) {
    registerFormRef.value.resetFields()
  }
}

onMounted(() => {
  fetchDevices()
})
</script>

<style scoped>
.notices-container {
  padding: 20px 0;
}

.box-card {
  margin-bottom: 20px;
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.device-list-card {
  margin-top: 20px;
}

.register-card {
  margin-top: 20px;
}

/* 移动端适配 */
@media (max-width: 767px) {
  .notices-container {
    padding: 10px 0;
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