package com.talagasoft.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.talagasoft.util.DbConnection;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 09/17/2016.
 */
public class ReportModel {
    private String TAG="ReportModel";
    private SQLiteDatabase database;
    private String m_err_msg="";
    private double _total_aktiva;

    public String ErrMsg(){return m_err_msg;}
    public ReportModel()
    {
        database = DbConnection.db;
    }
    private String _r(Cursor cur,String field){
        return cur.getString(cur.getColumnIndex(field));
    }
    private String _fmt(String vValue){
        return NumberFormat.getNumberInstance().format(Double.parseDouble(vValue));
    }

    public ArrayList<HashMap<String,String>> Neraca(){

        String sql = "select account,description,balance,dbcr,idx_icon,account_type from account " +
                " where account_type=1 order by account";

        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        Cursor cur = database.rawQuery(sql,null);

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("account","");
        map.put("description", "AKTIVA");
        map.put("balance", "");
        map.put("balance_value", "");
        map.put("idx_icon", "");
        map.put("account_type", "");
        map.put("dbcr", "");

        mapParent.add(map);

        if(cur!=null) {
            cur.moveToFirst();
            int i = 0;
            _total_aktiva = 0;
            while (cur.isAfterLast() == false) {

                map = new HashMap<String, String>();

                map.put("account", _r(cur, "account"));
                map.put("description", _r(cur, "description"));
                map.put("balance", _fmt(_r(cur, "balance")));
                map.put("balance_value", _r(cur, "balance"));
                map.put("idx_icon", _r(cur, "idx_icon"));
                map.put("account_type", _r(cur, "account_type"));
                map.put("dbcr", _r(cur, "dbcr"));

                mapParent.add(map);
                _total_aktiva = _total_aktiva + Double.parseDouble(_r(cur, "balance"));
                cur.moveToNext();
            }
            cur.close();

        }
        return mapParent;
    }
}
