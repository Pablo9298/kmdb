package com.pablo9298.kmdb.service;

import com.pablo9298.kmdb.exception.ResourceNotFoundException;
import com.pablo9298.kmdb.model.*;
import com.pablo9298.kmdb.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;

    // Creates a new actor
    public Actor createActor(Actor actor) {
        return actorRepository.save(actor);
    }

    // Retrieves all actors
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    // Retrieves an actor by ID
    public Actor getActorById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }

    // Updates an existing actor (PATCH)
    public Actor updateActor(Long id, Actor updateActor) {
        // Check if there is an attempt to modify the ID field
        if (updateActor.getId() != null && !updateActor.getId().equals(id)) {
            throw new IllegalArgumentException("ID field cannot be modified.");
        }

        return actorRepository.findById(id).map(actor -> {
            // Update fields only if provided in the request
            if (updateActor.getName() != null) {
                actor.setName(updateActor.getName());
            }
            if (updateActor.getBirthDate() != null) {
                actor.setBirthDate(updateActor.getBirthDate());
            }
            if (updateActor.getMovies() != null) {
                actor.setMovies(updateActor.getMovies());
            }
            return actorRepository.save(actor);
        }).orElseThrow(() -> new RuntimeException("Actor not found with id " + id));
    }

    // Deletes an actor with a check for the force parameter
    public void deleteActor(Long actorId, boolean force) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with ID: " + actorId));

        // Check if the actor is associated with movies
        if (!force && !actor.getMovies().isEmpty()) {
            throw new IllegalStateException("Cannot delete actor with associated movies. Use force=true to override.");
        }

        // If force=true, remove associations and delete the actor
        if (force || actor.getMovies().isEmpty()) {
            // Remove all associations between the actor and movies if force=true
            actor.getMovies().forEach(movie -> movie.getActors().remove(actor));
            actorRepository.delete(actor);
        }
    }

    // Retrieves actors by name (case-insensitive)
    public List<Actor> getActorsByName(String name) {
        List<Actor> actors = actorRepository.findByNameIgnoreCase(name);
        if (actors.isEmpty()) {
            throw new ResourceNotFoundException("No actors found with name: " + name);
        }
        return actors;
    }

    // Retrieves all movies an actor has appeared in by actor ID
    public Set<Movie> getMoviesByActor(Long actorId) {
        return actorRepository.findById(actorId)
                .orElseThrow(() -> new RuntimeException("Actor not found with id " + actorId))
                .getMovies();
    }

    // Retrieves all movies for a specific actor by actor ID with additional checks
    public Set<Movie> getMoviesByActorId(Long actorId) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with ID: " + actorId));

        Set<Movie> movies = actor.getMovies();
        if (movies.isEmpty()) {
            throw new ResourceNotFoundException("No movies found for actor with ID: " + actorId);
        }

        return movies;
    }
}
