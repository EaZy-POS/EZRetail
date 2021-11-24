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
public enum V_Purchase_Month {
    TABLENAME("v_purchase_month"),
    TRANDATE("tran_date"),
    TGL("tgl"),
    TOTAL("total"),;

    private final String mValue;

    private V_Purchase_Month(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
