package com.tuochebang.user.view.citypicker.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static Toast mToast;

    public static void showToast(Context context, String message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showToast(Context context, int messageResId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, messageResId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(messageResId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
