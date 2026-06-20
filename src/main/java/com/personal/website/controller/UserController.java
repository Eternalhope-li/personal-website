package com.personal.website.controller;

import com.personal.website.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;

    public UserController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    // SPA handles login/register/profile pages - forward to Vue
    @GetMapping("/login")
    public String loginForm() {
        return "forward:/index.html";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, Model model) {
        var userOpt = userService.authenticate(username, password);
        if (userOpt.isPresent()) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/profile";
        }
        model.addAttribute("error", messageSource.getMessage("user.login.error", null, "Invalid credentials", Locale.getDefault()));
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "forward:/index.html";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                           Model model) {
        try {
            userService.register(username, password, "USER");
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        var user = session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "forward:/index.html";
    }
}
