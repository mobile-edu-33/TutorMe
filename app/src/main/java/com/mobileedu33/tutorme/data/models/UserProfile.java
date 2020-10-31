package com.mobileedu33.tutorme.data.models;

import io.realm.RealmModel;

public interface UserProfile {
    String getDisplayName();

    String getEmail();

    String getPhotoUrl();

    String getAddress();

    String getPhone();
}
