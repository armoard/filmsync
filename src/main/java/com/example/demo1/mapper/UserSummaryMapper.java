package com.example.demo1.mapper;

import com.example.demo1.dto.profile.UserSummaryDto;
import com.example.demo1.entity.UserProfile;
import org.springframework.stereotype.Component;


// userprofile to user summary
@Component
public class UserSummaryMapper {

    public UserSummaryDto toSummaryDto(UserProfile user) {
        return new UserSummaryDto(
                user.getId(),
                user.getUser().getUsername(),
                user.getDescription(),
                user.getProfilePictureUrl()
        );
    }
}