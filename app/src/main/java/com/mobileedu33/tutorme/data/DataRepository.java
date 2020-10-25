package com.mobileedu33.tutorme.data;

import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.Lesson;
import com.mobileedu33.tutorme.data.models.LiveLesson;
import com.mobileedu33.tutorme.data.models.StudentProfile;

import java.util.List;

public interface DataRepository {
    StudentProfile getStudent();

    List<Assignment> getAllAssignments();

    List<Assignment> getAssignmentsByMentor(String mentorId);

    List<Lesson> getLessonsByMentor(String mentorId);

    List<Lesson> getAllLessons();

    List<LiveLesson> getAllLiveLessons();

    List<LiveLesson> getLiveLessonsByMentor(String mentorId);


}
