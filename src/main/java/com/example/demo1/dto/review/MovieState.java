package com.example.demo1.dto.review;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MovieState {
    private boolean isFavorite;
    private boolean isReviewed;
    private String reviewContent;
    private int reviewRating;
}
