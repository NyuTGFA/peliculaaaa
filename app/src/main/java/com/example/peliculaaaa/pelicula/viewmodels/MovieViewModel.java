package com.example.peliculaaaa.pelicula.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peliculaaaa.pelicula.models.Movies;
import com.example.peliculaaaa.pelicula.repositories.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movies>> movieSearchResults = new MutableLiveData<>();
    private MovieRepository movieRepository;

    public MovieViewModel(Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    // Método para obtener los resultados de búsqueda
    public LiveData<List<Movies>> getMovieSearchResults() {
        return movieSearchResults;
    }

    // Método para buscar películas
    public void searchMovies(String query, int page, MovieRepository.MovieRepositoryCallback callback) {
        movieRepository.searchMovies(query, page, new MovieRepository.MovieRepositoryCallback() {
            @Override
            public void onSuccess(List<Movies> movies) {
                movieSearchResults.postValue(movies);   // Actualiza el LiveData
            }

            @Override
            public void onError(String error) {
                // Aquí puedes manejar el error si es necesario
            }
        });
    }
}


