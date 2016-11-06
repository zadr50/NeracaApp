package com.talagasoft.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.talagasoft.neraca.R;

/**
 * Created by dell on 09/10/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    String CREATE_TABLE_ACCOUNT="create table account (account text primary key, description text," +
            "account_type int,balance real,dbcr int,sub_acc_type int,idx_icon int)";
    String CREATE_TABLE_JURNAL="create table jurnal (tanggal CURRENT_TIMESTAMP, kode text,account text, " +
            "debit real,credit real,keterangan text,ref1 text, ref2 text, "+
            "tran_type int, id INTEGER PRIMARY KEY AUTOINCREMENT)";

    SQLiteDatabase db;

    public DBHelper(Context context)
    {
        super(context, String.valueOf(R.string.app_db_name), null, 1);

    }
    public SQLiteDatabase open(){
        db=this.getReadableDatabase();
        return db;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNT);
            sqLiteDatabase.execSQL(CREATE_TABLE_JURNAL);
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        Log.d(String.valueOf(R.string.app_name),"onCreate "+CREATE_TABLE_ACCOUNT);
        this.db=sqLiteDatabase;
        CreateSampleData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS account");
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Account");
        //onCreate(sqLiteDatabase);
    }
    public void CreateSampleData(){
        add_account("11001","Kas",1,0,0,1,0);
        add_account("11002","Bank BCA",1,0,0,1,0);
        add_account("11003","Bank BRI",1,0,0,1,0);
        add_account("11004","Bank BNI",1,0,0,1,0);
        add_account("11005","Piutang Dagang",1,0,0,4,0);
        add_account("11006","Piutang Lainnya",1,0,0,4,0);
        add_account("21001","Hutang Dagang",2,0,1,3,0);
        add_account("21002","Hutang Lainnya",2,0,1,3,0);
        add_account("31001","Modal",3,0,1,0,0);
        add_account("31002","Laba Rugi Berjalan",3,0,1,0,0);
        add_account("31003","Laba Rugi Ditahan",3,0,1,0,0);
        add_account("31004","Prive",3,0,1,0,0);
        add_account("41001","Pendapatan",4,0,1,0,0);
        add_account("41002","Pendapatan Lain",4,0,1,0,0);
        add_account("41003","Ongkos Penjualan",4,0,1,0,0);
        add_account("41004","Potongan Penjualan",4,0,0,0,0);
        add_account("51001","Pembelian",5,0,0,0,0);
        add_account("51002","Ongkos Pembelian",5,0,0,0,0);
        add_account("51003","Potongan Pembelian",5,0,1,0,0);
        add_account("61001","Biaya Gaji",6,0,0,2,1);
        add_account("61002","Biaya Marketing",6,0,0,2,2);
        add_account("61003","Biaya Kantor",6,0,0,2,3);
        add_account("61004","Biaya Operasional",6,0,0,2,4);
        add_account("61005","Biaya Entertain",6,0,0,2,5);
        add_account("61006","Biaya Komunikasi",6,0,0,2,6);
        add_account("61007","Biaya Makan Minum",6,0,0,2,7);
        add_account("61008","Biaya Penginapan",6,0,0,2,8);
        add_account("61009","Biaya Transportasi",6,0,0,2,9);
        add_account("61010","Biaya Pengobatan",6,0,0,2,10);
        add_account("61011","Biaya Pakaian",6,0,0,2,11);
        add_account("61012","Biaya Listrik Telpon",6,0,0,2,12);
        add_account("61013","Biaya Alat Tulis Kantor",6,0,0,2,13);
        add_account("61014","Biaya Pajak",6,0,0,2,14);
        add_account("61015","Biaya Pemeliharaan",6,0,0,2,15);
        add_account("61016","Biaya Konsultasi",6,0,0,2,16);
        add_account("61017","Biaya Keperluan Pribadi",6,0,0,2,17);
        add_account("61018","Biaya Penyusutan Aktiva Tetap",6,0,0,2,18);
        add_account("61019","Biaya Lembur",6,0,0,2,19);
        add_account("61020","Biaya Asuransi",6,0,0,2,20);
        add_account("61021","Biaya Keamanan",6,0,0,2,21);
        add_account("61022","Biaya Sewa",6,0,0,2,22);
    }
    private void add_account(String account,String description,
			int account_type,double balance,int db_or_cr,int sub_acc_type,int idx_icon){
        String sql="INSERT INTO account(account,description,account_type,balance,dbcr,sub_acc_type,idx_icon)" +
                "VALUES('"+account+"','"+description+"',"+account_type+","+balance + 
				","+db_or_cr+","+sub_acc_type+","+idx_icon+")";
        db.execSQL(sql);
    }
}
