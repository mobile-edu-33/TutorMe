package com.mobileedu33.tutorme.data.usecases;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.models.CurrentUserType;
import com.mobileedu33.tutorme.data.models.StudentProfile;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import javax.inject.Inject;

import io.realm.Realm;

public class UpdateUserProfileUseCase extends BaseUseCase<Void, Void> {
    private final FireStoreHelper fireStoreHelper;
    private static final String TAG = UpdateUserProfileUseCase.class.getSimpleName();

    @Inject
    public UpdateUserProfileUseCase(FireStoreHelper fireStoreHelper,
                                    BackgroundThreadPoster backgroundThreadPoster,
                                    UiThreadPoster uiThreadPoster) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
    }

    public void updateStudentProfile(StudentProfile userProfile) {
        executeInBackground(() -> execute(userProfile));
    }

    public void updateTutorProfile(TutorProfile tutorProfile) {
        executeInBackground(() -> execute(tutorProfile));
    }

    private void execute(StudentProfile profile) {
        try {
            boolean isSuccess = fireStoreHelper.saveStudentProfile(profile);
            handleStudentProfileUpdateResult(isSuccess, profile);
        } catch (Exception e) {
            Log.e(TAG, "Error updating data", e);
            notifyError(null);
        }
    }

    private void handleStudentProfileUpdateResult(boolean isSuccess, StudentProfile profile) {
        if (isSuccess) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(profile));
            realm.close();
            notifySuccess(null);
        } else notifyError(null);
    }

    private void execute(TutorProfile profile) {
        try {
            boolean isSuccess = fireStoreHelper.saveTutorProfile(profile);
            handleTutorProfileUpdateResult(isSuccess, profile);
        } catch (Exception e) {
            Log.e(TAG, "Error updating data", e);
            notifyError(null);
        }
    }

    private void handleTutorProfileUpdateResult(boolean isSuccess, TutorProfile profile) {
        if (isSuccess) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(profile));
            realm.close();
            notifySuccess(null);
        } else notifyError(null);
    }

}
