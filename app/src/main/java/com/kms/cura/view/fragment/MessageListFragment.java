package com.kms.cura.view.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.MessageController;
import com.kms.cura.entity.MessageEntity;
import com.kms.cura.entity.MessageThreadEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.MultipleChoiceBackPress;
import com.kms.cura.view.activity.MessageThreadActivity;
import com.kms.cura.view.activity.NewMessageActivity;
import com.kms.cura.view.adapter.MessageListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Toolbar.OnMenuItemClickListener,
        View.OnClickListener, DialogInterface.OnClickListener, SwipeRefreshLayout.OnRefreshListener, MultipleChoiceBackPress {

    private ListView lvMessage;
    private MessageListAdapter adapter;
    private ArrayList<MessageThreadEntity> messageThreadEntities;
    private Toolbar messageListToolbar;
    private boolean multiMode = false;
    private Drawable naviIcon;
    private SwipeRefreshLayout refreshLayout;
    private static final int REQUEST_CODE = 1;
    public static final String TO_MESSAGE = "toMessage";

    public MessageListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_message_list, container, false);
        messageListToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        naviIcon = messageListToolbar.getNavigationIcon();
        loadData(true);
        modifyToolbar(R.menu.menu_blank, getString(R.string.messages));
        setUpListView(myFragmentView);
        return myFragmentView;
    }

    private void setUpListView(View parent) {
        lvMessage = (ListView) parent.findViewById(R.id.lvMessageList);
        lvMessage.setOnItemClickListener(this);
        lvMessage.setOnItemLongClickListener(this);
        refreshLayout = (SwipeRefreshLayout) parent.findViewById(R.id.lvRefreshMessage);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary), ContextCompat.getColor(getActivity(), R.color.colorAccent));
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

    private void loadData(final boolean showDialog) {
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            ProgressDialog pDialog;
            List<MessageEntity> messageEntities;

            @Override
            protected void onPreExecute() {
                if (showDialog) {
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage(getString(R.string.loading));
                    pDialog.setCancelable(false);
                    pDialog.show();
                }
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    messageEntities = MessageController.loadMessage(CurrentUserProfile.getInstance().getEntity());
                    messageThreadEntities = MessageThreadEntity.getMessageThreadFromList(CurrentUserProfile.getInstance().getEntity(), messageEntities);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (showDialog) {
                    pDialog.dismiss();
                } else {
                    refreshLayout.setRefreshing(false);
                }
                if (exception != null) {
                    ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                } else {
                    adapter = new MessageListAdapter(getActivity(), R.layout.message_list_item, messageThreadEntities);
                    lvMessage.setAdapter(adapter);
                }
            }
        }.execute();
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
            Intent intent = new Intent(getActivity(), MessageThreadActivity.class);
            intent.putExtra(MessageThreadEntity.CONVERSATION, messageThreadEntities.get(position).getConversationName(CurrentUserProfile.getInstance().getEntity()));
            startActivityForResult(intent, REQUEST_CODE);
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
        multiMode = true;
        adapter.setSelected(position, true);
        setLayoutColor(view, R.color.light_grey_2);
        modifyToolbar(R.menu.menu_message_list, adapter.getSelectedCount() + " selected");
        messageListToolbar.setNavigationIcon(R.drawable.back_arrow);
        messageListToolbar.setNavigationOnClickListener(this);
        refreshLayout.setEnabled(false);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.btnDeleteMessage) {
            createDialog(getString(R.string.delete_thread_warning)).show();
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
        refreshLayout.setEnabled(true);
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
            for (int i = 0; i < messageThreadEntities.size(); ++i) {
                if (adapter.isSelected(i)) {
                    deleteThreads(messageThreadEntities.get(i));
                }
            }
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

    private void deleteThreads(final MessageThreadEntity entity) {
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            ProgressDialog pDialog;
            List<MessageEntity> messageEntities;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    for (int i = 0; i < entity.getSize(); ++i) {
                        MessageController.deleteMessage(CurrentUserProfile.getInstance().getEntity(), entity.getMessage(i));
                    }
                    messageEntities = MessageController.loadMessage(CurrentUserProfile.getInstance().getEntity());
                    messageThreadEntities = MessageThreadEntity.getMessageThreadFromList(CurrentUserProfile.getInstance().getEntity(), messageEntities);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pDialog.dismiss();
                if (exception != null) {
                    ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                } else {
                    adapter.setData(messageThreadEntities);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK && data.getBooleanExtra(NewMessageActivity.REFRESH_REQUEST, false)) {
            loadData(false);
        }
    }
}
