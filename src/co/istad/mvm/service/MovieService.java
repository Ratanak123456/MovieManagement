package co.istad.mvm.service;

import co.istad.mvm.model.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    void addMovie(Movie movie);
    boolean updateMovie(Movie movie);
    boolean deleteMovie(String id);
    Movie findMovieById(String id);
    List<Movie> findMoviesByTitle(String title);
    List<Movie> findMoviesByGenre(String genre);
    List<Movie> findMoviesByDirector(String director);
}