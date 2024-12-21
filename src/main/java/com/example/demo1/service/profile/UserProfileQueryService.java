package com.example.demo1.service.profile;

import com.example.demo1.dto.profile.*;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.ProfileNotFoundException;
import com.example.demo1.exceptions.Profile.UserNotFoundException;
import com.example.demo1.mapper.UserSummaryMapper;
import com.example.demo1.repository.ProfileRepository;
import com.example.demo1.service.review.ReviewService;
import com.example.demo1.service.security.SecurityService;
import com.example.demo1.service.movie.FavoriteMovieService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserProfileQueryService {
    private final ProfileRepository profileRepository;

    private final UserSummaryMapper userSummaryMapper;
    private final SecurityService securityService;

    private final UserReviewService userReviewService;
    private final FavoriteMovieService favoriteMovieService;


    public UserProfileQueryService(ProfileRepository profileRepository,
                                   UserSummaryMapper userSummaryMapper,
                                   SecurityService securityService,
                                   ReviewService reviewService,
                                   FavoriteMovieService favoriteMovieService,UserReviewService userReviewService) {
        this.profileRepository = profileRepository;
        this.userSummaryMapper = userSummaryMapper;
        this.securityService = securityService;
        this.userReviewService = userReviewService;
        this.favoriteMovieService = favoriteMovieService;
    }

    public UserProfileDto getProfileInfo(String username) {
        UserProfile profile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for username: " + username));

        boolean isOwnProfile = securityService.isOwnProfile(username);
        boolean isFollowing = false;
        boolean followsYou = false;

        if (!isOwnProfile) {
            Long authUserId = securityService.getAuthenticatedUserProfile().getId();
            isFollowing = profileRepository.isFollowing(authUserId, profile.getId());
            followsYou = profileRepository.followsYou(authUserId, profile.getId());
        }

        return UserProfileDto.builder()
                .username(username)
                .profilePictureUrl(profile.getProfilePictureUrl())
                .description(profile.getDescription())
                .lastReviews(userReviewService.getLast5ReviewsByProfile(profile))
                .favoriteMovies(favoriteMovieService.getFavoriteMoviesByProfile(profile))
                .followers(profile.getFollowers().size())
                .following(profile.getFollowing().size())// create class "user stats" for this?
                .films(profile.getReviews().size())
                .monthlyActivity(calculateMonthlyActivity(profile.getReviews()))
                .isOwnProfile(isOwnProfile) // also can do the same with this 3
                .osFollowing(isFollowing)
                .followsYou(followsYou)
                .build();
    }

    public List<UserSummaryDto> getRandomUsers(int limit) {
        return profileRepository.findRandomUsers(limit).stream()
                .map(userSummaryMapper::toSummaryDto)
                .collect(Collectors.toList());
    }


    //need to remove the auth user from the list
    public List<UserSummaryDto> searchUsers(String query) {
        Pageable pageable = PageRequest.of(0, 6);
        return profileRepository.findByUserUsernameContainingIgnoreCase(query, pageable)
                .stream()
                .map(userSummaryMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    private List<MonthlyActivityDto> calculateMonthlyActivity(List<Review> reviews) {
        Map<String, Long> activityMap = reviews.stream()
                .collect(Collectors.groupingBy(
                        review -> review.getCreatedAt().getMonth().name(),
                        Collectors.counting()));
        List<String> allMonths = Arrays.asList(
                "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST",
                "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
        return allMonths.stream()
                .map(month -> new MonthlyActivityDto(
                        month,
                        activityMap.getOrDefault(month, 0L).intValue()))
                .sorted(Comparator.comparingInt(entry -> Month.valueOf(entry.getMonth()).getValue()))
                .collect(Collectors.toList());
    }
}