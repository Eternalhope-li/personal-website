// ========================================
// Personal Website - Enhanced JavaScript
// ========================================

document.addEventListener("DOMContentLoaded", function () {
  initMobileMenu();
  initScrollAnimations();
  initTypingEffect();
  initContactForm();
  initActiveNav();
  initThemeToggle();
  initSearchModal();
  initCommentForm();
});

/* --- Mobile Menu --- */
function initMobileMenu() {
  var toggle = document.querySelector(".menu-toggle");
  var navLinks = document.querySelector(".nav-links");
  if (!toggle || !navLinks) return;

  toggle.addEventListener("click", function () {
    this.classList.toggle("active");
    navLinks.classList.toggle("open");
  });

  navLinks.querySelectorAll("a").forEach(function (link) {
    link.addEventListener("click", function () {
      toggle.classList.remove("active");
      navLinks.classList.remove("open");
    });
  });
}

/* --- Scroll Animations --- */
function initScrollAnimations() {
  var elements = document.querySelectorAll(".fade-in, .fade-in-left, .fade-in-right");
  if (elements.length === 0) return;

  var observer = new IntersectionObserver(
    function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          entry.target.classList.add("visible");
          observer.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.1, rootMargin: "0px 0px -50px 0px" }
  );

  elements.forEach(function (el) { observer.observe(el); });
}

/* --- Typing Effect --- */
function initTypingEffect() {
  var target = document.getElementById("typing-target");
  if (!target) return;

  var phrases = JSON.parse(target.getAttribute("data-phrases") || '["Developer","Designer","Creator"]');
  var phraseIndex = 0, charIndex = 0, isDeleting = false;

  function type() {
    var currentPhrase = phrases[phraseIndex];
    if (isDeleting) {
      charIndex--;
    } else {
      charIndex++;
    }
    target.textContent = currentPhrase.substring(0, charIndex);

    if (!isDeleting && charIndex === currentPhrase.length) {
      isDeleting = true;
      setTimeout(type, 2000); return;
    }
    if (isDeleting && charIndex === 0) {
      isDeleting = false;
      phraseIndex = (phraseIndex + 1) % phrases.length;
      setTimeout(type, 500); return;
    }
    setTimeout(type, isDeleting ? 40 : 80);
  }
  setTimeout(type, 1000);
}

/* --- Theme Toggle --- */
function initThemeToggle() {
  var btn = document.getElementById("themeToggle");
  if (!btn) return;

  var icon = btn.querySelector("i");

  // Load saved theme
  var saved = localStorage.getItem("theme");
  if (saved === "light") {
    document.body.classList.add("light");
    icon.className = "fas fa-sun";
  } else {
    icon.className = "fas fa-moon";
  }

  btn.addEventListener("click", function () {
    document.body.classList.toggle("light");
    if (document.body.classList.contains("light")) {
      localStorage.setItem("theme", "light");
      icon.className = "fas fa-sun";
    } else {
      localStorage.setItem("theme", "dark");
      icon.className = "fas fa-moon";
    }
  });
}

