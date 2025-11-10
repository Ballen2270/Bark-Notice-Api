<template>
  <div class="app-container">
    <el-config-provider :locale="zhCn">
      <transition name="fade" mode="out-in">
        <router-view />
      </transition>
    </el-config-provider>
  </div>
</template>

<script setup>
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
</script>

<style>
/* CSS变量定义 - iOS风格主题 */
:root {
  /* 主要颜色 */
  --primary-color: #007aff;
  --primary-light: #58c8fa;
  --primary-dark: #0056d6;
  --success-color: #34c759;
  --success-light: #32c864;
  --warning-color: #ff9500;
  --warning-light: #ffb432;
  --danger-color: #ff3b30;
  --danger-light: #ff645a;
  --info-color: #5ac8fa;
  --info-light: #17a2b8;

  /* 文本颜色 */
  --text-primary: #1d1d1f;
  --text-secondary: #86868b;
  --text-tertiary: #c7c7cc;
  --text-inverse: #ffffff;

  /* 背景颜色 */
  --bg-primary: #ffffff;
  --bg-secondary: #f2f2f7;
  --bg-tertiary: #ffffff;
  --bg-glass: rgba(255, 255, 255, 0.8);
  --bg-overlay: rgba(0, 0, 0, 0.5);

  /* 边框颜色 */
  --border-color: #d1d1d6;
  --border-light: #e5e5ea;
  --border-glass: rgba(255, 255, 255, 0.8);

  /* 增强阴影系统 */
  --shadow-light: 0 1px 3px rgba(0, 0, 0, 0.1);
  --shadow-medium: 0 4px 12px rgba(0, 0, 0, 0.15);
  --shadow-heavy: 0 8px 25px rgba(0, 0, 0, 0.2);
  --shadow-card: 0 2px 8px rgba(0, 0, 0, 0.08), 0 1px 3px rgba(0, 0, 0, 0.12);
  --shadow-card-hover: 0 12px 30px rgba(0, 0, 0, 0.15), 0 4px 12px rgba(0, 0, 0, 0.1);
  --shadow-glow: 0 0 20px rgba(0, 122, 255, 0.3);

  /* 圆角 */
  --radius-small: 8px;
  --radius-medium: 12px;
  --radius-large: 16px;
  --radius-xl: 20px;
  --radius-full: 50%;

  /* 间距 */
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;
  --spacing-2xl: 48px;

  /* 字体大小 */
  --font-size-xs: 12px;
  --font-size-sm: 14px;
  --font-size-md: 16px;
  --font-size-lg: 18px;
  --font-size-xl: 24px;
  --font-size-2xl: 32px;

  /* 字体粗细 */
  --font-weight-light: 300;
  --font-weight-normal: 400;
  --font-weight-medium: 500;
  --font-weight-semibold: 600;
  --font-weight-bold: 700;

  /* 行高 */
  --line-height-tight: 1.2;
  --line-height-normal: 1.5;
  --line-height-relaxed: 1.8;

  /* 动画时长 */
  --duration-fast: 0.2s;
  --duration-normal: 0.3s;
  --duration-slow: 0.5s;

  /* 缓动函数 */
  --ease-out: cubic-bezier(0.4, 0, 0.2, 1);
  --ease-in: cubic-bezier(0.4, 0, 1, 1);
  --ease-in-out: cubic-bezier(0.4, 0, 0.2, 1);

  /* Z-index层级 */
  --z-dropdown: 1000;
  --z-modal: 1050;
  --z-popover: 1060;
  --z-tooltip: 1070;
  --z-toast: 2000;
}

/* 全局重置 */
*, *::before, *::after {
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Display', 'SF Pro Text', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: var(--text-primary);
  background-color: var(--bg-secondary);
  line-height: 1.5;
}

#app, .app-container {
  height: 100vh;
  width: 100%;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

/* 页面过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 滑动过渡动画 */
.slide-fade-enter-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-fade-enter-from {
  transform: translateX(-20px);
  opacity: 0;
}

.slide-fade-leave-to {
  transform: translateX(20px);
  opacity: 0;
}

