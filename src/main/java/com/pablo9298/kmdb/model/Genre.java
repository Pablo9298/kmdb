package com.pablo9298.kmdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Genre {

    // Unique identifier for each genre
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the genre, cannot be null
    @Column(nullable = false)
    @NotNull(message = "Name cannot be null")
    private String name;

    // Many-to-Many relationship with Movie entity
    // JsonIgnore prevents serialization of movies to avoid circular references in JSON responses
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    // Default constructor
    public Genre() {
        this.id = null;
    }

    // Parameterized constructor
    public Genre(String name) {
        this.id = null;
        this.name = name;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
