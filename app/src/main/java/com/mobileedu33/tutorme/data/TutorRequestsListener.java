package com.mobileedu33.tutorme.data;

import com.mobileedu33.tutorme.data.models.TutorRequest;

import java.util.List;

public interface TutorRequestsListener {
    default void onTutorAccepted(TutorRequest tutorRequest) {

    }

    default void onTutorRejected(TutorRequest tutorRequest) {

    }

    default void onTutorRequested(List<TutorRequest> requests) {

    }
}
