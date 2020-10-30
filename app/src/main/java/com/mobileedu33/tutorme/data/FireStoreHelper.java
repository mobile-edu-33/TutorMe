package com.mobileedu33.tutorme.data;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.Lesson;
import com.mobileedu33.tutorme.data.models.LiveLesson;
import com.mobileedu33.tutorme.data.models.StudentProfile;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import com.mobileedu33.tutorme.data.models.TutorRequest;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class FireStoreHelper {
    public static final String ASSIGNMENTS_ALL = "assignments-all";
    public static final String SCHEDULED_LESSONS = "scheduled-lessons";
    public static final String RECORDED_LESSONS = "recorded-lessons";
    public static final String TUTORS = "tutors";
    public static final String STUDENTS = "students";
    public static final String TUTOR_REQUESTS = "tutor-requests";
    private FirebaseFirestore firebaseFirestore;

    @Inject
    public FireStoreHelper() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public List<Assignment> getAssignments(List<String> creatorIds) throws ExecutionException, InterruptedException {
        Task<QuerySnapshot> snapshotTask = firebaseFirestore.collection(ASSIGNMENTS_ALL)
                .whereIn("creatorId", creatorIds)
                .get();
        Tasks.await(snapshotTask);
        return Objects.requireNonNull(snapshotTask.getResult()).toObjects(Assignment.class);
    }

    public boolean publishAssignment(Assignment assignment, boolean isUpdate) throws ExecutionException, InterruptedException {

        CollectionReference collection = firebaseFirestore.collection(ASSIGNMENTS_ALL);
        DocumentReference documentReference;
        if(isUpdate) documentReference = collection.document(assignment.getRefId());
        else {
            documentReference = collection.document();
            assignment.setRefId(documentReference.getId());
        }
        Task<Void> task = documentReference.set(assignment);
        Tasks.await(task);

        if (task.isSuccessful()) {
            assignment.setRefId(documentReference.getId());
            return true;
        } else return false;
    }

    public boolean deleteAssignment(Assignment assignment) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(ASSIGNMENTS_ALL)
                .document(assignment.getRefId())
                .delete();
        Tasks.await(task);
        return task.isSuccessful();
    }

    public List<LiveLesson> getScheduledLessons(List<String> creatorIds) throws ExecutionException, InterruptedException {
        Task<QuerySnapshot> querySnapshotTask = firebaseFirestore.collection(SCHEDULED_LESSONS)
                .whereIn("creatorId", creatorIds)
                .get();
        Tasks.await(querySnapshotTask);
        return Objects.requireNonNull(querySnapshotTask.getResult()).toObjects(LiveLesson.class);
    }

    public boolean publishScheduledLesson(LiveLesson liveLesson, boolean isUpdate) throws ExecutionException, InterruptedException {
        CollectionReference collection = firebaseFirestore.collection(SCHEDULED_LESSONS);
        DocumentReference documentReference;
        if(isUpdate) documentReference = collection.document(liveLesson.getId());
        else {
            documentReference = collection.document();
            liveLesson.setId(documentReference.getId());
        }
        Task<Void> task = documentReference.set(liveLesson);
        Tasks.await(task);
        return task.isSuccessful();
    }

    public boolean deleteScheduledLesson(LiveLesson liveLesson) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(SCHEDULED_LESSONS)
                .document(liveLesson.getId())
                .delete();
        Tasks.await(task);
        return task.isSuccessful();
    }

    public List<Lesson> getRecordedLessons(List<String> creatorIds) throws ExecutionException, InterruptedException {
        Task<QuerySnapshot> querySnapshotTask = firebaseFirestore.collection(RECORDED_LESSONS)
                .whereIn("creatorId", creatorIds)
                .get();
        Tasks.await(querySnapshotTask);
        return Objects.requireNonNull(querySnapshotTask.getResult()).toObjects(Lesson.class);
    }

    public boolean publishRecordedLesson(Lesson recordedLesson, boolean isUpdate) throws ExecutionException, InterruptedException {
        CollectionReference collection = firebaseFirestore.collection(RECORDED_LESSONS);
        DocumentReference documentReference;
        if(isUpdate) documentReference = collection.document(recordedLesson.getId());
        else {
            documentReference = collection.document();
            recordedLesson.setId(documentReference.getId());
        }
        Task<Void> task = documentReference.set(recordedLesson);
        Tasks.await(task);
        return task.isSuccessful();
    }

    public boolean deleteRecordedLesson(LiveLesson liveLesson) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(RECORDED_LESSONS)
                .document(liveLesson.getId())
                .delete();
        Tasks.await(task);
        return task.isSuccessful();
    }

    public boolean saveTutorProfile(TutorProfile tutorProfile, String userId) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(TUTORS)
                .document(userId)
                .set(tutorProfile);
        Tasks.await(task);
        return task.isSuccessful();
    }

    public TutorProfile getTutorProfile(String userId) throws ExecutionException, InterruptedException {
        Task<DocumentSnapshot> documentSnapshotTask = firebaseFirestore.collection(TUTORS)
                .document(userId)
                .get();
        Tasks.await(documentSnapshotTask);
        return Objects.requireNonNull(documentSnapshotTask.getResult()).toObject(TutorProfile.class);
    }

    public boolean deleteTutorProfile(TutorProfile profile) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(TUTORS)
                .document(profile.getId())
                .delete();
        Tasks.await(task);
        return task.isSuccessful();
    }

    public boolean saveStudentProfile(StudentProfile studentProfile, String userId) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(STUDENTS)
                .document(userId)
                .set(studentProfile);
        Tasks.await(task);
        return task.isSuccessful();
    }

    public StudentProfile getStudentProfile(String userId) throws ExecutionException, InterruptedException {
        Task<DocumentSnapshot> documentSnapshotTask = firebaseFirestore.collection(TUTORS)
                .document(userId)
                .get();
        Tasks.await(documentSnapshotTask);
        return Objects.requireNonNull(documentSnapshotTask.getResult()).toObject(StudentProfile.class);
    }

    public boolean deleteStudentProfile(StudentProfile profile) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(STUDENTS)
                .document(profile.getId())
                .delete();
        Tasks.await(task);
        return task.isSuccessful();
    }

    public boolean requestTutor(TutorRequest request) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(TUTOR_REQUESTS)
                .document()
                .set(request);
        Tasks.await(task);
        return task.isSuccessful();
    }

    public void getTutorRequestResponse(StudentProfile studentProfile, TutorRequestsListener listener) {
        firebaseFirestore.collection(TUTOR_REQUESTS)
                .whereEqualTo("studentId", studentProfile.getId())
                .addSnapshotListener((value, error) -> {
                    if (error != null && value != null) {
                        handleTutorRequestResponse(value, listener);
                    }
                });
    }

    private void handleTutorRequestResponse(QuerySnapshot querySnapshot, TutorRequestsListener listener) {
        List<TutorRequest> tutorRequests = querySnapshot.toObjects(TutorRequest.class);
        for (TutorRequest tutorRequest : tutorRequests) {
            if (tutorRequest == null) return;

            if (tutorRequest.isAccepted()) {
                listener.onTutorAccepted(tutorRequest);
            } else {
                listener.onTutorRejected(tutorRequest);
            }
        }
    }

    public void getTutorRequests(TutorProfile profile, TutorRequestsListener listener) {
        firebaseFirestore.collection(TUTOR_REQUESTS)
                .whereEqualTo("studentId", profile.getId())
                .addSnapshotListener((value, error) -> {
                    if (error != null && value != null) {
                        List<TutorRequest> tutorRequests = value.toObjects(TutorRequest.class);
                        if(!tutorRequests.isEmpty()) listener.onTutorRequested(tutorRequests);
                    }
                });
    }

}