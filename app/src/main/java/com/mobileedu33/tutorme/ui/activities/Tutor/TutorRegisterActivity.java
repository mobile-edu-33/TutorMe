package com.mobileedu33.tutorme.ui.activities.Tutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mobileedu33.tutorme.R;

public class TutorRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_register);

        tutorLoginRedirect();
    }

    private void tutorLoginRedirect() {
        Button signIn = findViewById(R.id.tutor_login_screen);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorRegisterActivity.this, TutorLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}