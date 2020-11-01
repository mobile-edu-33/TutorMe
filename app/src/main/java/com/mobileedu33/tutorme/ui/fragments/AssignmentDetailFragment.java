package com.mobileedu33.tutorme.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.models.Assignment;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AssignmentDetailFragment extends Fragment {

    @BindView(R.id.imgAssignment)
    ImageView imgAssignment;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.txtDueDate)
    TextView txtDueDate;
    private Assignment assignment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            assignment = (Assignment) arguments.getSerializable("assignment");
        }

        ((AppCompatActivity)requireActivity()).getSupportActionBar().setTitle(assignment.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (assignment != null) {
            txtDescription.setText(assignment.getDescription());
            txtDueDate.setText(assignment.getDateDue());
            txtTitle.setText(assignment.getTitle());

            if (assignment.getImageUrl() != null) {
                Glide.with(requireView())
                        .load(assignment.getImageUrl())
                        .placeholder(R.drawable.grey_background)
                        .into(imgAssignment);
            }
        }
    }
}