<template>
  <div class="min-h-screen flex items-center justify-center bg-apple-gray relative overflow-hidden">
    <!-- Background blobs -->
    <div class="absolute top-[-20%] left-[-10%] w-[500px] h-[500px] bg-blue-400/20 rounded-full blur-[100px]"></div>
    <div class="absolute bottom-[-20%] right-[-10%] w-[500px] h-[500px] bg-purple-400/20 rounded-full blur-[100px]"></div>

    <div class="w-full max-w-md p-4 sm:p-8 relative z-10">
      <div class="bg-white/70 backdrop-blur-2xl rounded-3xl shadow-2xl border border-white/50 p-6 sm:p-8">
        <div class="text-center mb-8">
          <div class="w-16 h-16 bg-black text-white rounded-2xl flex items-center justify-center mx-auto mb-4 shadow-lg">
            <span class="text-2xl font-bold">B</span>
          </div>
          <h1 class="text-2xl font-bold text-gray-900">Sign in to Bark</h1>
          <p class="text-gray-500 mt-2 text-sm">Welcome back to your dashboard</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-6">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Username</label>
            <input 
              v-model="form.username"
              type="text" 
              required
              class="w-full px-4 py-3 rounded-xl bg-gray-50/50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none text-gray-900 placeholder-gray-400"
              placeholder="Enter your username"
            >
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Password</label>
            <input 
              v-model="form.password"
              type="password" 
              required
              class="w-full px-4 py-3 rounded-xl bg-gray-50/50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none text-gray-900 placeholder-gray-400"
              placeholder="Enter your password"
            >
          </div>

          <div v-if="captchaImage" class="space-y-2">
             <label class="block text-sm font-medium text-gray-700 ml-1">Captcha</label>
             <div class="flex gap-4">
               <input 
                 v-model="form.captcha"
                 type="text" 
                 required
                 class="w-full px-4 py-3 rounded-xl bg-gray-50/50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none text-gray-900 placeholder-gray-400"
                 placeholder="Code"
               >
               <div 
                 class="h-[46px] w-32 bg-gray-200 rounded-xl overflow-hidden cursor-pointer flex-shrink-0"
                 @click="refreshCaptcha"
               >
                 <img :src="captchaImage" class="w-full h-full object-cover" alt="Captcha" />
               </div>
             </div>
          </div>

          <button 
            type="submit" 
            :disabled="loading"
            class="w-full bg-black text-white py-3.5 rounded-xl font-semibold hover:bg-gray-800 active:scale-[0.98] transition-all disabled:opacity-70 disabled:cursor-not-allowed shadow-lg shadow-black/10"
          >
            <span v-if="loading">Signing in...</span>
            <span v-else>Sign In</span>
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
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getCaptcha } from '../api/auth'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  username: '',
  password: '',
  captcha: '',
  captchaKey: ''
})

const loading = ref(false)
const captchaImage = ref('')

const refreshCaptcha = async () => {
  try {
    const res = await getCaptcha()
    if (res.code === '000000') {
      captchaImage.value = res.data.image
      form.value.captchaKey = res.data.key
    }
  } catch (error) {
    console.error('Failed to load captcha', error)
  }
}

const handleLogin = async () => {
  loading.value = true
  try {
    await authStore.login(form.value)
    router.push('/')
  } catch (error) {
    // Error handled in store/request
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refreshCaptcha()
})
</script>
