package com.talagasoft.neraca;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.talagasoft.model.AccountModel;
import com.talagasoft.util.CustomList;

import java.util.ArrayList;
import java.util.HashMap;

public class JurnalInputTemplate extends AppCompatActivity {

    ListView listView;
    private String accounts[];
    private String names[];

    private String desc[];


    private Integer imageid[] = {
            R.drawable.atk,
            R.drawable.book_open,
            R.drawable.compass,
            R.drawable.email,
            R.drawable.fabrik,
            R.drawable.file_new,
            R.drawable.folder_home2,
            R.drawable.format_bullets,
            R.drawable.gaji,
            R.drawable.hotel,
            R.drawable.ic_info_black_24dp,
            R.drawable.ic_notifications_black_24dp,
            R.drawable.ic_sync_black_24dp,
            R.drawable.keyboard,
            R.drawable.lamp,
            R.drawable.laptop,
            R.drawable.makan,
            R.drawable.member_card,
            R.drawable.pakaian,
            R.drawable.message_open,
            R.drawable.monel    ,
            R.drawable.pajak,
            R.drawable.telpon,
            R.drawable.tools,
            R.drawable.transport,
            R.drawable.web
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jurnal_input_template);
        AccountModel am=new AccountModel();
        ArrayList<HashMap<String, String>> map=am.LoadAccountBySubAccType("2");
        names=new String[map.size()];
        desc=new String[map.size()];
        accounts=new String[map.size()];
        Integer[] imageid2= new Integer[map.size()];
        for(int i=0;i<map.size();i++){
            HashMap m=new HashMap<String,String>();
            m=map.get(i);
            names[i]=m.get("description").toString();
            accounts[i]=m.get("account").toString();
            desc[i]="Account: " + m.get("account").toString()+" Saldo: "+m.get("balance").toString();
            //imageid2[i]= (Integer) m.get("idx_icon");
        };

        CustomList customList = new CustomList(this, names, desc, imageid);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),"You Clicked "+names[i],Toast.LENGTH_SHORT).show();
                Bundle bundle=new Bundle();
                bundle.putString("account",accounts[i]);
                bundle.putInt("mode",0);
                Intent intent=new Intent(getBaseContext(),JurnalBiaya.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });



    }
}
