package com.pablo9298.kmdb.repository;

import com.pablo9298.kmdb.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Custom query to find movies by genre ID
    // Joins the Movie and Genre entities and selects movies with the specified genre ID
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    List<Movie> findMoviesByGenreId(@Param("genreId") Long genreId);

    // Finds movies by title, ignoring case and allowing partial matches
    List<Movie> findByTitleContainingIgnoreCase(String title);
}
