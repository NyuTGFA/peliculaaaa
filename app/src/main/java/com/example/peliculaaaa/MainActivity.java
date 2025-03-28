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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                movieAdapter.submitList(movies);

                // Llenar el slider con los pósters de las películas
                slideItems.clear(); // Limpiar lista para evitar duplicados
                for (Movies movie : movies) {
                    slideItems.add(new SlideItems(movie.getPosterUrl()));
                }
                slideAdapter.notifyDataSetChanged(); // Notificar cambios

                Log.i("Movie Results", "Número de películas: " + movies.size());
            } else {
                Log.i("Movie Results", "No se encontraron películas.");
            }
        });

        // Realizar búsqueda inicial
        String searchQuery = "Batman";
        int page = 1;
        movieViewModel.searchMovies(searchQuery, page, new MovieRepository.MovieRepositoryCallback() {
            @Override
            public void onSuccess(List<Movies> movies) {
                // Ya se está observando el LiveData, no es necesario actualizar aquí
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Ajustar ventanas del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

