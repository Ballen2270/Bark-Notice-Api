<template>
  <div class="max-w-4xl mx-auto space-y-8">
    <!-- Header -->
    <div>
      <h1 class="text-3xl font-bold text-gray-900">Send Notice</h1>
      <p class="text-gray-500 mt-1">Push notifications to your devices</p>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
      <!-- Form Section -->
      <div class="lg:col-span-2 space-y-6">
        <div class="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
          <form @submit.prevent="handleSubmit" class="space-y-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Title</label>
              <input 
                v-model="form.title"
                type="text" 
                required
                class="w-full px-4 py-3 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
                placeholder="Notification Title"
              >
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Body</label>
              <textarea 
                v-model="form.body"
                required
                rows="4"
                class="w-full px-4 py-3 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none resize-none"
                placeholder="Notification Content"
              ></textarea>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Group (Optional)</label>
              <input 
                v-model="form.group"
                type="text" 
                class="w-full px-4 py-3 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
                placeholder="e.g. Finance, Alerts"
              >
            </div>

            <!-- Advanced Options Toggle -->
            <div>
              <button 
                type="button"
                @click="showAdvanced = !showAdvanced"
                class="flex items-center text-sm text-blue-600 font-medium hover:text-blue-700"
              >
                <span class="mr-1">{{ showAdvanced ? 'Hide' : 'Show' }} Advanced Options</span>
                <ChevronDownIcon class="w-4 h-4 transition-transform" :class="{ 'rotate-180': showAdvanced }" />
              </button>
            </div>

            <!-- Advanced Options -->
            <div v-show="showAdvanced" class="space-y-6 pt-2 animate-in slide-in-from-top-2 duration-200">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">URL (Optional)</label>
                <input 
                  v-model="form.url"
                  type="url" 
                  class="w-full px-4 py-3 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
                  placeholder="https://example.com"
                >
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Icon URL (Optional)</label>
                <input 
                  v-model="form.icon"
                  type="url" 
                  class="w-full px-4 py-3 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
                  placeholder="https://example.com/icon.png"
                >
              </div>
            </div>

            <div class="pt-4 flex items-center gap-4">
              <button 
                type="submit" 
                :disabled="loading"
                class="flex-1 bg-black text-white py-3.5 rounded-xl font-semibold hover:bg-gray-800 active:scale-[0.98] transition-all disabled:opacity-70 disabled:cursor-not-allowed shadow-lg shadow-black/10 flex items-center justify-center"
              >
                <PaperAirplaneIcon v-if="!loading" class="w-5 h-5 mr-2" />
                <ArrowPathIcon v-else class="w-5 h-5 mr-2 animate-spin" />
                {{ loading ? 'Sending...' : 'Send Notice' }}
              </button>
              <button 
                type="button"
                @click="resetForm"
                class="px-6 py-3.5 rounded-xl font-semibold text-gray-600 hover:bg-gray-100 transition-colors"
              >
                Reset
              </button>
            </div>
          </form>
        </div>
      </div>

      <!-- Sidebar / Templates -->
      <div class="space-y-6">
        <div class="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
          <h3 class="text-lg font-bold text-gray-900 mb-4">Quick Templates</h3>
          <div class="space-y-3">
            <button 
              v-for="(template, index) in templates" 
              :key="index"
              @click="applyTemplate(template)"
              class="w-full text-left p-4 rounded-2xl bg-gray-50 hover:bg-blue-50 hover:text-blue-700 transition-colors group"
            >
              <div class="font-medium text-gray-900 group-hover:text-blue-700">{{ template.title }}</div>
              <div class="text-xs text-gray-500 mt-1 truncate group-hover:text-blue-500">{{ template.body }}</div>
            </button>
          </div>
        </div>

        <div class="bg-blue-50 p-6 rounded-3xl border border-blue-100">
          <div class="flex items-start gap-4">
            <div class="p-3 bg-blue-100 rounded-xl text-blue-600">
              <InformationCircleIcon class="w-6 h-6" />
            </div>
            <div>
              <h4 class="font-bold text-blue-900">Did you know?</h4>
              <p class="text-sm text-blue-700 mt-1 leading-relaxed">
                You can group notifications by setting the same group name. They will be stacked together in the notification center.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { 
  PaperAirplaneIcon, 
  ArrowPathIcon, 
  ChevronDownIcon,
  InformationCircleIcon
} from '@heroicons/vue/24/outline'
import { sendNoticePost } from '../api/notice'

const loading = ref(false)
const showAdvanced = ref(false)

const form = reactive({
  title: '',
  body: '',
  group: '',
  url: '',
  icon: ''
})

const templates = [
  { title: 'Test Notification', body: 'This is a test notification from Bark Admin.' },
  { title: 'Server Alert', body: 'Server CPU usage is high (90%). Please check.', group: 'Alerts' },
  { title: 'Daily Report', body: 'Your daily report is ready to view.', group: 'Reports' }
]

const handleSubmit = async () => {
  loading.value = true
  try {
    await sendNoticePost(form)
    // Show success message (using native alert for now or implement toast later)
    alert('Notice sent successfully!')
    resetForm()
  } catch (e) {
    console.error(e)
    alert('Failed to send notice.')
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  Object.assign(form, {
    title: '',
    body: '',
    group: '',
    url: '',
    icon: ''
  })
}

const applyTemplate = (template) => {
  Object.assign(form, template)
}
</script>
