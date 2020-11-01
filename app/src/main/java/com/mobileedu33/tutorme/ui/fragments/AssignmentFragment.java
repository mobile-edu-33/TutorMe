package com.mobileedu33.tutorme.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.models.Assignment;
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
                });

    }

    @Override
    public void onItemClick(Assignment assignment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("assignment", assignment);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_assignmentsFragment_to_assignmentDetailFragment, bundle);
    }

    @OnClick(R.id.btnAddAssignment)
    public void onClick() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_assignmentsFragment_to_createAssignmentFragment);
    }
}