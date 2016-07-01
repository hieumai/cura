package com.kms.cura.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toanbnguyen on 6/28/2016.
 */
public class FilterAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private int resource;
    private ArrayList<String> listAll;
    private ArrayList<String> listFilter;
    private ItemFilter mFilter = new ItemFilter();

    public FilterAdapter(Context context, int resource, ArrayList<String> names) {
        this.context = context;
        this.resource = resource;
        this.listAll = names;
        listFilter = listAll;
    }

    @Override
    public int getCount() {
        return listFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return listAll.get((int) getItemId(position));
    }

    @Override
    public long getItemId(int position) {
        return listAll.indexOf(listFilter.get(position));
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
        String name = listFilter.get(position);
        TextView tv = (TextView) convertView.findViewById(R.id.tvStringItem);
        tv.setText(name);
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<String> nlist = new ArrayList<String>(listAll.size());
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).toLowerCase().startsWith(filterString)) {
                    nlist.add(listAll.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listFilter = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}