package com.example.peliculaaaa.pelicula.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.peliculaaaa.R;
import com.example.peliculaaaa.pelicula.models.Movies;

public class MovieAdapter extends ListAdapter<Movies, MovieAdapter.MovieViewHolder> {

    private final OnMovieClickListener onMovieClickListener;

    public MovieAdapter(OnMovieClickListener onMovieClickListener) {
        super(new MovieDiffCallback());
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movies movie = getItem(position);

        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(movie.getYear());

        // Verificar que el URL no sea nulo o vacío antes de cargar la imagen
        String posterUrl = movie.getPosterUrl();
        if (posterUrl != null && !posterUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(posterUrl)
                    .into(holder.posterImageView);
        }

        // Manejar el clic en el ítem
        holder.itemView.setOnClickListener(v -> {
            if (onMovieClickListener != null) {
                onMovieClickListener.onMovieClick(movie);
            }
        });
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView yearTextView;
        ImageView posterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            yearTextView = itemView.findViewById(R.id.movieYear);
            posterImageView = itemView.findViewById(R.id.moviePoster);
        }
    }

    // Clase para diferenciar los elementos del RecyclerView
    static class MovieDiffCallback extends DiffUtil.ItemCallback<Movies> {
        @Override
        public boolean areItemsTheSame(@NonNull Movies oldItem, @NonNull Movies newItem) {
            return oldItem.getId().equals(newItem.getId()); // Asegúrate de usar un campo único
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movies oldItem, @NonNull Movies newItem) {
            return oldItem.equals(newItem); // Compara los contenidos de los elementos
        }
    }

    // Interfaz para manejar los clics
    public interface OnMovieClickListener {
        void onMovieClick(Movies movie);
    }
}
