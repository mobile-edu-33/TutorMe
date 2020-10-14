package com.mobileedu33.tutorme.utils;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.mobileedu33.tutorme.R;

import java.util.Arrays;
import java.util.List;

public class SignInUtils {

    public static final int AUTH_SIGNIN_REQUEST_CODE = 233;

    /**
     * @param layoutID Id of the layout to be used by AuthUI.
     * @param fragment Calling fragment.
     */
    public static void signIn(@LayoutRes int layoutID, Fragment fragment) {
        // Specify a custom layout to be used by AuthUI
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(layoutID)
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
        fragment.startActivityForResult(signupIntent, AUTH_SIGNIN_REQUEST_CODE);
    }

    public static void processActivityResult(int resultCode, Intent data, SignInResultListener listener) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        // Successfully signed in
        if (resultCode == Activity.RESULT_OK) {
            listener.onSignInSuccess(response);
        } else {
            // Sign in failed
            listener.onSignInError(response);
        }
    }

    public interface SignInResultListener{
        void onSignInSuccess(IdpResponse response);

        void onSignInError(IdpResponse response);
    }
}
