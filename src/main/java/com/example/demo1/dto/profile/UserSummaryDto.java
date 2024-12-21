package com.example.demo1.dto.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserSummaryDto {
    private Long id;
    private String username;
    private String description;
    private String profileUrl;

    public UserSummaryDto(Long id, String username, String description, String profileUrl) {
        this.id = id;
        this.username = username;
        this.profileUrl = profileUrl;
        this.description = description;
    }


}