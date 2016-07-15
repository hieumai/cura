package com.kms.cura.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.UserController;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.model.Settings;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.utils.ImagePicker;

import java.io.ByteArrayOutputStream;

public class ProfilePictureActivity extends AppCompatActivity implements View.OnClickListener {
    private static String ACTIVITY_NAME = "Profile Picture";
    private static final int PICK_IMAGE = 505;
    private static final int MAX_IMAGE_SIZE = 25000000; //2.5 MB
    private ImageView profile;
    private Toolbar toolbar;
    private Button upload, save;
    private UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        user = CurrentUserProfile.getInstance().getEntity();
        initToolbar();
        DataUtils.loadProfile(user, (ImageView) findViewById(R.id.photo_profile));
        initButtons();
    }

    private void initButtons() {
        upload = (Button) findViewById(R.id.button_photo_upload);
        save = (Button) findViewById(R.id.button_photo_save);
        save.setOnClickListener(this);
        upload.setOnClickListener(this);
        save.setEnabled(false);
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
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP) {
                    requestPermission();
                }
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);

                if (bitmap != null && bitmap.getByteCount() > MAX_IMAGE_SIZE) {
                    Toast.makeText(this, R.string.ImageSizeError + " " + bitmap.getByteCount(), Toast.LENGTH_SHORT).show();
                } else if (bitmap != null && bitmap.getByteCount() != 0) {

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    UserController.savePhoto(user, encoded);
//                    profile.setImageBitmap(bitmap);
                    byte[] decodedString = Base64.decode(user.getImage().getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profile.setImageBitmap(decodedByte);
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

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Settings.MY_PERMISSION_READ_EXTERNAL_STORAGE);
    }
}
