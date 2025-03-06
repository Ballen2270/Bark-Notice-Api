<template>
  <div class="devices-container">
    <div class="toolbar">
      <el-button type="primary" @click="handleAddDevice">添加设备</el-button>
      <el-button type="success" @click="handleRefresh">刷新</el-button>
    </div>
    
    <el-table
      v-loading="loading"
      :data="deviceList"
      border
      style="width: 100%">
      <el-table-column prop="deviceToken" label="设备Token" width="180" />
      <el-table-column prop="name" label="设备名称" width="150" />
      <el-table-column prop="deviceKey" label="设备Key" width="180" />
      <el-table-column prop="algorithm" label="算法" width="100" />
      <el-table-column prop="model" label="模式" width="100" />
      <el-table-column prop="padding" label="填充" width="150" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ACTIVE' ? '激活' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200">
        <template #default="scope">
          <el-button 
            v-if="scope.row.status === 'ACTIVE'"
            type="danger" 
            size="small" 
            @click="handleDeactivate(scope.row)">停用</el-button>
          <el-button 
            v-else
            type="success" 
            size="small" 
            @click="handleActivate(scope.row)">激活</el-button>
          <el-button 
            type="primary" 
            size="small" 
            @click="handleEdit(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    
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
import { ref, reactive, onMounted } from 'vue'
import { 
  getAllDevices, 
  saveDevice, 
  activateDevice, 
  deactivateDevice 
} from '@/api/device'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const deviceList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const deviceFormRef = ref(null)

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
  padding: 20px 0;
}

.toolbar {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style> 