package com.mobileedu33.tutorme;


import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.FirebaseApp;
import com.mobileedu33.tutorme.data.FireStoreHelper;
import com.mobileedu33.tutorme.data.models.Assignment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class FireBaseHelperTests {

    private FireStoreHelper fireStoreHelper;

    @Before
    public void initialize() {
        FirebaseApp.
                initializeApp(InstrumentationRegistry.getInstrumentation().getContext());
        fireStoreHelper = new FireStoreHelper();
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

}
