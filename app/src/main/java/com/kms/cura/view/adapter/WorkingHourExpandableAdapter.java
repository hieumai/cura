package com.kms.cura.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.OpeningHour;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by linhtnvo on 6/23/2016.
 */
public class WorkingHourExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<HashMap<String,OpeningHour>> allworkingHours;

    public WorkingHourExpandableAdapter(Context mContext, List<HashMap<String, OpeningHour>> allworkingHours) {
        this.mContext = mContext;
        this.allworkingHours = allworkingHours;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 7;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_working_hour_layout, null);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        HashMap<String,OpeningHour> workingHours = allworkingHours.get(childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.working_hour_adapter, parent, false);
        }

        TextView dayOftheWeek = (TextView) convertView.findViewById(R.id.txtDayofTheWeek);
        LinearLayout workingTimeLayout = (LinearLayout) convertView.findViewById(R.id.layoutWorkingTime);
        if(workingTimeLayout.getChildCount() > 0){
            workingTimeLayout.removeAllViews();
        }
        dayOftheWeek.setText(DayOfTheWeek.getDayOfTheWeek(childPosition).toString().substring(0,3));
        Set<String> facilitiesName = workingHours.keySet();
        for(String facilityName : facilitiesName){
            workingTimeLayout.addView(createWorkingHourDetails(facilityName,workingHours.get(facilityName)));
        }
        if(childPosition % 2 ==0){
            dayOftheWeek.setBackgroundColor(R.color.light_grey);
            workingTimeLayout.setBackgroundColor(R.color.light_grey);
        }
        return convertView;
    }

    private TextView createWorkingHourDetails(String faciliyName, OpeningHour openingHour){
        TextView textView = new TextView(mContext);
        StringBuilder builder = new StringBuilder();
        builder.append(openingHour.getTime());
        builder.append(" | ");
        builder.append(faciliyName);
        textView.setText(builder.toString());
        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
