package com.kms.cura.view.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.kms.cura.R;
import com.kms.cura.controller.ConditionController;
import com.kms.cura.controller.ErrorController;
import com.kms.cura.controller.SymptomController;
import com.kms.cura.view.fragment.ConditionSearchFragment;

public class ConditionSymptomSearchActivity extends AppCompatActivity {

    public final static String TO_HEALTH_TRACKER = "health_tracker";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition_symptom_search);
        new AsyncTask<Object, Void, Void>() {
            private Exception exception = null;

            @Override
            protected void onPreExecute() {
                pDialog = new ProgressDialog(ConditionSymptomSearchActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                showProgressDialog();
            }

            @Override
            protected Void doInBackground(Object[] params) {
                try {
                    ConditionController.initData();
                    SymptomController.initData();
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressDialog();
                if (exception != null) {
                    ErrorController.showDialog(ConditionSymptomSearchActivity.this, "Error : " + exception.getMessage());
                } else {
                    Fragment conditionSearchFragment = new ConditionSearchFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, conditionSearchFragment).commit();
                }
            }
        }.execute();
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
