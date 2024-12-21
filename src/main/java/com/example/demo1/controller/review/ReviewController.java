package com.example.demo1.controller.review;


import com.example.demo1.dto.profile.UserReviewDto;

import com.example.demo1.service.review.ReviewService;
import com.example.demo1.service.profile.UserReviewService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserReviewService userReviewService;

    public ReviewController(ReviewService reviewService, UserReviewService userReviewService) {
        this.reviewService = reviewService;
        this.userReviewService = userReviewService;
    }

    @GetMapping("/{reviewId}/comments")
    public String viewComments(@PathVariable Long reviewId, Model model) {
        Map<String,Object> review = reviewService.getReviewWithComments(reviewId);
        model.addAttribute("review", review);
        return "review-page";
    }

    @GetMapping("/user/{username}")
    public String allReviews(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<UserReviewDto> reviewsPage = userReviewService.getAllReviews(username, page, size);

        model.addAttribute("username", username);
        model.addAttribute("reviews", reviewsPage.getContent());
        model.addAttribute("currentPage", reviewsPage.getNumber());
        model.addAttribute("totalPages", reviewsPage.getTotalPages());
        return "all-reviews";
    }
}