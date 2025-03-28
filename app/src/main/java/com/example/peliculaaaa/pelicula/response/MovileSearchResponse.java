package com.example.peliculaaaa.pelicula.response;

import com.example.peliculaaaa.pelicula.models.Movies;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovileSearchResponse {

    @SerializedName("Search")  // El campo en el JSON es "Search"
    private List<Movies> movies;  // Lista de películas obtenidas

    @SerializedName("totalResults")
    private String totalResults;  // Total de resultados encontrados

    // Getters y setters
    public List<Movies> getMovies() {
        return movies;
    }

    public void setMovies(List<Movies> movies) {
        this.movies = movies;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
