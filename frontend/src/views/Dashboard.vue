<template>
  <div class="dashboard-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon class="title-icon"><DataAnalysis /></el-icon>
            数据概览
          </h1>
          <p class="page-subtitle">实时监控系统状态和数据统计</p>
        </div>
        <div class="header-right">
          <el-button @click="refreshAllData" type="primary" :loading="refreshing">
            <el-icon><Refresh /></el-icon>
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 今日统计概览 -->
    <div class="stats-section">
      <div class="section-header">
        <h2 class="section-title">
          <el-icon><TrendCharts /></el-icon>
          今日通知统计
        </h2>
        <span class="section-time">{{ currentTime }}</span>
      </div>
      
      <el-row :gutter="20" class="stats-cards">
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6" class="stat-col">
          <div class="stat-card total-card">
            <div class="stat-icon-wrapper">
              <el-icon class="stat-icon total-icon"><Notification /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ totalCount }}</div>
              <div class="stat-label">总通知数</div>
              <div class="stat-trend">
                <el-icon class="trend-icon"><ArrowUp /></el-icon>
                <span class="trend-text">较昨日 +12%</span>
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6" class="stat-col">
          <div class="stat-card success-card">
            <div class="stat-icon-wrapper">
              <el-icon class="stat-icon success-icon"><SuccessFilled /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ successCount }}</div>
              <div class="stat-label">成功推送</div>
              <div class="stat-trend positive">
                <el-icon class="trend-icon"><ArrowUp /></el-icon>
                <span class="trend-text">成功率 {{ successRate }}%</span>
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6" class="stat-col">
          <div class="stat-card error-card">
            <div class="stat-icon-wrapper">
              <el-icon class="stat-icon error-icon"><CircleCloseFilled /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ failedCount }}</div>
              <div class="stat-label">失败数量</div>
              <div class="stat-trend negative" v-if="failedCount > 0">
                <el-icon class="trend-icon"><ArrowUp /></el-icon>
                <span class="trend-text">需要关注</span>
              </div>
              <div class="stat-trend positive" v-else>
                <el-icon class="trend-icon"><Check /></el-icon>
                <span class="trend-text">运行良好</span>
              </div>
            </div>
          </div>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6" :lg="6" :xl="6" class="stat-col">
          <div class="stat-card rate-card">
            <div class="stat-icon-wrapper">
              <el-icon class="stat-icon rate-icon"><TrendCharts /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ successRate }}%</div>
              <div class="stat-label">推送成功率</div>
              <div class="progress-wrapper">
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: successRate + '%' }"></div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 系统信息 -->
    <div class="system-section">
      <div class="section-header">
        <h2 class="section-title">
          <el-icon><Monitor /></el-icon>
          系统监控
        </h2>
      </div>
      
      <el-row :gutter="20" class="info-cards">
        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="info-col">
          <el-card class="info-card system-card">
            <template #header>
              <div class="card-header">
                <div class="card-title">
                  <el-icon class="card-icon"><Setting /></el-icon>
                  <span>系统状态</span>
                </div>
                <el-button 
                  @click="checkSystemStatus" 
                  :loading="systemLoading"
                  type="primary" 
                  size="small" 
                  class="refresh-btn"
                >
                  <el-icon><Refresh /></el-icon>
                </el-button>
              </div>
            </template>
            
            <div class="status-content">
              <div class="status-item">
                <div class="status-indicator" :class="{ 'online': systemStatus.online, 'offline': !systemStatus.online }"></div>
                <div class="status-info">
                  <span class="status-label">服务状态</span>
                  <span class="status-value" :class="{ 'success': systemStatus.online, 'error': !systemStatus.online }">
                    {{ systemStatus.online ? '在线运行中' : '服务离线' }}
                  </span>
                </div>
              </div>
              
              <div class="status-item">
                <div class="status-indicator version"></div>
                <div class="status-info">
                  <span class="status-label">API版本</span>
                  <span class="status-value">{{ systemStatus.version || '未知版本' }}</span>
                </div>
              </div>
              
              <div class="status-item">
                <div class="status-indicator time"></div>
                <div class="status-info">
                  <span class="status-label">最后检查</span>
                  <span class="status-value">{{ systemStatus.lastCheck || '未检查' }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="info-col">
          <el-card class="info-card device-card">
            <template #header>
              <div class="card-header">
                <div class="card-title">
                  <el-icon class="card-icon"><Cellphone /></el-icon>
                  <span>设备统计</span>
                </div>
              </div>
            </template>
            
            <div class="status-content">
              <div class="status-item">
                <div class="status-indicator total"></div>
                <div class="status-info">
                  <span class="status-label">总设备数</span>
                  <span class="status-value">{{ deviceStats.total }}</span>
                </div>
              </div>
              
              <div class="status-item">
                <div class="status-indicator active"></div>
                <div class="status-info">
                  <span class="status-label">活跃设备</span>
                  <span class="status-value success">{{ deviceStats.active }}</span>
                </div>
              </div>
              
              <div class="status-item">
                <div class="status-indicator inactive"></div>
                <div class="status-info">
                  <span class="status-label">停用设备</span>
                  <span class="status-value error">{{ deviceStats.inactive }}</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="info-col">
          <el-card class="info-card action-card">
            <template #header>
              <div class="card-header">
                <div class="card-title">
                  <el-icon class="card-icon"><Operation /></el-icon>
                  <span>快速操作</span>
                </div>
              </div>
            </template>
            
            <div class="quick-actions">
              <el-button 
                @click="$router.push('/devices')" 
                type="primary" 
                class="action-btn"
                size="large"
              >
                <el-icon><Cellphone /></el-icon>
                管理设备
              </el-button>
              
              <el-button 
                @click="$router.push('/notices')" 
                type="success" 
                class="action-btn"
                size="large"
              >
                <el-icon><ChatDotRound /></el-icon>
                发送通知
              </el-button>
              
              <el-button 
                @click="$router.push('/log-dashboard')" 
                type="info" 
                class="action-btn"
                size="large"
              >
                <el-icon><DataAnalysis /></el-icon>
                日志大屏
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue'
import { pingService } from '@/api/notice'
import { getAllDevices } from '@/api/device'
import { countByDate } from '@/api/noticeLog'
import { ElMessage } from 'element-plus'
import {
  TrendCharts,
  Monitor,
  Setting,
  Cellphone,
  Operation,
  Notification,
  SuccessFilled,
  CircleCloseFilled,
  ArrowUp,
  Check,
  Refresh,
  DataAnalysis,
  ChatDotRound
} from '@element-plus/icons-vue'

// 状态变量
const refreshing = ref(false)
const systemLoading = ref(false)
const currentTime = ref('')
const timeInterval = ref(null)

// 更新当前时间
const updateCurrentTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

// 刷新所有数据
const refreshAllData = async () => {
  refreshing.value = true
  try {
    await Promise.all([
      checkSystemStatus(),
      getDeviceStatistics(),
      getNoticeLogStatistics()
    ])
    ElMessage.success('数据刷新成功')
  } catch (error) {
    ElMessage.error('数据刷新失败')
  } finally {
    refreshing.value = false
  }
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
  systemLoading.value = true
  try {
    const res = await pingService()
    systemStatus.value.online = res.data?.code === 200
    systemStatus.value.version = res.data?.version || '未知'
    systemStatus.value.lastCheck = new Date().toLocaleString('zh-CN')
  } catch (error) {
    systemStatus.value.online = false
    systemStatus.value.lastCheck = new Date().toLocaleString('zh-CN')
    throw error
  } finally {
    systemLoading.value = false
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
  updateCurrentTime()
  timeInterval.value = setInterval(updateCurrentTime, 1000)

  checkSystemStatus()
  getDeviceStatistics()
  getNoticeLogStatistics()
})

onUnmounted(() => {
  if (timeInterval.value) {
    clearInterval(timeInterval.value)
  }
})
</script>

<style scoped>
.dashboard-container {
  padding: 0;
  min-height: 100vh;
}

/* 页面头部 */
.page-header {
  background: linear-gradient(135deg, var(--bg-primary) 0%, rgba(255, 255, 255, 0.9) 100%);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-large);
  padding: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
  box-shadow: var(--shadow-medium);
  border: 1px solid var(--border-light);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  flex: 1;
}

