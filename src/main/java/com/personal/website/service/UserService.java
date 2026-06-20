package com.personal.website.service;

import com.personal.website.entity.User;
import com.personal.website.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final String SALT = "PersonalWebsite2026!@#";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((SALT + password).getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean verifyPassword(String rawPassword, String storedHash) {
        return hashPassword(rawPassword).equals(storedHash);
    }

    public User register(String username, String password, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User(username, hashPassword(password), role != null ? role : "USER");
        return userRepository.save(user);
    }

    public String getNextAccount() {
        List<User> all = userRepository.findAll();
        int maxNum = 0;
        for (User u : all) {
            String name = u.getUsername();
            try {
                if (name.matches("\\d+")) {
                    int n = Integer.parseInt(name);
                    if (n > maxNum) maxNum = n;
                }
            } catch (NumberFormatException e) {}
        }
        int next = maxNum + 1;
        if (next < 100) next = 100;
        return String.valueOf(next);
    }

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> verifyPassword(password, u.getPassword()));
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User updateProfile(Long id, String displayName, String avatar) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (displayName != null && !displayName.isBlank()) user.setDisplayName(displayName);
        if (avatar != null) user.setAvatar(avatar);
        return userRepository.save(user);
    }
}