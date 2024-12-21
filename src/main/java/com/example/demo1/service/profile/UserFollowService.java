package com.example.demo1.service.profile;

import com.example.demo1.dto.profile.UserSummaryDto;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.AlreadyFollowingUserException;
import com.example.demo1.exceptions.Profile.NotFollowingUserException;
import com.example.demo1.exceptions.Profile.UserNotFoundException;
import com.example.demo1.mapper.UserSummaryMapper;
import com.example.demo1.repository.ProfileRepository;
import com.example.demo1.service.security.SecurityService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserFollowService {
    private final ProfileRepository profileRepository;
    private final SecurityService securityService;
    private final UserSummaryMapper userSummaryMapper;

    public UserFollowService(ProfileRepository profileRepository, SecurityService securityService,
                             UserSummaryMapper userSummaryMapper) {
        this.profileRepository = profileRepository;
        this.securityService = securityService;
        this.userSummaryMapper = userSummaryMapper;
    }

    @Transactional
    public void followUser(String username) {
        UserProfile authProfile = profileRepository.findWithFollowersAndFollowingById(securityService.getAuthenticatedUserProfile().getId())
                .orElseThrow(() -> new UserNotFoundException("Authenticated user not found"));
        UserProfile targetProfile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new UserNotFoundException("Profile not found for username: " + username));
        if (authProfile.getFollowing().contains(targetProfile)) {
            throw new AlreadyFollowingUserException("You are already following this user.");
        }
        authProfile.getFollowing().add(targetProfile);
        targetProfile.getFollowers().add(authProfile);

        profileRepository.save(authProfile);
        profileRepository.save(targetProfile);
    }

    @Transactional
    public void unfollowUser(String username) {
        UserProfile authProfile = profileRepository.findWithFollowersAndFollowingById(securityService.getAuthenticatedUserProfile().getId())
                .orElseThrow(() -> new UserNotFoundException("Authenticated user not found"));

        UserProfile targetProfile = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new UserNotFoundException("Profile not found for username: " + username));
        if (!authProfile.getFollowing().contains(targetProfile)) {
            throw new NotFollowingUserException("You are not following this user.");
        }
        authProfile.getFollowing().remove(targetProfile);
        targetProfile.getFollowers().remove(authProfile);

        profileRepository.save(authProfile);
        profileRepository.save(targetProfile);
    }


    public Page<UserSummaryDto> getFollowing(String username, int page, int size) {
        UserProfile user = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<UserProfile> following = profileRepository.findFollowing(user, pageable);

        return following.map(userSummaryMapper::toSummaryDto);
    }

    public Page<UserSummaryDto> getFollowers(String username, int page, int size) {
        UserProfile user = profileRepository.findByUser_Username(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<UserProfile> followers = profileRepository.findFollowers(user, pageable);
        return followers.map(userSummaryMapper::toSummaryDto);
    }
}