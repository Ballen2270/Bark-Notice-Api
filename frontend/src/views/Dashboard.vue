<template>
  <div class="space-y-8">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p class="text-gray-500 mt-1">Overview of your notification system</p>
      </div>
      <button 
        @click="refreshData" 
        :disabled="loading"
        class="p-2 rounded-full hover:bg-gray-100 transition-colors text-gray-500 active:scale-95"
      >
        <ArrowPathIcon class="w-6 h-6" :class="{ 'animate-spin': loading }" />
      </button>
    </div>

    <!-- Stats Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <!-- Total Notices -->
      <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 relative overflow-hidden group hover:shadow-md transition-all duration-300">
        <div class="absolute top-0 right-0 p-6 opacity-10 group-hover:opacity-20 transition-opacity">
          <BellIcon class="w-16 h-16 text-blue-500" />
        </div>
        <div class="relative z-10">
          <p class="text-sm font-medium text-gray-500">Total Notices</p>
          <h3 class="text-3xl font-bold text-gray-900 mt-2">{{ totalCount }}</h3>
          <div class="mt-4 flex items-center text-sm text-green-600 bg-green-50 w-fit px-2 py-1 rounded-lg">
            <ArrowUpIcon class="w-3 h-3 mr-1" />
            <span>Today</span>
          </div>
        </div>
      </div>

      <!-- Success Rate -->
      <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 relative overflow-hidden group hover:shadow-md transition-all duration-300">
        <div class="absolute top-0 right-0 p-6 opacity-10 group-hover:opacity-20 transition-opacity">
          <CheckCircleIcon class="w-16 h-16 text-green-500" />
        </div>
        <div class="relative z-10">
          <p class="text-sm font-medium text-gray-500">Success Rate</p>
          <h3 class="text-3xl font-bold text-gray-900 mt-2">{{ successRate }}%</h3>
          <div class="mt-4 w-full bg-gray-100 rounded-full h-1.5 overflow-hidden">
            <div class="bg-green-500 h-full rounded-full transition-all duration-1000" :style="{ width: `${successRate}%` }"></div>
          </div>
        </div>
      </div>

      <!-- Active Devices -->
      <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 relative overflow-hidden group hover:shadow-md transition-all duration-300">
        <div class="absolute top-0 right-0 p-6 opacity-10 group-hover:opacity-20 transition-opacity">
          <DevicePhoneMobileIcon class="w-16 h-16 text-purple-500" />
        </div>
        <div class="relative z-10">
          <p class="text-sm font-medium text-gray-500">Active Devices</p>
          <h3 class="text-3xl font-bold text-gray-900 mt-2">{{ deviceStats.active }}</h3>
          <p class="text-sm text-gray-400 mt-1">of {{ deviceStats.total }} registered</p>
        </div>
      </div>

      <!-- System Status -->
      <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 relative overflow-hidden group hover:shadow-md transition-all duration-300">
        <div class="absolute top-0 right-0 p-6 opacity-10 group-hover:opacity-20 transition-opacity">
          <ServerIcon class="w-16 h-16 text-orange-500" />
        </div>
        <div class="relative z-10">
          <p class="text-sm font-medium text-gray-500">System Status</p>
          <div class="flex items-center mt-2">
            <div class="w-3 h-3 rounded-full mr-2" :class="systemStatus.online ? 'bg-green-500 animate-pulse' : 'bg-red-500'"></div>
            <h3 class="text-xl font-bold text-gray-900">{{ systemStatus.online ? 'Online' : 'Offline' }}</h3>
          </div>
          <p class="text-sm text-gray-400 mt-2">Ver: {{ systemStatus.version }}</p>
        </div>
      </div>
    </div>

    <!-- Recent Activity & Charts (Placeholder for now) -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <div class="lg:col-span-2 bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
        <h3 class="text-lg font-bold text-gray-900 mb-6">Notice Traffic</h3>
        <div class="h-64 flex items-end justify-between gap-2">
          <!-- Simple CSS Bar Chart -->
          <div v-for="(item, index) in chartData" :key="index" class="flex-1 flex flex-col items-center group">
            <div 
              class="w-full bg-blue-100 rounded-t-lg group-hover:bg-blue-200 transition-colors relative"
              :style="{ height: `${(item.count / maxCount) * 100}%` }"
            >
               <div class="opacity-0 group-hover:opacity-100 absolute -top-8 left-1/2 -translate-x-1/2 bg-black text-white text-xs py-1 px-2 rounded transition-opacity whitespace-nowrap z-10">
                 {{ item.count }} notices
               </div>
            </div>
            <span class="text-xs text-gray-400 mt-2">{{ item.label }}</span>
          </div>
        </div>
      </div>

      <div class="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
        <h3 class="text-lg font-bold text-gray-900 mb-6">Quick Actions</h3>
        <div class="space-y-4">
          <button @click="$router.push('/notices')" class="w-full flex items-center p-4 rounded-2xl bg-gray-50 hover:bg-gray-100 transition-colors group">
            <div class="w-10 h-10 rounded-xl bg-blue-100 text-blue-600 flex items-center justify-center mr-4 group-hover:scale-110 transition-transform">
              <PaperAirplaneIcon class="w-5 h-5" />
            </div>
            <div class="text-left">
              <p class="font-semibold text-gray-900">Send Notice</p>
              <p class="text-xs text-gray-500">Push a new message</p>
            </div>
            <ChevronRightIcon class="w-5 h-5 text-gray-400 ml-auto" />
          </button>

          <button @click="$router.push('/devices')" class="w-full flex items-center p-4 rounded-2xl bg-gray-50 hover:bg-gray-100 transition-colors group">
            <div class="w-10 h-10 rounded-xl bg-purple-100 text-purple-600 flex items-center justify-center mr-4 group-hover:scale-110 transition-transform">
              <PlusIcon class="w-5 h-5" />
            </div>
            <div class="text-left">
              <p class="font-semibold text-gray-900">Add Device</p>
              <p class="text-xs text-gray-500">Register new device</p>
            </div>
            <ChevronRightIcon class="w-5 h-5 text-gray-400 ml-auto" />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { 
  BellIcon, 
  CheckCircleIcon, 
  DevicePhoneMobileIcon, 
  ServerIcon, 
  ArrowPathIcon, 
  ArrowUpIcon,
  PaperAirplaneIcon,
  PlusIcon,
  ChevronRightIcon
} from '@heroicons/vue/24/outline'
import { pingService } from '../api/notice'
import { getAllDevices } from '../api/device'
import { countByDate } from '../api/noticeLog'

