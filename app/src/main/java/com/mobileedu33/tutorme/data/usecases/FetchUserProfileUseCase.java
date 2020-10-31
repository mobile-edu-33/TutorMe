package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.dtos.GetProfileResult;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import javax.inject.Inject;

import io.realm.Realm;

public class FetchUserProfileUseCase extends BaseUseCase<Void, Void> {
    private final FireStoreHelper fireStoreHelper;
    private static final String TAG = FetchUserProfileUseCase.class.getSimpleName();

    @Inject
    public FetchUserProfileUseCase(FireStoreHelper fireStoreHelper,
                                   BackgroundThreadPoster backgroundThreadPoster,
                                   UiThreadPoster uiThreadPoster) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
    }

    public void fetch() {
        executeInBackground(this::execute);
    }

    private void execute() {
        FirebaseUser currentUser = FirebaseAuth.getInstance()
                .getCurrentUser();
        if (currentUser == null) throw new IllegalStateException("User should not be null.");

        try {
            GetProfileResult getProfileResult = fireStoreHelper.getUserProfile(currentUser.getUid());
            handleResult(getProfileResult);
        }catch (Exception e) {
            Log.e(TAG, "Error getting user profile", e);
            notifyError(null);
        }

    }

    private void handleResult(GetProfileResult getProfileResult) {
        if (getProfileResult.getStudentProfile() == null
                && getProfileResult.getTutorProfile() == null) {
            notifyError(null);
        } else if (getProfileResult.getStudentProfile() != null
                && getProfileResult.getTutorProfile() != null) {
            throw new IllegalStateException("User cannot have both Tutor and Student profiles");
        } else if (getProfileResult.getTutorProfile() != null) {
            Realm defaultInstance = Realm.getDefaultInstance();
            defaultInstance.beginTransaction();
            defaultInstance.copyToRealmOrUpdate(getProfileResult.getTutorProfile());
            defaultInstance.commitTransaction();
            defaultInstance.close();
            notifySuccess(null);
        } else if (getProfileResult.getStudentProfile() != null) {
            Realm defaultInstance = Realm.getDefaultInstance();
            defaultInstance.beginTransaction();
            defaultInstance.copyToRealmOrUpdate(getProfileResult.getStudentProfile());
            defaultInstance.commitTransaction();
            defaultInstance.close();
            notifySuccess(null);
        }
    }
}
