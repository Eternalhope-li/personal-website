<template>
  <div class="home-page">
    <!-- ===== HERO SECTION ===== -->
    <section class="hero">
      <div class="hero-gradient-mesh"></div>
      <div class="floating-particles" id="particleContainer"></div>
      <div class="hero-content">
        <div class="hero-badge fade-in-down">
          <span class="dot"></span> >{{t('hero.badge')}}
        </div>
        <h1 class="fade-in-up" style="animation-delay:0.2s">
          <span class="hero-line">{{t('hero.greeting')}}</span>
          <span class="gradient-text hero-name shimmer-text">{{nameDisplay}}</span><br>
          <span class="hero-role">{{roleDisplay}}</span>
          <span class="typewriter-cursor" :class="{blink:cursorBlink}">|</span>
        </h1>
        <p class="hero-desc fade-in-up" style="animation-delay:0.4s">{{t('hero.desc')}}</p>
        <div class="hero-actions fade-in-up" style="animation-delay:0.6s">
          <router-link to="/projects" class="btn btn-primary btn-ripple">{{t('hero.viewWork')}} <i class="fas fa-arrow-right btn-icon-move"></i></router-link>
          <router-link to="/contact" class="btn btn-secondary btn-ripple">{{t('hero.getInTouch')}} <i class="fas fa-envelope btn-icon-move"></i></router-link>
        </div>
      </div>
      <div class="scroll-indicator">
        <div class="scroll-line"></div>
        <span>{{t('hero.scroll')}}</span>
      </div>
    </section>

    <!-- ===== ABOUT SECTION ===== -->
    <section ref="aboutSec" class="section reveal-section" data-anim="left">
      <div class="section-bg-glow"></div>
      <div class="reveal-content">
                <h2 class="section-title reveal-text">{{t('about.title')}}</h2>
        <div class="stats-row" ref="statsRow">
          <div class="stat-card" v-for="(s,si) in stats" :key="si" :style="{'--i': si}">
            <span class="stat-num" :data-target="s.num">0</span>
            <span class="stat-label">{{s.label}}</span>
          </div>
        </div>
        <div class="about-cards">
          <div class="about-card" v-for="(item,ai) in aboutItems" :key="ai" :style="{'--i': ai}">
            <div class="about-icon-wrap"><span class="about-icon">{{item.icon}}</span></div>
            <h3>{{t(item.titleKey)}}</h3>
            <p>{{t(item.descKey)}}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- ===== PROJECTS SECTION ===== -->
    <section ref="projectsSec" class="section reveal-section alt-bg" data-anim="left">
      <div class="reveal-content">
                <h2 class="section-title reveal-text">{{t('projects.title')}}</h2>
        <p class="section-desc">{{t('projects.subtitle')}}</p>
        <div v-if="projects.length>0" class="project-grid">
          <div v-for="(p,i) in projects.slice(0,3)" :key="p.id" class="project-card" :style="{'--i': i}" @click="$router.push('/projects')">
            <div class="project-card-inner">
              <div class="project-image"><span class="project-icon">🚀</span></div>
              <div class="project-info">
                <h3>{{p.title}}</h3>
                <p>{{p.description}}</p>
              </div>
            </div>
          </div>
        </div>
        <div v-if="projects.length===0" class="empty-state"><p>{{t('common.emptyProjects')}}</p></div>
        <div v-if="projects.length>0" class="section-cta">
          <router-link to="/projects" class="btn btn-outline btn-ripple">{{t('projects.viewAll')}} <i class="fas fa-arrow-right btn-icon-move"></i></router-link>
        </div>
      </div>
    </section>

    <!-- ===== BLOG SECTION ===== -->
    <section ref="blogSec" class="section reveal-section" data-anim="left">
      <div class="section-bg-glow"></div>
      <div class="reveal-content">
                <h2 class="section-title reveal-text">{{t('blog.insights')}}</h2>
        <p class="section-desc">{{t('blog.insightsSubtitle')}}</p>
        <div v-if="posts.length>0" class="blog-grid">
          <div v-for="(post,i) in posts.slice(0,3)" :key="post.id" class="blog-card" :style="{'--i': i}" @click="$router.push('/blog/'+post.id)">
            <div class="blog-card-inner">
              <div class="blog-image"><span class="blog-icon">📝</span></div>
              <div class="blog-info">
                <h3>{{post.title}}</h3>
                <p>{{post.summary||stripHtml(post.content).substring(0,120)}}</p>
                <div class="blog-meta">
                  <span class="blog-date">{{new Date(post.createdAt).toLocaleDateString()}}</span>
                  <span class="blog-link">{{t('blog.readMore')}} →</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="posts.length===0" class="empty-state"><p>{{t('common.emptyPosts')}}</p></div>
        <div v-if="posts.length>0" class="section-cta">
          <router-link to="/blog" class="btn btn-outline btn-ripple">{{t('blog.readAll')}} <i class="fas fa-arrow-right btn-icon-move"></i></router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { t } from '../i18n/index.js'
