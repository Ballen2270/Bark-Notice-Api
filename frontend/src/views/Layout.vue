<template>
  <div class="layout-container">
    <el-container>
      <!-- 移动端顶部导航栏 -->
      <div class="mobile-header" v-if="isMobile">
        <el-button class="menu-btn" @click="toggleMenu" text>
          <el-icon size="20"><Menu /></el-icon>
        </el-button>
        <h3 class="mobile-title">{{ currentTitle }}</h3>
        <div class="header-actions">
          <el-button class="user-btn" @click="$router.push('/dashboard')" text>
            <el-icon size="18"><HomeFilled /></el-icon>
          </el-button>
        </div>
      </div>
      
      <!-- 侧边栏 -->
      <el-aside 
        :width="isMobile ? (isCollapse ? '0' : '280px') : (isCollapse ? '64px' : '260px')" 
        :class="{
          'mobile-aside': isMobile, 
          'is-collapsed': isCollapse,
          'menu-open': !isCollapse
        }"
      >
        <div class="logo" v-if="!isCollapse">
          <div class="logo-icon">
            <el-icon size="28" color="var(--primary-color)"><Notification /></el-icon>
          </div>
          <div class="logo-text">
            <h2>Bark通知管理</h2>
            <span class="logo-subtitle">智能通知推送系统</span>
          </div>
        </div>
        
        <div class="logo-collapsed" v-else>
          <el-icon size="24" color="var(--primary-color)"><Notification /></el-icon>
        </div>
        
        <el-menu
          router
          :default-active="activeMenu"
          class="nav-menu"
          :collapse="isCollapse && !isMobile"
          :unique-opened="true"
        >
          <el-menu-item index="/dashboard" class="nav-item">
            <el-icon class="nav-icon"><Odometer /></el-icon>
            <template #title>
              <span class="nav-text">仪表盘</span>
              <span class="nav-desc">数据概览</span>
            </template>
          </el-menu-item>
          
          <el-menu-item index="/devices" class="nav-item">
            <el-icon class="nav-icon"><Cellphone /></el-icon>
            <template #title>
              <span class="nav-text">设备管理</span>
              <span class="nav-desc">设备配置</span>
            </template>
          </el-menu-item>
          
          <el-menu-item index="/notices" class="nav-item">
            <el-icon class="nav-icon"><ChatDotRound /></el-icon>
            <template #title>
              <span class="nav-text">通知管理</span>
              <span class="nav-desc">发送通知</span>
            </template>
          </el-menu-item>
          
          <el-menu-item index="/log-dashboard" class="nav-item">
            <el-icon class="nav-icon"><DataAnalysis /></el-icon>
            <template #title>
              <span class="nav-text">日志数据</span>
              <span class="nav-desc">统计分析</span>
            </template>
          </el-menu-item>
          
          <el-menu-item index="/users" class="nav-item">
            <el-icon class="nav-icon"><User /></el-icon>
            <template #title>
              <span class="nav-text">用户管理</span>
              <span class="nav-desc">权限设置</span>
            </template>
          </el-menu-item>
          
          <div class="menu-divider"></div>
          
          <el-menu-item @click="handleLogout" class="nav-item logout-item">
            <el-icon class="nav-icon"><SwitchButton /></el-icon>
            <template #title>
              <span class="nav-text">退出登录</span>
              <span class="nav-desc">安全退出</span>
            </template>
          </el-menu-item>
        </el-menu>
        
        <!-- 移动端菜单遮罩 -->
        <transition name="fade">
          <div v-if="isMobile && !isCollapse" class="menu-overlay" @click="toggleMenu"></div>
        </transition>
      </el-aside>
      
      <el-container class="main-container">
        <el-header v-if="!isMobile" class="desktop-header">
          <div class="header-content">
            <div class="header-left">
              <el-button 
                v-if="!isMobile" 
                @click="toggleMenu" 
                text 
                class="collapse-toggle"
              >
                <el-icon size="18">
                  <Fold v-if="!isCollapse" />
                  <Expand v-else />
                </el-icon>
              </el-button>
              <h3 class="page-title">{{ currentTitle }}</h3>
            </div>
            
            <div class="header-right">
              <el-button @click="$router.push('/dashboard')" text class="header-btn">
                <el-icon><HomeFilled /></el-icon>
                <span>首页</span>
              </el-button>
            </div>
          </div>
        </el-header>
        
        <el-main class="main-content">
          <transition name="slide-fade" mode="out-in">
            <router-view />
          </transition>
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
  DataAnalysis,
  Notification,
  HomeFilled
} = ElementPlusIconsVue
</script>

