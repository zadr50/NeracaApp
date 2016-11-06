package com.talagasoft.neraca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.talagasoft.model.AccountModel;
import com.talagasoft.util.CustomList;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_menu);


        String[] names=new String[]{"Rugi Laba","Neraca","Neraca Saldo","Kartu Akun","Daftar Jurnal","Daftar Akun"};

        String[] desc=new String[]{"Laporan Rugi Laba","Laporan Neraca","Laporan Neraca Saldo (Trial Balance)",
                "Kartu Akun General Ledger","Daftar Jurnal Transaksi","Daftar Kode Perkiraan"};

        Integer[] imageid= new Integer[]{R.drawable.graph,R.drawable.monel,R.drawable.book_open,
            R.drawable.member_card,R.drawable.format_bullets,R.drawable.hotel};


        CustomList customList = new CustomList(this, names, desc, imageid);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),"You Clicked "+names[i],Toast.LENGTH_SHORT).show();
                Intent intent;
                switch (i){
                    case 0:
                        intent=new Intent(getBaseContext(),ReportRugiLaba.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent=new Intent(getBaseContext(),ReportNeraca.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(getBaseContext(),ReportNeracaSaldo.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent=new Intent(getBaseContext(),ReportKartuAkun.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent=new Intent(getBaseContext(),ReportDaftarJurnal.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent=new Intent(getBaseContext(),ReportDaftarAkun.class);
                        startActivity(intent);
                        break;

                }

            }
        });
    }
}
