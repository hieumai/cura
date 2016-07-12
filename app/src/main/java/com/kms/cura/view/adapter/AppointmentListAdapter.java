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
import com.kms.cura.view.fragment.DummyAppointment;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by linhtnvo on 7/11/2016.
 */
public class AppointmentListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private Context mContext;
    private List<DummyAppointment> appointments;
    private int[] mSectionIndices;
    private String[] mSectionTitle;
    private LayoutInflater mInflater;

    public AppointmentListAdapter(Context mContext, List<DummyAppointment> appointments) {
        this.mContext = mContext;
        this.appointments = appointments;
        mInflater = LayoutInflater.from(mContext);
        mSectionIndices = getSectionIndices();
        mSectionTitle = getSectionTitle();

    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        Date date = appointments.get(0).getDate();
        sectionIndices.add(0);
        for (int i = 1; i < appointments.size(); i++) {
            if (appointments.get(i).getDate().getTime() != date.getTime()) {
                date = appointments.get(i).getDate();
                sectionIndices.add(i);
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
            dateHeader[i] = getDate(appointments.get(mSectionIndices[i]).getDate());
        }
        return dateHeader;
    }

    public String getDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String dayOftheWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        StringBuilder builder = new StringBuilder();
        builder.append(dayOftheWeek);
        builder.append(", ");
        builder.append(month_name);
        builder.append(" ");
        builder.append(calendar.DAY_OF_MONTH);
        builder.append(", ");
        builder.append(calendar.YEAR);
        return builder.toString();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.app_list_header, parent, false);
        }
        TextView header = (TextView) convertView.findViewById(R.id.txtDateHeader);
        header.setText(getDate(appointments.get(position).getDate()));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return appointments.get(position).getDate().getTime();
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
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.appt_list_item_adapter,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        DummyAppointment appointment = appointments.get(position);
        holder.doctorName.setText(appointment.getDoctorName());
        holder.facilityName.setText(appointment.getFacilityName());
        holder.apptTime.setText(appointment.getTime());
        holder.tag.setText(appointment.getStatusName());
        Integer tagId = getTagId(appointment.getStatus());
        if(tagId == null){
            holder.tag.setVisibility(View.GONE);
        }
        else{
            holder.tag.setBackgroundResource(tagId);
        }
        return convertView;
    }

    private Integer getTagId(int status){
        switch (status){
            case 0:
                return R.drawable.pending_tag;
            case 2:
                return R.drawable.reject_tag;
            case 3:
                return R.drawable.cancel_tag;
            default:
                return null;
        }
    }

    @Override
    public Object[] getSections() {
        return mSectionTitle;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if(mSectionIndices.length == 0){
            return  0;
        }
        if(sectionIndex >= mSectionIndices.length){
            sectionIndex = mSectionIndices.length-1;
        }
        else if(sectionIndex < 0){
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

    private class ViewHolder{
        private TextView doctorName;
        private TextView facilityName;
        private TextView apptTime;
        private Button tag;
        private View root;

        public ViewHolder(View root) {
            this.root = root;
            doctorName = (TextView) root.findViewById(R.id.txtDoctorName);
            facilityName = (TextView) root.findViewById(R.id.txtFacilityName);
            apptTime = (TextView) root.findViewById(R.id.txtApptTime);
            tag = (Button) root.findViewById(R.id.btnTag);
        }
    }
}
