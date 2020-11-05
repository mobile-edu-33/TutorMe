package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.FirebaseStorageHelper;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.io.File;

import javax.inject.Inject;

import io.realm.Realm;

public class DownloadAttachmentsUseCase extends BaseUseCase<Void, Void> {
    public static final String TAG = DownloadAttachmentsUseCase.class.getSimpleName();
    private final FireStoreHelper fireStoreHelper;
    private final FirebaseStorageHelper firebaseStorageHelper;

    @Inject
    public DownloadAttachmentsUseCase(BackgroundThreadPoster backgroundThreadPoster, UiThreadPoster uiThreadPoster, FireStoreHelper fireStoreHelper, FirebaseStorageHelper firebaseStorageHelper) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
        this.firebaseStorageHelper = firebaseStorageHelper;
    }

    public void download(Assignment assignment, File destinationFolder) {
        String assignmentId = assignment.getId();
        executeInBackground(() -> execute(assignmentId, destinationFolder));
    }

    private void execute(String assignmentId, File destinationFolder) {
        try {
            boolean result = firebaseStorageHelper.getAssignmentFiles(assignmentId, destinationFolder);
            handleDownloadAttachmentResult(result);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting attachment", e);
            notifyError(null);
        }
    }

    private void handleDownloadAttachmentResult(boolean isSuccess) {
        if (isSuccess) {
            notifySuccess(null);
        } else {
            notifyError(null);
        }
    }

}
