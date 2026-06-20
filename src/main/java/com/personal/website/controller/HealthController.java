package com.personal.website.controller;

import com.personal.website.service.BlogService;
import com.personal.website.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {
    private final DataSource dataSource;
    private final BlogService blogService;
    private final CommentService commentService;

    public HealthController(DataSource dataSource, BlogService blogService, CommentService commentService) {
        this.dataSource = dataSource;
        this.blogService = blogService;
        this.commentService = commentService;
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().toString());
        try (Connection c = dataSource.getConnection()) {
            status.put("database", c.isValid(2) ? "UP" : "DOWN");
        } catch (Exception e) {
            status.put("database", "DOWN");
            status.put("status", "DEGRADED");
        }
        try {
            status.put("posts", blogService.findAll().size());
            status.put("pendingComments", commentService.pendingCount());
        } catch (Exception e) {
            status.put("data", "ERROR");
        }
        HttpStatus httpStatus = "DOWN".equals(status.get("database")) ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.OK;
        return ResponseEntity.status(httpStatus).body(status);
    }
}
