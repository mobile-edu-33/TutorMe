package com.mobileedu33.tutorme;


import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobileedu33.tutorme.data.FirebaseHelper;
import com.mobileedu33.tutorme.data.models.Assignment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FireBaseHelperTests {

    private FirebaseHelper firebaseHelper;

    @Before
    public void initialize() {
        FirebaseApp.
                initializeApp(InstrumentationRegistry.getInstrumentation().getContext());
        firebaseHelper = new FirebaseHelper();
    }

    @Test
    public void getAssignmentsTest() {
        String mentorId = "GQ4CVOwA3nMJq2Kj3gQ9";
        List<Assignment> assignments = Collections.emptyList();
        try {
            assignments.addAll(firebaseHelper.getAssignments(Collections.singletonList(mentorId)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(2, assignments.size());
    }

}
