package com.example.demo1.repository;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserProfileIdAndMovieId(Long userProfileId, String movieId);
    Page<Review> findByUserProfile(UserProfile user, Pageable pageable);
    boolean existsByUserProfileAndMovieId(UserProfile userProfile, String movieId);
    List<Review> findTop5ByUserProfileOrderByCreatedAtDesc(UserProfile userProfile);
    @Query("SELECT r FROM Review r WHERE r.movieId = :movieId AND r.userProfile IN :friends")
    List<Review> findByMovieIdAndUserProfiles(@Param("movieId") String movieId, @Param("friends") List<UserProfile> friends);
}