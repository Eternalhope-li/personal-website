package com.personal.website.controller;

import com.personal.website.entity.BlogPost;
import com.personal.website.service.BlogService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Controller
public class SeoController {

    private final BlogService blogService;
    private final String baseUrl;

    public SeoController(BlogService blogService) {
        this.blogService = blogService;
        this.baseUrl = System.getProperty("server.name", "http://localhost:8080");
    }

    @GetMapping("/sitemap.xml")
    public void sitemap(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/xml;charset=UTF-8");
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        String[] pages = {"/", "/about", "/projects", "/blog", "/contact", "/timeline", "/resume", "/friends", "/comments/guestbook"};
        for (String p : pages) {
            xml.append("  <url><loc>").append(baseUrl).append(p).append("</loc>");
            xml.append("<priority>").append(p.equals("/") ? "1.0" : "0.8").append("</priority>");
            xml.append("</url>\n");
        }
        for (BlogPost post : blogService.findAll()) {
            xml.append("  <url><loc>").append(baseUrl).append("/blog/").append(post.getId()).append("</loc>");
            xml.append("<priority>0.6</priority>");
            xml.append("</url>\n");
        }
        xml.append("</urlset>");
        resp.getWriter().write(xml.toString());
    }

    @GetMapping("/robots.txt")
    public void robots(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write("User-agent: *\nAllow: /\nSitemap: " + baseUrl + "/sitemap.xml\n");
    }

    @GetMapping("/rss")
    public void rss(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/rss+xml;charset=UTF-8");
        StringBuilder rss = new StringBuilder();
        var fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        rss.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        rss.append("<rss version=\"2.0\"><channel>\n");
        rss.append("<title>Personal Website Blog</title>\n");
        rss.append("<link>").append(baseUrl).append("/blog</link>\n");
        rss.append("<description>Latest blog posts</description>\n");
        for (BlogPost post : blogService.findAll()) {
            rss.append("<item>\n");
            rss.append("  <title>").append(escapeXml(post.getTitle())).append("</title>\n");
            rss.append("  <link>").append(baseUrl).append("/blog/").append(post.getId()).append("</link>\n");
            rss.append("  <description>").append(escapeXml(post.getSummary() != null ? post.getSummary() : "")).append("</description>\n");
            rss.append("  <pubDate>").append(post.getCreatedAt().format(fmt)).append("</pubDate>\n");
            rss.append("</item>\n");
        }
        rss.append("</channel></rss>");
        resp.getWriter().write(rss.toString());
    }

    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}