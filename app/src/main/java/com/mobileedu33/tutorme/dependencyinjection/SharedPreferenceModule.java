package com.mobileedu33.tutorme.dependencyinjection;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ActivityComponent.class)
public class SharedPreferenceModule {

    @Provides
    public static SharedPreferences providesSharePrefs(@ApplicationContext Context context) {
        return context.getSharedPreferences("appSharedPreferences", Context.MODE_PRIVATE);
    }
}
