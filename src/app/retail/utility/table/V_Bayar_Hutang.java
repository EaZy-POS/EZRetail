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
public enum V_Bayar_Hutang {
    TABLENAME("v_bayar_hutang"),
    ID("ID"),
    IDHUTANG("IDHUTANG"),
    TGL("TGL"),
    BUKTI("BUKTI"),
    TOTAL("TOTAL"),;

    private final String mValue;

    private V_Bayar_Hutang(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
