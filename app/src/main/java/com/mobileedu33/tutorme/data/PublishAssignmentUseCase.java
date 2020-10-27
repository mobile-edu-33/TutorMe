package com.mobileedu33.tutorme.data;

import com.mobileedu33.tutorme.data.models.Assignment;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import javax.inject.Inject;

public class PublishAssignmentUseCase extends BaseUseCase<Assignment, String> {
    private final FireStoreHelper fireStoreHelper;
    private final FirebaseStorageHelper firebaseStorageHelper;

    @Inject
    public PublishAssignmentUseCase(BackgroundThreadPoster backgroundThreadPoster, UiThreadPoster uiThreadPoster, FireStoreHelper fireStoreHelper, FirebaseStorageHelper firebaseStorageHelper) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
        this.firebaseStorageHelper = firebaseStorageHelper;
    }

    public void publish(Assignment assignment) {
        executeInBackground(() -> execute(assignment));
    }

    private void execute(Assignment assignment) {

    }
}
