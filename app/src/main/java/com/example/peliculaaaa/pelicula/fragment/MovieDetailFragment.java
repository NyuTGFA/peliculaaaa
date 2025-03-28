package com.example.peliculaaaa.pelicula.fragment;

import android.media.Rating;
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
import com.example.peliculaaaa.pelicula.response.Ranting;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModel;
import com.example.peliculaaaa.pelicula.viewmodels.MovieViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    private String movie;
    private MovieViewModel movieViewModel;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private TextView title;
    private TextView calificacion1 ;
    private TextView calificacion2;
    private TextView calificacion3;
    private TextView descripcionText;
    private ImageView imageView;
    private ImageView BACK;


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
        imageView = view.findViewById(R.id.imageView3);
        title = view.findViewById(R.id.title);
        calificacion1 = view.findViewById(R.id.calificacion1);
        calificacion2 = view.findViewById(R.id.calificacion2);
        calificacion3 = view.findViewById(R.id.calificacion3);
        descripcionText = view.findViewById(R.id.descripciontext);
        BACK = view.findViewById(R.id.back);

        BACK.setOnClickListener(v -> {
            // Regresar a la pantalla anterior
            requireActivity().onBackPressed();
        });


        seacrhMovie(movie);

    }

    public void seacrhMovie(String id){
        movieViewModel.searchMovieDetail(id);
        movieViewModel.getmovieDetail().observe(getViewLifecycleOwner(), moviesdetail -> {
            if (moviesdetail != null) {
                // Agregar películas a la lista
                Log.d("hoola", String.valueOf(moviesdetail));
                title.setText(moviesdetail.getTitle());
                List<Ranting> ratings = moviesdetail.getRatings();
                if (ratings != null && ratings.size() >= 3) {
                    // Concatenamos el source y value para cada calificación
                    String cal1 = ratings.get(0).getSource() + ": " + ratings.get(0).getValue();
                    String cal2 = ratings.get(1).getSource() + ": " + ratings.get(1).getValue();
                    String cal3 = ratings.get(2).getSource() + ": " + ratings.get(2).getValue();

                    calificacion1.setText(cal1); // Asignamos la calificación 1
                    calificacion2.setText(cal2); // Asignamos la calificación 2
                    calificacion3.setText(cal3); // Asignamos la calificación 3
                }// Si tienes esta calificación

                // Actualizamos la descripción
                descripcionText.setText(moviesdetail.getPlot());

                // Cargar la imagen usando Glide
                Glide.with(this)
                        .load(moviesdetail.getPoster())  // Asegúrate de tener una URL válida aquí
                        .into(imageView);
            }
        });
    }

}
