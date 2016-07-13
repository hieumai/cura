package com.kms.cura.view.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

import com.kms.cura.R;
import com.kms.cura.entity.AppointmentEntity;
import com.kms.cura.entity.FacilityEntity;
import com.kms.cura.entity.user.DoctorUserEntity;
import com.kms.cura.entity.user.PatientUserEntity;
import com.kms.cura.utils.CurrentUserProfile;
import com.kms.cura.view.AnimationExecutor;
import com.kms.cura.view.adapter.PatientAppointmentListAdapter;


import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by linhtnvo on 7/8/2016.
 */
public class PatientAppointmentListTabFragment extends Fragment {
    private int state;
    private List<AppointmentEntity> apptsList;
    private ExpandableStickyListHeadersListView lvAppts;
    private PatientAppointmentListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
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
                Toast.makeText(getActivity(),"Refreshing",Toast.LENGTH_LONG).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        setupData();
        setupListView(myFragmentView);
        return myFragmentView;
    }

    private void setupData() {
        Bundle bundle = getArguments();
        state = bundle.getInt(PatientAppointmentListFragment.KEY_STATE,PatientAppointmentListFragment.STATE_UPCOMING);
        apptsList = getData();
    }

    public List<AppointmentEntity> getData(){
        List<AppointmentEntity> appts = new ArrayList<>();
        if(state == PatientAppointmentListFragment.STATE_PAST){
            appts.addAll(CurrentUserProfile.getInstance().getPastAppts());
        }
        else {
            appts.addAll(CurrentUserProfile.getInstance().getUpcomingAppts());
        }
        Collections.sort(appts, new Comparator<AppointmentEntity>() {
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
        return appts;
    }

    private void setupListView(View parent) {
        lvAppts = (ExpandableStickyListHeadersListView) parent.findViewById(R.id.lvApptsList);
        adapter = new PatientAppointmentListAdapter(getActivity(),apptsList);
        lvAppts.setAdapter(adapter);
        lvAppts.setAnimExecutor(new AnimationExecutor(mOriginalViewHeightPool));
        lvAppts.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if(lvAppts.isHeaderCollapsed(headerId)){
                    lvAppts.expand(headerId);
                }else {
                    lvAppts.collapse(headerId);
                }
            }
        });
    }


}
