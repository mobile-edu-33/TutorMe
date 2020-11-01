package com.mobileedu33.tutorme.data;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobileedu33.tutorme.data.usecases.PublishAssignmentUseCase.UploadAssignmentResult;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.Lesson;
import com.mobileedu33.tutorme.data.models.LiveLesson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class FirebaseStorageHelper {
    public static final String PROFILE_IMAGES = "profile-images";
    public static final String JPG = ".jpg";
    public static final String ASSIGNMENTS_ATTACHMENTS = "assignments-attachments";
    public static final String LESSONS_BANNER_IMAGES = "lessons-banner-images";
    private final StorageReference storageReference;
    private final ExecutorService executorService;

    @Inject
    public FirebaseStorageHelper() {
        storageReference = FirebaseStorage.getInstance().getReference();
        executorService = Executors.newFixedThreadPool(3);
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

    public UploadAssignmentResult saveAssignmentsAttachment(Assignment assignment, File attachment, File bannerImage) throws ExecutionException, InterruptedException {
        UploadTask attachmentTask = null;
        UploadTask bannerImageTask = null;
        StorageReference folderRef = this.storageReference.child(ASSIGNMENTS_ATTACHMENTS)
                .child(assignment.getId().concat("/"));
        if (attachment != null) {
            Uri uri = Uri.fromFile(attachment);
            StorageReference fileRef = folderRef.child(Objects.requireNonNull(uri.getLastPathSegment()));
            attachmentTask = fileRef.putFile(uri);
        }

        if (bannerImage != null) {
            Uri uri1 = Uri.fromFile(bannerImage);
            StorageReference bannerImageRef = folderRef.child(Objects.requireNonNull(uri1.getLastPathSegment()));
            bannerImageTask = bannerImageRef.putFile(uri1);
        }

        if (bannerImageTask != null && attachmentTask != null) {
            Task<List<Task<?>>> listTask = Tasks.whenAllSuccess(bannerImageTask, attachmentTask);
            Tasks.await(listTask);
        } else if (bannerImageTask != null) {
            Tasks.await(bannerImageTask);
        } else if (attachmentTask != null) {
            Tasks.await(attachmentTask);
        }

        UploadAssignmentResult result = new UploadAssignmentResult();

        if (attachmentTask != null && attachmentTask.isSuccessful()) {
            Task<Uri> downloadUrl = attachmentTask.getResult().getStorage().getDownloadUrl();
            Tasks.await(downloadUrl);
            if(downloadUrl.isSuccessful()) result.setAttachmentUrl(downloadUrl.getResult().toString());
        }

        if (bannerImageTask != null && bannerImageTask.isSuccessful()) {
            Task<Uri> downloadUrl = bannerImageTask.getResult().getStorage().getDownloadUrl();
            Tasks.await(downloadUrl);
            if(downloadUrl.isSuccessful()) result.setImageUrl(downloadUrl.getResult().toString());
        }
        return result;
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
