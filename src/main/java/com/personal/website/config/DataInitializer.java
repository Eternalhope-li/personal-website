package com.personal.website.config;

import com.personal.website.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Value("${admin.username:001}")
    private String adminUsername;

    @Value("${admin.password:xiaoyao888}")
    private String adminPassword;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        try {
            if (!userService.existsByUsername(adminUsername)) {
                userService.register(adminUsername, adminPassword, "ADMIN");
                System.out.println(">>> Admin user created: " + adminUsername);
            } else {
                System.out.println(">>> Admin user already exists: " + adminUsername);
            }
        } catch (Exception e) {
            System.err.println(">>> DataInitializer: Could not initialize admin user - " + e.getMessage());
            // Non-fatal: the app can still function without admin init
        }
    }
}
