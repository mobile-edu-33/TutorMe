package com.mobileedu33.tutorme.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.viewmodels.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends BaseActivity {

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
           hideActionBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLifecycle().addObserver(viewModel);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getLifecycle().removeObserver(viewModel);
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