.page-title {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-xl);
  font-weight: 700;
  margin: 0 0 var(--spacing-xs) 0;
  color: var(--text-primary);
  background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-icon {
  font-size: 32px;
  color: var(--primary-color);
}

.page-subtitle {
  margin: 0;
  color: var(--text-secondary);
  font-size: var(--font-size-md);
  font-weight: 400;
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

/* 区块样式 */
.stats-section,
.system-section {
  margin-bottom: var(--spacing-xl);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--spacing-lg);
  padding: 0 var(--spacing-sm);
}

.section-title {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-lg);
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
}

.section-time {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  background: var(--bg-primary);
  padding: var(--spacing-xs) var(--spacing-md);
  border-radius: var(--radius-medium);
  border: 1px solid var(--border-light);
  font-weight: 500;
}

/* 统计卡片 */
.stats-cards .stat-col {
  margin-bottom: var(--spacing-md);
}

.stat-card {
  background: var(--bg-primary);
  border-radius: var(--radius-large);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-light);
  border: 1px solid var(--border-light);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  position: relative;
  overflow: hidden;
  height: 120px;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 0;
  transition: opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-heavy);
}

.stat-card:hover::before {
  opacity: 1;
}

.total-card::before {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.05) 0%, rgba(0, 122, 255, 0.02) 100%);
}