<style scoped>
.layout-container {
  height: 100%;
  background: var(--bg-secondary);
}

.el-container {
  height: 100%;
}

/* 侧边栏样式 */
.el-aside {
  background: var(--bg-primary);
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.08);
  position: relative;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1000;
  overflow: visible;
}

.mobile-aside {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  z-index: 2000;
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
  transform: translateX(-100%);
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: transform;
}

.mobile-aside.menu-open {
  transform: translateX(0);
}

/* Logo样式 */
.logo {
  height: 80px;
  display: flex;
  align-items: center;
  padding: 0 var(--spacing-lg);
  border-bottom: 1px solid var(--border-light);
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
  position: relative;
  overflow: hidden;
}

.logo::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.05) 0%, rgba(0, 122, 255, 0.02) 100%);
}

.logo-icon {
  position: relative;
  z-index: 1;
  margin-right: var(--spacing-sm);
}

.logo-text {
  flex: 1;
  position: relative;
  z-index: 1;
}

.logo-text h2 {
  font-size: var(--font-size-lg);
  font-weight: 700;
  margin: 0;
  background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.2;
}

.logo-subtitle {
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
  display: block;
  margin-top: 2px;
}

.logo-collapsed {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid var(--border-light);
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
}

/* 导航菜单样式 */
.nav-menu {
  border-right: none !important;
  background: transparent !important;
  padding: var(--spacing-md) 0;
}

.nav-item {
  margin: 0 var(--spacing-md) var(--spacing-xs) !important;
  border-radius: var(--radius-medium) !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  position: relative;
  overflow: hidden;
  height: 60px !important;
  display: flex !important;
  align-items: center !important;
}

.nav-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%);
  opacity: 0;
  transition: opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 0;
}

.nav-item:hover::before {
  opacity: 0.1;
}

.nav-item.is-active::before {
  opacity: 1;
}

.nav-icon {
  font-size: 20px !important;
  margin-right: var(--spacing-sm) !important;
  color: var(--text-secondary) !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  position: relative;
  z-index: 1;
}

.nav-item:hover .nav-icon {
  color: var(--primary-color) !important;
  transform: scale(1.1) !important;
}

.nav-item.is-active .nav-icon {
  color: white !important;
  transform: scale(1.1) !important;
}

.nav-text {
  font-weight: 600 !important;
  font-size: var(--font-size-md) !important;
  color: var(--text-primary) !important;
  position: relative;
  z-index: 1;
  display: block;
  line-height: 1.2;
}

.nav-desc {
  font-size: var(--font-size-xs) !important;
  color: var(--text-secondary) !important;
  display: block;
  margin-top: 2px;
  opacity: 0;
  transform: translateY(-10px);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  z-index: 1;
}

.nav-item:hover .nav-desc {
  opacity: 1;
  transform: translateY(0);
}

.nav-item.is-active .nav-text {
  color: white !important;
}

.nav-item.is-active .nav-desc {
  color: rgba(255, 255, 255, 0.8) !important;
  opacity: 1;
  transform: translateY(0);
}

.logout-item {
  margin-top: var(--spacing-lg) !important;
  border-top: 1px solid var(--border-light) !important;
  padding-top: var(--spacing-lg) !important;
}

.menu-divider {
  height: 1px;
  background: var(--border-light);
  margin: var(--spacing-lg) var(--spacing-md) var(--spacing-md);
}

/* 桌面端头部 */
.desktop-header {
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-light);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 0;
  height: 70px !important;
  line-height: 70px;
}

