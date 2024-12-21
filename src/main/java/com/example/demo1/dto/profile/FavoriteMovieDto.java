package com.example.demo1.dto.profile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class FavoriteMovieDto {
    private String movieId;
    private String title;
    private String posterPath;

    public FavoriteMovieDto(String movieId, String title, String posterPath) {
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
    }
}
