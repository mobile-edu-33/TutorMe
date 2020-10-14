package com.mobileedu33.tutorme.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.material.snackbar.Snackbar;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.activities.MainActivity;
import com.mobileedu33.tutorme.ui.viewmodels.LoginViewModel;
import com.mobileedu33.tutorme.utils.SignInUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StartScreenFragment extends Fragment implements SignInUtils.SignInResultListener {
    public static final int AUTH_SIGNIN_REQUEST_CODE = 34;
    private static final String TAG = StartScreenFragment.class.getSimpleName();

    private LoginViewModel loginViewModel;
    private Auth mAuth;
    private boolean isFirstRun;
    private boolean signinInProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_screen, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!signinInProgress) {
            loginViewModel.isFirstRun()
                    .observe(this, isFirstRun -> {
                        if (isFirstRun) {
                            // Navigate to welcome screen
                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.welcomeScreenFragment);
                            this.isFirstRun = true;
                        } else {
                            handleNotFirstTimeRun();
                        }
                    });
        }
    }

    private void handleNotFirstTimeRun() {
        if (loginViewModel.isLoggedIn()) {
            // User already logged in so let's go to the Main screen
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finish();
        } else {
            // else let's sign in user
            SignInUtils.signIn(R.layout.layout_auth_sign_in, this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignInUtils.processActivityResult(resultCode, data, this);

    }

    private void showSnackbar(@StringRes int stringId) {
        Snackbar.make(requireView(), stringId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSignInSuccess(IdpResponse response) {
        if (response.isNewUser()) {
            Toast.makeText(requireContext(), "Welcome new user", Toast.LENGTH_SHORT)
                    .show();
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_startScreenFragment_to_walkthroughFragment1);
        } else {
            Toast.makeText(requireContext(), "Welcome back!", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }

        if(isFirstRun) loginViewModel.setIsFirstRunFalse();
        loginViewModel.setIsLoggedInTrue();
    }

    @Override
    public void onSignInError(IdpResponse response) {
        if (response == null) {
            // User pressed back button
            showSnackbar(R.string.sign_in_cancelled);
            return;
        }

        if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
            showSnackbar(R.string.no_internet_connection);
            return;
        }
        showSnackbar(R.string.unknown_error);
        Log.e(TAG, "Sign-in error: ", response.getError());
    }
}
