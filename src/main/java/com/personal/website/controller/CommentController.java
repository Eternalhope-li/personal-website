package com.personal.website.controller;

import com.personal.website.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/guestbook")
    public String guestbook(Model model) {
        model.addAttribute("comments", commentService.getApprovedByPostId(0L));
        model.addAttribute("activeNav", "guestbook");
        return "guestbook";
    }
}
