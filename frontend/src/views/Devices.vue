<template>
  <div class="devices-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <el-icon class="title-icon"><Cellphone /></el-icon>
            设备管理
          </h1>
          <p class="page-subtitle">管理和配置Bark推送设备</p>
        </div>
        <div class="header-right">
          <el-button @click="handleAddDevice" type="primary" size="large" class="add-btn">
            <el-icon><Plus /></el-icon>
            添加设备
          </el-button>
          <el-button @click="handleRefresh" type="success" size="large" class="refresh-btn" :loading="loading">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-row">
      <div class="stat-card total">
        <div class="stat-icon">
          <el-icon><Monitor /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ deviceStats.total }}</div>
          <div class="stat-label">总设备数</div>
        </div>
      </div>

      <div class="stat-card active">
        <div class="stat-icon">
          <el-icon><SuccessFilled /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ deviceStats.active }}</div>
          <div class="stat-label">激活设备</div>
        </div>
      </div>

      <div class="stat-card inactive">
        <div class="stat-icon">
          <el-icon><CircleCloseFilled /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ deviceStats.inactive }}</div>
          <div class="stat-label">停用设备</div>
        </div>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-bar">
      <div class="search-input">
        <el-input
          v-model="searchText"
          placeholder="搜索设备名称、Token或Key..."
          size="large"
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>
      <div class="filter-controls">
        <el-select
          v-model="statusFilter"
          placeholder="状态筛选"
          size="large"
          clearable
          @change="handleStatusFilter"
        >
          <el-option label="全部状态" value="" />
          <el-option label="激活" value="ACTIVE" />
          <el-option label="停用" value="STOP" />
        </el-select>
      </div>
    </div>

    <!-- 桌面端表格视图 -->
    <div class="table-container desktop-only">
      <el-table
        v-loading="loading"
        :data="filteredDeviceList"
        stripe
        class="devices-table"
        empty-text="暂无设备数据">
        <el-table-column prop="deviceToken" label="设备Token" min-width="200" show-overflow-tooltip>
          <template #default="scope">
            <div class="token-cell">
              <el-icon class="cell-icon"><Key /></el-icon>
              <span class="token-text">{{ scope.row.deviceToken }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="name" label="设备名称" min-width="150">
          <template #default="scope">
            <div class="name-cell">
              <el-icon class="cell-icon"><Cellphone /></el-icon>
              <span>{{ scope.row.name || '未命名设备' }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="deviceKey" label="设备Key" min-width="180" show-overflow-tooltip>
          <template #default="scope">
            <div class="key-cell">
              <el-icon class="cell-icon"><Lock /></el-icon>
              <span class="key-text">{{ scope.row.deviceKey }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="加密配置" min-width="200">
          <template #default="scope">
            <div class="config-cell">
              <div class="config-item">
                <el-icon><Setting /></el-icon>
                <span>{{ scope.row.algorithm || 'AES' }}</span>
              </div>
              <div class="config-item">
                <el-icon><Operation /></el-icon>
                <span>{{ scope.row.model || 'CBC' }}</span>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag
              :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'"
              size="large"
              class="status-tag"
            >
              <el-icon class="status-icon">
                <component :is="scope.row.status === 'ACTIVE' ? 'SuccessFilled' : 'CircleCloseFilled'" />
              </el-icon>
              {{ scope.row.status === 'ACTIVE' ? '激活' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <div class="action-buttons">
              <el-button
                v-if="scope.row.status === 'ACTIVE'"
                type="danger"
                size="small"
                @click="handleDeactivate(scope.row)"
                class="action-btn"
              >
                <el-icon><SwitchButton /></el-icon>
                停用
              </el-button>
              <el-button
                v-else
                type="success"
                size="small"
                @click="handleActivate(scope.row)"
                class="action-btn"
              >
                <el-icon><Check /></el-icon>
                激活
              </el-button>
              <el-button
                type="primary"
                size="small"
                @click="handleEdit(scope.row)"
                class="action-btn"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 移动端卡片视图 -->
    <div class="mobile-devices-list mobile-only">
      <div v-if="loading" class="mobile-loading">
        <el-skeleton :rows="3" animated />
      </div>
      <div v-else-if="filteredDeviceList.length === 0" class="empty-state">
        <el-empty description="暂无设备数据" />
      </div>
      <div v-else class="device-cards">
        <div
          v-for="device in filteredDeviceList"
          :key="device.deviceToken"
          class="device-card"
          :class="{ 'inactive': device.status !== 'ACTIVE' }"
        >
          <div class="card-header">
            <div class="device-info">
              <div class="device-name">
                <el-icon class="device-icon"><Cellphone /></el-icon>
                <span>{{ device.name || '未命名设备' }}</span>
              </div>
              <el-tag
                :type="device.status === 'ACTIVE' ? 'success' : 'danger'"
                size="small"
                class="mobile-status-tag"
              >
                {{ device.status === 'ACTIVE' ? '激活' : '停用' }}
              </el-tag>
            </div>
          </div>

          <div class="card-content">
            <div class="info-row">
              <div class="info-item">
                <span class="info-label">Token:</span>
                <span class="info-value">{{ device.deviceToken }}</span>
              </div>
            </div>

            <div class="info-row">
              <div class="info-item">
                <span class="info-label">Key:</span>
                <span class="info-value">{{ device.deviceKey }}</span>
              </div>
            </div>

            <div class="info-row">
              <div class="info-item">
                <span class="info-label">算法:</span>
                <span class="info-value">{{ device.algorithm || 'AES' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">模式:</span>
                <span class="info-value">{{ device.model || 'CBC' }}</span>
              </div>
            </div>
          </div>

          <div class="card-actions">
            <el-button
              v-if="device.status === 'ACTIVE'"
              type="danger"
              size="small"
              @click="handleDeactivate(device)"
              class="mobile-action-btn"
            >
              <el-icon><SwitchButton /></el-icon>
              停用
            </el-button>
            <el-button
              v-else
              type="success"
              size="small"
              @click="handleActivate(device)"
              class="mobile-action-btn"
            >
              <el-icon><Check /></el-icon>
              激活
            </el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(device)"
              class="mobile-action-btn"
            >
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 添加/编辑设备对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑设备' : '添加设备'"
      width="500px">
      <el-form 
        ref="deviceFormRef"
        :model="deviceForm"
        :rules="rules"
        label-width="100px">
        <el-form-item label="设备Token" prop="deviceToken">
          <el-input v-model="deviceForm.deviceToken" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="deviceForm.name" />
        </el-form-item>
        <el-form-item label="设备Key" prop="deviceKey">
          <el-input v-model="deviceForm.deviceKey" />
        </el-form-item>
        <el-form-item label="算法" prop="algorithm">
          <el-input v-model="deviceForm.algorithm" placeholder="默认: AES" />
        </el-form-item>
        <el-form-item label="模式" prop="model">
          <el-input v-model="deviceForm.model" placeholder="默认: CBC" />
        </el-form-item>
        <el-form-item label="填充" prop="padding">
          <el-input v-model="deviceForm.padding" placeholder="默认: PKCS7Padding" />
        </el-form-item>
        <el-form-item label="编码Key" prop="encodeKey">
          <el-input v-model="deviceForm.encodeKey" placeholder="留空自动生成" />
        </el-form-item>
        <el-form-item label="IV" prop="iv">
          <el-input v-model="deviceForm.iv" placeholder="留空自动生成" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import {
  getAllDevices,
  saveDevice,
  activateDevice,
  deactivateDevice
} from '@/api/device'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Cellphone,
  Plus,
  Refresh,
  Monitor,
  SuccessFilled,
  CircleCloseFilled,
  Search,
  Key,
  Lock,
  Setting,
  Operation,
  SwitchButton,
  Check,
  Edit
} from '@element-plus/icons-vue'

const loading = ref(false)
const deviceList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const deviceFormRef = ref(null)

// 搜索和筛选状态
const searchText = ref('')
const statusFilter = ref('')

// 设备统计
const deviceStats = computed(() => {
  const stats = {
    total: deviceList.value.length,
    active: deviceList.value.filter(d => d.status === 'ACTIVE').length,
    inactive: deviceList.value.filter(d => d.status !== 'ACTIVE').length
  }
  return stats
})

// 过滤后的设备列表
const filteredDeviceList = computed(() => {
  let filtered = deviceList.value

  // 按状态筛选
  if (statusFilter.value) {
    filtered = filtered.filter(device => device.status === statusFilter.value)
  }

  // 按搜索文本筛选
  if (searchText.value) {
    const searchLower = searchText.value.toLowerCase()
    filtered = filtered.filter(device =>
      (device.name && device.name.toLowerCase().includes(searchLower)) ||
      (device.deviceToken && device.deviceToken.toLowerCase().includes(searchLower)) ||
      (device.deviceKey && device.deviceKey.toLowerCase().includes(searchLower)) ||
      (device.algorithm && device.algorithm.toLowerCase().includes(searchLower)) ||
      (device.model && device.model.toLowerCase().includes(searchLower))
    )
  }

  return filtered
})

const deviceForm = reactive({
  deviceToken: '',
  name: '',
  deviceKey: '',
  algorithm: '',
  model: '',
  padding: '',
  encodeKey: '',
  iv: ''
})

const rules = {
  deviceToken: [
    { required: true, message: '请输入设备Token', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入设备名称', trigger: 'blur' }
  ],
  deviceKey: [
    { required: true, message: '请输入设备Key', trigger: 'blur' }
  ]
}

// 获取所有设备
const fetchDevices = async () => {
  loading.value = true
  try {
    const res = await getAllDevices()
    // 检查返回数据结构，适应不同的API响应格式
    if (res.data !== undefined) {
      deviceList.value = res.data || []
    } else if (Array.isArray(res)) {
      // 如果直接返回数组数据
      deviceList.value = res
    } else {
      // 如果是其他未知结构
      deviceList.value = []
      console.warn('未知的API响应格式:', res)
    }
  } catch (error) {
    console.error('获取设备列表失败', error)
    // 显示更具体的错误信息
    ElMessage.error(`获取设备列表失败: ${error.message || '未知错误'}`)
    deviceList.value = [] // 确保列表不是undefined
  } finally {
    loading.value = false
  }
}

// 添加设备
const handleAddDevice = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑设备
const handleEdit = (row) => {
  isEdit.value = true
  resetForm()
  Object.keys(deviceForm).forEach(key => {
    if (key in row) {
      deviceForm[key] = row[key]
    }
  })
  dialogVisible.value = true
}

// 激活设备
const handleActivate = async (row) => {
  try {
    await activateDevice(row.deviceToken)
    ElMessage.success('设备激活成功')
    fetchDevices()
  } catch (error) {
    console.error('设备激活失败', error)
    ElMessage.error('设备激活失败')
  }
}

// 停用设备
const handleDeactivate = async (row) => {
  try {
    await ElMessageBox.confirm('确定要停用该设备吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deactivateDevice(row.deviceToken)
    ElMessage.success('设备停用成功')
    fetchDevices()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('设备停用失败', error)
      ElMessage.error('设备停用失败')
    }
  }
}

// 搜索处理
const handleSearch = () => {
  // 搜索功能由computed属性自动处理
}

// 状态筛选处理
const handleStatusFilter = () => {
  // 筛选功能由computed属性自动处理
}

// 刷新设备列表
const handleRefresh = () => {
  fetchDevices()
}

// 重置表单
const resetForm = () => {
  if (deviceFormRef.value) {
    deviceFormRef.value.resetFields()
  }
  Object.keys(deviceForm).forEach(key => {
    deviceForm[key] = ''
  })
}

// 提交表单
const submitForm = async () => {
  if (!deviceFormRef.value) return
  
  await deviceFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await saveDevice(deviceForm)
        ElMessage.success(isEdit.value ? '设备更新成功' : '设备添加成功')
        dialogVisible.value = false
        fetchDevices()
      } catch (error) {
        console.error(isEdit.value ? '设备更新失败' : '设备添加失败', error)
        ElMessage.error(isEdit.value ? '设备更新失败' : '设备添加失败')
      }
    }
  })
}

onMounted(() => {
  fetchDevices()
})
</script>

<style scoped>
.devices-container {
  padding: 0;
  min-height: 100vh;
  background: var(--bg-secondary);
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
  gap: var(--spacing-lg);
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

.add-btn,
.refresh-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-weight: 500;
}

/* 统计信息 */
.stats-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-xl);
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
  transform: translateY(-2px);
  box-shadow: var(--shadow-medium);
}

.stat-card:hover::before {
  opacity: 1;
}

.stat-card.total::before {
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.05) 0%, rgba(0, 122, 255, 0.02) 100%);
}

