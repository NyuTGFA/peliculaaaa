package com.example.peliculaaaa.pelicula.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.peliculaaaa.R;
import com.example.peliculaaaa.pelicula.adapters.MovieAdapter;
import com.example.peliculaaaa.pelicula.models.Movies;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModel;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModelFactory;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    private String movie;
    private MovieViewModel movieViewModel;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    public static MovieDetailFragment newInstance(String idmovie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE, idmovie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getString(ARG_MOVIE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Configurar ViewModel
        MovieViewModelFactory factory = new MovieViewModelFactory(requireActivity().getApplication());
        movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);
        seacrhMovie(movie);

    }

    public void seacrhMovie(String id){
        movieViewModel.searchMovieDetail(id);
        movieViewModel. getmovieDetail().observe(requireActivity(), moviesdetail -> {
            if (moviesdetail != null) {
                // Agregar películas a la lista
                Log.d("hoola", String.valueOf(moviesdetail));
            }
        });
    }

}
