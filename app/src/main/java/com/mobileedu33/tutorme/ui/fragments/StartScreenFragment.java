package com.mobileedu33.tutorme.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.activities.MainActivity;
import com.mobileedu33.tutorme.ui.viewmodels.LoginViewModel;
import com.mobileedu33.tutorme.utils.SignInUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StartScreenFragment extends Fragment implements SignInUtils.SignInResultListener {
    private static final String TAG = StartScreenFragment.class.getSimpleName();

    private LoginViewModel loginViewModel;
    private BaseActivity baseActivity;
    private boolean isSigninInProgress;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

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
        if (!isSigninInProgress) {
            loginViewModel.isFirstRun()
                    .observe(this, isFirstRun -> {
                        if (isFirstRun) {
                            // Navigate to welcome screen
                            NavHostFragment.findNavController(this)
                                    .navigate(R.id.welcomeScreenFragment);

                        } else {
                            handleNotFirstTimeRun();
                        }
                    });
        }
    }

    private void handleNotFirstTimeRun() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User already logged in so let's go to the Main screen
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finish();
        } else {
            SignInUtils.signIn(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignInUtils.processActivityResult(resultCode, data, this);
    }

    @Override
    public void onSignInSuccess(IdpResponse response) {
        if (!response.isNewUser()) {
            baseActivity.showMessageSnackBar(R.string.welcome_back);
        }

        loginViewModel.setIsFirstRunFalse();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
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
