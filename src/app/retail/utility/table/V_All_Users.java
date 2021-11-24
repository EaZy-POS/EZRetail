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
public enum V_All_Users {
    TABLENAME("v_all_users"),
    ID("ID"),
    USERID("USERID"),
    PASSWORD("PASSWORD"),
    KODEKAR("KODEKAR"),
    NAMA("NAMA"),
    IDAKSES("IDAKSES"),
    TYPEAKSES("TYPEAKSES"),
    INITIAL("INITIAL"),;

    private final String mValue;

    private V_All_Users(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
