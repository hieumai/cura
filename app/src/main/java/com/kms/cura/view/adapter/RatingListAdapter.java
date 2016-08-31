package com.kms.cura.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 8/25/2016.
 */
public class RatingListAdapter extends BaseAdapter {

    private final int MAX_LINE = 3;
    private Context context;
    private int resource;
    private List<AppointmentEntity> appointmentEntities;
    private ArrayList<Boolean> expand;

    public RatingListAdapter(Context context, int resource, List<AppointmentEntity> appointmentAll) {
        this.context = context;
        this.resource = resource;
        appointmentEntities = new ArrayList<>();
        expand = new ArrayList<>();
        for (AppointmentEntity entity : appointmentAll) {
            if (entity.getRate() != 0) {
                appointmentEntities.add(entity);
                expand.add(false);
            }
        }
    }

    @Override
    public int getCount() {
        return appointmentEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        setUpView(convertView, position);
        return convertView;
    }

    private void setUpView(View convertView, int position) {
        setUpRatingBar(convertView, position);
        setUpTextView(convertView, R.id.tvDate, appointmentEntities.get(position).getApptDay().toString());
        setUpTextView(convertView, R.id.tvRater, appointmentEntities.get(position).getPatientUserEntity().getName());
        setUpComment(convertView, position);
    }

    private void setUpComment(View convertView, final int position) {
        final AppointmentEntity entity = appointmentEntities.get(position);
        final TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        final TextView tvSeeMore = (TextView) convertView.findViewById(R.id.btnSeeMore);
        String comment = appointmentEntities.get(position).getRate_comment();
        if (comment == null || comment.isEmpty()) {
            tvComment.setVisibility(View.GONE);
            tvSeeMore.setVisibility(View.GONE);
            return;
        }
        tvComment.setText(comment);
        tvComment.post(new Runnable() {
            @Override
            public void run() {
                if (tvComment.getLineCount() > MAX_LINE) {
                    tvComment.setMaxLines(MAX_LINE);
                    tvSeeMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createDialog(entity.getPatientUserEntity().getName(), entity.getRate_comment()).show();
                        }
                    });
                } else {
                    tvSeeMore.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setUpTextView(View convertView, int id, String s) {
        ((TextView) convertView.findViewById(id)).setText(s);
    }

    private void setUpRatingBar(View convertView, int position) {
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBarItem);
        ratingBar.setRating(appointmentEntities.get(position).getRate());
        ratingBar.setEnabled(false);
    }

    private AlertDialog createDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton(R.string.Ok, null);
        return builder.create();
    }
}
