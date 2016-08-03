package com.kms.cura.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kms.cura.R;
import com.kms.cura.entity.HealthEntity;

import java.util.ArrayList;


public class SearchFilterFragment extends Fragment implements Toolbar.OnMenuItemClickListener, View.OnClickListener{

    private TextView tvHealthTitle, tvStartDate, tvEndDate, tvTitle, tvReset;
    private boolean edit_mode = false;
    private ImageButton btnStart, btnEnd, btnBack;
    private FrameLayout commentLayout;
    private EditText edtComment;
    private Toolbar toolbar;


    public SearchFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_filter, container, false);
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.tbSearchFilter);
        toolbar.getMenu().clear();
        tvTitle = (TextView) toolbar.findViewById(R.id.tvFilter);
        tvReset = (TextView) toolbar.findViewById(R.id.tvReset);
        btnBack = (ImageButton) toolbar.findViewById(R.id.button_cancel);

        toolbar.inflateMenu(R.menu.menu_filter);
        toolbar.setOnMenuItemClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
