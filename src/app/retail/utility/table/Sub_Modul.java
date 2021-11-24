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
public enum Sub_Modul {
    TABLENAME("sub_modul"),
    ID("id"),
    IDMODUL("id_modul"),
    SUBMODUL("sub_modul"),
    GROUPID("group_id"),;

    private final String mValue;

    private Sub_Modul(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
