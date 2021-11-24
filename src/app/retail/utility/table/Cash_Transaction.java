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
public enum Cash_Transaction {
    TABLENAME("cash_transaction"),
    ID("id"),
    TRANDATE("tran_date"),
    TYPE("type"),
    TOTAL("total"),
    INPUTBY("input_by"),
    KET("ket"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Cash_Transaction(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
