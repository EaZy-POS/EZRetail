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
public enum V_All_Purchase_detail {
    TABLENAME("v_all_purchase_detail"),
    PO("PO"),
    REFNUM("REFNUM"),
    IDITEM("IDITEM"),
    ITEMNAME("ITEMNAME"),
    SATUAN("SATUAN"),
    BUYPRICE("BUYPRICE"),
    QTY("QTY"),
    TOTAL("TOTAL"),
    QTYTERIMA("QTYTERIMA"),
    FLAG("FLAG"),;

    private final String mValue;

    private V_All_Purchase_detail(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
