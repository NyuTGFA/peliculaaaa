package com.example.peliculaaaa.pelicula.repositories;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.peliculaaaa.pelicula.Data.Dao.MovieDao;
import com.example.peliculaaaa.pelicula.Data.Database.MovieDatabase;
import com.example.peliculaaaa.pelicula.models.Movies;
import com.example.peliculaaaa.pelicula.network.ApiClient;
import com.example.peliculaaaa.pelicula.network.ApiService;
import com.example.peliculaaaa.pelicula.response.MovieDetailResponse;
import com.example.peliculaaaa.pelicula.response.MovieResponse;
import com.example.peliculaaaa.pelicula.utils.ApiConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieRepository {

    private MovieDao movieDao;
    private MovieDatabase movieDatabase;
    private ApiService apiService;
    private Executor executor;  // Executor para tareas en segundo plano

    public MovieRepository(Context context) {
        movieDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, "movie_database")
                .fallbackToDestructiveMigration()
                .build();
        movieDao = movieDatabase.movieDao();
        apiService = ApiClient.getClient().create(ApiService.class);

        // Inicialización del executor
        executor = Executors.newSingleThreadExecutor();
    }

    // Método para buscar películas por título usando un callback
    public void searchMovies(String searchQuery, int page, MovieRepositoryCallback callback) {
        // Realizar la consulta en un hilo en segundo plano
        apiService = ApiClient.getClient().create(ApiService.class);
        executor.execute(() -> {
            // Intentar obtener las películas de la base de datos
            List<Movies> cachedMovies = movieDao.getMoviesByTitle();
            if (cachedMovies != null && !cachedMovies.isEmpty()) {
                // Si tenemos resultados locales, devolverlos a través del callback
                callback.onSuccess(cachedMovies);
            }

            String apiKey = ApiConfig.getApiKey();
            // Realizar la búsqueda en la API
            apiService.searchMovies(apiKey,searchQuery,page).enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.body() != null && response.body().getMovies() != null) {
                        List<Movies> moviesFromApi = response.body().getMovies();
                        // Devolver los resultados de la API al callback
                        callback.onSuccess(moviesFromApi);

                        // Guardar las películas en la base de datos local para consultas futuras
                        executor.execute(() -> movieDao.insertMovies(moviesFromApi));  // Hacer la inserción en segundo plano
                    } else {
                        // Si no se encuentran películas, notificar el error
                        callback.onError("No se encontraron películas.");
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    // En caso de error, notificar el error
                    Log.i(
                            "hola","no hizo la consulta2");
                    callback.onError("Error al obtener las películas: " + t.getMessage());
                }
            });
        });
    }
    public void getMovieById(String movieId, MovieDetailCallback callback) {
        String apiKey = ApiConfig.getApiKey();

        apiService.searchMoviesbyid(apiKey, movieId).enqueue(new Callback<MovieDetailResponse>() {

            @Override
            public void onResponse(Call<MovieDetailResponse> call, Response<MovieDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Notificar al callback que la respuesta fue exitosa
                    callback.onSuccess(response.body());
                } else {
                    // Manejar el caso cuando la respuesta no fue exitosa
                    callback.onError("Error al obtener los detalles de la película");
                }
            }

            @Override
            public void onFailure(Call<MovieDetailResponse> call, Throwable t) {
                // Manejar el error de la solicitud
                callback.onError(t.getMessage());
            }
        });
    }


    public interface MovieDetailCallback {
        void onSuccess(MovieDetailResponse movie);
        void onError(String errorMessage);
    }


    // Interfaz para el callback
    public interface MovieRepositoryCallback {
        void onSuccess(List<Movies> movies);
        void onError(String errorMessage);
    }
}
