<template>
  <section class="section">
    <div style="max-width:400px;margin:0 auto;">
      <div style="text-align:center;margin-bottom:32px;"><div style="width:60px;height:60px;border-radius:50%;background:var(--accent-gradient);display:flex;align-items:center;justify-content:center;font-size:1.3rem;margin:0 auto 12px;">🔐</div><h2 style="font-size:1.4rem;">{{t('user.loginTitle')}}</h2></div>
      <form @submit.prevent="handleLogin" class="profile-card">
        <div class="form-group"><label>{{t('user.username')}}</label><input v-model="form.username" placeholder="请输入账号" required></div>
        <div class="form-group"><label>{{t('user.password')}}</label><input v-model="form.password" type="password" placeholder="请输入密码" required></div>
        <button type="submit" class="btn btn-primary" style="width:100%;border:none;cursor:pointer;justify-content:center;">{{t('user.login')}}</button>
        <p style="text-align:center;color:var(--text-muted);font-size:.84rem;margin-top:12px;">{{t('user.noAccount')}} <router-link to="/register" style="color:var(--accent-2);">{{t('user.register')}}</router-link></p>
        <div v-if="err" style="margin-top:12px;padding:10px;background:rgba(253,121,168,0.1);border:1px solid rgba(253,121,168,0.2);border-radius:var(--radius-sm);color:var(--accent-3);font-size:.84rem;text-align:center;">{{err}}</div>
      </form>
    </div>
  </section>
</template>
<script setup>
import { ref } from 'vue'
import { t } from '../i18n/index.js'
import { login } from '../api/index.js'
var form = ref({ username: '', password: '' }), err = ref('')
async function handleLogin() {
  try {
    var r = await login(form.value)
    var ud = r.data || r
    try { localStorage.setItem('user', JSON.stringify({ id: ud.id, username: ud.username, role: ud.role, avatar: ud.avatar || '', displayName: ud.displayName || '', createdAt: ud.createdAt })) } catch (e) {}
    window.location.href = '/'
  } catch (e) { err.value = t('user.error') }
}
</script>
