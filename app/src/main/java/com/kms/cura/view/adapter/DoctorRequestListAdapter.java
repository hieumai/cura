package com.kms.cura.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by linhtnvo on 7/13/2016.
 */
public class DoctorRequestListAdapter extends BaseAdapter implements StickyListHeadersAdapter,SectionIndexer {
    private Context mContext;
    private List<AppointmentEntity> appointments;
    private int[] mSectionIndices;
    private String[] mSectionTitle;
    private LayoutInflater mInflater;

    public DoctorRequestListAdapter(Context mContext, List<AppointmentEntity> appointments) {
        this.mContext = mContext;
        this.appointments = appointments;
        mInflater = LayoutInflater.from(mContext);
        mSectionIndices = getSectionIndices();
        mSectionTitle = getSectionTitle();

    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        if(appointments.size() > 0) {
            Date date = appointments.get(0).getApptDay();
            sectionIndices.add(0);
            for (int i = 1; i < appointments.size(); i++) {
                if (appointments.get(i).getApptDay().getTime() != date.getTime()) {
                    date = appointments.get(i).getApptDay();
                    sectionIndices.add(i);
                }
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private String[] getSectionTitle() {
        String[] dateHeader = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            dateHeader[i] = getDate(appointments.get(mSectionIndices[i]).getApptDay());
        }
        return dateHeader;
    }

    public String getDate(Date date) {
        Calendar calendar = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDate());
        String dayOftheWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        StringBuilder builder = new StringBuilder();
        builder.append(dayOftheWeek);
        builder.append(", ");
        builder.append(month_name);
        builder.append(" ");
        builder.append(date.getDate());
        builder.append(", ");
        builder.append(date.getYear()+1900);
        return builder.toString();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.app_list_header, parent, false);
        }
        TextView header = (TextView) convertView.findViewById(R.id.txtDateHeader);
        header.setText(getDate(appointments.get(position).getApptDay()));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return appointments.get(position).getApptDay().getTime();
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.request_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppointmentEntity appointment = appointments.get(position);
        loadData(holder,appointment);
        return convertView;
    }

    private void loadData(ViewHolder holder, AppointmentEntity appointment){
        holder.patientName.setText(appointment.getPatientUserEntity().getName());
        holder.facilityName.setText(appointment.getFacilityEntity().getName());
        holder.apptTime.setText(getAppointmentTime(appointment));
    }

    private String getAppointmentTime(AppointmentEntity appointment){
        StringBuilder builder = new StringBuilder();
        builder.append(appointment.getStartTime().toString().substring(0,5));
        builder.append(" - ");
        builder.append(appointment.getEndTime().toString().substring(0,5));
        return builder.toString();
    }

    @Override
    public Object[] getSections() {
        return mSectionTitle;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (mSectionIndices.length == 0) {
            return 0;
        }
        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    private class ViewHolder {
        private TextView patientName;
        private TextView facilityName;
        private TextView apptTime;

        public ViewHolder(View root) {
            patientName = (TextView) root.findViewById(R.id.txtPatientName);
            facilityName = (TextView) root.findViewById(R.id.txtFacilityName);
            apptTime = (TextView) root.findViewById(R.id.txtApptTime);
        }
    }
}
