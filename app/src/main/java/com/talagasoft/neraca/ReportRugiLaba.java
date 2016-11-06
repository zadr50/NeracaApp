package com.talagasoft.neraca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReportRugiLaba extends AppCompatActivity   implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_rugi_laba);
    }
    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.cmdRefresh:
                break;
            case R.id.cmdPrint:
                break;
            case R.id.cmdFilter:
                break;
            case R.id.cmdMenu:
                break;
        }
    }
}
