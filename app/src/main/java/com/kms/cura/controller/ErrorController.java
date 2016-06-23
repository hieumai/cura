package com.kms.cura.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
        dialog.show();
    }

}
