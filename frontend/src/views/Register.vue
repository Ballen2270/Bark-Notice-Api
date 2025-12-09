<template>
  <div class="min-h-screen flex items-center justify-center bg-apple-gray relative overflow-hidden">
    <!-- Background blobs (same as Login.vue) -->
    <div class="absolute top-[-20%] left-[-10%] w-[500px] h-[500px] bg-blue-400/20 rounded-full blur-[100px]"></div>
    <div class="absolute bottom-[-20%] right-[-10%] w-[500px] h-[500px] bg-purple-400/20 rounded-full blur-[100px]"></div>

    <!-- Alert Component -->
    <Alert 
      v-model="alert.show" 
      :type="alert.type" 
      :title="alert.title" 
      :message="alert.message" 
    />

    <div class="w-full max-w-md p-4 sm:p-8 relative z-10">
      <div class="bg-white/70 backdrop-blur-2xl rounded-3xl shadow-2xl border border-white/50 p-6 sm:p-8">
        <div class="text-center mb-8">
          <div class="w-16 h-16 bg-black text-white rounded-2xl flex items-center justify-center mx-auto mb-4 shadow-lg">
            <span class="text-2xl font-bold">B</span>
          </div>
          <h1 class="text-2xl font-bold text-gray-900">System Initialization</h1>
          <p class="text-gray-500 mt-2 text-sm">Create the first admin account</p>
        </div>

        <form @submit.prevent="handleRegister" class="space-y-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Username</label>
            <input 
              v-model="form.username"
              type="text" 
              required
              class="w-full px-4 py-3 rounded-xl bg-gray-50/50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none text-gray-900 placeholder-gray-400"
              placeholder="Enter admin username"
            >
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Password</label>
            <input 
              v-model="form.password"
              type="password" 
              required
              class="w-full px-4 py-3 rounded-xl bg-gray-50/50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none text-gray-900 placeholder-gray-400"
              placeholder="Enter admin password"
            >
          </div>

           <div>
            <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Confirm Password</label>
            <input 
              v-model="confirmPassword"
              type="password" 
              required
              class="w-full px-4 py-3 rounded-xl bg-gray-50/50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none text-gray-900 placeholder-gray-400"
              placeholder="Confirm password"
            >
          </div>

          <button 
            type="submit" 
            :disabled="loading"
            class="w-full bg-black text-white py-3.5 rounded-xl font-semibold hover:bg-gray-800 active:scale-[0.98] transition-all disabled:opacity-70 disabled:cursor-not-allowed shadow-lg shadow-black/10"
          >
            <span v-if="loading">Initializing...</span>
            <span v-else>Create System</span>
          </button>
        </form>
      </div>
      
      <p class="text-center text-gray-500 text-xs mt-8">
        &copy; {{ new Date().getFullYear() }} Bark Notice API. All rights reserved.
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { initRegister } from '../api/auth'
import Alert from '../components/Alert.vue'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  username: '',
  password: ''
})
const confirmPassword = ref('')
const loading = ref(false)

const alert = reactive({
  show: false,
  type: 'success',
  title: '',
  message: ''
})

const showAlert = (type, title, message = '') => {
  alert.type = type
  alert.title = title
  alert.message = message
  alert.show = true
}

const handleRegister = async () => {
    if (form.value.password !== confirmPassword.value) {
        showAlert('error', 'Validation Error', 'Passwords do not match')
        return
    }

    loading.value = true
    try {
        const res = await initRegister(form.value)
        if (res.code === '000000') {
            // Register success, automatically login or redirect
            // The API returns token directly
            const { token, userInfo } = res.data
            authStore.setToken(token)
            authStore.setUserInfo(userInfo)
            router.push('/')
        } else {
            showAlert('error', 'Initialization Failed', res.msg || 'Please check your inputs')
        }
    } catch (error) {
        console.error('Initialization error', error)
        showAlert('error', 'System Error', 'Failed to initialize system')
    } finally {
        loading.value = false
    }
}
</script>
