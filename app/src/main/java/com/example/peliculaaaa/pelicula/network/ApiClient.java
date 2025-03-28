package com.example.peliculaaaa.pelicula.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://www.omdbapi.com";  // URL de la API OMDb
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())  // Convierte el JSON en objetos Java
                    .build();
        }
        return retrofit;
    }
}
