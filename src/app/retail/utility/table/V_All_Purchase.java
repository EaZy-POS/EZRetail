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
public enum V_All_Purchase {
    TABLENAME("v_all_purchase"),
    REFNUM("REFNUM"),
    TGL("TGL"),
    FAKTUR("FAKTUR"),
    SUPPLIER("SUPPLIER"),
    TOTAL("TOTAL"),
    TYPEBAYAR("TYPEBAYAR"),
    DITERIMA("DITERIMA"),
    FLAG("FLAG"),
    JNS("JNS"),
    PO("PO"),
    SUPID("SUPID"),
    ALAMAT("ALAMAT"),
    TELP("TELP"),
    FAX("FAX"),
    EMAIL("EMAIL");

    private final String mValue;

    private V_All_Purchase(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
