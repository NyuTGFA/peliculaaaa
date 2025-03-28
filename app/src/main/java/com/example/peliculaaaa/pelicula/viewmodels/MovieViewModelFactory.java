package com.example.peliculaaaa.pelicula.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MovieViewModelFactory implements ViewModelProvider.Factory {


    private Application application;

    public MovieViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            // Se pasa el Application al ViewModel
            return (T) new MovieViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}