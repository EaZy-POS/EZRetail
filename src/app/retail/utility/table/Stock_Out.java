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
public enum Stock_Out {
    TABLENAME("stock_out"),
    IDTRANS("id_trans"),
    TRANDT("tran_dt"),
    TTLITEM("ttl_item"),
    TTLQTY("ttl_qty"),
    TTLHARGA("ttl_harga"),
    KET("ket"),
    INPUTBY("input_by"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Stock_Out(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
