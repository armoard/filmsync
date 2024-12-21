package com.example.demo1.dto.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCommentRequest {
    @NotBlank(message = "Comment content cannot be empty")
    private String content;
    @NotBlank(message = "Review ID cannot be empty")
    private String reviewId;
}
