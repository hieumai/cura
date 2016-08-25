package com.kms.cura.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.view.DoctorApptDayVIew;
import com.kms.cura.view.activity.ViewScheduleActivity;
import com.kms.cura.view.adapter.DayViewPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by linhtnvo on 8/3/2016.
 */
public class DoctorApptDayVIewFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static int MAXIMUN_PAGE = 10;
    private ViewPager vpCalendar;
    private DayViewPagerAdapter adapter;
    private int currentPos;
    private Calendar currentCalendar;
    private List<Integer> dateInPage;
    private PagerTabStrip tabStrip;
    private Toolbar appointmenttoolbar;
    private View toolbarView;
    private int INITIAL_SIZE = 5;
    private int INITIAL_POSITION_OF_CURRENT = 2;
    public static final String SELECTED_DAY = "SELECTED_DAY";
    private Date selectedDay;
    private int timeDiff;
    private boolean is31st = false;
    private boolean differentYear = false;
    private Bundle bundle;
    private int fromRequest;
    public DoctorApptDayVIewFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_doctor_appt_day_view, null);
        bundle = getArguments();
        tabStrip = (PagerTabStrip) root.findViewById(R.id.pager_header);
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.white));
        currentCalendar = Calendar.getInstance();
        getDataFromMonthView();
        vpCalendar = (ViewPager) root.findViewById(R.id.vpCalendar);
        adapter = new DayViewPagerAdapter(getActivity(), createDayViewForViewPager());
        vpCalendar.setAdapter(adapter);
        setCurrentCalendar();
        vpCalendar.addOnPageChangeListener(this);
        fromRequest = bundle.getInt(ViewScheduleActivity.FROM_REQUEST,0);
        if (fromRequest == 0) {
            modifyToolbar();
        }
        return root;
    }

    private void getDataFromMonthView() {
        selectedDay = new Date(bundle.getLong(SELECTED_DAY));
        Calendar newCalendar = (Calendar) currentCalendar.clone();
        newCalendar.setTime(selectedDay);
        if (currentCalendar.get(Calendar.YEAR) == newCalendar.get(Calendar.YEAR)) {
            timeDiff = newCalendar.get(Calendar.DAY_OF_YEAR) - currentCalendar.get(Calendar.DAY_OF_YEAR);
            return;
        }
        differentYear = true;
        long secondTimeDiff = newCalendar.getTime().getTime() - currentCalendar.getTime().getTime();
        timeDiff = (int) TimeUnit.DAYS.convert(secondTimeDiff, TimeUnit.MILLISECONDS);
    }


    private List<DoctorApptDayVIew> createDayViewForViewPager() {
        List<DoctorApptDayVIew> views = new ArrayList<>();
        int increasement = -2;
        dateInPage = new ArrayList<>();
        Calendar newCalendar = (Calendar) currentCalendar.clone();
        newCalendar.setTime(selectedDay);
        if (newCalendar.get(Calendar.DAY_OF_MONTH) ==31){
            is31st = true;
        }
        for (int i = 0; i < INITIAL_SIZE; ++i) {
            dateInPage.add(increasement+timeDiff);
            Calendar cloneCurrent = (Calendar) newCalendar.clone();
            cloneCurrent.add(Calendar.DAY_OF_MONTH, increasement);
            DoctorApptDayVIew dayView = new DoctorApptDayVIew(getActivity(), cloneCurrent.getTime());
            views.add(dayView);
            ++increasement;
        }
        return views;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    private void setCurrentCalendar() {
        vpCalendar.setCurrentItem(INITIAL_POSITION_OF_CURRENT);
        currentPos = vpCalendar.getCurrentItem();
    }

    @Override
    public void onPageSelected(int position) {
        if (currentPos == position) {
            if (currentPos == 1) {
                vpCalendar.setCurrentItem(currentPos + 1);
            } else {
                vpCalendar.setCurrentItem(currentPos - 1);
            }
            return;
        }
        if (currentPos < position) {
            ++currentPos;
        } else {
            --currentPos;
        }
        if (currentPos == dateInPage.size() - 2) {
            createDateAtLast();
        } else if (currentPos == 1) {
            createDateAtFirst();
        }
        vpCalendar.setCurrentItem(currentPos);
    }

    private void createDateAtLast() {
        int newDate = dateInPage.get(currentPos) + 2;
        dateInPage.add(newDate);
        if (is31st && differentYear){
            --newDate;
        }
        Calendar cloneCurrent = (Calendar) currentCalendar.clone();
        cloneCurrent.add(Calendar.DAY_OF_YEAR, newDate);
        adapter.addView(new DoctorApptDayVIew(getActivity(), cloneCurrent.getTime()));
        if (dateInPage.size() >= MAXIMUN_PAGE) {
            dateInPage.remove(0);
            adapter.removeView(vpCalendar, 0);
        }
        adapter.notifyDataSetChanged();
    }

    private void createDateAtFirst() {
        int newDate = dateInPage.get(currentPos) - 2;
        dateInPage.add(0, newDate);
        Calendar cloneCurrent = (Calendar) currentCalendar.clone();
        cloneCurrent.add(Calendar.DAY_OF_YEAR, newDate);
        adapter.addView(new DoctorApptDayVIew(getActivity(), cloneCurrent.getTime()), 0);
        if (dateInPage.size() >= MAXIMUN_PAGE) {
            int position = dateInPage.size() - 1;
            dateInPage.remove(position);
            adapter.removeView(vpCalendar, position);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void modifyToolbar() {
        appointmenttoolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        appointmenttoolbar.getMenu().clear();
        appointmenttoolbar.setTitle("");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        toolbarView = setUpToolbar();
        appointmenttoolbar.addView(toolbarView, params);
    }

    private View setUpToolbar() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.toolbar_day_view, null);
        TextView btnToday = setUpButton(root, R.id.btnToday);
        TextView btnMonthView = setUpButton(root, R.id.btnMonthView);
        return root;
    }

    private TextView setUpButton(View root, int id) {
        TextView button = (TextView) root.findViewById(id);
        button.setOnClickListener(this);
        return button;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnToday) {
            if (dateInPage.contains(0)) {
                currentPos = dateInPage.indexOf(0) + 1;
            } else {
                if (dateInPage.get(dateInPage.size() - 1) == -1) {
                    createTodayAtLast();
                    currentPos = dateInPage.size() - 1;
                    --currentPos;
                    createDateAtLast();
                    createDateAtLast();
                    vpCalendar.setCurrentItem(currentPos);
                    return;
                }
                vpCalendar.setAdapter(null);
                adapter.clear();
                adapter = null;
                timeDiff = 0;
                selectedDay = new Date(currentCalendar.getTime().getTime());
                adapter = new DayViewPagerAdapter(getActivity(), createDayViewForViewPager());
                vpCalendar.setAdapter(adapter);
                currentPos = 1;
            }
            vpCalendar.setCurrentItem(INITIAL_POSITION_OF_CURRENT - 1);
        } else if (id == R.id.btnMonthView) {
            DoctorAppointmentMonthViewFragment monthViewFragment = new DoctorAppointmentMonthViewFragment();
            Bundle bundle = new Bundle();
            Calendar cloneCalendar = (Calendar) currentCalendar.clone();
            if (is31st && differentYear){
                cloneCalendar.add(Calendar.DAY_OF_YEAR, dateInPage.get(currentPos-1));
            }
            else {
                cloneCalendar.add(Calendar.DAY_OF_YEAR, dateInPage.get(currentPos));
            }
            bundle.putLong(SELECTED_DAY, cloneCalendar.getTime().getTime());
            monthViewFragment.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.Fragment_UserView, monthViewFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    private void createTodayAtLast() {
        dateInPage.add(0);
        adapter.addView(new DoctorApptDayVIew(getActivity(), currentCalendar.getTime()));
        if (dateInPage.size() >= MAXIMUN_PAGE) {
            dateInPage.remove(0);
            adapter.removeView(vpCalendar, 0);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        if (fromRequest ==0) {
            appointmenttoolbar.removeView(toolbarView);
        }
        super.onDestroyView();
    }
}