.success-card::before {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.05) 0%, rgba(52, 199, 89, 0.02) 100%);
}

.error-card::before {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.05) 0%, rgba(255, 59, 48, 0.02) 100%);
}

.rate-card::before {
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.05) 0%, rgba(255, 149, 0, 0.02) 100%);
}

.stat-icon-wrapper {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-large);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 1;
}

.total-icon {
  color: var(--primary-color);
}

.success-icon {
  color: var(--success-color);
}

.error-icon {
  color: var(--danger-color);
}

.rate-icon {
  color: var(--warning-color);
}

.stat-content {
  flex: 1;
  position: relative;
  z-index: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
  line-height: 1;
}

.stat-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin-bottom: var(--spacing-xs);
  font-weight: 500;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-xs);
}

.trend-icon {
  font-size: 14px;
}

.stat-trend.positive {
  color: var(--success-color);
}

.stat-trend.negative {
  color: var(--danger-color);
}

.trend-text {
  font-weight: 500;
}

/* 进度条 */
.progress-wrapper {
  width: 100%;
  height: 6px;
  background: var(--bg-secondary);
  border-radius: 3px;
  overflow: hidden;
  position: relative;
}

.progress-bar {
  height: 100%;
  background: var(--border-light);
  border-radius: 3px;
  position: relative;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--warning-color) 0%, #fd7e14 100%);
  border-radius: 3px;
  transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
}

/* 信息卡片 */
.info-cards .info-col {
  margin-bottom: var(--spacing-md);
}

.info-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-light);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.info-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-medium);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-weight: 600;
  color: var(--text-primary);
}

.card-icon {
  color: var(--primary-color);
  font-size: 18px;
}

.refresh-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  padding: 0;
  border-radius: var(--radius-small);
}

/* 状态内容 */
.status-content {
  padding: var(--spacing-sm) 0;
}

.status-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-md);
  padding: var(--spacing-sm);
  border-radius: var(--radius-medium);
  transition: background-color 0.2s;
}

