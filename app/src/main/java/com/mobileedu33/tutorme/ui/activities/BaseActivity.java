package com.mobileedu33.tutorme.ui.activities;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.mobileedu33.tutorme.R;

public class BaseActivity extends AppCompatActivity {

    public void showMessageSnackBar(@StringRes int stringResId) {
        Snackbar.make(findViewById(android.R.id.content), stringResId, Snackbar.LENGTH_SHORT).show();
    }

    public void showMessageSnackBar(String string) {
        Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_SHORT).show();
    }

}
