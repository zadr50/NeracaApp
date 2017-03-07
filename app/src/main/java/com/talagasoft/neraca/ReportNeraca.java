package com.talagasoft.neraca;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.talagasoft.model.ReportModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportNeraca extends AppCompatActivity   implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_neraca);

        ReportModel rpt=new ReportModel();
        ArrayList<HashMap<String, String>> recAccount = rpt.Neraca();

        ListView lstAccount=(ListView) findViewById(R.id.list1);

        final int[] toLayoutId = new int[] {R.id.tvAccount,R.id.tvDescription,R.id.tvBalance};
        final String[] fromMapKey = new String[] {"account", "description","balance"};
        SimpleAdapter adapterCoa = new SimpleAdapter(this,recAccount,R.layout.content_account_items,
                fromMapKey,toLayoutId);
        lstAccount.setAdapter(adapterCoa);
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
