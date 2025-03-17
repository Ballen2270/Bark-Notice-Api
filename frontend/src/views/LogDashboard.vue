<template>
  <div class="log-dashboard">    
    <!-- 筛选条件 -->
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="filter-form">
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
        <el-form-item label="时间粒度">
          <el-select v-model="queryParams.dateType" @change="fetchDateData">
            <el-option label="按日" value="day" />
            <el-option label="按月" value="month" />
            <el-option label="按年" value="year" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchAllData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stat-cards">
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>总通知数</span>
          </div>
        </template>
        <div class="card-value">{{ totalCount }}</div>
      </el-card>
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>成功数</span>
          </div>
        </template>
        <div class="card-value success">{{ successCount }}</div>
      </el-card>
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>失败数</span>
          </div>
        </template>
        <div class="card-value failed">{{ failedCount }}</div>
      </el-card>
      <el-card class="stat-card">
        <template #header>
          <div class="card-header">
            <span>成功率</span>
          </div>
        </template>
        <div class="card-value">{{ successRate }}%</div>
      </el-card>
    </div>
    
    <!-- 按日期统计图表 -->
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span>通知发送趋势</span>
        </div>
      </template>
      <div ref="dateChartRef" class="chart-container"></div>
    </el-card>
    
    <!-- 饼图统计 -->
    <div class="pie-charts">
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>按状态统计</span>
          </div>
        </template>
        <div ref="statusChartRef" class="chart-container pie-chart"></div>
      </el-card>
      
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>按分组统计</span>
          </div>
        </template>
        <div ref="groupChartRef" class="chart-container pie-chart"></div>
      </el-card>
      
      <el-card class="chart-card">
        <template #header>
          <div class="card-header">
            <span>按设备统计</span>
          </div>
        </template>
        <div ref="deviceChartRef" class="chart-container pie-chart"></div>
      </el-card>
    </div>
    
    <!-- 查看详情按钮 -->
    <div class="action-container">
      <el-button type="primary" @click="goToDetail">查看详细日志</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts/core'
import { BarChart, LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { countByDate, countByStatus, countByGroup, countByDevice } from '../api/noticeLog'

// 注册必须的组件
echarts.use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  BarChart,
  LineChart,
  PieChart,
  CanvasRenderer
])

const router = useRouter()
const dateChartRef = ref(null)
const statusChartRef = ref(null)
const groupChartRef = ref(null)
const deviceChartRef = ref(null)
const dateRange = ref([])
const dateChart = ref(null)
const statusChart = ref(null)
const groupChart = ref(null)
const deviceChart = ref(null)
const dateData = ref([])
const statusData = ref([])
const groupData = ref([])
const deviceData = ref([])

// 查询参数
const queryParams = ref({
  beginTime: '',
  endTime: '',
  dateType: 'day'
})

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
    beginTime: '',
    endTime: '',
    dateType: 'day'
  }
  fetchAllData()
}

// 获取所有数据
const fetchAllData = () => {
  fetchDateData()
  fetchStatusData()
  fetchGroupData()
  fetchDeviceData()
}

// 获取日期统计数据
const fetchDateData = async () => {
  try {
    const res = await countByDate(queryParams.value)
    if (res.code === '000000') {
      dateData.value = res.data
      renderDateChart()
    }
  } catch (error) {
    console.error('获取日期统计数据失败', error)
  }
}

// 获取状态统计数据
const fetchStatusData = async () => {
  try {
    const res = await countByStatus({
      beginTime: queryParams.value.beginTime,
      endTime: queryParams.value.endTime
    })
    if (res.code === '000000') {
      statusData.value = res.data
      renderStatusChart()
    }
  } catch (error) {
    console.error('获取状态统计数据失败', error)
  }
}

// 获取分组统计数据
const fetchGroupData = async () => {
  try {
    const res = await countByGroup({
      beginTime: queryParams.value.beginTime,
      endTime: queryParams.value.endTime
    })
    if (res.code === '000000') {
      groupData.value = res.data
      renderGroupChart()
    }
  } catch (error) {
    console.error('获取分组统计数据失败', error)
  }
}

