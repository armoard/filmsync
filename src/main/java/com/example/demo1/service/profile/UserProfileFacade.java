package com.example.demo1.service.profile;
import com.example.demo1.dto.profile.*;
import com.example.demo1.dto.review.FriendReviewDto;
import com.example.demo1.dto.review.MovieState;
import com.example.demo1.service.movie.FavoriteMovieService;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;


@Service
public class UserProfileFacade {
    private final UserFollowService userFollowService;
    private final UserProfileQueryService userProfileQueryService;
    private final UserProfileUpdateService userProfileUpdateService;
    private final UserReviewService userReviewService;
    private final FavoriteMovieService favoriteMovieService;


    public UserProfileFacade(UserFollowService userFollowService,
                             UserProfileQueryService userProfileQueryService,
                             UserProfileUpdateService userProfileUpdateService,
                             UserReviewService userReviewService, FavoriteMovieService favoriteMovieService) {
        this.userFollowService = userFollowService;
        this.userProfileQueryService = userProfileQueryService;
        this.userProfileUpdateService = userProfileUpdateService;
        this.userReviewService = userReviewService;
        this.favoriteMovieService = favoriteMovieService;
    }

    public void followUser(String username) {
        userFollowService.followUser(username);
    }

    public void unfollowUser(String username) {
        userFollowService.unfollowUser(username);
    }

    public Page<UserSummaryDto> getFollowers(String username, int page, int size) {
        return userFollowService.getFollowers(username, page, size);
    }

    public Page<UserSummaryDto> getFollowing(String username, int page, int size) {
        return userFollowService.getFollowing(username, page, size);
    }

    public MovieState getMovieState(String movieId) {
        return favoriteMovieService.getMovieState(movieId);
    }

    public void addAsFavoriteMovie(FavoriteMovieDto input) {
        favoriteMovieService.addAsFavoriteMovie(input);
    }

    public void deleteFavoriteMovie(String movieId) {
        favoriteMovieService.deleteFavoriteMovie(movieId);
    }

    public UserProfileDto getProfileInfo(String username) {
        return userProfileQueryService.getProfileInfo(username);
    }

    public List<UserSummaryDto> getRandomUsers(int limit) {
        return userProfileQueryService.getRandomUsers(limit);
    }

    public List<UserSummaryDto> searchUsers(String query) {
        return userProfileQueryService.searchUsers(query);
    }

    public void editUsername(String username) {
        userProfileUpdateService.editUsername(username);
    }

    public void editAboutMe(String aboutMe,String username) {
        userProfileUpdateService.editAboutMe(aboutMe,username);
    }

    public void updateProfilePicture(String url) {
        userProfileUpdateService.updateProfilePicture(url);
    }

    public List<FriendReviewDto> getFriendsReviews(String movieId) {
        return userReviewService.getFriendsReviews(movieId); //maybe move this
    }

}