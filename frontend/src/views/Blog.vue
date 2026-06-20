<template>
  <section class="section">
    <router-link to="/" class="btn btn-outline" style="margin-bottom:16px;display:inline-flex;font-size:.82rem;"><i class="fas fa-arrow-left"></i> 返回首页</router-link>
    <div class="section-header"><h2>{{t('blog.title')}}</h2><p class="section-subtitle">{{t('blog.subtitle')}}</p></div>
    <div class="cards-grid">
      <div v-for="post in posts" :key="post.id" class="blog-card" @click="$router.push('/blog/'+post.id)" style="cursor:pointer;">
        <div class="card-image" style="background:linear-gradient(135deg,var(--accent-2),#00cec9);"><span class="card-icon">📝</span></div>
        <div class="card-body">
          <h3>{{post.title}}</h3>
          <p>{{post.summary||post.content?.substring(0,120)}}</p>
          <div class="card-meta"><span class="card-date">{{new Date(post.createdAt).toLocaleDateString()}}</span><span class="card-link">{{t('blog.readMore')}} →</span></div>
        </div>
      </div>
    </div>
    <div v-if="posts.length===0" class="empty-state"><div class="empty-icon">📝</div><p>{{t('common.emptyPosts')}}</p></div>
  </section>
</template>
<script setup>
import{ref,onMounted}from'vue'
import{t}from'../i18n/index.js'
import{getPosts}from'../api/index.js'
var posts=ref([])
onMounted(async()=>{try{posts.value=await getPosts()}catch(e){}})
</script>