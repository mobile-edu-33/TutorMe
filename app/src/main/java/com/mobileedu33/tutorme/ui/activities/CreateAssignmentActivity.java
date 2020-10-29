package com.mobileedu33.tutorme.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.BaseUseCase;
import com.mobileedu33.tutorme.data.PublishAssignmentUseCase;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.utils.FilePathUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateAssignmentActivity extends BaseActivity {

    public static final int WRITE_STORAGE_REQUEST_CODE = 234;
    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextDescription)
    EditText editTextDescription;
    @BindView(R.id.textView2)
    TextView textView2;

    @Inject
    public PublishAssignmentUseCase publishAssignmentUseCase;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.imageViewHeaderImage)
    ImageView imageViewHeaderImage;
    private Assignment assignment = new Assignment();
    private File attachment;
    private File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_assignment);
        ButterKnife.bind(this);
        if (ContextCompat.
                checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        publishAssignmentUseCase.addListener(publishListener);
    }

    @OnClick(R.id.btnAddAttachment)
    public void onAddAttachment() {
        selectAttachment();
    }

    // Request code for selecting a PDF document.
    private static final int PICK_ATTACHMENT_FILE = 2;

    private void selectAttachment() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_ATTACHMENT_FILE);
    }

    private static final int PICK_IMAGE_FILE = 345;

    @OnClick(R.id.viewAddImage)
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FILE);
    }

    @OnClick(R.id.btn_publishAssignment)
    public void onPublish() {
        assignment.setTitle(editTextTitle.getText().toString());
        assignment.setDescription(editTextDescription.getText().toString());
        publishAssignmentUseCase.publish(assignment, attachment, image);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
            File file = new File(Objects.requireNonNull(FilePathUtil.getPath(this, uri)));
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
            Log.e(CreateAssignmentActivity.class.getSimpleName(), "Error parsing uri", e);
        }
    }

    private BaseUseCase.UseCaseListener<Void, String> publishListener = new BaseUseCase.UseCaseListener<Void, String>() {
        @Override
        public void onSuccess(Void aVoid) {
            showMessageSnackBar("Assignment successfully published!", null, null);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onError(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            showMessageSnackBar(s, null, null);
        }
    };
}