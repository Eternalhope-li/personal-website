package com.personal.website.controller;

import com.personal.website.entity.*;
import com.personal.website.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.personal.website.entity.User;
import java.util.*;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final BlogService blogService;
    private final ProjectService projectService;
    private final SkillService skillService;
    private final MessageService messageService;
    private final CommentService commentService;
    private final UserService userService;

    public ApiController(BlogService blogService, ProjectService projectService,
                         SkillService skillService, MessageService messageService,
                         CommentService commentService, UserService userService) {
        this.blogService = blogService;
        this.projectService = projectService;
        this.skillService = skillService;
        this.messageService = messageService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/blog")
    public List<BlogPost> getPosts() { return blogService.findAll(); }

    @GetMapping("/blog/{id}")
    public ResponseEntity<BlogPost> getPost(@PathVariable Long id) {
        return blogService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/projects")
    public List<Project> getProjects() { return projectService.findAll(); }

    @GetMapping("/skills")
    public List<Skill> getSkills() { return skillService.findAll(); }

    @PostMapping("/contact")
    public ResponseEntity<Map<String, Object>> contact(@RequestBody Message msg) {
        messageService.save(msg);
        return ResponseEntity.ok(Map.of("success", true, "message", "Message received!"));
    }

    @PostMapping("/comments")
    public ResponseEntity<Map<String, Object>> submitComment(@RequestBody Map<String, Object> body, HttpSession session) {
        String author = (String) body.get("author");
        String content = (String) body.get("content");
        Object postIdObj = body.get("postId");
        Long postId = postIdObj != null ? ((Number) postIdObj).longValue() : 0L;

        if (author == null || author.isBlank() || content == null || content.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Author and content required."));
        }

        // Check if user is admin for auto-approval
        var sessionUser = (User) session.getAttribute("user");
        boolean isAdminSession = session.getAttribute("admin") != null;
        boolean isAdmin = (sessionUser != null && "ADMIN".equals(sessionUser.getRole())) || isAdminSession;

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setAuthor(author);
        comment.setEmail("");
        comment.setContent(content);
        String cmtAvatar = (String) body.getOrDefault("avatar", "");
        if (cmtAvatar != null && cmtAvatar.length() > 100000) { cmtAvatar = ""; }
        comment.setAvatar(cmtAvatar);
        comment.setDisplayName((String) body.getOrDefault("displayName", ""));
        comment.setApproved(isAdmin);
        commentService.save(comment);
        return ResponseEntity.ok(Map.of("success", true, "message", "Comment submitted!"));
    }

    @GetMapping("/comments/list/{postId}")
    public List<Comment> getComments(@PathVariable Long postId) {
        return commentService.getApprovedByPostId(postId);
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String q) {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = q.toLowerCase();
        for (BlogPost post : blogService.findAll()) {
            if (post.getTitle().toLowerCase().contains(query) || (post.getSummary() != null && post.getSummary().toLowerCase().contains(query))) {
                results.add(Map.of("id", post.getId(), "title", post.getTitle(), "summary", post.getSummary(), "type", "blog"));
            }
        }
        for (Project proj : projectService.findAll()) {
            if (proj.getTitle().toLowerCase().contains(query) || (proj.getDescription() != null && proj.getDescription().toLowerCase().contains(query))) {
                results.add(Map.of("id", proj.getId(), "title", proj.getTitle(), "summary", proj.getDescription(), "type", "project"));
            }
        }
        return results;
    }

    @GetMapping("/auth/captcha")
    public void captcha(HttpSession session, HttpServletResponse response) throws Exception {
        // Generate 4-digit code
        String code = String.format("%04d", (int)(Math.random() * 10000));
        session.setAttribute("captcha", code);

        // Create image
        int w = 120, h = 40;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(15, 13, 26));
        g.fillRect(0, 0, w, h);

        // Draw noise lines
        g.setColor(new Color(108, 92, 231, 80));
        for (int i = 0; i < 8; i++) {
            int x1 = (int)(Math.random() * w);
            int y1 = (int)(Math.random() * h);
            int x2 = (int)(Math.random() * w);
            int y2 = (int)(Math.random() * h);
            g.drawLine(x1, y1, x2, y2);
        }

        // Draw the code with style
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
        for (int i = 0; i < code.length(); i++) {
            int r = 108 + (int)(Math.random() * 80);
            int b = 92 + (int)(Math.random() * 100);
            int gr = 231 + (int)(Math.random() * 24);
            g.setColor(new Color(Math.min(r,255), Math.min(b,255), Math.min(gr,255)));
            int x = 15 + i * 24 + (int)(Math.random() * 6);
            int y = 28 + (int)(Math.random() * 6);
            g.drawString(String.valueOf(code.charAt(i)), x, y);
        }

        g.dispose();
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        ImageIO.write(img, "png", response.getOutputStream());
    }

    @GetMapping("/auth/next-account")
    public ResponseEntity<?> nextAccount() {
        return ResponseEntity.ok(Map.of("account", userService.getNextAccount()));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body, HttpSession session) {
        try {
            // Verify captcha
            String expectedCode = (String) session.getAttribute("captcha");
            String inputCode = body.get("captcha");
            if (expectedCode == null || inputCode == null || !expectedCode.equals(inputCode)) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "验证码错误"));
            }
            // Clear used captcha
            session.removeAttribute("captcha");

            String username = body.get("username");
            String password = body.get("password");
            if (username == null || password == null || username.isBlank() || password.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "账号和密码不能为空"));
            }
            userService.register(username, password, "USER");
            return ResponseEntity.ok(Map.of("success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        var userOpt = userService.authenticate(body.get("username"), body.get("password"));
        if (userOpt.isPresent()) {
            session.setAttribute("user", userOpt.get());
            Map<String, Object> res = new HashMap<>();
            User u = userOpt.get();
            res.put("id", u.getId());
            res.put("username", u.getUsername());
            res.put("role", u.getRole());
            res.put("avatar", u.getAvatar());
            res.put("displayName", u.getDisplayName());
            res.put("createdAt", u.getCreatedAt().toString());
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid credentials"));
    }

    @GetMapping("/auth/profile")
    public ResponseEntity<?> profile(HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body(Map.of("success", false));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/auth/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> body, HttpSession session) {
        var sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return ResponseEntity.status(401).body(Map.of("success", false, "message", "Not logged in"));
        try {
            String displayName = (String) body.get("displayName");
            String avatar = (String) body.get("avatar");
            // Limit avatar size to 100KB
            if (avatar != null && avatar.length() > 100000) {
                avatar = "";
            }
            User updated = userService.updateProfile(sessionUser.getId(), displayName, avatar);
            // Update session
            session.setAttribute("user", updated);
            Map<String, Object> res = new HashMap<>();
            res.put("id", updated.getId());
            res.put("username", updated.getUsername());
            res.put("role", updated.getRole());
            res.put("avatar", updated.getAvatar());
            res.put("displayName", updated.getDisplayName());
            res.put("createdAt", updated.getCreatedAt().toString());
            return ResponseEntity.ok(res);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

