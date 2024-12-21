package com.example.demo1.service.review;

import com.example.demo1.dto.review.CreateReviewRequest;
import com.example.demo1.dto.review.EditReviewRequest;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.DuplicateReviewException;
import com.example.demo1.exceptions.Profile.ProfileNotFoundException;
import com.example.demo1.exceptions.review.ReviewNotFoundException;
import com.example.demo1.mapper.ReviewMapper;
import com.example.demo1.repository.ProfileRepository;
import com.example.demo1.repository.ReviewRepository;
import com.example.demo1.service.security.SecurityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReviewService {

    private final SecurityService securityService;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProfileRepository profileRepository;

    public ReviewService(SecurityService securityService,
                         ReviewRepository reviewRepository,
                         ReviewMapper reviewMapper, ProfileRepository profileRepository) {
        this.securityService = securityService;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public void createReview(CreateReviewRequest input) {
        UserProfile userProfile = securityService.getAuthenticatedUserProfile();

        if (reviewRepository.existsByUserProfileAndMovieId(userProfile, input.getMovieId())) {
            throw new DuplicateReviewException("Review already exists for this movie and user");
        }

        Review review = new Review();
        review.setMovieTittle(input.getMovieTitle());
        review.setReviewContent(input.getReviewContent());
        review.setReviewRating(input.getReviewRating());
        review.setMovieId(input.getMovieId());
        review.setPoster_path(input.getPoster_path());
        review.setUserProfile(userProfile);

        reviewRepository.save(review);
    }
    @Transactional
    public void editReview(EditReviewRequest input){
        Long userId = securityService.getAuthenticatedUserProfile().getId();
        Review review = reviewRepository.findByUserProfileIdAndMovieId(userId, input.getMovieId())
            .orElseThrow(() -> new ReviewNotFoundException("Review not found for MovieID:" + input.getMovieId()));
        review.setReviewContent(input.getReviewContent());
        review.setReviewRating(input.getReviewRating());
        reviewRepository.save(review);
    }

    public Map<String, Object> getReviewWithComments(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));

        UserProfile authorProfile = profileRepository.findByReviewsId(reviewId)
                .orElseThrow(() -> new ProfileNotFoundException("UserProfile not found for review with ID: " + reviewId));

        Map<String, Object> result = new HashMap<>();
        result.put("author", authorProfile.getUsername());
        result.put("review", reviewMapper.toDto(review));

        return result;
    }
}