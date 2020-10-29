package com.mobileedu33.tutorme.data.models;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class TutorRequest implements RealmModel {
    private String id;
    private String tutorId;
    private String studentId;
    private boolean isAccepted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
