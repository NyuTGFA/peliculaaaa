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
import com.example.peliculaaaa.pelicula.response.MovieDetailResponse;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movies>> movieSearchResults = new MutableLiveData<>();
    private MutableLiveData<MovieDetailResponse> movieseachdetail= new MutableLiveData<>();
    private MovieRepository movieRepository;

    public MovieViewModel(Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    // Método para obtener los resultados de búsqueda
    public LiveData<List<Movies>> getMovieSearchResults() {
        return movieSearchResults;
    }
    public LiveData <MovieDetailResponse> getmovieDetail(){return movieseachdetail;}

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

    public void searchMovieDetail(String id){
        movieRepository.getMovieById(id,new MovieRepository.MovieDetailCallback (){


            @Override
            public void onSuccess(MovieDetailResponse movie) {
                movieseachdetail.postValue(movie);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });


    }


}


