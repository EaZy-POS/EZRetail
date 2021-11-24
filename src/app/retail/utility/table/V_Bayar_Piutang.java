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
public enum V_Bayar_Piutang {
    TABLENAME("v_bayar_piutang"),
    ID("ID"),
    IDPIUTANG("IDPIUTANG"),
    TGL("TGL"),
    BUKTI("BUKTI"),
    TOTAL("TOTAL"),
    INPUTDATE("INPUTDATE"),;

    private final String mValue;

    private V_Bayar_Piutang(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
