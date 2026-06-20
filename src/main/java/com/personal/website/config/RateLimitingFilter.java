package com.personal.website.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RateLimitingFilter extends OncePerRequestFilter {
    private final Map<String, ClientRequest> clients = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 100;
    private static final long WINDOW_MS = 60_000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/assets") || path.startsWith("/uploads") || path.startsWith("/favicon")) {
            filterChain.doFilter(request, response);
            return;
        }
        String ip = getClientIP(request);
        String key = ip + ":" + path;
        long now = System.currentTimeMillis();
        ClientRequest cr = clients.computeIfAbsent(key, k -> new ClientRequest(now));
        synchronized (cr) {
            if (now - cr.windowStart > WINDOW_MS) {
                cr.windowStart = now;
                cr.count = 1;
            } else if (cr.count >= MAX_REQUESTS) {
                response.setStatus(429);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"Too many requests\"}");
                return;
            } else { cr.count++; }
        }
        filterChain.doFilter(request, response);
    }
    private String getClientIP(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank() && !"unknown".equalsIgnoreCase(xff)) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    private static class ClientRequest {
        long windowStart; int count;
        ClientRequest(long now) { this.windowStart = now; this.count = 1; }
    }
}
