package com.example.demo1.controller.home;

import com.example.demo1.service.movie.MovieService;
import com.example.demo1.service.security.SecurityService;
import com.example.demo1.service.profile.UserProfileFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {


    private final UserProfileFacade userProfileFacade;
    private final SecurityService securityService;
    private final MovieService movieService;

    public HomeController(UserProfileFacade userProfileFacade, SecurityService securityService, MovieService movieService) {
        this.userProfileFacade = userProfileFacade;
        this.securityService = securityService;
        this.movieService = movieService;
    }

    @RequestMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }
}