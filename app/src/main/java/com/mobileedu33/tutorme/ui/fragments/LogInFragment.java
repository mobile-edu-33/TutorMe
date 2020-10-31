package com.mobileedu33.tutorme.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.activities.Common.DashboardActivity;
import com.mobileedu33.tutorme.ui.viewmodels.LoginViewModel;
import com.mobileedu33.tutorme.utils.SignInUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LogInFragment extends Fragment implements SignInUtils.SignInResultListener {
    private static final String TAG = LogInFragment.class.getSimpleName();
    @BindView(R.id.progressBar3)
    ProgressBar progressBar3;
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
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loginViewModel.getMessagesLiveData()
                .observe(this, s -> {
                    baseActivity.showMessageSnackBar(s, null, null);
                });
    }

    @Override
    public void onStop() {
        loginViewModel.removeLiveDataObservers(this);
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignInUtils.processActivityResult(resultCode, data, this);
    }

    @Override
    public void onSignInSuccess(IdpResponse response) {
        progressBar3.setVisibility(View.VISIBLE);
        loginViewModel.fetchUserProfile()
                .observe(this, this::handleFetchProfileResult);
    }

    private void handleFetchProfileResult(Boolean isSuccess) {
        if (isSuccess) {
            Intent intent = new Intent(requireContext(), DashboardActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }else {
            progressBar3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSignInError(IdpResponse response) {
        if (response == null) {
            // User pressed back button
            baseActivity.showMessageSnackBar(R.string.sign_in_cancelled, "RETRY", () -> SignInUtils.signIn(this));
            return;
        }

        if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
            baseActivity.showMessageSnackBar(R.string.no_internet_connection, null, null);
            return;
        }
        baseActivity.showMessageSnackBar(R.string.unknown_error, null, null);
        Log.e(TAG, "Sign-in error: ", response.getError());
    }

    @Override
    public void onPause() {
        super.onPause();
        loginViewModel.removeLiveDataObservers(this);
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {
        SignInUtils.signIn(this);
    }

    @OnClick(R.id.btnSignUp)
    public void onClick() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_logInFragment_to_chooseUserTypeFragment);
    }
}
