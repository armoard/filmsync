package com.example.demo1.service.profile;

import com.example.demo1.entity.User;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.AboutMeTooLongException;
import com.example.demo1.exceptions.Profile.InvalidUsernameCharacterException;
import com.example.demo1.exceptions.Profile.UsernameTooLongException;
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
        if(username.length() > 16){
            throw new UsernameTooLongException("Username must be less than 16 characters. ");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new InvalidUsernameCharacterException("Username can only contain letters, numbers, and underscores.");
        }
        UserProfile profile = securityService.getAuthenticatedUserProfile();
        User user = profile.getUser();

        profile.setUsername(username);
        user.setUsername(username);

        userRepository.save(user);
        profileRepository.save(profile);
    }

    @Transactional
    public void editAboutme(String aboutme){
        if(aboutme.length() > 255){
            throw new AboutMeTooLongException("Description must be less than 255 characters. ");
        }
        UserProfile profile = securityService.getAuthenticatedUserProfile();
        profile.setDescription(aboutme);
        profileRepository.save(profile);
    }
    @Transactional
    public void updateProfilePicture(String url){
        UserProfile profile = securityService.getAuthenticatedUserProfile();
        profile.setProfilePictureUrl(url);
        profileRepository.save(profile);
    }
}