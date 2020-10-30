package com.mobileedu33.tutorme.ui.activities.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mobileedu33.tutorme.R;


public class StudentSignUpActivity extends AppCompatActivity {

     Button studentLogin;
     Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        // Redirect the user to a sign in page when a button is clicked
        studentLogin = findViewById(R.id.std_login_screen);
        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentSignUpActivity.this, LoginActivity2.class);
                startActivity(intent);
            }
        });

        moveNextSignUp();
    }

    private void moveNextSignUp() {
        signUp = findViewById(R.id.btn_next);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentSignUpActivity.this, StudentRegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}