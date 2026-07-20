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
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #FDF5F5;
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
