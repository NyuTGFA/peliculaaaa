package com.example.peliculaaaa.pelicula.Data.Dao;

import android.graphics.Movie;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.peliculaaaa.pelicula.models.Movies;

import java.util.List;
@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    List<Movies> getAllMovies();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movies> movies);


    @Query("SELECT * FROM movies WHERE id = :movieId")
    Movies getMovieById(String movieId);


    @Query("DELETE FROM movies")
    void deleteAllMovies();

    // Buscar películas por título
    @Query("SELECT * FROM movies")
    List<Movies> getMoviesByTitle();
}
