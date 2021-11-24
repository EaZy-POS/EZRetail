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
public enum V_Piutang {
    TABLENAME("v_piutang"),
    IDTRANS("IDTRANS"),
    FAKTUR("FAKTUR"),
    TRANDATE("TRANDATE"),
    IDPELANGGAN("IDPELANGGAN"),
    TOTAL("TOTAL"),
    TERBAYAR("TERBAYAR"),
    SISA("SISA"),
    FLAG("FLAG"),
    PELANGGAN("PELANGGAN"),;

    private final String mValue;

    private V_Piutang(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
