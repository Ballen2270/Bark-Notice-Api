<template>
  <div class="flex min-h-screen bg-apple-gray">
    <!-- Mobile Header -->
    <div class="lg:hidden fixed top-0 left-0 right-0 h-16 bg-white/80 backdrop-blur-xl border-b border-gray-200 z-20 flex items-center justify-between px-4">
      <div class="flex items-center gap-2">
        <span class="w-8 h-8 bg-black text-white rounded-lg flex items-center justify-center font-bold">B</span>
        <h1 class="text-lg font-bold text-gray-900">Bark Admin</h1>
      </div>
      <button @click="isSidebarOpen = !isSidebarOpen" class="p-2 rounded-lg hover:bg-gray-100 text-gray-600">
        <Bars3Icon v-if="!isSidebarOpen" class="w-6 h-6" />
        <XMarkIcon v-else class="w-6 h-6" />
      </button>
    </div>

    <!-- Mobile Backdrop -->
    <div 
      v-if="isSidebarOpen" 
      @click="isSidebarOpen = false"
      class="lg:hidden fixed inset-0 bg-black/20 backdrop-blur-sm z-30"
    ></div>

    <!-- Sidebar -->
    <aside 
      class="fixed inset-y-0 left-0 w-64 bg-white/80 backdrop-blur-xl border-r border-gray-200 z-40 transform transition-transform duration-300 ease-in-out lg:translate-x-0"
      :class="isSidebarOpen ? 'translate-x-0' : '-translate-x-full'"
    >
      <div class="p-6 hidden lg:block">
        <h1 class="text-xl font-bold text-gray-900 flex items-center gap-2">
          <span class="w-8 h-8 bg-black text-white rounded-lg flex items-center justify-center">B</span>
          Bark Admin
        </h1>
      </div>
      <div class="p-6 lg:hidden">
        <h2 class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Menu</h2>
      </div>
      <nav class="px-4 space-y-1">
        <router-link 
          v-for="item in navigation" 
          :key="item.path"
          :to="item.path" 
          @click="isSidebarOpen = false"
          class="flex items-center px-4 py-2.5 text-sm font-medium rounded-xl text-gray-600 hover:bg-gray-100 hover:text-gray-900 transition-colors" 
          active-class="bg-gray-100 text-gray-900"
        >
          <component :is="item.icon" class="w-5 h-5 mr-3" />
          {{ item.name }}
        </router-link>
      </nav>
    </aside>

    <!-- Main Content -->
    <main class="flex-1 lg:ml-64 p-4 pt-20 lg:p-8 lg:pt-8 w-full">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { 
  HomeIcon, 
  DevicePhoneMobileIcon, 
  BellIcon, 
  DocumentTextIcon, 
  Cog6ToothIcon,
  Bars3Icon,
  XMarkIcon
} from '@heroicons/vue/24/outline'

const isSidebarOpen = ref(false)

const navigation = [
  { name: 'Dashboard', path: '/', icon: HomeIcon },
  { name: 'Devices', path: '/devices', icon: DevicePhoneMobileIcon },
  { name: 'Notices', path: '/notices', icon: BellIcon },
  { name: 'Logs', path: '/logs', icon: DocumentTextIcon },
  { name: 'Settings', path: '/settings', icon: Cog6ToothIcon },
]
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
