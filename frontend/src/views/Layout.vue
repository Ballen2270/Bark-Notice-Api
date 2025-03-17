<template>
  <div class="layout-container">
    <el-container>
      <!-- 移动端菜单按钮 -->
      <div class="mobile-header" v-if="isMobile">
        <el-button type="text" @click="toggleMenu">
          <el-icon><Menu /></el-icon>
        </el-button>
        <h3>{{ currentTitle }}</h3>
      </div>
      
      <!-- 侧边栏 -->
      <el-aside :width="isMobile ? (isCollapse ? '0' : '100%') : '220px'" 
                :class="{'mobile-aside': isMobile, 'is-collapsed': isCollapse}">
        <div class="logo">
          <h2>Bark通知管理</h2>
          <el-button v-if="!isMobile" type="text" class="collapse-btn" @click="toggleMenu">
            <el-icon><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
          </el-button>
        </div>
        <el-menu
          router
          :default-active="activeMenu"
          class="el-menu-vertical"
          :collapse="isCollapse && !isMobile"
          background-color="#ffffff"
          text-color="#303133"
          active-text-color="#007aff">
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          <el-menu-item index="/devices">
            <el-icon><Cellphone /></el-icon>
            <span>设备管理</span>
          </el-menu-item>
          <el-menu-item index="/notices">
            <el-icon><ChatDotRound /></el-icon>
            <span>通知管理</span>
          </el-menu-item>
          <el-menu-item index="/log-dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>日志数据</span>
          </el-menu-item>
          <el-menu-item index="/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          <el-divider />
          <el-menu-item @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出登录</span>
          </el-menu-item>
        </el-menu>
        <!-- 移动端菜单遮罩 -->
        <div v-if="isMobile && !isCollapse" class="menu-overlay" @click="toggleMenu"></div>
      </el-aside>
      
      <el-container>
        <el-header v-if="!isMobile">
          <div class="header-title">
            <h3>{{ currentTitle }}</h3>
          </div>
        </el-header>
        
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed, ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)
const isMobile = ref(false)

// 检查是否是移动设备
const checkIsMobile = () => {
  isMobile.value = window.innerWidth < 768
  if (isMobile.value) {
    isCollapse.value = true
  }
}

// 切换菜单展开/收起
const toggleMenu = () => {
  isCollapse.value = !isCollapse.value
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 清除Token和用户信息
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    
    // 跳转到登录页
    router.push('/login')
    
    ElMessage.success('已退出登录')
  }).catch(() => {})
}

const activeMenu = computed(() => {
  return route.path
})

const currentTitle = computed(() => {
  return route.meta.title || 'Bark通知管理系统'
})

// 监听窗口大小变化
onMounted(() => {
  checkIsMobile()
  window.addEventListener('resize', checkIsMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkIsMobile)
})

// 导入需要的图标
const { 
  Odometer, 
  Cellphone, 
  ChatDotRound, 
  Menu, 
  Fold, 
  Expand,
  User,
  SwitchButton,
  DataAnalysis
} = ElementPlusIconsVue
</script>

<style scoped>
.layout-container {
  height: 100%;
  background-color: #f7f8fa;
}

.el-container {
  height: 100%;
}

.el-aside {
  background-color: #ffffff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  position: relative;
  transition: width 0.3s;
  z-index: 10;
}

.is-collapsed {
  width: 64px;
}

.mobile-aside {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  z-index: 2000;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  border-bottom: 1px solid #ebeef5;
}

.logo h2 {
  font-size: 18px;
  color: #007aff;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.collapse-btn {
  padding: 0;
  font-size: 16px;
}

.el-menu {
  border-right: none;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 100%;
}

.el-header {
  background-color: #ffffff;
  color: #333;
  line-height: 60px;
  border-bottom: 1px solid #ebeef5;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  padding: 0 20px;
}

.header-title {
  display: flex;
  align-items: center;
  height: 100%;
}

.header-title h3 {
  font-size: 16px;
  font-weight: 500;
  margin: 0;
  color: #303133;
}

.el-main {
  background-color: #f7f8fa;
  padding: 20px;
}

/* 移动端样式 */
.mobile-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 50px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  padding: 0 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.mobile-header h3 {
  margin: 0;
  font-size: 16px;
  margin-left: 20px;
}

.menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: -1;
}

/* 移动端主内容区适配 */
@media (max-width: 767px) {
  .el-main {
    padding-top: 70px;
    padding-left: 10px;
    padding-right: 10px;
  }
}
</style> 