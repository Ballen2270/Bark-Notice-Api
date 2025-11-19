<template>
  <div class="space-y-8">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Devices</h1>
        <p class="text-gray-500 mt-1">Manage your registered devices</p>
      </div>
      <div class="flex items-center gap-3">
        <button 
          @click="refreshData" 
          :disabled="loading"
          class="p-2 rounded-full hover:bg-gray-100 transition-colors text-gray-500 active:scale-95"
        >
          <ArrowPathIcon class="w-6 h-6" :class="{ 'animate-spin': loading }" />
        </button>
        <button 
          @click="handleAdd"
          class="flex items-center px-4 py-2 bg-black text-white rounded-xl font-medium hover:bg-gray-800 active:scale-95 transition-all shadow-lg shadow-black/10"
        >
          <PlusIcon class="w-5 h-5 mr-2" />
          Add Device
        </button>
      </div>
    </div>

    <!-- Stats -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex items-center justify-between">
        <div>
          <p class="text-sm font-medium text-gray-500">Total Devices</p>
          <h3 class="text-2xl font-bold text-gray-900 mt-1">{{ stats.total }}</h3>
        </div>
        <div class="w-12 h-12 bg-blue-50 rounded-2xl flex items-center justify-center text-blue-600">
          <DevicePhoneMobileIcon class="w-6 h-6" />
        </div>
      </div>
      <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex items-center justify-between">
        <div>
          <p class="text-sm font-medium text-gray-500">Active</p>
          <h3 class="text-2xl font-bold text-gray-900 mt-1">{{ stats.active }}</h3>
        </div>
        <div class="w-12 h-12 bg-green-50 rounded-2xl flex items-center justify-center text-green-600">
          <CheckCircleIcon class="w-6 h-6" />
        </div>
      </div>
      <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100 flex items-center justify-between">
        <div>
          <p class="text-sm font-medium text-gray-500">Inactive</p>
          <h3 class="text-2xl font-bold text-gray-900 mt-1">{{ stats.inactive }}</h3>
        </div>
        <div class="w-12 h-12 bg-red-50 rounded-2xl flex items-center justify-center text-red-600">
          <XCircleIcon class="w-6 h-6" />
        </div>
      </div>
    </div>

    <!-- Search & Filter -->
    <div class="bg-white p-4 rounded-2xl shadow-sm border border-gray-100 flex flex-col sm:flex-row gap-4">
      <div class="relative flex-1">
        <MagnifyingGlassIcon class="w-5 h-5 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
        <input 
          v-model="searchQuery"
          type="text" 
          placeholder="Search devices..." 
          class="w-full pl-10 pr-4 py-2 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
        >
      </div>
      <select 
        v-model="statusFilter"
        class="px-4 py-2 rounded-xl bg-gray-50 border-transparent focus:bg-white focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
      >
        <option value="">All Status</option>
        <option value="ACTIVE">Active</option>
        <option value="STOP">Inactive</option>
      </select>
    </div>

    <!-- Device List -->
    <div class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full text-left">
          <thead class="bg-gray-50 border-b border-gray-100">
            <tr>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Device Name</th>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Token</th>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
              <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider text-right">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-100">
            <tr v-if="filteredDevices.length === 0">
              <td colspan="4" class="px-6 py-12 text-center text-gray-500">
                No devices found
              </td>
            </tr>
            <tr v-for="device in filteredDevices" :key="device.deviceToken" class="hover:bg-gray-50/50 transition-colors group">
              <td class="px-6 py-4">
                <div class="flex items-center">
                  <div class="w-10 h-10 rounded-xl bg-gray-100 flex items-center justify-center mr-3 text-gray-500">
                    <DevicePhoneMobileIcon class="w-5 h-5" />
                  </div>
                  <div>
                    <p class="font-medium text-gray-900">{{ device.name || 'Unnamed Device' }}</p>
                    <p class="text-xs text-gray-500">{{ device.algorithm }} / {{ device.model }}</p>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4">
                <code class="px-2 py-1 bg-gray-100 rounded text-xs text-gray-600 font-mono">{{ device.deviceToken }}</code>
              </td>
              <td class="px-6 py-4">
                <span 
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                  :class="device.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                >
                  <span class="w-1.5 h-1.5 rounded-full mr-1.5" :class="device.status === 'ACTIVE' ? 'bg-green-500' : 'bg-red-500'"></span>
                  {{ device.status === 'ACTIVE' ? 'Active' : 'Inactive' }}
                </span>
              </td>
              <td class="px-6 py-4 text-right">
                <div class="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                  <button 
                    @click="handleEdit(device)"
                    class="p-2 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                    title="Edit"
                  >
                    <PencilIcon class="w-4 h-4" />
                  </button>
                  <button 
                    v-if="device.status === 'ACTIVE'"
                    @click="toggleStatus(device)"
                    class="p-2 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                    title="Deactivate"
                  >
                    <StopIcon class="w-4 h-4" />
                  </button>
                  <button 
                    v-else
                    @click="toggleStatus(device)"
                    class="p-2 text-gray-400 hover:text-green-600 hover:bg-green-50 rounded-lg transition-colors"
                    title="Activate"
                  >
                    <PlayIcon class="w-4 h-4" />
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/20 backdrop-blur-sm" @click="showModal = false"></div>
      <div class="bg-white rounded-3xl shadow-2xl w-full max-w-lg relative z-10 overflow-hidden animate-in fade-in zoom-in duration-200">
        <div class="p-6 border-b border-gray-100 flex items-center justify-between">
          <h3 class="text-lg font-bold text-gray-900">{{ isEdit ? 'Edit Device' : 'Add Device' }}</h3>
          <button @click="showModal = false" class="text-gray-400 hover:text-gray-600">
            <XMarkIcon class="w-6 h-6" />
          </button>
        </div>
        
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Device Token</label>
            <input v-model="form.deviceToken" :disabled="isEdit" type="text" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all disabled:opacity-50">
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Device Name</label>
            <input v-model="form.name" type="text" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Device Key</label>
            <input v-model="form.deviceKey" type="text" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Algorithm</label>
              <input v-model="form.algorithm" placeholder="AES" type="text" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Model</label>
              <input v-model="form.model" placeholder="CBC" type="text" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
            </div>
          </div>
        </div>

        <div class="p-6 bg-gray-50 flex justify-end gap-3">
          <button @click="showModal = false" class="px-4 py-2 text-gray-600 font-medium hover:bg-gray-200 rounded-xl transition-colors">Cancel</button>
          <button @click="handleSubmit" class="px-4 py-2 bg-black text-white font-medium rounded-xl hover:bg-gray-800 transition-colors shadow-lg shadow-black/10">Save Device</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { 
  DevicePhoneMobileIcon, 
  PlusIcon, 
  ArrowPathIcon, 
  MagnifyingGlassIcon,
  CheckCircleIcon,
  XCircleIcon,
  PencilIcon,
  StopIcon,
  PlayIcon,
  XMarkIcon
} from '@heroicons/vue/24/outline'
import { getAllDevices, saveDevice, activateDevice, deactivateDevice } from '../api/device'

