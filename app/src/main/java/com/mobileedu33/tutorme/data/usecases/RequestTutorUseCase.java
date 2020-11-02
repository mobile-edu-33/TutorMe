package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.models.TutorRequest;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import javax.inject.Inject;

import io.realm.Realm;

public class RequestTutorUseCase extends BaseUseCase<Void, Void> {
    private final FireStoreHelper fireStoreHelper;
    private static final String TAG = RequestTutorUseCase.class.getSimpleName();

    @Inject
    public RequestTutorUseCase(FireStoreHelper fireStoreHelper,
                               BackgroundThreadPoster backgroundThreadPoster,
                               UiThreadPoster uiThreadPoster) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
    }

    public void request(TutorRequest tutorRequest) {
        executeInBackground(() -> execute(tutorRequest));
    }

    private void execute(TutorRequest tutorRequest) {
        try {
            boolean result = fireStoreHelper.saveTutorRequest(tutorRequest);
            handleResult(result, tutorRequest);
        } catch (Exception e) {
            Log.e(TAG, "Error saving tutor request", e);
            notifyError(null);
        }
    }

    private void handleResult(boolean isSuccess, TutorRequest tutorRequest) {
        if (isSuccess) {
            Realm defaultInstance = Realm.getDefaultInstance();
            defaultInstance.beginTransaction();
            defaultInstance.copyToRealmOrUpdate(tutorRequest);
            defaultInstance.commitTransaction();
            notifySuccess(null);
        } else notifyError(null);
    }
}
