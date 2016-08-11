package com.kms.cura.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.MessageController;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.MessageThreadEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.adapter.MessageAdapter;

import java.util.ArrayList;

public class MessageThreadActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Toolbar.OnMenuItemClickListener,
        View.OnClickListener, DialogInterface.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TO_MESSAGE = "message";
    private ListView lvMessage;
    private MessageAdapter adapter;
    private ArrayList<MessageEntity> messageEntities;
    private Toolbar messageListToolbar;
    private boolean multiMode = false;
    private SwipeRefreshLayout refreshLayout;
    private Button btnReply;
    private ImageButton btnBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_thread);
        messageListToolbar = (Toolbar) findViewById(R.id.tbMessageThread);
        tvTitle = (TextView) messageListToolbar.findViewById(R.id.tvMessageThreadTitle);
        modifyToolbar(R.menu.menu_blank, "");
        setUpButton();
        setUpListView();
        setUpData();
    }

    private void setUpButton() {
        btnReply = (Button) messageListToolbar.findViewById(R.id.btnReply);
        btnReply.setOnClickListener(this);
        btnBack = (ImageButton) messageListToolbar.findViewById(R.id.btnMessageThreadBack);
        btnBack.setOnClickListener(this);
    }

    private void setUpData() {
        messageEntities = MessageController.getMessageByThread(CurrentUserProfile.getInstance().getEntity(), getIntent().getStringExtra(MessageThreadEntity.CONVERSATION));
        adapter = new MessageAdapter(this, R.layout.message_item, messageEntities);
        lvMessage.setAdapter(adapter);
    }

    private void setUpListView() {
        lvMessage = (ListView) findViewById(R.id.lvMessage);
        lvMessage.setOnItemClickListener(this);
        lvMessage.setOnItemLongClickListener(this);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.lvRefreshMessage);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary), ContextCompat.getColor(this, R.color.colorAccent));
        refreshLayout.setEnabled(false);
    }

    private void modifyToolbar(int menuId, String title) {
        messageListToolbar.getMenu().clear();
        tvTitle.setText(title);
        messageListToolbar.inflateMenu(menuId);
        messageListToolbar.setOnMenuItemClickListener(this);
    }

    private void modifyToolbar(String title) {
        tvTitle.setText(title);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!multiMode) {
            return;
        }
        setSelection(view, position);
        if (adapter.getSelectedCount() == 0) {
            clearMultipleChoiceMode();
        } else {
            modifyToolbar(adapter.getSelectedCount() + " selected");
        }
    }

    private void setSelection(View view, int position) {
        if (!adapter.isSelected(position)) {
            setLayoutColor(view, R.color.light_grey_2);
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
        setMultipleChoiceMode(view, position);
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
        if (v.getId() == R.id.btnReply) {
            Toast.makeText(this, "Reply", Toast.LENGTH_SHORT).show();
            // navigate to New Message
        } else if (v.getId() == R.id.btnMessageThreadBack) {
            if (multiMode) {
                clearMultipleChoiceMode();
            } else {
                finish();
            }
        }
    }

    private void setLayoutColor(View view, int color) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layoutMessageItem);
        layout.setBackgroundColor(ContextCompat.getColor(this, color));
    }

    private AlertDialog createDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.yes), this);
        builder.setNegativeButton(getString(R.string.no), this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            for (int i = 0; i < messageEntities.size(); ++i) {
                if (adapter.isSelected(i)) {
                    // delete message(s)
                    // ...
                    clearMultipleChoiceMode();
                }
            }
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialog.dismiss();
        }
    }

    private void setMultipleChoiceMode(View view, int position) {
        multiMode = true;
        adapter.setSelected(position, true);
        setLayoutColor(view, R.color.light_grey_2);
        modifyToolbar(R.menu.menu_message_list, adapter.getSelectedCount() + " selected");
        btnReply.setVisibility(View.INVISIBLE);
    }

    private void clearMultipleChoiceMode() {
        multiMode = false;
        modifyToolbar(R.menu.menu_blank, "");
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
        btnReply.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        // refresh
        // ...
    }

    public ListView getLvMessage() {
        return lvMessage;
    }

    public boolean isMultiMode() {
        return multiMode;
    }
}