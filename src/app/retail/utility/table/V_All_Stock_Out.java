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
public enum V_All_Stock_Out {
    TABLENAME("v_all_stock_out"),
    TRANSID("TRANSID"),
    TGL("TGL"),
    TOTALITEM("TOTALITEM"),
    TTLQTY("TTLQTY"),
    TOTALHARGA("TOTALHARGA"),
    KET("KET"),
    INPUTBY("INPUTBY"),
    NAMA("NAMA");

    private final String mValue;

    private V_All_Stock_Out(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
