package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.FirebaseStorageHelper;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.LiveLesson;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import javax.inject.Inject;

import io.realm.Realm;

public class DeleteScheduledLessonUseCase extends BaseUseCase<Void, Void> {
    public static final String TAG = DeleteScheduledLessonUseCase.class.getSimpleName();
    private final FireStoreHelper fireStoreHelper;
    private final FirebaseStorageHelper firebaseStorageHelper;

    @Inject
    public DeleteScheduledLessonUseCase(BackgroundThreadPoster backgroundThreadPoster, UiThreadPoster uiThreadPoster, FireStoreHelper fireStoreHelper, FirebaseStorageHelper firebaseStorageHelper) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
        this.firebaseStorageHelper = firebaseStorageHelper;
    }

    public void delete(String lessonId) {
       executeInBackground(() -> execute(lessonId));
    }

    private void execute(String assignmentId) {
        try {
            boolean result = firebaseStorageHelper.deleteFile(FirebaseStorageHelper.LESSONS_IMAGES, assignmentId);
            handleDeleteAttachmentResult(result, assignmentId);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting attachment", e);
            notifyError(null);
        }
    }

    private void handleDeleteAttachmentResult(boolean isSuccess, String id) {
        if (isSuccess) {
            try {
                boolean result = fireStoreHelper.deleteAssignment(id);
                handleDeleteDocumentResult(result, id);
            }catch (Exception e) {
                Log.e(TAG, "Error deleting assignment from firebase database", e);
                notifyError(null);
            }
        } else {
            notifyError(null);
        }
    }

    private void handleDeleteDocumentResult(boolean isSuccess, String id) {
        if (isSuccess) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                LiveLesson itemToDelete = realm1.where(LiveLesson.class).
                        equalTo("id", id)
                        .findFirst();
                if (itemToDelete != null) itemToDelete.deleteFromRealm();
            });
            realm.close();
            notifySuccess(null);
        } else {
            notifyError(null);
        }
    }

}
