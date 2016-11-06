package com.talagasoft.neraca;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.talagasoft.model.AccountModel;
import com.talagasoft.model.JurnalModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDaftarAkun extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_daftar_akun);

        AccountModel account_model=new AccountModel();
        ArrayList<HashMap<String, String>> recAccount = account_model.GetAll();

        ListView lstAccount=(ListView) findViewById(R.id.coa_list);

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
