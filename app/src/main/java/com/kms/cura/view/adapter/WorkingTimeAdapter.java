package com.kms.cura.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.OpeningHour;

import java.util.ArrayList;

/**
 * Created by toanbnguyen on 8/12/2016.
 */
public class WorkingTimeAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private ArrayList<OpeningHour> openingHours;

    public WorkingTimeAdapter(Context context, int resource, ArrayList objects) {
        this.context = context;
        this.resource = resource;
        this.openingHours = objects;
    }

    @Override
    public int getCount() {
        return openingHours.size();
    }

    @Override
    public Object getItem(int position) {
        return openingHours.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        setupTextView(convertView, position);
        return convertView;
    }

    private void setupTextView(View convertView, int position) {
        TextView tvDay = (TextView) convertView.findViewById(R.id.tvDay);
        tvDay.setText(openingHours.get(position).getDayOfTheWeek().toString().substring(0,3));
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        tvTime.setText(openingHours.get(position).getTime());
    }
}