package com.kms.cura.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.view.UpdateSpinner;

import java.util.ArrayList;

/**
 * Created by toanbnguyen on 5/30/2016.
 */
public class CheckBoxAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private ArrayList<String> checkList;
    private boolean[] checked;
    private UpdateSpinner updateSpinner;

    public CheckBoxAdapter(Context context, int resource, ArrayList objects, boolean[] checked, UpdateSpinner updateSpinner) {
        this.context = context;
        this.resource = resource;
        this.checkList = objects;
        if (checked != null)
            this.checked = checked;
        else {
            this.checked = new boolean[checkList.size() - 1];
            for (int i = 0; i < checkList.size() - 1; ++i)
                this.checked[i] = false;
        }
        this.updateSpinner = updateSpinner;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.check_box_item, parent, false);
        }
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        final TextView tv = (TextView) convertView.findViewById(R.id.tvHint);
        modifyViewItem(cb, tv, position);
        return convertView;
    }

    private void modifyViewItem(CheckBox cb, TextView tv, final int position) {
        String name = checkList.get(position);
        // if it is not the hint (the hint is the last element of the array)
        if (position < checkList.size() - 1) {
            cb.setText(name);
            tv.setVisibility(View.INVISIBLE);
            initCheckBox(cb, position);
        }
        // if it is the hint text, only view the Textview
        if (position == checkList.size() - 1) {
            String show = getSelectedStringShow();
            if (show.equals(""))
                tv.setText(name);
            else
                tv.setText(show);
            cb.setVisibility(View.INVISIBLE);
        }
    }

    private void initCheckBox(CheckBox cb, final int position) {
        if (checked[position])
            cb.setChecked(true);
        if (cb.isChecked() && !checked[position])
            cb.setChecked(false);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked[position] = !checked[position];
                updateSpinner.callBackUpdateSpinner();
            }
        });
    }

    public ArrayList<String> getSelectedString() {
        ArrayList<String> select = new ArrayList<>();
        for (int i = 0; i < checkList.size() - 1; ++i) {
            if (checked[i])
                select.add(checkList.get(i));
        }
        return select;
    }

    private String getSelectedStringShow() {
        StringBuilder builder = new StringBuilder("");
        ArrayList<String> select = getSelectedString();
        for (int i = 0; i < select.size() - 1; ++i) {
            builder.append(select.get(i) + ", ");
        }
        if (!select.isEmpty())
            builder.append(select.get(select.size() - 1));
        return builder.toString();
    }

    public boolean[] getSelectedBoolean() {
        return checked;
    }

    public void setSelectedBoolean(boolean[] b) {
        this.checked = b;
    }
}