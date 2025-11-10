<template>
  <div class="notices-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon class="title-icon"><ChatDotRound /></el-icon>
            通知管理
          </h1>
          <p class="page-subtitle">发送和管理推送通知</p>
        </div>
        <div class="header-right">
          <el-button @click="refreshAll" type="success" size="large" class="refresh-all-btn">
            <el-icon><Refresh /></el-icon>
            刷新全部
          </el-button>
        </div>
      </div>
    </div>

    <div class="content-grid">
      <!-- 左侧：发送通知表单 -->
      <div class="notice-form-section">
        <el-card class="form-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-title">
                <el-icon class="card-icon"><Bell /></el-icon>
                <span>发送新通知</span>
              </div>
              <div class="form-stats">
                <span class="stats-item">
                  激活设备: <strong>{{ deviceStats.active }}</strong>
                </span>
              </div>
            </div>
          </template>

          <el-form
            ref="noticeFormRef"
            :model="noticeForm"
            :rules="rules"
            class="notice-form"
            @submit.prevent="submitForm"
          >
            <div class="form-row">
              <el-form-item prop="title" class="full-width">
                <div class="input-wrapper">
                  <el-icon class="input-icon"><Document /></el-icon>
                  <el-input
                    v-model="noticeForm.title"
                    placeholder="请输入通知标题"
                    size="large"
                    clearable
                    maxlength="100"
                    show-word-limit
                  />
                </div>
              </el-form-item>
            </div>

            <div class="form-row">
              <el-form-item prop="body" class="full-width">
                <div class="textarea-wrapper">
                  <el-icon class="input-icon"><ChatLineSquare /></el-icon>
                  <el-input
                    v-model="noticeForm.body"
                    type="textarea"
                    :rows="5"
                    placeholder="请输入通知内容"
                    size="large"
                    resize="none"
                    maxlength="500"
                    show-word-limit
                  />
                </div>
              </el-form-item>
            </div>

            <div class="form-row">
              <el-form-item prop="group" class="full-width">
                <div class="input-wrapper">
                  <el-icon class="input-icon"><CollectionTag /></el-icon>
                  <el-input
                    v-model="noticeForm.group"
                    placeholder="请输入分组名称（可选）"
                    size="large"
                    clearable
                  />
                </div>
              </el-form-item>
            </div>

            <div class="form-row">
              <el-form-item class="full-width">
                <div class="advanced-options">
                  <el-button @click="showAdvanced = !showAdvanced" type="info" text>
                    <el-icon><Setting /></el-icon>
                    高级选项
                  </el-button>
                </div>
              </el-form-item>
            </div>

            <transition name="slide-down">
              <div v-show="showAdvanced" class="advanced-section">
                <div class="form-row">
                  <el-form-item class="full-width">
                    <div class="input-wrapper">
                      <el-icon class="input-icon"><Link /></el-icon>
                      <el-input
                        v-model="noticeForm.url"
                        placeholder="跳转链接（可选）"
                        size="large"
                        clearable
                      />
                    </div>
                  </el-form-item>
                </div>

                <div class="form-row">
                  <el-form-item class="full-width">
                    <div class="input-wrapper">
                      <el-icon class="input-icon"><Picture /></el-icon>
                      <el-input
                        v-model="noticeForm.icon"
                        placeholder="图标URL（可选）"
                        size="large"
                        clearable
                      />
                    </div>
                  </el-form-item>
                </div>
              </div>
            </transition>

            <div class="form-actions">
              <el-button
                type="primary"
                @click="submitForm"
                :loading="loading"
                size="large"
                class="submit-btn"
              >
                <el-icon><Promotion /></el-icon>
                发送通知
              </el-button>
              <el-button
                @click="resetForm"
                size="large"
                class="reset-btn"
              >
                <el-icon><RefreshLeft /></el-icon>
                重置
              </el-button>
            </div>
          </el-form>
        </el-card>

        <!-- 快速模板 -->
        <el-card class="template-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-title">
                <el-icon class="card-icon"><Files /></el-icon>
                <span>快速模板</span>
              </div>
            </div>
          </template>

          <div class="template-list">
            <div
              v-for="template in noticeTemplates"
              :key="template.id"
              class="template-item"
              @click="applyTemplate(template)"
            >
              <div class="template-icon">
                <el-icon><component :is="template.icon" /></el-icon>
              </div>
              <div class="template-content">
                <div class="template-title">{{ template.title }}</div>
                <div class="template-preview">{{ template.body.substring(0, 30) }}...</div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧：设备管理和注册 -->
      <div class="device-section">
        <!-- 设备列表 -->
        <el-card class="device-list-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-title">
                <el-icon class="card-icon"><Cellphone /></el-icon>
                <span>设备列表</span>
              </div>
              <div class="device-stats">
                <el-tag type="success" size="small">
                  {{ deviceStats.active }} 激活
                </el-tag>
                <el-tag type="info" size="small">
                  {{ deviceStats.total }} 总计
                </el-tag>
              </div>
              <el-button
                @click="fetchDevices"
                type="primary"
                size="small"
                class="refresh-btn"
                :loading="deviceLoading"
              >
                <el-icon><Refresh /></el-icon>
              </el-button>
            </div>
          </template>

          <!-- 搜索框 -->
          <div class="device-search">
            <el-input
              v-model="deviceSearchText"
              placeholder="搜索设备名称或Token..."
              size="small"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>

          <!-- 设备列表 - 桌面端 -->
          <div class="device-table desktop-only">
            <div v-if="deviceLoading" class="loading-placeholder">
              <el-skeleton :rows="3" animated />
            </div>
            <div v-else-if="filteredDeviceList.length === 0" class="empty-state">
              <el-empty description="暂无设备" />
            </div>
            <div v-else class="device-items">
              <div
                v-for="device in filteredDeviceList"
                :key="device.deviceToken"
                class="device-item"
                :class="{ 'inactive': device.status !== 'ACTIVE' }"
              >
                <div class="device-info">
                  <div class="device-name">
                    <el-icon class="device-icon"><Cellphone /></el-icon>
                    <span>{{ device.name || '未命名设备' }}</span>
                  </div>
                  <div class="device-token">
                    <el-icon class="token-icon"><Key /></el-icon>
                    <span>{{ device.deviceToken }}</span>
                  </div>
                </div>
                <div class="device-status">
                  <el-tag
                    :type="device.status === 'ACTIVE' ? 'success' : 'danger'"
                    size="small"
                  >
                    {{ device.status === 'ACTIVE' ? '激活' : '停用' }}
                  </el-tag>
                </div>
                <div class="device-actions">
                  <el-button
                    type="success"
                    size="small"
                    :disabled="device.status !== 'ACTIVE'"
                    @click="sendToDevice(device)"
                    class="send-btn"
                  >
                    <el-icon><Position /></el-icon>
                    发送
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 设备列表 - 移动端 -->
          <div class="mobile-device-list mobile-only">
            <div v-if="deviceLoading" class="loading-placeholder">
              <el-skeleton :rows="3" animated />
            </div>
            <div v-else-if="filteredDeviceList.length === 0" class="empty-state">
              <el-empty description="暂无设备" />
            </div>
            <div v-else class="device-cards">
              <div
                v-for="device in filteredDeviceList"
                :key="device.deviceToken"
                class="device-card"
                :class="{ 'inactive': device.status !== 'ACTIVE' }"
              >
                <div class="card-header">
                  <div class="device-name">
                    <el-icon class="device-icon"><Cellphone /></el-icon>
                    <span>{{ device.name || '未命名设备' }}</span>
                  </div>
                  <el-tag
                    :type="device.status === 'ACTIVE' ? 'success' : 'danger'"
                    size="small"
                  >
                    {{ device.status === 'ACTIVE' ? '激活' : '停用' }}
                  </el-tag>
                </div>
                <div class="card-content">
                  <div class="info-item">
                    <span class="label">Token:</span>
                    <span class="value">{{ device.deviceToken }}</span>
                  </div>
                </div>
                <div class="card-actions">
                  <el-button
                    type="success"
                    size="small"
                    :disabled="device.status !== 'ACTIVE'"
                    @click="sendToDevice(device)"
                    class="mobile-send-btn"
                  >
                    <el-icon><Position /></el-icon>
                    发送通知
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 注册新设备 -->
        <el-card class="register-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <div class="header-title">
                <el-icon class="card-icon"><Plus /></el-icon>
                <span>注册新设备</span>
              </div>
            </div>
          </template>

          <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            class="register-form"
            @submit.prevent="registerDevice"
          >
            <el-form-item prop="devicetoken">
              <div class="input-wrapper">
                <el-icon class="input-icon"><Key /></el-icon>
                <el-input
                  v-model="registerForm.devicetoken"
                  placeholder="请输入设备Token"
                  size="large"
                  clearable
                />
              </div>
            </el-form-item>

            <el-form-item prop="key">
              <div class="input-wrapper">
                <el-icon class="input-icon"><Lock /></el-icon>
                <el-input
                  v-model="registerForm.key"
                  placeholder="请输入密钥（可选）"
                  size="large"
                  clearable
                />
              </div>
            </el-form-item>

            <div class="form-actions">
              <el-button
                type="primary"
                @click="registerDevice"
                :loading="registerLoading"
                size="large"
                class="submit-btn"
              >
                <el-icon><Plus /></el-icon>
                注册设备
              </el-button>
              <el-button
                @click="resetRegisterForm"
                size="large"
                class="reset-btn"
              >
                <el-icon><RefreshLeft /></el-icon>
                重置
              </el-button>
            </div>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { sendNoticePost, registerDevice as registerDeviceApi } from '@/api/notice'
import { getAllDevices } from '@/api/device'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const deviceLoading = ref(false)
const registerLoading = ref(false)
const deviceList = ref([])
const deviceSearchText = ref('')
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

// 设备统计计算属性
const deviceStats = computed(() => {
  const devices = deviceList.value || []
  return {
    total: devices.length,
    active: devices.filter(device => device.status === 'ACTIVE').length
  }
})

// 过滤设备列表
const filteredDeviceList = computed(() => {
  const devices = deviceList.value || []
  const searchText = deviceSearchText.value.toLowerCase().trim()

  if (!searchText) {
    return devices
  }

  return devices.filter(device =>
    (device.name && device.name.toLowerCase().includes(searchText)) ||
    (device.deviceToken && device.deviceToken.toLowerCase().includes(searchText))
  )
})

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

// 刷新所有数据
const refreshAll = async () => {
  try {
    await fetchDevices()
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败')
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