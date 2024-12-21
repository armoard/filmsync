package com.example.demo1.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class CreateReviewRequest {

    @Min(value = 1, message = "Review rating must be at least 1")
    @Max(value = 5, message = "Review rating must not exceed 5")
    public int reviewRating;


    @NotBlank(message = "Review content cannot be empty")
    public String reviewContent;

    @NotBlank(message = "Movie ID is required")
    private String movieId;
    @NotBlank(message = "Poster cannot be empty")
    public String poster_path;
    @NotBlank(message = "Title cannot be empty")
    public String movieTitle;
}