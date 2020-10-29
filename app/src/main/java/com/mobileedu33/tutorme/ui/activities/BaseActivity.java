package com.mobileedu33.tutorme.ui.activities;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity {

    public void showMessageSnackBar(@StringRes int stringResId, String actionTitle, Runnable onActionClicked) {

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), stringResId, Snackbar.LENGTH_SHORT);
        if(actionTitle != null && onActionClicked != null) {
            snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.setAction(actionTitle, v -> {
                onActionClicked.run();
                snackbar.dismiss();
            });
        }
        snackbar.show();
    }

    public void showMessageSnackBar(String string, String actionTitle, Runnable onActionClicked) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), string, Snackbar.LENGTH_SHORT);
        if(actionTitle != null && onActionClicked != null) {
            snackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
            snackbar.setAction(actionTitle, v -> {
                onActionClicked.run();
                snackbar.dismiss();
            });
        }
        snackbar.show();
    }
}
