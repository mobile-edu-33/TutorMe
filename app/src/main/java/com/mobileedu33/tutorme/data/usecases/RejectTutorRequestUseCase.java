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

public class RejectTutorRequestUseCase extends BaseUseCase<Void, Void> {
    private final FireStoreHelper fireStoreHelper;
    private static final String TAG = RejectTutorRequestUseCase.class.getSimpleName();

    @Inject
    public RejectTutorRequestUseCase(FireStoreHelper fireStoreHelper,
                                     BackgroundThreadPoster backgroundThreadPoster,
                                     UiThreadPoster uiThreadPoster) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
    }

    public void rejectRequest(TutorRequest tutorRequest) {
        TutorRequest copy = new TutorRequest();
        copy.setAccepted(false);
        copy.setId(tutorRequest.getId());
        copy.setTutorId(tutorRequest.getTutorId());
        copy.setStudentId(tutorRequest.getStudentId());
        executeInBackground(() -> execute(copy));
    }

    private void execute(TutorRequest tutorRequest) {
        tutorRequest.setAccepted(false);
        try {
            boolean isSuccess = fireStoreHelper.saveTutorRequest(tutorRequest);
            handleResult(isSuccess, tutorRequest);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching data", e);
            notifyError(null);
        }
    }

    private void handleResult(boolean isSuccess, TutorRequest tutorRequest) {
        if (isSuccess) {
            try (Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(realm1 -> {
                    TutorRequest itemToDelete = realm1.where(TutorRequest.class)
                            .equalTo("id", tutorRequest.getId())
                            .findFirst();
                    if(itemToDelete != null) itemToDelete.deleteFromRealm();
                });
            }
            notifySuccess(null);
        } else notifyError(null);
    }
}
