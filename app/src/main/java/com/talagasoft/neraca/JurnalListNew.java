package com.talagasoft.neraca;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.talagasoft.model.AccountModel;
import com.talagasoft.model.JurnalModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class JurnalListNew extends AppCompatActivity implements View.OnClickListener {
    ListView lstJurnal;
    private ProgressDialog pDialog;
    TextView terima,keluar,saldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jurnal_list_new);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        terima= (TextView) findViewById(R.id.txtTerima);
        keluar= (TextView) findViewById(R.id.txtKeluar);
        saldo= (TextView) findViewById(R.id.txtSaldo);

        //summary
        LoadSummary();



        lstJurnal = (ListView) findViewById(R.id.lstJurnal2);
        ArrayList<HashMap<String, String>> mapJurnal = new ArrayList<HashMap<String, String>>();

        JurnalModel jurnalModel=new JurnalModel();
        Cursor rstJurnal=jurnalModel.GetAll();
        if(rstJurnal != null) {
            loadJurnalBackground(mapJurnal,rstJurnal);
        } else {
            Log.e(getBaseContext().getClass().getSimpleName(),"rstJurnal == null");
        }


    }

    private void LoadSummary() {
        AccountModel am=new AccountModel();
        terima.setText(NumberFormat.getNumberInstance().format((am.getPenerimaan())));
        keluar.setText(NumberFormat.getNumberInstance().format(( am.getPengeluaran())));
        saldo.setText(NumberFormat.getNumberInstance().format(( (am.getPenerimaan()-am.getPengeluaran()))));
    }

    private void loadJurnalBackground(ArrayList<HashMap<String, String>> mapJurnal, Cursor rstJurnal) {
        loadJurnalRun oRun = new loadJurnalRun();
        oRun.mapJurnal=mapJurnal;
        oRun.rstJurnal=rstJurnal;
        oRun.execute();
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        JurnalModel jurnalModel=new JurnalModel();
        ArrayList<HashMap<String, String>> mapJurnal = new ArrayList<HashMap<String, String>>();
        Cursor rstJurnal;
        switch (id) {
            case R.id.cmdRefresh:
                LoadSummary();
                rstJurnal=jurnalModel.GetAll();
                loadJurnalBackground(mapJurnal, rstJurnal);
                break;
            case R.id.cmdJurSearch:
                LoadSummary();
                TextView oSrc=(TextView) findViewById(R.id.txtJurSearch);
                rstJurnal = jurnalModel.GetAllBySearch(oSrc.getText().toString());
                loadJurnalBackground(mapJurnal, rstJurnal);
                break;
            case R.id.cmdAdd:
                Intent iJurnal=new Intent(getBaseContext(),Jurnal.class);
                startActivity(iJurnal);
                break;
            case R.id.cmdJurMenu:
                Intent iJurnalInput=new Intent(getBaseContext(),JurnalInputTemplate.class);
                startActivity(iJurnalInput);
                break;
        }
    }
    public void updateListView(ArrayList<HashMap<String, String>> mapJurnal){
        if(mapJurnal == null ) {
            Log.d(getClass().getSimpleName(),"Why mapJurnal == null");
        } else {
            ListAdapter adapter = new SimpleAdapter(
                    JurnalListNew.this, mapJurnal,
                    R.layout.content_jurnal_item,
                    new String[]{"keterangan", "tanggal", "debit", "credit", "account", "description", "kode"},
                    new int[]{R.id.keterangan, R.id.tanggal, R.id.debet, R.id.credit, R.id.account,
                            R.id.description, R.id.kode});
                if(lstJurnal==null){
                    lstJurnal= (ListView) findViewById(R.id.lstJurnal2);
                    Log.e("Jurnal()","lstJurnal==null ??");
                }
                try{
                    lstJurnal.setAdapter(adapter);
                } catch (RuntimeException e){
                    Log.e("Jurnal()","lstJurnal : " + e.toString());
                }

        }
    }
    class loadJurnalRun extends AsyncTask<String, String, String> {

        public ArrayList<HashMap<String, String>> mapJurnal;
        public Cursor rstJurnal;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(JurnalListNew.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            loadJurnalData(mapJurnal,rstJurnal);
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    updateListView(mapJurnal);
                }
            });
        }
        private String _r(String field){
            return rstJurnal.getString(rstJurnal.getColumnIndex(field));
        }

        private HashMap<String, String> Add(String tanggal, String kode, String keterangan, String debit, String credit,
                                            String tran_type, String ref1, String ref2, String account, String description){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("tanggal",tanggal);
            map.put("kode",kode);
            map.put("keterangan",keterangan);
            map.put("debit", debit);
            map.put("credit", credit);
            map.put("tran_type",tran_type);
            map.put("ref1",ref1);
            map.put("ref2",ref2);
            map.put("account",account);
            map.put("description",description);
            return map;

        }
        private void loadJurnalData(ArrayList<HashMap<String, String>> mapJurnal, Cursor rstJurnal) {
            double zDb  =   0;  String nDb;
            double zCr  =   0;  String nCr;
            double zSaldo;
            this.mapJurnal.add(Add("TANGGAL","","","DEBIT","CREDIT","","","","",""));

            this.rstJurnal.moveToFirst();
            while(!this.rstJurnal.isAfterLast()){
                nDb= NumberFormat.getNumberInstance().format(Double.parseDouble(_r("debit")));
                nCr= NumberFormat.getNumberInstance().format(Double.parseDouble(_r("credit")));
                zDb = zDb+ Double.parseDouble(_r("debit"));
                zCr = zCr + Double.parseDouble(_r("credit"));
                this.mapJurnal.add(Add(_r("tanggal"),"GL#"+_r("kode"),_r("keterangan"),nDb,nCr,
                        _r("tran_type"),_r("ref1"), _r("ref2"),_r("account"),_r("description")));
                this.rstJurnal.moveToNext();
            }
            this.rstJurnal.close();

            zSaldo=zDb-zCr;
            this.mapJurnal.add(Add("TOTAL","","",NumberFormat.getNumberInstance().format(zDb),
                    NumberFormat.getNumberInstance().format(zCr),"","","","",""));

            this.mapJurnal.add(Add("SALDO","","","",
                    NumberFormat.getNumberInstance().format(zSaldo),"","","","",""));
        }
    }
}
