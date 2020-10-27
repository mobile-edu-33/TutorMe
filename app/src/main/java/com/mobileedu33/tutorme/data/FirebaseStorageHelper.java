package com.mobileedu33.tutorme.data;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.Lesson;
import com.mobileedu33.tutorme.data.models.LiveLesson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class FirebaseStorageHelper {
    public static final String PROFILE_IMAGES = "profile-images";
    public static final String JPG = ".jpg";
    public static final String ASSIGNMENTS_ATTACHMENTS = "assignments-attachments";
    public static final String LESSONS_BANNER_IMAGES = "lessons-banner-images";
    private final StorageReference storageReference;

    @Inject
    public FirebaseStorageHelper() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public String saveProfilePhoto(Bitmap bitmap, String userId) throws ExecutionException, InterruptedException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] bytes = bao.toByteArray();
        StorageReference storageReference = this.storageReference.child(PROFILE_IMAGES)
                .child(userId.concat(JPG));
        UploadTask uploadTask = storageReference.putBytes(bytes);
        boolean isSuccess = processTask(uploadTask);
        if(isSuccess) return storageReference.getDownloadUrl().toString();
        else return null;
    }

    public String saveProfilePhoto(Uri uri, String userId) throws ExecutionException, InterruptedException {
        StorageReference storageReference = this.storageReference.child(PROFILE_IMAGES)
                .child(userId.concat(JPG));
        UploadTask uploadTask = storageReference.putFile(uri);
        boolean isSuccess = processTask(uploadTask);
        if(isSuccess)return storageReference.getDownloadUrl().toString();
        else return null;
    }

    public String saveAssignmentsAttachment(Assignment assignment, File file) throws ExecutionException, InterruptedException {
        Uri uri = Uri.fromFile(file);
        StorageReference folderRef = this.storageReference.child(ASSIGNMENTS_ATTACHMENTS)
                .child(assignment.getId().concat("/"));
        StorageReference fileRef = folderRef.child(Objects.requireNonNull(uri.getLastPathSegment()));
        UploadTask uploadTask = fileRef.putFile(uri);
        Tasks.await(uploadTask);
        if(uploadTask.isSuccessful()) return folderRef.getDownloadUrl().toString();
        else return null;
    }

    public String saveLessonImage(Lesson lesson, File file) throws ExecutionException, InterruptedException {
        Uri uri = Uri.fromFile(file);
        StorageReference storageReference = this.storageReference.child(LESSONS_BANNER_IMAGES)
                .child(lesson.getId().concat("/"))
                .child(Objects.requireNonNull(uri.getLastPathSegment()));
        UploadTask uploadTask = storageReference.putFile(uri);
        boolean isSuccess = processTask(uploadTask);
        if(isSuccess) return storageReference.getDownloadUrl().toString();
        else return null;
    }

    public String saveLessonImage(LiveLesson lesson, File file) throws ExecutionException, InterruptedException {
        Uri uri = Uri.fromFile(file);
        StorageReference storageReference = this.storageReference.child(LESSONS_BANNER_IMAGES)
                .child(lesson.getId().concat("/"))
                .child(Objects.requireNonNull(uri.getLastPathSegment()));
        UploadTask uploadTask = storageReference.putFile(uri);
        boolean isSuccess = processTask(uploadTask);
        if(isSuccess) return storageReference.getDownloadUrl().toString();
        else return null;
    }


    private <T> boolean processTask(Task<T> tTask) throws ExecutionException, InterruptedException {
        Tasks.await(tTask);
        return tTask.isSuccessful();
    }
}
