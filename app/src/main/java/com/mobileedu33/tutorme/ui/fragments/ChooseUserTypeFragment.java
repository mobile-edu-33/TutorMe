package com.mobileedu33.tutorme.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.models.UserType;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.activities.LoginActivity;
import com.mobileedu33.tutorme.ui.viewmodels.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChooseUserTypeFragment extends Fragment {
    @BindView(R.id.radio_student)
    RadioButton radioStudent;
    @BindView(R.id.radio_tutor)
    RadioButton radioTutor;
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
        View view = inflater.inflate(R.layout.fragment_choose_usertype, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.radio_student, R.id.radio_tutor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_student:
                radioTutor.setChecked(false);
                loginViewModel.setUserType(UserType.STUDENT);
                break;
            case R.id.radio_tutor:
                radioStudent.setChecked(false);
                loginViewModel.setUserType(UserType.TUTOR);
                break;
        }
    }

    @OnClick(R.id.btn_cancel)
    public void onNext() {
        if (!radioStudent.isChecked() && !radioTutor.isChecked()) {
            baseActivity.showMessageSnackBar("Please select user type!", null, null);
            return;
        }

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_chooseUserTypeFragment_to_signUpFragment);
    }
}