.header-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--spacing-lg);
  max-width: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.collapse-toggle {
  padding: var(--spacing-sm);
  border-radius: var(--radius-small);
  transition: all 0.2s;
}

.collapse-toggle:hover {
  background: var(--bg-secondary);
  transform: scale(1.05);
}

.page-title {
  font-size: var(--font-size-lg);
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.header-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  padding: var(--spacing-xs) var(--spacing-sm);
  border-radius: var(--radius-small);
  transition: all 0.2s;
}

.header-btn:hover {
  background: var(--bg-secondary);
  transform: scale(1.02);
}

/* 主内容区 */
.main-container {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: transform;
}

.main-content {
  background: var(--bg-secondary);
  padding: var(--spacing-lg);
  min-height: calc(100vh - 70px);
  position: relative;
}

/* 移动端头部 */
.mobile-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--spacing-md);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.08);
  z-index: 1000;
  border-bottom: 1px solid var(--border-light);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: transform;
}

.mobile-aside.menu-open ~ .mobile-header {
  transform: translateX(280px);
}

.menu-btn {
  padding: var(--spacing-sm) !important;
  border-radius: var(--radius-small) !important;
  transition: all 0.2s !important;
}

.menu-btn:hover {
  background: var(--bg-secondary) !important;
  transform: scale(1.1) !important;
}

.mobile-title {
  margin: 0;
  font-size: var(--font-size-md);
  font-weight: 600;
  color: var(--text-primary);
  flex: 1;
  text-align: center;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.user-btn {
  padding: var(--spacing-sm) !important;
  border-radius: var(--radius-small) !important;
  transition: all 0.2s !important;
}

.user-btn:hover {
  background: var(--bg-secondary) !important;
  transform: scale(1.1) !important;
}

/* 菜单遮罩 */
.menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(2px);
  z-index: 1999;
  transition: opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  will-change: opacity;
}

/* 移动端适配 */
@media (max-width: 767px) {
  .mobile-aside {
    width: 280px !important;
    height: 100vh;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .logo {
    height: 70px;
    padding: 0 var(--spacing-md);
    position: relative;
    overflow: hidden;
  }

  .logo::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(0, 122, 255, 0.05) 0%, rgba(0, 122, 255, 0.02) 100%);
  }

  .logo-text h2 {
    font-size: var(--font-size-md);
    line-height: 1.2;
  }

  .logo-subtitle {
    font-size: 10px;
    line-height: 1.2;
  }

  .nav-item {
    height: 56px !important;
    margin: 0 var(--spacing-sm) var(--spacing-xs) !important;
    border-radius: var(--radius-medium) !important;
    position: relative;
    overflow: hidden;
  }

  .nav-item::after {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
    transition: left 0.5s ease;
    pointer-events: none;
  }

  .nav-item:active::after {
    left: 100%;
  }

  .nav-icon {
    font-size: 18px !important;
    transition: transform 0.2s ease !important;
  }

  .nav-item:active .nav-icon {
    transform: scale(1.1) !important;
  }

  .nav-text {
    font-size: var(--font-size-sm) !important;
    line-height: 1.2;
  }

  .nav-desc {
    font-size: 10px !important;
    line-height: 1.1;
  }

  .nav-item:active {
    transform: scale(0.98);
    transition: transform 0.1s ease;
  }

  .main-content {
    padding: var(--spacing-md) var(--spacing-sm);
    padding-top: calc(60px + var(--spacing-md));
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    will-change: transform;
    min-height: calc(100vh - 60px);
  }

  .mobile-aside.menu-open ~ .main-container .main-content {
    transform: translateX(280px);
  }

  .mobile-header {
    height: 60px;
    padding: 0 var(--spacing-sm);
    backdrop-filter: blur(20px);
    -webkit-backdrop-filter: blur(20px);
  }

  .mobile-header::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(248, 248, 248, 0.98) 100%);
    z-index: -1;
  }

  .mobile-title {
    font-size: var(--font-size-sm);
    line-height: 1.2;
  }

  .menu-btn, .user-btn {
    position: relative;
    overflow: hidden;
  }

  .menu-btn::before, .user-btn::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    border-radius: 50%;
    background: rgba(0, 122, 255, 0.1);
    transform: translate(-50%, -50%);
    transition: width 0.3s, height 0.3s;
  }

  .menu-btn:active::before, .user-btn:active::before {
    width: 60px;
    height: 60px;
  }
}

