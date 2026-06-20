<template>
  <footer>
    <div class="footer-container">
      <div class="footer-links">
        <router-link to="/">{{t('nav.home')}}</router-link>
        <router-link to="/about">{{t('nav.about')}}</router-link>
        <router-link to="/projects">{{t('nav.projects')}}</router-link>
        <router-link to="/blog">{{t('nav.blog')}}</router-link>
        <router-link to="/timeline">{{t('nav.timeline')}}</router-link>
        <router-link to="/guestbook">{{t('nav.guestbook')}}</router-link>
        <router-link to="/contact">{{t('nav.contact')}}</router-link>
      </div>
      <div class="footer-social">
        <a href="#" aria-label="GitHub"><i class="fab fa-github"></i></a>
        <a href="#" aria-label="Email"><i class="fas fa-envelope"></i></a>
      </div>
      <p class="footer-copy" v-html="t('footer.copyright')"></p>
    </div>
  </footer>
</template>
<script setup>import{t}from'../i18n/index.js'</script>