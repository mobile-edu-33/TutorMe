package com.mobileedu33.tutorme.utils;

import android.app.Activity;
import android.os.Build;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.appcompat.app.AppCompatActivity;

public class HideActionBarUtil {
    public static void hideActionBar(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController controller = activity.getWindow().getInsetsController();

            if (controller != null)
                controller.hide(WindowInsets.Type.navigationBars());
        }
        else {
            activity.getSupportActionBar().hide();
        }
    }
}
