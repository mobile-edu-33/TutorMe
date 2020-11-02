package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.models.StudentProfile;
import com.mobileedu33.tutorme.data.models.TutorRequest;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.realm.Realm;

public class AcceptTutorRequestUseCase extends BaseUseCase<Void, Void> {
    private final FireStoreHelper fireStoreHelper;
    private static final String TAG = AcceptTutorRequestUseCase.class.getSimpleName();

    @Inject
    public AcceptTutorRequestUseCase(FireStoreHelper fireStoreHelper,
                                     BackgroundThreadPoster backgroundThreadPoster,
                                     UiThreadPoster uiThreadPoster) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
    }

    public void acceptRequest(TutorRequest tutorRequest) {
        TutorRequest copy = new TutorRequest();
        copy.setAccepted(false);
        copy.setId(tutorRequest.getId());
        copy.setTutorId(tutorRequest.getTutorId());
        copy.setStudentId(tutorRequest.getStudentId());
        executeInBackground(() -> execute(copy));
    }

    private void execute(TutorRequest tutorRequest) {
        tutorRequest.setAccepted(true);
        try {
            StudentProfile studentProfile = fireStoreHelper.getStudentProfile(tutorRequest.getStudentId());
            handleGetProfileResult(studentProfile, tutorRequest);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching data", e);
            notifyError(null);
        }
    }

    private void handleGetProfileResult(StudentProfile studentProfile, TutorRequest tutorRequest) throws ExecutionException, InterruptedException {
        boolean isSuccess = fireStoreHelper.saveTutorRequest(tutorRequest);
        if (isSuccess) {
            if (studentProfile != null) {
                try (Realm defaultInstance = Realm.getDefaultInstance()) {
                    defaultInstance.executeTransaction(realm -> {
                        realm.copyToRealmOrUpdate(studentProfile);
                        TutorRequest itemToDelete = realm.where(TutorRequest.class)
                                .equalTo("id", tutorRequest.getId())
                                .findFirst();
                        if(itemToDelete != null) itemToDelete.deleteFromRealm();
                    });
                }
                notifySuccess(null);
            } else notifyError(null);
        }
    }
}
