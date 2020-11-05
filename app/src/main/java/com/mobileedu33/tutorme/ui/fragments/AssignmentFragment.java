package com.mobileedu33.tutorme.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.data.models.UserType;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.adapters.AssignmentsAdapter;
import com.mobileedu33.tutorme.ui.viewmodels.AssignmentsActivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AssignmentFragment extends Fragment implements AssignmentsAdapter.OnItemClickListener {

    @BindView(R.id.rvAssignments)
    RecyclerView rvAssignments;
    @BindView(R.id.progressBarList)
    ProgressBar progressBarList;
    @BindView(R.id.txtNothingToShow)
    TextView txtNothingToShow;
    AssignmentsAdapter assignmentsAdapter;
    @BindView(R.id.btnAddAssignment)
    FloatingActionButton btnAddAssignment;
    private AssignmentsActivityViewModel viewModel;
    private BaseActivity baseActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity())
                .get(AssignmentsActivityViewModel.class);
        assignmentsAdapter = new AssignmentsAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignments_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvAssignments.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        rvAssignments.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        rvAssignments.setAdapter(assignmentsAdapter);
        UserType userType = viewModel.getUserType();
        if (userType == UserType.STUDENT) {
            btnAddAssignment.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getMessagesLiveData()
                .observe(this, s -> {
                    baseActivity.showMessageSnackBar(s, null, null);
                });

        viewModel.getAssignments()
                .observe(this, assignments -> {
                    if (!assignments.isEmpty()) {
                        assignmentsAdapter.addData(assignments);
                        progressBarList.setVisibility(View.GONE);
                        txtNothingToShow.setVisibility(View.GONE);
                    } else {
                        progressBarList.setVisibility(View.GONE);
                        txtNothingToShow.setVisibility(View.VISIBLE);
                    }
                    assignmentsAdapter.notifyDataSetChanged();
                });

    }

    @Override
    public void onItemClick(Assignment assignment) {
        viewModel.setCurrentAssignment(assignment);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_assignmentsFragment_to_assignmentDetailFragment);
    }

    @Override
    public void onItemLongClick(Assignment assignment, View view) {
        if(viewModel.getUserType() == UserType.STUDENT) return;

        PopupMenu menu = new PopupMenu(requireContext(), view);
        menu.inflate(R.menu.menu_delete_assignment);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete_assignment:
                        deletAssignment(assignment);
                        break;
                    case R.id.menu_edit_assignment:
//                        TODO Implement assignment editing:
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    private void deletAssignment(Assignment assignment) {
        progressBarList.setVisibility(View.VISIBLE);
        viewModel.deleteAssignment(assignment)
                .observe(this, this::handleDeleteResult);
    }

    @OnClick(R.id.btnAddAssignment)
    public void onClick() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_assignmentsFragment_to_createAssignmentFragment);
    }

    private void handleDeleteResult(Boolean isSuccess) {
        progressBarList.setVisibility(View.GONE);
        if (isSuccess) {
            baseActivity.showMessageSnackBar("Assignment deleted.", null, null);
        } else {
            baseActivity.showMessageSnackBar("Error deleting assignment", null, null);
        }
    }
}