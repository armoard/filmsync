package com.example.demo1.repository;

import com.example.demo1.entity.FavoriteMovie;
import com.example.demo1.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {
    List<FavoriteMovie> findByUserProfile(UserProfile userProfile);
    boolean existsByMovieIdAndUserProfileId(String movieId, Long userProfileId);
    long countByUserProfileId(Long userProfileId);
    Optional<FavoriteMovie> findByMovieIdAndUserProfileId(String movieId, Long userId);

}