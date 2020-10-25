package com.mobileedu33.tutorme.utils;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobileedu33.tutorme.R;

import java.util.Arrays;
import java.util.List;

public class SignInUtils {

    public static final int AUTH_SIGNIN_REQUEST_CODE = 233;

    /**

     * @param fragment Calling fragment.
     */
    public static void signIn(Fragment fragment) {
        // Specify a custom layout to be used by AuthUI
//        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(layoutID)
//                .setGoogleButtonId(R.id.btn_googleSignIn)
//                .setEmailButtonId(R.id.btn_emailSignIn)
//                .build();

        // Specify sign in methods
        List<AuthUI.IdpConfig> idpConfigs = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().setScopes(List.of(Scopes.PROFILE, Scopes.EMAIL)).build()
        );


        Intent signupIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.FirebaseUITheme)
                .setLogo(R.drawable.logo)
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
