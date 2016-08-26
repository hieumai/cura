package com.kms.cura.view.adapter;

/**
 * Created by duyhnguyen on 6/14/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.SpecialityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;

import java.util.List;

public class DoctorListViewAdapter extends ArrayAdapter {
    List<DoctorUserEntity> result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public DoctorListViewAdapter(Activity activity, List<DoctorUserEntity> users) {
        super(activity, android.R.layout.simple_list_item_1);
        result = users;
        context = activity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView name;
        private TextView speciality;
        private RatingBar rating;

        public void setUpView(View convertView, int position) {
            name = (TextView) convertView.findViewById(R.id.doctorName);
            speciality = (TextView) convertView.findViewById(R.id.speciality);
            rating = (RatingBar) convertView.findViewById(R.id.ratingBar);
            name.setText(result.get(position).getName());
            speciality.setText(getSpecialityString(position));
            rating.setRating(result.get(position).getRating());
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setUpView(convertView, position);
        return convertView;
    }

    @NonNull
    private String getSpecialityString(int position) {
        StringBuilder sb = new StringBuilder();
        for (SpecialityEntity specialityEntity : result.get(position).getSpeciality()) {
            sb.append(specialityEntity.getName());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        if (sb.length() > 50) {
            sb.replace(50, sb.length(), "...");
            sb.trimToSize();
        }
        return sb.toString();
    }
}
