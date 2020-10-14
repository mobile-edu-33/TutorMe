package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.mobileedu33.tutorme.data.SharedPreferencesRepository;

public class LoginViewModel extends ViewModel {
    private final SharedPreferencesRepository sharedPreferencesRepository;
    private boolean isLoggedIn;

    @ViewModelInject
    public LoginViewModel(@Assisted SavedStateHandle stateHandle,
            SharedPreferencesRepository sharedPreferencesRepository) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;
    }

    public LiveData<Boolean> isFirstRun(){
        MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();
        // Loading sharedpreference in background thread
        new Thread(() ->{
            boolean firstRun = sharedPreferencesRepository.isFirstRun();
            isLoggedIn = sharedPreferencesRepository.isLoggedIn();
            mutableLiveData.postValue(firstRun);
        }).start();
        return mutableLiveData;
    }

    public void setIsFirstRunFalse() {
        sharedPreferencesRepository.setIsFirstRun(false);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedInTrue() {
        sharedPreferencesRepository.setIsLoggedIn(true);
    }
}
