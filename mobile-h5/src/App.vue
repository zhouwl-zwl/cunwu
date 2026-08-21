<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  width: 100%;
  min-height: 100%;
  font-family: -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #FDF8F7;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-rendering: optimizeLegibility;
}

::selection {
  background: rgba(210, 38, 48, 0.2);
  color: #D22630;
}

/* 全局滚动条美化 */
::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: rgba(210, 38, 48, 0.2);
  border-radius: 2px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(210, 38, 48, 0.4);
}

:root {
  --main-red: #C8102E;
  --main-gold: #FFD700;
  --active-bg: linear-gradient(90deg, #9E0F1E, #C23220);
  --van-primary-color: #C8102E;
  --van-primary-dark: #A80E28;
  --van-success-color: #C8102E;
  --van-warning-color: #FFD700;
}
</style>
