package com.personal.website.controller;

import com.personal.website.entity.*;
import com.personal.website.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BlogService blogService;
    private final ProjectService projectService;
    private final SiteSettingService siteSettingService;
    private final SkillService skillService;
    private final MessageService messageService;
    private final CommentService commentService;
    private final MessageSource messageSource;
    private final UserService userService;

    public AdminController(BlogService blogService, ProjectService projectService,
                           MessageService messageService, CommentService commentService,
                           MessageSource messageSource,
                           SiteSettingService siteSettingService, SkillService skillService,
                           UserService userService) {
        this.blogService = blogService;
        this.projectService = projectService;
        this.messageService = messageService;
        this.commentService = commentService;
        this.messageSource = messageSource;
        this.siteSettingService = siteSettingService;
        this.skillService = skillService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm() { return "admin-login"; }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        var userOpt = userService.authenticate(username, password);
        if (userOpt.isPresent()) {
            session.setAttribute("admin", true);
            session.setAttribute("adminUser", userOpt.get().getUsername());
            model.addAttribute("blogCount", blogService.findAll().size());
            model.addAttribute("projectCount", projectService.findAll().size());
            model.addAttribute("messageCount", messageService.findAll().size());
            model.addAttribute("pendingComments", commentService.getPending());
            model.addAttribute("messages", messageService.findAll());
            return "admin-dashboard";
        }
        model.addAttribute("error", messageSource.getMessage("admin.login.error", null, "Invalid credentials", Locale.getDefault()));
        return "admin-login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("blogCount", blogService.findAll().size());
        model.addAttribute("projectCount", projectService.findAll().size());
        model.addAttribute("messageCount", messageService.findAll().size());
        model.addAttribute("pendingComments", commentService.getPending());
        model.addAttribute("messages", messageService.findAll());
        return "admin-dashboard";
    }

    @GetMapping("/comments")
    public String comments(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("pendingComments", commentService.getPending());
        model.addAttribute("allComments", commentService.findAll());
        return "admin-comments";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/blogs")
    public String blogs(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("posts", blogService.findAll());
        return "admin-blogs";
    }

    @GetMapping("/blogs/edit/{id}")
    public String editBlog(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        blogService.findById(id).ifPresent(post -> model.addAttribute("post", post));
        model.addAttribute("posts", blogService.findAll());
        return "admin-blogs";
    }

    @PostMapping("/blogs/save")
    public String saveBlog(@ModelAttribute BlogPost post) {
        blogService.save(post);
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.deleteById(id);
        return "redirect:/admin/blogs";
    }

    @GetMapping("/projects")
    public String projects(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAttribute("projects", projectService.findAll());
        return "admin-projects";
    }

    @GetMapping("/projects/edit/{id}")
    public String editProject(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        projectService.findById(id).ifPresent(project -> model.addAttribute("project", project));
        model.addAttribute("projects", projectService.findAll());
        return "admin-projects";
    }

    @PostMapping("/projects/save")
    public String saveProject(@ModelAttribute Project project) {
        projectService.save(project);
        return "redirect:/admin/projects";
    }

    @GetMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return "redirect:/admin/projects";
    }

    @GetMapping("/comments/approve/{id}")
    public String approveComment(@PathVariable Long id) {
        commentService.approve(id);
        return "redirect:/admin/comments";
    }

    @GetMapping("/comments/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return "redirect:/admin/comments";
    }

    @GetMapping("/settings")
    public String settings(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        model.addAllAttributes(siteSettingService.all());
        model.addAttribute("skills", skillService.findAll());
        return "admin-settings";
    }

    @PostMapping("/settings/save")
    public String saveSetting(@RequestParam String key, @RequestParam String value, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        siteSettingService.set(key, value);
        return "redirect:/admin/settings";
    }

    @PostMapping("/skills/save")
    public String saveSkill(@ModelAttribute Skill skill, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        skillService.save(skill);
        return "redirect:/admin/settings";
    }

    @GetMapping("/skills/delete/{id}")
    public String deleteSkill(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        skillService.delete(id);
        return "redirect:/admin/settings";
    }
}