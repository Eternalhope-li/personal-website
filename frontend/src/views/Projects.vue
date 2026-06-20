<template>
  <section class="section">
    <router-link to="/" class="btn btn-outline" style="margin-bottom:16px;display:inline-flex;font-size:.82rem;"><i class="fas fa-arrow-left"></i> 返回首页</router-link>
    <div class="section-header"><h2>{{t('projects.title')}}</h2><p class="section-subtitle">{{t('projects.subtitle')}}</p></div>
    <div class="cards-grid">
      <div v-for="p in projects" :key="p.id" class="project-card">
        <div class="card-image" style="background:linear-gradient(135deg,var(--accent-1),var(--accent-3));"><span class="card-icon">🚀</span></div>
        <div class="card-body">
          <h3>{{p.title}}</h3>
          <p>{{p.description}}</p>
          <div v-if="p.technologies" style="display:flex;flex-wrap:wrap;gap:6px;margin:10px 0;">
            <span v-for="t2 in (p.technologiesList||p.technologies.split(','))" :key="t2" class="skill-tag" style="margin:0;font-size:.72rem;">{{t2.trim()}}</span>
          </div>
          <div style="display:flex;gap:10px;margin-top:auto;">
            <a :href="demoLink(p)" target="_blank" @click.stop class="btn btn-primary" style="padding:8px 18px;font-size:.82rem;cursor:pointer;">{{t('projects.demo')}}</a>
            <a v-if="p.githubUrl" :href="p.githubUrl" target="_blank" class="btn btn-secondary" style="padding:8px 18px;font-size:.82rem;cursor:pointer;"><i class="fab fa-github"></i> {{t('projects.source')}}</a>
          </div>
        </div>
      </div>
    </div>
    <div v-if="projects.length===0" class="empty-state"><div class="empty-icon">🚀</div><p>{{t('common.emptyProjects')}}</p></div>
  </section>
</template>
<script setup>
import{ref,onMounted}from'vue'
import{t}from'../i18n/index.js'
import{getProjects}from'../api/index.js'
var projects=ref([])
onMounted(async()=>{try{projects.value=await getProjects()}catch(e){}})
function demoLink(p) { return p.demoUrl && p.demoUrl.length > 1 ? p.demoUrl : window.location.origin }
</script>
