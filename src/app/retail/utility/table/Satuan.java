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
public enum Satuan {
    TABLENAME("satuan"),
    ID("id"),
    DESCRIP("descrip"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),;

    private final String mValue;

    private Satuan(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
