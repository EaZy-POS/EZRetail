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
public enum Supplier {
    TABLENAME("supplier"),
    ID("id"),
    NAMA("nama"),
    ALAMAT("alamat"),
    TELPON("telpon"),
    FAX("fax"),
    EMAIL("email"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Supplier(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
