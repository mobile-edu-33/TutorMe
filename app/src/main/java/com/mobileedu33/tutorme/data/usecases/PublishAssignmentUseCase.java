package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.FirebaseStorageHelper;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.io.File;

import javax.inject.Inject;

import io.realm.Realm;

public class PublishAssignmentUseCase extends BaseUseCase<Void, String> {
    public static final String TAG = PublishAssignmentUseCase.class.getSimpleName();
    private final FireStoreHelper fireStoreHelper;
    private final FirebaseStorageHelper firebaseStorageHelper;
    private Assignment currentAssignment;

    @Inject
    public PublishAssignmentUseCase(BackgroundThreadPoster backgroundThreadPoster, UiThreadPoster uiThreadPoster, FireStoreHelper fireStoreHelper, FirebaseStorageHelper firebaseStorageHelper) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
        this.firebaseStorageHelper = firebaseStorageHelper;
    }

    public void publish(Assignment assignment, File attachment, File imageUrl) {
       executeInBackground(() -> execute(assignment, attachment, imageUrl));
    }

    private void execute(Assignment assignment, File attachment, File imageUrl) {
        this.currentAssignment = assignment;
        Realm realm = Realm.getDefaultInstance();
        TutorProfile tutorProfile = realm.where(TutorProfile.class).findFirst();
        if(tutorProfile == null) throw new IllegalStateException("No tutor profile found! A tutor profile is required to publish an assignment.");
        assignment.setCreatorId(tutorProfile.getId());
        assignment.setId(
                tutorProfile.getId()
                        .concat("-")
                        .concat(String.valueOf(System.currentTimeMillis())));
        realm.close();

        try {
            UploadAssignmentResult result = firebaseStorageHelper.saveAssignmentsAttachment(assignment, attachment, imageUrl);
            handleAttachmentUploadResult(result);
        } catch (Exception e) {
            Log.e(TAG, "Error uploading data to storage", e);
            notifyError("Error saving attachment!");
        }
    }

    private void handleAttachmentUploadResult(UploadAssignmentResult result) {
        if (result == null) {
            notifyError("Error saving attachment");
            return;
        }
        notifySuccess(null);
        executeInBackground(() -> {
            currentAssignment.setAttachmentsUrl(result.attachmentUrl);
            currentAssignment.setImageUrl(result.imageUrl);
            try {
                boolean isSuccess = fireStoreHelper.publishAssignment(currentAssignment, false);
                handlePublishResult(isSuccess);
            } catch (Exception e) {
                Log.e(TAG, "Error saving data", e);
                notifyError("Error saving assignment. Try again.");
            }

        });
    }

    private void handlePublishResult(boolean isSuccessful) {
        if (isSuccessful) {
            saveToLocalStorage(currentAssignment);
            notifySuccess(null);
            return;
        }
        notifyError("Error saving assignment. Try again.");
    }

    private void saveToLocalStorage(Assignment assignment) {
        Realm defaultInstance = Realm.getDefaultInstance();
        defaultInstance.beginTransaction();
        defaultInstance.copyToRealmOrUpdate(assignment);
        defaultInstance.commitTransaction();
        defaultInstance.close();
    }

    public static class UploadAssignmentResult {
        public void setAttachmentUrl(String attachmentUrl) {
            this.attachmentUrl = attachmentUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        String attachmentUrl;
        String imageUrl;
    }
}
