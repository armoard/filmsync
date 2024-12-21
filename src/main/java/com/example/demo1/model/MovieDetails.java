package com.example.demo1.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MovieDetails {
    private Long id;
    private String title;
    private String overview;
    private String release_date;
    private String poster_path;
}