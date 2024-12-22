package com.example.demo1.controller.profile;

import com.example.demo1.service.aws.S3Service;
import com.example.demo1.service.profile.UserProfileFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class ProfileApiController {

    private final UserProfileFacade userProfileFacade;
    private final S3Service s3Service;

    public ProfileApiController(UserProfileFacade userProfileFacade, S3Service s3Service) {
        this.userProfileFacade = userProfileFacade;
        this.s3Service = s3Service;
    }

    @PostMapping("/picture")
    public ResponseEntity<Map<String, String>> uploadProfile(@RequestParam("file") MultipartFile file) {
        String key = "profile-pictures/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        String url = s3Service.uploadProfile(file, key);
        userProfileFacade.updateProfilePicture(url);
        return ResponseEntity.ok(Map.of("message", "File uploaded successfully!", "url", url));
    }

    @PutMapping("/edit/username/{username}")
    public ResponseEntity<Map<String, String>> editUsername(@PathVariable String username) {
        userProfileFacade.editUsername(username);
        return ResponseEntity.ok(Map.of("message", "Username updated successfully."));
    }

    @PutMapping("/edit/about-me")
    public ResponseEntity<Map<String, String>> submitProfileEdit(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String aboutme = requestBody.get("aboutme");
        userProfileFacade.editAboutMe(aboutme, username);
        return ResponseEntity.ok(Map.of("message", "About-me updated successfully."));
    }

    @PutMapping("/follow/{username}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable String username) {
        userProfileFacade.followUser(username);
        return ResponseEntity.ok(Map.of("message", "User followed successfully."));
    }

    @DeleteMapping("/unfollow/{username}")
    public ResponseEntity<Map<String, String>> unfollowUser(@PathVariable String username) {
        userProfileFacade.unfollowUser(username);
        return ResponseEntity.ok(Map.of("message", "User unfollowed successfully."));
    }
}