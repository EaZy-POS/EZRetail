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
public enum Purchase {
    TABLENAME("purchase"),
    REFNUM("refnum"),
    TRANDATE("tran_date"),
    JNS("jns"),
    FAKTUR("faktur"),
    IDSUPPLIER("id_supplier"),
    PONUMBER("po_number"),
    TOTAL("total"),
    TYPEBAYAR("type_bayar"),
    RECBY("rec_by"),
    FLAG("flag"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),
    DID("did"),
    RECDATE("rec_date"),
    INPUTDATE("input_date");

    private final String mValue;

    private Purchase(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