import { getPosts, getProjects } from '../api/index.js'

var posts = ref([]), projects = ref([])
function sortByDate(arr) { return arr.sort(function(a,b) { return new Date(b.createdAt) - new Date(a.createdAt) }) }

function stripHtml(html) { var d = document.createElement('div'); d.innerHTML = html; return d.textContent || d.innerText || '' }

var aboutSec = ref(null), projectsSec = ref(null), blogSec = ref(null)
var statsRow = ref(null)
var nameDisplay = ref("电饭煲")
var roleDisplay = ref("")
var cursorBlink = ref(false)
var obsList = []

var stats = [
  { num: 3, label: t("about.yearsLabel") },
  { num: 20, label: t("about.projectsLabel") },
  { num: 15, label: t("about.clientsLabel") }
]

var aboutItems = [
  { icon: "⚙️", titleKey: "about.backend", descKey: "about.backendDesc" },
  { icon: "🎨", titleKey: "about.frontend", descKey: "about.frontendDesc" },
  { icon: "☁️", titleKey: "about.cloud", descKey: "about.cloudDesc" }
]

// === Particles ===
function createParticles() {
  var c = document.getElementById("particleContainer")
  if (!c) return
  c.innerHTML = ""
  var colors = ["var(--accent-1)", "var(--accent-2)", "var(--accent-3)", "#00cec9"]
  for (var i = 0; i < 50; i++) {
    var p = document.createElement("span")
    var size = 2 + Math.random() * 6
    p.className = "particle"
    p.style.cssText =
      "width:" + size + "px;height:" + size + "px;" +
      "left:" + (Math.random() * 100) + "%;top:" + (Math.random() * 100) + "%;" +
      "background:" + colors[Math.floor(Math.random() * colors.length)] + ";" +
      "animation-delay:" + (Math.random() * 15) + "s;" +
      "animation-duration:" + (15 + Math.random() * 25) + "s;" +
      "opacity:" + (0.1 + Math.random() * 0.3)
    c.appendChild(p)
  }
}

// === Scroll Reveal ===
function initReveal() {
  var list = []
  document.querySelectorAll(".reveal-section").forEach(function(el) {
    if (el.classList.contains("visible")) return
    var o = new IntersectionObserver(function(entries) {
      entries.forEach(function(entry) {
        if (entry.isIntersecting) {
          el.classList.add("visible")
          // Trigger staggered child animations
          el.querySelectorAll(".stat-card, .about-card, .project-card, .blog-card").forEach(function(child, idx) {
            child.style.setProperty("--i", idx)
            child.classList.add("anim-in")
          })
          o.unobserve(el)
        }
      })
    }, { threshold: 0.08, rootMargin: "0px 0px -50px 0px" })
    o.observe(el)
    list.push(o)
  })
  return list
}

