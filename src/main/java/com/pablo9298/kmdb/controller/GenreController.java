package com.pablo9298.kmdb.controller;

import com.pablo9298.kmdb.exception.ResourceNotFoundException;
import com.pablo9298.kmdb.model.Genre;
import com.pablo9298.kmdb.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    // Creates a new genre
    @PostMapping
    public Genre createGenre(@RequestBody Genre genre) {
        return genreService.createGenre(genre);
    }

    // Retrieves all genres
    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    // Retrieves a genre by ID
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    // Updates an existing genre (PATCH)
    @PatchMapping("/{id}")
    public Genre updateGenre(@PathVariable Long id, @RequestBody Genre updateGenre) {
        return genreService.updateGenre(id, updateGenre);
    }

    // Deletes a genre, with an optional force parameter to override restrictions
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id,
                                         @RequestParam(defaultValue = "false") boolean force) {
        try {
            genreService.deleteGenre(id, force);
            return ResponseEntity.noContent().build();  // Returns 204 No Content on successful deletion
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
