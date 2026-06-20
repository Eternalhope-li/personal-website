<template>
  <section class="section">
    <div style="max-width:600px;margin:0 auto;">
      <router-link to="/" class="btn btn-outline" style="margin-bottom:16px;display:inline-flex;font-size:.82rem;"><i class="fas fa-arrow-left"></i> 返回首页</router-link>
      <div class="section-header"><h2>{{t('friends.tag')}}</h2><p class="section-subtitle">{{t('friends.subtitle')}}</p></div>
      <div class="friends-grid">
        <a v-for="(f2,i) in friends" :key="i" :href="f2.url" target="_blank" class="friend-card"><div class="friend-avatar">{{f2.name?.charAt(0)}}</div><div class="friend-info"><h4>{{f2.name}}</h4><p>{{f2.desc}}</p></div></a>
      </div>
      <div v-if="friends.length===0" class="empty-state"><div class="empty-icon">🔗</div><p>{{t('friends.empty')}}</p></div>
    </div>
  </section>
</template>
<script setup>
import{ref}from'vue'
import{t}from'../i18n/index.js'
var friends=ref([])
</script>