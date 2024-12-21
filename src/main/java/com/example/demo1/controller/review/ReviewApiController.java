package com.example.demo1.controller.review;

import com.example.demo1.dto.review.CreateReviewRequest;
import com.example.demo1.dto.review.EditReviewRequest;
import com.example.demo1.service.review.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewApiController {

    private final ReviewService reviewService;

    public ReviewApiController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> createReview(@Valid @RequestBody CreateReviewRequest input) {
        reviewService.createReview(input);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Review created successfully"));
    }

    @PutMapping("/edit/{movieId}")
    public ResponseEntity<Map<String, String>> editReview(@Valid @RequestBody EditReviewRequest input){
        reviewService.editReview(input);
        return ResponseEntity.ok(Map.of("message", "Review updated successfully."));
    }


}
