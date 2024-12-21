package com.example.demo1.dto.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class MonthlyActivityDto {
    private String month;
    private int reviewsCount;

    public MonthlyActivityDto(String month, int reviewsCount) {
        this.month = month;
        this.reviewsCount = reviewsCount;
    }
}