/* --- Search Modal --- */
function initSearchModal() {
  // "Ctrl+K" shortcut and "Search" button
  var searchContainer = document.createElement("div");
  searchContainer.id = "searchModal";
  searchContainer.style.cssText = "display:none;position:fixed;top:0;left:0;width:100%;height:100%;z-index:9999;background:rgba(0,0,0,0.6);backdrop-filter:blur(8px);justify-content:center;align-items:flex-start;padding-top:15vh;";
  searchContainer.innerHTML = `
    <div style="max-width:600px;width:90%;background:var(--bg-secondary);border-radius:16px;border:1px solid var(--glass-border);overflow:hidden;box-shadow:0 20px 60px rgba(0,0,0,0.5);">
      <div style="display:flex;align-items:center;padding:16px 20px;border-bottom:1px solid var(--glass-border);">
        <i class="fas fa-search" style="color:var(--text-muted);margin-right:12px;"></i>
        <input type="text" id="searchInput" placeholder="Search blog posts, projects..." style="flex:1;background:none;border:none;color:var(--text-primary);font-size:1.1rem;outline:none;font-family:inherit;">
        <button onclick="document.getElementById('searchModal').style.display='none'" style="background:var(--glass-bg);border:1px solid var(--glass-border);border-radius:8px;color:var(--text-secondary);padding:6px 12px;cursor:pointer;font-size:0.8rem;">Esc</button>
      </div>
      <div id="searchResults" style="max-height:400px;overflow-y:auto;padding:8px;"></div>
    </div>`;
  document.body.appendChild(searchContainer);

  // Keyboard shortcut
  document.addEventListener("keydown", function (e) {
    if ((e.ctrlKey || e.metaKey) && e.key === "k") {
      e.preventDefault();
      openSearch();
    }
    if (e.key === "Escape") {
      searchContainer.style.display = "none";
    }
  });

  var searchInput = document.getElementById("searchInput");
  var searchResults = document.getElementById("searchResults");

  searchInput.addEventListener("input", function () {
    var q = this.value.trim().toLowerCase();
    if (q.length < 2) { searchResults.innerHTML = '<div style="padding:20px;color:var(--text-muted);text-align:center;">Type at least 2 characters to search</div>'; return; }
    performSearch(q);
  });

  window.openSearch = function () {
    searchContainer.style.display = "flex";
    setTimeout(function () { searchInput.focus(); }, 100);
  };
}

function performSearch(q) {
  var resultsDiv = document.getElementById("searchResults");
  resultsDiv.innerHTML = '<div style="padding:20px;color:var(--text-muted);text-align:center;"><i class="fas fa-spinner fa-spin"></i> Searching...</div>';

  fetch("/search?q=" + encodeURIComponent(q))
    .then(function (r) { return r.json(); })
    .then(function (data) {
      if (!data || data.length === 0) {
        resultsDiv.innerHTML = '<div style="padding:30px;color:var(--text-muted);text-align:center;"><i class="fas fa-search" style="font-size:2rem;display:block;margin-bottom:12px;opacity:0.3;"></i>No results found for "' + q + '"</div>';
        return;
      }
      var html = "";
      data.forEach(function (item) {
        var badge = item.type === "blog" ? "Blog" : "Project";
        var link = item.type === "blog" ? "/blog/" + item.id : "/projects";
        html += '<a href="' + link + '" style="display:block;padding:12px 20px;border-radius:8px;transition:background 0.2s;text-decoration:none;" onmouseover="this.style.background=\'var(--glass-bg)\'" onmouseout="this.style.background=\'transparent\'">';
        html += '<div style="display:flex;align-items:center;gap:10px;">';
        html += '<span style="font-size:0.7rem;padding:2px 8px;border-radius:4px;background:var(--accent-1);color:#fff;font-weight:600;">' + badge + '</span>';
        html += '<span style="color:var(--text-primary);font-weight:500;">' + item.title + '</span>';
        html += '</div>';
        if (item.summary) html += '<p style="color:var(--text-muted);font-size:0.85rem;margin:4px 0 0 0;">' + item.summary.substring(0, 80) + '</p>';
        html += '</a>';
      });
      resultsDiv.innerHTML = html;
    })
    .catch(function () {
      resultsDiv.innerHTML = '<div style="padding:20px;color:var(--accent-3);text-align:center;">Search failed. Please try again.</div>';
    });
}

