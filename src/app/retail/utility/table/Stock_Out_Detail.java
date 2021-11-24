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
public enum Stock_Out_Detail {
    TABLENAME("stock_out_detail_1"),
    ID("id"),
    IDTRANS("id_trans"),
    KODEITEM("kode_item"),
    HARGA("harga"),
    QTY("qty"),
    TOTALHARGA("total_harga");

    private final String mValue;

    private Stock_Out_Detail(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
