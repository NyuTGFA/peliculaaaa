package com.example.peliculaaaa.pelicula.network;

import com.example.peliculaaaa.pelicula.models.Movies;
import com.example.peliculaaaa.pelicula.response.MovieDetailResponse;
import com.example.peliculaaaa.pelicula.response.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

        // Endpoint para obtener películas por búsqueda
        @GET("/")
        Call<MovieResponse> getMovies(@Query("s") String searchQuery,
                                      @Query("r") String responseFormat,
                                      @Query("apikey") String apiKey);

        // Endpoint para obtener los detalles de una película
        @GET("/")
        Call<Movies> getMovieDetails(@Query("i") String imdbId,
                                     @Query("r") String responseFormat,
                                     @Query("apikey") String apiKey);

        // Búsqueda de películas por título
        @GET("/")
        Call<MovieResponse> searchMovies(   @Query("apikey") String apiKey,
                                            @Query("s") String searchQuery,
                                            @Query("page") int page);

        @GET("/")
        Call<MovieDetailResponse> searchMoviesbyid(@Query("apikey") String apiKey,
                                                   @Query("i") String searchQuery);

}

