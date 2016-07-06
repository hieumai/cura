package com.kms.cura.view.activity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kms.cura.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class ChooseTimeActivity extends AppCompatActivity implements View.OnDragListener, View.OnLongClickListener, View.OnClickListener {
    private LinearLayout layoutChoseTime;
    private ImageView btnDone, btnBack;
    private TextView txtDay;
    private Bundle bundle;
    private int day, month, year;
    private int apptLength;
    private ScrollView scrollView;
    private int red, green, lightGrey, lightGrey2, grey;
    private int length;
    private int increasement;
    private int screenHeight;
    /**
     * Each element in this list can has one of three value :
     * + 1 : available
     * + 0 : unavailable
     **/
    private int[] availableStatus;

    private LayoutInflater inflater;
    private String[] timeFrame;
    private int[] tags;
    private List<Integer> selectedTime;
    private List<Integer> tempSelectedTime;
    private List<View> viewList;
    private int indexWhereclick;

    //------------------------------------------
    private boolean onSelecting = false;
    private boolean selectRight = false;
    public static String START_TIME = "start_time";
    public static String END_TIME = "end_time";
    private int[] months = {Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY,
            Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER,
            Calendar.NOVEMBER, Calendar.DECEMBER, Calendar.UNDECIMBER};

    //------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        getScreenHeight();
        getColor();
        initView();
        loadView();
    }

    public void initView() {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutChoseTime = (LinearLayout) findViewById(R.id.layoutTime);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnDone = (ImageView) findViewById(R.id.btnDone);
        txtDay = (TextView) findViewById(R.id.txtDay);
        btnDone.setVisibility(View.INVISIBLE);
        btnDone.setOnClickListener(this);
        getData();
    }

    public void loadView() {
        txtDay.setText(getSelectedDate());
        seedData();
        createTimeFrame();

    }

    public void getData() {
        bundle = getIntent().getExtras();
        day = bundle.getInt(BookAppointmentActivity.DATE_SELECTED);
        month = bundle.getInt(BookAppointmentActivity.MONTH_SELECTED);
        year = bundle.getInt(BookAppointmentActivity.YEAR_SELECTED);
        apptLength = bundle.getInt(BookAppointmentActivity.LENGTH_SELECTED);
        getTimeSpan();
    }

    public String getSelectedDate() {
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        String dayOftheWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        StringBuilder builder = new StringBuilder();
        builder.append(dayOftheWeek);
        builder.append(", ");
        builder.append(month_name);
        builder.append(" ");
        builder.append(day);
        builder.append(", ");
        builder.append(year);
        return builder.toString();
    }

    private void seedData() {
        tags = new int[length];
        for (int i = 0; i < length; ++i) {
            tags[i] = i;
        }
        selectedTime = new ArrayList<>();
    }

    private void createTimeFrame() {
        viewList = new ArrayList<>();
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < length; ++i) {
            layoutChoseTime.addView(createTimeView(i));
        }
        if (layoutChoseTime.getParent() != null) {
            ((ViewGroup) layoutChoseTime.getParent()).removeView(layoutChoseTime);
        }
        scrollView.addView(layoutChoseTime);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onSelecting;
            }
        });
        ((ViewGroup) txtDay.getParent()).addView(scrollView);
    }

    private View createTimeView(int position) {
        View view = inflater.inflate(R.layout.time_selection_layout, null);
        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
        TextView txtTimeChosen = (TextView) view.findViewById(R.id.txtTimeChosen);
        txtTime.setText(timeFrame[position]);
        if (position % 2 == 0) {
            txtTime.setBackgroundResource(R.drawable.border_right);
            txtTimeChosen.setBackgroundColor(lightGrey);
        } else {
            txtTime.setBackgroundResource(R.drawable.border_right_2);
            txtTimeChosen.setBackgroundColor(lightGrey2);
        }
        switch (availableStatus[position]) {
            case 0:
                txtTimeChosen.setBackgroundColor(grey);
                break;
            case -1:
                break;
        }
        txtTimeChosen.setTag(tags[position]);
        txtTimeChosen.setOnLongClickListener(this);
        txtTimeChosen.setOnDragListener(this);
        txtTimeChosen.setOnClickListener(this);
        viewList.add(txtTimeChosen);
        return view;
    }


    @Override
    public boolean onDrag(View v, DragEvent event) {
        int position = (int) v.getTag();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                dragStart();
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if(position < increasement){
                    colorTheTop();
                }
                else if(position > length - increasement){
                    colorThelast();
                }
                else {
                    dragEnter(position);
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                dragExit();
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                autoScroll(v, event);
                break;
            case DragEvent.ACTION_DROP:
                dragDrop();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                onSelecting = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void colorThelast(){
        List<Integer> listTmp = new ArrayList<>();
        int backgroundColor = 0;
        boolean check = true;
        for (int i = length - increasement; i < length; ++i){
            if(availableStatus[i] ==0){
                check = false;
            }
            listTmp.add(i);
        }
        if(check){
            backgroundColor = green;
        }
        else{
            backgroundColor = red;
        }
        for (int i = length - increasement; i < length; ++i){
            viewList.get(i).setBackgroundColor(backgroundColor);
        }
        tempSelectedTime.clear();
        tempSelectedTime.addAll(listTmp);
    }

    private void colorTheTop(){
        List<Integer> listTmp = new ArrayList<>();
        int backgroundColor = 0;
        boolean check = true;
        for (int i = 0; i < increasement; ++i){
            if(availableStatus[i] ==0){
                check = false;
            }
            listTmp.add(i);
        }
        if(check){
            backgroundColor = green;
        }
        else{
            backgroundColor = red;
        }
        for (int i = 0; i < increasement; ++i){
            viewList.get(i).setBackgroundColor(backgroundColor);
        }
        tempSelectedTime.clear();
        tempSelectedTime.addAll(listTmp);
    }

    private void dragStart() {
        for (int i = 0; i < selectedTime.size(); ++i) {
            int pos = selectedTime.get(i);
            if (availableStatus[pos] == 0) {
                viewList.get(pos).setBackgroundColor(grey);
            } else if (pos % 2 == 0) {
                viewList.get(pos).setBackgroundColor(lightGrey);
            } else {
                viewList.get(pos).setBackgroundColor(lightGrey2);
            }
        }
    }

    private void dragEnter(int position) {
        int change = 1;
        if (position < tempSelectedTime.get(indexWhereclick)) {
            change = -1;
        } else if (position == tempSelectedTime.get(indexWhereclick)) {
            change = 0;
        }
        int backgroundColor = 0;
        if (checkOneWayAvailable(change)) {
            backgroundColor = green;
        } else {
            backgroundColor = red;
        }
        if (tempSelectedTime.contains(position)) {
            indexWhereclick = tempSelectedTime.indexOf(position);
        }
        for (int i = 0; i < increasement; ++i) {
            int pos = tempSelectedTime.get(i);
            viewList.get(pos).setBackgroundColor(backgroundColor);
        }
    }

    private void dragExit() {
        for (int i = 0; i < increasement; ++i) {
            int pos = tempSelectedTime.get(i);
            if (availableStatus[pos] == 0) {
                viewList.get(pos).setBackgroundColor(grey);
            } else if (pos % 2 == 0) {
                viewList.get(pos).setBackgroundColor(lightGrey);
            } else {
                viewList.get(pos).setBackgroundColor(lightGrey2);
            }
        }
    }

    private boolean checkOneWayAvailable(int change) {
        boolean check = true;
        List<Integer> listtempSelected = getTranslate1TempSelected(tempSelectedTime, change);
        for (int i = 0; i < increasement; ++i) {
            int pos = tempSelectedTime.get(i) + change;
            if (pos >= 0 && pos < length) {
                if (availableStatus[pos] == 0) {
                    check = false;
                }
            }
        }
        tempSelectedTime.clear();
        tempSelectedTime.addAll(listtempSelected);
        return check;
    }

    private List<Integer> getTranslate1TempSelected(List<Integer> tempSelectedTime, int change) {
        ArrayList<Integer> listtempSelected = new ArrayList<>();
        for (int k = 0; k < increasement; ++k) {
            int pos = tempSelectedTime.get(k) + change;
            if (pos >= 0 && pos < length) {
                listtempSelected.add(pos);
            }

        }
        return listtempSelected;
    }


    private void autoScroll(View v, DragEvent event) {
        Point touchPosition = getTouchPositionFromDragEvent(v, event);
        if (screenHeight - touchPosition.y < 200) {
            scrollView.smoothScrollBy(0, 15);
            return;
        }
        if (touchPosition.y > 200 && touchPosition.y < 700) {
            scrollView.smoothScrollBy(0, -15);
        }
    }

    private boolean checkDrop() {
        for (int i = 0; i < increasement; ++i) {
            if (availableStatus[tempSelectedTime.get(i)] == 0) {
                return false;
            }
        }
        return true;
    }

    private void dragDrop() {
        btnDone.setVisibility(View.VISIBLE);
        int backgroundColor = 0;
        if (checkDrop()) {
            backgroundColor = green;
            selectRight = true;
            btnDone.setImageResource(R.drawable.done_button);
        } else {
            backgroundColor = red;
            selectRight = false;
            btnDone.setImageResource(R.drawable.error_button);
        }
        selectedTime.clear();
        selectedTime.addAll(tempSelectedTime);
        for (int i = 0; i < increasement; ++i) {
            int pos = tempSelectedTime.get(i);
            viewList.get(pos).setBackgroundColor(backgroundColor);

        }
        onSelecting = false;
    }

    private void getColor() {
        red = ContextCompat.getColor(this, R.color.red);
        green = ContextCompat.getColor(this, R.color.light_green);
        lightGrey = ContextCompat.getColor(this, R.color.light_grey);
        lightGrey2 = ContextCompat.getColor(this, R.color.light_grey_2);
        grey = ContextCompat.getColor(this, R.color.grey2);
    }

    @Override
    public boolean onLongClick(View v) {
        onSelecting = true;
        int position = (int) v.getTag();
        if (selectedTime.size() == 0) {
            colorTheSelected(position);
        }
        if (selectedTime.contains(position)) {
            tempSelectedTime = new ArrayList<>();
            tempSelectedTime.addAll(selectedTime);
            indexWhereclick = selectedTime.indexOf(position);
            for (int i = 0; i < selectedTime.size(); ++i) {
                View view = viewList.get(i);
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(null);
                view.startDrag(data, shadowBuilder, view, 0);
            }
            return true;
        }
        return false;
    }

    private void getTimeSpan() {
        length = 48;
        increasement = apptLength;
        timeFrame = getResources().getStringArray(R.array.TimeFrame48);
        availableStatus = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1};
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnDone) {
            clickDone();
        } else {
            clickOnTime(v);
        }
    }

    private void clickDone() {
        if (selectRight) {
            Collections.sort(selectedTime);
            Intent back = new Intent();
            Bundle bundle2 = new Bundle();
            int indexEnd = selectedTime.get(increasement - 1);
            bundle2.putString(START_TIME, timeFrame[selectedTime.get(0)]);
            if (indexEnd == length - 1) {
                indexEnd = -1;
            }
            bundle2.putString(END_TIME, timeFrame[indexEnd + 1]);
            back.putExtras(bundle2);
            setResult(BookAppointmentActivity.RESULT_OK, back);
            finish();
            return;
        }
        uncolorSelected();
    }

    private void clickOnTime(View v) {
        int position = (int) v.getTag();
        if (selectedTime.size() == 0) {
            colorTheSelected(position);
            return;
        }
        selectRight = false;
        if (selectedTime.contains(position)) {
            uncolorSelected();
        } else if (checkAvailable(position)) {
            colorTheSelected(position);
        }
    }

    private boolean checkAvailable(int postion) {
        ArrayList<Integer> selected = new ArrayList<>();
        for (int i = 0; i < increasement; ++i) {
            if (postion + i >= length || availableStatus[postion + i] == 0) {
                break;
            }
            selected.add(postion + i);
        }
        if (selected.size() == increasement) {
            uncolorSelected();
            Collections.sort(selected);
            selectedTime.addAll(selected);
            return true;
        }
        if (selected.contains(postion)) {
            selected.remove(0);
        }
        for (int i = 0; i < increasement; ++i) {
            if (postion - i < 0 || availableStatus[postion - i] == 0) {
                break;
            }
            selected.add(postion - i);
            if (selected.size() == increasement) {
                uncolorSelected();
                selectedTime.addAll(selected);
                return true;
            }
        }
        return false;
    }


    private void colorTheSelected(int position) {
        if (!checkAvailable(position)) {
            return;
        }
        for (int i = 0; i < selectedTime.size(); ++i) {
            viewList.get(selectedTime.get(i)).setBackgroundColor(green);
        }

        selectRight = true;
        btnDone.setVisibility(View.VISIBLE);
        btnDone.setImageResource(R.drawable.done_button);

    }

    private void uncolorSelected() {
        for (int i = 0; i < selectedTime.size(); ++i) {
            int pos = selectedTime.get(i);
            if (availableStatus[pos] == 0) {
                viewList.get(pos).setBackgroundColor(grey);
            } else if (pos % 2 == 0) {
                viewList.get(pos).setBackgroundColor(lightGrey);
            } else {
                viewList.get(pos).setBackgroundColor(lightGrey2);
            }
            btnDone.setVisibility(View.INVISIBLE);
        }
        selectedTime.clear();
    }

    public Point getTouchPositionFromDragEvent(View item, DragEvent event) {
        Rect rItem = new Rect();
        item.getGlobalVisibleRect(rItem);
        return new Point(rItem.left + Math.round(event.getX()), rItem.top + Math.round(event.getY()));
    }

    public void getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
    }

}
