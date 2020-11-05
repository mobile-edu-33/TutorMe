package com.mobileedu33.tutorme.ui.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.IOUtils;
import com.mobileedu33.tutorme.R;
import com.mobileedu33.tutorme.data.models.Assignment;
import com.mobileedu33.tutorme.ui.activities.BaseActivity;
import com.mobileedu33.tutorme.ui.viewmodels.AssignmentsActivityViewModel;
import com.mobileedu33.tutorme.utils.FileUtils;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.hilt.android.AndroidEntryPoint;

import static android.app.Activity.RESULT_OK;

@AndroidEntryPoint
public class CreateAssignmentFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    // Request code for selecting a PDF document.
    private static final int PICK_ATTACHMENT_FILE = 2;
    private static final int PICK_IMAGE_FILE = 345;
    public static final int WRITE_STORAGE_REQUEST_CODE = 234;
    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextDescription)
    EditText editTextDescription;
    @BindView(R.id.txtAttachment)
    TextView txtAttachment;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.imageViewHeaderImage)
    ImageView imageViewHeaderImage;
    @BindView(R.id.editTextDate)
    EditText editTextDate;
    private Assignment assignment = new Assignment();
    private File attachment;
    private File image;
    private boolean setHasStorageReadPermission = true;
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
            setHasStorageReadPermission = false;
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
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String dueDate = editTextDate.getText().toString();

        if (title.isEmpty()) {
            baseActivity.showMessageSnackBar("Title cannot be empty. Please add a title!", null, null);
            return;
        } else if (description.isEmpty()) {
            baseActivity.showMessageSnackBar("Description cannot be empty. Please add description!", null, null);
            return;
        } else if (dueDate.isEmpty()) {
            baseActivity.showMessageSnackBar("Due date cannot be empty. Please enter due date", null, null);
            return;
        }
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
                handleChooseFileResponse(requestCode, fileUri);
            }
        }
    }

    private void handleChooseFileResponse(int requestCode, Uri uri) {
        String fileName = FileUtils.getFileDisplayName(uri, requireContext());
        File cache = new File(requireContext().getCacheDir(), fileName);

        try {
            ParcelFileDescriptor r = requireContext().getContentResolver().openFileDescriptor(uri, "r", null);
            FileInputStream in = new FileInputStream(r.getFileDescriptor());
            FileOutputStream out = new FileOutputStream(cache);
            IOUtils.copyStream(in, out);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Error copying file", e);
        }


        switch (requestCode) {
            case PICK_IMAGE_FILE:
                image = cache;
                Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.grey_background)
                        .into(imageViewHeaderImage);
                break;
            case PICK_ATTACHMENT_FILE:
                fileName = fileName.isEmpty() ? "Attachment 1" : fileName;
                txtAttachment.setText(fileName);
                attachment = cache;
        }
    }

    private void requestReadStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                WRITE_STORAGE_REQUEST_CODE);
    }

    private void showRequiresPermissionMessage() {
        baseActivity.showMessageSnackBar(
                "Grant storage access to attach items!", "GRANT PERMISSION",
                this::requestReadStoragePermission
        );
    }

    @OnClick(R.id.viewPickDate)
    public void onClick() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateString = "" + dayOfMonth + "-" + month + "-" + year;
        try {
            Date date = DateUtils.parseDate(dateString, Locale.getDefault(), "dd-MM-yyyy");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd MMM yyyy", Locale.getDefault());
            assignment.setDueDate(date.getTime());
            String dueDate = simpleDateFormat.format(date);
            assignment.setDisplayDueDate(dueDate);
            editTextDate.setText(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}