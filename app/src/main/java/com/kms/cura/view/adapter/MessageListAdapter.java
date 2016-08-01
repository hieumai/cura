package com.kms.cura.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.MessageThreadEntity;
import com.kms.cura.utils.CurrentUserProfile;

import java.util.ArrayList;

/**
 * Created by toanbnguyen on 7/20/2016.
 */
public class MessageListAdapter extends BaseAdapter {

    private ArrayList<MessageThreadEntity> messageThreadEntities;
    private Context context;
    private int resource;
    private ArrayList<Boolean> selected;

    public MessageListAdapter(Context context, int resource, ArrayList objects) {
        this.context = context;
        this.resource = resource;
        messageThreadEntities = objects;
        selected = new ArrayList<>();
        for (int i = 0; i < messageThreadEntities.size(); ++i) {
            selected.add(false);
        }
    }
    @Override
    public int getCount() {
        return messageThreadEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return messageThreadEntities.get(position);
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

    private void setUpView(View convertView, int position) {
        TextView tvName = (TextView) convertView.findViewById(R.id.tvMesName);
        TextView tvLine = (TextView) convertView.findViewById(R.id.tvMesLine);
        tvName.setText(messageThreadEntities.get(position).getConversationName(CurrentUserProfile.getInstance().getEntity()));
        tvLine.setText(messageThreadEntities.get(position).getFirstLine());
        setColor(convertView, position);
    }

    private void setColor(View convertView, int position) {
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layoutMessageList);
        if (selected.get(position)) {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    public void setSelected(int position, boolean value) {
        selected.set(position, value);
    }

    public void clearSelection() {
        for (int i = 0; i < messageThreadEntities.size(); ++i) {
            selected.set(i, false);
        }
    }

    public int getSelectedCount() {
        int count = 0;
        for (int i = 0; i < messageThreadEntities.size(); ++i) {
            if (selected.get(i)) {
                count++;
            }
        }
        return count;
    }

    public boolean isSelected(int position) {
        return selected.get(position);
    }

    public void setData(ArrayList<MessageThreadEntity> messageThreadEntities) {
        this.messageThreadEntities = messageThreadEntities;
        selected.clear();
        for (int i = 0; i < messageThreadEntities.size(); ++i) {
            selected.add(false);
        }
    }

}
