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
public enum V_Shift {
    TABLENAME("v_shift"),
    KODE("KODE"),
    SHIFT("SHIFT"),
    AWAL("AWAL"),
    AKHIR("AKHIR"),;

    private final String mValue;

    private V_Shift(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
