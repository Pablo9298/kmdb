package com.pablo9298.kmdb.service;

import com.pablo9298.kmdb.exception.ResourceNotFoundException;
import com.pablo9298.kmdb.model.*;
import com.pablo9298.kmdb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private GenreRepository genreRepository;

    // Creates a new movie with associated actors and genres
    public Movie createMovie(Movie movie) {

        if (movieRepository.findByTitleIgnoreCase(movie.getTitle()).isPresent()) {
            throw new IllegalStateException("Film with title '" + movie.getTitle() + "' already exists.");
        }

        Set<Actor> managedActors = new HashSet<>();
        for (Actor actor : movie.getActors()) {
            Actor managedActor = actorRepository.findById(actor.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actor.getId()));
            managedActors.add(managedActor);
        }
        movie.setActors(managedActors);

        Set<Genre> managedGenres = new HashSet<>();
        for (Genre genre : movie.getGenres()) {
            Genre managedGenre = genreRepository.findById(genre.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genre.getId()));
            managedGenres.add(managedGenre);
        }
        movie.setGenres(managedGenres);

        return movieRepository.save(movie);
    }

    // Retrieves all movies with pagination support
    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    // Retrieves all movies without pagination
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Retrieves a movie by its ID
    public Optional<Movie> getMovieById(Long id) throws ResourceNotFoundException {
        return Optional.of(movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id)));
    }

    // Updates an existing movie with specific fields
    public Movie updateMovie(Long id, Movie updatedFields) {
        if (updatedFields.getId() != null && !updatedFields.getId().equals(id)) {
            throw new IllegalArgumentException("ID field cannot be modified.");
        }

        return movieRepository.findById(id).map(movie -> {
            // Update only the fields provided in the request
            if (updatedFields.getTitle() != null) {
                movie.setTitle(updatedFields.getTitle());
            }
            if (updatedFields.getReleaseYear() != null) {
                movie.setReleaseYear(updatedFields.getReleaseYear());
            }
            if (updatedFields.getDuration() != null) {
                movie.setDuration(updatedFields.getDuration());
            }

            // Update genres if provided, otherwise retain current values
            if (updatedFields.getGenres() != null) {
                if (!updatedFields.getGenres().isEmpty()) {
                    movie.setGenres(updatedFields.getGenres());
                } else {
                    movie.setGenres(movie.getGenres()); // Retain current genres if empty
                }
            }

            // Update actors if provided, otherwise retain current values
            if (updatedFields.getActors() != null) {
                if (!updatedFields.getActors().isEmpty()) {
                    Set<Actor> managedActors = updatedFields.getActors().stream()
                            .map(actor -> actorRepository.findById(actor.getId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actor.getId())))
                            .collect(Collectors.toSet());
                    movie.setActors(managedActors);
                } else {
                    movie.setActors(movie.getActors()); // Retain current actors if empty
                }
            }

            return movieRepository.save(movie);
        }).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    // Deletes a movie by its ID, optionally forcing deletion of associations
    public void deleteMovie(Long movieId, boolean force) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));

        // Check if the movie has associations with actors or genres
        if (!force && (!movie.getActors().isEmpty() || !movie.getGenres().isEmpty())) {
            throw new IllegalStateException("Cannot delete movie with associated actors or genres. Use force=true to override.");
        }

        // If force=true, remove associations and delete the movie
        if (force || (movie.getActors().isEmpty() && movie.getGenres().isEmpty())) {
            movie.getActors().forEach(actor -> actor.getMovies().remove(movie));
            movie.getGenres().forEach(genre -> genre.getMovies().remove(movie));
            movieRepository.delete(movie);
        }
    }

    // Retrieves movies based on a specific genre ID
    public List<Movie> getMoviesByGenreId(Long genreId) {
        if (!genreRepository.existsById(genreId)) {
            throw new ResourceNotFoundException("Genre not found with ID: " + genreId);
        }
        return movieRepository.findMoviesByGenreId(genreId);
    }

    // Retrieves movies by release year
    public List<Movie> getMoviesByReleaseYear(Integer year) {
        List<Movie> movies = movieRepository.findAll().stream()
                .filter(movie -> movie.getReleaseYear() != null && movie.getReleaseYear().equals(year))
                .toList();

        if (movies.isEmpty()) {
            throw new ResourceNotFoundException("No movies found for the year: " + year);
        }

        return movies;
    }

    // Retrieves all actors associated with a given movie ID
    public Set<Actor> getActorsByMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId))
                .getActors();
    }

    // Searches movies by title (case-insensitive search)
    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }
}
