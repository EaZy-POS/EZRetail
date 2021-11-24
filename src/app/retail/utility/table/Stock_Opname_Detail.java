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
public enum Stock_Opname_Detail {
    TABLENAME("stock_opname_detail"),
    ID("id"),
    TRANSID("trans_id"),
    KODEITEM("kode_item"),
    STOCK("stock"),
    OPNAME("opname"),
    SELISIH("selisih"),
    FLAG("flag"),;

    private final String mValue;

    private Stock_Opname_Detail(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
