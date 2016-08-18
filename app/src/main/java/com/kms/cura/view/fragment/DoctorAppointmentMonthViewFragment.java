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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.view.DepthPageTransformer;
import com.kms.cura.view.DoctorApptDayVIew;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private Date selectedDay;
    private int timeDiff;
    private int INITIAL_SIZE = 12;
    private boolean initialCalendar = false;
    private static int INITIAL_POSITION = 6;

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
        getDataFromDayView();
        vpCalendar = (ViewPager) root.findViewById(R.id.vpCalendar);
        adapter = new CalendarPagerAdapter(createCalendarViewForViewPager());
        vpCalendar.setAdapter(adapter);
        modifyChangeFromDayView();
        setCurrentCalendar();
        vpCalendar.addOnPageChangeListener(this);
        vpCalendar.setPageTransformer(true, new DepthPageTransformer());
        modifyToolbar();
        return root;
    }

    private void getDataFromDayView() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            initialCalendar = true;
            selectedDay = new Date(currentCalendar.getTime().getTime());
        } else {
            selectedDay = new Date(getArguments().getLong(DoctorApptDayVIewFragment.SELECTED_DAY));
        }
        Calendar newCalendar = (Calendar) currentCalendar.clone();
        newCalendar.setTime(selectedDay);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int newMonth = newCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int newYear = newCalendar.get(Calendar.YEAR);
        calculateTimeDiff(currentMonth, newMonth, currentYear, newYear);
    }

    private void modifyChangeFromDayView() {
        if (initialCalendar || timeDiff == 0) {
            return;
        }
        int createMore = 0;
        int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
        if (timeDiff > 0) {
            createMore = timeDiff - (INITIAL_SIZE - currentMonth) + 4;
            currentPos = dateInPage.size() - 2;
            for (int i = 0; i < createMore; ++i) {
                createDateAtLast();
                ++currentPos;
            }
            return;
        }
        currentPos = 1;
        createMore = (Math.abs(timeDiff) - currentMonth) + 4;
        for (int i = 0; i < createMore; ++i) {
            createDateAtFirst();
        }
    }


    private void calculateTimeDiff(int currentMonth, int newMonth, int currentYear, int newYear) {
        if (currentYear == newYear) {
            timeDiff = newMonth - currentMonth;
            return;
        }
        if (currentYear < newYear) {
            timeDiff = (INITIAL_SIZE - currentMonth) + newMonth;
            return;
        }
        timeDiff = -(currentMonth + (INITIAL_SIZE - newMonth));
    }

    private List<CalendarView> createCalendarViewForViewPager() {
        List<CalendarView> views = new ArrayList<>();
        dateInPage = new ArrayList<>();
        for (int i = 0; i < INITIAL_SIZE; ++i) {
            int position = i;
            dateInPage.add(position);
            CalendarView calendarView = new CalendarView(getActivity(), position);
            views.add(calendarView);
        }
        return views;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    private void setCurrentCalendar() {
        if (!initialCalendar) {
            vpCalendar.setCurrentItem(dateInPage.indexOf(currentCalendar.get(Calendar.MONTH) + timeDiff));
            currentPos = vpCalendar.getCurrentItem();
            int position = dateInPage.get(currentPos);
            if (position >= 0) {
                adapter.colorTheSelectedDayFromDayView(selectedDay, position);
            } else {
                adapter.colorTheSelectedDayFromDayView(selectedDay, (dateInPage.size() - currentPos - 1));
            }
            return;
        }
        Calendar calendar = Calendar.getInstance();
        vpCalendar.setCurrentItem(calendar.get(Calendar.MONTH));
        currentPos = vpCalendar.getCurrentItem();
    }

    @Override
    public void onPageSelected(int position) {
        int pos = dateInPage.get(currentPos);
        if (pos >= 0) {
            adapter.clearAlltheContent(pos);
        } else {
            adapter.clearAlltheContent(currentPos);
        }
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
        } else if (id == R.id.btnDayView) {
            DoctorApptDayVIewFragment dayViewFragment = new DoctorApptDayVIewFragment();
            dayViewFragment.setArguments(putDataToFragment());
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.Fragment_UserView, dayViewFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    private Bundle putDataToFragment() {
        Bundle bundle = new Bundle();
        int position = dateInPage.get(currentPos);
        Date selectedDay = null;
        if (position >= 0) {
            selectedDay = adapter.getSelectedDay(position);
        } else {
            selectedDay = adapter.getSelectedDay((dateInPage.size() - currentPos - 1));
        }
        if (selectedDay == null) {
            if (timeDiff == 0) {
                selectedDay = new Date(currentCalendar.getTime().getTime());
            } else {
                Calendar cloneCalendar = (Calendar) currentCalendar.clone();
                cloneCalendar.add(Calendar.MONTH, dateInPage.get(currentPos) - currentCalendar.get(Calendar.MONTH));
                cloneCalendar.set(Calendar.DAY_OF_MONTH, 1);
                selectedDay = new Date(cloneCalendar.getTime().getTime());
            }
        }
        bundle.putLong(DoctorApptDayVIewFragment.SELECTED_DAY, selectedDay.getTime());
        return bundle;
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
