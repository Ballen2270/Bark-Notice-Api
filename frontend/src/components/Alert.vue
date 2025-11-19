<template>
  <Transition name="alert">
    <div 
      v-if="visible" 
      class="fixed top-4 left-1/2 -translate-x-1/2 z-50 min-w-[320px] max-w-md"
    >
      <div 
        class="bg-white rounded-2xl shadow-2xl border border-gray-100 overflow-hidden backdrop-blur-xl"
        :class="typeClasses"
      >
        <div class="p-4 flex items-start gap-3">
          <!-- Icon -->
          <div class="flex-shrink-0">
            <div 
              class="w-10 h-10 rounded-full flex items-center justify-center"
              :class="iconBgClasses"
            >
              <CheckCircleIcon v-if="type === 'success'" class="w-6 h-6" :class="iconClasses" />
              <XCircleIcon v-else class="w-6 h-6" :class="iconClasses" />
            </div>
          </div>
          
          <!-- Content -->
          <div class="flex-1 pt-1">
            <h4 class="font-semibold text-gray-900 text-sm">{{ title }}</h4>
            <p v-if="message" class="text-sm text-gray-600 mt-0.5">{{ message }}</p>
          </div>
          
          <!-- Close Button -->
          <button 
            @click="close"
            class="flex-shrink-0 text-gray-400 hover:text-gray-600 transition-colors"
          >
            <XMarkIcon class="w-5 h-5" />
          </button>
        </div>
        
        <!-- Progress Bar -->
        <div class="h-1 bg-gray-100">
          <div 
            class="h-full transition-all ease-linear"
            :class="progressClasses"
            :style="{ width: `${progress}%` }"
          ></div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { CheckCircleIcon, XCircleIcon, XMarkIcon } from '@heroicons/vue/24/outline'

const props = defineProps({
  type: {
    type: String,
    default: 'success', // 'success' | 'error'
    validator: (value) => ['success', 'error'].includes(value)
  },
  title: {
    type: String,
    required: true
  },
  message: {
    type: String,
    default: ''
  },
  duration: {
    type: Number,
    default: 3000 // milliseconds
  },
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'close'])

const visible = ref(props.modelValue)
const progress = ref(100)
let timer = null
let progressTimer = null

const typeClasses = computed(() => {
  return props.type === 'success' 
    ? 'border-l-4 border-l-green-500' 
    : 'border-l-4 border-l-red-500'
})

const iconBgClasses = computed(() => {
  return props.type === 'success' 
    ? 'bg-green-100' 
    : 'bg-red-100'
})

const iconClasses = computed(() => {
  return props.type === 'success' 
    ? 'text-green-600' 
    : 'text-red-600'
})

const progressClasses = computed(() => {
  return props.type === 'success' 
    ? 'bg-green-500' 
    : 'bg-red-500'
})

const close = () => {
  visible.value = false
  emit('update:modelValue', false)
  emit('close')
  clearTimers()
}

const clearTimers = () => {
  if (timer) clearTimeout(timer)
  if (progressTimer) clearInterval(progressTimer)
}

const startTimer = () => {
  clearTimers()
  progress.value = 100
  
  // Progress bar animation
  const interval = 50
  const decrement = (interval / props.duration) * 100
  progressTimer = setInterval(() => {
    progress.value -= decrement
    if (progress.value <= 0) {
      clearInterval(progressTimer)
    }
  }, interval)
  
  // Auto close
  timer = setTimeout(() => {
    close()
  }, props.duration)
}

watch(() => props.modelValue, (newVal) => {
  visible.value = newVal
  if (newVal) {
    startTimer()
  }
})

onMounted(() => {
  if (visible.value) {
    startTimer()
  }
})
</script>

<style scoped>
.alert-enter-active {
  animation: slideDown 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.alert-leave-active {
  animation: slideUp 0.2s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translate(-50%, -100%);
  }
  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
}

@keyframes slideUp {
  from {
    opacity: 1;
    transform: translate(-50%, 0);
  }
  to {
    opacity: 0;
    transform: translate(-50%, -100%);
  }
}
</style>
