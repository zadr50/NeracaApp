package com.talagasoft.neraca;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.handstudio.android.hzgrapherlib.animation.GraphAnimation;
import com.handstudio.android.hzgrapherlib.graphview.CircleGraphView;
import com.handstudio.android.hzgrapherlib.vo.GraphNameBox;
import com.handstudio.android.hzgrapherlib.vo.circlegraph.CircleGraph;
import com.handstudio.android.hzgrapherlib.vo.circlegraph.CircleGraphVO;
import com.talagasoft.model.AccountModel;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reports extends AppCompatActivity implements   View.OnClickListener {
    private String TAG="Reports()";
    AccountModel accRek;
    String[] arRek;
    ArrayList<String> aRekAccount;
    private ViewGroup layoutGraphView;
    int idx_period=0;
    int idx_account=0;
    String account="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reports);
        initVars();
        loadActivity();

        layoutGraphView = (ViewGroup) findViewById(R.id.layoutGraphView);
        setCircleGraph();

    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.cmdPrint:
                Intent iReportList;
                iReportList = new Intent(getBaseContext(), ReportList.class);
                startActivity(iReportList);
                break;
            case R.id.cmdShare:
                break;
            case R.id.cmdRefresh:
                Spinner txtPeriode = (Spinner) findViewById(R.id.txtPeriod);
                idx_period=txtPeriode.getSelectedItemPosition();

                Spinner txtRekening=(Spinner) findViewById(R.id.txtRekening);
                idx_account=txtRekening.getSelectedItemPosition();
                account=aRekAccount.get(idx_account);
                Log.d("Account","idx_account="+idx_account+",account="+account);

                initVars();
                loadActivity();

                layoutGraphView = (ViewGroup) findViewById(R.id.layoutGraphView);
                setCircleGraph();
                break;
            case R.id.cmdList:
                break;
        }
    }
    private void initVars(){
        accRek=new AccountModel();
        arRek=accRek.AccSubTypeRekening();
        aRekAccount=accRek.account_list();
    }
    private void loadActivity(){

        final Spinner txtPeriode = (Spinner) findViewById(R.id.txtPeriod);
        String[] arPeriode = getResources().getStringArray(R.array.periode_array);
        txtPeriode.setAdapter(new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, arPeriode));
        txtPeriode.setSelection(idx_period);

        final Spinner txtRekening=(Spinner) findViewById(R.id.txtRekening);
        try{
            if(arRek!=null) {
                ArrayAdapter adapterRek = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arRek);
                txtRekening.setAdapter(adapterRek);
            }
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        txtRekening.setSelection(idx_account);

        loadSumJurnal();

        AccountModel am=new AccountModel();
        TextView terima=(TextView)findViewById(R.id.txtTerima);
        terima.setText(NumberFormat.getNumberInstance().format((am.getPenerimaan())));
        TextView keluar=(TextView)findViewById(R.id.txtKeluar);
        keluar.setText(NumberFormat.getNumberInstance().format(( am.getPengeluaran())));
        TextView saldo=(TextView)findViewById(R.id.txtSaldo);
        saldo.setText(NumberFormat.getNumberInstance().format(( (am.getPenerimaan()-am.getPengeluaran()))));


    }
    private void loadSumJurnal(){

        ListView lstJurnal= (ListView) findViewById(R.id.coa_list);

        AccountModel am=new AccountModel();
        ArrayList<HashMap<String,String>> arSum = am.SumByAccType("2",idx_period,account);
        ListAdapter adSum = new SimpleAdapter(
                this, arSum, R.layout.content_jurnal_summary,
                new String[]{"description", "periode", "sum_amount"},
                new int[]{R.id.txtDescription, R.id.txtJurPeriod, R.id.txtAmount});
//        startManagingCursor(cursor);
//        String[] columns = new String[] { "description","periode",
//                NumberFormat.getNumberInstance().format("sum_amount"),"_id" };
//        int[] to = new int[] { R.id.txtDescription,R.id.txtJurPeriod,R.id.txtAmount,R.id.txtId };
//        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, R.layout.content_jurnal_summary,
//                cursor, columns, to,0);
        lstJurnal.setAdapter(adSum);


    }
    private void setCircleGraph() {

        CircleGraphVO vo = makeLineGraphAllSetting();

        layoutGraphView.addView(new CircleGraphView(this,vo));
    }

    /**
     * make line graph using options
     * @return
     */
    private CircleGraphVO makeLineGraphAllSetting() {
        //BASIC LAYOUT SETTING
        //padding
        int paddingBottom 	= CircleGraphVO.DEFAULT_PADDING;
        int paddingTop 		= CircleGraphVO.DEFAULT_PADDING;
        int paddingLeft 	= CircleGraphVO.DEFAULT_PADDING;
        int paddingRight 	= CircleGraphVO.DEFAULT_PADDING;

        //graph margin
        int marginTop 		= CircleGraphVO.DEFAULT_MARGIN_TOP;
        int marginRight 	= CircleGraphVO.DEFAULT_MARGIN_RIGHT;

        // radius setting
        int radius = 130;

        List<CircleGraph> arrGraph 	= new ArrayList<CircleGraph>();
        AccountModel am=new AccountModel();
        ArrayList<HashMap<String, String>> cur=am.SumByAccType("2",idx_period,account,true);
        String sSumData;
        Double nSumData,nSumTotal;
        nSumTotal=am.Total();
        int nPrc;
        String[] aColor=new String[5];
        aColor[0]="#3366CC";
        aColor[1]="#DC3912";
        aColor[2]="#FF9900";
        aColor[3]="#109618";
        aColor[4]="#990099";
        int ii=0;
        for(int i=0;i<cur.size();i++) {
            HashMap<String,String> data=cur.get(i);
            sSumData=data.get("sum_amount_value");
            nSumData= Double.valueOf(sSumData);
            nPrc= (int) ((nSumData/nSumTotal)*100);
            arrGraph.add(new CircleGraph(data.get("description"), Color.parseColor(aColor[ii]), nPrc));
            ii=ii+1;
            if(ii>4)ii=0;
        }
        //arrGraph.add(new CircleGraph("ios", Color.parseColor("#DC3912"), 1));
        //arrGraph.add(new CircleGraph("tizen", Color.parseColor("#FF9900"), 1));
        //arrGraph.add(new CircleGraph("HTML", Color.parseColor("#109618"), 1));
        //arrGraph.add(new CircleGraph("C", Color.parseColor("#990099"), 3));

        CircleGraphVO vo = new CircleGraphVO(paddingBottom, paddingTop, paddingLeft, paddingRight,marginTop, marginRight,radius, arrGraph);

        // circle Line
        vo.setLineColor(Color.WHITE);

        // set text setting
        vo.setTextColor(Color.WHITE);
        vo.setTextSize(20);

        // set circle center move X ,Y
        vo.setCenterX(0);
        vo.setCenterY(0);

        //set animation
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, 2000));
        //set graph name box

        vo.setPieChart(true);

        GraphNameBox graphNameBox = new GraphNameBox();

        // nameBox
        graphNameBox.setNameboxMarginTop(25);
        graphNameBox.setNameboxMarginRight(25);

        vo.setGraphNameBox(graphNameBox);

        return vo;
    }


}
