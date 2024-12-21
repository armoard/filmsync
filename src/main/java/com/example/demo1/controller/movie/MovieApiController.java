package com.example.demo1.controller.movie;

import com.example.demo1.dto.profile.FavoriteMovieDto;
import com.example.demo1.model.MovieSearchResult;
import com.example.demo1.service.movie.MovieService;
import com.example.demo1.service.profile.UserProfileFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movie")
public class MovieApiController {

    private final MovieService movieService;
    private final UserProfileFacade userProfileFacade;

    public MovieApiController(MovieService movieService, UserProfileFacade userProfileFacade) {
        this.movieService = movieService;
        this.userProfileFacade = userProfileFacade;
    }

    @PostMapping("/{movieId}/favorite")
    public ResponseEntity<Map<String, String>> markAsFavoriteMovie(@RequestBody FavoriteMovieDto input) {
        userProfileFacade.addAsFavoriteMovie(input);
        return ResponseEntity.ok(Map.of("message", "Favorite Movie added successfully."));
    }

    @PostMapping("/search") //home search
    @ResponseBody
    public List<MovieSearchResult> getResultsFromQuery(@RequestBody Map<String, String> payload) {
        String query = payload.get("query");
        return movieService.getMoviesSearchResult(query);
    }

    @DeleteMapping("/{movieId}/unfavorite")
    public ResponseEntity<Map<String, String>> deleteFavoriteMovie(@PathVariable String movieId) {
        userProfileFacade.deleteFavoriteMovie(movieId);
        return ResponseEntity.ok(Map.of("message", "Favorite movie deleted successfully."));
    }
}
