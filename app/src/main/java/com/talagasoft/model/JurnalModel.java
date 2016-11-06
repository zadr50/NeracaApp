package com.talagasoft.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.talagasoft.util.DbConnection;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dell on 09/11/2016.
 */
public class JurnalModel {
    private String TAG="JurnalModel";
    private SQLiteDatabase database;
    private String m_err_msg="";

    private double _debit_total, _credit_total, _saldo_total;
    private int _tahun,_bulan,_hari;
    private int periode;
    public void setTahun(int tahun){this._tahun=tahun;};
    public void setBulan(int bulan){this._bulan=bulan;};
    public void setHari(int hari){this._hari=hari;};

    public String ErrMsg(){return m_err_msg;}

    public JurnalModel()
    {
        database = DbConnection.db;
        _saldo_total=0;         _debit_total=0;     _credit_total=0;
        _tahun=0;               _bulan=0;           _hari=0;
    }

    public void Save(int mode, String vKode, String vTanggal, String vAccount, String vMemo, double vDebit,
                     double vCredit, String vRef1,String vRef2,long vId,int vType) {
        String sql="";
        if(mode==0){
            sql="INSERT INTO jurnal(kode,account,tanggal,debit,credit,keterangan,ref1,ref2,tran_type)" +
                    " VALUES('"+vKode+"','"+vAccount+"','"+vTanggal+"'," +
                    vDebit+","+vCredit+",'"+vMemo+"','"+vRef1+"','"+vRef2+"',"+vType+")";
        } else {
            sql="UPDATE jurnal SET account='"+vAccount+"',debit="+vDebit + ",tanggal='"+vTanggal+"'"+
                    ",credit="+vCredit+",keterangan='"+vMemo+"',ref1='"+vRef1+"',ref2='"+ vRef2 + "'" +
                    ",tran_type="+vType+" WHERE id="+vId+"'";
        }
        boolean ok=false;
        Log.d(TAG,"Save().sql: "+sql);
        try{
            database.execSQL(sql);
            ok=true;
        } catch (SQLiteException e){
            m_err_msg=e.getMessage();
        };
        if(ok){
            //update balance
            double vAmt=vDebit-vCredit;
            sql="UPDATE account set balance=balance+"+vAmt+" where account='"+vAccount+"'";
            database.execSQL(sql);
        }

    }

    public void Delete(long vId) {
        String sql="";
        sql="DELETE FROM jurnal WHERE id="+vId;
        Log.d(TAG,"Delete().sql="+sql);
        database.execSQL(sql);
    }
    public void DeleteByKode(String vKode){
        String sql="";
        sql="DELETE FROM jurnal WHERE kode='"+vKode+"'";
        Log.d(TAG,"Delete().sql="+sql);
        database.execSQL(sql);

    }

    public Cursor GetAll() {

        if(database==null){return null;}

        Cursor cur=null;
        String where="";
        String sql="select j.*,a.description from jurnal j left join account a " +
		" on a.account=j.account";
        if(_tahun>0)where=where+" and strftime('%Y',tanggal)="+_tahun;
        if(_bulan>0)where=where+" and strftime('%m',tanggal)="+_bulan;
        if(_hari>0)where=where+" and strftime('%d',tanggal)="+_hari;
        if(where.length()>1)sql=sql+" WHERE "+where;
        try{
            cur=database.rawQuery(sql,null);
        } catch (SQLiteException e){
            m_err_msg=e.getMessage();
        };
        if(cur==null){
            Log.e(TAG,"GetAll == null");
        }
        return cur;
    }
    public ArrayList<HashMap<String,String>> GetAllArrayDaily(){

        if(database==null){return null;}

        Cursor curJurnal=null;
        String where="";
        String sql="select j.account,a.description,strftime('%d',j.tanggal) as z_day," +
                "sum(j.debit) as z_debit, sum(j.credit) as z_credit" +
                "from jurnal j left join account a " +
                " on a.account=j.account" +
                " group by  j.account,a.description,strftime('%d',j.tanggal)" +
                " where strftime('%Y',tanggal)="+_tahun+" and strftime('%m',tanggal)="+_bulan;
        try{
            curJurnal=database.rawQuery(sql,null);
        } catch (SQLiteException e){
            m_err_msg=e.getMessage();
        };
        if(curJurnal==null){
            Log.e(TAG,"GetAll == null");
        }

        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        if(curJurnal!=null) {
            curJurnal.moveToFirst();
            int i = 0; double _debit=0, _credit=0, _saldo=0;
            map = new HashMap<String, String>();
            map.put("tanggal", "TANGGAL");
            map.put("account", "");
            map.put("description", "");
            map.put("debit", "DEBET");
            map.put("credit", "CREDIT");
            map.put("saldo", "");
            map.put("keterangan", "");
            mapParent.add(map);

            while (curJurnal.isAfterLast() == false) {
                _debit= Double.parseDouble(_r(curJurnal,"debit"));
                _credit= Double.parseDouble(_r(curJurnal,"credit"));
                _saldo = _debit - _credit ;

                map = new HashMap<String, String>();
                map.put("tanggal", _r(curJurnal, "tanggal"));
                map.put("account", _r(curJurnal, "account"));
                map.put("description", _r(curJurnal, "description"));
                map.put("debit", _fmt(String.valueOf(_debit)));
                map.put("credit", _fmt(String.valueOf(_credit)));
                map.put("keterangan", "");

                mapParent.add(map);
                _debit_total = _debit_total + _debit;
                _credit_total = _credit_total + _credit;
                curJurnal.moveToNext();
            }
            curJurnal.close();
            _saldo_total = _debit_total - _credit_total;
        }
        map = new HashMap<String, String>();
        map.put("tanggal", "TOTAL");
        map.put("account", "");
        map.put("description", "");
        map.put("debit",_fmt(String.valueOf(_debit_total)));
        map.put("credit", _fmt(String.valueOf(_credit_total)));
        map.put("credit", _fmt(String.valueOf(_saldo_total)));
        map.put("keterangan", "");
        mapParent.add(map);

        return mapParent;

    }
    public ArrayList<HashMap<String,String>> GetAllArrayMonthly(){
        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        Cursor cur =this.GetAll();
        HashMap<String, String> map = new HashMap<String, String>();

        return mapParent;

    }
    public ArrayList<HashMap<String,String>> GetAllArrayYearly(){
        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        Cursor cur =this.GetAll();
        HashMap<String, String> map = new HashMap<String, String>();

        return mapParent;

    }

