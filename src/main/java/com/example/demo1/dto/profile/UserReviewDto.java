package com.example.demo1.dto.profile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Setter
@Getter
@Builder
public class UserReviewDto {
    private Long reviewId;
    private String movieId;
    private String movieTitle;
    private String reviewContent;
    private int reviewRating;
    private String posterPath;
    private LocalDateTime createdAt;
    private List<CommentDto> comments;

}