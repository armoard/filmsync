package com.example.demo1.mapper;

import com.example.demo1.dto.profile.CommentDto;
import com.example.demo1.dto.profile.UserSummaryDto;
import com.example.demo1.entity.ReviewComment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final UserSummaryMapper userSummaryMapper;

    public CommentMapper(UserSummaryMapper userSummaryMapper) {
        this.userSummaryMapper = userSummaryMapper;
    }
    public CommentDto toDto(ReviewComment comment) {
        UserSummaryDto authorDto = userSummaryMapper.toSummaryDto(comment.getAuthor());

        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                authorDto
        );
    }
}