.status-item:hover {
  background: var(--bg-secondary);
}

.status-item:last-child {
  margin-bottom: 0;
}

.status-indicator {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  flex-shrink: 0;
  position: relative;
}

.status-indicator.online {
  background: var(--success-color);
  box-shadow: 0 0 0 3px rgba(52, 199, 89, 0.2);
  animation: pulse-green 2s infinite;
}

.status-indicator.offline {
  background: var(--danger-color);
  box-shadow: 0 0 0 3px rgba(255, 59, 48, 0.2);
  animation: pulse-red 2s infinite;
}

.status-indicator.version {
  background: linear-gradient(135deg, var(--info-color) 0%, #17a2b8 100%);
  box-shadow: 0 0 0 3px rgba(90, 200, 250, 0.2);
}

.status-indicator.time {
  background: linear-gradient(135deg, var(--warning-color) 0%, #fd7e14 100%);
  box-shadow: 0 0 0 3px rgba(255, 149, 0, 0.2);
}

.status-indicator.total {
  background: linear-gradient(135deg, var(--text-secondary) 0%, #6c757d 100%);
  box-shadow: 0 0 0 3px rgba(134, 134, 139, 0.2);
}

.status-indicator.active {
  background: var(--success-color);
  box-shadow: 0 0 0 3px rgba(52, 199, 89, 0.2);
}

.status-indicator.inactive {
  background: var(--danger-color);
  box-shadow: 0 0 0 3px rgba(255, 59, 48, 0.2);
}

.status-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.status-label {
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-value {
  font-size: var(--font-size-md);
  color: var(--text-primary);
  font-weight: 600;
}

.status-value.success {
  color: var(--success-color);
}

.status-value.error {
  color: var(--danger-color);
}

/* 快速操作 */
.quick-actions {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.action-btn {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: var(--spacing-sm);
  padding: var(--spacing-md) var(--spacing-lg);
  border-radius: var(--radius-medium);
  font-weight: 500;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  height: auto;
}

.action-btn:hover {
  transform: translateX(4px);
  box-shadow: var(--shadow-medium);
}

/* 动画效果 */
@keyframes pulse-green {
  0% {
    box-shadow: 0 0 0 0 rgba(52, 199, 89, 0.7);
  }
  70% {
    box-shadow: 0 0 0 6px rgba(52, 199, 89, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(52, 199, 89, 0);
  }
}

@keyframes pulse-red {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 59, 48, 0.7);
  }
  70% {
    box-shadow: 0 0 0 6px rgba(255, 59, 48, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(255, 59, 48, 0);
  }
}

/* 移动端适配 */
@media (max-width: 767px) {
  .dashboard-container {
    padding: var(--spacing-sm);
    padding-top: calc(60px + var(--spacing-md));
    min-height: 100vh;
    overflow-x: hidden;
  }

  .page-header {
    padding: var(--spacing-lg) var(--spacing-md);
    margin-bottom: var(--spacing-lg);
    border-radius: var(--radius-medium);
    position: relative;
    overflow: hidden;
  }

  .page-header::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(0, 122, 255, 0.03) 0%, rgba(0, 122, 255, 0.01) 100%);
    pointer-events: none;
  }

  .header-content {
    flex-direction: column;
    gap: var(--spacing-lg);
    align-items: stretch;
    position: relative;
    z-index: 1;
  }

  .header-left {
    text-align: center;
  }

  .page-title {
    font-size: var(--font-size-lg);
    justify-content: center;
    flex-wrap: wrap;
    line-height: 1.3;
  }

  .title-icon {
    font-size: 28px;
    margin-right: var(--spacing-xs);
  }

  .page-subtitle {
    text-align: center;
    font-size: var(--font-size-sm);
    margin-top: var(--spacing-xs);
    line-height: 1.4;
  }

  .header-right {
    justify-content: center;
  }

  .section-header {
    flex-direction: column;
    gap: var(--spacing-md);
    align-items: stretch;
    text-align: center;
    padding: 0;
    margin-bottom: var(--spacing-lg);
  }

  .section-title {
    font-size: var(--font-size-md);
    justify-content: center;
    line-height: 1.3;
  }

  .section-time {
    text-align: center;
    align-self: center;
    font-size: var(--font-size-sm);
    padding: var(--spacing-xs) var(--spacing-md);
  }

  .stats-cards .stat-col {
    margin-bottom: var(--spacing-lg);
  }

  .stat-card {
    height: auto;
    min-height: 110px;
    padding: var(--spacing-lg);
    gap: var(--spacing-md);
    border-radius: var(--radius-medium);
    position: relative;
    overflow: hidden;
  }

  .stat-card::after {
    content: '';
    position: absolute;
    top: -50%;
    right: -50%;
    width: 100%;
    height: 100%;
    background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.1) 50%, transparent 70%);
    transform: rotate(45deg);
    transition: transform 0.6s ease;
    pointer-events: none;
  }

  .stat-card:active::after {
    transform: rotate(45deg) translateX(100%);
  }

  .stat-icon-wrapper {
    width: 54px;
    height: 54px;
    flex-shrink: 0;
  }

  .stat-content {
    flex: 1;
    min-width: 0;
  }

  .stat-value {
    font-size: 26px;
    line-height: 1.2;
    margin-bottom: var(--spacing-xs);
  }

  .stat-label {
    font-size: var(--font-size-sm);
    margin-bottom: var(--spacing-xs);
    line-height: 1.3;
  }

  .stat-trend {
    font-size: 11px;
    line-height: 1.2;
  }

  .progress-wrapper {
    height: 4px;
    margin-top: var(--spacing-xs);
  }

  .info-cards .info-col {
    margin-bottom: var(--spacing-lg);
  }

  .info-card {
    border-radius: var(--radius-medium);
    overflow: hidden;
  }

  .info-card .el-card__header {
    padding: var(--spacing-md);
    background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
  }

  .info-card .el-card__body {
    padding: var(--spacing-md);
  }

  .status-item {
    padding: var(--spacing-sm);
    margin-bottom: var(--spacing-md);
    border-radius: var(--radius-small);
    transition: all 0.2s ease;
  }

  .status-item:active {
    background: var(--bg-secondary);
    transform: scale(0.98);
  }

  .status-item:last-child {
    margin-bottom: 0;
  }

  .status-label {
    font-size: 11px;
    line-height: 1.2;
  }

  .status-value {
    font-size: var(--font-size-sm);
    line-height: 1.3;
  }

  .action-btn {
    padding: var(--spacing-md);
    font-size: var(--font-size-sm);
    min-height: 44px;
    justify-content: center;
    border-radius: var(--radius-medium);
    transition: all 0.2s ease;
    position: relative;
    overflow: hidden;
  }

  .action-btn::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    transform: translate(-50%, -50%);
    transition: width 0.3s, height 0.3s;
  }

  .action-btn:active::before {
    width: 200px;
    height: 200px;
  }

  .quick-actions {
    gap: var(--spacing-md);
  }
}

