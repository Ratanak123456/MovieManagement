package co.istad.mvm.database;

import co.istad.mvm.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieDatabase {
    private List<Movie> movies;

    public MovieDatabase() {
        movies = new ArrayList<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Clear any existing movies
        movies.clear();

        // Movie 1: The Shawshank Redemption
        Movie movie1 = new Movie();
        movie1.setId(generateMovieId());
        movie1.setTitle("The Shawshank Redemption");
        movie1.setDirector("Frank Darabont");
        movie1.setReleaseYear(1994);
        movie1.setGenre("Drama");
        movie1.setRating(9.3);
        movie1.setDuration(142);
        movies.add(movie1);

        // Movie 2: The Godfather
        Movie movie2 = new Movie();
        movie2.setId(generateMovieId());
        movie2.setTitle("The Godfather");
        movie2.setDirector("Francis Ford Coppola");
        movie2.setReleaseYear(1972);
        movie2.setGenre("Crime");
        movie2.setRating(9.2);
        movie2.setDuration(175);
        movies.add(movie2);

        // Movie 3: The Dark Knight
        Movie movie3 = new Movie();
        movie3.setId(generateMovieId());
        movie3.setTitle("The Dark Knight");
        movie3.setDirector("Christopher Nolan");
        movie3.setReleaseYear(2008);
        movie3.setGenre("Action");
        movie3.setRating(9.0);
        movie3.setDuration(152);
        movies.add(movie3);

        // Movie 4: Pulp Fiction
        Movie movie4 = new Movie();
        movie4.setId(generateMovieId());
        movie4.setTitle("Pulp Fiction");
        movie4.setDirector("Quentin Tarantino");
        movie4.setReleaseYear(1994);
        movie4.setGenre("Crime");
        movie4.setRating(8.9);
        movie4.setDuration(154);
        movies.add(movie4);

        // Movie 5: Forrest Gump
        Movie movie5 = new Movie();
        movie5.setId(generateMovieId());
        movie5.setTitle("Forrest Gump");
        movie5.setDirector("Robert Zemeckis");
        movie5.setReleaseYear(1994);
        movie5.setGenre("Drama");
        movie5.setRating(8.8);
        movie5.setDuration(142);
        movies.add(movie5);

        // Movie 6: Inception
        Movie movie6 = new Movie();
        movie6.setId(generateMovieId());
        movie6.setTitle("Inception");
        movie6.setDirector("Christopher Nolan");
        movie6.setReleaseYear(2010);
        movie6.setGenre("Sci-Fi");
        movie6.setRating(8.8);
        movie6.setDuration(148);
        movies.add(movie6);

        // Movie 7: The Matrix
        Movie movie7 = new Movie();
        movie7.setId(generateMovieId());
        movie7.setTitle("The Matrix");
        movie7.setDirector("Lana Wachowski");
        movie7.setReleaseYear(1999);
        movie7.setGenre("Action");
        movie7.setRating(8.7);
        movie7.setDuration(136);
        movies.add(movie7);

        // Movie 8: Parasite
        Movie movie8 = new Movie();
        movie8.setId(generateMovieId());
        movie8.setTitle("Parasite");
        movie8.setDirector("Bong Joon-ho");
        movie8.setReleaseYear(2019);
        movie8.setGenre("Thriller");
        movie8.setRating(8.6);
        movie8.setDuration(132);
        movies.add(movie8);

        // Movie 9: Spirited Away
        Movie movie9 = new Movie();
        movie9.setId(generateMovieId());
        movie9.setTitle("Spirited Away");
        movie9.setDirector("Hayao Miyazaki");
        movie9.setReleaseYear(2001);
        movie9.setGenre("Animation");
        movie9.setRating(8.6);
        movie9.setDuration(125);
        movies.add(movie9);

        // Movie 10: Interstellar
        Movie movie10 = new Movie();
        movie10.setId(generateMovieId());
        movie10.setTitle("Interstellar");
        movie10.setDirector("Christopher Nolan");
        movie10.setReleaseYear(2014);
        movie10.setGenre("Sci-Fi");
        movie10.setRating(8.6);
        movie10.setDuration(169);
        movies.add(movie10);

        // Movie 11: The Lion King
        Movie movie11 = new Movie();
        movie11.setId(generateMovieId());
        movie11.setTitle("The Lion King");
        movie11.setDirector("Roger Allers");
        movie11.setReleaseYear(1994);
        movie11.setGenre("Animation");
        movie11.setRating(8.5);
        movie11.setDuration(88);
        movies.add(movie11);

        // Movie 12: Avengers: Endgame
        Movie movie12 = new Movie();
        movie12.setId(generateMovieId());
        movie12.setTitle("Avengers: Endgame");
        movie12.setDirector("Anthony Russo");
        movie12.setReleaseYear(2019);
        movie12.setGenre("Action");
        movie12.setRating(8.4);
        movie12.setDuration(181);
        movies.add(movie12);

        // Movie 13: Joker
        Movie movie13 = new Movie();
        movie13.setId(generateMovieId());
        movie13.setTitle("Joker");
        movie13.setDirector("Todd Phillips");
        movie13.setReleaseYear(2019);
        movie13.setGenre("Crime");
        movie13.setRating(8.4);
        movie13.setDuration(122);
        movies.add(movie13);

        // Movie 14: The Silence of the Lambs
        Movie movie14 = new Movie();
        movie14.setId(generateMovieId());
        movie14.setTitle("The Silence of the Lambs");
        movie14.setDirector("Jonathan Demme");
        movie14.setReleaseYear(1991);
        movie14.setGenre("Thriller");
        movie14.setRating(8.6);
        movie14.setDuration(118);
        movies.add(movie14);

        // Movie 15: Gladiator
        Movie movie15 = new Movie();
        movie15.setId(generateMovieId());
        movie15.setTitle("Gladiator");
        movie15.setDirector("Ridley Scott");
        movie15.setReleaseYear(2000);
        movie15.setGenre("Action");
        movie15.setRating(8.5);
        movie15.setDuration(155);
        movies.add(movie15);
    }

    // Add this method to generate movie IDs
    private String generateMovieId() {
        return "MOV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public boolean updateMovie(Movie updatedMovie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getId().equals(updatedMovie.getId())) {
                movies.set(i, updatedMovie);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMovie(String id) {
        return movies.removeIf(movie -> movie.getId().equals(id));
    }

    public Movie findMovieById(String id) {
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                return movie;
            }
        }
        return null;
    }

    public List<Movie> findMoviesByTitle(String title) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }

    public List<Movie> findMoviesByGenre(String genre) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getGenre().equalsIgnoreCase(genre)) {
                result.add(movie);
            }
        }
        return result;
    }

    public List<Movie> findMoviesByDirector(String director) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getDirector().toLowerCase().contains(director.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }
}