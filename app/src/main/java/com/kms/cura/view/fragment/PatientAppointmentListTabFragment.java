package com.kms.cura.view.fragment;

import android.content.Intent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.NotificationController;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.NotificationEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.AnimationExecutor;
import com.kms.cura.view.activity.PatientAppointmentDetailsActivity;
import com.kms.cura.view.adapter.PatientAppointmentListAdapter;
import com.kms.cura.view.service.NotificationListener;


import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by linhtnvo on 7/8/2016.
 */
public class PatientAppointmentListTabFragment extends Fragment implements EventHandler {
    public static String APPT_POSITION = "APPT_POSITION";
    private int state;
    private List<AppointmentEntity> apptsList;
    private ExpandableStickyListHeadersListView lvAppts;
    private PatientAppointmentListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
    private boolean isUpdated;
    private ProgressDialog pDialog;
    private List<String> notifs;
    private List<NotificationEntity> apptNotifs;
    private View myFragmentView;
    public PatientAppointmentListTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_appts_list_tab, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateApptList();
            }
        });
        EventBroker.getInstance().register(this, EventConstant.UPDATE_APPT_PATIENT_LIST);
        setupData();
        setApptNotification();
        if (!isUpdated){
            mSwipeRefreshLayout.setRefreshing(true);
            updateApptList();
        }
        return myFragmentView;
    }

    private void updateApptList(){
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    PatientUserEntity patient = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
                    PatientUserEntity patientUserEntity = new PatientUserEntity(patient.getId(),null,null,null,null,null,null,null,null,null);
                    AppointmentEntity entity = new AppointmentEntity(null,patientUserEntity, null, null, null, null, null, -1, null, null);
                    patient.setAppointmentList(AppointmentController.getAppointment(new AppointSearchEntity(entity)));
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (exception != null) {
                    ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                } else {
                    apptsList.clear();
                    setupData();;
                    setApptNotification();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        task.execute();
    }

    private void setupData() {
        Bundle bundle = getArguments();
        isUpdated = bundle.getBoolean(NotificationListener.UPDATE);
        state = bundle.getInt(PatientAppointmentListFragment.KEY_STATE, PatientAppointmentListFragment.STATE_UPCOMING);
        apptsList = getData();
    }

    public List<AppointmentEntity> getData() {
        List<AppointmentEntity> appts = new ArrayList<>();
        if (state == PatientAppointmentListFragment.STATE_PAST) {
            appts.addAll(DataUtils.getPastAppts(((PatientUserEntity) CurrentUserProfile.getInstance().getEntity()).getAppointmentList()));
        } else {
            appts.addAll(DataUtils.getUpcomingAppts(((PatientUserEntity) CurrentUserProfile.getInstance().getEntity()).getAppointmentList()));
        }
        Collections.sort(appts, new Comparator<AppointmentEntity>() {
            @Override
            public int compare(AppointmentEntity lhs, AppointmentEntity rhs) {
                if (lhs.getApptDay().before(rhs.getApptDay())) {
                    return -1;
                } else if (lhs.getApptDay().after(rhs.getApptDay())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return appts;
    }

    private void setApptNotification(){
        if (notifs != null){
            notifs.clear();
        }
        else {
            notifs = new ArrayList<>();
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    apptNotifs = NotificationController.getApptNotification(CurrentUserProfile.getInstance().getEntity());
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showDialog(getActivity(), "Error : " + exception.getMessage());
                } else {
                    for (NotificationEntity entity : apptNotifs){
                        notifs.add(entity.getRefEntity().getId());
                    }
                    setupListView(myFragmentView);
                }

            }
        };
        task.execute();
    }

    private void setupListView(View parent) {
        lvAppts = (ExpandableStickyListHeadersListView) parent.findViewById(R.id.lvApptsList);
        adapter = new PatientAppointmentListAdapter(getActivity(), apptsList, notifs);
        lvAppts.setAdapter(adapter);
        lvAppts.setAnimExecutor(new AnimationExecutor(mOriginalViewHeightPool));
        lvAppts.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (lvAppts.isHeaderCollapsed(headerId)) {
                    lvAppts.expand(headerId);
                } else {
                    lvAppts.collapse(headerId);
                }
            }
        });
        lvAppts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppointmentEntity entity = apptsList.get(position);
                String entityID = entity.getId();
                if (notifs.contains(entityID)){
                    for (NotificationEntity notificationEntity : apptNotifs){
                        if (entityID.equals(notificationEntity.getRefEntity().getId())){
                            updateNotiWhenClick(notificationEntity, entityID, entity);
                            break;
                        }
                    }
                    return;
                }
                Intent toDetails = new Intent(getActivity(), PatientAppointmentDetailsActivity.class);
                List<AppointmentEntity> appts = ((PatientUserEntity)CurrentUserProfile.getInstance().getEntity()).getAppointmentList();
                toDetails.putExtra(APPT_POSITION, appts.indexOf(entity));
                startActivity(toDetails);
            }
        });
    }

    private void updateNotiWhenClick(final NotificationEntity entity, final String id, final AppointmentEntity apptEntity){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.loading));
        pDialog.setCancelable(false);
        showProgressDialog();
        AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;
            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    NotificationController.updateApptNoti(entity);
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    //TODO : log file for app
                } else {
                    notifs.remove(id);
                    EventBroker.getInstance().pusblish(EventConstant.UPDATE_APPT_NOTI_NUMBER, null);
                    adapter.notifyDataSetChanged();
                }
                Intent toDetails = new Intent(getActivity(), PatientAppointmentDetailsActivity.class);
                List<AppointmentEntity> appts = ((PatientUserEntity)CurrentUserProfile.getInstance().getEntity()).getAppointmentList();
                toDetails.putExtra(APPT_POSITION, appts.indexOf(apptEntity));
                startActivity(toDetails);
            }
        };
        task.execute();
    }


    @Override
    public void handleEvent(String event, Object data) {
        switch (event){
            case EventConstant.UPDATE_APPT_PATIENT_LIST:
                apptsList.clear();
                apptsList.addAll(getData());
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        EventBroker.getInstance().unRegister(this, EventConstant.UPDATE_APPT_PATIENT_LIST);
        super.onDestroyView();
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
