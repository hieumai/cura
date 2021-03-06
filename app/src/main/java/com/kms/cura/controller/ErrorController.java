package com.kms.cura.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.kms.cura.R;
import com.kms.cura.view.ReloadData;

/**
 * Created by linhtnvo on 6/6/2016.
 */
public class ErrorController {
    public static void showDialog(Context mContext, String message) {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        if (message.contains(":")) {
            message = message.split(":")[1];
        }
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (isActivityNotFinishing(mContext)) {
            dialog.show();
        }
    }

    public static void showSystemDialog(Context mContext, String message) {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        if (message.contains(":")) {
            message = message.split(":")[1];
        }
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static void showDialogRefresh(Context mContext, String message, final ReloadData reloadData) {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        if (message.contains(":")) {
            message = message.split(":")[1];
        }
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reloadData.callBackReload();
                dialog.dismiss();
            }
        });
        if (isActivityNotFinishing(mContext)) {
            dialog.show();
        }
    }

    private static boolean isActivityNotFinishing(Context mContext) {
        return !((Activity) mContext).isFinishing();
    }
}
