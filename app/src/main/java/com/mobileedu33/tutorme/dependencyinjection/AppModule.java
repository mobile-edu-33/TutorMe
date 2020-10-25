package com.mobileedu33.tutorme.dependencyinjection;

import com.techyourchance.threadposter.BackgroundThreadPoster;
import com.techyourchance.threadposter.UiThreadPoster;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public BackgroundThreadPoster providesBackgroundPoster() {
        return new BackgroundThreadPoster();
    }

    @Provides
    @Singleton
    public UiThreadPoster providesUiPoster() {
        return new UiThreadPoster();
    }
}
