package co.istad.mvm.service;

import co.istad.mvm.database.MovieDatabase;
import co.istad.mvm.model.Movie;

import java.util.List;
import java.util.UUID;

public class MovieServiceImpl implements MovieService {
    private MovieDatabase database;

    public MovieServiceImpl() {
        this.database = new MovieDatabase();
    }

    @Override
    public List<Movie> getAllMovies() {
        return database.getAllMovies();
    }

    @Override
    public void addMovie(Movie movie) {
        String uuid = "MOV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        movie.setId(uuid);
        database.addMovie(movie);
    }

    @Override
    public boolean updateMovie(Movie movie) {
        return database.updateMovie(movie);
    }

    @Override
    public boolean deleteMovie(String id) {
        return database.deleteMovie(id);
    }

    @Override
    public Movie findMovieById(String id) {
        return database.findMovieById(id);
    }

    @Override
    public List<Movie> findMoviesByTitle(String title) {
        return database.findMoviesByTitle(title);
    }

    @Override
    public List<Movie> findMoviesByGenre(String genre) {
        return database.findMoviesByGenre(genre);
    }

    @Override
    public List<Movie> findMoviesByDirector(String director) {
        return database.findMoviesByDirector(director);
    }
}