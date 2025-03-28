package com.example.peliculaaaa.pelicula.utils;

import android.content.Context;

import com.example.peliculaaaa.R;

public class ApiConfig {
    private static String apiKey;

    // Método para inicializar la API key (si no está ya inicializada)
    public static void initialize(Context context) {
        if (apiKey == null) {
            apiKey = context.getString(R.string.api_key);  // Cargar la API key desde strings.xml
        }
    }

    // Método para obtener la API key
    public static String getApiKey() {
        return apiKey;
    }
}
