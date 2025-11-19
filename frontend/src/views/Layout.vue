<template>
  <div class="flex min-h-screen bg-apple-gray">
    <!-- Sidebar -->
    <aside class="w-64 bg-white/80 backdrop-blur-xl border-r border-gray-200 fixed h-full z-10">
      <div class="p-6">
        <h1 class="text-xl font-bold text-gray-900 flex items-center gap-2">
          <span class="w-8 h-8 bg-black text-white rounded-lg flex items-center justify-center">B</span>
          Bark Admin
        </h1>
      </div>
      <nav class="mt-6 px-4 space-y-1">
        <router-link to="/" class="flex items-center px-4 py-2.5 text-sm font-medium rounded-xl text-gray-600 hover:bg-gray-100 hover:text-gray-900 transition-colors" active-class="bg-gray-100 text-gray-900">
          Dashboard
        </router-link>
        <router-link to="/devices" class="flex items-center px-4 py-2.5 text-sm font-medium rounded-xl text-gray-600 hover:bg-gray-100 hover:text-gray-900 transition-colors" active-class="bg-gray-100 text-gray-900">
          Devices
        </router-link>
        <router-link to="/notices" class="flex items-center px-4 py-2.5 text-sm font-medium rounded-xl text-gray-600 hover:bg-gray-100 hover:text-gray-900 transition-colors" active-class="bg-gray-100 text-gray-900">
          Notices
        </router-link>
        <router-link to="/logs" class="flex items-center px-4 py-2.5 text-sm font-medium rounded-xl text-gray-600 hover:bg-gray-100 hover:text-gray-900 transition-colors" active-class="bg-gray-100 text-gray-900">
          Logs
        </router-link>
        <router-link to="/settings" class="flex items-center px-4 py-2.5 text-sm font-medium rounded-xl text-gray-600 hover:bg-gray-100 hover:text-gray-900 transition-colors" active-class="bg-gray-100 text-gray-900">
          Settings
        </router-link>
      </nav>
    </aside>

    <!-- Main Content -->
    <main class="flex-1 ml-64 p-8">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

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
