<template>
  <div id="app">
    <div class="background-effects">
      <div class="gradient-orb"></div>
      <div class="gradient-orb"></div>
      <div class="gradient-orb"></div>
    </div>
    <div class="grid-overlay"></div>
    <NavBar />
    <router-view />
    <FooterBar />
    <SearchModal />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import NavBar from './components/NavBar.vue'
import FooterBar from './components/FooterBar.vue'
import SearchModal from './components/SearchModal.vue'

onMounted(() => {
  try {
    if (localStorage.getItem('theme') === 'light') {
      document.body.classList.add('light')
    }
  } catch (e) {}
})
</script>