@media (max-width: 480px) {
  .dashboard-container {
    padding: var(--spacing-xs);
    padding-top: calc(60px + var(--spacing-sm));
    min-height: 100vh;
    overflow-x: hidden;
  }

  .page-header {
    padding: var(--spacing-md);
    margin-bottom: var(--spacing-md);
    border-radius: var(--radius-small);
  }

  .header-content {
    gap: var(--spacing-md);
  }

  .page-title {
    font-size: var(--font-size-md);
    line-height: 1.2;
  }

  .title-icon {
    font-size: 24px;
  }

  .page-subtitle {
    font-size: var(--font-size-xs);
    line-height: 1.3;
  }

  .section-header {
    gap: var(--spacing-sm);
    margin-bottom: var(--spacing-md);
  }

  .section-title {
    font-size: var(--font-size-sm);
    line-height: 1.2;
  }

  .section-time {
    font-size: var(--font-size-xs);
    padding: var(--spacing-xs) var(--spacing-sm);
  }

  .stats-cards .stat-col {
    margin-bottom: var(--spacing-md);
  }

  .stat-card {
    height: auto;
    min-height: 100px;
    padding: var(--spacing-md);
    gap: var(--spacing-sm);
    border-radius: var(--radius-small);
  }

  .stat-icon-wrapper {
    width: 48px;
    height: 48px;
    flex-shrink: 0;
  }

  .stat-content {
    flex: 1;
    min-width: 0;
  }

  .stat-value {
    font-size: 24px;
    line-height: 1.1;
    margin-bottom: 2px;
  }

  .stat-label {
    font-size: var(--font-size-xs);
    margin-bottom: 2px;
    line-height: 1.2;
  }

  .stat-trend {
    font-size: 10px;
    line-height: 1.1;
  }

  .progress-wrapper {
    height: 3px;
    margin-top: 2px;
  }

  .info-cards .info-col {
    margin-bottom: var(--spacing-md);
  }

  .info-card {
    border-radius: var(--radius-small);
  }

  .info-card .el-card__header {
    padding: var(--spacing-sm);
  }

  .info-card .el-card__body {
    padding: var(--spacing-sm);
  }

  .card-header {
    padding-bottom: var(--spacing-sm);
    flex-wrap: wrap;
    gap: var(--spacing-xs);
  }

  .status-item {
    padding: var(--spacing-xs);
    margin-bottom: var(--spacing-sm);
    border-radius: var(--radius-small);
  }

  .status-item:last-child {
    margin-bottom: 0;
  }

  .status-label {
    font-size: 10px;
    line-height: 1.1;
  }

  .status-value {
    font-size: var(--font-size-xs);
    line-height: 1.2;
  }

  .quick-actions {
    gap: var(--spacing-sm);
  }

  .action-btn {
    padding: var(--spacing-sm) var(--spacing-md);
    font-size: var(--font-size-xs);
    min-height: 40px;
    border-radius: var(--radius-small);
    line-height: 1.2;
  }

  .refresh-btn {
    width: 28px;
    height: 28px;
    min-height: 28px;
  }
}

