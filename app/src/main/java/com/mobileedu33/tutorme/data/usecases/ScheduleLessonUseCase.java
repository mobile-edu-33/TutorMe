package com.mobileedu33.tutorme.data.usecases;

import android.util.Log;

import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.FirebaseStorageHelper;
import com.mobileedu33.tutorme.data.models.LiveLesson;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import java.io.File;

import javax.inject.Inject;

import io.realm.Realm;

public class ScheduleLessonUseCase extends BaseUseCase<Void, Void> {
    public static final String TAG = ScheduleLessonUseCase.class.getSimpleName();
    private final FireStoreHelper fireStoreHelper;
    private final FirebaseStorageHelper firebaseStorageHelper;
    private LiveLesson currentLesson;

    @Inject
    public ScheduleLessonUseCase(BackgroundThreadPoster backgroundThreadPoster, UiThreadPoster uiThreadPoster, FireStoreHelper fireStoreHelper, FirebaseStorageHelper firebaseStorageHelper) {
        super(backgroundThreadPoster, uiThreadPoster);
        this.fireStoreHelper = fireStoreHelper;
        this.firebaseStorageHelper = firebaseStorageHelper;
    }

    public void schedule(LiveLesson liveLesson, File imageUrl) {
       executeInBackground(() -> execute(liveLesson, imageUrl));
    }

    private void execute(LiveLesson liveLesson, File image) {
        Realm realm = Realm.getDefaultInstance();
        TutorProfile tutorProfile = realm.where(TutorProfile.class).findFirst();
        if(tutorProfile == null) throw new IllegalStateException("No tutor profile found! A tutor profile is required to publish an assignment.");
        liveLesson.setCreatorId(tutorProfile.getId());
        liveLesson.setId(
                tutorProfile.getId()
                        .concat("-")
                        .concat(String.valueOf(System.currentTimeMillis())));
        realm.close();

        try {
            String imageUrl = firebaseStorageHelper.saveLessonImage(liveLesson, image);
            handleImageUploadResult(imageUrl, liveLesson);
        } catch (Exception e) {
            Log.e(TAG, "Error uploading data to storage", e);
            notifyError(null);
        }
    }

    private void handleImageUploadResult(String result, LiveLesson liveLesson) {
        if (result == null) {
            notifyError(null);
            return;
        }

        try {
            boolean saveResult = fireStoreHelper.saveScheduledLesson(liveLesson, false);
            handleSaveResult(saveResult);
        } catch (Exception e) {
            Log.e(TAG, "Error saving data to database", e);
            notifyError(null);
        }
    }

    private void handleSaveResult(boolean isSuccessful) {
        if (isSuccessful) {
            saveToLocalStorage(currentLesson);
            notifySuccess(null);
            return;
        }
        notifyError(null);
    }

    private void saveToLocalStorage(LiveLesson liveLesson) {
        Realm defaultInstance = Realm.getDefaultInstance();
        defaultInstance.beginTransaction();
        defaultInstance.copyToRealmOrUpdate(liveLesson);
        defaultInstance.commitTransaction();
        defaultInstance.close();
    }
}
