package com.mobileedu33.tutorme.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.viewmodels.LoginViewModel;
import com.mobileedu33.tutorme.utils.HideActionBarUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends BaseActivity {

    private LoginViewModel viewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        HideActionBarUtil.hideActionBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);
        navController = Navigation.findNavController(this, R.id.login_navhost_fragment);

        assert getIntent().getAction() != null;
        if (getIntent().getAction().equalsIgnoreCase("signup")) {
            navController.getGraph().setStartDestination(R.id.signUpFragment);
            navController.navigate(R.id.signUpFragment);
        }

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
}