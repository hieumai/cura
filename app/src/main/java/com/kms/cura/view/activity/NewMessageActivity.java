package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.MessageController;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.InputUtils;

import java.sql.Timestamp;
import java.util.Calendar;

public class NewMessageActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener, DialogInterface.OnClickListener {

    public final static String KEY_SENDER = "sender";
    public final static String KEY_PATIENT = "patient";
    public final static String KEY_DOCTOR = "doctor";
    public final static String KEY_RECEIVER_ID = "doctor_id";
    public final static String KEY_RECEIVER_NAME = "doctor_name";
    private ImageButton btnCancel;
    private TextView tvName;
    private EditText edtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        modifyToolbar();
        setUpButton();
        setUpView();
    }

    private void setUpView() {
        tvName = (TextView) findViewById(R.id.tvSendName);
        tvName.setText(getIntent().getStringExtra(KEY_RECEIVER_NAME));
        edtMessage = (EditText) findViewById(R.id.edtMessage);
    }

    private void setUpButton() {
        btnCancel = (ImageButton) findViewById(R.id.btnNewMessageBack);
        btnCancel.setOnClickListener(this);
    }

    private void modifyToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbNewMessage);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_send_message);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.btnSendMessage) {
            if (edtMessage.getText().toString().equals("")) {
                Toast.makeText(this, getString(R.string.message_blank), Toast.LENGTH_SHORT).show();
            } else {
                createDialog(getString(R.string.new_message_warning)).show();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNewMessageBack) {
            finish();
        }
    }

    private MessageEntity createMessage(String message) {
        Intent intent = getIntent();
        UserEntity sender = CurrentUserProfile.getInstance().getEntity();
        UserEntity receiver = null;
        switch (intent.getStringExtra(KEY_SENDER)) {
            case KEY_PATIENT:
                receiver = new DoctorUserEntity(intent.getStringExtra(KEY_RECEIVER_ID), intent.getStringExtra(KEY_RECEIVER_NAME));
                break;
            case KEY_DOCTOR:
                receiver = new PatientUserEntity(intent.getStringExtra(KEY_RECEIVER_ID), intent.getStringExtra(KEY_RECEIVER_NAME));
                break;
        }
        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        return new MessageEntity(sender, receiver, timestamp, InputUtils.covertValidStringForSQL(message));
    }

    private void sendMessage(final MessageEntity entity) {
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(NewMessageActivity.this);
                pDialog.setMessage(getString(R.string.sending));
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    MessageController.insertMessage(entity);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                if (exception != null) {
                    ErrorController.showDialog(NewMessageActivity.this, "Error : " + exception.getMessage());
                } else {
                    Toast.makeText(NewMessageActivity.this, R.string.sent, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }.execute();
    }

    private AlertDialog createDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.yes), this);
        builder.setNegativeButton(getString(R.string.no), this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            MessageEntity messageEntity = createMessage(edtMessage.getText().toString());
            sendMessage(messageEntity);
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }
}
