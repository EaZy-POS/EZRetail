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
public enum Sale_Refund_Detail {
    TABLENAME("sale_refund_detail"),
    KODETRANS("kode_trans"),
    KODEITEM("kode_item"),
    HARGA("harga"),
    QTY("qty"),
    DISC("disc"),
    AUTOID("autoid"),
    TOTAL("total"),;

    private final String mValue;

    private Sale_Refund_Detail(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
