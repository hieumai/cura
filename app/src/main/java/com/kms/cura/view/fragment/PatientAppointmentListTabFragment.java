package com.kms.cura.view.fragment;

import android.content.Intent;
import android.app.ProgressDialog;
import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

import com.kms.cura.R;
import com.kms.cura.constant.EventConstant;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.event.EventBroker;
import com.kms.cura.event.EventHandler;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.utils.DataUtils;
import com.kms.cura.view.AnimationExecutor;
import com.kms.cura.view.activity.PatientAppointmentDetailsActivity;
import com.kms.cura.view.adapter.PatientAppointmentListAdapter;


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

    public PatientAppointmentListTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_appts_list_tab, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) myFragmentView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
                    private Exception exception = null;

                    @Override
                    protected Void doInBackground(Object[] params) {
                        try {
                            PatientUserEntity patient = (PatientUserEntity) CurrentUserProfile.getInstance().getEntity();
                            PatientUserEntity patientUserEntity = new PatientUserEntity(patient.getId(),null,null,null,null,null,null,null,null,null);
                            AppointmentEntity entity = new AppointmentEntity(patientUserEntity, null, null, null, null, null, -1, null, null);
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
                            setupData();
                            adapter = new PatientAppointmentListAdapter(getActivity(), apptsList);
                            lvAppts.setAdapter(adapter);
                            lvAppts.setAnimExecutor(new AnimationExecutor(mOriginalViewHeightPool));
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                };
                task.execute();
            }
        });
        EventBroker.getInstance().register(this, EventConstant.UPDATE_APPT_PATIENT_LIST);
        setupData();
        setupListView(myFragmentView);
        return myFragmentView;
    }

    private void setupData() {
        Bundle bundle = getArguments();
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

    private void setupListView(View parent) {
        lvAppts = (ExpandableStickyListHeadersListView) parent.findViewById(R.id.lvApptsList);
        adapter = new PatientAppointmentListAdapter(getActivity(), apptsList);
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
                Intent toDetails = new Intent(getActivity(), PatientAppointmentDetailsActivity.class);
                AppointmentEntity entity = apptsList.get(position);
                List<AppointmentEntity> appts = ((PatientUserEntity)CurrentUserProfile.getInstance().getEntity()).getAppointmentList();
                toDetails.putExtra(APPT_POSITION, appts.indexOf(entity));
                startActivity(toDetails);
            }
        });
        lvAppts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toDetails = new Intent(getActivity(), PatientAppointmentDetailsActivity.class);
                AppointmentEntity entity = apptsList.get(position);
                List<AppointmentEntity> appts = ((PatientUserEntity)CurrentUserProfile.getInstance().getEntity()).getAppointmentList();
                toDetails.putExtra(APPT_POSITION, appts.indexOf(entity));
                startActivity(toDetails);
            }
        });
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
}
