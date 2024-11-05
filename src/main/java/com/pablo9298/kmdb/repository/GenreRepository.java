package com.pablo9298.kmdb.repository;

import com.pablo9298.kmdb.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    // This interface inherits basic CRUD and query methods for the Genre entity from JpaRepository.
    // JpaRepository provides methods like save, findById, findAll, deleteById, etc.
}
