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
public enum Stock_Opname {
    TABLENAME("stock_opname"),
    TRANSID("trans_id"),
    TRANDATE("tran_date"),
    FLAG("flag"),
    INPUTBY("input_by"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),
    CANCELBY("cancel_by"),;

    private final String mValue;

    private Stock_Opname(String val) {
        this.mValue = val;
    }

    public String get() {
        return mValue;
    }
}
