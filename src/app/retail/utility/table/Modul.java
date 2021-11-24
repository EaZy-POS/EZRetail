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
public enum Modul {
    TABLENAME("modul"),
    ID("id"),
    MODUL("modul"),;

    private final String mValue;

    private Modul(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
