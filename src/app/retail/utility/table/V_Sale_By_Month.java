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
public enum V_Sale_By_Month {
    TABLENAME("v_sale_by_month"),
    TAHUN("TAHUN"),
    BULAN("BULAN"),
    TGL("TGL"),
    TOTAL("TOTAL"),;

    private final String mValue;

    private V_Sale_By_Month(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
