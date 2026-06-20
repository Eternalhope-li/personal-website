package com.personal.website.service;

import com.personal.website.entity.Comment;
import com.personal.website.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getApprovedByPostId(Long postId) {
        return commentRepository.findByPostIdAndApprovedTrueOrderByCreatedAtDesc(postId);
    }

    public List<Comment> findAll() {
        return commentRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Comment> getPending() {
        return commentRepository.findByApprovedFalseOrderByCreatedAtDesc();
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public void approve(Long id) {
        commentRepository.findById(id).ifPresent(c -> { c.setApproved(true); commentRepository.save(c); });
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public long pendingCount() {
        return commentRepository.countByApprovedFalse();
    }
}
