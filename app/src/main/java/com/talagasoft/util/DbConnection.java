package com.talagasoft.util;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.talagasoft.neraca.Main;

/**
 * Created by dell on 09/12/2016.
 */
public class DbConnection  {
    private static DbConnection ourInstance = new DbConnection();

    public static DbConnection getInstance() {
        return ourInstance;
    }
    public static SQLiteDatabase db;
    private static Context context;

    public static void setContext(Context c){
        context=c;
        DBHelper dbHelper = new DBHelper(context);
        db=dbHelper.open();
    }
    private DbConnection() {

    }

}
