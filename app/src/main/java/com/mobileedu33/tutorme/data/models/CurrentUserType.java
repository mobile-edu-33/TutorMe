package com.mobileedu33.tutorme.data.models;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class CurrentUserType implements RealmModel {
    @PrimaryKey
    private String Key = CurrentUserType.class.getSimpleName();
    private int userType;

    public UserType getUserType() {
        return UserType.getByValue(userType);
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
