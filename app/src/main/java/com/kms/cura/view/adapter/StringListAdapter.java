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
public class StringListAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private ArrayList<String> checkList;
    private int checked;
    public StringListAdapter(Context context, int resource, ArrayList objects, int checked) {
        this.context = context;
        this.resource = resource;
        this.checkList = objects;
        this.checked = checked;
    }

    @Override
    public int getCount() {
        return checkList.size() - 1; // not to include the hint in the list
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
            convertView = LayoutInflater.from(context).inflate(R.layout.string_list_item, parent, false);
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