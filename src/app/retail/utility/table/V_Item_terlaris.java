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
public enum V_Item_terlaris {
    TABLENAME("v_item_terlaris"),
    ITEMCODE("ITEMCODE"),
    ITEMNAME("ITEMNAME"),
    QTY("QTY"),;

    private final String mValue;

    private V_Item_terlaris(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
