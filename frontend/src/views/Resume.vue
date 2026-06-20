<template>
  <section class="section">
    <div style="max-width:600px;margin:0 auto;">
      <router-link to="/" class="btn btn-outline" style="margin-bottom:16px;display:inline-flex;font-size:.82rem;"><i class="fas fa-arrow-left"></i> 返回首页</router-link>
      <div class="section-header"><h2>{{t('resume.title')}}</h2><p class="section-subtitle">{{t('resume.subtitle')}}</p></div>
      <div class="profile-card">
        <div style="text-align:center;margin-bottom:24px;"><div style="width:64px;height:64px;border-radius:50%;background:var(--accent-gradient);display:flex;align-items:center;justify-content:center;font-size:1.5rem;margin:0 auto 10px;">👤</div><h1 style="font-size:1.6rem;background:var(--accent-gradient);-webkit-background-clip:text;-webkit-text-fill-color:transparent;">电饭煲</h1><p style="color:var(--text-secondary);margin-top:2px;">Full Stack Developer</p></div>
        <div class="form-group" style="margin-bottom:18px;"><h3 style="font-size:.95rem;color:var(--text-primary);margin-bottom:10px;padding-bottom:6px;border-bottom:2px solid var(--accent-gradient);">技能</h3><div style="display:flex;flex-wrap:wrap;gap:6px;"><span v-for="s in ['Spring Boot','Vue 3','MySQL','Java','JavaScript','Docker','Git']" :key="s" class="skill-tag" style="margin:0;">{{s}}</span></div></div>
        <div><h3 style="font-size:.95rem;color:var(--text-primary);margin-bottom:10px;padding-bottom:6px;border-bottom:2px solid var(--accent-gradient);">项目经验</h3><p style="color:var(--text-muted);font-size:.86rem;">{{t('resume.empty')}}</p></div>
      </div>
    </div>
  </section>
</template>
<script setup>import{t}from'../i18n/index.js'</script>
