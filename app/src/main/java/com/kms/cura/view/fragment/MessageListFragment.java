package com.kms.cura.view.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.MessageThreadEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.MultipleChoiceBackPress;
import com.kms.cura.view.adapter.MessageListAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class MessageListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Toolbar.OnMenuItemClickListener,
        View.OnClickListener, DialogInterface.OnClickListener, MultipleChoiceBackPress {

    private ListView lvMessage;
    private MessageListAdapter adapter;
    private ArrayList<MessageThreadEntity> messageThreadEntities;
    private Toolbar messageListToolbar;
    private boolean multiMode = false;
    private Drawable naviIcon;

    public MessageListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_message_list, container, false);
        initDummyData();
        messageListToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        naviIcon = messageListToolbar.getNavigationIcon();
        modifyToolbar(R.menu.menu_blank, getString(R.string.messages));
        setUpListView(myFragmentView);
        return myFragmentView;
    }

    private void setUpListView(View parent) {
        lvMessage = (ListView) parent.findViewById(R.id.lvMessageList);
        adapter = new MessageListAdapter(getActivity(), R.layout.message_list_item, messageThreadEntities);
        lvMessage.setAdapter(adapter);
        lvMessage.setOnItemClickListener(this);
        lvMessage.setOnItemLongClickListener(this);
    }

    private void modifyToolbar(int menuId, String title) {
        messageListToolbar.getMenu().clear();
        messageListToolbar.setTitle(title);
        messageListToolbar.inflateMenu(menuId);
        messageListToolbar.setOnMenuItemClickListener(this);
    }

    private void modifyToolbar(String title) {
        messageListToolbar.setTitle(title);
    }

    void initDummyData() {
        UserEntity user, user2, user3, user4, user5, user6, user7, user8, user9;
        Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        ArrayList<MessageEntity> messageEntities = new ArrayList<>();
        if (CurrentUserProfile.getInstance().isPatient()) {
            PatientUserEntity patientUserEntity = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
            user = new DoctorUserEntity("3", "Peter Pan");
            user2 = new DoctorUserEntity("4", "Doraemon");
            user3 = new DoctorUserEntity("5", "Pikachu");
            user4 = new DoctorUserEntity("6", "Chamander");
            user5 = new DoctorUserEntity("7", "Charizard");
            user6 = new DoctorUserEntity("8", "Togepi");
            user7 = new DoctorUserEntity("9", "Togetic");
            user8 = new DoctorUserEntity("10", "Togekiss");
            user9 = new DoctorUserEntity("11", "Spiritomb");
            MessageEntity entity1 = new MessageEntity(user, patientUserEntity, timestamp, "Hello from the other side");
            MessageEntity entity4 = new MessageEntity(user2, patientUserEntity, timestamp, "Hello");
            MessageEntity entity7 = new MessageEntity(user3, patientUserEntity, timestamp, "Hello");
            MessageEntity entity8 = new MessageEntity(user4, patientUserEntity, timestamp, "Hello");
            MessageEntity entity9 = new MessageEntity(user5, patientUserEntity, timestamp, "Hello");
            MessageEntity entity10 = new MessageEntity(user6, patientUserEntity, timestamp, "Hello");
            MessageEntity entity11 = new MessageEntity(user7, patientUserEntity, timestamp, "Hello");
            MessageEntity entity12 = new MessageEntity(user8, patientUserEntity, timestamp, "Hello");
            MessageEntity entity13 = new MessageEntity(user9, patientUserEntity, timestamp, "Hello");
            timestamp.setTime(Calendar.getInstance().getTimeInMillis() + 3000);
            MessageEntity entity2 = new MessageEntity(patientUserEntity, user, timestamp, "Hi");
            MessageEntity entity5 = new MessageEntity(patientUserEntity, user2, timestamp, "Hi");
            timestamp.setTime(Calendar.getInstance().getTimeInMillis() + 6000);
            MessageEntity entity3 = new MessageEntity(user, patientUserEntity, timestamp, "Bye");
            MessageEntity entity6 = new MessageEntity(user2, patientUserEntity, timestamp, "Bye");
            messageEntities.add(entity1);
            messageEntities.add(entity2);
            messageEntities.add(entity3);
            messageEntities.add(entity4);
            messageEntities.add(entity5);
            messageEntities.add(entity6);
            messageEntities.add(entity7);
            messageEntities.add(entity8);
            messageEntities.add(entity9);
            messageEntities.add(entity10);
            messageEntities.add(entity11);
            messageEntities.add(entity12);
            messageEntities.add(entity13);
            messageThreadEntities = MessageThreadEntity.getMessageThreadFromList(patientUserEntity, messageEntities);
        } else {
            user = new PatientUserEntity("1", "Peter Pan");
            user2 = new PatientUserEntity("2", "Doraemon");
            DoctorUserEntity doctorUserEntity = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
            MessageEntity entity1 = new MessageEntity(user, doctorUserEntity, timestamp, "Hello from the other side");
            MessageEntity entity4 = new MessageEntity(user2, doctorUserEntity, timestamp, "Hello");
            timestamp.setTime(Calendar.getInstance().getTimeInMillis() + 3000);
            MessageEntity entity2 = new MessageEntity(doctorUserEntity, user, timestamp, "Hi");
            MessageEntity entity5 = new MessageEntity(doctorUserEntity, user2, timestamp, "Hi");
            timestamp.setTime(Calendar.getInstance().getTimeInMillis() + 6000);
            MessageEntity entity3 = new MessageEntity(user, doctorUserEntity, timestamp, "Bye");
            MessageEntity entity6 = new MessageEntity(user2, doctorUserEntity, timestamp, "Bye");
            messageEntities.add(entity1);
            messageEntities.add(entity2);
            messageEntities.add(entity3);
            messageEntities.add(entity4);
            messageEntities.add(entity5);
            messageEntities.add(entity6);
            messageThreadEntities = MessageThreadEntity.getMessageThreadFromList(doctorUserEntity, messageEntities);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (multiMode) {
            setSelection(view, position);
            if (adapter.getSelectedCount() == 0) {
                resetNavigationDrawer();
            } else {
                modifyToolbar(adapter.getSelectedCount() + " selected");
            }
        } else {
            Toast.makeText(getActivity(), messageThreadEntities.get(position).getConversationName(CurrentUserProfile.getInstance().getEntity()), Toast.LENGTH_SHORT).show();
        }
    }

    private void setSelection(View view, int position) {
        if (!adapter.isSelected(position)) {
            setLayoutColor(view, R.color.grey);
            adapter.setSelected(position, true);
        } else {
            setLayoutColor(view, R.color.white);
            adapter.setSelected(position, false);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (multiMode) {
            return false;
        }
        multiMode = true;
        adapter.setSelected(position, true);
        setLayoutColor(view, R.color.grey);
        modifyToolbar(R.menu.menu_message_list, adapter.getSelectedCount() + " selected");
        messageListToolbar.setNavigationIcon(R.drawable.back_arrow);
        messageListToolbar.setNavigationOnClickListener(this);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.btnDeleteMessage) {
            createDialog(getString(R.string.delete_message_warning)).show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (multiMode) {
            resetNavigationDrawer();
        }
    }

    private void resetNavigationDrawer() {
        multiMode = false;
        modifyToolbar(R.menu.menu_blank, getString(R.string.messages));
        messageListToolbar.setNavigationIcon(naviIcon);
        messageListToolbar.setNavigationOnClickListener((View.OnClickListener) getActivity());
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
    }

    private void setLayoutColor(View view, int color) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutMessageList);
        layout.setBackgroundColor(ContextCompat.getColor(getActivity(), color));
    }

    private AlertDialog createDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.yes), this);
        builder.setNegativeButton(getString(R.string.no), this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            // delete the chosen in data and app
            // ...
            resetNavigationDrawer();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        if (multiMode) {
            resetNavigationDrawer();
        }
    }
}
