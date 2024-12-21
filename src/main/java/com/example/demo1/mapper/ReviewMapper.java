package com.example.demo1.mapper;

import com.example.demo1.dto.profile.CommentDto;
import com.example.demo1.dto.profile.UserReviewDto;
import com.example.demo1.entity.Review;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ReviewMapper {
    private final CommentMapper commentMapper;
    public ReviewMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public UserReviewDto toDto(Review review) {
        List<CommentDto> commentDtos = review.getReviewComments()
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());

        return UserReviewDto.builder()
                .reviewId(review.getId())
                .movieId(review.getMovieId())
                .reviewContent(review.getReviewContent())
                .reviewRating(review.getReviewRating())
                .posterPath(review.getPoster_path())
                .comments(commentDtos)
                .build();
    }
}
