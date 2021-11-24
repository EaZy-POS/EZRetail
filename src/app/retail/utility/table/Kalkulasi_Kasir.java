/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.retail.utility.table;

/**
 *
 * @author RCS
 */
public enum Kalkulasi_Kasir {
    TABLENAME("kalkulasi_kasir"),
    REFNUM("refnum"),
    TRANDATE("tran_date"),
    IDKARYAWAN("id_karyawan"),
    IDSHIFT("id_shift"),
    TOTAL("total"),
    CASH("cash"),
    DEBET("debet"),
    CREDIT("credit"),
    PIUTANG("piutang"),;

    private final String mValue;

    private Kalkulasi_Kasir(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
