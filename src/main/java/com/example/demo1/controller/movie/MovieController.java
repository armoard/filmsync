package com.example.demo1.controller.movie;

import com.example.demo1.dto.review.FriendReviewDto;
import com.example.demo1.dto.review.MovieState;
import com.example.demo1.model.MovieDetails;
import com.example.demo1.service.movie.MovieService;
import com.example.demo1.service.security.SecurityService;
import com.example.demo1.service.profile.UserProfileFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final SecurityService securityService;
    private final UserProfileFacade userProfileFacade;


    public MovieController(MovieService movieService, SecurityService securityService, UserProfileFacade userProfileFacade) {
        this.movieService = movieService;
        this.securityService = securityService;
        this.userProfileFacade = userProfileFacade;
    }


    @GetMapping("/{movieId}")
    public String movieDetails(@PathVariable String movieId, Model model){
        MovieDetails movieDetails = movieService.getMovieDetails(movieId);
        MovieState state = userProfileFacade.getMovieState(movieId); // need this for front
        List<FriendReviewDto> friendsReviews = userProfileFacade.getFriendsReviews(movieId);
        model.addAttribute("friendsReviews", friendsReviews);
        model.addAttribute("movieState",state);
        model.addAttribute("movieDetails", movieDetails);
        return "movie-details";
    }


}
