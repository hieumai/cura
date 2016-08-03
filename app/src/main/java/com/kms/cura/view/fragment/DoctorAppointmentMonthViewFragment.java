package com.kms.cura.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.event.EventBroker;
import com.kms.cura.view.DepthPageTransformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by linhtnvo on 7/26/2016.
 */
public class DoctorAppointmentMonthViewFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static int MAXIMUN_PAGE = 24;
    private ViewPager vpCalendar;
    private CalendarPagerAdapter adapter;
    private int currentPos;
    private Calendar currentCalendar;
    private List<Integer> dateInPage;
    private PagerTabStrip tabStrip;
    private Toolbar appointmenttoolbar;
    private View toolbarView;
    private int INITIAL_SIZE = 12;

    public DoctorAppointmentMonthViewFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_doctor_appt_month_view, null);
        tabStrip = (PagerTabStrip) root.findViewById(R.id.pager_header);
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.white));
        currentCalendar = Calendar.getInstance();
        vpCalendar = (ViewPager) root.findViewById(R.id.vpCalendar);
        adapter = new CalendarPagerAdapter(createCalendarViewForViewPager());
        vpCalendar.setAdapter(adapter);
        setCurrentCalendar();
        vpCalendar.addOnPageChangeListener(this);
        vpCalendar.setCurrentItem(currentCalendar.get(Calendar.MONTH));
        vpCalendar.setPageTransformer(true, new DepthPageTransformer());
        modifyToolbar();
        return root;
    }


    private List<CalendarView> createCalendarViewForViewPager() {
        List<CalendarView> views = new ArrayList<>();
        dateInPage = new ArrayList<>();
        for (int i = 0; i < INITIAL_SIZE; ++i) {
            dateInPage.add(i);
            CalendarView calendarView = new CalendarView(getActivity(), i);
            views.add(calendarView);
        }
        return views;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    private void setCurrentCalendar() {
        Calendar calendar = Calendar.getInstance();
        vpCalendar.setCurrentItem(calendar.get(Calendar.MONTH));
        currentPos = vpCalendar.getCurrentItem();
    }

    @Override
    public void onPageSelected(int position) {
        adapter.clearAlltheContent(currentPos);
        if (currentPos == position) {
            if (currentPos - 1 == 0) {
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
        int newDate = dateInPage.get(currentPos);
        dateInPage.add(newDate + 2);
        adapter.addView(new CalendarView(getActivity(), newDate + 2));
        if (dateInPage.size() >= MAXIMUN_PAGE) {
            dateInPage.remove(0);
            adapter.removeView(vpCalendar, 0);
        }
        adapter.notifyDataSetChanged();
    }

    private void createDateAtFirst() {
        int currentIndex = ((int) adapter.getView(2).getTag() - 3) + currentCalendar.get(Calendar.MONTH);
        dateInPage.add(0, currentIndex);
        adapter.addView(new CalendarView(getActivity(), currentIndex), 0);
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
        View root = inflater.inflate(R.layout.toolbar_month_view, null);
        TextView btnToday = setUpButton(root, R.id.btnToday);
        TextView btnDayView = setUpButton(root, R.id.btnDayView);
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
            int month = currentCalendar.get(Calendar.MONTH);
            if (dateInPage.contains(month)) {
                currentPos = dateInPage.indexOf(month) + 1;
            } else {
                if (dateInPage.get(dateInPage.size() - 1) == month - 1) {
                    createTodayAtLast(month);
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
                adapter = new CalendarPagerAdapter(createCalendarViewForViewPager());
                vpCalendar.setAdapter(adapter);
                currentPos = month + 1;
            }
            vpCalendar.setCurrentItem(currentPos - 1);
        }
    }

    private void createTodayAtLast(int pos) {
        dateInPage.add(pos);
        adapter.addView(new CalendarView(getActivity(), pos));
        if (dateInPage.size() >= MAXIMUN_PAGE) {
            dateInPage.remove(0);
            adapter.removeView(vpCalendar, 0);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        appointmenttoolbar.removeView(toolbarView);
        super.onDestroyView();
    }
}
