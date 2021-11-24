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
public enum Bayar_Piutang {
    TABLENAME("bayar_piutang"),
    IDTRANS("id_trans"),
    IDPIUTANG("id_piutang"),
    TRANDATE("tran_date"),
    TOTAL("total"),
    BUKTIBAYAR("bukti_bayar"),
    INPUTDATE("input_date"),
    SID("sid"),;

    private final String mValue;

    private Bayar_Piutang(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
