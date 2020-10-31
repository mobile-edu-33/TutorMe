package com.mobileedu33.tutorme.ui.activities.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.SharedPreferencesRepository;
import com.mobileedu33.tutorme.ui.activities.LoginActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    Animation topAnim, bottomAnim;
    ImageView splashImage;
    TextView logoText;
    TextView sloganText;
    @Inject
    SharedPreferencesRepository sharedPreferencesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        HideActionBarUtil.hideActionBar(this);
        super.onCreate(savedInstanceState);
        // Hides the status bar from the application
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.splash_main);
        splashImage = findViewById(R.id.splah_image);
        logoText = findViewById(R.id.logo_text);
        sloganText = findViewById(R.id.slogan_text);

        // Calls the animator() function
        // Calls this method to move to the choose category activity
        animator();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            animator();
//        }
    }

    private void goToCatergory() {
        boolean isFirstTime = sharedPreferencesRepository.isFirstRun();

        if (isFirstTime) {
            // Go to Onboarding screen
            sharedPreferencesRepository.setIsFirstRun(false);
            Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
        } else {
            // check if user is already signed in
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                // user is signed in go to Main activity
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setAction("login");
                startActivity(intent);
                finish();
            }
        }
//        new Handler(getMainLooper()).postDelayed(() -> {
//                  // Checks if the user is using the app for the first time and assign a variable
//
//        }, SPLASH_SCREEN);
    }

    private void animator() {
        // Creating animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        bottomAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goToCatergory();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // Assigning the animation to their respective views
        splashImage.setAnimation(topAnim);
        logoText.setAnimation(topAnim);
        sloganText.setAnimation(bottomAnim);
    }
}