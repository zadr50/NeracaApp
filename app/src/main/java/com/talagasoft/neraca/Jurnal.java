package com.talagasoft.neraca;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.talagasoft.model.AccountModel;
import com.talagasoft.model.JurnalModel;
import com.talagasoft.util.constanta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Jurnal extends AppCompatActivity implements View.OnClickListener {

    String[] array_account_description=null;
    ArrayList<String> array_account=null;

    Spinner txtAccount=null;
    TextView txtTanggal=null;
    TextView txtJam=null;
    TextView txtKode=null;
    TextView txtDebit=null;
    TextView txtCredit=null;
    TextView txtRef1=null;
    TextView txtRef2=null;
    TextView txtMemo=null;
    Spinner txtTranType=null;
    String[] tran_type_array;

    int mode=0;
    long id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jurnal);

        txtAccount=(Spinner)findViewById(R.id.txtJurAccount);

        AccountModel account_model=new AccountModel();

        array_account_description=account_model.ListAccount();
        array_account=account_model.account_list();
        if ( array_account_description != null) {

            txtAccount.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                    array_account_description));
        } else {
            Log.e(getLocalClassName().getClass().getSimpleName(),"array_account_description == null");
        }

        txtTanggal= (TextView) findViewById(R.id.txtJurTanggal);
        String dt = String.valueOf(new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date()));
        txtTanggal.setText(dt);

        txtJam = (TextView) findViewById(R.id.txtJurJam);
        String tm = String.valueOf(new SimpleDateFormat("HH:mm:ss",Locale.US).format(new Date()));
        txtJam.setText(tm);

        txtKode=(TextView) findViewById(R.id.txtJurKode);
        txtKode.setText(new SimpleDateFormat("yyMMdd",Locale.US).format(new Date()));

        txtDebit= (TextView) findViewById(R.id.txtJurDebit);
        txtCredit= (TextView) findViewById(R.id.txtJurCredit);
        txtRef1= (TextView) findViewById(R.id.txtJurRef1);
        txtRef2= (TextView) findViewById(R.id.txtJurRef2);
        txtMemo= (TextView) findViewById(R.id.txtJurMemo);

        txtTranType= (Spinner) findViewById(R.id.txtTranType);
        tran_type_array=getResources().getStringArray(R.array.tran_type_array);
        txtTranType.setAdapter(new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,
                tran_type_array));


    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();

        switch (id) {
            case R.id.cmdJurCancel:
                finish();
                break;
            case R.id.cmdJurSave:
                SaveJurnal();
                finish();
                break;
        }
    }

    private void SaveJurnal() {
        JurnalModel jurnal=new JurnalModel();
        String s = txtDebit.getText().toString();
        double debit = Double.valueOf(txtDebit.getText().toString().trim());
        double credit = Double.valueOf(txtCredit.getText().toString().trim());
        int idx_coa= (int) txtAccount.getSelectedItemId();
        String account= array_account.get(idx_coa);
        String tanggal_jam=txtTanggal.getText().toString()+" "+txtJam.getText().toString();
        jurnal.Save(mode,txtKode.getText().toString(),tanggal_jam,
                account,txtMemo.getText().toString(),debit,credit,
                txtRef1.getText().toString(),txtRef2.getText().toString(),id,
                txtTranType.getSelectedItemPosition());
        if(Objects.equals(jurnal.ErrMsg(), "")) {
            Toast.makeText(this.getBaseContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getBaseContext(), R.string.error_save, Toast.LENGTH_SHORT).show();
        }

    }
}