    public ArrayList<HashMap<String,String>> GetAllArray(){

        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        Cursor cur =this.GetAll();
        HashMap<String, String> map = new HashMap<String, String>();

        if(cur!=null) {
            cur.moveToFirst();
            int i = 0; double _debit=0, _credit=0, _saldo=0;
            map = new HashMap<String, String>();
            map.put("tanggal", "TANGGAL");
            map.put("account", "");
            map.put("description", "");
            map.put("debit", "DEBET");
            map.put("credit", "CREDIT");
            map.put("keterangan", "");
            mapParent.add(map);

            while (cur.isAfterLast() == false) {
                _debit= Double.parseDouble(_r(cur,"debit"));
                _credit= Double.parseDouble(_r(cur,"credit"));
                _saldo = _debit - _credit ;

                map = new HashMap<String, String>();
                map.put("tanggal", _r(cur, "tanggal"));
                map.put("account", _r(cur, "account"));
                map.put("description", _r(cur, "description"));
                map.put("debit", _fmt(String.valueOf(_debit)));
                map.put("credit", _fmt(String.valueOf(_credit)));
                map.put("keterangan", _r(cur, "keterangan"));

                mapParent.add(map);
                _debit_total = _debit_total + _debit;
                _credit_total = _credit_total + _credit;
                cur.moveToNext();
            }
            cur.close();
            _saldo_total = _debit_total - _credit_total;
        }
        map = new HashMap<String, String>();
        map.put("tanggal", "TOTAL");
        map.put("account", "");
        map.put("description", "");
        map.put("debit",_fmt(String.valueOf(_debit_total)));
        map.put("credit", _fmt(String.valueOf(_credit_total)));
        map.put("keterangan", "");
        mapParent.add(map);

        return mapParent;
    }
    public Cursor GetAllBySearch(String s) {
        Cursor cur=null;
        String sql="select j.*,a.description from jurnal j left join account a on a.account=j.account" +
                " where j.keterangan like '%"+s+"%'";
        try{
            cur=database.rawQuery(sql,null);
            Log.d(TAG,sql+", RowCount: "+cur.getCount());
        } catch (SQLiteException e){
            m_err_msg=e.getMessage();
        };
        return cur;
    }

    private String _r(Cursor cur,String field){
        return cur.getString(cur.getColumnIndex(field));
    }
    private String _fmt(String vValue){
        return NumberFormat.getNumberInstance().format(Double.parseDouble(vValue));
    }

    public void setPeriode(int periode) {
        this.periode = periode;
        this._bulan = new Date().getMonth();
        this._hari=new Date().getDate();
        this._tahun=new Date().getYear();
    }
    public ArrayList<HashMap<String,String>> DataFromPeriod(){
        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        switch(this.periode){
            case 0:     //daily
                mapParent= this.GetAllArrayDaily();
                break;
            case 1:     //monthly
                mapParent= this.GetAllArrayMonthly();

                break;
            case 2:     //yearly
                mapParent= this.GetAllArrayYearly();

                break;

        }
        return mapParent;
    }
}
