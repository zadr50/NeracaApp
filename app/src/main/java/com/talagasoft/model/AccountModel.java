package com.talagasoft.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.talagasoft.neraca.R;
import com.talagasoft.util.DBHelper;
import com.talagasoft.util.DbConnection;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 09/10/2016.
 */
public class AccountModel    {
	private String TAG="AccountModel";
    
    private SQLiteDatabase database;

    private String account="";
    private String description="";
    private double balance=0;
    private int account_type=0;
    private int dbcr=0;
	private int sub_acc_type=0;
	private int idx_icon=0;
	private Cursor currentCursor;
	
    private String m_err_msg="";

    ArrayList<String> array_list = new ArrayList<String>();
    ArrayList<String> array_list_account = new ArrayList<String>();
    ArrayList<String> array_list_account_balance = new ArrayList<String>();
    private double _total;


    public String ErrMsg(){return m_err_msg;}
    public void setAccount(String value){
        this.account=value;
    }
    public String getAccount(){
        return this.account;
    }
    public void setDescription(String value){
        this.description=value;
    }
    public String getDescription(){
        return this.description;
    }
    public void setBalance(double value){
        this.balance=value;
    }
    public double getBalance(){
        return this.balance;
    }
    public void setAccountType(int value){
        this.account_type=value;
    }
    public int getAccountType(){
        return this.account_type;
    }
    public void setDbcr(int value){
        this.dbcr=value;
    }
    public int getDbcr(){
        return this.dbcr;
    }

    public void setSubAccType(int value){
        this.sub_acc_type=value;
    }
    public int getSubAccType(){
        return this.sub_acc_type;
    }
    public void setIdxIcon(int value){
        this.idx_icon=value;
    }
    public int getIdxIcon(){
        return this.idx_icon;
    }


    public AccountModel()
    {
        database = DbConnection.db;
    }
    public ArrayList<String> ListAccountByType(int nType) {

        String sql="select * from account where account_type="+nType;
        Log.d("ArrayList",sql);

        Cursor res =  database.rawQuery( sql, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("description")));
            array_list_account.add(res.getString(res.getColumnIndex("account")));
            array_list_account_balance.add(res.getString(res.getColumnIndex("balance")));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<String>  account_list(){
        return array_list_account;
    }

    public void loadByCode(String account_code) {
        String sql="select * from account where account='"+account_code+"'";
        Cursor res;
        res=database.rawQuery(sql,null);
        res.moveToFirst();
		
        if(res.isAfterLast()==false) {
            this.setAccount(res.getString(res.getColumnIndex("account")));
            this.setDescription(res.getString(res.getColumnIndex("description")));
            this.setBalance(res.getDouble(res.getColumnIndex("balance")));
            this.setAccountType(res.getInt(res.getColumnIndex("account_type")));
            this.setDbcr(res.getInt(res.getColumnIndex("dbcr")));
            this.setSubAccType(res.getInt(res.getColumnIndex("sub_acc_type")));
            this.setIdxIcon(res.getInt(res.getColumnIndex("idx_icon")));
        }
    }

    public void Save(int mode, String vAccount, String vDescription, int vAccountType,
                     double vBalance, int vDbCr, int vSubAccType, int vIdxIcon) {
        String sql="";
        if(mode==0){
            sql="INSERT INTO account(account,description,account_type,balance,dbcr,sub_acc_type,idx_icon)" +
                    " VALUES('"+vAccount+"','"+vDescription+"',"+vAccountType+"," +
                    vBalance+","+vDbCr+","+vSubAccType+","+vIdxIcon+")";
        } else {
            sql="UPDATE account SET description='"+vDescription+"',account_type="+vAccountType +
                    ",balance="+vBalance+",dbcr="+vDbCr+",sub_acc_type="+vSubAccType+
					",idx_icon="+vIdxIcon+
					" WHERE account='"+vAccount+"'";
        }
        Log.d(TAG,"Save().sql: "+sql);
        try{
            database.execSQL(sql);
        } catch (SQLiteException e){
            m_err_msg=e.getMessage();
        };
    }

    public void Delete(String account) {
        String sql="";
        sql="DELETE FROM account WHERE account='"+account+"'";
        Log.d(TAG,"Delete().sql="+sql);
        database.execSQL(sql);
    }

    public ArrayList<String> account_balance_list() {
        return array_list_account_balance;
    }

