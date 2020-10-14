package com.mobileedu33.tutorme.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobileedu33.tutorme.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WalkthroughFragment1 extends Fragment {

    public static final String IS_NEW_USER = "isNewUser";
    private boolean isNewUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNewUser = getArguments().getBoolean(IS_NEW_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_walthrough_screen1, container, false);
    }
}