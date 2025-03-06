<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>系统状态</span>
              <el-button type="primary" size="small" @click="checkSystemStatus">刷新</el-button>
            </div>
          </template>
          <div class="status-content">
            <div class="status-item">
              <span class="label">服务状态:</span>
              <span class="value" :class="{ 'success': systemStatus.online, 'error': !systemStatus.online }">
                {{ systemStatus.online ? '在线' : '离线' }}
              </span>
            </div>
            <div class="status-item">
              <span class="label">API版本:</span>
              <span class="value">{{ systemStatus.version || '未知' }}</span>
            </div>
            <div class="status-item">
              <span class="label">最后检查时间:</span>
              <span class="value">{{ systemStatus.lastCheck || '未检查' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>设备统计</span>
            </div>
          </template>
          <div class="status-content">
            <div class="status-item">
              <span class="label">总设备数:</span>
              <span class="value">{{ deviceStats.total }}</span>
            </div>
            <div class="status-item">
              <span class="label">活跃设备:</span>
              <span class="value success">{{ deviceStats.active }}</span>
            </div>
            <div class="status-item">
              <span class="label">停用设备:</span>
              <span class="value error">{{ deviceStats.inactive }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>快速操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <el-button type="primary" @click="$router.push('/devices')">管理设备</el-button>
            <el-button type="success" @click="$router.push('/notices')">发送通知</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { pingService } from '@/api/notice'
import { getAllDevices } from '@/api/device'
import { ElMessage } from 'element-plus'

const systemStatus = ref({
  online: false,
  version: '',
  lastCheck: ''
})

const deviceStats = ref({
  total: 0,
  active: 0,
  inactive: 0
})

// 检查系统状态
const checkSystemStatus = async () => {
  try {
    const res = await pingService()
    systemStatus.value.online = res.code === 200
    systemStatus.value.version = res.data?.version || '未知'
    systemStatus.value.lastCheck = new Date().toLocaleString()
    ElMessage.success('系统状态检查完成')
  } catch (error) {
    systemStatus.value.online = false
    systemStatus.value.lastCheck = new Date().toLocaleString()
    ElMessage.error('系统状态检查失败')
  }
}

// 获取设备统计
const getDeviceStatistics = async () => {
  try {
    const res = await getAllDevices()
    const devices = res.data || []
    deviceStats.value.total = devices.length
    deviceStats.value.active = devices.filter(d => d.status === 'ACTIVE').length
    deviceStats.value.inactive = devices.filter(d => d.status === 'STOP').length
  } catch (error) {
    console.error('获取设备统计失败', error)
  }
}

onMounted(() => {
  checkSystemStatus()
  getDeviceStatistics()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px 0;
}

.box-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-content {
  padding: 10px 0;
}

.status-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.label {
  font-weight: bold;
  margin-right: 10px;
  width: 120px;
}

.value {
  font-size: 14px;
}

.success {
  color: #67C23A;
}

.error {
  color: #F56C6C;
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* 移动端适配 */
@media (max-width: 767px) {
  .el-table-column {
    min-width: 120px;
  }
  
  .el-dialog {
    width: 90% !important;
    max-width: 500px;
  }
  
  .el-form-item__label {
    padding: 0 0 8px;
    line-height: 1.2;
  }
}
</style> 