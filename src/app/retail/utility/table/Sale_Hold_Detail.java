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
public enum Sale_Hold_Detail {
    TABLENAME("sale_hold_detail"),
    KODETRANS("kode_trans"),
    KODEITEM("kode_item"),
    HARGA("harga"),
    QTY("qty"),
    DISC("disc"),
    TOTAL("total"),;

    private final String mValue;

    private Sale_Hold_Detail(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
