package com.kms.cura.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by linhtnvo on 7/20/2016.
 */
public class CustomCalendarView extends LinearLayout {
    private Context mContext;

    private View view;

    private CalendarListener calendarListener;
    private Calendar currentCalendar;
    private Locale locale;

    private Date lastSelectedDay;
    private Typeface customTypeface;

    private int firstDayOfWeek = Calendar.SUNDAY;

    private List<DayDecorator> decorators = null;

    private static final String DAY_OF_WEEK = "dayOfWeek";
    private static final String DAY_OF_MONTH_TEXT = "dayOfMonthText";
    private static final String DAY_OF_MONTH_CONTAINER = "dayOfMonthContainer";
    private static final String LAST_WEEK_ROW = "weekRow6";
    private static final String FIRST_DAY_OF_LAST_WEEK_ROW = "dayOfMonthText36";
    private int disabledDayBackgroundColor;
    private int disabledDayTextColor;
    private int calendarBackgroundColor;
    private int weekLayoutBackgroundColor;
    private int calendarTitleBackgroundColor;
    private int calendarTitleTextColor;
    private int dayOfWeekTextColor;
    private int dayOfMonthTextColor;
    private int currentDayOfMonth;
    private String textTitle;

    private int currentMonthIndex = 0;
    private boolean isOverflowDateVisible = true;
    private int previousSelected = 0;
    private List<Integer> apptDayNumber = new ArrayList<>();
    private static int MAXIMUM_DAYVIEW = 42;
    private static float NUMBER_DAY_PER_WEEK = 7.0f;
    private static int MAXIMUM_DAY_VISIBLE = 36;
    private int black, white, curentDay;
    private Date selectedDayNotHaveAppt;

    public CustomCalendarView(Context mContext) {
        this(mContext, null);
    }

