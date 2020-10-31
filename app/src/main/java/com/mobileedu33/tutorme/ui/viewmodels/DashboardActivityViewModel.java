package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LifecycleOwner;

import com.mobileedu33.tutorme.data.models.StudentProfile;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import com.mobileedu33.tutorme.data.models.UserProfile;
import com.mobileedu33.tutorme.data.models.UserType;

import io.realm.Realm;

public class DashboardActivityViewModel extends BaseViewModel {

    private UserProfile userProfile;
    private Realm realm;
    private UserType userType;

    @ViewModelInject
    public DashboardActivityViewModel() {
        this.realm = Realm.getDefaultInstance();
    }

    public UserProfile getUserProfile() {
        if (userProfile == null) loadUserProfile();
        return userProfile;
    }

    private void loadUserProfile() {
        TutorProfile tutorProfile = realm.where(TutorProfile.class).findFirst();
        StudentProfile studentProfile = realm.where(StudentProfile.class).findFirst();

        if (tutorProfile != null) {
            userProfile = tutorProfile;
            userType = UserType.TUTOR;
        } else if (studentProfile != null) {
            userProfile = studentProfile;
            userType = UserType.STUDENT;
        }
    }

    @Override
    public void executeOnCreate() {
        super.executeOnCreate();
        loadUserProfile();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        realm.close();
    }

    @Override
    public void removeLiveDataObservers(LifecycleOwner lifecycleOwner) {

    }

    public UserType getUserType() {
        return userType;
    }
}
