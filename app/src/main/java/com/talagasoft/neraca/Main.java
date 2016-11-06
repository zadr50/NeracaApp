package com.talagasoft.neraca;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.talagasoft.util.DbConnection;

public class Main extends AppCompatActivity  implements View.OnClickListener  {

    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init global database connection
        DbConnection.setContext(this.getApplicationContext());
        database = DbConnection.db;

        //test database
        //dbHelper = new DBHelper(this);
        //this.open();
        //database.rawQuery("insert into account(account,description,account_type,balance) values('100','Kas Kecil',1,100000)",null);
       // Cursor res =  database.rawQuery( "select * from account where account_type=0", null );
       // res.moveToFirst();
        //Log.d("neraca",res.toString());


    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        Intent i;
        switch (id) {
            case R.id.btnAccount:
                i=new Intent(getBaseContext(),AccountList.class);
                startActivity(i);
                break;
            case R.id.btnBackup:
                i=new Intent(getBaseContext(),Backup.class);
                startActivity(i);
                break;

            case R.id.btnJurnal:
                i=new Intent(getBaseContext(),JurnalListNew.class);
                startActivity(i);
                break;
            case R.id.btnMe:
                i=new Intent(getBaseContext(),SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.btnPrint:
                i=new Intent(getBaseContext(),Reports.class);
                startActivity(i);
                break;
            // even more buttons here
        }
    }
}
