package com.personal.website.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Object handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String uri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        boolean isApiRequest = uri != null && uri.startsWith("/api");
        boolean isAdminRequest = uri != null && uri.startsWith("/admin");

        // API errors return JSON
        if (isApiRequest) {
            int code = 500;
            if (status != null) {
                code = Integer.parseInt(status.toString());
            }
            return ResponseEntity.status(code).body(
                Map.of("success", false, "message", "Internal server error")
            );
        }

        if (status != null) {
            int code = Integer.parseInt(status.toString());
            if (isAdminRequest) {
                model.addAttribute("status", String.valueOf(code));
                return "error";
            }
            // All other 404s forward to Vue SPA
            if (code == HttpStatus.NOT_FOUND.value()) {
                return "forward:/index.html";
            }
        }
        model.addAttribute("status", "500");
        return "error";
    }
}
