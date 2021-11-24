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
public enum V_Stock_Opname {
    TABLENAME("v_stock_opname"),
    ID("ID"),
    TGL("TGL"),
    FLAG("FLAG"),
    CANCELBY("CANCELBY"),
    VERBY("VERBY"),
    NAMA("NAMA"),;

    private final String mValue;

    private V_Stock_Opname(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
