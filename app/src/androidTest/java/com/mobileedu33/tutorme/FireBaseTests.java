package com.mobileedu33.tutorme;


import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.mobileedu33.tutorme.data.BaseUseCase;
import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.FirebaseStorageHelper;
import com.mobileedu33.tutorme.data.PublishAssignmentUseCase;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FireBaseTests {

    public static final String TITLE = "About Cats";
    public static final String DESCRIPTION = "Write an essay about cats!";
    private FireStoreHelper fireStoreHelper;
    private Context targetContext;
    private FirebaseStorageHelper firebaseStorageHelper;

    @Before
    public void initialize() {
        targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.
                initializeApp(targetContext);
        fireStoreHelper = new FireStoreHelper();
        firebaseStorageHelper = new FirebaseStorageHelper();
        RealmConfiguration configuration = new RealmConfiguration.Builder().inMemory().build();
        Realm.init(targetContext);
        Realm.setDefaultConfiguration(configuration);
    }

    @Test
    public void getAssignmentsTest() {
        String mentorId = "GQ4CVOwA3nMJq2Kj3gQ9";
        List<Assignment> assignments = Collections.emptyList();
        try {
            assignments.addAll(fireStoreHelper.getAssignments(Collections.singletonList(mentorId)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(2, assignments.size());
    }

    @Test
    public void UploadFileToStorageTest() {
        PublishAssignmentUseCase publishAssignmentUseCase = new PublishAssignmentUseCase(
                new BackgroundThreadPoster(), new UiThreadPoster(),
                fireStoreHelper, firebaseStorageHelper
        );
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(targetContext, null);
        File catFile = new File(targetContext.getFilesDir(), "File.txt");
        Assignment assignment = new Assignment();
        assignment.setTitle(TITLE);
        assignment.setId("testId");
        assignment.setDescription(DESCRIPTION);

        try {
            boolean b = fireStoreHelper.publishAssignment(assignment, false);
            if(b);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CountDownLatch latch = new CountDownLatch(2);
         publishAssignmentUseCase.addListener(new BaseUseCase.UseCaseListener<Void, Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 latch.countDown();
             }

             @Override
             public void onError(Void aVoid) {
                latch.countDown();
             }
         });

        publishAssignmentUseCase.publish(assignment, catFile);
        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(assignment.getAttachmentsUrl().contains("File.txt"));
    }


}
