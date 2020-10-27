package com.mobileedu33.tutorme;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import dagger.hilt.android.HiltAndroidApp;
import io.realm.Realm;
import io.realm.RealmConfiguration;

@HiltAndroidApp
public class TutorMeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
