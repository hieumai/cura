package com.kms.cura.view.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.entity.OpeningHour;
import com.kms.cura.entity.json.EntityToJsonConverter;
import com.kms.cura.view.ListViewRemoveItemListener;
import com.kms.cura.view.activity.DoctorWorkingHourSettingsActivity;
import com.kms.cura.view.activity.SettingEditWorkingHourActivity;
import com.kms.cura.view.activity.SettingWorkingHourActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by toanbnguyen on 8/18/2016.
 */
public class SettingWorkingHourAdapter extends BaseAdapter implements DialogInterface.OnClickListener {

    private Activity context;
    private int resource;
    private HashMap<String, List<OpeningHour>> listWeekDay;
    private List<OpeningHour> openingHourList;
    private List<String> facilityNames;
    private ListViewRemoveItemListener removeItemListener;
    private int removePosition;

    public SettingWorkingHourAdapter(Activity context, int resource, HashMap<String, List<OpeningHour>> listWeekDay, ListViewRemoveItemListener removeItemListener) {
        this.context = context;
        this.resource = resource;
        this.listWeekDay = listWeekDay;
        this.removeItemListener = removeItemListener;
        openingHourList = new ArrayList<>();
        facilityNames = new ArrayList<>();
        Set<String> stringSet = listWeekDay.keySet();
        for (String string : stringSet) {
            List<OpeningHour> list = listWeekDay.get(string);
            openingHourList.addAll(list);
            for (OpeningHour hour : list) {
                facilityNames.add(string);
            }
        }
    }

    @Override
    public int getCount() {
        return openingHourList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }
        setUpView(convertView, position);
        return convertView;
    }

    private void setUpView(final View convertView, final int position) {
        TextView tvName = (TextView) convertView.findViewById(R.id.tvFacilityName);
        tvName.setText(facilityNames.get(position));
        TextView tvHour = (TextView) convertView.findViewById(R.id.tvHours);
        OpeningHour hour = openingHourList.get(position);
        tvHour.setText(hour.getTime());
        Button btnRemove = (Button) convertView.findViewById(R.id.btnWorkingHourRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePosition = position;
                showRemoveDialog();
            }
        });
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.btnEditWorkingHour);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingEditWorkingHourActivity.class);
                intent.putExtra(SettingWorkingHourActivity.MODE_KEY, SettingWorkingHourActivity.EDIT_KEY);
                intent.putStringArrayListExtra(SettingWorkingHourActivity.FACILITIES_KEY, getWorkingFacility());
                intent.putExtra(SettingWorkingHourActivity.FACILITY_KEY, facilityNames.get(position));
                intent.putExtra(SettingWorkingHourActivity.EDIT_KEY, position);
                intent.putExtra(SettingWorkingHourActivity.DAYOFWEEK_KEY, openingHourList.get(position).getDayOfTheWeek().getCode());
                intent.putExtra(SettingWorkingHourActivity.START_KEY, openingHourList.get(position).getOpenTime().toString());
                intent.putExtra(SettingWorkingHourActivity.END_KEY, openingHourList.get(position).getCloseTime().toString());
                intent.putExtra(SettingWorkingHourActivity.POSITION_KEY, position);
                context.startActivityForResult(intent, DoctorWorkingHourSettingsActivity.EDITTED_REQUEST_CODE);
            }
        });
    }

    private void showRemoveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.woking_remove);
        builder.setPositiveButton(R.string.yes, this);
        builder.setNegativeButton(R.string.no, this);
        builder.create().show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            removeItemListener.removeItem(facilityNames.get(removePosition), openingHourList.get(removePosition));
        }
    }

    public void setData(HashMap<String, List<OpeningHour>> listWeekDay) {
        this.listWeekDay = listWeekDay;
        openingHourList = new ArrayList<>();
        facilityNames = new ArrayList<>();
        Set<String> stringSet = listWeekDay.keySet();
        for (String string : stringSet) {
            List<OpeningHour> list = listWeekDay.get(string);
            openingHourList.addAll(list);
            for (OpeningHour hour : list) {
                facilityNames.add(string);
            }
        }
    }

    private ArrayList<String> getWorkingFacility() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String name : listWeekDay.keySet()) {
            arrayList.add(name);
        }
        return arrayList;
    }

    public void removePosition(int position) {
        removeItemListener.removeItem(facilityNames.get(position), openingHourList.get(position));
    }
}
