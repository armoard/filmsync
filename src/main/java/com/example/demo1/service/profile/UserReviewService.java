package com.example.demo1.service.profile;

import com.example.demo1.dto.profile.UserReviewDto;
import com.example.demo1.dto.review.FriendReviewDto;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.UserNotFoundException;
import com.example.demo1.mapper.ReviewMapper;
import com.example.demo1.repository.FavoriteMovieRepository;
import com.example.demo1.repository.ProfileRepository;
import com.example.demo1.repository.ReviewRepository;
import com.example.demo1.service.security.SecurityService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserReviewService {

    private final ReviewRepository reviewRepository;
    private final ProfileRepository profileRepository;
    private final ReviewMapper reviewMapper;
    private final SecurityService securityService;
    private final FavoriteMovieRepository favoriteMovieRepository;

    public UserReviewService(ReviewRepository reviewRepository, ProfileRepository profileRepository,
                             ReviewMapper reviewMapper,
                             SecurityService securityService, FavoriteMovieRepository favoriteMovieRepository) {
        this.reviewRepository = reviewRepository;
        this.profileRepository = profileRepository;
        this.reviewMapper = reviewMapper;
        this.securityService = securityService;
        this.favoriteMovieRepository = favoriteMovieRepository;
    }

    @Transactional
    public List<FriendReviewDto> getFriendsReviews(String movieId) {
        Long authUserId = securityService.getAuthenticatedUserProfile().getId();
        List<UserProfile> followedProfiles = profileRepository.findFollowedProfiles(authUserId);

        List<Review> reviews = reviewRepository.findByMovieIdAndUserProfiles(movieId, followedProfiles);


        return reviews.stream()
                .map(review -> new FriendReviewDto(
                        review.getUserProfile().getUsername(),
                        review.getReviewRating(),
                        review.getReviewContent(),
                        review.getUserProfile().getProfilePictureUrl()
                ))
                .collect(Collectors.toList());
    }

    // make this dynamic
    public List<UserReviewDto> getLast5ReviewsByProfile(UserProfile profile) {
        return reviewRepository.findTop5ByUserProfileOrderByCreatedAtDesc(profile)
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<UserReviewDto> getAllReviews(String username, int page, int size) {
        UserProfile user = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return reviewRepository.findByUserProfile(user, pageable)
                .map(review -> UserReviewDto.builder()
                        .reviewId(review.getId())
                        .posterPath(review.getPoster_path())
                        .reviewContent(review.getReviewContent())
                        .reviewRating(review.getReviewRating())
                        .createdAt(review.getCreatedAt())
                        .movieTitle(review.getMovieTittle())
                        .build());
    }


}