/* --- Contact Form --- */
function initContactForm() {
  var form = document.getElementById("contact-form");
  if (!form) return;

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    var btn = form.querySelector('button[type="submit"]');
    var orig = btn.textContent;

    var data = {
      name: form.querySelector("#name").value,
      email: form.querySelector("#email").value,
      subject: form.querySelector("#subject").value,
      content: form.querySelector("#content").value
    };

    if (!data.name || !data.email || !data.content) {
      showFormMessage(form, "Please fill in all required fields.", "error");
      return;
    }

    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending...';

    try {
      var resp = await fetch("/api/contact", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      });
      var result = await resp.json();
      if (result.success) {
        showFormMessage(form, result.message, "success");
        form.reset();
      } else {
        showFormMessage(form, "Something went wrong.", "error");
      }
    } catch (err) {
      showFormMessage(form, "Network error. Please try again.", "error");
    } finally {
      btn.disabled = false;
      btn.textContent = orig;
    }
  });
}

function showFormMessage(form, msg, type) {
  var existing = form.querySelector(".form-message");
  if (existing) existing.remove();

  var el = document.createElement("div");
  el.className = "form-message";
  el.style.cssText = "padding:12px 16px;border-radius:8px;margin-bottom:20px;font-size:0.9rem;transition:opacity 0.3s;";
  if (type === "success") {
    el.style.background = "rgba(0,184,148,0.1)";
    el.style.border = "1px solid rgba(0,184,148,0.3)";
    el.style.color = "#00b894";
    el.textContent = "\u2713 " + msg;
  } else {
    el.style.background = "rgba(253,121,168,0.1)";
    el.style.border = "1px solid rgba(253,121,168,0.3)";
    el.style.color = "#fd79a8";
    el.textContent = "\u2717 " + msg;
  }
  form.insertBefore(el, form.firstChild);
  setTimeout(function () { el.style.opacity = "0"; setTimeout(function () { el.remove(); }, 300); }, 4000);
}

/* --- Nav Active --- */
function initActiveNav() {
  // Already handled by Thymeleaf server-side
}

/* --- Comments --- */
function initCommentForm() {
  var form = document.getElementById("comment-form");
  if (!form) return;

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    var btn = form.querySelector('button');
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Submitting...';

    var data = {
      postId: form.querySelector("#commentPostId")?.value || 0,
      author: form.querySelector("#commentAuthor").value,
      email: form.querySelector("#commentEmail").value,
      content: form.querySelector("#commentContent").value
    };

    try {
      var resp = await fetch("/api/comments", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      });
      var result = await resp.json();
      if (result.success) {
        showFormMessage(form, "Comment submitted and pending review.", "success");
        form.reset();
        // Reload comments after short delay
        setTimeout(function () { location.reload(); }, 1500);
      } else {
        showFormMessage(form, result.message || "Failed to submit.", "error");
      }
    } catch (err) {
      showFormMessage(form, "Network error.", "error");
    } finally {
      btn.disabled = false;
      btn.textContent = "Submit Comment";
    }
  });
}


/* --- More Dropdown --- */
document.addEventListener("click", function(e) {
  var dd = document.getElementById("moreDropdown");
  var toggle = document.getElementById("moreToggle");
  if (dd && toggle && !toggle.contains(e.target) && !dd.contains(e.target)) {
    dd.classList.remove("show");
  }
});

document.addEventListener("click",function(e){
  var img=e.target.closest(".project-card-image img, .blog-card-image img, .image-frame img");
  if(!img||!img.src)return;
  var o=document.createElement("div");
  o.style.cssText="position:fixed;top:0;left:0;width:100%;height:100%;z-index:99999;background:rgba(0,0,0,0.85);backdrop-filter:blur(10px);display:flex;align-items:center;justify-content:center;cursor:zoom-out;";
  var l=document.createElement("img");
  l.src=img.src;
  l.style.cssText="max-width:80vw;max-height:80vh;border-radius:16px;box-shadow:0 20px 60px rgba(0,0,0,0.5);";
  o.appendChild(l);
  o.onclick=function(){o.remove()};
  document.addEventListener("keydown",function h(ev){if(ev.key==="Escape"){o.remove();document.removeEventListener("keydown",h)}});
  document.body.appendChild(o);
});
