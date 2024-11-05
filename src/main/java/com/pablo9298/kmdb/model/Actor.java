package com.pablo9298.kmdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Actor {

    // Unique identifier for each actor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the actor, cannot be null and must be between 2 and 100 characters
    @Column(nullable = false)
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    // Birth date of the actor, cannot be null and must follow the pattern YYYY-MM-DD
    @Column(nullable = false)
    @NotNull(message = "Birth date cannot be null")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birth date must follow the format YYYY-MM-DD")
    private String birthDate;

    // Many-to-Many relationship with Movie entity
    // JsonIgnore prevents serialization of movies to avoid circular references in JSON responses
    @ManyToMany
    @JoinTable(
            name = "movie_actor",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    // Default constructor
    public Actor() {
        this.id = null;
    }

    // Parameterized constructor
    public Actor(String name, String birthDate) {
        this.id = null;
        this.name = name;
        this.birthDate = birthDate;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
