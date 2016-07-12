package com.kms.cura.view.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import com.kms.cura.R;
import com.kms.cura.view.adapter.AppointmentListAdapter;


import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by linhtnvo on 7/8/2016.
 */
public class PatientAppointmentListTabFragment extends Fragment {
    private int state;
    private List<DummyAppointment> apptsList;
    private ExpandableStickyListHeadersListView lvAppts;
    private AppointmentListAdapter adapter;
    private WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
    public PatientAppointmentListTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.fragment_appts_list_tab, container, false);
        setupData();
        setupListView(myFragmentView);
        return myFragmentView;
    }

    private void setupData() {
        Bundle bundle = getArguments();
        state = bundle.getInt(HealthTrackerFragment.KEY_STATE);
        apptsList = seedData();
    }

    public List<DummyAppointment> seedData(){
        List<DummyAppointment> dummy = new ArrayList<>();
        Date date1,date2;
        int status1,status2;
        if(state == PatientAppointmentListFragment.STATE_PAST){
            date1 = Date.valueOf("2016-06-25");
            date2 = Date.valueOf("2016-06-24");
            status1 = 2;
            status2 = 3;
        }
        else{
            date1 = Date.valueOf("2016-07-14");
            date2 = Date.valueOf("2016-07-15");
            status1 = 0;
            status2 = 1;
        }
        dummy.add(new DummyAppointment("Alex","ABC","08:00am - 09:00am",status1,date1));
        dummy.add(new DummyAppointment("Alex","ABC","10:00am - 11:00am",status2,date1));
        dummy.add(new DummyAppointment("Alex","ABC","01:00pm - 02:00pm",status1,date1));
        dummy.add(new DummyAppointment("Alex","ABC","08:00am - 09:00am",status1,date1));
        dummy.add(new DummyAppointment("Alex","ABC","10:00am - 11:00am",status2,date1));
        dummy.add(new DummyAppointment("Alex","ABC","01:00pm - 02:00pm",status1,date1));
        dummy.add(new DummyAppointment("Alex","ABC","08:00am - 09:00am",status2,date2));
        dummy.add(new DummyAppointment("Alex","ABC","10:00am - 11:00am",status1,date2));
        dummy.add(new DummyAppointment("Alex","ABC","01:00pm - 02:00pm",status2,date2));
        dummy.add(new DummyAppointment("Alex","ABC","08:00am - 09:00am",status2,date2));
        dummy.add(new DummyAppointment("Alex","ABC","10:00am - 11:00am",status1,date2));
        dummy.add(new DummyAppointment("Alex","ABC","01:00pm - 02:00pm",status2,date2));
        return dummy;
    }

    private void setupListView(View parent) {
        lvAppts = (ExpandableStickyListHeadersListView) parent.findViewById(R.id.lvApptsList);
        adapter = new AppointmentListAdapter(getActivity(),apptsList);
        lvAppts.setAdapter(adapter);
        lvAppts.setAnimExecutor(new AnimationExecutor());
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


    class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if(ExpandableStickyListHeadersListView.ANIMATION_EXPAND==animType&&target.getVisibility()==View.VISIBLE){
                return;
            }
            if(ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE==animType&&target.getVisibility()!=View.VISIBLE){
                return;
            }
            if(mOriginalViewHeightPool.get(target)==null){
                mOriginalViewHeightPool.put(target,target.getHeight());
            }
            final int viewHeight = mOriginalViewHeightPool.get(target);
            float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
            float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(View.VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                        target.setVisibility(View.VISIBLE);
                    } else {
                        target.setVisibility(View.GONE);
                    }
                    target.getLayoutParams().height = viewHeight;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    target.setLayoutParams(lp);
                    target.requestLayout();
                }
            });
            animator.start();

        }
    }

}
