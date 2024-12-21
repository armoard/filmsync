package com.example.demo1.repository;

import com.example.demo1.entity.FavoriteMovie;
import com.example.demo1.entity.User;
import com.example.demo1.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByReviewsId(Long reviewId);
    Optional<UserProfile> findByUser_Username(String username);

    @Query("SELECT f FROM UserProfile u JOIN u.following f WHERE u = :user")
    Page<UserProfile> findFollowing(@Param("user") UserProfile user, Pageable pageable);

    @Query("SELECT f FROM UserProfile u JOIN u.followers f WHERE u = :user")
    Page<UserProfile> findFollowers(@Param("user") UserProfile user, Pageable pageable);

    @Query(value = "SELECT * FROM user_profile ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<UserProfile> findRandomUsers(@Param("limit") int limit);

    Page<UserProfile> findByUserUsernameContainingIgnoreCase(String username, Pageable pageable);

    @Query("SELECT COUNT(f) > 0 FROM UserProfile u JOIN u.following f WHERE u.id = :authUserId AND f.id = :targetProfileId")
    boolean isFollowing(@Param("authUserId") Long authUserId, @Param("targetProfileId") Long targetProfileId);

    @Query("SELECT COUNT(f) > 0 FROM UserProfile u JOIN u.followers f WHERE u.id = :authUserId AND f.id = :targetProfileId")
    boolean followsYou(@Param("authUserId") Long authUserId, @Param("targetProfileId") Long targetProfileId);

    @Query("SELECT u FROM UserProfile u LEFT JOIN FETCH u.following LEFT JOIN FETCH u.followers WHERE u.id = :id")
    Optional<UserProfile> findWithFollowersAndFollowingById(@Param("id") Long id);

    @Query("SELECT p FROM UserProfile u JOIN u.following p WHERE u.id = :authUserId")
    List<UserProfile> findFollowedProfiles(@Param("authUserId") Long authUserId);
}