<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 通知日志统计卡片 -->
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <h3>今日通知统计</h3>
      </el-col>
      
      <el-col :xs="24" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="notice-stat-card">
          <div class="notice-stat-item">
            <div class="notice-stat-value">{{ totalCount }}</div>
            <div class="notice-stat-label">总通知数</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="notice-stat-card">
          <div class="notice-stat-item">
            <div class="notice-stat-value success">{{ successCount }}</div>
            <div class="notice-stat-label">成功数</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="notice-stat-card">
          <div class="notice-stat-item">
            <div class="notice-stat-value error">{{ failedCount }}</div>
            <div class="notice-stat-label">失败数</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="6" :md="6" :lg="6" :xl="6">
        <el-card class="notice-stat-card">
          <div class="notice-stat-item">
            <div class="notice-stat-value primary">{{ successRate }}%</div>
            <div class="notice-stat-label">成功率</div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" style="margin-top: 20px;">
        <el-divider></el-divider>
        <h3>系统信息</h3>
      </el-col>
      
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
            <el-button type="primary" @click="$router.push('/devices')" :style="actionBtnStyle">管理设备</el-button>
            <el-button type="success" @click="$router.push('/notices')" :style="actionBtnStyle">发送通知</el-button>
            <el-button type="info" @click="$router.push('/log-dashboard')" :style="actionBtnStyle">日志大屏</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { pingService } from '@/api/notice'
import { getAllDevices } from '@/api/device'
import { countByDate } from '@/api/noticeLog'
import { ElMessage } from 'element-plus'

// 新增：按钮内联样式
const actionBtnStyle = {
  width: '100%',
  marginBottom: '10px',
  display: 'block'
}

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

const dateData = ref([])

// 计算属性
const totalCount = computed(() => {
  if (!dateData.value || dateData.value.length === 0) return 0
  return dateData.value.reduce((sum, item) => sum + item.count, 0)
})

const successCount = computed(() => {
  if (!dateData.value || dateData.value.length === 0) return 0
  return dateData.value.reduce((sum, item) => sum + item.successCount, 0)
})

const failedCount = computed(() => {
  if (!dateData.value || dateData.value.length === 0) return 0
  return dateData.value.reduce((sum, item) => sum + item.failedCount, 0)
})

const successRate = computed(() => {
  if (totalCount.value === 0) return '0.00'
  return ((successCount.value / totalCount.value) * 100).toFixed(2)
})

// 检查系统状态
const checkSystemStatus = async () => {
  try {
    const res = await pingService()
    systemStatus.value.online = res.data?.code === 200
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

// 获取当天通知日志统计
const getNoticeLogStatistics = async () => {
  try {
    // 获取当天日期
    const today = new Date().toISOString().slice(0, 10)
    
    const res = await countByDate({
      dateType: 'day',
      beginTime: today,
      endTime: today
    })
    
    if (res.code === '000000') {
      dateData.value = res.data
    }
  } catch (error) {
    console.error('获取通知日志统计失败', error)
  }
}

onMounted(() => {
  checkSystemStatus()
  getDeviceStatistics()
  getNoticeLogStatistics()
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

.primary {
  color: #409EFF;
}

.quick-actions {
  display: flex;
  flex-direction: column;
}

/* 通知统计卡片样式 */
.notice-stat-card {
  margin-bottom: 20px;
  height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.notice-stat-item {
  text-align: center;
}

.notice-stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
}

.notice-stat-label {
  font-size: 16px;
  color: #909399;
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