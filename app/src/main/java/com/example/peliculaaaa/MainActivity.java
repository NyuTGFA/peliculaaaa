package com.example.peliculaaaa;



import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peliculaaaa.pelicula.adapters.MovieAdapter;
import com.example.peliculaaaa.pelicula.models.Movies;
import com.example.peliculaaaa.pelicula.repositories.MovieRepository;
import com.example.peliculaaaa.pelicula.utils.ApiConfig;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModel;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModelFactory;

import java.util.List;
import androidx.lifecycle.Observer;

import kotlin.Result;

public class MainActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el diseño para Edge-to-Edge
        EdgeToEdge.enable(this);

        // Inicializar el API Key desde los recursos
        ApiConfig.initialize(this);

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);

        // Configurar el ViewModel con el ViewModelFactory
        MovieViewModelFactory factory = new MovieViewModelFactory(getApplication());
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);

        // Observar los resultados de la búsqueda de películas
        movieViewModel.getMovieSearchResults().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(List<Movies> movies) {
                if (movies != null && !movies.isEmpty()) {
                    movieAdapter.submitList(movies); // Actualiza el adaptador
                    Log.i("Movie Results", "Número de películas: " + movies.size());
                } else {
                    Log.i("Movie Results", "No se encontraron películas.");
                }
            }
        });

        // Realizamos una búsqueda inicial (puedes modificar la búsqueda aquí)
        String searchQuery = "Batman";  // Ejemplo de búsqueda
        int page = 1;
        movieViewModel.searchMovies(searchQuery, page, new MovieRepository.MovieRepositoryCallback() {
            @Override
            public void onSuccess(List<Movies> movies) {
                // No es necesario hacer nada aquí ya que estamos observando el LiveData
            }

            @Override
            public void onError(String errorMessage) {
                // Mostrar un mensaje de error
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar las ventanas de sistema (esto ya lo tienes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
