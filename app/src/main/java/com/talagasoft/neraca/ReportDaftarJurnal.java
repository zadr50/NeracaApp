package com.talagasoft.neraca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.talagasoft.model.AccountModel;
import com.talagasoft.model.JurnalModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportDaftarJurnal extends AppCompatActivity  implements View.OnClickListener {


    ListView lstAccount;
    Spinner periode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_daftar_jurnal);

        lstAccount=(ListView) findViewById(R.id.coa_list);
        periode=(Spinner) findViewById(R.id.txtJenis);

        final String[] periode_array=getResources().getStringArray(R.array.periode_array);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, periode_array);
        periode.setAdapter(adapter);
        periode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Log.d(parent.getClass().getName(),array_spinner[position]);
                //load_account(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });



        on_print();

    }
    private void on_print(){
        JurnalModel jurnalModel=new JurnalModel();
        jurnalModel.setPeriode(periode.getSelectedItemPosition());
        ArrayList<HashMap<String, String>> arrJurnal = jurnalModel.DataFromPeriod();
        final int[] toLayoutId = new int[] {R.id.tanggal,R.id.account,R.id.description,R.id.debet,
                R.id.credit,R.id.keterangan};
        final String[] fromMapKey = new String[] {"tanggal","account", "description","debit","credit","keterangan"};
        SimpleAdapter adapterCoa = new SimpleAdapter(this,arrJurnal,R.layout.content_jurnal_item,
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
