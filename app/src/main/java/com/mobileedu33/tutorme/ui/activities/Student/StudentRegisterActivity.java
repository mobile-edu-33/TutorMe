package com.mobileedu33.tutorme.ui.activities.Student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mobileedu33.tutorme.R;


public class StudentRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudent_register);

        StudentLoginRedirect();
    }

    private void StudentLoginRedirect() {
        Button signIn = findViewById(R.id.std_login_screen);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentRegisterActivity.this, LoginActivity2.class);
                startActivity(intent);
            }
        });
    }
}