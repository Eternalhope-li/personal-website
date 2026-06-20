<template>
  <section class="section">
    <div style="max-width:600px;margin:0 auto;">
      <router-link to="/" class="btn btn-outline" style="margin-bottom:16px;display:inline-flex;font-size:.82rem;"><i class="fas fa-arrow-left"></i> 返回首页</router-link>
      <div class="section-header"><h2>{{t('guestbook.title')}}</h2><p class="section-subtitle">{{t('guestbook.subtitle')}}</p></div>

      <div v-if="user" class="profile-card" style="margin-bottom:32px;">
        <div style="display:flex;align-items:center;gap:8px;margin-bottom:10px;">
          <img v-if="user.avatar" :src="user.avatar" style="width:28px;height:28px;border-radius:50%;object-fit:cover;border:1px solid var(--accent-2);" />
          <div v-else style="width:28px;height:28px;border-radius:50%;background:var(--accent-gradient);display:flex;align-items:center;justify-content:center;font-size:.7rem;color:#fff;font-weight:700;">{{user.displayName||user.username?.charAt(0)?.toUpperCase()||"?"}}</div>
          <span style="color:var(--accent-2);font-size:.85rem;font-weight:500;">{{user.displayName||user.username}}</span>
        </div>
        <form @submit.prevent="submit">
          <div style="display:grid;gap:10px;">
            <div class="form-group" style="margin:0;"><textarea v-model="form.content" placeholder="写下你的留言..." rows="3" required></textarea></div>
            <button type="submit" class="btn btn-primary" style="border:none;cursor:pointer;justify-self:start;">提交留言</button>
            <span v-if="errMsg" style="color:var(--accent-3);font-size:.82rem;">{{errMsg}}</span>
            <span v-if="okMsg" style="color:#00b894;font-size:.82rem;">{{okMsg}}</span>
          </div>
        </form>
      </div>
      <div v-else class="profile-card" style="margin-bottom:32px;text-align:center;padding:24px;">
        <p style="color:var(--text-muted);font-size:.9rem;margin-bottom:10px;">登录后即可留言</p>
        <router-link to="/login" class="btn btn-primary" style="display:inline-flex;"><i class="fas fa-sign-in-alt"></i> 登录</router-link>
      </div>

      <h3 style="font-size:.95rem;color:var(--text-primary);margin-bottom:14px;">留言列表 ({{c.length}})</h3>
      <div style="display:grid;gap:10px;">
        <div v-for="x in c" :key="x.id" class="comment-item">
          <div class="comment-header"><div class="comment-author">
            <img v-if="x.avatar" :src="x.avatar" class="comment-avatar-img" />
            <div v-else class="comment-avatar">{{x.displayName||x.author?.charAt(0)?.toUpperCase()||"?"}}</div>
            <strong style="color:var(--accent-2);font-size:.86rem;">{{x.displayName||x.author}}</strong>
          </div><span class="comment-date">{{new Date(x.createdAt).toLocaleDateString()}}</span></div>
          <p class="comment-text">{{x.content}}</p>
        </div>
        <div v-if="c.length===0" class="empty-state"><div class="empty-icon">💬</div><p>暂无留言</p></div>
      </div>
    </div>
  </section>
</template>
<script setup>
import{ref,onMounted}from'vue'
import{getComments,submitComment as apiSubmit}from'../api/index.js'
var c=ref([]),form=ref({content:''}),user=ref(null),errMsg=ref(''),okMsg=ref('')
async function load(){try{c.value=await getComments(0)}catch(e){}}
async function submit(){
  if(!user.value||!form.value.content.trim())return
  errMsg.value='';okMsg.value=''
  try{var av=user.value.avatar||'';if(av.length>50000){av='';user.value.avatar='';try{localStorage.setItem('user',JSON.stringify(user.value))}catch(e){}}await apiSubmit({postId:0,author:user.value.username,content:form.value.content,avatar:av,displayName:user.value.displayName||''});form.value={content:''};okMsg.value=user.value&&user.value.role==='ADMIN'?'已提交':'已提交，等待审核';setTimeout(()=>okMsg.value='',3000);await load()}catch(e){errMsg.value='发送失败，请刷新页面后重试'}
}
onMounted(function(){load();try{var u=localStorage.getItem('user');if(u){user.value=JSON.parse(u);if(user.value.avatar&&user.value.avatar.length>50000){user.value.avatar='';try{localStorage.setItem('user',JSON.stringify(user.value))}catch(e){}}}}catch(e){}})
</script>
