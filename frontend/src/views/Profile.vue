<template>
  <section class="section">
    <div style="max-width:480px;margin:0 auto;">
      <!-- Avatar Upload -->
      <div style="text-align:center;margin-bottom:28px;">
        <div class="avatar-wrap" @click="triggerUpload" :title="t('user.avatarHint')">
          <img v-if="previewAvatar" :src="previewAvatar" class="avatar-img" />
          <div v-else class="avatar-placeholder">{{avatarInitial}}</div>
          <div class="avatar-overlay"><i class="fas fa-camera"></i></div>
        </div>
        <input ref="fileInput" type="file" accept="image/*" style="display:none" @change="onFileSelect" />
        <h2 style="font-size:1.3rem;margin-top:4px;">{{displayName || u?.username || t('user.profile')}}</h2>
        <span style="font-size:.8rem;color:var(--text-muted);">@{{u?.username}}</span>
      </div>

      <div v-if="u" class="profile-card" style="padding:24px;">
        <!-- Display Name -->
        <div class="form-group">
          <label>{{t('user.displayName')}}</label>
          <input v-model="displayName" :placeholder="t('user.displayNamePH')" />
        </div>

        <!-- Account (read-only) -->
        <div class="profile-row"><span class="profile-label">{{t('user.username')}}</span><span class="profile-value">{{u.username}}</span></div>
        <div class="profile-row"><span class="profile-label">{{t('user.role')}}</span><span class="profile-value accent">{{u.role}}</span></div>
        <div class="profile-row"><span class="profile-label">{{t('user.joined')}}</span><span class="profile-value">{{u.createdAt?new Date(u.createdAt).toLocaleDateString():'-'}}</span></div>

        <!-- Save Button -->
        <button v-if="hasChanges" @click="saveProfile" class="btn btn-primary" style="width:100%;border:none;cursor:pointer;justify-content:center;margin-top:16px;">
          <i class="fas fa-save"></i> {{t('user.save')}}
        </button>
        <div v-if="savedMsg" style="margin-top:10px;padding:8px;background:rgba(0,206,201,0.1);border:1px solid rgba(0,206,201,0.2);border-radius:8px;color:#00cec9;font-size:.84rem;text-align:center;">{{savedMsg}}</div>

        <!-- Admin shortcut -->
        <a v-if="u.role==='ADMIN'" href="/admin/dashboard" class="admin-shortcut">
          <i class="fas fa-shield-alt"></i> 进入管理后台
        </a>

        <button @click="logout" class="logout-btn">🚪 {{t('user.logout')}}</button>
      </div>
      <div v-else class="empty-state"><p>{{t('user.loginTitle')}}</p><router-link to="/login" class="btn btn-primary" style="display:inline-block;margin-top:12px">}</router-link></div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { t, getLang } from '../i18n/index.js'
import { updateProfile, uploadAvatar } from '../api/index.js'

var u = ref(null)
var displayName = ref('')
var avatar = ref('')
var previewAvatar = ref('')
var savedMsg = ref('')
var fileInput = ref(null)
var selectedFile = ref(null)

var hasChanges = computed(function() {
  if (!u.value) return false
  if (selectedFile.value) return true
  return displayName.value !== (u.value.displayName || '')
})

var avatarInitial = computed(function() {
  if (!u.value) return '?'
  var dn = displayName.value || u.value.username || ''
  return dn.charAt(0).toUpperCase()
})

function triggerUpload() { fileInput.value?.click() }

function onFileSelect(e) {
  var file = e.target.files?.[0]
  if (!file) return
  selectedFile.value = file
  var reader = new FileReader()
  reader.onload = function(ev) { previewAvatar.value = ev.target.result }
  reader.readAsDataURL(file)
}

