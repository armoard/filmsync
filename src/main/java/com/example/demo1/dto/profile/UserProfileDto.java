package com.example.demo1.dto.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@Builder
public class UserProfileDto {

    private String username;
    private String profilePictureUrl;
    private String description;
    private List<UserReviewDto> lastReviews;
    private List<FavoriteMovieDto> favoriteMovies;
    private int followers;
    private int following;
    private int films;
    private List<MonthlyActivityDto> monthlyActivity;
    private boolean isOwnProfile;
    private boolean osFollowing;
    private boolean followsYou;



}