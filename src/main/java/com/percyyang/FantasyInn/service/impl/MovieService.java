package com.percyyang.FantasyInn.service.impl;

import com.percyyang.FantasyInn.entity.Movie;
import com.percyyang.FantasyInn.repo.MovieRepository;
import com.percyyang.FantasyInn.service.interfaces.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService implements IMovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieByImdbId(String imdbId) {
        return movieRepository.findByImdbId(imdbId);
    }

}
