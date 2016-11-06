package com.talagasoft.neraca;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.talagasoft.model.AccountModel;
import com.talagasoft.model.JurnalModel;
import com.talagasoft.util.MyLib;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by dell on 09/15/2016.
 */
public class JurnalBiaya extends AppCompatActivity implements View.OnClickListener  {
    int mode;
    String account_code;
    String[] arrCoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jurnal_biaya);
        Bundle bundle= getIntent().getExtras();
        mode=bundle.getInt("mode");
        account_code=bundle.getString("account");
        TextView debit_account=(TextView) findViewById(R.id.debit_account);
        debit_account.setText(account_code);
        debit_account.setEnabled(false);

        Spinner credit_account= (Spinner) findViewById(R.id.credit_account);
        AccountModel acc=new AccountModel();
        ArrayList<HashMap<String, String>> arrListCoa=acc.LoadAccountBySubAccType("1");
        String[] arrCoaDesc= MyLib.ArrayListHashMapToStringArray(arrListCoa,"description");
        arrCoa=MyLib.ArrayListHashMapToStringArray(arrListCoa,"account");
        credit_account.setAdapter(new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,
                arrCoaDesc));

        TextView txtTanggal= (TextView) findViewById(R.id.tanggal);
        String dt = String.valueOf(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));
        txtTanggal.setText(dt);

        TextView txtJam = (TextView) findViewById(R.id.jam);
        String tm = String.valueOf(new SimpleDateFormat("HH:mm:ss",Locale.US).format(new Date()));
        txtJam.setText(tm);

    }


    @Override
    public void onClick(View view) {
        final int id = view.getId();

        switch (id) {
            case R.id.cmdCancel:
                finish();
                break;
            case R.id.cmdSave:
                SaveJurnal();
                finish();
                break;
        }
    }

    private void SaveJurnal() {
        JurnalModel jurnal=new JurnalModel();
        TextView debit_account=(TextView) findViewById(R.id.debit_account);
        Spinner _account=(Spinner) findViewById(R.id.credit_account);
        String credit_account=arrCoa[_account.getSelectedItemPosition()];

        String kode=new SimpleDateFormat("yyMMdd",Locale.US).format(new Date());
        TextView tanggal=(TextView) findViewById(R.id.tanggal);
        TextView jam=(TextView) findViewById(R.id.jam);
        String tanggal_jam=tanggal.getText().toString()+" "
                + jam.getText().toString();
        TextView memo=(TextView) findViewById(R.id.keterangan);
        TextView jumlah=(TextView) findViewById(R.id.jumlah);

        double debit= Double.parseDouble(jumlah.getText().toString());
        double credit=0;
        int id=0;
        jurnal.Save(mode,kode,tanggal_jam,debit_account.getText().toString(),memo.getText().toString(),
                debit,credit,credit_account,"",id,2 );

        double tmp=debit;
        credit=debit;
        debit=0;
        jurnal.Save(mode,kode,tanggal_jam,credit_account,memo.getText().toString(),
                debit,credit,credit_account,"",id,2 );

        if(Objects.equals(jurnal.ErrMsg(), "")) {
            Toast.makeText(this.getBaseContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getBaseContext(), R.string.error_save, Toast.LENGTH_SHORT).show();
        }
    }
}
