package com.example.demo1.dto.profile;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CommentDto {

    private Long id;
    private String content;
    private UserSummaryDto author;

    public CommentDto(Long id, String content, UserSummaryDto author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }


}