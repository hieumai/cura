package com.kms.cura.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kms.cura.R;

public class ProfilePictureActivity extends AppCompatActivity {
    private static String ACTIVITY_NAME = "Profile Picture";
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
}
