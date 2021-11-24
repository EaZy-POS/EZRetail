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
public enum Bayar_Hutang {
    TABLENAME("bayar_hutang"),
    IDTRANS("id_trans"),
    IDHUTANG("id_hutang"),
    TRANDATE("tran_date"),
    INPUTDATE("input_date"),
    BUKTIBAYAR("bukti_bayar"),
    TOTAL("total"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Bayar_Hutang(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
