package com.kms.cura.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.activity.MessageThreadActivity;
import com.kms.cura.view.activity.SearchActivity;
import com.kms.cura.view.activity.ViewDoctorProfileActivity;
import com.kms.cura.view.activity.ViewPatientProfileActivity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by toanbnguyen on 7/26/2016.
 */
public class MessageAdapter extends BaseAdapter {

    private ArrayList<MessageEntity> messageEntities;
    private Activity activity;
    private int resource;
    private ArrayList<Boolean> selected;

    public MessageAdapter(Activity activity, int resource, ArrayList objects) {
        this.activity = activity;
        this.resource = resource;
        messageEntities = objects;
        selected = new ArrayList<>();
        for (int i = 0; i < messageEntities.size(); ++i) {
            selected.add(false);
        }
    }

    @Override
    public int getCount() {
        return messageEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return messageEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(resource, parent, false);
        }
        setUpView(convertView, position);
        return convertView;
    }

    private void setUpView(final View convertView, final int position) {
        final TextView tvName = (TextView) convertView.findViewById(R.id.tvMesName);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvMesTime);
        final TextView tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
        tvName.setText(messageEntities.get(position).getSender().getName());
        tvTime.setText(toStringTime(messageEntities.get(position).getTimeSent()));
        tvMessage.setText(messageEntities.get(position).getMessage());
        setColor(convertView, position);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentUserProfile.getInstance().getEntity().getName().equals(tvName.getText().toString()) || ((MessageThreadActivity) activity).isMultiMode()) {
                    ListView listView = ((MessageThreadActivity) activity).getLvMessage();
                    listView.performItemClick(convertView, position, getItemId(position));
                    return;
                }
                MessageEntity entity = messageEntities.get(position);
                if (entity.isSenderDoctor()) {
                    DoctorUserEntity doctorUserEntity = (DoctorUserEntity) entity.getSender();
                    Intent intent = new Intent(activity, ViewDoctorProfileActivity.class);
                    intent.putExtra(SearchActivity.DOCTOR_SELECTED, EntityToJsonConverter.convertEntityToJson(doctorUserEntity).toString());
                    activity.startActivity(intent);
                } else {
                    PatientUserEntity patientUserEntity = (PatientUserEntity) entity.getSender();
                    Intent intent = new Intent(activity, ViewPatientProfileActivity.class);
                    intent.putExtra(ViewPatientProfileActivity.PATIENT_KEY, EntityToJsonConverter.convertEntityToJson(patientUserEntity).toString());
                    activity.startActivity(intent);
                }

            }
        });
        tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                convertView.performLongClick();
                return true;
            }
        });
    }

    private void setColor(View convertView, int position) {
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layoutMessageItem);
        if (selected.get(position)) {
            layout.setBackgroundColor(ContextCompat.getColor(activity, R.color.grey));
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        }
    }

    public void setSelected(int position, boolean value) {
        selected.set(position, value);
    }

    public void clearSelection() {
        for (int i = 0; i < messageEntities.size(); ++i) {
            selected.set(i, false);
        }
    }

    public int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < messageEntities.size(); ++i) {
            if (selected.get(i)) {
                count++;
            }
        }
        return count;
    }

    public boolean isSelected(int position) {
        return selected.get(position);
    }

    public void setData(ArrayList<MessageEntity> messageEntities) {
        this.messageEntities = messageEntities;
        selected.clear();
        for (int i = 0; i < messageEntities.size(); ++i) {
            selected.add(false);
        }
    }

    private String toStringTime(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        StringBuilder builder = new StringBuilder();
        addZero(builder, calendar.get(Calendar.HOUR_OF_DAY));
        builder.append((calendar.get(Calendar.HOUR_OF_DAY) - 1) + ":");
        addZero(builder, calendar.get(Calendar.MINUTE));
        builder.append(calendar.get(Calendar.MINUTE) + " - ");
        addZero(builder, calendar.get(Calendar.DAY_OF_MONTH));
        builder.append(calendar.get(Calendar.DAY_OF_MONTH) + "/");
        addZero(builder, calendar.get(Calendar.MONTH));
        builder.append((calendar.get(Calendar.MONTH) + 1) + "/");
        builder.append(calendar.get(Calendar.YEAR));
        return builder.toString();
    }

    private void addZero(StringBuilder builder, int value) {
        if (value < 10) {
            builder.append("0");
        }
    }
}
