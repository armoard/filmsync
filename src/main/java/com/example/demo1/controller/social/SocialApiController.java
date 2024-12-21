package com.example.demo1.controller.social;

import com.example.demo1.dto.profile.UserSummaryDto;
import com.example.demo1.service.profile.UserProfileFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
public class SocialApiController {
    private final UserProfileFacade userProfileFacade;

    public SocialApiController(UserProfileFacade userProfileFacade) {
        this.userProfileFacade = userProfileFacade;
    }

    @PostMapping("/search")
    public ResponseEntity<List<UserSummaryDto>> searchUsers(@RequestBody Map<String, String> payload) { //this should be path?
        String query = payload.get("query");
        List<UserSummaryDto> searchResults = userProfileFacade.searchUsers(query);
        return ResponseEntity.ok(searchResults);
    }
}
