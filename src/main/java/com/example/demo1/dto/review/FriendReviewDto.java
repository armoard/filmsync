package com.example.demo1.dto.review;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FriendReviewDto {
    private String authorUsername;
    private String profileUrl;
    private String content;
    private int rating;

    public FriendReviewDto(String authorUsername, int rating, String content, String profileUrl) {
        this.authorUsername = authorUsername;
        this.rating = rating;
        this.content = content;
        this.profileUrl = profileUrl;
    }
}
