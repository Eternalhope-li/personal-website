<template>
  <nav>
    <div class="nav-container">
      <router-link to="/" class="nav-logo">电饭煲<span> // personal</span></router-link>
      <ul class="nav-links" :class="{open:menuOpen}">
        <li><router-link to="/" :class="{active:p==='/'}">{{t('nav.home')}}</router-link></li>
        <li><router-link to="/about" :class="{active:p.startsWith('/about')}">{{t('nav.about')}}</router-link></li>
        <li><router-link to="/projects" :class="{active:p.startsWith('/projects')}">{{t('nav.projects')}}</router-link></li>
        <li><router-link to="/blog" :class="{active:p.startsWith('/blog')}">{{t('nav.blog')}}</router-link></li>
        <li><router-link to="/contact" :class="{active:p==='/contact'}">{{t('nav.contact')}}</router-link></li>
        <li class="nav-more-wrap">
          <a href="#" @click.prevent="toggleMore" class="more-link">{{t('nav.more')}} <i class="fas fa-chevron-down"></i></a>
          <div ref="moreDropdown" :class="['more-dropdown', {show:showMore}]">
            <router-link to="/timeline" @click="closeMenu"><i class="fas fa-stream"></i> {{t('nav.timeline')}}</router-link>
            <router-link to="/resume" @click="closeMenu"><i class="fas fa-file-alt"></i> {{t('nav.resume')}}</router-link>
            <router-link to="/friends" @click="closeMenu"><i class="fas fa-link"></i> {{t('nav.friends')}}</router-link>
            <router-link to="/guestbook" @click="closeMenu"><i class="fas fa-comment-dots"></i> {{t('nav.guestbook')}}</router-link>
          </div>
        </li>
        <li><button class="theme-toggle" @click="toggleTheme"><i :class="isLight ? &apos;fas fa-sun&apos; : &apos;fas fa-moon&apos;"></i><span class="theme-label">{{isLight?t('theme.light'):t('theme.dark')}}</span></button></li>
        <li><a href="#" @click.prevent="swLang" class="lang-switch">{{getLang()==='zh'?'EN':'中文'}}</a></li>
        <li v-if="!user"><router-link to="/login" class="nav-login-btn"><i class="fas fa-sign-in-alt"></i> {{t('nav.login')}}</router-link></li>
        <li v-if="user"><router-link to="/profile" class="nav-user-btn"><img v-if="user.avatar" :src="user.avatar" class="nav-avatar" /><i v-else class="fas fa-user"></i> {{user.displayName || user.username}}</router-link></li>
        <li v-if="user"><a href="#" @click.prevent="logout" class="nav-logout-btn" :title="t('user.logout')"><i class="fas fa-sign-out-alt"></i></a></li>
      </ul>
      <button class="menu-toggle" :class="{active:menuOpen}" @click="menuOpen=!menuOpen"><span></span><span></span><span></span></button>
    </div>
  </nav>
</template>
<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { t, setLang, getLang } from '../i18n/index.js'
import { useRouter, useRoute } from 'vue-router'
var rt = useRouter(), ru = useRoute()
var p = computed(() => ru.path)
var menuOpen = ref(false), showMore = ref(false), user = ref(null), isLight = ref(false)
var moreDropdown = ref(null)

onMounted(() => {
  try { var u = localStorage.getItem('user'); if (u) { user.value = JSON.parse(u); if (user.value.avatar && !user.value.avatar.startsWith('/uploads/') && !user.value.avatar.startsWith('http')) { user.value.avatar = ''; try { localStorage.setItem('user', JSON.stringify(user.value)) } catch(e) {} } } } catch (e) {}
  try { isLight.value = localStorage.getItem('theme') === 'light'; if (isLight.value) document.body.classList.add('light') } catch (e) {}
  document.addEventListener('click', handleOutsideClick)
})

onUnmounted(() => { document.removeEventListener('click', handleOutsideClick) })

function handleOutsideClick(e) {
  if (moreDropdown.value && !moreDropdown.value.contains(e.target) && !e.target.closest('.more-link')) {
    showMore.value = false
  }
}

function toggleMore() { showMore.value = !showMore.value }

function closeMenu() { showMore.value = false; menuOpen.value = false }

function toggleTheme() {
  isLight.value = !isLight.value
  document.body.classList.toggle('light', isLight.value)
  try { localStorage.setItem('theme', isLight.value ? 'light' : 'dark') } catch (e) {}
}

function swLang() {
  var l = getLang() === 'zh' ? 'en' : 'zh'
  setLang(l)
  document.documentElement.lang = l === 'zh' ? 'zh-CN' : 'en'
  window.location.reload()
}

function logout() {
  try { localStorage.removeItem('user') } catch (e) {}
  user.value = null
  rt.push('/')
}
</script>

<style scoped>
.nav-avatar{width:24px;height:24px;border-radius:50%;object-fit:cover;border:1px solid var(--accent-2);flex-shrink:0}
</style>
