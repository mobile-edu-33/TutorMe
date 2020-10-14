package com.mobileedu33.tutorme.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.mobileedu33.tutorme.R;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StartScreenFragment extends Fragment {
    public static final int AUTH_SIGNIN_REQUEST_CODE = 34;
    private static final String TAG = StartScreenFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void signInUser() {
        // Specify a custom layout to be used by AuthUI
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.fragment_login)
                .setGoogleButtonId(R.id.btn_googleSignIn)
                .setEmailButtonId(R.id.btn_emailSignIn)
                .build();

        // Specify sign in methods
        List<AuthUI.IdpConfig> idpConfigs = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signupIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .setAvailableProviders(idpConfigs)
                .build();
        requireActivity().startActivityForResult(signupIntent, AUTH_SIGNIN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_SIGNIN_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                handleSignInSuccess(response);
            } else {
                // Sign in failed
                handleSignInError(response);
            }
        }
    }

    private void handleSignInSuccess(IdpResponse response) {
        // TODO: Add navigation to appropriate screen
        if (response.isNewUser()) {
            Toast.makeText(requireContext(), "Welcome new user", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(requireContext(), "Welcome back!", Toast.LENGTH_SHORT)
                    .show();
            // TODO: Handle accordingly
        }
    }

    private void handleSignInError(IdpResponse response) {
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

    private void showSnackbar(@StringRes int stringId) {
        Snackbar.make(requireView(), stringId, Snackbar.LENGTH_LONG).show();
    }

}
