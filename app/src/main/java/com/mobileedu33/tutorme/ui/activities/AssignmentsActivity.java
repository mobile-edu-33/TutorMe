package com.mobileedu33.tutorme.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.ui.viewmodels.AssignmentsActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AssignmentsActivity extends BaseActivity {

    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;
    private NavController navController;
    private AssignmentsActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMain);
        viewModel = new ViewModelProvider(this)
                .get(AssignmentsActivityViewModel.class);
        getLifecycle().addObserver(viewModel);
        navController = Navigation.findNavController(this, R.id.assignments_navhost_fragment);
        NavigationUI.setupWithNavController(toolbarMain, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLifecycle().addObserver(viewModel);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getLifecycle().removeObserver(viewModel);
    }
}