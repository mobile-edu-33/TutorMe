package com.mobileedu33.tutorme.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.usecases.BaseUseCase;
import com.mobileedu33.tutorme.data.usecases.PublishAssignmentUseCase;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.viewmodels.AssignmentsActivityViewModel;
import com.mobileedu33.tutorme.utils.FilePathUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

import static android.app.Activity.RESULT_OK;

@AndroidEntryPoint
public class CreateAssignmentFragment extends Fragment {

    // Request code for selecting a PDF document.
    private static final int PICK_ATTACHMENT_FILE = 2;
    private static final int PICK_IMAGE_FILE = 345;
    public static final int WRITE_STORAGE_REQUEST_CODE = 234;
    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextDescription)
    EditText editTextDescription;
    @BindView(R.id.textView2)
    TextView textView2;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.imageViewHeaderImage)
    ImageView imageViewHeaderImage;
    private Assignment assignment = new Assignment();
    private File attachment;
    private File image;
    private boolean setHasStorageReadPermission;
    private BaseActivity baseActivity;
    private AssignmentsActivityViewModel viewModel;


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

        if (ContextCompat.
                checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadStoragePermission();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_assignment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_STORAGE_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setHasStorageReadPermission = true;
        } else {
            setHasStorageReadPermission = false;
            showRequiresPermissionMessage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getMessagesLiveData()
                .observe(this, s -> {
                    baseActivity.showMessageSnackBar(s, null, null);
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.removeLiveDataObservers(this);
    }

    @OnClick(R.id.btnAddAttachment)
    public void onAddAttachment() {
        if (!setHasStorageReadPermission) {
            showRequiresPermissionMessage();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_ATTACHMENT_FILE);
    }


    @OnClick(R.id.viewAddImage)
    public void selectImage() {
        if (!setHasStorageReadPermission) {
            showRequiresPermissionMessage();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FILE);
    }

    @OnClick(R.id.btn_publishAssignment)
    public void onPublish() {
        assignment.setTitle(editTextTitle.getText().toString());
        assignment.setDescription(editTextDescription.getText().toString());
        viewModel.publishAssignment(assignment, attachment, image)
                .observe(this, this::handlePublishResult);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void handlePublishResult(Boolean isSuccess) {
        progressBar.setVisibility(View.GONE);
        if (isSuccess) {
            NavHostFragment.findNavController(this)
                    .popBackStack();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                textView2.setText(fileUri.getLastPathSegment());
                handleChooseFileResponse(requestCode, fileUri);
            }
        }
    }

    private void handleChooseFileResponse(int requestCode, Uri uri) {
        try {
            File file = new File(Objects.requireNonNull(FilePathUtil.getPath(requireContext(), uri)));
            switch (requestCode) {
                case PICK_IMAGE_FILE:
                    image = file;
                    Glide.with(this)
                            .load(uri)
                            .placeholder(R.drawable.grey_background)
                            .into(imageViewHeaderImage);
                    break;
                case PICK_ATTACHMENT_FILE:
                    attachment = file;
            }
        } catch (URISyntaxException e) {
            Log.e(CreateAssignmentFragment.class.getSimpleName(), "Error parsing uri", e);
        }
    }

    private void requestReadStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                WRITE_STORAGE_REQUEST_CODE);
    }

    private void showRequiresPermissionMessage() {
        baseActivity.showMessageSnackBar(
                "Grant storage access to attach items!", "GRANT PERMISSION",
                this::requestReadStoragePermission
                );
    }
}