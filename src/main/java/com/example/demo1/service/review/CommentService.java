package com.example.demo1.service.review;


import com.example.demo1.dto.comment.CreateCommentRequest;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.ReviewComment;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Login.UserNotLoggedInException;
import com.example.demo1.exceptions.review.ReviewNotFoundException;
import com.example.demo1.repository.CommentRepository;
import com.example.demo1.repository.ReviewRepository;
import com.example.demo1.service.security.SecurityService;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    private final ReviewRepository reviewRepository;
    private final SecurityService securityService;
    private final CommentRepository commentRepository;

    public CommentService(ReviewRepository reviewRepository, SecurityService securityService, CommentRepository commentRepository) {
        this.reviewRepository = reviewRepository;
        this.securityService = securityService;
        this.commentRepository = commentRepository;
    }

    public void createComment(CreateCommentRequest input) {

        UserProfile userprofile = securityService.getAuthenticatedUserProfile();

        Long reviewId;
        try {
            reviewId = Long.parseLong(input.getReviewId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid review ID: " + input.getReviewId());
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));

        ReviewComment comment = new ReviewComment();
        comment.setAuthor(userprofile);
        comment.setReview(review);
        comment.setContent(input.getContent());
        commentRepository.save(comment);
    }
}