    public String[] ListAccount() {
        String sql="select * from account order by account";
        Cursor res;
        res=database.rawQuery(sql,null);
        String[] retval=new String[res.getCount()];
        res.moveToFirst();
        int i=0;
        while(res.isAfterLast()==false) {
            retval[i]=res.getString(res.getColumnIndex("description"));
            i++;
            array_list.add(res.getString(res.getColumnIndex("description")));
            array_list_account.add(res.getString(res.getColumnIndex("account")));
            res.moveToNext();
        }
        return retval;
    }
    public double getPenerimaan(){
        double ret=0;
		String sql="select sum(balance) as zAmt from account where account_type=1" + 
		" and sub_acc_type=1";
        Cursor res=database.rawQuery(sql,null);
        res.moveToFirst();
        if(!res.isAfterLast()){
            ret=res.getDouble(res.getColumnIndex("zAmt"));
        }
        return ret;

    }
    public double getPengeluaran(){
        double ret=0;
		String sql="select sum(balance) as zAmt from account where account_type=6 " +
		" and sub_acc_type=2";
        Cursor res=database.rawQuery(sql,null);
        res.moveToFirst();
        if(!res.isAfterLast()){
            ret=res.getDouble(res.getColumnIndex("zAmt"));
        }

        return ret;
    }
    public ArrayList<HashMap<String,String>> SumByAccType(String sType, int nPeriode, String account_rekening_ref1) {
        return SumByAccType(sType,nPeriode,account_rekening_ref1,false);
    }
        public ArrayList<HashMap<String,String>> SumByAccType(String sType, int nPeriode, String account_rekening_ref1,
                                                          boolean lTopFive){

		//0 - All Periode, 1 - Today, 2 - ToMonth, 3 - ToYear
		//strftime('%Y%m%d',j.tanggal)
		String sPrd="strftime('";
        if(nPeriode==2)sPrd = sPrd + "%Y";
		if(nPeriode==1)sPrd = sPrd + "%Y%m";
		if(nPeriode==0)sPrd = sPrd + "%Y%m%d";

            sPrd=sPrd+"',j.tanggal)";

        String sql = " ";
            sql="select a.description," + sPrd + " as periode, sum(j.debit-j.credit) as sum_amount " +
			" from jurnal j left join account a on a.account=j.account " +
			" where 1=1";
        if(account_rekening_ref1!=""){
            sql=sql+" and j.ref1='"+account_rekening_ref1+"'";
        }
        if(sType != "") sql=sql+" and sub_acc_type in (" + sType + ") ";
			
		sql =  sql + " GROUP BY a.description,"+sPrd;
            if(lTopFive){
                sql = sql + " ORDER BY sum(j.debit-j.credit) DESC";
            }
            if(lTopFive){
                sql = sql+" LIMIT 5 ";
            }

        Cursor cur = database.rawQuery(sql,null);
        cur.moveToFirst();
        Log.d(TAG,"cur.count+"+cur.getCount()+", sql="+sql);

        String[] retval=new String[cur.getCount()];
        int i=0;
        _total=0;
        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        while(cur.isAfterLast()==false) {
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("description",_r(cur,"description"));
            map.put("periode",_r(cur,"periode"));
            map.put("sum_amount", _fmt(_r(cur,"sum_amount")));
            map.put("sum_amount_value",_r(cur,"sum_amount"));

            mapParent.add(map);
            _total=_total+Double.parseDouble(_r(cur,"sum_amount"));
            cur.moveToNext();
        }
        cur.close();
		return mapParent;
	}
	private String _r(Cursor cur,String field){
            return cur.getString(cur.getColumnIndex(field));
    }
    private String _fmt(String vValue){
        return NumberFormat.getNumberInstance().format(Double.parseDouble(vValue));
    }

    public String[] AccSubTypeRekening() {
        String sql="select account,description from account where sub_acc_type=1 order by description";
        String[] retval=null;
        Cursor res;
        res=database.rawQuery(sql,null);
        if(res!=null) {
            res.moveToFirst();
            int i = 0;
            retval = new String[res.getCount()];
            while (res.isAfterLast() == false) {
                retval[i] = res.getString(res.getColumnIndex("description"));
                i++;
                array_list.add(res.getString(res.getColumnIndex("description")));
                array_list_account.add(res.getString(res.getColumnIndex("account")));
                res.moveToNext();
            }
            res.close();
        }
        return retval;

    }

    public Double Total() {
        return _total;
    }

    public ArrayList<HashMap<String,String>> LoadAccountBySubAccType(String sType){

        String sql = "select account,description,balance,dbcr,idx_icon from account " +
                " where sub_acc_type in ("+sType+") order by description";

        Cursor cur = database.rawQuery(sql,null);
        cur.moveToFirst();
        Log.d(TAG+".LoadAccountByAccType()","cur.count+"+cur.getCount()+", sql="+sql);
        int i=0;
        _total=0;
        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        while(cur.isAfterLast()==false) {
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("account",_r(cur,"account"));
            map.put("description",_r(cur,"description"));
            map.put("balance", _fmt(_r(cur,"balance")));
            map.put("balance_value",_r(cur,"balance"));
            map.put("idx_icon",_r(cur,"idx_icon"));
            mapParent.add(map);
            _total=_total+Double.parseDouble(_r(cur,"balance"));
            cur.moveToNext();
        }
        cur.close();
        return mapParent;
    }
    public ArrayList<HashMap<String,String>> GetAll(){

        String sql = "select account,description,balance,dbcr,idx_icon,account_type from account " +
                " order by account";

        ArrayList<HashMap<String,String>> mapParent=new ArrayList<HashMap<String, String>>();
        Cursor cur = database.rawQuery(sql,null);
        if(cur!=null) {
            cur.moveToFirst();
            int i = 0;
            _total = 0;
            while (cur.isAfterLast() == false) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("account", _r(cur, "account"));
                map.put("description", _r(cur, "description"));
                map.put("balance", _fmt(_r(cur, "balance")));
                map.put("balance_value", _r(cur, "balance"));
                map.put("idx_icon", _r(cur, "idx_icon"));
                map.put("account_type", _r(cur, "account_type"));
                map.put("dbcr", _r(cur, "dbcr"));

                mapParent.add(map);
                _total = _total + Double.parseDouble(_r(cur, "balance"));
                cur.moveToNext();
            }
            cur.close();

        }
        return mapParent;
    }
}
