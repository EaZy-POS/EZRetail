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
public enum Sale {
    TABLENAME("sale"),
    KODETRANS("kode_trans"),
    TRANDATE("tran_date"),
    IDPELANGGAN("id_pelanggan"),
    TOTAL("total"),
    DISC("disc"),
    DISCAMMOUNT("disc_ammount"),
    GRANDTOTAL("grand_total"),
    IDKARYAWAN("id_karyawan"),
    SHIFT("shift"),
    SID("sid"),
    KET("ket"),;

    private final String mValue;

    private Sale(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
