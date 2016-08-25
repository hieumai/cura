package com.kms.cura.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.DayOfTheWeek;
import com.kms.cura.entity.OpeningHour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by linhtnvo on 6/23/2016.
 */
public class WorkingHourExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<HashMap<String, List<OpeningHour>>> allworkingHours;

    public WorkingHourExpandableAdapter(Context mContext, List<HashMap<String, List<OpeningHour>>> allworkingHours) {
        this.mContext = mContext;
        this.allworkingHours = allworkingHours;
    }

    @Override
    public int getGroupCount() {
        //The expandable listview has only one group so that return 1
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return DayOfTheWeek.values().length;
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
        //Because we only have one group, so the id alwys is 0
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
        final Comparator<OpeningHour> listComparator = new Comparator<OpeningHour>() {
            @Override
            public int compare(OpeningHour lhs, OpeningHour rhs) {
                if (lhs.getOpenTime().before(rhs.getOpenTime()) &&
                        lhs.getCloseTime().before(rhs.getCloseTime())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
        HashMap<String, List<OpeningHour>> workingHoursUnsort = allworkingHours.get(childPosition);
        List<Map.Entry<String,List<OpeningHour>>> workingHours = new ArrayList<Map.Entry<String,List<OpeningHour>>>(workingHoursUnsort.entrySet());
        Collections.sort(workingHours, new Comparator<Map.Entry<String, List<OpeningHour>>>() {
            @Override
            public int compare(Map.Entry<String, List<OpeningHour>> lhs, Map.Entry<String, List<OpeningHour>> rhs) {
                List<OpeningHour> lhsWH = lhs.getValue();
                List<OpeningHour> rhsWH = rhs.getValue();
                int lhsLength = lhsWH.size();
                int rhsLength = rhsWH.size();
                if (lhsLength > rhsLength){
                    return 1;
                }
                if (lhsLength < rhsLength){
                    return -1;
                }
                Collections.sort(lhsWH, listComparator);
                Collections.sort(rhsWH, listComparator);
                OpeningHour lhs1 = lhsWH.get(0);
                OpeningHour rhs1 = rhsWH.get(0);
                if (lhs1.getOpenTime().before(rhs1.getOpenTime()) &&
                        lhs1.getCloseTime().before(rhs1.getCloseTime())){
                    return -1;
                }
                return 1;
            }
        });
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.working_hour_adapter, parent, false);
        }

        TextView dayOftheWeek = (TextView) convertView.findViewById(R.id.txtDayofTheWeek);
        LinearLayout workingTimeLayout = (LinearLayout) convertView.findViewById(R.id.layoutWorkingTime);
        if (workingTimeLayout.getChildCount() > 0) {
            workingTimeLayout.removeAllViews();
        }
        dayOftheWeek.setText(DayOfTheWeek.getDayOfTheWeek(childPosition).toString().substring(0, 3));
        if (workingHours.size() == 0) {
            workingTimeLayout.addView(createWorkingHourDetails("N/A", null));
        } else {
            for (int j=0; j<workingHours.size(); ++j) {
                Map.Entry<String,List<OpeningHour>> entry = workingHours.get(j);
                workingTimeLayout.addView(createWorkingHourDetails(entry.getKey(), entry.getValue()));
            }
        }
        if (childPosition % 2 == 0) {
            dayOftheWeek.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey_2));
            workingTimeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey_2));
        }
        return convertView;
    }

    private View createWorkingHourDetails(String faciliyName, List<OpeningHour> openingHour) {
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.working_hour_details, null);
        TextView txtWHTime = (TextView) root.findViewById(R.id.txtWHTime);
        TextView txtWHFacility = (TextView) root.findViewById(R.id.txtWHFacility);
        if (openingHour == null) {
            txtWHTime.setVisibility(View.GONE);
            txtWHFacility.setText(faciliyName);
            txtWHFacility.setGravity(Gravity.CENTER);
        } else {
            StringBuilder facilityBuilder = new StringBuilder();
            StringBuilder timeBuilder = new StringBuilder();
            for (int i=0; i<openingHour.size(); ++i) {
                timeBuilder.append(openingHour.get(i).getTime() + " | ");
                facilityBuilder.append(faciliyName);
                if (i!=openingHour.size()-1){
                    timeBuilder.append("\n");
                    facilityBuilder.append("\n");
                }
            }
            txtWHTime.setText(timeBuilder.toString());
            txtWHFacility.setText(facilityBuilder.toString());
        }
        return root;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
