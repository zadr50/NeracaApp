package com.talagasoft.util;

/**
 * Created by dell on 09/11/2016.
 */
public class constanta {
    public String[] daftar_jenis_akun(){
        String[] data = new String[6];
        data[0]="Aktiva";
        data[1]="Pasiva";
        data[2]="Modal";
        data[3]="Pendapatan";
        data[4]="Pembelian";
        data[5]="Biaya";

        return data;
    }
    public String[] trans_type() {
        String[] data=new String[10];
        data[0]="Kas";
        data[1]="Penjualan";
        data[2]="Pembelian";
        data[3]="Piutang";
        data[4]="Hutang";
        data[5]="Biaya";
        data[6]="Persediaan";
        data[7]="Aktiva Tetap";
        data[8]="Koreksi";
        data[9]="Lainnya";

        return data;
    }
}