async function saveProfile() {
  if (!u.value) return
  try {
    if (selectedFile.value) {
      var fd = new FormData()
      fd.append("file", selectedFile.value)
      try {
        var resp = await uploadAvatar(fd)
        var ud = resp.data || resp
        var userData = { id: ud.id, username: ud.username, role: ud.role, avatar: ud.avatar || "", displayName: ud.displayName || "", createdAt: ud.createdAt }
        u.value = userData
        try { localStorage.setItem("user", JSON.stringify(userData)) } catch (e) {}
        previewAvatar.value = ud.avatar || ""
        avatar.value = ud.avatar || ""
        displayName.value = ud.displayName || ""
        selectedFile.value = null
        savedMsg.value = t("user.saved")
        setTimeout(function() { savedMsg.value = "" }, 2000)
        return
      } catch(e) {
        savedMsg.value = "头像上传失败，请重试"
        setTimeout(function() { savedMsg.value = "" }, 3000)
        return
      }
    }
    if (displayName.value === (u.value.displayName || "")) {
      savedMsg.value = t("user.saved")
      setTimeout(function() { savedMsg.value = "" }, 2000)
      return
    }
    var r = await updateProfile({ displayName: displayName.value || "" })
    var ud = r.data || r
    var userData = { id: ud.id, username: ud.username, role: ud.role, avatar: ud.avatar || "", displayName: ud.displayName || "", createdAt: ud.createdAt }
    u.value = userData
    try { localStorage.setItem("user", JSON.stringify(userData)) } catch (e) {}
    previewAvatar.value = ud.avatar || ""
    avatar.value = ud.avatar || ""
    displayName.value = ud.displayName || ""
    selectedFile.value = null
    savedMsg.value = t("user.saved")
    setTimeout(function() { savedMsg.value = "" }, 2000)
  } catch (e) {
    savedMsg.value = "Error: " + (e.message || "save failed")
  }
}

onMounted(function() {
  try {
    var x = localStorage.getItem('user')
    if (x) {
      u.value = JSON.parse(x)
      displayName.value = u.value.displayName || ''
      avatar.value = u.value.avatar || ''
      previewAvatar.value = u.value.avatar || ''
    }
  } catch(e) {}
})

function logout() {
  try { localStorage.removeItem('user') } catch(e) {}
  window.location.href = '/'
}
</script>

<style scoped>
.avatar-wrap{position:relative;width:80px;height:80px;margin:0 auto;border-radius:50%;cursor:pointer;overflow:hidden;border:3px solid var(--accent-2);transition:all .3s ease}
.avatar-wrap:hover{border-color:var(--accent-1);transform:scale(1.05)}
.avatar-img{width:100%;height:100%;object-fit:cover;display:block}
.avatar-placeholder{width:100%;height:100%;background:var(--accent-gradient);display:flex;align-items:center;justify-content:center;font-size:1.6rem;color:#fff;font-weight:700}
.avatar-overlay{position:absolute;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,.4);display:flex;align-items:center;justify-content:center;opacity:0;transition:opacity .3s ease;border-radius:50%}
.avatar-wrap:hover .avatar-overlay{opacity:1}
.avatar-overlay i{color:#fff;font-size:1.2rem}
.admin-shortcut{display:flex;align-items:center;justify-content:center;gap:8px;width:100%;margin-top:18px;padding:12px;border-radius:100px;background:linear-gradient(135deg,#6c5ce7,#a29bfe);color:#fff!important;font-size:.9rem;font-weight:600;text-decoration:none;cursor:pointer;border:none;transition:all .3s ease}
.admin-shortcut:hover{transform:translateY(-2px);box-shadow:0 8px 30px rgba(108,92,231,.4)}
.logout-btn{width:100%;margin-top:10px;padding:12px;border:1px solid rgba(253,121,168,0.2);border-radius:100px;background:transparent;color:var(--accent-3);font-size:.9rem;cursor:pointer;transition:all .3s ease}
.logout-btn:hover{background:rgba(253,121,168,0.1)}
.profile-row{display:flex;justify-content:space-between;padding:12px 16px;background:var(--bg-card);border-radius:var(--radius-sm);margin-bottom:6px}
.profile-label{color:var(--text-secondary);font-size:.86rem}
.profile-value{color:var(--text-primary);font-weight:500;font-size:.86rem}
.profile-value.accent{color:var(--accent-2)}
.form-group{margin-bottom:16px}
.form-group label{display:block;color:var(--text-secondary);font-size:.84rem;margin-bottom:6px;font-weight:500}
.form-group input{width:100%;padding:12px 16px;background:var(--bg-primary);border:1px solid var(--glass-border);border-radius:var(--radius-sm);color:var(--text-primary);font-size:.88rem;transition:all .3s ease;font-family:var(--font-sans);outline:none}
.form-group input:focus{border-color:var(--accent-1);box-shadow:0 0 0 3px rgba(108,92,231,.15)}
</style>
