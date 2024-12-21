package com.example.demo1.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class MovieSearchResult {
    private Long id;
    private String title;

    public MovieSearchResult(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
