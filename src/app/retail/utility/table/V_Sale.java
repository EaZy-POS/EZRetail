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
public enum V_Sale {
    TABLENAME("v_sale"),
    KODETRANS("KODETRANS"),
    TRANDATE("TRANDATE"),
    IDPELANGGAN("IDPELANGGAN"),
    TOTAL("TOTAL"),
    DISC("DISC"),
    DISCAMOUNT("DISCAMOUNT"),
    GRANDTOTAL("GRANDTOTAL"),
    KET("KET"),
    IDKARYAWAN("IDKARYAWAN"),
    NAMAPELANGGAN("NAMAPELANGGAN"),
    KARYAWAN("KARYAWAN"),
    SHIFT("SHIFT"),;

    private final String mValue;

    private V_Sale(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
