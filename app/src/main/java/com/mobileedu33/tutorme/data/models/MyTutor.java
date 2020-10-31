package com.mobileedu33.tutorme.data.models;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class MyTutor implements RealmModel {
    private String tutorId;

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }
}
