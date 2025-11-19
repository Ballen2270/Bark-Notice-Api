<template>
  <div class="space-y-8">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Logs</h1>
        <p class="text-gray-500 mt-1">History of sent notifications</p>
      </div>
      <div class="flex items-center gap-3">
        <button 
          @click="refreshData" 
          :disabled="loading"
          class="p-2 rounded-full hover:bg-gray-100 transition-colors text-gray-500 active:scale-95"
        >
          <ArrowPathIcon class="w-6 h-6" :class="{ 'animate-spin': loading }" />
        </button>
      </div>
    </div>

    <!-- Filters -->
    <div class="bg-white p-4 rounded-2xl shadow-sm border border-gray-100 flex flex-col md:flex-row gap-4">
      <div class="flex-1 relative">
        <MagnifyingGlassIcon class="w-5 h-5 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
        <input 
          v-model="filters.group"
          type="text" 
          placeholder="Filter by Group..." 
          class="w-full pl-10 pr-4 py-2 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
          @input="handleFilter"
        >
      </div>
      <select 
        v-model="filters.status"
        class="px-4 py-2 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
        @change="handleFilter"
      >
        <option value="">All Status</option>
        <option value="SUCCESS">Success</option>
        <option value="FAILED">Failed</option>
      </select>
    </div>

    <!-- Log List -->
    <div class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full text-left">
          <thead class="bg-gray-50 border-b border-gray-100">
            <tr>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Time</th>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Title / Body</th>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Group</th>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider text-right">Action</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr v-if="loading && logs.length === 0">
              <td colspan="5" class="px-6 py-12 text-center text-gray-500">
                Loading logs...
              </td>
            </tr>
            <tr v-else-if="logs.length === 0">
              <td colspan="5" class="px-6 py-12 text-center text-gray-500">
                No logs found
              </td>
            </tr>
            <tr v-for="log in logs" :key="log.id" class="hover:bg-gray-50/50 transition-colors">
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ formatDate(log.recCreateTime) }}
              </td>
              <td class="px-6 py-4">
                <div class="max-w-md">
                  <p class="font-medium text-gray-900 truncate">{{ log.title || 'No Title' }}</p>
                  <p class="text-xs text-gray-500 truncate mt-0.5">{{ log.body }}</p>
                </div>
              </td>
              <td class="px-6 py-4">
                <span v-if="log.group" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-50 text-blue-700">
                  {{ log.group }}
                </span>
                <span v-else class="text-gray-400 text-xs">-</span>
              </td>
              <td class="px-6 py-4">
                <span 
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                  :class="log.status === 'SUCCESS' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                >
                  <span class="w-1.5 h-1.5 rounded-full mr-1.5" :class="log.status === 'SUCCESS' ? 'bg-green-500' : 'bg-red-500'"></span>
                  {{ log.status === 'SUCCESS' ? 'Success' : 'Failed' }}
                </span>
              </td>
              <td class="px-6 py-4 text-right">
                <button 
                  @click="showDetail(log)"
                  class="text-blue-600 hover:text-blue-800 text-sm font-medium"
                >
                  Details
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- Pagination -->
      <div class="px-6 py-4 border-t border-gray-100 flex items-center justify-between">
        <div class="text-sm text-gray-500">
          Showing <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span> to <span class="font-medium">{{ Math.min(currentPage * pageSize, total) }}</span> of <span class="font-medium">{{ total }}</span> results
        </div>
        <div class="flex gap-2">
          <button 
            @click="changePage(currentPage - 1)"
            :disabled="currentPage === 1"
            class="px-3 py-1 rounded-lg border border-gray-200 text-sm font-medium hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Previous
          </button>
          <button 
            @click="changePage(currentPage + 1)"
            :disabled="currentPage * pageSize >= total"
            class="px-3 py-1 rounded-lg border border-gray-200 text-sm font-medium hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Next
          </button>
        </div>
      </div>
    </div>

    <!-- Detail Modal -->
    <div v-if="selectedLog" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/20 backdrop-blur-sm" @click="selectedLog = null"></div>
      <div class="bg-white rounded-3xl shadow-2xl w-full max-w-lg relative z-10 overflow-hidden animate-in fade-in zoom-in duration-200">
        <div class="p-6 border-b border-gray-100 flex items-center justify-between">
          <h3 class="text-lg font-bold text-gray-900">Log Details</h3>
          <button @click="selectedLog = null" class="text-gray-400 hover:text-gray-600">
            <XMarkIcon class="w-6 h-6" />
          </button>
        </div>
        
        <div class="p-6 space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-xs font-medium text-gray-500 uppercase">ID</label>
              <p class="mt-1 text-sm text-gray-900">{{ selectedLog.id }}</p>
            </div>
            <div>
              <label class="block text-xs font-medium text-gray-500 uppercase">Time</label>
              <p class="mt-1 text-sm text-gray-900">{{ formatDate(selectedLog.recCreateTime) }}</p>
            </div>
          </div>

          <div>
            <label class="block text-xs font-medium text-gray-500 uppercase">Title</label>
            <p class="mt-1 text-sm text-gray-900 font-medium">{{ selectedLog.title || '-' }}</p>
          </div>

          <div>
            <label class="block text-xs font-medium text-gray-500 uppercase">Body</label>
            <p class="mt-1 text-sm text-gray-900 bg-gray-50 p-3 rounded-xl">{{ selectedLog.body }}</p>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-xs font-medium text-gray-500 uppercase">Group</label>
              <p class="mt-1 text-sm text-gray-900">{{ selectedLog.group || '-' }}</p>
            </div>
            <div>
              <label class="block text-xs font-medium text-gray-500 uppercase">Status</label>
              <p class="mt-1">
                <span 
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                  :class="selectedLog.status === 'SUCCESS' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                >
                  {{ selectedLog.status === 'SUCCESS' ? 'Success' : 'Failed' }}
                </span>
              </p>
            </div>
          </div>

          <div v-if="selectedLog.url">
            <label class="block text-xs font-medium text-gray-500 uppercase">URL</label>
            <a :href="selectedLog.url" target="_blank" class="mt-1 text-sm text-blue-600 hover:underline truncate block">{{ selectedLog.url }}</a>
          </div>
          
          <div v-if="selectedLog.result">
             <label class="block text-xs font-medium text-gray-500 uppercase">Result</label>
             <pre class="mt-1 text-xs text-gray-600 bg-gray-50 p-2 rounded-lg overflow-x-auto">{{ selectedLog.result }}</pre>
          </div>
        </div>

        <div class="p-6 bg-gray-50 flex justify-end">
          <button @click="selectedLog = null" class="px-4 py-2 bg-white border border-gray-200 text-gray-700 font-medium rounded-xl hover:bg-gray-50 transition-colors">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { 
  ArrowPathIcon, 
  MagnifyingGlassIcon,
  XMarkIcon
} from '@heroicons/vue/24/outline'
import { getLogList } from '../api/noticeLog'

const loading = ref(false)
const logs = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const selectedLog = ref(null)

const filters = reactive({
  group: '',
  status: ''
})

const fetchLogs = async () => {
  loading.value = true
  try {
    const res = await getLogList({
      page: currentPage.value,
      pageSize: pageSize.value,
      ...filters
    })
    if (res.code === '000000') {
      logs.value = res.data.data || []
      total.value = res.data.total || 0
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const refreshData = () => {
  currentPage.value = 1
  fetchLogs()
}

const handleFilter = () => {
  currentPage.value = 1
  fetchLogs()
}

const changePage = (page) => {
  currentPage.value = page
  fetchLogs()
}

const showDetail = (log) => {
  selectedLog.value = log
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString()
}

onMounted(() => {
  fetchLogs()
})
</script>
