package com.example.demo1.service.movie;

import com.example.demo1.exceptions.movie.MovieIdException;
import com.example.demo1.exceptions.movie.MovieNotFoundException;
import com.example.demo1.model.MovieDetails;
import com.example.demo1.model.MovieSearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MovieService {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    public MovieService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MovieDetails getMovieDetails(String movieId) {
        if (movieId == null || movieId.isEmpty()) {
            throw new MovieIdException("Movie ID may not be null or empty");
        }
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
        try {
            return restTemplate.getForObject(url, MovieDetails.class);
        } catch (HttpClientErrorException ex) {
            throw new MovieNotFoundException("Movie not found for ID: " + movieId, ex);
        }
    }

    public List<MovieSearchResult> getMoviesSearchResult(String input) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", input)
                .toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.get("results");

            List<MovieSearchResult> movieresults = new ArrayList<>();
            if (results != null && results.isArray()) {
                for (JsonNode result : results) {
                    Long id = result.get("id").asLong();
                    String title = result.get("title").asText();
                    movieresults.add(new MovieSearchResult(id, title));
                }
            }
            return movieresults;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}
