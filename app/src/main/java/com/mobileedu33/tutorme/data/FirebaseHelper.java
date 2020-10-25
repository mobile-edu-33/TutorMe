package com.mobileedu33.tutorme.data;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.Lesson;
import com.mobileedu33.tutorme.data.models.LiveLesson;
import com.mobileedu33.tutorme.data.models.StudentProfile;
import com.mobileedu33.tutorme.data.models.TutorProfile;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;

public class FirebaseHelper {
    public static final String ASSIGNMENTS_ALL = "assignments-all";
    public static final String SCHEDULED_LESSONS = "scheduled-lessons";
    public static final String RECORDED_LESSONS = "recorded-lessons";
    public static final String TUTORS = "tutors";
    public static final String STUDENTS = "students";
    private FirebaseFirestore firebaseFirestore;

    @Inject
    public FirebaseHelper() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public List<Assignment> getAssignments(List<String> creatorIds) throws ExecutionException, InterruptedException {
        Task<QuerySnapshot> snapshotTask = firebaseFirestore.collection(ASSIGNMENTS_ALL)
                .whereIn("creatorId", creatorIds)
                .get();
        Tasks.await(snapshotTask);
        return Objects.requireNonNull(snapshotTask.getResult()).toObjects(Assignment.class);
    }

    public boolean saveAssignment(Assignment assignment) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(ASSIGNMENTS_ALL)
                .document()
                .set(assignment);
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

    public boolean saveScheduledLesson(LiveLesson liveLesson) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(SCHEDULED_LESSONS)
                .document()
                .set(liveLesson);
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

    public boolean saveRecordedLesson(Lesson recordedLesson) throws ExecutionException, InterruptedException {
        Task<Void> task = firebaseFirestore.collection(RECORDED_LESSONS)
                .document()
                .set(recordedLesson);
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
}
