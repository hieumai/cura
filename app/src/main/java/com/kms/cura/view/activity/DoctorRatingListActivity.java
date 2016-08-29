package com.kms.cura.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.json.JsonToEntityConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.view.adapter.RatingListAdapter;
import com.kms.cura.view.fragment.DoctorProfileFragment;

import java.util.List;

public class DoctorRatingListActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName;
    private RatingBar ratingBar;
    private ListView lvRating;
    private List<AppointmentEntity> appointmentEntities;
    private RatingListAdapter adapter;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_rating_list);
        DoctorUserEntity doctorUserEntity = JsonToEntityConverter.convertJsonStringToEntity(getIntent().getStringExtra(DoctorProfileFragment.DOCTOR_ENTITY), DoctorUserEntity.getDoctorEntityType());
        appointmentEntities = doctorUserEntity.getAppointmentList();
        tvName = (TextView) findViewById(R.id.tvDoctorName);
        tvName.setText(doctorUserEntity.getName());
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating((float) doctorUserEntity.getRating());
        ratingBar.setEnabled(false);
        btnBack = (ImageButton) findViewById(R.id.btnRatingListBack);
        btnBack.setOnClickListener(this);
        lvRating = (ListView) findViewById(R.id.lvRating);
        adapter = new RatingListAdapter(this, R.layout.rating_item, appointmentEntities);
        lvRating.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRatingListBack) {
            finish();
        }
    }
}
