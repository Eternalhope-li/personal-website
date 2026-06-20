package com.personal.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    // SPA routes - UserController handles /login, /register, /profile
    @GetMapping(value = {
        "/about", "/blog", "/projects", "/contact",
        "/guestbook", "/timeline", "/resume", "/friends"
    })
    public String spaPages() {
        return "forward:/index.html";
    }

    // Blog detail catch-all
    @GetMapping("/blog/**")
    public String blogDetail() {
        return "forward:/index.html";
    }
}