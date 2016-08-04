package com.kms.cura.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;

import java.util.List;

/**
 * Created by linhtnvo on 7/21/2016.
 */
public class DoctorAppointmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<AppointmentEntity> listAppts;

    public DoctorAppointmentAdapter(Context mContext, List<AppointmentEntity> listAppts) {
        this.mContext = mContext;
        this.listAppts = listAppts;
    }

    @Override
    public int getCount() {
        return listAppts.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.doctor_appt_item_adapter, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppointmentEntity entity = listAppts.get(position);
        holder.txtStartTime = loadText(convertView, R.id.txtApptStartTime, entity.getStartTime().toString().substring(0, 5));
        holder.txtEndTime = loadText(convertView, R.id.txtApptEndTime, entity.getEndTime().toString().substring(0, 5));
        holder.txtPatientName = (loadText(convertView, R.id.txtPatientName, entity.getPatientUserEntity().getName()));
        holder.status = loadStatus(convertView, entity.getStatus());
        return convertView;
    }

    private ImageView loadStatus(View root, int status) {
        ImageView imageView = (ImageView) root.findViewById(R.id.ivStatus);
        switch (status) {
            case AppointmentEntity.COMPLETED_STT:
                imageView.setBackgroundResource(R.drawable.circle_complete_tag);
                break;
            case AppointmentEntity.INCOMPLETED_STT:
                imageView.setBackgroundResource(R.drawable.circle_incomplete_tag);
                break;
            case AppointmentEntity.DOCTOR_CANCEL_STT:
                imageView.setBackgroundResource(R.drawable.circle_cancel_tag);
                break;
            default:
                imageView.setVisibility(View.GONE);
                break;
        }
        return imageView;
    }

    private TextView loadText(View root, int id, String src) {
        TextView textView = (TextView) root.findViewById(id);
        textView.setText(src);
        return textView;
    }

    private class ViewHolder {
        TextView txtStartTime, txtEndTime, txtPatientName;
        ImageView status;
    }

    public List<AppointmentEntity> getListAppts() {
        return listAppts;
    }
}
