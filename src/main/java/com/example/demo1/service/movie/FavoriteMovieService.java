package com.example.demo1.service.movie;

import com.example.demo1.dto.profile.FavoriteMovieDto;
import com.example.demo1.dto.review.MovieState;
import com.example.demo1.entity.FavoriteMovie;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.UserProfile;
import com.example.demo1.exceptions.Profile.FavoriteMovieNotFoundException;
import com.example.demo1.exceptions.Profile.MovieAlreadyFavoritedException;
import com.example.demo1.exceptions.Profile.TooManyFavoriteMoviesException;
import com.example.demo1.mapper.FavoriteMovieMapper;
import com.example.demo1.repository.FavoriteMovieRepository;
import com.example.demo1.repository.ReviewRepository;
import com.example.demo1.service.security.SecurityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteMovieService {
    private final FavoriteMovieRepository favoriteMovieRepository;
    private final SecurityService securityService;
    private final ReviewRepository reviewRepository;
    private final FavoriteMovieMapper favoriteMovieMapper;
    public FavoriteMovieService(FavoriteMovieRepository favoriteMovieRepository, SecurityService securityService,
                                ReviewRepository reviewRepository,FavoriteMovieMapper favoriteMovieMapper) {
        this.favoriteMovieRepository = favoriteMovieRepository;
        this.securityService = securityService;
        this.reviewRepository = reviewRepository;
        this.favoriteMovieMapper = favoriteMovieMapper;
    }

    @Transactional
    public void addAsFavoriteMovie(FavoriteMovieDto input) {
        Long userId = securityService.getAuthenticatedUserProfile().getId();
        boolean alreadyFavorite = favoriteMovieRepository.existsByMovieIdAndUserProfileId(input.getMovieId(), userId);
        if (alreadyFavorite) {
            throw new MovieAlreadyFavoritedException("This movie is already in your favorites.");
        }
        long favoriteCount = favoriteMovieRepository.countByUserProfileId(userId);
        if (favoriteCount >= 5) {
            throw new TooManyFavoriteMoviesException("You have reached the maximum of favorite movies in your profile.");
        }

        UserProfile profile = securityService.getAuthenticatedUserProfile();
        FavoriteMovie favoriteMovie = new FavoriteMovie();
        favoriteMovie.setTitle(input.getTitle());
        favoriteMovie.setMovieId(input.getMovieId());
        favoriteMovie.setUserProfile(profile);
        favoriteMovie.setPoster_path(input.getPosterPath());
        favoriteMovieRepository.save(favoriteMovie);

    }

    @Transactional
    public void deleteFavoriteMovie(String movieId) {
        Long userId = securityService.getAuthenticatedUserProfile().getId();
        Optional<FavoriteMovie> optionalMovie = favoriteMovieRepository.findByMovieIdAndUserProfileId(movieId, userId);
        FavoriteMovie movieToDelete = optionalMovie
                .orElseThrow(() -> new FavoriteMovieNotFoundException("Favorite movie not found"));
        favoriteMovieRepository.delete(movieToDelete);
    }


    public List<FavoriteMovieDto> getFavoriteMoviesByProfile(UserProfile profile){
        List<FavoriteMovie> favoriteMovies = favoriteMovieRepository.findByUserProfile(profile);
        return favoriteMovies.stream()
                .map(favoriteMovieMapper::toDto)
                .collect(Collectors.toList());
    }

    public MovieState getMovieState(String movieId) {
        Long userId = securityService.getAuthenticatedUserProfile().getId();
        MovieState movieState = new MovieState();
        boolean isFavorite = favoriteMovieRepository.existsByMovieIdAndUserProfileId(movieId, userId);
        movieState.setFavorite(isFavorite);
        Optional<Review> userReview = reviewRepository.findByUserProfileIdAndMovieId(userId, movieId);
        userReview.ifPresent(review -> {
            movieState.setReviewed(true);
            movieState.setReviewContent(review.getReviewContent());
            movieState.setReviewRating(review.getReviewRating());
        });
        return movieState;
    }
}