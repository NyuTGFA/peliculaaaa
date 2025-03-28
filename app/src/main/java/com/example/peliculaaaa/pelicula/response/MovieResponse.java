package com.example.peliculaaaa.pelicula.response;

import com.example.peliculaaaa.pelicula.models.Movies;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieResponse {

    @SerializedName("Search")  // El nombre del campo en el JSON es "Search"
    private List<Movies> movies;  // Lista de películas

    @SerializedName("totalResults")
    private String totalResults;  // Número total de resultados

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
