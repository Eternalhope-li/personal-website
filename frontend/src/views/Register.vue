<template>
  <section class="section">
    <div style="max-width:400px;margin:0 auto;">
      <div style="text-align:center;margin-bottom:32px;">
        <div style="width:60px;height:60px;border-radius:50%;background:var(--accent-gradient);display:flex;align-items:center;justify-content:center;font-size:1.3rem;margin:0 auto 12px;">📝</div>
        <h2 style="font-size:1.4rem;">{{t('user.registerTitle')}}</h2>
      </div>
      <form @submit.prevent="handleRegister" class="profile-card">
        <div class="form-group">
          <label>{{t('user.username')}}</label>
          <div style="display:flex;align-items:center;gap:8px;">
            <input :value="account" readonly style="background:var(--glass-bg);color:var(--accent-2);font-weight:600;letter-spacing:2px;" />
          </div>
        </div>
        <div class="form-group">
          <label>{{t('user.password')}}</label>
          <input v-model="form.password" type="password" :placeholder="t('user.passwordPH')" required minlength="6">
        </div>
        <div class="form-group">
          <label>确认密码</label>
          <input v-model="form.confirmPassword" type="password" placeholder="再次输入密码" required minlength="6">
          <span v-if="passwordMismatch" style="color:var(--accent-3);font-size:.78rem;margin-top:4px;display:block;">两次密码不一致</span>
        </div>
        <div class="form-group">
          <label>验证码</label>
          <div style="display:flex;align-items:center;gap:8px;">
            <img :src="captchaUrl" @click="refreshCaptcha" style="border-radius:8px;cursor:pointer;border:1px solid var(--glass-border);height:40px;width:120px;object-fit:cover;flex-shrink:0;" title="点击刷新验证码" />
            <input v-model="form.captcha" placeholder="输入验证码" required maxlength="4" style="flex:1;letter-spacing:3px;font-weight:600;" />
          </div>
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%;border:none;cursor:pointer;justify-content:center;margin-top:8px;" :disabled="loading">
          <span v-if="loading">注册中...</span>
          <span v-else>{{t('user.register')}}</span>
        </button>
        <p style="text-align:center;color:var(--text-muted);font-size:.84rem;margin-top:12px;">
          {{t('user.hasAccount')}} <router-link to="/login" style="color:var(--accent-2);">{{t('user.login')}}</router-link>
        </p>
        <div v-if="okMsg" style="margin-top:12px;padding:10px;background:rgba(108,92,231,0.1);border:1px solid rgba(108,92,231,0.2);border-radius:var(--radius-sm);color:var(--accent-2);font-size:.84rem;text-align:center;">{{okMsg}}</div>
        <div v-if="errMsg" style="margin-top:12px;padding:10px;background:rgba(253,121,168,0.1);border:1px solid rgba(253,121,168,0.2);border-radius:var(--radius-sm);color:var(--accent-3);font-size:.84rem;text-align:center;">{{errMsg}}</div>
      </form>
    </div>
  </section>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { t } from '../i18n/index.js'
import axios from 'axios'
import { useRouter } from 'vue-router'
var rt = useRouter()
var api = axios.create({ baseURL: '/api', timeout: 10000, withCredentials: true })
var account = ref('')
var captchaUrl = ref('')
var form = ref({ password: '', confirmPassword: '', captcha: '' })
var okMsg = ref('')
var errMsg = ref('')
var loading = ref(false)
var passwordMismatch = computed(function() {
  return form.value.confirmPassword.length > 0 && form.value.password !== form.value.confirmPassword
})
async function refreshAccount() {
  try {
    var r = await api.get('/auth/next-account')
    account.value = r.data.account
  } catch(e) { errMsg.value = '获取账号失败' }
}
function refreshCaptcha() {
  captchaUrl.value = '/api/auth/captcha?t=' + Date.now()
}
async function handleRegister() {
  errMsg.value = ''; okMsg.value = ''
  if (!form.value.password || form.value.password.length < 6) {
    errMsg.value = '密码至少6位'
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    errMsg.value = '两次密码不一致'
    return
  }
  if (!form.value.captcha || form.value.captcha.length !== 4) {
    errMsg.value = '请输入4位验证码'
    return
  }
  loading.value = true
  try {
    var r = await api.post('/auth/register', {
      username: account.value,
      password: form.value.password,
      captcha: form.value.captcha
    })
    if (r.data.success) {
      okMsg.value = '注册成功！您的账号是: ' + account.value + '，即将跳转登录...'
      setTimeout(function() { rt.push('/login') }, 2000)
    }
  } catch (e) {
    errMsg.value = e.response?.data?.message || '注册失败，请重试'
    refreshCaptcha()
    form.value.captcha = ''
  }
  loading.value = false
}
onMounted(function() {
  refreshAccount()
  refreshCaptcha()
})
</script>
