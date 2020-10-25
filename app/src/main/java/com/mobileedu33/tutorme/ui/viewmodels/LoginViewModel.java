package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.mobileedu33.tutorme.data.BaseUseCase.UseCaseListener;
import com.mobileedu33.tutorme.data.SharedPreferencesRepository;
import com.mobileedu33.tutorme.data.CreateUserProfileUseCase;
import com.mobileedu33.tutorme.data.models.UserType;

public class LoginViewModel extends BaseViewModel {
    private final SharedPreferencesRepository sharedPreferencesRepository;
    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final MutableLiveData<Boolean> createProfileLiveData = new MutableLiveData<>();
    private UserType userType = UserType.STUDENT;
    private boolean isLoggedIn;

    @ViewModelInject
    public LoginViewModel(@Assisted SavedStateHandle stateHandle,
                          SharedPreferencesRepository sharedPreferencesRepository, CreateUserProfileUseCase createUserProfileUseCase) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.createUserProfileUseCase = createUserProfileUseCase;
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

    public LiveData<Boolean> createUserProfile() {
        createUserProfileUseCase.createNewProfile(userType);
        return createProfileLiveData;
    }

    private UseCaseListener<Void, Void> createProfileListener = new UseCaseListener<Void, Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            createProfileLiveData.postValue(true);
        }

        @Override
        public void onError(Void aVoid) {
            messagesLiveData.postValue("An error occurred!");
            createProfileLiveData.postValue(false);
        }
    };

    @Override
    public void executeOnResume() {
        createUserProfileUseCase.addListener(createProfileListener);
    }

    @Override
    public void executeOnPause() {
        createUserProfileUseCase.removeListener(createProfileListener);
    }

    @Override
    public void removeLiveDataObservers(LifecycleOwner lifecycleOwner) {
        messagesLiveData.removeObservers(lifecycleOwner);
        createProfileLiveData.removeObservers(lifecycleOwner);
    }
}
