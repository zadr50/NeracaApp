package com.talagasoft.neraca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReportAction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_action);
        Bundle bundle= getIntent().getExtras();
        int idx=bundle.getInt("report_idx");


    }
}
