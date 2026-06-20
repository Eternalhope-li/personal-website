<template>
  <section class="section">
    <div style="max-width:600px;margin:0 auto;">
      <router-link to="/" class="btn btn-outline" style="margin-bottom:16px;display:inline-flex;font-size:.82rem;"><i class="fas fa-arrow-left"></i> 返回首页</router-link>
      <div class="section-header"><h2>{{t('timeline.title')}}</h2><p class="section-subtitle">{{t('timeline.subtitle')}}</p></div>
      <div class="timeline-wrapper">
        <div class="timeline-line"></div>
        <div v-for="(item,i) in items" :key="i" class="timeline-item">
          <div class="timeline-dot"></div>
          <div class="timeline-card"><div style="display:flex;justify-content:space-between;align-items:center;flex-wrap:wrap;gap:8px;margin-bottom:4px;"><h3>{{item.title}}</h3><span class="timeline-date">{{item.date}}</span></div><p class="timeline-desc">{{item.desc}}</p></div>
        </div>
        <div v-if="items.length===0" class="empty-state"><div class="empty-icon">📅</div><p>{{t('timeline.empty')}}</p></div>
      </div>
    </div>
  </section>
</template>
<script setup>
import{ref}from'vue'
import{t}from'../i18n/index.js'
var items=ref([{title:'搭建个人网站',date:'2026-06',desc:'使用 Spring Boot + Vue 3 构建个人网站。'}])
</script>