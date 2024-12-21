package com.example.demo1.controller.profile;

import com.example.demo1.dto.profile.UserProfileDto;
import com.example.demo1.dto.profile.UserSummaryDto;
import com.example.demo1.service.profile.UserProfileFacade;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class ProfileController {

    private final UserProfileFacade userProfileFacade;
    public ProfileController(UserProfileFacade userProfileFacade) {
        this.userProfileFacade = userProfileFacade;

    }

    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Model model) {
        UserProfileDto userProfileDto = userProfileFacade.getProfileInfo(username);
        model.addAttribute("profile", userProfileDto);
        return "profile";
    }
    @GetMapping("/following/{username}")
    public String getFollowing(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<UserSummaryDto> users = userProfileFacade.getFollowing(username, page, size);
        model.addAttribute("users", users.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        return "following";
    }

    @GetMapping("/followers/{username}")
    public String getFollowers(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<UserSummaryDto> users = userProfileFacade.getFollowers(username, page, size);
        model.addAttribute("users", users.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        return "followers";
    }


}