/* Element Plus 全局样式覆盖 */
.el-card {
  border-radius: var(--radius-medium) !important;
  border: none !important;
  box-shadow: var(--shadow-light) !important;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  background: var(--bg-primary) !important;
}

.el-card:hover {
  box-shadow: var(--shadow-medium) !important;
  transform: translateY(-2px) !important;
}

.el-card__header {
  padding: var(--spacing-md) var(--spacing-lg) !important;
  border-bottom: 1px solid var(--border-light) !important;
  background: var(--bg-primary) !important;
  font-weight: 600 !important;
  color: var(--text-primary) !important;
}

.el-card__body {
  padding: var(--spacing-lg) !important;
}

/* 按钮样式 */
.el-button {
  border-radius: var(--radius-small) !important;
  font-weight: 500 !important;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1) !important;
  border-width: 0 !important;
  box-shadow: var(--shadow-light) !important;
}

.el-button:hover {
  transform: translateY(-1px) !important;
  box-shadow: var(--shadow-medium) !important;
}

.el-button:active {
  transform: translateY(0) !important;
}

.el-button--primary {
  background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%) !important;
  color: white !important;
}

.el-button--success {
  background: linear-gradient(135deg, var(--success-color) 0%, #28a745 100%) !important;
  color: white !important;
}

.el-button--danger {
  background: linear-gradient(135deg, var(--danger-color) 0%, #dc3545 100%) !important;
  color: white !important;
}

.el-button--warning {
  background: linear-gradient(135deg, var(--warning-color) 0%, #fd7e14 100%) !important;
  color: white !important;
}

.el-button--info {
  background: linear-gradient(135deg, var(--info-color) 0%, #17a2b8 100%) !important;
  color: white !important;
}

/* 输入框样式 */
.el-input__wrapper {
  border-radius: var(--radius-small) !important;
  border: 1px solid var(--border-light) !important;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1) !important;
  box-shadow: none !important;
}

.el-input__wrapper:hover {
  border-color: var(--border-color) !important;
}

.el-input__wrapper.is-focus {
  border-color: var(--primary-color) !important;
  box-shadow: 0 0 0 2px rgba(0, 122, 255, 0.1) !important;
}

.el-input__inner {
  color: var(--text-primary) !important;
  font-size: var(--font-size-md) !important;
}

/* 表格样式 */
.el-table {
  border-radius: var(--radius-medium) !important;
  overflow: hidden !important;
  box-shadow: var(--shadow-light) !important;
}

.el-table__header-wrapper {
  background: var(--bg-secondary) !important;
}

.el-table th {
  background: var(--bg-secondary) !important;
  color: var(--text-secondary) !important;
  font-weight: 600 !important;
  border-bottom: 1px solid var(--border-light) !important;
}

.el-table td {
  border-bottom: 1px solid var(--border-light) !important;
  padding: var(--spacing-md) !important;
}

.el-table--border .el-table__cell {
  border-right: 1px solid var(--border-light) !important;
}

/* 对话框样式 */
.el-dialog {
  border-radius: var(--radius-large) !important;
  box-shadow: var(--shadow-heavy) !important;
  overflow: hidden !important;
}

.el-dialog__header {
  padding: var(--spacing-lg) !important;
  border-bottom: 1px solid var(--border-light) !important;
  background: var(--bg-primary) !important;
}

.el-dialog__title {
  color: var(--text-primary) !important;
  font-weight: 600 !important;
  font-size: var(--font-size-lg) !important;
}

.el-dialog__body {
  padding: var(--spacing-lg) !important;
  color: var(--text-primary) !important;
}

.el-dialog__footer {
  padding: var(--spacing-md) var(--spacing-lg) var(--spacing-lg) !important;
  border-top: 1px solid var(--border-light) !important;
}

/* 菜单样式 */
.el-menu {
  border-right: none !important;
  background: var(--bg-primary) !important;
}

.el-menu-item {
  color: var(--text-primary) !important;
  border-radius: var(--radius-small) !important;
  margin: 0 var(--spacing-sm) var(--spacing-xs) !important;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-menu-item:hover {
  background: var(--bg-secondary) !important;
  color: var(--primary-color) !important;
}

.el-menu-item.is-active {
  background: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 100%) !important;
  color: white !important;
  box-shadow: var(--shadow-light) !important;
}

.el-menu-item.is-active .el-icon {
  color: white !important;
}

/* 标签样式 */
.el-tag {
  border-radius: var(--radius-small) !important;
  border: none !important;
  font-weight: 500 !important;
  box-shadow: var(--shadow-light) !important;
}

.el-tag--success {
  background: linear-gradient(135deg, var(--success-color) 0%, #28a745 100%) !important;
  color: white !important;
}

.el-tag--danger {
  background: linear-gradient(135deg, var(--danger-color) 0%, #dc3545 100%) !important;
  color: white !important;
}

.el-tag--warning {
  background: linear-gradient(135deg, var(--warning-color) 0%, #fd7e14 100%) !important;
  color: white !important;
}

.el-tag--info {
  background: linear-gradient(135deg, var(--info-color) 0%, #17a2b8 100%) !important;
  color: white !important;
}

/* 分割线样式 */
.el-divider {
  border-top: 1px solid var(--border-light) !important;
  margin: var(--spacing-lg) 0 !important;
}

/* 加载动画 */
.el-loading-mask {
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(2px) !important;
}

/* 消息提示样式 */
.el-message {
  border-radius: var(--radius-medium) !important;
  box-shadow: var(--shadow-medium) !important;
  backdrop-filter: blur(10px) !important;
}

.el-message--success {
  background: rgba(52, 199, 89, 0.9) !important;
  border: 1px solid var(--success-color) !important;
  color: white !important;
}

.el-message--error {
  background: rgba(255, 59, 48, 0.9) !important;
  border: 1px solid var(--danger-color) !important;
  color: white !important;
}

.el-message--warning {
  background: rgba(255, 149, 0, 0.9) !important;
  border: 1px solid var(--warning-color) !important;
  color: white !important;
}

/* 移动端适配 */
@media (max-width: 767px) {
  :root {
    --spacing-xs: 2px;
    --spacing-sm: 6px;
    --spacing-md: 12px;
    --spacing-lg: 16px;
    --spacing-xl: 24px;

    --font-size-xs: 11px;
    --font-size-sm: 13px;
    --font-size-md: 14px;
    --font-size-lg: 16px;
    --font-size-xl: 20px;

    --radius-small: 6px;
    --radius-medium: 8px;
    --radius-large: 12px;

    --shadow-light: 0 1px 2px rgba(0, 0, 0, 0.08);
    --shadow-medium: 0 2px 8px rgba(0, 0, 0, 0.12);
    --shadow-heavy: 0 4px 16px rgba(0, 0, 0, 0.15);
  }

  html, body {
    font-size: var(--font-size-md);
    line-height: 1.4;
    -webkit-text-size-adjust: 100%;
    -ms-text-size-adjust: 100%;
  }

  #app, .app-container {
    overflow-x: hidden;
    position: relative;
  }

  .el-card {
    margin-bottom: var(--spacing-md) !important;
    border-radius: var(--radius-small) !important;
    box-shadow: var(--shadow-light) !important;
    transition: all 0.2s ease !important;
    overflow: hidden;
  }

  .el-card:hover {
    box-shadow: var(--shadow-medium) !important;
    transform: translateY(-1px) !important;
  }

  .el-card:active {
    transform: scale(0.98) !important;
    box-shadow: var(--shadow-light) !important;
  }

  .el-card__header {
    padding: var(--spacing-sm) var(--spacing-md) !important;
    font-size: var(--font-size-md) !important;
    border-radius: var(--radius-small) var(--radius-small) 0 0 !important;
    background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%) !important;
  }

  .el-card__body {
    padding: var(--spacing-md) !important;
    border-radius: 0 0 var(--radius-small) var(--radius-small) !important;
  }

  .el-dialog {
    width: 90% !important;
    max-width: 400px !important;
    margin: 0 auto !important;
    border-radius: var(--radius-medium) !important;
    box-shadow: var(--shadow-heavy) !important;
    overflow: hidden;
  }

  .el-dialog__header {
    padding: var(--spacing-md) !important;
    border-radius: var(--radius-medium) var(--radius-medium) 0 0 !important;
  }

  .el-dialog__body {
    padding: var(--spacing-md) !important;
    max-height: 70vh;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .el-dialog__footer {
    padding: var(--spacing-sm) var(--spacing-md) var(--spacing-md) !important;
    border-radius: 0 0 var(--radius-medium) var(--radius-medium) !important;
  }

  .el-table {
    font-size: var(--font-size-sm) !important;
    border-radius: var(--radius-small) !important;
    overflow: hidden;
  }

  .el-table th, .el-table td {
    padding: var(--spacing-sm) !important;
  }

  .el-button {
    padding: var(--spacing-xs) var(--spacing-sm) !important;
    font-size: var(--font-size-sm) !important;
    border-radius: var(--radius-small) !important;
    min-height: 40px !important;
    box-shadow: var(--shadow-light) !important;
    position: relative;
    overflow: hidden;
    transition: all 0.2s ease !important;
  }

  .el-button::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    transform: translate(-50%, -50%);
    transition: width 0.3s, height 0.3s;
    pointer-events: none;
  }

  .el-button:active::before {
    width: 200px;
    height: 200px;
  }

  .el-button:hover {
    box-shadow: var(--shadow-medium) !important;
  }

  .el-button:active {
    transform: scale(0.95) !important;
  }

  .el-input__wrapper {
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
  }

  .el-input__wrapper:hover {
    box-shadow: var(--shadow-light) !important;
  }

  .el-input__wrapper.is-focus {
    box-shadow: 0 0 0 2px rgba(0, 122, 255, 0.1) !important;
    transform: scale(1.01) !important;
  }

  .el-input__inner {
    font-size: var(--font-size-sm) !important;
    height: 40px !important;
  }

  .el-form-item {
    margin-bottom: var(--spacing-md) !important;
  }

  .el-form-item__label {
    font-size: var(--font-size-sm) !important;
    line-height: 1.4 !important;
    min-height: 24px;
    display: flex;
    align-items: center;
  }

  .el-select {
    font-size: var(--font-size-sm) !important;
  }

  .el-select .el-input__inner {
    height: 40px !important;
    font-size: var(--font-size-sm) !important;
  }

  .el-textarea__inner {
    font-size: var(--font-size-sm) !important;
    border-radius: var(--radius-small) !important;
    min-height: 80px;
    resize: vertical;
  }

  .el-tag {
    border-radius: var(--radius-small) !important;
    font-size: var(--font-size-xs) !important;
    min-height: 24px;
    display: inline-flex;
    align-items: center;
    padding: 0 var(--spacing-xs) !important;
  }

  .el-pagination {
    font-size: var(--font-size-sm) !important;
    padding: var(--spacing-sm) 0 !important;
  }

  .el-pagination .el-pager li {
    min-height: 32px !important;
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
  }

  .el-pagination .el-pager li:active {
    transform: scale(0.9) !important;
  }

  .el-pagination .btn-prev,
  .el-pagination .btn-next {
    min-height: 32px !important;
    border-radius: var(--radius-small) !important;
  }

  .el-checkbox {
    margin-bottom: var(--spacing-xs) !important;
  }

  .el-checkbox__inner {
    width: 16px !important;
    height: 16px !important;
    border-radius: var(--radius-small) !important;
  }

  .el-radio {
    margin-bottom: var(--spacing-xs) !important;
  }

  .el-radio__inner {
    width: 16px !important;
    height: 16px !important;
  }

  .el-transfer {
    font-size: var(--font-size-sm) !important;
  }

  .el-transfer-panel {
    border-radius: var(--radius-small) !important;
  }

  .el-drawer {
    border-radius: var(--radius-medium) var(--radius-medium) 0 0 !important;
  }

  .el-drawer__header {
    padding: var(--spacing-md) !important;
    font-size: var(--font-size-md) !important;
  }

  .el-drawer__body {
    padding: var(--spacing-md) !important;
  }
}

@media (max-width: 480px) {
  :root {
    --radius-small: 4px;
    --radius-medium: 6px;
    --radius-large: 8px;

    --shadow-light: 0 1px 2px rgba(0, 0, 0, 0.06);
    --shadow-medium: 0 2px 6px rgba(0, 0, 0, 0.1);
    --shadow-heavy: 0 3px 12px rgba(0, 0, 0, 0.12);
  }

  html, body {
    font-size: var(--font-size-sm);
    line-height: 1.3;
  }

  .el-dialog {
    width: 95% !important;
    max-width: 360px !important;
    border-radius: var(--radius-medium) !important;
    overflow: hidden;
  }

  .el-dialog__header {
    padding: var(--spacing-sm) var(--spacing-md) !important;
    font-size: var(--font-size-sm) !important;
  }

  .el-dialog__body {
    padding: var(--spacing-sm) var(--spacing-md) !important;
    font-size: var(--font-size-sm) !important;
    max-height: 75vh;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .el-dialog__footer {
    padding: var(--spacing-xs) var(--spacing-md) var(--spacing-sm) !important;
  }

  .el-dialog__footer .el-button {
    margin-bottom: 0 !important;
    display: inline-block !important;
    width: auto !important;
    margin-right: var(--spacing-xs) !important;
  }

  .el-dialog__footer .el-button:last-child {
    margin-right: 0 !important;
  }

  .el-card {
    border-radius: var(--radius-small) !important;
    margin-bottom: var(--spacing-sm) !important;
  }

  .el-card__header {
    padding: var(--spacing-xs) var(--spacing-sm) !important;
    font-size: var(--font-size-sm) !important;
    border-radius: var(--radius-small) var(--radius-small) 0 0 !important;
  }

  .el-card__body {
    padding: var(--spacing-sm) !important;
    font-size: var(--font-size-sm) !important;
    border-radius: 0 0 var(--radius-small) var(--radius-small) !important;
  }

  .el-button {
    display: block !important;
    width: 100% !important;
    margin-bottom: var(--spacing-xs) !important;
    padding: var(--spacing-xs) var(--spacing-sm) !important;
    font-size: var(--font-size-xs) !important;
    min-height: 36px !important;
    border-radius: var(--radius-small) !important;
  }

  .el-button:last-child {
    margin-bottom: 0 !important;
  }

  .el-button:active {
    transform: scale(0.95) !important;
  }

  .el-input__wrapper {
    border-radius: var(--radius-small) !important;
  }

  .el-input__inner {
    height: 36px !important;
    font-size: var(--font-size-sm) !important;
  }

  .el-select .el-input__inner {
    height: 36px !important;
    font-size: var(--font-size-sm) !important;
  }

  .el-textarea__inner {
    font-size: var(--font-size-sm) !important;
    border-radius: var(--radius-small) !important;
    min-height: 72px;
  }

  .el-form-item {
    margin-bottom: var(--spacing-sm) !important;
  }

  .el-form-item__label {
    font-size: var(--font-size-xs) !important;
    line-height: 1.3 !important;
    min-height: 20px;
  }

  .el-table {
    font-size: var(--font-size-xs) !important;
  }

  .el-table th,
  .el-table td {
    padding: var(--spacing-xs) var(--spacing-sm) !important;
  }

  .el-table th {
    font-weight: 600 !important;
  }

  .el-tag {
    border-radius: var(--radius-small) !important;
    font-size: 10px !important;
    min-height: 20px;
    padding: 0 4px !important;
  }

  .el-pagination {
    font-size: var(--font-size-xs) !important;
    text-align: center;
  }

  .el-pagination .el-pager li {
    min-height: 28px !important;
    border-radius: var(--radius-small) !important;
    min-width: 28px;
  }

  .el-pagination .btn-prev,
  .el-pagination .btn-next {
    min-height: 28px !important;
    min-width: 28px;
  }

  .el-checkbox__inner {
    width: 14px !important;
    height: 14px !important;
  }

  .el-radio__inner {
    width: 14px !important;
    height: 14px !important;
  }

  .el-transfer {
    font-size: var(--font-size-xs) !important;
  }

  .el-transfer-panel {
    border-radius: var(--radius-small) !important;
  }

  .el-drawer__header {
    padding: var(--spacing-sm) !important;
    font-size: var(--font-size-sm) !important;
  }

  .el-drawer__body {
    padding: var(--spacing-sm) !important;
  }

  .el-message {
    font-size: var(--font-size-xs) !important;
    padding: var(--spacing-xs) var(--spacing-sm) !important;
    border-radius: var(--radius-small) !important;
  }

  .el-notification {
    font-size: var(--font-size-xs) !important;
    border-radius: var(--radius-small) !important;
  }
}

/* 触摸设备优化 */
@media (hover: none) and (pointer: coarse) {
  .el-button {
    min-height: 44px !important;
    min-width: 44px !important;
    padding: var(--spacing-xs) var(--spacing-md) !important;
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
    position: relative;
    overflow: hidden;
  }

  .el-button:hover {
    transform: none !important;
    box-shadow: var(--shadow-light) !important;
  }

  .el-button:active {
    transform: scale(0.95) !important;
    box-shadow: var(--shadow-medium) !important;
  }

  .el-menu-item {
    min-height: 44px !important;
    padding: 0 var(--spacing-md) !important;
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
  }

  .el-menu-item:hover {
    transform: none !important;
  }

  .el-menu-item:active {
    transform: scale(0.98) !important;
    background: var(--bg-secondary) !important;
  }

  .el-input__wrapper {
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
  }

  .el-input__wrapper:hover {
    box-shadow: var(--shadow-light) !important;
  }

  .el-input__wrapper.is-focus {
    box-shadow: 0 0 0 2px rgba(0, 122, 255, 0.1) !important;
    transform: scale(1.01) !important;
  }

  .el-input__inner {
    min-height: 44px !important;
    font-size: var(--font-size-md) !important;
  }

  .el-select .el-input__inner {
    min-height: 44px !important;
  }

  .el-textarea__inner {
    min-height: 88px !important;
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
  }

  .el-textarea__inner:focus {
    box-shadow: 0 0 0 2px rgba(0, 122, 255, 0.1) !important;
  }

  .el-card {
    border-radius: var(--radius-medium) !important;
    transition: all 0.2s ease !important;
  }

  .el-card:hover {
    transform: none !important;
    box-shadow: var(--shadow-light) !important;
  }

  .el-card:active {
    transform: scale(0.98) !important;
    box-shadow: var(--shadow-medium) !important;
  }

  .el-tag {
    border-radius: var(--radius-small) !important;
    min-height: 28px !important;
    display: inline-flex;
    align-items: center;
    padding: 0 var(--spacing-sm) !important;
    transition: all 0.2s ease !important;
  }

  .el-tag:active {
    transform: scale(0.95) !important;
  }

  .el-dialog {
    border-radius: var(--radius-large) !important;
    overflow: hidden;
  }

  .el-dialog__header {
    cursor: default !important;
  }

  .el-pagination .el-pager li {
    min-height: 44px !important;
    min-width: 44px !important;
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
  }

  .el-pagination .el-pager li:active {
    transform: scale(0.9) !important;
  }

  .el-pagination .btn-prev,
  .el-pagination .btn-next {
    min-height: 44px !important;
    min-width: 44px !important;
    transition: all 0.2s ease !important;
  }

  .el-pagination .btn-prev:active,
  .el-pagination .btn-next:active {
    transform: scale(0.9) !important;
  }

  .el-table th,
  .el-table td {
    padding: var(--spacing-md) !important;
  }

  .el-table__body tr {
    transition: all 0.2s ease !important;
  }

  .el-table__body tr:hover {
    transform: none !important;
  }

  .el-table__body tr:active {
    background-color: var(--bg-secondary) !important;
    transform: scale(0.995) !important;
  }

  /* 增加点击区域 */
  .el-form-item__label {
    min-height: 24px !important;
    display: flex;
    align-items: center;
    cursor: default !important;
  }

  .el-checkbox {
    margin-bottom: var(--spacing-xs) !important;
  }

  .el-checkbox__inner {
    width: 20px !important;
    height: 20px !important;
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
  }

  .el-checkbox__inner:hover {
    transform: scale(1.1) !important;
  }

  .el-checkbox__inner:active {
    transform: scale(0.9) !important;
  }

  .el-radio {
    margin-bottom: var(--spacing-xs) !important;
  }

  .el-radio__inner {
    width: 20px !important;
    height: 20px !important;
    transition: all 0.2s ease !important;
  }

  .el-radio__inner:hover {
    transform: scale(1.1) !important;
  }

  .el-radio__inner:active {
    transform: scale(0.9) !important;
  }

  .el-switch {
    min-height: 24px !important;
  }

  .el-switch__core {
    min-height: 20px !important;
    border-radius: 10px !important;
  }

  .el-slider {
    margin: var(--spacing-md) 0 !important;
  }

  .el-slider__runway {
    height: 6px !important;
    border-radius: 3px !important;
  }

  .el-slider__button {
    width: 20px !important;
    height: 20px !important;
    border: 2px solid var(--primary-color) !important;
  }

  .el-progress {
    margin-bottom: var(--spacing-sm) !important;
  }

  .el-progress-bar__outer {
    height: 8px !important;
    border-radius: 4px !important;
  }

  .el-progress-bar__inner {
    border-radius: 4px !important;
  }

  .el-upload {
    cursor: pointer !important;
  }

  .el-upload-dragger {
    min-height: 120px !important;
    border-radius: var(--radius-medium) !important;
    transition: all 0.2s ease !important;
  }

  .el-upload-dragger:hover {
    transform: none !important;
    border-color: var(--primary-color) !important;
  }

  .el-upload-dragger:active {
    transform: scale(0.98) !important;
    background: var(--bg-secondary) !important;
  }

  .el-tree-node__content {
    min-height: 36px !important;
    padding: var(--spacing-xs) var(--spacing-sm) !important;
    border-radius: var(--radius-small) !important;
    transition: all 0.2s ease !important;
  }

  .el-tree-node__content:hover {
    transform: none !important;
  }

  .el-tree-node__content:active {
    transform: scale(0.98) !important;
    background: var(--bg-secondary) !important;
  }

  .el-tabs__item {
    min-height: 40px !important;
    padding: 0 var(--spacing-md) !important;
    transition: all 0.2s ease !important;
  }

  .el-tabs__item:active {
    transform: scale(0.95) !important;
  }

  .el-step__title {
    cursor: pointer !important;
    transition: all 0.2s ease !important;
  }

  .el-step__title:active {
    transform: scale(0.95) !important;
  }

  .el-timeline-item__node {
    cursor: pointer !important;
    transition: all 0.2s ease !important;
  }

  .el-timeline-item__node:active {
    transform: scale(0.9) !important;
  }

  /* 滚动条优化 */
  ::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  ::-webkit-scrollbar-track {
    background: var(--bg-secondary);
    border-radius: 3px;
  }

  ::-webkit-scrollbar-thumb {
    background: var(--border-color);
    border-radius: 3px;
    transition: background 0.2s ease;
  }

  ::-webkit-scrollbar-thumb:hover {
    background: var(--text-secondary);
  }

  /* 选择优化 */
  ::selection {
    background: rgba(0, 122, 255, 0.2);
    color: var(--text-primary);
  }

  ::-moz-selection {
    background: rgba(0, 122, 255, 0.2);
    color: var(--text-primary);
  }
}

/* 高对比度模式 */
@media (prefers-contrast: high) {
  :root {
    --border-color: #000000;
    --text-primary: #000000;
    --bg-primary: #ffffff;
  }
}

/* 颜色优化和渐变增强 */
:root {
  /* 增强的渐变色彩 */
  --gradient-primary: linear-gradient(135deg, var(--primary-color) 0%, #0056d6 50%, #003d99 100%);
  --gradient-success: linear-gradient(135deg, var(--success-color) 0%, #28a745 50%, #1e7e34 100%);
  --gradient-danger: linear-gradient(135deg, var(--danger-color) 0%, #dc3545 50%, #c82333 100%);
  --gradient-warning: linear-gradient(135deg, var(--warning-color) 0%, #fd7e14 50%, #dc6502 100%);
  --gradient-info: linear-gradient(135deg, var(--info-color) 0%, #17a2b8 50%, #117a8b 100%);

  /* 增强的阴影效果 */
  --shadow-inset-light: inset 0 1px 3px rgba(0, 0, 0, 0.1);
  --shadow-inset-medium: inset 0 2px 8px rgba(0, 0, 0, 0.15);
  --shadow-glow: 0 0 20px rgba(0, 122, 255, 0.3);
  --shadow-glow-success: 0 0 20px rgba(52, 199, 89, 0.3);
  --shadow-glow-danger: 0 0 20px rgba(255, 59, 48, 0.3);
}

/* 增强的微交互动画 */
.el-button {
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-button::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.el-button:active::before {
  width: 300px;
  height: 300px;
}

.el-button:hover {
  transform: translateY(-2px) !important;
  box-shadow: var(--shadow-medium) !important;
}

.el-button:active {
  transform: translateY(0) !important;
  transition: transform 0.1s !important;
}

.el-card {
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-card::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent 30%,
    rgba(255, 255, 255, 0.1) 50%,
    transparent 70%
  );
  transform: rotate(45deg) translateX(-100%);
  transition: transform 0.6s;
  pointer-events: none;
}

.el-card:hover::before {
  transform: rotate(45deg) translateX(100%);
}

.el-input__wrapper {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-input__wrapper:hover {
  box-shadow: var(--shadow-light) !important;
}

.el-input__wrapper.is-focus {
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.1), var(--shadow-light) !important;
  transform: scale(1.01) !important;
}

.el-tag {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-tag:hover {
  transform: scale(1.05) !important;
  box-shadow: var(--shadow-light) !important;
}

.el-dialog {
  animation: dialog-in 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

@keyframes dialog-in {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(-20px);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

/* 消息提示增强 */
.el-message {
  animation: message-slide 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  backdrop-filter: blur(20px) !important;
}

@keyframes message-slide {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 加载动画增强 */
.el-loading-mask {
  backdrop-filter: blur(4px) !important;
}

.el-loading-spinner {
  animation: loading-pulse 1.5s ease-in-out infinite !important;
}

@keyframes loading-pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.7;
    transform: scale(1.05);
  }
}

/* 表格行动画 */
.el-table__body tr {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-table__body tr:hover {
  background-color: var(--bg-secondary) !important;
  transform: scale(1.005) !important;
  box-shadow: var(--shadow-light) !important;
}

/* 菜单项动画增强 */
.el-menu-item {
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-menu-item::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: var(--primary-color);
  transform: translateX(-50%);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.el-menu-item:hover::after {
  width: 80%;
}

/* 分页动画 */
.el-pagination .el-pager li {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.el-pagination .el-pager li:hover {
  transform: scale(1.1) !important;
  box-shadow: var(--shadow-light) !important;
}

.el-pagination .el-pager li.is-active {
  animation: pagination-pulse 0.3s ease-out !important;
}

@keyframes pagination-pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

/* 减少动画模式 */
@media (prefers-reduced-motion: reduce) {
  *, *::before, *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }

  .el-button:hover,
  .el-card:hover,
  .el-input__wrapper.is-focus,
  .el-tag:hover,
  .el-table__body tr:hover,
  .el-menu-item:hover,
  .el-pagination .el-pager li:hover {
    transform: none !important;
    box-shadow: none !important;
  }

  .el-button::before,
  .el-card::before,
  .el-menu-item::after {
    display: none !important;
  }

  }
</style> 