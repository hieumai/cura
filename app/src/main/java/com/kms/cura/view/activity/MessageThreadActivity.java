package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.MessageController;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.MessageThreadEntity;
import com.kms.cura.entity.user.UserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.adapter.MessageAdapter;

import java.util.ArrayList;

public class MessageThreadActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Toolbar.OnMenuItemClickListener,
        View.OnClickListener, DialogInterface.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView lvMessage;
    private MessageAdapter adapter;
    private ArrayList<MessageEntity> messageEntities;
    private Toolbar messageListToolbar;
    private boolean multiMode = false;
    private SwipeRefreshLayout refreshLayout;
    private Button btnReply;
    private ImageButton btnBack;
    private TextView tvTitle;
    private static final int REQUEST_CODE = 1;
    private String receiverID, receiverName;

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
        MessageEntity message = messageEntities.get(0);
        if ((CurrentUserProfile.getInstance().isPatient() && message.isSenderDoctor()) || (CurrentUserProfile.getInstance().isDoctor() && !message.isSenderDoctor())) {
            receiverID = message.getSender().getId();
            receiverName = message.getSender().getName();
        } else {
            receiverID = message.getReceiver().getId();
            receiverName = message.getReceiver().getName();
        }
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
        refreshLayout.setEnabled(true);
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
            Intent intent = new Intent(this, NewMessageActivity.class);
            intent.putExtra(NewMessageActivity.KEY_SENDER, NewMessageActivity.KEY_PATIENT);
            intent.putExtra(NewMessageActivity.KEY_RECEIVER_ID, receiverID);
            intent.putExtra(NewMessageActivity.KEY_RECEIVER_NAME, receiverName);
            startActivityForResult(intent, REQUEST_CODE);
        } else if (v.getId() == R.id.btnMessageThreadBack) {
            if (multiMode) {
                clearMultipleChoiceMode();
            } else {
                backToMain();
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
            ArrayList<MessageEntity> deleteMessageList = new ArrayList<>();
            for (int i = 0; i < messageEntities.size(); ++i) {
                if (adapter.isSelected(i)) {
                    deleteMessageList.add(messageEntities.get(i));
                }
            }
            deleteMessages(deleteMessageList);
            clearMultipleChoiceMode();
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
        refreshLayout.setEnabled(false);
    }

    private void clearMultipleChoiceMode() {
        multiMode = false;
        modifyToolbar(R.menu.menu_blank, "");
        adapter.clearSelection();
        adapter.notifyDataSetChanged();
        btnReply.setVisibility(View.VISIBLE);
        refreshLayout.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        if (!multiMode) {
            refreshMessage();
        }
    }

    public ListView getLvMessage() {
        return lvMessage;
    }

    public boolean isMultiMode() {
        return multiMode;
    }

    private void deleteMessages(final ArrayList<MessageEntity> entities) {
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(MessageThreadActivity.this);
                pDialog.setMessage(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    UserEntity userEntity = CurrentUserProfile.getInstance().getEntity();
                    for (MessageEntity entity : entities) {
                        MessageController.deleteMessage(userEntity, entity);
                    }
                    MessageController.loadMessage(userEntity);
                    messageEntities = MessageController.getMessageByThread(userEntity, getIntent().getStringExtra(MessageThreadEntity.CONVERSATION));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                if (exception != null) {
                    ErrorController.showDialog(MessageThreadActivity.this, "Error : " + exception.getMessage());
                    return;
                }
                if (messageEntities == null) {
                    backToMain();
                    return;
                }
                adapter.setData(messageEntities);
                adapter.notifyDataSetChanged();
                Toast.makeText(MessageThreadActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                sendRefreshRequest();

            }
        }.execute();
    }

    private void refreshMessage() {
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    UserEntity userEntity = CurrentUserProfile.getInstance().getEntity();
                    MessageController.loadMessage(userEntity);
                    messageEntities = MessageController.getMessageByThread(userEntity, getIntent().getStringExtra(MessageThreadEntity.CONVERSATION));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (exception != null) {
                    ErrorController.showDialog(MessageThreadActivity.this, "Error : " + exception.getMessage());
                    return;
                }
                adapter.setData(messageEntities);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        }.execute();
    }

    private void backToMain() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (multiMode) {
            clearMultipleChoiceMode();
        } else {
            backToMain();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data.getBooleanExtra(NewMessageActivity.REFRESH_REQUEST, false)) {
            refreshMessage();
            sendRefreshRequest();
        }
    }

    private void sendRefreshRequest() {
        Intent intent = new Intent();
        intent.putExtra(NewMessageActivity.REFRESH_REQUEST, true);
        setResult(RESULT_OK, intent);
    }
}