.stat-card.active::before {
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.05) 0%, rgba(52, 199, 89, 0.02) 100%);
}

.stat-card.inactive::before {
  background: linear-gradient(135deg, rgba(255, 59, 48, 0.05) 0%, rgba(255, 59, 48, 0.02) 100%);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: var(--radius-medium);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  position: relative;
  z-index: 1;
}

.stat-card.total .stat-icon {
  background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%);
  color: white;
}

.stat-card.active .stat-icon {
  background: linear-gradient(135deg, var(--success-color) 0%, #28a745 100%);
  color: white;
}

.stat-card.inactive .stat-icon {
  background: linear-gradient(135deg, var(--danger-color) 0%, #dc3545 100%);
  color: white;
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
  font-weight: 500;
}

/* 搜索和筛选 */
.search-bar {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-xl);
  background: var(--bg-primary);
  padding: var(--spacing-lg);
  border-radius: var(--radius-large);
  box-shadow: var(--shadow-light);
  border: 1px solid var(--border-light);
}

.search-input {
  flex: 1;
}

.filter-controls {
  min-width: 180px;
}

/* 桌面端表格 */
.table-container {
  background: var(--bg-primary);
  border-radius: var(--radius-large);
  overflow: hidden;
  box-shadow: var(--shadow-light);
  border: 1px solid var(--border-light);
}

.devices-table {
  width: 100%;
}

.devices-table :deep(.el-table__header-wrapper) {
  background: var(--bg-secondary);
}

.devices-table :deep(.el-table__header) {
  th {
    background: var(--bg-secondary) !important;
    color: var(--text-secondary) !important;
    font-weight: 600 !important;
    border-bottom: 1px solid var(--border-light) !important;
  }
}

.devices-table :deep(.el-table__row) {
  td {
    border-bottom: 1px solid var(--border-light) !important;
    padding: var(--spacing-md) !important;
  }
}

.devices-table :deep(.el-table__row:hover) {
  background: var(--bg-secondary) !important;
}

.token-cell,
.name-cell,
.key-cell {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.cell-icon {
  color: var(--text-secondary);
  font-size: 16px;
}

.token-text,
.key-text {
  font-family: 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace;
  font-size: var(--font-size-sm);
}

.config-cell {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.config-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-xs);
}

.config-item .el-icon {
  font-size: 12px;
  color: var(--text-secondary);
}

.status-tag {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.status-icon {
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: var(--spacing-xs);
  flex-wrap: wrap;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-xs);
  white-space: nowrap;
}

/* 移动端卡片视图 */
.mobile-devices-list {
  background: var(--bg-primary);
  border-radius: var(--radius-large);
  padding: var(--spacing-md);
  box-shadow: var(--shadow-light);
  border: 1px solid var(--border-light);
}

.mobile-loading {
  padding: var(--spacing-lg);
}

.empty-state {
  padding: var(--spacing-xl);
  text-align: center;
}

.device-cards {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.device-card {
  background: var(--bg-secondary);
  border-radius: var(--radius-medium);
  padding: var(--spacing-md);
  border: 1px solid var(--border-light);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.device-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: var(--success-color);
  transition: all 0.2s;
}

.device-card.inactive::before {
  background: var(--danger-color);
}

.device-card:hover {
  transform: translateY(-1px);
  box-shadow: var(--shadow-medium);
}

.card-header {
  margin-bottom: var(--spacing-md);
}

.device-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--spacing-md);
}

