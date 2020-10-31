package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.dtos.GetProfileResult;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.CurrentUserType;
import com.mobileedu33.tutorme.data.models.UserType;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.realm.Realm;

public class FetchAssingmentsUseCase extends BaseUseCase<Void, Void> {
    private final FireStoreHelper fireStoreHelper;
    private static final String TAG = FetchAssingmentsUseCase.class.getSimpleName();

    @Inject
    public FetchAssingmentsUseCase(FireStoreHelper fireStoreHelper,
                                   BackgroundThreadPoster backgroundThreadPoster,
                                   UiThreadPoster uiThreadPoster) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
    }

    public void fetch() {
        executeInBackground(this::execute);
    }

    private void execute() {
        Realm realm = Realm.getDefaultInstance();
        CurrentUserType currentUserType = realm.where(CurrentUserType.class).findFirst();
        if(currentUserType.getUserType() == UserType.TUTOR) getTutorAssignments();
        else getStudentAssignments();
        realm.close();
    }

    private void getStudentAssignments() {

    }

    private void getTutorAssignments() {
        FirebaseUser currentUser = FirebaseAuth.getInstance()
                .getCurrentUser();
        if (currentUser == null) throw new IllegalStateException("User should not be null.");

        try {
            List<Assignment> assignments = fireStoreHelper.getAssignments(Collections.singletonList(currentUser.getUid()));
            handleResult(assignments);
        } catch (Exception e) {
            Log.e(TAG, "Error getting assignments", e);
            notifyError(null);
        }
    }

    private void handleResult(List<Assignment> assignments) {
        if (!assignments.isEmpty()) {
            Realm defaultInstance = Realm.getDefaultInstance();
            defaultInstance.beginTransaction();
            defaultInstance.copyToRealmOrUpdate(assignments);
            defaultInstance.commitTransaction();
            notifySuccess(null);
        }
    }
}
