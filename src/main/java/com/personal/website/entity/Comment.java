package com.personal.website.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId; // 0 = guestbook

    @Column(nullable = false, length = 100)
    private String author;

    @Column(length = 200)
    private String email;

    @Column(length = 500)
    private String avatar = "";

    @Column(length = 100)
    private String displayName = "";

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private boolean approved = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String a) { this.avatar = a; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String n) { this.displayName = n; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
