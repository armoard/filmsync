package com.example.demo1.mapper;
import com.example.demo1.dto.profile.FavoriteMovieDto;
import com.example.demo1.entity.FavoriteMovie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FavoriteMovieMapper {

    public FavoriteMovieDto toDto(FavoriteMovie favoriteMovie) {
        return new FavoriteMovieDto(
                favoriteMovie.getMovieId(),
                favoriteMovie.getTitle(),
                favoriteMovie.getPoster_path()
        );
    }
    public List<FavoriteMovieDto> toDtoList(List<FavoriteMovie> favoriteMovies) {
        return favoriteMovies.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}