package com.mobileedu33.tutorme.ui.activities.Common;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.HelperClasses.SliderAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout layoutDots;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetStarted;
    Animation animation;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        // Views references
        viewPager = findViewById(R.id.slider);
        layoutDots = findViewById(R.id.dots);
        letsGetStarted = findViewById(R.id.get_started_btn);

        // Calling the SliderAdapter class
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    /* Skips the walkthrough screens and redirects the user to the sign up screen*/
    public void skip(View view) {
        startActivity(new Intent(this, ChooseCategoryActivity.class));
        // Destroys the activity
        finish();
    }

    public void next(View view) {
        // Move to a new screen
        viewPager.setCurrentItem(currentPosition + 1);
    }

    private void addDots(int position) {
        // Create the dots using text views
        dots = new TextView[4];
        layoutDots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            // Creating next Text Views in this current class by passing the index
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            // Gets the current screen the user is in
            currentPosition = position;

            // Checks if on which position the button is and hide it if the position is 0
            switch (position) {
                case 0:
                case 1:
                case 2:
                    letsGetStarted.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    // Animates the page once the button is visible
                    animation = AnimationUtils.loadAnimation(OnBoardingActivity.this, R.anim.bottom_animation);
                    // Sets the animation on the button
                    letsGetStarted.setAnimation(animation);
                    letsGetStarted.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}