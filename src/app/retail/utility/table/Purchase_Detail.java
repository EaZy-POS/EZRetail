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
public enum Purchase_Detail {
    TABLENAME("purchase_detail"),
    AUTONUM("auto_num"),
    REFNUM("refnum"),
    KODEITEM("kode_item"),
    BUYPRICE("buy_price"),
    QTY("qty"),
    TOTAL("total"),
    FLAG("flag"),
    QTYTERIMA("qty_terima");

    private final String mValue;

    private Purchase_Detail(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
