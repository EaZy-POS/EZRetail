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
public enum V_Hutang_By_Supplier {
    TABLENAME("v_hutang_by_supplier"),
    NAMA("nama"),
    HUTANG("hutang"),
    TERBAYAR("terbayar"),
    SISA("sisa"),;

    private final String mValue;

    private V_Hutang_By_Supplier(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