const loading = ref(false)
const systemStatus = ref({ online: false, version: '-' })
const deviceStats = ref({ total: 0, active: 0 })
const dateData = ref([])

const totalCount = computed(() => {
  return dateData.value.reduce((sum, item) => sum + item.count, 0)
})

const successRate = computed(() => {
  if (totalCount.value === 0) return '0.0'
  const success = dateData.value.reduce((sum, item) => sum + (item.successCount || 0), 0)
  return ((success / totalCount.value) * 100).toFixed(1)
})

const chartData = computed(() => {
  // Transform dateData for chart
  // API returns: { dateGroup: '2025-11-13', count: 28, successCount: 28, failedCount: 0 }
  return dateData.value
    .filter(item => item && item.dateGroup)
    .map(item => ({
      label: item.dateGroup.slice(5), // MM-DD
      count: item.count || 0
    }))
})

const maxCount = computed(() => {
  return Math.max(...chartData.value.map(d => d.count), 10)
})

const refreshData = async () => {
  loading.value = true
  try {
    // System Status
    try {
      const res = await pingService()
      // Check for both number 200 and string '000000' codes
      const isOnline = res.code === 200 || res.code === '200' || res.success === true
      systemStatus.value = { 
        online: isOnline, 
        version: res.data?.version || res.version || '1.0.0' 
      }
    } catch {
      systemStatus.value = { online: false, version: '-' }
    }

    // Devices
    try {
      const res = await getAllDevices()
      const devices = res.data || []
      deviceStats.value = {
        total: devices.length,
        active: devices.filter(d => d.status === 'ACTIVE').length
      }
    } catch (e) {
      console.error(e)
    }

    // Logs - Get last 7 days of data
    try {
      const today = new Date()
      const sevenDaysAgo = new Date(today)
      sevenDaysAgo.setDate(today.getDate() - 6) // Last 7 days including today
      
      const endTime = today.toISOString().slice(0, 10)
      const beginTime = sevenDaysAgo.toISOString().slice(0, 10)
      
      const res = await countByDate({
        dateType: 'day',
        beginTime: beginTime,
        endTime: endTime
      })
      if (res.code === '000000') {
        dateData.value = res.data || []
      }
    } catch (e) {
      console.error(e)
    }

  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refreshData()
})
</script>
