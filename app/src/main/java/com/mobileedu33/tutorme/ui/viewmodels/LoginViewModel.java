package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.google.firebase.auth.FirebaseAuth;
import com.mobileedu33.tutorme.data.usecases.BaseUseCase.UseCaseListener;
import com.mobileedu33.tutorme.data.SharedPreferencesRepository;
import com.mobileedu33.tutorme.data.usecases.CreateUserProfileUseCase;
import com.mobileedu33.tutorme.data.models.UserType;
import com.mobileedu33.tutorme.data.usecases.FetchUserProfileUseCase;

public class LoginViewModel extends BaseViewModel {
    private final SharedPreferencesRepository sharedPreferencesRepository;
    private final CreateUserProfileUseCase createUserProfileUseCase;
    private final FetchUserProfileUseCase fetchUserProfileUseCase;
    private final MutableLiveData<Boolean> createProfileLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> fetchProfileLiveData = new MutableLiveData<>();

    private UserType userType = UserType.TUTOR;

    @ViewModelInject
    public LoginViewModel(@Assisted SavedStateHandle stateHandle,
                          SharedPreferencesRepository sharedPreferencesRepository, CreateUserProfileUseCase createUserProfileUseCase, FetchUserProfileUseCase fetchUserProfileUseCase) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.createUserProfileUseCase = createUserProfileUseCase;
        this.fetchUserProfileUseCase = fetchUserProfileUseCase;
    }

    public void setUserType(UserType type) {
        userType = type;
    }

    public void setIsFirstRunFalse() {
        sharedPreferencesRepository.setIsFirstRun(false);
    }

    public LiveData<Boolean> createUserProfile() {
        createUserProfileUseCase.createNewProfile(userType);
        return createProfileLiveData;
    }

    public LiveData<Boolean> fetchUserProfile() {
        fetchUserProfileUseCase.fetch();
        return fetchProfileLiveData;
    }

    private UseCaseListener<Void, Void> createProfileListener = new UseCaseListener<Void, Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            createProfileLiveData.postValue(true);
        }

        @Override
        public void onError(Void aVoid) {
            FirebaseAuth.getInstance().signOut();
            messagesLiveData.postValue("An error occurred!");
            createProfileLiveData.postValue(false);
        }
    };

    private UseCaseListener<Void, Void> fetchProfileListener = new UseCaseListener<Void, Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            fetchProfileLiveData.postValue(true);
        }

        @Override
        public void onError(Void aVoid) {
            messagesLiveData.postValue("An error occurred. Try again later.");
            fetchProfileLiveData.postValue(false);
        }
    };

    @Override
    public void executeOnResume() {
        createUserProfileUseCase.addListener(createProfileListener);
        fetchUserProfileUseCase.addListener(fetchProfileListener);
    }

    @Override
    public void executeOnPause() {
        createUserProfileUseCase.removeListener(createProfileListener);
        fetchUserProfileUseCase.removeListener(fetchProfileListener);
    }

    @Override
    public void removeLiveDataObservers(LifecycleOwner lifecycleOwner) {
        messagesLiveData.removeObservers(lifecycleOwner);
        createProfileLiveData.removeObservers(lifecycleOwner);
        fetchProfileLiveData.removeObservers(lifecycleOwner);
    }
}
