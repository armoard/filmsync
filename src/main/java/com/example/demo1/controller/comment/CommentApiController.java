package com.example.demo1.controller.comment;

import com.example.demo1.dto.comment.CreateCommentRequest;
import com.example.demo1.service.review.CommentService;
import com.example.demo1.service.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/comment")
public class CommentApiController {
    private final CommentService commentService;
    private final ReviewService reviewService;

    public CommentApiController(CommentService commentService, ReviewService reviewService) {
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    @PostMapping("/upload/{reviewid}")
    public ResponseEntity<Map<String, String>> createComment(@Valid @RequestBody CreateCommentRequest input){
        commentService.createComment(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Comment created successfully"));
    }

}
