package com.mobileedu33.tutorme.data;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobileedu33.tutorme.data.models.StudentProfile;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import com.mobileedu33.tutorme.data.models.UserType;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import javax.inject.Inject;

import io.realm.Realm;

public class CreateUserProfileUseCase extends BaseUseCase<Void, Void> {
    private final FireStoreHelper fireStoreHelper;
    private static final String TAG = CreateUserProfileUseCase.class.getSimpleName();

    @Inject
    public CreateUserProfileUseCase(FireStoreHelper fireStoreHelper,
                                    BackgroundThreadPoster backgroundThreadPoster,
                                    UiThreadPoster uiThreadPoster) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
    }

    public void createNewProfile(UserType userType) {
        executeInBackground(() -> execute(userType));
    }

    private void execute(UserType userType) {
        FirebaseUser currentUser = FirebaseAuth.getInstance()
                .getCurrentUser();
        switch (userType) {
            case TUTOR:
                createNewTutorProfile(currentUser);
                break;
            case STUDENT:
                createNewStudentProfile(currentUser);
                break;
        }
    }

    private void createNewStudentProfile(FirebaseUser user) {
        if(user == null) throw new IllegalStateException("User should not be null.");

        StudentProfile profile = new StudentProfile();
        profile.setEmail(user.getEmail());
        profile.setDisplayName(user.getDisplayName());
        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null) profile.setPhotoUrl(user.getPhotoUrl().toString());
        profile.setId(user.getUid());

        try {
            boolean isSuccess = fireStoreHelper.saveStudentProfile(profile, user.getUid());
            if (isSuccess) handleCreateStudentSuccess(profile);
            else notifyError(null);
        } catch (Exception e) {
            Log.e(TAG, "Error creating saving data", e);
            notifyError(null);
        }
    }

    private void createNewTutorProfile(FirebaseUser user) {
        if(user == null) throw new IllegalStateException("User should not be null.");
        TutorProfile profile = new TutorProfile();
        profile.setEmail(user.getEmail());
        profile.setDisplayName(user.getDisplayName());
        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null) profile.setPhotoUrl(user.getPhotoUrl().toString());
        profile.setId(user.getUid());

        try {
            boolean isSuccess = fireStoreHelper.saveTutorProfile(profile, user.getUid());
            if (isSuccess) handleCreateTutorSuccess(profile);
            else notifyError(null);
        } catch (Exception e) {
            Log.e(TAG, "Error creating saving data", e);
            notifyError(null);
        }
    }

    private void handleCreateTutorSuccess(TutorProfile tutorProfile) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(tutorProfile);
        realm.commitTransaction();
        realm.close();
        notifySuccess(null);
    }

    private void handleCreateStudentSuccess(StudentProfile studentProfile) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(studentProfile);
        realm.commitTransaction();
        realm.close();
        notifySuccess(null);
    }

}
