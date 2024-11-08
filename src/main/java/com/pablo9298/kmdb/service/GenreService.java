package com.pablo9298.kmdb.service;

import com.pablo9298.kmdb.exception.ResourceNotFoundException;
import com.pablo9298.kmdb.model.*;
import com.pablo9298.kmdb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Creates a new genre
    public Genre createGenre(Genre genre) {
        Optional<Genre> existingGenre = genreRepository.findByNameIgnoreCase(genre.getName());
        if(existingGenre.isPresent()) {
            throw new IllegalStateException("Genre with name '" + genre.getName() + "' already exists");
        }
        return genreRepository.save(genre);
    }

    // Retrieves all genres
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    // Retrieves a genre by its ID
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    // Updates an existing genre (PATCH)
    public Genre updateGenre(Long id, Genre updatedFields) {
        // Check if there is an attempt to modify the ID field
        if (updatedFields.getId() != null && !updatedFields.getId().equals(id)) {
            throw new IllegalArgumentException("ID field cannot be modified.");
        }

        return genreRepository.findById(id).map(genre -> {
            genre.setName(updatedFields.getName()); // Update only allowed fields
            return genreRepository.save(genre);
        }).orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
    }

    // Deletes a genre, with an option to force delete
    @Transactional
    public void deleteGenre(Long genreId, boolean force) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + genreId));

        // Check if the genre has associated movies
        if (!force && !genre.getMovies().isEmpty()) {
            throw new IllegalStateException("Cannot delete genre with associated movies. Use force=true to override.");
        }

        // If force=true, remove associations and delete the genre
        if (force || genre.getMovies().isEmpty()) {
            genre.getMovies().forEach(movie -> movie.getGenres().remove(genre));
            genreRepository.delete(genre);
        }
    }
}