@media (max-width: 480px) {
  .mobile-aside {
    width: 260px !important;
    box-shadow: 4px 0 25px rgba(0, 0, 0, 0.2);
  }

  .logo {
    padding: 0 var(--spacing-sm);
    height: 65px;
  }

  .logo-text h2 {
    font-size: var(--font-size-sm);
  }

  .logo-subtitle {
    font-size: 9px;
  }

  .nav-item {
    height: 52px !important;
    margin: 0 var(--spacing-xs) var(--spacing-xs) !important;
  }

  .nav-icon {
    font-size: 16px !important;
  }

  .nav-text {
    font-size: var(--font-size-xs) !important;
  }

  .nav-desc {
    font-size: 9px !important;
  }

  .main-content {
    padding: var(--spacing-sm);
    padding-top: calc(60px + var(--spacing-sm));
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }

  .mobile-aside.menu-open ~ .main-container .main-content {
    transform: translateX(260px);
  }

  .mobile-aside.menu-open ~ .mobile-header {
    transform: translateX(260px);
  }

  .mobile-header {
    padding: 0 var(--spacing-sm);
    height: 58px;
  }

  .mobile-title {
    font-size: var(--font-size-xs);
    line-height: 1.2;
  }

  .menu-btn, .user-btn {
    min-width: 44px;
    min-height: 44px;
    border-radius: var(--radius-small);
  }

  .menu-btn:active::before, .user-btn:active::before {
    width: 50px;
    height: 50px;
  }

  .menu-overlay {
    background: rgba(0, 0, 0, 0.4);
  }
}

/* 平板端适配 */
@media (min-width: 768px) and (max-width: 1024px) {
  .el-aside {
    width: 220px !important;
  }
  
  .is-collapsed {
    width: 64px !important;
  }
  
  .logo {
    padding: 0 var(--spacing-md);
  }
  
  .main-content {
    padding: var(--spacing-md);
  }
}

/* 触摸设备优化 */
@media (hover: none) and (pointer: coarse) {
  .nav-item {
    min-height: 48px !important;
    transition: all 0.2s ease !important;
  }

  .nav-item:hover {
    transform: none !important;
  }

  .nav-item:active {
    transform: scale(0.95) !important;
    background: var(--bg-secondary) !important;
  }

  .nav-item.is-active:active {
    background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%) !important;
  }

  .menu-btn, .user-btn, .collapse-toggle, .header-btn {
    min-height: 44px;
    min-width: 44px;
    transition: all 0.2s ease !important;
  }

  .menu-btn:hover, .user-btn:hover, .collapse-toggle:hover, .header-btn:hover {
    transform: none !important;
    background: transparent !important;
  }

  .menu-btn:active, .user-btn:active, .collapse-toggle:active, .header-btn:active {
    transform: scale(0.9) !important;
    background: var(--bg-secondary) !important;
  }

  .mobile-header {
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  }

  .mobile-aside {
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  }

  .main-content {
    transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  }

  .menu-overlay {
    transition: opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  }

  .nav-icon {
    transition: transform 0.2s ease !important;
  }

  .nav-item:active .nav-icon {
    transform: scale(1.1) !important;
  }

  .logo {
    transition: all 0.3s ease !important;
  }

  .nav-item::after {
    transition: left 0.3s ease !important;
  }

  .menu-btn::before, .user-btn::before {
    transition: width 0.2s ease, height 0.2s ease !important;
  }
}

/* 减少动画模式 */
@media (prefers-reduced-motion: reduce) {
  .nav-item, .nav-icon, .nav-desc, .menu-btn, .user-btn, .collapse-toggle, .header-btn {
    transition: none !important;
  }
  
  .nav-item:hover {
    transform: none !important;
  }
}
</style> 