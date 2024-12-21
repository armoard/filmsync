package com.example.demo1.controller.social;

import com.example.demo1.dto.profile.UserSummaryDto;
import com.example.demo1.service.profile.UserProfileFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/social")
public class SocialController {

    private final UserProfileFacade userProfileFacade;

    public SocialController(UserProfileFacade userProfileFacade) {
        this.userProfileFacade = userProfileFacade;
    }


    @GetMapping()
    public String social(Model model) {
        List<UserSummaryDto> randomUsers = userProfileFacade.getRandomUsers(6);
        model.addAttribute("users",randomUsers);
        return "social";
    }

}