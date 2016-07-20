package com.kms.cura.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.entity.HealthEntity;
import com.kms.cura.entity.SymptomEntity;
import com.kms.cura.view.activity.ConditionInfoActivity;
import com.kms.cura.view.activity.SymptomInfoActivity;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by linhtnvo on 7/25/2016.
 */
public class RejectApptDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String TITLE_KEY = "title";
    public static final String TYPE_KEY = "type";
    private TextView tvTitle;
    private EditText edtComment;
    private AlertDialog dialog;
    private int dialogPositive;
    public RejectApptDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View parent = inflater.inflate(R.layout.fragment_reject_dialog, null);
        AlertDialog.Builder builder = createDialogBuilder(parent);
        edtComment = (EditText) parent.findViewById(R.id.edtComment);
        dialog = builder.create();
        return dialog;
    }

    private AlertDialog.Builder createDialogBuilder(View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(parent);
        builder.setPositiveButton(getString(R.string.YES), null);
        builder.setNegativeButton(getString(R.string.no), this);
        return builder;
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dismiss();
        }
        else {
            // Reject request
        }
    }
}
