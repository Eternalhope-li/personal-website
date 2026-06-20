<template>
  <section class="section">
    <router-link to="/" class="btn btn-outline" style="margin-bottom:16px;display:inline-flex;font-size:.82rem;"><i class="fas fa-arrow-left"></i> 返回首页</router-link>
      <div class="section-header"><h2>{{t('about.title')}}</h2><p class="section-subtitle" v-html="t('about.story')"></p></div>
    <div class="about-grid">
      <div class="profile-card">
        <div style="width:64px;height:64px;border-radius:50%;background:var(--accent-gradient);display:flex;align-items:center;justify-content:center;font-size:1.5rem;margin-bottom:16px;">👤</div>
        <h3 style="font-size:1.1rem;margin-bottom:10px;color:var(--text-primary);">{{t('about.doBest')}}</h3>
        <p style="color:var(--text-secondary);line-height:1.7;margin-bottom:18px;">本站由<strong style="color:var(--accent-2);">电饭煲</strong> 使用 Spring Boot + Vue 3 搭建。<br/>热爱技术，专注于构建优雅的数字体验。</p>
        <div class="expertise-grid">
          <div v-for="item in ei" :key="item.k" style="background:var(--bg-card);border-radius:var(--radius);padding:18px;border:1px solid var(--glass-border);"><div style="font-size:1.5rem;margin-bottom:8px;">{{item.i}}</div><h4 style="font-size:.9rem;margin-bottom:4px;color:var(--text-primary);">{{t('about.'+item.k)}}</h4><p style="font-size:.8rem;color:var(--text-secondary);line-height:1.5;">{{t('about.'+item.k+'Desc')}}</p></div>
        </div>
      </div>
      <div class="profile-card">
        <h3 style="font-size:1.1rem;margin-bottom:16px;color:var(--text-primary);">{{t('skills.title')}}</h3>
        <div v-for="sk in skills" :key="sk.id" style="margin-bottom:14px;">
          <div style="display:flex;justify-content:space-between;margin-bottom:4px;"><span style="color:var(--text-primary);font-size:.84rem;">{{sk.name}}</span><span style="color:var(--accent-2);font-size:.78rem;">{{sk.category}}</span></div>
          <div style="height:4px;background:var(--bg-card);border-radius:2px;overflow:hidden;"><div style="height:100%;width:80%;background:var(--accent-gradient);border-radius:2px;"></div></div>
        </div>
        <div v-if="skills.length===0" class="empty-state" style="padding:30px 0;"><p style="font-size:.88rem;">{{t('common.emptyPosts')}}</p></div>
      </div>
    </div>
  </section>
</template>
<script setup>
import{ref,onMounted}from'vue'
import{t}from'../i18n/index.js'
import{getSkills}from'../api/index.js'
var skills=ref([]),ei=[{k:'backend',i:'🔧'},{k:'frontend',i:'🎨'},{k:'cloud',i:'☁️'}]
onMounted(async()=>{try{skills.value=await getSkills()}catch(e){}})
</script>