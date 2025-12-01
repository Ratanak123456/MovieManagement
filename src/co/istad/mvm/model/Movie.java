package co.istad.mvm.model;

public class Movie {
    private String id;
    private String title;
    private String director;
    private int releaseYear;
    private String genre;
    private double rating;
    private int duration;

    public Movie() {}

    public Movie(String id, String title, String director, int releaseYear, String genre, double rating, int duration) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}