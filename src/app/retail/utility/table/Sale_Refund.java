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
public enum Sale_Refund {
    TABLENAME("sale_refund"),
    KODETRANS("kode_trans"),
    TRANDATE("tran_date"),
    FAKTUR("faktur"),
    TOTAL("total"),
    IDKARYAWAN("id_karyawan"),
    SHIFT("shift"),
    SID("sid"),;

    private final String mValue;

    private Sale_Refund(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
