package com.mobileedu33.tutorme.ui.activities.Common;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobileedu33.tutorme.R;

public class AllSubjects extends AppCompatActivity {
    ;
    ImageView btnBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subjects);

        menuIconBackPressed();
    }

    // This function will be called when the back icon is pressed
    private void menuIconBackPressed() {
        btnBackPressed = findViewById(R.id.btn_back_pressed);
        // When the back button is called, it will jump back to the previous activity
        btnBackPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllSubjects.super.onBackPressed();
            }
        });
    }
}