const loading = ref(false)
const devices = ref([])
const searchQuery = ref('')
const statusFilter = ref('')
const showModal = ref(false)
const isEdit = ref(false)

const form = reactive({
  deviceToken: '',
  name: '',
  deviceKey: '',
  algorithm: 'AES',
  model: 'CBC',
  padding: 'PKCS7Padding',
  encodeKey: '',
  iv: ''
})

const stats = computed(() => ({
  total: devices.value.length,
  active: devices.value.filter(d => d.status === 'ACTIVE').length,
  inactive: devices.value.filter(d => d.status !== 'ACTIVE').length
}))

const filteredDevices = computed(() => {
  return devices.value.filter(device => {
    const matchesSearch = (device.name?.toLowerCase().includes(searchQuery.value.toLowerCase()) || 
                          device.deviceToken?.toLowerCase().includes(searchQuery.value.toLowerCase()))
    const matchesStatus = !statusFilter.value || device.status === statusFilter.value
    return matchesSearch && matchesStatus
  })
})

const refreshData = async () => {
  loading.value = true
  try {
    const res = await getAllDevices()
    devices.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    deviceToken: '',
    name: '',
    deviceKey: '',
    algorithm: 'AES',
    model: 'CBC',
    padding: 'PKCS7Padding',
    encodeKey: '',
    iv: ''
  })
  showModal.value = true
}

const handleEdit = (device) => {
  isEdit.value = true
  Object.assign(form, device)
  showModal.value = true
}

const handleSubmit = async () => {
  try {
    await saveDevice(form)
    showModal.value = false
    refreshData()
  } catch (e) {
    console.error(e)
    alert('Failed to save device')
  }
}

const toggleStatus = async (device) => {
  if (!confirm(`Are you sure you want to ${device.status === 'ACTIVE' ? 'deactivate' : 'activate'} this device?`)) return
  
  try {
    if (device.status === 'ACTIVE') {
      await deactivateDevice(device.deviceToken)
    } else {
      await activateDevice(device.deviceToken)
    }
    refreshData()
  } catch (e) {
    console.error(e)
    alert('Failed to update status')
  }
}

onMounted(() => {
  refreshData()
})
</script>
