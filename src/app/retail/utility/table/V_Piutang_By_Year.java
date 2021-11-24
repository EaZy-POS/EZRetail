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
public enum V_Piutang_By_Year {
    TABLENAME("v_piutang_by_year"),
    TAHUN("TAHUN"),
    BLN("BLN"),
    TOTAL("TOTAL"),;

    private final String mValue;

    private V_Piutang_By_Year(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
