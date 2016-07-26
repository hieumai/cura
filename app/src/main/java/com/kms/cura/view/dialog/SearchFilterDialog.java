package com.kms.cura.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kms.cura.R;

import java.util.ArrayList;
import java.util.List;


public class SearchFilterDialog extends Dialog implements Toolbar.OnMenuItemClickListener, View.OnClickListener {
    public static String FILTER_DATA = "FilterData";
    private TextView tvHealthTitle, tvStartDate, tvEndDate, tvTitle, tvReset;
    private boolean edit_mode = false;
    private ImageButton btnStart, btnEnd, btnBack;
    private FrameLayout commentLayout;
    private EditText edtComment;
    private Toolbar toolbar;
    private List<FilterData> filterData;
    private CheckBox acceptInsurance;
    private TextView reset;
    private Intent intent;
    private String searchData, filterRawData;
    private Context context;

    public SearchFilterDialog(final Context context, SearchFilterDialog searchFilterDialog) {
        // Set your theme here
        super(context, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        this.context = context;
        // This is the layout XML file that describes your Dialog layout
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
        FilterData data = null;
        if (v.getId() == R.id.male) {
            data = getData(R.id.male);
        } else if (v.getId() == R.id.female) {
            data = getData(R.id.female);
        } else if (v.getId() == R.id.sunday) {
            data = getData(R.id.sunday);
        } else if (v.getId() == R.id.monday) {
            data = getData(R.id.monday);
        } else if (v.getId() == R.id.tuesday) {
            data = getData(R.id.tuesday);
        } else if (v.getId() == R.id.wednesday) {
            data = getData(R.id.wednesday);
        } else if (v.getId() == R.id.thursday) {
            data = getData(R.id.thursday);
        } else if (v.getId() == R.id.friday) {
            data = getData(R.id.friday);
        } else if (v.getId() == R.id.saturday) {
            data = getData(R.id.saturday);
        } else if (v.getId() == R.id.beforeEight) {
            data = getData(R.id.beforeEight);
        } else if (v.getId() == R.id.eightToSix) {
            data = getData(R.id.eightToSix);
        } else if (v.getId() == R.id.afterSix) {
            data = getData(R.id.afterSix);
        } else if (v.getId() == R.id.less_than_100) {
            data = getData(R.id.less_than_100);
        } else if (v.getId() == R.id.between_100_and_300) {
            data = getData(R.id.between_100_and_300);
        } else if (v.getId() == R.id.between_301_and_600) {
            data = getData(R.id.between_301_and_600);
        } else if (v.getId() == R.id.more_than_600) {
            data = getData(R.id.more_than_600);
        } else if (v.getId() == R.id.tvReset) {
            for (FilterData dataFilter : filterData) {
                LinearLayout layout = dataFilter.getLinearLayout();
                TextView textView = (TextView) layout.getChildAt(0);
                textView.setTextColor(context.getResources().getColor(R.color.black));

                dataFilter.setBoo(false);
                layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            }
            acceptInsurance.setChecked(false);
            return;
        } else if (v.getId() == R.id.button_apply) {
            this.dismiss();
            return;
        }
        setFilterState(data);
    }

    private void initData() {
        filterData = new ArrayList<FilterData>();

        acceptInsurance = (CheckBox) findViewById(R.id.checkbox_accept_insurance);

        filterData.add(new FilterData((LinearLayout) findViewById(R.id.male), R.id.male));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.female), R.id.female));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.sunday), R.id.sunday));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.monday), R.id.monday));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.tuesday), R.id.tuesday));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.wednesday), R.id.wednesday));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.thursday), R.id.thursday));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.friday), R.id.friday));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.saturday), R.id.saturday));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.beforeEight), R.id.beforeEight));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.eightToSix), R.id.eightToSix));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.afterSix), R.id.afterSix));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.less_than_100), R.id.less_than_100));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.between_100_and_300), R.id.between_100_and_300));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.between_301_and_600), R.id.between_301_and_600));
        filterData.add(new FilterData((LinearLayout) findViewById(R.id.more_than_600), R.id.more_than_600));

        for (FilterData data : filterData) {
            data.getLinearLayout().setOnClickListener(this);
        }
    }

    private void initView(List<FilterData> datas) {
        for (FilterData data : datas) {
            TextView textView = (TextView) data.getLinearLayout().getChildAt(0);
            if (data.getBoo() == true) {
                data.getLinearLayout().setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                textView.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                data.getLinearLayout().setBackgroundColor(context.getResources().getColor(R.color.transparent));
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private FilterData getData(int id) {
        for (FilterData data : filterData) {
            if (data.getId() == id) {
                return data;
            }
        }
        return null;
    }

    private void setFilterState(FilterData data) {
        TextView textView = (TextView) data.getLinearLayout().getChildAt(0);

        if (data.getBoo() == false) {
            data.getLinearLayout().setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
            textView.setTextColor(context.getResources().getColor(R.color.white));
            data.setBoo(true);
        } else {
            data.getLinearLayout().setBackgroundColor(context.getResources().getColor(R.color.transparent));
            textView.setTextColor(context.getResources().getColor(R.color.black));
            data.setBoo(false);
        }
    }

    public CheckBox getAcceptInsurance() {
        return acceptInsurance;
    }

    public List<FilterData> getFilterData() {
        return filterData;
    }

    public class FilterData {
        LinearLayout linearLayout;
        boolean boo;
        int id;

        FilterData(LinearLayout linearLayout, int id) {
            this.linearLayout = linearLayout;
            this.boo = false;
            this.id = id;
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

        public void setLinearLayout(LinearLayout linearLayout) {
            this.linearLayout = linearLayout;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean getBoo() {
            return boo;
        }

        public void setBoo(boolean boo) {
            this.boo = boo;
        }
    }
}