// === Counter ===
function animateCounters() {
  document.querySelectorAll(".stat-num").forEach(function(el) {
    var target = parseInt(el.getAttribute("data-target")) || 0
    if (target === 0) return
    var duration = 2000, start = performance.now()
    function step(now) {
      var p = Math.min((now - start) / duration, 1)
      var eased = 1 - Math.pow(1 - p, 3)
      el.textContent = Math.floor(eased * target)
      if (p < 1) requestAnimationFrame(step)
      else el.textContent = target
    }
    requestAnimationFrame(step)
  })
}

onMounted(function() {
  // Fallback: ensure all content visible after 3s
  var revealFallback = setTimeout(function() {
    document.querySelectorAll(".reveal-section, .reveal-card, .about-card, .project-card, .blog-card, .stat-card").forEach(function(el) {
      el.classList.add("visible")
      el.style.opacity = "1"
      el.style.transform = "translateY(0)"
    })
  }, 3000)

  // Load data
  getPosts().then(function(d) { posts.value = sortByDate(d); setTimeout(initReveal, 100) }).catch(function(){})
  getProjects().then(function(d) { projects.value = sortByDate(d); setTimeout(initReveal, 100) }).catch(function(){})
  
  createParticles()
  setTimeout(initReveal, 200)
  
  // Counter observer
  if (statsRow.value) {
    var co = new IntersectionObserver(function(entries) {
      entries.forEach(function(entry) {
        if (entry.isIntersecting) { animateCounters(); co.unobserve(entry.target) }
      })
    }, { threshold: 0.3 })
    co.observe(statsRow.value)
  }
  
  // Typewriter
  var roleFull = t("hero.creative")
  var idx = 0
  cursorBlink.value = true
  var tid = setInterval(function() {
    if (idx < roleFull.length) { roleDisplay.value += roleFull[idx]; idx++ }
    else { clearInterval(tid); cursorBlink.value = false }
  }, 60)
})

onUnmounted(function() { obsList.forEach(function(o) { o.disconnect() }) })
</script>

<style scoped>
.hero{position:relative;min-height:100vh;display:flex;align-items:center;justify-content:center;text-align:center;padding:60px 24px;overflow:hidden}
.hero-gradient-mesh{position:absolute;top:0;left:0;width:100%;height:100%;background:radial-gradient(ellipse at 20% 50%,rgba(108,92,231,.15) 0%,transparent 50%),radial-gradient(ellipse at 80% 20%,rgba(253,121,168,.1) 0%,transparent 50%),radial-gradient(ellipse at 50% 80%,rgba(0,206,201,.08) 0%,transparent 50%);pointer-events:none;z-index:0}
.hero-content{position:relative;z-index:5;max-width:680px;width:100%}
.hero-badge{display:inline-flex;align-items:center;gap:6px;font-size:.75rem;font-weight:600;text-transform:uppercase;letter-spacing:1.5px;color:var(--accent-2);padding:6px 16px;border-radius:100px;background:rgba(162,155,254,.1);border:1px solid rgba(162,155,254,.15);margin-bottom:16px}
.hero-badge .dot{width:5px;height:5px;border-radius:50%;background:var(--accent-2);animation:dotPulse 2s ease-in-out infinite}
@keyframes dotPulse{0%,100%{opacity:1}50%{opacity:.3}}
.hero h1{font-size:clamp(1.8rem,4vw,3.2rem);line-height:1.25;margin-bottom:14px;font-weight:700}
.hero-name{font-size:clamp(2.2rem,5vw,4rem);font-weight:800;display:block;margin:2px 0}
.hero-role{font-weight:500;color:var(--accent-2);font-size:1rem}
.hero-desc{font-size:1rem;color:var(--text-secondary);max-width:480px;margin:0 auto 28px;line-height:1.7}
.hero-actions{display:flex;gap:12px;justify-content:center;flex-wrap:wrap}

/* Scroll Indicator */
.scroll-indicator{position:absolute;bottom:30px;left:50%;transform:translateX(-50%);display:flex;flex-direction:column;align-items:center;gap:8px;z-index:10;animation:fadeInUp 1s ease .8s both}
.scroll-line{width:1px;height:40px;background:linear-gradient(to bottom,var(--accent-2),transparent);animation:scrollLine 2s ease-in-out infinite}
.scroll-indicator span{font-size:.65rem;text-transform:uppercase;letter-spacing:2px;color:var(--text-muted)}
@keyframes scrollLine{0%{opacity:1;transform:scaleY(1)}50%{opacity:.5;transform:scaleY(.6)}100%{opacity:1;transform:scaleY(1)}}

