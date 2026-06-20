<template>
  <section class="section">
    <div style="max-width:720px;margin:0 auto;">
      <div v-if="post">
        <router-link to="/blog" class="btn btn-outline" style="margin-bottom:24px;display:inline-flex;"><i class="fas fa-arrow-left"></i> 返回博客</router-link>
        <h1 style="font-size:2rem;line-height:1.25;margin-bottom:12px;background:var(--accent-gradient);-webkit-background-clip:text;-webkit-text-fill-color:transparent;">{{post.title}}</h1>
        <div style="display:flex;gap:14px;color:var(--text-muted);font-size:.86rem;margin-bottom:36px;flex-wrap:wrap;">
          <span><i class="far fa-calendar"></i> {{new Date(post.createdAt).toLocaleDateString()}}</span>
          <span v-if="post.category"><i class="far fa-folder"></i> {{post.category}}</span>
          <span><i class="far fa-comment"></i> {{comments.length}} 条评论</span>
        </div>
        <div class="blog-content" v-html="post.content"></div>

        <div style="margin-top:60px;padding-top:40px;border-top:1px solid var(--glass-border);">
          <h3 style="font-size:1.1rem;margin-bottom:20px;color:var(--text-primary);">
            <i class="far fa-comment-dots" style="color:var(--accent-2);"></i> 发表评论
          </h3>

          <div v-if="user" class="profile-card" style="margin-bottom:28px;">
            <div style="display:flex;align-items:center;gap:8px;margin-bottom:12px;">
              <img v-if="user.avatar" :src="user.avatar" style="width:30px;height:30px;border-radius:50%;object-fit:cover;border:1px solid var(--accent-2);" />
              <div v-else style="width:30px;height:30px;border-radius:50%;background:var(--accent-gradient);display:flex;align-items:center;justify-content:center;font-size:.75rem;color:#fff;font-weight:700;">{{user.displayName||user.username?.charAt(0)?.toUpperCase()||"?"}}</div>
              <span style="color:var(--accent-2);font-size:.9rem;font-weight:500;">{{user.displayName||user.username}}</span>
              <span style="color:var(--text-muted);font-size:.75rem;margin-left:auto;">已登录</span>
            </div>
            <form @submit.prevent="handleSubmit">
              <div class="form-group" style="margin:0;">
                <textarea v-model="form.content" placeholder="写下你的评论..." rows="3" required style="width:100%;padding:12px 16px;border-radius:8px;background:rgba(255,255,255,0.03);border:1px solid var(--glass-border);color:var(--text-primary);font-family:inherit;font-size:.9rem;outline:none;resize:vertical;"></textarea>
              </div>
              <div style="display:flex;align-items:center;gap:10px;margin-top:10px;flex-wrap:wrap;">
                <button type="submit" class="btn btn-primary" style="border:none;cursor:pointer;" :disabled="sending">{{sending ? '提交中...' : '发表评论'}}</button>
                <span v-if="errMsg" style="color:var(--accent-3);font-size:.82rem;">{{errMsg}}</span>
                <span v-if="okMsg" style="color:#00b894;font-size:.82rem;">{{okMsg}}</span>
              </div>
            </form>
          </div>
          <div v-else class="profile-card" style="margin-bottom:28px;text-align:center;padding:28px;">
            <div style="font-size:2.5rem;margin-bottom:12px;">🔒</div>
            <p style="color:var(--text-muted);font-size:.95rem;margin-bottom:14px;">登录后即可发表评论</p>
            <router-link to="/login" class="btn btn-primary" style="display:inline-flex;"><i class="fas fa-sign-in-alt"></i> 立即登录</router-link>
          </div>

          <div style="display:grid;gap:10px;margin-top:30px;">
            <h4 style="font-size:.95rem;color:var(--text-primary);margin-bottom:4px;">全部评论 ({{comments.length}})</h4>
            <div v-for="c in comments" :key="c.id" class="comment-item">
              <div class="comment-header">
                <div class="comment-author">
                  <img v-if="c.avatar" :src="c.avatar" class="comment-avatar-img" />
                  <div v-else class="comment-avatar">{{c.displayName||c.author?.charAt(0)?.toUpperCase()||"?"}}</div>
                  <strong style="color:var(--accent-2);font-size:.86rem;">{{c.displayName||c.author}}</strong>
                </div>
                <span class="comment-date">{{new Date(c.createdAt).toLocaleDateString()}}</span>
              </div>
              <p class="comment-text">{{c.content}}</p>
            </div>
            <div v-if="comments.length===0" class="empty-state" style="padding:30px 0;">
              <div class="empty-icon">💬</div>
              <p>暂无评论，登录后发表第一条评论</p>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="empty-state"><div class="empty-icon">📄</div><p>页面不存在</p></div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getPost, getComments, submitComment as apiSubmitComment } from '../api/index.js'
var post = ref(null), comments = ref([]), route = useRoute()
var user = ref(null), sending = ref(false), errMsg = ref(''), okMsg = ref('')
var form = ref({ content: '' })

onMounted(async () => {
  try { post.value = await getPost(route.params.id) } catch(e) {}
  try { comments.value = await getComments(route.params.id) } catch(e) {}
  try { var u = localStorage.getItem('user'); if (u) { user.value = JSON.parse(u); if (user.value.avatar && user.value.avatar.length > 50000) { user.value.avatar = ''; try { localStorage.setItem('user', JSON.stringify(user.value)) } catch(e) {} } } } catch(e) {}
})

async function handleSubmit() {
  if (!user.value || !form.value.content.trim()) return
  sending.value = true; errMsg.value = ''; okMsg.value = ''
  try {
    var av=user.value.avatar||'';if(av&&!av.startsWith('/uploads/')&&!av.startsWith('http')){av='';user.value.avatar='';try{localStorage.setItem('user',JSON.stringify(user.value))}catch(e){}}var r = await apiSubmitComment({ postId: Number(route.params.id), author: user.value.username, content: form.value.content, avatar: av, displayName: user.value.displayName||'' })
    form.value = { content: '' }
    okMsg.value = user.value&&user.value.role==='ADMIN'?'评论已提交':'评论已提交，等待审核...'
    setTimeout(() => { okMsg.value = '' }, 3000)
    try { comments.value = await getComments(route.params.id) } catch(e) {}
  } catch (e) {
    if (e.response?.data?.message) { errMsg.value = e.response.data.message }
    else { errMsg.value = '评论发送失败' }
  }
  sending.value = false
}
</script>
