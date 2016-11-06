package com.talagasoft.neraca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.talagasoft.model.AccountModel;

import java.text.NumberFormat;

public class Account extends AppCompatActivity {
    private int mode=0;
    private int account_type=1;
    private String account_code="";
    private TextView txtAccount=null;
    private TextView txtDescription=null;
    private TextView txtBalance=null;
    private Spinner txtAccountType=null;
    private RadioButton txtDb=null;
    private RadioButton txtCr=null;
    private RadioGroup radioDbCr=null;
    private Spinner txtSubType=null;
    private TextView txtIdxIcon=null;

    String[] account_type_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Bundle bundle= getIntent().getExtras();
        mode=bundle.getInt("mode");
        account_code=bundle.getString("account");
        txtAccount = (TextView) findViewById(R.id.txtAccount);
        txtAccount.setText(account_code);
        Log.d("Account()","BeforeAm");
        //load info account
        AccountModel am=new AccountModel();
        am.loadByCode(account_code);
        Log.d("Account()","AfterAM");
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDescription.setText(bundle.getString("description"));

        txtBalance = (TextView) findViewById(R.id.txtBalance);
        txtBalance.setText(""+ NumberFormat.getNumberInstance().format(am.getBalance()));

        radioDbCr=(RadioGroup) findViewById(R.id.radioDbCr);

        txtDb = (RadioButton) findViewById(R.id.txtDb);
        radioDbCr.check(R.id.txtDb);
        if(am.getDbcr()==1)         radioDbCr.check(R.id.txtCr);


        Log.d("Account()","AfterDbCr, getDbCr="+am.getDbcr());

        account_type=bundle.getInt("account_type");

        txtAccountType=(Spinner) findViewById(R.id.txtJenis);


        //final String[] array_spinner=new com.talagasoft.util.constanta().daftar_jenis_akun();
        account_type_array=getResources().getStringArray(R.array.account_type_array);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, account_type_array);
        txtAccountType.setAdapter(adapter);
        txtAccountType.setSelection(--account_type);
        txtAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Log.d(parent.getClass().getName(),array_spinner[position]);
                //load_account(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        txtSubType=(Spinner) findViewById(R.id.txtSubAccType);
        String[] sub_type_array=getResources().getStringArray(R.array.account_sub_type_array);
        ArrayAdapter adapterSubType = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, sub_type_array);
        txtSubType.setAdapter(adapterSubType);
        txtSubType.setSelection(am.getSubAccType());

        txtIdxIcon=(TextView) findViewById(R.id.txtIdxIcon);

        Log.d("Account()","mode="+mode);

        if(mode==1) {
            Log.d("Account","Mode=edit");
            mode=1;
            txtAccount.setEnabled(false);
        }

        Button btnSave= (Button) findViewById(R.id.cmdSimpan);
        btnSave.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                SaveAccount();
                finish();
            }
        });
        Button btnCancel= (Button) findViewById(R.id.cmdCancel);
        btnCancel.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnDelete= (Button) findViewById(R.id.cmdDelete);
        btnDelete.setOnClickListener(new Button.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                DeleteAccount();
                finish();
            }
        });

    }


    public void DeleteAccount(){
        AccountModel am=new AccountModel();
        String account = txtAccount.getText().toString();
        am.Delete(account);
        Toast.makeText(this.getBaseContext(),"Data sudah dihapus.  Klik Refresh untuk melihat data baru.", Toast.LENGTH_SHORT).show();

    }
    public void SaveAccount(){
        AccountModel am=new AccountModel();

        String s = txtBalance.getText().toString();
        double balance = Double.valueOf(s.trim().replace(",","")).doubleValue();

        int dbCr=0;
        // get selected radio button from radioGroup
        int selectedId = radioDbCr.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton rb = (RadioButton) findViewById(selectedId);
        Log.d("Account()","RadioButtonCaption="+rb.getText()+",SelectedId="+selectedId+",txtCr.Text="+rb.getText().toString());

        if(rb.getText().toString().equals("Credit")) {
            dbCr = 1;
        }
        int account_type= (int) txtAccountType.getSelectedItemId();
        account_type++;

        int sub_type=txtSubType.getSelectedItemPosition();
        s= txtIdxIcon.getText().toString();
        int idx_icon= Integer.parseInt(s);

        am.Save(mode,txtAccount.getText().toString(),txtDescription.getText().toString(),
                account_type,balance,dbCr,sub_type,idx_icon);
        if(am.ErrMsg()=="") {
            Toast.makeText(this.getBaseContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getBaseContext(), R.string.error_save, Toast.LENGTH_SHORT).show();

        }

    }
}
