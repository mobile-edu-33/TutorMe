package com.mobileedu33.tutorme.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.fragments.CreateAssignmentFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onClick() {
        startActivity(new Intent(this, CreateAssignmentFragment.class));
    }
}