// 获取设备统计数据
const fetchDeviceData = async () => {
  try {
    const res = await countByDevice({
      beginTime: queryParams.value.beginTime,
      endTime: queryParams.value.endTime
    })
    if (res.code === '000000') {
      deviceData.value = res.data
      renderDeviceChart()
    }
  } catch (error) {
    console.error('获取设备统计数据失败', error)
  }
}

// 渲染日期图表
const renderDateChart = () => {
  if (!dateChartRef.value) return
  
  if (!dateChart.value) {
    dateChart.value = echarts.init(dateChartRef.value)
  }
  
  const xAxisData = dateData.value.map(item => item.dateGroup)
  const seriesData = [
    {
      name: '总数',
      type: 'line',
      data: dateData.value.map(item => item.count),
      smooth: true,
      lineStyle: {
        width: 3
      }
    },
    {
      name: '成功',
      type: 'bar',
      data: dateData.value.map(item => item.successCount),
      itemStyle: {
        color: '#67C23A'
      }
    },
    {
      name: '失败',
      type: 'bar',
      data: dateData.value.map(item => item.failedCount),
      itemStyle: {
        color: '#F56C6C'
      }
    }
  ]
  
  const option = {
    title: {
      text: '通知发送趋势'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['总数', '成功', '失败']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: true,
      data: xAxisData
    },
    yAxis: {
      type: 'value'
    },
    series: seriesData
  }
  
  dateChart.value.setOption(option)
}

// 渲染状态饼图
const renderStatusChart = () => {
  if (!statusChartRef.value) return
  
  if (!statusChart.value) {
    statusChart.value = echarts.init(statusChartRef.value)
  }
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: statusData.value.map(item => item.name)
    },
    series: [
      {
        name: '状态分布',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: statusData.value
      }
    ]
  }
  
  statusChart.value.setOption(option)
}

// 渲染分组饼图
const renderGroupChart = () => {
  if (!groupChartRef.value) return
  
  if (!groupChart.value) {
    groupChart.value = echarts.init(groupChartRef.value)
  }
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: groupData.value.map(item => item.name)
    },
    series: [
      {
        name: '分组分布',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: groupData.value
      }
    ]
  }
  
  groupChart.value.setOption(option)
}

// 渲染设备饼图
const renderDeviceChart = () => {
  if (!deviceChartRef.value) return
  
  if (!deviceChart.value) {
    deviceChart.value = echarts.init(deviceChartRef.value)
  }
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: deviceData.value.map(item => item.name)
    },
    series: [
      {
        name: '设备分布',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: deviceData.value
      }
    ]
  }
  
  deviceChart.value.setOption(option)
}

// 跳转到详情页
const goToDetail = () => {
  router.push({
    path: '/log-detail',
    query: {
      beginTime: queryParams.value.beginTime,
      endTime: queryParams.value.endTime
    }
  })
}

// 窗口大小变化时重新渲染图表
const handleResize = () => {
  dateChart.value && dateChart.value.resize()
  statusChart.value && statusChart.value.resize()
  groupChart.value && groupChart.value.resize()
  deviceChart.value && deviceChart.value.resize()
}

// 组件挂载时初始化
onMounted(() => {
  fetchAllData()
  window.addEventListener('resize', handleResize)
})

// 组件卸载时清理
const onUnmounted = () => {
  window.removeEventListener('resize', handleResize)
  dateChart.value && dateChart.value.dispose()
  statusChart.value && statusChart.value.dispose()
  groupChart.value && groupChart.value.dispose()
  deviceChart.value && deviceChart.value.dispose()
}
</script>

<style scoped>
.log-dashboard {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
}

.card-value.success {
  color: #67C23A;
}

.card-value.failed {
  color: #F56C6C;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-container {
  height: 400px;
  width: 100%;
}

.pie-charts {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.pie-chart {
  height: 300px;
}

.action-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .stat-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .pie-charts {
    grid-template-columns: 1fr;
  }
}
</style> 