package com.example.demo1.service.profile;

import com.example.demo1.entity.User;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.*;
import com.example.demo1.exceptions.review.ReviewNotFoundException;
import com.example.demo1.repository.ProfileRepository;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.service.security.SecurityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserProfileUpdateService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final SecurityService securityService;


    public UserProfileUpdateService(UserRepository userRepository, ProfileRepository profileRepository,
                                    SecurityService securityService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.securityService = securityService;
    }

    @Transactional
    public void editUsername(String username) {
        if (username == null || username.length() > 16) {
            throw new UsernameTooLongException("Username must be less than 16 characters.");
        }

        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new InvalidUsernameCharacterException("Username can only contain letters, numbers, and underscores.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyTakenException("The username is already taken.");
        }

        UserProfile profile = securityService.getAuthenticatedUserProfile();

        if (!profile.getUsername().equals(securityService.getAuthenticatedUser().getUsername())) {
            throw new NotOwnProfileException("You can't edit someone else's profile.");
        }

        User user = profile.getUser();
        user.setUsername(username);
        profile.setUsername(username);

        userRepository.save(user);
        profileRepository.save(profile);
    }

    @Transactional
    public void editAboutMe(String aboutMe, String username){
        if(aboutMe.length() > 255){
            throw new AboutMeTooLongException("Description must be less than 255 characters. ");
        }
        UserProfile profile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!profile.getUsername().equals(securityService.getAuthenticatedUser().getUsername())) {
            throw new NotOwnProfileException("You can't edit someone else's profile.");
        }

        profile.setDescription(aboutMe);
        profileRepository.save(profile);

    }
    @Transactional
    public void updateProfilePicture(String url){
        UserProfile profile = securityService.getAuthenticatedUserProfile();
        profile.setProfilePictureUrl(url);
        profileRepository.save(profile);
    }
}