package com.pablo9298.kmdb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Movie {

    // Unique identifier for each movie
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Title of the movie, cannot be null, with a length restriction
    @Column(nullable = false)
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    // Release year of the movie with validation constraints
    @Column
    @Min(value = 1888, message = "Release year must be after 1888")
    @Max(value = 2100, message = "Release year must be before 2100")
    private Integer releaseYear;

    // Duration of the movie in minutes, must be greater than 0
    @Column
    @Min(value = 1, message = "Duration must be greater than 0 minutes")
    private Integer duration;

    // Many-to-Many relationship with Genre entity
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    // Many-to-Many relationship with Actor entity, with specific fetch and cascade types
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();

    // Default constructor
    public Movie() {
        this.id = null;
    }

    // Parameterized constructor
    public Movie(String title, Integer releaseYear, Integer duration) {
        this.id = null;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }
}
