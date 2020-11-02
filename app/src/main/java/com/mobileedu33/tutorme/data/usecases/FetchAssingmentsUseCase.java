package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.dtos.GetProfileResult;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.CurrentUserType;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import com.mobileedu33.tutorme.data.models.TutorRequest;
import com.mobileedu33.tutorme.data.models.UserType;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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
        assert currentUserType != null;

        if(currentUserType.getUserType() == UserType.TUTOR) getTutorAssignments(realm);
        else getStudentAssignments(realm);

        realm.close();
    }

    private void getStudentAssignments(Realm realm) {
        RealmResults<TutorProfile> tutorProfiles = realm.where(TutorProfile.class).findAll();
        if (tutorProfiles.isEmpty()) {
            notifySuccess(null);
            return;
        }
        getAssignments(tutorProfiles);
    }

    private void getTutorAssignments(Realm realm) {
        TutorProfile tutorProfile = realm.where(TutorProfile.class).findFirst();
        if(tutorProfile == null) throw new IllegalStateException("There should be a tutor profile");
        getAssignments(Collections.singletonList(tutorProfile));
    }

    private void getAssignments(List<TutorProfile> tutorProfiles) {
        List<String> tutorIds = new ArrayList<>();
        for (TutorProfile tutorProfile : tutorProfiles ) {
            tutorIds.add(tutorProfile.getId());
        }

        try {
            List<Assignment> assignments = fireStoreHelper.getAssignments(tutorIds);
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
