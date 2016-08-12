package com.kms.cura.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.view.dialog.filter.AfterSixFilterData;
import com.kms.cura.view.dialog.filter.BeforeEightFilterData;
import com.kms.cura.view.dialog.filter.BetweenOneAndThreeHundredFilterData;
import com.kms.cura.view.dialog.filter.BetweenThreeAndSixHundredFilterData;
import com.kms.cura.view.dialog.filter.EightToSixFilterData;
import com.kms.cura.view.dialog.filter.FemaleFilterData;
import com.kms.cura.view.dialog.filter.FilterData;
import com.kms.cura.view.dialog.filter.FridayFilterData;
import com.kms.cura.view.dialog.filter.LessThanOneHundredFilterData;
import com.kms.cura.view.dialog.filter.MaleFilterData;
import com.kms.cura.view.dialog.filter.MondayFilterData;
import com.kms.cura.view.dialog.filter.MoreThanSixHundredFilterData;
import com.kms.cura.view.dialog.filter.SaturdayFilterData;
import com.kms.cura.view.dialog.filter.SundayFilterData;
import com.kms.cura.view.dialog.filter.ThursdayFilterData;
import com.kms.cura.view.dialog.filter.TuesdayFilterData;
import com.kms.cura.view.dialog.filter.WednesdayFilterData;

import java.util.ArrayList;
import java.util.List;


public class SearchFilterDialog extends Dialog implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    public static String FILTER_DATA = "FilterData";
    private TextView tvHealthTitle, tvStartDate, tvEndDate, tvTitle, tvReset;
    private boolean edit_mode = false;
    private ImageButton btnStart, btnEnd, btnBack;
    private Toolbar toolbar;
    private List<FilterData> filterData;
    private CheckBox acceptInsurance;
    private List<Integer> oldData;
    private TextView reset;
    private Context context;

    public SearchFilterDialog(final Context context, SearchFilterDialog searchFilterDialog) {
        super(context, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        this.context = context;
        this.setContentView(R.layout.fragment_search_filter);
        if (searchFilterDialog != null) {
            this.filterData = searchFilterDialog.filterData;
        } else {
            initData();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setUpToolbar();
        initOptions();
        initView(filterData);
    }


    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tbSearchFilter);
        toolbar.getMenu().clear();
        tvTitle = (TextView) toolbar.findViewById(R.id.tvFilter);
        tvReset = (TextView) toolbar.findViewById(R.id.tvReset);
        btnBack = (ImageButton) toolbar.findViewById(R.id.button_cancel);

        toolbar.setOnMenuItemClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvReset) {
            for (FilterData dataFilter : filterData) {
                LinearLayout layout = (LinearLayout) findViewById(dataFilter.getId());
                TextView textView = (TextView) layout.getChildAt(0);
                textView.setTextColor(context.getResources().getColor(R.color.black));

                dataFilter.setBoo(false);
                layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            }
            acceptInsurance.setChecked(false);
            return;
        } else if (v.getId() == R.id.button_apply) {
            this.cancel();
        } else if (v.getId() == R.id.button_cancel) {
            for (FilterData data : filterData) {
                for (int i : oldData) {
                    data.setBoo(i == data.getId());
                }
            }
            initView(filterData);
            this.dismiss();
        } else {
            for (FilterData data : filterData) {
                if (data.getId() == v.getId()) {
                    setFilterState(data);
                }
            }
        }
    }

    private void initData() {
        filterData = new ArrayList<FilterData>();

        acceptInsurance = (CheckBox) findViewById(R.id.checkbox_accept_insurance);

        filterData.add(new MaleFilterData());
        filterData.add(new FemaleFilterData());

        filterData.add(new SundayFilterData());
        filterData.add(new MondayFilterData());
        filterData.add(new TuesdayFilterData());
        filterData.add(new WednesdayFilterData());
        filterData.add(new ThursdayFilterData());
        filterData.add(new FridayFilterData());
        filterData.add(new SaturdayFilterData());

        filterData.add(new BeforeEightFilterData());
        filterData.add(new EightToSixFilterData());
        filterData.add(new AfterSixFilterData());

        filterData.add(new LessThanOneHundredFilterData());
        filterData.add(new BetweenOneAndThreeHundredFilterData());
        filterData.add(new BetweenThreeAndSixHundredFilterData());
        filterData.add(new MoreThanSixHundredFilterData());

        for (FilterData data : filterData) {
            LinearLayout layout = (LinearLayout) findViewById(data.getId());
            layout.setOnClickListener(this);
        }
    }

    private void initView(List<FilterData> datas) {
        for (FilterData data : datas) {
            LinearLayout layout = (LinearLayout) findViewById(data.getId());
            TextView textView = (TextView) layout.getChildAt(0);
            if (data.getBoo()) {
                layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                textView.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                textView.setTextColor(context.getResources().getColor(R.color.black));
            }
        }
    }

    private void initOptions() {
        reset = (TextView) findViewById(R.id.tvReset);
        reset.setOnClickListener(this);


        ImageButton cancel = (ImageButton) findViewById(R.id.button_cancel);
        Button apply = (Button) findViewById(R.id.button_apply);
        apply.setOnClickListener(this);

        cancel.setOnClickListener(this);
    }

    public List<FilterData> getData() {
        List<FilterData> result = new ArrayList<FilterData>();

        for (FilterData data : filterData) {
            if (data.getBoo()) {
                result.add(data);
            }
        }
        return result;
    }

    public void saveFilterData() {
        oldData = new ArrayList<Integer>();
        for (FilterData data : filterData) {
            if (data.getBoo()) {
                oldData.add(data.getId());
            }
        }
    }

    private void setFilterState(FilterData data) {
        LinearLayout layout = (LinearLayout) findViewById(data.getId());
        TextView textView = (TextView) layout.getChildAt(0);

        if (!data.getBoo()) {
            layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
            textView.setTextColor(context.getResources().getColor(R.color.white));
            data.setBoo(true);
        } else {
            layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            textView.setTextColor(context.getResources().getColor(R.color.black));
            data.setBoo(false);
        }
    }

    public CheckBox getAcceptInsurance() {
        return acceptInsurance;
    }


}
