package com.pablo9298.kmdb.controller;

import com.pablo9298.kmdb.exception.ResourceNotFoundException;
import com.pablo9298.kmdb.model.*;
import com.pablo9298.kmdb.service.*;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ActorService actorService;

    // Creates a new movie
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody @Valid Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    // Retrieves all movies with optional pagination
    @GetMapping
    public ResponseEntity<?> getAllMovies(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        // Validate page and size parameters
        if (page != null && page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Page parameter cannot be negative"));
        }

        if (size != null && (size < 1 || size > 100)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Size parameter must be between 1 and 100"));
        }

        // Apply pagination if parameters are provided
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> moviesPage = movieService.getAllMovies(pageable);
            List<Movie> movies = moviesPage.getContent();
            return ResponseEntity.ok(movies);
        } else {
            List<Movie> movies = movieService.getAllMovies();
            return ResponseEntity.ok(movies);
        }
    }

    // Retrieves a movie by ID
    @GetMapping(path = "/{id}")
    public Movie getMovieById(
            @PathVariable Long id)
            throws ResourceNotFoundException {

        Optional<Movie> movie = movieService.getMovieById(id);

        if (movie.isPresent()) {
            return movie.get();
        } else {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }
    }

    // Updates an existing movie (PATCH)
    @PatchMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie updateMovie) {
        return movieService.updateMovie(id, updateMovie);
    }

    // Deletes a movie, with an optional force parameter to override restrictions
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id,
                                         @RequestParam(defaultValue = "false") boolean force) {
        try {
            movieService.deleteMovie(id, force);
            return ResponseEntity.noContent().build();  // Returns 204 No Content on successful deletion
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Filters movies by genre
    @GetMapping(params = "genre")
    public List<Movie> getMoviesByGenre(@RequestParam("genre") Long genreId) {
        return movieService.getMoviesByGenreId(genreId);
    }

    // Filters movies by release year
    @GetMapping(params = "year")
    public List<Movie> getMoviesByReleaseYear(@RequestParam Integer year) {
        return movieService.getMoviesByReleaseYear(year);
    }

    // Retrieves all actors associated with a specific movie
    @GetMapping("/{movieId}/actors")
    public Set<Actor> getActorsByMovie(@PathVariable Long movieId) {
        return movieService.getActorsByMovie(movieId);
    }

    // Retrieves all movies in which a specified actor has appeared
    @GetMapping(params = "actor")
    public Set<Movie> getMoviesByActor(@RequestParam("actor") Long actorId) {
        return actorService.getMoviesByActorId(actorId);
    }

    // Searches for movies by title
    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam("title") String title) {
        List<Movie> movies = movieService.searchMoviesByTitle(title);
        return ResponseEntity.ok(movies);
    }
}
