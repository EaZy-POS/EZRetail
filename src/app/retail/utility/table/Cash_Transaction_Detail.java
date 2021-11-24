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
public enum Cash_Transaction_Detail {
    TABLENAME("cash_transaction_detail"),
    IDTRANS("id_trans"),
    TOTAL("total"),
    FAKTUR("faktur"),
    KET("ket"),;

    private final String mValue;

    private Cash_Transaction_Detail(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
