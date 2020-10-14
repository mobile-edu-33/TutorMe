package com.mobileedu33.tutorme.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.core.view.WindowInsetsCompat;

import com.mobileedu33.tutorme.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends BaseActivity {
       @Override
    protected void onCreate(Bundle savedInstanceState) {
           hideActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void hideActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController controller = getWindow().getInsetsController();

            if (controller != null)
                controller.hide(WindowInsets.Type.navigationBars());
        }
        else {
            getSupportActionBar().hide();
        }
    }

}