.device-name {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-weight: 600;
  color: var(--text-primary);
}

.device-icon {
  color: var(--primary-color);
  font-size: 18px;
}

.mobile-status-tag {
  font-size: var(--font-size-xs);
}

.card-content {
  margin-bottom: var(--spacing-md);
}

.info-row {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-sm);
}

.info-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.info-label {
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-value {
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  font-weight: 500;
  word-break: break-all;
}

.card-actions {
  display: flex;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
}

.mobile-action-btn {
  flex: 1;
  min-width: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-xs);
}

/* 对话框优化 */
.el-dialog {
  border-radius: var(--radius-large) !important;
  overflow: hidden !important;
}

.el-dialog__header {
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%) !important;
  padding: var(--spacing-lg) !important;
  border-bottom: 1px solid var(--border-light) !important;
}

.el-dialog__title {
  font-size: var(--font-size-lg) !important;
  font-weight: 600 !important;
  color: var(--text-primary) !important;
}

.el-dialog__body {
  padding: var(--spacing-lg) !important;
}

.dialog-footer {
  padding: var(--spacing-md) var(--spacing-lg) var(--spacing-lg) !important;
  border-top: 1px solid var(--border-light) !important;
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-sm);
}

/* 响应式适配 */
@media (max-width: 767px) {
  .devices-container {
    padding: var(--spacing-md);
  }

  .page-header {
    padding: var(--spacing-lg);
    margin-bottom: var(--spacing-lg);
  }

  .header-content {
    flex-direction: column;
    gap: var(--spacing-md);
    align-items: stretch;
    text-align: center;
  }

  .header-left {
    text-align: center;
  }

  .page-title {
    font-size: var(--font-size-lg);
    justify-content: center;
  }

  .title-icon {
    font-size: 24px;
  }

  .page-subtitle {
    text-align: center;
    font-size: var(--font-size-sm);
  }

  .header-right {
    justify-content: center;
    gap: var(--spacing-sm);
  }

  .add-btn,
  .refresh-btn {
    font-size: var(--font-size-sm);
    padding: var(--spacing-sm) var(--spacing-md);
  }

  .stats-row {
    grid-template-columns: 1fr;
    gap: var(--spacing-md);
  }

  .stat-card {
    padding: var(--spacing-md);
    gap: var(--spacing-sm);
  }

  .stat-icon {
    width: 40px;
    height: 40px;
    font-size: 20px;
  }

  .stat-value {
    font-size: 24px;
  }

  .stat-label {
    font-size: var(--font-size-xs);
  }

  .search-bar {
    flex-direction: column;
    gap: var(--spacing-sm);
    padding: var(--spacing-md);
  }

  .filter-controls {
    min-width: auto;
    width: 100%;
  }

  .desktop-only {
    display: none !important;
  }

  .mobile-only {
    display: block !important;
  }

  .mobile-devices-list {
    padding: var(--spacing-sm);
  }

  .device-card {
    padding: var(--spacing-sm);
  }

  .info-row {
    flex-direction: column;
    gap: var(--spacing-sm);
  }

  .card-actions {
    flex-direction: column;
    gap: var(--spacing-xs);
  }

  .mobile-action-btn {
    min-width: auto;
    font-size: var(--font-size-sm);
    padding: var(--spacing-xs) var(--spacing-sm);
  }
}

@media (max-width: 480px) {
  .devices-container {
    padding: var(--spacing-sm);
  }

  .page-header {
    padding: var(--spacing-md);
  }

  .stats-row {
    gap: var(--spacing-sm);
  }

  .search-bar {
    padding: var(--spacing-sm);
  }

  .device-card {
    padding: var(--spacing-xs);
  }

  .device-name {
    font-size: var(--font-size-sm);
  }
}

/* 显示控制 */
.desktop-only {
  display: block;
}

.mobile-only {
  display: none;
}

@media (max-width: 767px) {
  .desktop-only {
    display: none !important;
  }

  .mobile-only {
    display: block !important;
  }
}

/* 触摸设备优化 */
@media (hover: none) and (pointer: coarse) {
  .action-btn,
  .mobile-action-btn {
    min-height: 44px;
    min-width: 44px;
  }

  .stat-card:hover {
    transform: none;
  }

  .device-card:hover {
    transform: none;
  }
}

/* 减少动画模式 */
@media (prefers-reduced-motion: reduce) {
  .stat-card,
  .device-card,
  .action-btn,
  .mobile-action-btn {
    transition: none !important;
  }

  .stat-card:hover,
  .device-card:hover {
    transform: none !important;
  }
}
</style> 