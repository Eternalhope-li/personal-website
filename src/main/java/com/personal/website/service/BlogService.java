package com.personal.website.service;

import com.personal.website.entity.BlogPost;
import com.personal.website.repository.BlogPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final BlogPostRepository blogPostRepository;

    public BlogService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public List<BlogPost> findAll() {
        return blogPostRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<BlogPost> findById(Long id) {
        return blogPostRepository.findById(id);
    }

    public BlogPost save(BlogPost post) {
        return blogPostRepository.save(post);
    }

    public void deleteById(Long id) {
        blogPostRepository.deleteById(id);
    }

    public List<BlogPost> findByCategory(String category) {
        return blogPostRepository.findByCategoryOrderByCreatedAtDesc(category);
    }
}
