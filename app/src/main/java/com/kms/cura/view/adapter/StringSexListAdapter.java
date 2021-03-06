package com.kms.cura.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kms.cura.R;

import java.util.ArrayList;

/**
 * Created by toanbnguyen on 6/2/2016.
 */
public class StringSexListAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private ArrayList<String> checkList;
    public StringSexListAdapter(Context context, int resource, ArrayList objects) {
        this.context = context;
        this.resource = resource;
        this.checkList = objects;
    }

    @Override
    public int getCount() {
        return checkList.size();
    }

    @Override
    public Object getItem(int position) {
        return checkList.get(position);
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

    private void setupTextView(View convertView, final int position) {
        String name = checkList.get(position);
        TextView tv = (TextView) convertView.findViewById(R.id.tvStringItem);
        tv.setText(name);
    }
}