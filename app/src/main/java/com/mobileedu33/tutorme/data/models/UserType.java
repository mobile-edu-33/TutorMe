package com.mobileedu33.tutorme.data.models;

public enum UserType {
    TUTOR(0),
    STUDENT(1);
    final int value;

    UserType(int i) {
        value = i;
    }

}
