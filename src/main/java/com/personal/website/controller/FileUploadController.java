package com.personal.website.controller;

import com.personal.website.entity.User;
import com.personal.website.service.UserService;
import com.personal.website.storage.StorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final UserService userService;
    private final StorageService storageService;

    public FileUploadController(UserService userService, StorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    @PostMapping("/auth/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file, HttpSession session) {
        var sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Not logged in"));
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "File is empty"));
        }

        try {
            // Create unique filename
            String ext = "";
            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String fileName = "avatars/" + sessionUser.getId() + "_" + System.currentTimeMillis() + ext;

            // Upload via storage service (local filesystem or MinIO S3)
            String avatarUrl = storageService.upload(fileName, file);

            // Update user profile
            User updated = userService.updateProfile(sessionUser.getId(), null, avatarUrl);
            session.setAttribute("user", updated);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "avatar", avatarUrl,
                "storageType", storageService.getType(),
                "id", updated.getId(),
                "username", updated.getUsername(),
                "role", updated.getRole(),
                "displayName", updated.getDisplayName() != null ? updated.getDisplayName() : "",
                "createdAt", updated.getCreatedAt().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Upload failed: " + e.getMessage()));
        }
    }
}