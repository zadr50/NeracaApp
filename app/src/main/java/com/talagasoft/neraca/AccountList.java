package com.talagasoft.neraca;

import android.accounts.*;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.talagasoft.model.AccountAdapter;
import com.talagasoft.model.AccountModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountList extends AppCompatActivity implements View.OnClickListener {
    int account_type=1;
    ListView lstAccount;
    ArrayList<String> array_description = null;
    ArrayList<String> array_account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        setClickType();
        setClickAccount();

    }


    private void setClickAccount() {

        lstAccount = (ListView) findViewById(R.id.coa_list);
        lstAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Account()","array_description.count="+array_description.size());
                Log.d("Account()","array_account.count="+array_account.size());
                Log.d("Account()","account_type="+account_type);

                Bundle bundle=new Bundle();
                bundle.putString("account",array_account.get(i));
                bundle.putString("description",array_description.get(i));
                bundle.putInt("mode",1);
                bundle.putInt("account_type",account_type);
                Intent intent=new Intent(getBaseContext(),Account.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void setClickType() {
        // Here come all the options that you wish to show depending on the
        // size of the array.

        final String[] array_spinner=new com.talagasoft.util.constanta().daftar_jenis_akun();

        Spinner s = (Spinner) findViewById(R.id.coa_type);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, array_spinner);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(account_type<array_spinner.length) {
                    Log.d(parent.getClass().getName(), array_spinner[account_type]);
                    account_type = ++position;
                    load_account(account_type);
                } else {
                    Log.e(getClass().getSimpleName().toString(),"array_spinner.length="+array_spinner.length+",account_type="+account_type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    private void load_account(int position) {
        int nType = position;
        AccountModel account_model;
        account_model = new AccountModel();
        array_description = account_model.ListAccountByType(nType);
        array_account=account_model.account_list();
        ArrayList<String> array_balance = account_model.account_balance_list();
        //list item with sub item
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final String[] fromMapKey = new String[] {"text1", "text2"};
        final List<Map<String, String>> list=new ArrayList<Map<String, String>>(array_account.size());
        for(int i=0;i<array_account.size();i++) {
            String s1=array_description.get(i).toString();
            String s2=array_account.get(i).toString();
            double saldo= Double.parseDouble(array_balance.get(i).toString());
            s2="Account: "+s2+" , Saldo: Rp. "+ NumberFormat.getNumberInstance().format(saldo);
            final Map<String, String> listItemMap = new HashMap<String, String>();
            listItemMap.put("text1", s1);
            listItemMap.put("text2", s2);
            list.add(Collections.unmodifiableMap(listItemMap));
        }

        SimpleAdapter adapterCoa = new SimpleAdapter(this,list, android.R.layout.simple_list_item_2,
                fromMapKey,toLayoutId);
        lstAccount.setAdapter(adapterCoa);

    }

    @Override
    public void onClick(View view) {
        Log.d("Account()",view.getClass().getName());

        final int id = view.getId();
        switch (id) {
            case R.id.cmdAdd:
                Bundle bundle=new Bundle();
                bundle.putString("account","");
                bundle.putString("description","");
                bundle.putInt("mode",0);
                bundle.putInt("account_type",account_type);
                Intent intent=new Intent(getBaseContext(),Account.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.cmdRefresh:
                load_account(account_type);
                break;
            case R.id.cmdPrint:
                break;


        }
    }
}
