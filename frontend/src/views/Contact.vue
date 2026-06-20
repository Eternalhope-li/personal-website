<template>
  <section class="section">
    <div style="max-width:560px;margin:0 auto;">
      <router-link to="/" class="btn btn-outline" style="margin-bottom:16px;display:inline-flex;font-size:.82rem;"><i class="fas fa-arrow-left"></i> 返回首页</router-link>
      <div class="section-header"><h2>{{t('contact.title')}}</h2><p class="section-subtitle">{{t('contact.subtitle')}}</p></div>
      <div v-if="sent" class="profile-card" style="text-align:center;padding:40px;"><div style="font-size:3rem;margin-bottom:12px;">✅</div><h3 style="color:var(--text-primary);margin-bottom:8px;">{{t('contact.success')}}</h3><router-link to="/" class="btn btn-primary" style="margin-top:12px;">{{t('nav.home')}}</router-link></div>
      <form v-else @submit.prevent="submit" class="profile-card">
        <div class="form-group"><label>{{t('contact.name')}}</label><input v-model="f.name" :placeholder="t('contact.namePH')" required></div>
        <div class="form-group"><label>{{t('contact.email')}}</label><input v-model="f.email" type="email" :placeholder="t('contact.emailPH')" required></div>
        <div class="form-group"><label>{{t('contact.subject')}}</label><input v-model="f.subject" :placeholder="t('contact.subjectPH')"></div>
        <div class="form-group"><label>{{t('contact.message')}}</label><textarea v-model="f.content" :placeholder="t('contact.messagePH')" rows="4" required></textarea></div>
        <button type="submit" class="btn btn-primary" style="border:none;cursor:pointer;">{{t('contact.send')}} <i class="fas fa-paper-plane"></i></button>
      </form>
    </div>
  </section>
</template>
<script setup>
import{ref}from'vue'
import{t}from'../i18n/index.js'
import{submitContact}from'../api/index.js'
var f=ref({name:'',email:'',subject:'',content:''}),sent=ref(false)
async function submit(){try{await submitContact(f.value);sent.value=true}catch(e){}}
</script>