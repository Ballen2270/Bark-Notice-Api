<template>
  <div class="users-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" size="small" @click="handleAddUser">新增用户</el-button>
        </div>
      </template>
      
      <el-table 
        :data="userList" 
        style="width: 100%" 
        border 
        v-loading="loading"
        :header-cell-style="{background:'#f5f7fa'}"
      >
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ scope.row.status === 'ACTIVE' ? '激活' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录时间" width="180">
          <template #default="scope">
            {{ scope.row.lastLoginTime ? new Date(scope.row.lastLoginTime).toLocaleString() : '暂无' }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 'INACTIVE'"
              type="success" 
              size="small" 
              @click="handleActivate(scope.row)"
            >
              激活
            </el-button>
            <el-button 
              v-if="scope.row.status === 'ACTIVE'"
              type="danger" 
              size="small" 
              @click="handleDeactivate(scope.row)"
            >
              禁用
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 添加/编辑用户弹窗 -->
    <el-dialog 
      :title="isEdit ? '编辑用户' : '添加用户'" 
      v-model="dialogVisible" 
      width="500px"
    >
      <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
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
import { getUserList, activateUser, deactivateUser } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const loading = ref(false)
const userList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const userFormRef = ref(null)

const userForm = reactive({
  username: '',
  password: '',
  realName: '',
  email: '',
  phone: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

// 获取用户列表
const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await getUserList()
    userList.value = res.data || []
  } catch (error) {
    console.error('获取用户列表失败', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 添加用户
const handleAddUser = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 激活用户
const handleActivate = async (row) => {
  try {
    await activateUser(row.id)
    ElMessage.success('用户激活成功')
    fetchUsers()
  } catch (error) {
    console.error('用户激活失败', error)
    ElMessage.error('用户激活失败')
  }
}

// 禁用用户
const handleDeactivate = async (row) => {
  try {
    await ElMessageBox.confirm('确定要禁用该用户吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deactivateUser(row.id)
    ElMessage.success('用户禁用成功')
    fetchUsers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('用户禁用失败', error)
      ElMessage.error('用户禁用失败')
    }
  }
}

// 提交表单
const submitForm = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 注册新用户
        await axios.post('/api/auth/register', userForm)
        ElMessage.success('用户添加成功')
        dialogVisible.value = false
        fetchUsers()
      } catch (error) {
        console.error('用户添加失败', error)
        ElMessage.error(error.response?.data?.msg || '用户添加失败')
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  if (userFormRef.value) {
    userFormRef.value.resetFields()
  }
  
  userForm.username = ''
  userForm.password = ''
  userForm.realName = ''
  userForm.email = ''
  userForm.phone = ''
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.users-container {
  padding: 20px 0;
}

.box-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 移动端适配 */
@media (max-width: 767px) {
  .users-container {
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