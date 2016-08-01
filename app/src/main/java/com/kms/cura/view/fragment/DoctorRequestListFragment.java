package com.kms.cura.view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kms.cura.R;
import com.kms.cura.controller.AppointmentController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.entity.AppointSearchEntity;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.AnimationExecutor;
import com.kms.cura.view.adapter.DoctorRequestListAdapter;
import com.kms.cura.view.adapter.PatientAppointmentListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by linhtnvo on 7/13/2016.
 */
public class DoctorRequestListFragment extends Fragment {
    private ExpandableStickyListHeadersListView lvRequestList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DoctorRequestListAdapter adapter;
    private WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
    public DoctorRequestListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request_list,container,false);
        lvRequestList = (ExpandableStickyListHeadersListView) root.findViewById(R.id.lvRequestList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTask<Object, Void, Void> task = new AsyncTask<Object, Void, Void>() {
                    private Exception exception = null;

                    @Override
                    protected Void doInBackground(Object[] params) {
                        try {
                            DoctorUserEntity doctor = (DoctorUserEntity) CurrentUserProfile.getInstance().getEntity();
                            DoctorUserEntity doctorUserEntity = new DoctorUserEntity(doctor.getId(),null);
                            AppointmentEntity entity = new AppointmentEntity(null, doctorUserEntity, null, null, null, null, -1, null, null);
                            doctor.setAppointmentList(AppointmentController.getAppointment(new AppointSearchEntity(entity)));
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
                            adapter = new DoctorRequestListAdapter(getActivity(),getRequests());
                            lvRequestList.setAdapter(adapter);
                            lvRequestList.setAnimExecutor(new AnimationExecutor(mOriginalViewHeightPool));
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                };
                task.execute();
            }
        });
        adapter = new DoctorRequestListAdapter(getActivity(),getRequests());
        lvRequestList.setAdapter(adapter);
        lvRequestList.setAnimExecutor(new AnimationExecutor(mOriginalViewHeightPool));
        lvRequestList.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if(lvRequestList.isHeaderCollapsed(headerId)){
                    lvRequestList.expand(headerId);
                }else {
                    lvRequestList.collapse(headerId);
                }
            }
        });
        modifyToolbar();
        return root;
    }

    private List<AppointmentEntity> getRequests(){
        List<AppointmentEntity> requestList = new ArrayList<>();
        List<AppointmentEntity> appointmentEntities = ((DoctorUserEntity)(CurrentUserProfile.getInstance().getEntity())).getAppointmentList();
        for(AppointmentEntity entity : appointmentEntities){
            if (entity.getStatus() == AppointmentEntity.PENDING_STT){
                requestList.add(entity);
            }
        }
        Collections.sort(requestList, new Comparator<AppointmentEntity>() {
            @Override
            public int compare(AppointmentEntity lhs, AppointmentEntity rhs) {
                if(lhs.getApptDay().before(rhs.getApptDay())){
                    return -1;
                }
                else if(lhs.getApptDay().after(rhs.getApptDay())){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        });
        return requestList;
    }
    private void modifyToolbar() {
        Toolbar healthTrackerToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        healthTrackerToolbar.getMenu().clear();
        healthTrackerToolbar.setTitle(getString(R.string.request));
    }
}