/* Fade animations */
.fade-in-up{opacity:0;transform:translateY(20px);animation:fadeInUp .8s cubic-bezier(.22,1,.36,1) forwards}
.fade-in-down{opacity:0;transform:translateY(-10px);animation:fadeInDown .6s cubic-bezier(.22,1,.36,1) forwards}
@keyframes fadeInUp{to{opacity:1;transform:translateY(0)}}
@keyframes fadeInDown{to{opacity:1;transform:translateY(0)}}

/* Sections */
.section{padding:80px 24px;position:relative;z-index:2;overflow:hidden}
.alt-bg{background:rgba(255,255,255,.02)}
.section-bg-glow{position:absolute;top:-50%;left:-20%;width:140%;height:200%;background:radial-gradient(ellipse at center,rgba(108,92,231,.04) 0%,transparent 60%);pointer-events:none}
.section-title{font-size:clamp(1.5rem,3vw,2.2rem);font-weight:700;text-align:center;margin-bottom:8px;background:var(--accent-gradient);-webkit-background-clip:text;-webkit-text-fill-color:transparent;background-clip:text}
.section-desc{text-align:center;color:var(--text-muted);font-size:.9rem;max-width:500px;margin:0 auto 32px}

/* Reveal */
.reveal-section{opacity:0;transform:translateY(40px);transition:opacity .8s cubic-bezier(.22,1,.36,1),transform .8s cubic-bezier(.22,1,.36,1)}
.reveal-section.visible{opacity:1;transform:translateY(0)}
.reveal-text{opacity:0;transform:translateY(20px);transition:opacity .6s ease .2s,transform .6s ease .2s}.reveal-section.visible .reveal-text{opacity:1;transform:translateY(0)}

/* Stats */
.stats-row{display:grid;grid-template-columns:repeat(3,1fr);gap:16px;max-width:420px;margin:0 auto 32px}
.stat-card{padding:20px 8px;border-radius:12px;background:var(--bg-card);border:1px solid var(--glass-border);text-align:center;transform:translateY(20px);opacity:0;transition:all .6s cubic-bezier(.22,1,.36,1);transition-delay:calc(var(--i,0) * 0.1s)}
.reveal-section.visible .stat-card{opacity:1;transform:translateY(0)}
.stat-card:hover{background:var(--bg-card-hover);transform:translateY(-2px) scale(1.02)}
.stat-num{display:block;font-size:1.8rem;font-weight:800;background:var(--accent-gradient);-webkit-background-clip:text;-webkit-text-fill-color:transparent;background-clip:text;line-height:1.2;font-variant-numeric:tabular-nums}
.stat-label{display:block;font-size:.76rem;color:var(--text-muted);margin-top:2px}

/* About Cards */
.about-cards{display:grid;grid-template-columns:repeat(3,1fr);gap:20px;max-width:700px;margin:0 auto}
.about-card{padding:28px 20px;border-radius:16px;background:var(--bg-card);border:1px solid var(--glass-border);text-align:center;transform:translateY(30px);opacity:0;transition:all .6s cubic-bezier(.22,1,.36,1);transition-delay:calc(var(--i,0) * 0.12s);cursor:default}
.reveal-section.visible .about-card{opacity:1;transform:translateY(0)}
.about-card:hover{background:var(--bg-card-hover);transform:translateY(-4px);box-shadow:0 8px 32px rgba(108,92,231,.1)}
.about-icon-wrap{width:48px;height:48px;border-radius:12px;background:linear-gradient(135deg,rgba(108,92,231,.15),rgba(162,155,254,.1));display:flex;align-items:center;justify-content:center;margin:0 auto 12px}
.about-icon{font-size:1.4rem}
.about-card h3{font-size:.95rem;font-weight:600;margin-bottom:6px}
.about-card p{font-size:.82rem;color:var(--text-muted);line-height:1.5}

