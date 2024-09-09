package com.percyyang.FantasyInn.service.interfaces;

import com.percyyang.FantasyInn.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface IMovieService {

    List<Movie> getAllMovies();

    Optional<Movie> getMovieByImdbId(String imdbId);

}
