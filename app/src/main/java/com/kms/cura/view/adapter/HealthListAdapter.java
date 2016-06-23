package com.kms.cura.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kms.cura.R;

import java.util.ArrayList;

/**
 * Created by toanbnguyen on 6/14/2016.
 */
public class HealthListAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private ArrayList<String> health;

    public HealthListAdapter(Context context, int resource, ArrayList health) {
        this.context = context;
        this.resource = resource;
        this.health = health;
    }
    @Override
    public int getCount() {
        return health.size();
    }

    @Override
    public Object getItem(int position) {
        return health.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tvHealthItem);
        tv.setText(health.get(position));
        return convertView;
    }

    public void resetAdapter(ArrayList<String> health) {
        this.health = health;
    }
}
