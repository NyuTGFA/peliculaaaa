package com.example.peliculaaaa.pelicula.Data.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.peliculaaaa.pelicula.Data.Dao.MovieDao;
import com.example.peliculaaaa.pelicula.models.Movies;

@Database(entities = {Movies.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    // Definir el DAO
    public abstract MovieDao movieDao();

    // Instancia de la base de datos (singleton)
    private static MovieDatabase INSTANCE;

    public static MovieDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MovieDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration() // Permite borrar la base si hay cambios en el esquema
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