    public CustomCalendarView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE && isInEditMode()) {
            return;
        }

        getAttributes(attrs);

        initializeCalendar();
    }

    private void getAttributes(AttributeSet attrs) {
        getColor();
        final TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CustomCalendarView, 0, 0);
        calendarBackgroundColor = typedArray.getColor(R.styleable.CustomCalendarView_calendarBackgroundColor, white);
        calendarTitleBackgroundColor = typedArray.getColor(R.styleable.CustomCalendarView_titleLayoutBackgroundColor, white);
        calendarTitleTextColor = typedArray.getColor(R.styleable.CustomCalendarView_calendarTitleTextColor, black);
        weekLayoutBackgroundColor = typedArray.getColor(R.styleable.CustomCalendarView_weekLayoutBackgroundColor, white);
        dayOfWeekTextColor = typedArray.getColor(R.styleable.CustomCalendarView_dayOfWeekTextColor, black);
        dayOfMonthTextColor = typedArray.getColor(R.styleable.CustomCalendarView_dayOfMonthTextColor, black);
        disabledDayBackgroundColor = typedArray.getColor(R.styleable.CustomCalendarView_disabledDayBackgroundColor, disabledDayBackgroundColor);
        disabledDayTextColor = typedArray.getColor(R.styleable.CustomCalendarView_disabledDayTextColor, disabledDayTextColor);
        currentDayOfMonth = typedArray.getColor(R.styleable.CustomCalendarView_currentDayOfMonthColor, curentDay);
        typedArray.recycle();
    }

    private void getColor() {
        black = ContextCompat.getColor(mContext, R.color.black);
        white = ContextCompat.getColor(mContext, R.color.white);
        disabledDayBackgroundColor = ContextCompat.getColor(mContext, R.color.day_disabled_background_color);
        disabledDayTextColor = ContextCompat.getColor(mContext, R.color.day_disabled_text_color);
        curentDay = ContextCompat.getColor(mContext, R.color.colorPrimaryDark);
    }

    private void initializeCalendar() {
        final LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(R.layout.custom_calendar_layout, this, true);

        // Initialize calendar for current month
        Locale locale = mContext.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);

        setFirstDayOfWeek(Calendar.SUNDAY);
        refreshCalendar(currentCalendar);
    }

    public void changeMonthCalendar(){
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        refreshCalendar(currentCalendar);
    }

    public int getCurrentMonthIndex(){
        return this.currentMonthIndex;
    }

    /**
     * Display calendar title with next previous month button
     */
    private void initializeTextTitle() {

        String dateText = new DateFormatSymbols(locale).getShortMonths()[currentCalendar.get(Calendar.MONTH)].toString();
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
        StringBuilder builder = new StringBuilder();
        builder.append(dateText);
        builder.append(" ");
        builder.append(currentCalendar.get(Calendar.YEAR));
        textTitle = builder.toString();
    }

    public String getTextTitle(){
        return this.textTitle;
    }

    /**
     * Initialize the calendar week layout, considers start day
     */
    @SuppressLint("DefaultLocale")
    private void initializeWeekLayout() {
        TextView dayOfWeek;
        String dayOfTheWeekString;

        //Setting background color white
        View titleLayout = view.findViewById(R.id.weekLayout);
        titleLayout.setBackgroundColor(weekLayoutBackgroundColor);

        final String[] weekDaysArray = new DateFormatSymbols(locale).getShortWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++) {
            dayOfTheWeekString = weekDaysArray[i];
            if (dayOfTheWeekString.length() > 3) {
                dayOfTheWeekString = dayOfTheWeekString.substring(0, 3).toUpperCase();
            }

            dayOfWeek = (TextView) view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(i, currentCalendar));
            dayOfWeek.setText(dayOfTheWeekString.toUpperCase());
            dayOfWeek.setTextColor(dayOfWeekTextColor);

            if (null != getCustomTypeface()) {
                dayOfWeek.setTypeface(getCustomTypeface());
            }
        }
    }

    private void setDaysInCalendar() {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);

        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, calendar);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        final Calendar startCalendar = (Calendar) calendar.clone();
        //Add required number of days
        startCalendar.add(Calendar.DATE, -(dayOfMonthIndex - 1));
        int monthEndIndex = MAXIMUM_DAYVIEW - (actualMaximum + dayOfMonthIndex - 1);

        DayView dayView;
        ViewGroup dayOfMonthContainer;
        for (int i = 1; i < MAXIMUM_DAYVIEW + 1; i++) {
            dayOfMonthContainer = (ViewGroup) view.findViewWithTag(DAY_OF_MONTH_CONTAINER + i);
            dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
            if (dayView == null)
                continue;

            //Apply the default styles
            dayOfMonthContainer.setOnClickListener(null);
            dayView.bind(startCalendar.getTime(), getDecorators());
            dayView.setVisibility(View.VISIBLE);
            if (dayView.isOnDraw()){
                dayView.setOnDraw(false);
            }
            if (null != getCustomTypeface()) {
                dayView.setTypeface(getCustomTypeface());
            }

            if (isSameMonth(calendar, startCalendar)) {
                dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener);
                dayView.setBackgroundColor(calendarBackgroundColor);
                dayView.setTextColor(dayOfWeekTextColor);
                //Set the current day color
                markDayAsCurrentDay(startCalendar);

            } else {
                if (!isOverflowDateVisible()) {
                    dayView.setAlpha(0.4f);
                    dayView.setTextColor(Color.WHITE);
                    dayView.setClickable(false);
                }
                if (i >= MAXIMUM_DAY_VISIBLE && ((float) monthEndIndex / NUMBER_DAY_PER_WEEK) >= 1) {
                    dayView.setVisibility(View.GONE);
                }
            }
            startCalendar.add(Calendar.DATE, 1);
            dayOfMonthIndex++;
        }

        // If the last week row has no visible days, hide it or show it in case
        ViewGroup weekRow = (ViewGroup) view.findViewWithTag(LAST_WEEK_ROW);
        dayView = (DayView) view.findViewWithTag(FIRST_DAY_OF_LAST_WEEK_ROW);
        if (dayView.getVisibility() != VISIBLE) {
            weekRow.setVisibility(GONE);
        } else {
            weekRow.setVisibility(VISIBLE);
        }
    }


    public boolean isSameMonth(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return false;
        return (c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }

    /**
     * <p>Checks if a calendar is today.</p>
     *
     * @param calendar the calendar, not altered, not null.
     * @return true if the calendar is today.
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar calendar) {
        return isSameDay(calendar, Calendar.getInstance());
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }


    private void clearDayOfTheMonthStyle(Date currentDate) {
        if (currentDate == null) {
            return;
        }
        final Calendar calendar = getTodaysCalendar();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentDate);

        final DayView dayView = getDayOfMonthText(calendar);
        dayView.setBackgroundColor(calendarBackgroundColor);
        dayView.setTextColor(dayOfWeekTextColor);
        dayView.decorate();
    }

    private DayView getDayOfMonthText(Calendar currentCalendar) {
        return (DayView) getView(DAY_OF_MONTH_TEXT, currentCalendar);
    }

    private int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int index = currentDay + monthOffset;
        return index;
    }

    public void setPreviousSelected(int previousSelected) {
        this.previousSelected = previousSelected;
    }


    private int getMonthOffset(Calendar currentCalendar) {
        final Calendar calendar = Calendar.getInstance();

        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else if (dayPosition == 1) {
            return 6;
        } else {
            return dayPosition - 2;
        }
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();
        if (firstDayWeekPosition == 1) {
            return weekIndex;
        }
        if (weekIndex == 1) {
            return 7;
        }
        return weekIndex - 1;
    }

    private View getView(String key, Calendar currentCalendar) {
        int index = getDayIndexByDate(currentCalendar);
        View childView = view.findViewWithTag(key + index);
        return childView;
    }

    private Calendar getTodaysCalendar() {
        Calendar currentCalendar = Calendar.getInstance(mContext.getResources().getConfiguration().locale);
        currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        return currentCalendar;
    }

    @SuppressLint("DefaultLocale")
    public void refreshCalendar(Calendar currentCalendar) {
        this.currentCalendar = currentCalendar;
        this.currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        locale = mContext.getResources().getConfiguration().locale;

        // Set text title
        initializeTextTitle();

        // Set weeks days titles
        initializeWeekLayout();

        // Initialize and set days in calendar
        setDaysInCalendar();
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public void markDayAsCurrentDay(Calendar calendar) {
        if (calendar == null) {
            return;
        }
        if (!isToday(calendar)) {
            return;
        }
        DayView dayOfMonth = getDayOfMonthText(calendar);
        dayOfMonth.setOnDraw(true);
        dayOfMonth.invalidate();
        dayOfMonth.setTextColor(currentDayOfMonth);
        dayOfMonth.setOnDraw(true);
        dayOfMonth.setmColor(Color.WHITE);
        int dayNumber = calendar.get(Calendar.DAY_OF_MONTH) + getMonthOffset(calendar);
        if (calendarListener == null) {
            return;
        }
        if (apptDayNumber.contains(dayNumber)) {
            calendarListener.onDateSelected(calendar.getTime());
        } else {
            calendarListener.onDateSelected(null);
        }
    }



    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    private OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            ViewGroup dayOfMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfMonthContainer.getTag();
            tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length(), tagId.length());
            final DayView dayOfMonthText = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + tagId);
            colorSelectedDay(dayOfMonthText, tagId);
            final Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(getFirstDayOfWeek());
            calendar.setTime(currentCalendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dayOfMonthText.getText().toString()));
            if (calendar == null) {
                return;
            }
            if (apptDayNumber.contains(Integer.parseInt(tagId))) {
                calendarListener.onDateSelected(calendar.getTime());
            } else {
                selectedDayNotHaveAppt = calendar.getTime();
                calendarListener.onDateSelected(null);
            }
        }
    };

    private void colorSelectedDay(DayView dayOfMonthText, String tagID) {
        int id = Integer.parseInt(tagID);
        if (previousSelected != 0) {
            unColorThePriviousSelected();
        }
        previousSelected = id;
        dayOfMonthText.setOnSelected(true);
        dayOfMonthText.invalidate();
    }

    public void colorSelectedDay(java.sql.Date selectedDate) {
        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.setTime(selectedDate);
        DayView dayOfMonth = getDayOfMonthText(calendar);
        String tagId = (String) ((ViewGroup)dayOfMonth.getParent()).getTag();
        tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length(), tagId.length());
        int id = Integer.parseInt(tagId);
        if (previousSelected != 0) {
            unColorThePriviousSelected();
        }
        previousSelected = id;
        dayOfMonth.setOnSelected(true);
        dayOfMonth.invalidate();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        if (calendar == null) {
            return;
        }
        if (apptDayNumber.contains(Integer.parseInt(tagId))) {
            calendarListener.onDateSelected(calendar.getTime());
        } else {
            selectedDayNotHaveAppt = calendar.getTime();
            calendarListener.onDateSelected(null);
        }
    }

    public Date getSelectedDayNotHaveAppt() {
        return selectedDayNotHaveAppt;
    }

    public void unColorThePriviousSelected() {
        if (previousSelected == 0){
            return;
        }
        String previousSelect = String.valueOf(previousSelected);
        ViewGroup previous = (ViewGroup) findViewWithTag(DAY_OF_MONTH_CONTAINER + previousSelect);
        DayView dayView = (DayView) previous.findViewWithTag(DAY_OF_MONTH_TEXT + String.valueOf(previousSelected));
        if (apptDayNumber.contains(previousSelected)) {
            dayView.decorate();
            dayView.setOnSelected(false);
            dayView.invalidate();
        } else {
            dayView.setOnSelected(false);
            dayView.invalidate();
        }
    }


    public List<DayDecorator> getDecorators() {
        return decorators;
    }

    public void setDecorators(List<DayDecorator> decorators) {
        this.decorators = decorators;
    }

    public boolean isOverflowDateVisible() {
        return isOverflowDateVisible;
    }

    public void setShowOverflowDate(boolean isOverFlowEnabled) {
        isOverflowDateVisible = isOverFlowEnabled;
    }

    public void setCustomTypeface(Typeface customTypeface) {
        this.customTypeface = customTypeface;
    }

    public Typeface getCustomTypeface() {
        return customTypeface;
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }

    public void decorateApptDay(DayDecorator decorator, List<java.sql.Date> apptDay) {
        apptDayNumber.clear();
        for (int i = 0; i < apptDay.size(); ++i) {
            java.sql.Date day = apptDay.get(i);
            Calendar calendar = (Calendar) Calendar.getInstance().clone();
            calendar.setTime(day);
            if (calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
                int dayNumber = calendar.get(Calendar.DAY_OF_MONTH) + getMonthOffset(currentCalendar);
                ViewGroup view = (ViewGroup) findViewWithTag(DAY_OF_MONTH_CONTAINER + dayNumber);
                DayView dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + dayNumber);
                dayView.decorate(decorator);
                apptDayNumber.add(dayNumber);
            }
        }
    }

    public void setCurrentMonthIndex(int currentMonthIndex) {
        this.currentMonthIndex = currentMonthIndex;
    }

}
