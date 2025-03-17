<template>
  <div class="log-detail">
    <h2>日志详情</h2>
    
    <!-- 筛选条件 -->
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="分组">
          <el-input v-model="queryParams.group" placeholder="请输入分组" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="成功" value="SUCCESS" />
            <el-option label="失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchLogList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
          <el-button @click="goToDashboard">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 日志表格 -->
    <el-card>
      <el-table
        v-loading="loading"
        :data="logList"
        border
        stripe
        style="width: 100%"
        max-height="650">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" width="150" show-overflow-tooltip />
        <el-table-column prop="body" label="内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="group" label="分组" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'SUCCESS' ? 'success' : 'danger'">
              {{ scope.row.status === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recCreateTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button type="primary" link @click="showDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="通知详情"
      width="600px">
      <div v-if="currentLog">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="ID">{{ currentLog.id }}</el-descriptions-item>
          <el-descriptions-item label="URL">{{ currentLog.url }}</el-descriptions-item>
          <el-descriptions-item label="标题">{{ currentLog.title }}</el-descriptions-item>
          <el-descriptions-item label="内容">{{ currentLog.body }}</el-descriptions-item>
          <el-descriptions-item label="分组">{{ currentLog.group || '无' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="currentLog.status === 'SUCCESS' ? 'success' : 'danger'">
              {{ currentLog.status === 'SUCCESS' ? '成功' : '失败' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="结果">{{ currentLog.result }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentLog.recCreateTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getLogList } from '../api/noticeLog'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const logList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const dateRange = ref([])
const dialogVisible = ref(false)
const currentLog = ref(null)

// 查询参数
const queryParams = ref({
  group: '',
  status: '',
  beginTime: '',
  endTime: ''
})

// 日期变更处理
const handleDateChange = (val) => {
  if (val) {
    queryParams.value.beginTime = val[0]
    queryParams.value.endTime = val[1]
  } else {
    queryParams.value.beginTime = ''
    queryParams.value.endTime = ''
  }
}

// 重置查询
const resetQuery = () => {
  dateRange.value = []
  queryParams.value = {
    group: '',
    status: '',
    beginTime: '',
    endTime: ''
  }
  currentPage.value = 1
  fetchLogList()
}

// 获取日志列表
const fetchLogList = async () => {
  loading.value = true
  try {
    const res = await getLogList({
      ...queryParams.value,
      page: currentPage.value,
      pageSize: pageSize.value
    })
    
    if (res.code === '000000') {
      logList.value = res.data
      // 假设后端返回的是完整的数据列表，前端进行分页处理
      // 实际项目中应该由后端返回分页后的数据和总数
      total.value = res.data.length
    } else {
      ElMessage.error(res.msg || '获取日志列表失败')
    }
  } catch (error) {
    console.error('获取日志列表失败', error)
    ElMessage.error('获取日志列表失败')
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchLogList()
}

// 处理每页条数变化
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchLogList()
}

// 显示详情
const showDetail = (row) => {
  currentLog.value = row
  dialogVisible.value = true
}

// 返回大屏
const goToDashboard = () => {
  router.push('/log-dashboard')
}

// 组件挂载时初始化
onMounted(() => {
  // 从路由参数中获取时间范围
  if (route.query.beginTime && route.query.endTime) {
    queryParams.value.beginTime = route.query.beginTime
    queryParams.value.endTime = route.query.endTime
    dateRange.value = [route.query.beginTime, route.query.endTime]
  }
  
  fetchLogList()
})
</script>

<style scoped>
.log-detail {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-descriptions {
  margin: 20px 0;
}
</style> 