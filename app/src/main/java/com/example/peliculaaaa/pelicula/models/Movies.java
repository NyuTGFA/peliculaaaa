package com.example.peliculaaaa.pelicula.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "movies")
public class Movies {
    @PrimaryKey
    @NonNull
    @SerializedName("imdbID")
    private String id;

    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("Poster")
    private String posterUrl;

    @SerializedName("Type")
    private String type;

    // Constructor
    public Movies(String id, String title, String year, String posterUrl, String type) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.posterUrl = posterUrl;
        this.type = type;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movies movies = (Movies) o;
        return Objects.equals(id, movies.id) &&  // Compara el ID, que es único
                Objects.equals(title, movies.title) &&
                Objects.equals(year, movies.year) &&
                Objects.equals(posterUrl, movies.posterUrl) &&
                Objects.equals(type, movies.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, year, posterUrl, type); // Asegúrate de que todos los atributos relevantes estén aquí
    }
}
