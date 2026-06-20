<template>
  <div v-show="visible" id="searchModal">
    <div>
      <div style="display:flex;align-items:center;padding:16px 20px;border-bottom:1px solid var(--glass-border);">
        <i class="fas fa-search" style="color:var(--text-muted);margin-right:12px;"></i>
        <input ref="ir" v-model="q" @input="doSearch" placeholder="搜索..." style="flex:1;background:none;border:none;color:var(--text-primary);font-size:1rem;outline:none;font-family:inherit;">
        <button @click="visible=false" class="search-esc-btn">Esc</button>
      </div>
      <div style="max-height:400px;overflow-y:auto;padding:8px;">
        <div v-for="r in rs" :key="r.id" class="search-result-item" @click="go(r)">
          <i :class="r.type==='blog'?'fas fa-file-alt':'fas fa-cube'" style="color:var(--accent-2);width:20px;"></i>
          <div><div style="font-weight:500;font-size:.9rem;">{{r.title}}</div><div style="font-size:.78rem;color:var(--text-muted);">{{r.summary}}</div></div>
        </div>
        <div v-if="q&&rs.length===0" style="padding:24px;text-align:center;color:var(--text-muted);font-size:.9rem;">{{t('common.emptyPosts')}}</div>
      </div>
    </div>
  </div>
</template>
<script setup>
import{ref,nextTick,onMounted,onUnmounted}from'vue'
import{search}from'../api/index.js'
import{useRouter}from'vue-router'
import{t}from'../i18n/index.js'
var rt=useRouter(),visible=ref(false),q=ref(''),rs=ref([]),ir=ref(null)
var tid=null
function doSearch(){clearTimeout(tid);if(!q.value.trim()){rs.value=[];return}tid=setTimeout(async()=>{try{rs.value=(await search(q.value)).data}catch(e){rs.value=[]}},300)}
function go(r){visible.value=false;q.value='';rs.value=[];if(r.type==='blog')rt.push('/blog/'+r.id);else if(r.type==='project')rt.push('/projects')}
function h(e){if((e.ctrlKey||e.metaKey)&&e.key==='k'){e.preventDefault();visible.value=true;nextTick(()=>ir.value?.focus())}if(e.key==='Escape')visible.value=false}
onMounted(()=>document.addEventListener('keydown',h))
onUnmounted(()=>document.removeEventListener('keydown',h))
</script>