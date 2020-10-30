package com.mobileedu33.tutorme.ui.activities.Common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.SharedPreferencesRepository;
import com.mobileedu33.tutorme.ui.activities.Student.StudentDashboard;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation topAnim, bottomAnim;
    ImageView splashImage;
    TextView logoText;
    TextView sloganText;
    @Inject
    SharedPreferencesRepository sharedPreferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hides the status bar from the application
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.splash_main);

        // Calls the animator() function
        animator();
        // Calls this method to move to the choose category activity
        goToCatergory();

    }

    private void goToCatergory() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Checks if the user is using the app for the first time and assign a variable
                boolean isFirstTime = sharedPreferencesRepository.isFirstRun();

                // Lets the user go through to the on boarding screen and start activity
                if (isFirstTime) {
                    sharedPreferencesRepository.setIsFirstRun(false);
                    Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                    startActivity(intent);
                    // The finish method removes this activity from the activity list
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, StudentDashboard.class);
                    startActivity(intent);
                }
            }
        }, SPLASH_SCREEN);
    }

    private void animator() {
        // Creating animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Hooks
        splashImage = findViewById(R.id.splah_image);
        logoText = findViewById(R.id.logo_text);
        sloganText = findViewById(R.id.slogan_text);

        // Assigning the animation to their respective views
        splashImage.setAnimation(topAnim);
        logoText.setAnimation(topAnim);
        sloganText.setAnimation(bottomAnim);
    }
}