package com.example.demo1.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class EditReviewRequest {

    @NotEmpty(message = "Movie ID cannot be empty")
    private String movieId;

    @NotEmpty(message = "Review content cannot be empty")
    private String reviewContent;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private int reviewRating;

    public EditReviewRequest(String movieId, int rating, String content) {
        this.movieId = movieId;
        this.reviewRating = rating;
        this.reviewContent = content;
    }
}
