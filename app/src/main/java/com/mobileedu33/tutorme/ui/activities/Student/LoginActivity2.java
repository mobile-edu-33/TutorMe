package com.mobileedu33.tutorme.ui.activities.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.mobileedu33.tutorme.R;

public class LoginActivity2 extends AppCompatActivity {

    Button callLogin, signupButton;
    TextView logoText, sloganText;
    TextInputLayout username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        // Redirects the student to a sign up form
        signUpRedirect();
    }

    private void signUpRedirect() {

        // Hooks
       /* callLogin = findViewById(R.id.std_login_screen);
        //signupButton = findViewById(R.id.std_signup_screen);
        logoText = findViewById(R.id.welcome_name);
        sloganText = findViewById(R.id.slogan_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);*/

        Button studentSign = findViewById(R.id.std_signup_screen);

        studentSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity2.this, StudentSignUpActivity.class);
                startActivity(intent);
                /*Pair[] pairs = new Pair[5];

                pairs[0] = new Pair<View, String>(logoText, "welcome_message");
                pairs[1] = new Pair<View, String>(sloganText, "slogan_name");
                pairs[2] = new Pair<View, String>(username, "username_trans");
                pairs[3] = new Pair<View, String>(password, "password_trans");
                //pairs[4] = new Pair<View, String>(signupButton, "dont_have_account");
                pairs[4] = new Pair<View, String>(callLogin, "welcome_message");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentLoginActivity.this, pairs);*/
            }

        });
    }
}