package com.kms.cura.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.view.ListViewRemoveItemListener;

import java.util.List;

/**
 * Created by toanbnguyen on 8/18/2016.
 */
public class SpecialitySettingAdapter extends BaseAdapter implements DialogInterface.OnClickListener {

    private List<SpecialityEntity> specialities;
    private int resource;
    private Context context;
    private ListViewRemoveItemListener removeItemListener;
    private int removePosition;

    public SpecialitySettingAdapter(List<SpecialityEntity> specialities, int resource, Context context, ListViewRemoveItemListener removeItemListener) {
        this.specialities = specialities;
        this.resource = resource;
        this.context = context;
        this.removeItemListener = removeItemListener;
    }

    @Override
    public int getCount() {
        return specialities.size();
    }

    @Override
    public Object getItem(int position) {
        return specialities.get(position);
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
        setUpView(convertView, position);
        return convertView;
    }

    private void setUpView(View convertView, final int position) {
        TextView tvName = (TextView) convertView.findViewById(R.id.tvSpecialityName);
        tvName.setText(specialities.get(position).getName());
        Button btnRemove = (Button) convertView.findViewById(R.id.btnSpecialityRemove);
        if (removeItemListener != null) {
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removePosition = position;
                    showRemoveDialog();
                }
            });
        } else {
            btnRemove.setVisibility(View.GONE);
        }
    }

    private void showRemoveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.remove_speciality);
        builder.setPositiveButton(R.string.yes, this);
        builder.setNegativeButton(R.string.no, this);
        builder.create().show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            removeItemListener.removeItem(removePosition);
        }
    }

    public void setData(List<SpecialityEntity> specialities) {
        this.specialities = specialities;
    }
}
