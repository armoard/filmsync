package com.example.demo1.repository;

import com.example.demo1.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CommentRepository extends JpaRepository<ReviewComment,Long> {
    List<ReviewComment> findByReview_Id(Long reviewId);
}
