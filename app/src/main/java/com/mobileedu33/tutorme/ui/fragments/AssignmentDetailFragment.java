package com.mobileedu33.tutorme.ui.fragments;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.viewmodels.AssignmentsActivityViewModel;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.btnDownloadAttachments)
    Button btnDownloadAttachments;
    private Assignment assignment;
    private long downloadId;

    private AssignmentsActivityViewModel viewModel;
    private BaseActivity baseActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity())
                .get(AssignmentsActivityViewModel.class);
        assignment = viewModel.getCurrentAssignment();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(assignment.getTitle());
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (assignment.getAttachmentsUrl() == null) {
            btnDownloadAttachments.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (assignment != null) {
            txtDescription.setText(assignment.getDescription());
            txtDueDate.setText(assignment.getDisplayDueDate());
            txtTitle.setText(assignment.getTitle());

            if (assignment.getImageUrl() != null) {
                Glide.with(requireView())
                        .load(assignment.getImageUrl())
                        .placeholder(R.drawable.grey_background)
                        .into(imgAssignment);
            }
        }
    }

    @OnClick(R.id.btnDownloadAttachments)
    public void onDownloadAttachment() {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        requireContext().registerReceiver(downloadsReceiver, intentFilter);

        DownloadManager downloadManager = (DownloadManager) requireContext()
                .getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(assignment.getAttachmentsUrl());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(assignment.getTitle().concat(" - assignment attachment"));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        downloadId = downloadManager.enqueue(request);
    }

    private BroadcastReceiver downloadsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long longExtra = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (longExtra == downloadId) {
                    baseActivity.showMessageSnackBar("Attachment downloaded. Check downloads folder", null, null);
                }
            }
        }
    };

}