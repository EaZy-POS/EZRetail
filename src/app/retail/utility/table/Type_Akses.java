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
public enum Type_Akses {
    TABLENAME("type_akses"),
    ID("id"),
    TIPEAKSES("tipe_akses"),
    AKSES("akses"),;

    private final String mValue;

    private Type_Akses(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
