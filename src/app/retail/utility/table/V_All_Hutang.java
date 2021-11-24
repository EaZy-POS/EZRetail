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
public enum V_All_Hutang {
    TABLENAME("v_all_hutang"),
    ID("ID"),
    TGL("TGL"),
    REFNUM("REFNUM"),
    JNS("JNS"),
    PO("PO"),
    FAKTUR("FAKTUR"),
    SUPID("SUPID"),
    SUPPLIER("SUPPLIER"),
    TOTAL("TOTAL"),
    TERBAYAR("TERBAYAR"),
    SISA("SISA"),
    FLAG("FLAG"),;

    private final String mValue;

    private V_All_Hutang(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
