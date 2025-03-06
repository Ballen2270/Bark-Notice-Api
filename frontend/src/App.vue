<template>
  <div class="app-container">
    <el-config-provider :locale="zhCn">
      <router-view />
    </el-config-provider>
  </div>
</template>

<script setup>
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  color: #303133;
}

#app, .app-container {
  height: 100vh;
  width: 100%;
}

/* iOS风格卡片 */
.el-card {
  border-radius: 12px !important;
  border: none !important;
  box-shadow: 0 1px 8px rgba(0, 0, 0, 0.05) !important;
  overflow: hidden;
  transition: all 0.3s;
}

.el-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08) !important;
}

.el-card__header {
  padding: 16px 20px !important;
  border-bottom: 1px solid #f0f0f0 !important;
}

/* iOS风格按钮 */
.el-button {
  border-radius: 8px !important;
  font-weight: 500 !important;
}

.el-button--primary {
  background-color: #007aff !important;
  border-color: #007aff !important;
}

.el-button--success {
  background-color: #34c759 !important;
  border-color: #34c759 !important;
}

.el-button--danger {
  background-color: #ff3b30 !important;
  border-color: #ff3b30 !important;
}

/* iOS风格表单 */
.el-input__inner {
  border-radius: 8px !important;
}

.el-input.is-focus .el-input__inner {
  border-color: #007aff !important;
}

/* 表格圆角 */
.el-table {
  border-radius: 12px !important;
  overflow: hidden;
}

/* 移动端表格响应式处理 */
@media (max-width: 767px) {
  .el-table {
    width: 100% !important;
    display: block;
    overflow-x: auto;
  }
  
  .el-card {
    margin-bottom: 15px !important;
  }
  
  .el-form-item {
    margin-bottom: 18px !important;
  }
}
</style> 