<template>
  <aside class="side-menu">
    <div class="menu-brand">
      <div class="brand-star"></div>
      <h1>村级智慧村务</h1>
      <div class="brand-line"></div>
      <p>乡村治理一体化平台</p>
    </div>
    
    <nav class="menu-nav">
      <MenuCollapse 
        v-for="menu in menuConfig" 
        :key="menu.id" 
        :menu="menu" 
      />
    </nav>
    
    <div class="menu-footer">
      <div class="footer-version">V1.0.0</div>
    </div>
  </aside>
</template>

<script setup>
import MenuCollapse from './MenuCollapse.vue'
import { menuConfig } from '../config/menuConfig'
</script>

<style scoped>
.side-menu {
  width: 250px;
  min-height: 100vh;
  background: #C8102E;
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 100;
  box-shadow: 2px 0 12px rgba(0, 0, 0, 0.15);
}

.menu-brand {
  padding: 40px 20px;
  text-align: center;
  border-bottom: 1px solid rgba(255, 215, 0, 0.1);
}

.brand-star {
  width: 56px;
  height: 56px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #FFD700 0%, #FFA500 50%, #E5C100 100%);
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
  box-shadow: 0 4px 16px rgba(255, 215, 0, 0.3), inset 0 2px 4px rgba(255, 255, 255, 0.5);
}

.menu-brand h1 {
  font-size: 20px;
  font-weight: 700;
  color: #FFFFFF;
  margin: 0 0 8px;
  letter-spacing: 1px;
}

.brand-line {
  width: 48px;
  height: 2px;
  background: linear-gradient(90deg, transparent, #FFD700, transparent);
  margin: 0 auto 12px;
}

.menu-brand p {
  font-size: 12px;
  color: #FFE499;
  margin: 0;
}

.menu-nav {
  flex: 1;
  overflow-y: auto;
  padding: 12px 0;
}

.menu-nav::-webkit-scrollbar {
  width: 4px;
}

.menu-nav::-webkit-scrollbar-track {
  background: transparent;
}

.menu-nav::-webkit-scrollbar-thumb {
  background: rgba(255, 215, 0, 0.3);
  border-radius: 2px;
}

.menu-footer {
  padding: 16px 20px;
  border-top: 1px solid rgba(255, 215, 0, 0.1);
}

.footer-version {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.5);
  text-align: center;
}

@media (max-width: 768px) {
  .side-menu {
    display: none;
  }
}
</style>