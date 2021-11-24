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
public enum V_Piutang_By_Customer {
    TABLENAME("v_piutang_by_customer"),
    PELANGGAN("PELANGGAN"),
    IDPELANGGAN("IDPELANGGAN"),
    TOTAL("TOTAL"),
    TERBAYAR("TERBAYAR"),
    SISA("SISA"),;

    private final String mValue;

    private V_Piutang_By_Customer(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
