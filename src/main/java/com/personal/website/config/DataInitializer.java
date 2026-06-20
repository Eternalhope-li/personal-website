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
        if (!userService.existsByUsername(adminUsername)) {
            userService.register(adminUsername, adminPassword, "ADMIN");
            System.out.println(">>> Admin user created");
        }
    }
}