/* Project / Blog Grids */
.project-grid,.blog-grid{display:grid;grid-template-columns:repeat(3,1fr);gap:20px;max-width:900px;margin:0 auto 24px}
.project-card,.blog-card{border-radius:16px;background:var(--bg-card);border:1px solid var(--glass-border);overflow:hidden;cursor:pointer;transform:translateY(30px);opacity:0;transition:all .6s cubic-bezier(.22,1,.36,1);transition-delay:calc(var(--i,0) * 0.12s)}
.reveal-section.visible .project-card,.reveal-section.visible .blog-card{opacity:1;transform:translateY(0)}
.project-card:hover,.blog-card:hover{background:var(--bg-card-hover);transform:translateY(-6px);box-shadow:0 12px 40px rgba(108,92,231,.12)}
.project-card-inner,.blog-card-inner{height:100%;display:flex;flex-direction:column}
.project-image,.blog-image{height:100px;display:flex;align-items:center;justify-content:center;background:linear-gradient(135deg,var(--accent-1),#a29bfe);position:relative}
.blog-image{background:linear-gradient(135deg,var(--accent-2),#00cec9)}
.project-icon,.blog-icon{font-size:2rem;position:relative;z-index:1;filter:drop-shadow(0 2px 8px rgba(0,0,0,.2))}
.project-info,.blog-info{padding:16px 18px 20px;flex:1;display:flex;flex-direction:column}
.project-info h3,.blog-info h3{font-size:.9rem;font-weight:600;margin-bottom:6px;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden;line-height:1.35}
.project-info p,.blog-info p{font-size:.8rem;color:var(--text-muted);display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden;line-height:1.5;flex:1}
.blog-meta{display:flex;justify-content:space-between;align-items:center;margin-top:10px;font-size:.76rem}
.blog-date{color:var(--text-muted)}
.blog-link{color:var(--accent-2);font-weight:600}
.section-cta{text-align:center;margin-top:8px}
.empty-state{padding:40px 20px;text-align:center;color:var(--text-muted);font-size:.9rem}

/* Particles */
.floating-particles{position:absolute;top:0;left:0;width:100%;height:100%;overflow:hidden;pointer-events:none;z-index:1}
.particle{position:absolute;border-radius:50%;will-change:transform;animation:particleFloat 18s ease-in-out infinite;pointer-events:none}
@keyframes particleFloat{0%,100%{transform:translateY(0) translateX(0) scale(1);opacity:0}10%{opacity:1}50%{transform:translateY(-80px) translateX(20px) scale(1.15)}90%{opacity:1}100%{transform:translateY(-180px) translateX(-15px) scale(.6);opacity:0}}

/* Shimmer */
.shimmer-text{background:linear-gradient(90deg,var(--accent-1) 0%,#a29bfe 25%,#fd79a8 50%,#a29bfe 75%,var(--accent-1) 100%);background-size:300% 100%;-webkit-background-clip:text;-webkit-text-fill-color:transparent;background-clip:text;animation:shimmer 4s ease-in-out infinite}
@keyframes shimmer{0%,100%{background-position:0% 50%}50%{background-position:100% 50%}}

/* Typewriter */
.typewriter-cursor{font-weight:100;color:var(--accent-2);margin-left:2px;font-size:.9em}.typewriter-cursor.blink{animation:cursorBlink .8s step-end infinite}
@keyframes cursorBlink{0%,100%{opacity:1}50%{opacity:0}}

/* Responsive */
@media(max-width:768px){
  .section{padding:48px 16px}
  .about-cards,.project-grid,.blog-grid{grid-template-columns:1fr;gap:14px}
  .stats-row{gap:10px}
  .stat-card{padding:14px 6px}
  .hero{padding:60px 16px 40px}
  .scroll-indicator{display:none}
}
@media(min-width:769px) and (max-width:1024px){
  .about-cards,.project-grid,.blog-grid{gap:14px}
}
</style>



