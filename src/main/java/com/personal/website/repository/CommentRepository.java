package com.personal.website.repository;

import com.personal.website.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndApprovedTrueOrderByCreatedAtDesc(Long postId);
    List<Comment> findAllByOrderByCreatedAtDesc();
    List<Comment> findByApprovedFalseOrderByCreatedAtDesc();
    long countByApprovedFalse();
}
