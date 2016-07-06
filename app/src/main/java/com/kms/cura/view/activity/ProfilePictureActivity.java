package com.kms.cura.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.utils.ImagePicker;

public class ProfilePictureActivity extends AppCompatActivity implements View.OnClickListener {
    private static String ACTIVITY_NAME = "Profile Picture";
    private static final int PICK_IMAGE = 505;
    private static final int MAX_IMAGE_SIZE = 2500000; //2.5 MB
    private ImageView profile;
    private Toolbar toolbar;
    private Button upload, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        initToolbar();
        initProfilePhoto();
        initButtons();
    }

    private void initButtons() {
        upload = (Button) findViewById(R.id.button_photo_upload);
        save = (Button) findViewById(R.id.button_photo_save);
        save.setOnClickListener(this);
        upload.setOnClickListener(this);
        save.setEnabled(false);
    }

    private void initProfilePhoto() {
        profile = (ImageView) findViewById(R.id.photo_profile);
        profile.setImageResource(R.drawable.profile_anon128);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.photo_profile_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ACTIVITY_NAME);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                if (bitmap != null && bitmap.getByteCount() > MAX_IMAGE_SIZE) {
                    Toast.makeText(this, R.string.ImageSizeError, Toast.LENGTH_SHORT).show();
                } else if (bitmap != null && bitmap.getByteCount() != 0) {
                    profile.setImageBitmap(bitmap);
                    save.setEnabled(true);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_photo_upload) {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
            startActivityForResult(chooseImageIntent, PICK_IMAGE);
        }
    }
}
