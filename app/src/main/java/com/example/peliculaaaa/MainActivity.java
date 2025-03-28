package com.example.peliculaaaa;



import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peliculaaaa.pelicula.adapters.MovieAdapter;
import com.example.peliculaaaa.pelicula.adapters.SlideAdapters;
import com.example.peliculaaaa.pelicula.models.Movies;
import com.example.peliculaaaa.pelicula.models.SlideItems;
import com.example.peliculaaaa.pelicula.repositories.MovieRepository;
import com.example.peliculaaaa.pelicula.utils.ApiConfig;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModel;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModelFactory;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import kotlin.Result;

public class MainActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ViewPager2 viewPager2;
    private SlideAdapters slideAdapter;
    private List<SlideItems> slideItems = new ArrayList<>();
    private Handler slidehandler = new Handler();
    private int page = 1; // Página inicial para la paginación
    private boolean isLoading = false; // Para evitar cargar datos mientras ya estamos cargando
    private List<Movies> allMovies = new ArrayList<>(); // Lista que acumula todas las películas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar Edge-to-Edge
        EdgeToEdge.enable(this);

        // Inicializar la API Key
        ApiConfig.initialize(this);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columnas para el RecyclerView
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);

        // Configurar ViewPager2
        initView();
        setupBannerSlider();

        // Configurar ViewModel
        MovieViewModelFactory factory = new MovieViewModelFactory(getApplication());
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);

        // Observar los resultados de la API
        movieViewModel.getMovieSearchResults().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                // Agregar las nuevas películas a la lista, evitando duplicados
                addMoviesToList(movies);

                // Actualizar el adaptador con la lista completa
                movieAdapter.submitList(new ArrayList<>(allMovies)); // Notificar cambios al adaptador

                // Llenar el slider con los pósters de las películas
                slideItems.clear(); // Limpiar lista para evitar duplicados
                for (Movies movie : movies) {
                    slideItems.add(new SlideItems(movie.getPosterUrl()));
                }
                slideAdapter.notifyDataSetChanged(); // Notificar cambios
                page++;
                Log.i("page", String.valueOf(page));

                Log.i("Movie Results", "Número de películas: " + allMovies.size());
            } else {
                Log.i("Movie Results", "No se encontraron películas.");
            }
        });

        // Realizar búsqueda inicial
        String searchQuery = "Batman";
        loadMoreData(searchQuery); // Cargar los datos iniciales

        // Configurar scroll listener para cargar más datos cuando el usuario llega al final
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Verificar si hemos llegado al final del RecyclerView
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    // Si estamos cerca del final y no estamos cargando más datos
                    if (!isLoading && lastVisibleItemPosition == totalItemCount - 1) {
                        isLoading = true; // Marcar que estamos cargando

                        // Cargar más datos
                        loadMoreData(searchQuery);
                    }
                }
            }
        });

        // Ajustar ventanas del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para cargar más datos desde la API
    private void loadMoreData(String searchQuery) {
        movieViewModel.searchMovies(searchQuery, page, new MovieRepository.MovieRepositoryCallback() {
            @Override
            public void onSuccess(List<Movies> movies) {
                if (movies != null && !movies.isEmpty()) {
                    // Agregar las nuevas películas a la lista, evitando duplicados8

                    // Actualizar la lista completa
                    movieAdapter.submitList(new ArrayList<>(allMovies)); // Notificar cambios al adaptador



                    isLoading = false; // Marcar que la carga ha terminado
                }
            }

            @Override
            public void onError(String errorMessage) {
                isLoading = false;
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para agregar películas a la lista, evitando duplicados
    private void addMoviesToList(List<Movies> movies) {
        for (Movies movie : movies) {
            // Verificar si la película ya está en la lista
            boolean alreadyExists = false;
            for (Movies existingMovie : allMovies) {
                if (existingMovie.getId().equals(movie.getId())) {
                    alreadyExists = true;
                    break;
                }
            }

            // Si la película no está en la lista, agregarla
            if (!alreadyExists) {
                allMovies.add(movie);
            }
        }
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewpageslider);
        slideAdapter = new SlideAdapters(slideItems, viewPager2);
        viewPager2.setAdapter(slideAdapter);
    }

    private void setupBannerSlider() {
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float scale = 0.85f + 0.15f * (1 - Math.abs(position));
            page.setScaleY(scale);
        });

        viewPager2.setPageTransformer(transformer);
        viewPager2.setCurrentItem(1);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slidehandler.removeCallbacks(sliderRunnable);
                slidehandler.postDelayed(sliderRunnable, 2000);
            }
        });
    }

    private final Runnable sliderRunnable = () -> viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);

    @Override
    protected void onPause() {
        super.onPause();
        slidehandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slidehandler.postDelayed(sliderRunnable, 2000);
    }
}


