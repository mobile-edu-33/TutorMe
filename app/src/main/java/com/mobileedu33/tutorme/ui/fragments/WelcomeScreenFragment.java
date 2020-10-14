package com.mobileedu33.tutorme.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.activities.MainActivity;
import com.mobileedu33.tutorme.ui.viewmodels.LoginViewModel;
import com.mobileedu33.tutorme.utils.SignInUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WelcomeScreenFragment extends Fragment implements SignInUtils.SignInResultListener {

    private static final String TAG = WelcomeScreenFragment.class.getSimpleName();
    private LoginViewModel loginViewModel;
    private BaseActivity baseActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_next)
    public void onNextClick() {
        SignInUtils.signIn(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignInUtils.processActivityResult(resultCode, data, this);

    }

    @Override
    public void onSignInSuccess(IdpResponse response) {
        baseActivity.showMessageSnackBar(R.string.account_created_successfully);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_welcomeScreenFragment_to_walkthroughFragment1);
        loginViewModel.setIsFirstRunFalse();
    }

    @Override
    public void onSignInError(IdpResponse response) {
        if (response == null) {
            // User pressed back button
            baseActivity.showMessageSnackBar(R.string.sign_in_cancelled);
            return;
        }

        if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
            baseActivity.showMessageSnackBar(R.string.no_internet_connection);
            return;
        }
        baseActivity.showMessageSnackBar(R.string.unknown_error);
        Log.e(TAG, "Sign-in error: ", response.getError());
    }
}