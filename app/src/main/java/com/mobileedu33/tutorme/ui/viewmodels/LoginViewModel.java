package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.mobileedu33.tutorme.data.SharedPreferencesRepository;

public class LoginViewModel extends ViewModel {
    private final SharedPreferencesRepository sharedPreferencesRepository;


    public LoginViewModel(SharedPreferencesRepository sharedPreferencesRepository) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;
    }
}
