package com.mobileedu33.tutorme.ui.viewmodels;

import androidx.lifecycle.LifecycleOwner;

import com.mobileedu33.tutorme.data.models.Assignment;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint

public class AssignmentViewModel extends  BaseViewModel {




    @Override
    public void removeLiveDataObservers(LifecycleOwner lifecycleOwner) {

    }

    public void publish(Assignment assignment, File attachment, File imageUrl) {
         }

    private void executeInBackground(Object o) {

    }

    private void execute(Assignment assignment, File attachment, File imageUrl) {
    }

}
