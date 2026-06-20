<template>
  <section class="section" style="display:flex;align-items:center;justify-content:center;min-height:60vh;text-align:center;">
    <div>
      <div style="font-size:6rem;font-weight:800;background:var(--accent-gradient);-webkit-background-clip:text;-webkit-text-fill-color:transparent;background-clip:text;line-height:1;margin-bottom:16px;">404</div>
      <h2 style="font-size:1.5rem;margin-bottom:8px;color:var(--text-primary);">{{t('error.notfound')}}</h2>
      <p style="color:var(--text-muted);margin-bottom:32px;">{{t('error.notfoundDesc')}}</p>
      <router-link to="/" class="btn btn-primary"><i class="fas fa-home"></i> {{t("nav.home")}}</router-link>
    </div>
  </section>
</template>
<script setup>import { t } from "../i18n/index.js"</script>
