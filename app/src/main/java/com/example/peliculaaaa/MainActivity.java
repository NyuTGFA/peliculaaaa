package com.example.peliculaaaa;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.peliculaaaa.pelicula.adapters.MovieAdapter;
import com.example.peliculaaaa.pelicula.adapters.SlideAdapters;
import com.example.peliculaaaa.pelicula.fragment.MovieDetailFragment;
import com.example.peliculaaaa.pelicula.models.Movies;
import com.example.peliculaaaa.pelicula.models.SlideItems;
import com.example.peliculaaaa.pelicula.repositories.MovieRepository;
import com.example.peliculaaaa.pelicula.utils.ApiConfig;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModel;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener{

    private MovieViewModel movieViewModel;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ViewPager2 viewPager2;
    private SlideAdapters slideAdapter;
    private List<SlideItems> slideItems = new ArrayList<>();
    private Handler slidehandler = new Handler();
    private int page = 1;
    private boolean isLoading = false;
    private List<Movies> allMovies = new ArrayList<>();
    private EditText editTextSearch;
    private Boolean clear=false;

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
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);

        // Configurar ViewPager2
        initView();
        setupBannerSlider();

        // Configurar ViewModel
        MovieViewModelFactory factory = new MovieViewModelFactory(getApplication());
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);

        // Realizar la búsqueda predeterminada
        String defaultSearchQuery = "Batman"; // O cualquier término predeterminado
        searchMovies(defaultSearchQuery);

        // Buscar películas cuando el texto cambia en el EditText
        editTextSearch = findViewById(R.id.edit_text_search);
        editTextSearch.setText(defaultSearchQuery); // Inicializar con la búsqueda predeterminada
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchMovies(query);
                }
                return true; // Indicar que el evento fue manejado
            }
            return false;
        });

        editTextSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { // Si el usuario salió del campo
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchMovies(query);
                }
            }
        });


        // Configurar ViewModel para observar resultados
        movieViewModel.getMovieSearchResults().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                addMoviesToList(movies); // Agregar películas a la lista
                movieAdapter.submitList(new ArrayList<>(allMovies)); // Actualizar el RecyclerView
                updateBannerSlider(movies); // Actualizar el slider
            }
        });

        // Ajustar ventanas del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para realizar la búsqueda de películas
    private void searchMovies(String query) {
        page = 1; // Reiniciar la página para nuevas búsquedas
        allMovies.clear();
        // Limpiar la lista de películas anteriores
        movieViewModel.searchMovies(query, page, new MovieRepository.MovieRepositoryCallback() {
            @Override
            public void onSuccess(List<Movies> movies) {
                if (movies != null && !movies.isEmpty()) {
                  // Agregar películas a la lista
                    movieAdapter.submitList(new ArrayList<>(allMovies)); // Notificar al adaptador
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para agregar películas a la lista, evitando duplicados
    private void addMoviesToList(List<Movies> movies) {
        for (Movies movie : movies) {
            if (!allMovies.contains(movie)) {
                allMovies.add(movie); // Agregar solo si no está duplicada
            }
        }
    }

    private void updateBannerSlider(List<Movies> movies) {
        slideItems.clear();
        for (Movies movie : movies) {
            slideItems.add(new SlideItems(movie.getPosterUrl())); // Actualizar los pósters en el slider
        }
        slideAdapter.notifyDataSetChanged(); // Notificar al adaptador
    }

    // Método para configurar el ViewPager2
    private void initView() {
        viewPager2 = findViewById(R.id.viewpageslider);
        slideAdapter = new SlideAdapters(slideItems, viewPager2);
        viewPager2.setAdapter(slideAdapter);
    }

    // Configuración del slider de banners
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

    @Override
    public void onMovieClick(Movies movie) {
        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movie.getId());

        // Iniciar la transacción para mostrar el fragmento de detalles
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, movieDetailFragment)
                .addToBackStack(null)  // Esto permite que el fragmento de detalle sea visible en el historial de back
                .commit();
    }
}

