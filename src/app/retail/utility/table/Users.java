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
public enum Users {
    TABLENAME("users"),
    ID("id"),
    USERID("User_id"),
    TIPEAKSES("tipe_akses"),
    INITIAL("initial"),
    KDKARYAWAN("kd_karyawan"),
    PASSWORD("password"),
    CDT("c_dt"),
    UDT("u_dt"),
    SID("sid"),
    FLAG("flag"),;

    private final String mValue;

    private Users(String val) {
        this.mValue = val;
    }
    
    public String get(){
        return mValue;
    }
}
