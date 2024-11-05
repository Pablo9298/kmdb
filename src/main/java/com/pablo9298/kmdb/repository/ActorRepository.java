package com.pablo9298.kmdb.repository;

import com.pablo9298.kmdb.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    // Finds actors by name, ignoring case sensitivity for the name field
    List<Actor> findByNameIgnoreCase(String name);
}
