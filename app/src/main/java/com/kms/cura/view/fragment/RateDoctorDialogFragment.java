package com.kms.cura.view.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;

/**
 * Created by linhtnvo on 8/4/2016.
 */
public class RateDoctorDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, DialogInterface.OnShowListener {
    private EditText edtComment;
    private TextView txtDoctorName;
    private RatingBar ratingBar;
    private AlertDialog dialog;
    private int apptPos;
    private AppointmentEntity appt;
    public static String APPT_POSITION = "APPT_POSITION";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View parent = inflater.inflate(R.layout.rate_doctor_dialog_fragment, null);
        AlertDialog.Builder builder = createDialogBuilder(parent);
        initView(parent);
        loadView();
        dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    private void loadView() {
        Bundle bundle = getArguments();
        apptPos = bundle.getInt(APPT_POSITION, -1);
        if (apptPos == -1) {
            return;
        }
        appt = ((PatientUserEntity) CurrentUserProfile.getInstance().getEntity()).getAppointmentList().get(apptPos);
        txtDoctorName.setText(appt.getDoctorUserEntity().getName());
    }

    private AlertDialog.Builder createDialogBuilder(View parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(parent);
        builder.setPositiveButton(getString(R.string.Submit), this);
        builder.setNegativeButton(getString(R.string.cancel), this);
        return builder;
    }

    private void initView(View parent) {
        edtComment = (EditText) parent.findViewById(R.id.edtComment);
        txtDoctorName = (TextView) parent.findViewById(R.id.txtDoctorName);
        ratingBar = (RatingBar) parent.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(rating != 0.0f);
            }
        });
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        } else {
            //Submit Rating
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }
}