/* 触摸设备优化 */
@media (hover: none) and (pointer: coarse) {
  .stat-card:hover {
    transform: none;
    box-shadow: var(--shadow-light);
  }

  .stat-card:active {
    transform: scale(0.98);
    box-shadow: var(--shadow-medium);
  }

  .info-card:hover {
    transform: none;
    box-shadow: var(--shadow-light);
  }

  .info-card:active {
    transform: scale(0.98);
    box-shadow: var(--shadow-medium);
  }

  .action-btn:hover {
    transform: none;
    box-shadow: var(--shadow-light);
  }

  .action-btn:active {
    transform: scale(0.95);
    box-shadow: var(--shadow-medium);
  }

  .status-item:hover {
    background: transparent;
  }

  .status-item:active {
    background: var(--bg-secondary);
    transform: scale(0.98);
  }

  .stat-card {
    transition: all 0.2s ease;
  }

  .info-card {
    transition: all 0.2s ease;
  }

  .action-btn {
    transition: all 0.2s ease;
  }

  .status-item {
    transition: all 0.2s ease;
  }
}

/* 减少动画模式 */
@media (prefers-reduced-motion: reduce) {
  .stat-card,
  .info-card,
  .action-btn,
  .status-item,
  .status-indicator,
  .progress-fill {
    transition: none !important;
  }

  .stat-card:hover,
  .info-card:hover,
  .action-btn:hover {
    transform: none !important;
  }

  .status-indicator.online,
  .status-indicator.offline {
    animation: none;
  }
}
</style> 