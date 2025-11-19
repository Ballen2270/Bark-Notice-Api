<template>
  <div class="max-w-4xl mx-auto space-y-8">
    <!-- Header -->
    <div>
      <h1 class="text-3xl font-bold text-gray-900">Settings</h1>
      <p class="text-gray-500 mt-1">Manage your account and system users</p>
    </div>

    <!-- Tabs -->
    <div class="flex space-x-1 bg-gray-100 p-1 rounded-xl w-fit">
      <button 
        v-for="tab in tabs" 
        :key="tab.id"
        @click="currentTab = tab.id"
        class="px-6 py-2 rounded-lg text-sm font-medium transition-all"
        :class="currentTab === tab.id ? 'bg-white text-gray-900 shadow-sm' : 'text-gray-500 hover:text-gray-700'"
      >
        {{ tab.name }}
      </button>
    </div>

    <!-- Profile Settings -->
    <div v-if="currentTab === 'profile'" class="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-300">
      <div class="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
        <h3 class="text-lg font-bold text-gray-900 mb-6">Change Password</h3>
        <form @submit.prevent="handleChangePassword" class="space-y-6 max-w-md">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Old Password</label>
            <input 
              v-model="passwordForm.oldPassword"
              type="password" 
              required
              class="w-full px-4 py-3 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
            >
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">New Password</label>
            <input 
              v-model="passwordForm.newPassword"
              type="password" 
              required
              class="w-full px-4 py-3 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
            >
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2 ml-1">Confirm New Password</label>
            <input 
              v-model="passwordForm.confirmPassword"
              type="password" 
              required
              class="w-full px-4 py-3 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 transition-all outline-none"
            >
          </div>
          <button 
            type="submit" 
            :disabled="loading"
            class="w-full bg-black text-white py-3.5 rounded-xl font-semibold hover:bg-gray-800 active:scale-[0.98] transition-all disabled:opacity-70 disabled:cursor-not-allowed shadow-lg shadow-black/10"
          >
            {{ loading ? 'Updating...' : 'Update Password' }}
          </button>
        </form>
      </div>

      <div class="bg-white p-8 rounded-3xl shadow-sm border border-gray-100">
        <h3 class="text-lg font-bold text-gray-900 mb-4">Account Info</h3>
        <div class="space-y-4">
          <div class="flex items-center justify-between py-3 border-b border-gray-50">
            <span class="text-gray-500">Username</span>
            <span class="font-medium text-gray-900">{{ userInfo.username }}</span>
          </div>
          <div class="flex items-center justify-between py-3 border-b border-gray-50">
            <span class="text-gray-500">Role</span>
            <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
              {{ userInfo.role || 'User' }}
            </span>
          </div>
          <div class="flex items-center justify-between py-3">
            <span class="text-gray-500">Last Login</span>
            <span class="font-medium text-gray-900">{{ formatDate(userInfo.lastLoginTime) }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- User Management -->
    <div v-if="currentTab === 'users'" class="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-300">
      <div class="flex justify-end">
        <button 
          @click="showAddUserModal = true"
          class="flex items-center px-4 py-2 bg-black text-white rounded-xl font-medium hover:bg-gray-800 active:scale-95 transition-all shadow-lg shadow-black/10"
        >
          <PlusIcon class="w-5 h-5 mr-2" />
          Add User
        </button>
      </div>

      <div class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full text-left">
            <thead class="bg-gray-50 border-b border-gray-100">
              <tr>
                <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">User</th>
                <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Contact</th>
                <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
                <th class="px-6 py-4 text-xs font-semibold text-gray-500 uppercase tracking-wider text-right">Actions</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-100">
              <tr v-for="user in userList" :key="user.id" class="hover:bg-gray-50/50 transition-colors">
                <td class="px-6 py-4">
                  <div class="flex items-center">
                    <div class="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center mr-3 text-gray-500 font-bold">
                      {{ user.username.charAt(0).toUpperCase() }}
                    </div>
                    <div>
                      <p class="font-medium text-gray-900">{{ user.username }}</p>
                      <p class="text-xs text-gray-500">{{ user.realName || '-' }}</p>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4">
                  <p class="text-sm text-gray-900">{{ user.email || '-' }}</p>
                  <p class="text-xs text-gray-500">{{ user.phone || '-' }}</p>
                </td>
                <td class="px-6 py-4">
                  <span 
                    class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                    :class="user.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                  >
                    <span class="w-1.5 h-1.5 rounded-full mr-1.5" :class="user.status === 'ACTIVE' ? 'bg-green-500' : 'bg-red-500'"></span>
                    {{ user.status === 'ACTIVE' ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td class="px-6 py-4 text-right">
                  <button 
                    v-if="user.status === 'ACTIVE'"
                    @click="toggleUserStatus(user)"
                    class="text-red-600 hover:text-red-800 text-sm font-medium"
                  >
                    Deactivate
                  </button>
                  <button 
                    v-else
                    @click="toggleUserStatus(user)"
                    class="text-green-600 hover:text-green-800 text-sm font-medium"
                  >
                    Activate
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Add User Modal -->
    <div v-if="showAddUserModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div class="absolute inset-0 bg-black/20 backdrop-blur-sm" @click="showAddUserModal = false"></div>
      <div class="bg-white rounded-3xl shadow-2xl w-full max-w-md relative z-10 overflow-hidden animate-in fade-in zoom-in duration-200">
        <div class="p-6 border-b border-gray-100 flex items-center justify-between">
          <h3 class="text-lg font-bold text-gray-900">Add User</h3>
          <button @click="showAddUserModal = false" class="text-gray-400 hover:text-gray-600">
            <XMarkIcon class="w-6 h-6" />
          </button>
        </div>
        
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Username</label>
            <input v-model="newUserForm.username" type="text" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input v-model="newUserForm.password" type="password" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Real Name</label>
            <input v-model="newUserForm.realName" type="text" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input v-model="newUserForm.email" type="email" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Phone</label>
            <input v-model="newUserForm.phone" type="tel" class="w-full px-4 py-2 rounded-xl bg-gray-50 border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all">
          </div>
        </div>

        <div class="p-6 bg-gray-50 flex justify-end gap-3">
          <button @click="showAddUserModal = false" class="px-4 py-2 text-gray-600 font-medium hover:bg-gray-200 rounded-xl transition-colors">Cancel</button>
          <button @click="handleAddUser" class="px-4 py-2 bg-black text-white font-medium rounded-xl hover:bg-gray-800 transition-colors shadow-lg shadow-black/10">Create User</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useAuthStore } from '../stores/auth'
import { changePassword } from '../api/auth'
import { getUserList, activateUser, deactivateUser, registerUser } from '../api/user'
import { PlusIcon, XMarkIcon } from '@heroicons/vue/24/outline'

const authStore = useAuthStore()
const userInfo = computed(() => authStore.userInfo)
const currentTab = ref('profile')
const loading = ref(false)
const userList = ref([])
const showAddUserModal = ref(false)

const tabs = [
  { id: 'profile', name: 'My Profile' },
  { id: 'users', name: 'User Management' }
]

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const newUserForm = reactive({
  username: '',
  password: '',
  realName: '',
  email: '',
  phone: ''
})

const handleChangePassword = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    alert('Passwords do not match')
    return
  }
  
  loading.value = true
  try {
    await changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    alert('Password updated successfully')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (e) {
    console.error(e)
    alert('Failed to update password')
  } finally {
    loading.value = false
  }
}

const fetchUsers = async () => {
  try {
    const res = await getUserList()
    userList.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

const handleAddUser = async () => {
  try {
    await registerUser(newUserForm)
    alert('User created successfully')
    showAddUserModal.value = false
    Object.keys(newUserForm).forEach(key => newUserForm[key] = '')
    fetchUsers()
  } catch (e) {
    console.error(e)
    alert('Failed to create user')
  }
}

const toggleUserStatus = async (user) => {
  if (!confirm(`Are you sure you want to ${user.status === 'ACTIVE' ? 'deactivate' : 'activate'} this user?`)) return
  
  try {
    if (user.status === 'ACTIVE') {
      await deactivateUser(user.id)
    } else {
      await activateUser(user.id)
    }
    fetchUsers()
  } catch (e) {
    console.error(e)
    alert('Failed to update user status')
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString()
}

onMounted(() => {
  if (currentTab.value === 'users') {
    fetchUsers()
  }
})

// Watch tab change to fetch users
import { watch } from 'vue'
watch(currentTab, (val) => {
  if (val === 'users') {
    fetchUsers()
  }
})
</script>
