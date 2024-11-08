package com.pablo9298.kmdb.controller;

import com.pablo9298.kmdb.exception.ResourceNotFoundException;
import com.pablo9298.kmdb.model.Actor;
import com.pablo9298.kmdb.model.Movie;
import com.pablo9298.kmdb.service.ActorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    // Creates a new actor
    @PostMapping
    public ResponseEntity<?> createActor(@RequestBody @Valid Actor actor) {
        if (!isValidDate(actor.getBirthDate())) {
            return new ResponseEntity<>("Birth date must be in the format YYYY-MM-DD", HttpStatus.BAD_REQUEST);
        }
        Actor createdActor = actorService.createActor(actor);
        return new ResponseEntity<>(createdActor, HttpStatus.CREATED);
    }

    // Retrieves all actors
    @GetMapping
    public List<Actor> getAllActors() {
        return actorService.getAllActors();
    }

    // Retrieves an actor by ID
    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable Long id) {
        return actorService.getActorById(id);
    }

    // Updates an existing actor (PATCH)
    @PatchMapping("/{id}")
    public Actor updateActor(@PathVariable Long id, @RequestBody Actor updateActor) {
        return actorService.updateActor(id, updateActor);
    }

    // Deletes an actor, with an optional force parameter
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable Long id,
                                         @RequestParam(defaultValue = "false") boolean force) {
        try {
            actorService.deleteActor(id, force);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Filters actors by name
    @GetMapping(params = "name")
    public List<Actor> getActorsByName(@RequestParam String name) {
        return actorService.getActorsByName(name);
    }

    // Retrieves all movies an actor has appeared in
    @GetMapping("/{actorId}/movies")
    public Set<Movie> getMoviesByActor(@PathVariable Long actorId) {
        return actorService.getMoviesByActor(actorId);
    }

    // Checks if a date string matches the format YYYY-MM-DD
    private boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);  // Checks if date is in the correct format
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
