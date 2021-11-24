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
public enum V_Stock_Out_Detail {
    TABLENAME("v_stock_out_detail"),
    ID("ID"),
    TRANID("TRAN_ID"),
    KODEITEM("KODE_ITEM"),
    HARGA("HARGA"),
    QTY("QTY"),
    TOTALHARGA("TOTAL_HARGA"),
    BARCODE("BARCODE"),
    SKU("SKU"),
    ITEMNAME("ITEMNAME"),
    SATUAN("SATUAN"),;

    private final String mValue;

    private V_Stock_Out_Detail(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }

}
